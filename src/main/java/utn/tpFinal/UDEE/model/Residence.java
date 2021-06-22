package utn.tpFinal.UDEE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "residences")
public class Residence {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String street;
    @NotNull
    private String number;
    @NotNull
    private Integer postalNumber;

    @Transient
    private FeeType feeType;

    @Basic
    private Integer feeValue;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "energyMeter_serialNumber")
    @ToString.Exclude
    private EnergyMeter energyMeter;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Client client;

    @PostLoad
    void fillTransient() {
        if (feeValue > 0) {
            this.feeType = FeeType.of(feeValue);
        }
    }
    @PrePersist
    void fillPersistent() {
        if (feeType != null) {
            this.feeValue = feeType.getId();
        }
    }
}
