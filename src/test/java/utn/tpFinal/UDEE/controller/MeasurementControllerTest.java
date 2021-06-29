package utn.tpFinal.UDEE.controller;


import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import utn.tpFinal.UDEE.model.Dto.MeasureRequestDto;
import utn.tpFinal.UDEE.model.Measurement;
import utn.tpFinal.UDEE.service.MeasurementService;
import utn.tpFinal.UDEE.util.TestConstants;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class MeasurementControllerTest {


    MeasurementService measurementService;

    MeasurementController measurementController;

    @BeforeEach
    public void setUp(){
        measurementService = mock(MeasurementService.class);
        measurementController = new MeasurementController(measurementService);
    }

    @Test
    public void addMeasurement_Test201 () throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST","/measurements");
        request.setServerPort(8080);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Integer returnId = 1;
        MeasureRequestDto measureRequestDto = TestConstants.getMeasureRequestDto();

        when(measurementService.addMeasurement(measureRequestDto)).thenReturn(returnId);

        ResponseEntity response = measurementController.addMeasurement(measureRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("http://localhost:8080/measurements?id=1",response.getHeaders().get("Location").get(0));
    }
    @Test
    public void addMeasurement_Test500 () throws Exception {
        Integer idReturn = null;
        MeasureRequestDto measureRequestDto = TestConstants.getMeasureRequestDto();

        when(measurementService.addMeasurement(measureRequestDto)).thenReturn(idReturn);

        ResponseEntity response = measurementController.addMeasurement(measureRequestDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
