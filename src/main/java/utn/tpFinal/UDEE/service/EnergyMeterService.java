package utn.tpFinal.UDEE.service;

import io.micrometer.core.instrument.Meter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import utn.tpFinal.UDEE.exceptions.*;
import utn.tpFinal.UDEE.model.Brand;
import utn.tpFinal.UDEE.model.Dto.*;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.MeterModel;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.repository.BrandRepository;
import utn.tpFinal.UDEE.repository.MeterModelRepository;
import utn.tpFinal.UDEE.repository.EnergyMeterRepository;

import java.util.List;

@Service
public class EnergyMeterService {

    private EnergyMeterRepository energyMeterRepository;
    private BrandRepository brandRepository;
    private MeterModelRepository meterModelRepository;

    @Autowired
    public EnergyMeterService(EnergyMeterRepository energyMeterRepository, BrandRepository brandRepository, MeterModelRepository meterModelRepository) {
        this.energyMeterRepository = energyMeterRepository;
        this.brandRepository = brandRepository;
        this.meterModelRepository = meterModelRepository;
    }



    public Integer add(EnergyMeterAddDto energyMeterAddDto) throws ModelNotFoundException, BrandNotFoundException, MeterAlreadyExistException {

        if(!energyMeterRepository.existsById(energyMeterAddDto.getSerialNumber())){
            Brand brand = brandRepository.findById(energyMeterAddDto.getIdBrand()).orElseThrow(()-> new BrandNotFoundException(this.getClass().getSimpleName(),"addEnergyMeter"));
            MeterModel meterModel = meterModelRepository.findById(energyMeterAddDto.getIdModel()).orElseThrow(()-> new ModelNotFoundException(this.getClass().getSimpleName(),"addEnergyMeter"));

            EnergyMeter energyMeter = EnergyMeter.builder()
                    .serialNumber(energyMeterAddDto.getSerialNumber())
                    .password(energyMeterAddDto.getPassword())
                    .meterModel(meterModel)
                    .brand(brand)
                    .build();

            meterModel.getEnergyMeters().add(energyMeter);
            meterModelRepository.save(meterModel);

            EnergyMeter newMeter = energyMeterRepository.save(energyMeter);
            return newMeter.getSerialNumber();
        }else {
            throw new MeterAlreadyExistException(this.getClass().getSimpleName(),"add");
        }
    }

    public Page<EnergyMeterResponseDto> getAll(Specification<EnergyMeter> meterSpecification, Integer page, Integer size, List<Sort.Order>orderList) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderList));
        Page<EnergyMeter>meters = energyMeterRepository.findAll(meterSpecification,pageable);
        Page<EnergyMeterResponseDto> dtoMeters= Page.empty();
        if(!meters.isEmpty()){
            dtoMeters = meters.map(m-> EnergyMeterResponseDto.from(m));
        }

        return  dtoMeters;
    }

    public EnergyMeterResponseDto getBySerialNumber(Integer serialNumber) throws MeterNotFoundException{
        EnergyMeter energyMeter = energyMeterRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new MeterNotFoundException(this.getClass().getSimpleName(), "getBySerialNumber"));
        EnergyMeterResponseDto energyMeterResponseDto = new EnergyMeterResponseDto();

        energyMeterResponseDto = energyMeterResponseDto.from(energyMeter);

        return energyMeterResponseDto;
    }
    public Boolean deleteEnergyMeterBySerialNumber(Integer meterSerialNumber) throws MeterNotFoundException {
        Boolean deleted = false;
        if(energyMeterRepository.existsById(meterSerialNumber)) {
            energyMeterRepository.deleteById(meterSerialNumber);
            deleted = true;
        }
        else{
            throw new MeterNotFoundException(this.getClass().getSimpleName(), "deleteEnergyMeterById");
        }
        return deleted;
    };

    public EnergyMeterResponseDto updateMeter(Integer serialNumber, EnergyMeterPutDto energyMeterPutDto) throws MeterNotFoundException, BrandNotFoundException, ModelNotFoundException {
        Brand brand = brandRepository.findById(energyMeterPutDto.getIdBrand())
                .orElseThrow(()->new BrandNotFoundException(this.getClass().getSimpleName(),"updateMeter"));
        MeterModel model = meterModelRepository.findById(energyMeterPutDto.getIdModel())
                .orElseThrow(()->new ModelNotFoundException(this.getClass().getSimpleName(),"updateMeter"));
        if(energyMeterRepository.existsById(serialNumber)){
            EnergyMeter energyMeter = EnergyMeter.builder()
                    .meterModel(MeterModel.builder()
                            .id(model.getId())
                            .name(model.getName())
                            .build())
                    .brand(Brand.builder()
                            .id(brand.getId())
                            .name(brand.getName())
                            .build())
                    .password(energyMeterPutDto.getPassword())
                    .serialNumber(serialNumber)
                    .build();
            EnergyMeter updatedMeter = energyMeterRepository.save(energyMeter);

            EnergyMeterResponseDto energyMeterResponseDto = new EnergyMeterResponseDto();
            energyMeterResponseDto = energyMeterResponseDto.from(updatedMeter);

            return energyMeterResponseDto;
        }else{
            throw new MeterNotFoundException(this.getClass().getSimpleName(),"updateMeter");
        }

    };

}
