package com.example.progetto_psw.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Table(name="prodotto")

public class Prodotto {

    public Prodotto(Integer codice,String nome, float prezzo,int quantita, String marca){
        this.codice=codice;
        this.nome=nome;
        this.prezzo=prezzo;
        this.quantita=quantita;
        this.marca=marca;
    }

    @Version
    private long version;

    @Id
    @Column(name = "codice", nullable = false)
    private Integer codice;

    @Basic
    @Column(name = "nome", nullable = false)
    private String nome;


    @Basic
    @Column(name = "marca", nullable = false)
    private String marca;

    @Basic
    private String descrizione;

    @Basic
    @Column(name = "prezzo", nullable = false)
    private float prezzo;

    @Basic
    @Column(name = "quantita", nullable = false)
    private int quantita;

    @OneToMany(mappedBy = "prodotto", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<ProdottoNelCarrello> prodottoNelcarrello;

    public Prodotto() {

    }
}
