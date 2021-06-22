package utn.tpFinal.UDEE.controller;


import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.exceptions.WrongPasswordException;
import utn.tpFinal.UDEE.model.Dto.MeasureReceiverDto;
import utn.tpFinal.UDEE.model.Measurement;
import utn.tpFinal.UDEE.service.MeasurementService;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService){ this.measurementService = measurementService; }

    @PostMapping
    public ResponseEntity addMeasurement(@RequestBody MeasureReceiverDto dto) throws ParseException, MeterNotFoundException, WrongPasswordException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        Measurement measurement = Measurement.builder()
                .dateTime(dateFormat.parse(dto.getDate()))
                .kwH(dto.getValue())
                .build();
        Integer newMeasureId = measurementService.addMeasurement(measurement,Integer.parseInt(dto.getSerialNumber()),dto.getPassword());

        if(newMeasureId != null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("")
                    .query("id={idMeasurement}")
                    .buildAndExpand(measurement.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
