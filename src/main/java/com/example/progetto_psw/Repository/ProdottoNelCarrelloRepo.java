package com.example.progetto_psw.Repository;

import com.example.progetto_psw.Model.Prodotto;
import com.example.progetto_psw.Model.ProdottoNelCarrello;
import com.example.progetto_psw.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdottoNelCarrelloRepo extends JpaRepository<ProdottoNelCarrello,Integer> {

    ProdottoNelCarrello findByUserAndProdotto(User user, Prodotto p);

}
