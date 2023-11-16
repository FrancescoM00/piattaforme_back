package com.example.progetto_psw.Service;

import Support.Exceptions.*;
import com.example.progetto_psw.Model.ProdottoNelCarrello;
import com.example.progetto_psw.Model.Ordine;
import com.example.progetto_psw.Model.Prodotto;
import com.example.progetto_psw.Model.User;
import com.example.progetto_psw.Repository.ProdottoNelCarrelloRepo;
import com.example.progetto_psw.Repository.OrdineRepo;
import com.example.progetto_psw.Repository.ProdottoRepo;
import com.example.progetto_psw.Repository.UserRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdineService {

    @Autowired
    private OrdineRepo ordineRepo;

    @Autowired
    private ProdottoRepo prodottoRepo;

    @Autowired
    private ProdottoNelCarrelloRepo prodottoNelCarrelloRepo;

    @Autowired
    private UserRepo userRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public List<Ordine> tuttiOrdini(String email) throws UserNotExistException {
        if(!userRepo.existsByEmail(email)) {
            throw new UserNotExistException();
        }
        return ordineRepo.findByUser(userRepo.findByEmail(email));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void eliminaOrdine(String email, Integer id) throws OrdineNotExistsException, UserNotExistException {
        if(!ordineRepo.existsById(id)){
            throw new OrdineNotExistsException();
        }
        if(!userRepo.existsByEmail(email)){
            throw  new UserNotExistException();
        }
        User user=userRepo.findByEmail(email);
        List<Ordine> ordini=ordineRepo.findByUser(user);
        for(Ordine o: ordini){
            if(id==o.getId())
               ordineRepo.deleteById(o.getId());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void eliminaAllOrdine(String email) throws UserNotExistException {
        if(!userRepo.existsByEmail(email)){
            throw  new UserNotExistException();
        }
        User user=userRepo.findByEmail(email);
        List<Ordine> ordini=ordineRepo.findByUser(user);
        for(Ordine o: ordini){
                ordineRepo.deleteById(o.getId());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void eliminaOrdine(Integer id) throws OrdineNotExistsException {
        if(ordineRepo.existsById(id)){
            throw new OrdineNotExistsException();
        }
        ordineRepo.deleteById(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED, rollbackFor = {CarrelloVuotoException.class, ProductModifiedException.class})
    public Ordine effettuaOrdine(String email) throws UserNotExistException,ProductNotAvailableException, CarrelloVuotoException, ProductModifiedException {
        if(!userRepo.existsByEmail(email)){
            throw new UserNotExistException();
        }

        User user=userRepo.findByEmail(email);
        List<ProdottoNelCarrello> carrello=user.getCarrello();
        if(carrello.isEmpty()){
            throw new CarrelloVuotoException();
        }
        List<ProdottoNelCarrello> prodotti=new LinkedList<>();

        Ordine ret=new Ordine();

        for(ProdottoNelCarrello pnc:carrello){
            Prodotto prodotto= prodottoRepo.findByCodice(pnc.getProdotto().getCodice());
            entityManager.lock(pnc.getProdotto(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);

//            if(prodotto.getPrezzo()!= pnc.getPrezzo()){
//                pnc.setPrezzo(prodotto.getPrezzo());
//                prodottoNelCarrelloRepo.save(pnc);
//                entityManager.refresh(pnc);
//
//                System.out.println(pnc.getPrezzo());
//                throw new ProductModifiedException();
//            }

            int nuovaQuantita=pnc.getProdotto().getQuantita()-pnc.getQuantita();

            if(nuovaQuantita<0){
                throw new ProductNotAvailableException();
            }
            pnc.getProdotto().setQuantita(nuovaQuantita);

            ProdottoNelCarrello pnc2=new ProdottoNelCarrello();
            pnc2.setProdotto(pnc.getProdotto());
            pnc2.setQuantita(pnc.getQuantita());
            pnc2.setPrezzo(pnc.getPrezzo());
            pnc2.setOrdine(ret);
            prodotti.add(pnc2);
            //prodottoNelCarrelloRepo.delete(pnc);
        }
        ordineRepo.save(ret);
        entityManager.refresh(ret);
        ret.setUser(user);
        ret.setCarrello(prodotti);
        entityManager.merge(ret);
        return ret;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public Ordine aggiorna(Ordine ordine) throws OrdineNotExistsException {
        if(!ordineRepo.existsById(ordine.getId()))
            throw new OrdineNotExistsException();
        return ordineRepo.save(ordine);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Ordine> ricercaPerPeriodo (Date inizio, Date fine, User user){
        return ordineRepo.findByBuyerInPeriod(inizio, fine, user);
    }
}
