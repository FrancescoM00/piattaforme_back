package com.example.progetto_psw.Service;

import Support.Exceptions.ExistProductException;
import Support.Exceptions.ProductNotExistException;
import com.example.progetto_psw.Model.ProdottoNelCarrello;
import com.example.progetto_psw.Model.Prodotto;
import com.example.progetto_psw.Model.User;
import com.example.progetto_psw.Repository.ProdottoNelCarrelloRepo;
import com.example.progetto_psw.Repository.ProdottoRepo;
import com.example.progetto_psw.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class ProdottoService {

    @Autowired
    private ProdottoRepo prodottoRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProdottoNelCarrelloRepo prodottoNelCarrelloRepo;

    @Transactional (propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
    public List<Prodotto> tuttiProdotti(){
        return prodottoRepo.findAll();
    }

    @Transactional (propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED)
    public Prodotto aggiungiProdotto(Prodotto prodotto) throws ExistProductException {
        if(prodottoRepo.existsById(prodotto.getCodice())){
            throw new ExistProductException();
        }
        return prodottoRepo.save(prodotto);
    }

    @Transactional (propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED)
    public void rimuoviProdotto(Integer codice) throws ProductNotExistException {
        if(!prodottoRepo.existsById(codice)){
            throw new ProductNotExistException();
        }

        Prodotto p=prodottoRepo.findByCodice(codice);
        List<User> users=userRepo.findAll();
        for(User u: users){
           List<ProdottoNelCarrello>  carrello= u.getCarrello();
           for(ProdottoNelCarrello prod:carrello){
               if(prod.equals(p)){
                   carrello.remove(prod);
               }
           }
        }
        prodottoRepo.delete(p);
        }

    @Transactional (propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED)
    public Prodotto updateProdotto(Prodotto prodottoNow) throws ProductNotExistException {
            if(!prodottoRepo.existsById(prodottoNow.getCodice())){
                throw new ProductNotExistException();
            }
            Prodotto prodotto=prodottoRepo.findByCodice(prodottoNow.getCodice());
            prodotto.setPrezzo(prodottoNow.getPrezzo());
            prodotto.setQuantita(prodottoNow.getQuantita());

            return prodottoRepo.save(prodotto);
    }

    @Transactional (propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
    public List<Prodotto> ricerca(String nome, List<String> marche) throws ProductNotExistException {
        if(marche.isEmpty())
            return prodottoRepo.findByNomeContaining(nome);

        return prodottoRepo.trovaProdottiPerMarcheEContenenteNome(marche, nome);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public List<Prodotto> paginazione(int nPagine, int grandezzaPagina, String ordinaPer){
        Pageable pageable = PageRequest.of(nPagine, grandezzaPagina, Sort.by(ordinaPer));
        Page<Prodotto> risultato = prodottoRepo.findAll(pageable);
        if(risultato.hasContent()){
            return risultato.getContent();
        }
        else{
            return new LinkedList<>();
        }
    }

}
