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
import utn.tpFinal.UDEE.exceptions.*;
import utn.tpFinal.UDEE.model.Dto.*;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.service.ResidenceService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/backoffice/residences")
public class ResidenceController {

    ResidenceService residenceService;

    @Autowired
    public ResidenceController(ResidenceService residenceService){
        this.residenceService = residenceService;
    }


    @PostMapping
    public ResponseEntity addResidence(@RequestBody ResidenceAddDto residence) throws ResidenceAlreadyExists, MeterNotFoundException, ClientNotFoundException, MeterAlreadyHasResidenceException, FeeTypeNotFoundException {
        Integer newResidenceId = residenceService.addResidence(residence);
        if(newResidenceId != null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .query("id={idResidence}")
                    .buildAndExpand(newResidenceId)
                    .toUri();
            return ResponseEntity.created(location).build();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ResidenceDto>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "5") Integer size,
                                                     @RequestParam(defaultValue = "id") String sortField1,
                                                     @RequestParam(defaultValue = "street") String sortField2,
                                                     @And({  @Spec(path = "id", spec = Equal.class),
                                                             @Spec(path = "street", spec = LikeIgnoreCase.class),
                                                             @Spec(path = "number", spec = LikeIgnoreCase.class)
                                                     }) Specification<Residence> residenceSpecification){
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));
        Page<ResidenceDto> residenceDto = residenceService.getAll(residenceSpecification,page,size,orders);

        if(residenceDto.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            return ResponseEntity.status(HttpStatus.OK)
                    .header("X-Total-Elements", Long.toString(residenceDto.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(residenceDto.getTotalPages()))
                    .header("X-Actual-Page", Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(residenceDto.getContent());
        }
    }

    @DeleteMapping("/{idResidence}")
    public ResponseEntity deleteResidence(@PathVariable Integer idResidence) throws ResidenceNotFoundException{
        residenceService.removeResidence(idResidence);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{idResidence}")
    public ResponseEntity<ResidenceDto> updateResidence(@PathVariable Integer idResidence,@RequestBody ResidencePutDto residencePutDto) throws ResidenceNotFoundException, ClientNotFoundException, MeterNotFoundException, FeeTypeNotFoundException {
        residenceService.updateResidence(idResidence,residencePutDto);
        return ResponseEntity.ok().build();
    }

    //CONSULTA DE FACTURAS IMPAGAS
    @GetMapping("/{idResidence}/invoices/unpaid")
    public ResponseEntity<List<InvoiceDto>>getResidenceUnpaidInvoices(@PathVariable Integer idResidence,
                                                                   @RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "5") Integer size,
                                                                   @RequestParam(defaultValue = "id") String sortField1,
                                                                   @RequestParam(defaultValue = "emissionDate") String sortField2) throws ResidenceNotFoundException{
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<InvoiceDto> invoiceDtos = residenceService.getUnpaidInvoices(idResidence,page,size,orders);
        if(invoiceDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok().header("X-Total-Elements", Long.toString(invoiceDtos.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(invoiceDtos.getTotalPages()))
                    .header("X-Actual-Page", Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(invoiceDtos.getContent());
        }
    }
    //CONSULTA MEDICIONES POR RANGO DE FECHAS
    @GetMapping("/{idResidence}/measures")
    public ResponseEntity<List<MeasureResponseDto>> getResidenceMeasuresByDates(@PathVariable Integer idResidence,
                                                                                @RequestBody DatesFromAndToDto datesFromAndToDto,
                                                                                @RequestParam(defaultValue = "0") Integer page,
                                                                                @RequestParam(defaultValue = "5") Integer size,
                                                                                @RequestParam(defaultValue = "id") String sortField1,
                                                                                @RequestParam(defaultValue = "date") String sortField2) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<MeasureResponseDto> measures = residenceService.getResidenceMeasuresBetweenDates(idResidence, datesFromAndToDto, page, size, orders);

        if(measures.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok()
                    .header("X-Total-Elements", Long.toString(measures.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(measures.getTotalPages()))
                    .header("X-Actual-Page", Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(measures.getContent());
        }
    }


}
