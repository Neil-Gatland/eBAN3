<html>
<head>
  <title>View Process Messages</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String processName = request.getParameter("processName");
  String message = "";
  String fromCallMonth = "";
  String button1Text = "Refresh";
  String button2Text = "Exit";
  String button3Text = "Download";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String button3OnClick = "downloadMsg();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  if (!fromSelf.equals(""))
  {
    if (fromSelf.equals("refresh"))
    {
      RSB.populateProcessMessageList();
    }
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if (buttonPressed == 'Action')
  {
    window.open("viewProcessKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
function valAndSub()
{
  viewProcessMessages.action = "viewProcessMessages.jsp";
  viewProcessMessages.fromSelf.value = "refresh";
  viewProcessMessages.submit();
}

function downloadMsg()
{
  viewProcessMessages.action = "downloadList.jsp";
  viewProcessMessages.submit();
}


function window_onload()
{
  window.focus();
}
</script>
<body language=javascript onload="return window_onload()">
<form name="viewProcessMessages" method="post" action="viewProcessMessages.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<input name="processSeqNo" type=hidden value="">
<input name="processName" type=hidden value="<%=processName%>">
<input name="fileName" type=hidden value="<%=processName.replace(' ', '_') + "_" + RSB.getLatestCallMonth()%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=3 align=center>
      <b>View Process Messages for <%=processName%> <%=RSB.getLatestCallMonth()%></b>
    </td>
  </tr>
  <tr>
    <td colspan=3>
      <!--header-->
      <table border=0 width=100%>
        <tr class=gridHeader>
          <td class=gridHeader NOWRAP valign=top width="570">
            <button class=grid_menu>Message</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width="130">
            <button class=grid_menu>Message Time</button>
          </td>
          <td class=gridHeader NOWRAP valign=top>
            <button class=grid_menu>&nbsp;</button>
          </td>
        </tr>
        <tr>
          <td colspan=6>
          <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="viewProcessMessagesGrid.jsp"></iframe>
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


