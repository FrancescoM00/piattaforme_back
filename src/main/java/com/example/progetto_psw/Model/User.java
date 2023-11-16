package com.example.progetto_psw.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name="user")
public class User {

    public User() {

    }

    public User(String nome,String cognome,String email){
        this.nome=nome;
        this.cognome=cognome;
        this.email=email;
    }

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @NotBlank
    @NotEmpty
    @Column (name="email", unique = true,nullable = false)
    private  String email;


    @Basic
    @Column (name="nome")
    private  String nome;


    @Basic
    @Column (name="cognome")
    private  String cognome;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private List<Ordine> ordini;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<ProdottoNelCarrello> carrello;

    @NotEmpty
    @NotBlank
    @Basic
    @Column (name="password", nullable = false)
    private  String password;

    @NotEmpty
    @NotBlank
    @Basic
    @Column (name="role", nullable = false)
    private  Boolean role;
}
