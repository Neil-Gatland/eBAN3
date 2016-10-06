package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class SiteBANBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private String siteReference;
    private String siteName;
    private String siteAddress;
    private String country;
    private String billingRegion;
    private String geoCode;

  public SiteBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("Site_Reference");
    mandatory.addElement("Site_Name");
    mandatory.addElement("Country");
    mandatory.addElement("Billing_Region_Site");
    mandatory.addElement("Geo_Code");
    errored.clear();//.addElement("");
    siteReference = "";
    siteName = "";
    siteAddress = "";
    country = "United Kingdom";
    billingRegion = "";
    geoCode = "770000000";
  }

  public void Reset()
  {
    super.Reset();
    siteReference = "";
    siteName = "";
    siteAddress = "";
    country = "United Kingdom";
    billingRegion = "";
    geoCode = "770000000";
    errored.clear();//.addElement("");
  }
/*set Methods*/
  public void setSiteReference(String value)
  {
    ScreenData.put("Site_Reference",value);
    siteReference = SU.isNull(value,"");
  }
  public void setSiteName(String value)
  {
    siteName = SU.isNull(value,"");
    ScreenData.put("Site_Name",value);
  }
  public void setSiteAddress(String value)
  {
    siteAddress = SU.isNull(value,"");
    ScreenData.put("Site_Address",value);
  }
  public void setCountry(String value)
  {
    country = SU.isNull(value,"");
    ScreenData.put("Country",value);
  }
  public void setBillingRegion(String value)
  {
    billingRegion = SU.isNull(value,"");
    ScreenData.put("Billing_Region_Site",value);
  }
  public void setGeoCode(String value)
  {
    geoCode = SU.isNull(value,"");
    ScreenData.put("Geo_Code",value);
  }
  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.compareTo("Site_Reference")==0)
    {
      setSiteReference(value);
    }
    else if (name.compareTo("Required_BAN_Effective_Dateh")==0)
    {
     setRequired_BAN_Effective_Date(value);
     ScreenData.put("Required_BAN_Effective_Dateh",value);
    }
    else if (name.compareTo("BAN_Summary")==0)
    {
      setBAN_Summary(value);
      ScreenData.put("BAN_Summary",value);
    }
    else if (name.compareTo("Site_Name")==0)
    {
      setSiteName(value);
    }
    else if (name.compareTo("Site_Address")==0)
    {
      setSiteAddress(value);
    }
    else if (name.compareTo("Country")==0)
    {
      setCountry(value);
    }
    else if (name.compareTo("Billing_Region_Site")==0)
    {
      setBillingRegion(value);
    }
    else if (name.compareTo("Geo_Code")==0)
    {
      setGeoCode(value);
    }
}
/*******************************************************************************/
/*get Methods*/
  public String getSiteReference()
  {
    return siteReference;
  }
  public String getSiteName()
  {
    return siteName;
  }
  public String getSiteAddress()
  {
    return siteAddress;
  }
  public String getCountry()
  {
    return country;
  }
  public String getBillingRegion()
  {
    return billingRegion;
  }
  public String getGeoCode()
  {
    return geoCode;
  }
  public String getMode(String FieldName)
  {
    if ((Mode.compareTo("Amend")==0) && (FieldName.compareTo("Site_Reference")== 0))
    {
      return "READONLY";
    }
    if (action.compareTo("Authorise") == 0)
    {
      return "READONLY";
    }
    if (FieldName.compareTo("Geo_Code") ==0)
    {
      if ((country.equalsIgnoreCase("USA")) || (country.equalsIgnoreCase("Canada")))
      {
        return InputMode;
      }
      else
      {
        setGeoCode("770000000");
        return "READONLY";
      }
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
	    if ((FormField.equals("")) || (SU.isSpaces(FormField)))
	    {
	      setErrored(FieldName);
	    }
	  }
        }
	if ((Mode.compareTo("Create")==0) && (SiteExists()))
	{
	  setErrored("Site_Reference");
	  Message = "<font color=red><b>A Site with this name already exists";
	  return false;
	}
	if (geoCode.length() != 9)
	{
	  setErrored("Geo_Code");
	  Message = "<font color=red><b>Geocode must be 9 characters long";
	  return false;
	}
        try
        {
          double temp = Double.parseDouble(geoCode);
          if (temp < 0)
          {
            setErrored("Geo_Code");
            Message = "<font color=red><b>Geocode cannot be negative";
            return false;
          }
          else if (temp == 0)
          {
            setErrored("Geo_Code");
            Message = "<font color=red><b>Geocode cannot be all zeros";
            return false;
          }
        }
        catch (java.lang.NumberFormatException NE)
        {
          setErrored("Geo_Code");
          Message = "<font color=red><b>Geocode must be numeric";
          return false;
        }
	if (((country.equals("USA")) || (country.equals("Canada"))) &&
          (geoCode.equals("770000000")))
	{
	  setErrored("Geo_Code");
	  Message = "<font color=red><b>Geocode cannot be 770000000 for " + country;
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
public boolean SiteExists()
{

  SQL = "Select 'Found' from givn_ref..Site where Site_Id = '"+ siteReference + "'";

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
public boolean authoriseSiteBAN()
{
  boolean authoriseSiteBAN = false;
  Required_BAN_Effective_Dateh = new java.sql.Date((new java.util.Date()).getTime());


    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Authorise_Site_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,banStatus);
	cstmt.setString(3,BAN_Summary);
	cstmt.setDate(4,Required_BAN_Effective_Dateh);
        cstmt.setString(5,banCreatedBy);
        cstmt.setString(6,siteReference);
        cstmt.setString(7,siteName);
        cstmt.setString(8,siteAddress);
        cstmt.setString(9,billingRegion);
        cstmt.setString(10,country);
        cstmt.setString(11,geoCode);
        cstmt.setString(12,Mode);

	try
	{
          cstmt.execute();
	  RS = cstmt.getResultSet();
	  if (RS.next())
	  {
            String ret = RS.getString(1);
            if (ret.startsWith("-"))
            {
              authoriseSiteBAN = false;
              Message = "<font color=red><b>Unable to authorise BAN: " +
                (ret.equals("-99")?"a bill job is currently running for this customer."
                :("return code " + ret)) + "</b></font>";

            }
            else
            {
              authoriseSiteBAN = true;
              banIdentifier = ret;
              Message = "<font color=blue><b>BAN Id:-" + banIdentifier +
                " authorised and site:-" + siteReference +
                (Mode.equals("Create")?" created":" updated");
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
      return authoriseSiteBAN;
    }
  }
/**********************************************************************************************/
  public boolean updateSiteBAN()
  {
    boolean updateSiteBAN = false;


    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..update_Site_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
        cstmt.setString(2,banStatus);
	cstmt.setString(3,BAN_Summary);
	cstmt.setDate(4,Required_BAN_Effective_Dateh);

        cstmt.execute();
        String tempId = banIdentifier;
        Reset();
        updateSiteBAN = true;
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
      return updateSiteBAN;
    }
  }
/*********************************************************************/
 public boolean getExistingSite()
 {
    boolean getSite = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call givn_ref..get_Site ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,siteReference);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    siteName=RS.getString(1);
	    siteAddress=RS.getString(2);
	    country=RS.getString(3);
	    billingRegion=RS.getString(4);
	    geoCode=RS.getString(5);
            getSite = true;
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
      return getSite;
    }
 }
}