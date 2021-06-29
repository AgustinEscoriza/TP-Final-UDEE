package utn.tpFinal.UDEE.service;

import io.micrometer.core.instrument.Meter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;
import utn.tpFinal.UDEE.exceptions.BrandNotFoundException;
import utn.tpFinal.UDEE.exceptions.MeterAlreadyExistException;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.exceptions.ModelNotFoundException;
import utn.tpFinal.UDEE.model.Brand;
import utn.tpFinal.UDEE.model.Dto.EnergyMeterAddDto;
import utn.tpFinal.UDEE.model.Dto.EnergyMeterPutDto;
import utn.tpFinal.UDEE.model.Dto.EnergyMeterResponseDto;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.MeterModel;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.repository.BrandRepository;
import utn.tpFinal.UDEE.repository.EnergyMeterRepository;
import utn.tpFinal.UDEE.repository.MeterModelRepository;
import utn.tpFinal.UDEE.util.TestConstants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;


public class EnergyMeterServiceTest {
    private EnergyMeterService energyMeterService;
    private EnergyMeterRepository energyMeterRepository;
    private BrandRepository brandRepository;
    private MeterModelRepository meterModelRepository;

    @BeforeEach
    public void setUp(){
        energyMeterRepository = mock(EnergyMeterRepository.class);
        brandRepository = mock(BrandRepository.class);
        meterModelRepository= mock(MeterModelRepository.class);
        energyMeterService = new EnergyMeterService(energyMeterRepository, brandRepository,meterModelRepository);
    }

    @Test
    public void add_Test200() throws ParseException, BrandNotFoundException, ModelNotFoundException, MeterAlreadyExistException {
        EnergyMeterAddDto meter = TestConstants.getEnergyMeterAddDto(1);

        Residence residence = TestConstants.getResidence(1);
        EnergyMeter addedMeter = EnergyMeter.builder()
                .serialNumber(1)
                .meterModel(TestConstants.getModel(meter.getIdModel()))
                .brand(TestConstants.getBrand(meter.getIdBrand()))
                .password("1234")
                .residences(residence)
                .measure(new ArrayList<>())
                .build();
        when(energyMeterRepository.existsById(meter.getSerialNumber())).thenReturn(false);
        when(energyMeterRepository.save(any(EnergyMeter.class))).thenReturn(addedMeter);
        when(brandRepository.findById(meter.getIdBrand())).thenReturn(Optional.of(TestConstants.getBrand(meter.getIdBrand())));
        when(meterModelRepository.findById(meter.getIdModel())).thenReturn(Optional.of(MeterModel.builder().id(meter.getIdModel()).name("sadsa").energyMeters(new ArrayList<>()).build()));

        when(meterModelRepository.save(TestConstants.getModel(meter.getIdModel()))).thenReturn(MeterModel.builder().build());

        Integer addedId= energyMeterService.add(meter);
        assertEquals(1,addedId);
    }
    @Test
    public void add_TestMeterAlreadyExist() throws BrandNotFoundException, ModelNotFoundException, MeterAlreadyExistException {
        EnergyMeterAddDto meter = TestConstants.getEnergyMeterAddDto(1);
        try{

            Residence residence = TestConstants.getResidence(1);
            EnergyMeter addedMeter = EnergyMeter.builder()
                    .serialNumber(1)
                    .meterModel(TestConstants.getModel(meter.getIdModel()))
                    .brand(TestConstants.getBrand(meter.getIdBrand()))
                    .password("1234")
                    .residences(residence)
                    .measure(new ArrayList<>())
                    .build();
            when(energyMeterRepository.existsById(meter.getSerialNumber())).thenReturn(true);
            when(energyMeterRepository.save(any(EnergyMeter.class))).thenReturn(addedMeter);
            when(brandRepository.findById(meter.getIdBrand())).thenReturn(Optional.of(TestConstants.getBrand(meter.getIdBrand())));
            when(meterModelRepository.findById(meter.getIdModel())).thenReturn(Optional.of(MeterModel.builder().id(meter.getIdModel()).name("sadsa").energyMeters(new ArrayList<>()).build()));

            when(meterModelRepository.save(TestConstants.getModel(meter.getIdModel()))).thenReturn(MeterModel.builder().build());

            Integer addedId= energyMeterService.add(meter);
            assertEquals(1,addedId);
        }catch (MeterAlreadyExistException e){
            assertEquals(e.getClass(),MeterAlreadyExistException.class);
            assertThrows(MeterAlreadyExistException.class, ()->energyMeterService.add(meter));
        }
    }

    @Test
    public void getBySerialNumber_TestOk() throws MeterNotFoundException {
        Integer serialNumber = 1;
        EnergyMeter findedMeter = EnergyMeter.builder().brand(TestConstants.getBrand(1)).meterModel(TestConstants.getModel(1)).serialNumber(serialNumber).measure(new ArrayList<>()).residences(TestConstants.getResidence(1)).password("324").build();
        when(energyMeterRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.ofNullable(findedMeter));

        EnergyMeterResponseDto energyMeterResponseDto = energyMeterService.getBySerialNumber(serialNumber);

        assertEquals(serialNumber,energyMeterResponseDto.getSerialNumber());
    }
    @Test
    public void getBySerialNumber_TestNotFound()  {
        Integer serialNumber = 1;
        EnergyMeter findedMeter = EnergyMeter.builder().brand(TestConstants.getBrand(1)).meterModel(TestConstants.getModel(1)).serialNumber(serialNumber).measure(new ArrayList<>()).residences(TestConstants.getResidence(1)).password("324").build();
        when(energyMeterRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());

        assertThrows(MeterNotFoundException.class,()->energyMeterService.getBySerialNumber(serialNumber));
    }
    @Test
    public void deleteEnergyMeterBySerialNumberOk() throws MeterNotFoundException {
        Integer serialNumber = 1;

        when(energyMeterRepository.existsById(serialNumber)).thenReturn(true);

        Boolean deleted = energyMeterService.deleteEnergyMeterBySerialNumber(serialNumber);

        assertTrue(deleted);
    };
    @Test
    public void deleteEnergyMeterBySerialNumberMeterNotFoundException() {
        Integer serialNumber = 1;

        when(energyMeterRepository.existsById(serialNumber)).thenReturn(false);

        assertThrows(MeterNotFoundException.class,()->energyMeterService.deleteEnergyMeterBySerialNumber(serialNumber));
    };
    @Test
    public void updateMeter_TestOK() throws BrandNotFoundException, MeterNotFoundException, ModelNotFoundException {
        EnergyMeterPutDto meter = TestConstants.getEnergyMeterPutDto();
        Integer serialNumber = 1;
        Residence residence = TestConstants.getResidence(1);
        EnergyMeter changedMeter = EnergyMeter.builder()
                .serialNumber(serialNumber)
                .meterModel(TestConstants.getModel(meter.getIdModel()))
                .brand(TestConstants.getBrand(meter.getIdBrand()))
                .password("123")
                .residences(residence)
                .measure(new ArrayList<>())
                .build();
        when(energyMeterRepository.existsById(serialNumber)).thenReturn(true);
        when(energyMeterRepository.save(any(EnergyMeter.class))).thenReturn(changedMeter);
        when(brandRepository.findById(meter.getIdBrand())).thenReturn(Optional.of(TestConstants.getBrand(meter.getIdBrand())));
        when(meterModelRepository.findById(meter.getIdModel())).thenReturn(Optional.of(MeterModel.builder().id(meter.getIdModel()).name("sadsa").energyMeters(new ArrayList<>()).build()));

        EnergyMeterResponseDto updateMeter= energyMeterService.updateMeter(serialNumber,meter);
        assertEquals(serialNumber,updateMeter.getSerialNumber());
        assertEquals(meter.getPassword(),updateMeter.getPassword());
    }
    @Test
    public void updateMeter_TestMeterNotFoundException() throws BrandNotFoundException, MeterNotFoundException, ModelNotFoundException {
        EnergyMeterPutDto meter = TestConstants.getEnergyMeterPutDto();
        Integer serialNumber = 1;
        Residence residence = TestConstants.getResidence(1);
        EnergyMeter changedMeter = EnergyMeter.builder()
                .serialNumber(serialNumber)
                .meterModel(TestConstants.getModel(meter.getIdModel()))
                .brand(TestConstants.getBrand(meter.getIdBrand()))
                .password("123")
                .residences(residence)
                .measure(new ArrayList<>())
                .build();
        when(brandRepository.findById(meter.getIdBrand())).thenReturn(Optional.of(TestConstants.getBrand(meter.getIdBrand())));
        when(meterModelRepository.findById(meter.getIdModel())).thenReturn(Optional.of(MeterModel.builder().id(meter.getIdModel()).name("sadsa").energyMeters(new ArrayList<>()).build()));
        when(energyMeterRepository.existsById(serialNumber)).thenReturn(false);
        assertThrows(MeterNotFoundException.class,()->energyMeterService.updateMeter(serialNumber,meter));
    }

    @Test
    public void getAll_TestOk(){
        Specification<EnergyMeter> energyMeterSpecification = mock(Specification.class);
        
    }
}
