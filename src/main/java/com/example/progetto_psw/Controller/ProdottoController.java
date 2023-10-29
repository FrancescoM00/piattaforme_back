package com.example.progetto_psw.Controller;

import Support.Exceptions.ExistProductException;
import Support.Exceptions.ProductNotExistException;
import com.example.progetto_psw.Model.Prodotto;
import com.example.progetto_psw.Service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@RequestMapping("/prodotti")
public class ProdottoController {

    @Autowired
    ProdottoService prodottoService;


    @GetMapping("/tutti_prodotti")
    public ResponseEntity<List<Prodotto>> getTutti(){
      return new ResponseEntity<>(prodottoService.tuttiProdotti(), HttpStatus.OK);
    }

    @DeleteMapping("/elimina_prodotto")
    public ResponseEntity eliminaProdotto(@RequestParam (value="codice") Integer codice){
        try{
            prodottoService.rimuoviProdotto(codice);
            return new ResponseEntity("Il prodotto e' stato eliminato",HttpStatus.OK);
        } catch(ProductNotExistException ex){
            return new ResponseEntity("Il prodotto non esiste",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/aggiungi_prodotto")
    public ResponseEntity<Prodotto> creaProdotto(@Valid @RequestBody Prodotto prodotto){
          try{
              Prodotto ret=prodottoService.aggiungiProdotto(prodotto);
              return new ResponseEntity<>(ret,HttpStatus.OK);
          } catch (ExistProductException ex){
              return new ResponseEntity("Il prodotto esiste gia'",HttpStatus.BAD_REQUEST);
          }
    }

    @PutMapping("/update_prodotto")
    public ResponseEntity<Prodotto> aggiornaProdotto(@RequestBody @Valid Prodotto prodotto){
          try{
              Prodotto ret=prodottoService.updateProdotto(prodotto);
              return new ResponseEntity<>(ret,HttpStatus.OK);
          } catch (ProductNotExistException ex){
              return new ResponseEntity("Il prodotto non esiste",HttpStatus.BAD_REQUEST);
          }
    }

    @GetMapping("/ricerca_per_nome")
    public ResponseEntity<List<Prodotto>> getByNome(@RequestParam(value="nome") String nome){
          try{
              List<Prodotto> ret=prodottoService.ricercaPerNome(nome);
              return new ResponseEntity<>(ret,HttpStatus.OK);
          } catch(ProductNotExistException ex){
              return new ResponseEntity("Nessun prodotto ha quel nome",HttpStatus.BAD_REQUEST);
          }
    }

    @GetMapping("/paginate")
    public ResponseEntity<List<Prodotto>> paginazione(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "codice",required = false) String sortBy) {
        List<Prodotto> result = prodottoService.paginazione(pageNumber, pageSize, sortBy);
        if ( result.size() <= 0 ) {
            return new ResponseEntity("Nessun risultato!", HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
