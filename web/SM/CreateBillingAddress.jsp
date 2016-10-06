<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
  <title>Billing Address Maintenance</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="DBUtilities.DBAccess,java.util.Enumeration,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  DBAccess DBA = new DBAccess();
  StringUtil SU = new StringUtil();
  String System = (String)session.getAttribute("System");
  String type = "";
  String Account_No = (String)request.getParameter("account");
  String Billing_Source = SU.isNull((String)request.getParameter("bsource"),"");
  String Message = "";
  String Contact_Name = "";
  String Address_Line_1 = "";
  String Address_Line_2 = "";
  String Address_Line_3 = "";
  String Postcode = "";
  String Site_Country = "";
  if (request.getParameter("fromSelf") == null)
  {
    Enumeration address = DBA.getAddress(Account_No, Billing_Source);
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
        type = "create";
        Contact_Name = "";
      }
      else
      {
        type = "update";
        Address_Line_1 = (String)address.nextElement();
        Address_Line_2 = (String)address.nextElement();
        Address_Line_3 = (String)address.nextElement();
        Postcode = (String)address.nextElement();
        Site_Country = (String)address.nextElement();
      }
      Message = "<font color=\"blue\">Please "+(type.equals("create")?"enter":"amend")+
        " the Address details below and then press 'Submit'.<br>Mandatory fields are shown in blue.</font>";
    }
  }
  else
  {
    type = (String)request.getParameter("type");
    Contact_Name = (String)request.getParameter("Contact_Name");
    Address_Line_1 = (String)request.getParameter("Address_Line_1");
    Address_Line_2 = (String)request.getParameter("Address_Line_2");
    Address_Line_3 = (String)request.getParameter("Address_Line_3");
    Postcode = (String)request.getParameter("Postcode");
    Site_Country = (String)request.getParameter("Site_Country");
    String Created_By = (String)session.getAttribute("User_Id");
    Message = "<font color=\"blue\">"+
      DBA.createAddress(Account_No, Billing_Source, Contact_Name,
      Address_Line_1, Address_Line_2, Address_Line_3, Postcode, Site_Country,
      Created_By, type)+"</font>";
  }
 %>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--

  function closeClick()
  {
    window.close();
  }

  function bsubmitClick()
  {
    if (CreateAddress.Contact_Name.value == "")
    {
      alert('Please enter a contact name');
    }
    else if (CreateAddress.Address_Line_1.value == "")
    {
      alert('Please enter the first line of the address');
    }
    else if (CreateAddress.Postcode.value == "")
    {
      alert('Please enter a postcode');
    }
    else if (CreateAddress.Site_Country.value == "")
    {
      alert('Please select a country');
    }
    else
    {
      CreateAddress.submit();
    }
  }
  //-->
  </script>
<body>
<form name="CreateAddress" method="post" action="CreateBillingAddress.jsp">
<input type="hidden" name="fromSelf" value="true">
<input type="hidden" name="type" value="<%=type%>">
<input type="hidden" name="account" value="<%=Account_No%>">
<input type="hidden" name="bsource" value="<%=Billing_Source%>">
<table>
  <tr>
    <td colspan=2 align="center">
      <h3><b><%=type.equals("create")?"Create":"Amend"%> Billing Address<b></h3>
    </td>
  </tr>
  <tr>
    <td colspan=2 align="center">
      <%=Message%>
    </td>
  </tr>
  <tr>
    <td>
      &nbsp;
    </td>
  </tr>
  <tr>
    <td>
      <font color="#0000FF">Contact Name:</font>
    </td>
    <td>
      <input class=inp type=text name="Contact_Name" size="35"
      value="<%=Contact_Name%>">
    </td>
  </tr>
  <tr>
    <td>
      <font color="#0000FF">Address Line 1:</font>
    </td>
    <td>
      <input class=inp type=text name="Address_Line_1" size="35"
      value="<%=Address_Line_1%>">
    </td>
  </tr>
  <tr>
    <td>
      <font color="#000000">Address Line 2:</font>
    </td>
    <td>
      <input class=inp type=text name="Address_Line_2" size="35"
      value="<%=Address_Line_2%>">
    </td>
  </tr>
  <tr>
    <td>
      <font color="#000000">Address Line 3:</font>
    </td>
    <td>
      <input class=inp type=text name="Address_Line_3" size="35"
      value="<%=Address_Line_3%>">
    </td>
  </tr>
  <tr>
    <td>
      <font color="#0000FF">Postcode:</font>
    </td>
    <td>
      <input class=inp type=text name="Postcode" size="35"
      value="<%=Postcode%>">
    </td>
  </tr>
  <tr>
    <td>
      <font color="#0000FF">Country:</font>
    </td>
    <td>
      <%=DBA.getListBox("Site_Country","input",Site_Country,System)%>
    </td>
  </tr>
  <tr>
    <td>
      &nbsp;
    </td>
  </tr>
  <tr>
    <td colspan="2" align="center">
      <%=HB.getImageAsAnchor("bsubmit")%>
      <%=HB.getImageAsAnchor("close")%>
    </td>
  </tr>
</table>
</form>
</body>
</html>

