<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="HTMLUtil.HTMLBean,JavaUtil.StringUtil"%>
<jsp:useBean id="caBAN" class="DBUtilities.CarrierBANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

  <%!//Variables

    String BAN_Identifier,Carrier_Contact_Name,Currency_Desc,Comments,Carrier_Country;
    String Carrier_Name,VAS_List_No,Bank_Account_No,Carrier_Billing_Address;
    String Required_BAN_Effective_Date,BAN_Summary,BAN_Reason,Bank_Address,Local_Tax_Rate;
    String General_Ledger_Code,Tax_Precision;
    String SelectMode="",Calendar="",InputMode="",Mode="",Action="";

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
   %>
  <%//Initialization
    //Get values from last submission
    Currency_Desc=caBAN.getCurrency_Desc();
    BAN_Summary = caBAN.getBAN_Summary();
    BAN_Reason = caBAN.getBAN_Reason();
    Comments=caBAN.getComments();
    Required_BAN_Effective_Date=SU.isNull(caBAN.getRequired_BAN_Effective_Date(),"Today");
    Carrier_Contact_Name=caBAN.getCarrier_Contact_Name();
    Bank_Address=caBAN.getBank_Address();

    Local_Tax_Rate=caBAN.getLocal_Tax_Rate();
    Bank_Account_No=caBAN.getBank_Account_No();
    Carrier_Name=caBAN.getCarrier_Name();
    Carrier_Billing_Address=caBAN.getCarrier_Billing_Address();
    VAS_List_No=caBAN.getVAS_List_No();
    Carrier_Country=caBAN.getCarrier_Country();
    General_Ledger_Code=caBAN.getGeneral_Ledger_Code();
    Tax_Precision=caBAN.getTax_Precision();

    Mode=caBAN.getMode();

    SelectMode=caBAN.getSelectMode();
    InputMode=caBAN.getInputMode();
    Calendar=caBAN.getCalendar();

    BAN_Identifier=caBAN.getBanIdentifier();
    Action=caBAN.getAction();
    caBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
    String Status = caBAN.getBanStatus();
   %>

   <table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td>
      <%@ include file="../includes/Page_Header2.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
      <td>
	<form name="CarrierBAN" method="post" action="CarrierBANHandler.jsp">
        <%=HB.getBANMenu_Bar("BAN",Mode,Action,(String)session.getAttribute("System"),caBAN.getIsDirect())%>
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
		  <td valign="top" colpspan=2>
		    <h3 align="center"><%=Mode%> a Carrier</h3>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(caBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" colspan=2>
		    <%=SU.isNull(caBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" id=4>
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
			<td class=<%=caBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
		          <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=caBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
		        </td>
		        <td class=<%=caBAN.getClass("BAN_Reason")%>>
			  <b><small>BAN Reason
		          <br><textarea class=tworows name="BAN_Reason" cols="40" rows="2" <%=caBAN.getMode("BAN_Reason")%>><%=BAN_Reason%></textarea>
		        </td>
		        <td>&nbsp
			</td>
		        <td class=<%=caBAN.getClass("Required_BAN_Effective_Dateh")%>>BAN Effective Date<br>
			  <input type="text" name="Required_BAN_Effective_Date" READONLY value="<%=Required_BAN_Effective_Date%>">
			  <a <%=Calendar%>href="javascript:show_calendar('CarrierBAN.Required_BAN_Effective_Date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			  <img src="../nr/cw/newimages/show_calendar.gif" border=0></a>
			  <input type=hidden name="Required_BAN_Effective_Dateh" value=<%=Required_BAN_Effective_Date%>>
			</td>
		      </tr>
		      <tr>
			<td class=<%=caBAN.getClass("Carrier_Name")%>><b><small>Carrier Name
			  <br><input class=longinp type=text name="Carrier_Name" width=40 maxlength=50 value="<%=Carrier_Name%>" <%=caBAN.getMode("Carrier_Name")%>>
			</td>
			<td class=<%=caBAN.getClass("Carrier_Contact_Name")%>><b><small>Carrier Contact Name
			  <br><textarea class=tworows name="Carrier_Contact_Name" cols="40" rows="2" <%=InputMode%>><%=Carrier_Contact_Name%></textarea>
			</td>
			<td class=<%=caBAN.getClass("Carrier_Country")%>><b><small>Country
			  <br><%=DBA.getListBox("Carrier_Country",InputMode,Carrier_Country,"")%></td>
			</td>
		        <td class=<%=caBAN.getClass("Currency_Desc")%>><b><small>Currency<br>
			  <%=DBA.getListBox("Currency_Desc",InputMode,Currency_Desc,"")%>
			</td>
		      </tr>
		      <tr>
			<td class=<%=caBAN.getClass("Carrier_Billing_Address")%> align=top>
			  <b><small>Billing Address<br>
			  <textarea class=tworows name="Carrier_Billing_Address" cols=40 <%=InputMode%>><%=Carrier_Billing_Address%></textarea>
			</td>
			<td class=<%=caBAN.getClass("Bank_Account_No")%> valign=top><b><small>Bank Account Number
			  <br><input class=inp type=text name="Bank_Account_No" width=20 maxlength=50 value="<%=Bank_Account_No%>" <%=InputMode%>>
			</td>
			<td class=<%=caBAN.getClass("Bank_Address")%>>
			  <b><small>Bank Address<br>
			  <textarea class=tworows name="Bank_Address" cols=40 <%=InputMode%>><%=Bank_Address%></textarea></td>
			</td>
			<td  class=<%=caBAN.getClass("General_Ledger_Code")%> valign=top><b><small>General Ledger Code
			  <br><input class=inp type=text name="General_Ledger_Code" width=40 maxlength=50 value="<%=General_Ledger_Code%>" <%=InputMode%>>
			</td>
		      </tr>
		      <tr>
			<td  class=<%=caBAN.getClass("Comments")%>><b><small>Comments
			  <br><textarea class=tworows name="Comments" cols=40 <%=InputMode%>><%=Comments%></textarea>
			</td>
			<td class=<%=caBAN.getClass("Local_Tax_Rate")%> valign=top><b><small>Local Tax Rate<br>
			  <input class=inp type=text name="Local_Tax_Rate" width=30 maxlength=10 value="<%=Local_Tax_Rate%>" <%=InputMode%>>
			</td>
			<td class=<%=caBAN.getClass("Tax_Precision")%> valign=top><b><small>Tax Precision
			  <br><%=DBA.getListBox("Tax_Precision",InputMode,Tax_Precision,"")%></td>
			</td>
			<td  class=<%=caBAN.getClass("VAS_List_No")%> valign=top><b><small>VAS List No
			  <br><input class=inp type=text name="VAS_List_No" width=40 maxlength=50 value="<%=VAS_List_No%>" <%=InputMode%>>
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