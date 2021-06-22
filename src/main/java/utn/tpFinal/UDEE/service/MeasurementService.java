package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.exceptions.WrongPasswordException;
import utn.tpFinal.UDEE.model.Dto.MeasureRequestDto;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.Measurement;
import utn.tpFinal.UDEE.repository.EnergyMeterRepository;
import utn.tpFinal.UDEE.repository.MeasurementRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class MeasurementService {

    MeasurementRepository measurementRepository;

    EnergyMeterRepository energyMeterRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, EnergyMeterRepository energyMeterRepository){
        this.measurementRepository = measurementRepository;
        this.energyMeterRepository = energyMeterRepository;
    }

    public Integer addMeasurement(MeasureRequestDto dto) throws MeterNotFoundException, WrongPasswordException, ParseException {
        EnergyMeter energyMeter = energyMeterRepository.findById(Integer.parseInt(dto.getSerialNumber()))
                .orElseThrow(()->new MeterNotFoundException(this.getClass().getSimpleName(),"addMeasurement"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");


        if(energyMeter.getPassword().equals(dto.getPassword())){
            Measurement measurement = Measurement.builder()
                    .kwH(dto.getValue())
                    .energyMeter(energyMeter)
                    .residence(energyMeter.getResidence())
                    .date(dateFormat.parse(dto.getDate()))
                    .billed(false)
                    .build();
            Measurement newMeasurement = measurementRepository.save(measurement);
            return newMeasurement.getId();
        }else{
            throw new WrongPasswordException(this.getClass().getSimpleName(),"addMeasurement");
        }
    }
}
