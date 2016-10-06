<html>
<head>
  <title>Adjustment Line Maintenance</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="ADJ" class="DBUtilities.AdjustmentBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String message = "";
  String button1Text = "";
  String action = "";
  String mode = "";
  if (ADJ.getLineAction().equals("Add"))
  {
    message = "<font color=blue><b>Please enter a description and amount, and then press 'Add'</b></font>";
    button1Text = "Add";
    action = "Add";
  }
  else
  {
    message = "<font color=blue><b>Please amend description and amount as required, and then press 'Update'</b></font>";
    button1Text = "Update";
    action = "Update";
  }
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  if (!fromSelf.equals(""))
  {
    if (fromSelf.equals("init"))
    {
      ADJ.setAdjustmentLineDescription(request.getParameter("adjustmentLineDescription"));
      ADJ.setAdjustmentLineAmount(request.getParameter("adjustmentLineAmount"));
      message ="<font color=blue><b>Press 'Confirm' to complete the operation or 'Exit' to cancel</b></font>";
      button1Text = "Confirm";
      button1OnClick = "confirmAndSub();";
      mode = "readonly";
    }
    else if (fromSelf.equals("confirm"))
    {
      boolean done = false;
      String msg = "";
      if (ADJ.getLineAction().equals("Add"))
      {
        done = ADJ.createAdjustmentInvoiceLine();
        msg = "<font color=blue><b>Adjustment Line added</b></font>";
      }
      else
      {
        msg = "<font color=blue><b>Adjustment Line updated</b></font>";
        done = ADJ.updateAdjustmentInvoiceLine();
      }
      if (done)
      {
        ADJ.populateAdjustmentLineList();
        ADJ.updateAdjustmentInvoiceAmounts();
        ADJ.setMessage(msg);
      }
    }
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function confirmAndSub()
{
  adjustmentLineMaint.fromSelf.value = "confirm";
  adjustmentLineMaint.submit();
}
function valAndSub()
{
  adjustmentLineMaint.button1.disabled = true;
  if ((adjustmentLineMaint.adjustmentLineDescription.value == "") ||
    (adjustmentLineMaint.adjustmentLineAmount.value == ""))
  {
    alert("Please enter both description and amount");
  }
  else
  {
    adjustmentLineMaint.button1.disabled = false;
    adjustmentLineMaint.fromSelf.value = "init";
    adjustmentLineMaint.submit();
  }
}


function window_onload()
{
  window.focus();
<%if (fromSelf.equals("confirm"))
  {%>
  window.opener.location.href = "adjustmentMaintHandler.jsp?ButtonPressed=lineMaint";
  self.close();
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="adjustmentLineMaint" method="post" action="adjustmentLineMaint.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="<%=fromSelf%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b><%=action%> Adjustment Line for invoice <%=ADJ.getInvoiceNumber()%></b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td align=left colspan=2><font color="#0000FF">Adjustment Description:</font>
          </td>
          <td  align=left>
          <input style="height:18px;font-size:xx-small;width:80%" maxlength=200 type=text name="adjustmentLineDescription" value="<%=ADJ.getAdjustmentLineDescription()%>" <%=mode%>>
          </td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td align=left colspan=2><font color="#0000FF">Adjustment Amount:</font>
          </td>
          <td  align=left>
          <input style="height:18px;font-size:xx-small;" type=text name="adjustmentLineAmount" value="<%=ADJ.getAdjustmentLineAmount()%>" <%=mode%>>
          </td>
          <td>&nbsp;</td>
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


