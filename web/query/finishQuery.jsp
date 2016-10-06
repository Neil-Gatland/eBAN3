<%@ page import="java.util.*"%>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<html>
<head>
<title>Raise a query</title>
<%
  String message = request.getParameter("message");
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function onLoad()
{
  window.focus();
}

function closeClick()
{
  window.close();
}
</script>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<body onload="onLoad()">
<table width="90%" align="center">
  <tr>
    <td valign="top">
      <h3 align="center"><b>Raise a query</b></h3>
    </td>
  </tr>
  <tr>
    <td valign="top" align="center">
      <b><%=message.startsWith("(query id: ")?("Your query has been " +
        "submitted successfully "+message+". Please press 'Close' to exit."):
        ("Unable to submit your query: "+request.getParameter("message")+
        ". Please contact Systems Support.")%></b>
    </td>
  </tr>
  <tr>
    <td valign="top">&nbsp;
    </td>
  </tr>
  <tr>
    <td align="center" valign="top">
      <input class=button type=button value="Close" style="width:90px" onClick="closeClick()">
    </td>
  </tr>
</table>
</body>
</form>
</html>


