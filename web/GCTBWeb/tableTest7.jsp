<%@ page import="java.util.GregorianCalendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
GregorianCalendar now = new GregorianCalendar();
BAN.setRefreshCustomerGrid(false);
%>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
<br>
<%=BAN.getCustomerList("kg08953", "Customer")%>
<%
now = new GregorianCalendar();

%>
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
<br>
