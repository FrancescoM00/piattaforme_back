package Support.Exceptions;


public class ProductModifiedException extends Exception {

    private String msg="Prodotto modificato";

    public ProductModifiedException(){
        System.out.println(msg);
    }
}
