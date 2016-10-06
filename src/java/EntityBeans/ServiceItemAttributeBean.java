package EntityBeans;

import java.sql.*;
import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import JavaUtil.StringUtil;

public class ServiceItemAttributeBean extends EntityBean
{
    private String SQL;
    private String CMDB_Service_Item_Attribute_Id="",Item_Value="",Product_Attribute_Id="";
    private String CMDB_Service_Item_Id="",Product_Id="";
    protected Vector mandatory=new Vector();

    //private Hashtable ScreenData=new Hashtable();
    private JavaUtil.StringUtil SU = new JavaUtil.StringUtil();

  public ServiceItemAttributeBean ()
  {
  }
  public void Reset()
  {
    super.Reset();
    CMDB_Service_Item_Attribute_Id="";
    Item_Value="";
    Product_Attribute_Id="";
    setErrored("clear");
  }
/*set Methods*/
  public void setCMDB_Service_Item_Attribute_Id(String value)
  {
   CMDB_Service_Item_Attribute_Id = SU.isNull(value,"");
  }
  public void setItem_Value(String value)
  {
   Item_Value = value;
  }
  public void setProduct_Attribute_Id(String value)
  {
   Product_Attribute_Id = SU.isNull(value,"");
  }
  public void setCMDB_Service_Item_Id(String value)
  {
    CMDB_Service_Item_Id=value;
  }
  public void setProduct_Id(String value)
  {
   Product_Id = SU.isNull(value,"");
  }

 /**************************************************************/
  public void setValuefromTag(String name,String value)
  {
    if (name.compareToIgnoreCase("RecordId")==0)
    {
      setCMDB_Service_Item_Attribute_Id(value);
    }
    if (name.compareToIgnoreCase("ServiceItemId")==0)
    {
      setCMDB_Service_Item_Id(value);
    }
    else if ((name.compareToIgnoreCase("Item_Value")==0) ||
	    (name.compareToIgnoreCase("ITEMVALUE") ==0))
    {
      setItem_Value(value);
    }
    else if ((name.compareToIgnoreCase("Product_Attribute_Id") == 00) ||
	    (name.compareToIgnoreCase("PRODUCTATTRIBUTEID") ==0))
    {
      setProduct_Attribute_Id(value);
    }
    else
    {// assume an attribute name has been supplied, so do a lookup
     // on the Product Attribute table

      setProduct_Attribute_Id(getAttribute_Id_from_Name(name));
      setItem_Value(value);
    }
  }
  /****************************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.compareToIgnoreCase("CMDB_Service_Item_Attribute_Id")==0)
    {
      setCMDB_Service_Item_Attribute_Id(value);
    }
    else if (name.startsWith("Item_Value"))
    {
      setItem_Value(value);
    }
    else if ((name.compareToIgnoreCase("Product_Attribute_Id")==0) ||
	    (name.compareToIgnoreCase("PRODUCTATTRIBUTEID")==0))
    {
      setProduct_Attribute_Id(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getCMDB_Service_Item_Attribute_Id()
  {
    return SU.isNull(CMDB_Service_Item_Attribute_Id,"");
  }
  public String getItem_Value()
  {
    return Item_Value;
  }
  private String getAttribute_Id_from_Name( String name)
  {
    StringBuffer SQLBuff = new StringBuffer("");

    SQLBuff.append("select convert(varchar(5),Product_Attribute_Id) from Barclays..Product_Attribute where CMDB_Product_Id =");
    SQLBuff.append(Product_Id).append(" and Name = '");
    SQLBuff.append(name).append("'");

    SQL=SQLBuff.toString();

    return DBC.getValue(SQL,Barclays);
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

	if (Feed.compareTo("Audit") == 0)
	{
	  if (Product_Attribute_Id.compareTo("") == 0)
	  {
	    Message="Attribute Id not supplied";
	    Tag_Id="Service Item:- "+CMDB_Service_Item_Id;
	    Tag_Name="PRODUCTATTRIBUTEID";
	    Tag_Value=Item_Value;
	  }
	}
	writeLog();
	return true;
}
/*******************************************************************************/
public boolean get()
{
    try{
      Conn = DBC.Connect(PREPARE,Barclays);
      if (Conn != null)
      {
        SQL = "{call barclays..get_service_item_attribute ";
        SQL += "(?)}";
	cstmt = Conn.prepareCall(SQL);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    ModifiedBy=RS.getString(7);
	    CMDB_Service_Item_Attribute_Id=RS.getString(11);
	    Item_Value=RS.getString(12);
	    Product_Attribute_Id=RS.getString(31);

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
        SQL = "{call barclays..Create_Service_Item_Attribute ";
        SQL += "(?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
	cstmt.setInt(1,SU.toInt(CMDB_Service_Item_Attribute_Id));
	cstmt.setInt(2,SU.toInt(CMDB_Service_Item_Id));
	cstmt.setInt(3,SU.toInt(Product_Attribute_Id));
	cstmt.setString(4,Item_Value);
	cstmt.setString(5,ModifiedOn);
	cstmt.setString(6,ModifiedBy);
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
/**********************************************************************************************/
  public boolean update()
  {

    try
    {
      //Conn=DBC.Connect(WRITE,P4);
      if (Conn != null)
      {
	SQL = "{call barclays..update_Site ";
	SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	cstmt = Conn.prepareCall(SQL);

	cstmt.setString(10,CMDB_Service_Item_Attribute_Id);
	cstmt.setString(11,Item_Value);
	cstmt.setString(30,Product_Attribute_Id);

	cstmt.execute();
        closeupdate(Conn);
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      closeupdate(Conn);
      return false;
    }
    catch(java.lang.NullPointerException se){Message="<font color=red><b>"+se.getMessage();return false;}//message set in underlying code
   return true;
  }
/********************************************************************/
public boolean Exists()
{

  SQL = "Select 'Found' from OSS..Circuit where CW_Circuit_Ref = ''";

  if(DBC.getExists(SQL,Barclays))
  {
    return true;
  }
  else
  {
    return false;
  }
}
private boolean writeLog()
{
  return true;
}
 /********************************************************************/
  //for debugging
/*  public static void main (String[] args)
  {
    boolean OK;
    BANBean BAN=new BANBean();
    CircuitBANBean ctBAN=new CircuitBANBean();
    BAN.setCircuit_CMDB_Service_Item_Attribute_Id("34T5345");
	ctBAN.setGlobalCustomerName(BAN.getGlobalCustomerName());
	ctBAN.inheritGlobalCustomerId(BAN.getGlobalCustomerId());
	ctBAN.setAccount_Id(BAN.getAccount_Id());
	ctBAN.setCircuit_CMDB_Service_Item_Attribute_Id(BAN.getCircuit_CMDB_Service_Item_Attribute_Id());
	ctBAN.getCircuit();

 }*/
}