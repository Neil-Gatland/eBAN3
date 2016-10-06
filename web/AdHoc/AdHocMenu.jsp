<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%
  String SQL;
  String Customer_Id="";
  String Customer_Name="";
  String Account_Id="";
  String Account="";
  String Account_Filter="";
  String Message,ActionAccount,ActionCustomer,ActionAddress;
  String Account_Qualifier="0",Customer_Qualifier="0";
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  DBAccess DBA = new DBAccess();
  String PageSent;
  String sharedPath=
        EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
  String colspan;
  String system = (String)session.getAttribute("System");
  String groupName = (String)session.getAttribute("Group_Name");
  String email = EBANProperties.getEBANProperty(EBANProperties.PROCESSEMAIL);
  //Are we coming from the handler page, or a fresh start?

  Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  if (PageSent.endsWith("MenuHandler.jsp")
  ||  PageSent.endsWith("PopUp.jsp")
  || (Message.endsWith("charges for this Service<br>"))
  || (PageSent.endsWith("MenuHandler.jsp") && BAN.getAction().compareTo("Authorise")==0))
  {
    Customer_Id=BAN.getGlobalCustomerId();
    Account_Qualifier=SU.isBlank(Customer_Id,"0");
    Account_Id=BAN.getAccount_Id();
    Account=BAN.getAccount_Id();
    Account_Filter=BAN.getAccountFilter();
  }
  else
  {
    //Store old values
    session.setAttribute("Global_Customer",BAN.getGlobalCustomerId());
    session.setAttribute("Account",BAN.getAccount_Id());

    BAN.setGlobalCustomerId("");
    BAN.setGlobalCustomerName("");
    BAN.setAccount_Id("");
    BAN.setAccountFilter("");
    Customer_Id="";
    Account_Id="";
    Account_Filter="";
  }
  if (Account_Id.compareTo("")==0)
  {
    ActionAccount="alert('You must select an Account (and Billing Source, if appropriate) for this option')";
    ActionAddress=ActionAccount;
  }
  else
  {
    ActionAccount="submitForm(this)";
    ActionAddress="window.open('CreateBillingAddress.jsp?account="+Account+"&bsource="+
      BAN.getBillingSource()+
      "','addpop','toolbar=no,menubar=no,location=no,directories=no,status=no,"+
      "scrollbars=auto,resizable=yes,height=325,width=360,top=250,left=400')";
  }
  if (Customer_Id.compareTo("")==0)
  {
    ActionCustomer="alert('You must select a Customer for this option')";
  }
  else
  {
    ActionCustomer="submitForm(this)";
    ActionAccount="submitForm(this)";
  }

%>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--
  function Ad_Hoc_Account_List_Process()
  {
    AdHocMenu.Account.value =
      AdHocMenu.Ad_Hoc_Account[AdHocMenu.Ad_Hoc_Account.selectedIndex].text;
    if (AdHocMenu.Ad_Hoc_Account[AdHocMenu.Ad_Hoc_Account.selectedIndex].value == 1)
    {
      AdHocMenu.submit();
    }
    else
    {
      window.open('BillingSourcePopUp.jsp?whereFrom=menu&Account='+AdHocMenu.Account.value,
      'bspop','toolbar=no,menubar=no,location=no,directories=no,status=no,scrollbars=auto,resizable=yes,height=135,width=330,top=250,left=400');
    }
  }
  function searchClick()
  {
    if (AdHocMenu.Account_Filter.value == "")
    {
      alert('Please enter an account filter value');
    }
    else
    {
      AdHocMenu.submit();
    }
  }
  //-->
  </script>
  <a name="top"></a>
  <table id=1 width ="100%">
    <tr>
      <td colspan=3>
        <%@ include file="../includes/Page_Header4.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
      <form name="AdHocMenu" method="post" action="AdHocMenuHandler.jsp">
      <%=
      HB.getBANMenu_Bar("Menu","","",(String)session.getAttribute("System"))
      %>
      <input name="ButtonPressed" type=hidden value="">
      </td>
    </tr>
    <tr>
      <td colspan=3>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
	    <td width="13">&nbsp;
	    <!-- this is a spacer column-->
	    </td>
	    <td width="100" valign="top" align="left">
	    <!--former menu column-->
	    </td>
	    <td width="12"><!-- a spacer column-->
		&nbsp;
	    </td>
	    <td valign="top"><br>
	      <h3 align="center"><b>Create an Invoice</b></h3>
	       <%=Message%><br>
	       <%session.setAttribute("Message","");%></font>
	      <b>Please insert filter criteria and select the 'Search' button to populate the account list. Then select an account from the list and continue.  </b>
	      <table width="100%" border="0">
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Account Filter:</font>
		  </td>
		  <td align=left>
                    <table border="0">
                      <tr>
                        <td>
                          <input class=inp type=text name="Account_Filter" size="21"
                            value="<%=Account_Filter%>">
                        </td>
                        <td>
                          <%=HB.getImageAsAnchor("search")%>
                        </td>
                        <td>
                          <b><nobr>(Use * as the wildcard)</nobr></b>
                        </td>
                      </tr>
                    </table>
		  </td>
                  <td align="left">&nbsp;</td>
		</tr>
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Account:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("Ad_Hoc_Account","process",Account_Id,Account_Filter)%>
                  <input type="hidden" name="Account" value="<%=Account%>">
		  </td>
                  <td>&nbsp;</td>
		</tr>
	        <tr>
                  <td colspan=4>&nbsp;</td>
		</tr>
	        <tr>
                  <td colspan=4>
                    <font color="#0000FF">If the account is not available, please
                    select the relevant customer from the following list. If the customer
                    does not appear, please use the 'Create a Customer' option.
                    This will create a provisional customer entry to allow the charge
                    to be applied. However, the charge will not be eligible for authorisation
                    until valid account details have been received from AML.</font>
                  </td>
		</tr>
	        <tr>
		  <td align=left colspan=2><font color="#0000FF">Customer Name:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("Ad_Hoc_Customer","submit",Customer_Id,"")%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
	      </table>
	      <table border="0">
		  <tr>
		    <td>&nbsp
		    </td>
		  </tr>
		  <tr>
		    <td width=800 colspan=3 valign="top"> <b></b>
		  </tr>
		  <tr>
		    <td>&nbsp
		    </td>
		  <tr>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Customer","window.open('CustomerPopUp.jsp','custpop','toolbar=no,menubar=no,location=no,directories=no,status=no,scrollbars=auto,resizable=yes,height=125,width=310,top=250,left=400')")%>
		    </td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create an AssureCharge",ActionAccount)%>
		    </td>
		  </tr>
		  <tr>
                  <%if (groupName.equals("Admin"))
                    {%>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create/Maintain Account Billing Address",ActionAddress)%>
		    </td>
                  <%}
                    else
                    {%>
		    <td>&nbsp
		    </td>
                  <%}%>
		  </tr>
		  <tr>
		    <td>&nbsp
		    </td>
		  </tr>
		  <tr>
		    <td>&nbsp
		    </td>
		  </tr>
		  <tr>
                    <td colspan=3><font color="#0000FF">
                      If the account you require is not available
                      then please contact
                      <A HREF='mailto:<%=email%>'>Process Support</A></font>
                    </td>
		  </tr>
		</table>
	      </td>
	    </tr>
	  </table><!--2-->
	</td>
      </tr>
     <tr>
      <td height="100">&nbsp
      </td>
    </form>
    </tr>
  </table><!--1-->
</body>
</html>