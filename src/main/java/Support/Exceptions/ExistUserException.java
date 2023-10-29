package Support.Exceptions;

public class ExistUserException extends Exception {

     private String msg="Email gia' utilizzata";

     public ExistUserException(){
         System.out.println(msg);
     }

}
