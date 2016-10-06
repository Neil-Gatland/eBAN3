<SCRIPT language="JavaScript">
function sendOrderBy(OrderBy)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.BAN_Id.value=" ";
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.OrderBy.value=OrderBy;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<%@ page import="DBUtilities.BANBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%=BAN.getHeader((String)session.getAttribute("System"))%>