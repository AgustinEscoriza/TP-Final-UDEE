package utn.tpFinal.UDEE.model.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResidenceAddDto {
    private Integer id;
    private Integer fee_value;
    private String number;
    private String street;
    private Integer postal_number;
    private Integer idClient;
    private Integer energyMeterSerialNumber;
}
