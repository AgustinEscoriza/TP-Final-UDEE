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
public class EnergyMeterResponseDto {
    private Integer serialNumber;
    private String brandName;
    private String modelName;
    private String password;
    private String residenceStreet;
    private String residenceNumber;
    private List<MeasureResponseDto> measureResponseDto;

    public static EnergyMeterResponseDto from (EnergyMeter energyMeter){
        EnergyMeterResponseDto energyMeterResponseDto = null;

        energyMeterResponseDto = EnergyMeterResponseDto.builder()
                .serialNumber(energyMeter.getSerialNumber())
                .brandName(energyMeter.getBrand().getName())
                .modelName(energyMeter.getMeterModel().getName())
                .password(energyMeter.getPassword())
                .build();

        if(energyMeter.getMeasure() != null) energyMeterResponseDto.setMeasureResponseDto(MeasureResponseDto.from(energyMeter.getMeasure()));

        if(energyMeter.getResidences() != null){
            energyMeterResponseDto.setResidenceNumber(energyMeter.getResidences().getNumber());
            energyMeterResponseDto.setResidenceStreet(energyMeter.getResidences().getStreet());
        }
        return energyMeterResponseDto;
    };
    
    public static List<EnergyMeterResponseDto> from (List<EnergyMeter> listEnergyMeter){
        List<EnergyMeterResponseDto> listDto = new ArrayList<EnergyMeterResponseDto>();

        for(EnergyMeter e : listEnergyMeter)
            listDto.add(EnergyMeterResponseDto.from(e));

        return listDto;
    }
}
