package utn.tpFinal.UDEE.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.FeeType;
import utn.tpFinal.UDEE.model.Residence;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResidenceDto {
    private Integer id;
    private String street;
    private String number;
    private Integer postalNumber;
    private FeeType feeType;
    private EnergyMeterDto energyMeter;

    public static ResidenceDto from(Residence residence){
        return ResidenceDto.builder()
                .street(residence.getStreet())
                .id(residence.getId())
                .number(residence.getNumber())
                .postalNumber(residence.getPostalNumber())
                .feeType(residence.getFeeType())
                .energyMeter(EnergyMeterDto.from(residence.getEnergyMeter()))
                .build();
    }

    public static List<ResidenceDto> from (List<Residence> residencesList){
        List<ResidenceDto> listDto = new ArrayList<ResidenceDto>();

        for(Residence r : residencesList)
            listDto.add(ResidenceDto.from(r));

        return listDto;
    }
}
