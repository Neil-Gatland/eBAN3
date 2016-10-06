<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.StringUtil"%>
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>
  <%!//Variables
    String SQL;
    String Global_Customer_Id,Global_Customer_Name,Invoice_Region,Billing_Start_Date,Billing_End_Date;
    String Contract_Number,C00_Number,Product_Type="",Account_Id,Split_Billing_Ind="N",C03_Number;
    String Contract_Date,Initial_Period_Days,Initial_Period_Months,VAT_Code="",Action="",BAN_Identifier="";
    String Fixed_Charge_Period,Invoice_Option,Bill_Option="",Billing_Frequency="",InputMode="",Mode="",SelectMode="",Calendar="";

    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
   %>
  <%//Initialization
    Global_Customer_Id=(String)session.getAttribute("Global_Customer_Id");
    Invoice_Region=(String)session.getAttribute("Invoice_Region");
    Account_Id=ctBAN.getAccount_Id();
    Global_Customer_Name=ctBAN.getGlobalCustomerName();
    BAN_Identifier=ctBAN.getBanIdentifier();

    Mode=ctBAN.getMode();
    Action=ctBAN.getAction();

    SelectMode=ctBAN.getSelectMode();
    InputMode=ctBAN.getInputMode();
    Calendar=ctBAN.getCalendar();

    //Get values from Bean
    //Circuit_Reference=ctBAN.getCircuit_Reference();
    Contract_Number = ctBAN.getContract_Number();
    C00_Number = ctBAN.getC00_Number();
    Invoice_Option=ctBAN.getInvoice_Option();
    Bill_Option=ctBAN.getBill_Option();
    Split_Billing_Ind=ctBAN.getSplit_Billing_Ind();
    Contract_Date=SU.isNull(ctBAN.getContract_Date(),"Today");
    Billing_Start_Date=SU.isNull(ctBAN.getBilling_Start_Date(),"Today");
    Billing_End_Date=SU.isNull(ctBAN.getBilling_End_Date(),"");

    Fixed_Charge_Period=ctBAN.getFixed_Charge_Period();
    Initial_Period_Days=ctBAN.getInitial_Period_Days();
    Initial_Period_Months=ctBAN.getInitial_Period_Months();
    Billing_Frequency=ctBAN.getBilling_Frequency();
    VAT_Code=ctBAN.getVAT_Code();
    C03_Number=ctBAN.getC03_Number();

    session.setAttribute("PageSent",request.getRequestURI());
    String Status = ctBAN.getBanStatus();
   %>
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%@ include file="../includes/Page_Header2.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
      <td colspan=2>
	<form name="CircuitBAN" method="post" action="CircuitBANHandler.jsp">
        <%=HB.getBANMenu_Bar("Circuit2BAN",Mode,Action,(String)session.getAttribute("System"),Status)%>

        <input name="ButtonPressed" type=hidden value="">

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
		    <h3 align="center"><%=Mode%> Circuit (Page 2 of 2)</h2>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(ctBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" height="341" colspan=2>
		    <%=session.getAttribute("Error")%>
		    <%session.setAttribute("Error","");%>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td width=50%><b>Customer:</b></td>
			<td class=bluebold><%=Global_Customer_Name%></td>
			<td width=50%><b>CCD Account Number</b></td>
			<td class=bluebold><%=Account_Id%></td>
		      </tr>
		      <tr>
			<td><b>Billing Advice Status</td>
			<td class=bluebold><%=Status%></td>
			<td><b>Requested By</td>
			<td class=bluebold>
			  <%=SU.isNull(ctBAN.getBanCreatedBy(),(String)session.getAttribute("User_Name"))%><br>
			</td>
		      </tr>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
			<td colspan=4 class=bluebold>
			  Initial Installation Details
			</td>
		      </tr>
		      <tr>
			<td class=<%=ctBAN.getClass("Contract_Number")%>>
			  <b><small>Contract Number
		          <br><input class=inp type=text name="Contract_Number" value="<%=Contract_Number%>" <%=InputMode%>>
		        </td>
		        <td class=<%=ctBAN.getClass("C00_Number")%>>
			  <b><small>C00 Number
		          <br><input class=inp type=text name="C00_Number" value="<%=C00_Number%>" <%=InputMode%>>
		        </td>
			<td class=<%=ctBAN.getClass("Contract_Dateh")%>>Contract Date<br>
			  <input class=inp type="text" name="Contract_Date" READONLY value=<%=Contract_Date%>>
			  <a <%=Calendar%>href="javascript:show_calendar('CircuitBAN.Contract_Date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			  <img src="../nr/cw/newimages/show_calendar.gif" border=0></a>
			  <input type=hidden name="Contract_Dateh" value=<%=Contract_Date%>>
		        </td>
		      </tr>
		      <!--
		      <tr>
			<td class=<%=ctBAN.getClass("Initial_Period_Days")%>>Initial Period Days
			  <br><input class=inp type=text name="Initial_Period_Days" value="<%=Initial_Period_Days%>" <%=InputMode%>>
			<td class=<%=ctBAN.getClass("Initial_Period_Months")%>>Initial Period Months
			  <br><input class=inp type=text name="Initial_Period_Months" value="<%=Initial_Period_Months%>" <%=InputMode%>>
			<td class=<%=ctBAN.getClass("Fixed_Charge_Period")%>>
			  <b><small>Fixed Charge Period
			  <br><input class=inp type=text name="Fixed_Charge_Period" value="<%=Fixed_Charge_Period%>" <%=InputMode%>>
			</td>
			<td class=<%=ctBAN.getClass("VAT_Code")%>>
			  <b><small>VAT Code
			  <br><%=DBA.getListBox("VAT_Code",InputMode,VAT_Code,"")%>
			</td>
		      </tr>-->
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
			<td colspan=4 class=bluebold>
			  Billing Details
			</td>
		      </tr>
		      <tr>
			<td class=<%=ctBAN.getClass("Billing_Start_Dateh")%>><b><small>Billing Start Date<br>
			  <input class=inp type="text" name="Billing_Start_Date" READONLY value=<%=Billing_Start_Date%>>
			  <a <%=Calendar%>href="javascript:show_calendar('CircuitBAN.Billing_Start_Date');"
			    onmouseover="window.status='Date Picker';return true;"
			    onmouseout="window.status='';return true;">
			  <img src="../nr/cw/newimages/show_calendar.gif" border=0></a>
			  <input type=hidden name="Billing_Start_Dateh" value=<%=Billing_Start_Date%>>
			</td>
			<td class=<%=ctBAN.getClass("Billing_End_Dateh")%>><b><small>Billing End Date<br>
			  <input class=inp type="text" name="Billing_End_Date" READONLY value=<%=Billing_End_Date%>>
			  <a <%=ctBAN.getMode("Billing_End_Date")%>href="javascript:show_calendar('CircuitBAN.Billing_End_Date');"
			    onmouseover="window.status='Date Picker';return true;"
			    onmouseout="window.status='';return true;">
			  <img src="../nr/cw/newimages/show_calendar.gif" border=0></a>
			  <input type=hidden name="Billing_End_Dateh" value=<%=Billing_End_Date%> <%=InputMode%>>
			</td>
			<td class=<%=ctBAN.getClass("VAT_Code")%>>
			  <b><small>VAT Code
			  <br><%=DBA.getListBox("VAT_Code",InputMode,VAT_Code,"")%>
			</td>
			<td class=<%=ctBAN.getClass("C03_Number")%>><b><small>C03 Number<br>
			  <input class=inp type="text" name="C03_Number" value="<%=C03_Number%>" <%=ctBAN.getMode("C03_Number")%>>
			</td>
		      </tr>
		      <tr>
			<td class=<%=ctBAN.getClass("Invoice_Option")%><b><b><small>Invoice Option
			  <br>
			  <%=DBA.getListBox("Invoice_Option",InputMode,Invoice_Option,"")%>
			</td>
			<td class=<%=ctBAN.getClass("Bill_Option")%>><b><small>Bill Option
			  <br>
			  <%=DBA.getListBox("Bill_Option",InputMode,Bill_Option,"")%>
			</td>
			<td class=<%=ctBAN.getClass("Split_Billing_Ind")%>>
			<b><small>Split Billing<br>
			  <%=DBA.getListBox("Split_Billing",InputMode,Split_Billing_Ind,"")%>
			</td>
			<td class=<%=ctBAN.getClass("Billing_Frequency")%>>
			<b><small>Billing Frequency<br>
			  <%=DBA.getListBox("Billing_Frequency",InputMode,Billing_Frequency,"")%> </td>
			<td></td>
		      </tr>
		      <tr>
			<td colspan=5>
			</td>
		      </tr>
		      <tr>
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


