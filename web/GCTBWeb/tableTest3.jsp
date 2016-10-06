<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY>
<%@ page import="DBUtilities.DBAccess,DBUtilities.BANBean,java.util.Enumeration,JavaUtil.*"%>
<%@ page import="java.util.GregorianCalendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
GregorianCalendar now = new GregorianCalendar();

%>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
<br>
<table border=0>
<%=BAN.testGetCustomerList(false)%>
<%
now = new GregorianCalendar();
%>
</table>
<br>
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
</BODY>
</HTML>
