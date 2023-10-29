package com.example.progetto_psw.Controller;

import Support.Exceptions.*;
import com.example.progetto_psw.Model.User;
import com.example.progetto_psw.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@RequestMapping("/users")
public class UserController {

     @Autowired
     UserService userService;


     @GetMapping("/ricerca_user")
     public ResponseEntity<User> ricercaUser(@RequestParam(value="email") String email) {
         try {
             User ret = userService.getUser(email);
             return new ResponseEntity<>(ret, HttpStatus.OK);
         } catch (UserNotExistException ex) {
             return new ResponseEntity("Email non valida",HttpStatus.BAD_REQUEST);
         }
     }

     @PostMapping("/registra_user")
    public ResponseEntity<User> registraUser(@Valid @RequestBody User user) {
         try {
              User ret=userService.registerUser(user);
              return new ResponseEntity<>(ret,HttpStatus.OK);
         } catch(ExistUserException ex){
              return new ResponseEntity("Utente gia' esistente",HttpStatus.BAD_REQUEST);
         } catch (MethodArgumentNotValidException ex2){
             return new ResponseEntity("Utente non registrato, inserisci dei dati validi",HttpStatus.BAD_REQUEST);
         } catch (IllegalArgumentException ex3){
             return new ResponseEntity("Utente non registrato, inserisci dei dati validi",HttpStatus.BAD_REQUEST);
         }
     }

     @DeleteMapping ("/delete_user_id")
    public ResponseEntity deleteUser(@RequestParam(value="id") int id){
          try {
               userService.deleteUser(id);
               return new ResponseEntity("Utente eliminato",HttpStatus.OK);
          } catch (UserNotExistException ex) {
              return new ResponseEntity("Utente non esistente",HttpStatus.BAD_REQUEST);
          }
          catch (OrdineNotExistsException ex2){
              return new ResponseEntity("Ordine non esistente",HttpStatus.BAD_REQUEST);
          }

    }

    @DeleteMapping ("/delete_user_email")
    public ResponseEntity deleteUser(@RequestParam(value="email") String email){
         try{
             userService.deleteUser(email);
             return new ResponseEntity("Utente eliminato",HttpStatus.OK);
         } catch(UserNotExistException ex){
             return new ResponseEntity("Utente non esistente",HttpStatus.BAD_REQUEST);
         } catch (OrdineNotExistsException ex2){
             return new ResponseEntity("Ordine non esistente",HttpStatus.BAD_REQUEST);
         }
    }

    @GetMapping("/all_user")
    public ResponseEntity<List<User>> allUser(){
         List<User> ret=userService.getAllUsers();
         return new ResponseEntity<>(ret,HttpStatus.OK);
    }
}
