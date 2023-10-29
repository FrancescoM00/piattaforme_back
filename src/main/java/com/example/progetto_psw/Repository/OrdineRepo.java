package com.example.progetto_psw.Repository;

import com.example.progetto_psw.Model.ProdottoNelCarrello;
import com.example.progetto_psw.Model.Ordine;
import com.example.progetto_psw.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrdineRepo extends JpaRepository<Ordine,Integer> {

    List<Ordine> findByUser(User user);

    @Query("SELECT o "+
            "FROM Ordine o "+
            "WHERE o.dataAcquisto > :inizio AND o.dataAcquisto < :fine AND o.user = :user")
    List<Ordine> findByBuyerInPeriod(Date inizio, Date fine, User user);
}
