<SCRIPT language="JavaScript">
function sendSelected(selectedId, selectedOption, selectedFreq, selectedSource)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.billedProductId.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.billedProductFreq.value=selectedFreq;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.billedProductSource.value=selectedSource;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="ccBAN" class="DBUtilities.ConglomCustomerBANBean" scope="session"/>
<%=ccBAN.getBilledProductList(request.getParameter("sortOrder"))%>
