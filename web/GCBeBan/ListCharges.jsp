<html>
<jsp:include page="../includes/Page_Header5.htm" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="chBAN" class="DBUtilities.GCBChargeBANBean" scope="session"/>
<%! String SQL;
    String Global_Customer_Id="";
    String Global_Service_Reference="";
    String Invoice_Region="";
    String Status;
    String BAN_Date;
    String Message;
    String Qualifier="";


    String action;
    String Menu_Headings[]={"Go To","List","Filter"};
    String Options[][] = {{"Main Menu","Create BAN Menu"," ","Logout"},
			    {"All Charges","Selected Charges"},
			    {"Show Filters","Most Recent Values","Reset Filters"}};

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //Define the key values to be stored in the Radio button
    String[] Key={"Charge_Id"};
    %>
    <%
    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);

    if (SU.isNull((String)request.getQueryString(),"") != "")
    {//First time thru
      action=request.getQueryString();
    }
    else
    {
      action=chBAN.getAction();
    }
    %>
<a name="top"></a>
<table id=1 border=0 width="100%">
  <tr>
    <td colspan=3>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
    <td colspan=3>
      <form name="ListCharges" method="post" action="ListChargesHandler.jsp">
        <%=HB.getMenu_Bar(50)%>
        <input name="ButtonPressed" type=hidden value="">
        <input type=hidden name="Charge_Id" value=" ">
	<input type=hidden name="OrderBy" value="">
    </td>
  </tr>
  <tr>
   <td colspan=3>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
      <tr id=2>
	<!-- this is a spacer column-->
	<td width="1" id=2>&nbsp;</td>
	<td width="1" valign="top" align="left" id=2>
	</td>
	<td width="12" id=2><!-- a spacer column-->
	  &nbsp;
	</td>
	<td valign="top" id=2>
	  <h2 align="left">List Recurring Charges</h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","ListCharges");
	      %>
    	        <%=SU.isNull(chBAN.getMessage(),"")%>
	        <%=chBAN.getChargeHeader()%>
	        <%if(SU.isNull((String)request.getQueryString(),"").compareTo("View") != 0)
	        {//First time thru on a general lookup, don't want to waste time listing all
		%>
	        <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="ChargeGrid.jsp"></iframe>
	      <%}%>
	      </td>
	    </tr>
	  </table><!--table 3-->
	</form>
      </td>
      <td width="1">&nbsp;</td><!--spacer-->
      <td width="1" valign="top"><!--Right Hand column-->
      </td>
    </tr>
  </table><!--2-->
</table><!--1-->
</body>
</html>


