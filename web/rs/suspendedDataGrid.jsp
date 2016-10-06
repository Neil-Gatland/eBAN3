<SCRIPT language="JavaScript">
function sendSelected(selectedNo, selectedProduct, selectedSus, selectedWO)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.accountNumber.value=selectedNo;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.product.value=selectedProduct;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.suspended.value=selectedSus;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.writeOff.value=selectedWO;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="list_UpdateWriteOff";
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%=RSB.getSuspendedDataList()%>
