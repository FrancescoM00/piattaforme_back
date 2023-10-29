package Support.Exceptions;

public class ProductNotAvailableException extends Exception {

    private String msg="Il prodotto e' esaurito";

    public ProductNotAvailableException(){
        System.out.println(msg);
    }
}
