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
import utn.tpFinal.UDEE.exceptions.BrandNotFoundException;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.exceptions.ModelNotFoundException;
import utn.tpFinal.UDEE.exceptions.ResidenceNotFoundException;
import utn.tpFinal.UDEE.model.Dto.EnergyMeterDto;
import utn.tpFinal.UDEE.model.Dto.ResidenceDto;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.service.BrandService;
import utn.tpFinal.UDEE.service.EnergyMeterService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utn.tpFinal.UDEE.service.ModelService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/backOffice/meters")
public class EnergyMeterController {

    private EnergyMeterService energyMeterService;

    @Autowired
    public EnergyMeterController(EnergyMeterService energyMeterService){
        this.energyMeterService = energyMeterService;
    }

    // METODOS BASE REST

   /* @PreAuthorize(value = "hasAuthority('EMPLOYEE')")*/
    @PostMapping
    public ResponseEntity addEnergyMeter(@RequestBody EnergyMeter energyMeter,@RequestParam Integer idModel, @RequestParam Integer idBrand) throws ModelNotFoundException, BrandNotFoundException {
        EnergyMeter meter =  energyMeterService.add(energyMeter, idModel, idBrand);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/")
                .query("id={serialNumberEnergyMeter}")
                .buildAndExpand(meter.getSerialNumber())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<EnergyMeterDto>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "5") Integer size,
                                                    @RequestParam(defaultValue = "serialNumber") String sortField1,
                                                    @And({
                                                            @Spec(path = "serialNumber", spec = LikeIgnoreCase.class)
                                                    }) Specification<EnergyMeter> meterSpecification){
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, sortField1));

        Page<EnergyMeterDto> meters = energyMeterService.getAll(meterSpecification, page, size, orderList);
        if(meters.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.ok()
                    .header("X-Total-Elements", Long.toString(meters.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(meters.getTotalPages()))
                    .header("X-Actual-Page",Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .body(meters.getContent());
        }
    }

 /*   @PreAuthorize(value = "hasAuthority('EMPLOYEE')")*/
    @DeleteMapping("/{serialNumberEnergyMeter}")
    public ResponseEntity deleteEnergyMeterBySerialNumber(@PathVariable Integer serialNumberMeter ) throws MeterNotFoundException {
        Boolean deleted = energyMeterService.deleteEnergyMeterBySerialNumber(serialNumberMeter);

        if(!deleted){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

   /* @PreAuthorize(value = "hasAuthority('EMPLOYEE')")*/
    @GetMapping("/{serialNumberEnergyMeter}")
    public ResponseEntity<EnergyMeterDto> getEnergyMeterBySerialNumber(@PathVariable Integer serialNumberMeter) throws MeterNotFoundException {
        EnergyMeterDto energyMeterDto = energyMeterService.getBySerialNumber(serialNumberMeter);
        return ResponseEntity.ok().body(energyMeterDto);
    }

    /*@PreAuthorize(value = "hasAuthority('EMPLOYEE')")*/
    @PutMapping("/{serialNumberEnergyMeter}")
    public ResponseEntity<EnergyMeterDto> updateMeter(@PathVariable Integer serialNumberMeter, @RequestBody EnergyMeter energyMeter) throws MeterNotFoundException{
        EnergyMeterDto energyMeterDto = energyMeterService.updateMeter(serialNumberMeter,energyMeter);
        return ResponseEntity.ok().body(energyMeterDto);
    }

    /*@PreAuthorize(value = "hasAuthority('EMPLOYEE')")*/
    @GetMapping("/{serialNumberEnergyMeter}/residence")
    public ResponseEntity<ResidenceDto> getResidenceByEnergyMeterId(@PathVariable Integer serialNumberMeter) throws MeterNotFoundException, ResidenceNotFoundException {
        ResidenceDto residence = energyMeterService.getResidenceByMeterSerialNumber(serialNumberMeter);
        return ResponseEntity.ok(residence);
    }

}
