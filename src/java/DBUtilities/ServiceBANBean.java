package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class ServiceBANBean extends BANBean
{
    private String SQL;
    private String From_End="",To_End="",From_End_Code="",To_End_Code="";
    private String Service_Type="",Site_Reference="",Site_Name="",Site_Address="",Speed="";
    private String Product_Type="";
    private String Customer_Reference="",Service_Description="";
    protected Vector mandatory=new Vector();
    private java.sql.Date Live_Service_Dateh,Ready_For_Service_Dateh,Billing_Start_Dateh,Billing_End_Dateh;
    private Hashtable ScreenData=new Hashtable();

  public ServiceBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("BAN_Summary");
    mandatory.addElement("Service_Description");
    mandatory.addElement("Billing_Start_Date");
    errored.addElement("");
  }
  public void Reset()
  {
    super.Reset();
    Division_Id="";
    Speed="";
    setErrored("clear");
  }
/*set Methods*/
  public void setService_Type(String newService_Type)
  {
   Service_Type = newService_Type;
  }
  public void setFrom_End(String newFrom_End)
  {
   From_End = newFrom_End;
  }
  public void setFrom_End_Code(String value)
  {
   From_End_Code = value;
  }
  public void setTo_End(String newTo_End)
  {
   To_End = SU.isNull(newTo_End,"");
  }
  public void setTo_End_Code(String value)
  {
   To_End_Code = value;
  }
  public void setProduct_Type(String newProduct_Type)
  {
   Product_Type = SU.isNull(newProduct_Type,"");
  }
  public void setSpeed(String newSpeed)
  {
   Speed = SU.isNull(newSpeed,"");
  }
  public void setSite_Reference(String newSite_Reference)
  {
   Site_Reference = SU.isNull(newSite_Reference,"");
  }
  public void setSite_Name(String newSite_Name)
  {
   Site_Name = SU.isNull(newSite_Name,"");
  }
  public void setSite_Address(String newSite_Address)
  {
   Site_Address = SU.isNull(newSite_Address,"");
  }
  public void setLive_Service_Date(String value)
  {
    Live_Service_Dateh = SU.toJDBCDate(value);
  }
  public void setReady_For_Service_Date(String value)
  {
    Ready_For_Service_Dateh = SU.toJDBCDate(value);
  }
  public void setBilling_Start_Date(String value)
  {
    Billing_Start_Dateh = SU.toJDBCDate(value);
  }
  public void setBilling_End_Date(String value)
  {
    Billing_End_Dateh = SU.toJDBCDate(value);
  }
  public void setCustomer_Reference(String value)
  {
    Customer_Reference=value;
  }
  public void setService_Description(String value)
  {
    Service_Description=value;
  }

/*set BAN values from form*/
  public void setParameter(String name,String value)
  {
    if (name.compareTo("Service_Type")==0)
    {
      setService_Type(value);
      ScreenData.put("Service_Type",value);
    }
    else if (name.compareTo("Service_Description")==0)
    {
      setService_Description(value);
      ScreenData.put("Service_Description",value);
    }
    else if (name.compareTo("GCB_Product_Type")==0)
    {
      setProduct_Type(value);
       }
    else if (name.compareTo("Required_BAN_Effective_Dateh")==0)
    {
      setRequired_BAN_Effective_Date(value);
       }
    else if (name.compareTo("Customer_Reference")==0)
    {
      setCustomer_Reference(value);
    }
    else if (name.compareTo("Live_Service_Dateh")==0)
    {
      setLive_Service_Date(value);
    }
    else if (name.compareTo("Ready_For_Service_Dateh")==0)
    {
      setReady_For_Service_Date(value);
    }
    else if (name.compareTo("From_End")==0)
    {
      setFrom_End_Code(value);
    }
    else if (name.compareTo("Site")==0)
    {
      setSite(value);
    }
    else if (name.compareTo("To_End")==0)
    {
      setTo_End_Code(value);
    }
    else if (name.compareTo("Billing_Start_Dateh")==0)
    {
      setBilling_Start_Date(value);
      ScreenData.put("Billing_Start_Date",value);
    }
    else if (name.compareTo("Billing_End_Dateh")==0)
    {
      setBilling_End_Date(value);
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
    else if (name.compareTo("Speed")==0)
    {
      setSpeed(value);
    }
  }
/*******************************************************************************/
/*get Methods*/

public String getLive_Service_Date()
  {
    if (Live_Service_Dateh != null)
    {
      return SU.reformatDate(Live_Service_Dateh.toString());
    }
    else
    {
      return "";
    }
  }
 public String getReady_For_Service_Date()
  {
     if (Ready_For_Service_Dateh != null)
    {
      return SU.reformatDate(Ready_For_Service_Dateh.toString());
    }
    else
    {
      return "";
    }
  }
  public String getBilling_Start_Date()
  {
     if (Billing_Start_Dateh != null)
    {
      return SU.reformatDate(Billing_Start_Dateh.toString());
    }
    else
    {
      return "";
    }
  }
  public String getBilling_End_Date()
  {
     if (Billing_End_Dateh != null)
    {
      return SU.reformatDate(Billing_End_Dateh.toString());
    }
    else
    {
      return "";
    }
  }
  public String getService_Status()
  {
    if ((Live_Service_Dateh != null) &&
        (Live_Service_Dateh.before(SU.toJDBCDate("Today"))) &&
        (Billing_Start_Dateh != null) &&
        (Billing_Start_Dateh.before(SU.toJDBCDate("Today"))) &&
	(Billing_End_Dateh == null) &&
        (Billing_End_Dateh.after(SU.toJDBCDate("Today"))))
	{
	  return "Live";
	}
	else
	{
	  return "Inactive";
	}
  }
  public String getFrom_End()
  {
    return From_End;
  }
  public String getFrom_End_Code()
  {
    return From_End_Code;
  }
  public String getService_Type()
  {
    return Service_Type;
  }
  public String getTo_End()
  {
    return To_End;
  }
  public String getTo_End_Code()
  {
    return To_End_Code;
  }
  public String getProduct_Type()
  {
    return SU.isNull(Product_Type,"");
  }
  public String getSite_Reference()
  {
    return Site_Reference;
  }
  public String getSite_Name()
  {
    return Site_Name;
  }
  public String getSite_Address()
  {
    return Site_Address;
  }
  public String getSpeed()
  {
    return Speed;
  }
  public String getCustomer_Reference()
  {
    return Customer_Reference;
  }
  public String getService_Description()
  {
    return Service_Description;
  }
/*****************************************************************************/
  public String getMode(String FieldName)
  {
    if (FieldName.compareTo("Billing_End_Date") ==0)
    {
      if (Mode.compareTo("Cease") == 0)
	  return "";
      else
	  return "no";
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
/***************************************************************************/
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
/*******************************************************************************/
public boolean isSiteValid()
{
  	setErrored("clear");
        Message = "<font color=red><b>";

	if (SU.isNull(Site_Reference,"").compareTo("")==0)
        {
          Message = Message+ "You must enter a value for Site Reference";
	  setErrored("Site_Reference");
        }
	else if (SU.isNull(Site_Name,"").compareTo("")==0)
        {
          Message = Message+ "You must enter a value for Site Name";
	  setErrored("Site_Name");
        }
	if (errored.isEmpty())
	  return true;
	else
	{
	  if (errored.size() > 1)
	  {
	    Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
	  }
	  return false;
	}
}
/*******************************************************************************/
public boolean getService()
{
      boolean getService = false;
      try{
      if (DBA.Connect(PREPARE,P4))
      {
	SQL = "{call gcd..Get_Service ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,Service_Reference);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
	  if (RS.next())
	  {
	    From_End=RS.getString(1);
	    To_End=RS.getString(2);
	    Product_Type=RS.getString(3);
	    Speed=RS.getString(4);
	    Live_Service_Dateh=RS.getDate(5);
	    site=RS.getString(6);
	    Service_Type=RS.getString(7);
	    Billing_End_Dateh=RS.getDate(8);
	    Billing_Start_Dateh=RS.getDate(9);
	    Ready_For_Service_Dateh=RS.getDate(10);
	    Service_Description=RS.getString(11);
	    Division_Id=RS.getString(12);
	    From_End_Code=RS.getString(13);
	    To_End_Code=RS.getString(14);
            getService = true;
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
      return getService;
    }
}
/*******************************************************************************/
public boolean getService(String BAN_Id)
{
    StringBuffer SQLBuffer = new StringBuffer("");
    String Service_Ref="";

    if (BAN_Id.compareTo("")!=0)
    {
      SQLBuffer.append("Select Service_Reference from eban..Charge_BAN");
      SQLBuffer.append(" where BAN_Identifier = '").append(BAN_Id);
      SQLBuffer.append("'");

      Service_Ref=DBA.getValue(SQLBuffer.toString(),P5);

      if (Service_Ref.compareTo("") !=0)
      {
        setService_Reference(Service_Ref);
      }
    }
    return getService();
}
/*******************************************************************************/
public boolean getServiceForGCB(String BAN_Id)
{
    StringBuffer SQLBuffer = new StringBuffer("");
    String Service_Ref="";

    if (BAN_Id.compareTo("")!=0)
    {
      SQLBuffer.append("Select Service_Reference from eban..GCB_Charge_BAN");
      SQLBuffer.append(" where BAN_Identifier = '").append(BAN_Id);
      SQLBuffer.append("'");

      Service_Ref=DBA.getValue(SQLBuffer.toString(),P5);

      if (Service_Ref.compareTo("") !=0)
      {
        setService_Reference(Service_Ref);
      }
    }
    return getService();
}
/*******************************************************************************/
public boolean isServiceValid(String ButtonPressed)
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
	if ((!errored.isEmpty()) && (errored.size() > 0))
	{
	    Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
	    return false;
	}

	//No Billing End Date for a 'Cease'?
	if ((Mode.compareTo("Cease")==0) && (SU.isNull((String)ScreenData.get("Billing_End_Date"),"").compareTo("")==0))
	{
	  setErrored("Billing_End_Date");
	  Message = "<font color=red><b>You must supply a Billing End Date for a Cease";
	  return false;
	}
	return true;
}

/*******************************************************************************/
public boolean getServiceBAN()
{
    boolean getServiceBAN = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_Service_BAN ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);

        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    BAN_Summary=RS.getString(1);
	    BAN_Reason=RS.getString(2);
	    Service_Type=RS.getString(3);
	    Live_Service_Dateh=RS.getDate(4);
	    Ready_For_Service_Dateh=RS.getDate(5);
	    From_End_Code=RS.getString(6);
	    To_End_Code=RS.getString(7);
    	    Billing_Start_Dateh=RS.getDate(8);
    	    Billing_End_Dateh=RS.getDate(9);
	    Division_Id=RS.getString(10);
	    Product_Type=RS.getString(11);
	    Speed=RS.getString(12);
    	    Global_Customer_Id=RS.getString(13);
	    banStatus=RS.getString(14);
	    Required_BAN_Effective_Dateh=RS.getDate(15);
	    site=RS.getString(16);
	    banCreatedBy=RS.getString(17);
	    banAuthorisedBy=RS.getString(18);
	    Service_Description=RS.getString(19);
	    Customer_Reference=RS.getString(20);
	    rejectReason=RS.getString(21);
	    Mode=RS.getString(22);
	    Service_Reference=RS.getString(23);
            getServiceBAN = true;
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
      return getServiceBAN;
    }
}
/*****************************************************************************************/
public boolean createServiceBAN()
{
    boolean createServiceBAN = false;
    try
    {
      if (DBA.Connect(WRITE,P5))
      {
         SQL = "{call eban..Create_Service_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,BAN_Summary);
        cstmt.setString(2,BAN_Reason);
	cstmt.setString(3,Service_Type);
	cstmt.setDate(4,Live_Service_Dateh);
	cstmt.setDate(5,Ready_For_Service_Dateh);
	cstmt.setString(6,From_End_Code);
	cstmt.setString(7,To_End_Code);
	cstmt.setDate(8,Billing_Start_Dateh);
	cstmt.setDate(9,Billing_End_Dateh);
	cstmt.setString(10,Division_Id);
	cstmt.setString(11,Product_Type);
	cstmt.setString(12,Speed);
	cstmt.setString(13,site);
	cstmt.setString(14,Global_Customer_Id);
	cstmt.setString(15,Service_Reference);
	cstmt.setString(16,banStatus);
	cstmt.setDate(17,Required_BAN_Effective_Dateh);
	cstmt.setString(18,banCreatedBy);
	cstmt.setString(19,Service_Description);
	cstmt.setString(20,Customer_Reference);
	cstmt.setString(21,Mode);

	try
	{
          cstmt.execute();
          createServiceBAN = true;
          Message="Service BAN created";
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
      return createServiceBAN;
    }
  }
/**********************************************************************************************/
  public boolean updateServiceBAN()
  {
    boolean updateServiceBAN = false;
    int rowcount=0;
    try
    {
      if (DBA.Connect(WRITE,P5))
      {
	  SQL = "{call eban..update_Service_BAN ";
	  SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	  cstmt = DBA.Conn.prepareCall(SQL);
	  cstmt.setString(1,BAN_Summary);
	  cstmt.setString(2,BAN_Reason);
	  cstmt.setString(3,Service_Type);
	  cstmt.setDate(4,Live_Service_Dateh);
	  cstmt.setDate(5,Ready_For_Service_Dateh);
	  cstmt.setString(6,From_End_Code);
	  cstmt.setString(7,To_End_Code);
	  cstmt.setDate(8,Billing_Start_Dateh);
	  cstmt.setDate(9,Billing_End_Dateh);
	  cstmt.setString(10,Division_Id);
	  cstmt.setString(11,Product_Type);
	  cstmt.setString(12,Speed);
	  cstmt.setString(13,site);
	  cstmt.setString(14,Global_Customer_Id);
	  cstmt.setString(15,Service_Reference);
	  cstmt.setString(16,banStatus);
	  cstmt.setDate(17,Required_BAN_Effective_Dateh);
	  cstmt.setString(18,banCreatedBy);
	  cstmt.setString(19,Service_Description);
	  cstmt.setString(20,Customer_Reference);
	  cstmt.setString(21,userId);
	  cstmt.setString(22,rejectReason);
	  cstmt.setString(23,banIdentifier);

	  cstmt.execute();
	  rowcount=cstmt.getUpdateCount();
	  if (rowcount != 1)
	  {
	    Message="Unexpected! - "+Integer.toString(rowcount) + " Rows Updated";
	  }
          else
          {
            updateServiceBAN = true;
            Message="Service BAN amended";
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
        return updateServiceBAN;
      }
  }
 /********************************************************************/
 public boolean ImplementServiceBAN()
{
  boolean ImplementServiceBAN = false;
  int rowcount=0;
  try
  {
    if (Mode.compareTo("Create") ==0)
    {
      SQL = "exec gcd..Create_Service '"+ banIdentifier+ "'";
      Service_Reference=DBA.getValue(SQL,P4);
      ImplementServiceBAN = true;
    }
    else if ((Mode.compareTo("Amend") ==0) || (Mode.compareTo("Cease") ==0))
    {
      try
      {
        if (DBA.Connect(WRITE,P5))
        {
            SQL = "{call gcd..Update_Service ";
            SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?)}";

            cstmt = DBA.Conn.prepareCall(SQL);
            cstmt.setString(1,Service_Type);
            cstmt.setDate(2,Live_Service_Dateh);
            cstmt.setDate(3,Ready_For_Service_Dateh);
            cstmt.setString(4,From_End_Code);
            cstmt.setString(5,To_End_Code);
            cstmt.setDate(6,Billing_Start_Dateh);
            cstmt.setDate(7,Billing_End_Dateh);
            cstmt.setString(8,Product_Type);
            cstmt.setString(9,Speed);
            cstmt.setString(10,site);
            cstmt.setString(11,Service_Reference);
            cstmt.setString(12,Service_Description);
            cstmt.setString(13,banIdentifier);

            cstmt.execute();
            rowcount=cstmt.getUpdateCount();
            ImplementServiceBAN = true;
            Message="Service BAN amended";

            //if (rowcount != 1)
            //{
            //  Message="Unexpected! - "+Integer.toString(rowcount) + " Rows Updated";
            //  return false;
            //}
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
    }
  }
  catch(Exception ex)
  {
    Message= ex.getMessage();
  }
  finally
  {
    close();
    return ImplementServiceBAN;
  }
}

 /********************************************************************/
  //for debugging
/*  public static void main (String[] args)
  {
    boolean OK;
    //HttpServletRequest request = null;
    ServiceBANBean iBAN=new ServiceBANBean();
    CustomerBANBean cBAN=new CustomerBANBean();
    cBAN.setBanIdentifier("nmb");
    //cBAN.isValid();
 }
*/
}