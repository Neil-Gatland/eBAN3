package DBUtilities;

import java.sql.*;
import java.util.*;
import java.math.BigInteger;
import JavaUtil.SiteSplitDescriptor;

public class FixedChargeBANBean extends BANBean
{
    private String Charge_Description="",Charge_Category="",Charge_CategoryDisplay="";
    private java.sql.Date Charge_Valid_From_Date=null,Charge_Valid_To_Date, storedToDate;
    private String Charge_Frequency="Monthly",VAT_Indicator="Y",Revenue_Reason_Code="";
    private String Fixed_Charge_Type,Standard_Bill_Indicator="N",Charge_Entity="C&W Charge";
    private String Currency_Display="<font color=black> (UK Pounds Sterling)";
    private double Gross_Amount,VAT_Amount=0,Total_Amount=0;
    protected Vector mandatory=new Vector();
    private int Charge_Id, splits;
    private Hashtable ScreenData=new Hashtable();
    private Vector siteSplits=new Vector();
    private String Charge_Currency;
    private int BEDays, BEMonths, BEYears, FCDays, FCMonths, FCYears, TCDays,
      TCMonths, TCYears;
    private String siteReference, siteReference2, siteReference3;
    private int users;
    private boolean cdcsRequired;
    private double split, split2, split3;
    private String chargeDescriptionCode;
    private String discountsApplicable;
    private String revenueType;
    private String revenueDescription;
    private String revenueNetOrFull;
    private String revenueRootCause;
    private int chargeId;
    private boolean chargeBilled;
    private boolean chargeTrialled;
    private boolean toDateNullable;
    private String trialDelete;
    private String Charge_Description_for_List;
    private boolean archived;
    private String invoiceNo;


   public FixedChargeBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("Required_BAN_Effective_Dateh");
    mandatory.addElement("Gross_Amount");
    mandatory.addElement("Charge_Currency");
    mandatory.addElement("Charge_Description");
    mandatory.addElement("From_Charge_Valid_Dateh");
    mandatory.addElement("Users");
    mandatory.addElement("Credit_Debit");
    mandatory.addElement("Fixed_Charge_Type");

    Charge_Currency = "GBP";
/*    BEDays=0;
    BEMonths=0;
    BEYears=0;
    FCDays=0;
    FCMonths=0;
    FCYears=0;
*/    chargeId=0;
    users=1;
    Charge_Valid_From_Date=null;
    Charge_Valid_To_Date=null;
    chargeBilled=false;
    chargeTrialled=false;
    trialDelete="N";
    storedToDate=null;
    Charge_Description_for_List="All";
    archived=false;
    Fixed_Charge_Type="";
    CRDB="";
    setDates();
    FCDays=1;
    //resetDates();
    invoiceNo = null;
  }
  public void Reset()
  {
    super.Reset();
    Charge_Description="";
    Charge_Category="";
    Charge_Valid_From_Date=null;
    Charge_Valid_To_Date=null;
    Currency_Display="<font color=black> (UK Pounds Sterling)";
    Gross_Amount=0;
    ScreenData.clear();
    setErrored("clear");
    setRequired_BAN_Effective_Date("Today");
    Charge_Currency = "GBP";
/*    BEDays=0;
    BEMonths=0;
    BEYears=0;
    FCDays=0;
    FCMonths=0;
    FCYears=0;
*/    users=1;
    chargeId=0;
    chargeBilled=false;
    chargeTrialled=false;
    trialDelete="N";
    Charge_Description_for_List="All";
    archived=false;
    Fixed_Charge_Type="";
    CRDB="";
    setDates();
    FCDays=1;
    //resetDates();
    invoiceNo = null;
  }
  public String getCharge_Description_for_List()
  {
    return Charge_Description_for_List;
  }
  public void setCharge_Description_for_List(String value)
  {
    Charge_Description_for_List = SU.hasNoValue(value,"All");
    if ((Charge_Description_for_List.equalsIgnoreCase("All")) &&
      (!Charge_Description_for_List.equals("All")))
      Charge_Description_for_List = "All";
  }
  public void setGSR_for_List(String value)
  {
    GSR_for_List=SU.hasNoValue(value,"All");
  }
  public String getCharge_Description()
  {
    return Charge_Description;
  }
  public String getUsers()
  {
    if ((String)ScreenData.get("Users") != null)
    {
      return (String)ScreenData.get("Users");
    }
    else
    {
      return String.valueOf(users);
    }
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
    if (Charge_Valid_To_Date==null)
      return null;
    else
      return SU.reformatDate(Charge_Valid_To_Date.toString());
  }
  public String getCRDB()
  {
    return CRDB;
  }
  public String getFixedChargeType()
  {
    return Fixed_Charge_Type;
  }
  public String getCharge_Currency()
  {
    return Charge_Currency;
  }
  public String getChargeFieldVisibility(String value)
  {
/*     if ((value.compareTo("Charge_Entity")==0) || (value.compareTo("Charge_Frequency")==0))
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
    }*/
    if ((value.equals("ToDate")) || (value.equals("Unit_Quantity")) ||
      (value.equals("Charge_Frequency")))
    {
      if (Charge_Category.equals("01"))
	return "visible";
      else
	return "hidden";
    }
    if ((value.equals("Revenue_Description")) || (value.equals("Revenue_Net_Or_Full")) ||
      (value.equals("Revenue_Root_Cause")))
    {
      if (Charge_Category.equals("03"))
	return "visible";
      else
	return "hidden";
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
    if (archived)
        return "READONLY";
    if ((Mode.equals("Delete")) && (!FieldName.equals("BAN_Summary")))
        return "READONLY";
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
/*    if (FieldName.equals("Unit_Quantity"))
    {
      if (chargeBilled)
        return "READONLY";
    }
    if (FieldName.compareTo("Gross_Amount") ==0)
    {
      if (((action.compareTo("Create") == 0) ||
       (action.compareTo("Amend") == 0)) &&
       ((OSS_Charge_Type.compareTo("07")) != 0) && (OSS_Charge_Type.compareTo("") !=0))
        return InputMode;
      else
        return "READONLY";
    }*/
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
    else if (name.compareTo("BAN_Summary")==0)
    {
      setBAN_Summary(value);
    }
    else if (name.startsWith("Required_BAN_Effective_Dateh"))
    {
      setRequired_BAN_Effective_Date(value);
      ScreenData.put("Required_BAN_Effective_Dateh",value);
    }
    else if (name.startsWith("Required_BAN_Effective_DateDay"))
    {
      setBANEffectiveDateDays(value);
    }
    else if (name.startsWith("Required_BAN_Effective_DateMonth"))
    {
      setBANEffectiveDateMonths(value);
    }
    else if (name.startsWith("Required_BAN_Effective_DateYear"))
    {
      setBANEffectiveDateYears(value);
    }
    else if (name.compareTo("From_Charge_Valid_Dateh")==0)
    {
      setFrom_Charge_Valid_Date(value);
    }
    else if (name.compareTo("To_Charge_Valid_Dateh")==0)
    {
      setTo_Charge_Valid_Date(value);
    }
    else if (name.startsWith("From_Charge_Valid_DateDay"))
    {
      setFromChargeValidDateDays(value);
    }
    else if (name.startsWith("From_Charge_Valid_DateMonth"))
    {
      setFromChargeValidDateMonths(value);
    }
    else if (name.startsWith("From_Charge_Valid_DateYear"))
    {
      setFromChargeValidDateYears(value);
    }
    else if (name.startsWith("To_Charge_Valid_DateDay"))
    {
      setToChargeValidDateDays(value);
    }
    else if (name.startsWith("To_Charge_Valid_DateMonth"))
    {
      setToChargeValidDateMonths(value);
    }
    else if (name.startsWith("To_Charge_Valid_DateYear"))
    {
      setToChargeValidDateYears(value);
    }
    else if (name.compareTo("Gross_Amount")==0)
    {
      setGross_Amount(value);
    }
    else if (name.compareTo("Users")==0)
    {
      setUsers(value);
    }
    else if (name.compareTo("Charge_Currency")==0)
    {
      setCharge_Currency(value);
    }
    else if (name.compareTo("RejectReason")==0)
    {
      setRejectReason(value);
    }
    else if (name.compareTo("Credit_Debit")==0)
    {
      setCRDB(value);
    }
    else if (name.compareTo("Fixed_Charge_Type")==0)
    {
      setFixedChargeType(value);
    }
  }
  /*********************************************************************/
  public void setCharge_Description(String value)
  {
    String noReturn = value.replace('\n', ' ').replace('\r', ' ');
    Charge_Description = noReturn;
    ScreenData.put("Charge_Description",noReturn);
  }
  public void setChargeDescriptionCode(String value)
  {
    chargeDescriptionCode = value;
    ScreenData.put("Charge_Description_Code",value);
  }
  public void setUsers(String value)
  {
    ScreenData.put("Users",value);
    if ((value.compareTo("")!=0) && (value !=null))
    {
      try
      {
        users = Integer.parseInt(value);
      }
      catch (java.lang.NumberFormatException NE)
      {
      }
    }
    else
    {
      users=1;
    }
  }
  public void setGross_Amount(String value)
  {
    if (!Charge_Category.equals("01"))
    {
      //get rid of negative if present
      if (value.indexOf('-') != -1)
        value = value.replace('-',' ').trim();
    }
    ScreenData.put("Gross_Amount",value);
    if ((value.compareTo("")!=0) && (value !=null))
    {
      try
      {
        Gross_Amount = Double.parseDouble(value);
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
    ScreenData.put("Credit_Debit",value);
  }
  public void setFixedChargeType(String value)
  {
    Fixed_Charge_Type=value;
    ScreenData.put("Fixed_Charge_Type",value);
  }
  public void setFrom_Charge_Valid_Date(String value)
  {
    Charge_Valid_From_Date = SU.toJDBCDate(value);
    ScreenData.put("From_Charge_Valid_Dateh",value);
  }
  public void setTo_Charge_Valid_Date(String value)
  {
    Charge_Valid_To_Date = SU.toJDBCDate(value);
    ScreenData.put("To_Charge_Valid_Dateh",value);
  }
  public void setCharge_Currency(String value)
  {
    Charge_Currency=value;
    ScreenData.put("Charge_Currency",value);
  }

  public void setMode(String value)
  {
    Mode=value;
    setdefaultFieldModes();
    if (Mode.equals("Cease"))
    {
      if (!mandatory.contains("To_Charge_Valid_Date"))
      {
        mandatory.addElement("To_Charge_Valid_Date");
      }
    }
    else
    {
      if (mandatory.contains("To_Charge_Valid_Date"))
      {
        mandatory.removeElement("To_Charge_Valid_Date");
      }
    }
  }

  public void setCharge_Id(String value)
  {
    Charge_Id=SU.toInt(value);
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
  if (ButtonPressed.startsWith("Submit"))
  {
    while (FormFields.hasMoreElements())
    {
      FieldName=(String)FormFields.nextElement();
      FormField=SU.isNull((String)ScreenData.get(FieldName),"");
      if(FormField.equals(""))
      {
        String fieldMode = getMode(FieldName);
        if ((!fieldMode.equalsIgnoreCase("DISABLED")) &&
          (!fieldMode.equalsIgnoreCase("DISABLED")))
          setErrored(FieldName);
      }
    }
    if ((!errored.isEmpty()) && (errored.size() > 0))
    {
        Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
        //Message=(String)errored.firstElement();
        return false;
    }
  }
  //These checks apply to all saves
  //Amounts must be numeric
  if ((Charge_Valid_To_Date != null) && (Charge_Valid_From_Date != null))
  {
    if (Charge_Valid_To_Date.before(Charge_Valid_From_Date))
    {
      setErrored("From_Charge_Valid_Dateh");
      setErrored("To_Charge_Valid_Dateh");
      Message = "<font color=red><b>Fixed Charge Valid To Date cannot be less than Fixed Charge Valid From Date";
      return false;
    }
  }
  try
  {
    Gross_Amount = Double.parseDouble((String)ScreenData.get("Gross_Amount"));
  }
  catch (java.lang.NumberFormatException NE)
  {
    setErrored("Gross_Amount");
    Message = "<font color=red><b>Unit Charge Amount must be numeric";
    return false;
  }
  try
  {
    users = Integer.parseInt((String)ScreenData.get("Users"));
  }
  catch (java.lang.NumberFormatException NE)
  {
    setErrored("Users");
    Message = "<font color=red><b>Users must be numeric";
    return false;
  }
  if (users < 1)
  {
    setErrored("Users");
    Message = "<font color=red><b>Users must be a positive number greater than zero";
    return false;
  }

  return true;
}
/****************************************************************************************/
  public String getChargeHeader()
 {
    StringBuffer HB=new StringBuffer("<br><table border=0><tr class=gridHeader>");

    //Column Headings

      HB.append("<tr><td class=gridHeader NOWRAP valign=top width=450><button class=grid_menu>Description</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=60><button class=grid_menu>Amount</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=198><button class=grid_menu>From Date</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=200><button class=grid_menu>To Date</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=45><button class=grid_menu>Select</button></td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr>");

      //Filters

      HB.append("<TR><TD class=grid1>");//<SPAN id=Desc");
      //HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 450px; POSITION: absolute; TOP: 199px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append("<iframe width=445px scrolling=no hspace=0 marginheight=0 marginwidth=0 src=\"" +
        "filterInput.jsp?name=Description&value=" + Charge_Description_for_List +
        "\" style=\"height:18px;\"></iframe>");
      HB.append("</tr></td></table></TD>");
      //HB.append("</tr></td></table></SPAN></TD>");
      HB.append("<TD class=grid1><SPAN id=Amount");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 60px; POSITION: absolute; TOP: 177px\"></TD>");
      HB.append("<TD class=grid1>");//<SPAN id=From");
      //HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 200px; POSITION: absolute; TOP: 199px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("List_Month2","submit",Month_for_List,"",1,"style=\"width:196px\"",true));
      HB.append("</tr></td></table></TD>");
      //HB.append("</tr></td></table></SPAN></TD>");
      HB.append("<TD class=grid1><SPAN id=To");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 200px; POSITION: absolute; TOP: 177px\"></TD>");
      HB.append("<TD class=grid1><SPAN id=Select");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 45px; POSITION: absolute; TOP: 177px\"></TD>");

      HB.append("</TR></TABLE>");

    return HB.toString();
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
	String chargeDescription = Charge_Description_for_List.equalsIgnoreCase("All")
          ?"All":("%"+Charge_Description_for_List+"%");

	RadioButton="width=45 align=center><img src=\"../nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";
    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call dialup_products..List_Fixed_Charges ";
        SQL += "(?,?,?,?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,action);
	cstmt.setString(2,OrderBy);
	cstmt.setString(3,Global_Customer_Id_for_List);
	cstmt.setString(4,chargeDescription);
	cstmt.setString(5,Month_for_List);

	columns=4;

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
              StringBuffer value = new StringBuffer(RS.getString(counter));
              if (value.toString().endsWith("NOWRAP>"))
                value.append("&nbsp;");
	      grid.append(value.toString());
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
/*****************************************************************************************/
  public boolean createChargeBAN()
  {
    boolean createChargeBAN = false;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Create_Charge_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
/*        cstmt.setString(1,banStatus);
	cstmt.setString(2,BAN_Summary);
	cstmt.setString(3,banCreatedBy);
	cstmt.setDate(4,Required_BAN_Effective_Dateh);
        cstmt.setString(5,Circuit_Reference);
	cstmt.setDouble(6,Gross_Amount);
	cstmt.setString(7,chargeDescriptionCode);
	cstmt.setDate(8,Charge_Valid_From_Date);
	cstmt.setDate(9,Charge_Valid_To_Date);
	cstmt.setString(10,Charge_Description);
        cstmt.setInt(11,splits);
        cstmt.setString(12,siteReference);
        cstmt.setString(13,siteReference2);
        cstmt.setString(14,siteReference3);
        cstmt.setDouble(15,split);
        cstmt.setDouble(16,split2);
        cstmt.setDouble(17,split3);
	cstmt.setString(18,Charge_Frequency);
	cstmt.setString(19,Charge_Currency);
	cstmt.setString(20,revenueType);
	cstmt.setString(21,"Z");
	cstmt.setString(22,"Z");
	cstmt.setString(23,"Z");
        cstmt.setInt(24,unitQuantity);
        cstmt.setString(25,Global_Customer_Id);
	cstmt.setString(26,Mode);
	cstmt.setString(27,Charge_Entity);
*/
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
/*****************************************************************************************/
  public boolean authoriseChargeBAN()
  {
    boolean authoriseChargeBAN = false;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban.dbo.Authorise_Fixed_Charge_BAN " +
          "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banStatus);
        cstmt.setString(2,BAN_Summary);
        cstmt.setString(3,banCreatedBy);
        cstmt.setDate(4,Required_BAN_Effective_Dateh);
        cstmt.setString(5,Global_Customer_Id);
        cstmt.setDouble(6,Gross_Amount);
        cstmt.setDate(7,Charge_Valid_From_Date);
        cstmt.setDate(8,Charge_Valid_To_Date);
        cstmt.setString(9,Charge_Currency);
        cstmt.setString(10,Charge_Description);
        cstmt.setString(11,Fixed_Charge_Type);
        cstmt.setString(12,CRDB);
        cstmt.setInt(13,users);
        cstmt.setString(14,Mode);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          String ret = RS.getString(1);
          if (ret.startsWith("-"))
          {
            authoriseChargeBAN = false;
            Message = "<font color=red><b>Unable to authorise BAN: " +
              (ret.equals("-99")?"a bill job is currently running for this customer."
              :("return code " + ret)) + "</b></font>";

          }
          else
          {
            authoriseChargeBAN = true;
            banIdentifier = ret;
            Message = "<font color=blue><b>BAN Id:-" + banIdentifier +
              " authorised and " +
                (Charge_Category.equals("03")?"credit":"charge") + " created";
          }
        }
        ScreenData.clear();
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
      return authoriseChargeBAN;
    }

  }
/*****************************************************************************************/
  public boolean updateChargeBAN()
  {
    boolean updateChargeBAN = false;
    try{
      if (DBA.Connect(WRITE,P5))
      {
            //trialDelete = DBA.trialDelete(Charge_Id, Charge_Category)?"Y":"N";
          SQL = "{call eban.dbo." + (Mode.equals("Delete")?"Delete":"Update") +
            "_Fixed_Charge_BAN " +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banStatus);
        cstmt.setString(2,BAN_Summary);
        cstmt.setString(3,banCreatedBy);
        cstmt.setDate(4,Required_BAN_Effective_Dateh);
        cstmt.setString(5,Global_Customer_Id);
        cstmt.setDouble(6,Gross_Amount);
        cstmt.setDate(7,Charge_Valid_From_Date);
        cstmt.setDate(8,Charge_Valid_To_Date);
        cstmt.setString(9,Charge_Currency);
        cstmt.setString(10,Charge_Description);
        cstmt.setString(11,Fixed_Charge_Type);
        cstmt.setString(12,CRDB);
        cstmt.setInt(13,users);
        cstmt.setString(14,Mode);
        cstmt.setInt(15,Charge_Id);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          String ret = RS.getString(1);
          if (banIdentifier.startsWith("-"))
          {
            updateChargeBAN = false;
            Message = "<font color=red><b>Unable to authorise BAN: " +
              (ret.equals("-99")?"a bill job is currently running for this customer."
              :("return code " + ret)) + "</b></font>";

          }
          else
          {
            updateChargeBAN = true;
            banIdentifier = ret;
            Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" authorised " +
              "and fixed charge" + (Mode.equals("Delete")?" deleted":" updated") +
              (invoiceNo==null?"":". The relevant trial bill must now be rerun.");
          }
        }
        ScreenData.clear();
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
      return updateChargeBAN;
    }

  }
/*********************************************************************/
public boolean ImplementChargeBAN()
{

  SQL = "exec eban..Implement_OSS_Charge_BAN '"+ banIdentifier+ "','" +
    userId + "'";

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
/*	    banStatus=RS.getString(1);
	    BAN_Summary=RS.getString(2);
	    banCreatedBy=RS.getString(3);
	    Required_BAN_Effective_Dateh=RS.getDate(4);
	    Circuit_Reference=RS.getString(5);
            Gross_Amount=RS.getDouble(6);
            chargeDescriptionCode=RS.getString(7);
	    Charge_Valid_From_Date=RS.getDate(8);
	    Charge_Valid_To_Date=RS.getDate(9);
            Charge_Description=RS.getString(10);
            splits=RS.getInt(11);
            Charge_Frequency=RS.getString(12);
            Charge_Currency=RS.getString(13);
            revenueType=RS.getString(14);
            unitQuantity=RS.getInt(15);
	    Global_Customer_Id=RS.getString(16);
	    Mode=RS.getString(17);
	    Charge_Entity=RS.getString(18);
            chargeId=RS.getInt(19);
*/	  }
//          getChargeBan = getChargeSiteBan();
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
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call dialup_products..Get_Fixed_Charge (?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setInt(1,Charge_Id);
        cstmt.execute();
	RS = cstmt.getResultSet();
        if (RS.next())
        {
          Gross_Amount=RS.getDouble(1);
          Charge_Currency=RS.getString(2);
          users=RS.getInt(3);
          Charge_Description=RS.getString(4);
          Charge_Valid_From_Date=RS.getDate(5);
          Fixed_Charge_Type=RS.getString(6);
          invoiceNo=RS.getString(7);
          archived=RS.getInt(8)==1?true:false;
          Charge_Valid_To_Date=RS.getDate(9);
        }
        if (Gross_Amount < 0)
        {
          CRDB = "Credit";
          Gross_Amount = Gross_Amount * -1;
        }
        else
        {
          CRDB = "Debit";
        }
        setDates();
        getCharge = true;
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
/************************************************************************************************/
  public int getFromChargeValidDateDays()
  {
    return FCDays;
  }
/************************************************************************************************/
  public void setFromChargeValidDateDays(String value)
  {
    FCDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getFromChargeValidDateMonths()
  {
    return FCMonths;
  }
/************************************************************************************************/
  public void setFromChargeValidDateMonths(String value)
  {
    FCMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getFromChargeValidDateYears()
  {
    return FCYears;
  }
/************************************************************************************************/
  public void setFromChargeValidDateYears(String value)
  {
    FCYears = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getToChargeValidDateDays()
  {
    return TCDays;
  }
/************************************************************************************************/
  public void setToChargeValidDateDays(String value)
  {
    TCDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getToChargeValidDateMonths()
  {
    return TCMonths;
  }
/************************************************************************************************/
  public void setToChargeValidDateMonths(String value)
  {
    TCMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getToChargeValidDateYears()
  {
    return TCYears;
  }
/************************************************************************************************/
  public void setToChargeValidDateYears(String value)
  {
    TCYears = value.equals("")?0:Integer.parseInt(value);
  }
/****************************************************************************************/
  public void setChargeBilled()
  {
    chargeBilled = DBA.chargeBilled(Charge_Id, Charge_Category, false);
    if (!chargeBilled)
    {
      chargeTrialled = DBA.chargeBilled(Charge_Id, Charge_Category, true);
      if (chargeTrialled)
      {
        Message = "<font color=red>Warning - this charge exists on a trial bill. " +
          "You must rerun the trial after updating the charge.";
      }
    }
  }
/****************************************************************************************/
  public String getSelectMode()
  {
    if (archived)
      return "DISABLED";
    else
      return super.getSelectMode();
  }
/****************************************************************************************/
  public String getInputMode()
  {
    if (archived)
      return "READONLY";
    else
      return super.getInputMode();
  }
/****************************************************************************************/
  public boolean isChargeArchived()
  {
    return archived;
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
    cal.setTime(Charge_Valid_From_Date==null?(new java.util.Date())
      :Charge_Valid_From_Date);
    FCDays=cal.get(cal.DATE);
    FCMonths=cal.get(cal.MONTH)+1;
    FCYears=cal.get(cal.YEAR);
    if (Charge_Valid_To_Date==null)
    {
      TCDays=0;
      TCMonths=0;
      TCYears=0;
    }
    else
    {
      cal.setTime(Charge_Valid_To_Date);
      TCDays=cal.get(cal.DATE);
      TCMonths=cal.get(cal.MONTH)+1;
      TCYears=cal.get(cal.YEAR);
    }
  }
/****************************************************************************************/
  private void resetDates()
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.set(FCDays, FCMonths-1, FCYears);
    Charge_Valid_From_Date = new java.sql.Date(cal.getTime().getTime());
  }
/********************************************************************/
  public boolean disableDate(String FieldName, boolean disableAnyway)
  {
    if (disableAnyway)
      return true;
    else
    {
      if (((FieldName.equals("From_Charge_Valid_Date")) ||
        (FieldName.equals("To_Charge_Valid_Date"))) && (chargeBilled))
        return true;
      else
        return false;
    }
  }
  //for debugging
  public static void main (String[] args)
  {
    String Currency;
    OSSChargeBANBean chBAN = new OSSChargeBANBean();
    chBAN.setCircuit_Reference("345345");
    chBAN.setCharge_Entity("C&W");
    chBAN.setCharge_Entity("Carrier");
    Currency=chBAN.getCurrency_Desc();
    //BAN.getBanList();
 }
}
