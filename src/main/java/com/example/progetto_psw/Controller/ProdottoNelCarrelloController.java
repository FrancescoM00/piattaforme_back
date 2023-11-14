package com.example.progetto_psw.Controller;

import Support.Exceptions.ProductNotAvailableException;
import Support.Exceptions.ProductNotExistException;
import Support.Exceptions.UserNotExistException;
import com.example.progetto_psw.Model.ProdottoNelCarrello;
import com.example.progetto_psw.Model.User;
import com.example.progetto_psw.Service.ProdottoNelCarrelloService;
import com.example.progetto_psw.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@RequestMapping("/carrello")
public class ProdottoNelCarrelloController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProdottoNelCarrelloService prodottoNelCarrelloService;

    @GetMapping("/ottieni_carrello")
    public ResponseEntity<List<ProdottoNelCarrello>> getCarrello(@RequestParam(value="email") String email) {
        System.out.println("entrato");
        try {
            User user = userService.getUser(email);
            List<ProdottoNelCarrello> ret = user.getCarrello();
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (UserNotExistException ex) {
            return new ResponseEntity("Email non valida",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/elimina_prodotto")
    public ResponseEntity eliminaProdotto(@RequestParam(value="id") int id, @RequestParam(value="email") String email){
        try{
            prodottoNelCarrelloService.eliminaProdottoNelCarrello(email,id);
            return new ResponseEntity("Prodotto eliminato con successo",HttpStatus.OK);
        } catch(ProductNotExistException ex1){
            return new ResponseEntity("Prodotto inesistente",HttpStatus.BAD_REQUEST);
        } catch(UserNotExistException ex2){
            return new ResponseEntity("Utente inesistente",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/svuota_carrello")
    public ResponseEntity svuotaCarrello(@RequestParam(value="email") String email ){
         try{
             prodottoNelCarrelloService.svuotaCarrello(email);
             return new ResponseEntity("Carrello svuotato con successo",HttpStatus.OK);
         } catch(UserNotExistException ex){
             return new ResponseEntity("Utente non valido",HttpStatus.BAD_REQUEST);
         }
    }

    @GetMapping("/aggiungi_prodotto_nel_carrello")
    public ResponseEntity<ProdottoNelCarrello> aggiungiProdotto(@RequestParam(value = "email") String email, @RequestParam (value="codice") int codice, @RequestParam(value="quantita") int quantita){
        try{
            ProdottoNelCarrello ret= prodottoNelCarrelloService.add(email,codice,quantita);
            return new ResponseEntity<>(ret,HttpStatus.OK);
        } catch(UserNotExistException ex1){
             return new ResponseEntity("Utente non esistente",HttpStatus.BAD_REQUEST);
        } catch (ProductNotExistException ex2){
            return new ResponseEntity("Prodotto inesistente",HttpStatus.BAD_REQUEST);
        } catch (ProductNotAvailableException ex3){
            return new ResponseEntity("Prodotto non disponibile",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/nProdotti")
    public ResponseEntity<Integer> nProdotti(@RequestParam(value = "email") String email){
        try{
            int ret=prodottoNelCarrelloService.nProdottiNelCarrello(email);
            return new ResponseEntity<>(ret,HttpStatus.OK);
        } catch (UserNotExistException ex){
            return new ResponseEntity("Utente inesistente",HttpStatus.BAD_REQUEST);
        }
    }

}
