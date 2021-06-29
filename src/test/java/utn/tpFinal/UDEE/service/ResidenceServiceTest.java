package utn.tpFinal.UDEE.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import utn.tpFinal.UDEE.exceptions.*;
import utn.tpFinal.UDEE.model.*;
import utn.tpFinal.UDEE.model.Dto.ResidenceAddDto;
import utn.tpFinal.UDEE.model.Dto.ResidencePutDto;
import utn.tpFinal.UDEE.model.Dto.ResidenceResponseDto;
import utn.tpFinal.UDEE.repository.*;
import utn.tpFinal.UDEE.util.TestConstants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class ResidenceServiceTest {

    ResidenceRepository residenceRepository;
    ClientRepository clientRepository;
    EnergyMeterRepository energyMeterRepository;
    MeasurementRepository measurementRepository;
    InvoiceRepository invoiceRepository;
    FeeTypeRepository feeTypeRepository;
    ResidenceService residenceService;

    @BeforeEach
    public void setUp(){
        this.residenceRepository = mock(ResidenceRepository.class);
        this.clientRepository = mock(ClientRepository.class);
        this.energyMeterRepository = mock(EnergyMeterRepository.class);
        this.measurementRepository = mock(MeasurementRepository.class);
        this.invoiceRepository = mock(InvoiceRepository.class);
        this.feeTypeRepository = mock(FeeTypeRepository.class);
        this.residenceService = new ResidenceService(residenceRepository,clientRepository,energyMeterRepository,measurementRepository,invoiceRepository,feeTypeRepository);
    }
    @Test
    public void getAll_TestOk() throws ParseException {
        Specification<Residence> residenceSpecification = mock(Specification.class);
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","street");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Residence> residenceList = TestConstants.getResidenceList();
        Page<Residence> residencePage = new PageImpl<Residence>(residenceList);

        when(residenceRepository.findAll(residenceSpecification,pageable)).thenReturn(residencePage);

        Page<ResidenceResponseDto> residenceResponseDtoPage = residenceService.getAll(residenceSpecification,page,size,orders);

        assertFalse(residenceResponseDtoPage.getContent().isEmpty());
        assertEquals(1,residenceResponseDtoPage.getContent().get(0).getId());
        assertEquals(2,residenceResponseDtoPage.getNumberOfElements());
    }
    @Test
    public void getAll_TestEmpty() throws ParseException {
        Specification<Residence> residenceSpecification = mock(Specification.class);
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","street");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Residence> residenceList = new ArrayList<>();
        Page<Residence> residencePage = new PageImpl<Residence>(residenceList);

        when(residenceRepository.findAll(residenceSpecification,pageable)).thenReturn(residencePage);

        Page<ResidenceResponseDto> residenceResponseDtoPage = residenceService.getAll(residenceSpecification,page,size,orders);

        assertTrue(residenceResponseDtoPage.getContent().isEmpty());
    }

    @Test
    public void updateResidence_TestOk() throws ParseException, FeeTypeNotFoundException, ClientNotFoundException, ResidenceNotFoundException, MeterNotFoundException {
        Integer residenceId = 1;
        ResidencePutDto residencePutDto = TestConstants.getResidencePutDto();
        Client client = TestConstants.getClient(1);
        FeeType feeType = TestConstants.getFeeType(1);
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);

        Residence toUpdateResidence = Residence.builder().id(residenceId).energyMeter(energyMeter).feeType(feeType)
                .street("street").number("322").postalNumber(7600).client(client).invoices(new ArrayList<>()).build();

        when(residenceRepository.findById(residenceId)).thenReturn(Optional.of(TestConstants.getResidence(1)));
        when(clientRepository.findById(residencePutDto.getIdClient())).thenReturn(Optional.of(client));
        when(feeTypeRepository.findById(residencePutDto.getFee_value())).thenReturn(Optional.of(feeType));
        when(energyMeterRepository.findById(residencePutDto.getEnergyMeterSerialNumber())).thenReturn(Optional.of(energyMeter));

        when(residenceRepository.save(any(Residence.class))).thenReturn(toUpdateResidence);

        ResidenceResponseDto residenceResponseDto = residenceService.updateResidence(residenceId,residencePutDto);

        assertEquals(residenceId,residenceResponseDto.getId());
    }
    @Test
    public void updateResidence_TestFeeTypeNotFoundException() throws ParseException , ClientNotFoundException, ResidenceNotFoundException, MeterNotFoundException {
        Integer residenceId = 1;
        ResidencePutDto residencePutDto = TestConstants.getResidencePutDto();
        Client client = TestConstants.getClient(1);
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);

        when(residenceRepository.findById(residenceId)).thenReturn(Optional.of(TestConstants.getResidence(1)));
        when(clientRepository.findById(residencePutDto.getIdClient())).thenReturn(Optional.of(client));
        when(feeTypeRepository.findById(residencePutDto.getFee_value())).thenReturn(Optional.empty());
        when(energyMeterRepository.findById(residencePutDto.getEnergyMeterSerialNumber())).thenReturn(Optional.of(energyMeter));

        assertThrows(FeeTypeNotFoundException.class,()->residenceService.updateResidence(residenceId,residencePutDto));
    }
    @Test
    public void updateResidence_TestFeeMeterNotFoundException() throws ParseException, FeeTypeNotFoundException, ClientNotFoundException, ResidenceNotFoundException  {
        Integer residenceId = 1;
        ResidencePutDto residencePutDto = TestConstants.getResidencePutDto();
        Client client = TestConstants.getClient(1);
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);
        FeeType feeType = TestConstants.getFeeType(1);

        when(residenceRepository.findById(residenceId)).thenReturn(Optional.of(TestConstants.getResidence(1)));
        when(clientRepository.findById(residencePutDto.getIdClient())).thenReturn(Optional.of(client));
        when(feeTypeRepository.findById(residencePutDto.getFee_value())).thenReturn(Optional.of(feeType));
        when(energyMeterRepository.findById(residencePutDto.getEnergyMeterSerialNumber())).thenReturn(Optional.empty());

        assertThrows(MeterNotFoundException.class,()->residenceService.updateResidence(residenceId,residencePutDto));
    }
    @Test
    public void updateResidence_TestClientNotFoundException() throws ParseException, ResidenceNotFoundException  {
        Integer residenceId = 1;
        ResidencePutDto residencePutDto = TestConstants.getResidencePutDto();
        Client client = TestConstants.getClient(1);
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);
        FeeType feeType = TestConstants.getFeeType(1);

        when(residenceRepository.findById(residenceId)).thenReturn(Optional.of(TestConstants.getResidence(1)));
        when(clientRepository.findById(residencePutDto.getIdClient())).thenReturn(Optional.empty());
        when(feeTypeRepository.findById(residencePutDto.getFee_value())).thenReturn(Optional.of(feeType));
        when(energyMeterRepository.findById(residencePutDto.getEnergyMeterSerialNumber())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,()->residenceService.updateResidence(residenceId,residencePutDto));
    }
    @Test
    public void updateResidence_TestResidenceNotFoundException() throws ParseException  {
        Integer residenceId = 1;
        ResidencePutDto residencePutDto = TestConstants.getResidencePutDto();
        Client client = TestConstants.getClient(1);
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);
        FeeType feeType = TestConstants.getFeeType(1);

        when(residenceRepository.findById(residenceId)).thenReturn(Optional.empty());
        when(clientRepository.findById(residencePutDto.getIdClient())).thenReturn(Optional.empty());
        when(feeTypeRepository.findById(residencePutDto.getFee_value())).thenReturn(Optional.of(feeType));
        when(energyMeterRepository.findById(residencePutDto.getEnergyMeterSerialNumber())).thenReturn(Optional.empty());

        assertThrows(ResidenceNotFoundException.class,()->residenceService.updateResidence(residenceId,residencePutDto));
    }
    @Test
    public void addResidence_TestOk() throws ParseException, ClientNotFoundException, MeterAlreadyHasResidenceException, FeeTypeNotFoundException, MeterNotFoundException, ResidenceAlreadyExists {
        ResidenceAddDto residenceAddDto = TestConstants.getResidenceAddDto();
        Client client = TestConstants.getClient(1);
        FeeType feeType = TestConstants.getFeeType(1);
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);

        Residence newResidence = Residence.builder().id(1).energyMeter(energyMeter).feeType(feeType)
                .street("street").number("322").postalNumber(7600).client(client).invoices(new ArrayList<>()).build();

        when(clientRepository.findById(residenceAddDto.getIdClient())).thenReturn(Optional.of(client));
        when(feeTypeRepository.findById(residenceAddDto.getFee_value())).thenReturn(Optional.of(feeType));
        when(energyMeterRepository.findById(residenceAddDto.getEnergyMeterSerialNumber())).thenReturn(Optional.of(energyMeter));

        when(residenceRepository.save(any(Residence.class))).thenReturn(newResidence);

        Integer newId = residenceService.addResidence(residenceAddDto);

        assertEquals(1,newId);
    }
    @Test
    public void addResidence_TestClientNotFoundException() throws ParseException, ClientNotFoundException, MeterAlreadyHasResidenceException, FeeTypeNotFoundException, MeterNotFoundException, ResidenceAlreadyExists {
        ResidenceAddDto residenceAddDto = TestConstants.getResidenceAddDto();
        Client client = TestConstants.getClient(1);
        FeeType feeType = TestConstants.getFeeType(1);
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);

        Residence newResidence = Residence.builder().id(1).energyMeter(energyMeter).feeType(feeType)
                .street("street").number("322").postalNumber(7600).client(client).invoices(new ArrayList<>()).build();

        when(clientRepository.findById(residenceAddDto.getIdClient())).thenReturn(Optional.empty());
        when(feeTypeRepository.findById(residenceAddDto.getFee_value())).thenReturn(Optional.of(feeType));
        when(energyMeterRepository.findById(residenceAddDto.getEnergyMeterSerialNumber())).thenReturn(Optional.of(energyMeter));


        assertThrows(ClientNotFoundException.class,()->residenceService.addResidence(residenceAddDto));

    }
    @Test
    public void addResidence_TestMeterAlreadyHasResidenceException() throws ParseException, MeterAlreadyHasResidenceException, FeeTypeNotFoundException, MeterNotFoundException, ResidenceAlreadyExists {
        ResidenceAddDto residenceAddDto = TestConstants.getResidenceAddDto();
        Client client = TestConstants.getClient(1);
        FeeType feeType = TestConstants.getFeeType(1);
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);
        energyMeter.setResidences(TestConstants.getResidence(1));
        Residence newResidence = Residence.builder().id(1).energyMeter(energyMeter).feeType(feeType)
                .street("street").number("322").postalNumber(7600).client(client).invoices(new ArrayList<>()).build();

        when(clientRepository.findById(residenceAddDto.getIdClient())).thenReturn(Optional.of(client));
        when(feeTypeRepository.findById(residenceAddDto.getFee_value())).thenReturn(Optional.of(feeType));
        when(energyMeterRepository.findById(residenceAddDto.getEnergyMeterSerialNumber())).thenReturn(Optional.of(energyMeter));


        assertThrows(MeterAlreadyHasResidenceException.class,()->residenceService.addResidence(residenceAddDto));

    }
    @Test
    public void addResidence_TestFeeTypeNotFoundException() throws ParseException, ClientNotFoundException, MeterAlreadyHasResidenceException, MeterNotFoundException, ResidenceAlreadyExists {
        ResidenceAddDto residenceAddDto = TestConstants.getResidenceAddDto();
        Client client = TestConstants.getClient(1);
        FeeType feeType = TestConstants.getFeeType(1);
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);

        Residence newResidence = Residence.builder().id(1).energyMeter(energyMeter).feeType(feeType)
                .street("street").number("322").postalNumber(7600).client(client).invoices(new ArrayList<>()).build();

        when(clientRepository.findById(residenceAddDto.getIdClient())).thenReturn(Optional.of(client));
        when(feeTypeRepository.findById(residenceAddDto.getFee_value())).thenReturn(Optional.empty());
        when(energyMeterRepository.findById(residenceAddDto.getEnergyMeterSerialNumber())).thenReturn(Optional.of(energyMeter));


        assertThrows(FeeTypeNotFoundException.class,()->residenceService.addResidence(residenceAddDto));

    }
    @Test
    public void addResidence_TestMeterNotFoundException() throws ParseException, ClientNotFoundException, MeterAlreadyHasResidenceException, MeterNotFoundException {
        ResidenceAddDto residenceAddDto = TestConstants.getResidenceAddDto();
        Client client = TestConstants.getClient(1);
        FeeType feeType = TestConstants.getFeeType(1);
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);

        Residence newResidence = Residence.builder().id(1).energyMeter(energyMeter).feeType(feeType)
                .street("street").number("322").postalNumber(7600).client(client).invoices(new ArrayList<>()).build();

        when(clientRepository.findById(residenceAddDto.getIdClient())).thenReturn(Optional.of(client));
        when(feeTypeRepository.findById(residenceAddDto.getFee_value())).thenReturn(Optional.of(feeType));
        when(energyMeterRepository.findById(residenceAddDto.getEnergyMeterSerialNumber())).thenReturn(Optional.empty());


        assertThrows(MeterNotFoundException.class,()->residenceService.addResidence(residenceAddDto));
    }

    @Test
    public void removeResidence_TestOk() throws ResidenceNotFoundException {
        Integer idResidence = 1;
        when(residenceRepository.existsById(idResidence)).thenReturn(true);
        Boolean deleted = residenceService.removeResidence(idResidence);
        assertTrue(deleted);
    }
    @Test
    public void removeResidence_TestFail(){
        Integer idResidence = 1;
        when(residenceRepository.existsById(idResidence)).thenReturn(false);
        assertThrows(ResidenceNotFoundException.class,()->residenceService.removeResidence(idResidence));
    }
    @Test
    public void getResidenceByMeterSerialNumber_TestOk() throws ParseException, MeterNotFoundException, ResidenceNotFoundException {
        Integer serialNumber = 1;
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(serialNumber);
        energyMeter.setResidences(TestConstants.getResidence(1));
        when(energyMeterRepository.findById(serialNumber)).thenReturn(Optional.of(energyMeter));

        ResidenceResponseDto residenceResponseDto = residenceService.getResidenceByMeterSerialNumber(serialNumber);

        assertEquals(serialNumber,residenceResponseDto.getId());
    }
    @Test
    public void getREsidenceByMeterSerialNumber_TEstResidenceNotFound() throws ParseException {
        Integer serialNumber = 1;
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(serialNumber);

        when(energyMeterRepository.findById(serialNumber)).thenReturn(Optional.of(energyMeter));

        assertThrows(ResidenceNotFoundException.class,()->residenceService.getResidenceByMeterSerialNumber(serialNumber));
    }
    @Test
    public void getResidenceByMeterSerialNumber_TestMeterNotFound()  {
        Integer serialNumber = 1;

        when(energyMeterRepository.findById(serialNumber)).thenReturn(Optional.empty());

        assertThrows(MeterNotFoundException.class,()->residenceService.getResidenceByMeterSerialNumber(serialNumber));
    }

}
