package utn.tpFinal.UDEE.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    private LocalDateTime dateTime;
    @NotNull
    private Float kwH;
    @NotNull
    private Boolean billed;

    @ManyToOne
    @JoinColumn(name = "id_energy_meter")
    private EnergyMeter energyMeter;
}
