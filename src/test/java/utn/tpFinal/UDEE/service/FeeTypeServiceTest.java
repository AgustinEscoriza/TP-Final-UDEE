package utn.tpFinal.UDEE.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import utn.tpFinal.UDEE.controller.EnergyMeterController;
import utn.tpFinal.UDEE.exceptions.FeeTypeNotFoundException;
import utn.tpFinal.UDEE.model.Dto.FeeTypeRequestDto;
import utn.tpFinal.UDEE.model.Dto.FeeTypeResponseDto;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.FeeType;
import utn.tpFinal.UDEE.repository.BrandRepository;
import utn.tpFinal.UDEE.repository.EnergyMeterRepository;
import utn.tpFinal.UDEE.repository.FeeTypeRepository;
import utn.tpFinal.UDEE.repository.MeterModelRepository;
import utn.tpFinal.UDEE.util.TestConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class FeeTypeServiceTest {

    private FeeTypeRepository feeTypeRepository;
    private FeeTypeService feeTypeService;

    @BeforeEach
    public void setUp(){
        this.feeTypeRepository = mock(FeeTypeRepository.class);
        this.feeTypeService = new FeeTypeService(feeTypeRepository);
    }

    @Test
    public void add_Test(){
        FeeTypeRequestDto feeTypeRequestDto = TestConstants.getFeeTypeRequestDto();
        FeeType feeType = TestConstants.getFeeType(1);

        when(feeTypeRepository.save(any(FeeType.class))).thenReturn(feeType);

        Integer addedId = feeTypeService.add(feeTypeRequestDto);

        assertEquals(addedId,feeType.getId());
    }
    @Test
    public void getById_TestOk() throws FeeTypeNotFoundException {
        Integer idFeeType = 1;
        FeeType feeType = TestConstants.getFeeType(1);

        when(feeTypeRepository.findById(idFeeType)).thenReturn(Optional.of(feeType));

        FeeTypeResponseDto feeTypeResponseDto = feeTypeService.getById(idFeeType);

        assertEquals(idFeeType,feeTypeResponseDto.getId());
    }
    @Test
    public void getById_TestFeeTypeNotFound() {
        Integer idFeeType = 1;
        FeeType feeType = TestConstants.getFeeType(1);

        when(feeTypeRepository.findById(idFeeType)).thenReturn(Optional.empty());

        assertThrows(FeeTypeNotFoundException.class,()->feeTypeService.getById(idFeeType));

    }
    @Test
    public void deleteFeeType_TestOk() throws FeeTypeNotFoundException {
        Integer idFeeType = 1;
        when(feeTypeRepository.existsById(idFeeType)).thenReturn(true);
        Boolean deleted = feeTypeService.deleteFeeType(idFeeType);
        assertTrue(deleted);
    }
    @Test
    public void deleteFeeType_TestFeeTypeNotFound() {
        Integer idFeeType = 1;
        when(feeTypeRepository.existsById(idFeeType)).thenReturn(false);
        assertThrows(FeeTypeNotFoundException.class,()->feeTypeService.deleteFeeType(idFeeType));
    }
    @Test
    public void getAllFees_TestOk(){
        Specification<FeeType> feeTypeSpecification = mock(Specification.class);
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("detail","kwPricePerHour");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<FeeType> feeTypeList = TestConstants.getFeeTypeList();
        Page<FeeType> feeTypePage = new PageImpl<FeeType>(feeTypeList);

        when(feeTypeRepository.findAll(feeTypeSpecification,pageable)).thenReturn(feeTypePage);

        Page<FeeTypeResponseDto> feeTypeResponseDtoPage = feeTypeService.getAllFees(feeTypeSpecification,page,size,orders);

        assertEquals(false,feeTypeResponseDtoPage.getContent().isEmpty());
        assertEquals(1,feeTypeResponseDtoPage.getContent().get(0).getId());
        assertEquals(2,feeTypeResponseDtoPage.getNumberOfElements());
    }
    @Test
    public void getAllFees_TestEmpty(){
        Specification<FeeType> feeTypeSpecification = mock(Specification.class);
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("detail","kwPricePerHour");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<FeeType> feeTypeList = new ArrayList<>();
        Page<FeeType> feeTypePage = new PageImpl<FeeType>(feeTypeList);

        when(feeTypeRepository.findAll(feeTypeSpecification,pageable)).thenReturn(feeTypePage);

        Page<FeeTypeResponseDto> feeTypeResponseDtoPage = feeTypeService.getAllFees(feeTypeSpecification,page,size,orders);
        assertTrue(feeTypeResponseDtoPage.getContent().isEmpty());
    }

    @Test
    public void update_TestOk() throws FeeTypeNotFoundException {
        Integer idFeeType = 1;
        FeeTypeRequestDto feeTypeRequestDto = TestConstants.getFeeTypeRequestDto();
        FeeType updatedFeeType = FeeType.builder().id(1).detail("ASFGSAS").kwPricePerHour(231).build();

        when(feeTypeRepository.existsById(idFeeType)).thenReturn(true);
        when(feeTypeRepository.save(any(FeeType.class))).thenReturn(updatedFeeType);

        FeeTypeResponseDto feeTypeResponseDto = feeTypeService.update(idFeeType,feeTypeRequestDto);

        assertEquals(idFeeType,feeTypeResponseDto.getId());
        assertEquals(feeTypeRequestDto.getDetail(),feeTypeResponseDto.getDetail());
        assertEquals(feeTypeRequestDto.getKwPricePerHour(),feeTypeResponseDto.getKwPricePerHour());
    }
    @Test
    public void update_TestFeeTypeNotFound() {
        Integer idFeeType = 1;
        FeeTypeRequestDto feeTypeRequestDto = TestConstants.getFeeTypeRequestDto();

        when(feeTypeRepository.existsById(idFeeType)).thenReturn(false);

        assertThrows(FeeTypeNotFoundException.class,()->feeTypeService.update(idFeeType,feeTypeRequestDto));
    }
}
