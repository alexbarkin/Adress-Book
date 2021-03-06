import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.*;

/** This class is responsible for the flow of the program and actually accepting, saving, and processing user inputs. 
  *  It calls two of the other classes, and is called by the driver class. It is also responsible for all of the file IO in the 
  *  program.
  *
  * @author A Barkin
  * @version 1 09.03.15
  */
public class AddressBook extends JPanel implements ActionListener
{
  /*The final int to ensure no more than 50 entries can be in one address book*/
  final int MAX_RECORD = 50;
  /*static int used to record which entry is currently being editted*/
  static int currentRecord = 0;
  /*static final String which holds the header line used for all files to ensure there are no typing errors and that the file is created by my program*/
  static final String HEADER_LINE = "This header line is soooooooo unique and made by Alex Barkin";
  /*PersonRecord instance array list used to create 50 instances of PersonRecord*/
  ArrayList<PersonRecord> pR = new ArrayList<PersonRecord>();
  /*JLabel used to label first name field to help the user*/
  JLabel firstNameL = new JLabel ("First Name:");
  /*JLabel used to label last name field to help the user*/
  JLabel lastNameL = new JLabel ("Last Name: ");
  /*JLabel used to label the phone number field to help the user*/
  JLabel phoneL = new JLabel ("Phone Number: ");
  /*JLabel used to label the email address field to help the user*/
  JLabel emailL = new JLabel ("Email:");
  /*JLabel used to label the the number of entries so the user can keep track*/
  JLabel currentNumL = new JLabel ("Number of Entries: 0");
  /*JLabel used to label the the current entry which the user is on so the user can keep track*/
  JLabel currentEntryL = new JLabel ("Current Entry: 1");
  /*JTextField used to recieve user input for the first name*/
  JTextField firstText = new JTextField ();
  /*JTextField used to recieve user input for the last name*/
  JTextField lastText = new JTextField ();
  /*JTextField used to recieve user input for the phone number*/
  JTextField phoneText = new JTextField ();
  /*JTextField used to recieve user input for the email address*/
  JTextField emailText = new JTextField ();
  /*boolean used to keep track of wether the entries have been editted so the program knows when saving is required*/
  public boolean isSaved = true;
  /*File variable used to choose which file the user wants to access or save*/
  File file;
  
  /** This method is used to update the text fields to contain the new information in the current entry.
    */ 
  public void updateEntries ()
  {
    clearFields ();
    if (currentRecord == -1)
      return;
    currentNumL.setText ("Number of Entries: " + (PersonRecord.numPerson));
    currentEntryL.setText ("Current Entry: " + ((PersonRecord.numPerson == 0) ? (1):currentRecord + 1));
    firstText.setText (pR.get(currentRecord).getFirstName ());
    lastText.setText (pR.get(currentRecord).getLastName ());
    phoneText.setText (pR.get(currentRecord).getPhone ());
    emailText.setText (pR.get(currentRecord).getEmail ());
  }
  
  /** This method is called when the user would like to make a new file. If they have not saved prior to this, then it asks if they would like to save.
    *  It then resets the values for all of the variables, makes a new PersonRecord, and clears all of the fields.
    */ 
  public void neww ()
  {
    if (!isSaved)
      saveQ ();
    pR = new ArrayList<PersonRecord>();
    file = null;
    isSaved = true;
    PersonRecord.resetRecords ();
    currentRecord = 0;
    pR.add(new PersonRecord());
    clearFields ();
    currentEntryL.setText ("Current Entry: " + ((PersonRecord.numPerson == 0) ? (1):currentRecord + 1));
    currentNumL.setText ("Number of Entries: 0");
  }
  
  /** This method is responsible for opening new files when the user selects to do so.
    * If the file hasn't been saved it asks the user if they would like to do so at this time.
    * It then uses JFileChooser to let the user easily select the file they would like to 
    * use with the filter that it must have extension.ahb. It then updates the entries for
    * ther new data. It also catches any IO exception as well as it makes sure that the 
    * file was created by theis program, The first if statement is used to decide if the 
    * user should be asked to save the file. The second is used to ensure that the file
    * extension is correct (meets the filter requirement) The Third is used to ensure 
    * that the file is created by this program, if it isnt then a JOptionPane appears to 
    * tell the user.
    * 
    * <p>
    * <b>Local variables:</b>
    * <p>
    * <b>fC -</b> JFileChooser used to select files from a neat menu that have the proper extension
    * <p>
    * <b>bR -</b> BufferedReader used to read from the selected file to import the data
    */ 
  public void open ()
  {
    if (!isSaved)
      saveQ ();
    JFileChooser fC = new JFileChooser ("..\\files");
    fC.setFileFilter (new FileNameExtensionFilter ("AddressBook (.ahb)", "ahb"));
    fC.setFileSelectionMode (JFileChooser.FILES_ONLY);
    if (fC.showOpenDialog (this) == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        BufferedReader bR = new BufferedReader (new FileReader (fC.getSelectedFile ()));
        if (bR.readLine ().equals (HEADER_LINE))
        {
          int inNum = Integer.parseInt (bR.readLine ());
          pR = new ArrayList<PersonRecord>();
          
          for (int i = 0 ; i < inNum ; i++)
            pR.add(new PersonRecord (bR.readLine (), bR.readLine (), bR.readLine (), bR.readLine ()));
          
          currentRecord = 0;
          isSaved = true;
          file = fC.getSelectedFile ();
          PersonRecord.numPerson = inNum;
          updateEntries ();
        }
        else
          JOptionPane.showMessageDialog (null, "The chosen file was not created by this program or is corrupt!", "Invalid file", JOptionPane.ERROR_MESSAGE);
      }
      catch (IOException e)
      {
        JOptionPane.showMessageDialog (null, "Input Error...sorry!", "IO Exception", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  
  /** This method is responsible for saving files. If the file doesn't have a name, then it will call save as 
    * This will output telling the user when the file has been saved, or it will tell the user that an error 
    * occured. This method also sets the save button to disabled because it was just saved. The if statement
    * is used to ask the user to enter a file name and location if there is not one.
    * 
    * <p>
    * <b>Local variables:</b>
    * <p>
    * <b>pW -</b> PrintWriter used to write all of the data to the file.
    */ 
  public void save ()
  {
    if (checkFields ())
      return;
    if (file == null)
      saveAs ();
    else
    {
      try
      {
        PrintWriter pW = new PrintWriter (new FileWriter (file));
        pW.println (HEADER_LINE);
        pW.println (PersonRecord.numPerson);
        for (int i = 0 ; i < PersonRecord.numPerson ; i++)
        {
          pW.println (pR.get(i).getFirstName ());
          pW.println (pR.get(i).getLastName ());
          pW.println (pR.get(i).getPhone ());
          pW.println (pR.get(i).getEmail ());
        }
        pW.close ();
        isSaved = true;
        JOptionPane.showMessageDialog (null, "AddressBook " + file.getName () + " has been saved.", "Saved", JOptionPane.INFORMATION_MESSAGE);
      }
      catch (IOException e)
      {
        JOptionPane.showMessageDialog (null, "Output Error...sorry!", "IO Exception", JOptionPane.ERROR_MESSAGE);
      }
    }
    DataBaseApp.save.setEnabled (false);
  }
  
  /** This method is responsible for saving files with a new name. You can also choose the location
    * with the JFileChooser. The if statement is used to only read from the file if it has the proper extension.
    * 
    * <p>
    * <b>Local variables:</b>
    * <p>
    * <b>fS -</b> JFileChooser used to select files from a neat menu and automatically add the extension
    * <p>
    * <b>loc -</b> integer used for the location of the file
    * <p>
    * <b>ask -</b> integer used to temporarily store the response of the user to the yes, no or cancel JOptionPane
    */ 
  public void saveAs ()
  {
    if (checkFields ())
      return;
    JFileChooser fS = new JFileChooser ("..\\files");
    fS.setFileFilter (new FileNameExtensionFilter ("AddressBook (.ahb)", "ahb"));
    fS.setFileSelectionMode (JFileChooser.FILES_ONLY);
    int loc = fS.showSaveDialog (this);
    if (loc == JFileChooser.APPROVE_OPTION)
    {
      file = new File (fS.getSelectedFile ().toString () + ((!fS.getSelectedFile ().toString ().endsWith (".ahb")) ? ".ahb":""));
      if (file.exists())
      {
        int ask = JOptionPane.showConfirmDialog (null, "This file already exists, would you like to overwrite it?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ask != JOptionPane.YES_OPTION)
          return;
      }
      save ();
    }
  }
  
  /** This method is responsible for using a yes no or cancel JOptionPane to ask if the user would like to save before 
    * they do anything to overwrite unsaved data in the files. The if statement is used to only call the save method if yes is selected. 
    * <p>
    * <b>Local variables:</b>
    * <p>
    * <b>ask -</b> integer used to temporarily store the response of the user to the yes, no or cancel JOptionPane
    */ 
  public void saveQ ()
  {
    int ask = JOptionPane.showConfirmDialog (null, "Looks like someone forgot to save to the file, would you like to now?", "Save??", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (ask == JOptionPane.YES_OPTION)
      save ();
  }
  
  /** This method is responsible for using a yes no or cancel JOptionPane to ask if the user would like to save before 
    * they do anything to overwrite unsaved data in the fields. Th if statement is used to only call the saveEntry method if yes is selected.
    * <p>
    * <b>Local variables:</b>
    * <p>
    * <b>ask -</b> integer used to temporarily store the response of the user to the yes, no or cancel JOptionPane
    */ 
  public void saveQ2 ()
  {
    int ask = JOptionPane.showConfirmDialog (null, "Looks like someone forgot to save to memory, would you like to now?", "Save??", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (ask == JOptionPane.YES_OPTION)
      saveEntry ();
  }
  
  /** This method adds a new blank entry but it only does so if the user input for the last entry is valid and there are not 50 records.
    * It also clears the fields for the new entry and an empty instance of PersonRecord. It then sets the isSaved variable to false because
    * changes have been made,
    */ 
  private void addNewRecord ()
  {
    if (PersonRecord.numPerson == MAX_RECORD-1)
    {
      JOptionPane.showMessageDialog (null, "Error: You have hit the max number of records (50)!", "Max records hit!", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    if (!(pR.get(currentRecord).getFirstName ().equals (firstText.getText ()) &&
          pR.get(currentRecord).getLastName ().equals (lastText.getText ()) &&
          pR.get(currentRecord).getPhone ().equals (phoneText.getText ()) &&
          pR.get(currentRecord).getEmail ().equals (emailText.getText ()))) 
      saveQ2();
    if (checkFields ())
      return;
    firstText.setText ("");
    lastText.setText ("");
    phoneText.setText ("");
    emailText.setText ("");
    pR.add(new PersonRecord (firstText.getText (), lastText.getText (), phoneText.getText (), emailText.getText ()));
    currentRecord = PersonRecord.numPerson - 1;
    updateEntries ();
    isSaved = false;
  }
  
  /** This method is responsible for setting the values in pR as the current values in the fields. It also sets isSaved to false 
    * because there have been changes to the PersonRecord not saved to the file.
    */ 
  private void updateRecord ()
  {
    pR.get(currentRecord).setFirstName (firstText.getText ());
    pR.get(currentRecord).setLastName (lastText.getText ());
    pR.get(currentRecord).setPhone ((phoneText.getText ()));
    pR.get(currentRecord).setEmail (emailText.getText ());
    isSaved = false;
  }
  
  /** This method is responsible for just clearing the text fields to be blank.
    */ 
  private void clearFields ()
  {
    emailText.setText ("");
    firstText.setText ("");
    lastText.setText ("");
    phoneText.setText ("");
  }
  
  /** This method is responsible for checking user input and verifying that it is acceptible to save. 
    *  This method also has 3 error messages for different errors which may occur in the user's input
    * 
    * <p>
    * <b>Local variables:</b>
    * <p>
    * <b>good -</b> boolean used to say whether the data is good or not.
    * @return returns the boolean which is used to say whether the data is good or not.
    */ 
  private boolean checkFields ()
  {
    boolean good = false;
    if (!DataCheck.isCheckPhone (phoneText.getText ()) && !phoneText.getText ().equals (""))
    {
      JOptionPane.showMessageDialog (null, "Error: Invalid phone number. Please enter a valid phone number (To find out more about valid phone numbers check out the help menu)!", "Invalid phone number.", JOptionPane.ERROR_MESSAGE);
      phoneText.setText ("");
      phoneText.requestFocus ();
      good = true;
    }
    if (DataCheck.isCheckEmail (emailText.getText ()) == false && !emailText.getText ().equals (""))
    {
      JOptionPane.showMessageDialog (null, "Error: Invalid email address! Please enter a valid email address (To find out more about valid emails check out the help menu)!", "Invalid email Address.", JOptionPane.ERROR_MESSAGE);
      emailText.setText ("");
      emailText.requestFocus ();
      good = true;
    }
    if (!good&&firstText.getText ().equals ("") && lastText.getText ().equals ("") && phoneText.getText ().equals ("") && emailText.getText ().equals (""))
    {
      JOptionPane.showMessageDialog (null, "Error: Please enter at least one field.", "Error: No Information", JOptionPane.ERROR_MESSAGE);
      good = true;
    }
    return good;
  }
  
  /** This constructor is responsible for setting up all of the fields and buttons as well as it controls the layout for the interactions with the user.
    * The layout of the panel is also set to border layout
    * 
    * <p>
    * <b>Local variables:</b>
    * <p>
    * <b>nextRecord -</b>JButton used for advancing to the next record
    * <p>
    * <b>previousRecord -</b> JButton used for going to the last record
    * <p>
    * <b>newRecord -</b> JButton used to make a new blank record
    * <p>
    * <b>modifyRecord -</b> JButton used to save the data into the PersonRecord
    * <p>
    * <b>emptyRecord -</b> JButton used to clear the current text in the fields 
    * <p>
    * <b>firstNameL -</b> JLabel used to label the first name text field
    * <p>
    * <b>lastNameL -</b> JLabel used to label the last name text field
    * <p>
    * <b>phoneL -</b> JLabel used to label the phone text field
    * <p>
    * <b>emailL -</b> JLabel used to label the email text field
    * <p>
    * <b>blueBox -</b> Box used to hold all of the textfields and labels
    * <p>
    * <b>redBox -</b> Box used to hold the two buttons for the top of the blueBox
    */ 
  public AddressBook ()
  {
    pR.add(new PersonRecord ());
    
    JButton nextRecord = new JButton ("Next Record");
    JButton previousRecord = new JButton ("Last Record");
    JButton newRecord = new JButton ("New Record");
    JButton modifyRecord = new JButton ("Save Entry");
    JButton removeRecord = new JButton ("Remove Entry");
    
    newRecord.addActionListener (this);
    previousRecord.addActionListener (this);
    nextRecord.addActionListener (this);
    modifyRecord.addActionListener (this);
    removeRecord.addActionListener (this);
    
    firstText.setPreferredSize (new Dimension (50, 14));
    lastText.setPreferredSize (new Dimension (50, 14));
    phoneText.setPreferredSize (new Dimension (50, 14));
    emailText.setPreferredSize (new Dimension (50, 14));
    
    JLabel firstNameL = new JLabel ("First Name: ");
    JLabel lastNameL = new JLabel ("Last Name: ");
    JLabel phoneL = new JLabel ("Phone Number: ");
    JLabel emailL = new JLabel ("Email Address: ");
    
    firstText.add (firstNameL);
    lastText.add (lastNameL);
    phoneText.add (phoneL);
    emailText.add (emailL);
    setLayout (new BorderLayout ());
    
    Box blueBox = new Box (BoxLayout.Y_AXIS);
    blueBox.add (firstNameL, BorderLayout.CENTER);
    blueBox.add (firstText, BorderLayout.CENTER);
    blueBox.add (lastNameL, BorderLayout.CENTER);
    blueBox.add (lastText, BorderLayout.CENTER);
    blueBox.add (phoneL, BorderLayout.CENTER);
    blueBox.add (phoneText, BorderLayout.CENTER);
    blueBox.add (emailL, BorderLayout.CENTER);
    blueBox.add (emailText, BorderLayout.CENTER);
    blueBox.add (currentNumL, BorderLayout.CENTER);
    blueBox.add (currentEntryL, BorderLayout.CENTER);
    Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    newRecord.setPreferredSize (new Dimension ((int)screenSize.getWidth(), 44));
    removeRecord.setPreferredSize (new Dimension ((int)screenSize.getWidth(), 44));
    
    Box redBox = new Box (BoxLayout.X_AXIS);
    redBox.add (newRecord);
    redBox.add (removeRecord);
    
    add (previousRecord, BorderLayout.WEST);
    add (redBox, BorderLayout.NORTH);
    add (blueBox, BorderLayout.CENTER);
    add (nextRecord, BorderLayout.EAST);
    add (modifyRecord, BorderLayout.SOUTH);
  }
  
  /** This method is responsible for saving the data in the input fields to the current PersonRecord.
    * It also formats the sata in the fields themselves. The if statement is used to create a new 
    * PersonRecord if there are no entries. The method also sets isSaved to false because changes 
    * have been made to the PersonRecord.
    * 
    */ 
  private void saveEntry ()
  {
    if (checkFields ())
      return;
    pR.get(currentRecord).setFirstName (firstText.getText ());
    pR.get(currentRecord).setLastName (lastText.getText ());
    pR.get(currentRecord).setPhone (phoneText.getText ());
    pR.get(currentRecord).setEmail (emailText.getText ());
    firstText.setText (pR.get(currentRecord).formatName (firstText.getText ()));
    lastText.setText (pR.get(currentRecord).formatName (lastText.getText ()));
    phoneText.setText (pR.get(currentRecord).formatPhone (phoneText.getText ()));
    isSaved = false;
  }
  
  
  /** Performs a given action depending on which event occurs (user input). All if statements are used to execute different commands based 
    * on which action event happened. It is also responsible for setting whether or not the save button works,
    * @param ae An ActionEvent reference which identifies the source of the command.
    */ 
  public void actionPerformed (ActionEvent ae)
  {
    if (ae.getActionCommand ().equals ("Remove Entry"))
    {
      clearFields ();
      pR.remove(currentRecord);
      currentRecord--;
      updateEntries ();
    }
    else if (ae.getActionCommand ().equals ("New Record"))
    {
      addNewRecord ();
    }
    else if (ae.getActionCommand ().equals ("Save Entry"))
    {
      saveEntry ();
    }
    else
    {
      if (PersonRecord.numPerson <=1)
      {
        JOptionPane.showMessageDialog (null, "You have no records, thus you must create one!", "No Records!", JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (ae.getActionCommand ().equals ("Last Record"))
        prevRec ();
      else
        nextRec ();
      updateEntries ();
      updateRecord ();
    }
    if (isSaved)
      DataBaseApp.save.setEnabled (false);
    else
      DataBaseApp.save.setEnabled (true);
  }
  
  /** This method is responsible for changing the current record to the next in the array list or the first if it is at the end. 
    * It will also ask the user if they would like to change any unsaved data in the fields before they continue.
    * 
    */ 
  public void nextRec ()
  {
    if (pR.get(currentRecord).getFirstName ().equals (firstText.getText ()) &&
        pR.get(currentRecord).getLastName ().equals (lastText.getText ()) &&
        pR.get(currentRecord).getPhone ().equals (phoneText.getText ()) &&
        pR.get(currentRecord).getEmail ().equals (emailText.getText ()))
    {
      if (checkFields())
        return;
      currentRecord = (PersonRecord.numPerson == currentRecord + 1) ? (0):
        (currentRecord + 1);
      clearFields ();
    }
    else
    {
      saveQ2 ();
      updateEntries ();
      updateRecord ();
    }
  }
  
  /** This method is responsible for changing the current record to the previous in the array list or the last if it is at the start. 
    * It will also ask the user if they would like to change any unsaved data in the fields before they continue.
    * 
    */ 
  public void prevRec ()
  {
    if (pR.get(currentRecord).getFirstName ().equals (firstText.getText ()) &&
        pR.get(currentRecord).getLastName ().equals (lastText.getText ()) &&
        pR.get(currentRecord).getPhone ().equals (phoneText.getText ()) &&
        pR.get(currentRecord).getEmail ().equals (emailText.getText ()))
    {
      if (checkFields())
        return;
      currentRecord = (currentRecord == 0) ? (PersonRecord.numPerson - 1):
        (currentRecord - 1);
      clearFields ();
    }
    else
    {
      saveQ2 ();
      updateEntries ();
      updateRecord ();
    }
  }
}
