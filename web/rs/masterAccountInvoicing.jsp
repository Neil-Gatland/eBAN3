<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String fromForm = SU.isNull((String)session.getAttribute("formname"), "");
  session.setAttribute("formname","masterAccountInvoicing");
  String SQL;
  String Mode=RSB.getMode();
  String action;
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","Revenue Share Desktop"},
    {"Submit","","View Invoice Report","View Suspense Report","Refresh",
    "View All Processes","View This Process"}};


  if (!RSB.getCanSubmit())
  {
    Options[1][0] = "";
    Options[1][1] = "";
  }
  else if (Mode.equals("Confirm"))
  {
    Options[1][0] = "Confirm";
    Options[1][1] = "Cancel";
  }
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  //session.setAttribute("Message","");
  //RSB.setMessage("");
  String userId = (String)session.getAttribute("User_Id");
%>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
}
</script>
<form name="masterAccountInvoicing" method="post" action="masterAccountInvoicingHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<a name="top"></a>
<table id=1 border=0 width="100%">
  <tr>
    <td colspan=3>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
    <td colspan=3>
      <%=HB.getMenu_Bar()%>
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
	  <h3 align="center">Master Account Invoicing</h3>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="6" width=100% class=bluebold>
	       <%=(SU.isBlank(RSB.getMessage(),SU.isNull((String)session.getAttribute("Message"),"")))%>
<%
//(SU.isBlank(RSB.getMessage(),SU.isNull((String)session.getAttribute("Message"),"")))
session.setAttribute("Message","");
RSB.setMessage("");
%>
              </td>
            </tr>
            <tr>
              <td><b>Call Month:</b></td>
              <td class=bluebold><%=RSB.getLatestCallMonth()%></td>
              <td><b>Current Status:</b></td>
              <td class=bluebold><%=RSB.getCurrentStatus()%></td>
              <td><b>RQR09 Load Time:</b></td>
              <td class=bluebold><%=RSB.getRQR09LoadTime()%></td>
            </tr>
            <tr>
              <td><b>Rating Time:</b></td>
              <td class=bluebold><%=RSB.getRatingTime()%></td>
              <td><b>Invoicing Time:</b></td>
              <td class=bluebold><%=RSB.getInvoicingTime()%></td>
              <td><b>Ebilling Upload Time:</b></td>
              <td class=bluebold><%=RSB.getEbillingUploadTime()%></td>
            </tr>
            <tr>
              <td colspan="6">
                <hr>
              </td>
            </tr>
            <tr>
              <td colspan="6"><b>Master Account Invoicing Process:</b></td>
            </tr>
            <tr>
              <td><b>Status:</b></td>
              <td class=bluebold><%=RSB.getNameProcessStatus()%></td>
              <td><b>Time:</b></td>
              <td class=bluebold><%=RSB.getNameProcessTime()%></td>
              <td><b>Completed OK:</b></td>
              <td class=bluebold><%=RSB.getNameProcessCompletedOK()%></td>
            </tr>
            <tr>
              <td colspan="6">
                <hr>
              </td>
            </tr>
            <tr>
              <td colspan="6">
                <!--header-->
	      </td>
	    </tr>
	  </table><!--table 3-->
      </td>
      <td width="1">&nbsp;</td><!--spacer-->
      <td width="1" valign="top"><!--Right Hand column-->
      </td>
    </tr>
  </table><!--2-->
    </td>
  </tr>
</table><!--1-->
</form>
</body>
</html>


