package utn.tpFinal.UDEE.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import utn.tpFinal.UDEE.exceptions.ClientNotFoundException;
import utn.tpFinal.UDEE.exceptions.DatesBadRequestException;
import utn.tpFinal.UDEE.exceptions.ResidenceNotFoundException;
import utn.tpFinal.UDEE.model.Client;
import utn.tpFinal.UDEE.model.Dto.ConsumptionDto;
import utn.tpFinal.UDEE.model.Dto.DatesFromAndToDto;
import utn.tpFinal.UDEE.model.Dto.InvoiceResponseDto;
import utn.tpFinal.UDEE.model.Invoice;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.repository.ClientRepository;
import utn.tpFinal.UDEE.repository.InvoiceRepository;
import utn.tpFinal.UDEE.repository.ResidenceRepository;
import utn.tpFinal.UDEE.util.TestConstants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class InvoiceServiceTest {

    private InvoiceRepository invoiceRepository;
    private ResidenceRepository residenceRepository;
    private ClientRepository clientRepository;
    private InvoiceService invoiceService;

    @BeforeEach
    public void setUp(){
        this.invoiceRepository = mock(InvoiceRepository.class);
        this.residenceRepository = mock(ResidenceRepository.class);
        this.clientRepository = mock(ClientRepository.class);
        this.invoiceService = new InvoiceService(invoiceRepository,residenceRepository,clientRepository);
    }
    @Test
    public void getUnpaidInvoices_TestOk() throws ParseException, ResidenceNotFoundException {
        Integer residenceId = 1;
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Invoice> invoices = TestConstants.getInvoiceList();
        Page<Invoice> invoicePage =new PageImpl<>(invoices);

        when(residenceRepository.existsById(residenceId)).thenReturn(true);
        when(invoiceRepository.findByPaidAndResidenceId(false,residenceId,pageable)).thenReturn(invoicePage);

        Page<InvoiceResponseDto> invoiceResponseDtoPage = invoiceService.getUnpaidInvoices(residenceId,page,size,orders);

        assertFalse(invoiceResponseDtoPage.getContent().isEmpty());
        assertEquals(1,invoiceResponseDtoPage.getContent().get(0).getId());
        assertEquals(2,invoiceResponseDtoPage.getNumberOfElements());
    }
    @Test
    public void getUnpaidInvoices_TestEmpty() throws ParseException, ResidenceNotFoundException {
        Integer residenceId = 1;
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Invoice> invoices = new ArrayList<>();
        Page<Invoice> invoicePage =new PageImpl<>(invoices);

        when(residenceRepository.existsById(residenceId)).thenReturn(true);
        when(invoiceRepository.findByPaidAndResidenceId(false,residenceId,pageable)).thenReturn(invoicePage);

        Page<InvoiceResponseDto> invoiceResponseDtoPage = invoiceService.getUnpaidInvoices(residenceId,page,size,orders);

        assertTrue(invoiceResponseDtoPage.getContent().isEmpty());
    }
    @Test
    public void getUnpaidInvoices_TestResidenceNotFoundException() throws ParseException, ResidenceNotFoundException {
        Integer residenceId = 1;
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Invoice> invoices = TestConstants.getInvoiceList();
        Page<Invoice> invoicePage =new PageImpl<>(invoices);

        when(residenceRepository.existsById(residenceId)).thenReturn(false);
        when(invoiceRepository.findByPaidAndResidenceId(false,residenceId,pageable)).thenReturn(invoicePage);

        assertThrows(ResidenceNotFoundException.class,()->invoiceService.getUnpaidInvoices(residenceId,page,size,orders));
    }
    @Test
    public void getClientUnpaidInvoices_TestOk() throws ParseException, ResidenceNotFoundException {
        Integer idClient= 1;
        Integer residenceId= 1;
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Invoice> invoices = TestConstants.getInvoiceList();
        Page<Invoice> invoicePage =new PageImpl<>(invoices);
        Client c = TestConstants.getClient(1);
        List<Residence> residenceList = TestConstants.getResidenceList();
        c.setResidences(residenceList);
        List<Integer> residenceIds = new ArrayList<>();
        residenceIds.add(1);
        residenceIds.add(2);

        when(residenceRepository.existsById(residenceId)).thenReturn(true);
        when(clientRepository.findById(idClient)).thenReturn(Optional.of(c));
        when(invoiceRepository.findByPaidAndResidenceId(false,residenceId,pageable)).thenReturn(invoicePage);

        Page<InvoiceResponseDto> invoiceResponseDtoPage = invoiceService.getUnpaidInvoices(idClient,page,size,orders);

        assertFalse(invoiceResponseDtoPage.isEmpty());
    }
    @Test
    public void getConsumtionBetweenDates_TestOk() throws ParseException, DatesBadRequestException, ClientNotFoundException {
        Integer dniClient =1;
        Integer idClient= 1;
        Integer residenceId= 1;
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Invoice>invoiceList = TestConstants.getInvoiceList();
        List<ConsumptionDto> consumptionDtoList = TestConstants.getConsumpionDtoList();

        Client c = TestConstants.getClient(1);

        when(clientRepository.existsById(dniClient)).thenReturn(true);
        when(invoiceRepository.findByClientDniAndEmissionDateBetween(dniClient,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo())).thenReturn(invoiceList);
        ConsumptionDto consumptionDto = invoiceService.getConsumtionBetweenDates(dniClient,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo());

        assertEquals(dniClient,consumptionDto.getDni());

    }
}
