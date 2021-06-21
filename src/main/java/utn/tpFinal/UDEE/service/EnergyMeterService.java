package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import utn.tpFinal.UDEE.exceptions.BrandNotFoundException;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.exceptions.ModelNotFoundException;
import utn.tpFinal.UDEE.exceptions.ResidenceNotFoundException;
import utn.tpFinal.UDEE.model.Brand;
import utn.tpFinal.UDEE.model.Dto.EnergyMeterDto;
import utn.tpFinal.UDEE.model.Dto.ResidenceDto;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.MeterModel;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.repository.BrandRepository;
import utn.tpFinal.UDEE.repository.ModelRepository;
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
    private ModelRepository modelRepository;

    public EnergyMeter getEnergyMeterBySerialNumber(Integer serial) throws MeterNotFoundException {
        return energyMeterRepository.findBySerialNumber(serial)
                .orElseThrow(() -> new MeterNotFoundException(this.getClass().getSimpleName(), "getEnergyMeterBySerialNumber"));
    }

    public EnergyMeter add(EnergyMeter energyMeter,Integer idModel,Integer idBrand) throws ModelNotFoundException, BrandNotFoundException {
        Brand brand = brandRepository.findById(idBrand).orElseThrow(()-> new BrandNotFoundException(this.getClass().getSimpleName(),"addEnergyMeter"));
        energyMeter.setBrand(brand);

        MeterModel meterModel = modelRepository.findById(idModel).orElseThrow(()-> new ModelNotFoundException(this.getClass().getSimpleName(),"addEnergyMeter"));
        energyMeter.setMeterModel(meterModel);
        meterModel.getEnergyMeters().add(energyMeter);
        modelRepository.save(meterModel);

        return energyMeterRepository.save(energyMeter);
    }

    public Page<EnergyMeterDto> getAll(Specification<EnergyMeter> meterSpecification, Integer page, Integer size, List<Sort.Order>orderList) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderList));
        Page<EnergyMeter>meters = energyMeterRepository.findAll(meterSpecification,pageable);
        Page<EnergyMeterDto> dtoMeters= Page.empty();

        if(!meters.isEmpty())
            dtoMeters=meters.map(m-> EnergyMeterDto.from(m));

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

    public EnergyMeterDto updateMeter(Integer serialNumber, EnergyMeter newEnergyMeter) throws MeterNotFoundException {
        Optional<EnergyMeter> energyMeter = energyMeterRepository.findBySerialNumber(serialNumber);
        EnergyMeterDto energyMeterDto = new EnergyMeterDto();
        if(energyMeter.isPresent()){
            newEnergyMeter.setSerialNumber(serialNumber);
            EnergyMeter updatedMeter = energyMeterRepository.save(newEnergyMeter);
            energyMeterDto = energyMeterDto.from(updatedMeter);
        }else{
            throw new MeterNotFoundException(this.getClass().getSimpleName(),"updateMeter");
        }

        return energyMeterDto;
    };


    public ResidenceDto getResidenceByMeterSerialNumber(Integer serialNumber)throws MeterNotFoundException, ResidenceNotFoundException {
        EnergyMeter energyMeter = getEnergyMeterBySerialNumber(serialNumber);
        Residence residence = energyMeter.getResidence();


        if(isNull(residence)){
            throw new ResidenceNotFoundException(this.getClass().getSimpleName(),"getResidenceByEnergyMeterId");
        }
        return ResidenceDto.from(residence);
    }

    public void add(EnergyMeter energyMeter) {
        energyMeterRepository.save(energyMeter);
    }
}
