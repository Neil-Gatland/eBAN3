<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<% String SQL;
    String Global_Customer_Id="";
    String Nostro_User_Id="";
    String Product="";
    String Global_Customer_Name="";
    String Account_Id="",Division_Id="";
    String Payment_Group_Id="";
    String Service_Reference="",Account_Name="";
    String Message,ActionAccount,ActionService,ActionCustomer,ActionDivision,
    ActionProduct,ActionPayGroup,ActionUser,ActionCustomerInit,ActionCustomerAmend;
    String Account_Qualifier="All",Service_Qualifier="All",
      Customer_Qualifier="All",Payment_Group_Qualifier="All",
      Nostro_User_Qualifier="All";
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    DBAccess DBA = new DBAccess();
    String PageSent;
    String sharedPath=
          EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
    String colspan;
    String system;
  //Are we coming from the handler page, or a fresh start?

  system = (String)session.getAttribute("System");

  Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  if (PageSent.endsWith("BANMenuHandler.jsp")
  || (Message.endsWith("charges for this Service<br>"))
  || (PageSent.endsWith("BANMenuHandler.jsp") && BAN.getAction().compareTo("Authorise")==0))
  {
    Product=BAN.getProduct();
    session.setAttribute("Product",Product);
    Customer_Qualifier=SU.isBlank(Product,"All");
    Global_Customer_Id=BAN.getGlobalCustomerId();
    Account_Qualifier=SU.isBlank(Global_Customer_Id,"All");
    Payment_Group_Qualifier=SU.isBlank(Global_Customer_Id,"All");
    Account_Id=BAN.getAccount_Id();
    Nostro_User_Qualifier=SU.isBlank(Account_Id,"All");
    Account_Name=BAN.getAccountName();
    Division_Id=BAN.getDivision_Id();
    Service_Qualifier=SU.isBlank(Division_Id,"All");
    Service_Reference=BAN.getService_Reference();
    Payment_Group_Id=BAN.getPaymentGroupId();
    Nostro_User_Id=BAN.getNostroUserId();
  }
  else
  {
    //Store old values
    session.setAttribute("Product",BAN.getProduct());
    session.setAttribute("Global_Customer",BAN.getGlobalCustomerId());
    session.setAttribute("Account",BAN.getAccount_Id());
    session.setAttribute("Division",BAN.getDivision_Id());
    session.setAttribute("Service",BAN.getService_Reference());
    session.setAttribute("Payment_Group",BAN.getPaymentGroupId());
    session.setAttribute("Nostro_User",BAN.getNostroUserId());

    BAN.setService_Reference("");
    BAN.setGlobalCustomerId("");
    BAN.setGlobalCustomerName("");
    BAN.setAccount_Id("");
    BAN.setDivision_Id("");
    BAN.setPaymentGroupId("");
    Global_Customer_Id="";
    Product="";
    Account_Id="";
    Service_Reference="";
    Division_Id="";
    Payment_Group_Id="0";
    Nostro_User_Id="";
  }
  if (Product.compareTo("")==0)
    {ActionProduct="alert('You must select a Product for this option')";}
  else
    {ActionProduct="submitForm(this)";}
  if (Global_Customer_Id.compareTo("")==0)
  {
    ActionCustomerInit="alert('You must select a Customer for this option')";
    ActionCustomerAmend="alert('You must select a Customer for this option')";
  }
  else
  {
    if (BAN.customerInitiated())
    {
      ActionCustomerInit="alert('This Customer has already been initiated')";
      ActionCustomerAmend="submitForm(this)";
    }
    else
    {
      ActionCustomerInit="submitForm(this)";
      ActionCustomerAmend="alert('You must select a Customer which has been initiated for this option')";
    }
  }
  if (Account_Id.compareTo("")==0)
    {ActionAccount="alert('You must select an Account for this option')";}
  else
    {ActionAccount="submitForm(this)";}
  if (Division_Id.compareTo("")==0)
    {ActionDivision="alert('You must select a Division for this option')";}
  else
    {ActionDivision="submitForm(this)";}
  if ((SU.isNull(Service_Reference,"").compareTo("")==0) ||
      (SU.isNull(Service_Reference,"").startsWith("N/A")))
    {ActionService="alert('You must select a Service Reference for this option')";}
  else
    {ActionService="submitForm(this)";}
  if (Payment_Group_Id.compareTo("0")==0)
  {
    ActionPayGroup="alert('You must select a Payment Group for this option')";
  }
  else
  {
    ActionPayGroup="submitForm(this)";
  }
  if (Nostro_User_Id.compareTo("")==0)
    {ActionUser="alert('You must select a User for this option')";}
  else
    {ActionUser="submitForm(this)";}

%>
  <script language="javascript">
  <!--
  function setAccountName()
  {
    NostroBANMenu.Account_Name.value =
      NostroBANMenu.Nostro_Account[NostroBANMenu.Nostro_Account.selectedIndex].text;
    NostroBANMenu.submit();
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
      <form name="NostroBANMenu" method="post" action="NostroBANMenuHandler.jsp">
      <%=
      HB.getBANMenu_Bar("Menu","","",(String)session.getAttribute("System"))
      //HB.getBANMenu_Bar("Menu","",""
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
	    <td valign="top">
	      <h3 align="center"><b>Create a BAN</b></h3>
	       <%=Message%><br>
	       <%session.setAttribute("Message","");%></font>
	      <b>Please select the entries from the lists relevant to the type of BAN you wish to create</b>
	      <table width="100%" border="0">
	        <tr>
		  <td align=left colspan=2><font color="#0000FF">Customer Name:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("All_Customer","submit",Global_Customer_Id,"")%>
		  </td>
                  <td width="50%">&nbsp;</td>
		</tr>
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Account:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("Nostro_Account"," onChange='setAccountName()'>",Account_Id,Account_Qualifier)%>
                  <input type="hidden" name="Account_Name" value="<%=Account_Name%>">
		  </td>
                  <td width="50%">&nbsp;</td>
		</tr>
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Payment Group:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("Payment_Group","submit",Payment_Group_Id,Payment_Group_Qualifier)%>
		  </td>
                  <td width="50%">&nbsp;</td>
		</tr>
		<tr>
		  <td align=left colspan=2><font color="#0000FF">User:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("Nostro_User","submit",Nostro_User_Id,Nostro_User_Qualifier)%>
		  </td>
                  <td width="50%">&nbsp;</td>
		</tr>
	      </table>
	      <table border="0">
		  <tr>
		    <td>&nbsp
		    </td>
		  </tr>
		  <tr>
		    <td width=800 colspan=4 valign="top"> <b>Please select the type of BAN you wish to create</b>
		  </tr>
		  <tr>
		    <td>&nbsp
		    </td>
		  <tr>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Initiate a Customer",ActionCustomerInit)%>
		    </td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Initiate an Account",ActionCustomerAmend)%>
		    </td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create an e-billing Payment Group",ActionCustomerAmend)%>
		    </td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a User",ActionAccount)%>
		     </td>
		  </tr>
		  <tr>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend a Customer",ActionCustomerAmend)%>
		    </td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend an Account",ActionAccount)%>
		    </td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend an e-billing Payment Group",ActionPayGroup)%>
		    </td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend a User",ActionUser)%>
		     </td>
		  </tr>
		  <tr>
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
    </tr>
  </table><!--1-->
</body>
</html>