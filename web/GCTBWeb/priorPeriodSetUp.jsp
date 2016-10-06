<html>
<head>
  <title>Prior Period Set-Up</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="JavaUtil.StringUtil"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="ccBAN" class="DBUtilities.ConglomCustomerBANBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String message = "<font color=blue>Select Prior Period to set up and press 'OK'</font>";
  long conglomCustId = ccBAN.getConglomCustomerId();
  String conglomCustName = ccBAN.getCustomerName();
  String priorPeriod = "";
  String billingFreq = ccBAN.getBillingFrequency();
  String billingRefPrefix = ccBAN.getBillingRefPrefix();
  Collection billedProducts = null;
  String button1Title = "";
  String button1Text = "OK";
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  boolean disablePP = false;
  if (!fromSelf.equals(""))
  {
    billedProducts = ccBAN.getBilledProducts();
    button1Title = "";
    if (fromSelf.equals("refresh"))
    {
    }
    else if (fromSelf.equals("confirm"))
    {
      disablePP = true;
      priorPeriod = request.getParameter("conglomPriorPeriods");
      message = "<font color=blue>Press 'Confirm' to set up the selected prior billing period</font>";
      button1Text = "Confirm";
      button1OnClick = "confirmSub();";
      button2Text = "Cancel";
    }
    else if (fromSelf.equals("setup"))
    {
      priorPeriod = request.getParameter("priorPeriod");
      int ret = ccBAN.setUpPriorPeriod(priorPeriod);
      BAN.setMessage(ccBAN.getMessage());
      if (ret == 0)
      {
        BAN.getConglomCustomerSummary();
      }
    }
  }
  else
  {
    billedProducts = ccBAN.findBilledProducts();
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function viewLog()
{
  window.open("../GCTBWeb/billingLog.jsp?conglom=true&fromSelf=true", "mblog", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=400,width=800,top=50,left=50");
  this.close();
}
function confirmSub()
{
  priorPeriodSetUp.fromSelf.value = "setup";
  priorPeriodSetUp.submit();
}
function refreshSub()
{
  priorPeriodSetUp.fromSelf.value = "refresh";
  priorPeriodSetUp.submit();
}
function valAndSub()
{
  if (priorPeriodSetUp.conglomPriorPeriods.selectedIndex == 0)
  {
    alert("Please select a prior period");
  }
  else
  {
    priorPeriodSetUp.fromSelf.value = "confirm";
    priorPeriodSetUp.submit();
  }
}


function window_onload()
{
  window.focus();
<%if (fromSelf.equals("setup"))
  {%>
  window.opener.location.href = "conglomBillingMenu.jsp";
  self.close();
<%}
  else if (fromSelf.equals("refresh"))
  {%>
  //window.opener.location.href = "adHocInvoice.jsp";
  //self.close();
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="priorPeriodSetUp" method="post" action="priorPeriodSetUp.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="refresh">
<input name="priorPeriod" type=hidden value="<%=priorPeriod%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Prior Period Set-Up</b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td><b>Customer:</b></td>
          <td class=bluebold><%=conglomCustName%></td>
        </tr>
        <tr>
          <td><b>Billing Frequency:</b></td>
          <td class=bluebold><%=billingFreq%></td>
          </td>
        </tr>
        <tr>
          <td><b>Billing Ref. Prefix:</b></td>
          <td class=bluebold><%=billingRefPrefix%></td>
          </td>
        </tr>
        <tr>
          <td><b>Prior Periods:</b></td>
          <td>
            <%=ccBAN.getPriorPeriodsListBox(priorPeriod, disablePP)%>
          </td>
        </tr>
<%boolean showTitle = true;
  for(Iterator it = billedProducts.iterator(); it.hasNext(); )
  {%>
        <tr>
          <td><%=showTitle?"<b>Billed Products:</b>":"&nbsp;"%></td>
          <td class=bluebold><%=(String)it.next()%></td>
          </td>
        </tr>
<%  showTitle = false;
  }%>
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
      <input class=button title="<%=button1Title%>" type=button value="<%=button1Text%>" style="width:90px" onClick="<%=button1OnClick%>">
    </td>
    <td align=left>
      <input class=button type=button value="<%=button2Text%>" style="width:90px" onClick="<%=button2OnClick%>">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


