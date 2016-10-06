<html>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  String reportCode = "";
  String gcId = "";
  String gsr = "";
  String param1 = "&nbsp;";
  String param2 = "&nbsp;";
  String param3 = "&nbsp;";
  String param4 = "&nbsp;";
  int month = 0;
  int year = 0;
  String instructions = "Please select a report from the list below:";
  if (request.getParameter("fromSelf") != null)
  {
    reportCode = request.getParameter("GCB_Download_Report");
    if (reportCode.endsWith("00"))
    {
      instructions = "Click 'Submit' to retrieve the report";
    }
    else if (reportCode.endsWith("01"))
    {
      instructions = "Select a month and year and then click 'Submit' to retrieve the report";
      param1 = "Month:";
      param2 = HB.getMonths("chooseReport", "report", month, false, false) +
        " Year: " + HB.getYears("chooseReport", "report", year, false, false);
    }
    else if (reportCode.endsWith("02"))
    {
      if (request.getParameter("LLU_Global_Customer") != null)
      {
        gcId = request.getParameter("LLU_Global_Customer");
        instructions = "Select a GSR and then click 'Submit' to retrieve the report";
        param3 =  "GSR:";
        param4 =  DBA.getListBox("LLU_GSR","input",gsr,gcId,1," style=\"width:190px\"",true);
      }
      else
      {
        instructions = "Select a global customer";
      }
      param1 =  "Customer:";
      param2 =  DBA.getListBox("LLU_Global_Customer","submit",gcId,"gcId",1," style=\"width:320px\"",true);
    }
    else if (reportCode.endsWith("03"))
    {
      instructions = "Enter an account id and then click 'Submit' to retrieve the report";
      param1 = "Account Id:";
      param2 = "<input type=text name=accountId>";
    }
    else if (reportCode.endsWith("04"))
    {
      instructions = "Enter an invoice number and then click 'Submit' to retrieve the report";
      param1 = "Invoice No:";
      param2 = "<input type=text name=invoiceNo>";
    }
    else if (reportCode.endsWith("05"))
    {
      instructions = "Select a global customer";
      param1 =  "Customer:";
      param2 =  DBA.getListBox("All_Customer","input",gcId,"gcId",1," style=\"width:320px\"",true);
    }
  }
  else if (request.getParameter("instructions") != null)
  {
    instructions = "<font color=red>" + request.getParameter("instructions") +
      "</font>";
  }
%>
<head>
  <title>GCB Billing Downloads</title>
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
<%if (reportCode.endsWith("01"))
  {%>
  if (chooseReport.reportMonth.selectedIndex < 0)
  {
    alert("Please choose report month");
  }
  else if (chooseReport.reportYear.selectedIndex < 0)
  {
    alert("Please choose report year");
  }
  else
  {
    chooseReport.reportMonthName.value = chooseReport.reportMonth[chooseReport.reportMonth.selectedIndex].text;
    showReport();
    //window.open("showReport.jsp?reportCode="+, "geocode", "toolbar=no,menubar=no,location=no," +
      //"directories=no,status=no,scrollbars=no,resizable=no,height=600,width=800,top=100,left=100");
  }
<%}
  else if (reportCode.endsWith("02"))
  {%>
  if (chooseReport.LLU_Global_Customer.selectedIndex <= 0)
  {
    alert("Please choose global customer");
  }
  else if (chooseReport.LLU_GSR.selectedIndex <= 0)
  {
    alert("Please choose GSR");
  }
  else
  {
    showReport();
  }
<%}
  else if (reportCode.endsWith("03"))
  {%>
  if (chooseReport.accountId.value == "")
  {
    alert("Please enter an account id");
  }
  else
  {
    showReport();
  }
<%}
  else if (reportCode.endsWith("04"))
  {%>
  if (chooseReport.invoiceNo.value == "")
  {
    alert("Please enter an invoice number");
  }
  else
  {
    showReport();
  }
<%}
  else if (reportCode.endsWith("05"))
  {%>
  if (chooseReport.All_Customer.selectedIndex <= 0)
  {
    alert("Please choose global customer");
  }
  else
  {
    showReport();
  }
<%}
  else if (reportCode.endsWith("00"))
  {
  %>
    showReport();
<%}
  else
  {%>
    alert("Please select a report from the list");
<%}%>
}
</script>
<body topmargin="0" leftmargin="0" marginleft="0" margintop="0" onLoad="window.focus();">
<form name="chooseReport" method="GET" action="chooseReport.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<input name="reportMonthName" type=hidden value="">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b><%=instructions%></b>
    </td>
  </tr>
  <tr>
    <td align=right valign=top width="15%">
      Report:
    </td>
    <td align=left valign=top>
      <%=DBA.getListBox("GCB_Download_Report","onChange=\"listSub()\"",reportCode,"reportCode",1," style=\"width:390px\"",true)%>
    </td>
  </tr>
  <tr>
    <td align=right valign=top width="15%">
      <%=param1%>
    </td>
    <td align=left valign=top>
      <%=param2%>
    </td>
  </tr>
  <tr>
    <td align=right valign=top width="15%">
      <%=param3%>
    </td>
    <td align=left valign=top>
      <%=param4%>
    </td>
  </tr>
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


