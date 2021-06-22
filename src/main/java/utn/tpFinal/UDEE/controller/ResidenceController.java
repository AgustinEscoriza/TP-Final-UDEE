package utn.tpFinal.UDEE.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utn.tpFinal.UDEE.exceptions.ResidenceNotFoundException;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.service.ResidenceService;

import javax.servlet.Servlet;
import java.net.URI;
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
    public ResponseEntity addResidence(@RequestBody Residence residence){
        Residence newResidence = residenceService.addResidence(residence);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("id={idResidence}")
                .buildAndExpand(newResidence.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{idResidence}")
    public ResponseEntity deleteResidence(@PathVariable Integer idResidence) throws ResidenceNotFoundException{
        residenceService.removeResidence(idResidence);
        return ResponseEntity.ok().build();
    }

    //CONSULTA DE FACTURAS IMPAGAS
    @GetMapping("/{idResidence}/invoices/unpaid")
    public ResponseEntity<List<Invoice>>
}
