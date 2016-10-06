<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.*,HTMLUtil.HTMLBean"%>
<jsp:useBean id="cBAN" class="DBUtilities.CustomerBANBean" scope="session"/>
<jsp:useBean id="iBAN" class="DBUtilities.InvoiceRegionBean" scope="session"/>

<%! String SQL;
    String Invoice_Region;
    String action;
    String Menu_Headings[]={"Go To","Options"};
    String Options1[][] = {{"Preceding Page","Main Menu","Customer Details"," ","Logout"},
			    {"Add Invoice Region","Submit BAN for Authorisation"}};
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    //Define the key values to be stored in the Radio button

    String[] Key={"Invoice_Region"};
  %>
  <%
    iBAN.setUserId((String)session.getAttribute("User_Id"));
  %>
<a name="top"></a>
<table id=1 border=0 width="100%">
  <tr>
    <td colspan=3>
      <%@ include file="../includes/Page_Header2.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
      <td>
	<form name="listInvoiceRegion" method="post" action="ListInvoiceRegionHandler.jsp">
        <%
          HB.setMenu_Headings(Menu_Headings);
          HB.setOption_Array(Options1);
	%>
        <%=HB.getMenu_Bar()%>
        <input name="ButtonPressed" type=hidden value="">
        <input type=hidden name="InvoiceRegion" value=" ">
	<input type=hidden name="OrderBy" value="1">
	</form>
      </td>
    </tr>
  <td>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
    <tr id=2>
      <!-- this is a spacer column-->
      <td width="1" id=2>&nbsp;
      </td>
      <td width="1" valign="top" align="left" id=2>
      </td>
      <td width="12" id=2><!-- a spacer column-->
      &nbsp;
      </td>
      <td valign="top" id=2>
	<h2 align="left">Invoice Regions</h2>
          <table width="100%" border="0" id=3 >
	  <tr>
	    <td colspan=4 width=100%>
              <%=iBAN.getMessage()%>
	      <%iBAN.setMessage("");%>
	    </td>
	  <tr>
	    <td colspan=4 width=100%>&nbsp
	    </td>
	  <tr>
	    <td colspan=4 width=100%>&nbsp
	    </td>
	  </tr>
	    <td colspan=4 width=100%>
	      <%
	      //iBAN.getInvoiceRegionList();
	      session.setAttribute("formname","listInvoiceRegion");%>
	      <%iBAN.setBANs_to_List("Invoice Region");%>
	      <iframe frameborder=0 width="100%" height=40 id=GridHeader name=GridHeader src="ListInvoiceRegionHeader.jsp" scrolling=no></iframe>
	      <br>
	      <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="ListInvoiceRegionGrid.jsp" ></iframe>
	    </td>
	  </tr>
	</table><!--table 3-->
	</td>
        <td width="1">&nbsp;</td><!--spacer-->
        <td width="1" valign="top"><!--Right Hand column-->
        </td>
      </tr>
    </table><!--2-->
 <!--Footer-->
  <tr><td colspan=4>
      <%@ include file="../includes/Footer.htm"%>
  <tr><td>
</table><!--1-->
<br>
</body>
</html>
