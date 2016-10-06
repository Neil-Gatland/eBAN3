<html>
<head>
  <title>View Process</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String processName = request.getParameter("processName")==null?"":
    request.getParameter("processName");
  String message ="";
  String fromCallMonth = "";
  String button1Text = "Refresh";
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf")==null?"":
    request.getParameter("fromSelf");
  if (!fromSelf.equals(""))
  {
    if (fromSelf.equals("refresh"))
    {
      RSB.populateProcessList();
    }
    else if (fromSelf.equals("drill"))
    {
      RSB.setProcessSeqNo(Long.parseLong(request.getParameter("processSeqNo")));
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
  viewProcess.fromSelf.value = "refresh";
  viewProcess.submit();
}


function window_onload()
{
  window.focus();
<%if (fromSelf.equals("drill"))
  {%>
  window.open("viewProcessMessages.jsp?processName=<%=processName%>", "rsAdv2", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=400,width=800,top=75,left=75");
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="viewProcess" method="post" action="viewProcess.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<input name="processSeqNo" type=hidden value="">
<input name="processName" type=hidden value="<%=processName%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>View Processes for <%=RSB.getLatestCallMonth()%></b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr class=gridHeader>
          <td class=gridHeader NOWRAP valign=top width="200">
            <button class=grid_menu>Process Name</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width="130">
            <button class=grid_menu>Process Time</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width="90">
            <button class=grid_menu>Process Status</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width="90">
            <button class=grid_menu>Completed OK</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width="100">
            <button class=grid_menu>RQR09 Rerated</button>
          </td>
          <td class=gridHeader NOWRAP valign=top>
            <button class=grid_menu onclick="Toggle_Sort('Action')">Action <font color=red>(click here for key)</font></button>
          </td>
        </tr>
        <tr>
          <td colspan=6>
          <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="viewProcessGrid.jsp"></iframe>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center>
      <b><%=message%></b>
    </td>
  </tr>
  <tr>
    <td align=right>
      <input name="button1" class=button type=button value="<%=button1Text%>" style="width:90px" onClick="<%=button1OnClick%>">
    </td>
    <td align=left>
      <input class=button type=button value="<%=button2Text%>" style="width:90px" onClick="<%=button2OnClick%>">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


