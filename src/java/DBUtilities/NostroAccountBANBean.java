package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class NostroAccountBANBean extends BANBean
{
  private String sql;
  private String Nostro_Reference;
  private String Primary_Account;
  private String Account_Name;
  private String Account_Invoice_Currency;
  private String Billing_Customer_Name;
  private String Billing_Customer_Contact_Name;
  private String Bill_Address_1;
  private String Bill_Address_2;
  private String Bill_Address_3;
  private String Bill_Address_4;
  private String Bill_Address_5;
  private String Bill_Address_6;
  private String Bill_Address_7;
  private String Bill_Address_Country;
  private String CW_Contract_Entity;
  private String Primary_Contract_Reference;
  protected Vector mandatory=new Vector();
  private Hashtable ScreenData=new Hashtable();
  private int BEDays, BEMonths, BEYears;
  private String extraBANIdentifier;
  private String Bank_Address_1;
  private String Bank_Address_2;
  private String Bank_Address_3;
  private String Bank_Address_4;
  private String Bank_Address_5;
  private String Bank_Address_6;
  private String Bank_Address_7;
  private String Alt_Bank_Address_1;
  private String Alt_Bank_Address_2;
  private String Alt_Bank_Address_3;
  private String Alt_Bank_Address_4;
  private String Alt_Bank_Address_5;
  private String Alt_Bank_Address_6;
  private String Alt_Bank_Address_7;
  private String CW_Registered_Entity;
  private String Registered_Number;
  private String Registered_Address;
  private String Tax_Reference_Literal;
  private String Tax_Reference;
  private String General_Ledger_Code;
  private String Jurisdiction_Currency_Code;

  public NostroAccountBANBean ()
  {
    Nostro_Reference = "";
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    mandatory.clear();
    mandatory.addElement("Nostro_Reference");
    mandatory.addElement("BANEffectiveDateh");
    mandatory.addElement("Primary_Account");
    mandatory.addElement("Account_Name");
    mandatory.addElement("Bill_Address_1");
    mandatory.addElement("Bill_Address_2");
    mandatory.addElement("Bill_Address_Country");
    mandatory.addElement("CW_Contract_Entity");
    mandatory.addElement("Primary_Contract_Reference");
    mandatory.addElement("Account_Invoice_Currency");
    mandatory.addElement("Billing_Customer_Name");
    mandatory.addElement("Billing_Customer_Contact_Name");
    errored.addElement("");
  }

  public void Reset()
  {
    super.Reset();
    Nostro_Reference = "";
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    Primary_Account = "";
    Account_Name = "";
    Account_Invoice_Currency = "";
    Billing_Customer_Name = "";
    Billing_Customer_Contact_Name = "";
    Bill_Address_1 = "";
    Bill_Address_2 = "";
    Bill_Address_3 = "";
    Bill_Address_4 = "";
    Bill_Address_5 = "";
    Bill_Address_6 = "";
    Bill_Address_7 = "";
    Bill_Address_Country = "";
    CW_Contract_Entity = "";
    Primary_Contract_Reference = "";
    Required_BAN_Effective_Dateh = new java.sql.Date(new java.util.Date().getTime());
    setErrored("clear");
  }
/************************************************************************************************/
public String getNostroReference()
{
  return Nostro_Reference;
}
/************************************************************************************************/
public void setNostroReference(String inRef)
{
  Nostro_Reference = inRef;
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
/****************************************************************************************/
public boolean getAccount()
{
    boolean getAccount = false;
    try
    {
      if (DBA.Connect(PREPARE,P3))
      {
        SQL = "{call givn_ref..Get_Account_For_Nostro (?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,Account_Id);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
	  if (RS.next())
	  {
	    Account_Name=RS.getString(1);
	    Nostro_Reference=RS.getString(2);
	    Primary_Account=RS.getString(3);
	    Billing_Customer_Name=RS.getString(4);
	    Billing_Customer_Contact_Name=RS.getString(5);
	    Bill_Address_1=RS.getString(6);
	    Bill_Address_2=RS.getString(7);
	    Bill_Address_3=RS.getString(8);
	    Bill_Address_4=RS.getString(9);
	    Bill_Address_5=RS.getString(10);
	    Bill_Address_6=RS.getString(11);
	    Bill_Address_7=RS.getString(12);
            determineCountry();
	    Account_Invoice_Currency=RS.getString(13);
            getAccount = true;
            getBANPrimaryContractReference();
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
      Message="<font color=red><b>Null Pointer "+se.getMessage();
    }
    finally
    {
      close();
      return getAccount;
    }
}
/************************************************************************************************/
  private void determineCountry()
  {
    if (!Bill_Address_7.equals(" "))
      Bill_Address_Country = Bill_Address_7;
    else if (!Bill_Address_6.equals(" "))
      Bill_Address_Country = Bill_Address_6;
    else if (!Bill_Address_5.equals(" "))
      Bill_Address_Country = Bill_Address_5;
    else if (!Bill_Address_4.equals(" "))
      Bill_Address_Country = Bill_Address_4;
  }
/************************************************************************************************/
  public boolean createAccountBAN()
  {
    boolean createAccountBAN = false;
    try{
	  if (DBA.Connect(WRITE,P5))
	  {
            SQL = "{call eban..Create_Nostro_Account_BAN " +
              "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

            cstmt = DBA.Conn.prepareCall(SQL);
            cstmt.setString(1,banStatus);
            cstmt.setString(2,Global_Customer_Id);
            cstmt.setString(3,BAN_Summary);
            cstmt.setDate(4,Required_BAN_Effective_Dateh);
            cstmt.setString(5,Mode);
            cstmt.setString(6,banCreatedBy);
            cstmt.setString(7,Nostro_Reference);
            cstmt.setString(8,Account_Name);
            cstmt.setString(9,Primary_Account);
            cstmt.setString(10,Billing_Customer_Name);
            cstmt.setString(11,Billing_Customer_Contact_Name);
            cstmt.setString(12,Bill_Address_1);
            cstmt.setString(13,Bill_Address_2);
            cstmt.setString(14,Bill_Address_3);
            cstmt.setString(15,Bill_Address_4);
            cstmt.setString(16,Bill_Address_Country);
            cstmt.setString(17,Primary_Contract_Reference);
            cstmt.setString(18,CW_Contract_Entity);
            cstmt.setString(19,Account_Invoice_Currency);

	    try
	    {
	      cstmt.execute();
	      RS = cstmt.getResultSet();
	      if (RS.next())
	      {
	        banIdentifier=RS.getString(1);
                createAccountBAN = true;
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
	  return createAccountBAN;
        }
  }
/*******************************************************************************/
 public boolean getAccountBAN()
 {
    boolean getAccountBAN = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_Nostro_Account_BAN ";
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
	    Nostro_Reference=(RS.getString(7));
	    Account_Name=(RS.getString(8));
	    Primary_Account=(RS.getString(9));
	    Billing_Customer_Name=(RS.getString(10));
	    Billing_Customer_Contact_Name=(RS.getString(11));
	    Bill_Address_1=(RS.getString(12));
	    Bill_Address_2=(RS.getString(13));
	    Bill_Address_3=(RS.getString(14));
	    Bill_Address_4=(RS.getString(15));
	    Bill_Address_Country=(RS.getString(16));
	    Primary_Contract_Reference=(RS.getString(17));
	    CW_Contract_Entity=(RS.getString(18));
	    Account_Invoice_Currency=(RS.getString(19));
            setDates();
            getAccountBAN = true;
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
      Message="<font color=red><b>Null Pointer "+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return getAccountBAN;
    }
 }
/************************************************************************************************/
public boolean AuthoriseAccountBAN()
{
  if (canAuthorise())
  {
    if (getStaticAccountDetails())
    {
      if (insertUpdateAccount())
      {
        if (updateBANStatus())
          return true;
        else
        {
          Message="<font color=red><b>Unable to update BAN status. Please contact system support.";
          return false;
        }
      }
      else
        return false;
    }
    else
      return false;
  }
  else
  {
    //Message="<font color=red><b>You do not have sufficient authority to authorise this BAN";
    return false;
  }
}
/*********************************************************************/
  private boolean insertUpdateAccount()
  {
    boolean insertUpdateAccount = false;
    try{
	  if (DBA.Connect(WRITE,P3))
	  {
            if (Mode.equals("Add"))
            {
              SQL = "{call givn_ref..Insert_Nostro_Account " +
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

              cstmt = DBA.Conn.prepareCall(SQL);
              cstmt.setString(1,Global_Customer_Id);
              cstmt.setString(2,Account_Name);
              cstmt.setString(3,Account_Invoice_Currency);
              cstmt.setString(4,Bill_Address_1);
              cstmt.setString(5,Bill_Address_2);
              cstmt.setString(6,Bill_Address_3);
              cstmt.setString(7,Bill_Address_4);
              cstmt.setString(8,Bill_Address_Country);
              cstmt.setString(9,Billing_Customer_Contact_Name);
              cstmt.setString(10,Billing_Customer_Name);
              cstmt.setString(11,Bank_Address_1);
              cstmt.setString(12,Bank_Address_2);
              cstmt.setString(13,Bank_Address_3);
              cstmt.setString(14,Bank_Address_4);
              cstmt.setString(15,Bank_Address_5);
              cstmt.setString(16,Bank_Address_6);
              cstmt.setString(17,Bank_Address_7);
              cstmt.setString(18,Alt_Bank_Address_1);
              cstmt.setString(19,Alt_Bank_Address_2);
              cstmt.setString(20,Alt_Bank_Address_3);
              cstmt.setString(21,Alt_Bank_Address_4);
              cstmt.setString(22,Alt_Bank_Address_5);
              cstmt.setString(23,Alt_Bank_Address_6);
              cstmt.setString(24,Alt_Bank_Address_7);
              cstmt.setString(25,CW_Contract_Entity);
              cstmt.setString(26,General_Ledger_Code);
              cstmt.setString(27,Tax_Reference);
              cstmt.setString(28,Tax_Reference_Literal);
              cstmt.setString(29,Jurisdiction_Currency_Code);
              cstmt.setString(30,userId);
              cstmt.setString(31,Primary_Account);
              cstmt.setString(32,Nostro_Reference);
            }
            else
            {
              SQL = "{call givn_ref..Update_Nostro_Account " +
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

              cstmt = DBA.Conn.prepareCall(SQL);
              cstmt.setString(1,Global_Customer_Id);
              cstmt.setString(2,Account_Name);
              cstmt.setString(3,Account_Invoice_Currency);
              cstmt.setString(4,Bill_Address_1);
              cstmt.setString(5,Bill_Address_2);
              cstmt.setString(6,Bill_Address_3);
              cstmt.setString(7,Bill_Address_4);
              cstmt.setString(8,Bill_Address_Country);
              cstmt.setString(9,Billing_Customer_Contact_Name);
              cstmt.setString(10,Billing_Customer_Name);
              cstmt.setString(11,Bank_Address_1);
              cstmt.setString(12,Bank_Address_2);
              cstmt.setString(13,Bank_Address_3);
              cstmt.setString(14,Bank_Address_4);
              cstmt.setString(15,Bank_Address_5);
              cstmt.setString(16,Bank_Address_6);
              cstmt.setString(17,Bank_Address_7);
              cstmt.setString(18,Alt_Bank_Address_1);
              cstmt.setString(19,Alt_Bank_Address_2);
              cstmt.setString(20,Alt_Bank_Address_3);
              cstmt.setString(21,Alt_Bank_Address_4);
              cstmt.setString(22,Alt_Bank_Address_5);
              cstmt.setString(23,Alt_Bank_Address_6);
              cstmt.setString(24,Alt_Bank_Address_7);
              cstmt.setString(25,CW_Contract_Entity);
              cstmt.setString(26,General_Ledger_Code);
              cstmt.setString(27,Tax_Reference);
              cstmt.setString(28,Tax_Reference_Literal);
              cstmt.setString(29,Jurisdiction_Currency_Code);
              cstmt.setString(30,userId);
              cstmt.setString(31,Primary_Account);
              cstmt.setString(32,Nostro_Reference);
              cstmt.setString(33,Account_Id);
            }

	    try
	    {
	      cstmt.execute();
              Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
              insertUpdateAccount = true;
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
	  return insertUpdateAccount;
	}
  }
/****************************************************************************************/
private boolean getStaticAccountDetails()
{
    boolean getStaticAccountDetails = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        String tempCurrency = (Account_Invoice_Currency.equals("USD")||
          Account_Invoice_Currency.equals("EUR"))?Account_Invoice_Currency:"GBP";
        SQL = "{call eBAN..Get_Company_Address_Details (?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setInt(1, Integer.parseInt(CW_Contract_Entity));
        cstmt.setString(2, tempCurrency);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
	  if (RS.next())
	  {
	    Bank_Address_1=RS.getString(1);
	    Bank_Address_2=RS.getString(2);
	    Bank_Address_3=RS.getString(3);
	    Bank_Address_4=RS.getString(4);
	    Bank_Address_5=RS.getString(5);
	    Bank_Address_6=RS.getString(6);
	    Bank_Address_7=RS.getString(7);
	    Alt_Bank_Address_1=RS.getString(8);
	    Alt_Bank_Address_2=RS.getString(9);
	    Alt_Bank_Address_3=RS.getString(10);
	    Alt_Bank_Address_4=RS.getString(11);
	    Alt_Bank_Address_5=RS.getString(12);
	    Alt_Bank_Address_6=RS.getString(13);
	    Alt_Bank_Address_7=RS.getString(14);
	    CW_Registered_Entity=RS.getString(15);
	    Registered_Number=RS.getString(16);
	    Registered_Address=RS.getString(17);
	    Tax_Reference_Literal=RS.getString(18);
	    Tax_Reference=RS.getString(19);
            General_Ledger_Code=RS.getString(20);
            getStaticAccountDetails = true;
	  }
          else
          {
            Message="<font color=red><b>Unable to find company address details";
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
      Message="<font color=red><b>Null Pointer "+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      if (getStaticAccountDetails)
        getStaticAccountDetails = getCurrencyCode();
      return getStaticAccountDetails;
    }
}
/*****************************************************************************************/
  private boolean getCurrencyCode()
  {
    boolean getCurrencyCode = false;
    try{
      SQL = "SELECT Currency_Code from eBAN..Country_Currency " +
       "WHERE Country = '" + Bill_Address_Country + "' "
       ;
      if (DBA.Connect(PREPARE,P5))
      {

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
        RS = Stmt.getResultSet();
        if (RS.next())
        {
          Jurisdiction_Currency_Code=RS.getString(1);
          getCurrencyCode = true;
        }
        else
        {
          Message="<font color=red><b>Unable to find currency code";
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      getCurrencyCode = false;
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
      getCurrencyCode = false;
    }//message set in underlying code
    finally
    {
      close();
      return getCurrencyCode;
    }
  }
/************************************************************************************************/
  private boolean updateBANStatus()
  {
    boolean updateBANStatus = false;
    try{
      SQL = "UPDATE eban..Nostro_Account_BAN " +
        "Set BAN_Status_Code = 'Implemented' " +
        "WHERE BAN_Identifier = '" + banIdentifier + "' ";
      if (DBA.Connect(PREPARE,P5))
      {

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
        updateBANStatus = true;
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      updateBANStatus = false;
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
      updateBANStatus = false;
    }//message set in underlying code
    finally
    {
      closeupdate();
      return updateBANStatus;
    }
  }
/************************************************************************************************/
  public boolean updateAccountBAN()
  {
    boolean updateAccountBAN = false;
    int rowcount=0;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Update_Nostro_Account_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
        cstmt.setString(2,banStatus);
        cstmt.setString(3,BAN_Summary);
        cstmt.setDate(4,Required_BAN_Effective_Dateh);
        cstmt.setString(5,banCreatedBy);
        cstmt.setString(6,Nostro_Reference);
        cstmt.setString(7,Account_Name);
        cstmt.setString(8,Primary_Account);
        cstmt.setString(9,Billing_Customer_Name);
        cstmt.setString(10,Billing_Customer_Contact_Name);
        cstmt.setString(11,Bill_Address_1);
        cstmt.setString(12,Bill_Address_2);
        cstmt.setString(13,Bill_Address_3);
        cstmt.setString(14,Bill_Address_4);
        cstmt.setString(15,Bill_Address_Country);
        cstmt.setString(16,Primary_Contract_Reference);
        cstmt.setString(17,CW_Contract_Entity);
        cstmt.setString(18,Account_Invoice_Currency);
        cstmt.setString(19,rejectReason);

        cstmt.execute();
        rowcount=cstmt.getUpdateCount();
	if (rowcount != 1)
	{
	  Message="Error! - "+Integer.toString(rowcount) + " Rows Updated";
	}
        else
        {
          Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" "+action+
            (action.endsWith("e")?"d":"ed");
          updateAccountBAN = true;
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
      return updateAccountBAN;
    }
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
    else if (name.compareTo("Nostro_Reference")==0)
    {
      setNostroReference(value);
      ScreenData.put("Nostro_Reference",value);
    }
    else if (name.compareTo("Primary_Account")==0)
    {
      setPrimaryAccount(value);
      ScreenData.put("Primary_Account",value);
    }
    else if (name.compareTo("Account_Name")==0)
    {
      setAccountName(value);
      ScreenData.put("Account_Name",value);
    }
    else if (name.compareTo("Bill_Address_1")==0)
    {
      setBillAddress1(value);
      ScreenData.put("Bill_Address_1",value);
    }
    else if (name.compareTo("Bill_Address_2")==0)
    {
      setBillAddress2(value);
      ScreenData.put("Bill_Address_2",value);
    }
    else if (name.compareTo("Bill_Address_3")==0)
    {
      setBillAddress3(value);
    }
    else if (name.compareTo("Bill_Address_4")==0)
    {
      setBillAddress4(value);
    }
    else if (name.compareTo("Bill_Address_Country")==0)
    {
      setBillAddressCountry(value);
      ScreenData.put("Bill_Address_Country",value);
    }
    else if (name.compareTo("CW_Contract_Entity")==0)
    {
      setCWContractEntity(value);
      ScreenData.put("CW_Contract_Entity",value);
    }
    else if (name.compareTo("Primary_Contract_Reference")==0)
    {
      setPrimaryContractReference(value);
      ScreenData.put("Primary_Contract_Reference",value);
    }
    else if (name.compareTo("Account_Invoice_Currency")==0)
    {
      setAccountInvoiceCurrency(value);
      ScreenData.put("Account_Invoice_Currency",value);
    }
    else if (name.compareTo("Billing_Customer_Name")==0)
    {
      setBillingCustomerName(value);
      ScreenData.put("Billing_Customer_Name",value);
    }
    else if (name.compareTo("Billing_Customer_Contact_Name")==0)
    {
      setBillingCustomerContactName(value);
      ScreenData.put("Billing_Customer_Contact_Name",value);
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
/************************************************************************************************/
  public String getPrimaryAccount()
  {
    return Primary_Account;
  }
/************************************************************************************************/
  public void setPrimaryAccount(String value)
  {
    Primary_Account = value;
  }
/************************************************************************************************/
  public String getAccountName()
  {
    return Account_Name;
  }
/************************************************************************************************/
  public void setAccountName(String value)
  {
    Account_Name = value;
  }
/************************************************************************************************/
  public String getBillAddress1()
  {
    return Bill_Address_1;
  }
/************************************************************************************************/
  public void setBillAddress1(String value)
  {
    Bill_Address_1 = value;
  }
/************************************************************************************************/
  public String getBillAddress2()
  {
    return Bill_Address_2;
  }
/************************************************************************************************/
  public void setBillAddress2(String value)
  {
    Bill_Address_2 = value;
  }
/************************************************************************************************/
  public String getBillAddress3()
  {
    return Bill_Address_3;
  }
/************************************************************************************************/
  public void setBillAddress3(String value)
  {
    Bill_Address_3 = value;
  }
/************************************************************************************************/
  public String getBillAddress4()
  {
    return Bill_Address_4;
  }
/************************************************************************************************/
  public void setBillAddress4(String value)
  {
    Bill_Address_4 = value;
  }
/************************************************************************************************/
  public String getBillAddressCountry()
  {
    return Bill_Address_Country;
  }
/************************************************************************************************/
  public void setBillAddressCountry(String value)
  {
    Bill_Address_Country = value;
  }
/************************************************************************************************/
  public String getCWContractEntity()
  {
    return CW_Contract_Entity;
  }
/************************************************************************************************/
  public void setCWContractEntity(String value)
  {
    CW_Contract_Entity = value;
  }
/************************************************************************************************/
  public String getPrimaryContractReference()
  {
    return Primary_Contract_Reference;
  }
/************************************************************************************************/
  public void setPrimaryContractReference(String value)
  {
    Primary_Contract_Reference = value;
  }
/************************************************************************************************/
  public String getAccountInvoiceCurrency()
  {
    return Account_Invoice_Currency;
  }
/************************************************************************************************/
  public void setAccountInvoiceCurrency(String value)
  {
    Account_Invoice_Currency = value;
  }
/************************************************************************************************/
  public String getBillingCustomerName()
  {
    return Billing_Customer_Name;
  }
/************************************************************************************************/
  public void setBillingCustomerName(String value)
  {
    Billing_Customer_Name = value;
  }
/************************************************************************************************/
  public String getBillingCustomerContactName()
  {
    return Billing_Customer_Contact_Name;
  }
/************************************************************************************************/
  public void setBillingCustomerContactName(String value)
  {
    Billing_Customer_Contact_Name = value;
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
	    FormField=SU.isNull((String)ScreenData.get(FieldName),"");
	    if(FormField.compareTo("") == 0)
	    {
	      setErrored(FieldName);
	    }
	  }
        }

	if ((!errored.isEmpty()) && (errored.size() > 0))
	{
	  Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
	  return false;
	}
	else
        {
          if (nameAlreadyExists())
          {
      	    Message = "<font color=red><b>Account Name has already been used";
            setErrored("Account_Name");
    	    return false;
          }
          else if (needsPrimaryAccount())
          {
      	    Message = "<font color=red><b>This must be the Primary Account";
            setErrored("Primary_Account");
    	    return false;
          }
          else if (notPrimaryAccount())
          {
      	    Message = "<font color=red><b>This cannot be the Primary Account.  Unimplemented BAN " +
            extraBANIdentifier + " has been desginated  the Primary Account";
            setErrored("Primary_Account");
    	    return false;
          }
          else
	    return true;
        }

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
/****************************************************************************************/
  private boolean nameAlreadyExists()
  {
    if ((Mode.equals("Add")) && (accountBANExists()))
      return true;
    else
      return accountExists();
  }
/*****************************************************************************************/
  private boolean accountBANExists()
  {
    boolean accountBANExists = false;
    try{
      SQL = "SELECT Account_Name from eban..Nostro_Account_BAN " +
       "WHERE Global_Customer_Id = '" + Global_Customer_Id + "' " +
       "AND Account_Name = '" + Account_Name + "' " +
       "AND BAN_Identifier <> '" +banIdentifier + "' "
       ;
      if (DBA.Connect(PREPARE,P5))
      {

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
        RS = Stmt.getResultSet();
        if (RS.next())
        {
          accountBANExists = true;
        }
        else
        {
          accountBANExists = false;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      accountBANExists = false;
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
      accountBANExists = false;
    }//message set in underlying code
    finally
    {
      close();
      return accountBANExists;
    }
  }
/*****************************************************************************************/
  private boolean accountExists()
  {
    boolean accountExists = false;
    try{
      SQL = "SELECT Invoice_Region from givn_ref..Invoice_Region " +
       "WHERE Global_Customer_Id = '" + Global_Customer_Id + "' " +
       "AND Invoice_Region = '" + Account_Name + "' " +
       (Mode.equals("Add")?"":" AND Account_Id <> '"+Account_Id+"' ")
       ;
      if (DBA.Connect(PREPARE,P3))
      {

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
        RS = Stmt.getResultSet();
        if (RS.next())
        {
          accountExists = true;
        }
        else
        {
          accountExists = false;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      accountExists = false;
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
      accountExists = false;
    }//message set in underlying code
    finally
    {
      close();
      return accountExists;
    }
  }
/*****************************************************************************************/
  private boolean needsPrimaryAccount()
  {
    if (Primary_Account.equals("N"))
    {
      if ((Mode.equals("Add")) && (primaryAccountBANExists(false)))
        return false;
      else
        return !primaryAccountExists();
    }
    else
      return false;
  }
/*****************************************************************************************/
  private boolean primaryAccountBANExists(boolean notImp)
  {
    boolean primaryAccountBANExists = false;
    try{
      SQL = "SELECT Ban_Identifier from eban..Nostro_Account_BAN " +
       "WHERE Global_Customer_Id = '" + Global_Customer_Id + "' " +
       "AND Account_Name <> '" + Account_Name + "' " +
       "AND Primary_Account = 'Y' " +
       "AND BAN_Identifier <> '" +banIdentifier + "' " +
       (notImp?" AND Ban_Status_code <> 'Implemented' ":"")
       ;
      if (DBA.Connect(PREPARE,P5))
      {

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
        RS = Stmt.getResultSet();
        if (RS.next())
        {
          primaryAccountBANExists = true;
          extraBANIdentifier = RS.getString(1);
        }
        else
        {
          primaryAccountBANExists = false;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      primaryAccountBANExists = false;
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
      primaryAccountBANExists = false;
    }//message set in underlying code
    finally
    {
      close();
      return primaryAccountBANExists;
    }
  }
/*****************************************************************************************/
  private boolean primaryAccountExists()
  {
    boolean primaryAccountExists = false;
    try{
      SQL = "SELECT Invoice_Region from givn_ref..Invoice_Region " +
       "WHERE Global_Customer_Id = '" + Global_Customer_Id + "' " +
       "AND Primary_Account_Ind = 'Y' " +
       (Mode.equals("Add")?"":" AND Account_Id <> '"+Account_Id+"' ")
       ;
      if (DBA.Connect(PREPARE,P3))
      {

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
        RS = Stmt.getResultSet();
        if (RS.next())
        {
          primaryAccountExists = true;
        }
        else
        {
          primaryAccountExists = false;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      primaryAccountExists = false;
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
      primaryAccountExists = false;
    }//message set in underlying code
    finally
    {
      close();
      return primaryAccountExists;
    }
  }
/*****************************************************************************************/
  private boolean notPrimaryAccount()
  {
    if ((Primary_Account.equals("Y")) && (Mode.equals("Add")) &&
      (primaryAccountBANExists(true)))
        return true;
    else
      return false;
  }
/*****************************************************************************************/
  private void getBANPrimaryContractReference()
  {
    Primary_Contract_Reference = "";
    try{
      SQL = "SELECT Primary_Contract_Reference from eban..Nostro_Account_BAN " +
       "WHERE Global_Customer_Id = '" + Global_Customer_Id + "' " +
       "AND Account_Name = '" + Account_Name + "' " +
       "ORDER BY Last_Update_Date DESC "
       ;
      if (DBA.Connect(PREPARE,P5))
      {

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
        RS = Stmt.getResultSet();
        if (RS.next())
        {
          Primary_Contract_Reference = RS.getString(1);
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
    }
  }
}