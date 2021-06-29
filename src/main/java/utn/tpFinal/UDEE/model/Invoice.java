package utn.tpFinal.UDEE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    //CONSUMPTION
    private Float initialMeasurement;
    private Float finalMeasurement;
    @NotNull
    private Float totalConsumption;

    //DATES
    @NotNull
    private Date emissionDate;
    private Date initialReadingDate;
    private Date finalReadingDate;

    @NotNull
    private float finalPrice;

    @Column(columnDefinition = "bool default 0")
    @NotNull
    private Boolean paid;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_residence")
    private Residence residence;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "energyMeter_serialNumber")
    private EnergyMeter energyMeter;


}

// DEBE TENER;
//        . Cliente --> traigo en relacion
//        ● Domicilio --> traigo en relacion
//        ● Numero de medidor --> traigo en relacion
//        ● Medición inicial --> la tiene el medidor
//        ● Medición final --> atributo de Factura
//        ● Fecha y hora medición inicial -->
//        ● Fecha y hora medición final --> todos los iniciales y finales hay que ver la mejor forma de traerlos.
//        ● Consumo total en Kwh --> atributo de Factura
//        ● Tipo de tarifa --> Enum
//        ● Total a pagar (Consumo * Tarifa) -->