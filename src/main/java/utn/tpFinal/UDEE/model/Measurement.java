package utn.tpFinal.UDEE.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "measurements")
public class Measurement {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Date date;
    @NotNull
    private Float kwH;
    @NotNull
    private Boolean billed;

    @ManyToOne
    @JoinColumn(name = "id_energy_meter")
    @ToString.Exclude
    private EnergyMeter energyMeter;
    @ManyToOne
    @JoinColumn(name = "id_residence")
    @ToString.Exclude
    private Residence residence;
}
