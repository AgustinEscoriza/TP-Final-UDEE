package utn.tpFinal.UDEE.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionDto {

    private Float totalConsumption;
    private Integer dni;
    private List<InvoiceResponseDto> invoices;
}
