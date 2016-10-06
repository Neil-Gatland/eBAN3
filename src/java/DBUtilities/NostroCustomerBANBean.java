package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class NostroCustomerBANBean extends BANBean
{
  private String sql;
  private String NostroRefs;
  protected Vector mandatory=new Vector();
  private Hashtable ScreenData=new Hashtable();
  private int BEDays, BEMonths, BEYears;

  public NostroCustomerBANBean ()
  {
    NostroRefs = "";
    mandatory.clear();
    mandatory.addElement("NostroRefs");
    mandatory.addElement("BANEffectiveDateh");
    errored.addElement("");
  }

  public void Reset()
  {
    super.Reset();
    NostroRefs = "";
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    Required_BAN_Effective_Dateh = new java.sql.Date(new java.util.Date().getTime());
    setErrored("clear");
  }
/************************************************************************************************/
public String getNostroRefs()
{
  return NostroRefs;
}
/************************************************************************************************/
public void setNostroRefs(String inRef)
{
  NostroRefs = inRef;
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
 public boolean getCustomerBAN()
 {
    boolean getCustomerBAN = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_Nostro_Customer_BAN ";
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
	    rejectReason=RS.getString(6);
	    Mode=(RS.getString(7));
            setDates();
            getCustomerBAN = getNostroRefsFromTable();
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
      return getCustomerBAN;
    }
 }
/************************************************************************************************/
  public boolean AuthoriseCustomerBAN()
  {
    if (canAuthorise())
    {
      if (insertUpdateGCA())
      {
        if (createCustomerSalesOrder())
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
          Message="<font color=red><b>Unable to update Nostro reference. Please contact system support.";
          return false;
      }
      else
        return false;
    }
    else
      return false;
  }
/************************************************************************************************/
  private boolean updateBANStatus()
  {
    boolean updateBANStatus = false;
    try{
      SQL = "UPDATE eban..Nostro_Customer_BAN " +
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
/*********************************************************************/
  public boolean insertUpdateGCA()
  {
    boolean insertUpdateGCA = false;
    try{
	  if (DBA.Connect(WRITE,P3))
	  {
            if (Mode.equals("Add"))
            {
              SQL = "{call givn..Insert_Global_Cust_App " +
                "(?,?,?,?,?,?,?,?)}";

              cstmt = DBA.Conn.prepareCall(SQL);
              cstmt.setString(1,Global_Customer_Id);
              cstmt.setString(2,"11");
              cstmt.setDate(3,Required_BAN_Effective_Dateh);
              cstmt.setString(4,userId);
              cstmt.setString(5,"N");
              cstmt.setString(6,"N");
              cstmt.setString(7,"N");
              cstmt.setString(8,"Y");
            }
            else
            {
              SQL = "{call givn..Update_Global_Cust_App " +
                "(?,?,?,?)}";

              cstmt = DBA.Conn.prepareCall(SQL);
              cstmt.setString(1,Global_Customer_Id);
              cstmt.setString(2,"11");
              cstmt.setDate(3,Required_BAN_Effective_Dateh);
              cstmt.setString(4,userId);
            }

	    try
	    {
	      cstmt.execute();
	      insertUpdateGCA = true;
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
          return insertUpdateGCA;
        }
  }
/************************************************************************************************/
  public boolean createCustomerBAN()
  {
    boolean createCustomerBAN = false;
    try{
	  if (DBA.Connect(WRITE,P5))
	  {
            SQL = "{call eban..Create_Nostro_Customer_BAN " +
              "(?,?,?,?,?,?,?,?)}";

            cstmt = DBA.Conn.prepareCall(SQL);
            cstmt.setString(1,banStatus);
            cstmt.setString(2,Global_Customer_Id);
            cstmt.setString(3,Global_Customer_Name);
            cstmt.setString(4,BAN_Summary);
            cstmt.setDate(5,Required_BAN_Effective_Dateh);
            cstmt.setString(6,Mode);
            cstmt.setString(7,banCreatedBy);
            cstmt.setString(8,NostroRefs);

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
/************************************************************************************************/
  public boolean updateCustomerBAN()
  {
    boolean updateCustomerBAN = false;
    int rowcount=0;

    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..update_Nostro_Customer_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
        cstmt.setString(2,banStatus);
	cstmt.setString(3,Global_Customer_Id);
	cstmt.setString(4,Global_Customer_Name);
	cstmt.setString(5,BAN_Summary);
	cstmt.setDate(6,Required_BAN_Effective_Dateh);
	cstmt.setString(7,NostroRefs);
	cstmt.setString(8,userId);
	cstmt.setString(9,rejectReason);

        cstmt.execute();
        rowcount=cstmt.getUpdateCount();
        updateCustomerBAN = true;
        Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" "+action+
          (action.endsWith("e")?"d":"ed");
	/*if (rowcount != 1)
	{
	  Message="Error! - "+Integer.toString(rowcount) + " Rows Updated";
	  return false;
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
/************************************************************************************************/
  private boolean createCustomerSalesOrder()
  {
    boolean createCustomerSalesOrder = false;

    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Create_Customer_Sales_Order ";
        SQL += "(?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,Global_Customer_Id);

        cstmt.execute();
        createCustomerSalesOrder = true;
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
      return createCustomerSalesOrder;
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
            FormField=SU.isNull((String)ScreenData.get(FieldName),"");
            if(FormField.compareTo("") == 0)
            {
              if (FieldName.equals("NostroRefs"))
              {
                if (!hasNostroRefs())
                  setErrored(FieldName);
              }
              else
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
    else if (name.compareTo("NostroRefs")==0)
    {
      setNostroRefs(value);
      ScreenData.put("NostroRefs",value);
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
/*************************************************************************************/
private boolean getNostroRefsFromTable()
{
  boolean getNostroRefsFromTable = false;
  StringBuffer sb = new StringBuffer();
    try{
      SQL = "SELECT Sales_Order_Number FROM eban..Customer_Sales_Order_Temp " +
        "WHERE Global_Customer_Id='" + Global_Customer_Id + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          sb.append(RS.getString(1)+",");
        }
        NostroRefs=sb.toString();
        getNostroRefsFromTable = true;
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      NostroRefs="";
    }
    finally
    {
      close();
      return getNostroRefsFromTable;
    }
  }
/************************************************************************************************/
  private boolean hasNostroRefs()
  {
    boolean hasNostroRefs = false;
    try{
      SQL = "SELECT Sales_Order_Number from eban..Customer_Sales_Order_Temp " +
       "WHERE Global_Customer_Id = '" + Global_Customer_Id + "' ";
      if (DBA.Connect(PREPARE,P5))
      {

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
        RS = Stmt.getResultSet();
        if (RS.next())
        {
          hasNostroRefs = true;
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
      return hasNostroRefs;
    }
  }
}