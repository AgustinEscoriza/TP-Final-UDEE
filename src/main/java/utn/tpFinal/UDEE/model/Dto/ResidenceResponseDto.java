package utn.tpFinal.UDEE.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.tpFinal.UDEE.model.FeeType;
import utn.tpFinal.UDEE.model.Residence;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResidenceResponseDto {
    private Integer id;
    private String street;
    private String number;
    private Integer postalNumber;
    private FeeType feeType;
    private List<InvoiceResponseDto> invoices;
    private EnergyMeterResponseDto energyMeter;

    public static ResidenceResponseDto from(Residence residence){
        return ResidenceResponseDto.builder()
                .street(residence.getStreet())
                .id(residence.getId())
                .invoices(InvoiceResponseDto.from(residence.getInvoices()))
                .number(residence.getNumber())
                .postalNumber(residence.getPostalNumber())
                .feeType(FeeType.builder().id(residence.getFeeType().getId()).kwPricePerHour(residence.getFeeType().getKwPricePerHour()).detail(residence.getFeeType().getDetail()).build())
                .energyMeter(EnergyMeterResponseDto.from(residence.getEnergyMeter()))
                .build();
    }

    public static List<ResidenceResponseDto> from (List<Residence> residencesList){
        List<ResidenceResponseDto> listDto = new ArrayList<ResidenceResponseDto>();

        for(Residence r : residencesList)
            listDto.add(ResidenceResponseDto.from(r));

        return listDto;
    }
}
