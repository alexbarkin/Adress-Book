/** This class is used to verify wether both the phone and or email data entered by the user is correct.
  * 
  * @author A Barkin 
  * @version 1 09.03.15
  */ 
public class DataCheck
{
  /** This method is used do verify that the phone number entered by the user is correct or not.
    * 
    * @param phone A String used to check the specific phone number which is currently being passed into the method.
    * @return returns true if the phone number is valid false if not
    */ 
  public static boolean isCheckPhone (String phone)
  {
    return phone.matches ("\\d{3}[ ]\\d{3}[ ]\\d{4}|\\d{3}[-]\\d{3}[-]\\d{4}|\\d{10}");
  }
  
  /** This method is used do verify that the email address entered by the user is correct or not.
    * 
    * @param email A String used to check the specific email address which is currently being passed into the method.
    * @return returns true if the email address is valid false if not
    */ 
  public static boolean isCheckEmail (String email)
  {
    return email.matches("[0-9a-zA-Z]+[0-9a-zA-Z!#$%&'*+-/=?^_`{|}~\\.]*@([0-9a-zA-Z\\-]+\\.)+[0-9a-zA-Z]+");
  }
}
