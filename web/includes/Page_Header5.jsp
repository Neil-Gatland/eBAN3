<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.*"%>
<head>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript" src="../includes/RightClick.js">
</script>
<script language="JavaScript">
function showQuery()
{
<%if (request.getParameter("query") != null)
  {
    Enumeration en = request.getParameterNames();
    StringBuffer sb = new StringBuffer("../query/addQuery.jsp?");
    while (en.hasMoreElements())
    {
      String pName = (String)en.nextElement();
      if (!pName.equals("query"))
      {
        sb.append(pName + "=" +
          URLEncoder.encode(request.getParameter(pName)) +
          "&");
      }
    }
    sb.deleteCharAt(sb.length()-1);
  %>
  window.open("<%=sb.toString()%>", "abqs", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=600,width=800,top=50,left=50");
<%}%>
}
</script>
<title>eBan</title>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<meta http-equiv="REFRESH" content="1801; url=../GCTBWeb/Login.jsp?type=t">
<meta http-equiv="PRAGMA" content="NO-CACHE">
<meta http-equiv="EXPIRES" content="-1">
</head>
<body topmargin="0" leftmargin="0" marginleft="0" margintop="0" onLoad="showQuery()">