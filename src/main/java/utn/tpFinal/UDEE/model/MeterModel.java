package utn.tpFinal.UDEE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "meterModels")
public class MeterModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_brand",foreignKey = @ForeignKey(name="FK_models_brands"))
    @JsonIgnore
    @ToString.Exclude
    private Brand brand;

    @OneToMany(mappedBy = "meterModel",
            cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<EnergyMeter> energyMeters;
}
