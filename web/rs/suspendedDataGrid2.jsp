<%@ page import="JavaUtil.SuspendedDataDescriptor"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  Collection suspendedList = RSB.populateSuspendedDataList2();
%>
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
<table border=0>
<%
  boolean first = true;
  boolean one = true;
  for (Iterator it = suspendedList.iterator(); it.hasNext(); )
  {
    SuspendedDataDescriptor sdd = (SuspendedDataDescriptor)it.next();
    sdd.getProviderName();
    String cl = one?"grid1":"grid2";
    one = !one;
%>
  <tr>
    <td class=<%=cl%><%=first?" width=147":""%> nowrap><%=sdd.getProviderName()%></td>
    <td class=<%=cl%><%=first?" width=195":""%> nowrap><%=sdd.getMasterAccount()%></td>
    <td class=<%=cl%><%=first?" width=195":""%> nowrap><%=sdd.getAccountNumber()%> <%=sdd.getAccountName()%></td>
    <td class=<%=cl%><%=first?" width=200":""%> nowrap><%=sdd.getProduct()%></td>
    <td class=<%=cl%><%=first?" width=200":""%> nowrap><%=sdd.getSuspenseReason()%></td>
    <td class=<%=cl%><%=first?" width=80":""%> nowrap><%=sdd.getRecordCount()%></td>
    <td class=<%=cl%><%=first?" width=70":""%> nowrap><%=sdd.getWrittenOff()%></td>
    <td class=<%=cl%>><a name="<%=sdd.getAccountNumber()%><%=sdd.getProductCode()%>"></a>
      <input class=listbutton type=button value="R" onClick="sendSelected('<%=sdd.getAccountNumber()%>',
      '<%=sdd.getProductCode()%>','<%=sdd.getSuspended()%>',
      '<%=sdd.getWrittenOff().equals("Y")?"N":"Y"%>')"></td>
  </tr>
<%
  }
%>
