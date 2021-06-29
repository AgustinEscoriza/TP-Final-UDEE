package utn.tpFinal.UDEE.controller;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utn.tpFinal.UDEE.exceptions.*;
import utn.tpFinal.UDEE.model.Client;
import utn.tpFinal.UDEE.model.Dto.*;
import utn.tpFinal.UDEE.model.proyection.Top10Clients;
import utn.tpFinal.UDEE.service.ClientService;
import utn.tpFinal.UDEE.service.InvoiceService;
import utn.tpFinal.UDEE.service.MeasurementService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("")
public class ClientController { //no hay delete por idCliente porque es en cascada cuando se borre usuario

    ClientService clientService;
    MeasurementService measurementService;
    InvoiceService invoiceService;

    @Autowired
    public ClientController(ClientService clientService,MeasurementService measurementService,InvoiceService invoiceService){
        this.clientService = clientService;
        this.measurementService = measurementService;
        this.invoiceService = invoiceService;
    }

    public Boolean checkEmployeeOrClientWithCorrectId(Authentication authentication, Integer id){
        String role =  authentication.getAuthorities().stream().findFirst().get().getAuthority();

        UserDto userDto = (UserDto)authentication.getPrincipal();
        return (role.equalsIgnoreCase("BACKOFFICE") || (role.equalsIgnoreCase("CLIENT") && userDto.getId() == id) );
    }

    @PreAuthorize(value= "hasAuthority('BACKOFFICE')")
    @GetMapping("/clients")
    public ResponseEntity<List<ClientResponseDto>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "5") Integer size,
                                                          @RequestParam(defaultValue = "dni") String sortField1,
                                                          @And({
                                                       @Spec(path = "dni", spec = Equal.class)
                                               }) Specification<Client> clientSpecification){
        List<Sort.Order> orders =new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        Page<ClientResponseDto> clientPage = clientService.getAll(clientSpecification,page,size,orders);

        if(clientPage.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok()
                    .header("X-Total-Elements", Long.toString(clientPage.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(clientPage.getTotalPages()))
                    .header("X-Actual-Page", Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .body(clientPage.getContent());
        }
    }

    @PreAuthorize(value= "hasAuthority('BACKOFFICE')")
    @PostMapping("/clients")
    public ResponseEntity add(@RequestBody ClientAddDto clientAddDto) throws UserNotFoundException, ClientNotFoundException, UserAlreadyHasClientException, ClientAlreadyExistsException, ClientCannotBeAnEmployeeExcecption {  //Clientes se crean sin residencia asignada, se agregar por PUT o del otro lado

        Integer newClientDni= clientService.add(clientAddDto);

        if(newClientDni != null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .query("id={newClientDni}")
                    .buildAndExpand(newClientDni)
                    .toUri();
            return ResponseEntity.created(location).build();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/clients/{dniClient}")                //Cambio de user?? no se si deberia estar esto, claramente cambiar DNI no es.
    public ResponseEntity <ClientResponseDto> update(Authentication authentication, @PathVariable Integer dniClient, @RequestBody ClientPutDto clientPutDto) throws UserNotFoundException, ClientNotFoundException, UserAlreadyHasClientException {
        if(!checkEmployeeOrClientWithCorrectId(authentication,dniClient)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ClientResponseDto clientResponseDto = clientService.updateClient(dniClient,clientPutDto.getIdUser());
        return ResponseEntity.ok().body(clientResponseDto);
    }

    //DELETE?? No deberia porque se deberian borrar residencias/Meters/Invoices o lo que sea y el cliente dentro de la base de datos, pero vacio, a lo sumo un cascade de usuario pero no de Cliente
    //Cliente
    @GetMapping("/clients/{dniClient}/invoices/unpaid")
    public ResponseEntity<List<InvoiceResponseDto>> getUnpaidByClient(Authentication authentication, @PathVariable Integer dniClient,
                                                                      @RequestParam(defaultValue = "0") Integer page,
                                                                      @RequestParam(defaultValue = "5") Integer size,
                                                                      @RequestParam(defaultValue = "id") String sortField1,
                                                                      @RequestParam(defaultValue = "emissionDate") String sortField2) throws ClientNotFoundException {

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));
        if(!checkEmployeeOrClientWithCorrectId(authentication,dniClient)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Page<InvoiceResponseDto> invoiceDtos = invoiceService.getClientUnpaidInvoices(dniClient,page,size,orders);
        if(invoiceDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok()
                .header("X-Total-Elements", Long.toString(invoiceDtos.getTotalElements()))
                .header("X-Total-Pages", Long.toString(invoiceDtos.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(invoiceDtos.getContent());

    }

    @PostMapping("/clients/{dniClient}/consumption")
    public ResponseEntity<ConsumptionDto> getClientConsumptionBetweenDates(Authentication authentication, @PathVariable Integer dniClient,
                                                           @RequestBody DatesFromAndToDto datesFromAndToDto) throws DatesBadRequestException, ClientNotFoundException {
        if(checkEmployeeOrClientWithCorrectId(authentication,dniClient)){
            ConsumptionDto consumptionDto = invoiceService.getConsumtionBetweenDates(dniClient,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo());
            if(consumptionDto == null){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(consumptionDto);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/clients/{dniClient}/measures")
    public ResponseEntity<List<MeasureResponseDto>> getClientMeasurementsByDates(Authentication authentication, @PathVariable Integer dniClient,
                                                                             @RequestBody DatesFromAndToDto datesFromAndToDto,
                                                                             @RequestParam(defaultValue = "0") Integer page,
                                                                             @RequestParam(defaultValue = "5") Integer size,
                                                                             @RequestParam(defaultValue = "id") String sortField1,
                                                                             @RequestParam(defaultValue = "date") String sortField2) throws DatesBadRequestException, ClientNotFoundException {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        if(!checkEmployeeOrClientWithCorrectId(authentication,dniClient)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Page<MeasureResponseDto> measureResponseDtos = measurementService.getClientMeasurementsByDates(dniClient,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),page,size,orders);

        if(measureResponseDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok()
                .header("X-Total-Elements", Long.toString(measureResponseDtos.getTotalElements()))
                .header("X-Total-Pages", Long.toString(measureResponseDtos.getTotalPages()))
                .header("X-Actual-Page", Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(measureResponseDtos.getContent());

    }

    ////----------------------------- BACKOFFICE-------------------------------------------------
    @PostMapping("/backoffice/clients/top10")
    public ResponseEntity<List<Top10Clients>> getTop10(@RequestBody DatesFromAndToDto datesFromAndToDto) {
        List<Top10Clients> top10ConsumerByDates = clientService.getTop10CostumersByDate(datesFromAndToDto.getFrom(), datesFromAndToDto.getTo());
        if(top10ConsumerByDates.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(top10ConsumerByDates);
    }




}
