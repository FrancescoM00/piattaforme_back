package Support.Exceptions;

public class CarrelloVuotoException extends  Exception {

    public CarrelloVuotoException(){
        System.out.println("Inserisci almeno un prodotto nel carrello");
    }
}
