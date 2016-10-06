<SCRIPT language="JavaScript">
function sendSelected(selectedId, selectedOption)
{
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%=BAN.getConglomProductList(request.getParameter("sortOrder"))%>
