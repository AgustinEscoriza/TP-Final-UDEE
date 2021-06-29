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
import utn.tpFinal.UDEE.exceptions.BrandNotFoundException;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.exceptions.ModelNotFoundException;
import utn.tpFinal.UDEE.exceptions.ResidenceNotFoundException;
import utn.tpFinal.UDEE.model.Dto.*;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.service.EnergyMeterService;
import utn.tpFinal.UDEE.service.ResidenceService;
import utn.tpFinal.UDEE.util.TestConstants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnergyMeterControllerTest {
    private final static Integer PAGE=0;
    private final static Integer SIZE=5;
    private final static Integer SERIAL_NUMBER =1;

    private EnergyMeterService energyMeterService;
    private ResidenceService residenceService;
    private EnergyMeterController energyMeterController;

    @BeforeEach
    public void setUp(){
        energyMeterService = mock(EnergyMeterService.class);
        residenceService = mock(ResidenceService.class);
        energyMeterController = new EnergyMeterController(energyMeterService,residenceService);
    }

    @Test
    public void addEnergyMeter_Test201() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST","/backoffice/meters");
        request.setServerPort(8080);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Integer returnId = 7575;
        EnergyMeterAddDto energyMeterAddDto = TestConstants.getEnergyMeterAddDto(returnId);

        when(energyMeterService.add(energyMeterAddDto)).thenReturn(returnId);

        ResponseEntity responseEntity = energyMeterController.addEnergyMeter(energyMeterAddDto);

        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals("http://localhost:8080/backoffice/meters?id=7575",responseEntity.getHeaders().get("Location").get(0)); // no pude mockear el ServletUriComponentsBuilder.fromCurrentRequest() para que devuelva http://localhost:8080/backoffice/meters

    }
    @Test
    public void addEnergyMeter_Test500() throws Exception {

        Integer returnId = null;
        EnergyMeterAddDto energyMeterAddDto = TestConstants.getEnergyMeterAddDto(1);

        when(energyMeterService.add(energyMeterAddDto)).thenReturn(returnId);

        ResponseEntity responseEntity = energyMeterController.addEnergyMeter(energyMeterAddDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode());

    }
    @Test
    public void getAll_Test200() throws Exception {
        //Given
        Specification<EnergyMeter> specification = mock(Specification.class);
        List<EnergyMeterResponseDto> energyMeterResponseDtoList = TestConstants.getEnergyMeterResponseDtoList();
        List<Sort.Order> orders = TestConstants.getOrders("serialNumber","meterModel");
        Page<EnergyMeterResponseDto> energyMeterResponseDtoPage = mock(Page.class);

        when(energyMeterResponseDtoPage.getTotalElements()).thenReturn(Long.valueOf(energyMeterResponseDtoList.size()));
        when(energyMeterResponseDtoPage.getTotalPages()).thenReturn(1);
        when(energyMeterResponseDtoPage.getContent()).thenReturn(energyMeterResponseDtoList);
        when(energyMeterService.getAll(specification,PAGE,SIZE,orders)).thenReturn(energyMeterResponseDtoPage);

        // Then
        ResponseEntity<List<EnergyMeterResponseDto>> responseEntity = energyMeterController.getAll(PAGE,SIZE,"serialNumber","meterModel",specification);
        //Assert
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(1,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(2,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, responseEntity.getBody().get(0).getSerialNumber());
        assertEquals(2, responseEntity.getBody().get(1).getSerialNumber());
    }
    @Test
    public void getAll_Test204() throws Exception {
        //Given

        Specification<EnergyMeter> specification = mock(Specification.class);
        List<EnergyMeterResponseDto> energyMeterResponseDtoList =  new ArrayList<>();
        List<Sort.Order> orders = TestConstants.getOrders("serialNumber","meterModel");
        Page<EnergyMeterResponseDto> energyMeterResponseDtoPage = Page.empty();


        when(energyMeterService.getAll(specification,PAGE,SIZE,orders)).thenReturn(energyMeterResponseDtoPage);

        // Then
        ResponseEntity<List<EnergyMeterResponseDto>> responseEntity = energyMeterController.getAll(PAGE,SIZE,"serialNumber","meterModel",specification);
        //Assert
        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
        /*assertEquals(null,responseEntity.getHeaders());*/
        assertEquals(null,responseEntity.getBody());
    }
    @Test
    public void deleteEnergyMeterBySerialNumber_Test404() throws MeterNotFoundException {

        when(energyMeterService.deleteEnergyMeterBySerialNumber(SERIAL_NUMBER)).thenReturn(false);

        ResponseEntity responseEntity = energyMeterController.deleteEnergyMeterBySerialNumber(SERIAL_NUMBER);
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }
    @Test
    public void deleteEnergyMeterBySerialNumber_Test200() throws MeterNotFoundException {

        when(energyMeterService.deleteEnergyMeterBySerialNumber(SERIAL_NUMBER)).thenReturn(true);

        ResponseEntity responseEntity = energyMeterController.deleteEnergyMeterBySerialNumber(SERIAL_NUMBER);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void getEnergyMeterBySerialNumber_Test200() throws MeterNotFoundException {
        Integer serialNumber = 1;
        EnergyMeterResponseDto energyMeterResponseDto = TestConstants.getEnergyMeterResponseDto(serialNumber);

        when(energyMeterService.getBySerialNumber(serialNumber)).thenReturn(energyMeterResponseDto);

        ResponseEntity responseEntity = energyMeterController.getEnergyMeterBySerialNumber(serialNumber);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
    @Test
    public void updateMeter_Test200() throws BrandNotFoundException, MeterNotFoundException, ModelNotFoundException {
        Integer serialNumber = 1;
        EnergyMeterResponseDto energyMeterResponseDto = TestConstants.getEnergyMeterResponseDto(serialNumber);
        EnergyMeterPutDto energyMeterPutDto = TestConstants.getEnergyMeterPutDto();

        when(energyMeterService.updateMeter(serialNumber,energyMeterPutDto)).thenReturn(energyMeterResponseDto);

        ResponseEntity responseEntity = energyMeterController.updateMeter(serialNumber,energyMeterPutDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
    @Test
    public void getResidenceByEnergyMeterSerialNumber() throws MeterNotFoundException, ResidenceNotFoundException, ParseException {
        Integer serialNumber = 1;
        ResidenceResponseDto residenceResponseDto = TestConstants.getResidenceResponseDto();

        when(residenceService.getResidenceByMeterSerialNumber(serialNumber)).thenReturn(residenceResponseDto);

        ResponseEntity responseEntity = energyMeterController.getResidenceByEnergyMeterSerialNumber(serialNumber);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

}
