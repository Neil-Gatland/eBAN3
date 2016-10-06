<html>
<jsp:include page="../includes/Page_Header5.jsp" flush="true" />
<%@ page import="JavaUtil.StringUtil"%>
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
  <%!//Variables
    String SQL;
    String Global_Customer_Id,Global_Customer_Name,Invoice_Region,Circuit_Reference,GCB_Product_Type="";
    String Speed,Carrier="",Customer_Reference="",Action="",InputMode="",Mode="",SelectMode="",Calendar="";
    String Circuit_Status="Live",BAN_Summary,BAN_Reason,Product_Type="",Account_Id,Billed_By="";
    String Required_BAN_Effective_Date,Billing_Start_Date,Billing_End_Date,Bill_Option,GCD_Id;
    String Circuit_Description,Circuit_Int_Designator,BAN_Identifier="";
    String FromEnd_Site_Reference="",FromEnd_Site_Address="",FromEnd_City="",FromEnd_Country="";
    String ToEnd_Site_Reference="",ToEnd_Site_Address="",ToEnd_City="",ToEnd_Country="";
    int BEDay, BEMonth, BEYear, BiSDay, BiSMonth, BiSYear, BiEDay, BiEMonth, BiEYear;
    String C2_Ref_No, C3_Ref_No;

    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
   %>
  <%//Initialization
    Global_Customer_Id=ctBAN.getGlobalCustomerId();
    Invoice_Region=(String)session.getAttribute("Invoice_Region");
    Account_Id=ctBAN.getAccount_Id();
    Global_Customer_Name=ctBAN.getGlobalCustomerName();

    BAN_Identifier=ctBAN.getBanIdentifier();
    Action=ctBAN.getAction();

    Mode=ctBAN.getMode();

    SelectMode=ctBAN.getSelectMode();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=ctBAN.getInputMode();
    Calendar=ctBAN.getCalendar();

    //Get values from last submission
    BAN_Summary = ctBAN.getBAN_Summary();
    BAN_Reason = ctBAN.getBAN_Reason();
    GCB_Product_Type=ctBAN.getGCB_Product_Type();
    Product_Type=ctBAN.getProduct_Type();
    Circuit_Status=ctBAN.getCircuit_Status();
    Speed=ctBAN.getSpeed();
    Required_BAN_Effective_Date=SU.isNull(ctBAN.getRequired_BAN_Effective_Date(),"Today");
    Circuit_Reference=ctBAN.getCircuit_Reference();
    Customer_Reference=ctBAN.getCustomer_Reference();
    Circuit_Description=ctBAN.getCircuit_Description();
    Circuit_Int_Designator=ctBAN.getCircuit_Int_Designator();
    Carrier=ctBAN.getCarrier();
    FromEnd_Site_Reference=ctBAN.getFromEnd_Site_Reference();
    FromEnd_Site_Address=ctBAN.getFromEnd_Site_Address();
    FromEnd_Country=ctBAN.getFromEnd_Country();
    FromEnd_City=ctBAN.getFromEnd_City();
    ToEnd_Site_Reference=ctBAN.getToEnd_Site_Reference();
    ToEnd_Site_Address=ctBAN.getToEnd_Site_Address();
    ToEnd_Country=ctBAN.getToEnd_Country();
    ToEnd_City=ctBAN.getToEnd_City();
    Billed_By=ctBAN.getBilled_By();
    //session.setAttribute("PageSent",request.getRequestURI());
    String Status = ctBAN.getBanStatus();
    Billing_Start_Date=SU.isNull(ctBAN.getBilling_Start_Date(),"FirstOfMonth");
    Billing_End_Date=SU.isNull(ctBAN.getBilling_End_Date(),"");
    Bill_Option=ctBAN.getBill_Option();
    GCD_Id=ctBAN.getDivision_Id();
    BEDay=ctBAN.getBANEffectiveDateDays();
    BEMonth=ctBAN.getBANEffectiveDateMonths();
    BEYear=ctBAN.getBANEffectiveDateYears();
    BiSDay=ctBAN.getBillingStartDateDays();
    BiSMonth=ctBAN.getBillingStartDateMonths();
    BiSYear=ctBAN.getBillingStartDateYears();
    BiEDay=ctBAN.getBillingEndDateDays();
    BiEMonth=ctBAN.getBillingEndDateMonths();
    BiEYear=ctBAN.getBillingEndDateYears();
    String speedSelection = GCB_Product_Type.equals("LLUF")?
      DBA.getListBox("Circuit_Speed",InputMode,Speed," "):
      ("<input class=inp type=text name=\"Circuit_Speed\" width=200 <" +
      InputMode + " value=\"" + Speed + "\">");
    C2_Ref_No=ctBAN.getC2RefNo();
    C3_Ref_No=ctBAN.getC3RefNo();
   %>
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
      <td colspan=2>
	<form name="CircuitBAN" id="CircuitBAN" method="post" action="CircuitBANHandler.jsp">
        <%=HB.getBANMenu_Bar("CircuitBAN",Mode,Action,(String)session.getAttribute("System"),ctBAN.getIsDirect())%>
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
	      <span id=formspan>
	      <table width="100%" border="0" cellpadding="0" cellspacing="0" id=3>
		<tr>
		  <td valign="top" colspan=2>
		    <h3 align="center"><%=Mode%> a Circuit</h2>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(ctBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" height="341" colspan=2>
		    <%=session.getAttribute("Error")%>
		    <%session.setAttribute("Error","");%>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" id=4>
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
			      <%=SU.isNull(ctBAN.getBanCreatedBy(),(String)session.getAttribute("User_Name"))%><br>
			    </td>
			  </tr>
			</table>
		      </tr>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
			<td width="25%" class=<%=ctBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
			  <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=ctBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
			</td>
                        <td>
                          <input type="hidden" name="Required_BAN_Effective_Dateh" value="<%=Required_BAN_Effective_Date%>">
			</td>
<!--
		        <td width="50%" colspan="2" valign="top" class=<%=ctBAN.getClass("Required_BAN_Effective_Dateh")%>>Effective Date<br>
                          <%=HB.getDays("CircuitBAN", "Required_BAN_Effective_Date", BEDay, false, disableDate)%>
                          <%=HB.getMonths("CircuitBAN", "Required_BAN_Effective_Date", BEMonth, false, disableDate)%>
                          <%=HB.getYears("CircuitBAN", "Required_BAN_Effective_Date", BEYear, false, disableDate)%>
		        </td>
-->
		      </tr>
		      <tr>
			<td width="50%" colspan="2" class=<%=ctBAN.getClass("Circuit_Reference")%>>GSR / Circuit Reference
			  <br><input class=inp style="width:400px" type=text name="Circuit_Reference" value="<%=Circuit_Reference%>" <%=ctBAN.getMode("Circuit_Reference")%> onChange="this.value=this.value.toUpperCase();setSiteRefs(this.value)">
			</td>
			<td width="50%" colspan="2" class=<%=ctBAN.getClass("Customer_Reference")%>>Customer Reference
			  <br><input class=inp style="width:400px" type=text name="Customer_Reference" value="<%=Customer_Reference%>" <%=InputMode%>>
			</td>
		      </tr>
		      <tr>
			<td width="50%" class=<%=ctBAN.getClass("Circuit_Description")%> colspan="2">
			  <b><small>Circuit Description
			  <br><input class=inp style="width:400px" type=text name="Circuit_Description" width=200 <%=InputMode%> value="<%=Circuit_Description%>">
			</td>
			<td class=<%=ctBAN.getClass("Circuit_Speed")%>>
			  <b><small>Circuit Speed
			  <br><%=speedSelection%>
			</td>
		      </tr>
		      <tr>
			<td width="50%" class=<%=ctBAN.getClass("GCD_Id")%> colspan=2><b><small>Global Customer Division Id
			  <br>
			  <%=DBA.getListBox("GCD_Id",InputMode,GCD_Id,Global_Customer_Id)%>
			</td>
			<td width="25%" class=<%=ctBAN.getClass("GCB_Product_Type")%> colspan=2><b><small>Product Type
			  <br>
			  <%=DBA.getListBox("GCB_Product_Type",SelectMode,GCB_Product_Type,"x",
                            1," style=\"width:85%;\"",true)%>
			</td>
		      </tr>
		      <tr>
			<td colspan="2" class=<%=ctBAN.getClass("FromEnd_Site_Reference")%> colspan=2><b><small>From Site<br>
			  <%=DBA.getListBox("FromEnd_Site_Reference",InputMode,FromEnd_Site_Reference,Global_Customer_Id,
                            1," style=\"width:85%;\"",true)%>
			</td>
			<td colspan="2" class=<%=ctBAN.getClass("ToEnd_Site_Reference")%> colspan=2><b><small>To Site<br>
			  <%=DBA.getListBox("ToEnd_Site_Reference",InputMode,ToEnd_Site_Reference,Global_Customer_Id,
                            1," style=\"width:85%;\"",true)%>
			</td>
		      </tr>
		      <tr>
		        <td class=<%=ctBAN.getClass("Billing_Start_Dateh")%>>Billing Start Date<br>
                          <%=HB.getDays("CircuitBAN", "Billing_Start_Date", BiSDay, false, disableDate, BiSMonth, BiSYear)%>
                          <%=HB.getMonths("CircuitBAN", "Billing_Start_Date", BiSMonth, false, disableDate)%>
                          <%=HB.getYears("CircuitBAN", "Billing_Start_Date", BiSYear, false, disableDate)%>
                          <input type="hidden" name="Billing_Start_Dateh" value="<%=Billing_Start_Date%>">
		        </td>
		        <td class=<%=ctBAN.getClass("Billing_End_Dateh")%>>Billing End Date<br>
                          <%=HB.getDays("CircuitBAN", "Billing_End_Date", BiEDay, true, disableDate, BiEMonth, BiEYear)%>
                          <%=HB.getMonths("CircuitBAN", "Billing_End_Date", BiEMonth, true, disableDate)%>
                          <%=HB.getYears("CircuitBAN", "Billing_End_Date", BiEYear, true, disableDate)%>
                          <input type="hidden" name="Billing_End_Dateh" value="<%=Billing_End_Date%>">
		        </td>
			<td class=<%=ctBAN.getClass("Bill_Option")%>><b><small>Bill Option
			  <br>
			  <%=DBA.getListBox("Bill_Option",InputMode,Bill_Option,"")%>
			</td>
		      </tr>
		      <tr>
			<td colspan="2" class=<%=ctBAN.getClass("Carrier")%> colspan=2><b><small>Carrier<br>
			  <%=DBA.getListBox("Carrier",InputMode,Carrier,"",
                            1," style=\"width:85%;\"",true)%>
			</td>
		      </tr>
                      <%if (GCB_Product_Type.equals("LLUF"))
                        {%>
		      <tr>
		        <td width="50%" colspan="2" class=<%=ctBAN.getClass("C2_Ref_No")%>><b><small>C2 Ref No<br>
			  <input class=inp style="width:400px" type="text" name="C2_Ref_No" maxlength="25" size="50" value="<%=C2_Ref_No%>" <%=ctBAN.getMode("C2_Ref_No")%>>
			</td>
		        <td width="50%" colspan="2" class=<%=ctBAN.getClass("C3_Ref_No")%>><b><small>C3 Ref No<br>
			  <input class=inp style="width:400px" type="text" name="C3_Ref_No" maxlength="25" size="50" value="<%=C3_Ref_No%>" <%=ctBAN.getMode("C3_Ref_No")%>>
			</td>
		      </tr>
                      <%}%>
		    </table><!--4-->
		    </span>
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


