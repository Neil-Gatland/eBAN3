<SCRIPT language="JavaScript">
function sendSelected(selectedAccNo, selectedNetSource, newValue, selectedOption)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.accountNumber.value=selectedAccNo;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.networkSource.value=selectedNetSource;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.newValue.value=newValue;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%=RSB.getIncorrectTrafficList()%>
