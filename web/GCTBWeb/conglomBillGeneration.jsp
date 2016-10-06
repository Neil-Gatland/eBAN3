<html>
<head>
  <title>Generate Conglomerate Bill</title>
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
  String billType = request.getParameter("billType");
  boolean close = billType.equals("Close");
  String message = "<font color=blue>Press 'OK' to " + (close?"close":"trial") +
    " the bill</font>";
  Calendar now = Calendar.getInstance();
  int dd = now.get(now.DATE);
  int mm = now.get(now.MONTH) + 1;
  int yyyy = now.get(now.YEAR);
  String billedDate = (dd<10?"0":"") + dd + "/" + (mm<10?"0":"") + mm + "/" + yyyy;
  String billingRef = BAN.getConglomInvoiceRef();
  String services = null;
  long conglomCustId = ccBAN.getConglomCustomerId();
  String conglomCustName = BAN.getGlobalCustomerId() + " " +
    ccBAN.getCustomerName() + " (" + conglomCustId + ")";
  String button1Title = "";
  String button1Text = "OK";
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  Collection billedProducts = null;
  if (!fromSelf.equals(""))
  {
    billedProducts = ccBAN.getBilledProducts();
    button1Title = "";
    if (fromSelf.equals("refresh"))
    {
    }
    else if (fromSelf.equals("confirm"))
    {
      message = "<font color=blue>Please 'Confirm' that you wish to " +
        (close?"close":"trial") + " the bill</font>";
      button1Text = "Confirm";
      button1OnClick = "confirmSub();";
      button2Text = "Cancel";
    }
    else if (fromSelf.equals("generate"))
    {
      ccBAN.setUserId((String)session.getAttribute("User_Id"));
      int ret = ccBAN.generateBill(close);
      BAN.setMessage(ccBAN.getMessage());
      if ((ret == 0) || (ret == -28))
      {
        BAN.getConglomCustomerSummary();
      }
    }
  }
  else
  {
    billedProducts = ccBAN.getBilledProductsHtml(close);
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function confirmSub()
{
  conglomBillGeneration.fromSelf.value = "generate";
  conglomBillGeneration.submit();
}
function refreshSub()
{
  conglomBillGeneration.fromSelf.value = "refresh";
  conglomBillGeneration.submit();
}
function valAndSub()
{
  conglomBillGeneration.fromSelf.value = "confirm";
  conglomBillGeneration.submit();
}


function window_onload()
{
  window.focus();
<%if (fromSelf.equals("generate"))
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
<form name="conglomBillGeneration" method="post" action="conglomBillGeneration.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="refresh">
<input name="billType" type=hidden value="<%=billType%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Generate <%=billType%> Conglomerate Bill</b>
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
          <td><b>Billed:</b></td>
          <td class=bluebold><%=billedDate%></td>
          </td>
        </tr>
        <tr>
          <td><b>Billing Ref.:</b></td>
          <td class=bluebold><%=billingRef%></td>
          </td>
        </tr>
        <tr>
          <td colspan=2><b>The following Services will be billed this period:</b></td>
        </tr>
        <tr>
          <td colspan=2>
            <table width=100% border=1>
              <tr>
                <td><b>Description</b></td>
<%if (close)
  {%>
                <td><b>Net</b></td>
                <td><b>VAT</b></td>
                <td><b>Total Due</b></td>
<%}
  else
  {%>
                <td><b>Source Invoices</b></td>
<%}%>
              </tr>
<%for(Iterator it = billedProducts.iterator(); it.hasNext(); )
  {%>
              <%=(String)it.next()%>
<%}%>
            </table>
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


