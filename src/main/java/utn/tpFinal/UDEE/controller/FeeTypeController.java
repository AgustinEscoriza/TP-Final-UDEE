package utn.tpFinal.UDEE.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utn.tpFinal.UDEE.exceptions.FeeTypeNotFoundException;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.model.Dto.EnergyMeterDto;
import utn.tpFinal.UDEE.model.Dto.FeeTypeDto;
import utn.tpFinal.UDEE.model.Dto.FeeTypePutDto;
import utn.tpFinal.UDEE.model.FeeType;
import utn.tpFinal.UDEE.service.FeeTypeService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/backoffice/feetypes")
public class FeeTypeController {

    FeeTypeService feeTypeService;

    @Autowired
    public FeeTypeController(FeeTypeService feeTypeService) {
        this.feeTypeService = feeTypeService;
    }
    @GetMapping
    public ResponseEntity<List<FeeType>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "5") Integer size,
                                 @RequestParam(defaultValue = "detail") String sortField1,
                                 @RequestParam(defaultValue = "kwPricePerHour") String sortField2){
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orderList.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<FeeType> feeTypes = feeTypeService.getAll(page,size,orderList);

        if(feeTypes.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok()
                    .header("X-Total-Elements", Long.toString(feeTypes.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(feeTypes.getTotalPages()))
                    .header("X-Actual-Page",Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(feeTypes.getContent());
        }
    }
    @PostMapping
    public ResponseEntity addFeeType(@RequestBody FeeType feeType){
        Integer newFeeTypeId = feeTypeService.add(feeType);

        if(newFeeTypeId != null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .query("id={newFeeTypeId}")
                    .buildAndExpand(newFeeTypeId)
                    .toUri();
            return ResponseEntity.created(location).build();
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{idFeeType}")
    public ResponseEntity<FeeTypeDto> getEnergyMeterBySerialNumber(@PathVariable Integer idFeeType) throws MeterNotFoundException, FeeTypeNotFoundException {
        FeeTypeDto dto = feeTypeService.getById(idFeeType);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{idFeeType}")
    public ResponseEntity deleteFeeType(@PathVariable Integer idFeeType) throws FeeTypeNotFoundException{
        Boolean deleted = false;
        deleted = feeTypeService.deleteFeeType(idFeeType);

        if(!deleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();

    }
    @PutMapping("/{idFeeType}")
    public ResponseEntity updateFeeType(@PathVariable Integer idFeeType,@RequestBody FeeTypePutDto feeType)throws FeeTypeNotFoundException{
        FeeTypeDto updatedFeeType = feeTypeService.update(idFeeType,feeType);
        return ResponseEntity.ok().body(updatedFeeType);
    }
}
