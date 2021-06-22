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
import utn.tpFinal.UDEE.model.Dto.EnergyMeterDto;
import utn.tpFinal.UDEE.model.Dto.MeasureSenderDto;
import utn.tpFinal.UDEE.model.Dto.ResidenceDto;
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

    public EnergyMeter add(EnergyMeter energyMeter,Integer idModel,Integer idBrand) throws ModelNotFoundException, BrandNotFoundException, MeterAlreadyExistException {

        if(!energyMeterRepository.existsById(energyMeter.getSerialNumber())){
            Brand brand = brandRepository.findById(idBrand).orElseThrow(()-> new BrandNotFoundException(this.getClass().getSimpleName(),"addEnergyMeter"));
            energyMeter.setBrand(brand);

            MeterModel meterModel = meterModelRepository.findById(idModel).orElseThrow(()-> new ModelNotFoundException(this.getClass().getSimpleName(),"addEnergyMeter"));
            energyMeter.setMeterModel(meterModel);
            meterModel.getEnergyMeters().add(energyMeter);
            meterModelRepository.save(meterModel);

            return energyMeterRepository.save(energyMeter);
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

    public EnergyMeterDto updateMeter(Integer serialNumber, EnergyMeter newEnergyMeter,  Integer idModel, Integer idBrand) throws MeterNotFoundException, BrandNotFoundException, ModelNotFoundException {
        EnergyMeter energyMeter = energyMeterRepository.findById(serialNumber)
                .orElseThrow(()->new MeterNotFoundException(this.getClass().getSimpleName(),"updateMeter"));
        Brand brand = brandRepository.findById(idBrand)
                .orElseThrow(()->new BrandNotFoundException(this.getClass().getSimpleName(),"updateMeter"));
        MeterModel model = meterModelRepository.findById(idModel)
                .orElseThrow(()->new ModelNotFoundException(this.getClass().getSimpleName(),"updateMeter"));

        EnergyMeterDto energyMeterDto = new EnergyMeterDto();
        newEnergyMeter.setSerialNumber(serialNumber);
        newEnergyMeter.setBrand(brand);
        newEnergyMeter.setMeterModel(model);
        EnergyMeter updatedMeter = energyMeterRepository.save(newEnergyMeter);

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
