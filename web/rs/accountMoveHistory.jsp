<html>
<head>
  <title>Account Move History</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<jsp:useBean id="ACC" class="DBUtilities.AccountBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String message = "";
  String button1Text = "Refresh";
  String button2Text = "Exit";
  String button3Text = "Download";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String button3OnClick = "downloadMsg();";
  RSB.populateAccountMoveHistoryList();
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function valAndSub()
{
  accountMoveHistory.action = "accountMoveHistory.jsp";
  accountMoveHistory.fromSelf.value = "refresh";
  accountMoveHistory.submit();
}

function downloadMsg()
{
  accountMoveHistory.action = "downloadList.jsp";
  accountMoveHistory.submit();
}


function window_onload()
{
  window.focus();
}
</script>
<body language=javascript onload="return window_onload()">
<form name="accountMoveHistory" method="post" action="accountMoveHistory.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<input name="processSeqNo" type=hidden value="">
<input name="fileName" type=hidden value="<%=ACC.getAccountNumber() + "_History"%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=3 align=center>
      <b>Account Move History for <%=ACC.getAccountName()%>/<%=ACC.getAccountNumber()%> (<%=ACC.getAccountId()%>)</b>
    </td>
  </tr>
  <tr>
    <td colspan=3>
      <!--header-->
      <table border=0 width=100%>
        <tr class=gridHeader>
          <td class=gridHeader NOWRAP valign=top width="200">
            <button class=grid_menu>Old Master Account</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width="200">
            <button class=grid_menu>Old Provider</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width="200">
            <button class=grid_menu>New Master Account</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width="200">
            <button class=grid_menu>New Provider</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width="100">
            <button class=grid_menu>Move Date</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width="100">
            <button class=grid_menu> Moved By</button>
          </td>
          <td class=gridHeader NOWRAP valign=top>
            <button class=grid_menu>&nbsp;</button>
          </td>
        </tr>
        <tr>
          <td colspan=7>
          <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="accountMoveHistoryGrid.jsp"></iframe>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan=3 align=center>
      <b><%=message%></b>
    </td>
  </tr>
  <tr>
    <td width="40%" align=right>
      <input name="button1" class=button type=button value="<%=button1Text%>" style="width:90px" onClick="<%=button1OnClick%>">
    </td>
    <td align=center>
      <input class=button type=button value="<%=button3Text%>" style="width:90px" onClick="<%=button3OnClick%>">
    </td>
    <td width="40%" align=left>
      <input class=button type=button value="<%=button2Text%>" style="width:90px" onClick="<%=button2OnClick%>">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


