package com.example.progetto_psw.Service;

import Support.Exceptions.ExistUserException;
import Support.Exceptions.OrdineNotExistsException;
import Support.Exceptions.UserNotExistException;
import com.example.progetto_psw.Model.Ordine;
import com.example.progetto_psw.Model.User;
import com.example.progetto_psw.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrdineService ordineService;

    @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED)
    public User registerUser(User user) throws ExistUserException,MethodArgumentNotValidException,IllegalArgumentException {
        if(user.getEmail().isEmpty() || user.getEmail().isBlank() )
            throw new IllegalArgumentException();
        if(userRepo.existsByEmail(user.getEmail()))
            throw new ExistUserException();
        return userRepo.save(user);
    }

    @Transactional (readOnly = true,propagation=Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    @Transactional (readOnly = true, propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
    public User getUser(String email) throws UserNotExistException {
        if(!userRepo.existsByEmail(email))
            throw new UserNotExistException();
        return userRepo.findByEmail(email);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED )
    public void deleteUser(Integer id) throws UserNotExistException, OrdineNotExistsException {

        if(!userRepo.existsById(id)){
            throw new UserNotExistException();
        }

        List<User> users=userRepo.findAll();

        for(User u:users){

            if(u.getId()==id){
               List<Ordine> ordini=u.getOrdini();
               for(Ordine o:ordini){
                   ordineService.eliminaOrdine(o.getId());
               }

            }
            userRepo.deleteById(id);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED )
    public void deleteUser(String email) throws UserNotExistException, OrdineNotExistsException {
        if(!userRepo.existsByEmail(email))
            throw new UserNotExistException();

        User user=userRepo.findByEmail(email);
        List<User> users=userRepo.findAll();

        for(User u:users){

            if(u.getEmail().equals(email)){
                List<Ordine> ordini=u.getOrdini();
                for(Ordine o:ordini){
                    ordineService.eliminaOrdine(o.getId());
                }
            }
            userRepo.deleteByEmail(email);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED)
    public User addAndGetUser(String nome, String cognome,String email) throws ExistUserException {

        if(userRepo.existsByEmail(email)){
            throw new ExistUserException();
        }
        User user=new User(nome,cognome,email);
        return userRepo.save(user);
    }

}
