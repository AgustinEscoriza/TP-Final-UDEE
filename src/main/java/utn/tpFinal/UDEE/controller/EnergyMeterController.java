package utn.tpFinal.UDEE.controller;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/*import org.springframework.security.access.prepost.PreAuthorize;*/
import org.springframework.web.bind.annotation.*;
import utn.tpFinal.UDEE.exceptions.*;
import utn.tpFinal.UDEE.model.Dto.EnergyMeterAddDto;
import utn.tpFinal.UDEE.model.Dto.EnergyMeterResponseDto;
import utn.tpFinal.UDEE.model.Dto.EnergyMeterPutDto;
import utn.tpFinal.UDEE.model.Dto.ResidenceResponseDto;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.service.EnergyMeterService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utn.tpFinal.UDEE.service.ResidenceService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/backoffice/meters")
public class EnergyMeterController {

    private EnergyMeterService energyMeterService;
    private ResidenceService residenceService;

    @Autowired
    public EnergyMeterController(EnergyMeterService energyMeterService,ResidenceService residenceService){
        this.energyMeterService = energyMeterService;
        this.residenceService = residenceService;
    }

    // METODOS BASE REST

    @PostMapping
    public ResponseEntity addEnergyMeter(@RequestBody EnergyMeterAddDto energyMeter) throws ModelNotFoundException, BrandNotFoundException, MeterAlreadyExistException {
        Integer newMeterId =  energyMeterService.add(energyMeter);

        if(newMeterId != null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .query("id={serialNumberEnergyMeter}")
                    .buildAndExpand(newMeterId)
                    .toUri();
            return ResponseEntity.created(location).build();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EnergyMeterResponseDto>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "5") Integer size,
                                                               @RequestParam(defaultValue = "serialNumber") String sortField1,
                                                               @RequestParam(defaultValue = "meterModel") String sortField2,
                                                               @And({
                                                            @Spec(path = "serialNumber", spec = Equal.class),
                                                            @Spec(path = "meterModel",spec =LikeIgnoreCase.class)
                                                    }) Specification<EnergyMeter> meterSpecification){
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orderList.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<EnergyMeterResponseDto> meters = energyMeterService.getAll(meterSpecification, page, size, orderList);
        if(meters.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.ok()
                    .header("X-Total-Elements", Long.toString(meters.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(meters.getTotalPages()))
                    .header("X-Actual-Page",Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(meters.getContent());
        }
    }

    @DeleteMapping("/{serialNumberEnergyMeter}")
    public ResponseEntity deleteEnergyMeterBySerialNumber(@PathVariable Integer serialNumberEnergyMeter ) throws MeterNotFoundException {
        Boolean deleted = false;
        deleted = energyMeterService.deleteEnergyMeterBySerialNumber(serialNumberEnergyMeter);


        if(!deleted){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{serialNumberEnergyMeter}")
    public ResponseEntity<EnergyMeterResponseDto> getEnergyMeterBySerialNumber(@PathVariable Integer serialNumberEnergyMeter) throws MeterNotFoundException {
        EnergyMeterResponseDto energyMeterResponseDto;
        energyMeterResponseDto = energyMeterService.getBySerialNumber(serialNumberEnergyMeter);

        return ResponseEntity.ok().body(energyMeterResponseDto);
    }

    @PutMapping("/{serialNumberEnergyMeter}")
    public ResponseEntity<EnergyMeterResponseDto> updateMeter(@PathVariable Integer serialNumberEnergyMeter, @RequestBody EnergyMeterPutDto energyMeterPutDto) throws MeterNotFoundException, ModelNotFoundException, BrandNotFoundException {
        EnergyMeterResponseDto energyMeterResponseDto = energyMeterService.updateMeter(serialNumberEnergyMeter,energyMeterPutDto);
        return ResponseEntity.ok().body(energyMeterResponseDto);
    }


    //
    @GetMapping("/{serialNumberEnergyMeter}/residence")
    public ResponseEntity<ResidenceResponseDto> getResidenceByEnergyMeterSerialNumber(@PathVariable Integer serialNumberEnergyMeter) throws MeterNotFoundException, ResidenceNotFoundException {

        ResidenceResponseDto residence = residenceService.getResidenceByMeterSerialNumber(serialNumberEnergyMeter);
        return ResponseEntity.ok(residence);
    }

}
