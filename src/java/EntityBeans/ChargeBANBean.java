package EntityBeans;

import java.sql.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.math.BigInteger;
import java.util.Hashtable;
//import DBUtilities.BANBean;

public class ChargeBANBean extends BANBean
{
    private String Charge_Description="",Charge_Category="",Charge_CategoryDisplay="";
    private java.sql.Date Charge_Valid_From_Date=null,Charge_Valid_To_Date,Down_Time_Date;
    private String Charge_Frequency="Quarterly",Revenue_Reason_Code="",Site_Id="";
    private String Charge_Type="",Contract_Number="",Percentage="";
    private String Currency_Display="<font color=black> (UK Pounds Sterling)";
    private String Service_Reference,Down_Time;
    private float Gross_Amount=0;
    protected Vector mandatory=new Vector();
    private int Charge_Id;
    private Hashtable ScreenData=new Hashtable();

   public ChargeBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("Currency_Desc");
    mandatory.addElement("BAN_Summary");
    mandatory.addElement("Gross_Amount");
    mandatory.addElement("Required_BAN_Effective_Date");
    mandatory.addElement("Charge_Type");
    mandatory.addElement("From_Charge_Valid_Dateh");
    mandatory.addElement("Charge_Description");
  }
  public void Reset()
  {
    super.Reset();
    Charge_Description="";
    Charge_Category="";
    Charge_Valid_From_Date=null;
    Charge_Valid_To_Date=null;
    Down_Time_Date=null;
    Charge_Frequency="Quarterly";
    Revenue_Reason_Code="";
    Charge_Type="";
    Percentage="0.0";
    Currency_Display="<font color=black> (UK Pounds Sterling)";
    Gross_Amount=0;
    Down_Time="";
    Contract_Number="";
    Site_Id="";
    ScreenData.clear();
    setErrored("clear");
  }
  //Get Methods
  public String getCharge_Category()
  {
    return Charge_Category;
  }
  public String getCharge_CategoryDisplay()
  {
    return SU.isNull(Charge_CategoryDisplay," ");
  }
  public String getCharge_Description()
  {
    return Charge_Description;
  }
  public String getGross_Amount()
  {
    if ((String)ScreenData.get("Gross_Amount") != null)
    {
      return (String)ScreenData.get("Gross_Amount");
    }
    else
    {
      return String.valueOf(Gross_Amount);
    }
  }
  public String getCharge_Valid_From_Date()
  {
    if (Charge_Valid_From_Date==null)
      return null;
    else
      return SU.reformatDate(Charge_Valid_From_Date.toString());
  }
  public String getCharge_Valid_To_Date()
  {
     if (Charge_Valid_To_Date != null)
    {
      return SU.reformatDate(Charge_Valid_To_Date.toString());
    }
    else
    {
      return "";
    }
  }
  public String getDown_Time_Date()
  {
     if (Down_Time_Date != null)
    {
      return SU.reformatDate(Down_Time_Date.toString());
    }
    else
    {
      return "";
    }
  }
   public String getCRDB()
  {
    return SU.isNull(CRDB,"");
  }
  public String getCharge_Frequency()
  {
    return SU.isNull(Charge_Frequency,"");
  }
  public String getCharge_Type()
  {
    return Charge_Type;
  }
  public String getDown_Time()
  {
    return Down_Time;
  }
  public String getContract_Number()
  {
    return Contract_Number;
  }
  public String getRevenue_Reason_Code()
  {
    return SU.isNull(Revenue_Reason_Code,"");
  }
  public String getCurrency_Display()
  {
    return Currency_Display;
  }
  public String getPercentage()
  {
   return Percentage;
  }
  public String getChargeFieldVisibility(String value)
  {
     if (value.compareTo("Charge_Frequency")==0)
    {
      if (Charge_Category.compareTo("05")!=0)
        return "visible";
      else
	return "hidden";
    }
    if (value.compareTo("VAT")==0)
    {
      if (Charge_Category.compareTo("05")==0)
	return "visible";
      else
	return "visible";
    }
    if (value.compareTo("CRDB")==0)
    {
      if (Charge_Category.compareTo("05")==0)
	  return "visible";
      else
          return "hidden";
    }
    else
    {
	return "visible";
    }
  }
  public String getMode(String FieldName)
  {
    if (action.compareTo("View") == 0)
    {
      if ((FieldName.endsWith("Date")) ||
	  (FieldName.endsWith("Dateh")))
	  {
	    return "DISABLED";
	  }
	  else
	  {
            return "READONLY";
	  }
    }
    if (FieldName.compareTo("To_Charge_Valid_Date") ==0)
    {
      if (Mode.compareTo("Cease") == 0)
	  return "";
      else
	  return Calendar;
    }
    if ((action.compareTo("Cancel") == 0) ||
	  (action.compareTo("Cease") == 0) ||
	  (Mode.compareTo("Cease") == 0))
    {
      if ((FieldName.compareTo("BAN_Summary") ==0)  ||
	    (FieldName.compareTo("BAN_Reason") ==0))
      {
        return "";
      }
      else if (FieldName.compareTo("Effective_Date") ==0)
      {
        return "no";
      }
      else return InputMode;
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
/********************************************************************************************/
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
public void setParameter(String name,String value)
  {
    if (name.compareTo("Charge_Description")==0)
    {
      setCharge_Description(value);
    }
    else if (name.compareTo("CRDB")==0)
    {//if a 'Clear' action has been requested, don't clear the Credit/Debit Type
      if (value.compareTo("") != 0)
      {
        setCRDB(value);
      }
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
    else if (name.compareTo("Gross_Amount")==0)
    {
      setGross_Amount(value);
    }
    else if (name.compareTo("Required_BAN_Effective_Dateh")==0)
    {
      setRequired_BAN_Effective_Date(value);
      ScreenData.put("Required_BAN_Effective_Date",value);
    }
    else if (name.compareTo("Currency_Desc")==0)
    {
      if (value.compareTo("") != 0)
      {
        setCurrency_Desc(value);
        ScreenData.put("Currency_Desc",value);
      }
    }
    else if (name.compareTo("From_Charge_Valid_Dateh")==0)
    {
      setFrom_Charge_Valid_Date(value);
    }
    else if (name.compareTo("Down_Time_Dateh")==0)
    {
      setDown_Time_Date(value);
    }
    else if (name.compareTo("To_Charge_Valid_Dateh")==0)
    {
      setTo_Charge_Valid_Date(value);
    }
    else if (name.compareTo("Charge_Frequency")==0)
    {
      setCharge_Frequency(value);
    }
    else if (name.compareTo("Charge_Type")==0)
    {
      setCharge_Type(value);
      ScreenData.put("Charge_Type",value);
    }
    else if (name.compareTo("RejectReason")==0)
    {
      setRejectReason(value);
      ScreenData.put("RejectReason",value);
    }
    else if (name.compareTo("Down_Time")==0)
    {
      setDown_Time(value);
      ScreenData.put("Down_Time",value);
    }
    else if (name.compareTo("Revenue_Reason")==0)
    {
      setRevenue_Reason_Code(value);
      ScreenData.put("Revenue_Reason",value);
    }
    else if (name.compareTo("Percentage")==0)
    {
      setPercentage(value);
      ScreenData.put("Percentage",value);
    }
    else if (name.compareTo("Contract_Number")==0)
    {
      setContract_Number(value);
      ScreenData.put("Contract_Number",value);
    }
    else if (name.compareTo("Barclays_Site")==0)
    {
      setSite(value);
      ScreenData.put("Site_Id",value);
    }
  }
  /*********************************************************************/
  public void setCharge_Description(String value)
  {
    Charge_Description = value;
    ScreenData.put("Charge_Description",value);
  }
  public void setGross_Amount(String value)
  {
    ScreenData.put("Gross_Amount",value);
    if ((value.compareTo("")!=0) && (value !=null))
    {
      try
      {
        Gross_Amount = Float.parseFloat(value);
      }
      catch (java.lang.NumberFormatException NE)
      {
      }
    }
    else
    {
      Gross_Amount=0;
    }
  }
  public void setCRDB(String value)
  {
    CRDB=value;
    ScreenData.put("CRDB",value);
  }
  public void setTo_Charge_Valid_Date(String value)
  {
    Charge_Valid_To_Date = SU.toJDBCDate(value);
    ScreenData.put("To_Charge_Valid_Dateh",value);
  }
  public void setFrom_Charge_Valid_Date(String value)
  {
    Charge_Valid_From_Date = SU.toJDBCDate(value);
    ScreenData.put("From_Charge_Valid_Dateh",value);
  }
  public void setDown_Time_Date(String value)
  {
    Down_Time_Date = SU.toJDBCDate(value);
    ScreenData.put("Down_Time_Dateh",value);
  }
  public void setCharge_Frequency(String value)
  {
    Charge_Frequency=value;
    ScreenData.put("Charge_Frequency",value);
  }
  public void setCharge_Type(String value)
  {
    Charge_Type=value;
    ScreenData.put("Charge_Type",value);
  }
  public void setContract_Number(String value)
  {
    Contract_Number=value;
    ScreenData.put("Contract_Number",value);
  }
  public void setPercentage(String value)
  {
    Percentage=value;
    ScreenData.put("Percentage",value);
  }
  public void setDown_Time(String value)
  {
    Down_Time=SU.isNull(value,"");

    ScreenData.put("Down_Time",value);
  }
  public void setCharge_Category(String value)
  {
    Charge_CategoryDisplay=(SU.after(value));
    Charge_Category = SU.before(value);
    if (Charge_Category.compareTo("01")==0)
    {
      ChargeDateHeading="Charge Valid From Date";
      ChargeFieldVisibility="visible";
      CRDB="Debit";
    }
    else if (Charge_Category.compareTo("02")==0)
    {
      ChargeDateHeading="Charge Due Date";
      ChargeFieldVisibility="hidden";
      CRDB="Debit";
    }
    else if ((Charge_Category.compareTo("03")==0) || (Charge_Category.compareTo("04")==0))
    {
      ChargeDateHeading="Credit Payable Date";
      ChargeFieldVisibility="hidden";
      CRDB="Credit";
    }
    else if (Charge_Category.compareTo("05")==0)
    {
      ChargeDateHeading="Valid From Date";
      ChargeFieldVisibility="hidden";
    }
    else
    {
      ChargeDateHeading="Charge Date";
      ChargeFieldVisibility="hidden";
    }
  }
  public void setCharge_Id(String value)
  {
    Charge_Id=SU.toInt(value);
  }
  public void setRevenue_Reason_Code(String value)
  {
    Revenue_Reason_Code=value;
  }
  public void setMode(String value)
  {
    super.setMode(value);
    if (Mode.compareTo("Create")==0)
    {
      Reset();
    }
  }
/*******************************************************************************/
public boolean isValid(String ButtonPressed)
{
	Enumeration FormFields=mandatory.elements();
	setErrored("clear");
        Message = "<font color=red><b>";
	String FormField="";
	String FieldName;
	String value="";

	//First set of checks do not apply to draft saves
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
	  if ((Charge_Frequency.compareTo("") == 0) &&
	      (Charge_Category.compareTo("01") == 0))
	  {
	    setErrored("Charge_Frequency");
	  }
	  if ((CRDB.compareTo("") == 0) &&
	      (Charge_Category.compareTo("05") == 0))
	  {
	    setErrored("CRDB");
	  }
	  if ((!errored.isEmpty()) && (errored.size() > 0))
	  {
	      Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
	      //Message=(String)errored.firstElement();
	      return false;
	  }
	  if (((String.valueOf(Gross_Amount).compareTo("0")==0) ||
	      (String.valueOf(Gross_Amount).compareTo("0.0")==0)) &&
	      (Charge_Type.compareTo("07") != 0))
	  {
	    Message = "<font color=red><b>You must enter a value for Gross Amount";
	    setErrored("Gross_Amount");
	    return false;
	  }
	}
	//These checks apply to all saves
	if ((Charge_Valid_To_Date != null) && (Charge_Valid_From_Date != null))
	{
	  if ((Charge_Category.compareTo("01") == 0) &&
	      (Charge_Valid_To_Date.before(Charge_Valid_From_Date)))
	  {
	    setErrored("Charge_Valid_To_Date");
	    Message = "<font color=red><b>Valid To Date cannot be less than Valid From Date";
	    return false;
	  }
	}
	//Amounts must be numeric
        try
        {
          Gross_Amount = Float.parseFloat((String)ScreenData.get("Gross_Amount"));
        }
        catch (java.lang.NumberFormatException NE)
        {
	  setErrored("Gross_Amount");
	  Message = "<font color=red><b>Gross Amount must be numeric";
	  return false;
        }
      return true;
}
/****************************************************************************************/
public boolean getChargeBan()
{
    try{
      Conn=DBC.Connect(PREPARE,P5);
      if (Conn !=null)
      {
        SQL = "{call eban..Get_Barclays_Charge_BAN ";
        SQL += "(?)}";
	cstmt = Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
	  if (RS.next())
	  {
	    Service_Reference=RS.getString(1);
	    Global_Customer_Name=RS.getString(2);
	    Global_Customer_Id=RS.getString(3);
	    Account_Id=RS.getString(4);
	    banStatus=RS.getString(5);
	    Required_BAN_Effective_Dateh=RS.getDate(6);
	    banCreatedBy=RS.getString(7);
	    banAuthorisedBy=RS.getString(8);
	    BAN_Summary=RS.getString(9);
	    BAN_Reason=RS.getString(10);
	    Charge_Type=RS.getString(11);
	    Charge_Description=RS.getString(12);
	    setCurrency_Desc(RS.getString(13));
	    Gross_Amount=RS.getFloat(14);
	    Charge_Valid_From_Date=RS.getDate(15);
	    Charge_Valid_To_Date=RS.getDate(16);
	    Charge_Frequency=RS.getString(18);
	    setCRDB(RS.getString(20));
	    setCharge_Category(RS.getString(21));
	    rejectReason=RS.getString(22);
	    Revenue_Reason_Code=RS.getString(25);
	    Charge_Id=RS.getInt(26);
	    Mode=RS.getString(27);
	  }
	}
	catch(java.sql.SQLException se)
        {
	  Message=se.getMessage();
	  close(Conn);
	  return false;
        }
	close(Conn);
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
/****************************************************************************************/
public boolean getCharge()
{
    try{
      Conn=DBC.Connect(PREPARE,P5);
      if (Conn !=null)
      {
        SQL = "{call gcd..Get_Charge ";
        SQL += "(?)}";
	cstmt = Conn.prepareCall(SQL);
	cstmt.setInt(1,Charge_Id);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
	  if (RS.next())
	  {
	    Service_Reference=RS.getString(1);
	    Global_Customer_Name=RS.getString(2);
	    Global_Customer_Id=RS.getString(3);
	    Account_Id=RS.getString(4);
	    Charge_Type=RS.getString(5);
	    Charge_Description=RS.getString(6);
	    setCurrency_Desc(RS.getString(7));
	    Gross_Amount=RS.getFloat(8);
	    Charge_Valid_From_Date=RS.getDate(9);
	    Charge_Valid_To_Date=RS.getDate(10);
	    Charge_Frequency=RS.getString(12);
	    setCRDB(RS.getString(14));
	    setCharge_Category(RS.getString(15));
	    Revenue_Reason_Code=RS.getString(17);
	  }
	}
	catch(java.sql.SQLException se)
        {
	  Message=se.getMessage();
	  close(Conn);
	  return false;
        }
	close(Conn);
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
/****************************************************************************************/
  public String getChargeHeader()
 {
    StringBuffer HB=new StringBuffer("<br><table border=0><tr class=gridHeader>");

    //Column Headings

      HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu onclick=\"Toggle_Menu('Customer')\">Customer</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu onclick=\"Toggle_Menu('Account_List')\">Account</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=110><button class=grid_menu onclick=\"Toggle_Menu('GSR')\">Circuit</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=90><button class=grid_menu onclick=\"Toggle_Menu('Type')\">Type</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu>Description</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu onclick=\"Toggle_Menu('Month')\">From Date</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu>To Date</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=60><button class=grid_menu>Amount</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=45><button class=grid_menu>Select</button></td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr>");

      //Filters

      HB.append("<TR><TD class=grid1><SPAN id=Customer ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 225px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBC.getListBox("Global_Customer","submit",Global_Customer_Id_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD class=grid1><SPAN id=\"Account_List\" ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 225px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBC.getListBox("Account","submit",Account_for_List,Global_Customer_Id_for_List));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=1 class=grid1><SPAN id=GSR ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 225px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBC.getListBox("Circuit","submit",GSR_for_List,Global_Customer_Id_for_List+"|"+Account_for_List));
      //HB.append(DBC.getListBox("Global_Service_Reference","submit",GSR_for_List,Global_Customer_Id_for_List));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD class=grid1><SPAN id=Type");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 225px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBC.getListBox("Charge_Type","submit",Charge_Type_for_List,"01"));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD class=grid1></TD>");

      HB.append("<TD class=grid1><SPAN id=Month");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 225px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBC.getListBox("List_Month","submit",Month_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("</TR></TABLE>");

    return HB.toString();
  }
/*****************************************************************************************/
  public boolean createChargeBAN()
  {
    try{
      Conn=DBC.Connect(WRITE,P5);
      if (Conn !=null)
      {
        SQL = "{call eban..Create_Barclays_Charge_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,banStatus);
        cstmt.setString(2,Global_Customer_Id);
	cstmt.setString(3,Account_Id);
	cstmt.setDate(4,Required_BAN_Effective_Dateh);
	cstmt.setString(5,BAN_Summary);
	cstmt.setString(6,BAN_Reason);
	cstmt.setString(7,banCreatedBy);
	cstmt.setString(8,Service_Reference);
	cstmt.setString(9,Charge_Category);
	cstmt.setFloat(10,Gross_Amount);
	cstmt.setString(11,Charge_Description);
	cstmt.setString(12,Currency_Desc);
	cstmt.setString(13,Charge_Type);
	cstmt.setString(14,CRDB);
	cstmt.setString(15,Charge_Frequency);
	cstmt.setDate(16,Charge_Valid_From_Date);
	cstmt.setDate(17,Charge_Valid_To_Date);
	cstmt.setString(18,Revenue_Reason_Code);
	cstmt.setString(19,"");
	cstmt.setString(20,Down_Time);
	cstmt.setDate(21,Down_Time_Date);
	cstmt.setString(22,Contract_Number);
	cstmt.setString(23,site);
	cstmt.setString(24,Percentage);
	cstmt.setInt(25,Charge_Id);
	cstmt.setString(26,Mode);

	try
	{
          cstmt.execute();
	  RS = cstmt.getResultSet();
	  if (RS.next())
	  {
	    banIdentifier=RS.getString(1);
	  }
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
	return false;
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      return false;
    }
    catch(java.lang.NullPointerException se){Message="<font color=red><b>"+se.getMessage();return false;}//message set in underlying code
    close(Conn);
    Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
    return true;

  }
/**********************************************************************************************/
  public boolean updateChargeBan()
  {
    int rowcount=0;

    try{
      Conn=DBC.Connect(WRITE,P5);
      if (Conn !=null )
      {
        SQL = "{call eban..update_Charge_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	//if we are only doing a partial update (e.g. Authorise)
	//we need to make sure the values of composite fields
	//i.e. fields made up from id and description are set
	//to the id only
        cstmt = Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
	cstmt.setString(2,BAN_Summary);
	cstmt.setDate(3,Required_BAN_Effective_Dateh);
	cstmt.setString(4,BAN_Reason);
	cstmt.setString(5,userId);
	cstmt.setString(6,SU.before(Charge_Category));
	cstmt.setFloat(7,Gross_Amount);
	cstmt.setString(8,Charge_Description);
	cstmt.setString(9,Currency_Desc);
	cstmt.setString(12,Charge_Type);
	cstmt.setString(13,banStatus);
	cstmt.setString(14,rejectReason);
	cstmt.setString(15,CRDB);
	cstmt.setString(16,Charge_Frequency);
	cstmt.setDate(17,Charge_Valid_From_Date);
	cstmt.setDate(18,Charge_Valid_To_Date);
	cstmt.setString(21,Revenue_Reason_Code);

        cstmt.execute();
        rowcount=cstmt.getUpdateCount();
	if (rowcount != 1)
	{
	  Message="Unexpected! - "+Integer.toString(rowcount) + " Rows Updated";
	}
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      closeupdate(Conn);
      return false;
    }
    catch(java.lang.NullPointerException se){Message="<font color=red><b>"+se.getMessage();return false;}//message set in underlying code

    closeupdate(Conn);
    Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" "+action;

    if (action.endsWith("ed"))
    {
    }
    else if (action.endsWith("e"))
    {
      Message=Message+"d";
    }
    else
    {
      Message=Message+"ed";
    }
    return true;
  }
/*********************************************************************/
public boolean ImplementChargeBAN()
{

  SQL = "exec eban..Implement_Charge_BAN '"+ banIdentifier+ "'";

  if(DBC.NoResult(SQL,P5))
  {
    return true;
  }
  else
  {
    Message=DBC.getMessage();
    return false;
  }
}
/*******************************************************************************/
  public String getChargeList()
  {
	int counter=0;
	String RadioButton;
	int rowcount;
	BigInteger rows = new BigInteger("0");
	String gridClass;
        StringBuffer grid=new StringBuffer("");

	RadioButton="width=45 align=center><img src=\"../nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";
    try{
      Conn=DBC.Connect(PREPARE,P5);
      if (Conn != null)
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call OSS..listOSSCharges ";
        SQL += "(?,?,?,?,?,?,?)}";
	cstmt = Conn.prepareCall(SQL);
	cstmt.setString(1,action);
	cstmt.setString(2,OrderBy);
	cstmt.setString(3,Global_Customer_Id_for_List);
	cstmt.setString(4,Account_for_List);
	cstmt.setString(5,GSR_for_List);
	cstmt.setString(6,Charge_Type_for_List);
	cstmt.setString(7,Month_for_List);

	columns=8;

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
	      grid.append("<td class=");
	      grid.append(gridClass);
	      grid.append(RS.getString(counter));
	      grid.append("</td>");
	    }
	    //Add the extra generated column for the radio button
	    grid.append("<td width=45 class=");
	    grid.append(gridClass);
	    grid.append(RadioButton);
	    grid.append(RS.getString(counter));
	    grid.append("')\"></td></tr>");
	    //End the table
	  }
	  grid.append("</table>");
	  // Clean up after ourselves
	  close(Conn);
	}
	catch(java.sql.SQLException se)
        {
	  Message=se.getMessage();
	  close(Conn);
	  return Message;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      return Message;
    }
    catch(java.lang.NullPointerException se){Message="<font color=red><b>Null Pointer "+se.getMessage();return Message;}//message set in underlying code
    return grid.toString();
  }
/********************************************************************/
  //for debugging
  public static void main (String[] args)
  {
    /*String Currency;
    OSSChargeBANBean chBAN = new OSSChargeBANBean();
    chBAN.setCircuit_Reference("345345");
    chBAN.setCharge_Entity("C&W");
    chBAN.setCharge_Entity("Carrier");
    Currency=chBAN.getCurrency_Desc();
    //BAN.getBanList();
    */
 }
}