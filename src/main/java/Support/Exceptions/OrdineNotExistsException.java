package Support.Exceptions;

public class OrdineNotExistsException extends Exception {

    private String msg="Ordine inesistente";

    public OrdineNotExistsException(){
        System.out.println(msg);
    }
}
