package utn.tpFinal.UDEE.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "clients")
public class Client {

    @Id
    private Integer dni;

    @OneToMany(mappedBy = "client",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Residence> residences;

    @OneToMany(mappedBy = "client",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Invoice> invoices;

    @OneToOne
    @JoinColumn(name="user",foreignKey = @ForeignKey(name="FK_clients_users"))
    private User user;
}
