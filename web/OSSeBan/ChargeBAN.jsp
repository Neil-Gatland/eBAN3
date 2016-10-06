<html>
<jsp:include page="../includes/Page_Header5.jsp" flush="true" />
<script language="JavaScript" src="../includes/ChargeFunctions.js">
</script>
<%@ page isThreadSafe="false" %>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="DBUtilities.OSSChargeBANBean"%>
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>
<!--jsp:useBean id="chBAN" class="DBUtilities.OSSChargeBANBean" scope="session"/-->
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
  <%
    StringBuffer SQL2 = new StringBuffer("");
    String Global_Customer_Id,Global_Customer_Name,Circuit_Reference,Product_Category="";
    String BAN_Summary,BAN_Reason,Product_Type="",Charge_Entity="";
    String Required_BAN_Effective_Date;
    String BAN_Identifier="";
    String SelectMode="",Calendar="",InputMode="",Mode="";
    String Division_Id="",Site_Reference="",Site_Reference2="",Site_Reference3="";
    String OSS_Charge_Type,Charge_Category,Charge_Category_Display,Charge_Frequency="Annual";
    String Charge_Description="";
    String Gross_Amount,From_Charge_Valid_Date,To_Charge_Valid_Date,Action;
    String Charge_Period_End_Date;
    String Charge_Period_Start_Date;
    int BEDay, BEMonth, BEYear, FCDay, FCMonth, FCYear, TCDay, TCMonth, TCYear,
      PCDay, PCMonth, PCYear, PEDay, PEMonth, PEYear, PSDay, PSMonth, PSYear;
    String Splits="",Unit_Quantity="",Charge_Currency="",Charge_Description_Code="",
      Discounts_Applicable="",Revenue_Type="",Split="",Split2="",Split3="",
      Revenue_Description="",Revenue_Net_Or_Full="",Revenue_Root_Cause="";
    String C2_Ref_No="",C3_Ref_No="",Price_Change_Date,Reopen_Bill,
      oldGrossAmount;

    String userId = (String)session.getAttribute("User_Id");
    OSSChargeBANBean chBAN = (OSSChargeBANBean)session.getAttribute("chBAN" + userId);
    if (chBAN == null)
    {
      chBAN = new OSSChargeBANBean();
      session.setAttribute("chBAN" + userId, chBAN);
    }
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
   //Initialization
    //Global_Customer_Id=(String)session.getAttribute("Global_Customer_Id");
    Global_Customer_Id=chBAN.getGlobalCustomerId();
    Global_Customer_Name=chBAN.getGlobalCustomerName();

    Mode=chBAN.isChargeArchived()?"View":chBAN.getMode();
    //chBAN.setModes();

    SelectMode=chBAN.getSelectMode();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=chBAN.getInputMode();
    Calendar=chBAN.getCalendar();

    BAN_Identifier=chBAN.getBanIdentifier();
    Action=chBAN.isChargeArchived()?"View":chBAN.getAction();

    BAN_Summary = chBAN.getBAN_Summary();
    BAN_Reason = chBAN.getBAN_Reason();
    Product_Category=ctBAN.getGCB_Product_Type();
    Product_Type=ctBAN.getProduct_Type();
    Required_BAN_Effective_Date=SU.isNull(chBAN.getRequired_BAN_Effective_Date(),"Today");
    Circuit_Reference=chBAN.getCircuit_Reference();
    Division_Id = chBAN.getDivision_Id();

    Charge_Category=chBAN.getCharge_Category();
    Charge_Category_Display=chBAN.getCharge_CategoryDisplay();

    String cdcAuthority = chBAN.getCDCAuthority();
    Charge_Description  = chBAN.getCharge_Description();
    Charge_Currency = chBAN.getCharge_Currency();
    Gross_Amount = chBAN.getGross_Amount();
    Price_Change_Date=SU.isNull(chBAN.getPrice_Change_Date(),"Today");
    From_Charge_Valid_Date=SU.isNull(chBAN.getCharge_Valid_From_Date(),"FirstOfMonth");
    To_Charge_Valid_Date=chBAN.getCharge_Valid_To_Date();
    Charge_Period_Start_Date=chBAN.getCharge_Period_Start_Date();
    Charge_Period_End_Date=chBAN.getCharge_Period_End_Date();
    BEDay=chBAN.getBANEffectiveDateDays();
    BEMonth=chBAN.getBANEffectiveDateMonths();
    BEYear=chBAN.getBANEffectiveDateYears();
    FCDay=chBAN.getFromChargeValidDateDays();
    FCMonth=chBAN.getFromChargeValidDateMonths();
    FCYear=chBAN.getFromChargeValidDateYears();
    TCDay=chBAN.getToChargeValidDateDays();
    TCMonth=chBAN.getToChargeValidDateMonths();
    TCYear=chBAN.getToChargeValidDateYears();
    PCDay=chBAN.getPriceChangeDateDays();
    PCMonth=chBAN.getPriceChangeDateMonths();
    PCYear=chBAN.getPriceChangeDateYears();
    PEDay=chBAN.getChargePeriodEndDateDays();
    PEMonth=chBAN.getChargePeriodEndDateMonths();
    PEYear=chBAN.getChargePeriodEndDateYears();
    PSDay=chBAN.getChargePeriodStartDateDays();
    PSMonth=chBAN.getChargePeriodStartDateMonths();
    PSYear=chBAN.getChargePeriodStartDateYears();
    Charge_Frequency=chBAN.getCharge_Frequency();
    Charge_Entity=chBAN.getCharge_Entity();
    OSS_Charge_Type=chBAN.getOSS_Charge_Type();
    String Status = chBAN.getBanStatus();
    Splits = chBAN.getSplits();
    Split = chBAN.getSplit();
    Split2 = chBAN.getSplit2();
    Split3 = chBAN.getSplit3();
    Unit_Quantity = chBAN.getUnit_Quantity();
    Site_Reference = chBAN.getSite_Reference();
    Site_Reference2 = chBAN.getSite_Reference2();
    Site_Reference3 = chBAN.getSite_Reference3();
    String pctStyle = Splits.equals("0")?"style=\"visibility:hidden\"":"";
    Charge_Description_Code  = chBAN.getChargeDescriptionCode();
    Discounts_Applicable = chBAN.getDiscountsApplicable();
    Revenue_Type = chBAN.getRevenueType();
    Revenue_Description=chBAN.getRevenueDescription();
    Revenue_Net_Or_Full=chBAN.getRevenueNetOrFull();
    Revenue_Root_Cause=chBAN.getRevenueRootCause();
    boolean toDateNullable = chBAN.getToDateNullable();
    String fromDateTitle =
      Charge_Category.equals("01")?"From Charge Valid Date":
      Charge_Category.equals("02")?"Charge Payable Date":"Credit Payable Date";
    String unitChargeCredit = Charge_Category.equals("03")?"Credit":"Unit Charge";
    String chargeCredit = Charge_Category.equals("03")?"Credit":"Charge";
    C2_Ref_No=chBAN.getC2RefNo();
    C3_Ref_No=chBAN.getC3RefNo();
    Reopen_Bill=chBAN.getReopenBill();
    oldGrossAmount=chBAN.getOldGrossAmount();
    String[] circuitEnds = chBAN.getCircuitEnds();
    String chargeId = request.getParameter("chargeId");
    if ((chargeId != null) && (!chargeId.equals("")))
    {
      try
      {
        if (Long.parseLong(chargeId) != chBAN.getChargeIdCheck())
        {
          session.setAttribute("Message","<font color=red><b>" +
            "There has been a problem with the charge you requested. " +
            "Please try again.</b></font>");
          %>
<script language="javascript">
window.location="OSSBANMenu.jsp";
</script>
          <%
        }
      }
      catch (NumberFormatException ex)
      {
        //don't panic - charge id is not numeric
      }
    }
    if (chBAN.isChargeArchived())
    {
      chBAN.setMessage("<font color=blue>This charge has been archived and cannot be amended or deleted");
    }
   %>
<script language="javascript">
function checkCDC()
{
  var cdc = ChargeBAN.Charge_Description_Code[ChargeBAN.Charge_Description_Code.selectedIndex].value;
  if ((cdc == "Tax Exempt") || (cdc == "Sales Tax"))
  {
    alert("WARNING!\n\rThis charge description code should only be used with " +
      "the prior agreement of\n\r<%=cdcAuthority%>.");
  }
}
</script>
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
      <td colspan=2>
	<form name="ChargeBAN" method="post" action="ChargeBANHandler.jsp">
        <%=HB.getBANMenu_Bar("BAN",Mode,Action,(String)session.getAttribute("System"),chBAN.getIsDirect())%>
        <input name="ButtonPressed" type=hidden value="">
        <input name="RejectReason" type=hidden value="">
        <input name="errorMessage" type=hidden value="<%=chBAN.getMessage()%>">
        <input name="chargeId" type=hidden value="<%=chargeId==null?"":chargeId%>">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
	    <!-- this is a spacer column-->
	    <td width="13">&nbsp;</td>
	    <td valign="top" align="left" NOWRAP width=10><!--Formerly Menu Column--></td>
	    <!-- this is a spacer column-->
	    <td width="13">&nbsp;</td>
	    <td  valign ="Top"><!-- Main table starts here-->
	      <table width="100%" border="0" cellpadding="0" cellspacing="0" id=3>
		<tr>
		  <td valign="top" colspan=2>
		    <h3 align="center"><%=Mode%> <%=Charge_Category_Display%></h3>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(chBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" height="341" colspan=2>
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td colspan=4>
			  <table width=100%>
			    <tr>
			      <td><b>Customer:</b></td>
			      <td class=bluebold><%=Global_Customer_Name%></td>
			      <td colspan="2">&nbsp;</td>
			    </tr>
			    <tr>
			      <td><b>Billing Advice Status:</td>
			      <td class=bluebold><%=Status%></td>
			      <td><b>Requested By:</td>
			      <td class=bluebold>
				<%=SU.isBlank(chBAN.getBanCreatedBy(),(String)session.getAttribute("User_Name"))%><br>
			      </td>
			    </tr>
			  </table>
			</td>
		      </tr>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
			<td class=<%=chBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
		          <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=chBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
		        </td>
		        <td style="visibility:hidden" width="50%" colspan="2" valign="top" class=<%=chBAN.getClass("Required_BAN_Effective_Dateh")%>>Effective Date<br>
                          <%=HB.getDays("ChargeBAN", "Required_BAN_Effective_Date", BEDay, false, disableDate)%>
                          <%=HB.getMonths("ChargeBAN", "Required_BAN_Effective_Date", BEMonth, false, disableDate)%>
                          <%=HB.getYears("ChargeBAN", "Required_BAN_Effective_Date", BEYear, false, disableDate)%>
                          <input type="hidden" name="Required_BAN_Effective_Dateh" value="<%=Required_BAN_Effective_Date%>">
		        </td>
			<td>
			  <span style="VISIBILITY: <%=chBAN.getRejectVisibility()%>">
			    <b><small><%=Status%> Reason
		            <br><textarea class=red cols="40" rows="2" READONLY><%=chBAN.getRejectReason()%></textarea>
			  </span>
		        </td>
		      </tr>
		      <tr>
		        <td colspan=4 class=bluebold><%=Charge_Category_Display%> Details</td>
		      </tr>
		      <tr>
		        <td class=<%=chBAN.getClass("Circuit_Reference")%> valign=top>
			  <b><small>Circuit Reference<br>
        		    <%=DBA.getListBox("Circuit_Reference",chBAN.getMode("Circuit_Reference"),Circuit_Reference,Global_Customer_Id,
                              Division_Id.equals("")?"":(" Global_Customer_Division_Id = '" + Division_Id + "' and "),1," style=\"width:85%;\"",true)%>
		        </td>
		      </tr>
		      <tr>
			<td colspan="2" class=<%=chBAN.getClass("Site_Reference")%> colspan=2><b><small>Site<br>
			  <%=DBA.getListBox("Site_Reference",chBAN.getMode("Site_Reference"),Site_Reference,Global_Customer_Id,
                            1," style=\"width:85%;\"",true)%>
			</td>
		        <td class=<%=chBAN.getClass("Splits")%> <%=pctStyle%> valign="top"><b><small>Percentage<br>
			  <input class=inp type="text" name="Split" maxlength="6" size="6" value="<%=Split%>" <%=chBAN.getMode("Splits")%>>%
			</td>
		        <td class=<%=chBAN.getClass("Splits")%> valign=top>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Repro")%>"><b><small>Number of Splits<br>
			    <%=DBA.getListBox("Splits",/*SelectMode*/"READONLY",Splits,"0","2",1,"",false)%>
                            <!--Splits drop-down disabled but processing left in just in case,
                              hence hidden input below - TA 02/06/2010 -->
                            <input type="hidden" name="Splits" value="<%=Splits%>">
			  </span>
		        </td>
		      </tr>
<%if (!Splits.equals("0"))
  {%>
		      <tr>
			<td style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Repro")%>" colspan="2" class=<%=chBAN.getClass("Site_Reference")%> colspan=2>
			  <%=DBA.getListBox("Site_Reference2",SelectMode,Site_Reference2,Global_Customer_Id,
                            1," style=\"width:85%;\"",true)%>
			</td>
		        <td class=<%=chBAN.getClass("Splits")%> <%=pctStyle%> valign="top">
			  <input class=inp type="text" name="Split2" maxlength="6" size="6" value="<%=Split2%>" <%=chBAN.getMode("Splits")%>>%
			</td>
		      </tr>
<%}
  if (Splits.equals("2"))
  {%>
		      <tr>
			<td style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Repro")%>" colspan="2" class=<%=chBAN.getClass("Site_Reference")%> colspan=2>
			  <%=DBA.getListBox("Site_Reference3",SelectMode,Site_Reference3,Global_Customer_Id,
                            1," style=\"width:85%;\"",true)%>
			</td>
		        <td class=<%=chBAN.getClass("Splits")%> <%=pctStyle%> valign="top">
			  <input class=inp type="text" name="Split3" maxlength="6" size="6" value="<%=Split3%>" <%=chBAN.getMode("Splits")%>>%
			</td>
		      </tr>
<%}%>
		      <tr>
                      <%if (Mode.equals("Cease / "))
                        {%>
		        <td class=<%=chBAN.getClass("Old_Gross_Amount")%>><b><small>Original <%=unitChargeCredit%> Amount<br>
			  <input class=inp type="text" name="Old_Gross_Amount" maxlength="25" size="25" value="<%=oldGrossAmount%>" disabled>
			</td>
		        <td class=<%=chBAN.getClass("Gross_Amount")%>><b><small>New <%=unitChargeCredit%> Amount<br>
			  <input class=inp type="text" name="Gross_Amount" maxlength="25" size="25" value="<%=Gross_Amount%>" <%=chBAN.getMode("Gross_Amount")%>>
			</td>
                      <%}
                        else
                        {%>
		        <td class=<%=chBAN.getClass("Gross_Amount")%>><b><small><%=unitChargeCredit%> Amount<br>
			  <input class=inp type="text" name="Gross_Amount" maxlength="25" size="25" value="<%=Gross_Amount%>" <%=chBAN.getMode("Gross_Amount")%>>
			</td>
		        <td class=<%=chBAN.getClass("Unit_Quantity")%>>
                          <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Unit_Quantity")%>">
                            <b><small>Unit Quantity<br>
			    <input class=inp type="text" name="Unit_Quantity" maxlength="25" size="25" value="<%=Unit_Quantity%>" <%=chBAN.getMode("Unit_Quantity")%>>
                          </span>
			</td>
                      <%}%>
		        <td class=<%=chBAN.getClass("Charge_Currency")%>>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Charge_Currency")%>"><b><small><%=chargeCredit%> Currency<br>
			    <%=DBA.getListBox("Charge_Currency",chBAN.getMode("Charge_Currency"),Charge_Currency,"")%>
			  </span>
		        </td>
<%if (Charge_Category.equals("01"))
  {%>
		        <td class=<%=chBAN.getClass("Charge_Frequency")%>>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Charge_Frequency")%>"><b><small>Charge Period<br>
			    <%=DBA.getListBox("Charge_Frequency",chBAN.getMode("Charge_Frequency"),Charge_Frequency,"")%>
			  </span>
		        </td>
<%}
  else
  {%>
		        <td valign="top" class=<%=chBAN.getClass("Charge_Period_Start_Dateh")%>>
                          <%=chargeCredit%> Period Start Date<br>
                          <%=HB.getDays("ChargeBAN", "Charge_Period_Start_Date", PSDay, true/*toDateNullable*/, chBAN.disableDate("Charge_Period_Start_Date", disableDate), PSMonth, PSYear)%>
                          <%=HB.getMonths("ChargeBAN", "Charge_Period_Start_Date", PSMonth, true/*toDateNullable*/, chBAN.disableDate("Charge_Period_Start_Date", disableDate))%>
                          <%=HB.getYears("ChargeBAN", "Charge_Period_Start_Date", PSYear, true/*toDateNullable*/, chBAN.disableDate("Charge_Period_Start_Date", disableDate))%>
                          <input type="hidden" name="Charge_Period_Start_Dateh" value="<%=Charge_Period_Start_Date%>">
		        </td>
<%}%>
		      </tr>
		      <tr>
		        <td colspan=2 class=<%=chBAN.getClass("Charge_Description")%>><b><small>Description to appear on invoice
			  <br><textarea class=tworows name="Charge_Description" cols="60" <%=chBAN.getMode("Charge_Description")%>><%=Charge_Description%></textarea>
			</td>
		        <td valign="top" class=<%=chBAN.getClass("From_Charge_Valid_Dateh")%>><%=fromDateTitle%><br>
                          <%=HB.getDays("ChargeBAN", "From_Charge_Valid_Date", FCDay, false, chBAN.disableDate("From_Charge_Valid_Date", disableDate), FCMonth, FCYear)%>
                          <%=HB.getMonths("ChargeBAN", "From_Charge_Valid_Date", FCMonth, false, chBAN.disableDate("From_Charge_Valid_Date", disableDate))%>
                          <%=HB.getYears("ChargeBAN", "From_Charge_Valid_Date", FCYear, false, chBAN.disableDate("From_Charge_Valid_Date", disableDate))%>
                          <input type="hidden" name="From_Charge_Valid_Dateh" value="<%=From_Charge_Valid_Date%>">
		        </td>
<%if (Charge_Category.equals("01"))
  {%>
		        <td valign="top" class=<%=chBAN.getClass("To_Charge_Valid_Dateh")%>>
                          <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("ToDate")%>">
                          To Charge Valid Date<br>
                          <%=HB.getDays("ChargeBAN", "To_Charge_Valid_Date", TCDay, true/*toDateNullable*/, chBAN.disableDate("To_Charge_Valid_Date", disableDate), TCMonth, TCYear)%>
                          <%=HB.getMonths("ChargeBAN", "To_Charge_Valid_Date", TCMonth, true/*toDateNullable*/, chBAN.disableDate("To_Charge_Valid_Date", disableDate))%>
                          <%=HB.getYears("ChargeBAN", "To_Charge_Valid_Date", TCYear, true/*toDateNullable*/, chBAN.disableDate("To_Charge_Valid_Date", disableDate))%>
                          <input type="hidden" name="To_Charge_Valid_Dateh" value="<%=To_Charge_Valid_Date%>">
                          </span>
		        </td>
<%}
  else
  {%>
		        <td valign="top" class=<%=chBAN.getClass("Charge_Period_End_Dateh")%>>
                          <%=chargeCredit%> Period End Date<br>
                          <%=HB.getDays("ChargeBAN", "Charge_Period_End_Date", PEDay, true/*toDateNullable*/, chBAN.disableDate("Charge_Period_End_Date", disableDate), PEMonth, PEYear)%>
                          <%=HB.getMonths("ChargeBAN", "Charge_Period_End_Date", PEMonth, true/*toDateNullable*/, chBAN.disableDate("Charge_Period_End_Date", disableDate))%>
                          <%=HB.getYears("ChargeBAN", "Charge_Period_End_Date", PEYear, true/*toDateNullable*/, chBAN.disableDate("Charge_Period_End_Date", disableDate))%>
                          <input type="hidden" name="Charge_Period_End_Dateh" value="<%=Charge_Period_End_Date%>">
		        </td>
<%}%>
		      </tr>
                      <%if (!Mode.equals("Cease / "))
                        {%>
		      <tr>
			<td colspan="2" class=<%=chBAN.getClass("Charge_Description_Code")%> colspan=2><b><small>Charge Description Code<br>
			  <%=chBAN.getCDCListBox("Charge_Description_Code",InputMode,Charge_Description_Code,Circuit_Reference,
                            1," style=\"width:85%;\"",true)%>
			</td>
		        <td class=<%=chBAN.getClass("Charge_Entity")%>>
			  <span><b><small>C&W or Carrier Charge<br>
			    <%=DBA.getListBox("Charge_Entity",chBAN.getMode("Charge_Entity"),Charge_Entity,Product_Category)%>
			  </span>
		        </td>
		      </tr>
		      <tr>
		        <td class=<%=chBAN.getClass("Discounts_Applicable")%> valign=bottom>
			  <span><br><b><small>Discounts Applicable?<br>
			    <%=DBA.getListBox("Discounts_Applicable","DISABLED",Discounts_Applicable,"")%>
			  </span>
		        </td>
		      </tr>
		      <tr>
		        <td class=<%=chBAN.getClass("Revenue_Type")%> valign=bottom>
			  <span><br><b><small>Revenue Type<br>
			    <%=DBA.getListBox("Revenue_Type",InputMode,Revenue_Type,"XX")%>
			  </span>
		        </td>
		        <td class=<%=chBAN.getClass("Revenue_Description")%> valign=bottom>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Revenue_Description")%>"><br><b><small>Revenue Description<br>
			    <%=DBA.getListBox("Revenue_Description",InputMode,Revenue_Description,"XX")%>
			  </span>
		        </td>
		        <td class=<%=chBAN.getClass("Revenue_Net_Or_Full")%> valign=bottom>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Revenue_Net_Or_Full")%>"><br><b><small>Net Or Full<br>
			    <%=DBA.getListBox("Revenue_Net_Or_Full",InputMode,Revenue_Net_Or_Full,"XX")%>
			  </span>
		        </td>
		        <td class=<%=chBAN.getClass("Revenue_Root_Cause")%> valign=bottom>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Revenue_Root_Cause")%>"><br><b><small>Revenue Root Cause<br>
			    <%=DBA.getListBox("Revenue_Root_Cause",InputMode,Revenue_Root_Cause,"XX")%>
			  </span>
		        </td>
		      </tr>
                      <%}%>
		      <tr>
		        <td width="50%" colspan="2" class=<%=chBAN.getClass("C2_Ref_No")%>><b><small>C2 Ref No<br>
			  <input class=inp style="width:400px" type="text" name="C2_Ref_No" maxlength="50" size="50" value="<%=C2_Ref_No%>" <%=chBAN.getMode("C2_Ref_No")%>>
			</td>
                      <%if (Mode.equals("Cease / "))
                        {%>
		        <td class=<%=chBAN.getClass("Price_Change_Dateh")%>><b><small>Effective Date of Price Change<br>
                          <%=HB.getDays("ChargeBAN", "Price_Change_Date", PCDay, false, chBAN.disableDate("Price_Change_Date", disableDate), PCMonth, PCYear)%>
                          <%=HB.getMonths("ChargeBAN", "Price_Change_Date", PCMonth, false, chBAN.disableDate("Price_Change_Date", disableDate))%>
                          <%=HB.getYears("ChargeBAN", "Price_Change_Date", PCYear, false, chBAN.disableDate("Price_Change_Date", disableDate))%>
                          <input type="hidden" name="Price_Change_Dateh" value="<%=Price_Change_Date%>">
			</td>
		        <td class=<%=chBAN.getClass("Reopen_Bill")%>><b><small>Re-open Bill?<br>
			    <%=DBA.getListBox("Reopen_Bill",InputMode,Reopen_Bill,"")%>
		        </td>
		      </tr>
		      <tr>
		        <td colspan="3">
		        </td>
		        <td style="FONT-FAMILY: verdana, arial, helvetica, sans-serif; FONT-SIZE: xx-small;">Selecting 'Y' will allow you to produce another bill for this customer which will
                        include only the credits/debits produced from the cease/reprovide charges
		        </td>
                      <%}
                        else
                        {%>
		        <td width="50%" colspan="2" class=<%=chBAN.getClass("C3_Ref_No")%>><b><small>C3 Ref No<br>
			  <input class=inp style="width:400px" type="text" name="C3_Ref_No" maxlength="50" size="50" value="<%=C3_Ref_No%>" <%=chBAN.getMode("C3_Ref_No")%>>
			</td>
                      <%}%>
		      </tr>
		      <tr>
		        <td width="50%" colspan="2" class=<%=chBAN.getClass("From_End")%>>
                          <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Circuit_Ends")%>"><b><small>A End<br>
			  <input class=inp style="width:400px" type="text" name="From_End" maxlength="200" size="50" value="<%=circuitEnds[0]%>" READONLY>
			</td>
		        <td width="50%" colspan="2" class=<%=chBAN.getClass("To_End")%>>
                          <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Circuit_Ends")%>"><b><small>B End<br>
			  <input class=inp style="width:400px" type="text" name="To_End" maxlength="200" size="50" value="<%=circuitEnds[1]==null?"":circuitEnds[1]%>" READONLY>
			</td>
		      </tr>
		    </table><!--4-->
		  </td>
		</tr>
	      </table><!--3-->
	    </td>
	  </tr>
        </table><!--2-->
        </form>
      </td>
    </tr>
    <!--Footer-->
    <tr>
      <td>
      </td>
    </tr>
  </table><!--1-->
</body>
</html>


