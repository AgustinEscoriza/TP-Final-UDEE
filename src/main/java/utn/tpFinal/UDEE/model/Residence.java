package utn.tpFinal.UDEE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotNull
    @Transient
    private FeeType feeType;

    @Basic
    private Integer feeValue;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "energyMeter_serialNumber")
    private EnergyMeter energyMeter;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
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
