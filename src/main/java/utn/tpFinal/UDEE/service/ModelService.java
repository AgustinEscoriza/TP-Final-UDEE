package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.tpFinal.UDEE.repository.MeterModelRepository;

@Service
public class ModelService {

    MeterModelRepository meterModelRepository;

    @Autowired
    public ModelService(MeterModelRepository meterModelRepository){ this.meterModelRepository = meterModelRepository;}
}
