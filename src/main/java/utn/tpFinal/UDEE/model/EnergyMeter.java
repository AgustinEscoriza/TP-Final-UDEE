package utn.tpFinal.UDEE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "energyMeters")
public class EnergyMeter {

    @Id
    @NotEmpty(message = "Serial Number Missing.")
    private Integer serialNumber;

    @NotEmpty(message = "Password Missing.")
    private String password;

    @ManyToOne
    @JoinColumn(name="id_model")
    @ToString.Exclude
    @JsonIgnore
    private MeterModel meterModel;

    @ManyToOne
    @JoinColumn(name="id_brand")
    @ToString.Exclude
    @JsonIgnore
    private Brand brand;

    @OneToOne(mappedBy = "energyMeter")
    private Residence residence;

    @OneToMany(mappedBy = "energyMeter",fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<Measurement> measure;

}