<html>
<head>
  <title>Change Password</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.net.URLEncoder"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  String userId = (String)session.getAttribute("User_Id");
  //String fromLogin = request.getParameter("fromLogin");
  boolean isRS = BAN.getBillingTeam().equals("Revenue");
  String target = isRS?"../rs/rsDesktopHandler.jsp?ButtonPressed=refresh&from=pwd"
    :"newDesktopHandler.jsp?ButtonPressed=refresh";
  String message = "";
  String passwordStatus = request.getParameter("passwordStatus");
  /*if (passwordStatus != null)
  {
    if (passwordStatus.equals("a"))
    {
      message = "<font color=blue>Password has been reset and must be changed</font>";
    }
    if (passwordStatus.equals("b"))
    {
      message = "<font color=blue>Password has expired and must be changed</font>";
    }
  }*/
  if (request.getParameter("fromSelf") != null)
  {
    BAN.updatePassword(userId, request.getParameter("current"),
            request.getParameter("newpass"));//, false);
    message = BAN.getMessage();
    //session.setAttribute("Message", message);
    /*if (!fromLogin.equals("null"))
    {
      target = "Authenticate.jsp?passChange=true&username=" + userId + "&userpass="+
        URLEncoder.encode(request.getParameter("newpass"));
    }*/
  }
%>
<script language="JavaScript">
function valAndSub()
{
//alert(changePassword.current.value.length);
  if (changePassword.current.value == "")
  {
    alert("Please enter your current password.");
  }
  else if (changePassword.newpass.value == "")
  {
    alert("Please enter your new password.");
  }
  else if (changePassword.confirm.value == "")
  {
    alert("Please confirm your new password.");
  }
  else if (changePassword.confirm.value != changePassword.newpass.value)
  {
    alert("New password and confirm values differ.");
  }
  else if (changePassword.newpass.value.length < 8)
  {
    alert("New password must be at least 8 characters long.");
  }
  else if (!alphanumeric(changePassword.newpass.value))
  {
    alert("New password can only consist of alphanumeric characters (a-z, 0-9).");
  }
  /*else if (foundConsecutive(changePassword.newpass.value))
  {
    alert("New password must not contain the same characters consecutively.");
  }
  else if ((!foundSymbol(changePassword.newpass.value)) ||
    (!foundNumber(changePassword.newpass.value)) ||
    (!foundUpper(changePassword.newpass.value)))
  {
    alert("New password must contain at least one capital letter, one number and one symbol.");
  }*/
  else
  {
    changePassword.submit();
  }
}

function alphanumeric(alphane)
{
  var numaric = alphane;
  for(var j=0; j<numaric.length; j++)
  {
    var alphaa = numaric.charAt(j);
    var hh = alphaa.charCodeAt(0);
    if((hh > 47 && hh < 58) || (hh > 64 && hh < 91) || (hh > 96 && hh < 123))
    {
    }
    else
    {
      return false;
    }
  }
  return true;
}

function foundSymbol(alphane)
{
  var numaric = alphane;
  for(var j=0; j<numaric.length; j++)
  {
    var alphaa = numaric.charAt(j);
    var hh = alphaa.charCodeAt(0);
    if ((hh >= 33 && hh <= 47) || (hh >= 58 && hh <= 64) || (hh >= 91 && hh <= 96))
    {
      return true;
    }
  }
  return false;
}

function foundNumber(alphane)
{
  var numaric = alphane;
  for(var j=0; j<numaric.length; j++)
  {
    var alphaa = numaric.charAt(j);
    var hh = alphaa.charCodeAt(0);
    if (hh >= 48 && hh <= 57)
    {
      return true;
    }
  }
  return false;
}

function foundUpper(alphane)
{
  var numaric = alphane;
  for(var j=0; j<numaric.length; j++)
  {
    var alphaa = numaric.charAt(j);
    var hh = alphaa.charCodeAt(0);
    if (hh >= 65 && hh <= 90)
    {
      return true;
    }
  }
  return false;
}

function foundConsecutive(alphane)
{
  var prev = -1;
  var numaric = alphane;
  for(var j=0; j<numaric.length; j++)
  {
    var alphaa = numaric.charAt(j);
    var hh = alphaa.charCodeAt(0);
    if (hh == prev)
    {
      return true;
    }
    prev = hh;
  }
  return false;
}

function window_onload()
{
  window.focus();
<%if (message.equals("<font color=blue>Password updated successfully</font>"))
  {%>
  window.opener.location.href = "<%=target%>";
  self.close();
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="changePassword" method="post" action="changePassword.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<!--input name="fromLogin" type=hidden value="%=fromLogin%"-->
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Change Password</b>
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
          <td class=bluebold><%=userId%></td>
        </tr>
        <tr>
          <td><b>Current Password:</b></td>
          <td><input type=password name=current></td>
        </tr>
        <tr>
          <td><b>New Password:</b></td>
          <td><input type=password name=newpass></td>
        </tr>
        <tr>
          <td><b>Confirm New Password:</b></td>
          <td><input type=password name=confirm></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td align=right>
      <input class=button type=button value=Submit onClick="valAndSub();">
    </td>
    <td align=left>
      <input class=button type=button value=Close onClick="window.close();">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


