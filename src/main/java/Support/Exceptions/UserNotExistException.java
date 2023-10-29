package Support.Exceptions;

public class UserNotExistException extends Exception{

    private String msg="L'utente selezionato non esiste";

    public UserNotExistException(){
        System.out.println(msg);
    }
}
