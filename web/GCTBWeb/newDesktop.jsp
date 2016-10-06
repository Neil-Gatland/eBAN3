<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String fromForm = SU.isNull((String)session.getAttribute("formname"), "");
  String prevGCId = BAN.getGlobalCustomerId();
  if (!fromForm.equals("newDesktopHandler"))
  {
    if ((!fromForm.equals("billSubmissionHandler")) || (request.getParameter("mblog") == null))
    {
      BAN.setGlobalCustomerId("");
    }
  }
  session.setAttribute("formname","newDesktop");
  String SQL;

  String action;
  String billingTeam = BAN.getBillingTeam();
  boolean isFinance = billingTeam.equalsIgnoreCase("finance");
  String Menu_Headings[]={"Go To", "Options", "Admin"};
  String Options[][] = {{"Log Off"," "},
    {"Refresh","Raise Query","Check Queue","GCB Billing Downloads"},
    {/*"DOB",*/"Change Password",isFinance?"":"Invoice Reversal",
    isFinance?"":"One-Off Invoice",/*"Account Discount",*/
    isFinance?"":"Account Administration","Geocode Search","Analyst Search",
    "Customer Data Search",isFinance?"Invoice Search":"Ad Hoc Invoice"}};

  //Define the key values to be stored in the Radio button
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
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
    (buttonPressed == 'Auto Close') ||
    (buttonPressed == 'Status'))
  {
    newDesktop.sortOrder.value = buttonPressed;
    newDesktop.submit();
  }
  else if (buttonPressed == 'Action')
  {
    window.open("newDesktopKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<form name="newDesktop" method="post" action="newDesktopHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="gcId" type=hidden value="">
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
	  <h2 align="left">Global Customer Billing Desktop</h2>
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
              <td class=bluebold><%=billingTeam%></td>
              <td><b>Current Bill month:</b></td>
              <td class=bluebold><%=BAN.getBillMonth()%></td>
              <td><b>Last Processed DOB:</b></td>
              <td class=bluebold><%=BAN.getLastDOB()%></td>
              <td><b>SAP Close Date:</b></td>
              <td class=bluebold><%=BAN.getSapDate()%></td>
            </tr>
            <tr>
              <td><b>Analyst Name:</b></td>
              <td colspan=2 class=bluebold><%=(String)session.getAttribute("User_Name")%></td>
              <td></td>
              <td colspan=2><b>Act as group member:</b></td>
              <td colspan=2>
              <%=DBA.getListBox("Act_As_Logon","onChange=\"submitForm(this)\"",actAsLogon,logonGroup,userId)%>
              </td>
            </tr>
            <tr>
              <td colspan="8">
                <!--header-->
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=370>
                      <button class=grid_menu onclick="Toggle_Sort('Customer_Order')">Customer</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=65>
                      <button class=grid_menu onclick="Toggle_Sort('Period')">Period</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=115>
                      <button class=grid_menu onclick="Toggle_Sort('Frequency')">Frequency</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=37>
                      <button class=grid_menu onclick="Toggle_Sort('DOB')">DOB</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=55>
                      <button class=grid_menu onclick="Toggle_Sort('Accounts')">Accounts</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=70>
                      <button class=grid_menu onclick="Toggle_Sort('Status')">Status</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=60>
                      <button class=grid_menu onclick="Toggle_Sort('Auto Close')">Auto Close</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="Toggle_Sort('Action')">Action <font color=red>(click here for key)</font></button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=8>
                    <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="customerGrid2.jsp?userId=<%=actAsLogon%>&sortOrder=<%=sortOrder%>#<%=prevGCId%>"></iframe>
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


