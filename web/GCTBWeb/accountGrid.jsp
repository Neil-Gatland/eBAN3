<SCRIPT language="JavaScript">
function sendSelected(selectedId, accId, accName, total, selectedOption)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.iRId.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.accountId.value=accId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.accountName.value=accName;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.total.value=total;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%=BAN.getAccountList(request.getParameter("accountType"))%>
