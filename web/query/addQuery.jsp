<%@ page import="java.util.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<html>
<head>
<title>Raise a query</title>
<%
  Enumeration en = request.getParameterNames();
  StringBuffer sb = new StringBuffer();
  boolean isGSO = false;
  while (en.hasMoreElements())
  {
    String pName = (String)en.nextElement();
    if (pName.equals("gso"))
    {
      isGSO = true;
    }
    sb.append("<input name=\"" + pName + "\" type=\"hidden\" value=\"" +
      request.getParameter(pName) + "\">");
  }
  String customer = isGSO?BAN.getSPName():BAN.getGlobalCustomerId();
  boolean gcIdNotSet = customer.equals("");
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function onLoad()
{
  window.focus();
}

function okClick()
{
  if (frmQuery.queryText.value=='')
  {
    alert("Please enter the details of your query in the box marked 'Query Details'.");
  }
<%if (gcIdNotSet)
  {%>
  else if (frmQuery.frmq_GCId.value=='')
  {
    alert("Please enter the Global Customer Id or 'na' if it is not relevant to your query.");
  }
  else if (frmQuery.frmq_AccId.value=='')
  {
    alert("Please enter the Account Id or 'na' if it is not relevant to your query.");
  }
  else if (frmQuery.frmq_InvId.value=='')
  {
    alert("Please enter the Invoice No or 'na' if it is not relevant to your query.");
  }
<%}%>
  else
  {
    frmQuery.submit();
  }
}

function cancelClick()
{
  window.close();
}
</script>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<body onload="onLoad()">
<form name="frmQuery" action="../query/addQueryHandler.jsp" method="post">
<%=sb.toString()%>
<table width="90%" align="center">
  <tr>
    <td valign="top" colspan="2">
      <h3 align="center"><b>Raise a query</b></h3>
    </td>
  </tr>
  <tr>
    <td valign="top" colspan="2">
      <b>Please enter the details of your query in the box below and press 'OK' to submit the query,
      or 'Cancel' to exit without submitting</b>
    </td>
  </tr>
  <tr>
    <td valign="top" colspan="2">&nbsp;
    </td>
  </tr>
  <tr>
    <td valign="top" colspan="2">
      Query Details:
    </td>
  </tr>
  <tr>
    <td align="center" valign="top" colspan="2">
      <textarea name="queryText" cols="70" rows="8"></textarea>
    </td>
  </tr>
<%if (gcIdNotSet)
  {%>
  <tr>
    <td align="right" valign="top" width="50%">
      Global Customer Id:
    </td>
    <td align="left" valign="top">
      <input type="text" name="frmq_GCId">
    </td>
  </tr>
  <tr>
    <td align="right" valign="top">
      Account Id:
    </td>
    <td align="left" valign="top">
      <input type="text" name="frmq_AccId">
    </td>
  </tr>
  <tr>
    <td align="right" valign="top">
      Invoice No:
    </td>
    <td align="left" valign="top">
      <input type="text" name="frmq_InvId">
    </td>
  </tr>
<%}
  else
  {%>
  <tr>
    <td colspan=2>&nbsp;</td>
  </tr>
  <tr>
    <td align="right" valign="top" width="50%">
      <b>Global Customer Id:</b>
    </td>
    <td align="left" valign="top">
      <b><%=customer%></b>
    </td>
  </tr>
  <tr>
    <td colspan=2>&nbsp;</td>
  </tr>
<%}%>
  <tr>
    <td align="right" valign="top">
      <input class=button type=button value="Submit" style="width:90px" onClick="okClick()">
    </td>
    <td align="left" valign="top">
      <input class=button type=button value="Cancel" style="width:90px" onClick="cancelClick()">
    </td>
  </tr>
</table>
</body>
</form>
</html>


