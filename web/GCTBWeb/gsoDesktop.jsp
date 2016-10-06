<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%
  session.setAttribute("formname","gsoDesktop");
  String SQL;

  String action;
  String Menu_Headings[]={"Go To", "Options", "Admin"};
  String Options[][] = {{"Log Off"," "},
                          {"Refresh","Raise Query"},
                          {"Change Password"}};

  //HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  //Define the key values to be stored in the Radio button
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  //String Message=SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  //session.setAttribute("Message","");
  //BAN.setMessage("");
  String userId = (String)session.getAttribute("User_Id");
  String actAsLogon = BAN.getActAsLogon();
  String logonGroup = BAN.getLogonGroup();
  String sortOrder = BAN.getDesktopSortOrder();
%>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if ((buttonPressed == 'Customer_Order') ||
    (buttonPressed == 'DOB') ||
    (buttonPressed == 'Status'))
  {
    gsoDesktop.sortOrder.value = buttonPressed;
    gsoDesktop.submit();
  }
  else if (buttonPressed == 'Action')
  {
    window.open("gsoDesktopKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<form name="gsoDesktop" method="post" action="gsoDesktopHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="spId" type=hidden value="">
<input name="spName" type=hidden value="">
<input name="sortOrder" type=hidden value="<%=sortOrder%>">
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
	  <h2 align="left">GSO Billing Desktop</h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="8" width=100% class=bluebold>
	       <%=(SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),"")))%>
<%
//(SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),"")))
session.setAttribute("Message","");
BAN.setMessage("");
%>
              </td>
            </tr>
            <tr>
              <td><b>Billing Team:</b></td>
              <td class=bluebold><%=BAN.getBillingTeam()%></td>
              <td colspan="4">&nbsp;</td>
              <td><b>Analyst Name:</b></td>
              <td class=bluebold><%=(String)session.getAttribute("User_Name")%></td>
            </tr>
            <tr>
              <td colspan="8">
                <!--header-->
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=400>
                      <button class=grid_menu onclick="Toggle_Sort('Customer_Order')">Service Partner</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=80>
                      <button class=grid_menu onclick="Toggle_Sort('Period')">Billing Period</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=110>
                      <button class=grid_menu onclick="Toggle_Sort('Status')">Status</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=100>
                      <button class=grid_menu onclick="Toggle_Sort('Extract Rejects')">Extract Rejects</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=100>
                      <button class=grid_menu onclick="Toggle_Sort('Billing Exceptions')">Billing Exceptions</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="Toggle_Sort('Action')">Action</button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=8>
                    <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="gsoCustomerGrid.jsp?userId=<%=actAsLogon%>&sortOrder=<%=sortOrder%>"></iframe>
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


