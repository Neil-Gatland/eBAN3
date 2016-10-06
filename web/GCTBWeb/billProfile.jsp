<html>
<head>
  <title>Bill Profile</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%
  boolean isConglom = request.getParameter("conglom") != null;
  if (isConglom)
  {
    BAN.getConglomBillProfile();
  }
  else
  {
    BAN.getBillProfile(false);
    BAN.getAccountTaxPayable();
  }
  String message = BAN.getMessage();
  BAN.setMessage("");
  String[] ca = BAN.getCompanyAddress(false);
  String[] ba = BAN.getBillingAddress();
  String[] b1a = BAN.getBankAddress();
  String[] b2a = BAN.getAltBankAddress();
%>
<script language="JavaScript">
</script>
<body onLoad="window.focus();">
<form name="billProfile" method="post" action="">
<input name="ButtonPressed" type=hidden value="">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Bill Profile</b>
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center>
      <b><%=message%></b>
    </td>
  </tr>
  <tr>
    <td valign=top>
      <table width="100%" border="1">
        <tr>
          <td class=grid1>
            <%=ca[0]%>
          </td>
        </tr>
<%/*if (isConglom)
  {*/%>
        <tr>
          <td class=grid1>
            <%=BAN.getCompanyDetails1()%>
          </td>
        </tr>
<%/*}*/%>
        <tr>
          <td class=grid1>
            <%=BAN.getCompanyDetails2()%>
          </td>
        </tr>
      </table>
    </td>
    <td>
      <table width="100%" border="1">
        <tr>
          <td class=grid1>
            <%=ca[0]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=ca[1]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=ca[2]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=ca[3]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=ca[4]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=ca[5]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=ca[6]%>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr class=gridHeader>
    <td class=gridHeader colspan=2 align=center>
      Invoice Details
    </td>
  </tr>
  <tr>
    <td>
      <table width="100%" border="1">
        <tr>
          <td class=grid1 rowspan=<%=isConglom?"7":"5"%> valign=top>
            <b>Address:</b>
          </td>
          <td class=grid1>
            <%=ba[0]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=ba[1]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=ba[2]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=ba[3]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=ba[4]%>
          </td>
        </tr>
<%if (isConglom)
  {%>
        <tr>
          <td class=grid1>
            <%=ba[5]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=ba[6]%>
          </td>
        </tr>
<%}%>
        <tr>
          <td class=grid1>
            <b>Attention:</b>
          </td>
          <td class=grid1>
            <%=BAN.getBillingContact()%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <b>Contact:</b>
          </td>
          <td class=grid1>
            <%=BAN.getCustomerContact()%>
          </td>
        </tr>
      </table>
    </td>
    <td valign=top>
      <table width="100%" border="1">
        <tr>
          <td class=grid1>
            <b><%=BAN.getTaxReferenceLiteral()%></b>
          </td>
          <td class=grid1>
            <%=BAN.getTaxReference()%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <b>Account No:</b>
          </td>
          <td class=grid1>
            <%=BAN.getAccountId()%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <b>Invoice Currency:</b>
          </td>
          <td class=grid1>
            <%=BAN.getOutgoingCurrencyCode()%>
          </td>
        </tr>
<%if (!isConglom)
  {%>
        <tr>
          <td class=grid1>
            <b>Tax Applicable:</b>
          </td>
          <td class=grid1>
            <%=BAN.getTaxPayable()%>
          </td>
        </tr>
<%}%>
      </table>
    </td>
  </tr>
  <tr class=gridHeader>
    <td class=gridHeader colspan=2 align=center>
      How To Pay
    </td>
  </tr>
  </tr>
    <td>
      <table width="100%" border="1">
        <tr>
          <td class=grid1>
            <b>By Cheque To:</b>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b1a[0]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b1a[1]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b1a[2]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b1a[3]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b1a[4]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b1a[5]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b1a[6]%>
          </td>
        </tr>
      </table>
    </td>
    <td>
      <table width="100%" border="1">
        <tr>
          <td class=grid1>
            <b>By EFT To:</b>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b2a[0]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b2a[1]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b2a[2]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b2a[3]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b2a[4]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b2a[5]%>
          </td>
        </tr>
        <tr>
          <td class=grid1>
            <%=b2a[6]%>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center>
      <input class=button type=button value=Close onClick="window.close();">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


