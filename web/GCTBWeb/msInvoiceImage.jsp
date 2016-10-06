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
<%@ page import="JavaUtil.MSInvoiceDataDescriptor"%>
<%@ page import="java.util.*"%>
<%@ page import="java.math.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="SU" class="JavaUtil.StringUtil" scope="session"/>
<%
  Collection data = BAN.getMSInvoiceCharges();
  Calendar now = new GregorianCalendar();
  int dd = now.get(now.DATE);
  int mm =  now.get(now.MONTH) + 1;
  String taxPointDate = (dd<10?"0":"") + dd + "/" + (mm<10?"0":"") + mm + "/" +
    now.get(now.YEAR);
  BAN.getBillProfile(true);
  BAN.getAccountTaxPayable();
  //String[] ca = BAN.getCompanyAddress(true);
  String[] ba = BAN.getBillingAddress();
  String accountId = BAN.getAccountId();
  BigDecimal sumCharges = new BigDecimal(new BigInteger("0"), 2);
  BigDecimal taxTotal = new BigDecimal(new BigInteger("0"), 2);
  BigDecimal total = new BigDecimal(new BigInteger("0"), 2);
  boolean isAdjustment = BAN.getInvoiceNo().startsWith("4");
  boolean noTaxType = BAN.getTaxType() == null;

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
<form name="msInvoiceImage" method="post" action="">
<input name="ButtonPressed" type=hidden value="">
<a name="top"></a>
<table width="655" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td rowspan="2" width="25">&nbsp;
    </td>
    <td align="center">
      <table width="225" border="0" cellspacing="0" cellpadding="5">
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
      <table width="625" border="1" cellspacing="0" cellpadding="5">
        <tr>
          <td>
            <table width="100%" border="0">
              <tr>
                <td align="left" colspan="3">
                  <font color="#aaaaff" face="Arial"><b><i>Customised Network Services Invoice</i></b></font>
                </td>
                <td colspan="2" width="49%">
<%if (!BAN.getCompanyAddressId().equals("44"))
  {%>
                  <img src="../nr/cw/newimages/newLogoDuplSmall.gif" align="right">
<%}%>
                </td>
              </tr>
              <tr>
                <td colspan="5">
                  <table width="100%" border="0">
                    <tr>
                      <td align="left" colspan="4">
                        <table width="100%" border="0">
                          <tr>
                            <td width="10%"> 
                            </td>
                            <td align="left">
                              <font face="Arial" size="-1"><%=BAN.getBillingCustomerName()%></font>
                            </td>
                          </tr>
<!--
                          <tr>
                          <td width="100"> 
                          </td>
                          <td>
                            <font face="Arial" size="-1"><%=BAN.getBillingContact()%></font>
                          </td>
                        </tr>
-->
      <%for (int i=0; i<7; i++)
        {
          if (!ba[i].equals("&nbsp;"))
          {
      %>
                        <tr>
                          <td width="100"> 
                          </td>
                          <td>
                            <font face="Arial" size="-1"><%=ba[i]%></font>
                          </td>
                        </tr>
      <%  }
        }%>
                      </table>
                    </td>
                    <td colspan="1" width="45%" valign="top">
                      <table width="100%" border="0">
                        <tr>
                          <td colspan="2" align="left">
                            <font face="Arial" size="-1"><%=BAN.getCompanyName()%></font>
                          </td>
                        </tr>
                        <tr>
                          <td width="40%" align="left" style="font: xx-small Arial">Registered number:
                          </td>
                          <td valign="top" width="60%" align="left" style="font: xx-small Arial"><%=BAN.getCompanyDetails1()%>
                          </td>
                        </tr>
                        <tr>
                          <td valign="top" width="40%" align="left" style="font: xx-small Arial">Registered address:
                          </td>
                          <td width="60%" align="left" valign="top" style="font: xx-small Arial" rowspan="2"><%=BAN.getCompanyDetails2()%>
                          </td>
                        </tr>
                        <tr>
                          <td>
                          </td>
                        </tr>
                        <tr>
                          <td width="40%" align="left" style="font: xx-small Arial"><%=BAN.getTaxReferenceLiteral()%>
                          </td>
                          <td width="60%" align="left" style="font: xx-small Arial"><%=BAN.getTaxReference()%>
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
              </td>
            </tr>
            <tr>
              <table width="100%" border="0">
                <tr>
                  <td align="left" colspan="1" style="font: smaller Arial">Customer A/C No:
                  </td>
                  <td align="left" colspan="2" style="font: smaller Arial"><%=accountId.substring(accountId.indexOf("/")+1)%>
                  </td>
                  <td align="left" colspan="1" style="font: smaller Arial">Date of Bill:
                  </td>
                  <td align="left" colspan="2" style="font: smaller Arial"><%=taxPointDate%>
                  </td>
                  <td align="left" colspan="1" style="font: smaller Arial"> 
                  </td>
                  <td align="left" colspan="1" style="font: smaller Arial" width="25%">Enquiries:
                  </td>
                </tr>
                <tr>
                  <td align="left" colspan="1" style="font: smaller Arial"> 
                  </td>
                  <td align="left" colspan="6" style="font: smaller Arial">
                  </td>
                  <td valign="top" align="left" colspan="1" style="font: xx-small Arial" rowspan="1"><%=BAN.getCustomerContact()%>
                  </td>
                </tr>
                <tr>
                  <td align="left" colspan="1" style="font: smaller Arial">Bill Reference:
                  </td>
                  <td align="left" colspan="7" style="font: smaller Arial"><%=BAN.getInvoiceNo()%>
                  </td>
                </tr>
                <tr>
                  <td align="left" colspan="1" style="font: smaller Arial">
                  </td>
                  <td align="left" colspan="1" style="font: smaller Arial">
                  </td>
                  <td align="left" colspan="6" style="font: smaller Arial"> 
                  </td>
                </tr>
              </table>
              <table bgcolor="#aaaaff" width="100%" height="30" cellpadding="0">
                <tr>
                  <td align="left" colspan="1" style="font: smaller Arial" width="49%"><b>Description</b>
                  </td>
                  <td align="left" colspan="1" style="font: smaller Arial" width="23%"><b><%=isAdjustment?"":"Period"%></b>
                  </td>
                  <td align="left" colspan="1" style="font: smaller Arial" width="18%"><b><%=isAdjustment?"":"Customer Data"%></b>
                  </td>
                  <td align="right" colspan="1" style="font: smaller Arial" width="10%"><b>Cost <%=BAN.getOutgoingCurrencyCode()%></b>
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <%for (Iterator it = data.iterator(); it.hasNext(); )
        {
          MSInvoiceDataDescriptor msidd = (MSInvoiceDataDescriptor)it.next();
          if (msidd.getInvChargeDescriptionCode().equalsIgnoreCase("header"))
          {
      %>
                <tr>
                  <td colspan="6" align="left" width="100%" style="font: smaller Arial"><b><%=msidd.getInvNotes()%></b>
                  </td>
                </tr>
                <tr>
                  <td colspan="6" align="left" width="100%" style="font: smaller Arial"><b><i>Charge Description</i></b>
                  </td>
                </tr>
      <%  }
          else
          {
            sumCharges = sumCharges.add(msidd.getInvChargeAmountBD());
            taxTotal = msidd.getInvTaxTotalBD();
      %>
      <!-- start repeat
      -->
                <tr>
                  <td colspan="1" width="1%" style="font: smaller Arial"> 
                  </td>
                  <td colspan="1" align="left" width="43%" style="font: smaller Arial"><%=msidd.getInvNotes()%>
                  </td>
                  <td valign="top" align="left" colspan="1" width="22%" style="font: smaller Arial"><%=isAdjustment?"":msidd.getInvChargePeriod()%>
                  </td>
                  <td valign="top" align="left" colspan="2" width="18%" style="font: smaller Arial"><%=isAdjustment?"":msidd.getInvCompanyDetails1()%>
                  </td>
<!--
                  <td valign="top" align="left" colspan="1" width="9%" style="font: smaller Arial">
                  </td>
-->
                  <td valign="top" align="right" colspan="1" width="16%" style="font: smaller Arial"><%=msidd.getInvChargeAmountBD().toString()%>
                  </td>
                </tr>
      <%
          }
        }
        /*total = sumCharges + taxTotal;
        String net = SU.roundToString((double)sumCharges/100, 2);
        String tax = SU.roundToString((double)taxTotal/100, 2);
        String gross = SU.roundToString((double)total/100, 2);*/
        String net = sumCharges.toString();
        String tax = taxTotal.toString();
        sumCharges = sumCharges.add(taxTotal);
        String gross = sumCharges.toString();
      %>
      <!-- end repeat
      -->
              </table>
              <table>
                <tr>
                  <td> 
                </td>
                </tr>
                <tr>
                  <td> 
                  </td>
                </tr>
                <tr>
                  <td> 
                  </td>
                </tr>
                <tr>
                  <td> 
                  </td>
                </tr>
                <tr>
                  <td> 
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td align="left" colspan="1" style="font: smaller Arial" width="18%"><nobr>Payment Terms:</nobr>
                  </td>
                  <td align="left" colspan="1" style="font: smaller Arial" width="22%"> 
                  </td>
                  <td align="left" colspan="2" style="font: smaller Arial" width="30%"><nobr>Total Charges (excluding tax)</nobr>
                  </td>
                  <td align="right" colspan="1" style="font: smaller Arial" width="30%"><%=net%>
                  </td>
                </tr>
      <!--
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
      -->
                <tr>
                  <td rowspan="3" valign="top" align="left" colspan="1" style="font: smaller Arial" width="18%">This
                    invoice is due for payment within 30 days of invoice
                  </td>
                  <td rowspan="3" align="left" colspan="1" style="font: smaller Arial" width="22%"> 
                  </td>
                  <td valign="top" align="left" colspan="2" style="font: smaller Arial" width="30%">Total Tax <%=noTaxType?"":("("+BAN.getTaxType()+")")%>
                  </td>
                  <td valign="top" align="right" colspan="1" style="font: smaller Arial" width="30%"><%=tax%>
                  </td>
                </tr>
                <tr>
                <td valign="bottom" align="left" colspan="2" style="font: smaller Arial" width="70%"><b><nobr>Total Payments Due</nobr></b>
                </td>
                <td valign="top" align="right" colspan="1" style="font: smaller Arial" width="30%"> 
                  <table width="70%" border="2" bordercolor="#aaaaff" cellspacing="0" cellpadding="0">
                    <tr>
                      <td bgcolor="#aaaaff" align="center" colspan="1" style="font: smaller Arial" width="50%">
                        <font color="white">AMOUNT</font>
                      </td>
                      <td align="right" colspan="1" style="font: smaller Arial" width="50%"><b><%=gross%></b>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            <table>
              <tr>
              <tr>
                <td colspan="5" style="text-align:left; font: xx-small Arial">This invoice is in <%=BAN.getOutgoingCurrencyCode()%>
                  (<%=BAN.getOutgoingCurrencyDesc()%>)<%=(noTaxType||(taxTotal.doubleValue()==0))?"":("and "+BAN.getTaxType()+" applied at "+ BAN.getTaxRate())%>.
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
              </tr>
            </table>
          </table>
        </tr>
      </table>
      </td>
      </tr>
      </table>
    </td>
  </tr>
</table>

</form>
</body>
</html>


