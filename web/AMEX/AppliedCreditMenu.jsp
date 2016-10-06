<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%
  String SQL;
  String Customer_Id="";
  String User_Staff_Number="";
  String Customer_Name="";
  String Message,ActionCustomer;
  //String Customer_Qualifier="0";
  String Surname_Filter="", CLI="";
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  JavaUtil.UserData UD = new UserData();
  DBAccess DBA = new DBAccess();
  String PageSent;
  String sharedPath=
        EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
  String colspan;
  String system = (String)session.getAttribute("System");
  String groupName = (String)session.getAttribute("Group_Name");
  String email = EBANProperties.getEBANProperty(EBANProperties.PROCESSEMAIL);
  String Header = "Header";
    //Are we coming from the handler page, or a fresh start?

  Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  if (PageSent.endsWith("AppliedCreditMenuHandler.jsp"))
  {
    Customer_Id=BAN.getGlobalCustomerId();
    Surname_Filter=BAN.getSurname_Filter();
    CLI=BAN.getCLI();
    User_Staff_Number=BAN.getUser_Staff_Number();
  }
  else
  {
    //Store old values
    session.setAttribute("Global_Customer",BAN.getGlobalCustomerId());
    session.setAttribute("Surname_Filter",BAN.getSurname_Filter());
    session.setAttribute("CLI",BAN.getCLI());
    session.setAttribute("User_Staff_Number",BAN.getUser_Staff_Number());

    BAN.setGlobalCustomerId("");
    BAN.setGlobalCustomerName("");
    BAN.setSurname_Filter("");
    BAN.setCLI("");
    BAN.setUser_Staff_Number("");
    Customer_Id="";
    Surname_Filter="";
    CLI="";
    User_Staff_Number="";
  }

if (Customer_Id.compareTo("")==0)
{
  ActionCustomer="alert('You must select a Customer for this option')";
}
else
{
  ActionCustomer="submitForm(this)";
}

%>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--
  function searchClick()
  {
    if ((AppliedCreditMenu.Surname_Filter.value == "")&&
        (AppliedCreditMenu.CLI.value == "")&&
        (AppliedCreditMenu.AMEX_Staff_Number.selectedIndex == "0"))
    {
      if (AppliedCreditMenu.AMEX_Customer[AppliedCreditMenu.AMEX_Customer.selectedIndex].value == "")
      {
          alert("Please select a customer first");
      }
      else
      {
        alert("Please enter at least one value for the search");
      }
    }
    else
    {
      AppliedCreditMenu.submit();
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
      <form name="AppliedCreditMenu" method="post" action="AppliedCreditMenuHandler.jsp">
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
            <td>&nbsp;
            </td>
	    <td valign="top"><br>
	      <h3 align="center"><b>Create Home Workers Voice Credit List</b></h3>
	       <%=Message%><br>
	       <%session.setAttribute("Message","");%></font>
	      <table width="100%" border="0">
                <tr>
                  <td align=right colspan=4>
                    <font col="000000">
                      <b>Requested By:</b></font>
                    &nbsp;&nbsp;&nbsp;
                    <font color="#0000FF">
                      <%=session.getAttribute("User_Name")
                      %></font>
                  <td>
                </tr>
                <tr>
                  <td colspan=4><font color="#0000FF"><hr></font>
                  </td>
                </tr>
		<tr>
		  <td align=left colspan=2><font color="#000000">Please select the required Customer</font>
		  </td>
		  <td>&nbsp;</td>
		</tr>
	        <tr>
		  <td align=left colspan=1><font color="#0000FF">Customer Name:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("AMEX_Customer","submit",Customer_Id,"")%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
                <tr>
                  <td>
                  &nbsp;
                  </td>
                </tr>
                <tr>
                  <td align=left colspan=2><font color="#000000">
                    Select at least one of the user staff number, surname or CLI filters
                    and press search to select credit records for the customer.</font>
                  </td>
                </tr>
                <tr>
		  <td align=left colspan=1><font color="#0000FF">User Staff Number:</font>
		  </td>
                  <td align=left>
		    <%=DBA.getListBox("AMEX_Staff_Number","",User_Staff_Number,Customer_Id,true)%>
                  </td>
                </tr>
                <tr>
		  <td align=left colspan=1><font color="#0000FF">User Surname:</font>
		  </td>
                  <td align=left>
                    <input class=inp type=text name="Surname_Filter" size="21"
                            value="<%=Surname_Filter%>">
                  </td>
                </tr>
                <tr>
		  <td align=left colspan=1><font color="#0000FF">User's CLI:</font>
		  </td>
                  <td align=left>
                    <input class=inp type=text name="CLI" size="21"
                            value="<%=CLI%>">
                    &nbsp;&nbsp;&nbsp;
                    <%=HB.getImageAsAnchor("search")%>
                    &nbsp;
                    <font color="#000000"><b>(Use * as the wildcard)</b></font>
                  <td>
                </tr>
                <tr>
                  <td colspan=4><font color="#0000FF"><hr></font>
                  </td>
                </tr>
                <tr>
                  <td width=100% colspan=4 class=bluebold>
                    <iframe frameborder=0 width="100%" height=20 id=GridHeader name=GridHeader src="CreditGridHeader.jsp" scrolling=no>
                    </iframe>
	            <%=SU.isNull((String)session.getAttribute("Message"),"")%><br>
	            <%
	              session.setAttribute("formname","listCredits");
                      session.setAttribute("Message","");
	            %>
	            <%if(SU.isNull((String)request.getQueryString(),"").compareTo("Processed") != 0)
                      //if (PageSent.endsWith("CreditMenuHandler.jsp"))
	              {//First time thru on a general lookup, don't want to waste time listing all
		    %>
	            <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="CreditGrid.jsp" ></iframe>
	            <%}
                     else
                    {%>
                      Please choose search criteria and perform a search
                    <%}%>
                    %>
	          </td>
                <tr>
	      </table>
	      </td>
            <td>&nbsp;
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