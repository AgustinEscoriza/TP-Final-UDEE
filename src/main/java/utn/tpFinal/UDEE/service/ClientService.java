package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import utn.tpFinal.UDEE.exceptions.*;
import utn.tpFinal.UDEE.model.*;
import utn.tpFinal.UDEE.model.Dto.*;
import utn.tpFinal.UDEE.model.proyection.Top10Clients;
import utn.tpFinal.UDEE.repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClientService {

    ClientRepository clientRepository;
    UserRepository userRepository;
    ResidenceRepository residenceRepository;
    InvoiceRepository invoiceRepository;
    MeasurementRepository measurementRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository,UserRepository userRepository,ResidenceRepository residenceRepository,InvoiceRepository invoiceRepository,MeasurementRepository measurementRepository){
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.residenceRepository = residenceRepository;
        this.invoiceRepository = invoiceRepository;
        this.measurementRepository = measurementRepository;
    }

    public Page<ClientDto> getAll(Specification<Client> clientSpecification, Integer page, Integer size, List<Sort.Order>orderList) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderList));

        Page<Client> clients = clientRepository.findAll(clientSpecification,pageable);
        Page<ClientDto> clientDtos = Page.empty();
        if(!clients.isEmpty()){
            clientDtos = clients.map(c->ClientDto.from(c));
        }
        return clientDtos;
    }

    public Client getById(Integer id) {
        return clientRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public Integer add(ClientAddDto clientAddDto) throws ClientNotFoundException, UserNotFoundException, UserAlreadyHasClientException, ClientAlreadyExistsException, ClientCannotBeAnEmployeeExcecption {
        if(!clientRepository.existsById(clientAddDto.getDni())){
            User user = userRepository.findById(clientAddDto.getUserId()).orElseThrow(()->new UserNotFoundException(this.getClass().getSimpleName(),"add"));
            List<Residence> residence = residenceRepository.findByClientDni(clientAddDto.getDni());

            if(user.getClient() != null){
                throw new UserAlreadyHasClientException(this.getClass().getSimpleName(),"add");
            }
            if(user.getIsEmployee()){
                throw new ClientCannotBeAnEmployeeExcecption(this.getClass().getSimpleName(),"add");
            }
            Client client = Client.builder().dni(clientAddDto.getDni()).user(user).residences(residence).build();
            Client addedClient = clientRepository.save(client);
            return addedClient.getDni();
        }else{
            throw new ClientAlreadyExistsException(this.getClass().getSimpleName(),"add");
        }
    }

    public ClientDto updateClient(Integer idClient,Integer idUser) throws UserNotFoundException, ClientNotFoundException, UserAlreadyHasClientException {
        if(clientRepository.existsById(idClient)){
            User user = userRepository.findById(idUser).orElseThrow(()->new UserNotFoundException(this.getClass().getSimpleName(),"updateClient"));
            List<Residence> residence = residenceRepository.findByClientDni(idClient);
            if(user.getClient() != null){
                throw new UserAlreadyHasClientException(this.getClass().getSimpleName(),"updateClient");
            }
            Client client = Client.builder().dni(idClient).user(user).residences(residence).build();
            Client updatedClient = clientRepository.save(client);

            ClientDto dto = ClientDto.from(updatedClient);
            return dto;
        }else{
            throw new ClientNotFoundException(this.getClass().getSimpleName(),"updateClient");
        }
    }

    public Page<InvoiceDto> getClientUnpaidInvoices(Integer idClient,Integer page,Integer size, List<Sort.Order> orders) throws ClientNotFoundException {
        Client c = clientRepository.findById(idClient).orElseThrow(()->new ClientNotFoundException(this.getClass().getSimpleName(),"getClientUnpaidInvoices"));
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
                                                                                    //Elegir entre loop o query con array de Ids
        List<Integer> residenceIds = new ArrayList<Integer>();

        for(Residence residence : c.getResidences()){
            residenceIds.add(residence.getId());
        }

        Page<Invoice> invoices = invoiceRepository.findByPaidAndResidenceIdIn(false,residenceIds,pageable);

        Page<InvoiceDto> invoiceDtos = Page.empty();
        if(!invoices.isEmpty()){
            invoiceDtos = invoices.map(i->InvoiceDto.from(i));
        }
        return invoiceDtos;
    }

    public ConsumptionDto getConsumtionBetweenDates(Integer dniClient, Date from, Date to) throws ClientNotFoundException, DatesBadRequestException {
        if(from.after(to)){
            throw new DatesBadRequestException(this.getClass().getSimpleName(),"getConsumtionBetweenDates");
        }
        if(!clientRepository.existsById(dniClient)){
            throw new ClientNotFoundException(this.getClass().getSimpleName(),"getConsumptionBetweenDates");
        }

        List<Invoice> invoices = invoiceRepository.findByClientDniAndEmissionDateBetween(dniClient,from,to);

        ConsumptionDto consumptionDto = ConsumptionDto.builder().dni(dniClient).totalConsumption(0f).build();
        if(!invoices.isEmpty()){
            consumptionDto.setInvoices(InvoiceDto.from(invoices));
        }
        for(Invoice i: invoices){
            consumptionDto.setTotalConsumption((consumptionDto.getTotalConsumption()+ i.getTotalConsumption()));
        }

        return consumptionDto;
    }

    public Page<MeasureResponseDto> getClientMeasurementsByDates(Integer idClient,Date from,Date to,Integer page, Integer size, List<Sort.Order> orders) throws DatesBadRequestException, ClientNotFoundException {
        if(from.after(to)){
            throw new DatesBadRequestException(this.getClass().getSimpleName(),"getClientmeasurementsByDates");
        }
        Client c = clientRepository.findById(idClient).orElseThrow(()->new ClientNotFoundException(this.getClass().getSimpleName(),"getClientMeasurementsByDates"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        List<Integer>residencesIds = new ArrayList<Integer>();
        for(Residence r: c.getResidences())
            residencesIds.add(r.getId());
        Page<Measurement> measurements = measurementRepository.findByResidenceIdInAndDateBetween(residencesIds,from,to,pageable);

        Page<MeasureResponseDto> measureDtos = Page.empty(pageable);

        if (!measurements.isEmpty())
            measureDtos = measurements.map(m -> MeasureResponseDto.from(m));

        return measureDtos;
    }

    public List<Top10Clients> getTop10CostumersByDate(Date from, Date to){
         return clientRepository.getTop10Clients(from,to);
    }
}
