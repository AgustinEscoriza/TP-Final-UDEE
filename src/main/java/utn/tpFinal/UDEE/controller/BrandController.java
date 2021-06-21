package utn.tpFinal.UDEE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utn.tpFinal.UDEE.service.BrandService;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService){ this.brandService = brandService; }


}
