/**This class is responsible for holding the data entered for the current record and formattig said data. 
  * It also keeps track of the number of entries that have been entered.
  * 
  * @author A Barkin 
  * @version 1 09.03.15
  */ 
public class PersonRecord
{
  /*The Static int which keeps track of the number of entries that have been entered during the current entry session*/
  static int numPerson = 0;  
  /*The private strings which hle the current entry data*/
  private String first = "", last = "", email = "", phone = "";
  
  /** This is the full constructor which is responsible for entering the data to the private variables and increases the amount of entries by one.
    * 
    * @param first A String used to hold the first name of the current entry
    * @param last A String used to hold the last name of the current entry
    *  @param phone A String used to hold the phone number of the current entry
    *  @param email A String used to hold the email of the current entry
    */ 
  public PersonRecord (String first, String last, String phone, String email)
  {
    this.first = formatName(first);
    this.last = formatName(last); 
    this.email = email; 
    this.phone =formatPhone(phone);
    numPerson++;
  }
  
  /** This is the empty constructor which is responsible for gaining access to methods in this class as well as increasing the number of entries.
    */ 
  public PersonRecord()
  {
    numPerson++;
  }
  
  /** This method resets the number of records if a new entry session is started
    */
  public static void resetRecords()
  {
    numPerson = 0; 
  }
  
  /** This is the constructor which is responsible for entering the data without a phone number to the current entry.
    * 
    * @param first A String used to hold the first name of the current entry
    * @param last A String used to hold the last name of the current entry
    *  @param email A String used to hold the email of the current entry
    */ 
  public PersonRecord (String first, String last, String email)
  {
    this (first, last, "", email);
  }
  
  /** This is the constructor which is responsible for entering the data without an email address to the current entry.
    * 
    * @param first A String used to hold the first name of the current entry
    * @param last A String used to hold the last name of the current entry
    * @param phone A String used to hold the phone number of the current entry
    * @param isPhone A boolean used to differentiate between missing the user's phone number versus missing their email address. This says that they have the phone number when called.
    */ 
  public PersonRecord (String first, String last, String phone, boolean isPhone)
  {
    this (first, last, phone, "");
  }
  
  /** This is the constructor which is responsible for just changing the user's last name. It also increases the number of entries.
    * 
    * @param newLastName A String used to pass a new last name in to replace the old one.
    */ 
  public PersonRecord (String newLastName)
  {
    last = formatName(newLastName);
    numPerson++;
  }
  
  /** This is the method used to set the last name of the current entry to a new last name.
    * 
    * @param newLastName A String used to pass a new last name in to replace the old one.
    */ 
  public void setLastName(String newLastName)
  {
    last = formatName(newLastName);
  }
  
  /** This is the method used to set the first name of the current entry to a new first name.
    * 
    * @param newFirstName A String used to pass a new first name in to replace the old one.
    */ 
  public void setFirstName(String newFirstName)
  {
    first = formatName(newFirstName);
  }
  
  /** This is the method used to set the phone number of the current entry to a new phone number.
    * 
    * @param newPhone A String used to pass a new phone number in to replace the old one.
    */ 
  public void setPhone(String newPhone)
  {
    phone = formatPhone (newPhone);
  }
  
  /** This is the method used to set the email address of the current entry to a new email address.
    * 
    * @param newEmail A String used to pass a new email address in to replace the old one.
    */ 
  public void setEmail(String newEmail)
  {
    email = newEmail;
  }
  
  /** This is the String return method used to allow other classes access to the first name variable. It simply returns the variable.\
    * @return returns the current first name 
    */ 
  public String getFirstName()
  {
    return first;
  }
  
  /** This is the String return method used to allow other classes access to the last name variable. It simply returns the variable.
    * @return returns the current last name 
    */ 
  public String getLastName()
  {
    return last;
  }
  
  /** This is the String return method used to allow other classes access to the email address variable. It simply returns the variable.
    *  @return returns the current email address 
    */ 
  public String getEmail()
  {
    return email;
  }
  
  /** This is the String return method used to allow other classes access to the phone number variable. It simply returns the variable.
    *  @return returns the current phone number
    */ 
  public String getPhone()
  {
    return phone;
  }
  
  /** This is the method used to format the user input for first and last name. It makes the first letter capital and all other letters lowercase.
    * 
    * <p>
    * <b>Local variables:</b>
    * <p>
    * <b>name3 -</b> String which holds the new name with only the good and formatted elements
    * <p>
    * <b>i -</b> String which keeps track of the for each loop and holds the element from the array at each location
    * 
    * @param names A String used to pass the name to be formatted into the method.
    *  @return returns the name sent in with a capital first letter and all other letters lower case
    */ 
  public String formatName(String names)
  {
    String name3 = "";
    for (String i:names.split(" "))
    {
      if (!i.equals(""))
        name3+=(""+i.charAt(0)).toUpperCase() + i.substring(1).toLowerCase() + " ";
    }
    return name3;
  }
  
  /** This is the method used to format the user input for their phone number. It inserts a dash after 3 numbers and then again after another 3.
    * 
    * <p>
    * <b>Local variables:</b>
    * <p>
    * <b>tempStr -</b> String which holds only the digits of the user input
    * <p>
    * <b>i -</b> integer which keeps track of the for loop to check each character and only adds digits to the tempStr
    * 
    * @param phone A String used to pass the phone number to be formatted into the method.
    * @return returns the phone number formatted with dashes
    */ 
  public String formatPhone (String phone)
  {
    String tempStr = "";
    for (int i = 0 ; i < phone.length(); i++)
      tempStr+=(phone.charAt(i) != ' ' && phone.charAt(i) != '-')? phone.charAt(i):"";
    return (tempStr.equals(""))?"":(tempStr.substring(0, 3) + "-" + tempStr.substring(3, 6) + "-" + tempStr.substring(6));
  }
}