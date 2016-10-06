<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
  <title>Create Ad Hoc Invoice</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="adjBAN" class="DBUtilities.AdjustmentBANBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String message = "<font color=blue>Enter the relevant dates and press 'Create' to begin</font>";
  String gcId = request.getParameter("gcId");
  String invoiceRegion = request.getParameter("invoiceRegion");
  String button1Text = "Create";
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  Calendar rightNow = Calendar.getInstance();
  String bpSDate = SU.isNull(null,"Today");
  String bpEDate = SU.isNull(null,"Today");
  int bpSDay = rightNow.get(rightNow.DATE);
  int bpSMonth = rightNow.get(rightNow.MONTH) + 1;
  int bpSYear = rightNow.get(rightNow.YEAR);
  int bpEDay = rightNow.get(rightNow.DATE);
  int bpEMonth = rightNow.get(rightNow.MONTH) + 1;
  int bpEYear = rightNow.get(rightNow.YEAR);
  int invoiceNo = 0;
  boolean disableDate = false;
  if (!fromSelf.equals(""))
  {
    disableDate = true;
    if (fromSelf.equals("numGen"))
    {
      bpSDay = Integer.parseInt(request.getParameter("BPStartDateDay"));
      bpSMonth = Integer.parseInt(request.getParameter("BPStartDateMonth"));
      bpSYear = Integer.parseInt(request.getParameter("BPStartDateYear"));
      bpEDay = Integer.parseInt(request.getParameter("BPEndDateDay"));
      bpEMonth = Integer.parseInt(request.getParameter("BPEndDateMonth"));
      bpEYear = Integer.parseInt(request.getParameter("BPEndDateYear"));
      invoiceNo = BAN.generateAdHocInvoiceNo(bpSDay, bpSMonth, bpSYear);
      if (invoiceNo > 0)
      {
        button1Text = "Confirm";
        button1OnClick = "confirmAndSub();";
        button2Text = "Cancel";
        message = "<font color=blue>Press 'Confirm' to create this ad hoc invoice</font>";
      }
      else
      {
        button1Text = "&nbsp;";
        button1OnClick = "";
        message = BAN.getMessage();
      }
    }
    else if (fromSelf.equals("confirm"))
    {
      /*bpSDay = Integer.parseInt(request.getParameter("bpSDay"));
      bpSMonth = Integer.parseInt(request.getParameter("bpSMonth"));
      bpSYear = Integer.parseInt(request.getParameter("bpSYear"));
      bpEDay = Integer.parseInt(request.getParameter("bpEDay"));
      bpEMonth = Integer.parseInt(request.getParameter("bpEMonth"));
      bpEYear = Integer.parseInt(request.getParameter("bpEYear"));
      String invNo = request.getParameter("invoiceNo");
      invoiceNo = Integer.parseInt(invNo);*/
      bpSDay = Integer.parseInt(request.getParameter("BPStartDateDay"));
      bpSMonth = Integer.parseInt(request.getParameter("BPStartDateMonth"));
      bpSYear = Integer.parseInt(request.getParameter("BPStartDateYear"));
      bpEDay = Integer.parseInt(request.getParameter("BPEndDateDay"));
      bpEMonth = Integer.parseInt(request.getParameter("BPEndDateMonth"));
      bpEYear = Integer.parseInt(request.getParameter("BPEndDateYear"));
      /*invoiceNo = BAN.generateAdHocInvoiceNo(bpSDay, bpSMonth, bpSYear);
      if (invoiceNo > 0)
      {
        int ret = BAN.createAdHocInvoice(Integer.toString(invoiceNo),
          bpSDay, bpSMonth, bpSYear, bpEDay, bpEMonth, bpEYear);*/
        invoiceNo = BAN.generateAdHocInvoice(bpSDay, bpSMonth, bpSYear,
          bpEDay, bpEMonth, bpEYear);
        if (invoiceNo > 0)
        {
          BAN.setInvoiceNo(Integer.toString(invoiceNo));
          adjBAN.setInvoiceNumber(Integer.toString(invoiceNo));
          button1Text = "Adjustment";
          button1OnClick = "adjustment();";
          button2Text = "Exit";
          button2OnClick = "closeRefresh();";
          message = "<font color=blue>Press 'Adjustment' to add an adjustment to this invoice</font>";
        }
        else
        {
          button1Text = "&nbsp;";
          button1OnClick = "";
          button2Text = "Exit";
          message = BAN.getMessage();
        }
      /*}
      else
      {
        button1Text = "&nbsp;";
        button1OnClick = "";
        button2Text = "Exit";
        message = BAN.getMessage();
      }*/
    }
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function adjustment()
{
  createInvoice.fromSelf.value = "adjust";
  createInvoice.submit();
}
function confirmAndSub()
{
  createInvoice.fromSelf.value = "confirm";
  createInvoice.submit();
}
function closeRefresh()
{
  createInvoice.fromSelf.value = "refresh";
  createInvoice.submit();
}
function valAndSub()
{
  createInvoice.button1.disabled = true;
  var dateOK = true;
//alert(createInvoice.BPEndDateDay[createInvoice.BPEndDateDay.selectedIndex].text);
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
    alert("End Date cannot be before Start Date");
  }
  else
  {
    //createInvoice.fromSelf.value = "numGen";
    createInvoice.fromSelf.value = "confirm";
    createInvoice.submit();
  }
}


function window_onload()
{
  window.focus();
<%if (fromSelf.equals("adjust"))
  {%>
  window.opener.location.href = "adHocInvoiceHandler.jsp?ButtonPressed=fromInvoice";
  self.close();
<%}
  else if (fromSelf.equals("refresh"))
  {%>
  window.opener.location.href = "adHocInvoice.jsp";
  self.close();
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="createInvoice" method="post" action="createInvoice.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<input name="gcId" type=hidden value="<%=gcId%>">
<input name="invoiceRegion" type=hidden value="<%=invoiceRegion%>">
<input name="invoiceNo" type=hidden value="<%=invoiceNo%>">
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
      <b>Create Ad Hoc Invoice</b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td><b>Customer:</b></td>
          <td class=bluebold><%=gcId%></td>
        </tr>
        <tr>
          <td><b>Invoice Region:</b></td>
          <td class=bluebold><%=invoiceRegion%></td>
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
        <tr style="visibility:<%=invoiceNo>0?"visible":"hidden"%>">
          <td><b>Invoice Number:</b></td>
          <td class=bluebold><%=invoiceNo%></td>
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


