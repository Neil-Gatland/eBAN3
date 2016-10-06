<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY onload="alert('done');">
<%@ page import="DBUtilities.DBAccess,DBUtilities.BANBean,java.util.Enumeration,JavaUtil.*"%>
<%@ page import="java.util.GregorianCalendar"%>
<%
GregorianCalendar now = new GregorianCalendar();

%>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
<br>
<table border=0>
<tr><td>
<iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="tableTest.jsp"></iframe>   
<!--
<iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="customerGrid.jsp?userId=kg08953&sortOrder=Customer"></iframe>   
-->
</td></tr>
</table>
<br>
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
</BODY>
</HTML>

