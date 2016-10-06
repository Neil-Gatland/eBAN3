<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="JavaUtil.StringUtil"%>
<jsp:useBean id="cuBAN" class="DBUtilities.OSSCustomerBANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

  <%!//Variables
    String BAN_Identifier,Global_Customer_Id,Global_Customer_Name,Global_Account_Manager,Invoice_Region;
    String Customer_Billing_Name,Customer_Contact_Name,Customer_Billing_Address;
    String Required_BAN_Effective_Date,BAN_Summary,BAN_Reason,Account_Id,Customer_Contact,Currency_Desc;
    String SelectMode="",Calendar="",InputMode="",Mode="",Action="";

    //HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
   %>
  <%//Initialization
    //Get values from last submission
    Global_Customer_Name=cuBAN.getGlobalCustomerName();
    BAN_Summary = cuBAN.getBAN_Summary();
    BAN_Reason = cuBAN.getBAN_Reason();
    Global_Account_Manager=cuBAN.getGlobal_Account_Manager();
    Required_BAN_Effective_Date=SU.isNull(cuBAN.getRequired_BAN_Effective_Date(),"Today");
    Global_Customer_Id=cuBAN.getGlobalCustomerId();
    Account_Id=cuBAN.getAccount_Id();

    Customer_Contact=cuBAN.getCustomer_Contact();
    Customer_Contact_Name=cuBAN.getCustomer_Contact_Name();
    Customer_Billing_Address=cuBAN.getCustomer_Billing_Address();
    Customer_Billing_Name=cuBAN.getCustomer_Billing_Name();
    Invoice_Region=cuBAN.getInvoice_Region();
    Currency_Desc = SU.isBlank(cuBAN.getCurrency_Desc(),"GBP");

    Mode=cuBAN.getMode();

    SelectMode=cuBAN.getSelectMode();
    InputMode=cuBAN.getInputMode();
    Calendar=cuBAN.getCalendar();

    BAN_Identifier=cuBAN.getBanIdentifier();
    Action=cuBAN.getAction();
    cuBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
    String Status = cuBAN.getBanStatus();
   %>

   <table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td>
      <%@ include file="../includes/Page_Header2.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
      <td>
	<form name="CustomerBAN" method="post" action="CustomerBANHandler.jsp">
        <%=HB.getBANMenu_Bar("BAN",Mode,Action,(String)session.getAttribute("System"),cuBAN.getIsDirect())%>
        <input name="ButtonPressed" type=hidden value="">
      </td>
    </tr>
     <tr>
      <td>
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
		    <h3 align="center"><%=Mode%> a Customer Account</h2>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(cuBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" colspan=2>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td><b>Customer:</b></td>
			<td colspan=2class=bluebold><%=Global_Customer_Name%></td>
		      </tr>
		      <tr>
			<td height="22" class=optional>Billing Advice Status</td>
			<td height="22" class=bluebold><%=Status%></td>
			<td class=optional>Requested By</td>
			<td class=bluebold><%=session.getAttribute("User_Name")%></td>
		      </tr>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
			<td class=<%=cuBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
		          <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=cuBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
		        </td>
		        <td class=<%=cuBAN.getClass("BAN_Reason")%>>
			  <b><small>BAN Reason
		          <br><textarea class=tworows name="BAN_Reason" cols="40" rows="2" <%=cuBAN.getMode("BAN_Reason")%>><%=BAN_Reason%></textarea>
		        </td>
		        <td>
		        </td>
		        <td class=<%=cuBAN.getClass("Required_BAN_Effective_Dateh")%>>BAN Effective Date<br>
			  <input type="text" name="Required_BAN_Effective_Date" READONLY value="<%=Required_BAN_Effective_Date%>">
			  <a <%=Calendar%>href="javascript:show_calendar('CustomerBAN.Required_BAN_Effective_Date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			  <img src="../nr/cw/newimages/show_calendar.gif" border=0></a>
			  <input type=hidden name="Required_BAN_Effective_Dateh" value=<%=Required_BAN_Effective_Date%>>
			</td>
		      </tr>
		      <tr>
			<!--<td height="18" class=<%=cuBAN.getClass("Global_Customer_Name")%>>
			<span style="VISIBILITY: <%=cuBAN.getCustomerVisibility("Customer")%>">
			    <b><small>Customer Name:
			  <br><input class=longinp type=text name="Global_Customer_Name" maxlength=50 value="<%=Global_Customer_Name%>" READONLY>
			</td>-->
			<td class=<%=cuBAN.getClass("Account_Id")%>>
			<span style="VISIBILITY: <%=cuBAN.getCustomerVisibility("Account")%>">
			    <b><small>CCD Account Number<br>
			    <input class=inp type=text name="Account_Id" maxlength=10 value="<%=Account_Id%>" <%=cuBAN.getMode("Account_Id")%>>
			</span>
			</td>
			<td  class=<%=cuBAN.getClass("Invoice_Region")%>><b><small>Customer Account Name
			  <br><input class=longinp type=text name="Invoice_Region" maxlength=50 value="<%=Invoice_Region%>" <%=InputMode%>>
			</td>
			<td  class=<%=cuBAN.getClass("Customer_Billing_Name")%>><b><small>Customer Billing Name
			  <br><input class=longinp type=text name="Customer_Billing_Name" maxlength=50 value="<%=Customer_Billing_Name%>" <%=InputMode%>>
			</td>
		      </tr>
		      <tr>
			<td class=<%=cuBAN.getClass("Customer_Contact")%>><b><small>C&W Contact Point<br>
			  <input class=longinp type=text name="Customer_Contact" maxlength=50 value="<%=Customer_Contact%>" <%=InputMode%>>
			</td>
			<td class=<%=cuBAN.getClass("Customer_Contact_Name")%>>
			  <b><small>Customer Contact Name
			  <br><input class=longinp type=text name="Customer_Contact_Name" maxlength=50 value="<%=Customer_Contact_Name%>" <%=InputMode%>>
			</td>
			<td  class=<%=cuBAN.getClass("Global_Account_Manager")%>><b><small>Account Manager
			  <br><input class=inp type=text name="Global_Account_Manager" width=50 maxlength=50 value="<%=Global_Account_Manager%>" <%=InputMode%>>
			</td>
		      </tr>
		      <tr>
			<td colspan=2 class=<%=cuBAN.getClass("Customer_Billing_Address")%>>
			<b><small>Customer Billing Address<br>
			<textarea class=tworows name="Customer_Billing_Address" cols=50 <%=InputMode%>><%=Customer_Billing_Address%></textarea></td>
			</td>
		        <td class=<%=cuBAN.getClass("Currency_Desc")%>><b><small>Billing Currency<br>
			  <%=DBA.getListBox("Currency_Desc",InputMode,Currency_Desc,"")%>
			</td>
		      </tr>
		      <tr>
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
     <tr>
      <td height="100">&nbsp
      </td>
    </tr>
    <!--Footer-->
    <tr>
      <td>
        <%@ include file="../includes/Footer.htm"%>
      </td>
    </tr>
  </table><!--1-->
</body>
</html>