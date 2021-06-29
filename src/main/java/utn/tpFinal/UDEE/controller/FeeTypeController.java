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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utn.tpFinal.UDEE.exceptions.FeeTypeNotFoundException;
import utn.tpFinal.UDEE.model.Dto.FeeTypeResponseDto;
import utn.tpFinal.UDEE.model.Dto.FeeTypeRequestDto;
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
    public ResponseEntity<List<FeeTypeResponseDto>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "5") Integer size,
                                 @RequestParam(defaultValue = "detail") String sortField1,
                                 @RequestParam(defaultValue = "kwPricePerHour") String sortField2,
                                                @And({  @Spec(path = "id", spec = Equal.class),
                                                        @Spec(path = "detail", spec = LikeIgnoreCase.class),
                                                        @Spec(path = "kwPricePerHour", spec = Equal.class)}) Specification<FeeType> feeTypeSpecification){
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orderList.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<FeeTypeResponseDto> feeTypes = feeTypeService.getAllFees(feeTypeSpecification,page,size,orderList);

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
    public ResponseEntity addFeeType(@RequestBody FeeTypeRequestDto feeType){
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
    public ResponseEntity<FeeTypeResponseDto> getFeeTypeById(@PathVariable Integer idFeeType) throws FeeTypeNotFoundException {
        FeeTypeResponseDto dto = feeTypeService.getById(idFeeType);
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
    public ResponseEntity updateFeeType(@PathVariable Integer idFeeType,@RequestBody FeeTypeRequestDto feeType)throws FeeTypeNotFoundException{
        FeeTypeResponseDto updatedFeeType = feeTypeService.update(idFeeType,feeType);
        return ResponseEntity.ok().body(updatedFeeType);
    }
}
