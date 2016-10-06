<%@ page import="java.util.GregorianCalendar"%>
<SCRIPT language="JavaScript">
function sendSelected(selectedId, selectedOption)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.gcId.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<%
GregorianCalendar now = new GregorianCalendar();

%>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
<br>
<%=BAN.getCustomerList(request.getParameter("userId"), request.getParameter("sortOrder"))%>
<%
now = new GregorianCalendar();

%>
<%=now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+now.get(now.SECOND)+"."+now.get(now.MILLISECOND)%>
<br>
