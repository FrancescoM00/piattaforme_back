package com.example.progetto_psw.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name="carrello")

public class ProdottoNelCarrello {

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    private int quantita;

    @Basic
    private float prezzo;

    @ManyToOne
    @JoinColumn(name = "prodotto")
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "ordine_id")
    @JsonIgnore
    private Ordine ordine;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public ProdottoNelCarrello(User user, Prodotto prodotto, int quantita, float prezzo) {
        this.user=user;
        this.prodotto=prodotto;
        this.quantita=quantita;
        this.prezzo=prezzo;
    }

    public ProdottoNelCarrello() {

    }
}
