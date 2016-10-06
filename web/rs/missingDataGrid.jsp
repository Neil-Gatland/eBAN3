<SCRIPT language="JavaScript">
function sendSelected(missingDataValue, missingDataType)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.missingDataValue.value=missingDataValue;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="create"+missingDataType;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%=RSB.getMissingDataList()%>
