<html>
<head>
  <title>Enter actual CR/Docket number</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="JavaUtil.StringUtil"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String interimNo = request.getParameter("interimNo");
  String message = "<font color=blue>Press 'OK' to " +
    "update the item</font>";
  String conglomCustName = BAN.getGlobalCustomerId() + " " +
    BAN.getConglomCustomerName() + " (" + BAN.getConglomCustomerId() + ")";
  String button1Title = "";
  String button1Text = "OK";
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  String product = request.getParameter("product");
  String sourceAccount = request.getParameter("sourceAccount");
  String actualNo = "";
  boolean success = false;
  if (!fromSelf.equals(""))
  {
    actualNo = request.getParameter("actualNo");
    success = BAN.updateCRDocket(interimNo, actualNo);
    message = BAN.getMessage();
  }
  else
  {
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function valAndSub()
{
  if (updateCRDocket.actualNo.value == "")
  {
    alert('Please enter an actual CR/Docket number');
  }
  else
  {
    updateCRDocket.fromSelf.value = "update";
    updateCRDocket.submit();
  }
}


function window_onload()
{
  window.focus();
<%if ((fromSelf.equals("update")) && (success))
  {%>
  window.opener.location.href = "listConglomCRDocket.jsp";
  self.close();
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="updateCRDocket" method="post" action="updateCRDocket.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="refresh">
<input name="interimNo" type=hidden value="<%=interimNo%>">
<input name="product" type=hidden value="<%=product%>">
<input name="sourceAccount" type=hidden value="<%=sourceAccount%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Enter actual CR/Docket number</b>
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
          <td><b>Product:</b></td>
          <td class=bluebold><%=product%></td>
          </td>
        </tr>
        <tr>
          <td><b>Source Account:</b></td>
          <td class=bluebold><%=sourceAccount%></td>
          </td>
        </tr>
        <tr>
          <td><b>Interim No:</b></td>
          <td class=bluebold><%=interimNo%></td>
          </td>
        </tr>
        <tr>
          <td><b>Actual No:</b></td>
          <td class=bluebold><input name="actualNo" value="<%=actualNo%>"></td>
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


