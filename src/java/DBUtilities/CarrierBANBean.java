package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class CarrierBANBean extends BANBean
{

    private String SQL;
    private String Carrier_Billing_Address_Lines[]={"","","",""};
    private String Carrier_Contact_Name="",Comments="";
    private String Carrier_Country="";

    private String Bank_Account_No="",Bank_Address="",Local_Tax_Rate="0.00";
    private String Carrier_Billing_Address="";
    private String VAS_List_No="",General_Ledger_Code="",Tax_Precision="";
    private Hashtable ScreenData=new Hashtable();
    private int Carrier_Id=0;
    protected Vector mandatory=new Vector();

  public CarrierBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("BAN_Summary");
    mandatory.addElement("Carrier_Name");
    mandatory.addElement("Carrier_Contact_Name");
    mandatory.addElement("Required_BAN_Effective_Dateh");
    mandatory.addElement("Carrier_Billing_Address");
    mandatory.addElement("Currency_Desc");
    mandatory.addElement("General_Ledger_Code");
    mandatory.addElement("Bank_Account_No");
    mandatory.addElement("Bank_Address");
    mandatory.addElement("Local_Tax_Rate");
    errored.addElement("");
  }

  public void Reset()
  {
    super.Reset();
    Carrier_Billing_Address_Lines[0]="";
    Carrier_Billing_Address_Lines[1]="";
    Carrier_Billing_Address_Lines[2]="";
    Carrier_Billing_Address_Lines[3]="";
    Carrier_Contact_Name="";
    Comments="";
    Carrier_Country="";
    Bank_Account_No="";
    Bank_Address="";
    Local_Tax_Rate="0.00";
    Carrier_Billing_Address="";
    VAS_List_No="";
    General_Ledger_Code="";
    Tax_Precision="";
    Carrier_Id=0;
  }
/*set Methods*/
  public void setCarrier_Contact_Name(String newCarrier_Contact_Name)
  {
   Carrier_Contact_Name = SU.isNull(newCarrier_Contact_Name,"");
  }
  public void setCarrier_Country(String value)
  {
   Carrier_Country = SU.isNull(value,"");
  }
  public void setCarrier_Billing_Address(String value)
  {
    Carrier_Billing_Address=value;
  }
  public void setBank_Account_No( String value)
  {
    Bank_Account_No=value;
  }
  public void setBank_Address( String value)
  {
    Bank_Address=value;
  }
  public void setLocal_Tax_Rate(String value)
  {
    Local_Tax_Rate=value;
  }
  public void setErrored(String Item)
  {
    if (Item.startsWith("clear"))
      errored.clear();
    else
      errored.addElement(Item);
  }
  public void setVAS_List_No(String value)
  {
   VAS_List_No = SU.isNull(value,"");
  }
  public void setTax_Precision(String value)
  {
   Tax_Precision = SU.isNull(value,"");
  }
  public void setGeneral_Ledger_Code(String value)
  {
   General_Ledger_Code = SU.isNull(value,"");
  }
  public void setComments(String value)
  {
   Comments = SU.isNull(value,"");
  }
  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.compareTo("Carrier_Name")==0)
    {
      setCarrier_Name(value);
      ScreenData.put("Carrier_Name",value);
    }
    else if (name.compareTo("Carrier_Contact_Name")==0)
    {
      setCarrier_Contact_Name(value);
      ScreenData.put("Carrier_Contact_Name",value);
    }
    else if (name.compareTo("Required_BAN_Effective_Dateh")==0)
    {
     setRequired_BAN_Effective_Date(value);
     ScreenData.put("Required_BAN_Effective_Dateh",value);
    }
    else if (name.compareTo("Currency_Desc")==0)
    {
      setCurrency_Desc(value);
      ScreenData.put("Currency_Desc",value);
    }
    else if (name.compareTo("Comments")==0)
    {
      setComments(value);
      ScreenData.put("Comments",value);
    }
    else if (name.compareTo("Carrier_Country")==0)
    {
      setCarrier_Country(value);
      ScreenData.put("Carrier_Country",value);
    }
    else if (name.compareTo("Carrier_Billing_Address")==0)
    {
      setCarrier_Billing_Address(value);
      ScreenData.put("Carrier_Billing_Address",value);
    }
    else if (name.compareTo("BAN_Summary")==0)
    {
      setBAN_Summary(value);
      ScreenData.put("BAN_Summary",value);
    }
    else if (name.compareTo("BAN_Reason")==0)
    {
      setBAN_Reason(value);
    }
    else if (name.compareTo("Bank_Account_No")==0)
    {
      setBank_Account_No(value);
      ScreenData.put("Bank_Account_No",value);
    }
    else if (name.compareTo("Bank_Address")==0)
    {
      setBank_Address(value);
      ScreenData.put("Bank_Address",value);
    }
    else if (name.compareTo("Local_Tax_Rate")==0)
    {
      setLocal_Tax_Rate(value);
      ScreenData.put("Local_Tax_Rate",value);
    }
    else if (name.compareTo("Tax_Precision")==0)
    {
      setTax_Precision(value);
      ScreenData.put("Tax_Precision",value);
    }
    else if (name.compareTo("VAS_List_No")==0)
    {
      setVAS_List_No(value);
      ScreenData.put("VAS_List_No",value);
    }
    else if (name.compareTo("General_Ledger_Code")==0)
    {
      setGeneral_Ledger_Code(value);
      ScreenData.put("General_Ledger_Code",value);
    }
}
/*******************************************************************************/
/*get Methods*/
  public String getCarrier_Contact_Name()
  {
    return Carrier_Contact_Name;
  }
  public String getCarrier_Billing_Address()
  {
    return Carrier_Billing_Address;
  }
  public String getCarrier_Country()
  {
    return Carrier_Country;
  }
  public String getComments()
  {
    return Comments;
  }
  public String getBank_Account_No()
  {
    return Bank_Account_No;
  }
  public String getBank_Address()
  {
    return Bank_Address;
  }
  public String getLocal_Tax_Rate()
  {
    return Local_Tax_Rate;
  }
  public String getVAS_List_No()
  {
    return VAS_List_No;
  }
  public String getTax_Precision()
  {
    return Tax_Precision;
  }
  public String getGeneral_Ledger_Code()
  {
    return General_Ledger_Code;
  }
  public String getMode(String FieldName)
  {
    if ((Mode.compareTo("Amend")==0) && (FieldName.compareTo("Carrier_Name")== 0))
    {
      return "READONLY";
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

	if ((ButtonPressed.startsWith("Submit")) && (Mode.compareTo("Cease") !=0))
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
	if ((Local_Tax_Rate.compareTo("") != 0) &&
	    (SU.toFloat(Local_Tax_Rate) == -999999999))
	{
	  setErrored("Local_Tax_Rate");
          Message = "<font color=red><b>Local Tax Rate must be numeric (decimals permitted)";
	  return false;
	}
	if ((General_Ledger_Code.compareTo("") != 0) &&
	    (SU.toInt(General_Ledger_Code) == NOT_INT))
	{
	  setErrored("General_Ledger_Code");
          Message = "<font color=red><b>General Ledger Code must be numeric";
	  return false;
	}
	if ((Mode.compareTo("Create")==0) && (CarrierExists()))
	{
	  setErrored("Carrier_Name");
	  Message = "<font color=red><b>A Carrier with this name already exists";
	  return false;
	}
	if ((!errored.isEmpty()) && (errored.size() > 0))
	{
	    Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
	    return false;
	}
	else
	  return true;
}
/********************************************************************/
public boolean CarrierExists()
{

  SQL = "Select 'Found' from OSS..Carrier where Carrier_Name = '"+ Carrier_Name+ "'";

  if(DBA.getExists(SQL,P5))
  //Datasource changes if(DBA.getExists(SQL))
  {
    return true;
  }
  else
  {
    return false;
  }
}

/*******************************************************************************/
 public boolean getCarrierBAN()
 {
    boolean getCarrierBAN = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_Carrier_BAN ";
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
	    Required_BAN_Effective_Dateh=RS.getDate(2);
   	    BAN_Summary=RS.getString(3);
    	    BAN_Reason=RS.getString(4);
	    Carrier_Contact_Name=RS.getString(5);
	    Currency_Desc=RS.getString(6);
	    Comments=RS.getString(7);
	    Carrier_Country=RS.getString(8);
	    Carrier_Name=RS.getString(9);
	    Carrier_Billing_Address_Lines[0]=RS.getString(10);
	    Carrier_Billing_Address_Lines[1]=RS.getString(11);
	    Carrier_Billing_Address_Lines[2]=RS.getString(12);
	    Carrier_Billing_Address_Lines[3]=RS.getString(13);
	    VAS_List_No=RS.getString(14);
	    Bank_Account_No=RS.getString(15);
	    Bank_Address=RS.getString(16);
	    Local_Tax_Rate=RS.getString(17);
	    General_Ledger_Code=RS.getString(18);
	    Tax_Precision=RS.getString(19);
	    rejectReason=RS.getString(20);
	    Mode=RS.getString(21);
	    Carrier_Id=RS.getInt(22);
            getCarrierBAN = true;
	  }
	  Carrier_Billing_Address=SU.packTextArea(Carrier_Billing_Address_Lines);
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
      return getCarrierBAN;
    }
 }
/*****************************************************************************************/
public boolean createCarrierBAN()
{
  boolean createCarrierBAN = false;
  Enumeration Carrier_Billing_Address_UnPacked;
  int rowcount=0,i=0;

      if (Carrier_Billing_Address.compareTo("") !=0 )
      {
	  Carrier_Billing_Address_UnPacked = SU.unpackTextArea(Carrier_Billing_Address);

	  while ((Carrier_Billing_Address_UnPacked.hasMoreElements()) && (i < 4))
	  {
	    Carrier_Billing_Address_Lines[i]=(String)Carrier_Billing_Address_UnPacked.nextElement();
	    i++;
	  }
      }
      while (i < 4)
      {
	Carrier_Billing_Address_Lines[i]="";
	i++;
      }

    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Create_Carrier_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banStatus);
	cstmt.setString(2,BAN_Summary);
	cstmt.setDate(3,Required_BAN_Effective_Dateh);
	cstmt.setString(4,BAN_Reason);
	cstmt.setString(5,Carrier_Contact_Name );
	cstmt.setString(6,Currency_Desc );
	cstmt.setString(7,Comments );
	cstmt.setString(8,Carrier_Country );
	cstmt.setString(9,Carrier_Name);
	cstmt.setString(10,Carrier_Billing_Address_Lines[0] );
	cstmt.setString(11,Carrier_Billing_Address_Lines[1] );
	cstmt.setString(12,Carrier_Billing_Address_Lines[2]);
	cstmt.setString(13,Carrier_Billing_Address_Lines[3] );
	cstmt.setString(14,VAS_List_No);
	cstmt.setString(15,Bank_Account_No);
	cstmt.setString(16,Bank_Address);
	cstmt.setString(17,Local_Tax_Rate);
	cstmt.setString(18,General_Ledger_Code);
	cstmt.setString(19,Tax_Precision);
	cstmt.setString(20,Mode);
	cstmt.setString(21,banCreatedBy);
	cstmt.setInt(22,Carrier_Id);

	try
	{
          cstmt.execute();
	  RS = cstmt.getResultSet();
	  if (RS.next())
	  {
	    banIdentifier=RS.getString(1);
            createCarrierBAN = true;
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
      return createCarrierBAN;
    }
  }
/**********************************************************************************************/
  public boolean updateCarrierBAN()
  {
    boolean updateCarrierBAN = false;
    int rowcount=0;
    Enumeration Carrier_Billing_Address_UnPacked;
    int i=0;

      if (Carrier_Billing_Address.compareTo("") !=0 )
      {
	  Carrier_Billing_Address_UnPacked = SU.unpackTextArea(Carrier_Billing_Address);

	  while ((Carrier_Billing_Address_UnPacked.hasMoreElements()) && (i < 4))
	  {
	    Carrier_Billing_Address_Lines[i]=(String)Carrier_Billing_Address_UnPacked.nextElement();
	    i++;
	  }
      }
      while (i < 4)
      {
	Carrier_Billing_Address_Lines[i]="";
	i++;
      }


    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..update_Carrier_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
        cstmt.setString(2,banStatus);
	cstmt.setString(3,BAN_Summary);
	cstmt.setDate(4,Required_BAN_Effective_Dateh);
	cstmt.setString(5,BAN_Reason);
	cstmt.setString(6,Carrier_Contact_Name );
	cstmt.setString(7,Currency_Desc );
	cstmt.setString(8,Comments );
	cstmt.setString(9,Carrier_Country );
	cstmt.setString(10,Carrier_Name);
	cstmt.setString(11,Carrier_Billing_Address_Lines[0] );
	cstmt.setString(12,Carrier_Billing_Address_Lines[1] );
	cstmt.setString(13,Carrier_Billing_Address_Lines[2]);
	cstmt.setString(14,Carrier_Billing_Address_Lines[3] );
	cstmt.setString(15,VAS_List_No);
	cstmt.setString(16,Bank_Account_No);
	cstmt.setString(17,Bank_Address);
	cstmt.setString(18,Local_Tax_Rate);
	cstmt.setString(19,General_Ledger_Code);
	cstmt.setString(20,Tax_Precision);
	cstmt.setString(21,banCreatedBy);
	cstmt.setString(22,rejectReason);

        cstmt.execute();
        String tempId = banIdentifier;
        Reset();
        updateCarrierBAN = true;
        Message = "<font color=blue><b>BAN Id:-"+tempId+" updated";
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
      return updateCarrierBAN;
    }
  }
/*********************************************************************/
public boolean ImplementCarrierBAN()
{

  SQL = "exec eban..Implement_Carrier_BAN '"+ banIdentifier+ "'";

  if(DBA.NoResult(SQL,P5))
  {
    return true;
  }
  else
  {
    Message=DBA.getMessage();
    return false;
  }
}
/*************************************************************************************/
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
/*************************************************************************************/
 public boolean getCarrier(String Carrier)
 {
    boolean getCarrier = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_Carrier ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,Carrier);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    Carrier_Contact_Name=RS.getString(1);
	    Currency_Desc=RS.getString(2);
	    Comments=RS.getString(3);
	    Carrier_Country=RS.getString(4);
	    Carrier_Name=RS.getString(5);
	    Carrier_Billing_Address_Lines[0]=RS.getString(6);
	    Carrier_Billing_Address_Lines[1]=RS.getString(7);
	    Carrier_Billing_Address_Lines[2]=RS.getString(8);
	    Carrier_Billing_Address_Lines[3]=RS.getString(9);
	    VAS_List_No=RS.getString(10);
	    Bank_Account_No=RS.getString(11);
	    Bank_Address=RS.getString(12);
	    Local_Tax_Rate=RS.getString(13);
	    General_Ledger_Code=RS.getString(14);
	    Tax_Precision=RS.getString(15);
	    Carrier_Id=RS.getInt(16);
            getCarrier = true;
	  }
	  Carrier_Billing_Address=SU.packTextArea(Carrier_Billing_Address_Lines);
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
      return getCarrier;
    }
 }
 /************************************************************************************************/
  //for debugging
  public static void main (String[] args)
  {
    boolean OK;
    CarrierBANBean caBAN=new CarrierBANBean();
    caBAN.setBanIdentifier("tes-2003-2-11-5");
    caBAN.setLocal_Tax_Rate("0");
    caBAN.setGeneral_Ledger_Code("14");
    caBAN.setTax_Precision("1");
    //banCreatedBy
    caBAN.updateCarrierBAN();

 //exec  eban..update_carrier_ban N'tes-2003-2-11-5',N'Draft',N'qwewqe','2003-02-11','',N'Contact',N'FRF',N'Comments',N'Antigua and Barbuda',N'test 1',N'Address',N'',N'','',N'56',N'4564564',N'Bank Address',N'788.000000',N'88888',N'4',N'Julian Ustaszewski',null }
  }
}