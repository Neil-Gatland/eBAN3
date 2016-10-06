package EntityBeans;

import java.sql.*;
import java.util.Date;
import JavaUtil.StringUtil;
import java.util.Enumeration;
import java.math.BigInteger;
import java.util.Vector;

public class BANBean extends EntityBean
{
    protected String SQL;

    protected String banIdentifier;
    protected String banType;
    protected String BANs_to_List,Global_Customer_Id_for_List="All",BAN_Type_for_List="All";
    protected String ChargeDateHeading,ChargeFieldVisibility,CRDB,Currency_Desc="",Account_Id="";
    protected String banStatus="Draft",site,banCreatedBy,siteDisplay,action="",Invoice_Region="",Global_Customer_Name;
    protected String Return_BAN_To_Name="",BAN_Summary="",BAN_Reason="",Mode="";
    protected String globalServiceReference="",Global_Customer_Id="",Message="",Carrier_Name="";
    protected String chargeTypeDisplay,chargeType,Last_Updated_By="",Service_Reference="";
    protected String SelectMode="",InputMode="",Calendar="";

    protected java.sql.Date Required_BAN_Effective_Dateh,proposedBanDate;
    protected String banAuthorisedBy,userId,bansToList,rejectReason="",OrderBy="Status";
    protected final int NOT_INT=-2147483648;
    protected static final String P3 = "P3";
    protected static final String P5 = "P5";

    protected String Status_for_List="All",Account_for_List="All",GSR_for_List="All",Month_for_List="All",Charge_Type_for_List="All";

    protected String showFilters="hidden";

  public BANBean()
  {
  }
  protected void Reset()
  {
    Currency_Desc="";
    Return_BAN_To_Name="";
    BAN_Summary="";
    BAN_Reason="";
    Message="";
    Last_Updated_By="";
    banIdentifier="";
  }
  public String getBanIdentifier()
  {
   return SU.isNull(banIdentifier,"");
  }
  public String getGlobalCustomerId()
  {
    return Global_Customer_Id;
  }
  public String getGlobalServiceReference()
  {
    return SU.isNull(globalServiceReference,"N/A");
  }
  public String getService_Reference()
  {
    return Service_Reference;
  }
  public String getGlobalCustomerName()
  {
    return SU.isNull(Global_Customer_Name,"");
  }
  public String getInvoice_Region()
  {
    return SU.isNull(Invoice_Region,"");
  }
  public String getBanStatus()
  {
    return banStatus;
  }
  public String getBanAuthorisedBy()
  {
    return banAuthorisedBy;
  }
  public String getReturn_BAN_To_Name()
  {
    return Return_BAN_To_Name;
  }
  public String getRequired_BAN_Effective_Date()
  {
    if (Required_BAN_Effective_Dateh != null)
    {
      return SU.reformatDate(Required_BAN_Effective_Dateh.toString());
    }
    else
    {
      return null;
    }
  }
  public String getBanCreatedBy()
  {
    return banCreatedBy;
  }
  public String getBAN_Reason()
  {
    return BAN_Reason;
  }
  public String getBAN_Summary()
  {
    return BAN_Summary;
  }
  public String getMessage()
  {
    String temp=SU.isNull(Message,"");
    Message="";
    return temp;
  }
  public int getRecordCount()
  {
    return 0;
  }
  public String getBansToList()
  {
    return bansToList;
  }
   public String getAction()
  {
    return action;
  }
  public String getProposedBanDate()
  {
    if (proposedBanDate != null)
    {
      return SU.reformatDate(proposedBanDate.toString());
    }
    else
    {
      return "";
    }
  }
  public String getchargeDateHeading()
  {
    return ChargeDateHeading;
  }
  public String getSite()
  {
    return SU.isNull(site,"");
  }
  public String getSiteDisplay()
  {
    return siteDisplay;
  }
  public String getBANs_to_List()
  {
    return SU.isNull(BANs_to_List,"");
  }
  public String getGSR_for_List()
  {
    return GSR_for_List;
  }
  public String getAccount_for_List()
  {
    return Account_for_List;
  }
  public String getGlobal_Customer_Id_for_List()
  {
    return Global_Customer_Id_for_List;
  }
  public String getStatus_for_List ()
  {
    return Status_for_List;
  }
  public String getBAN_Month_for_List ()
  {
    return Month_for_List;
  }
  public String getAccount_Id()
  {
    return Account_Id;
  }
  public String getMode()
  {
   return Mode;
  }
  public String getRejectReason()
  {
    return rejectReason;
  }
  public String getRejectVisibility()
  {
    if ((banStatus.compareTo("Rejected")!=0) && (banStatus.compareTo("Returned")!=0))
    {
      return "hidden";
    }
    else
    {
      return "visible";
    }
  }
  public String getSelectMode()
  {
    return SelectMode;
  }
  public String getInputMode()
  {
    return InputMode;
  }
  public String getCalendar()
  {
    return Calendar;
  }
  public String getCurrency_Desc()
  {
    return SU.isNull(Currency_Desc,"");
  }
  public String getCarrier_Name()
  {
    return SU.isNull(Carrier_Name,"");
  }
  /***********************************************************************/
  public String getHeader(String System)
  {
    StringBuffer HB=new StringBuffer("<br><table border=0><tr class=gridHeader>");
    String imagename1,imagename2;
    if (OrderBy.compareTo("StatusSortAsc")==0)
    {
      imagename1="sorted_by.gif";
      imagename2="sort_by.gif";
    }
    else
    {
      imagename2="sorted_by.gif";
      imagename1="sort_by.gif";
    }
    if (BANs_to_List.startsWith("Invoice"))
    {
      HB.append("<br><table border=0><tr class=gridHeader>");
      HB.append("<td class=gridHeader NOWRAP width=110>BAN Id</td>");
      HB.append("<td class=gridHeader NOWRAP width=120>Customer</td>");
      HB.append("<td class=gridHeader NOWRAP width=120>Invoice Region<br><image name=\"Region\" onclick=\"sendOrderBy('Region')\" align=right width=13 height=8 src=\"../nr/cw/newimages/").append(imagename1).append("\"></td>");
      HB.append("<td class=gridHeader NOWRAP width=120>Billing Customer Name<br><image onclick=\"sendOrderBy('Customer')\" name=\"Billing\" valign=bottom align=right width=13 height=8 src=\"../nr/cw/newimages/").append(imagename2).append("\"></td>");
      HB.append("<td class=gridHeader NOWRAP width=90>Currency</td><td class=gridHeader NOWRAP width=45>Select</td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr></table>");
      columns=8;
    }
    else
    {//Main List
      HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu onclick=\"Toggle_Menu('Customer')\">Customer/Carrier</button></td>");
      //onmouseover=\"Close_Menus();Open_Menu('Customer')\"\"
      HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu onclick=\"Toggle_Menu('Account_List')\">Account</button></td>");
      //onmouseover=\"Close_Menus();Open_Menu('Account')\"\"
      HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu onclick=\"Toggle_Menu('GSR')\">Service</button></td>");
      // onmouseover=\"Close_Menus();Open_Menu('GSR')\"\"
      HB.append("<td class=gridHeader NOWRAP valign=top width=75><button id=StatusSort url=\"StatusSort\" class=grid_menu onclick=\"Toggle_Menu('Status')\">Status");
      HB.append("</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=90><button class=grid_menu onclick=\"Toggle_Menu('Type')\">Type");
      HB.append("</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu onclick=\"Toggle_Menu('Month')\">BAN Date</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=60><button class=grid_menu>Amount</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu>BAN Id</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=45><button class=grid_menu>Select</button></td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr>");

      //Now add the filters
      HB.append("<TR><TD class=grid1><SPAN id=Customer ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 260\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBC.getListBox("BAN_Customers","submit",Global_Customer_Id_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD class=grid1><SPAN id=\"Account_List\" ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 260\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      if (System.compareTo("OSS")==0)
      {
        HB.append(DBC.getListBox("BAN_Accounts","submit",Account_for_List,Global_Customer_Id_for_List));
      }
      else
      {
	HB.append(DBC.getListBox("Invoice_Region","submit",Account_for_List,Global_Customer_Id_for_List));
      }
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=1 class=grid1><SPAN id=GSR ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 260\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      if (System.compareTo("OSS")==0)
      {
        HB.append(DBC.getListBox("BAN_Services","submit",GSR_for_List,Global_Customer_Id_for_List+"|"+Account_for_List));
      }
      else
      {
	HB.append(DBC.getListBox("Global_Service_Reference","submit",GSR_for_List,Global_Customer_Id_for_List));
      }
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=1 class=grid1><SPAN id=Status ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 260\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      if (action.compareTo("View") == 0)
        HB.append(DBC.getListBox("Ban_Status","submit",Status_for_List,""));
      else
	HB.append(DBC.getListBox("Ban_Status","DISABLED",Status_for_List,""));

      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=1 class=grid1><SPAN id=Type");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 260\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBC.getListBox("BAN_Type","submit",BAN_Type_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=4 class=grid1><SPAN id=Month");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 260\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBC.getListBox("List_Month","submit",Month_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("</TR></TABLE>");

    }
    return HB.toString();
  }
/**************************************************************/
  public String getBanType()
  {
    return SU.isNull(banType,"Unknown");
  }
  public void setChargeType(String newchargeType)
  {
    chargeTypeDisplay=(SU.after(newchargeType));
    chargeType = SU.before(newchargeType);
    if (chargeType.compareTo("01")==0)
    {
      ChargeDateHeading="Charge Valid From Date";
      ChargeFieldVisibility="visible";
      CRDB="Debit";
    }
    else if (chargeType.compareTo("02")==0)
    {
      ChargeDateHeading="Charge Due Date";
      ChargeFieldVisibility="hidden";
      CRDB="Debit";
    }
    else if ((chargeType.compareTo("03")==0) || (chargeType.compareTo("04")==0))
    {
      ChargeDateHeading="Credit Payable Date";
      ChargeFieldVisibility="hidden";
      CRDB="Credit";
    }
    else if (chargeType.compareTo("05")==0)
    {
      ChargeDateHeading="Adjustment Date";
      ChargeFieldVisibility="hidden";
    }
    else
    {
      ChargeDateHeading="Charge Date";
      ChargeFieldVisibility="hidden";
    }
  }
  public void setGSR_for_List(String value)
  {
    GSR_for_List=SU.isNull(value,"All");
  }
  public void setAccount_for_List(String value)
  {
    Account_for_List=SU.isNull(value,"All");
  }
  public void setGlobal_Customer_Id_for_List(String value)
  {
    Global_Customer_Id_for_List=SU.isNull(value,"All");
  }
  public void setBAN_Type_for_List(String value)
  {
    BAN_Type_for_List=SU.isNull(value,"All");
  }
  public void setStatus_for_List(String value)
  {
    Status_for_List=SU.isNull(value,"All");
  }
  public void setBAN_Month_for_List(String value)
  {
    Month_for_List=SU.isNull(value,"All");
  }
  public void setCharge_Type_for_List(String value)
  {
    Charge_Type_for_List=SU.isNull(value,"All");
  }
  public void setMRUValues()
  {
      Global_Customer_Id_for_List=SU.isNull(Global_Customer_Id,"All");
      Account_for_List=SU.isNull(Invoice_Region,"All");
      GSR_for_List=SU.isNull(globalServiceReference,"All");
      BAN_Type_for_List=SU.isNull(chargeType,"All");
      Status_for_List=SU.isNull(banStatus,"All");
      Month_for_List=SU.isBlank(SU.reformatDate(SU.isNull(getRequired_BAN_Effective_Date(),""),"MMMMM yyyy"),"All");
  }
  public void setdefaultFieldModes()
  {
    if ((action.compareTo("Authorise") == 0) ||
        (action.compareTo("View") == 0) ||
	(action.compareTo("History") == 0) ||
        (Mode.compareTo("Cease") ==0))
    {
      SelectMode="DISABLED";
      InputMode="READONLY";
      Calendar="no";
    }
    else
    {
      SelectMode="submit";
      InputMode="";
      Calendar="";
    }
  }
  /*********************************************************************/
  public void setBanIdentifier(String newBanIdentifier)
  {
   banIdentifier=newBanIdentifier;
  }
  public void setCustomerfromList(String newCustomer)
  {
   Global_Customer_Id=SU.before(newCustomer);
  }
   public void setGlobalCustomerName(String newCustomer)
  {
   Global_Customer_Name = newCustomer;
  }
  public void setGlobalCustomerId(String value)
  {
    Enumeration e,e2;
    if (Global_Customer_Id.compareTo(value) == 0)
    {
      return;
    }
    Global_Customer_Id = value;
    Account_Id="";
    Service_Reference="";

    StringBuffer SQLBuffer = new StringBuffer("");

    if (Global_Customer_Id.compareTo("")!=0)
    {
      SQLBuffer.append("Select Global_Customer_Name from givn..Global_Customer");
      SQLBuffer.append(" where Global_Customer_Id = '").append(Global_Customer_Id);
      SQLBuffer.append("'");
      Global_Customer_Name=DBC.getValue(SQLBuffer.toString(),P3);
      //Datasource changes Global_Customer_Name=DBC.getValue(SQLBuffer.toString());

      //Usually only one account, so find it
      StringBuffer SQLBuffer2 = new StringBuffer("");
      SQLBuffer2.append("Select Account_Id,OutGoing_Currency_Code from givn_ref..Invoice_Region");
      SQLBuffer2.append(" where Global_Customer_Id = '").append(Global_Customer_Id).append("'");

      e2=DBC.getResults(SQLBuffer2.toString(),P3);
      //Datasource changes e2=DBC.getResults(SQLBuffer2.toString());

      if (e2.hasMoreElements())
      {
        Account_Id=SU.isNull((String)e2.nextElement(),"");
        if (e2.hasMoreElements())
        {
	  Currency_Desc=SU.isNull((String)e2.nextElement(),"");
        }
      }

    }
  }
  public void inheritGlobalCustomerId(String value)
  {
    if (Global_Customer_Id.compareTo(value) == 0)
    {
      return;
    }
    Global_Customer_Id = value;

    StringBuffer SQLBuffer = new StringBuffer("");

    if (Global_Customer_Id.compareTo("")!=0)
    {
      SQLBuffer.append("Select Global_Customer_Name from givn..Global_Customer (nolock)");
      SQLBuffer.append(" where Global_Customer_Id = '").append(Global_Customer_Id);
      SQLBuffer.append("'");
      Global_Customer_Name=DBC.getValue(SQLBuffer.toString(),P3);
      //Datasource changes Global_Customer_Name=DBC.getValue(SQLBuffer.toString());
    }
  }

  public void setCustomerFromAccount(String value)
  {
    Enumeration e;
    Global_Customer_Id = value;

    StringBuffer SQLBuffer = new StringBuffer("");

    if (Global_Customer_Id.compareTo("")!=0)
    {
      SQLBuffer.append("Select Global_Customer_Name from givn..Global_Customer (nolock)");
      SQLBuffer.append(" where Global_Customer_Id = '").append(Global_Customer_Id);
      SQLBuffer.append("'");

      Global_Customer_Name=DBC.getValue(SQLBuffer.toString(),P3);
      //Datasource changes Global_Customer_Name=DBC.getValue(SQLBuffer.toString());
    }
  }
  public void setInvoice_Region(String newInvoice_Region)
  {
   Enumeration e;
   StringBuffer SQLBuffer = new StringBuffer("");
   Invoice_Region = SU.isNull(newInvoice_Region,"");

   if (Invoice_Region.compareTo("")!=0)
   {
    SQLBuffer.append("Select Account_Id,OutGoing_Currency_Code from givn_ref..Invoice_Region (nolock)");
    SQLBuffer.append(" where Global_Customer_Id = '").append(Global_Customer_Id);
    SQLBuffer.append("' and Invoice_Region = '").append(Invoice_Region).append("'");

    //DBC.setSQL(SQLBuffer.toString());

      e=DBC.getResults(SQLBuffer.toString(),P3);
      //Datasource Changes e=DBC.getResults(SQLBuffer.toString());

    if (e.hasMoreElements())
    {
      Account_Id=SU.isNull((String)e.nextElement(),"");
        if (e.hasMoreElements())
        {
            Currency_Desc=SU.isNull((String)e.nextElement(),"");
        }
    }
   }
  }
  public void setAccount_Id(String value)
  {
    String This_Customer_Id;

    if (value.compareTo(Account_Id) !=0)
    {
      Account_Id = SU.isNull(value,"");

      Enumeration e;
      StringBuffer SQLBuffer = new StringBuffer("");

      if (Account_Id.compareTo("")!=0)
      {
	SQLBuffer.append("Select upper(Global_Customer_Id),Invoice_Region,OutGoing_Currency_Code from givn_ref..Invoice_Region (nolock)");
	SQLBuffer.append(" where Account_Id = '").append(Account_Id).append("'");

	e=DBC.getResults(SQLBuffer.toString(),P3);
	//Datasource changes e=DBC.getResults(SQLBuffer.toString());

	if (e.hasMoreElements())
	{
	  This_Customer_Id=SU.isNull((String)e.nextElement(),"");
	  Invoice_Region=SU.isNull((String)e.nextElement(),"");
	  Currency_Desc=SU.isNull((String)e.nextElement(),"");
	  if (This_Customer_Id.compareTo(Global_Customer_Id) !=0)
	  {
	    setCustomerFromAccount(This_Customer_Id);
	  }
	}
      }
    }
  }
  public void setBanStatus(String newbanStatus)
  {
   banStatus = newbanStatus;
  }
  public void setGlobalServiceReference(String newglobalServiceReference)
  {
    globalServiceReference = newglobalServiceReference;
  }
  public void setService_Reference(String value)
  {
    String This_Account_Id,This_Status;

    if (value.compareTo(Service_Reference) !=0)
    {
      Service_Reference = SU.isNull(value,"");

      /*if (Mode.compareTo("Create") !=0)
      {  //need to ripple up to Account

        StringBuffer SQLBuffer = new StringBuffer("");
        Enumeration e;

        if (Service_Reference.compareTo("")!=0)
        {
	  SQLBuffer.append("Select Account_Id from OSS..Service_Item (nolock)");
	  SQLBuffer.append(" where CMDB_Service_Reference = '").append(Service_Reference).append("'");

	  This_Account_Id=DBC.getValue(SQLBuffer.toString(),P5);

	  if (!This_Account_Id.startsWith("No Data"))
	  {
	    if (This_Account_Id.compareTo(Account_Id) !=0)
	    {
	      setAccount_Id(This_Account_Id);
	    }
	  }
	  else
	  {
	    Message = "<font color=red><b>Invalid Service Reference";
	    Global_Customer_Id="";
	    Account_Id="";
	  }
        }
      }*/
    }
  }
  public void setBanCreatedBy(String newbanCreatedBy)
  {
    banCreatedBy = newbanCreatedBy;
  }
  public void setReturn_BAN_To_Name(String newReturn_BAN_To_Name)
  {
    Return_BAN_To_Name = newReturn_BAN_To_Name;
  }
  public void setBAN_Summary(String newBAN_Summary)
  {
    BAN_Summary = newBAN_Summary;
  }
  public void setBAN_Reason(String newBAN_Reason)
  {
   BAN_Reason = newBAN_Reason;
  }
  public void setRequired_BAN_Effective_Date(String newRequired_BAN_Effective_Date)
  {
    Required_BAN_Effective_Dateh = SU.toJDBCDate(newRequired_BAN_Effective_Date);
  }
  public void setProposedBanDate(String newproposedBanDate)
  {
    proposedBanDate = SU.toJDBCDate(newproposedBanDate);
  }
  public void setUserId(String newuserId)
  {
     userId = newuserId;
  }
  public void setRejectReason(String newrejectReason)
  {
    rejectReason = newrejectReason;
  }
  public void setOrderBy(String newOrderBy)
  {
    OrderBy = newOrderBy;
  }
  public void setCurrency_Desc(String value)
  {
   Currency_Desc = SU.before(SU.isNull(value,""));
  }
  public void setCarrier_Name(String value)
  {
   Carrier_Name = SU.isNull(value,"");
  }
  public void setErrored(String Item)
  {
    if (Item.startsWith("clear"))
      errored.clear();
    else
      errored.addElement(Item);
  }
  public void setMessage(String newMessage)
  {
    Message=newMessage;
  }
  public void setMode(String value)
  {
    Mode=value;
    setdefaultFieldModes();
  }
  public void setSite(String newSite)
  {//site can contain spaces
    siteDisplay=newSite;
    if (newSite.length() > 8)
    {
      site = newSite.substring(0,8);
    }
    else
    {
      site=newSite;
    }
  }
  public void setBANs_to_List(String value)
  {
    BANs_to_List=value;
    Message="Please select the required filter values";
    if (BANs_to_List.compareTo("All BANs")==0)
    {
      Global_Customer_Id_for_List="All";
      Account_for_List="All";
      GSR_for_List="All";
      BAN_Type_for_List="All";
      Status_for_List="All";
      Month_for_List="All";
    }
    else if  (BANs_to_List.startsWith("BANs"))
    {
      Account_for_List="All";
      GSR_for_List="All";
      BAN_Type_for_List="All";
      Status_for_List="All";
      Month_for_List="All";
    }
    else if (BANs_to_List.endsWith("Account"))
    {
      GSR_for_List="All";
      BAN_Type_for_List="All";
      Status_for_List="All";
      Month_for_List="All";
    }
    else if (BANs_to_List.endsWith("Service"))
    {
      BAN_Type_for_List="All";
      Status_for_List="All";
      Month_for_List="All";
    }
  }
  public void setShowFilters(String value)
  {
    showFilters=value;
  }
/****************************************************************************/
  public void setAction(String newaction)
  {
    action = newaction;
    if (action == null)
    {
      action="View";
    }
    if (action.equals("Amend"))
    {
      bansToList="'Draft','Proposed','Authorised'";
    }
    else if (action.equals("Authorise"))
    {
      bansToList="'Proposed'";
    }
    else if (action.equals("Accept"))
    {
      bansToList="'Authorised'";
    }
    else
    {
      bansToList="'Draft','Proposed','Authorised','Accepted','Complete','Rejected','Cancelled','Submitted'";
    }
  }
/*******************************************************************************/
  public boolean errors()
  {
    if(errored.isEmpty())
      return false;
    else
      return true;
  }
/*******************************************************************************/
  public String getBanList(String System)
  {
    int counter=0;
    String RadioButton;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");

    RadioButton="width=45 align=center><img src=\"../nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";
    try{

      DBC.Connect(PREPARE,P5);
      if (Conn  != null)
      {
	grid.append("<table border=0>");
	rowcount = 0;

	if (System.compareTo("OSS") ==0)
	{
	  if (action.compareTo("History") !=0)
	  {
	    SQL = "{call listOSSBANs ";
	  }
	  else
	  {
    	    SQL = "{call List_Archived_OSS_BANs ";
	  }
	}
	else
	{
	  SQL = "{call listBANs ";
	}
	columns=8;

        SQL += "(?,?,?,?,?,?,?,?)}";

	cstmt = Conn.prepareCall(SQL);
	cstmt.setString(1,action);
	cstmt.setString(2,OrderBy);
	cstmt.setString(3,Global_Customer_Id_for_List);
	cstmt.setString(4,Account_for_List);
	cstmt.setString(5,GSR_for_List);
	cstmt.setString(6,BAN_Type_for_List);
	cstmt.setString(7,Status_for_List);
	cstmt.setString(8,Month_for_List);
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
/*******************************************************************************/
  public boolean findBanType()
  {
    try
    {
      Conn=DBC.Connect(PREPARE,P5);

      if (Conn != null)
      {
	SQL = "{call Get_BAN_Type ";
	SQL += "(?)}";
	cstmt = Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
	cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
	  if (RS.next())
	  {
	    banType=RS.getString(1);
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
      else
      {
	Message="<font color=red><b>"+DBC.getMessage();
	return false;
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      close(Conn);
      return false;
    }
    catch(java.lang.NullPointerException se){Message="<font color=red><b>"+se.getMessage();return false;}
    //message set in underlying code
    return true;
  }
/*************************************************************************************/
  //for debugging
  public static void main (String[] args)
  {
    /*StringBuffer SQLBuffer = new StringBuffer("");
    String gn;
      DBCccess DBC = new DBCccess();
      SQLBuffer.append("Select Global_Customer_Name from givn..Global_Customer");
      SQLBuffer.append(" where Global_Customer_Id = 'bvd'");

      //gn=DBC.getValue(SQLBuffer.toString(),P3);
      */
  }
}