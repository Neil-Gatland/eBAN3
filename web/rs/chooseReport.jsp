<html>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  String reportType = request.getParameter("reportType");
  StringBuffer instructions = new StringBuffer("Please select a report");
  if (reportType.equals("rateflag"))
  {
    instructions.append(" and provider");
  }
  else if (reportType.equals("archive"))
  {
    instructions.append(", provider and call month");
  }
  else //if (reportType.equals("audit"))
  {
    instructions.append(", provider and call month (if required)");
  }
  instructions.append(" from the list below and then click 'Submit' to retrieve the report");
  String rsReport = "";
  String reportCode = "";
  String callMonth = "";
  String providerNameParameter = reportType.equals("rateflag")?"providerName2":
    "providerName";
  String providerName =request.getParameter("providerName")==null?"":
    request.getParameter("providerName");
  //String providerName = reportType.equals("rateflag")?request.getParameter("providerName2"):
    //request.getParameter("providerName");
  try
  {
    rsReport = request.getParameter("rsReport");
    reportCode = rsReport==null?"":
      rsReport.equals("none")?"":rsReport;
    int month = 0;
    int year = 0;
    if (request.getParameter("fromSelf") != null)
    {
      callMonth = request.getParameter("fromCallMonth").equals("All")?"":
        request.getParameter("fromCallMonth");
      if (request.getParameter("instructions") != null)
      {
        instructions = new StringBuffer(request.getParameter("instructions"));
      }
    }
    else
    {
      callMonth = "";//RSB.getLatestCallMonth();
    }
  }
  catch (NullPointerException ex)
  {
  }
%>
<head>
  <title>Revenue Share Report Downloads</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<script language="JavaScript">
function listSub()
{
    chooseReport.action = "chooseReport.jsp";
    chooseReport.submit();
}
function showReport()
{
    alert("Your data will now be retrieved. This may take some time. Please do not press 'Submit' again.");
    chooseReport.action = "showReport.jsp";
    chooseReport.submit();
}
function valAndSub()
{
  var report = chooseReport.rsReport[chooseReport.rsReport.selectedIndex].value;
  var reportCode = report.substring(report.length - 2);
  if (chooseReport.rsReport.selectedIndex <= 0)
  {
    alert("Please select a report to download");
  }
<%if ((reportType.equals("archive")) || (reportType.equals("rateflag")))
  {%>
  else if (((reportCode == "03") || (reportCode == "04")) &&
    (chooseReport.<%=providerNameParameter%>.selectedIndex <= 0))
  {
    alert("Please select a provider");
  }
<%}
  if (!reportType.equals("rateflag"))
  {%>
  else if (((reportCode == "01") || (reportCode == "03")) &&
    (chooseReport.fromCallMonth.selectedIndex <= 0))
  {
    alert("Please select a call month");
  }
  else if ((reportCode == "02") && (chooseReport.fromCallMonth.selectedIndex > 0))
  {
    if (confirm("Call month is not relevant to this report and will be ignored. Press 'OK' to continue."))
    {
      showReport();
    }
  }
<%}%>
  else
  {
    showReport();
  }
}
</script>
<body topmargin="0" leftmargin="0" marginleft="0" margintop="0" onLoad="window.focus();">
<form name="chooseReport" method="GET" action="chooseReport.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<input name="reportMonthName" type=hidden value="">
<input name="reportType" type=hidden value="<%=reportType%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b><%=instructions.toString()%></b>
    </td>
  </tr>
  <tr>
    <td align=right valign=top width="15%">
      Report:
    </td>
    <td align=left valign=top>
      <%=DBA.getListBox("rsReport","input",reportCode,reportType,1," style=\"width:390px\"",true)%>
    </td>
  </tr>
<%if ((reportType.equals("archive")) || (reportType.equals("rateflag")))
  {%>
  <tr>
    <td align=right valign=top width="15%">
      Provider:
    </td>
    <td align=left valign=top>
      <%=DBA.getListBox(providerNameParameter,"input",providerName,(String)session.getAttribute("User_Id"),1," style=\"width:390px\"",true)%>
    </td>
  </tr>
<%}
  if (!reportType.equals("rateflag"))
  {%>
  <tr>
    <td align=right valign=top width="15%">
      Call Month:
    </td>
    <td align=left valign=top>
      <%=DBA.getListBox("fromCallMonth","input",callMonth,"000000",1)%>
    </td>
  </tr>
<%}%>
  <tr>
    <td colspan=2 align=center>
      <input class=button type=button value=Submit onClick="valAndSub()">
      <input class=button type=button value=Close onClick="window.close();">
    </td>
  </tr>
<!--
  <tr>
    <td colspan=2 align=center id="loading" name="loading" style="visibility:hidden">
      <img src="../nr/cw/newimages/cwloading.gif">
    </td>
  </tr>
-->
</table><!--table 3-->
</form>
</body>
</html>


