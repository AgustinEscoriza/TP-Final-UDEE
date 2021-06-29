package utn.tpFinal.UDEE.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.tpFinal.UDEE.model.Measurement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  MeasureResponseDto {

    private Integer id;
    private String dateTime;
    private Float kwH;
    private Boolean billed;

    public static MeasureResponseDto from (Measurement measurement){
        return MeasureResponseDto.builder()
                .id(measurement.getId())
                .billed(measurement.getBilled())
                .kwH(measurement.getKwH())
                .dateTime(measurement.getDate().toString())
                .build();
    }
    public static List<MeasureResponseDto> from (List<Measurement> measurements){
        List<MeasureResponseDto> measureResponseDto = new ArrayList<MeasureResponseDto>();

        for(Measurement m : measurements)
            measureResponseDto.add(MeasureResponseDto.from(m));

        return measureResponseDto;
    }
}
