<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY>
<%@ page import="DBUtilities.DBAccess,DBUtilities.BANBean,java.util.Enumeration,JavaUtil.*"%>
<%@ page import="java.util.GregorianCalendar"%>
<%
  DBAccess DBA = new DBAccess();
  DBA.startDB();
  String result = DBA.oracleTest2();
%>
<%=result%>
</BODY>
</HTML>
