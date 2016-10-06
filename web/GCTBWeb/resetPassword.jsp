<html>
<head>
  <title>Change Password</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.net.URLEncoder"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%
  String userId = request.getParameter("username");
  String message = "<font color=blue>A temporary password will be sent to the email address registered to the user id entered below.</font>";
  String disabled = "";
  if (request.getParameter("fromSelf") != null)
  {
    BAN.resetPassword(userId);
    message = BAN.getMessage();
    disabled = "disabled style=\"background-color:gray\"";
  }
%>
<script language="JavaScript">
function valAndSub()
{
//alert(changePassword.current.value.length);
  if (resetPassword.username.value == "")
  {
    alert("Please enter your user id.");
  }
  else
  {
    resetPassword.submit();
  }
}

function window_onload()
{
  window.focus();
}
</script>
<body language=javascript onload="return window_onload()">
<form name="resetPassword" method="post" action="resetPassword.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Reset Password</b>
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center>
      <b><%=message%></b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td><b>User Id:</b></td>
          <td><input name=username size=7 style="WIDTH: 90px" value="<%=userId%>"></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td align=right>
      <input class=button type=button value=Submit onClick="valAndSub();" <%=disabled%>>
    </td>
    <td align=left>
      <input class=button type=button value=Close onClick="window.close();">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


