package utn.tpFinal.UDEE.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.tpFinal.UDEE.model.EnergyMeter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnergyMeterDto {
    private Integer serialNumber;
    private String brandName;
    private String modelName;
    private String password;
    private String residenceStreet;
    private String residenceNumber;
    private List<MeasureSenderDto> measureSenderDto;

    public static EnergyMeterDto from (EnergyMeter energyMeter){
        EnergyMeterDto energyMeterDto = null;

        energyMeterDto = EnergyMeterDto.builder()
                .serialNumber(energyMeter.getSerialNumber())
                .brandName(energyMeter.getBrand().getName())
                .modelName(energyMeter.getMeterModel().getName())
                .password(energyMeter.getPassword())
                .build();

        if(energyMeter.getMeasure() == null) energyMeterDto.setMeasureSenderDto(MeasureSenderDto.from(energyMeter.getMeasure()));

        if(energyMeter.getResidence() == null){
            energyMeterDto.setResidenceNumber(energyMeter.getResidence().getNumber());
            energyMeterDto.setResidenceStreet(energyMeter.getResidence().getStreet());
        }
        return energyMeterDto;
    };
    
    public static List<EnergyMeterDto> from (List<EnergyMeter> listEnergyMeter){
        List<EnergyMeterDto> listDto = new ArrayList<EnergyMeterDto>();

        for(EnergyMeter e : listEnergyMeter)
            listDto.add(EnergyMeterDto.from(e));

        return listDto;
    }
}
