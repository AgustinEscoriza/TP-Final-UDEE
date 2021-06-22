package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import utn.tpFinal.UDEE.exceptions.FeeTypeNotFoundException;
import utn.tpFinal.UDEE.model.Dto.FeeTypeDto;
import utn.tpFinal.UDEE.model.Dto.FeeTypePutDto;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.FeeType;
import utn.tpFinal.UDEE.repository.FeeTypeRepository;

import java.util.List;

@Service
public class FeeTypeService {

    FeeTypeRepository feeTypeRepository;

    @Autowired
    public FeeTypeService(FeeTypeRepository feeTypeRepository) {
        this.feeTypeRepository = feeTypeRepository;
    }

    public Page<FeeType> getAll(Integer page, Integer size, List<Sort.Order> orderList){
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderList));
        Page<FeeType>feeTypes = feeTypeRepository.findAll(pageable);
        Page<FeeType> returnFeeTypes = Page.empty();
        if(!feeTypes.isEmpty()){

        }
        return feeTypes;
    }

    public Integer add(FeeType feeType){
        FeeType addedFeeType = feeTypeRepository.save(feeType);
        return addedFeeType.getId();
    }

    public FeeTypeDto getById(Integer idFeeType) throws FeeTypeNotFoundException {
        FeeType feeType = feeTypeRepository.findById(idFeeType).orElseThrow(()-> new FeeTypeNotFoundException(this.getClass().getSimpleName(),"getById"));
        FeeTypeDto dto = FeeTypeDto.builder().id(feeType.getId()).kwPricePerHour(feeType.getKwPricePerHour()).detail(feeType.getDetail()).build();
        return dto;
    }

    public Boolean deleteFeeType(Integer idFeeType)throws  FeeTypeNotFoundException{
        Boolean deleted = false;
        if(!feeTypeRepository.existsById(idFeeType)){
            throw new FeeTypeNotFoundException(this.getClass().getSimpleName(),"deleteFeeType");
        }
        feeTypeRepository.deleteById(idFeeType);

        if(!feeTypeRepository.existsById(idFeeType)){
            deleted = true;
        }
        return deleted;
    }

    public FeeTypeDto update(Integer idFeeType, FeeTypePutDto feeType) throws FeeTypeNotFoundException {
        if(!feeTypeRepository.existsById(idFeeType)){
            throw new FeeTypeNotFoundException(this.getClass().getSimpleName(),"update");
        }
        FeeType newFeeType = FeeType.builder().id(idFeeType).detail(feeType.getDetail()).kwPricePerHour(feeType.getKwPricePerHour()).build();

        FeeType updatedFeeType = feeTypeRepository.save(newFeeType);
        FeeTypeDto dto = FeeTypeDto.builder().id(updatedFeeType.getId()).detail(feeType.getDetail()).kwPricePerHour(feeType.getKwPricePerHour()).build();
        return dto;
    }
}
