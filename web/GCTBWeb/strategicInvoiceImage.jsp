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
  String accountId = BAN.getAccountId();
  String[] details = BAN.getStrategicInvoiceDetails();
  double totalTax = Double.parseDouble(details[8]);
  boolean showTax = totalTax != 0;
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
      <table width="625" border="1" cellspacing="0" cellpadding="5">
        <tr>
          <td>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td>
                  <table border="0" style="font: smaller Arial; width:100%;">
                    <tr>
                      <td width="10%"> 
                      </td>
                      <td><%=BAN.getBillingCustomerName()%>
                      </td>
                    </tr>
      <%for (int i=0; i<7; i++)
        {
          if (!ba[i].equals("&nbsp;"))
          {
      %>
                    <tr>
                      <td width="100"> 
                      </td>
                      <td><font face="Arial" size="-1"><%=ba[i]%></font>
                      </td>
                    </tr>
      <%  }
        }%>
                  </table>
                </td>
                <td align="left">
                  <table border="0" cellpadding="0" cellspacing="0" width="360">
                    <tbody>
                      <tr>
                        <td rowspan="2" width="120">
                          &nbsp;
                        </td>
                        <td bgcolor="#aaaaff" valign="bottom" align="center" height="133">
<%if (!BAN.getCompanyAddressId().equals("44"))
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
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
            </table>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="6" valign="top">
                </td>
                <td colspan="3" align="left" valign="top">
                  <table border="0" style="text-align:left;font: smaller Arial; width:100%">
                    <tr>
                      <td style="font: xx-small Arial"><%=BAN.getCompanyName()%>
                      </td>
                    </tr>
      <%for (int i=0; i<7; i++)
        {
          if (!ca[i].equals("&nbsp;"))
          {
      %>
                    <tr>
                      <td style="font: xx-small Arial"><%=ca[i]%>
                      </td>
                    </tr>
      <%  }
        }%>
                  </table>
                </td>
              </tr>
              <tr>
                <td valign="bottom" rowspan="2" colspan="7" style="text-align:left; font: bold larger Arial">Invoice
                </td>
                <td style="text-align:left; font: xx-small Arial"><nobr><%=BAN.getTaxReferenceLiteral()%></nobr>
                </td>
                <td style="text-align:left; font: xx-small Arial"><nobr><%=BAN.getTaxReference()%></nobr>
                </td>
              </tr>
              <tr>
                <td style="text-align:left; font: xx-small Arial">Company No:
                </td>
                <td style="text-align:left; font: xx-small Arial"><nobr><%=BAN.getCompanyDetails1()%></nobr>
                </td>
              </tr>
              <tr>
                <td colspan="9"> 
                </td>
              </tr>
              <tr>
                <td width="10%" style="text-align:left; font: bold smaller Arial">Invoice Date:
                </td>
                <td width="45%" colspan="3" style="text-align:left; font: xx-small Arial"><%=taxPointDate%>
                </td>
                <td width="20%" style="text-align:left; font: bold smaller Arial"><nobr>Account No: </nobr>
                </td>
                <td width="10%"> 
                </td>
                <td colspan="3" style="text-align:left; font: xx-small Arial"><%=accountId.substring(accountId.indexOf("/")+1)%>
                </td>
              </tr>
              <tr>
                <td style="text-align:left; font: bold smaller Arial">Period:
                </td>
                <td colspan="3" style="text-align:left; font: xx-small Arial"><%=details[12]%>
                </td>
                <td style="text-align:left; font: bold smaller Arial">Invoice No:
                </td>
                <td> 
                </td>
                <td colspan="3" style="text-align:left; font: xx-small Arial"><%=BAN.getInvoiceNo()%>
                </td>
              </tr>
              <tr>
                <td style="text-align:left; font: bold smaller Arial"><nobr> </nobr>
                </td>
                <td colspan="3" style="text-align:left; font: xx-small Arial">
                </td>
                <td colspan="2" style="text-align:left; font: bold smaller Arial">
                </td>
                <td style="text-align:left; font: xx-small Arial">
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td> 
                </td>
                <td colspan="3"> 
                </td>
                <td colspan="2" style="text-align:left; font: bold smaller Arial">CCD No: 
                </td>
                <td style="text-align:left; font: xx-small Arial"><%=details[0]%>
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td colspan="4" style="border-bottom: 1px solid black;text-align:left; font: bold xx-small Arial">This period
                </td>
                <td style="border-bottom: 1px solid black;text-align:right; font: bold xx-small Arial">Charges
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td colspan="2" style="text-align:left; font: bold smaller Arial">What can we do for you?
                </td>
              </tr>
              <tr>
                <td colspan="4" style="text-align:left; font: xx-small Arial">Services
                </td>
                <td style="text-align:right; font: xx-small Arial"><%=details[1]%>
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td colspan="2" valign="top" rowspan="2" style="text-align:left; font: xx-small Arial"><%=BAN.getCustomerContact()%>
                </td>
              </tr>
              <tr>
                <td colspan="4" style="text-align:left; font: xx-small Arial">Sundry Charges
                </td>
                <td style="text-align:right; font: xx-small Arial"><%=details[2]%>
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td colspan="4" style="border-bottom: 1px solid black;text-align:left; font: xx-small Arial">Adjustments
                </td>
                <td style="border-bottom: 1px solid black;text-align:right; font: xx-small Arial"><%=details[3]%>
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td colspan="4" style="text-align:left; font: bold xx-small Arial">Total (excluding tax)
                </td>
                <td style="text-align:right; font: bold xx-small Arial"><%=details[4]%>
                </td>
                <td width="1%" style="text-align:left; font: bold xx-small Arial"><%=BAN.getOutgoingCurrencyCode()%>
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td colspan="9"> 
                </td>
              </tr>
              <tr>
                <td colspan="5" style="border-bottom: 1px solid black;text-align:left; font: bold xx-small Arial">Taxes
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td colspan="2" style="text-align:left; font: bold smaller Arial">Web site
                </td>
              </tr>
              <tr>
                <td colspan="4" style="text-align:left; font: xx-small Arial"><%=details[10].equals("n/a")?"Tax":(details[10] + " " + details[11] + "%")%>
                </td>
                <td style="text-align:right; font: xx-small Arial"><%=details[5]%>
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td colspan="2" style="text-align:left; font: xx-small Arial"><nobr>https://e-billing.cw.com</nobr>
                </td>
              </tr>
              <tr>
                <td colspan="4" style="text-align:left; font: xx-small Arial">Sundry Charges (tax only)
                </td>
                <td style="text-align:right; font: xx-small Arial"><%=details[6]%>
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td colspan="4" style="border-bottom: 1px solid black;text-align:left; font: xx-small Arial">Adjustments (tax only)
                </td>
                <td style="border-bottom: 1px solid black;text-align:right; font: xx-small Arial"><%=details[7]%>
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td colspan="4" style="text-align:left; font: bold xx-small Arial">Total <%=details[10].equals("n/a")?"Tax":details[10]%>
                </td>
                <td style="text-align:right; font: bold xx-small Arial"><%=details[8]%>
                </td>
                <td style="text-align:left; font: bold xx-small Arial"><%=BAN.getOutgoingCurrencyCode()%>
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td colspan="5" style="text-align:left; font: xx-small Arial">This invoice is in
                  <%=BAN.getOutgoingCurrencyCode() +
                  " (" + BAN.getOutgoingCurrencyDesc() + ")" +
                  (((details[10].equals("n/a"))||(!showTax))?".":(" and " + details[10] +
                  " applied at " + details[11] + "%"))%></font>
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td colspan="9"> 
                </td>
              </tr>
              <tr>
                <td colspan="4" style="border-bottom: 1px solid black;border-top: 1px solid black;text-align:left; font: bold xx-small Arial">
                  Total this period amount (including tax)
                </td>
                <td style="border-bottom: 1px solid black;border-top: 1px solid black;text-align:right; font: bold xx-small Arial"><%=details[9]%>
                </td>
                <td style="text-align:left; font: bold xx-small Arial"><%=BAN.getOutgoingCurrencyCode()%>
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td colspan="9"> 
                </td>
              </tr>
              <tr>
                <td colspan="6" style="font: xx-small Arial;">Please pay the balance from your previous invoices immediately. Please pay the rest
                                                       of the invoice total as soon as possible, and no later than 30 days from the invoice date.
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td height="5px" colspan="9">
                </td>
              </tr>
              <tr>
                <td height="5px">
                </td>
                <td height="5px" width="2%">
                </td>
                <td height="5px" colspan="7"></td></tr><tr><td valign="top" colspan="2" style="font: bold xx-small Arial;"><nobr>Your registered address: </nobr>
                </td>
                <td valign="top" colspan="4" style="font: xx-small Arial;">
                </td>
                <td> 
                </td>
                <td>
<%if (!BAN.getCompanyAddressId().equals("44"))
  {%>
                  <img border="0" src="../nr/cw/newimages/newLogoSmall.gif"></img>
<%}%>
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td colspan="6" style="font: bold xx-small Arial;"><b></b> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
                <td> 
                </td>
              </tr>
              <tr>
                <td colspan="6" style="font: bold xx-small Arial;">All charges shown are inclusive of discount
                </td>
                <td> 
                </td>
                <td colspan="2" style="font: bold larger Arial;"> 
                </td>
              </tr>
              <tr>
                <td colspan="9" style="font: xx-small Arial;">To pay by post send payments to: <%=bka[0]%>,
<%for (int i=1; i<7; i++)
  {
    if ((!bka[i].equals("&nbsp;")) && (bka[i].indexOf(":") == -1))
    {
%>
                   , <%=bka[i]%>
<%  }
  }%>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>