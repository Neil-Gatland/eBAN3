<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
  <title>Print Invoice</title>
  <style type="text/css">
  <!--
  .address { overflow: hidden; font-family: Arial, Helvetica, Geneva, Swiss, SunSans-Regular; font-size: 12px; font-weight: normal; }
  .bigbold { overflow: hidden; font-family: Arial, Helvetica, Geneva, Swiss, SunSans-Regular; font-size: 12px; font-weight: bolder; }
  .smallbold { overflow: hidden; font-family: Arial, Helvetica, Geneva, Swiss, SunSans-Regular; font-size: 10px; font-weight: bolder; }
  .smallnorm { overflow: hidden; font-family: Arial, Helvetica, Geneva, Swiss, SunSans-Regular; font-size: 10px; font-weight: normal; }
  -->
  </style>
</head>
<%@ page import="DBUtilities.DBAccess,java.util.Enumeration,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="acb" class="DBUtilities.AssureChargeBean" scope="session"/>
<%
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  DBAccess DBA = new DBAccess();
  String Account_No = "";
  String Invoice_No = "";
  String Billing_Source = "";
  String Address_Line_1 = "";
  String Address_Line_2 = "";
  String Address_Line_3 = "";
  String Postcode = "";
  String Site_Country = "";
  Enumeration address = null;
  String Contact_Name = "";
  String Message = null;
  String VAT_Amount = "";
  String Total_Amount = "";
  String Fault_Details = "";
  String Fault_Code = "";
  String onLoad = "";
  String text = "Please make sure your browser settings are correct before printing. " +
    "For more information click 'Help'.";

  if (request.getParameter("fromSelf") != null)
  {
    onLoad = "window.close();";
    acb.setBanStatus("Printed");
    acb.setBanCreatedBy((String)session.getAttribute("User_Id"));
    acb.updateAssureCharge();
  }
  else
  {
    Account_No = acb.getAccount_Id();
    Invoice_No = acb.getBanIdentifier();
    Billing_Source = acb.getBillingSource();
    address = DBA.getAddress(Account_No, Billing_Source);
    Contact_Name = (String)address.nextElement();
    if (Contact_Name.startsWith("error - "))
    {
      Message = "<font color=\"red\">"+Contact_Name+"</font>";
      Contact_Name = "";
    }
    else
    {
      if (Contact_Name.equals("not found"))
      {
        Message = "<font color=\"red\">No address details found for this account</font>";
        Contact_Name = "";
      }
      else
      {
        Address_Line_1 = (String)address.nextElement();
        Address_Line_2 = (String)address.nextElement();
        Address_Line_3 = (String)address.nextElement();
        Postcode = (String)address.nextElement();
        Site_Country = (String)address.nextElement();
      }
    }
    Enumeration vatAmounts = acb.getVATAmounts();
    VAT_Amount = (String)vatAmounts.nextElement();
    Total_Amount = (String)vatAmounts.nextElement();
    Fault_Code = acb.getFault_Code();
    if (Fault_Code.endsWith("a"))
    {
      Fault_Details = acb.getFault_Details();
    }
    else
    {
      Fault_Details = acb.getFaultDetailsPrint();
    }
  }
 %>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--

  function helpClick()
  {
  window.open('PrintHelp.jsp','helppop',
  'toolbar=no,menubar=yes,location=no,directories=no,status=no,scrollbars=auto,resizable=yes,height=550,width=600,top=50,left=50');
  }

  function closeClick()
  {
    window.close();
  }

  function bsubmitClick()
  {
    Print.bsubmit.style.visibility = "hidden";
    Print.close.style.visibility = "hidden";
    Print.help.style.visibility = "hidden";
    window.print();
    //window.close();
    Print.submit();
  }
  function window_onload()
  {
    <%=onLoad%>
  }
  //-->
  </script>
<body language=javascript onload="return window_onload()">
<form name="Print" method="post" action="Print.jsp">
<input type="hidden" name="fromSelf" value="true">
<table width="760px" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td width="35px" height="48px">&nbsp;
    </td>
    <td width="96px">&nbsp;
    </td>
    <td width="204px">&nbsp;
    </td>
    <td width="60px">&nbsp;
    </td>
    <td width="55px">&nbsp;
    </td>
    <td width="60px">&nbsp;
    </td>
    <td width="65px">&nbsp;
    </td>
    <td width="60px">&nbsp;
    </td>
    <td width="170px">&nbsp;
    </td>
  </tr>
  <tr>
    <td colspan="9" align="center" height="80px">
<%if (Message == null)
  {%>
      <%=HB.getImageAsAnchor("bsubmit", text)%>
      <%=HB.getImageAsAnchor("close")%>
      <%=HB.getImageAsAnchor("help")%>
<%}
  else
  {%>
      <%=Message%>
<%}%>
    </td>
  </tr>
  <tr>
    <td colspan="2" class="address">&nbsp;
    </td>
    <td colspan="6" class="address">
      <%=acb.getContact_Name()%><br>
      <%=Address_Line_1%><br>
<%if (!Address_Line_2.equals(""))
  {%>
      <%=Address_Line_2%><br>
<%}%>
<%if (!Address_Line_3.equals(""))
  {%>
      <%=Address_Line_3%><br>
<%}%>
      <%=Postcode%>
    </td>
    <td class="smallbold">
      Bill &amp; General Enquiries<br>Freecall: <%=acb.getGeneralNo()%><br>&nbsp;<br>
      Faults: <%=acb.getFaultNo()%>
    </td>
  </tr>
  <tr>
    <td height="45px" colspan="9">&nbsp;
    </td>
  </tr>
  <tr>
    <td>&nbsp;
    </td>
    <td colspan="2" class="smallbold">
      Account Number: <%=Account_No%><br>
      Invoice Number: <%=Invoice_No%>
    </td>
    <td colspan="5" class="smallbold">
      Date/tax point: <%=acb.getPrintCreatedDate()%><br>
      Page: 1
    </td>
    <td>&nbsp;
    </td>
  </tr>
  <tr>
    <td height="10px" colspan="9">
    </td>
  </tr>
  <tr>
    <td>&nbsp;
    </td>
    <td colspan="8" class="smallbold">
      Customer Name: <%=acb.getContact_Name()%>
    </td>
  </tr>
  <tr>
    <td height="25px" colspan="9">&nbsp;
    </td>
  </tr>
  <tr>
    <td>&nbsp;
    </td>
    <td colspan="2" class="smallbold" valign="top">
      Description
    </td>
    <td class="smallbold" align="center" valign="top">
      Amount<br>£
    </td>
    <td class="smallbold" align="center" valign="top">
      VAT<br>Rate
    </td>
    <td class="smallbold" align="center" valign="top">
      VAT<br>£
    </td>
    <td class="smallbold" align="center" valign="top">
      Total<br>£
    </td>
    <td colspan="2">&nbsp;
    </td>
  </tr>
  <tr>
    <td height="20px" colspan="9">
    </td>
  </tr>
  <tr>
    <td>&nbsp;
    </td>
    <td colspan="2" class="smallbold">
      Engineer Callout Charge
    </td>
    <td class="smallnorm" align="center">
      <%=acb.getInvoice_Amount()%>
    </td>
    <td class="smallnorm" align="center">
      <%=acb.getVATRate()%>%
    </td>
    <td class="smallnorm" align="center">
      <%=VAT_Amount%>
    </td>
    <td class="smallnorm" align="center">
      <%=Total_Amount%>
    </td>
    <td colspan="2">&nbsp;
    </td>
  </tr>
  <tr>
    <td>
    </td>
    <td colspan="6"><hr>
    </td>
    <td colspan="2">
    </td>
  </tr>
  <tr>
    <td>&nbsp;
    </td>
    <td class="smallbold">
      Date of Visit:
    </td>
    <td class="smallnorm">
      <%=acb.getVisit_Date_Print()%>
    </td>
    <td colspan="2" class="smallbold">
<%if (!acb.getCircuit_Ref().equals(""))
  {%>
      Circuit Reference/CLI:
<%}%>
    </td>
    <td colspan="2" class="smallnorm">
<%if (!acb.getCircuit_Ref().equals(""))
  {%>
      <%=acb.getCircuit_Ref()%>
<%}%>
    </td>
    <td colspan="2">&nbsp;
    </td>
  </tr>
  <tr>
    <td height="5px" colspan="9">
    </td>
  </tr>
  <tr>
    <td rowspan="3">&nbsp;
    </td>
    <td class="smallbold" valign="top" rowspan="3">
      Site Address:
    </td>
    <td class="smallnorm" height="60px" valign="top" rowspan="3">
      <%=acb.getSite_Address_1()%><br>
<%if (!acb.getSite_Address_2().equals(""))
  {%>
      <%=acb.getSite_Address_2()%><br>
<%}
  if (!acb.getSite_Address_3().equals(""))
  {%>
      <%=acb.getSite_Address_3()%><br>
<%}%>
      <%=acb.getSite_Postcode()%>
    </td>
    <td colspan="2" class="smallbold" valign="top">
      Fault Reference:
    </td>
    <td colspan="2" class="smallnorm" valign="top">
      <%=acb.getSR_Ref()%>
    </td>
    <td rowspan="3" colspan="2">&nbsp;
    </td>
  <tr>
    <td colspan="2" class="smallbold" valign="top">
<%if (!acb.getSignatory().equals(""))
  {%>
      Customer Signatory:
<%}%>
    </td>
    <td colspan="2" class="smallnorm" valign="top">
<%if (!acb.getSignatory().equals(""))
  {%>
      <%=acb.getSignatory()%>
<%}%>
    </td>
  </tr>
  <tr>
    <td colspan="4" class="smallbold" valign="top">&nbsp;
    </td>
  </tr>
  <tr>
    <td height="33px">&nbsp;
    </td>
    <td class="smallbold" valign="top">
      Fault Details:
    </td>
    <td colspan="5" class="smallnorm" valign="top">
      <%=Fault_Details%>
    </td>
    <td colspan="2">&nbsp;
    </td>
  </tr>
  <tr>
    <td height="210px">&nbsp;
    </td>
    <td class="smallbold" valign="top">
<%if (!acb.getCustomer_Info().equals("") && !acb.getCustomer_Info().equals(" "))
  {%>
      Customer Details:
<%}%>
    </td>
    <td colspan="5" class="smallnorm" valign="top">
<%if (!acb.getCustomer_Info().equals("") && !acb.getCustomer_Info().equals(" "))
  {%>
      <%=acb.getCustomer_Info()%>
<%}%>
    </td>
    <td colspan="2">&nbsp;
    </td>
  </tr>
  <tr>
    <td height="20px" colspan="9">
    </td>
  </tr>
  <tr>
    <td>&nbsp;
    </td>
    <td colspan="5" class="smallbold">
      Total Charge
    </td>
    <td class="bigbold" align="center">
      <%=Total_Amount%>
    </td>
    <td colspan="2">&nbsp;
    </td>
  </tr>
  <tr>
    <td height="125px" colspan="9">&nbsp;
    </td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;
    </td>
    <td colspan="3">
      <font size="4"><tt><b><%=Account_No+Invoice_No%></b></tt></font>
    </td>
    <td colspan="2" align="center">
      <font size="4"><tt><b><%=Total_Amount%></b></tt></font>
    </td>
    <td colspan="2">&nbsp;
    </td>
  </tr>
</table>
</form>
</body>
</html>

