<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String fromForm = SU.isNull((String)session.getAttribute("formname"), "");
  String prevGCId = BAN.getGlobalCustomerId();
  session.setAttribute("formname","suspendedData");
  String SQL;
  String Mode=RSB.getMode();
  boolean popUp = RSB.getSuspendPopUp();
  String action;
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{popUp?"Close":"Log Off",popUp?"":"Revenue Share Desktop"},
    {"","","Show Incorrectly Posted Only","Show Unrateable Only",
    "Show Uninvoiceable Only","Show Written-Off Only","Show All",
    "Refresh",popUp?"":"View All Processes",popUp?"":"View As Pop-Up"}};

  if (Mode.equals("Confirm"))
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
  if (buttonPressed == 'Action')
  {
    window.open("suspendedDataKey.htm", "rtk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=300,top=50,left=50");
  }
}
</script>
<form name="suspendedData" method="post" action="suspendedDataHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="accountNumber" type=hidden value="">
<input name="product" type=hidden value="">
<input name="suspended" type=hidden value="">
<input name="writeOff" type=hidden value="">
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
	  <h3 align="center">Suspended Summarised CDR Data</h3>
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
<%if (!popUp)
  {%>
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
<%}%>
            <tr>
              <td colspan="6">
                <!--header-->
	      </td>
	    </tr>
            <tr>
              <td colspan="6" align="center">
                <table border=0 width="100%">
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width="150">
                      <button class=grid_menu>Provider</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="195">
                      <button class=grid_menu>Master Account</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="195">
                      <button class=grid_menu>Account</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="200">
                      <button class=grid_menu>Product</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="200">
                      <button class=grid_menu>Suspense Reason</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="80">
                      <button class=grid_menu>Record Count</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="70">
                      <button class=grid_menu>Written Off</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="Toggle_Sort('Action')">Action</font></button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=8>
                    <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="suspendedDataGrid.jsp"></iframe>
                    </td>
                  </tr>
                </table>
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


