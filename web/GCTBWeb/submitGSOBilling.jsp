<html>
<head>
  <title>GSO Billing Submission</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String button1Text = "Submit";
  String button2Text = "Cancel";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  String message = "";
  String extractStatus = "";
  String trialStatus = "";
  String closeStatus = "";
  String extractChecked = "";
  String trialChecked = "";
  String closeChecked = "";
  if (fromSelf.equals(""))
  {
    message = BAN.determineGSOSubmissionOptions();
    extractChecked = BAN.getGSOButtonChecked("GCD Extract");
    extractStatus = BAN.getGSOExtractButtonStatus();
    trialChecked = BAN.getGSOButtonChecked("Trial");
    trialStatus = BAN.getGSOTrialButtonStatus();
    closeChecked = BAN.getGSOButtonChecked("Close");
    closeStatus = BAN.getGSOCloseButtonStatus();
    if ((extractStatus.equals("disabled")) && (trialStatus.equals("disabled")) &&
      (closeStatus.equals("disabled")))
    {
      button1Text = "&nbsp;";
      button1OnClick = "";
      button2Text = "Exit";
    }
  }
  else
  {
    extractChecked = request.getParameter("extractChecked");
    closeChecked = request.getParameter("closeChecked");
    trialChecked = request.getParameter("trialChecked");
    if (fromSelf.equals("reflect"))
    {
      button1Text = "Confirm";
      button1OnClick = "confirmAndSub();";
      message = "Click 'Confirm' to confirm submission or 'Cancel' to exit without submitting";
      extractStatus = "disabled";
      trialStatus = "disabled";
      closeStatus = "disabled";
    }
    else if (fromSelf.equals("confirm"))
    {
      button1Text = "";
      button1OnClick = "";
      button2Text = "Exit";
      button2OnClick = "closeRefresh();";
      String mode = extractChecked.equals("checked")?"GCD Extract":
        trialChecked.equals("checked")?"Trial":"Close";
      long ret = BAN.submitGSOJobRequest(mode);
      if (ret == 0)
      {
        message = BAN.determineGSOSubmissionOptions();
        extractChecked = BAN.getGSOButtonChecked("GCD Extract");
        extractStatus = BAN.getGSOExtractButtonStatus();
        trialChecked = BAN.getGSOButtonChecked("Trial");
        trialStatus = BAN.getGSOTrialButtonStatus();
        closeChecked = BAN.getGSOButtonChecked("Close");
        closeStatus = BAN.getGSOCloseButtonStatus();
      }
      else
      {
        message = BAN.getMessage();
        BAN.setMessage("");
        extractStatus = "disabled";
        trialStatus = "disabled";
        closeStatus = "disabled";
        extractChecked = "";
        closeChecked = "";
        trialChecked = "";
      }
    }
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function confirmAndSub()
{
  submitGSOBilling.fromSelf.value = "confirm";
  submitGSOBilling.submit();
}
function closeRefresh()
{
  submitGSOBilling.fromSelf.value = "refresh";
  submitGSOBilling.submit();
}
function valAndSub()
{
  if (submitGSOBilling.mbMode[0].checked == true)
  {
    submitGSOBilling.extractChecked.value = "checked";
    submitGSOBilling.trialChecked.value = "";
    submitGSOBilling.closeChecked.value = "";
  }
  else if (submitGSOBilling.mbMode[1].checked == true)
  {
    submitGSOBilling.extractChecked.value = "";
    submitGSOBilling.trialChecked.value = "checked";
    submitGSOBilling.closeChecked.value = "";
  }
  else if (submitGSOBilling.mbMode[2].checked == true)
  {
    submitGSOBilling.extractChecked.value = "";
    submitGSOBilling.trialChecked.value = "";
    submitGSOBilling.closeChecked.value = "checked";
  }
  else
  {
    submitGSOBilling.extractChecked.value = "";
    submitGSOBilling.trialChecked.value = "";
    submitGSOBilling.closeChecked.value = "";
  }
  submitGSOBilling.fromSelf.value = "reflect";
  submitGSOBilling.submit();
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
<form name="submitGSOBilling" method="post" action="submitGSOBilling.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<input name="extractChecked" type=hidden value="<%=extractChecked%>">
<input name="trialChecked" type=hidden value="<%=trialChecked%>">
<input name="closeChecked" type=hidden value="<%=closeChecked%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>GSO Billing Submission</b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      &nbsp;
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td></td>
          <td><b>GCD Extract</b></td>
          <td><input type="radio" name="mbMode" value="GCD Extract" <%=extractChecked%> <%=extractStatus%>></td>
        </tr>
        <tr>
          <td><b>Run Mode:</b></td>
          <td><b>Trial</b></td>
          <td><input type="radio" name="mbMode" value="Trial" <%=trialChecked%> <%=trialStatus%>></td>
        </tr>
        <tr>
          <td></td>
          <td><b>Close</b></td>
          <td><input type="radio" name="mbMode" value="Close" <%=closeChecked%> <%=closeStatus%>></td>
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


