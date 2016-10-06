<html>
<jsp:include page="../includes/Page_Header5.jsp" flush="true" />
<script language="JavaScript" src="../includes/ChargeFunctions.js">
</script>
<%@ page import="JavaUtil.StringUtil"%>
<jsp:useBean id="chBAN" class="DBUtilities.FixedChargeBANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
  <%!//Variables
    StringBuffer SQL2 = new StringBuffer("");
    String Global_Customer_Id,Global_Customer_Name="";
    String BAN_Summary,BAN_Reason;
    String Required_BAN_Effective_Date;
    String BAN_Identifier="";
    String SelectMode="",InputMode="",Mode="";
    String Charge_Description="";
    String Gross_Amount,From_Charge_Valid_Date,To_Charge_Valid_Date,Action;
    int BEDay, BEMonth, BEYear, FCDay, FCMonth, FCYear, TCDay, TCMonth, TCYear;
    String Charge_Currency="";

    String Users, Credit_Debit, Fixed_Charge_Type = "";

    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
   %>
  <%//Initialization
    //Global_Customer_Id=(String)session.getAttribute("Global_Customer_Id");
    Global_Customer_Id=chBAN.getGlobalCustomerId();
    Global_Customer_Name=chBAN.getGlobalCustomerName();

    Mode=chBAN.isChargeArchived()?"View":chBAN.getMode();
    //chBAN.setModes();

    SelectMode=chBAN.getSelectMode();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=chBAN.getInputMode();

    BAN_Identifier=chBAN.getBanIdentifier();
    Action=chBAN.isChargeArchived()?"View":chBAN.getAction();

    BAN_Summary = chBAN.getBAN_Summary();
    BAN_Reason = chBAN.getBAN_Reason();
    Required_BAN_Effective_Date=SU.isNull(chBAN.getRequired_BAN_Effective_Date(),"Today");


    Charge_Description  = chBAN.getCharge_Description();
    Charge_Currency = chBAN.getCharge_Currency();
    Gross_Amount = chBAN.getGross_Amount();
    From_Charge_Valid_Date=SU.isNull(chBAN.getCharge_Valid_From_Date(),"FirstOfMonth");
    To_Charge_Valid_Date=chBAN.getCharge_Valid_To_Date();
    BEDay=chBAN.getBANEffectiveDateDays();
    BEMonth=chBAN.getBANEffectiveDateMonths();
    BEYear=chBAN.getBANEffectiveDateYears();
    FCDay=chBAN.getFromChargeValidDateDays();
    FCMonth=chBAN.getFromChargeValidDateMonths();
    FCYear=chBAN.getFromChargeValidDateYears();
    TCDay=chBAN.getToChargeValidDateDays();
    TCMonth=chBAN.getToChargeValidDateMonths();
    TCYear=chBAN.getToChargeValidDateYears();
    String Status = chBAN.getBanStatus();
    Users = chBAN.getUsers();
    Credit_Debit = chBAN.getCRDB();
    Fixed_Charge_Type = chBAN.getFixedChargeType();

   %>
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
      <td colspan=2>
	<form name="FixedChargeBAN" method="post" action="FixedChargeBANHandler.jsp">
        <%=HB.getBANMenu_Bar("BAN",Mode,Action,(String)session.getAttribute("System"),chBAN.getIsDirect())%>
        <input name="ButtonPressed" type=hidden value="">
        <input name="RejectReason" type=hidden value="">
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
		    <h3 align="center"><%=Mode%> a Fixed Charge</h3>
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
                          <%=HB.getDays("FixedChargeBAN", "Required_BAN_Effective_Date", BEDay, false, disableDate)%>
                          <%=HB.getMonths("FixedChargeBAN", "Required_BAN_Effective_Date", BEMonth, false, disableDate)%>
                          <%=HB.getYears("FixedChargeBAN", "Required_BAN_Effective_Date", BEYear, false, disableDate)%>
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
		        <td colspan=4 class=bluebold>Fixed Charge Details</td>
		      </tr>
		      <tr>
		        <td class=<%=chBAN.getClass("Users")%>><b><small>Users<br>
			    <input class=inp type="text" name="Users" maxlength="25" size="25" value="<%=Users%>" <%=chBAN.getMode("Users")%>>
			</td>
		        <td class=<%=chBAN.getClass("Gross_Amount")%>><b><small>Unit Charge Amount<br>
			  <input class=inp type="text" name="Gross_Amount" maxlength="25" size="25" value="<%=Gross_Amount%>" <%=chBAN.getMode("Gross_Amount")%>>
			</td>
		        <td class=<%=chBAN.getClass("Charge_Currency")%>>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Charge_Currency")%>"><b><small>Charge Currency<br>
			    <%=DBA.getListBox("Charge_Currency",InputMode,Charge_Currency,"")%>
			  </span>
		        </td>
		        <td class=<%=chBAN.getClass("Credit_Debit")%>>
			  <span style="VISIBILITY: <%=chBAN.getChargeFieldVisibility("Credit_Debit")%>"><b><small>Credit/Debit<br>
			    <%=DBA.getListBox("Credit_Debit",chBAN.getMode("Credit_Debit"),Credit_Debit,"Credit Debit")%>
			  </span>
		        </td>
		      </tr>
		      <tr>
		      </tr>
		      <tr>
		        <td valign="top" colspan=2 rowspan=2 class=<%=chBAN.getClass("Charge_Description")%>><b><small>Charge Description
			  <br><textarea class=tworows name="Charge_Description" cols="60" <%=chBAN.getMode("Charge_Description")%>><%=Charge_Description%></textarea>
			</td>
		        <td valign="top" class=<%=chBAN.getClass("From_Charge_Valid_Dateh")%>>Fixed Charge Valid From<br>
                          <%=HB.getDays("FixedChargeBAN", "From_Charge_Valid_Date", FCDay, false, chBAN.disableDate("From_Charge_Valid_Date", true), FCMonth, FCYear)%>
                          <%=HB.getMonths("FixedChargeBAN", "From_Charge_Valid_Date", FCMonth, false, chBAN.disableDate("From_Charge_Valid_Date", disableDate))%>
                          <%=HB.getYears("FixedChargeBAN", "From_Charge_Valid_Date", FCYear, false, chBAN.disableDate("From_Charge_Valid_Date", disableDate))%>
                          <input type="hidden" name="From_Charge_Valid_Dateh" value="<%=From_Charge_Valid_Date%>">
		        </td>
		        <td valign="top" class=<%=chBAN.getClass("Fixed_Charge_Type")%>><b><small>Charge Type<br>
			    <%=DBA.getListBox("Fixed_Charge_Type",chBAN.getMode("Fixed_Charge_Type"),Fixed_Charge_Type,"Fixed Charge Type")%>
		        </td>
		      </tr>
		      <tr>
		        <td valign="top" class=<%=chBAN.getClass("To_Charge_Valid_Dateh")%>>Fixed Charge Valid To<br>
                          <%=HB.getDays("FixedChargeBAN", "To_Charge_Valid_Date", TCDay, true, chBAN.disableDate("To_Charge_Valid_Date", true), TCMonth, TCYear)%>
                          <%=HB.getMonths("FixedChargeBAN", "To_Charge_Valid_Date", TCMonth, true, chBAN.disableDate("To_Charge_Valid_Date", disableDate))%>
                          <%=HB.getYears("FixedChargeBAN", "To_Charge_Valid_Date", TCYear, true, chBAN.disableDate("To_Charge_Valid_Date", disableDate))%>
                          <input type="hidden" name="To_Charge_Valid_Dateh" value="<%=To_Charge_Valid_Date%>">
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


