package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import utn.tpFinal.UDEE.exceptions.FeeTypeNotFoundException;
import utn.tpFinal.UDEE.model.Dto.FeeTypeResponseDto;
import utn.tpFinal.UDEE.model.Dto.FeeTypeRequestDto;
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

    public Page<FeeTypeResponseDto> getAllFees(Specification<FeeType> feeTypeSpecification, Integer page, Integer size, List<Sort.Order> orderList){
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderList));
        Page<FeeType>feeTypes = feeTypeRepository.findAll(feeTypeSpecification,pageable);
        Page<FeeTypeResponseDto> feeTypeResponseDtoPage = Page.empty();
        if(!feeTypes.isEmpty()){
            feeTypeResponseDtoPage = feeTypes.map(f->FeeTypeResponseDto.from(f));
        }
        return feeTypeResponseDtoPage;
    }

    public Integer add(FeeTypeRequestDto feeType){
        FeeType toAddType = FeeType.builder().detail(feeType.getDetail()).kwPricePerHour(feeType.getKwPricePerHour()).build();
        FeeType addedFeeType = feeTypeRepository.save(toAddType);
        return addedFeeType.getId();
    }

    public FeeTypeResponseDto getById(Integer idFeeType) throws FeeTypeNotFoundException {
        FeeType feeType = feeTypeRepository.findById(idFeeType).orElseThrow(()-> new FeeTypeNotFoundException(this.getClass().getSimpleName(),"getById"));
        FeeTypeResponseDto dto = FeeTypeResponseDto.builder().id(feeType.getId()).kwPricePerHour(feeType.getKwPricePerHour()).detail(feeType.getDetail()).build();
        return dto;
    }

    public Boolean deleteFeeType(Integer idFeeType)throws  FeeTypeNotFoundException{
        Boolean deleted = false;
        if(feeTypeRepository.existsById(idFeeType)){
            feeTypeRepository.deleteById(idFeeType);
            deleted = true;
        } else {
            throw new FeeTypeNotFoundException(this.getClass().getSimpleName(),"deleteFeeType");
        }
        return deleted;
    }

    public FeeTypeResponseDto update(Integer idFeeType, FeeTypeRequestDto feeType) throws FeeTypeNotFoundException {
        if(!feeTypeRepository.existsById(idFeeType)){
            throw new FeeTypeNotFoundException(this.getClass().getSimpleName(),"update");
        }
        FeeType newFeeType = FeeType.builder().id(idFeeType).detail(feeType.getDetail()).kwPricePerHour(feeType.getKwPricePerHour()).build();

        FeeType updatedFeeType = feeTypeRepository.save(newFeeType);
        FeeTypeResponseDto dto = FeeTypeResponseDto.builder().id(updatedFeeType.getId()).detail(feeType.getDetail()).kwPricePerHour(feeType.getKwPricePerHour()).build();
        return dto;
    }
}
