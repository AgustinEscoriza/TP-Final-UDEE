package utn.tpFinal.UDEE.model.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnergyMeterAddDto {

    private Integer serialNumber;
    private String password;
    private Integer idModel;
    private Integer idBrand;

}
