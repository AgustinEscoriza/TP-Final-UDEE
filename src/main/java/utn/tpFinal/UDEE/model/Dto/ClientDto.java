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
public class ClientDto {
    private Integer dni;
    private List<InvoiceDto> invoices;
    private UserDto user;
    private List<ResidenceDto> residences;


    public static ClientDto from(Client client){
        return ClientDto.builder()
                .dni(client.getDni())
                .user(UserDto.from(client.getUser()))
                .residences(ResidenceDto.from(client.getResidences()))
                .build();
    }

    public static List<ClientDto> from (List<Client> clientList){
        List<ClientDto> listDto = new ArrayList<ClientDto>();

        for(Client c : clientList)
            listDto.add(ClientDto.from(c));

        return listDto;
    }
}
