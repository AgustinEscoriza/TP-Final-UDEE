package utn.tpFinal.UDEE.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import utn.tpFinal.UDEE.exceptions.ClientNotFoundException;
import utn.tpFinal.UDEE.exceptions.FeeTypeNotFoundException;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.exceptions.ResidenceNotFoundException;
import utn.tpFinal.UDEE.model.Dto.*;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.Invoice;
import utn.tpFinal.UDEE.model.Measurement;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.service.InvoiceService;
import utn.tpFinal.UDEE.service.MeasurementService;
import utn.tpFinal.UDEE.service.ResidenceService;
import utn.tpFinal.UDEE.util.TestConstants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResidenceControllerTest {
    private final static Integer PAGE=0;
    private final static Integer SIZE=5;
    private final static Integer ID_RESIDENCE =1;

    private ResidenceService residenceService;
    private InvoiceService invoiceService;
    private MeasurementService measurementService;
    private ResidenceController residenceController;


    @BeforeEach
    public void setUp() {
        this.residenceService = mock(ResidenceService.class);
        this.measurementService = mock(MeasurementService.class);
        this.invoiceService = mock(InvoiceService.class);
        this.residenceController = new ResidenceController(residenceService,invoiceService,measurementService);
    }

    @Test
    public void addResidence_Test201() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST","/backoffice/residences");
        request.setServerPort(8080);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Integer returnId = 7575;
        ResidenceAddDto residenceAddDto = TestConstants.getResidenceAddDto();

        when(residenceService.addResidence(residenceAddDto)).thenReturn(returnId);

        ResponseEntity responseEntity = residenceController.addResidence(residenceAddDto);


        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals("http://localhost:8080/backoffice/residences?id=7575",responseEntity.getHeaders().get("Location").get(0)); // no pude mockear el ServletUriComponentsBuilder.fromCurrentRequest() para que devuelva http://localhost:8080/backoffice/meters, mentira resuelto(no se si esta bien igual jaja)

    }
    @Test
    public void addResidence_Test500() throws Exception{
        Integer returnId = null;
        ResidenceAddDto residenceAddDto = TestConstants.getResidenceAddDto();

        when(residenceService.addResidence(residenceAddDto)).thenReturn(returnId);

        ResponseEntity responseEntity = residenceController.addResidence(residenceAddDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode());
        //No encontre la forma de validar el null(algo similar al 200) pero bueno en teoria ya se valida solo porque me entro en el INTERNAL_SERVER_ERROR
    }
    @Test
    public void getAll_Test200(){
        Specification<Residence> specification = mock(Specification.class);
        List<ResidenceResponseDto> residenceResponseDtoList =  TestConstants.getResidenceResponseDtoList();
        List<Sort.Order> orders = TestConstants.getOrders("street","number");
        Page<ResidenceResponseDto> residenceResponseDtoPage = mock(Page.class);


        when(residenceResponseDtoPage.getTotalElements()).thenReturn(Long.valueOf(residenceResponseDtoList.size()));
        when(residenceResponseDtoPage.getContent()).thenReturn(residenceResponseDtoList);
        when(residenceResponseDtoPage.getTotalPages()).thenReturn(1);
        when(residenceService.getAll(specification,PAGE,SIZE,orders)).thenReturn(residenceResponseDtoPage);

        //then
        ResponseEntity<List<ResidenceResponseDto>> responseEntity = residenceController.getAll(PAGE,SIZE,"street","number",specification);

        //assert
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(1,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(2,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, responseEntity.getBody().get(0).getId());
        assertEquals(2, responseEntity.getBody().get(1).getId());
    }
    @Test
    public void getAll_Test204(){
        Specification<Residence> specification = mock(Specification.class);
        List<ResidenceResponseDto> residenceResponseDtoList =  new ArrayList<>();
        List<Sort.Order> orders = TestConstants.getOrders("street","number");
        Page<ResidenceResponseDto> residenceResponseDtoPage = Page.empty();


        when(residenceService.getAll(specification,PAGE,SIZE,orders)).thenReturn(residenceResponseDtoPage);

        //then
        ResponseEntity<List<ResidenceResponseDto>> responseEntity = residenceController.getAll(PAGE,SIZE,"street","number",specification);

        //assert
        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }
    @Test
    public void deleteResidence_Test200() throws ResidenceNotFoundException {

        when(residenceService.removeResidence(ID_RESIDENCE)).thenReturn(true);
        ResponseEntity responseEntity = residenceController.deleteResidence(ID_RESIDENCE);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
    @Test
    public void deleteResidence_Test404() throws ResidenceNotFoundException {

        when(residenceService.removeResidence(ID_RESIDENCE)).thenReturn(false);
        ResponseEntity responseEntity = residenceController.deleteResidence(ID_RESIDENCE);
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }
    @Test
    public void updateResidence_Test200() throws ParseException, FeeTypeNotFoundException, ClientNotFoundException, ResidenceNotFoundException, MeterNotFoundException {
        ResidenceResponseDto residenceResponseDto = TestConstants.getResidenceResponseDto();
        ResidencePutDto residencePutDto = TestConstants.getResidencePutDto();

        when(residenceService.updateResidence(ID_RESIDENCE,residencePutDto)).thenReturn(residenceResponseDto);

        ResponseEntity responseEntity = residenceController.updateResidence(ID_RESIDENCE,residencePutDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
    @Test
    public void getResidenceUnpaidInvoices_Test200() throws ResidenceNotFoundException, ParseException {
        List<InvoiceResponseDto> invoiceResponseDtoList = TestConstants.getInvoiceResponseList();
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        Page<InvoiceResponseDto> invoiceResponseDtoPage = mock(Page.class);

        when(invoiceResponseDtoPage.getTotalElements()).thenReturn(Long.valueOf(invoiceResponseDtoList.size()));
        when(invoiceResponseDtoPage.getContent()).thenReturn(invoiceResponseDtoList);
        when(invoiceResponseDtoPage.getTotalPages()).thenReturn(1);
        when(invoiceService.getUnpaidInvoices(ID_RESIDENCE,PAGE,SIZE,orders)).thenReturn(invoiceResponseDtoPage);


        ResponseEntity<List<InvoiceResponseDto>> responseEntity = residenceController.getResidenceUnpaidInvoices(ID_RESIDENCE,PAGE,SIZE,"id","emissionDate");


        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(1,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(2,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, responseEntity.getBody().get(0).getId());
        assertEquals(2, responseEntity.getBody().get(1).getId());

    }
    @Test
    public void getResidenceUnpaidInvoices_Test204() throws ResidenceNotFoundException {
        List<InvoiceResponseDto> invoiceResponseDtoList = new ArrayList<>();
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        Page<InvoiceResponseDto> invoiceResponseDtoPage = Page.empty();

        when(invoiceService.getUnpaidInvoices(ID_RESIDENCE,PAGE,SIZE,orders)).thenReturn(invoiceResponseDtoPage);

        ResponseEntity<List<InvoiceResponseDto>> responseEntity = residenceController.getResidenceUnpaidInvoices(ID_RESIDENCE,PAGE,SIZE,"id","emissionDate");

        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void getResidenceMeasuresByDates_Test200() throws ParseException {
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        List<MeasureResponseDto> measureResponseDtoList = TestConstants.getMeasureResponseDtoList();
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        Page<MeasureResponseDto> measureResponseDtoPage = mock(Page.class);

        when(measureResponseDtoPage.getTotalElements()).thenReturn(Long.valueOf(measureResponseDtoList.size()));
        when(measureResponseDtoPage.getContent()).thenReturn(measureResponseDtoList);
        when(measureResponseDtoPage.getTotalPages()).thenReturn(1);
        when(measurementService.getResidenceMeasuresBetweenDates(ID_RESIDENCE,datesFromAndToDto,PAGE,SIZE,orders)).thenReturn(measureResponseDtoPage);


        ResponseEntity<List<MeasureResponseDto>> responseEntity = residenceController.getResidenceMeasuresByDates(ID_RESIDENCE,datesFromAndToDto,PAGE,SIZE,"id","emissionDate");


        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(1,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(2,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, responseEntity.getBody().get(0).getId());
        assertEquals(2, responseEntity.getBody().get(1).getId());
    }
    @Test
    public void getResidenceMeasuresByDates_Test204() throws ParseException {
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        List<Sort.Order> orders = TestConstants.getOrders("id","emissionDate");
        Page<MeasureResponseDto> measureResponseDtoPage = Page.empty();


        when(measurementService.getResidenceMeasuresBetweenDates(ID_RESIDENCE,datesFromAndToDto,PAGE,SIZE,orders)).thenReturn(measureResponseDtoPage);


        ResponseEntity<List<MeasureResponseDto>> responseEntity = residenceController.getResidenceMeasuresByDates(ID_RESIDENCE,datesFromAndToDto,PAGE,SIZE,"id","emissionDate");


        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }
}
