package utn.tpFinal.UDEE.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.tpFinal.UDEE.model.FeeType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeTypeResponseDto {
    private Integer id;
    private String detail;
    private Integer kwPricePerHour;

    public static FeeTypeResponseDto from(FeeType f) {
        return FeeTypeResponseDto.builder()
                .detail(f.getDetail())
                .id(f.getId())
                .kwPricePerHour(f.getKwPricePerHour())
                .build();
    }
}
