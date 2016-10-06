<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
  <title>Create One-Off Invoice</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String message = "<font color=blue>Select customer, enter the relevant dates and press 'Create'</font>";
  String gcId = request.getParameter("GCB_Adhoc_Customer");
  String button1Text = "Create";
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  Calendar rightNow = Calendar.getInstance();
  String bpSDate = SU.isNull(null,"Today");
  String bpEDate = SU.isNull(null,"Today");
  int bpSDay = 1;
  int bpSMonth = rightNow.get(rightNow.MONTH) + 1;
  int bpSYear = rightNow.get(rightNow.YEAR);
  int bpEDay = rightNow.getActualMaximum(rightNow.DATE);
  int bpEMonth = rightNow.get(rightNow.MONTH) + 1;
  int bpEYear = rightNow.get(rightNow.YEAR);
  boolean disableDate = false;
  if (fromSelf.equals(""))
  {
    gcId = "";
  }
  else
  {
    disableDate = true;
    bpSDay = Integer.parseInt(request.getParameter("BPStartDateDay"));
    bpSMonth = Integer.parseInt(request.getParameter("BPStartDateMonth"));
    bpSYear = Integer.parseInt(request.getParameter("BPStartDateYear"));
    bpEDay = Integer.parseInt(request.getParameter("BPEndDateDay"));
    bpEMonth = Integer.parseInt(request.getParameter("BPEndDateMonth"));
    bpEYear = Integer.parseInt(request.getParameter("BPEndDateYear"));
    BAN.setGlobalCustomerId(gcId);
    String ret = BAN.generateOneOffInvoice(bpSDay, bpSMonth, bpSYear,
      bpEDay, bpEMonth, bpEYear);
    if (ret.equals("0"))
    {
      session.setAttribute("formname","newDesktopHandler");
      button1Text = "";
      button1OnClick = "";
      button2Text = "Exit";
      button2OnClick = "closeRefresh();";
    }
    else
    {
      button1Text = "&nbsp;";
      button1OnClick = "";
      button2Text = "Exit";
    }
    message = BAN.getMessage();
    BAN.setMessage("");
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function closeRefresh()
{
  window.opener.location.href = "newDesktop.jsp";
  self.close();
}
function valAndSub()
{
  createInvoice.button1.disabled = true;
  if (((createInvoice.GCB_Adhoc_Customer.selectedIndex <= 0) &&
    (createInvoice.GCB_Adhoc_Customer.length > 1)) ||
    (createInvoice.GCB_Adhoc_Customer[createInvoice.GCB_Adhoc_Customer.selectedIndex].value==""))
  {
    createInvoice.button1.disabled = false;
    alert('Please select a customer');
    return;
  }
  var dateOK = true;
  if (parseInt(createInvoice.BPEndDateYear[createInvoice.BPEndDateYear.selectedIndex].text) <
    parseInt(createInvoice.BPStartDateYear[createInvoice.BPStartDateYear.selectedIndex].text))
  {
    dateOK = false;
  }
  else if (parseInt(createInvoice.BPEndDateYear[createInvoice.BPEndDateYear.selectedIndex].text) ==
    parseInt(createInvoice.BPStartDateYear[createInvoice.BPStartDateYear.selectedIndex].text))
  {
    if (parseInt(createInvoice.BPEndDateMonth[createInvoice.BPEndDateMonth.selectedIndex].value) <
      parseInt(createInvoice.BPStartDateMonth[createInvoice.BPStartDateMonth.selectedIndex].value))
    {
      dateOK = false;
    }
    else if (parseInt(createInvoice.BPEndDateMonth[createInvoice.BPEndDateMonth.selectedIndex].value) ==
      parseInt(createInvoice.BPStartDateMonth[createInvoice.BPStartDateMonth.selectedIndex].value))
    {
      if (parseInt(createInvoice.BPEndDateDay[createInvoice.BPEndDateDay.selectedIndex].text) <
        parseInt(createInvoice.BPStartDateDay[createInvoice.BPStartDateDay.selectedIndex].text))
      {
        dateOK = false;
      }
    }
  }

  if (dateOK == false)
  {
    createInvoice.button1.disabled = false;
    alert("End Date must be later than Start Date");
  }
  else
  {
    createInvoice.fromSelf.value = "confirm";
    createInvoice.submit();
  }
}


function window_onload()
{
  window.focus();
}
</script>
<body language=javascript onload="return window_onload()">
<form name="createInvoice" method="post" action="createOneOffInvoice.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<input name="gcId" type=hidden value="<%=gcId%>">
<input name="bpSDay" type=hidden value="<%=bpSDay%>">
<input name="bpSMonth" type=hidden value="<%=bpSMonth%>">
<input name="bpSYear" type=hidden value="<%=bpSYear%>">
<input name="bpEDay" type=hidden value="<%=bpEDay%>">
<input name="bpEMonth" type=hidden value="<%=bpEMonth%>">
<input name="bpEYear" type=hidden value="<%=bpEYear%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Create One-Off Invoice</b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td><b>Customer:</b></td>
          <td><%=DBA.getListBox("GCB_Adhoc_Customer",disableDate?"disabled":"input",gcId,BAN.getActAsLogon(),1," style=\"width:400px\"",true)%></td>
        </tr>
        <tr>
          <td><b>Billing Period Start Date:</b></td>
          <td>
            <%=HB.getDays("createInvoice", "BPStartDate", bpSDay, false, disableDate, bpSMonth, bpSYear)%>
            <%=HB.getMonths("createInvoice", "BPStartDate", bpSMonth, false, disableDate)%>
            <%=HB.getYears("createInvoice", "BPStartDate", bpSYear, false, disableDate)%>
            <input type="hidden" name="BPStartDateh" value="<%=bpSDate%>">
          </td>
        </tr>
        <tr>
          <td><b>Billing Period End Date:</b></td>
          <td>
            <%=HB.getDays("createInvoice", "BPEndDate", bpEDay, false, disableDate, bpEMonth, bpEYear)%>
            <%=HB.getMonths("createInvoice", "BPEndDate", bpEMonth, false, disableDate)%>
            <%=HB.getYears("createInvoice", "BPEndDate", bpEYear, false, disableDate)%>
            <input type="hidden" name="BPEndDateh" value="<%=bpEDate%>">
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


