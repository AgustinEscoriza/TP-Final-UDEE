package utn.tpFinal.UDEE.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import utn.tpFinal.UDEE.exceptions.*;
import utn.tpFinal.UDEE.model.Client;
import utn.tpFinal.UDEE.model.Dto.*;
import utn.tpFinal.UDEE.model.Measurement;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.model.proyection.Top10Clients;
import utn.tpFinal.UDEE.service.ClientService;
import utn.tpFinal.UDEE.service.InvoiceService;
import utn.tpFinal.UDEE.service.MeasurementService;
import utn.tpFinal.UDEE.service.ResidenceService;
import utn.tpFinal.UDEE.util.TestConstants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientControllerTest {
    private final static Integer PAGE=0;
    private final static Integer SIZE=5;
    private final static Integer DNI_CLIENT =1;

    private ClientService clientService;
    private MeasurementService measurementService;
    private InvoiceService invoiceService;
    private ClientController clientController;

    @BeforeEach
    public void setUp() {
        this.clientService = mock(ClientService.class);
        this.measurementService = mock(MeasurementService.class);
        this.invoiceService = mock(InvoiceService.class);
        this.clientController = new ClientController(clientService,measurementService,invoiceService);
    }

    @Test
    public void getAll_Test200(){
        Specification<Client> specification = mock(Specification.class);
        List<ClientResponseDto> clientResponseDtoList = TestConstants.getClientResponseDtoList();
        List<Sort.Order> orders = TestConstants.getOrder("dni");
        Page<ClientResponseDto> clientResponseDtopage = mock(Page.class);

        when(clientResponseDtopage.getTotalPages()).thenReturn(1);
        when(clientResponseDtopage.getContent()).thenReturn(clientResponseDtoList);
        when(clientResponseDtopage.getTotalElements()).thenReturn(Long.valueOf(clientResponseDtoList.size()));
        when(clientService.getAll(specification,PAGE,SIZE,orders)).thenReturn(clientResponseDtopage);

        ResponseEntity<List<ClientResponseDto>> responseEntity = clientController.getAll(PAGE,SIZE,"dni",specification);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(1,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(2,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, responseEntity.getBody().get(0).getDni());
        assertEquals(2, responseEntity.getBody().get(1).getDni());
    }
    @Test
    public void getAll_Test204(){
        Specification<Client> specification = mock(Specification.class);
        List<ClientResponseDto> clientResponseDtoList = new ArrayList<>();
        List<Sort.Order> orders = TestConstants.getOrder("dni");
        Page<ClientResponseDto> clientResponseDtopage = Page.empty();


        when(clientService.getAll(specification,PAGE,SIZE,orders)).thenReturn(clientResponseDtopage);

        ResponseEntity<List<ClientResponseDto>> responseEntity = clientController.getAll(PAGE,SIZE,"dni",specification);

        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void add_Test201() throws UserNotFoundException, ClientAlreadyExistsException, ClientNotFoundException, ClientCannotBeAnEmployeeExcecption, UserAlreadyHasClientException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST","/clients");
        request.setServerPort(8080);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ClientAddDto clientAddDto = TestConstants.getClientAddDto();

        when(clientService.add(clientAddDto)).thenReturn(DNI_CLIENT);

        ResponseEntity responseEntity = clientController.add(clientAddDto);

        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals("http://localhost:8080/clients?id=1",responseEntity.getHeaders().get("Location").get(0));
    }
    @Test
    public void add_Test500() throws UserNotFoundException, ClientAlreadyExistsException, ClientNotFoundException, ClientCannotBeAnEmployeeExcecption, UserAlreadyHasClientException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST","/clients");
        request.setServerPort(8080);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ClientAddDto clientAddDto = TestConstants.getClientAddDto();

        when(clientService.add(clientAddDto)).thenReturn(null);

        ResponseEntity responseEntity = clientController.add(clientAddDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode());
    }
    @Test
    public void update_Test200() throws UserNotFoundException, ClientNotFoundException, UserAlreadyHasClientException {
        Authentication authentication = mock(Authentication.class);
        ClientResponseDto clientResponseDto = TestConstants.getClientResponseDto();
        ClientPutDto clientPutDto = ClientPutDto.builder().idUser(1).build();
        UserDto userDto = TestConstants.getUserDto(1);
        List authorotiesList = TestConstants.getGrandAuthorityEmployee();

        when(authentication.getAuthorities()).thenReturn(authorotiesList);
        when(authentication.getPrincipal()).thenReturn(userDto);
        when(clientService.updateClient(DNI_CLIENT,clientPutDto.getIdUser())).thenReturn(clientResponseDto);

        ResponseEntity<ClientResponseDto> responseEntity = clientController.update(authentication,DNI_CLIENT,clientPutDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
    @Test
    public void update_Test401() throws UserNotFoundException, ClientNotFoundException, UserAlreadyHasClientException {
        Authentication authentication = mock(Authentication.class);
        ClientResponseDto clientResponseDto = TestConstants.getClientResponseDto();
        ClientPutDto clientPutDto = ClientPutDto.builder().idUser(1).build();
        UserDto userDto = TestConstants.getUserDto(1);
        List authorotiesList = TestConstants.getInvalidGrandAuthority();

        when(authentication.getAuthorities()).thenReturn(authorotiesList);
        when(authentication.getPrincipal()).thenReturn(userDto);

        ResponseEntity<ClientResponseDto> responseEntity = clientController.update(authentication,DNI_CLIENT,clientPutDto);

        assertEquals(HttpStatus.UNAUTHORIZED,responseEntity.getStatusCode());
    }
    @Test
    public void getUnpaidByClient_Test200() throws ParseException, ClientNotFoundException, ResidenceNotFoundException {
        Authentication authentication = mock(Authentication.class);
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        List<InvoiceResponseDto> invoiceResponseDto = TestConstants.getInvoiceResponseList();
        Page<InvoiceResponseDto> invoiceResponsePage = mock(Page.class);

        UserDto userDto = TestConstants.getUserDto(1);
        List authorotiesList = TestConstants.getGrandAuthorityEmployee();

        when(invoiceResponsePage.getTotalPages()).thenReturn(1);
        when(invoiceResponsePage.getContent()).thenReturn(invoiceResponseDto);
        when(invoiceResponsePage.getTotalElements()).thenReturn(Long.valueOf(invoiceResponseDto.size()));
        when(authentication.getAuthorities()).thenReturn(authorotiesList);
        when(authentication.getPrincipal()).thenReturn(userDto);
        when(invoiceService.getUnpaidInvoices(DNI_CLIENT,PAGE,SIZE,orders)).thenReturn(invoiceResponsePage);//no toma este
        when(invoiceResponsePage.isEmpty()).thenReturn(false);

        ResponseEntity<List<InvoiceResponseDto>> responseEntity = clientController.getUnpaidByClient(authentication,DNI_CLIENT,PAGE,SIZE,"id","emissionDate");

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(1,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(2,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, responseEntity.getBody().get(0).getId());
        assertEquals(2, responseEntity.getBody().get(1).getId());
    }
    @Test
    public void getUnpaidByClient_Test401() throws ClientNotFoundException {
        Authentication authentication = mock(Authentication.class);
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        UserDto userDto = TestConstants.getUserDto(1);
        List authorotiesList = TestConstants.getInvalidGrandAuthority();

        when(authentication.getAuthorities()).thenReturn(authorotiesList);
        when(authentication.getPrincipal()).thenReturn(userDto);

        ResponseEntity<List<InvoiceResponseDto>> responseEntity = clientController.getUnpaidByClient(authentication,DNI_CLIENT,PAGE,SIZE,"id","emissionDate");

        assertEquals(HttpStatus.UNAUTHORIZED,responseEntity.getStatusCode());
    }
    @Test
    public void getUnpaidByClient_Test204() throws ResidenceNotFoundException, ClientNotFoundException {
        Authentication authentication = mock(Authentication.class);
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        Page<InvoiceResponseDto> invoiceResponsePage = Page.empty();

        UserDto userDto = TestConstants.getUserDto(1);
        List authorotiesList = TestConstants.getGrandAuthorityEmployee();


        when(authentication.getAuthorities()).thenReturn(authorotiesList);
        when(authentication.getPrincipal()).thenReturn(userDto);
        when(invoiceService.getUnpaidInvoices(DNI_CLIENT,PAGE,SIZE,orders)).thenReturn(invoiceResponsePage);

        ResponseEntity<List<InvoiceResponseDto>> responseEntity = clientController.getUnpaidByClient(authentication,DNI_CLIENT,PAGE,SIZE,"id","emissionDate");

        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
        assertEquals(null,responseEntity.getBody());
    }
    @Test
    public void getClientConsumptionBetweenDates_Test200() throws ClientNotFoundException, ParseException, DatesBadRequestException {
        Authentication authentication = mock(Authentication.class);
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        ConsumptionDto consumptionDto = ConsumptionDto.builder().dni(1).invoices(TestConstants.getInvoiceResponseList()).totalConsumption(123f).build();
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        UserDto userDto = TestConstants.getUserDto(1);
        List authorotiesList = TestConstants.getGrandAuthorityEmployee();


        when(authentication.getAuthorities()).thenReturn(authorotiesList);
        when(authentication.getPrincipal()).thenReturn(userDto);
        when(invoiceService.getConsumtionBetweenDates(DNI_CLIENT,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo())).thenReturn(consumptionDto);

        ResponseEntity<ConsumptionDto> responseEntity = clientController.getClientConsumptionBetweenDates(authentication,DNI_CLIENT,datesFromAndToDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getDni());
    }
    @Test
    public void getClientConsumptionBetweenDates_Test204() throws ParseException, DatesBadRequestException, ClientNotFoundException {
        Authentication authentication = mock(Authentication.class);
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        UserDto userDto = TestConstants.getUserDto(1);
        List authorotiesList = TestConstants.getGrandAuthorityEmployee();


        when(authentication.getAuthorities()).thenReturn(authorotiesList);
        when(authentication.getPrincipal()).thenReturn(userDto);
        when(invoiceService.getConsumtionBetweenDates(DNI_CLIENT,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo())).thenReturn(null);

        ResponseEntity<ConsumptionDto> responseEntity = clientController.getClientConsumptionBetweenDates(authentication,DNI_CLIENT,datesFromAndToDto);

        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }
    @Test
    public void getClientConsumptionBetweenDates_Test401() throws DatesBadRequestException, ClientNotFoundException, ParseException {
        Authentication authentication = mock(Authentication.class);
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        UserDto userDto = TestConstants.getUserDto(1);
        List authorotiesList = TestConstants.getInvalidGrandAuthority();


        when(authentication.getAuthorities()).thenReturn(authorotiesList);
        when(authentication.getPrincipal()).thenReturn(userDto);

        ResponseEntity<ConsumptionDto> responseEntity = clientController.getClientConsumptionBetweenDates(authentication,DNI_CLIENT,datesFromAndToDto);

        assertEquals(HttpStatus.UNAUTHORIZED,responseEntity.getStatusCode());
    }

    @Test
    public void getClientMeasurementsByDates_Test200() throws ParseException, DatesBadRequestException, ClientNotFoundException {
        Authentication authentication = mock(Authentication.class);
        List<Sort.Order> orders = TestConstants.getOrders("id","date");
        List<MeasureResponseDto> measureResponseDtoList = TestConstants.getMeasureResponseDtoList();
        Page<MeasureResponseDto> measureResponseDtoPage = mock(Page.class);
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        UserDto userDto = TestConstants.getUserDto(1);
        List authorotiesList = TestConstants.getGrandAuthorityEmployee();

        when(measureResponseDtoPage.getTotalPages()).thenReturn(1);
        when(measureResponseDtoPage.getContent()).thenReturn(measureResponseDtoList);
        when(measureResponseDtoPage.getTotalElements()).thenReturn(Long.valueOf(measureResponseDtoList.size()));
        when(authentication.getAuthorities()).thenReturn(authorotiesList);
        when(authentication.getPrincipal()).thenReturn(userDto);
        when(measurementService.getClientMeasurementsByDates(DNI_CLIENT,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),PAGE,SIZE,orders)).thenReturn(measureResponseDtoPage);

        ResponseEntity<List<MeasureResponseDto>> responseEntity = clientController.getClientMeasurementsByDates(authentication,DNI_CLIENT,datesFromAndToDto,PAGE,SIZE,"id","date");

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(1,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(2,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, responseEntity.getBody().get(0).getId());
        assertEquals(2, responseEntity.getBody().get(1).getId());
    }
    @Test
    public void getClientMeasurementsByDates_Test204() throws DatesBadRequestException, ClientNotFoundException, ParseException {
        Authentication authentication = mock(Authentication.class);
        List<Sort.Order> orders = TestConstants.getOrders("id","date");
        Page<MeasureResponseDto> measureResponseDtoPage = Page.empty();
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        UserDto userDto = TestConstants.getUserDto(1);
        List authorotiesList = TestConstants.getGrandAuthorityEmployee();

        when(authentication.getAuthorities()).thenReturn(authorotiesList);
        when(authentication.getPrincipal()).thenReturn(userDto);
        when(measurementService.getClientMeasurementsByDates(DNI_CLIENT,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),PAGE,SIZE,orders)).thenReturn(measureResponseDtoPage);

        ResponseEntity<List<MeasureResponseDto>> responseEntity = clientController.getClientMeasurementsByDates(authentication,DNI_CLIENT,datesFromAndToDto,PAGE,SIZE,"id","date");

        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
    @Test
    public void getClientMeasurementsByDates_Test401() throws ParseException, DatesBadRequestException, ClientNotFoundException {
        Authentication authentication = mock(Authentication.class);
        List<Sort.Order> orders = TestConstants.getOrders("id","date");
        List<MeasureResponseDto> measureResponseDtoList = TestConstants.getMeasureResponseDtoList();
        Page<MeasureResponseDto> measureResponseDtoPage = Page.empty();
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        UserDto userDto = TestConstants.getUserDto(1);
        List authorotiesList = TestConstants.getInvalidGrandAuthority();

        when(authentication.getAuthorities()).thenReturn(authorotiesList);
        when(authentication.getPrincipal()).thenReturn(userDto);
        when(measurementService.getClientMeasurementsByDates(DNI_CLIENT,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),PAGE,SIZE,orders)).thenReturn(measureResponseDtoPage);

        ResponseEntity<List<MeasureResponseDto>> responseEntity = clientController.getClientMeasurementsByDates(authentication,DNI_CLIENT,datesFromAndToDto,PAGE,SIZE,"id","date");

        assertEquals(HttpStatus.UNAUTHORIZED,responseEntity.getStatusCode());
    }
    @Test
    public void getTop10_Test200() throws ParseException {
        List top10ClientsList = mock(List.class);
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        when(clientService.getTop10CostumersByDate(datesFromAndToDto.getFrom(),datesFromAndToDto.getTo())).thenReturn(top10ClientsList);

        ResponseEntity<List<Top10Clients>>response = clientController.getTop10(datesFromAndToDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void getTop10_Test204() throws ParseException {
        List<Top10Clients> top10ClientsList = new ArrayList<>();
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        when(clientService.getTop10CostumersByDate(datesFromAndToDto.getFrom(),datesFromAndToDto.getTo())).thenReturn(top10ClientsList);

        ResponseEntity<List<Top10Clients>>response = clientController.getTop10(datesFromAndToDto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
