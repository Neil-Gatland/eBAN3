package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class AssureChargeBean extends BANBean
{
  private String sql;
  private String Site_Address_1;
  private String Site_Address_2;
  private String Site_Address_3;
  private String Site_Postcode;
  private String Site_Country;
  private String Signatory;
  private String Product;
  private String Authorising_Manager;
  private String Status_Details;
  private String Customer_Name;
  private String Customer_Id;
  private String CW_Info;
  private String Customer_Info;
  private String Invoice_Currency;
  private String Circuit_Ref;
  private String Contact_Name;
  private String Contact_Telephone;
  private String Contact_Mobile;
  private String Contact_Fax;
  private String Contact_Email;
  private String Fault_Details;
  protected Vector mandatory=new Vector();
  private Hashtable ScreenData=new Hashtable();
  private int Invoice_Id;
  private String SR_Ref;
  private String C002_Ref;
  private String Fault_Code;
  private int VDays, VMonths, VYears;
  private java.sql.Date Visit_Date;
  private float Invoice_Amount;
  private String displayCreatedDate;
  private String printCreatedDate;
  private String displayAuthorisedDate;
  private String Authorisor;
  private String Engineer;
  private String Job_Title;
  private String fdVis;
  private String VAT_Rate;
  private String General_No;
  private String Fault_No;


  public AssureChargeBean ()
  {
    Site_Address_1 = "";
    Site_Address_2 = "";
    Site_Address_3 = "";
    Site_Postcode = "";
    Site_Country = "";
    Signatory = "";
    Product = "";
    Authorising_Manager = "";
    Status_Details = "";
    Customer_Id = "";
    Customer_Name = "";
    Account_Id = "";
    Account_Name = "";
    CW_Info = "";
    Customer_Info = "";
    Invoice_Currency = "";
    Contact_Name = "";
    Contact_Telephone = "";
    Contact_Mobile = "";
    Contact_Fax = "";
    Contact_Email = "";
    Fault_Details = "";
    Invoice_Id = 0;
    SR_Ref = "";
    C002_Ref = "";
    Circuit_Ref = "";
    Fault_Code = "";
    VDays = 0;
    VMonths = 0;
    VYears = 0;
    Visit_Date = null;
    getInitialChargeAmount();
    getPrintDetails();
    mandatory.clear();
    mandatory.addElement("Site_Address_1");
    mandatory.addElement("Site_Postcode");
    mandatory.addElement("Site_Country");
    mandatory.addElement("SR_Ref");
    mandatory.addElement("Ad_Hoc_Product");
    mandatory.addElement("VisitDateh");
    mandatory.addElement("Fault_Code");
    mandatory.addElement("Invoice_Amount");
    mandatory.addElement("Invoice_Currency");
    mandatory.addElement("Contact_Name");
    mandatory.addElement("Engineer");
    mandatory.addElement("CW_Info");
    errored.addElement("");
    displayCreatedDate = "";
    printCreatedDate = "";
    displayAuthorisedDate = "";
    Authorisor = "";
    banStatus = "Draft";
    Mode = "";
    Engineer = "";
    Job_Title = "";
    fdVis = "hidden";
  }

  public void Reset()
  {
    super.Reset();
    setErrored("clear");
    setDates();
    Site_Address_1 = "";
    Site_Address_2 = "";
    Site_Address_3 = "";
    Site_Postcode = "";
    Site_Country = "";
    Signatory = "";
    Product = "";
    Authorising_Manager = "";
    Status_Details = "";
    Customer_Id = "";
    Customer_Name = "";
    Account_Id = "";
    Account_Name = "";
    CW_Info = "";
    Customer_Info = "";
    Invoice_Currency = "";
    Contact_Name = "";
    Contact_Telephone = "";
    Contact_Mobile = "";
    Contact_Fax = "";
    Contact_Email = "";
    Fault_Details = "";
    Invoice_Id = 0;
    SR_Ref = "";
    C002_Ref = "";
    Circuit_Ref = "";
    Fault_Code = "";
    VDays = 0;
    VMonths = 0;
    VYears = 0;
    Visit_Date = null;
    getInitialChargeAmount();
    getPrintDetails();
    displayCreatedDate = "";
    printCreatedDate = "";
    displayAuthorisedDate = "";
    Authorisor = "";
    banStatus = "Draft";
    Mode = "";
    Engineer = "";
    Job_Title = "";
    fdVis = "hidden";
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
 public boolean getAssureCharge()
 {
    boolean getAssureCharge = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
       SQL = "{call eban..get_Assure_Charge ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
            Customer_Id=Integer.toString(RS.getInt(1));
            Customer_Name=RS.getString(2);
            Account_Id=RS.getString(3);
            Billing_Source=RS.getString(4);
            Site_Address_1=RS.getString(5);
            Site_Address_2=RS.getString(6);
            Site_Address_3=RS.getString(7);
            Site_Postcode=RS.getString(8);
            Site_Country=RS.getString(9);
            Contact_Name=RS.getString(10);
            Contact_Telephone=RS.getString(11);
            Contact_Mobile=RS.getString(12);
            Contact_Fax=RS.getString(13);
            Contact_Email=RS.getString(14);
            Signatory=RS.getString(15);
            SR_Ref=RS.getString(16);
            C002_Ref=Integer.toString(RS.getInt(17));
            Circuit_Ref=RS.getString(18);
            Product=RS.getString(19);
            Visit_Date=RS.getDate(20);
            setFault_Code(RS.getString(21));
            Fault_Details=RS.getString(22);
            Authorising_Manager=RS.getString(23);
            Status_Details=RS.getString(24);
            Customer_Info=RS.getString(25);
            CW_Info=RS.getString(26);
            Invoice_Currency=RS.getString(27);
            Invoice_Amount=RS.getFloat(28);
	    banStatus=RS.getString(29);
            banCreatedBy=RS.getString(30);
            displayCreatedDate=RS.getString(31);
            Authorisor=RS.getString(32);
            displayAuthorisedDate=RS.getString(33);
            Engineer=RS.getString(34);
            Job_Title=RS.getString(35);
            printCreatedDate=RS.getString(36);
            setDates();
            findAccountName();
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
      return getAssureCharge;
    }
 }
/************************************************************************************************/
  public boolean autoSetBilling_Source()
  {
    return true;
  }
/************************************************************************************************/
  public void setBillingSource(String value)
  {
    if (value.equals(""))
    {
      findBillingSource();
    }
    else
    {
      Billing_Source = value;
    }
  }
/************************************************************************************************/
  private void getInitialChargeAmount()
  {
    Invoice_Amount = 0;
    try
    {
      SQL = "SELECT Value FROM eban..Ban_Reference_Data " +
        "WHERE Datatype = 'Charge Amount' " +
        "AND Text = 'Assure Charge' " +
        "AND Qualifier = 'AdHoc' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          Invoice_Amount = Float.parseFloat(RS.getString(1));
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
    }
  }
/************************************************************************************************/
  private void getPrintDetails()
  {
    VAT_Rate = "0";
    General_No = "";
    Fault_No = "";
    try
    {
      SQL = "SELECT Value FROM eban..Ban_Reference_Data " +
        "WHERE Datatype = 'Print' " +
        "AND Qualifier = 'AdHoc' " +
        "ORDER BY Display_Order ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          VAT_Rate = RS.getString(1);
        }
        if (RS.next())
        {
          General_No = RS.getString(1);
        }
        if (RS.next())
        {
          Fault_No = RS.getString(1);
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
    }
  }
/************************************************************************************************/
  private void findBillingSource()
  {
    Billing_Source = "Unknown";
    if (!Account_Id.equals(""))
    {
      try
      {
        SQL = "SELECT Billing_Source FROM eban..Ad_Hoc_Account " +
          "WHERE Account_No='" + Account_Id + "' ";
        DBA.setSQL(SQL);
        if (DBA.Connect(READ,P5))
        {
          RS = DBA.getResultsSet();
          if (RS.next())
          {
            Billing_Source = RS.getString(1);
          }
        }
      }
      catch(Exception ex)
      {
        Message=ex.getMessage();
      }
      finally
      {
        close();
      }
    }
  }
/************************************************************************************************/
  private void findAccountName()
  {
    try
    {
      SQL = "SELECT Account_Name FROM eban..Ad_Hoc_Account " +
        "WHERE Account_No='" + Account_Id + "' " +
        "AND Billing_Source='" + Billing_Source + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          Account_Name = RS.getString(1);
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
    }
  }
/************************************************************************************************/
  private boolean C002_Compulsory()
  {
    boolean C002_Compulsory = false;
    try
    {
      SQL = "SELECT C002_Validation FROM eban..Ad_Hoc_Product " +
        "WHERE Prime_Product_Code=" + Product + " " +
        "AND C002_Validation='Y' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        C002_Compulsory = RS.next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return C002_Compulsory;
    }
  }
/************************************************************************************************/
  public boolean AuthoriseAssureCharge()
  {
    boolean authoriseAssureCharge = false;
    int rowcount=0;

    try
    {
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Authorise_Assure_Charge " +
          "(?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banCreatedBy);
        cstmt.setString(2,Authorising_Manager);
        cstmt.setString(3,Status_Details);
        cstmt.setString(4,banIdentifier);

        cstmt.execute();
        rowcount=cstmt.getUpdateCount();
        authoriseAssureCharge = true;
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
      return authoriseAssureCharge;
    }
  }
/************************************************************************************************/
  public int createCustomer(String Customer_Name)
  {
    int Customer_Id = 0;
    try{
	  if (DBA.Connect(WRITE,P5))
	  {
            SQL = "{call eban..Create_Ad_Hoc_Customer (?)}";

            cstmt = DBA.Conn.prepareCall(SQL);
            cstmt.setString(1,Customer_Name);

	    try
	    {
	      cstmt.execute();
	      RS = cstmt.getResultSet();
              if (RS == null)
              {
                SQL = "SELECT Customer_Id from eban..Ad_Hoc_Customer " +
                  " where Party_Name = '" + Customer_Name + "'";
                DBA.setSQL(SQL);
                if (!DBA.Connect(READ,P5))
                { //Failed to connect - message set in underlying code
        	  Message=DBA.Message;
                }
                else
                {
                  RS = DBA.getResultsSet();
                  if (RS.next())
                  {
                    Customer_Id=RS.getInt(1);
                  }
                }
              }
              else
              {
                if (RS.next())
                {
                  Customer_Id=RS.getInt(1);
                }
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
          return Customer_Id;
        }
  }
/************************************************************************************************/
  public String createCustomerTest(String Customer_Name)
  {
    int Customer_Id = 0;
    StringBuffer sb = new StringBuffer();
sb.append("top<br>");
    try{
sb.append("try<br>");
	  if (DBA.Connect(WRITE,P5))
	  {
sb.append("connected<br>");
            SQL = "{call eban..Create_Ad_Hoc_Customer (?)}";

            cstmt = DBA.Conn.prepareCall(SQL);
            cstmt.setString(1,Customer_Name);

	    try
	    {
sb.append("try exec<br>");
	      cstmt.execute();
sb.append("executed<br>");
	      RS = cstmt.getResultSet();
sb.append("got RS<br>");
if (RS == null)
sb.append("RS is null<br>");
	      if (RS.next())
	      {
sb.append("RS not empty<br>");
	        Customer_Id=RS.getInt(1);
sb.append("Customer Id:" + Integer.toString(Customer_Id) + "<br>");
	      }
	    }
	    catch(java.sql.SQLException se)
	    {
sb.append("sql exception<br>");
	      Message=se.getMessage();
	    }
	  }
	  else
	  { //Failed to connect - message set in underlying code
sb.append("not connected<br>");
	    Message=DBA.Message;
	  }
	}
	catch(java.sql.SQLException se)
	{
sb.append("sql exception 2<br>");
	  Message=se.getMessage();
	}
        catch(java.lang.NullPointerException se)
        {
sb.append("null pointer exception<br>");
          Message="<font color=red><b>"+se.getMessage();
        }//message set in underlying code
        finally
        {
sb.append("before close<br>");
          close();
sb.append("after close<br>");
          return sb.toString();
        }
  }
/************************************************************************************************/
  public boolean createAssureCharge()
  {
    boolean createAssureCharge = false;
    try{
	  if (DBA.Connect(WRITE,P5))
	  {
            SQL = "{call eban..Create_Assure_Charge " +
              "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

            cstmt = DBA.Conn.prepareCall(SQL);
            cstmt.setString(1,banStatus);
            cstmt.setString(2,banCreatedBy);
            cstmt.setInt(3,Integer.parseInt(Customer_Id));
            cstmt.setString(4,Customer_Name);
            cstmt.setString(5,Account_Id);
            cstmt.setString(6,Billing_Source);
            cstmt.setString(7,Site_Address_1);
            cstmt.setString(8,Site_Address_2);
            cstmt.setString(9,Site_Address_3);
            cstmt.setString(10,Site_Postcode);
            cstmt.setString(11,Site_Country);
            cstmt.setString(12,Contact_Name);
            cstmt.setString(13,Contact_Telephone);
            cstmt.setString(14,Contact_Mobile);
            cstmt.setString(15,Contact_Fax);
            cstmt.setString(16,Contact_Email);
            cstmt.setString(17,Signatory);
            cstmt.setString(18,SR_Ref);
            cstmt.setInt(19,Integer.parseInt(C002_Ref));
            cstmt.setString(20,Circuit_Ref);
            cstmt.setString(21,Product);
            cstmt.setDate(22,Visit_Date);
            cstmt.setString(23,Fault_Code);
            cstmt.setString(24,Fault_Details);
            cstmt.setString(25,Authorising_Manager);
            cstmt.setString(26,Status_Details);
            cstmt.setString(27,Customer_Info);
            cstmt.setString(28,CW_Info);
            cstmt.setString(29,Invoice_Currency);
            cstmt.setFloat(30,Invoice_Amount);
            cstmt.setString(31,Engineer);
            cstmt.setString(32,Job_Title);

	    try
	    {
	      cstmt.execute();
	      RS = cstmt.getResultSet();
	      if (RS.next())
	      {
	        banIdentifier=RS.getString(1);
                createAssureCharge = true;
        	Message = "<font color=blue><b>Assure Charge:-"+banIdentifier+" created";
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
          return createAssureCharge;
        }
  }
/************************************************************************************************/
  public boolean updateAssureCharge()
  {
    boolean updateAssureCharge = false;
    int rowcount=0;

    try
    {
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Update_Assure_Charge " +
          "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banStatus);
        cstmt.setString(2,banCreatedBy);
        cstmt.setInt(3,Integer.parseInt(Customer_Id));
        cstmt.setString(4,Customer_Name);
        cstmt.setString(5,Account_Id);
        cstmt.setString(6,Billing_Source);
        cstmt.setString(7,Site_Address_1);
        cstmt.setString(8,Site_Address_2);
        cstmt.setString(9,Site_Address_3);
        cstmt.setString(10,Site_Postcode);
        cstmt.setString(11,Site_Country);
        cstmt.setString(12,Contact_Name);
        cstmt.setString(13,Contact_Telephone);
        cstmt.setString(14,Contact_Mobile);
        cstmt.setString(15,Contact_Fax);
        cstmt.setString(16,Contact_Email);
        cstmt.setString(17,Signatory);
        cstmt.setString(18,SR_Ref);
        cstmt.setInt(19,Integer.parseInt(C002_Ref));
        cstmt.setString(20,Circuit_Ref);
        cstmt.setString(21,Product);
        cstmt.setDate(22,Visit_Date);
        cstmt.setString(23,Fault_Code);
        cstmt.setString(24,Fault_Details);
        cstmt.setString(25,Authorising_Manager);
        cstmt.setString(26,Status_Details);
        cstmt.setString(27,Customer_Info);
        cstmt.setString(28,CW_Info);
        cstmt.setString(29,Invoice_Currency);
        cstmt.setFloat(30,Invoice_Amount);
        cstmt.setString(31,banIdentifier);
        cstmt.setString(32,Engineer);
        cstmt.setString(33,Job_Title);

        cstmt.execute();
        rowcount=cstmt.getUpdateCount();
        updateAssureCharge = true;
        Message = "<font color=blue><b>Assure Charge:-"+banIdentifier+" "+action+
          (action.endsWith("e")?"d":"ed");
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
      return updateAssureCharge;
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

        if ((Account_Id.equals("")) || (Account_Id.equals(" ")))
        {
          Message = "<font color=red><b>An account must be chosen";
          return false;
        }

	if ((ButtonPressed.startsWith("Submit") ||
          (ButtonPressed.startsWith("Save Exception")) ||
          (ButtonPressed.startsWith("Save Draft"))) &&
          (Mode.compareTo("Cease") !=0))
	{
	  while (FormFields.hasMoreElements())
	  {
	    FieldName=(String)FormFields.nextElement();
            FormField=SU.isNull((String)ScreenData.get(FieldName),"");
            if ((FormField.equals("")) || (FormField.equals(" ")))
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

        /*if (!SR_Ref.equals(""))
        {
          try
          {
            int temp = Integer.parseInt(SR_Ref);
          }
          catch (java.lang.NumberFormatException NE)
          {
            setErrored("SR_Ref");
            Message = "<font color=red><b>Fault SR Reference must be numeric";
            return false;
          }
        }*/

        if (!C002_Ref.equals(""))
        {
          try
          {
            int temp = Integer.parseInt(C002_Ref);
          }
          catch (java.lang.NumberFormatException NE)
          {
            setErrored("C002_Ref");
            Message = "<font color=red><b>Works Order Reference must be numeric";
            return false;
          }
        }

        try
        {
          float temp = Float.parseFloat((String)ScreenData.get("Invoice_Amount"));
        }
        catch (java.lang.NumberFormatException NE)
        {
          setErrored("C002_Ref");
          Message = "<font color=red><b>Charge Amount must be numeric";
          return false;
        }

        return true;
  }
/*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.compareTo("VisitDateh")==0)
    {
     setVisit_Date(value);
     ScreenData.put("VisitDateh",value);
    }
    else if (name.compareTo("VisitDateDay")==0)
    {
     setVisitDateDays(value);
     ScreenData.put("VisitDateDay",value);
    }
    else if (name.compareTo("VisitDateMonth")==0)
    {
     setVisitDateMonths(value);
     ScreenData.put("VisitDateMonth",value);
    }
    else if (name.compareTo("VisitDateYear")==0)
    {
     setVisitDateYears(value);
     ScreenData.put("VisitDateYear",value);
    }
    else if (name.compareTo("Site_Address_1")==0)
    {
      setSite_Address_1(value);
      ScreenData.put("Site_Address_1",value);
    }
    else if (name.compareTo("Site_Address_2")==0)
    {
      setSite_Address_2(value);
      ScreenData.put("Site_Address_2",value);
    }
    else if (name.compareTo("Site_Address_3")==0)
    {
      setSite_Address_3(value);
      ScreenData.put("Site_Address_3",value);
    }
    else if (name.compareTo("Site_Postcode")==0)
    {
      setSite_Postcode(value);
      ScreenData.put("Site_Postcode",value);
    }
    else if (name.compareTo("Site_Country")==0)
    {
      setSite_Country(value);
      ScreenData.put("Site_Country",value);
    }
    else if (name.compareTo("Invoice_Amount")==0)
    {
      setInvoice_Amount(value);
      ScreenData.put("Invoice_Amount",value);
    }
    else if (name.compareTo("Invoice_Currency")==0)
    {
      setInvoice_Currency(value);
      ScreenData.put("Invoice_Currency",value);
    }
    else if (name.compareTo("Signatory")==0)
    {
      setSignatory(value);
      ScreenData.put("Signatory",value);
    }
    else if (name.compareTo("Ad_Hoc_Product")==0)
    {
      setProduct(value);
      ScreenData.put("Ad_Hoc_Product",value);
    }
    else if (name.compareTo("Authorising_Manager")==0)
    {
      setAuthorising_Manager(value);
      ScreenData.put("Authorising_Manager",value);
    }
    else if (name.compareTo("Status_Details")==0)
    {
      setStatus_Details(value);
      ScreenData.put("Status_Details",value);
    }
    else if (name.compareTo("CW_Info")==0)
    {
      setCW_Info(value);
      ScreenData.put("CW_Info",value);
    }
    else if (name.compareTo("Customer_Info")==0)
    {
      setCustomer_Info(value);
      ScreenData.put("Customer_Info",value);
    }
    else if (name.compareTo("SR_Ref")==0)
    {
      setSR_Ref(value);
      ScreenData.put("SR_Ref",value);
    }
    else if (name.compareTo("C002_Ref")==0)
    {
      setC002_Ref(value);
      ScreenData.put("C002_Ref",value);
    }
    else if (name.compareTo("Fault_Code")==0)
    {
      setFault_Code(value);
      ScreenData.put("Fault_Code",value);
    }
    else if (name.compareTo("Contact_Name")==0)
    {
      setContact_Name(value);
      ScreenData.put("Contact_Name",value);
    }
    else if (name.compareTo("Contact_Telephone")==0)
    {
      setContact_Telephone(value);
      ScreenData.put("Contact_Telephone",value);
    }
    else if (name.compareTo("Contact_Mobile")==0)
    {
      setContact_Mobile(value);
      ScreenData.put("Contact_Mobile",value);
    }
    else if (name.compareTo("Contact_Fax")==0)
    {
      setContact_Fax(value);
      ScreenData.put("Contact_Fax",value);
    }
    else if (name.compareTo("Contact_Email")==0)
    {
      setContact_Email(value);
      ScreenData.put("Contact_Email",value);
    }
    else if (name.compareTo("Fault_Details")==0)
    {
      setFault_Details(value);
      ScreenData.put("Fault_Details",value);
    }
    else if (name.compareTo("Account")==0)
    {
      setAccountDetails(value);
      if ((!value.equals("")) && (banStatus.equals("Provisional")))
        setBanStatus("Draft");
      ScreenData.put("Account",value);
    }
    else if (name.compareTo("Account_Id")==0)
    {
      setAccount_Id(value);
      ScreenData.put("Account_Id",value);
    }
    else if (name.compareTo("Account_Name")==0)
    {
      setAccountName(value);
      ScreenData.put("Account_Name",value);
    }
    else if (name.compareTo("Account_Filter")==0)
    {
      setAccountFilter(value);
      ScreenData.put("Account_Filter",value);
    }
    else if (name.compareTo("Engineer")==0)
    {
      setEngineer(value);
      ScreenData.put("Engineer",value);
    }
    else if (name.compareTo("Job_Title")==0)
    {
      setJob_Title(value);
      ScreenData.put("Job_Title",value);
    }
    else if (name.compareTo("Circuit_Ref")==0)
    {
      setCircuit_Ref(value);
      ScreenData.put("Circuit_Ref",value);
    }
  }
/************************************************************************************************/
public String getSite_Address_1()
{
  return Site_Address_1;
}
/************************************************************************************************/
public void setSite_Address_1(String value)
{
  Site_Address_1 = value;
}
/************************************************************************************************/
public String getSite_Address_2()
{
  return Site_Address_2;
}
/************************************************************************************************/
public void setSite_Address_2(String value)
{
  Site_Address_2 = value;
}
/************************************************************************************************/
public String getSite_Address_3()
{
  return Site_Address_3;
}
/************************************************************************************************/
public void setSite_Address_3(String value)
{
  Site_Address_3 = value;
}
/************************************************************************************************/
public String getSite_Postcode()
{
  return Site_Postcode;
}
/************************************************************************************************/
public void setSite_Postcode(String value)
{
  Site_Postcode = value;
}
/************************************************************************************************/
public String getSite_Country()
{
  return Site_Country;
}
/************************************************************************************************/
public void setSite_Country(String value)
{
  Site_Country = value;
}
/************************************************************************************************/
public String getInvoice_Amount()
{
  String temp = String.valueOf(Invoice_Amount);
  StringBuffer sb = new StringBuffer(temp);
  int dp = temp.indexOf(".");
  if ((dp != -1) && (temp.length() < (dp + 3)))
  {
    sb.append("0");
  }
  return sb.toString();
}
/************************************************************************************************/
public void setInvoice_Amount(String value)
{
  if ((value.compareTo("")!=0) && (value !=null))
  {
    try
    {
      Invoice_Amount = Float.parseFloat(value);
    }
    catch (java.lang.NumberFormatException NE)
    {
    }
  }
  else
  {
    Invoice_Amount=0;
  }
}
/************************************************************************************************/
public String getInvoice_Currency()
{
  return Invoice_Currency;
}
/************************************************************************************************/
public void setInvoice_Currency(String value)
{
  Invoice_Currency = value;
}
/************************************************************************************************/
public String getSignatory()
{
  return Signatory;
}
/************************************************************************************************/
public void setSignatory(String value)
{
  Signatory = value;
}
/************************************************************************************************/
public String getProduct()
{
  return Product;
}
/************************************************************************************************/
public void setProduct(String value)
{
  Product = value;
  if (C002_Compulsory())
  {
    if (!mandatory.contains("C002_Ref"))
    {
      mandatory.addElement("C002_Ref");
    }
  }
  else
  {
    if (mandatory.contains("C002_Ref"))
    {
      mandatory.removeElement("C002_Ref");
    }
  }
}
/************************************************************************************************/
public String getCircuit_Ref()
{
  return Circuit_Ref;
}
/************************************************************************************************/
public void setCircuit_Ref(String value)
{
  Circuit_Ref = value;
}
/************************************************************************************************/
public String getAuthorising_Manager()
{
  return Authorising_Manager;
}
/************************************************************************************************/
public void setAuthorising_Manager(String value)
{
  Authorising_Manager = value;
}
/************************************************************************************************/
public String getStatus_Details()
{
  return Status_Details;
}
/************************************************************************************************/
public void setStatus_Details(String value)
{
  Status_Details = value;
}
/************************************************************************************************/
public String getRejectReason()
{
  return Status_Details;
}
/************************************************************************************************/
public void setRejectReason(String value)
{
  Status_Details = value;
}
/************************************************************************************************/
public String getCustomerId()
{
  return Customer_Id;
}
/************************************************************************************************/
public void setCustomerId(String value)
{
  Customer_Id = value;
}
/************************************************************************************************/
public String getCustomerName()
{
  return Customer_Name;
}
/************************************************************************************************/
public void setCustomerName()
{
  if (Customer_Id.equals(""))
    setCustomerNameWithAccount();
  else
    setCustomerNameWithCustomerId();

}
/************************************************************************************************/
  public void setCustomerNameWithCustomerId()
  {
    try
    {
      SQL = "SELECT Party_Name FROM eban..Ad_Hoc_Customer " +
        "WHERE Customer_Id=" + Customer_Id + " ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          Customer_Name = RS.getString(1);
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
    }
  }
/************************************************************************************************/
  public void setCustomerNameWithAccount()
  {
    try
    {
      SQL = "SELECT c.Customer_Id, c.Party_Name FROM eban..Ad_Hoc_Customer c, " +
        "eban..Ad_Hoc_Account a " +
        "WHERE c.Party_Id=a.Party_Id " +
        "AND a.Account_No = '" + Account_Id + "' " +
        "AND a.Billing_Source = '" + Billing_Source + "' "
        ;
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          Customer_Id = Integer.toString(RS.getInt(1));
          Customer_Name = RS.getString(2);
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
    }
  }
/************************************************************************************************/
public String getCustomer_Info()
{
  return Customer_Info;
}
/************************************************************************************************/
public void setCustomer_Info(String value)
{
  Customer_Info = value;
}
/************************************************************************************************/
public String getCW_Info()
{
  return CW_Info;
}
/************************************************************************************************/
public void setCW_Info(String value)
{
  CW_Info = value;
}
/************************************************************************************************/
public String getSR_Ref()
{
  return SR_Ref.equals("0")?"":SR_Ref;
}
/************************************************************************************************/
public void setSR_Ref(String value)
{
  SR_Ref = value.equals("")?"0":value;
}
/************************************************************************************************/
public String getC002_Ref()
{
  return C002_Ref.equals("0")?"":C002_Ref;
}
/************************************************************************************************/
  public void setC002_Ref(String value)
  {
    C002_Ref = value.equals("")?"0":value;
  }
/************************************************************************************************/
  public String getFault_Code()
  {

    return Fault_Code;
  }
/************************************************************************************************/
  public void setFault_Code(String value)
  {
    Fault_Code = value;
    if (Fault_Code.endsWith("a"))
    {
      fdVis= "visible";
      if (!mandatory.contains("Fault_Details"))
      {
        mandatory.add("Fault_Details");
      }
    }
    else
    {
      fdVis= "hidden";
      if (mandatory.contains("Fault_Details"))
      {
        mandatory.remove("Fault_Details");
      }
    }
  }
/************************************************************************************************/
  public int getVisitDateDays()
  {
    return VDays;
  }
/************************************************************************************************/
  public void setVisitDateDays(String value)
  {
    VDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getVisitDateMonths()
  {
    return VMonths;
  }
/************************************************************************************************/
  public void setVisitDateMonths(String value)
  {
    VMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getVisitDateYears()
  {
    return VYears;
  }
/************************************************************************************************/
  public void setVisitDateYears(String value)
  {
    VYears = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  private void setDates()
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.setTime(Visit_Date==null?(new java.util.Date())
      :Visit_Date);
    VDays=cal.get(cal.DATE);
    VMonths=cal.get(cal.MONTH)+1;
    VYears=cal.get(cal.YEAR);
  }
/************************************************************************************************/
  public String getVisit_Date()
  {
    if (Visit_Date != null)
    {
      return SU.reformatDate(Visit_Date.toString());
    }
    else
    {
      return null;
    }
  }
/************************************************************************************************/
  public String getVisit_Date_Print()
  {
    if (Visit_Date != null)
    {
      String tempDate = SU.reformatDate(Visit_Date.toString(), "dd MMM yyyy");
      String tempDate2 = tempDate.substring(0, 6) + tempDate.substring(tempDate.length()-5);
      return tempDate2;
    }
    else
    {
      return null;
    }
  }
/************************************************************************************************/
  public void setVisit_Date(String newVisit_Date)
  {
    Visit_Date = SU.toJDBCDate(newVisit_Date);
  }
/************************************************************************************************/
  public String getContact_Name()
  {
    return Contact_Name;
  }
/************************************************************************************************/
  public void setContact_Name(String value)
  {
    Contact_Name = value;
  }
/************************************************************************************************/
  public void setContact_Telephone(String value)
  {
    Contact_Telephone = value;
  }
/************************************************************************************************/
  public String getContact_Telephone()
  {
    return Contact_Telephone;
  }
/************************************************************************************************/
  public void setContact_Mobile(String value)
  {
    Contact_Mobile = value;
  }
/************************************************************************************************/
  public String getContact_Mobile()
  {
    return Contact_Mobile;
  }
/************************************************************************************************/
  public void setContact_Fax(String value)
  {
    Contact_Fax = value;
  }
/************************************************************************************************/
  public String getContact_Fax()
  {
    return Contact_Fax;
  }
/************************************************************************************************/
  public void setContact_Email(String value)
  {
    Contact_Email = value;
  }
/************************************************************************************************/
  public String getContact_Email()
  {
    return Contact_Email;
  }
/************************************************************************************************/
  public void setFault_Details(String value)
  {
    Fault_Details = value;
  }
/************************************************************************************************/
  public String getFault_Details()
  {
    return Fault_Details;
  }
/************************************************************************************************/
  public String getDisplayCreatedDate()
  {
    return displayCreatedDate;
  }
/************************************************************************************************/
  public String getPrintCreatedDate()
  {
    return printCreatedDate;
  }
/************************************************************************************************/
  public String getDisplayAuthorisedDate()
  {
    return displayAuthorisedDate;
  }
/************************************************************************************************/
  public String getAuthorisor()
  {
    return Authorisor;
  }
/************************************************************************************************/
  public String getRejectVisibility()
  {
    if ((banStatus.equals("Rejected")) || (banStatus.equals("Returned")) ||
      (banStatus.equals("Implemented")))
    {
      return "visible";
    }
    else
    {
      return "hidden";
    }
  }
/************************************************************************************************/
  public void createReversal()
  {
    Customer_Info = "Reversal for Assure Charge " + banIdentifier + ".";
    Invoice_Amount = Invoice_Amount * -1;
    banIdentifier = "";
    banStatus = "Draft";
    setAction("Create");
    setMode("Add");
  }
/************************************************************************************************/
  public void setAccountDetails(String value)
  {
    Account_Id = value;
    if ((Billing_Source.equals("")) || (Billing_Source.equals("Unknown")))
      findBillingSource();
    findAccountName();
  }
/************************************************************************************************/
  public void setEngineer(String value)
  {
    Engineer = value;
  }
/************************************************************************************************/
  public String getEngineer()
  {
    return Engineer;
  }
/************************************************************************************************/
  public void setJob_Title(String value)
  {
    Job_Title = value;
  }
/************************************************************************************************/
  public String getJob_Title()
  {
    return Job_Title;
  }
/************************************************************************************************/
  public String getFDVis()
  {
    return fdVis;
  }
/************************************************************************************************/
  public String getVATRate()
  {
    return VAT_Rate;
  }
/************************************************************************************************/
  public Enumeration getVATAmounts()
  {
    float vatRate = Float.parseFloat(VAT_Rate);
    vatRate /= 100;
    float vatAmount = Invoice_Amount * vatRate;
    StringBuffer VAT_Amount = new StringBuffer(Float.toString(vatAmount));
    StringBuffer totalAmount = new StringBuffer(Float.toString(Invoice_Amount + vatAmount));
    if (VAT_Amount.charAt(VAT_Amount.length() - 2) =='.')
      VAT_Amount.append("0");
    if (totalAmount.charAt(VAT_Amount.length() - 2) =='.')
      totalAmount.append("0");

    Vector Result = new Vector();
    Result.addElement(VAT_Amount.toString());
    Result.addElement(totalAmount.toString());

    return Result.elements();
  }
/************************************************************************************************/
  public String getGeneralNo()
  {
    return General_No;
  }
/************************************************************************************************/
  public String getFaultNo()
  {
    return Fault_No;
  }
/************************************************************************************************/
  public String getFaultDetailsPrint()
  {
    String dets = "";
    try
    {
      SQL = "SELECT Text from eban..Ban_Reference_Data  " +
        "WHERE Datatype = 'Fault Code' " +
        "AND Value = '" + Fault_Code + "' "
        ;
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          dets = RS.getString(1);
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return dets.substring(4);
    }
  }
}