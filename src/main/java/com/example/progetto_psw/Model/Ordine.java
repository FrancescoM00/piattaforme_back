package com.example.progetto_psw.Model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name="ordine")

public class Ordine {

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_acquisto")
    private Date dataAcquisto;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @OneToMany(mappedBy = "ordine", cascade = CascadeType.MERGE)
    private List<ProdottoNelCarrello> carrello;

    public Ordine(List<ProdottoNelCarrello> prodottoNelCarrello, User user) {
        this.carrello = prodottoNelCarrello;
        this.user = user;
    }

    public Ordine() {
    }
}
