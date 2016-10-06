<SCRIPT language="JavaScript">
function sendSelected(selectedId)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.Charge_Id.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="Select";
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="/shared/nr/cw/newcss/world_ie.css">
<jsp:useBean id="chBAN" class="DBUtilities.GCBChargeBANBean" scope="session"/>
<%=chBAN.getChargeList()%>