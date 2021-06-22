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
import utn.tpFinal.UDEE.exceptions.BrandNotFoundException;
import utn.tpFinal.UDEE.exceptions.ModelNotFoundException;
import utn.tpFinal.UDEE.model.Dto.EnergyMeterDto;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.service.EnergyMeterService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utn.tpFinal.UDEE.service.ModelService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/backoffice/model")
public class ModelController {

    ModelService modelService;

    @Autowired
    public ModelController(ModelService modelService){ this.modelService = modelService; }
}
