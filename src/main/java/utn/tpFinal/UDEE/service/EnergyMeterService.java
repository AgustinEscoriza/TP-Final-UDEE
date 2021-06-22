package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class EnergyMeterService {

    @Autowired
    private EnergyMeterRepository energyMeterRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private MeterModelRepository meterModelRepository;

    public EnergyMeter getEnergyMeterBySerialNumber(Integer serial) throws MeterNotFoundException {
        return energyMeterRepository.findBySerialNumber(serial)
                .orElseThrow(() -> new MeterNotFoundException(this.getClass().getSimpleName(), "getEnergyMeterBySerialNumber"));
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

    public Page<EnergyMeterDto> getAll(Specification<EnergyMeter> meterSpecification, Integer page, Integer size, List<Sort.Order>orderList) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderList));
        Page<EnergyMeter>meters = energyMeterRepository.findAll(meterSpecification,pageable);
        Page<EnergyMeterDto> dtoMeters= Page.empty();
        List<EnergyMeterDto> listDto = null;
        if(!meters.isEmpty()){
            dtoMeters = meters.map(m->EnergyMeterDto.from(m));
        }

        return  dtoMeters;
    }

    public EnergyMeterDto getBySerialNumber(Integer serialNumber) throws MeterNotFoundException{
        EnergyMeter energyMeter = energyMeterRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new MeterNotFoundException(this.getClass().getSimpleName(), "getBySerialNumber"));
        EnergyMeterDto energyMeterDto = new EnergyMeterDto();

        energyMeterDto = energyMeterDto.from(energyMeter);

        return energyMeterDto;
    }
    public Boolean deleteEnergyMeterBySerialNumber(Integer meterSerialNumber) throws MeterNotFoundException {
        Boolean deleted = false;
        if(!energyMeterRepository.existsById(meterSerialNumber))
            throw new MeterNotFoundException(this.getClass().getSimpleName(), "deleteEnergyMeterById");

        energyMeterRepository.deleteById(meterSerialNumber);
        if(!energyMeterRepository.existsById(meterSerialNumber)){
            deleted = true;
        }
        return deleted;
    };

    public EnergyMeterDto updateMeter(Integer serialNumber, EnergyMeterPutDto energyMeterPutDto) throws MeterNotFoundException, BrandNotFoundException, ModelNotFoundException {
        EnergyMeter previousEnergyMeter = energyMeterRepository.findById(serialNumber)
                .orElseThrow(()->new MeterNotFoundException(this.getClass().getSimpleName(),"updateMeter"));
        Brand brand = brandRepository.findById(energyMeterPutDto.getIdBrand())
                .orElseThrow(()->new BrandNotFoundException(this.getClass().getSimpleName(),"updateMeter"));
        MeterModel model = meterModelRepository.findById(energyMeterPutDto.getIdModel())
                .orElseThrow(()->new ModelNotFoundException(this.getClass().getSimpleName(),"updateMeter"));

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

        EnergyMeterDto energyMeterDto = new EnergyMeterDto();
        energyMeterDto = energyMeterDto.from(updatedMeter);

        return energyMeterDto;
    };


    public ResidenceDto getResidenceByMeterSerialNumber(Integer serialNumber)throws MeterNotFoundException, ResidenceNotFoundException {
        EnergyMeter energyMeter = energyMeterRepository.findById(serialNumber).orElseThrow(()-> new MeterNotFoundException(this.getClass().getSimpleName(),"getResidenceByMeterSerialNumber"));
        Residence residence = new Residence();
        if(energyMeter.getResidence() != null){
            return ResidenceDto.from(energyMeter.getResidence());
        }else{
            throw new ResidenceNotFoundException(this.getClass().getSimpleName(),"getResidenceByMeterSerialNumber");
        }
    }

}
