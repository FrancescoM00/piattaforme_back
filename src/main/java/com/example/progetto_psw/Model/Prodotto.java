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

    public Prodotto(Integer codice,String nome, float prezzo,int quantita){
        this.codice=codice;
        this.nome=nome;
        this.prezzo=prezzo;
        this.quantita=quantita;
    }

    @Version
    private long version;

    @Id
    @Column(name = "codice", nullable = false)
    private Integer codice;

    @Basic
    private String nome;


    @Basic
    private String descrizione;

    @Basic
    private float prezzo;

    @Basic
    private int quantita;

    @OneToMany(mappedBy = "prodotto")
    @JsonIgnore
    private List<ProdottoNelCarrello> prodottoNelcarrello;

    public Prodotto() {

    }
}
