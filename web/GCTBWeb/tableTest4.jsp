<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<script language=javascript>
function showTime()
{
  var t = new Date();
  alert(t.getHours()+"."+t.getMinutes()+"."+t.getSeconds()+"."+t.getMilliseconds());	
}
</script>
<BODY onload="showTime();">
<%@ page import="DBUtilities.DBAccess,DBUtilities.BANBean,java.util.Enumeration,JavaUtil.*"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
GregorianCalendar now = new GregorianCalendar();
Collection list = BAN.testGetCustomerList3("kg08953","Customer",true,true);

%>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<%
now = new GregorianCalendar();
%>
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
<br>
<table border=0>
<%for(Iterator it = list.iterator(); it.hasNext(); )
  {%>
<%=(String)it.next()%>
<%}
now = new GregorianCalendar();
%>
</table>
<br>
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
</BODY>
</HTML>
