package com.example.progetto_psw.Repository;

import com.example.progetto_psw.Model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoRepo extends JpaRepository<Prodotto,Integer> {

    List<Prodotto> findByNome(String nome);
    Prodotto findByCodice(Integer codice);
    boolean existsByCodice(Integer codice);
    boolean existsByNome(String nome);

}
