<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="nosaBAN" class="DBUtilities.NostroAccountBANBean" scope="session"/>

<%! String SQL;
    String ActionCustomer="";
    String Message;
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    DBAccess DBA = new DBAccess();
    String PageSent,SelectMode,InputMode,BAN_Identifier;
    String Product,Global_Customer_Id,Global_Customer_Name;
    String BAN_Summary,BAN_Reason,Product_Type="";
    String Required_BAN_Effective_Date;
    String Nostro_Reference="",Account_Name="",Primary_Account="";
    int BEDay, BEMonth, BEYear;
    String Bill_Address_1="",Bill_Address_2="",Bill_Address_3="",
      Bill_Address_4="",Bill_Address_Country="",Account_Invoice_Currency="";
    String Billing_Customer_Name="",Billing_Customer_Contact_Name="",
      Primary_Contract_Reference="";

%>
<%//Set Initial values
    String Mode=nosaBAN.getMode().equals("Add")?"Initiate":nosaBAN.getMode();
    String Action=nosaBAN.getAction();
    SelectMode=nosaBAN.getSelectMode();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=nosaBAN.getInputMode();
    Nostro_Reference=nosaBAN.getNostroReference();
    //Message=nosaBAN.getMessage();

  //Are we coming from the handler page, or a fresh start?

  //Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
    //Message=SU.isNull((String)session.getAttribute("Message"),"");
    //PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
    Global_Customer_Id=nosaBAN.getGlobalCustomerId();
    Global_Customer_Name=nosaBAN.getGlobalCustomerName();
    BAN_Summary = nosaBAN.getBAN_Summary();
    BAN_Reason = nosaBAN.getBAN_Reason();
    BEDay = nosaBAN.getBANEffectiveDateDays();
    BEMonth = nosaBAN.getBANEffectiveDateMonths();
    BEYear = nosaBAN.getBANEffectiveDateYears();
    Required_BAN_Effective_Date=SU.isNull(nosaBAN.getRequired_BAN_Effective_Date(),"Today");
    Account_Name=nosaBAN.getAccountName();
    Primary_Account=nosaBAN.getPrimaryAccount();
    Bill_Address_1=nosaBAN.getBillAddress1();
    Bill_Address_2=nosaBAN.getBillAddress2();
    Bill_Address_3=nosaBAN.getBillAddress3();
    Bill_Address_4=nosaBAN.getBillAddress4();
    Bill_Address_Country=nosaBAN.getBillAddressCountry();
    Account_Invoice_Currency=nosaBAN.getAccountInvoiceCurrency();
    Billing_Customer_Name=nosaBAN.getBillingCustomerName();
    Billing_Customer_Contact_Name=nosaBAN.getBillingCustomerContactName();
    Primary_Contract_Reference=nosaBAN.getPrimaryContractReference();
%>
  <a name="top"></a>
  <table id=1 width ="100%">
    <tr>
      <td colspan=3>
        <%@ include file="../includes/Page_Header4.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
        <form name="AccountInitBAN" method="post" action="AccountInitBANHandler.jsp">
        <%=HB.getBANMenu_Bar("BAN",Mode,Action,(String)session.getAttribute("System"))%>
        <input name="ButtonPressed" type=hidden value="">
        <input name="RejectReason" type=hidden value="">
      </td>
    </tr>
    <tr>
      <td colspan=3>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
	    <td width="13">&nbsp;
	    <!-- this is a spacer column-->
	    </td>
	    <td valign="top" align="left" NOWRAP width=10>
	    <!--former menu column-->
	    </td>
	    <td width="13">&nbsp;</td>
	    <td  valign ="Top"><!-- Main table starts here-->
	      <table width="100%" border="0" cellpadding="0" cellspacing="0" id=3>
		<tr>
		  <td valign="top" colspan=2>
		    <h3 align="center"><%=Mode%> an Account</h3>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(nosaBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" height="341" colspan=2>
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td colspan=4>
			  <table width=100%>
                      <!--
                      <tr>
                        <td><b>Product:</b></td>
                        <td class=bluebold><%=nosaBAN.getProductName()%></td>
                        <td cosplan=2>&nbsp;</td>
                      </tr>
                      -->
                      <tr>
                        <td><b>Customer:</b></td>
                        <td class=bluebold><%=Global_Customer_Name%></td>
                        <td><b>Billing Customer Reference:</b></td>
                        <td class=bluebold><%=Global_Customer_Id%></td>
                      </tr>
                      <tr>
                        <td><b>Billing Advice Status:</td>
                        <td class=bluebold><%=nosaBAN.getBanStatus()%></td>
                        <td><b>Requested By:</td>
                        <td class=bluebold>
                          <%=SU.isNull(nosaBAN.getBanCreatedBy(),(String)session.getAttribute("User_Name"))%><br>
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
                  <td class=<%=nosaBAN.getClass("BAN_Summary")%>>
                    <b><small>BAN summary
                    <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=nosaBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
                  </td>
                  <!--
                  <td class=<%=nosaBAN.getClass("BAN_Reason")%>>
                    <b><small>BAN Reason
                    <br><textarea class=tworows name="BAN_Reason" cols="40" rows="2" <%=nosaBAN.getMode("BAN_Reason")%>><%=BAN_Reason%></textarea>
                  </td>
                  -->
                  <td class=<%=nosaBAN.getClass("BANEffectiveDateh")%>>BAN Effective Date<br>
                    <%=HB.getDays("AccountInitBAN", "BANEffectiveDate", BEDay, false, disableDate)%>
                    <%=HB.getMonths("AccountInitBAN", "BANEffectiveDate", BEMonth, false, disableDate)%>
                    <%=HB.getYears("AccountInitBAN", "BANEffectiveDate", BEYear, false, disableDate)%>
                    <input type="hidden" name="BANEffectiveDateh" value="<%=Required_BAN_Effective_Date%>">
                  </td>
                  <td>
                    <span style="VISIBILITY: <%=nosaBAN.getRejectVisibility()%>">
                      <b><small><%=nosaBAN.getBanStatus()%> Reason
                      <br><textarea class=red cols="40" rows="2" READONLY><%=nosaBAN.getRejectReason()%></textarea>
                    </span>
                  </td>
                  <td>&nbsp;</td>
                </tr>
		<tr>
                  <td class=<%=nosaBAN.getClass("Nostro_Reference")%>>
		    <b><small>Sales Order Number<br>
        	    <%=DBA.getListBox("Nostro_Reference",InputMode,Nostro_Reference,Global_Customer_Id)%>
                  </td>
                  <td>&nbsp;</td>
                  <td class=<%=nosaBAN.getClass("Bill_Address_1")%>>
		    <b><small>Billing Customer Address Line 1<br>
		    <input class=inp type=text name="Bill_Address_1" size="35"
                      value="<%=Bill_Address_1%>" <%=InputMode%>>
                  </td>
                </tr>
		<tr>
                  <td class=<%=nosaBAN.getClass("Account_Name")%>>
		    <b><small>Account Name<br>
		    <input class=inp type=text name="Account_Name" size="35"
                      value="<%=Account_Name%>" <%=InputMode%>>
                  </td>
                  <td class=<%=nosaBAN.getClass("Primary_Account")%>>
		    <b><small>Primary Account?<br>
        	    <%=DBA.getListBox("Primary_Account",InputMode,Primary_Account,"")%>
                  </td>
                  <td class=<%=nosaBAN.getClass("Bill_Address_2")%>>
		    <b><small>Billing Customer Address Line 2<br>
		    <input class=inp type=text name="Bill_Address_2" size="35"
                      value="<%=Bill_Address_2%>" <%=InputMode%>>
                  </td>
                </tr>
		<tr>
                  <td class=<%=nosaBAN.getClass("Billing_Customer_Name")%>>
		    <b><small>Billing Customer Name<br>
		    <input class=inp type=text name="Billing_Customer_Name" size="35"
                      value="<%=Billing_Customer_Name%>" <%=InputMode%>>
                  </td>
                  <td class=<%=nosaBAN.getClass("Billing_Customer_Contact_Name")%>>
		    <b><small>Billing Customer Contact Name<br>
		    <input class=inp type=text name="Billing_Customer_Contact_Name" size="35"
                      value="<%=Billing_Customer_Contact_Name%>" <%=InputMode%>>
                  </td>
                  <td class=<%=nosaBAN.getClass("Bill_Address_3")%>>
		    <b><small>Billing Customer Address Line 3<br>
		    <input class=inp type=text name="Bill_Address_3" size="35"
                      value="<%=Bill_Address_3%>" <%=InputMode%>>
                  </td>
                </tr>
		<tr>
                  <td class=<%=nosaBAN.getClass("Primary_Contract_Reference")%>>
		    <b><small>Primary Contract Reference<br>
		    <input class=inp type=text name="Primary_Contract_Reference" size="35"
                      value="<%=Primary_Contract_Reference%>" <%=InputMode%>>
                  </td>
                  <td class=<%=nosaBAN.getClass("CW_Contract_Entity")%>>
		    <b><small>C&W Contracting Entity<br>
                    <select disabled>
                      <option>C&W UK</option>
                    </select>
        	    <input type="hidden" name="CW_Contract_Entity" value="12">
                  </td>
                  <td class=<%=nosaBAN.getClass("Bill_Address_4")%>>
		    <b><small>Billing Customer Address Line 4<br>
		    <input class=inp type=text name="Bill_Address_4" size="35"
                      value="<%=Bill_Address_4%>" <%=InputMode%>>
                  </td>
                </tr>
		<tr>
                  <td class=<%=nosaBAN.getClass("Account_Invoice_Currency")%>>
		    <b><small>Account Invoice Currency<br>
        	    <%=DBA.getListBox("Account_Invoice_Currency",InputMode,Account_Invoice_Currency,"")%>
                  </td>
                  <td>&nbsp;</td>
                  <td class=<%=nosaBAN.getClass("Bill_Address_Country")%>>
		    <b><small>Country<br>
        	    <%=DBA.getListBox("Bill_Address_Country",InputMode,Bill_Address_Country,"")%>
                  </td>
                </tr>
		<tr>
                  <td colspan=4>&nbsp;
                  </td>
                </tr>
	      </table>
		</form>
	      </td>
	    </tr>
	  </table><!--2-->
	</td>
      </tr>
     <tr>
      <td height="100">&nbsp
      </td>
  </table><!--1-->
</body>
</html>


