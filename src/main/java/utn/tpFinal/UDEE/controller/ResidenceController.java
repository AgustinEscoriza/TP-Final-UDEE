package utn.tpFinal.UDEE.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utn.tpFinal.UDEE.exceptions.ClientNotFoundException;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.exceptions.ResidenceAlreadyExists;
import utn.tpFinal.UDEE.exceptions.ResidenceNotFoundException;
import utn.tpFinal.UDEE.model.Dto.InvoiceDto;
import utn.tpFinal.UDEE.model.Dto.MeasureSenderDto;
import utn.tpFinal.UDEE.model.Dto.ResidenceAddDto;
import utn.tpFinal.UDEE.model.Invoice;
import utn.tpFinal.UDEE.model.Measurement;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.service.ResidenceService;

import javax.servlet.Servlet;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/backoffice/residence")
public class ResidenceController {

    ResidenceService residenceService;

    @Autowired
    public ResidenceController(ResidenceService residenceService){
        this.residenceService = residenceService;
    }


    @PostMapping
    public ResponseEntity addResidence(@RequestBody ResidenceAddDto residence) throws ResidenceAlreadyExists, MeterNotFoundException, ClientNotFoundException {
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

    /*@DeleteMapping("/{idResidence}")
    public ResponseEntity deleteResidence(@PathVariable Integer idResidence) throws ResidenceNotFoundException{
        residenceService.removeResidence(idResidence);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{idResidence}")
    public ResponseEntity deleteResidence(@PathVariable Integer idResidence) throws ResidenceNotFoundException{
        residenceService.updateResidence(idResidence);
        return ResponseEntity.ok().build();
    }

    //CONSULTA DE FACTURAS IMPAGAS
    @GetMapping("/{idResidence}/invoices/unpaid")
    public ResponseEntity<List<InvoiceDto>>getResidenceUnpaidInvoices(@PathVariable Integer idResidence,
                                                                   @RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "5") Integer size,
                                                                   @RequestParam(defaultValue = "id") String sortField1,
                                                                   @RequestParam(defaultValue = "emmisionDate") String sortField2) throws ResidenceNotFoundException{
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
    @GetMapping("/residence/{idResidence}/measures")
    public ResponseEntity<List<MeasureSenderDto>> getResidenceMeasuresByDates(@PathVariable Integer idResidence,
                                                                             @RequestParam(defaultValue = "0") Integer page,
                                                                             @RequestParam(defaultValue = "5") Integer size,
                                                                             @RequestParam(defaultValue = "id") String sortField1,
                                                                             @RequestParam(defaultValue = "date") String sortField2,
                                                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));


        Page<MeasureSenderDto> measures = residenceService.getResidenceMeasuresByDates(idResidence, from, to, page, size, orders);
        return ResponseEntity.ok()
                .header("X-Total-Elements", Long.toString(measures.getTotalElements()))
                .header("X-Total-Pages", Long.toString(measures.getTotalPages()))
                .header("X-Actual-Page", Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(measures.getContent());
    }*/


}
