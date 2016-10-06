<SCRIPT language="JavaScript">
function sendSelected(selectedId, selectedName, selectedOption)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.spId.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.spName.value=selectedName;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%=BAN.getGSOCustomerList(request.getParameter("userId"), request.getParameter("sortOrder"))%>
