package utn.tpFinal.UDEE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotEmpty(message = "Serial Number Missing.")
    private Integer serialNumber;

    @NotEmpty(message = "Password Missing.")
    private String password;

    @ManyToOne
    @JoinColumn(name="id_model")
    private MeterModel meterModel;

    @ManyToOne
    @JoinColumn(name="id_brand")
    private Brand brand;

    @OneToOne(mappedBy = "energyMeter")
    @JsonIgnore
    private Residence residence;

    @OneToMany(mappedBy = "energyMeter",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Measurement> measure;

}