package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.tpFinal.UDEE.repository.ModelRepository;

@Service
public class ModelService {

    ModelRepository modelRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository){ this.modelRepository = modelRepository;}
}
