package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.exceptions.WrongPasswordException;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.Measurement;
import utn.tpFinal.UDEE.repository.EnergyMeterRepository;
import utn.tpFinal.UDEE.repository.MeasurementRepository;

@Service
public class MeasurementService {

    MeasurementRepository measurementRepository;

    EnergyMeterRepository energyMeterRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, EnergyMeterRepository energyMeterRepository){
        this.measurementRepository = measurementRepository;
        this.energyMeterRepository = energyMeterRepository;
    }

    public Measurement addMeasurement(Measurement measurement, Integer serialNumberEnergyMeter, String password) throws MeterNotFoundException, WrongPasswordException {
        EnergyMeter energyMeter = energyMeterRepository.findById(serialNumberEnergyMeter)
                .orElseThrow(()->new MeterNotFoundException(this.getClass().getSimpleName(),"addMeasurement"));
        if(energyMeter.getPassword().equals(password)){
            measurement.setEnergyMeter(energyMeter);
            measurement.setBilled(false);
            return measurementRepository.save(measurement);
        }else{
            throw new WrongPasswordException(this.getClass().getSimpleName(),"addMeasurement");
        }
    }
}
