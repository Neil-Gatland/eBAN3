package DBUtilities;

import java.sql.*;
import java.math.BigInteger;
import java.util.*;
import JavaUtil.StringUtil;

public class GCBCustomerBANBean extends BANBean
{
    private String SQL;
    //private String Global_Customer_Name="";
    private String Product_Type="";
    private String DOB_Date="";
    private String gsrPrefix="";
    private String Customer_Contact="please call: +44(0) 1908 845750";
    private String[] alpha = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P",
      "Q","R","S","T","U","V","W","X","Y","Z"};
    private String[] alphaNum = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P",
      "Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9"};
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private java.sql.Date Required_BP_Start_Dateh;
    private int BEDays, BEMonths, BEYears, BPSDays, BPSMonths, BPSYears;
    private int billingFrequency;
    private String autoClose;
    private boolean isMANS;
    private String mansCustomer;
    private String fullMansCustomer;
    private String mansCustomerName;
    private String currentBusinessAnalyst;
    private String newBusinessAnalyst;
    private String multipleGCDs;
    private String gcds;
    private String origCustomerName;

  public GCBCustomerBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("Global_Customer_Name");
    mandatory.addElement("gsrPrefix");
    //mandatory.addElement("BAN_Summary");
    mandatory.addElement("BANEffectiveDateh");
    mandatory.addElement("BPStartDateh");
    mandatory.addElement("Product_Type");
    mandatory.addElement("DOB_Date");
    mandatory.addElement("Billing_Frequency");
    mandatory.addElement("Auto_Close");
    mandatory.addElement("Multiple_GCDs");
    errored.addElement("");
    billingFrequency=1;
    autoClose="N";
    isMANS=false;
    mansCustomer="";
    fullMansCustomer="";
    mansCustomerName="";
    currentBusinessAnalyst="";
    newBusinessAnalyst="";
    multipleGCDs="N";
    gcds=null;
    origCustomerName="";
  }

  public void Reset()
  {
    super.Reset();
    Global_Customer_Id="";
    Global_Customer_Name="";
    gsrPrefix="";
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    BPSDays=1; //default to 1st day of the month
    BPSMonths=0;
    BPSYears=0;
    Required_BP_Start_Dateh=null;
    Required_BAN_Effective_Dateh=null;
    Product_Type="";
    DOB_Date="";
    setErrored("clear");
    billingFrequency=1;
    autoClose="N";
    isMANS=false;
    mansCustomer="";
    fullMansCustomer="";
    mansCustomerName="";
    currentBusinessAnalyst="";
    newBusinessAnalyst="";
    multipleGCDs="N";
    gcds=null;
    origCustomerName="";
  }
  /*set Methods*/
  public void setMultipleGCDs(String value)
  {
    multipleGCDs = SU.isNull(value,"");
    if ((!Product_Type.equals("GM Billing")) && (multipleGCDs.equals("Y")))
    {
      multipleGCDs = "N";
    }
  }
  public void setGCDs(String value)
  {
   gcds = SU.isNull(value,"");
  }
  public void setAutoClose(String value)
  {
    autoClose = value;
  }
  public void setBillingFrequency(String value)
  {
    billingFrequency = value.equals("")?0:Integer.parseInt(value);
  }
  public void setDOBDate(String inDOB_Date)
  {
   DOB_Date = SU.isNull(inDOB_Date,"");
  }
  public void setProductType(String ProductType)
  {
    Product_Type = SU.isNull(ProductType,"");
    if ((!Product_Type.equals("GM Billing")) && (multipleGCDs.equals("Y")))
    {
      multipleGCDs = "N";
    }
    if (Product_Type.equals("Managed Services"))
    {
      isMANS=true;
      if (mandatory.indexOf("MANS_Customer") == -1)
      {
        mandatory.add("MANS_Customer");
      }
      if (mandatory.indexOf("gsrPrefix") != -1)
      {
        mandatory.remove("gsrPrefix");
      }
      if (mandatory.indexOf("Auto_Close") != -1)
      {
        mandatory.remove("Auto_Close");
      }
      gsrPrefix="XXXXX";
      autoClose="N";

    }
    else
    {
      isMANS=false;
      if (mandatory.indexOf("MANS_Customer") != -1)
      {
        mandatory.remove("MANS_Customer");
      }
      if (mandatory.indexOf("gsrPrefix") == -1)
      {
        mandatory.add("gsrPrefix");
      }
      if (mandatory.indexOf("Auto_Close") == -1)
      {
        mandatory.add("Auto_Close");
      }
      if (gsrPrefix.equals("XXXXX"))
        gsrPrefix="";
    }
  }
  public void setMANSCustomer(String value)
  {
    Global_Customer_Id = SU.isNull(value,"");
  }
  public void setMANSCustomerName(String value)
  {
    mansCustomerName = SU.isNull(value,"");
  }
  public void setGlobalCustomerName(String CustomerName)
  {
    Global_Customer_Name = SU.isNull(CustomerName,"");
  }
  public void setGSRPrefix(String gsr)
  {
   gsrPrefix = SU.isNull(gsr,"");
  }
  public boolean setGlobalCustomerIdfromName(String CustomerName)
  {

    if (!Mode.equals("Add"))
    {
      return true;
    }
    else if ((isMANS) && (!Global_Customer_Id.equals("")))
    {
      return true;
    }
    StringBuffer Abbreviation = new StringBuffer();
    String NamePart = "";

    if (CustomerName != null)
    {
      if ((isMANS) && (!CustomerName.equals("")))
        mansCustomerName = CustomerName;
      int i = 0;
      Global_Customer_Id=StringUtil.alphaNumericOnly(CustomerName);
      if (Global_Customer_Id.length() == 3)
      {
        if (isMANS)
        {
          while ((CustomerExists()) && (i < 26))
          {
            Global_Customer_Id=Global_Customer_Id.substring(0,2) + alpha[i];
            i++;
          }
          i = 0;
          while ((CustomerExists()) && (i < 35))
          {
            Global_Customer_Id=Global_Customer_Id.substring(0,1) + alphaNum[i] +
              Global_Customer_Id.substring(2,3);
            i++;
          }
          i = 0;
          while ((CustomerExists()) && (i < 35))
          {
            int j = 0;
            while ((CustomerExists()) && (j < 26))
            {
              Global_Customer_Id=Global_Customer_Id.substring(0,1) +
                alphaNum[i] + alpha[j];
              j++;
            }
            i++;
          }
        }
        else
        {
          i = 0;
          while ((CustomerExists()) && (i < 35))
          {
            Global_Customer_Id=Global_Customer_Id.substring(0,2) + alphaNum[i];
            i++;
          }
          i = 0;
          while ((CustomerExists()) && (i < 35))
          {
            Global_Customer_Id=Global_Customer_Id.substring(0,1) + alphaNum[i] +
              Global_Customer_Id.substring(2,3);
            i++;
          }
          i = 0;
          while ((CustomerExists()) && (i < 35))
          {
            int j = 0;
            while ((CustomerExists()) && (j < 35))
            {
              Global_Customer_Id=Global_Customer_Id.substring(0,1) +
                alphaNum[i] + alphaNum[j];
              j++;
            }
            i++;
          }
        }
      }
      else if (Global_Customer_Id.length() < 3)
      {
        if (Global_Customer_Id.length() == 1)
        {
          if (isMANS)
          {
            i = 0;
            while ((CustomerExists()) && (i < 35))
            {
              int j = 0;
              while ((CustomerExists()) && (j < 26))
              {
                Global_Customer_Id=Global_Customer_Id + alphaNum[i] + alpha[j];
                j++;
              }
              i++;
            }
          }
          else
          {
            i = 0;
            while ((CustomerExists()) && (i < 35))
            {
              int j = 0;
              while ((CustomerExists()) && (j < 35))
              {
                Global_Customer_Id=Global_Customer_Id + alphaNum[i] + alphaNum[j];
                j++;
              }
              i++;
            }
          }
        }
        else
        {
          if (isMANS)
          {
            i = 0;
            while ((CustomerExists()) && (i < 26))
            {
              Global_Customer_Id+=alpha[i];
              i++;
            }
          }
          else
          {
            i = 0;
            while ((CustomerExists()) && (i < 35))
            {
              Global_Customer_Id+=alphaNum[i];
              i++;
            }
          }
        }
      }
      else
      {//add a space to the string to force at least one token
        StringTokenizer st = new StringTokenizer(Global_Customer_Id+" ");
        while ((st.hasMoreTokens()) && (Abbreviation.length() < 3))
        {
          NamePart=st.nextToken();
          //if (!NamePart.startsWith("&"))
            Abbreviation.append(NamePart.substring(0,1));
        }
        if (Abbreviation.length()<3)
        {
          if (NamePart.length()>3-Abbreviation.length())
          {
            Abbreviation.append(NamePart.substring(1,1+3-Abbreviation.length()));
          }
          else if (NamePart.length()==3-Abbreviation.length())
          {
            Abbreviation.append(NamePart.substring(0,3-Abbreviation.length()));
          }
        }
        Global_Customer_Id = Abbreviation.toString().toUpperCase();
        i = 1;
        while ((CustomerExists()) && (i < NamePart.length()))
        {
          Global_Customer_Id = Global_Customer_Id.substring(0,2) +
            NamePart.charAt(i);
          i++;
        }
        if (isMANS)
        {
          i = 0;
          while ((CustomerExists()) && (i < 26))
          {
            Global_Customer_Id=Global_Customer_Id.substring(0,2) + alpha[i];
            i++;
          }
          i = 0;
          while ((CustomerExists()) && (i < 35))
          {
            Global_Customer_Id=Global_Customer_Id.substring(0,1) + alphaNum[i] +
              Global_Customer_Id.substring(2,3);
            i++;
          }
          i = 0;
          while ((CustomerExists()) && (i < 35))
          {
            int j = 0;
            while ((CustomerExists()) && (j < 26))
            {
              Global_Customer_Id=Global_Customer_Id.substring(0,1) +
                alphaNum[i] + alpha[j];
              j++;
            }
            i++;
          }
        }
        else
        {
          i = 0;
          while ((CustomerExists()) && (i < 35))
          {
            Global_Customer_Id=Global_Customer_Id.substring(0,2) + alphaNum[i];
            i++;
          }
          i = 0;
          while ((CustomerExists()) && (i < 35))
          {
            Global_Customer_Id=Global_Customer_Id.substring(0,1) + alphaNum[i] +
              Global_Customer_Id.substring(2,3);
            i++;
          }
          i = 0;
          while ((CustomerExists()) && (i < 35))
          {
            int j = 0;
            while ((CustomerExists()) && (j < 35))
            {
              Global_Customer_Id=Global_Customer_Id.substring(0,1) +
                alphaNum[i] + alphaNum[j];
              j++;
            }
            i++;
          }
        }
      }
      if (CustomerExists())
      {
        Message = "<font color=red><b>Unable to generate a unique GCID. Please contact systems support.";
        return false;
      }
      else
        return true;
    }
    else
    {
      Message = "<font color=red><b>Unable to generate a unique GCID. Please contact systems support.";
      return false;
    }
  }
  public void setErrored(String Item)
  {
    if (Item.startsWith("clear"))
      errored.clear();
    else
      errored.addElement(Item);
  }
  public void setAction(String value)
  {
    super.setAction(value);
    if (value.compareTo("Create")==0)
    {
      Reset();
      banIdentifier="";
    }
  }
  public void setCurrentBusinessAnalyst(String value)
  {
    currentBusinessAnalyst = value;
  }
  public void setNewBusinessAnalyst(String value)
  {
    newBusinessAnalyst = value;
  }
/*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.compareTo("Global_Customer_Name")==0)
    {
      setGlobalCustomerName(value);
      ScreenData.put("Global_Customer_Name",value);
    }
    else if (name.compareTo("gsrPrefix")==0)
    {
     setGSRPrefix(value);
     ScreenData.put("gsrPrefix",value);
    }
    else if (name.compareTo("Product_Type")==0)
    {
     setProductType(value);
     ScreenData.put("Product_Type",value);
    }
    else if (name.compareTo("DOB_Date")==0)
    {
     setDOBDate(value);
     ScreenData.put("DOB_Date",value);
    }
    else if (name.compareTo("BANEffectiveDateh")==0)
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
    else if (name.compareTo("BPStartDateh")==0)
    {
     setRequired_BP_Start_Date(value);
     ScreenData.put("BPStartDateh",value);
    }
    else if (name.compareTo("BPStartDateDay")==0)
    {
     setBPStartDateDays(value);
     ScreenData.put("BANEffectiveDateDay",value);
    }
    else if (name.compareTo("BPStartDateMonth")==0)
    {
     setBPStartDateMonths(value);
     ScreenData.put("BPStartDateMonth",value);
    }
    else if (name.compareTo("BPStartDateYear")==0)
    {
     setBPStartDateYears(value);
     ScreenData.put("BPStartDateYear",value);
    }
    else if (name.compareTo("BAN_Summary")==0)
    {
      setBAN_Summary(value);
      ScreenData.put("BAN_Summary",value);
    }
    else if (name.compareTo("Billing_Frequency")==0)
    {
      setBillingFrequency(value);
      ScreenData.put("Billing_Frequency",value);
    }
    else if (name.compareTo("Auto_Close")==0)
    {
      setAutoClose(value);
      ScreenData.put("Auto_Close",value);
    }
    else if (name.compareTo("MANS_Customer")==0)
    {
      setMANSCustomer(value);
      ScreenData.put("MANS_Customer",value);
    }
    else if (name.compareTo("MANS_Customer_Name")==0)
    {
      setMANSCustomerName(value);
      ScreenData.put("MANS_Customer_Name",value);
    }
    else if (name.compareTo("Current_BA")==0)
    {
      setCurrentBusinessAnalyst(value);
      ScreenData.put("Current_BA",value);
    }
    else if (name.compareTo("New_BA")==0)
    {
      setNewBusinessAnalyst(value);
      ScreenData.put("New_BA",value);
    }
    else if (name.compareTo("Multiple_GCDs")==0)
    {
      setMultipleGCDs(value);
      ScreenData.put("Multiple_GCDs",value);
    }
    else if (name.compareTo("GCDs")==0)
    {
      setGCDs(value);
      ScreenData.put("GCDs",value);
    }
}
/*******************************************************************************/
/*get Methods*/
  public String getMode(String FieldName)
  {//This defines whether or not a field is enterable

    if ((action.compareTo("Authorise") == 0) ||
      (action.compareTo("Delete") == 0))
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
  public String getCustomerVisibility(String value)
  {
    return "";
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
/******************************************************************************/
public boolean isValid(String ButtonPressed)
{
	Enumeration FormFields=mandatory.elements();
	setErrored("clear");
        Message = "<font color=red><b>";
	String FormField="";
	String FieldName;
	String value="";
        String mgcd=SU.isNull((String)ScreenData.get("Multiple_GCDs"),"");

        if (Mode.equals("Delete"))
        {
          String trialBillInvoices = DBA.customerHasTrialBill(Global_Customer_Id);
          if (!trialBillInvoices.equals("none"))
          {
            if (trialBillInvoices.equals("error"))
            {
              Message = "<font color=red><b>Database error selecting trial bill info.";
            }
            else
            {
              Message = "<font color=red><b>Trial bill(s) exist for this customer. Please close before deleting. " +
                "Invoice No(s) - " + trialBillInvoices.substring(0, trialBillInvoices.length()-1) ;
            }
            return false;
          }
          else if (DBA.customerHasCharges(Global_Customer_Id))
          {
            Message = "<font color=red><b>Valid Charges exist for this customer. Please cease these before deleting.";
            return false;
          }
          else if (DBA.customerHasSingleCharges(Global_Customer_Id, "02"))
          {
            Message = "<font color=red><b>Unbilled one-off charges exist for this customer. Please cease these before deleting.";
            return false;
          }
          else if (DBA.customerHasSingleCharges(Global_Customer_Id, "03"))
          {
            Message = "<font color=red><b>Credits exist for this customer. Please cease these before deleting.";
            return false;
          }
          else
          {
            return true;
          }
        }
	if ((ButtonPressed.startsWith("Submit")) &&
          (Mode.compareTo("Cease") !=0))
	{
	  while (FormFields.hasMoreElements())
	  {
	    FieldName=(String)FormFields.nextElement();
            if ((Mode.equals("Add")) || (FieldName.equals("DOB_Date")) ||
              (FieldName.equals("Auto_Close")))

            {
              if (isMANS)
              {
                if ((FieldName.equals("Global_Customer_Name")) ||
                  (FieldName.equals("MANS_Customer")))
                {
                  if ((SU.isNull((String)ScreenData.get("Global_Customer_Name"),"").equals("")) &&
                    (SU.isNull((String)ScreenData.get("MANS_Customer"),"").equals("")))
                  {
                    setErrored("Global_Customer_Name");
                    setErrored("MANS_Customer");
                  }
                  else if ((!SU.isNull((String)ScreenData.get("Global_Customer_Name"),"").equals("")) &&
                    (!SU.isNull((String)ScreenData.get("MANS_Customer"),"").equals("")))
                  {
                    setErrored("Global_Customer_Name");
                    setErrored("MANS_Customer");
                    Message = "<font color=red><b>Please select an existing customer from the list or enter the name of a new customer, not both";
                    return false;
                  }
                }
                else
                {
                  FormField=SU.isNull((String)ScreenData.get(FieldName),"");
                  if(FormField.equals(""))
                  {
                    setErrored(FieldName);
                  }
                }
              }
              else
              {
                FormField=SU.isNull((String)ScreenData.get(FieldName),"");
                if(FormField.compareTo("") == 0)
                {
                  if (FieldName.equals("BAN_GCD"))
                  {
                    if (mgcd.equals("Y"))
                      setErrored(FieldName);
                  }
                  else if (FieldName.equals("gsrPrefix"))
                  {
                    if (!mgcd.equals("Y"))
                      setErrored(FieldName);
                  }
                  else
                    setErrored(FieldName);
                }
              }
            }
            if (FieldName.equals("Global_Customer_Name"))
            {
              FormField=SU.isNull((String)ScreenData.get(FieldName),"");
              if ((!origCustomerName.trim().equals(FormField.trim())) &&
                (DBA.globalCustomerNameExists(FormField)))
              {
                setErrored("Global_Customer_Name");
                Message = "<font color=red><b>The customer name entered exists already. Please amend it.";
                return false;
              }
            }

	  }
          if (((gcds == null) || (gcds.equals("")) || (gcds.equals("null"))) && (mgcd.equals("Y")))
          {
            setErrored("BAN_GCD");
            Message = "<font color=red><b>Please enter at least one global customer division";
            return false;
          }
	        if ((Mode.equals("Amend")) && (isMANS) &&
            (!Global_Customer_Name.substring(0, 13).equals(origCustomerName.substring(0, 13))))
	        {
            Message = "<font color=red><b>The first 13 characters of a Managed Services Customer Name cannot be changed";
            setErrored("Global_Customer_Name");
            return false;
          }
        }
	else if ((ButtonPressed.startsWith("Save Draft")) &&
          (Mode.equals("Add")))
	{
          FormField=SU.isNull((String)ScreenData.get("Global_Customer_Name"),"");
          if(FormField.compareTo("") == 0)
          {
            setErrored("Global_Customer_Name");
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
/*******************************************************************************/
 public boolean getCustomerBAN()
 {
    boolean getCustomerBAN = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_GCB_Customer_BAN ";
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
            //Global_Customer_Id=RS.getString(2);
	    Required_BP_Start_Dateh=RS.getDate(4);
	    gsrPrefix=RS.getString(5);
            setProductType(RS.getString(6));
            if (isMANS)
            {
              fullMansCustomer=RS.getString(2);
              Global_Customer_Id=RS.getString(2).substring(0,3);
              mansCustomerName=RS.getString(3);
              Global_Customer_Name=mansCustomerName;
            }
            else
            {
              Global_Customer_Id=RS.getString(2);
              Global_Customer_Name=RS.getString(3);
            }
	    DOB_Date=RS.getString(7);
	    Required_BAN_Effective_Dateh=RS.getDate(8);
   	    BAN_Summary=RS.getString(9);
	    Mode=(RS.getString(10));
	    billingFrequency=(RS.getInt(11));
	    autoClose=(RS.getString(12));
            currentBusinessAnalyst=(RS.getString(13));
            newBusinessAnalyst=(RS.getString(14));
            multipleGCDs=(RS.getString(15));
            origCustomerName=(RS.getString(16));
            setDates();
            getCustomerBAN = true;
            if (multipleGCDs.equals("Y"))
            {
              getCustomerBAN = getBANGCDsFromTable();
            }
            else
            {
              getCustomerBAN = true;
            }
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
      int[] bpItems = setDateItems(Required_BP_Start_Dateh);
      BPSDays = bpItems[0];
      BPSMonths = bpItems[1];
      BPSYears = bpItems[2];
      int[] beItems = setDateItems(Required_BAN_Effective_Dateh);
      BEDays = beItems[0];
      BEMonths = beItems[1];
      BEYears = beItems[2];
      return getCustomerBAN;
    }
 }
/*******************************************************************************/
 public boolean getCustomer()
 {
    boolean getCustomer = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call givn..get_GCB_Customer ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,Global_Customer_Id);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
          int rows = 0;
          String gcdName = null;
	  while (RS.next())
	  {
	    Global_Customer_Name=RS.getString(1);
            origCustomerName=Global_Customer_Name;
	    Required_BP_Start_Dateh=RS.getDate(2);
	    gsrPrefix=RS.getString(3);
            setProductType(RS.getString(4));
	    DOB_Date=RS.getString(5);
	    billingFrequency=RS.getInt(6);
	    autoClose=RS.getString(7);
	    gcdName=RS.getString(8);
	    //multipleGCDs=RS.getString(8);
            //getCustomer = true;
            rows++;
          }
          setDates();
          if (rows > 1)
          {
            multipleGCDs = "Y";
            getCustomer = getGCDsFromTable();
          }
          else
          {
            multipleGCDs = "N";
            String temp = "";
            int pos = gcdName.indexOf("(");
            if (pos == -1)
            {
              temp = gcdName;
            }
            else
            {
              temp = gcdName.substring(0, pos-1);
            }
            gcds = temp+"|"+gsrPrefix+",";
            getCustomer = true;
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
      return getCustomer;
    }
 }
/*****************************************************************************************/
public boolean createCustomerBAN()
{
  boolean createCustomerBAN = false;
  int rowcount=0,i=0;
  String baLoginId = ((Mode.equals("Amend")) &&
    (currentBusinessAnalyst != newBusinessAnalyst) &&
    (newBusinessAnalyst != ""))?newBusinessAnalyst:null;
  if (!Mode.equals("Amend"))
  {
    origCustomerName = isMANS?mansCustomerName:Global_Customer_Name;
  }

    try{
	  if (DBA.Connect(WRITE,P5))
	  {
	    SQL = "{call eban..Create_GCB_Customer_BAN " +
	      "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	    cstmt = DBA.Conn.prepareCall(SQL);
	    cstmt.setString(1,banStatus);
	    //cstmt.setString(2,((Mode.equals("Amend"))&&(isMANS))?Global_Customer_Id.substring(0,3):Global_Customer_Id);
	    cstmt.setString(2,Global_Customer_Id);
	    cstmt.setString(3,Mode.equals("Amend")?Global_Customer_Name:isMANS?mansCustomerName:Global_Customer_Name);
	    cstmt.setDate(4,Required_BP_Start_Dateh);
	    cstmt.setString(5,gsrPrefix);
	    cstmt.setString(6,Product_Type);
	    cstmt.setString(7,DOB_Date);
	    cstmt.setString(8,BAN_Summary);
	    cstmt.setDate(9,Required_BAN_Effective_Dateh);
	    cstmt.setString(10,Mode);
	    cstmt.setString(11,banCreatedBy);
            cstmt.setInt(12,billingFrequency);
	    cstmt.setString(13,autoClose);
            cstmt.setInt(14,isMANS?1:0);
	    cstmt.setString(15,Mode.equals("Amend")?currentBusinessAnalyst:null);
	    cstmt.setString(16,Mode.equals("Amend")?baLoginId:userId);
	    cstmt.setString(17,multipleGCDs);
	    cstmt.setString(18,gcds);
            cstmt.setString(19,origCustomerName);

	    try
	    {
	      cstmt.execute();
	      RS = cstmt.getResultSet();
	      if (RS.next())
	      {
	        banIdentifier=RS.getString(1);
                createCustomerBAN = true;
                StringBuffer msg = new StringBuffer("<font color=blue><b>BAN Id:-"+banIdentifier+" created");
                if ((Mode.equals("Amend")) && (!Global_Customer_Name.equals(origCustomerName)))
                {
                  msg.append(". Any existing trial bills will need to be rerun to reflect the customer name change.");
                  if (multipleGCDs.equals("Y"))
                  {
                    msg.append(" Please also check Global Customer Division names for relevance.");
                  }
                }
                Message = msg.toString();
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
      return createCustomerBAN;
    }
  }
/**********************************************************************************************/
  public boolean updateCustomerBAN()
  {
    boolean updateCustomerBAN = false;
    int rowcount=0;
    Enumeration Customer_Billing_Address_UnPacked;
    int i=0;

    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..update_GCB_Customer_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
        cstmt.setString(2,banStatus);
        cstmt.setString(3,Global_Customer_Id);
        cstmt.setString(4,isMANS?mansCustomerName:Global_Customer_Name);
        cstmt.setDate(5,Required_BP_Start_Dateh);
        cstmt.setString(6,gsrPrefix);
        cstmt.setString(7,Product_Type);
        cstmt.setString(8,DOB_Date);
        cstmt.setString(9,BAN_Summary);
        cstmt.setDate(10,Required_BAN_Effective_Dateh);
	cstmt.setString(11,rejectReason);
        cstmt.setString(12,banCreatedBy);
        cstmt.setInt(13,billingFrequency);
        cstmt.setString(14,autoClose);
        cstmt.setInt(15,isMANS?1:0);
        cstmt.setString(16,currentBusinessAnalyst);
        cstmt.setString(17,newBusinessAnalyst);
        cstmt.setString(18,multipleGCDs);
	cstmt.setString(19,gcds);

        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          String newBanIdentifier=RS.getString(1);
          if (newBanIdentifier.startsWith("error"))
          {
            Message = "<font color=red><b>Unable to update BAN. Please contact " +
              "systems support, quoting " + newBanIdentifier + ".";
          }
          else
          {
            updateCustomerBAN = true;
            StringBuffer msg = new StringBuffer("<font color=blue><b>BAN Id:-"+banIdentifier+" "+action+
              (action.endsWith("e")?"d":"ed"));
            if ((Mode.equals("Amend")) && (!Global_Customer_Name.equals(origCustomerName)))
            {
              msg.append(". Any existing trial bills will need to be rerun to reflect the customer name change.");
              if (multipleGCDs.equals("Y"))
              {
                msg.append(" Please also check Global Customer Division names for relevance.");
              }
            }
            if (!banIdentifier.equals(newBanIdentifier))
            {
              msg.append(" </font><br><font color=red>(WARNING! Amendment " +
                "has caused BAN Id to change to " +
                newBanIdentifier + " )");
            }
            Message = msg.toString();
          }
        }
        /*cstmt.execute();
        rowcount=cstmt.getUpdateCount();
	if (rowcount != 1)
	{
	  Message="Error! - "+Integer.toString(rowcount) + " Rows Updated";
	}
        else
        {
          updateCustomerBAN = true;
          Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" "+action+
            (action.endsWith("e")?"d":"ed");
        }*/
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
      return updateCustomerBAN;
    }
  }
/*********************************************************************/
public boolean AuthoriseCustomerBAN()
{
    boolean AuthoriseCustomerBAN = false;
    try{
      if (DBA.Connect(WRITE,P3))
      {
        String bpsd = SU.DateToString(Required_BP_Start_Dateh);
        String bps2 = SU.DateToString(Required_BP_Start_Dateh, "dd MM yyyy");
        java.util.Date now = new java.util.Date();
        java.sql.Date today = new java.sql.Date(now.getTime());

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(Required_BP_Start_Dateh);

        String heute = SU.DateToString(today, "dd MM yyyy");
        String startOfMonth = heute.substring(6, 10) + heute.substring(3, 5) +
          "01";
        String reverseStart = bps2.substring(6, 10) + bps2.substring(3, 5) +
          "01";
        String backBilling = reverseStart.compareTo(startOfMonth)<0?"Y":"N";
        String startDay = bpsd.substring(0, 2);
        int sd = cal.get(cal.DATE);
        String start = startDay + " " + bpsd.substring(3, 6) + " " +
          bpsd.substring(7, 11);
        String end = null;
        if (sd == 1)
        {
          end = Integer.toString(cal.getActualMaximum(cal.DAY_OF_MONTH)) +
          " " + bpsd.substring(3, 6) + " " + bpsd.substring(7, 11);
        }
        else
        {
          java.util.Calendar cal2 = cal;
          cal2.add(cal2.DATE, -1);
          cal2.add(cal2.MONTH, 1);
          String ed = SU.DateToString(cal2.getTime());
          end = ed.substring(0, 2) + " " + ed.substring(3, 6) + " " +
            ed.substring(7, 11);
        }
        if (Mode.equals("Add"))
        {
	  SQL = "{call givn..Create_GCB_Customer " +
	    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	  cstmt = DBA.Conn.prepareCall(SQL);
	  cstmt.setString(1,isMANS?banIdentifier:Global_Customer_Id);
	  cstmt.setString(2,isMANS?mansCustomerName.replace(',', ' '):Global_Customer_Name.replace(',', ' '));
	  cstmt.setString(3,start);
	  cstmt.setString(4,end);
	  cstmt.setString(5,backBilling);
	  cstmt.setString(6,gsrPrefix);
	  cstmt.setString(7,Product_Type);
	  cstmt.setString(8,DOB_Date);
	  cstmt.setString(9,banIdentifier);
	  cstmt.setString(10,banCreatedBy);
	  cstmt.setInt(11,billingFrequency);
	  cstmt.setString(12,autoClose);
          cstmt.setString(13,multipleGCDs);
          cstmt.setString(14,gcds);
	}
        else if (Mode.equals("Delete"))
        {
	  SQL = "{call givn..Cease_GCB_Customer (?)}";

	  cstmt = DBA.Conn.prepareCall(SQL);
	  cstmt.setString(1,banIdentifier);
        }
        else
        {
	  SQL = "{call givn..Update_GCB_Customer (?)}";

	  cstmt = DBA.Conn.prepareCall(SQL);
	  cstmt.setString(1,banIdentifier);
        }
        try
	{

	  cstmt.execute();
          RS = cstmt.getResultSet();
          StringBuffer msg = new StringBuffer();
          if (RS.next())
          {
            int retcode=RS.getInt(1);
            if ((retcode == 0) || (retcode == 4))
            {
              AuthoriseCustomerBAN = true;
              //Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
              msg.append("<font color=blue><b>You have successfully implemented BAN Id :-"+
                banIdentifier + ".");
              if (retcode == 4)
              {
                msg.append("</b></font><font color=red><b> Any existing trial bills will need to be rerun to reflect the customer name change.");
                if (multipleGCDs.equals("Y"))
                {
                  msg.append(" Please also check Global Customer Division names for relevance.");
                }
              }
            }
            else if ((retcode == 2) || (retcode == 6))
            {
              AuthoriseCustomerBAN = true;
              msg.append("<font color=red><b>BAN Id:-"+banIdentifier+" created " +
                "but back-billing not set-up. Please contact Systems Support.");
              if (retcode == 6)
              {
                msg.append(" Any existing trial bills will need to be rerun to reflect the customer name change.");
                if (multipleGCDs.equals("Y"))
                {
                  msg.append(" Please also check Global Customer Division names for relevance.");
                }
              }
            }
            else if ((retcode == 3) || (retcode == 7))
            {
              AuthoriseCustomerBAN = true;
              msg.append("<font color=red><b>BAN Id:-"+banIdentifier+" created " +
                "but at least one GCD selected for deletion has charges " +
                "and cannot be deleted.");
              if (retcode == 7)
              {
                msg.append(" Any existing trial bills will need to be rerun to reflect the customer name change.");
                if (multipleGCDs.equals("Y"))
                {
                  msg.append(" Please also check Global Customer Division names for relevance.");
                }
              }
            }
            else if (retcode == -99)
            {
              msg.append("<font color=red><b>Unable to authorise BAN Id:-" +
                banIdentifier + ". A bill job is currently running for this customer.");
            }
            else
            {
              msg.append("<font color=red><b>Unable to authorise BAN Id:-" +
                banIdentifier + ". Please contact Systems Support.");
            }
          }
          else
          {
            msg.append("<font color=red><b>Unable to authorise BAN Id:-" +
              banIdentifier + ". Please contact Systems Support.");
          }
          Message = msg.toString();
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
      return AuthoriseCustomerBAN;
    }
}
 /********************************************************************/
  public String getCustomerBanList()
  {
	int counter=0;
	StringBuffer grid = new StringBuffer();
	String RadioButton;
	int rowcount;
	BigInteger rows = new BigInteger("0");
	String gridClass;
        Message="";

	//setHeader();

	//RadioButton="<td width=100 bgcolor=lightblue ><INPUT type='radio' id='Select' name='Select' ";
	RadioButton="width=45 align=center><img src=\"../nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";
    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border ="+border+">");
	rowcount = 0;

        SQL = "{call eban..listBANs ";
        SQL += "(?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,action);
	cstmt.setString(2,OrderBy);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
	  while (RS.next())
	  {
	    rowcount++;
	    rows = BigInteger.valueOf(rowcount);
	    //Alternate row colours
	    if (rows.testBit(0))
	    {//An odd number
	      gridClass="grid1 ";
	    }
	    else
	    {
	      gridClass="grid2 ";
	    }

	    for(counter=1;counter<columns+1;counter++)
	    {
	      grid.append("<td class="+gridClass+RS.getString(counter)+"</td>");
	    }
	    //Add the extra generated column for the radio button
	    grid.append("<td class="+gridClass+RadioButton+RS.getString(counter)+"')\"></td></tr>");
	    //End the table
	  }
	  grid.append("</table>");
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
      if (Message.equals(""))
        return grid.toString();
      else
        return Message;
    }
  }
/************************************************************************************************/
  public String getRequired_BP_Start_Date()
  {
    if (Required_BP_Start_Dateh != null)
    {
      return SU.reformatDate(Required_BP_Start_Dateh.toString());
    }
    else
    {
      return null;
    }
  }
/************************************************************************************************/
  public String getGSRPrefix()
  {
    return gsrPrefix;
  }
/************************************************************************************************/
  public String getAutoClose( )
  {
    return autoClose;
  }
/************************************************************************************************/
  public String getDOBDate()
  {
    return DOB_Date;
  }
/************************************************************************************************/
  public String getProductType()
  {
    return Product_Type;
  }
/************************************************************************************************/
  public void setRequired_BP_Start_Date(String newRequired_BP_Start_Date)
  {
    Required_BP_Start_Dateh = SU.toJDBCDate(newRequired_BP_Start_Date);
  }
/************************************************************************************************/
  public int getBPStartDateDays()
  {
    return BPSDays;
  }
/************************************************************************************************/
  public void setBPStartDateDays(String value)
  {
    BPSDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getBPStartDateMonths()
  {
    return BPSMonths;
  }
/************************************************************************************************/
  public void setBPStartDateMonths(String value)
  {
    BPSMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getBPStartDateYears()
  {
    return BPSYears;
  }
/************************************************************************************************/
  public void setBPStartDateYears(String value)
  {
    BPSYears = value.equals("")?0:Integer.parseInt(value);
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
  public int getBillingFrequency()
  {
    return billingFrequency;
  }
/************************************************************************************************/
  public void setBANEffectiveDateYears(String value)
  {
    BEYears = value.equals("")?0:Integer.parseInt(value);
  }
/****************************************************************************************/
  public String getSelectMode(String field)
  {
    return super.getSelectMode();
  }
/****************************************************************************************/
  public String getInputMode(String field)
  {
    return super.getInputMode();
  }
/****************************************************************************************/
  public String getInputMode()
  {
    if (Mode.equals("Amend"))
      return "READONLY";
    else
      return super.getInputMode();
  }
/****************************************************************************************/
  public String getSelectMode()
  {
    if (Mode.equals("Amend"))
      return "DISABLED";
    else
      return super.getSelectMode();
  }
/****************************************************************************************/
  public boolean getIsMANS()
  {
    return isMANS;
  }
/****************************************************************************************/
  public String getMANSCustomer()
  {
    return Global_Customer_Id;
  }
/****************************************************************************************/
  public String getFullMANSCustomer()
  {
    return fullMansCustomer;
  }
/****************************************************************************************/
  public String getGlobalCustomerName()
  {
    if (isMANS)
    {
      return Global_Customer_Name;
    }
    else
    {
      return super.getGlobalCustomerName();
    }
  }
/****************************************************************************************/
  public String getMANSCustomerName()
  {
    return mansCustomerName;
  }
/****************************************************************************************/
  public String getCurrentBusinessAnalyst()
  {
    return currentBusinessAnalyst;
  }
/****************************************************************************************/
  public String getNewBusinessAnalyst()
  {
    return newBusinessAnalyst;
  }
/****************************************************************************************/
  public String getMultipleGCDs()
  {
   return multipleGCDs;
  }
/****************************************************************************************/
  public String getGCDs()
  {
   return gcds;
  }
/****************************************************************************************/
  public String getGCDListBox()
  {
    String listBox = null;
    listBox = DBA.getListBox("BAN_GCD",InputMode,"", banIdentifier,
      3,"style=\"height:80;width:350\"",false);
    if (listBox.indexOf("</SELECT>") == -1)
    {
      StringBuffer sb = new StringBuffer(listBox);
      int start = 0;
      if (gcds != null)
      {
        int pos = gcds.indexOf(",");
        while (pos > 0)
        {
          String thisgcd = gcds.substring(start, pos);
          int pos2 = thisgcd.indexOf("|");
          String gcdName = thisgcd.substring(0, pos2);
          String gsr = thisgcd.substring(pos2+1);
          String display = null;
          if (gcdName.indexOf(" (" + gsr + ")") != -1)
          {
            display = gcdName;
          }
          else
          {
            display = gcdName + " (" + gsr + ")";
          }
          sb.append("<option value=\""+thisgcd+"\">");
          sb.append(display);
          sb.append("</option>");
          start = ++pos;
          pos = gcds.indexOf(",", start);
        }
      }
      sb.append("</SELECT>");
      listBox = sb.toString();
    }
    return listBox;
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
    cal.setTime(Required_BP_Start_Dateh==null?(new java.util.Date())
      :Required_BP_Start_Dateh);
    BPSDays=cal.get(cal.DATE);
    BPSMonths=cal.get(cal.MONTH)+1;
    BPSYears=cal.get(cal.YEAR);
  }
/*************************************************************************************/
  private boolean getBANGCDsFromTable()
  {
    boolean getGCDsFromTable = false;
    StringBuffer sb = new StringBuffer();
    try
    {
      SQL = "SELECT GCD_Name, GSR_Prefix FROM eban..GCB_Cust_BAN_GCD " +
        "WHERE BAN_Identifier='" + banIdentifier + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          sb.append(RS.getString(1)+"|"+RS.getString(2)+",");
        }
        gcds=sb.toString();
        getGCDsFromTable = true;
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      gcds="";
    }
    finally
    {
      close();
      return getGCDsFromTable;
    }
  }
/*************************************************************************************/
  private boolean getGCDsFromTable()
  {
    boolean getGCDsFromTable = false;
    StringBuffer sb = new StringBuffer();
    try
    {
      SQL = "SELECT Global_Customer_Division_Name, Global_Srv_Reference_Prefix FROM givn..Global_Customer_Division " +
        "WHERE Global_Customer_Id='" + Global_Customer_Id + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          sb.append(RS.getString(1)+"|"+RS.getString(2)+",");
        }
        gcds=sb.toString();
        getGCDsFromTable = true;
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      gcds="";
    }
    finally
    {
      close();
      return getGCDsFromTable;
    }
  }
/*******************************************************************************/
  public boolean CustomerExists()
  {
    if (Global_Customer_Id.equalsIgnoreCase("KJG"))
    {
      return true;
    }
    SQL = "Select 'Found' from givn..Global_Customer " +
      "where Global_Customer_Id like '" + Global_Customer_Id + "%' Union " +
      "Select 'Found' from eBAN..GCB_Customer_BAN " +
      "where Global_Customer_Id like '" + Global_Customer_Id + "%' ";

    if(DBA.getExists(SQL,P3))
    //Datasource changes if(DBA.getExists(SQL))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

}
