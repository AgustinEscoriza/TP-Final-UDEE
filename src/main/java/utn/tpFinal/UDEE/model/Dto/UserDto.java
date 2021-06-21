package utn.tpFinal.UDEE.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.tpFinal.UDEE.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Integer id;
    private String name;
    private String lastName;
    private String email;
    private Boolean isEmployee;

    public static UserDto from(User user){
        return UserDto.builder().id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .isEmployee(user.getIsEmployee())
                .email(user.getEmail())
                .build();
    }
}
