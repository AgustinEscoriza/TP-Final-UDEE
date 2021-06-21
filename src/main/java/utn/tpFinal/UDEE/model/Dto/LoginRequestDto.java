package utn.tpFinal.UDEE.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.tpFinal.UDEE.model.EnergyMeter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private Boolean isEmployee;

}
