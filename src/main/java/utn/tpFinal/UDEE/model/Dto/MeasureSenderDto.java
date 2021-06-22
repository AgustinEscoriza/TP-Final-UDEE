package utn.tpFinal.UDEE.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.tpFinal.UDEE.model.EnergyMeter;
import utn.tpFinal.UDEE.model.Measurement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeasureSenderDto {

    private Integer id;
    private Date dateTime;
    private Float kwH;
    private Boolean billed;

    public static MeasureSenderDto from (Measurement measurement){
        return MeasureSenderDto.builder()
                .id(measurement.getId())
                .billed(measurement.getBilled())
                .kwH(measurement.getKwH())
                .dateTime(measurement.getDateTime())
                .build();
    }
    public static List<MeasureSenderDto> from (List<Measurement> measurements){
        List<MeasureSenderDto> measureSenderDto = new ArrayList<MeasureSenderDto>();

        for(Measurement m : measurements)
            measureSenderDto.add(MeasureSenderDto.from(m));

        return measureSenderDto;
    }
}
