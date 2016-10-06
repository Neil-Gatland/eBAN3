<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="nospBAN" class="DBUtilities.NostroPayGroupBANBean" scope="session"/>

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
    int BEDay, BEMonth, BEYear;
    String Accounts;
    String Payment_Group_Name,Payment_Group_Id,Customer_Name,
      Customer_Surname,Customer_Contact_Number,
      Customer_Email, Location_City,Location_Country;

%>
<%//Set Initial values
    String Mode=nospBAN.getMode();//.equals("Add")?"Initiate":nospBAN.getMode();
    String Action=nospBAN.getAction();
    SelectMode=nospBAN.getSelectMode();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=nospBAN.getInputMode();

  //Are we coming from the handler page, or a fresh start?

  //Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
    Message=SU.isNull((String)session.getAttribute("Message"),"");
    PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
    Global_Customer_Id=nospBAN.getGlobalCustomerId();
    Global_Customer_Name=nospBAN.getGlobalCustomerName();
    BAN_Summary = nospBAN.getBAN_Summary();
    BAN_Reason = nospBAN.getBAN_Reason();
    BEDay = nospBAN.getBANEffectiveDateDays();
    BEMonth = nospBAN.getBANEffectiveDateMonths();
    BEYear = nospBAN.getBANEffectiveDateYears();
    Required_BAN_Effective_Date=SU.isNull(nospBAN.getRequired_BAN_Effective_Date(),"Today");
    Payment_Group_Id=nospBAN.getPaymentGroupId();
    Payment_Group_Name=nospBAN.getPaymentGroupName();
    Customer_Name=nospBAN.getCustomerName();
    Customer_Surname=nospBAN.getCustomerSurname();
    Location_City=nospBAN.getLocationCity();
    Location_Country=nospBAN.getLocationCountry();
    Customer_Contact_Number=nospBAN.getCustomerContactNumber();
    Customer_Email=nospBAN.getCustomerEmail();
%>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--
  function addarrowClick()
  {
  <%if (disableDate)
    {%>
    alert("Add not allowed");
  <%}
    else
    {%>
    if (PaymentGroupBAN.New_Account.selectedIndex == -1)
    {
      alert("Please select a new account to add");
    }
    else
    {
      PaymentGroupBAN.ButtonPressed.value="Account Add";
      PaymentGroupBAN.submit();
    }
  <%}%>
  }

  function removearrowClick()
  {
  <%if (disableDate)
    {%>
    alert("Remove not allowed");
  <%}
    else
    {%>
    if (PaymentGroupBAN.Existing_Account.selectedIndex == -1)
    {
      alert("No account selected for removal");
    }
    else
    {
      PaymentGroupBAN.ButtonPressed.value="Account Remove";
      PaymentGroupBAN.submit();
    }
  <%}%>
  }

  //-->
  </script>
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
      <td colspan=2>
	<form name="PaymentGroupBAN" method="post" action="PaymentGroupBANHandler.jsp">
        <%=HB.getBANMenu_Bar("BAN",Mode,Action,(String)session.getAttribute("System"))%>

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
		    <h3 align="center"><%=Mode%> a Payment Group</h3>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(nospBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
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
			      <td class=bluebold><%=nospBAN.getProductName()%></td>
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
			      <td class=bluebold><%=nospBAN.getBanStatus()%></td>
			      <td><b>Requested By:</td>
			      <td class=bluebold>
				<%=SU.isNull(nospBAN.getBanCreatedBy(),(String)session.getAttribute("User_Name"))%><br>
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
			<td class=<%=nospBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
		          <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=nospBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
		        </td>
                        <!--
		        <td class=<%=nospBAN.getClass("BAN_Reason")%>>
			  <b><small>BAN Reason
		          <br><textarea class=tworows name="BAN_Reason" cols="40" rows="2" <%=nospBAN.getMode("BAN_Reason")%>><%=BAN_Reason%></textarea>
		        </td>
                        -->
		        <td class=<%=nospBAN.getClass("BANEffectiveDateh")%>>BAN Effective Date<br>
                          <%=HB.getDays("PaymentGroupBAN", "BANEffectiveDate", BEDay, false, disableDate)%>
                          <%=HB.getMonths("PaymentGroupBAN", "BANEffectiveDate", BEMonth, false, disableDate)%>
                          <%=HB.getYears("PaymentGroupBAN", "BANEffectiveDate", BEYear, false, disableDate)%>
                          <input type="hidden" name="BANEffectiveDateh" value="<%=Required_BAN_Effective_Date%>">
		        </td>
			<td>
			  <span style="VISIBILITY: <%=nospBAN.getRejectVisibility()%>">
			    <b><small><%=nospBAN.getBanStatus()%> Reason
		            <br><textarea class=red cols="40" rows="2" READONLY><%=nospBAN.getRejectReason()%></textarea>
			  </span>
		        </td>
                        <td>&nbsp;</td>
		      </tr>
		<tr>
                  <td colspan=4>&nbsp;
                  </td>
                </tr>
		<tr>
                  <td class=<%=nospBAN.getClass("Payment_Group_Name")%> valign="top">
		    Payment Group Name<br>
		    <input class=inp type=text name="Payment_Group_Name" size="35"
                    value="<%=Payment_Group_Name%>" <%=InputMode%>>
                  </td>
                  <td class=<%=nospBAN.getClass("Customer_Name")%> valign="top">
		    Customer Given Name<br>
		    <input class=inp type=text name="Customer_Name" size="35"
                    value="<%=Customer_Name%>" <%=InputMode%>>
                  </td>
                  <td class=<%=nospBAN.getClass("Customer_Surname")%> valign="top">
		    Customer Surname<br>
		    <input class=inp type=text name="Customer_Surname" size="35"
                    value="<%=Customer_Surname%>" <%=InputMode%>>
                  </td>
                </tr>
		<tr>
                  <td class=<%=nospBAN.getClass("Customer_Contact_Number")%> valign="top">
		    Customer Contact Number<br>
		    <input class=inp type=text name="Customer_Contact_Number" size="35"
                    value="<%=Customer_Contact_Number%>" <%=InputMode%>>
                  </td>
                  <td class=<%=nospBAN.getClass("Customer_Email")%> valign="top">
		    Customer e-mail Address<br>
		    <input class=inp type=text name="Customer_Email" size="35"
                    value="<%=Customer_Email%>" <%=InputMode%>>
                  </td>
                </tr>
		<tr>
                  <td class=<%=nospBAN.getClass("Location_City")%> valign="top">
		    Location City<br>
		    <input class=inp type=text name="Location_City" size="35"
                    value="<%=Location_City%>" <%=InputMode%>>
                  </td>
                  <td class=<%=nospBAN.getClass("Location_Country")%> valign="top">
		    Location Country<br>
		    <input class=inp type=text name="Location_Country" size="35"
                    value="<%=Location_Country%>" <%=InputMode%>>
                  </td>
                </tr>
		<tr>
                  <td colspan=4>&nbsp;
                  </td>
                </tr>
                <tr>
                  <td class=<%=nospBAN.getClass("New_Account")%> valign="top">
		    Accounts to associate<br>
		    <%=DBA.getListBox("New_Account",InputMode,"",
                      Global_Customer_Id,Payment_Group_Id,3,
                      "style=\"height:80\"",false)%>
                  </td>
		  <td align="center">
                    <%=HB.getImageAsAnchor("addarrow")%><br>&nbsp;<br>
                    <%=HB.getImageAsAnchor("removearrow")%>
		  </td>
                  <td class=<%=nospBAN.getClass("Existing_Account")%>>
		    Associated Accounts<br>
		    <%=DBA.getListBox("Existing_Account",InputMode,"",
                      Global_Customer_Id,Payment_Group_Id,3,
                      "style=\"height:80\"",false)%>
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


