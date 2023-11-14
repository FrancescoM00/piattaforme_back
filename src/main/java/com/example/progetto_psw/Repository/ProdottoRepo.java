package com.example.progetto_psw.Repository;

import com.example.progetto_psw.Model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoRepo extends JpaRepository<Prodotto,Integer> {

    List<Prodotto> findByNome(String nome);
    Prodotto findByCodice(Integer codice);
    boolean existsByCodice(Integer codice);
    boolean existsByNome(String nome);
    @Query("SELECT p " +
            "FROM Prodotto p "+
             "WHERE (p.nome like %:nome%) AND "+
             "(p.marca IN (:marche))"
    )
    List<Prodotto> trovaProdottiPerMarcheEContenenteNome(@Param("marche") List<String> marche,@Param("nome") String nome);
    List<Prodotto> findByNomeContaining(String nome);
}
