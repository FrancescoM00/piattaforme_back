package Support.Exceptions;

public class ExistProductException extends Exception {

    private String msg="Prodotto gia' esistente";

    public ExistProductException(){
        System.out.println(msg);
    }
}
