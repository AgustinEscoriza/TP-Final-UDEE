package utn.tpFinal.UDEE.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import utn.tpFinal.UDEE.exceptions.*;
import utn.tpFinal.UDEE.model.Client;
import utn.tpFinal.UDEE.model.Dto.*;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.model.User;
import utn.tpFinal.UDEE.model.proyection.Top10Clients;
import utn.tpFinal.UDEE.repository.*;
import utn.tpFinal.UDEE.util.TestConstants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class ClientServiceTest {

    private ClientRepository clientRepository;
    private UserRepository userRepository;
    private ResidenceRepository residenceRepository;
    private InvoiceRepository invoiceRepository;
    private MeasurementRepository measurementRepository;
    private ClientService clientService;

    @BeforeEach
    public void setUp(){
        this.clientRepository = mock(ClientRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.residenceRepository = mock(ResidenceRepository.class);
        this.invoiceRepository = mock(InvoiceRepository.class);
        this.measurementRepository = mock(MeasurementRepository.class);
        this.clientService = new ClientService(clientRepository,userRepository,residenceRepository,invoiceRepository,measurementRepository);
    }

    @Test
    public void getAll_TestOk(){
        Specification<Client> clientSpecification = mock(Specification.class);
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","date");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Client> clientList = TestConstants.getClientList();
        Page<Client> clientPage = new PageImpl<Client>(clientList);

        when(clientRepository.findAll(clientSpecification,pageable)).thenReturn(clientPage);

        Page<ClientResponseDto> residenceResponseDtoPage = clientService.getAll(clientSpecification,page,size,orders);

        assertFalse(residenceResponseDtoPage.getContent().isEmpty());
        assertEquals(1,residenceResponseDtoPage.getContent().get(0).getDni());
        assertEquals(2,residenceResponseDtoPage.getNumberOfElements());
    }
    @Test
    public void getAll_TestEmpty(){
        Specification<Client> clientSpecification = mock(Specification.class);
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","date");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Client> clientList = new ArrayList<>();
        Page<Client> clientPage = new PageImpl<Client>(clientList);

        when(clientRepository.findAll(clientSpecification,pageable)).thenReturn(clientPage);

        Page<ClientResponseDto> residenceResponseDtoPage = clientService.getAll(clientSpecification,page,size,orders);

        assertTrue(residenceResponseDtoPage.getContent().isEmpty());
    }

   @Test
    public void add_TestOk() throws ParseException, UserNotFoundException, ClientAlreadyExistsException, ClientNotFoundException, ClientCannotBeAnEmployeeExcecption, UserAlreadyHasClientException {
       ClientAddDto clientAddDto = TestConstants.getClientAddDto();
       User user = TestConstants.getUserClient(1);
       List<Residence> residenceList = TestConstants.getResidenceList();
       Client addedClient = TestConstants.getClient(1);

       when(clientRepository.existsById(clientAddDto.getDni())).thenReturn(false);
       when(userRepository.findById(clientAddDto.getUserId())).thenReturn(java.util.Optional.of(user));
       when(residenceRepository.findByClientDni(clientAddDto.getDni())).thenReturn(residenceList);
       when(clientRepository.save(any(Client.class))).thenReturn(addedClient);

       Integer addedDni = clientService.add(clientAddDto);

       assertEquals(clientAddDto.getDni(),addedDni);
   }
    @Test
    public void add_TestClientAlreadyExistsException() throws ParseException, UserNotFoundException, ClientNotFoundException, ClientCannotBeAnEmployeeExcecption, UserAlreadyHasClientException {
        ClientAddDto clientAddDto = TestConstants.getClientAddDto();
        User user = TestConstants.getUserClient(1);
        List<Residence> residenceList = TestConstants.getResidenceList();
        Client addedClient = TestConstants.getClient(1);

        when(clientRepository.existsById(clientAddDto.getDni())).thenReturn(true);
        when(userRepository.findById(clientAddDto.getUserId())).thenReturn(java.util.Optional.of(user));
        when(residenceRepository.findByClientDni(clientAddDto.getDni())).thenReturn(residenceList);
        when(clientRepository.save(any(Client.class))).thenReturn(addedClient);


        assertThrows(ClientAlreadyExistsException.class,()->clientService.add(clientAddDto));
    }
    @Test
    public void add_TestClientUserNotFoundException() throws ParseException, UserNotFoundException, ClientNotFoundException, ClientCannotBeAnEmployeeExcecption, UserAlreadyHasClientException {
        ClientAddDto clientAddDto = TestConstants.getClientAddDto();
        User user = TestConstants.getUserClient(1);
        List<Residence> residenceList = TestConstants.getResidenceList();
        Client addedClient = TestConstants.getClient(1);

        when(clientRepository.existsById(clientAddDto.getDni())).thenReturn(false);
        when(userRepository.findById(clientAddDto.getUserId())).thenReturn(Optional.empty());
        when(residenceRepository.findByClientDni(clientAddDto.getDni())).thenReturn(residenceList);
        when(clientRepository.save(any(Client.class))).thenReturn(addedClient);


        assertThrows(UserNotFoundException.class,()->clientService.add(clientAddDto));
    }
    @Test
    public void add_TestClientCannotBeAnEmployeeExcecption() throws ParseException, UserNotFoundException, ClientNotFoundException, ClientCannotBeAnEmployeeExcecption, UserAlreadyHasClientException {
        ClientAddDto clientAddDto = TestConstants.getClientAddDto();
        User user = TestConstants.getUserAdmin(1);
        List<Residence> residenceList = TestConstants.getResidenceList();
        Client addedClient = TestConstants.getClient(1);

        when(clientRepository.existsById(clientAddDto.getDni())).thenReturn(false);
        when(userRepository.findById(clientAddDto.getUserId())).thenReturn(Optional.of(user));
        when(residenceRepository.findByClientDni(clientAddDto.getDni())).thenReturn(residenceList);
        when(clientRepository.save(any(Client.class))).thenReturn(addedClient);


        assertThrows(ClientCannotBeAnEmployeeExcecption.class,()->clientService.add(clientAddDto));
    }
    @Test
    public void add_TestUserAlreadyHasClientException() throws ParseException, UserNotFoundException, ClientNotFoundException {
        ClientAddDto clientAddDto = TestConstants.getClientAddDto();
        User user = TestConstants.getUserAdmin(1);
        user.setClient(TestConstants.getClient(2));
        List<Residence> residenceList = TestConstants.getResidenceList();
        Client addedClient = TestConstants.getClient(1);

        when(clientRepository.existsById(clientAddDto.getDni())).thenReturn(false);
        when(userRepository.findById(clientAddDto.getUserId())).thenReturn(Optional.of(user));
        when(residenceRepository.findByClientDni(clientAddDto.getDni())).thenReturn(residenceList);
        when(clientRepository.save(any(Client.class))).thenReturn(addedClient);

        assertThrows(UserAlreadyHasClientException.class,()->clientService.add(clientAddDto));
    }

    @Test
    public void getTop10CostumersByDate_Test() throws ParseException {
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        List<Top10Clients> top10ClientsList = clientService.getTop10CostumersByDate(datesFromAndToDto.getFrom(),datesFromAndToDto.getTo());
        assertNotNull(top10ClientsList);
    }
    @Test
    public void updateClient_TestOk() throws ParseException, UserNotFoundException, ClientNotFoundException, UserAlreadyHasClientException {
        ClientPutDto clientPutDto = TestConstants.getClientPutDto();
        Integer idClient = 1;
        User user = TestConstants.getUserClient(1);
        List<Residence> residenceList = TestConstants.getResidenceList();
        Client addedClient = TestConstants.getClient(1);

        when(clientRepository.existsById(idClient)).thenReturn(true);
        when(userRepository.findById(clientPutDto.getIdUser())).thenReturn(java.util.Optional.of(user));
        when(residenceRepository.findByClientDni(idClient)).thenReturn(residenceList);
        when(clientRepository.save(any(Client.class))).thenReturn(addedClient);

        ClientResponseDto clientResponseDto = clientService.updateClient(idClient,clientPutDto.getIdUser());

        assertEquals(clientPutDto.getIdUser(),clientResponseDto.getUser().getId());
        assertEquals(idClient,clientResponseDto.getDni());
    }
    @Test
    public void updateClient_TestUserNotFoundException() throws ParseException, ClientNotFoundException, UserAlreadyHasClientException, UserNotFoundException {
        ClientPutDto clientPutDto = TestConstants.getClientPutDto();
        Integer idClient = 1;
        User user = TestConstants.getUserClient(1);
        List<Residence> residenceList = TestConstants.getResidenceList();
        Client addedClient = TestConstants.getClient(1);

        when(clientRepository.existsById(idClient)).thenReturn(true);
        when(userRepository.findById(clientPutDto.getIdUser())).thenReturn(Optional.empty());
        when(residenceRepository.findByClientDni(idClient)).thenReturn(residenceList);
        when(clientRepository.save(any(Client.class))).thenReturn(addedClient);

        assertThrows(UserNotFoundException.class,()->clientService.updateClient(idClient,clientPutDto.getIdUser()));
    }
}
