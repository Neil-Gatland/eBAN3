<html>
<head>
  <title>Create Ad Hoc Invoice</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String message = "<font color=blue>Enter the relevant details and press 'Create' to begin</font>";
  String spId = request.getParameter("spId");
  String spName = request.getParameter("spName");
  String invoiceCurrency = request.getParameter("invoiceCurrency");
  String invoiceDesc = "";
  String invoiceCRDE = "";
  double invoiceAmt = 0;
  String gsoBillingPeriod = "";
  String gsoEndCustomer = "";
  String button1Text = "Create";
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  Calendar rightNow = Calendar.getInstance();
  int tpDay = rightNow.get(rightNow.DATE);
  int tpMonth = rightNow.get(rightNow.MONTH) + 1;
  int tpYear = rightNow.get(rightNow.YEAR);
  String today = Integer.toString(tpYear) + (tpMonth<10?"0":"") + Integer.toString(tpMonth) +
    (tpDay<10?"0":"") + Integer.toString(tpDay);
  String tpDate = SU.isNull(null,"Today");
  int invoiceNo = 0;
  boolean disableDate = false;
  String selectStyle = " style=\"width:300px\"";
  if (!fromSelf.equals(""))
  {
    tpDay = Integer.parseInt(request.getParameter("tpDayh"));
    tpMonth = Integer.parseInt(request.getParameter("tpMonthh"));
    tpYear = Integer.parseInt(request.getParameter("tpYearh"));
    invoiceDesc = request.getParameter("invoiceDesch");
    invoiceCRDE = request.getParameter("invoiceCRDEh");
    invoiceAmt = Double.parseDouble(request.getParameter("invoiceAmth"));
    gsoBillingPeriod = request.getParameter("gsoBillingPeriodh");
    gsoEndCustomer = request.getParameter("gsoEndCustomerh");
    disableDate = true;
    if (fromSelf.equals("reflect"))
    {
      button1Text = "Confirm";
      button1OnClick = "confirmAndSub();";
      button2Text = "Cancel";
      message = "<font color=blue>Press 'Confirm' to create this ad hoc invoice</font>";
    }
    else if (fromSelf.equals("confirm"))
    {
      double invAmt = (((invoiceCRDE.equals("Credit"))&&(invoiceAmt>0))||
        ((invoiceCRDE.equals("Debit"))&&(invoiceAmt<0)))?invoiceAmt *= -1:invoiceAmt;
/*      if (((invoiceCRDE.equals("Credit")) && (invoiceAmt > 0)) ||
        ((invoiceCRDE.equals("Debit")) && (invoiceAmt < 0)))
      {
        invoiceAmt *= -1;
      }*/
      long ret = BAN.createGSOAdHocInvoice(spName, spId, gsoEndCustomer,
        invAmt, invoiceCRDE, tpDay, tpMonth, tpYear, gsoBillingPeriod,
        invoiceDesc);
      if (ret > 0)
      {
        //BAN.setInvoiceNo(invNo);
        button1Text = "&nbsp;";
        button1OnClick = "";
        button2Text = "Exit";
        button2OnClick = "closeRefresh();";
        message = "<font color=blue>Invoice number " + ret + " created</font>";
      }
      else
      {
        button1Text = "&nbsp;";
        button1OnClick = "";
        button2Text = "Exit";
        message = BAN.getMessage();
      }
    }
  }
  StringBuffer invoiceAmount = new StringBuffer(String.valueOf(invoiceAmt));
  if (invoiceAmount.charAt(invoiceAmount.length()-2) == '.')
  {
    invoiceAmount.append("0");
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function confirmAndSub()
{
  createGSOInvoice.fromSelf.value = "confirm";
  createGSOInvoice.submit();
}
function closeRefresh()
{
  createGSOInvoice.fromSelf.value = "refresh";
  createGSOInvoice.submit();
}
function valAndSub()
{
  var strDesc = createGSOInvoice.invoiceDesc.value;
  strDesc = trimAll(strDesc);
  var strAmt = createGSOInvoice.invoiceAmt.value;
  var numAmt = Number(strAmt);
  var tpDateStr = createGSOInvoice.TPDateYear[createGSOInvoice.TPDateYear.selectedIndex].text +
    (createGSOInvoice.TPDateMonth[createGSOInvoice.TPDateMonth.selectedIndex].value<10?"0":"") +
    createGSOInvoice.TPDateMonth[createGSOInvoice.TPDateMonth.selectedIndex].value +
    (createGSOInvoice.TPDateDay[createGSOInvoice.TPDateDay.selectedIndex].text<10?"0":"") +
    createGSOInvoice.TPDateDay[createGSOInvoice.TPDateDay.selectedIndex].text;
  if (createGSOInvoice.gsoEndCustomer.selectedIndex == 0)
  {
    alert("Please select an end customer");
  }
  else if (createGSOInvoice.invoiceCRDE.selectedIndex == 0)
  {
    alert("Please select credit or debit");
  }
  else if (createGSOInvoice.gsoBillingPeriod.selectedIndex == 0)
  {
    alert("Please select a billing period");
  }
  else if (createGSOInvoice.invoiceDesc.value == "")
  {
    alert("Please add a description");
  }
  else if(strDesc.length == 0)
  {
    alert("Description cannot be blank");
  }
  else if (validateNumeric(strAmt) == false)
  {
    alert("Amount must be numeric");
  }
  else if (numAmt == '0')
  {
    alert("Amount must greater than zero");
  }
  else if (tpDateStr > '<%=today%>')
  {
    alert("Tax Point Date cannot be in the future");
  }
  else if (tpDateStr < createGSOInvoice.gsoBillingPeriod[createGSOInvoice.gsoBillingPeriod.selectedIndex].value)
  {
    alert("Tax Point Date cannot predate the selected billing period");
  }

  createGSOInvoice.tpDayh.value = createGSOInvoice.TPDateDay[createGSOInvoice.TPDateDay.selectedIndex].text;
  createGSOInvoice.tpMonthh.value = createGSOInvoice.TPDateMonth[createGSOInvoice.TPDateMonth.selectedIndex].value;
  createGSOInvoice.tpYearh.value = createGSOInvoice.TPDateYear[createGSOInvoice.TPDateYear.selectedIndex].text;
  createGSOInvoice.invoiceCRDEh.value = createGSOInvoice.invoiceCRDE[createGSOInvoice.invoiceCRDE.selectedIndex].value;
  createGSOInvoice.invoiceAmth.value = createGSOInvoice.invoiceAmt.value;
  createGSOInvoice.invoiceDesch.value = createGSOInvoice.invoiceDesc.value;
  createGSOInvoice.gsoBillingPeriodh.value = createGSOInvoice.gsoBillingPeriod[createGSOInvoice.gsoBillingPeriod.selectedIndex].value;
  createGSOInvoice.gsoEndCustomerh.value = createGSOInvoice.gsoEndCustomer[createGSOInvoice.gsoEndCustomer.selectedIndex].value;
  createGSOInvoice.fromSelf.value = "reflect";
  createGSOInvoice.submit();
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
  window.opener.location.href = "gsoDesktop.jsp";
  self.close();
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="createGSOInvoice" method="post" action="createGSOInvoice.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<input name="spId" type=hidden value="<%=spId%>">
<input name="spName" type=hidden value="<%=spName%>">
<input name="invoiceCurrency" type=hidden value="<%=invoiceCurrency%>">
<input name="invoiceNo" type=hidden value="<%=invoiceNo%>">
<input name="tpDayh" type=hidden value="<%=tpDay%>">
<input name="tpMonthh" type=hidden value="<%=tpMonth%>">
<input name="tpYearh" type=hidden value="<%=tpYear%>">
<input name="invoiceCRDEh" type=hidden value="<%=invoiceCRDE%>">
<input name="invoiceAmth" type=hidden value="<%=invoiceAmount.toString()%>">
<input name="invoiceDesch" type=hidden value="<%=invoiceDesc%>">
<input name="gsoBillingPeriodh" type=hidden value="<%=gsoBillingPeriod%>">
<input name="gsoEndCustomerh" type=hidden value="<%=gsoEndCustomer%>">
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
          <td><b>Service Partner:</b></td>
          <td class=bluebold><%=spName%></td>
        </tr>
        <tr>
          <td><b>Invoice Currency:</b></td>
          <td class=bluebold><%=invoiceCurrency%></td>
        </tr>
        <tr>
          <td><b>End Customer:</b></td>
          <td>
            <%=DBA.getListBox("gsoEndCustomer",(disableDate?"disabled":"input"),gsoEndCustomer,spId,1,selectStyle,true)%>
          </td>
        </tr>
        <tr>
          <td><b>Tax Point Date:</b></td>
          <td>
            <%=HB.getDays("createGSOInvoice", "TPDate", tpDay, false, disableDate, tpMonth, tpYear)%>
            <%=HB.getMonths("createGSOInvoice", "TPDate", tpMonth, false, disableDate)%>
            <%=HB.getYears("createGSOInvoice", "TPDate", tpYear, false, disableDate)%>
            <input type="hidden" name="TPDateh" value="<%=tpDate%>">
          </td>
        </tr>
        <tr>
          <td><b>Amount:</b></td>
          <td>
            <input style="width:300px;height:18px;font-size:xx-small;" type=text name="invoiceAmt" value="<%=invoiceAmount.toString()%>" <%=disableDate?"READONLY":""%>>
          </td>
        </tr>
        <tr>
          <td><b>Credit/Debit:</b></td>
          <td>
            <%=DBA.getListBox("invoiceCRDE",(disableDate?"disabled":"input"),invoiceCRDE,"Credit Debit",1,selectStyle,true)%>
          </td>
        </tr>
        <tr>
          <td><b>Description:</b></td>
          <td>
            <input style="width:300px;height:18px;font-size:xx-small;" type=text name="invoiceDesc" value="<%=invoiceDesc%>" <%=disableDate?"READONLY":""%>>
          </td>
        </tr>
        <tr>
          <td><b>Billing Period:</b></td>
          <td>
            <%=DBA.getListBox("gsoBillingPeriod",(disableDate?"disabled":"input"),gsoBillingPeriod,spName,1,selectStyle,true)%>
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
      <input class=button type=button value="<%=button1Text%>" style="width:90px" onClick="<%=button1OnClick%>">
    </td>
    <td align=left>
      <input class=button type=button value="<%=button2Text%>" style="width:90px" onClick="<%=button2OnClick%>">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


