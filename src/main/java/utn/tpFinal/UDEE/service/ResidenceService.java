package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import utn.tpFinal.UDEE.exceptions.*;
import utn.tpFinal.UDEE.model.*;
import utn.tpFinal.UDEE.model.Dto.ResidenceAddDto;
import utn.tpFinal.UDEE.repository.ClientRepository;
import utn.tpFinal.UDEE.repository.EnergyMeterRepository;
import utn.tpFinal.UDEE.repository.ResidenceRepository;

import java.util.List;

@Service
public class ResidenceService {

    ResidenceRepository residenceRepository;
    ClientRepository clientRepository;
    EnergyMeterRepository energyMeterRepository;


    @Autowired
    public ResidenceService(ResidenceRepository residenceRepository, ClientRepository clientRepository, EnergyMeterRepository energyMeterRepository){
        this.residenceRepository = residenceRepository;
        this.clientRepository = clientRepository;
        this.energyMeterRepository = energyMeterRepository;
    }

    public Integer addResidence(ResidenceAddDto residenceAddDto) throws ResidenceAlreadyExists, ClientNotFoundException, MeterNotFoundException {
        if(!residenceRepository.existsById(residenceAddDto.getId())){
            Client client = clientRepository.findById(residenceAddDto.getIdClient()).orElseThrow(()-> new ClientNotFoundException(this.getClass().getSimpleName(),"addResidence"));
            EnergyMeter energyMeter = energyMeterRepository.findById(residenceAddDto.getEnergyMeterSerialNumber()).orElseThrow(()-> new MeterNotFoundException(this.getClass().getSimpleName(),"addResidence"));
            FeeType feeType = FeeType.of(residenceAddDto.getFee_value());

            Residence residence = Residence.builder()
                    .client(client)
                    .energyMeter(energyMeter)
                    .feeType(feeType)
                    .number(residenceAddDto.getNumber())
                    .postalNumber(residenceAddDto.getPostal_number())
                    .street(residenceAddDto.getStreet())
                    .id(residenceAddDto.getId())
                    .build();

            energyMeter.setResidence(residence);
            energyMeterRepository.save(energyMeter);

            client.getResidence().add(residence);
            clientRepository.save(client);

            Residence newResidence = residenceRepository.save(residence);
            return newResidence.getId();
        }else{
            throw new ResidenceAlreadyExists(this.getClass().getSimpleName(),"addResidence");
        }
    }
}
