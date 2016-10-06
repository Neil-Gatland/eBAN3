package EntityBeans;

import java.sql.*;
import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import JavaUtil.StringUtil;

public class ServiceItemBean extends EntityBean
{
    private String SQL;
    private String Reference="",Service_Id="",Product_Id="",Service_Item_Status_Id="";
    private String Tariff_Id="",Cost_Centre="",CW_Works_Order="",Service_Description="";
    private String Billing_Code="",Billing_Transfer_Flag="",CW_Cessation_Reference="";
    private String Notes="",Product_Description="",Service_Reference="",Barclays_Site_Id="";
    private String Billing_Region="",Site_Name="";
    private String CMDB_Service_Item_Id="";
    protected Vector mandatory=new Vector();
    private java.sql.Date Acceptance_Date,Cessation_Date,Billing_Start_Date,Billing_Cease_Date;

    //private Hashtable ScreenData=new Hashtable();
    private JavaUtil.StringUtil SU = new JavaUtil.StringUtil();

  public ServiceItemBean ()
  {
  }
  public void Reset()
  {
    super.Reset();
    Reference="";
    Service_Id="";
    Product_Id="";
    Service_Item_Status_Id="";
    Tariff_Id="";
    Cost_Centre="";
    CW_Works_Order="";
    Billing_Code="";
    Billing_Transfer_Flag="L";
    CW_Cessation_Reference="";
    Notes="";
    Cessation_Date=null;
    setErrored("clear");
  }
/*set Methods*/
  public void setBarclays_Site_Id(String value)
  {
   Barclays_Site_Id = value;
  }
  public void setSite_Name(String value)
  {
   Site_Name = value;
  }
  public void setTariff_Id(String value)
  {
   Tariff_Id = value;
  }
  public void setCost_Centre(String value)
  {
   Cost_Centre = value;
  }
  public void setCW_Works_Order(String value)
  {
   CW_Works_Order = value;
  }
  public void setBilling_Code(String value)
  {
   Billing_Code = value;
  }
  public void setReference(String value)
  {
   Reference = SU.isNull(value,"");
  }
  public void setService_Id(String value)
  {
   Service_Id = value;
   Service_Reference="BAR-XXX-"+Service_Id;
  }
  public void setService_Reference(String value)
  {
   Service_Reference=value;
  }
  public void setProduct_Description(String value)
  {
   Product_Description=value;
   Product_Id=getProduct_Id_from_Name(value);
  }
  public void setBilling_Transfer_Flag(String value)
  {
   Billing_Transfer_Flag = value;
  }
  public void setAcceptance_Date(String value)
  {
    Acceptance_Date = SU.toJDBCDate(value,"MMM dd yyyy hh:mma");
  }
  public void setCessation_Date(String value)
  {
    Cessation_Date = SU.toJDBCDate(value,"MMM dd yyyy hh:mma");
  }
  public void setBilling_Start_Date(String value)
  {
    Billing_Start_Date = SU.toJDBCDate(value,"MMM dd yyyy hh:mma");
  }
  public void setBilling_Cease_Date(String value)
  {
    Billing_Cease_Date = SU.toJDBCDate(value,"MMM dd yyyy hh:mma");
  }
  public void setProduct_Id(String value)
  {
   Product_Id = SU.isNull(value,"");
  }
  public void setCW_Cessation_Reference(String value)
  {
    CW_Cessation_Reference=value;
  }
  public void setService_Item_Status_Id(String value)
  {
   Service_Item_Status_Id = SU.isNull(value,"");
  }
  public void setNotes(String value)
  {
   Notes = SU.isNull(value,"");
  }
  public void setCMDB_Service_Item_Id(String value)
  {
    CMDB_Service_Item_Id=value;
  }
 /**************************************************************/
  public void setValuefromTag(String name,String value)
  {
    if (name.compareToIgnoreCase("Reference")==0)
    {
      setReference(value);
    }
    else if ((name.compareToIgnoreCase("BillingCode")==0) ||
	    (name.compareToIgnoreCase("Billing_Code")==0))
    {
      setBilling_Code(value);
    }
    else if (name.compareToIgnoreCase("CMDBServiceItemIdentifier")==0)
    {
      setCMDB_Service_Item_Id(value);
    }
    else if (name.compareToIgnoreCase("Barclays_Site_Id")==0)
    {
      setBarclays_Site_Id(value);
    }
    else if (name.compareToIgnoreCase("Site_Name")==0)
    {
      setSite_Name(value);
    }
    else if (name.compareToIgnoreCase("CMDB_Service_Reference")==0)
    {
      setService_Reference(value);
    }
    else if (name.compareToIgnoreCase("Product")==0)
    {
      setProduct_Description(value);
    }
    else if ((name.compareToIgnoreCase("TariffId")==0) ||
	    (name.compareToIgnoreCase("Tariff")==0))
    {
      setTariff_Id(value);
    }
     else if ((name.compareToIgnoreCase("CostCentre")==0) ||
	      (name.compareToIgnoreCase("Cost_Centre")==0))
    {
      setCost_Centre(value);
    }
     else if ((name.compareToIgnoreCase("CWWorksOrder")==0) ||
     (name.compareToIgnoreCase("CW_Works_Order")==0))
    {
      setCW_Works_Order(value);
    }
    else if ((name.compareToIgnoreCase("AcceptanceDate")==0) ||
	    (name.compareToIgnoreCase("Acceptance_Date")==0))
    {
      setAcceptance_Date(value);
    }
    else if ((name.compareToIgnoreCase("CessationDate")==0) ||
	    (name.compareToIgnoreCase("Cessation_Date")==0))
    {
      setCessation_Date(value);
    }
    else if (name.compareToIgnoreCase("ServiceId")==0)
    {
      setService_Id(value);
    }
    else if (name.compareToIgnoreCase("ServiceItemStatusId")==0)
    {
      setService_Item_Status_Id(value);
    }
    else if (name.compareToIgnoreCase("Notes")==0)
    {
      setNotes(value);
    }
    else if (name.compareToIgnoreCase("ProductId")==0)
    {
      setProduct_Id(value);
    }
    else if ((name.compareToIgnoreCase("CWCessationReference")==0) ||
	    (name.compareToIgnoreCase("CW_Cessation_Reference")==0))
    {
      setCW_Cessation_Reference(value);
    }
    else if ((name.compareToIgnoreCase("BillingTransferFlag")==0) ||
	    (name.compareToIgnoreCase("Billing_Transfer_Flag")==0))
    {
      setBilling_Transfer_Flag(value);
    }
    else if ((name.compareToIgnoreCase("BILLINGSTARTDATE")==0) ||
	    (name.compareToIgnoreCase("Billing_Start_Date")==0))
    {
      setBilling_Start_Date(value);
    }
    else if ((name.compareToIgnoreCase("BILLINGCEASEDATE")==0) ||
	    (name.compareToIgnoreCase("Billing_Cease_Date")==0))
    {
      setBilling_Cease_Date(value);
    }
    else if (name.compareToIgnoreCase("LastModifiedOn")==0)
    {
      setModifiedOn(value);
    }
    else if (name.compareToIgnoreCase("LastModifiedBy")==0)
    {
      setModifiedBy(value);
    }
  }
  /****************************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.startsWith("Reference"))
    {
      setReference(value);
    }
    else if (name.startsWith("Billing_Code"))
    {
      setBilling_Code(value);
    }
    else if (name.startsWith("Tariff_Id"))
    {
      setTariff_Id(value);
    }
     else if (name.startsWith("Cost_Centre"))
    {
      setCost_Centre(value);
    }
     else if (name.startsWith("CW_Works_Order"))
    {
      setCW_Works_Order(value);
    }
    else if (name.startsWith("Acceptance_Date"))
    {
      setAcceptance_Date(value);
    }
    else if (name.startsWith("Cessation_Date"))
    {
      setCessation_Date(value);
    }
    else if (name.startsWith("Service_Id"))
    {
      setService_Id(value);
    }
    else if (name.startsWith("Service_Item_Status_Id"))
    {
      setService_Item_Status_Id(value);
    }
    else if (name.startsWith("Notes"))
    {
      setNotes(value);
    }
    else if (name.startsWith("Product_Id"))
    {
      setProduct_Id(value);
    }
    else if (name.startsWith("CW_Cessation_Reference"))
    {
      setCW_Cessation_Reference(value);
    }
    else if (name.startsWith("Billing_Transfer_Flag"))
    {
      setBilling_Transfer_Flag(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getAcceptance_Date()
  {
     if (Acceptance_Date != null)
    {
      return SU.reformatDate(Acceptance_Date.toString());
    }
    else
    {
      return "";
    }
  }
  public String getCessation_Date()
  {
     if (Cessation_Date != null)
    {
      return SU.reformatDate(Cessation_Date.toString());
    }
    else
    {
      return "";
    }
  }
  public String getTariff_Id()
  {
    return Tariff_Id;
  }
  public String getCW_Works_Order()
  {
    return SU.isNull(CW_Works_Order,"");
  }
  public String getCost_Centre()
  {
    return Cost_Centre;
  }
  public String getBilling_Code()
  {
    return SU.isNull(Billing_Code,"");
}
  public String getReference()
  {
    return SU.isNull(Reference,"");
  }
  public String getBilling_Transfer_Flag()
  {
    return SU.isNull(Billing_Transfer_Flag,"");
  }
  public String getService_Id()
  {
    return Service_Id;
  }
  public String getService_Item_Status_Id()
  {
    return Service_Item_Status_Id;
  }
  public String getCW_Cessation_Reference()
  {
    return CW_Cessation_Reference;
  }
  public String getNotes()
  {
   return Notes;
  }
  public String getCMDB_Service_Item_Id()
  {
    return CMDB_Service_Item_Id;
  }
  private String getProduct_Id_from_Name(String name)
  {
    StringBuffer SQLBuff = new StringBuffer("");

    SQLBuff.append("select convert(varchar(5),CMDB_Product_Id) from Barclays..Product where Product_Name ='");
    SQLBuff.append(name).append("'");

    SQL=SQLBuff.toString();

    return DBC.getValue(SQL,Barclays);
  }
  public String getProduct_Id()
  {
    return Product_Id;
  }
  public String getProduct_Description()
  {
    return Product_Description;
  }
  public String getSite_Name()
  {
    return Site_Name;
  }
  public String getService_Reference()
  {
    return Service_Reference;
  }
  public String getService_Description()
  {
    return Service_Description;
  }
/***************************************************************************/
public boolean isValid(String ButtonPressed)
{
	Enumeration FormFields=mandatory.elements();
	setErrored("clear");
        Message = "<font color=red><b>";
	String FormField="";
	String FieldName;
	String value="";

	//Circuit Ref already exists for a 'Create'?
	if ((Service_Item_Status_Id.compareTo("0")==0) && ((Cessation_Date == null)))
	{
	  Message = "Service has a status of 'Cancelled' or 'Closed' but no Cessation Date has been supplied";
	  //writeLog
	}
	if ((!Exists()) && ((Cessation_Date == null)))
	{
	  Message = "Service has a status of 'Cancelled' or 'Closed' but no Cessation Date has been supplied";
	}

	return true;
}
/*******************************************************************************/
public boolean getService()
{
    try{
      Conn = DBC.Connect(PREPARE,Barclays);
      if (Conn != null)
      {
        SQL = "{call barclays..get_Service ";
        SQL += "(?)}";
	cstmt = Conn.prepareCall(SQL);
	cstmt.setString(1,Service_Reference);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    Barclays_Site_Id=RS.getString(1);
	    Site_Name=RS.getString(2);
	    Product_Description=RS.getString(4);
    	    Acceptance_Date=(RS.getDate(5));
    	    Cessation_Date=RS.getDate(6);
       	    Billing_Start_Date=(RS.getDate(7));
    	    ModifiedBy=RS.getString(8);

	    close(Conn);
	  }
	}
	catch(java.sql.SQLException se)
        {
	  Message=se.getMessage();
	  close(Conn);
	  return false;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      close(Conn);
      return false;
    }
    catch(java.lang.NullPointerException se){Message="<font color=red><b>Null Pointer "+se.getMessage();return false;}//message set in underlying code
    return true;
}
/*****************************************************************************************/
public boolean create(Connection Conn)
{
    try
    {
      //Conn=DBC.Connect(WRITE,P4);
      if (Conn !=null)
      {
        SQL = "{call barclays..Create_Service_Item ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
	cstmt.setInt(1,SU.toInt(CMDB_Service_Item_Id));
	cstmt.setString(2,Service_Reference);
	cstmt.setString(3,Product_Description);
	cstmt.setString(4,Reference);
	cstmt.setString(5,Tariff_Id);
	cstmt.setString(6,Cost_Centre);
	cstmt.setDate(7,Acceptance_Date);
	cstmt.setString(8,CW_Works_Order);
	cstmt.setDate(9,Cessation_Date);
	cstmt.setString(10,CW_Cessation_Reference);
	cstmt.setString(11,Billing_Code);
	cstmt.setDate(12,Billing_Start_Date);
	cstmt.setDate(13,Billing_Cease_Date);
	cstmt.setString(14,Billing_Transfer_Flag);
	cstmt.setString(15,Notes);
	cstmt.setString(16,ModifiedOn);
	cstmt.setString(17,ModifiedBy);
	cstmt.setString(18,Barclays_Site_Id);
	cstmt.setString(19,Site_Name);
	try
	{
          cstmt.executeUpdate();
	  closeupdate(Conn);
	}
	catch(java.sql.SQLException se)
        {
	  Message=se.getMessage();
	  close(Conn);
	  return false;
        }
      }
      else
      { //Failed to connect - message set in underlying code
	Message=DBC.getMessage();
	return false;
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      return false;
    }
    catch(java.lang.NullPointerException se){Message="<font color=red><b>"+se.getMessage();return false;}//message set in underlying code
    //Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
    return true;
  }
/********************************************************************/
public boolean Exists()
{

  SQL = "Select 'Found' from Barclays..Service_Item where CMDB_Service_Item_Id = "+ CMDB_Service_Item_Id;

  if(DBC.getExists(SQL,Barclays))
  {
    return true;
  }
  else
  {
    return false;
  }
}
 /********************************************************************/
  //for debugging
/*  public static void main (String[] args)
  {
    boolean OK;
    BANBean BAN=new BANBean();
    CircuitBANBean ctBAN=new CircuitBANBean();
    BAN.setCircuit_Reference("34T5345");
	ctBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	ctBAN.inheritGlobalCustomerId(BAN.getGlobalCustomerId());
	ctBAN.setAccount_Id(BAN.getAccount_Id());
	ctBAN.setCircuit_Reference(BAN.getCircuit_Reference());
	ctBAN.getCircuit();

 }*/
}