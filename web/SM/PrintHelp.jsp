<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
  <title>Print Help</title>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="DBUtilities.DBAccess,java.util.Enumeration,HTMLUtil.HTMLBean,JavaUtil.*"%>
<%
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  DBAccess DBA = new DBAccess();
 %>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--

  function closeClick()
  {
    window.close();
  }

  function window_onload()
  {
    var agent = new String(navigator.userAgent);
    if (agent.indexOf("5.5") == -1)
    {
      Print.setup.src="../nr/cw/newimages/pagesetup6.gif";
    }
    else
    {
      Print.setup.src="../nr/cw/newimages/pagesetup.gif";
    }
  }
  //-->
  </script>
<body language=javascript onload="return window_onload()">
<form name="Print" method="post" action="Print.jsp">
<input type="hidden" name="fromSelf" value="true">
<table width="100%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td colspan="3">&nbsp;</td>
  </tr>
  <tr>
    <td width="10%">&nbsp;</td>
    <td>Click on 'File' top left and select 'Page Setup'. If the screen contains different
    values to those shown below, amend it as required and click 'OK'<br>
    (Note: deleting the contents of the 'Margins' boxes, clicking 'OK' and then re-selecting
    will result in the values shown being entered automatically).<br>
    Please make a note of the original settings before altering them as they can only
    be restored manually.
    </td>
    <td width="10%">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="3">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="3" align="center"><img id="setup" src="">
    </td>
  </tr>
  <tr>
    <td colspan="3">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="3" align="center">
      <%=HB.getImageAsAnchor("close")%>
    </td>
  </tr>
</table>
</form>
</body>
</html>

