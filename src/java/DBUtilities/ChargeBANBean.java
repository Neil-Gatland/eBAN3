package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.math.BigInteger;
import java.util.Hashtable;

public class ChargeBANBean extends BANBean
{
    private String Charge_Description="",Charge_Category="",Charge_CategoryDisplay="";
    private java.sql.Date Charge_Valid_From_Date=null,Charge_Valid_To_Date;
    private String Charge_Frequency="Annual",VAT_Indicator="Y",Revenue_Reason_Code="",Revenue_Type_Code="";
    private String Charge_Type="",Contract_Number="",Split_Sites="",Tax_Type="None Applicable";
    private String Currency_Display="<font color=black> (UK Pounds Sterling)";
    private float Gross_Amount;
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
    mandatory.addElement("Site");
    mandatory.addElement("Split_Sites");
  }
  public void Reset()
  {
    super.Reset();
    Charge_Description="";
    Charge_Category="";
    Charge_Valid_From_Date=null;
    Charge_Valid_To_Date=null;
    Revenue_Reason_Code="";
    Revenue_Type_Code="";
    Charge_Type="";
    Currency_Display="<font color=black> (UK Pounds Sterling)";
    Gross_Amount=0;
    ScreenData.clear();
    setErrored("clear");
    setRequired_BAN_Effective_Date("Today");
    Contract_Number="";
    Tax_Type="None Applicable";
    Split_Sites="0";
  }
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
  public String getContract_Number()
  {
    return Contract_Number;
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
  public String getVAT_Indicator()
  {
    return VAT_Indicator;
  }
  public String getRevenue_Reason_Code()
  {
    return SU.isNull(Revenue_Reason_Code,"");
  }
  public String getRevenue_Type_Code()
  {
    return SU.isNull(Revenue_Type_Code,"");
  }
  public String getCurrency_Display()
  {
    return Currency_Display;
  }
  public String getSplit_Sites()
  {
    return Split_Sites;
  }
  public String getTax_Type()
  {
    return Tax_Type;
  }


  public String getChargeFieldVisibility(String value)
  {
    if ((value.compareTo("Charge_Frequency")== 0) ||
        (value.compareTo("ToDate") == 0))
    {
      if (Charge_Category.compareTo("01")==0)
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
  {//Determines whether fields are updateable
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
    if (FieldName.compareTo("VAT_Amount") ==0)
    {
      if (((action.compareTo("Create") == 0) ||
       (action.compareTo("Amend") == 0)) &&
       (Charge_Type.compareTo("07") != 0))
	  return "READONLY";
      else
	  return InputMode;
    }
    if (FieldName.compareTo("Gross_Amount") ==0)
    {
      if (((action.compareTo("Create") == 0) ||
       (action.compareTo("Amend") == 0)) &&
       ((Charge_Type.compareTo("07")) != 0) && (Charge_Type.compareTo("") !=0))
	  return InputMode;
      else
	  return "READONLY";
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
      ScreenData.put("From_Charge_Valid_Dateh",value);
    }
    else if (name.compareTo("To_Charge_Valid_Dateh")==0)
    {
      setTo_Charge_Valid_Date(value);
      ScreenData.put("To_Charge_Valid_Dateh",value);
    }
    else if (name.compareTo("Charge_Frequency")==0)
    {
      setCharge_Frequency(value);
    }
    else if (name.compareTo("GCB_Charge_Type")==0)
    {
      setCharge_Type(value);
      ScreenData.put("Charge_Type",value);
    }
    else if (name.compareTo("RejectReason")==0)
    {
      setRejectReason(value);
      ScreenData.put("RejectReason",value);
    }
    else if (name.compareTo("GCB_Revenue_Reason")==0)
    {
      setRevenue_Reason_Code(value);
      ScreenData.put("Revenue_Reason",value);
    }
    else if (name.compareTo("GCB_Revenue_Type")==0)
    {
      setRevenue_Type_Code(value);
      ScreenData.put("Revenue_Type",value);
    }
    else if (name.compareTo("Site")==0)
    {
      setSite(value);
      ScreenData.put("Site",value);
    }
    else if (name.compareTo("Contract_Number")==0)
    {
      Contract_Number=value;
    }
    else if (name.compareTo("Split_Sites")==0)
    {
      Split_Sites=value;
      ScreenData.put("Split_Sites",value);
    }
    else if (name.compareTo("Tax_Type")==0)
    {
      Tax_Type=value;
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
  }
  public void setSplit_Sites(String value)
  {
    Split_Sites=value;
  }
  public void setTax_Type(String value)
  {
    Tax_Type=value;
  }
  /**********************************************************************************/
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
  public void setRevenue_Type_Code(String value)
  {
    Revenue_Type_Code=value;
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
	  //if ((Charge_Frequency.compareTo("") == 0) &&
	  //    (Charge_Category.compareTo("01") == 0))
	  //{
	  //  setErrored("Charge_Frequency");
	  //}
	  if ((CRDB.compareTo("") == 0) &&
	      (Charge_Category.compareTo("05") == 0))
	  {
	    setErrored("CRDB");
	  }
	  if ((!errored.isEmpty()) && (errored.size() > 0))
	  {
	      Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
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
    boolean getChargeBan = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..Get_Charge_BAN ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
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
	    setAccount_Id(RS.getString(4));
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
	    setContract_Number(RS.getString(17));
	    Charge_Frequency=RS.getString(18);
	    setCRDB(RS.getString(19));
	    setCharge_Category(RS.getString(20));
	    rejectReason=RS.getString(21);
	    Revenue_Reason_Code=RS.getString(22);
	    Revenue_Type_Code=RS.getString(23);
	    Charge_Id=RS.getInt(24);
	    Mode=RS.getString(25);
	    site=RS.getString(26);
	    Split_Sites=String.valueOf(RS.getInt(27));
	    Tax_Type=RS.getString(28);
            getChargeBan = true;
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
      return getChargeBan;
    }
}
/****************************************************************************************/
public boolean getCharge()
{
    boolean getCharge = false;
    try
    {
      if (DBA.Connect(PREPARE,P4))
      {
	if (Charge_Category.compareTo("01") == 0)
	{
          SQL = "{call gcd..Get_Charge ";
	}
	else
	{
	  SQL = "{call gcd..Get_Single_Charge ";
	}
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
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
	    Charge_Type=RS.getString(4);
	    Charge_Description=RS.getString(5);
	    setCurrency_Desc(RS.getString(6));
	    Gross_Amount=RS.getFloat(7);
	    Charge_Valid_From_Date=RS.getDate(8);
	    Charge_Valid_To_Date=RS.getDate(9);
	    Contract_Number=RS.getString(10);
	    Charge_Frequency=RS.getString(11);
	    site=RS.getString(12);
	    setCRDB(RS.getString(13));
	    Revenue_Reason_Code=RS.getString(14);
    	    Revenue_Type_Code=RS.getString(15);
	    Division_Id=RS.getString(16);
            getCharge = true;
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
      return getCharge;
    }
}
/****************************************************************************************/
  public String getChargeHeader()
 {
    StringBuffer HB=new StringBuffer("<br><table border=0><tr class=gridHeader>");

    //Column Headings

      HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu onclick=\"Toggle_Menu('Customer')\">Customer</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu onclick=\"Toggle_Menu('Account_List')\">Account</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=110><button class=grid_menu onclick=\"Toggle_Menu('GSR')\">Service</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=90><button class=grid_menu onclick=\"Toggle_Menu('Type')\">Type</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu>Description</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu onclick=\"Toggle_Menu('Month')\">From Date</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu>To Date</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=60><button class=grid_menu>Amount</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=45><button class=grid_menu>Select</button></td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr>");

      //Filters

      HB.append("<TR><TD class=grid1><SPAN id=Customer ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 260px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("GCB_Customer","submit",Global_Customer_Id_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD class=grid1><SPAN id=\"Account_List\" ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 260px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("Division","submit",Division_for_List,Global_Customer_Id_for_List));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=1 class=grid1><SPAN id=GSR ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 260px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("Service","submit",GSR_for_List,Global_Customer_Id_for_List+"|"+Account_for_List));
      //HB.append(DBA.getListBox("Global_Service_Reference","submit",GSR_for_List,Global_Customer_Id_for_List));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD class=grid1><SPAN id=Type");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 260px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("Charge_Type","submit",Charge_Type_for_List,"01"));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD class=grid1></TD>");

      HB.append("<TD class=grid1><SPAN id=Month");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 260px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("List_Month","submit",Month_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("</TR></TABLE>");

    return HB.toString();
  }
/*****************************************************************************************/
  public boolean createChargeBAN()
  {
    boolean createChargeBAN = false;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Create_Charge_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banStatus);
        cstmt.setString(2,Global_Customer_Id);
	cstmt.setString(3,Division_Id);
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
	cstmt.setString(19,Revenue_Type_Code);
	cstmt.setInt(20,Charge_Id);
	cstmt.setString(21,Mode);
	cstmt.setString(22,Contract_Number);
	cstmt.setString(23,site);
	cstmt.setInt(24,SU.toInt(Split_Sites));
	cstmt.setString(25,Tax_Type);
	try
	{
          cstmt.execute();
	  RS = cstmt.getResultSet();
	  if (RS.next())
	  {
	    banIdentifier=RS.getString(1);
            createChargeBAN = true;
            Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
	  }
	  ScreenData.clear();
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
      return createChargeBAN;
    }

  }
/**********************************************************************************************/
  public boolean updateChargeBan()
  {
    boolean updateChargeBan = false;
    int rowcount=0;

    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..update_Charge_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	//if we are only doing a partial update (e.g. Authorise)
	//we need to make sure the values of composite fields
	//i.e. fields made up from id and description are set
	//to the id only
        cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
	cstmt.setString(2,BAN_Summary);
	cstmt.setDate(3,Required_BAN_Effective_Dateh);
	cstmt.setString(4,BAN_Reason);
	cstmt.setString(5,userId);
	cstmt.setString(6,SU.before(Charge_Category));
	cstmt.setFloat(7,Gross_Amount);
	cstmt.setString(8,Charge_Description);
	cstmt.setString(9,Currency_Desc);
	cstmt.setString(10,Contract_Number);
	cstmt.setString(11,site);
	cstmt.setString(12,Charge_Type);
	cstmt.setString(13,banStatus);
	cstmt.setString(14,rejectReason);
	cstmt.setString(15,CRDB);
	cstmt.setString(16,Charge_Frequency);
	cstmt.setDate(17,Charge_Valid_From_Date);
	cstmt.setDate(18,Charge_Valid_To_Date);
	cstmt.setString(19,Revenue_Reason_Code);
	cstmt.setString(20,Revenue_Type_Code);
	cstmt.setInt(21,SU.toInt(Split_Sites));
	cstmt.setString(22,Tax_Type);

        cstmt.execute();
        rowcount=cstmt.getUpdateCount();
	if (rowcount != 1)
	{
	  Message="Unexpected! - "+Integer.toString(rowcount) + " Rows Updated";
	}
	else
	{
          updateChargeBan = true;
	  Reset();
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
      return updateChargeBan;
    }
  }
/*********************************************************************/
public boolean AuthoriseChargeBAN()
{
    boolean AuthoriseChargeBAN = false;
    try{
      if (DBA.Connect(WRITE,P4))
      {
        if (Mode.compareTo("Create") ==0)
        {
	  SQL = "{call gcd..Create_Charge ";
	  SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	  cstmt = DBA.Conn.prepareCall(SQL);
	  cstmt.setString(1,Service_Reference);
	  cstmt.setFloat(2,Gross_Amount);
	  cstmt.setString(3,Charge_Description);
	  cstmt.setString(4,Currency_Desc);
	  cstmt.setString(5,Charge_Type);
	  cstmt.setString(6,CRDB);
	  cstmt.setString(7,Charge_Frequency);
	  cstmt.setDate(8,Charge_Valid_From_Date);
	  cstmt.setDate(9,Charge_Valid_To_Date);
	  cstmt.setString(10,Revenue_Reason_Code);
	  cstmt.setString(11,Revenue_Type_Code);
	  cstmt.setString(12,Contract_Number);
	  cstmt.setString(13,site);
	  cstmt.setInt(14,SU.toInt(Split_Sites));
	  cstmt.setString(15,Tax_Type);
	  cstmt.setString(16,banIdentifier);
	}
	else if (Mode.compareTo("Amend") ==0)
	{
	  SQL = "{call gcd..Update_Charge ";
	  SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	  cstmt = DBA.Conn.prepareCall(SQL);
	  cstmt.setFloat(1,Gross_Amount);
	  cstmt.setString(2,Charge_Description);
	  cstmt.setString(3,Currency_Desc);
	  cstmt.setString(4,CRDB);
	  cstmt.setString(5,Charge_Frequency);
	  cstmt.setDate(6,Charge_Valid_From_Date);
	  cstmt.setDate(7,Charge_Valid_To_Date);
	  cstmt.setString(8,Revenue_Reason_Code);
	  cstmt.setString(9,Revenue_Type_Code);
	  cstmt.setString(10,Contract_Number);
	  cstmt.setString(11,site);
	  cstmt.setString(12,banIdentifier);
	  cstmt.setInt(13,Charge_Id);
	  cstmt.setInt(14,SU.toInt(Split_Sites));
	  cstmt.setString(15,Tax_Type);

	}
        try
	{
	  cstmt.execute();
          AuthoriseChargeBAN = true;
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
      return AuthoriseChargeBAN;
    }
}

/*********************************************************************/
public boolean ImplementChargeBAN()
{

  SQL = "exec eban..Implement_GCB_Charge_BAN '"+ banIdentifier+ "'";

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
/*******************************************************************************/
  public String getChargeList()
  {
	int counter=0;
	String RadioButton;
	int rowcount;
	BigInteger rows = new BigInteger("0");
	String gridClass;
        StringBuffer grid=new StringBuffer("");
        Message="";

	RadioButton="width=45 align=center><img src=\"/shared/nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";
    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call gcd..listCharges ";
        SQL += "(?,?,?,?,?,?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,action);
	cstmt.setString(2,OrderBy);
	cstmt.setString(3,Global_Customer_Id_for_List);
	cstmt.setString(4,Division_for_List);
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
/********************************************************************/
  //for debugging
  public static void main (String[] args)
  {
    String Currency;
    ChargeBANBean chBAN = new ChargeBANBean();
    DBAccess DBA = new DBAccess();
    chBAN.setBanIdentifier("BFB-2003-10-9-7");
    DBA.startDB("OSS");
    chBAN.getChargeBan();
    //BAN.getBanList();
  }
 }
