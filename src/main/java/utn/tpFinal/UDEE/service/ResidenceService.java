package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import utn.tpFinal.UDEE.exceptions.*;
import utn.tpFinal.UDEE.model.*;
import utn.tpFinal.UDEE.model.Dto.*;
import utn.tpFinal.UDEE.repository.*;

import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ResidenceService {

    ResidenceRepository residenceRepository;
    ClientRepository clientRepository;
    EnergyMeterRepository energyMeterRepository;
    MeasurementRepository measurementRepository;
    InvoiceRepository invoiceRepository;


    @Autowired
    public ResidenceService(ResidenceRepository residenceRepository, ClientRepository clientRepository, EnergyMeterRepository energyMeterRepository,MeasurementRepository measurementRepository,InvoiceRepository invoiceRepository){
        this.residenceRepository = residenceRepository;
        this.clientRepository = clientRepository;
        this.energyMeterRepository = energyMeterRepository;
        this.measurementRepository = measurementRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public Page<ResidenceDto> getAll(Specification<Residence> residenceSpecification, Integer page,Integer size,List<Sort.Order> orders){
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Residence>residences = residenceRepository.findAll(residenceSpecification,pageable);
        Page<ResidenceDto> residenceDtos = Page.empty();
        if(!residences.isEmpty()){
            residenceDtos = residences.map(r-> ResidenceDto.from(r));
        }
        return residenceDtos;
    }
    public Integer addResidence(ResidenceAddDto residenceAddDto) throws ResidenceAlreadyExists, ClientNotFoundException, MeterNotFoundException, MeterAlreadyHasResidenceException {
        Client client = clientRepository.findById(residenceAddDto.getIdClient()).orElseThrow(()-> new ClientNotFoundException(this.getClass().getSimpleName(),"addResidence"));
        EnergyMeter energyMeter = energyMeterRepository.findById(residenceAddDto.getEnergyMeterSerialNumber()).orElseThrow(()-> new MeterNotFoundException(this.getClass().getSimpleName(),"addResidence"));
        FeeType feeType = FeeType.of(residenceAddDto.getFee_value());

        if(!isNull(energyMeter.getResidence())){
            throw new MeterAlreadyHasResidenceException(this.getClass().getSimpleName(),"addResidence");
        }
        Residence residence = Residence.builder()
                .client(client)
                .energyMeter(energyMeter)
                .number(residenceAddDto.getNumber())
                .postalNumber(residenceAddDto.getPostal_number())
                .street(residenceAddDto.getStreet())
                .feeValue(residenceAddDto.getFee_value())
                .build();

        Residence newResidence = residenceRepository.save(residence);
        return newResidence.getId();
    }

    public Boolean removeResidence(Integer residenceId) throws ResidenceNotFoundException {
        Boolean deleted = false;
        if(residenceRepository.existsById(residenceId)){
            residenceRepository.deleteById(residenceId);
            if(!residenceRepository.existsById(residenceId)){
                 deleted = true;
            }
        }else{
            throw new ResidenceNotFoundException(this.getClass().getSimpleName(),"removeResidence");
        }
        return deleted;
    }

    public ResidenceDto updateResidence(Integer residenceId, ResidencePutDto residencePutDto) throws ClientNotFoundException, ResidenceNotFoundException, MeterNotFoundException {
        Residence previousResidence = residenceRepository.findById(residenceId).orElseThrow(()-> new ResidenceNotFoundException(this.getClass().getSimpleName(),"updateResidence"));
        Client client = clientRepository.findById(residencePutDto.getIdClient()).orElseThrow(()-> new ClientNotFoundException(this.getClass().getSimpleName(), "updateResidence"));
        FeeType feeType = FeeType.of(residencePutDto.getFee_value());
        EnergyMeter energyMeter = energyMeterRepository.findById(residencePutDto.getEnergyMeterSerialNumber()).orElseThrow(()-> new MeterNotFoundException(this.getClass().getSimpleName(),"updateResidence"));

        Residence residence = Residence.builder()
                .feeValue(residencePutDto.getFee_value())
                .feeType(feeType)
                .energyMeter(energyMeter)
                .client(client)
                .id(residenceId)
                .number(residencePutDto.getNumber())
                .postalNumber(residencePutDto.getPostal_number())
                .street(residencePutDto.getStreet())
                .build();

        Residence updatedResidence = residenceRepository.save(residence);
        ResidenceDto residenceDto = ResidenceDto.from(updatedResidence);

        return residenceDto;
    }

    public Page<InvoiceDto> getUnpaidInvoices(Integer residenceId, Integer page,Integer size,List<Sort.Order> orders) throws ResidenceNotFoundException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        if(!residenceRepository.existsById(residenceId)){
            throw new ResidenceNotFoundException(this.getClass().getSimpleName(),"getUnpaidInvoices");
        }

        Page<Invoice> invoices = invoiceRepository.findByPaidFalseAndResidenceId(residenceId,pageable);


        Page<InvoiceDto> invoiceDtos = Page.empty();
        if(!invoices.isEmpty()){
            invoiceDtos = invoices.map(i->InvoiceDto.from(i));
        }

        return invoiceDtos;
    }

    public Page<MeasureResponseDto> getResidenceMeasuresBetweenDates(Integer idResidence,ResidenceMeasuresByDatesDto requestDto, Integer page, Integer size,List<Sort.Order> orders){
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Measurement> measurements = measurementRepository.getAllResidenceIdAndDateBetween(idResidence, requestDto.getFrom(),requestDto.getTo(),pageable);


        Page<MeasureResponseDto> measurementDtos = Page.empty();
        if(!measurements.isEmpty()){
            measurementDtos = measurements.map(m-> MeasureResponseDto.from(m));
        }
        return measurementDtos;
    }
}
