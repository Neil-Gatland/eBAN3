package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class NostroPayGroupBANBean extends BANBean
{
  private String sql;
  protected Vector mandatory=new Vector();
  private Hashtable ScreenData=new Hashtable();
  private int BEDays, BEMonths, BEYears;
  private String Payment_Group_Name;
  //private int Payment_Group_Id;
  private String Customer_Name;
  private String Customer_Surname;
  private String Location_City;
  private String Location_Country;
  private String Customer_Contact_Number;
  private String Customer_Email;

  public NostroPayGroupBANBean ()
  {
    Payment_Group_Name="";
    Payment_Group_Id=0;
    Customer_Name="";
    Customer_Surname="";
    Location_City="";
    Location_Country="";
    Customer_Contact_Number="";
    Customer_Email="";
    mandatory.clear();
    mandatory.addElement("Payment_Group_Name");
    mandatory.addElement("Customer_Name");
    mandatory.addElement("Customer_Surname");
    mandatory.addElement("Customer_Email");
    mandatory.addElement("Existing_Account");
    mandatory.addElement("Location_City");
    mandatory.addElement("Location_Country");
    errored.addElement("");
  }

  public void Reset()
  {
    super.Reset();
    Payment_Group_Name="";
    Payment_Group_Id=0;
    Customer_Name="";
    Customer_Surname="";
    Location_City="";
    Location_Country="";
    Customer_Contact_Number="";
    Customer_Email="";
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    Required_BAN_Effective_Dateh = new java.sql.Date(new java.util.Date().getTime());
    setErrored("clear");
  }
/************************************************************************************************/
public String getPaymentGroupName()
{
  return Payment_Group_Name;
}
/************************************************************************************************/
public void setPaymentGroupName(String value)
{
  Payment_Group_Name = value;
}
/************************************************************************************************/
public String getCustomerName()
{
  return Customer_Name;
}
/************************************************************************************************/
public void setCustomerName(String value)
{
  Customer_Name = value;
}
/************************************************************************************************/
public String getCustomerSurname()
{
  return Customer_Surname;
}
/************************************************************************************************/
public void setCustomerSurname(String value)
{
  Customer_Surname = value;
}
/************************************************************************************************/
public String getLocationCity()
{
  return Location_City;
}
/************************************************************************************************/
public void setLocationCity(String value)
{
  Location_City = value;
}
/************************************************************************************************/
public String getLocationCountry()
{
  return Location_Country;
}
/************************************************************************************************/
public void setLocationCountry(String value)
{
  Location_Country = value;
}
/************************************************************************************************/
public String getCustomerContactNumber()
{
  return Customer_Contact_Number;
}
/************************************************************************************************/
public void setCustomerContactNumber(String value)
{
  Customer_Contact_Number = value;
}
/************************************************************************************************/
public String getCustomerEmail()
{
  return Customer_Email;
}
/************************************************************************************************/
public void setCustomerEmail(String value)
{
  Customer_Email = value;
}
/************************************************************************************************/
public String getPaymentGroupId()
{
  if (Payment_Group_Id == 0)
  {
    getNewPaymentGroupId();
  }
  return Integer.toString(Payment_Group_Id);
}
/************************************************************************************************/
  public void getNewPaymentGroupId()
  {

    try
    {
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Get_Pay_Group_Sequence ()}";

        cstmt = DBA.Conn.prepareCall(SQL);

        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          Payment_Group_Id=RS.getInt(1);
        }
      }
    }
    catch(Exception se)
    {
    }
    finally
    {
      close();
    }
  }
/************************************************************************************************/
  private boolean hasAccounts()
  {
    boolean hasAccounts = false;
    try{
      SQL = "SELECT Account_Id from eban..Payment_Group_Account " +
       "WHERE Payment_Group_Id = " + Payment_Group_Id + " ";
      if (DBA.Connect(PREPARE,P5))
      {

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
        RS = Stmt.getResultSet();
        if (RS.next())
        {
          hasAccounts = true;
        }
        else
        {
          hasAccounts = false;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      hasAccounts = false;
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
      hasAccounts = false;
    }//message set in underlying code
    finally
    {
      close();
      return hasAccounts;
    }
  }
/************************************************************************************************/
  public boolean addAccount(String Account_Id)
  {
    boolean addAccount = false;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "INSERT INTO eban..Payment_Group_Account " +
         "(Payment_Group_Id, Account_Id) VALUES (" + Payment_Group_Id + ", " +
         "'" + Account_Id +"') ";

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
	addAccount = true;
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return addAccount;
    }
  }
/************************************************************************************************/
  public boolean removeAccount(String Account_Id)
  {
    boolean removeAccount = false;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "DELETE FROM eban..Payment_Group_Account " +
         "WHERE Payment_Group_Id = " + Payment_Group_Id + " " +
         "AND Account_Id = '" + Account_Id +"' ";

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
	removeAccount = true;
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return removeAccount;
    }
  }
  /*******************************************************************************/
  public String getClass(String Item)
  {
          if (!errored.isEmpty())
          {
            if (errored.contains(Item))
              return "errored";
            else
            {
              if (mandatory.contains(Item))
                return "mandatory";
              else
                return "optional";
            }
          }
          else
          {
            if (mandatory.contains(Item))
              return "mandatory";
            else
              return "optional";
          }
  }
/************************************************************************************************/
  public String getMode(String FieldName)
  {//This defines whether or not a field is enterable

    if (FieldName.compareTo("Unused_Global_Customer") ==0)
    {
      if (Mode.compareTo("Amend") == 0)
	  return "READONLY";
      else
	  return InputMode;
    }
    if (action.compareTo("Authorise") == 0)
    {
      return "READONLY";
    }
    if ((FieldName.compareTo("BAN_Summary") ==0) ||
	    (FieldName.compareTo("BAN_Reason") ==0) ||
	    (FieldName.compareTo("Effective_Date") ==0))
    {
      if ((action.compareTo("View") !=0 ) &&
	  (action.compareTo("History") !=0 ) &&
	  (action.compareTo("Authorise") !=0 ))
	  return "";
      else
        return InputMode;
    }
    else
    {
      return InputMode;
    }
  }
/*******************************************************************************/
 public boolean getPaymentGroup()
 {
    boolean getPaymentGroup = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_Nostro_Payment_Group ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setInt(1,Payment_Group_Id);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    Payment_Group_Name=(RS.getString(1));
	    Customer_Name=(RS.getString(2));
	    Customer_Surname=(RS.getString(3));
	    Customer_Contact_Number=(RS.getString(4));
	    Customer_Email=(RS.getString(5));
	    Location_City=(RS.getString(6));
	    Location_City=(RS.getString(7));
            getPaymentGroup = true;
	  }
	}
	catch(java.sql.SQLException se)
        {
	  Message=se.getMessage();
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return getPaymentGroup;
    }
 }
/************************************************************************************************/
  public boolean createPaymentGroupBAN()
  {
    boolean createPaymentGroupBAN = false;
    try{
	  if (DBA.Connect(WRITE,P5))
	  {
            SQL = "{call eban..Create_Nostro_Pay_Group_BAN " +
              "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

            cstmt = DBA.Conn.prepareCall(SQL);
            cstmt.setString(1,banStatus);
            cstmt.setString(2,Global_Customer_Id);
            cstmt.setString(3,BAN_Summary);
            cstmt.setDate(4,Required_BAN_Effective_Dateh);
            cstmt.setString(5,Mode);
            cstmt.setString(6,banCreatedBy);
            cstmt.setInt(7,Payment_Group_Id);
            cstmt.setString(8,Payment_Group_Name);
            cstmt.setString(9,Customer_Name);
            cstmt.setString(10,Customer_Surname);
            cstmt.setString(11,Customer_Contact_Number);
            cstmt.setString(12,Customer_Email);
            cstmt.setString(13,Location_City);
            cstmt.setString(14,Location_Country);

	    try
	    {
	      cstmt.execute();
	      RS = cstmt.getResultSet();
	      if (RS.next())
	      {
	        banIdentifier=RS.getString(1);
                createPaymentGroupBAN = true;
        	Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
	      }
	    }
	    catch(java.sql.SQLException se)
	    {
	      Message=se.getMessage();
	    }
	  }
	  else
	  { //Failed to connect - message set in underlying code
	    Message=DBA.Message;
	  }
	}
	catch(java.sql.SQLException se)
	{
	  Message=se.getMessage();
	}
        catch(java.lang.NullPointerException se)
        {
          Message="<font color=red><b>"+se.getMessage();
        }//message set in underlying code
        finally
        {
          close();
          return createPaymentGroupBAN;
        }
  }
/*******************************************************************************/
 public boolean getPaymentGroupBAN()
 {
    boolean getPaymentGroupBAN = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_Nostro_Payment_Group_BAN ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    banStatus=(RS.getString(1));
	    Global_Customer_Id=RS.getString(2);
	    Required_BAN_Effective_Dateh=RS.getDate(3);
   	    BAN_Summary=RS.getString(4);
	    rejectReason=RS.getString(5);
	    Mode=(RS.getString(6));
	    Payment_Group_Id=(RS.getInt(7));
	    Payment_Group_Name=(RS.getString(8));
	    Customer_Name=(RS.getString(9));
	    Customer_Surname=(RS.getString(10));
	    Customer_Contact_Number=(RS.getString(11));
	    Customer_Email=(RS.getString(12));
	    Location_City=(RS.getString(13));
	    Location_Country=(RS.getString(14));
            setDates();
            getPaymentGroupBAN = true;
	  }
	}
	catch(java.sql.SQLException se)
        {
	  Message=se.getMessage();
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return getPaymentGroupBAN;
    }
 }
/************************************************************************************************/
  public boolean AuthorisePaymentGroupBAN()
  {
    boolean AuthorisePaymentGroupBAN = false;
    try
    {
      if (canAuthorise())
      {
        if (DBA.Connect(WRITE,P5))
        {
          SQL = "{call eban..Implement_Nostro_Pay_Group_BAN ";
          SQL += "(?,?,?,?,?,?,?,?,?,?,?,?)}";

          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,banIdentifier);
          cstmt.setString(2,Global_Customer_Id);
          cstmt.setString(3,Mode);
          cstmt.setString(4,userId);
          cstmt.setInt(5,Payment_Group_Id);
          cstmt.setString(6,Payment_Group_Name);
          cstmt.setString(7,Customer_Name);
          cstmt.setString(8,Customer_Surname);
          cstmt.setString(9,Customer_Contact_Number);
          cstmt.setString(10,Customer_Email);
          cstmt.setString(11,Location_City);
          cstmt.setString(12,Location_Country);

          cstmt.execute();
          AuthorisePaymentGroupBAN = true;
          Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" "+action+
            (action.endsWith("e")?"d":"ed");
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return AuthorisePaymentGroupBAN;
    }
  }
/************************************************************************************************/
  public boolean updatePaymentGroupBAN()
  {
    boolean updatePaymentGroupBAN = false;
    int rowcount=0;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Update_Nostro_Pay_Group_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
        cstmt.setString(2,banStatus);
	cstmt.setString(3,Global_Customer_Id);
	cstmt.setString(4,BAN_Summary);
	cstmt.setDate(5,Required_BAN_Effective_Dateh);
	cstmt.setInt(6,Payment_Group_Id);
	cstmt.setString(7,Payment_Group_Name);
	cstmt.setString(8,Customer_Name);
	cstmt.setString(9,Customer_Surname);
	cstmt.setString(10,Customer_Contact_Number);
	cstmt.setString(11,Customer_Email);
	cstmt.setString(12,userId);
	cstmt.setString(13,rejectReason);
	cstmt.setString(14,Location_City);
	cstmt.setString(15,Location_Country);

        cstmt.execute();
        rowcount=cstmt.getUpdateCount();
	if (rowcount != 1)
	{
	  Message="Error! - "+Integer.toString(rowcount) + " Rows Updated";
	}
        else
        {
          updatePaymentGroupBAN = true;
          Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" "+action+
            (action.endsWith("e")?"d":"ed");
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      closeupdate();
      return updatePaymentGroupBAN;
    }
  }
/******************************************************************************/
  public boolean isValid(String ButtonPressed)
  {
	Enumeration FormFields=mandatory.elements();
	setErrored("clear");
        Message = "<font color=red><b>";
	String FormField="";
	String FieldName;
	String value="";

	if ((ButtonPressed.startsWith("Submit") ||
          (ButtonPressed.startsWith("Save Draft"))) &&
          (Mode.compareTo("Cease") !=0))
	{
	  while (FormFields.hasMoreElements())
	  {
	    FieldName=(String)FormFields.nextElement();
            if (FieldName.equals("Existing_Account"))
            {
              if (!hasAccounts())
                setErrored(FieldName);
            }
            else
            {
              FormField=SU.isNull((String)ScreenData.get(FieldName),"");
              if(FormField.compareTo("") == 0)
              {
                setErrored(FieldName);
              }
            }
	  }
        }

	if ((!errored.isEmpty()) && (errored.size() > 0))
	{
	    Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
	    return false;
	}
	else
	  return true;
  }
/*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.compareTo("BANEffectiveDateh")==0)
    {
     setRequired_BAN_Effective_Date(value);
     ScreenData.put("BANEffectiveDateh",value);
    }
    else if (name.compareTo("BANEffectiveDateDay")==0)
    {
     setBANEffectiveDateDays(value);
     ScreenData.put("BANEffectiveDateDay",value);
    }
    else if (name.compareTo("BANEffectiveDateMonth")==0)
    {
     setBANEffectiveDateMonths(value);
     ScreenData.put("BANEffectiveDateMonth",value);
    }
    else if (name.compareTo("BANEffectiveDateYear")==0)
    {
     setBANEffectiveDateYears(value);
     ScreenData.put("BANEffectiveDateYear",value);
    }
    else if (name.compareTo("BAN_Summary")==0)
    {
      setBAN_Summary(value);
      ScreenData.put("BAN_Summary",value);
    }
    else if (name.compareTo("BAN_Reason")==0)
    {
      setBAN_Reason(value);
      ScreenData.put("BAN_Reason",value);
    }
    else if (name.compareTo("Payment_Group_Name")==0)
    {
      setPaymentGroupName(value);
      ScreenData.put("Payment_Group_Name",value);
    }
    else if (name.compareTo("Customer_Name")==0)
    {
      setCustomerName(value);
      ScreenData.put("Customer_Name",value);
    }
    else if (name.compareTo("Customer_Surname")==0)
    {
      setCustomerSurname(value);
      ScreenData.put("Customer_Surname",value);
    }
    else if (name.compareTo("Location_City")==0)
    {
      setLocationCity(value);
      ScreenData.put("Location_City",value);
    }
    else if (name.compareTo("Location_Country")==0)
    {
      setLocationCountry(value);
      ScreenData.put("Location_Country",value);
    }
    else if (name.compareTo("Customer_Contact_Number")==0)
    {
      setCustomerContactNumber(value);
    }
    else if (name.compareTo("Customer_Email")==0)
    {
      setCustomerEmail(value);
      ScreenData.put("Customer_Email",value);
    }
  }
/************************************************************************************************/
  public int getBANEffectiveDateDays()
  {
    return BEDays;
  }
/************************************************************************************************/
  public void setBANEffectiveDateDays(String value)
  {
    BEDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getBANEffectiveDateMonths()
  {
    return BEMonths;
  }
/************************************************************************************************/
  public void setBANEffectiveDateMonths(String value)
  {
    BEMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getBANEffectiveDateYears()
  {
    return BEYears;
  }
/************************************************************************************************/
  public void setBANEffectiveDateYears(String value)
  {
    BEYears = value.equals("")?0:Integer.parseInt(value);
  }
/****************************************************************************************/
  private void setDates()
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.setTime(Required_BAN_Effective_Dateh==null?(new java.util.Date())
      :Required_BAN_Effective_Dateh);
    BEDays=cal.get(cal.DATE);
    BEMonths=cal.get(cal.MONTH)+1;
    BEYears=cal.get(cal.YEAR);
  }
}