package com.example.progetto_psw.Service;

import Support.Exceptions.ProductNotAvailableException;
import Support.Exceptions.ProductNotExistException;
import Support.Exceptions.UserNotExistException;
import com.example.progetto_psw.Model.Ordine;
import com.example.progetto_psw.Model.ProdottoNelCarrello;
import com.example.progetto_psw.Model.Prodotto;
import com.example.progetto_psw.Model.User;
import com.example.progetto_psw.Repository.OrdineRepo;
import com.example.progetto_psw.Repository.ProdottoNelCarrelloRepo;
import com.example.progetto_psw.Repository.ProdottoRepo;
import com.example.progetto_psw.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProdottoNelCarrelloService {

    @Autowired
    private ProdottoNelCarrelloRepo prodottoNelCarrelloRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProdottoRepo prodottoRepo;

    @Autowired
    private OrdineRepo ordineRepo;

    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public List<ProdottoNelCarrello> getCarrello(String email) throws UserNotExistException{
        if(!userRepo.existsByEmail(email)){
            throw new UserNotExistException();
        }
        User user=userRepo.findByEmail(email);
        return user.getCarrello();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void eliminaProdottoNelCarrello (String email, int id ) throws UserNotExistException, ProductNotExistException {
        if(!userRepo.existsByEmail(email))
            throw new UserNotExistException();
        if(!prodottoNelCarrelloRepo.existsById(id))
            throw new ProductNotExistException();

        User u=userRepo.findByEmail(email);
        List<ProdottoNelCarrello> carrello = u.getCarrello();
        for(ProdottoNelCarrello p:carrello){
            if(p.getId()==id){
               if(p.getQuantita()==1) {
                   prodottoNelCarrelloRepo.delete(p);
               }
               else
                   p.setQuantita(p.getQuantita()-1);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public ProdottoNelCarrello add(String email, Integer codice, int quantita) throws UserNotExistException,ProductNotExistException, ProductNotAvailableException {
        if(!userRepo.existsByEmail(email))
            throw new UserNotExistException();
        if(!prodottoRepo.existsByCodice(codice))
            throw new ProductNotExistException();

        ProdottoNelCarrello ret;
        User user=userRepo.findByEmail(email);
        List<ProdottoNelCarrello> carrello=user.getCarrello();
        Prodotto p=prodottoRepo.findByCodice(codice);



        if (prodottoNelCarrelloRepo.findByUserAndProdotto(user,p)!=null) {//il prodotto è già presente nel carrello devo aggiornare la quantita'
            ret=prodottoNelCarrelloRepo.findByUserAndProdotto(user,p);
            ret.setProdotto(p);
            ret.setQuantita(ret.getQuantita() + quantita);
            ret.setPrezzo(p.getPrezzo());
            ret.setUser(user);

        } else{ //se non è ancora presente nel carrello

            ret=new ProdottoNelCarrello(user,p,quantita,p.getPrezzo());
            carrello.add(ret);
            prodottoNelCarrelloRepo.save(ret);
        }
        return ret;
   }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
   public void svuotaCarrello(String email) throws UserNotExistException {
        if (!userRepo.existsByEmail(email))
            throw new UserNotExistException();

        User u = userRepo.findByEmail(email);
        List<ProdottoNelCarrello> carrello = u.getCarrello();
        for (ProdottoNelCarrello pnc : carrello) {
            prodottoNelCarrelloRepo.delete(pnc);
        }
        prodottoNelCarrelloRepo.flush();
    }


    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public int nProdottiNelCarrello(String email) throws UserNotExistException {
        if (!userRepo.existsByEmail(email))
            throw new UserNotExistException();
        User u = userRepo.findByEmail(email);
        List<ProdottoNelCarrello> carrello=u.getCarrello();
        int ret=0;
        for(ProdottoNelCarrello pnc : carrello)
            ret+=pnc.getQuantita();
        return ret;
    }


}

