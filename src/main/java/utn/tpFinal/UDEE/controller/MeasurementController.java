package utn.tpFinal.UDEE.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utn.tpFinal.UDEE.service.MeasurementService;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService){ this.measurementService = measurementService; }
}
