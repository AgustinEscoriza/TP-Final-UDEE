package utn.tpFinal.UDEE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

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


    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="id_fee_type")
    @ToString.Exclude
    @JsonIgnore
    private FeeType feeType;

    @OneToMany(mappedBy = "residence",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Invoice> invoices;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "energyMeter_serialNumber")
    @ToString.Exclude
    private EnergyMeter energyMeter;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Client client;

}
