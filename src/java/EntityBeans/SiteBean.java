package EntityBeans;

import java.sql.*;
import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import JavaUtil.StringUtil;

public class SiteBean extends EntityBean
{
    private String SQL;
    private String Building_Reference="",Site_Status_Id="",Reference="",Customer_Code="";
    private String Organisation_Id="",Site_Address="",Town="",Country="",Site_Address_Lines[]={"","","","",""};
    private String Site_Type_Id="",Service_Level_Id="",Closure_Reference="",Name="";
    private String Notes="",Record_Id="";
    private String Billing_Region="",Service_Location_Code;
    protected Vector mandatory=new Vector();
    private java.sql.Date Acceptance_Date,Closure_Date;
    private Hashtable ScreenData=new Hashtable();
    private JavaUtil.StringUtil SU = new JavaUtil.StringUtil();

  public SiteBean ()
  {
    mandatory.clear();
    mandatory.addElement("Site_Type_Id");
    mandatory.addElement("Building_Reference");
    mandatory.addElement("Circuit_Reference");
    mandatory.addElement("Customer_Code");
    mandatory.addElement("Organisation_Id");
    mandatory.addElement("Site_Address");
    mandatory.addElement("Country");
    mandatory.addElement("Closure_Reference");
    mandatory.addElement("Name");
    mandatory.addElement("Contract_Number");

    errored.addElement("");
  }
  public void Reset()
  {
    super.Reset();
    Building_Reference="";
    Site_Status_Id="";
    Reference="";
    Customer_Code="";
    Organisation_Id="";
    Site_Address="";
    Town="";
    Country="";
    Site_Address_Lines[0]=Site_Address_Lines[1]=Site_Address_Lines[2]=Site_Address_Lines[3]=Site_Address_Lines[4]="";
    Site_Type_Id="";
    Service_Level_Id="L";
    Closure_Reference="";
    Name="";
    Notes="";
    Closure_Date=null;
    setErrored("clear");
  }

/*set Methods*/
  public void setOrganisation_Id(String value)
  {
   Organisation_Id = value;
  }
  public void setRecord_Id(String value)
  {
   Record_Id = value;
  }
  public void setSite_Address(String value)
  {
   Site_Address = SU.isNull(value,"");
  }
  public void setTown(String value)
  {
   Town = value;
  }
  public void setCountry(String value)
  {
   Country = value;
  }
  public void setSite_Type_Id(String value)
  {
   Site_Type_Id = value;
  }
  public void setBuilding_Reference(String value)
  {
   Building_Reference = SU.isNull(value,"");
  }
  public void setSite_Status_Id(String value)
  {
   Site_Status_Id = value;
  }
  public void setService_Level_Id(String value)
  {
   Service_Level_Id = value;
  }
  public void setService_Location_Code(String value)
  {
   Service_Location_Code = value;
  }
  public void setAcceptance_Date(String value)
  {
    Acceptance_Date = SU.toJDBCDate(value,"MMM dd yyyy hh:mma");
  }
  public void setClosure_Date(String value)
  {
    Closure_Date = SU.toJDBCDate(value,"MMM dd yyyy hh:mma");
  }
  public void setReference(String value)
  {
   Reference = SU.isNull(value,"");
  }
  public void setClosure_Reference(String value)
  {
    Closure_Reference=value;
  }
  public void setName(String value)
  {
    Name=value;
  }
  public void setCustomer_Code(String value)
  {
   Customer_Code = SU.isNull(value,"");
  }
  public void setNotes(String value)
  {
   Notes = SU.isNull(value,"");
  }
  public void setCompany(String value)
  {
   Global_Customer_Id="BAR";
  }
  public void setBilling_Region(String value)
  {
   Billing_Region=value;
  }
  /****************************************************************************/
  public void setValuefromTag(String name,String value)
  {
      if (name.compareToIgnoreCase("ORGANISATIONID")==0)
      {
	setOrganisation_Id(value);
      }
      else if ((name.compareToIgnoreCase("NAME")==0) ||
	      (name.compareToIgnoreCase("SiteName")==0))
      {
	setName(value);
      }
      else if (name.compareToIgnoreCase("REFERENCE")==0)
      {
	setReference(value);
      }
      else if ((name.compareToIgnoreCase("CUSTOMERCODE")==0) ||
	      (name.compareToIgnoreCase("CustomerCode")==0))
      {
	setCustomer_Code(value);
      }
      else if ((name.compareToIgnoreCase("BUILDINGREFERENCE")==0) ||
	      (name.compareToIgnoreCase("BuildingReference ")==0))
      {
	setBuilding_Reference(value);
      }
      else if ((name.compareToIgnoreCase("SITESTATUSID")==0) ||
	      (name.compareToIgnoreCase("SiteStatus")==0))
      {
	setSite_Status_Id(value);
      }
      else if ((name.compareToIgnoreCase("SITETYPEID")==0) ||
	      (name.compareToIgnoreCase("SiteType")==0))
      {
	setSite_Type_Id(value);
      }
      else if ((name.compareToIgnoreCase("SERVICELEVELID")==0) ||
	      (name.compareToIgnoreCase("ServiceLevel")==0))
      {
	setService_Level_Id(value);
      }
      else if ((name.compareToIgnoreCase("ACCEPTANCEDATE")==0) ||
	      (name.compareToIgnoreCase("SiteAcceptanceDate")==0))
      {
	setAcceptance_Date(value);
      }
      else if ((name.compareToIgnoreCase("CLOSUREDATE")==0) ||
	      (name.compareToIgnoreCase("SiteClosureDate ")==0))
      {
	setClosure_Date(value);
      }
      else if ((name.compareToIgnoreCase("CLOSUREREFERENCE")==0) ||
	      (name.compareToIgnoreCase("ClosureRef  ")==0))
      {
	setClosure_Reference(value);
      }
      else if (name.compareToIgnoreCase("CMDBSiteIdentifier")==0)
      {
	setRecord_Id(value);
      }
      else if (name.compareToIgnoreCase("Company")==0)
      {
	setCompany(value);
      }
      else if (name.compareToIgnoreCase("Subsiduary")==0)
      {
	setBilling_Region(value);
      }
      else if (name.compareToIgnoreCase("Address1")==0)
      {
	Site_Address_Lines[0]=value;
      }
      else if (name.compareToIgnoreCase("Address2")==0)
      {
	Site_Address_Lines[1]=value;
      }
      else if (name.compareToIgnoreCase("Town")==0)
      {
	setTown(value);
      }
      else if (name.compareToIgnoreCase("County")==0)
      {
	Site_Address_Lines[2]=value;
      }
      else if (name.compareToIgnoreCase("PostCode")==0)
      {
	Site_Address_Lines[3]=value;
      }
      else if (name.compareToIgnoreCase("ServiceLocationCode")==0)
      {
	setService_Location_Code(value);
      }
      else if (name.compareToIgnoreCase("LastModifiedOn")==0)
      {
	setModifiedOn(ModifiedOn);
      }
      else if (name.compareToIgnoreCase("LastModifiedBy")==0)
      {
	setModifiedBy(ModifiedBy);
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
  public String getClosure_Date()
  {
     if (Closure_Date != null)
    {
      return SU.reformatDate(Closure_Date.toString());
    }
    else
    {
      return "";
    }
  }
  public String getOrganisation_Id()
  {
    return Organisation_Id;
  }
  public String getSite_Address()
  {
    return Site_Address;
  }
  public String getCountry()
  {
    return SU.isNull(Country,"");
  }
  public String getTown()
  {
    return Town;
  }
  public String getSite_Type_Id()
  {
    return SU.isNull(Site_Type_Id,"");
}
  public String getBuilding_Reference()
  {
    return SU.isNull(Building_Reference,"");
  }
  public String getService_Level_Id()
  {
    return SU.isNull(Service_Level_Id,"");
  }
  public String getSite_Status_Id()
  {
    return Site_Status_Id;
  }
  public String getCustomer_Code()
  {
    return Customer_Code;
  }
  public String getClosure_Reference()
  {
    return Closure_Reference;
  }
  public String getName()
  {
    return Name;
  }
  public String getNotes()
  {
   return Notes;
  }
  public String getReference()
  {
   return Reference;
  }
/***************************************************************************/
public boolean isSiteValid(String ButtonPressed)
{
	Enumeration FormFields=mandatory.elements();
	setErrored("clear");
        Message = "<font color=red><b>";
	String FormField="";
	String FieldName;
	String value="";

	//Circuit Ref already exists for a 'Create'?
	//No Billing End Date for a 'Cease'?
	return true;
}
/*******************************************************************************/
/*****************************************************************************************/
public boolean create(Connection Conn)
{
  int i=0;
  Enumeration Site_Address_UnPacked;

    try
    {
      /*if (Site_Address.compareTo("") !=0 )
      {
	  Site_Address_UnPacked = SU.unpackTextArea(Site_Address);

	  while ((Site_Address_UnPacked.hasMoreElements()) && (i < 5))
	  {
	    Site_Address_Lines[i]=(String)Site_Address_UnPacked.nextElement();
	    i++;
	  }
      }
      while (i < 5)
      {
	Site_Address_Lines[i]="";
	i++;
      }
      */
      //Conn=DBC.Connect(WRITE,P3);
      if (Conn !=null)
      {
        SQL = "{call barclays..Create_Site ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
	cstmt.setString(1,"BAR");
	cstmt.setString(2,Reference);
	cstmt.setString(3,Billing_Region);
	cstmt.setString(4,Name);
	cstmt.setString(5,Site_Status_Id);
	cstmt.setString(6,Site_Type_Id);
	cstmt.setString(7,Service_Level_Id);
	cstmt.setDate(8,Acceptance_Date);
	cstmt.setDate(9,Closure_Date);
	cstmt.setString(10,Closure_Reference);
	cstmt.setString(11,Building_Reference);
	cstmt.setString(12,Customer_Code);
	cstmt.setString(13,ModifiedBy);
	cstmt.setString(14,ModifiedOn);

	try
	{
          cstmt.execute();
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
      return false;
    }
    catch(java.lang.NullPointerException se){Message="<font color=red><b>"+se.getMessage();return false;}//message set in underlying code
    //Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
    return true;
  }

/********************************************************************/
public boolean SiteExists()
{
    return false;
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