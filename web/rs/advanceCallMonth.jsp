<html>
<head>
  <title>Advance From Call Month</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String selectStyle = " style=\"width:400px\"";
  String message ="<font color=blue><b>Please select From Call Month and then press 'Update'</b></font>";
  String fromCallMonth = "";
  String button1Text = "Update";
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  if (!fromSelf.equals(""))
  {
    fromCallMonth = request.getParameter("fromCallMonth");
    RSB.setNewFromCallMonth(fromCallMonth);
    if (fromSelf.equals("update"))
    {
      if (!RSB.callMonthExists())
      {
        if (RSB.advanceRatingSchemeCallMonth())
        {
          RSB.setFromCallMonth(fromCallMonth);
          RSB.populateRatingSchemeList();
        }
      }
      else
      {
        fromSelf = "isThere";
        message ="<font color=blue><b>This combination exists already</b></font>";
      }
    }
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function valAndSub()
{
  advanceCallMonth.button1.disabled = true;
<%if (fromCallMonth.equals(""))
  {%>
    alert("Please select a From Call Month");
<%}
  else
  {%>
    advanceCallMonth.button1.disabled = false;
    advanceCallMonth.fromSelf.value = "update";
    advanceCallMonth.submit();
<%}%>
}


function window_onload()
{
  window.focus();
<%if (fromSelf.equals("update"))
  {%>
  window.opener.location.href = "ratingSchemeMenu.jsp";
  self.close();
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="advanceCallMonth" method="post" action="advanceCallMonth.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Advance From Call Month for <%=RSB.getFromCallMonth()%> <%=RSB.getNameOrNumber()%> <%=RSB.getProductCode()%> <%=RSB.getRatingType()%> <%=RSB.getAccountNumber()%></b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td align=right colspan=2><font color="#0000FF">New From Call Month:</font>
          </td>
          <td  align=left>
          <%=DBA.getListBox("fromCallMonth","submit",RSB.getNewFromCallMonth(),RSB.getMinCallMonth(),1)%>
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


