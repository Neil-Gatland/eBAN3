<SCRIPT language="JavaScript">
function sendSelected(selectedId, selectedOption)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.productGroup.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="listApprove"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="MAC" class="DBUtilities.MasterAccountBean" scope="session"/>
<%=MAC.getProductGroupList()%>
