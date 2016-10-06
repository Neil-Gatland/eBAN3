package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class CustomerBANBean extends BANBean
{
    private String SQL;
    private String Global_Account_Manager="",Customer_Billing_Address_Lines[]={"","","","","","",""};
    private String Customer_Contact="please Tel:  +44 (0)1908 833164 or fax +44 (0)1908 833152";
    private String Customer_Billing_Name="",Customer_Contact_Name="";
    private String Customer_Billing_Address="";
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();

  public CustomerBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("BAN_Summary");
    mandatory.addElement("Customer_Contact");
    mandatory.addElement("Required_BAN_Effective_Dateh");
    mandatory.addElement("Customer_Billing_Address");
    mandatory.addElement("Customer_Billing_Name");
    mandatory.addElement("Invoice_Region");
    errored.addElement("");
  }

  public void Reset()
  {
    super.Reset();
    Global_Account_Manager="";
    Customer_Billing_Address_Lines[0]=Customer_Billing_Address_Lines[1]=Customer_Billing_Address_Lines[3]="";
    Customer_Contact="";
    Invoice_Region="";
    Customer_Billing_Name="";
    Customer_Contact_Name="please Tel:  +44 (0)1908 833164 or fax +44 (0)1908 833152";
    Customer_Billing_Address="";
    setErrored("clear");
  }
  /*set Methods*/

  public void setGlobalCustomerName(String CustomerName)
  {
   Global_Customer_Name = SU.isNull(CustomerName,"");
  }
  public void setGlobalCustomerIdfromName(String CustomerName)
  {
   String Base_Id;
   int Counter=0;
   Global_Customer_Id = SU.getAbbreviation(CustomerName,3);

   Base_Id=Global_Customer_Id;
   while ((CustomerExists()) && (Counter < 101))
   {
    Counter++;
    Global_Customer_Id =Base_Id+Integer.toString(Counter);
   }
  }
  public void setCustomer_Contact(String newCustomer_Contact)
  {
   Customer_Contact = SU.isNull(newCustomer_Contact,"");
  }
  public void setCustomer_Billing_Name(String value)
  {
   Customer_Billing_Name = SU.isNull(value,"");
  }
  public void setCustomer_Billing_Address(String value)
  {
    Customer_Billing_Address=value;
  }
  public void setCustomer_Contact_Name( String value)
  {
    Customer_Contact_Name=value;
  }
  public void setErrored(String Item)
  {
    if (Item.startsWith("clear"))
      errored.clear();
    else
      errored.addElement(Item);
  }
  public void setGlobal_Account_Manager(String newGlobal_Account_Manager)
  {
   Global_Account_Manager = SU.isNull(newGlobal_Account_Manager,"");
  }
  public void setGlobalCustomerId(String value)
  {
    super.setGlobalCustomerId(value);
    Enumeration e;
    StringBuffer SQLBuffer = new StringBuffer("");

    //There will only be one acount, so get it!
    SQLBuffer.append("Select Account_Id,OutGoing_Currency_Code from givn_ref..Invoice_Region");
    SQLBuffer.append(" where Global_Customer_Id = '").append(Global_Customer_Id).append("'");

    e=DBA.getResults(SQLBuffer.toString(),P3);
    //Datasource changes e=DBA.getResults(SQLBuffer.toString());

    if (e.hasMoreElements())
    {
      Account_Id=SU.isNull((String)e.nextElement(),"");
      if (e.hasMoreElements())
      {
        Currency_Desc=SU.isNull((String)e.nextElement(),"");
      }
    }
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
/*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.compareTo("Global_Customer_Name")==0)
    {
      if (Mode.compareTo("Create") == 0)
      {
        setGlobalCustomerIdfromName(value);
      }
      setGlobalCustomerName(value);
      ScreenData.put("Global_Customer_Name",value);
    }
    else if (name.compareTo("Global_Account_Manager")==0)
    {
      setGlobal_Account_Manager(value);
      ScreenData.put("Global_Account_Manager",value);
    }
    else if (name.compareTo("Customer_Contact")==0)
    {
      setCustomer_Contact(value);
      ScreenData.put("Customer_Contact",value);
    }
    else if (name.compareTo("Required_BAN_Effective_Dateh")==0)
    {
     setRequired_BAN_Effective_Date(value);
     ScreenData.put("Required_BAN_Effective_Dateh",value);
    }
    else if (name.compareTo("Inactive_Global_Customer")==0)
    {
      if (value.compareTo("") !=0 )
      {
        setCustomerfromList(value);
        ScreenData.put("Global_Customer_Name",Global_Customer_Id);
        Mode="Amend";
      }
   }
    else if (name.compareTo("Invoice_Region")==0)
    {
      setInvoice_Region(value);
      ScreenData.put("Invoice_Region",value);
    }
    else if (name.compareTo("Customer_Billing_Name")==0)
    {
      setCustomer_Billing_Name(value);
      ScreenData.put("Customer_Billing_Name",value);
    }
    else if (name.compareTo("Customer_Billing_Address")==0)
    {
      setCustomer_Billing_Address(value);
      ScreenData.put("Customer_Billing_Address",value);
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
    else if (name.compareTo("Customer_Contact_Name")==0)
    {
      setCustomer_Contact_Name(value);
    }
    else if (name.compareTo("Currency_Desc")==0)
    {
      if (value.compareTo("") != 0)
      {
        setCurrency_Desc(value);
        ScreenData.put("Currency_Desc",value);
      }
    }
}
/*******************************************************************************/
/*get Methods*/
  public String getGlobal_Account_Manager()
  {
    return Global_Account_Manager;
  }
  public String getCustomer_Contact()
  {
    return Customer_Contact;
  }
  public String getCustomer_Contact_Name()
  {
    return Customer_Contact_Name;
  }
  public String getCustomer_Billing_Address()
  {
    return Customer_Billing_Address;
  }
  public String getCustomer_Billing_Name()
  {
    return Customer_Billing_Name;
  }
  public String getMode(String FieldName)
  {//This defines whether or not a field is enterable

    if (FieldName.compareTo("Inactive_Global_Customer") ==0)
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
	//Account already exists for a 'Create'?
	if ((Mode.compareTo("Add")==0) && (AccountNameExists()))
	{
	  setErrored("Invoice_Region");
	  Message = "<font color=red><b>An Account with this Customer Account Name already exists";
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
/*******************************************************************************/
public boolean AccountExists()
{

  SQL = "Select 'Found' from givn_ref..Invoice_Region where Account_Id = '"+ Account_Id + "'";


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
public boolean AccountNameExists()
{
  SQL = "Select 'Found' from givn_ref..Invoice_Region where Invoice_Region = '"+ Invoice_Region + "'";
  SQL += " and Global_Customer_Id = '"+ Global_Customer_Id + "'";


  if(DBA.getExists(SQL,P3))
  {
    return true;
  }
  else
  {
    return false;
  }
}
/*******************************************************************************/
public boolean CustomerExists()
{

  SQL = "Select 'Found' from givn..Global_Customer where Global_Customer_Id = '"+ Global_Customer_Id + "'";

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
	    Global_Customer_Id=RS.getString(2);
	    Global_Customer_Name=RS.getString(3);
	    Required_BAN_Effective_Dateh=RS.getDate(4);
   	    BAN_Summary=RS.getString(5);
    	    BAN_Reason=RS.getString(6);
	    Global_Account_Manager=RS.getString(7);
	    Invoice_Region=RS.getString(8);
	    Customer_Contact=RS.getString(9);
	    Customer_Billing_Name=RS.getString(10);
	    Customer_Contact_Name=RS.getString(11);
	    Customer_Billing_Address_Lines[0]=RS.getString(12);
	    Customer_Billing_Address_Lines[1]=RS.getString(13);
	    Customer_Billing_Address_Lines[2]=RS.getString(14);
	    Customer_Billing_Address_Lines[3]=RS.getString(15);
	    Customer_Billing_Address_Lines[4]=RS.getString(16);
	    Customer_Billing_Address_Lines[5]=RS.getString(17);
	    Customer_Billing_Address_Lines[6]=RS.getString(18);
	    rejectReason=RS.getString(19);
	    Account_Id=RS.getString(20);
	    setCurrency_Desc(RS.getString(21));
	    Mode=(RS.getString(22));
            getCustomerBAN = true;
	  }
	  Customer_Billing_Address=SU.packTextArea(Customer_Billing_Address_Lines);
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
      return getCustomerBAN;
    }
 }
/*****************************************************************************************/
public boolean createCustomerBAN()
{
  boolean createCustomerBAN = false;
  Enumeration Customer_Billing_Address_UnPacked;
  int rowcount=0,i=0;

      if (Customer_Billing_Address.compareTo("") !=0 )
      {
	  Customer_Billing_Address_UnPacked = SU.unpackTextArea(Customer_Billing_Address);

	  while ((Customer_Billing_Address_UnPacked.hasMoreElements()) && (i < 7))
	  {
	    Customer_Billing_Address_Lines[i]=(String)Customer_Billing_Address_UnPacked.nextElement();
	    i++;
	  }
      }
      while (i < 7)
      {
	Customer_Billing_Address_Lines[i]="";
	i++;
      }

    try{
	  if (DBA.Connect(WRITE,P5))
	  {
	    SQL = "{call eban..Create_GCB_Customer_BAN ";
	    SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	    cstmt = DBA.Conn.prepareCall(SQL);
	    cstmt.setString(1,banStatus);
	    cstmt.setString(2,Global_Customer_Id);
	    cstmt.setString(3,Global_Customer_Name);
	    cstmt.setString(4,BAN_Summary);
	    cstmt.setDate(5,Required_BAN_Effective_Dateh);
	    cstmt.setString(6,BAN_Reason);
	    cstmt.setString(7,Global_Account_Manager);
	    cstmt.setString(8,Invoice_Region );
	    cstmt.setString(9,Customer_Contact );
	    cstmt.setString(10,Customer_Billing_Name );
	    cstmt.setString(11,Customer_Contact_Name );
	    cstmt.setString(12,Customer_Billing_Address_Lines[0] );
	    cstmt.setString(13,Customer_Billing_Address_Lines[1] );
	    cstmt.setString(14,Customer_Billing_Address_Lines[2]);
	    cstmt.setString(15,Customer_Billing_Address_Lines[3] );
	    cstmt.setString(16,Customer_Billing_Address_Lines[4] );
	    cstmt.setString(17,Customer_Billing_Address_Lines[5]);
	    cstmt.setString(18,Customer_Billing_Address_Lines[6] );
	    cstmt.setString(19,Mode);
	    cstmt.setString(20,banCreatedBy);
	    cstmt.setString(21,Account_Id);
	    cstmt.setString(22,Currency_Desc);

	    try
	    {
	      cstmt.execute();
	      RS = cstmt.getResultSet();
	      if (RS.next())
	      {
	        banIdentifier=RS.getString(1);
                createCustomerBAN = true;
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

      if (Customer_Billing_Address.compareTo("") !=0 )
      {
	  Customer_Billing_Address_UnPacked = SU.unpackTextArea(Customer_Billing_Address);

	  while ((Customer_Billing_Address_UnPacked.hasMoreElements()) && (i < 7))
	  {
	    Customer_Billing_Address_Lines[i]=(String)Customer_Billing_Address_UnPacked.nextElement();
	    i++;
	  }
      }
      while (i < 7)
      {
	Customer_Billing_Address_Lines[i]="";
	i++;
      }


    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..update_GCB_Customer_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
        cstmt.setString(2,banStatus);
	cstmt.setString(3,Global_Customer_Name);
	cstmt.setString(4,BAN_Summary);
	cstmt.setDate(5,Required_BAN_Effective_Dateh);
	cstmt.setString(6,BAN_Reason);
	cstmt.setString(7,Global_Account_Manager);
	cstmt.setString(8,Invoice_Region );
	cstmt.setString(9,Customer_Contact );
	cstmt.setString(10,Customer_Billing_Name );
	cstmt.setString(11,Customer_Contact_Name );
	cstmt.setString(12,Customer_Billing_Address_Lines[0] );
	cstmt.setString(13,Customer_Billing_Address_Lines[1] );
	cstmt.setString(14,Customer_Billing_Address_Lines[2]);
	cstmt.setString(15,Customer_Billing_Address_Lines[3] );
	cstmt.setString(16,Customer_Billing_Address_Lines[4] );
	cstmt.setString(17,Customer_Billing_Address_Lines[5]);
	cstmt.setString(18,Customer_Billing_Address_Lines[6] );
	cstmt.setString(19,userId);
	cstmt.setString(20,rejectReason);
	cstmt.setString(21,Account_Id);
	cstmt.setString(22,Currency_Desc);

        cstmt.execute();
        rowcount=cstmt.getUpdateCount();
	closeupdate();
	if (rowcount != 1)
	{
	  Message="Error! - "+Integer.toString(rowcount) + " Rows Updated";
	  return false;
	}
	else
	{
          updateCustomerBAN = true;
          Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" "+action+
            (action.endsWith("ed")?"":(action.endsWith("e")?"d":"ed"));
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
        if (Mode.compareTo("Create") ==0)
        {
	  SQL = "{call givn..Create_GCB_Customer ";
	  SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	  cstmt = DBA.Conn.prepareCall(SQL);
	  cstmt.setString(1,Global_Customer_Id);
	  cstmt.setString(2,Global_Customer_Name);
	  cstmt.setString(3,Global_Account_Manager);
	  cstmt.setString(4,Customer_Contact );
	  cstmt.setString(5,Customer_Billing_Name );
	  cstmt.setString(6,Customer_Contact_Name );
	  cstmt.setString(7,Customer_Billing_Address_Lines[0] );
	  cstmt.setString(8,Customer_Billing_Address_Lines[1] );
	  cstmt.setString(9,Customer_Billing_Address_Lines[2]);
	  cstmt.setString(10,Customer_Billing_Address_Lines[3] );
	  cstmt.setString(11,Customer_Billing_Address_Lines[4] );
	  cstmt.setString(12,Customer_Billing_Address_Lines[5]);
	  cstmt.setString(13,Customer_Billing_Address_Lines[6] );
	  cstmt.setString(14,Account_Id);
	  cstmt.setString(15,Currency_Desc);
	  cstmt.setDate(16,Required_BAN_Effective_Dateh);
	  cstmt.setString(17, banIdentifier);
	}
	else if (Mode.compareTo("Add") ==0)
	{
	  SQL = "{call givn_ref..Create_GCB_Account ";
	  SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	  cstmt = DBA.Conn.prepareCall(SQL);

	  cstmt.setString(1,Global_Customer_Id);
	  cstmt.setString(2,Global_Account_Manager);
	  cstmt.setString(3,Customer_Billing_Name);
	  cstmt.setString(4,Customer_Contact);
	  cstmt.setString(5,Customer_Contact_Name );
	  cstmt.setString(6,Customer_Billing_Address_Lines[0] );
	  cstmt.setString(7,Customer_Billing_Address_Lines[1] );
	  cstmt.setString(8,Customer_Billing_Address_Lines[2]);
	  cstmt.setString(9,Customer_Billing_Address_Lines[3] );
	  cstmt.setString(10,Customer_Billing_Address_Lines[4] );
	  cstmt.setString(11,Customer_Billing_Address_Lines[5] );
	  cstmt.setString(12,Customer_Billing_Address_Lines[6] );
	  cstmt.setString(13,Account_Id);
	  cstmt.setString(14,Currency_Desc);
	  cstmt.setString(15, banIdentifier);
	  cstmt.setString(16, Invoice_Region);
	}
        else
	{
	  SQL = "{call givn_ref..Update_GCB_Account ";
	  SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	  cstmt = DBA.Conn.prepareCall(SQL);
	  cstmt.setString(1,Global_Customer_Id);
	  cstmt.setString(2,Global_Customer_Name);
	  cstmt.setString(3,Global_Account_Manager);
	  cstmt.setString(4,Customer_Contact );
	  cstmt.setString(5,Customer_Billing_Name );
	  cstmt.setString(6,Customer_Contact_Name );
	  cstmt.setString(7,Customer_Billing_Address_Lines[0]);
	  cstmt.setString(8,Customer_Billing_Address_Lines[1]);
	  cstmt.setString(9,Customer_Billing_Address_Lines[2]);
	  cstmt.setString(10,Customer_Billing_Address_Lines[3]);
	  cstmt.setString(11,Customer_Billing_Address_Lines[4]);
	  cstmt.setString(12,Customer_Billing_Address_Lines[5]);
	  cstmt.setString(13,Customer_Billing_Address_Lines[6]);
	  cstmt.setString(14,Account_Id);
	  cstmt.setString(15,Currency_Desc);
	  cstmt.setString(16,banIdentifier);
	}
        try
	{
	  cstmt.execute();
          AuthoriseCustomerBAN = true;
          Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
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
/*************************************************************************************/
 public boolean getCustomer()
 {
    boolean getCustomer = false;
    try{
      if (DBA.Connect(PREPARE,P3))
      {
        SQL = "{call givn..get_GCB_Customer ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,Account_Id);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    Global_Customer_Name=RS.getString(1);
	    Global_Account_Manager=RS.getString(2);
	    Customer_Contact=RS.getString(3);
	    Customer_Billing_Name=RS.getString(4);
	    Customer_Contact_Name=RS.getString(5);
	    Customer_Billing_Address_Lines[0]=RS.getString(6);
	    Customer_Billing_Address_Lines[1]=RS.getString(7);
	    Customer_Billing_Address_Lines[2]=RS.getString(8);
	    Customer_Billing_Address_Lines[3]=RS.getString(9);
	    Customer_Billing_Address_Lines[4]=RS.getString(10);
	    Customer_Billing_Address_Lines[5]=RS.getString(11);
	    Customer_Billing_Address_Lines[6]=RS.getString(12);
	    setCurrency_Desc(RS.getString(13));
	    Invoice_Region=(RS.getString(14));
            getCustomer = true;
	  }
	  Customer_Billing_Address=SU.packTextArea(Customer_Billing_Address_Lines);
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
/************************************************************************************************/
}