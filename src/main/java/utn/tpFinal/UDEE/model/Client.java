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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "client",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Residence> residence;

    @OneToMany(mappedBy = "client",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Invoice> invoice;

    @OneToOne
    @JoinColumn(name="user",foreignKey = @ForeignKey(name="FK_clients_users"))
    private User user;
}
