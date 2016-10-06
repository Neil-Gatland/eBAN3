<SCRIPT language="JavaScript">
function sendSelected(selectedId, selectedOption)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ratingSchemeId.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%=RSB.getRatingSchemeList()%>
