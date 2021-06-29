package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import utn.tpFinal.UDEE.exceptions.ClientNotFoundException;
import utn.tpFinal.UDEE.exceptions.DatesBadRequestException;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.exceptions.WrongPasswordException;
import utn.tpFinal.UDEE.model.Client;
import utn.tpFinal.UDEE.model.Dto.DatesFromAndToDto;
import utn.tpFinal.UDEE.model.Dto.MeasureRequestDto;
import utn.tpFinal.UDEE.model.Dto.MeasureResponseDto;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.Measurement;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.repository.ClientRepository;
import utn.tpFinal.UDEE.repository.EnergyMeterRepository;
import utn.tpFinal.UDEE.repository.MeasurementRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class MeasurementService {

    MeasurementRepository measurementRepository;
    EnergyMeterRepository energyMeterRepository;
    ClientRepository clientRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, EnergyMeterRepository energyMeterRepository,ClientRepository clientRepository){
        this.measurementRepository = measurementRepository;
        this.energyMeterRepository = energyMeterRepository;
        this.clientRepository = clientRepository;
    }

    public Integer addMeasurement(MeasureRequestDto dto) throws MeterNotFoundException, WrongPasswordException, ParseException {
        EnergyMeter energyMeter = energyMeterRepository.findById(Integer.parseInt(dto.getSerialNumber()))
                .orElseThrow(()->new MeterNotFoundException(this.getClass().getSimpleName(),"addMeasurement"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");


        if(energyMeter.getPassword().equals(dto.getPassword())){
            Measurement measurement = Measurement.builder()
                    .kwH(dto.getValue())
                    .energyMeter(energyMeter)
                    .residence(energyMeter.getResidences())
                    .residence(energyMeter.getResidences())
                    .date(dateFormat.parse(dto.getDate()))
                    .billed(false)
                    .build();
            Measurement newMeasurement = measurementRepository.save(measurement);
            return newMeasurement.getId();
        }else{
            throw new WrongPasswordException(this.getClass().getSimpleName(),"addMeasurement");
        }
    }
    public Page<MeasureResponseDto> getResidenceMeasuresBetweenDates(Integer idResidence, DatesFromAndToDto requestDto, Integer page, Integer size, List<Sort.Order> orders){
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Measurement> measurements = measurementRepository.findByResidenceIdAndDateBetween(idResidence, requestDto.getFrom(),requestDto.getTo(),pageable);


        Page<MeasureResponseDto> measurementDtos = Page.empty();
        if(!measurements.isEmpty()){
            measurementDtos = measurements.map(m-> MeasureResponseDto.from(m));
        }
        return measurementDtos;
    }
    public Page<MeasureResponseDto> getClientMeasurementsByDates(Integer idClient, Date from, Date to, Integer page, Integer size, List<Sort.Order> orders) throws DatesBadRequestException, ClientNotFoundException {
        if(from.after(to)){
            throw new DatesBadRequestException(this.getClass().getSimpleName(),"getClientmeasurementsByDates");
        }
        Client c = clientRepository.findById(idClient).orElseThrow(()->new ClientNotFoundException(this.getClass().getSimpleName(),"getClientMeasurementsByDates"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        List<Integer>residencesIds = new ArrayList<Integer>();
        for(Residence r: c.getResidences())
            residencesIds.add(r.getId());
        Page<Measurement> measurements = measurementRepository.findByResidenceIdInAndDateBetween(residencesIds,from,to,pageable);

        Page<MeasureResponseDto> measureDtos = Page.empty(pageable);

        if (!measurements.isEmpty())
            measureDtos = measurements.map(m -> MeasureResponseDto.from(m));

        return measureDtos;
    }
}
