<html>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%
  boolean isConglom = request.getParameter("conglom")!=null;
  String logMessages = "";
  if (request.getParameter("fromSelf") != null)
  {
    logMessages = BAN.getLogMessages(isConglom);
  }
  else
  {
    logMessages = BAN.getMBLogGrid();
  }
  boolean showView = false;
  String cols = "2";
  String closeAlign = "left";
  if ((!isConglom) && (logMessages.indexOf("Processing has ended successfully") > 0))
  {
    if (!BAN.checkAutoClose())
    {
      showView = true;
      cols = "3";
      closeAlign = "center";
    }
  }
%>
<head>
  <title>Monthly Billing Log</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<script language="JavaScript">
function viewInvoices()
{
  window.opener.location.href = "newDesktopHandler.jsp?ButtonPressed=list_A&gcId=<%=BAN.getGlobalCustomerId()%>";
  self.close();
}
</script>
<body topmargin="0" leftmargin="0" marginleft="0" margintop="0" onLoad="window.focus();">
<form name="billingLog" method="post" action="billingLog.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<%if (isConglom)
  {%>
<input name="conglom" type=hidden value="true">
<%}%>
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=<%=cols%> align=center>
      <b><%=isConglom?"Goldfish Extract Log Messages":"Monthly Billing Log"%></b>
    </td>
  </tr>
  <tr>
    <td colspan=<%=cols%> align=left>
      <b>Customer:</b><%=isConglom?(BAN.getGlobalCustomerId()+" "+
        BAN.getConglomCustomerName()+" ("+BAN.getConglomCustomerId()+")"):
        (BAN.getGlobalCustomerId()+" "+BAN.getGlobalCustomerName())%>
    </td>
  </tr>
  <tr>
    <td colspan=<%=cols%>>
      <!--header-->
      <table border=0 width=100%>
        <tr class=gridHeader>
          <td class=gridHeader NOWRAP valign=top width=150>
            <button class=grid_menu>Job Id</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=150>
            <button class=grid_menu>Date</button>
          </td>
          <td class=gridHeader NOWRAP valign=top>
            <button class=grid_menu>Message</button>
          </td>
        </tr>
        <tr>
          <td colspan=5>
          <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="billingLogGrid.jsp"></iframe>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td align=right width="50%">
      <input class=button type=submit value=Refresh>
    </td>
    <td align=<%=closeAlign%>>
      <input class=button type=button value=Close onClick="window.close();">
    </td>
<%if (showView)
  {%>
    <td align=left width="50%">
      <input class=button type=button value="View Invoices" onClick="viewInvoices()">
    </td>
<%}%>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


