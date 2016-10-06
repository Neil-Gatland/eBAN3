<html>
<head>
  <title>Trial Invoice - not for issue</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
  <style>
    body{
    background-attachment:fixed;
    background-image:url("../nr/cw/newimages/trialwm4.gif");
    background-repeat: no-repeat;
    }
  </style>
</head>
<%@ page import="JavaUtil.StandardInvoiceChargeDescriptor"%>
<%@ page import="JavaUtil.StandardInvoiceAdjustmentDescriptor"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%
  Calendar now = new GregorianCalendar();
  int dd = now.get(now.DATE);
  int mm =  now.get(now.MONTH) + 1;
  String taxPointDate = (dd<10?"0":"") + dd + "/" + (mm<10?"0":"") + mm + "/" +
    now.get(now.YEAR);
  BAN.getBillProfile(true);
  String[] ca = BAN.getCompanyAddress(true);
  String[] ba = BAN.getBillingAddress();
  String[] bka = BAN.getBankAddress();
  String[] abka = BAN.getAltBankAddress();
  String[] details = BAN.getStandardInvoiceDetails();
  String[] vatDetails = BAN.getCustomerVATDetails();
  double totalTax = Double.parseDouble(details[2]);
  boolean showTax = totalTax != 0;
  String accountId = BAN.getAccountId();
  BAN.getInvoiceBillDetails();
  Collection ict =  BAN.getInvoiceChargeTotals();
  Collection adj =  BAN.getInvoiceAdjustments();
  String companyAddressId = BAN.getCompanyAddressId();
  boolean useRegisteredNumber = !companyAddressId.equals("19") &&
    !companyAddressId.equals("28") && !companyAddressId.equals("39");
%>
<script language="JavaScript">
function doSaveAs()
{
    if (document.execCommand)
    {
        document.execCommand("SaveAs", false, "<%=BAN.getInvoiceNo()%>.html");
    }
    else {
        alert('This command is available in Internet Explorer only.');
    }
}
</script>
<body onLoad="window.focus();" scroll="auto">
<table width="655" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td rowspan="2" width="25">&nbsp;
    </td>
    <td>
      <table width="625" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td>
            <table width="100%" border="0">
            <tr>
              <td align=right height=30 valign=center>
                <input class=button type=button value=Print style="width:70" onclick="window.print();return false;">
              </td>
              <td height=30 width=20>
                &nbsp;
              </td>
              <td align=center height=30 valign=center>
                <input class=button type=button value=Save style="width:70" onClick="doSaveAs();">
              </td>
              <td height=30 width=20>
                &nbsp;
              </td>
              <td align=left height=30 valign=center>
                <input class=button type=button value=Close style="width:70" onClick="window.close();">
              </td>
            </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td>
      <table border="1" cellpadding="5" cellspacing="0" width="625">
        <tbody>
          <tr>
            <td>
              <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tbody>
                  <tr>
                    <td>
                      <table>
                        <tbody>
                          <tr>
                            <td>
                              &nbsp;
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <table align="left" border="0" cellpadding="0" cellspacing="0" width="250">
                                <tbody>
                                  <tr>
                                    <td>
                                      <font size="-1" face="Arial"><%=BAN.getCompanyName()%></font>
                                    </td>
                                  </tr>
      <%for (int i=0; i<7; i++)
        {
          if (!ca[i].equals("&nbsp;"))
          {
      %>
                                  <tr>
                                    <td>
                                      <font size="-1" face="Arial"><%=ca[i]%></font>
                                    </td>
                                  </tr>
      <%  }
        }%>
                                </tbody>
                              </table>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </td>
                    <td valign="top" width="350">
                      <font size="-1" face="Arial">
                        <table border="0" cellpadding="0" cellspacing="0" width="360">
                          <tbody>
                            <tr>
                              <td rowspan="2" width="120">
                                &nbsp;
                              </td>
                              <td bgcolor="#aaaaff" valign="bottom" align="center" height="133">
<%if (!companyAddressId.equals("44"))
  {%>
                                <img src="../nr/cw/newimages/cwlogo25pcwhite.gif" align="center">
<%}%>
                              </td>
                            </tr>
                            <tr>
                              <td align="center">
                                <font size="-1" face="Arial" color="red"><b>Trial Invoice - not for issue</b></font>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </font>
                    </td>
                  </tr>
                </tbody>
              </table>
              <table border="0" cellpadding="0" cellspacing="0" width="600">
                <tbody>
                  <tr>
                    <td colspan="1">
                      &nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <font size="-1" face="Arial"><b><%=BAN.getCompanyName()%></b></font>
                    </td>
                    <td>
                      <font size="-1" face="Arial">https://e-billing.cw.com</font>
                    </td>
                  </tr>
                </tbody>
              </table>
              <table border="0" cellpadding="0" cellspacing="0" width="600">
                <tbody>
                  <tr>
                    <td>
                      <font size="-1" face="Arial"><nobr><%=useRegisteredNumber?"Registered number: ":"Registered address: "%></nobr></font>
                    </td>
                    <td>
                      <font size="-1" face="Arial"><%=BAN.getCompanyDetails1()%></font>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <font size="-1" face="Arial"><nobr><%=useRegisteredNumber?"Registered address: ":"&nbsp;"%></nobr></font>
                    </td>
                    <td>
                      <font size="-1" face="Arial"><%=BAN.getCompanyDetails2()%></font>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="1">
                      &nbsp;
                    </td>
                  </tr>
                </tbody>
              </table>
              <table border="0" cellpadding="5" cellspacing="0" width="600">
                <tbody>
                  <tr>
                    <td align="center" bgcolor="#aaaaff">
                      <font color="#ffffff" size="+1" face="Arial"><b>TAX INVOICE</b></font>
                    </td>
                  </tr>
                </tbody>
              </table>
              <table border="0" cellpadding="0" cellspacing="0" width="600">
                <tbody>
                  <tr>
                    <td colspan="2">
                      &nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td valign="top" width="300">
                      <table align="left" border="0" cellpadding="0" cellspacing="0" width="300">
                        <tbody>
                          <tr>
                            <td width="100">
                              <font size="-1" face="Arial">Address:</font>
                            </td>
                            <td>
                              <font size="-1" face="Arial"><%=BAN.getBillingCustomerName()%></font>
                            </td>
                          </tr>
      <%for (int i=0; i<7; i++)
        {
          if (!ba[i].equals("&nbsp;"))
          {
      %>
                          <tr>
                            <td width="100">
                              &nbsp;
                            </td>
                            <td>
                              <font size="-1" face="Arial"><%=ba[i]%></font>
                            </td>
                          </tr>
      <%  }
        }%>
                          <tr>
                            <td colspan="2">
                              &nbsp;
                            </td>
                          </tr>
                          <tr>
                            <td width="100">
                              <font size="-1" face="Arial">Attention:</font>
                            </td>
                            <td>
                              <font size="-1" face="Arial"><%=BAN.getBillingContact()%></font>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </td>
                    <td valign="top" width="300">
                      <table align="left" border="0" cellpadding="0" cellspacing="0" width="293">
                        <tbody>
                          <tr>
                            <td>
                              <font size="-1" face="Arial">Date of Issue:</font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=taxPointDate%></font>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <font size="-1" face="Arial">Tax Point:</font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=taxPointDate%></font>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <font size="-1" face="Arial">Period from:</font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=BAN.getBPSD()%></font>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <font size="-1" face="Arial">Period to:</font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=BAN.getBPED()%></font>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <font size="-1" face="Arial"><%=vatDetails[1]==null?"&nbsp":vatDetails[1]%></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=vatDetails[0]==null?"&nbsp":vatDetails[0]%></font>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <font size="-1" face="Arial"><%=BAN.getTaxReferenceLiteral()==null?"&nbsp":BAN.getTaxReferenceLiteral()%></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=BAN.getTaxReference()==null?"&nbsp":BAN.getTaxReference()%></font>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <font size="-1" face="Arial">Account No:</font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=accountId%></font>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <font size="-1" face="Arial">Invoice No:</font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=BAN.getInvoiceNo()%></font>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <font size="-1" face="Arial">Invoice Currency:</font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=BAN.getOutgoingCurrencyCode()%></font>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <font size="-1" face="Arial"></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"></font>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </td>
                  </tr>
                </tbody>
              </table>
              <table border="0" cellpadding="0" cellspacing="0" width="600">
                <tbody>
                  <tr>
                    <td width="100">
                      &nbsp;
                    </td>
                    <td width="450">
                      &nbsp;
                    </td>
                    <td width="50">
                      &nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td valign="top">
                      <font size="-1" face="Arial">Invoice Details:</font>
                    </td>
                    <td>
                      <table align="left" border="0" cellpadding="0" cellspacing="0" width="500">
                        <tbody>
                          <tr>
                            <td colspan="2">
                              <font size="-1" face="Arial"><b>Charges for <%=BAN.getGlobalCustomerName()%> (<%=BAN.getInvoiceRegionName()%>)</b></font>
                            </td>
                          </tr>
                          <tr>
                            <td colspan="2">
                              &nbsp;
                            </td>
                          </tr>
                          <tr>
                            <td colspan="2" width="450">
                              <font size="-1" face="Arial"><b><u>Charges</u></b></font>
                            </td>
                          </tr>
      <%for(Iterator it = ict.iterator(); it.hasNext(); )
        {
          StandardInvoiceChargeDescriptor sicd = (StandardInvoiceChargeDescriptor)it.next();
      %>
                          <tr>
                            <td align="left">
                              <font size="-1" face="Arial">Total Charges (excluding Tax) / <%=sicd.getInvGCDId()%></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=sicd.getInvGCDTotal()%></font>
                            </td>
                          </tr>
      <%}%>
                          <tr>
                            <td align="left">
                              <font size="-1" face="Arial"><b>Charges Sub-Total excluding Tax</b></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><b><%=details[0]%></b></font>
                            </td>
                          </tr>
                          <tr>
                            <td colspan="2">
                              &nbsp;
                            </td>
                          </tr>
                          <tr>
                            <td colspan="2" width="450">
                              <font size="-1" face="Arial"><b><u>Adjustments</u></b></font>
                            </td>
                          </tr>
      <%if (adj.isEmpty())
        {%>
                          <tr>
                            <td align="left">
                              <font size="-1" face="Arial">None</font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial">0.00</font>
                            </td>
                          </tr>
      <%}
        else
        {
          for(Iterator it = adj.iterator(); it.hasNext(); )
          {
            StandardInvoiceAdjustmentDescriptor siad = (StandardInvoiceAdjustmentDescriptor)it.next();
      %>
                          <tr>
                            <td align="left">
                              <font size="-1" face="Arial"><%=siad.getInvAdjustmentDescription()%></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=siad.getInvAdjustmentTotal()%></font>
                            </td>
                          </tr>
      <%  }
        }%>
                          <tr>
                            <td align="left">
                              <font size="-1" face="Arial"><b>Adjustments Sub-Total excluding Tax</b></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><b><%=details[1]%></b></font>
                            </td>
                          </tr>
                          <tr>
                            <td colspan="2">
                              &nbsp;
                            </td>
                          </tr>
                          <tr>
                            <td colspan="2">
                              <font size="-1" face="Arial"><b><u>Tax</u></b></font>
                            </td>
                          </tr>
                          <tr>
                            <td align="left">
                              <font size="-1" face="Arial">Tax in <%=BAN.getOutgoingCurrencyCode()%></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=details[2]%></font>
                            </td>
                          </tr>
                          <tr>
                            <td align="left">
                              <font size="-1" face="Arial"><b>Tax Sub-Total</b></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><b><%=details[2]%></b></font>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </td>
                    <td>
                      &nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td colspan="3">
                      &nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td width="100">
                      &nbsp;
                    </td>
                    <td width="450">
                      <table border="0" cellpadding="0" cellspacing="0" width="500">
                        <tbody>
                          <tr>
                            <td align="left">
                              <font size="-1" face="Arial"><b>Total Amount Payable (excluding tax)</b></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=details[3]%></font>
                            </td>
                          </tr>
                          <tr>
                            <td align="left">
                              <font size="-1" face="Arial"><b>Tax</b></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><%=details[2]%></font>
                            </td>
                          </tr>
                          <tr>
                            <td align="left">
                              <font size="-1" face="Arial"><b>Total Amount Payable (including tax)</b></font>
                            </td>
                            <td align="right">
                              <font size="-1" face="Arial"><b><%=details[4]%></b></font>
                            </td>
                          </tr>
                          <tr>
                            <td colspan="3">
                              &nbsp;
                            </td>
                          </tr>
                          <tr>
                            <td align="left">
                              <font size="-1" face="Arial">This invoice is in
                              <%=BAN.getOutgoingCurrencyCode() +
                              " (" + BAN.getOutgoingCurrencyDesc() + ")" +
                              (((details[5].equals("n/a"))||(!showTax))?".":(" and " + details[5] +
                              " applied at " + details[6] + "%"))%></font>
                            </td>
                            <td>
                              <font size="-1" face="Arial"><b><%=vatDetails[2]==null?"&nbsp;":vatDetails[2]%></b></font>
                            </td>
                          </tr>
                          <tr>
                            <td colspan="2">
                              &nbsp;
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </td>
                    <td width="50">
                      &nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td colspan="3">
                      &nbsp;
                    </td>
                  </tr>
                </tbody>
              </table>
              <table border="0" cellpadding="0" cellspacing="0" width="600">
                <tbody>
                  <tr>
                    <td align="center">
                      <font size="-1" face="Arial"><%=BAN.getCustomerContact()%></font>
                    </td>
                  </tr>
                </tbody>
              </table>
              <table border="0" cellpadding="5" cellspacing="0" width="600">
                <tbody>
                  <tr>
                    <td align="center" bgcolor="#aaaaff">
                      <font color="#ffffff" size="+1" face="Arial"><b>HOW TO PAY</b></font>
                    </td>
                  </tr>
                  <tr>
                    <td align="center">
                      <font size="-1" face="Arial">The total amount is due and payable within 30 days of submission of invoice</font>
                    </td>
                  </tr>
                  <tr>
                    <td align="center">
                      <font size="-1" face="Arial">Payment should be made by Cheque or EFT quoting: '<%=accountId.substring(accountId.indexOf("/")+1) + "/" + BAN.getInvoiceNo()%>'</font>
                    </td>
                  </tr>
                </tbody>
              </table>
              <table border="0" cellpadding="0" cellspacing="0" width="600">
                <tbody>
                  <tr>
                    <td width="25">
                      &nbsp;
                    </td>
                    <td width="275">
                      &nbsp;
                    </td>
                    <td width="25">
                      &nbsp;
                    </td>
                    <td width="275">
                      &nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td colspan="4">
                      &nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td>
                      &nbsp;
                    </td>
                    <td valign="top">
                      <table align="left" border="0" cellpadding="0" cellspacing="0" width="300">
                        <tbody>
                          <tr>
                            <td valign="top">
                              <font size="-1" face="Arial">By Cheque to:</font>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              &nbsp;
                            </td>
                          </tr>
      <%for (int i=0; i<7; i++)
        {
          if (!bka[i].equals("&nbsp;"))
          {
      %>
                          <tr>
                            <td>
                              <font size="-1" face="Arial"><%=bka[i]%></font>
                            </td>
                          </tr>
      <%  }
        }%>
                        </tbody>
                      </table>
                    </td>
                    <td>
                      &nbsp;
                    </td>
                    <td valign="top">
                      <table align="left" border="0" cellpadding="0" cellspacing="0" width="300">
                        <tbody>
                          <tr>
                            <td valign="top">
                              <font size="-1" face="Arial">By EFT to:</font>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              &nbsp;
                            </td>
                          </tr>
      <%for (int i=0; i<7; i++)
        {
          if (!abka[i].equals("&nbsp;"))
          {
      %>
                          <tr>
                            <td>
                              <font size="-1" face="Arial"><%=abka[i]%></font>
                            </td>
                          </tr>
      <%  }
        }%>
                        </tbody>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="4">
                      &nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td colspan="4" align="center">
                      &nbsp;
                    </td>
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
        </tbody>
      </table>
    </td>
  </tr>
</table>
</body>
</html>
