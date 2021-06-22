package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.repository.ResidenceRepository;

import java.util.List;

@Service
public class ResidenceService {

    ResidenceRepository residenceRepository;

    @Autowired
    public ResidenceService(ResidenceRepository residenceRepository){
        this.residenceRepository = residenceRepository;
    }


}
