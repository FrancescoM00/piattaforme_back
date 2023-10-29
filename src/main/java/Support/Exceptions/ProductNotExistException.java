package Support.Exceptions;

public class ProductNotExistException extends Exception {

    private String msg="Il prodotto non esiste";

    public ProductNotExistException(){
        System.out.println(msg);
    }
}
