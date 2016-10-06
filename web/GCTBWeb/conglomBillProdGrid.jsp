<SCRIPT language="JavaScript">
function sendSelected(selectedId, selectedOption, product, status, sourceAccount)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.itemId.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.product.value=product;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.status.value=status;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.sourceAccount.value=sourceAccount;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_"+selectedOption;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%=BAN.getConglomBillProdGrid()%>
