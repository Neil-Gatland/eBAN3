<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
  <title>Create a Customer</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="acb" class="DBUtilities.AssureChargeBean" scope="session"/>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
  String Customer_Name = "";
  String Message = "";
  String onLoad = "";
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  if (request.getParameter("fromSelf") != null)
  {
    Customer_Name = (String)request.getParameter("Customer_Name");
    DBAccess DBA = new DBAccess();
    String SQL = "Select 'Found' from eBAN..Ad_Hoc_Customer where Party_Name = '" +
      Customer_Name + "'";
    if(DBA.getExists(SQL, "P5"))
    {
      Message = "A customer with this name exists already";
    }
    else
    {
      int Customer_Id = acb.createCustomer(Customer_Name);
      //String test = acb.createCustomerTest(Customer_Name);
      if (Customer_Id > 0)
      {
        onLoad = "window.open('AdHocMenu.jsp','ahmain');window.close();";
        session.setAttribute("PageSent",request.getRequestURI());
        BAN.setGlobalCustomerId(Integer.toString(Customer_Id));
        BAN.setAccount_Id("");
        BAN.setAccountFilter("");
      }
      else
      {
        Message = acb.getMessage();
        //Message = test;
      }
    }
  }
 %>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--
  function bsubmitClick()
  {
    if (CustomerPopUp.Customer_Name.value == "")
    {
      alert('Please enter a customer name');
    }
    else
    {
      CustomerPopUp.submit();
    }
  }
  function window_onload()
  {
    <%=onLoad%>
  }
  //-->
  </script>
<body language=javascript onload="return window_onload()">
<form name="CustomerPopUp" method="post" action="CustomerPopUp.jsp">
<input type="hidden" name="fromSelf" value="true">
<table>
  <tr>
    <td colspan=2 align="center">
      <h3><b>Create a Customer<b></h3>
    </td>
  </tr>
  <tr>
    <td colspan=2 align="center">
      <font color="red"><%=Message%></font>
    </td>
  </tr>
  <tr>
    <td>
      <font color="#0000FF">Customer Name:</font>
    </td>
    <td>
      <input class=inp type=text name="Customer_Name" size="35"
      value="<%=Customer_Name%>">
    </td>
  </tr>
  <tr>
    <td colspan=2 align="center">
      <%=HB.getImageAsAnchor("bsubmit")%>
    </td>
  </tr>
</table>
</form>
</body>
</html>

