package utn.tpFinal.UDEE.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import utn.tpFinal.UDEE.exceptions.FeeTypeNotFoundException;
import utn.tpFinal.UDEE.model.Dto.*;
import utn.tpFinal.UDEE.model.FeeType;
import utn.tpFinal.UDEE.service.FeeTypeService;
import utn.tpFinal.UDEE.util.TestConstants;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FeeTypeControllerTest {
    private final static Integer PAGE=0;
    private final static Integer SIZE=5;
    private final static Integer ID_FEETYPE =1;

    private FeeTypeService feeTypeService;
    private FeeTypeController feeTypeController;


    @BeforeEach
    public void setUp(){
        feeTypeService = mock(FeeTypeService.class);
        feeTypeController = new FeeTypeController(feeTypeService);
    }
    /////////////// REMINDER: FEETYPE NO USA DTO DE RESPONSE/ADD PORQUE ME TERMINAN SIENDO LO MISMO QUE EL MODELO
    @Test
    public void getAll_Test200() throws Exception{
        Specification<FeeType> specification = mock(Specification.class);
        List<FeeTypeResponseDto> feeTypeList = TestConstants.getFeeTypeResponseDtoList();
        List<Sort.Order> orders = TestConstants.getOrders("detail","kwPricePerHour");
        Page<FeeTypeResponseDto> feeTypePage = mock(Page.class);

        when(feeTypePage.getTotalElements()).thenReturn(Long.valueOf(feeTypeList.size()));
        when(feeTypePage.getTotalPages()).thenReturn(1);
        when(feeTypePage.getContent()).thenReturn(feeTypeList);

        when(feeTypeService.getAllFees(specification,PAGE,SIZE,orders)).thenReturn(feeTypePage); ///no me toma este when no se por que


        ResponseEntity<List<FeeTypeResponseDto>> responseEntity = feeTypeController.getAll(PAGE,SIZE,"detail","kwPricePerHour",specification);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(2,Integer.parseInt(responseEntity.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, responseEntity.getBody().get(0).getId());
        assertEquals(2, responseEntity.getBody().get(1).getId());
    }
    @Test
    public void getAll_Test204(){
        Specification<FeeType> specification = mock(Specification.class);
        List<FeeTypeResponseDto> feeTypeList = new ArrayList<>();
        List<Sort.Order> orders = TestConstants.getOrders("detail","kwPricePerHour");
        Page<FeeTypeResponseDto> feeTypePage = Page.empty();

        when(feeTypeService.getAllFees(specification,PAGE,SIZE,orders)).thenReturn(feeTypePage);


        ResponseEntity<List<FeeTypeResponseDto>> responseEntity = feeTypeController.getAll(PAGE,SIZE,"detail","kwPricePerHour",specification);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(null,responseEntity.getBody());
    }

    @Test
    public void addFeeType_Test201(){
        MockHttpServletRequest request = new MockHttpServletRequest("POST","/backoffice/feetypes");
        request.setServerPort(8080);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        FeeTypeRequestDto feeTypeRequestDto = TestConstants.getFeeTypeRequestDto();

        when(feeTypeService.add(feeTypeRequestDto)).thenReturn(ID_FEETYPE);

        ResponseEntity responseEntity = feeTypeController.addFeeType(feeTypeRequestDto);

        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals("http://localhost:8080/backoffice/feetypes?id=1",responseEntity.getHeaders().get("Location").get(0));
    }

    @Test
    public void addFeeType_Test500(){
        MockHttpServletRequest request = new MockHttpServletRequest("POST","/backoffice/feetypes");
        request.setServerPort(8080);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        FeeTypeRequestDto feeTypeRequestDto = TestConstants.getFeeTypeRequestDto();

        when(feeTypeService.add(feeTypeRequestDto)).thenReturn(null);

        ResponseEntity responseEntity = feeTypeController.addFeeType(feeTypeRequestDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode());
    }

    @Test
    public void getFeeTypeById_Test200() throws FeeTypeNotFoundException {
        FeeTypeResponseDto feeTypeResponseDto = TestConstants.getFeeTypeResponseDto(ID_FEETYPE);

        when(feeTypeService.getById(ID_FEETYPE)).thenReturn(feeTypeResponseDto);

        ResponseEntity responseEntity = feeTypeController.getFeeTypeById(ID_FEETYPE);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void deleteFeeType_Test204() throws FeeTypeNotFoundException {
        when(feeTypeService.deleteFeeType(ID_FEETYPE)).thenReturn(false);
        ResponseEntity responseEntity = feeTypeController.deleteFeeType(ID_FEETYPE);
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    @Test
    public void deleteFeeType_Test200() throws FeeTypeNotFoundException {
        when(feeTypeService.deleteFeeType(ID_FEETYPE)).thenReturn(true);
        ResponseEntity responseEntity = feeTypeController.deleteFeeType(ID_FEETYPE);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void updateFeeType_Test200() throws FeeTypeNotFoundException {

        FeeTypeResponseDto feeTypeResponseDto = TestConstants.getFeeTypeResponseDto(ID_FEETYPE);
        FeeTypeRequestDto feeTypeRequestDto = TestConstants.getFeeTypeRequestDto();

        when(feeTypeService.update(ID_FEETYPE,feeTypeRequestDto)).thenReturn(feeTypeResponseDto);


        ResponseEntity responseEntity = feeTypeController.updateFeeType(ID_FEETYPE,feeTypeRequestDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(ID_FEETYPE,feeTypeResponseDto.getId());
    }

}
