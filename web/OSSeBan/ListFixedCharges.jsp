<html>
<jsp:include page="../includes/Page_Header5.htm" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="chBAN" class="DBUtilities.FixedChargeBANBean" scope="session"/>
<% String SQL;
    String Global_Customer_Id=chBAN.getGlobal_Customer_Id_for_List();
    String Global_Customer_Name=chBAN.getGlobalCustomerName();
    String Status;
    String BAN_Date;
    String Message;
    String Qualifier="";
    boolean firstTime = request.getParameter("firstTime")!=null;


    String action;
    String Menu_Headings[]={"Go To"};
    String Options[][] = {{" ","Create BAN Menu"," "," "},
			    {"All Charges","Selected Charges"},
			    {"Show Filters","Most Recent Values","Reset Filters"}};

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //Define the key values to be stored in the Radio button
    String[] Key={"Charge_Id"};
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
    String title = "List Fixed Charges";
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
      <form name="ListCharges" method="post" action="ListFixedChargesHandler.jsp">
        <%=HB.getMenu_Bar()%>
        <input name="ButtonPressed" type=hidden value="">
        <input type=hidden name="Charge_Id" value=" ">
	<input type=hidden name="OrderBy" value="">
	<input type=hidden name="Description" value="">
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
	  <h2 align="left"><%=title%></h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="6" width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","ListCharges");
	      %>
    	        <%=SU.isNull(chBAN.getMessage(),"")%>
              <td>
            </tr>
            <tr>
              <td><b>Customer:</b></td>
              <td class=bluebold><%=Global_Customer_Name%></td>
            </tr>
            <tr>
              <td colspan="6">
	        <%=chBAN.getChargeHeader()%>
<%
  if (((firstTime) &&
    ((!chBAN.getBAN_Month_for_List().equals("All")) ||
    (!chBAN.getCharge_Description_for_List().equals("All")))) ||
    (!firstTime))
  {%>
	        <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="FixedChargeGrid.jsp"></iframe>
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
    </td>
  </tr>
</table><!--1-->
</body>
</html>


