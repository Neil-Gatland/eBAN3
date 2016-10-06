package EntityBeans;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class CustomerBean extends EntityBean
{
    private String SQL;
    private String Organisation_Type_Id="",Reference="",Parent_Organisation_Id="",Organisation_Status_Id,Notes="";

    protected Vector mandatory=new Vector();

  public CustomerBean ()
  {
    mandatory.clear();
  }

  public void Reset()
  {
    super.Reset();
  }
  /*set Methods*/

  public void setOrganisation_Type_Id(String value)
  {
    Organisation_Type_Id=value;
  }

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
  public void setGlobalCustomerId(String value)
  {
    Global_Customer_Id=value;
  }
  public void setReference(String value)
  {
    Reference=value;
  }
  public void setParent_Organisation_Id(String value)
  {
    Parent_Organisation_Id=value;
  }
  public void setOrganisation_Status_Id(String value)
  {
    Organisation_Status_Id=value;
  }
  public void setNotes(String value)
  {
    Notes=value;
  }

  /***********/
  /*public void setMode(String value)
  {
    super.setMode(value);
    if (Mode.compareTo("Create")==0)
    {
      Reset();
      ScreenData.clear();
    }8?
  }

/*****************************************************************/
public void setValuefromTag(String name,String value)
{
  if (name.compareToIgnoreCase("ORGANISATIONTYPEID")==0)
  {
    setOrganisation_Type_Id(value);
  }
  else if (name.compareToIgnoreCase("NAME")==0)
  {
    setGlobalCustomerName(value);
    setGlobalCustomerIdfromName(value);
  }
  else if (name.compareToIgnoreCase("REFERENCE")==0)
  {
    setReference(value);
  }
  else if (name.compareToIgnoreCase("PARENTORGANISATIONID")==0)
  {
    setParent_Organisation_Id(value);
  }
  else if (name.compareToIgnoreCase("ORGANISATIONSTATUSID")==0)
  {
    setOrganisation_Status_Id(value);
  }
  else if (name.compareToIgnoreCase("NOTES")==0)
  {
    setNotes(value);
  }
}
/*****************************************************************/
/*get Methods*/

/******************************************************************************/
public boolean isValid(String ButtonPressed)
{
  Enumeration FormFields=mandatory.elements();
  setErrored("clear");
  Message = "<font color=red><b>";
  String FormField="";
  String FieldName;
  String value="";
  return true;
}
/*******************************************************************************/
public boolean AccountExists()
{

  SQL = "Select 'Found' from givn_ref..Invoice_Region where Account_Id = '"+ Account_Id + "'";


  if(DBC.getExists(SQL,GIVN))
  //Datasource changes if(DBC.getExists(SQL))
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

  if(DBC.getExists(SQL,GIVN))
  //Datasource changes if(DBC.getExists(SQL))
  {
    return true;
  }
  else
  {
    return false;
  }
}

/*******************************************************************************/
public boolean create()
{
    StringBuffer SQLBuff=new StringBuffer("");
    try{
	  Conn=DBC.Connect(WRITE,Barclays);
	  if (Conn != null)
	  {
	    SQLBuff.append("insert into Barclays..Organisation (");
	    SQLBuff.append("Audit_Trail_Id,Record_Id,Version_Number,Modified_Type,Global_Customer_Id,");
	    SQLBuff.append("Global_Customer_Name,Organisation_Type,Reference,Parent_Organisation_Id,");
	    SQLBuff.append("Organisation_Status_Id,Notes,Run_Id,Last_Update_Id,Last_Update_Date) values");
	    SQLBuff.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

	    pstmt = Conn.prepareStatement(SQLBuff.toString());
	    pstmt.setString(1,AuditTrailId);
	    pstmt.setString(2,RecordId);
	    pstmt.setString(3,VersionNumber);
	    pstmt.setString(4,ModifiedType);
	    pstmt.setString(5,Global_Customer_Id);
	    pstmt.setString(6,Global_Customer_Name);
	    pstmt.setInt(7,SU.toInt(Organisation_Type_Id));
	    pstmt.setString(8,Reference );
	    pstmt.setInt(9,SU.toInt(Parent_Organisation_Id) );
	    pstmt.setInt(10,SU.toInt(Organisation_Status_Id) );
	    pstmt.setString(11,Notes);
	    pstmt.setString(12, Run_Id);
    	    pstmt.setString(13, ModifiedBy);
	    pstmt.setString(14, ModifiedOn);
	    try
	    {
	      pstmt.execute();
	      close(Conn);
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
	  close(Conn);
	  return false;
	}
	catch(java.lang.NullPointerException se){Message="<font color=red><b>"+se.getMessage();return false;}//message set in underlying code
	return true;
  }
/**********************************************************************************************/
}