package utn.tpFinal.UDEE.util;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import utn.tpFinal.UDEE.model.*;
import utn.tpFinal.UDEE.model.Dto.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestConstants {

    // -----------------------ENERGY METERS------------------------------------------------
    public static List<EnergyMeterResponseDto> getEnergyMeterResponseDtoList(){
        EnergyMeterResponseDto energyMeterResponseDto1 = EnergyMeterResponseDto.builder().brandName("brand1").modelName("model1").password("pass1").serialNumber(1).measureResponseDto(getMeasureResponseDtoList()).build();
        EnergyMeterResponseDto energyMeterResponseDto2 = EnergyMeterResponseDto.builder().brandName("brand2").modelName("model2").password("pass2").serialNumber(2).measureResponseDto(getMeasureResponseDtoList()).build();
        List<EnergyMeterResponseDto> energyMeterResponseDtoList = new ArrayList<>();
        energyMeterResponseDtoList.add(energyMeterResponseDto1);
        energyMeterResponseDtoList.add(energyMeterResponseDto2);
        return energyMeterResponseDtoList;
    }
    public static EnergyMeterAddDto getEnergyMeterAddDto(Integer serialNumber) {
        return EnergyMeterAddDto.builder()
                .serialNumber(serialNumber)
                .idBrand(1)
                .idModel(2)
                .password("OEA333")
                .build();
    }
    public static EnergyMeterPutDto getEnergyMeterPutDto() {
        return EnergyMeterPutDto.builder()
                .idBrand(1)
                .idModel(5)
                .password("123")
                .build();
    }
    public static EnergyMeter getEnergyMeter(Integer serialNumber) throws ParseException {
        return EnergyMeter.builder()
                .measure(getMeasurementsList())
                .brand(getBrand(1))
                .meterModel(getModel(1))
                .build();
    }
    public static EnergyMeterResponseDto getEnergyMeterResponseDto(Integer serialNumber) {
        return EnergyMeterResponseDto.builder()
                .brandName("brand1")
                .modelName("model1")
                .password("pass1")
                .serialNumber(serialNumber)
                .measureResponseDto(getMeasureResponseDtoList())
                .build();
    }
    // -----------------------MEASUREMENTS------------------------------------------------
    public static List<MeasureResponseDto> getMeasureResponseDtoList(){
        List<MeasureResponseDto> measureResponseDtoList = new ArrayList<>();
        measureResponseDtoList.add(MeasureResponseDto.builder().id(1).billed(true).dateTime("2020-02-02").kwH(12.4f).build());
        measureResponseDtoList.add(MeasureResponseDto.builder().id(2).billed(false).dateTime("2020-03-02").kwH(15.4f).build());
        return measureResponseDtoList;
    }
    public static List<Measurement> getMeasurementsList() throws ParseException {
        List<Measurement> measurementList = new ArrayList<>();
        measurementList.add(Measurement.builder().id(1).billed(true).date(getDate(1)).kwH(12.4f).build());
        measurementList.add(Measurement.builder().id(2).billed(false).date(getDate(2)).kwH(15.4f).build());
        return measurementList;
    }
    public static Measurement getMeasurement(Integer id) throws ParseException {
        return Measurement.builder()
                .id(id)
                .kwH(100F)
                .date(getDate(1))
                .billed(false)
                .build();
    }

    public static MeasureRequestDto getMeasureRequestDto() throws ParseException {
        return MeasureRequestDto.builder()
                .value(100F)
                .date("2021-06-02 12:12:12")
                .password("1234")
                .serialNumber("001")
                .build();
    }

    // -----------------------RESIDENCE------------------------------------------------

    public static ResidenceAddDto getResidenceAddDto() {
        return ResidenceAddDto.builder()
                .fee_value(1)
                .energyMeterSerialNumber(1)
                .idClient(1)
                .number("312")
                .street("Street")
                .postal_number(7600)
                .build();
    }

    public static List<ResidenceResponseDto> getResidenceResponseDtoList() {
        List<ResidenceResponseDto> residenceResponseDtoList = new ArrayList<>();
        residenceResponseDtoList.add(ResidenceResponseDto.builder().id(1).postalNumber(7600).street("steet1").number("123").build());
        residenceResponseDtoList.add(ResidenceResponseDto.builder().id(2).postalNumber(7600).street("steet2").number("456").build());
        return residenceResponseDtoList;
    }

    public static ResidenceResponseDto getResidenceResponseDto() throws ParseException {
        return ResidenceResponseDto.builder()
                .id(1)
                .street("Street")
                .postalNumber(123)
                .number("123")
                .energyMeter(getEnergyMeterResponseDto(1))
                .invoices(getInvoiceResponseList())
                .feeType(getFeeType(1))
                .build();
    }
    public static Residence getResidence(Integer id) {
        return Residence.builder()
                .street("street")
                .postalNumber(123)
                .number("213")
                .id(id)
                .build();
    }
    public static ResidencePutDto getResidencePutDto() {
        return ResidencePutDto.builder()
                .energyMeterSerialNumber(1)
                .fee_value(1)
                .idClient(1)
                .number("132")
                .postal_number(7600)
                .street("Street")
                .build();
    }

    // -----------------------FeeType------------------------------------------------
    public static FeeType getFeeType(Integer id) {
        return FeeType.builder()
                .id(id)
                .kwPricePerHour(22)
                .detail("comercial")
                .build();
    }
    public static FeeTypeResponseDto getFeeTypeResponseDto(Integer id) {
        return FeeTypeResponseDto.builder()
                .id(id)
                .kwPricePerHour(22)
                .detail("comercial")
                .build();
    }

    public static List<FeeType> getFeeTypeList() {
        List<FeeType> feeTypeList = new ArrayList<>();
        feeTypeList.add(getFeeType(1));
        feeTypeList.add(getFeeType(2));
        return feeTypeList;
    }
    public static List<FeeTypeResponseDto> getFeeTypeResponseDtoList() {
        List<FeeTypeResponseDto> feeTypeList = new ArrayList<>();
        feeTypeList.add(FeeTypeResponseDto.builder().kwPricePerHour(22).detail("comercio").id(1).build());
        feeTypeList.add(FeeTypeResponseDto.builder().kwPricePerHour(23).detail("Industria").id(2).build());
        return feeTypeList;
    }
    public static FeeTypeRequestDto getFeeTypeRequestDto() {
        return FeeTypeRequestDto.builder()
                .detail("Comercial")
                .kwPricePerHour(23)
                .build();
    }
    // -----------------------CLIENT------------------------------------------------

    public static ClientAddDto getClientAddDto() {
        return ClientAddDto.builder().dni(1).build();
    }
    public static Client getClient(Integer dni) {
        return Client.builder()
                .dni(dni)
                .user(getUserClient(1))
                .residences(new ArrayList<>())
                .invoices(new ArrayList<>())
                .build();
    }
    public static List<ClientResponseDto> getClientResponseDtoList() {
        List<ClientResponseDto> clientResponseDtoList = new ArrayList<>();
        clientResponseDtoList.add(ClientResponseDto.from(getClient(1)));
        clientResponseDtoList.add(ClientResponseDto.from(getClient(2)));
        return clientResponseDtoList;
    }
    public static ClientResponseDto getClientResponseDto() {
        return ClientResponseDto.from(getClient(1));
    }
    // -----------------------User------------------------------------------------
    public static LoginRequestDto getLoginRequestDto() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("juan","juanez","juanjuan@email.com","213",false);
        return loginRequestDto;
    }
    public static User getUserClient(Integer id) {
        return User.builder()
                .id(id)
                .email("email@email.com")
                .isEmployee(false)
                .lastName("esco")
                .name("Agus")
                .password("1231")
                .build();
    }
    public static User getUserAdmin(Integer id) {
        return User.builder()
                .id(id)
                .email("email@email.com")
                .isEmployee(true)
                .lastName("esco")
                .name("Agus")
                .password("1231")
                .build();
    }
    public static UserDto getUserDto(Integer id) {
        return UserDto.from(getUserClient(id));
    }
    // -----------------------INVOICES------------------------------------------------
    public static Invoice getInvoice(Integer idInvoice) throws ParseException {
        return Invoice.builder()
                .client(getClient(2411244))
                .residence(Residence.builder().id(1).number("213").street("23123").build())
                .emissionDate(getDate(2))
                .energyMeter(EnergyMeter.builder().serialNumber(1).brand(Brand.builder().id(1).name("ADS23").build()).meterModel(MeterModel.builder().id(1).name("ASD123").build()).build())
                .finalMeasurement(36.5f)
                .finalPrice(2322f)
                .finalReadingDate(getDate(2))
                .initialReadingDate(getDate(1))
                .initialMeasurement(12.5f)
                .paid(false)
                .id(idInvoice)
                .totalConsumption(24.5f)
                .build();
    }

    public static List<InvoiceResponseDto> getInvoiceResponseList() throws ParseException {
        List<InvoiceResponseDto> invoiceResponseDtoList = new ArrayList<>();
        invoiceResponseDtoList.add(InvoiceResponseDto.from(getInvoice(1)));
        invoiceResponseDtoList.add(InvoiceResponseDto.from(getInvoice(2)));

        return invoiceResponseDtoList;
    }

    // -----------------------OTHERS------------------------------------------------
    public static List<ConsumptionDto> getConsumpionDtoList() throws ParseException {
        List<ConsumptionDto> consumptionDtoList = new ArrayList<>();
        consumptionDtoList.add(ConsumptionDto.builder().dni(1).totalConsumption(123f).invoices(getInvoiceResponseList()).build());
        consumptionDtoList.add(ConsumptionDto.builder().dni(2).totalConsumption(133f).invoices(getInvoiceResponseList()).build());
        return consumptionDtoList;
    }
    public static Brand getBrand(Integer idBrand){
        return Brand.builder()
                .id(idBrand)
                .name("brand1")
                .build();
    }
    public static MeterModel getModel(Integer idModel){
        return MeterModel.builder()
                .name("model1")
                .id(idModel)
                .build();
    }
    public static List getGrandAuthorityClient(){
        List list = new ArrayList<SimpleGrantedAuthority>();
        list.add(new SimpleGrantedAuthority("CLIENT"));
        return list;
    }
    public static List getInvalidGrandAuthority(){
        List list = new ArrayList<SimpleGrantedAuthority>();
        list.add(new SimpleGrantedAuthority("AWEADAWD"));
        return list;
    }
    public static List getGrandAuthorityEmployee(){
        List list = new ArrayList<SimpleGrantedAuthority>();
        list.add(new SimpleGrantedAuthority("BACKOFFICE"));
        return list;
    }
    public static Date getDate(Integer i) throws ParseException {
        Date date1 =  new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-02");
        Date date2 =  new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-02");
        Date rta=new Date();
        switch (i){
            case 1:
                rta= date1;
                break;
            case 2:
                rta= date2;
                break;
        }
        return rta;
    }
    public static List<Sort.Order> getOrders(String field1, String field2){
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, field1));
        orders.add(new Sort.Order(Sort.Direction.ASC, field2));

        return orders;
    }
    public static List<Sort.Order> getOrder(String field1) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, field1));

        return orders;
    }

    public static DatesFromAndToDto getDatesFromAndToDto() throws ParseException {
        return DatesFromAndToDto.builder()
                .from(getDate(1))
                .to(getDate(2))
                .build();
    }



}
