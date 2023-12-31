package com.example.progetto_psw.Controller;

import Support.Exceptions.*;
import com.example.progetto_psw.Model.Ordine;
import com.example.progetto_psw.Service.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@RequestMapping("/ordini")
public class OrdineController {

    @Autowired
    private OrdineService ordineService;

    @GetMapping("/tutti_ordini")
    public ResponseEntity<List<Ordine>> getAllOrdini( @RequestParam (value = "email") String email) {
        try {
            List<Ordine> ret = ordineService.tuttiOrdini(email);
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (UserNotExistException ex) {
            return new ResponseEntity("utente non trovato", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/elimina_ordine")
    public ResponseEntity deleteOrdine(@RequestParam (value="email") String email, @RequestParam(value="id") Integer id){
        try{
            ordineService.eliminaOrdine(email,id);
            return new ResponseEntity("Ordine eliminato con successo",HttpStatus.OK);
        } catch(OrdineNotExistsException ex){
            return new ResponseEntity("Ordine inesistente",HttpStatus.BAD_REQUEST);
        } catch (UserNotExistException ex2){
            return new ResponseEntity("Utente inesistente",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/elimina_allordine")
    public ResponseEntity deleteAllOrdine(@RequestParam (value="email") String email){
        try{
            ordineService.eliminaAllOrdine(email);
            return new ResponseEntity("Tutti gli ordini sono stati cancellati",HttpStatus.OK);
        } catch (UserNotExistException ex){
            return new ResponseEntity("L'utente non esiste",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/elimina_ordine2")
    public ResponseEntity deleteOrdine(@RequestParam(value="id") Integer id){
        try{
            ordineService.eliminaOrdine(id);
            return new ResponseEntity("Ordine cancellato con successo",HttpStatus.OK);
        } catch(OrdineNotExistsException ex){
            return new ResponseEntity("Ordine inesistente",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/crea_ordine")
    public ResponseEntity<Ordine> effettuaOrdine(@RequestParam (value="email") String email) {
        try {
            Ordine ret = ordineService.effettuaOrdine(email);
            return new ResponseEntity<>(ret,HttpStatus.OK);
        } catch (ProductNotAvailableException ex){
            return new ResponseEntity("Impossibile effettuare l'ordine",HttpStatus.BAD_REQUEST);
        } catch (CarrelloVuotoException ex2){
            return new ResponseEntity("Il carrello e' vuoto, ordine non effettuato",HttpStatus.BAD_REQUEST);
        } catch(UserNotExistException ex3){
            return new ResponseEntity("Utente non trovato",HttpStatus.BAD_REQUEST);
        } catch (ProductModifiedException ex4){
            return new ResponseEntity("Il prodotto ha subito modifiche", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/aggiorna_ordine")
    public ResponseEntity<Ordine> updateOrdine(@RequestBody @Valid Ordine ordine){
       try{
           Ordine ret=ordineService.aggiorna(ordine);
           return new ResponseEntity<>(ret,HttpStatus.OK);
       } catch(OrdineNotExistsException ex){
           return new ResponseEntity("Ordine inesistente",HttpStatus.BAD_REQUEST);
       }
    }

}
