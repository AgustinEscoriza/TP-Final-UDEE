package utn.tpFinal.UDEE.controller;


import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import utn.tpFinal.UDEE.model.Dto.MeasureRequestDto;
import utn.tpFinal.UDEE.model.Measurement;
import utn.tpFinal.UDEE.service.MeasurementService;
import utn.tpFinal.UDEE.util.TestConstants;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MeasurementControllerTest {

    MeasurementService measurementService;
    MeasurementController measurementController;

    @Before
    public void setUp(){
        measurementService = mock(MeasurementService.class);
        measurementController = new MeasurementController(measurementService);
    }

    @Test
    public void addMeasurement_Test200 () throws Exception {

        Measurement m = TestConstants.getMeasure(1);
        MeasureRequestDto measureRequestDto = TestConstants.getMeasureRequestDto();

        when(measurementService.addMeasurement(measureRequestDto)).thenReturn(1);

        ResponseEntity response = measurementController.addMeasurement(measureRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    @Test
    public void addMeasurement_Test500 () throws Exception {

        Measurement m = TestConstants.getMeasure(1);
        MeasureRequestDto measureRequestDto = TestConstants.getMeasureRequestDto();

        when(measurementService.addMeasurement(measureRequestDto)).thenReturn(null);

        ResponseEntity response = measurementController.addMeasurement(measureRequestDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
