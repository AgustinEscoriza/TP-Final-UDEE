package utn.tpFinal.UDEE.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.tpFinal.UDEE.model.Invoice;
import utn.tpFinal.UDEE.model.Residence;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceDto {
    private Integer id;
    private Boolean paid;
    private Integer initialMeasurement;
    private Integer finalMeasurement;
    private Float totalConsumption;
    private String emissionDate;
    private String initialReadingDate;
    private String finalReadingDate;
    private Float finalPayment;
    private String adress;



    public static InvoiceDto from (Invoice invoice){
        Residence residence = invoice.getResidence();
        String address = residence.getStreet() + ' ' + residence.getNumber();

        return InvoiceDto.builder()
                .id(invoice.getId())
                .paid(invoice.getPaid())
                .initialMeasurement(invoice.getInitialMeasurement())
                .finalMeasurement(invoice.getFinalMeasurement())
                .totalConsumption(invoice.getTotalConsumption())
                .emissionDate(invoice.getEmissionDate().toString().substring(0,10))
                .initialReadingDate(invoice.getInitialReadingDate().toString().substring(0,10))
                .finalReadingDate(invoice.getFinalReadingDate().toString().substring(0,10))
                .adress(address)
                .build();
    }
}
