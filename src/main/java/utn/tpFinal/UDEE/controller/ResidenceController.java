package utn.tpFinal.UDEE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.service.ResidenceService;

import java.util.List;

@RestController
@RequestMapping("/api/residence")
public class ResidenceController {

    ResidenceService residenceService;

    @Autowired
    public ResidenceController(ResidenceService residenceService){
        this.residenceService = residenceService;
    }

    @PostMapping
    public void addResidence(@RequestBody Residence residence) {
        residenceService.add(residence);
    }

    @GetMapping
    public List<Residence> getAll(){
        return residenceService.getAll();
    }

    @GetMapping("/{id}")
    public Residence getResidenceById(@PathVariable Integer id) {
        return residenceService.getByID(id);
    }
}
