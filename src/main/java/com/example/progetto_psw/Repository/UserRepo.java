package com.example.progetto_psw.Repository;

import com.example.progetto_psw.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {


    List<User> findByNome(String nome);
    List<User> findByCognome(String cognome);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsById(Integer Id);
    void deleteByEmail(String email);
    Boolean existsByEmailAndPassword(String email, String password);
}
