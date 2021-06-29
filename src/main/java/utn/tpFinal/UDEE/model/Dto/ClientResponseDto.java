package utn.tpFinal.UDEE.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.tpFinal.UDEE.model.Client;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponseDto {
    private Integer dni;
    private List<InvoiceResponseDto> invoices;
    private UserDto user;
    private List<ResidenceResponseDto> residences;


    public static ClientResponseDto from(Client client){
        return ClientResponseDto.builder()
                .dni(client.getDni())
                .user(UserDto.from(client.getUser()))
                .residences(ResidenceResponseDto.from(client.getResidences()))
                .build();
    }

    public static List<ClientResponseDto> from (List<Client> clientList){
        List<ClientResponseDto> listDto = new ArrayList<ClientResponseDto>();

        for(Client c : clientList)
            listDto.add(ClientResponseDto.from(c));

        return listDto;
    }
}
