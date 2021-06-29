package utn.tpFinal.UDEE.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import utn.tpFinal.UDEE.exceptions.ClientNotFoundException;
import utn.tpFinal.UDEE.exceptions.DatesBadRequestException;
import utn.tpFinal.UDEE.exceptions.MeterNotFoundException;
import utn.tpFinal.UDEE.exceptions.WrongPasswordException;
import utn.tpFinal.UDEE.model.Client;
import utn.tpFinal.UDEE.model.Dto.DatesFromAndToDto;
import utn.tpFinal.UDEE.model.Dto.MeasureRequestDto;
import utn.tpFinal.UDEE.model.Dto.MeasureResponseDto;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.Measurement;
import utn.tpFinal.UDEE.model.Residence;
import utn.tpFinal.UDEE.repository.ClientRepository;
import utn.tpFinal.UDEE.repository.EnergyMeterRepository;
import utn.tpFinal.UDEE.repository.MeasurementRepository;
import utn.tpFinal.UDEE.util.TestConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class MeasurementServiceTest {

    private MeasurementRepository measurementRepository;
    private EnergyMeterRepository energyMeterRepository;
    private ClientRepository clientRepository;
    private MeasurementService measurementService;

    @BeforeEach
    public void setUp(){
        this.measurementRepository = mock(MeasurementRepository.class);
        this.clientRepository = mock(ClientRepository.class);
        this.energyMeterRepository = mock(EnergyMeterRepository.class);
        this.measurementService = new MeasurementService(measurementRepository,energyMeterRepository,clientRepository);
    }

    @Test
    public void addMeasurement_TestOk() throws ParseException, MeterNotFoundException, WrongPasswordException {
        MeasureRequestDto measureRequestDto = TestConstants.getMeasureRequestDto();
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Measurement measurement = Measurement.builder()
                .id(1)
                .kwH(measureRequestDto.getValue())
                .energyMeter(energyMeter)
                .residence(energyMeter.getResidences())
                .residence(energyMeter.getResidences())
                .date(dateFormat.parse(measureRequestDto.getDate()))
                .billed(false)
                .build();
        when(energyMeterRepository.findById(Integer.parseInt(measureRequestDto.getSerialNumber()))).thenReturn(Optional.of(energyMeter));
        when(measurementRepository.save(any(Measurement.class))).thenReturn(measurement);

        Integer addedMeasurement = measurementService.addMeasurement(measureRequestDto);

        assertEquals(1,addedMeasurement);
    }
    @Test
    public void addMeasurement_TestWrongPassword() throws ParseException  {
        MeasureRequestDto measureRequestDto = TestConstants.getMeasureRequestDto();
        EnergyMeter energyMeter = TestConstants.getEnergyMeter(1);
        energyMeter.setPassword("ASFLKAVASKL");
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Measurement measurement = Measurement.builder()
                .id(1)
                .kwH(measureRequestDto.getValue())
                .energyMeter(energyMeter)
                .residence(energyMeter.getResidences())
                .residence(energyMeter.getResidences())
                .date(dateFormat.parse(measureRequestDto.getDate()))
                .billed(false)
                .build();
        when(energyMeterRepository.findById(Integer.parseInt(measureRequestDto.getSerialNumber()))).thenReturn(Optional.of(energyMeter));
        when(measurementRepository.save(any(Measurement.class))).thenReturn(measurement);

        assertThrows(WrongPasswordException.class,()->measurementService.addMeasurement(measureRequestDto));
    }
    @Test
    public void addMeasurement_TestMeterNotFound() throws ParseException {
        MeasureRequestDto measureRequestDto = TestConstants.getMeasureRequestDto();

        when(energyMeterRepository.findById(Integer.parseInt(measureRequestDto.getSerialNumber()))).thenReturn(Optional.empty());

        assertThrows(MeterNotFoundException.class,()->measurementService.addMeasurement(measureRequestDto));
    }

    @Test
    public void getResidenceMeasuresBetweenDates_Test() throws ParseException {
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        Integer idResidence = 1;
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","date");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Measurement> measurementList = TestConstants.getMeasurementsList();
        Page<Measurement> measurementPage = new PageImpl<Measurement>(measurementList);

        when(measurementRepository.findByResidenceIdAndDateBetween(idResidence,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),pageable)).thenReturn(measurementPage);

        Page<MeasureResponseDto> measureResponseDto = measurementService.getResidenceMeasuresBetweenDates(idResidence,datesFromAndToDto,page,size,orders);

        assertEquals(measurementList.get(0).getId(),measureResponseDto.getContent().get(0).getId());
        assertEquals(measurementList.get(0).getKwH(),measureResponseDto.getContent().get(0).getKwH());
    }

    @Test
    public void getResidenceMeasuresBetweenDates_TestEmpty() throws ParseException {
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        Integer idResidence = 1;
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","date");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Measurement> measurementList = new ArrayList<>();
        Page<Measurement> measurementPage = new PageImpl<Measurement>(measurementList);

        when(measurementRepository.findByResidenceIdAndDateBetween(idResidence,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),pageable)).thenReturn(measurementPage);

        Page<MeasureResponseDto> measureResponseDto = measurementService.getResidenceMeasuresBetweenDates(idResidence,datesFromAndToDto,page,size,orders);

        assertTrue(measureResponseDto.getContent().isEmpty());
    }

    @Test
    public void getClientMeasurementsByDates_TestOk() throws ParseException, DatesBadRequestException, ClientNotFoundException {
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        Integer idClient = 1;
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","date");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        Client client = TestConstants.getClient(idClient);
        List<Measurement> measurementList = TestConstants.getMeasurementsList();
        Page<Measurement> measurementPage = new PageImpl<Measurement>(measurementList);
        List<Residence> residences = new ArrayList<>();
        residences.add(TestConstants.getResidence(1));
        residences.add(TestConstants.getResidence(2));
        client.setResidences(residences);
        List<Integer> residencesIds = new ArrayList<>();
        residencesIds.add(1);
        residencesIds.add(2);

        when(clientRepository.findById(idClient)).thenReturn(Optional.of(client));
        when(measurementRepository.findByResidenceIdInAndDateBetween(residencesIds,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),pageable)).thenReturn(measurementPage);

        Page<MeasureResponseDto> measureResponseDto = measurementService.getClientMeasurementsByDates(idClient,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),page,size,orders);

        assertEquals(false,measureResponseDto.getContent().isEmpty());
        assertEquals(1,measureResponseDto.getContent().get(0).getId());
    }
    @Test
    public void getClientMeasurementsByDates_TestEmpty() throws ParseException, DatesBadRequestException, ClientNotFoundException {
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        Integer idClient = 1;
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","date");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        Client client = TestConstants.getClient(idClient);
        List<Measurement> measurementList = new ArrayList<>();
        Page<Measurement> measurementPage = new PageImpl<Measurement>(measurementList);
        List<Integer> residencesIds = new ArrayList<Integer>();
        when(clientRepository.findById(idClient)).thenReturn(Optional.of(client));
        when(measurementRepository.findByResidenceIdInAndDateBetween(residencesIds,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),pageable)).thenReturn(measurementPage);

        Page<MeasureResponseDto> measureResponseDto = measurementService.getClientMeasurementsByDates(idClient,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),page,size,orders);

        assertTrue(measureResponseDto.getContent().isEmpty());
    }
    @Test
    public void getClientMeasurementsByDates_TestDatesClientNotFound() throws ParseException {
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        Integer idClient = 1;
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","date");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        List<Measurement> measurementList = TestConstants.getMeasurementsList();
        Page<Measurement> measurementPage = new PageImpl<Measurement>(measurementList);
        when(clientRepository.findById(idClient)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,()->measurementService.getClientMeasurementsByDates(idClient,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),page,size,orders));

    }
    @Test
    public void getClientMeasurementsByDates_TestDatesBadRequest() throws ParseException {
        DatesFromAndToDto datesFromAndToDto = TestConstants.getDatesFromAndToDto();
        Integer idClient = 1;
        Integer page = 0;
        Integer size = 5;
        List<Sort.Order> orders = TestConstants.getOrders("id","date");
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        Client client = TestConstants.getClient(idClient);
        List<Measurement> measurementList = TestConstants.getMeasurementsList();
        Page<Measurement> measurementPage = new PageImpl<Measurement>(measurementList);
        List<Integer> residencesIds = new ArrayList<Integer>();
        residencesIds.add(1);
        when(clientRepository.findById(idClient)).thenReturn(Optional.of(client));
        when(measurementRepository.findByResidenceIdInAndDateBetween(residencesIds,datesFromAndToDto.getFrom(),datesFromAndToDto.getTo(),pageable)).thenReturn(measurementPage);

        assertThrows(DatesBadRequestException.class,()->measurementService.getClientMeasurementsByDates(idClient,datesFromAndToDto.getTo(),datesFromAndToDto.getFrom(),page,size,orders));

    }

}
