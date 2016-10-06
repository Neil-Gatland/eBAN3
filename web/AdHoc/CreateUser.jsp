<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
  <title>User Maintenance</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="DBUtilities.DBAccess,java.util.Enumeration,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="acb" class="DBUtilities.AssureChargeBean" scope="session"/>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  DBAccess DBA = new DBAccess();
  String User_Name = "";
  String User_Id = "";
  String Password = "";
  String User_Group = "";
  String onLoad = "";
  String System = (String)session.getAttribute("System");
  String type = (String)request.getParameter("type");
  String refresh = (String)request.getParameter("refresh");
  String Message = type.equals("create")?
    "<font color=\"blue\">Please enter all the User details below and then press 'Submit'</font>"
    :"<font color=\"blue\">Please select the required User Id, amend the details and then press 'Submit'</font>";
  if (request.getParameter("fromSelf") != null)
  {
    User_Id = (String)request.getParameter("User_Id");
    if (refresh.equals("true"))
    {
      Enumeration User_Details = DBA.getUser(System, User_Id);
      if (User_Details.hasMoreElements())
      {
        User_Name = (String)User_Details.nextElement();
        Password = (String)User_Details.nextElement();
        User_Group = (String)User_Details.nextElement();
      }
      else
      {
        Message = "<font color=\"red\">Unable to retrieve User details</font>";
      }
    }
    else
    {
      User_Name = (String)request.getParameter("User_Name");
      Password = (String)request.getParameter("Password");
      User_Group = (String)request.getParameter("User_Group");
      String Created_By = (String)session.getAttribute("User_Id");
      Message = "<font color=\"red\">" +
        DBA.createUser(User_Name, User_Id, Password, User_Group, System,
        Created_By, type) + "</font>";
      Password = "";
    }
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
    if (CreateUser.User_Name.value == "")
    {
      alert('Please enter a user name');
    }
<%if (type.equals("create"))
  {%>
    else if (CreateUser.User_Id.value == "")
    {
      alert('Please enter a user id');
    }
<%}
  else
  {%>
    if (CreateUser.User_Id.selectedIndex == 0)
    {
      alert('Please select a user id');
    }
<%}%>
    else if (CreateUser.Password.value == "")
    {
      alert('Please enter a password');
    }
    else if (CreateUser.Password.value != CreateUser.Confirm_Password.value)
    {
      alert("'Password' and 'Confirm Password' values are not the same");
    }
    else if (CreateUser.User_Group.selectedIndex == 0)
    {
      alert('Please select a User Group');
    }
    else
    {
      CreateUser.refresh.value = "false";
      CreateUser.submit();
    }
  }
  function window_onload()
  {
    <%=onLoad%>
  }
  //-->
  </script>
<body language=javascript onload="return window_onload()">
<form name="CreateUser" method="post" action="CreateUser.jsp">
<input type="hidden" name="fromSelf" value="true">
<input type="hidden" name="refresh" value="true">
<input type="hidden" name="type" value="<%=type%>">
<table>
  <tr>
    <td colspan=2 align="center">
      <h3><b><%=type.equals("create")?"Create":"Amend"%> User<b></h3>
    </td>
  </tr>
  <tr>
    <td colspan=2 align="center">
      <%=Message%>
    </td>
  </tr>
  <tr>
    <td>
      <font color="#0000FF">User Name:</font>
    </td>
    <td>
      <input class=inp type=text name="User_Name" size="35"
      value="<%=User_Name%>">
    </td>
  </tr>
  <tr>
    <td>
      <font color="#0000FF">User Id:</font>
    </td>
    <td>
<%if (type.equals("create"))
  {%>
      <input class=inp type=text name="User_Id" size="35"
      value="<%=User_Id%>">
<%}
  else
  {%>
      <%=DBA.getListBox("User_Id","submit",User_Id,System)%>
<%}%>
    </td>
  </tr>
  <tr>
    <td>
      <font color="#0000FF">Password:</font>
    </td>
    <td>
      <input class=inp type=password name="Password" size="35"
      value="<%=Password%>">
    </td>
  </tr>
  <tr>
    <td>
      <font color="#0000FF"><nobr>Confirm Password:</nobr></font>
    </td>
    <td>
      <input class=inp type=password name="Confirm_Password" size="35"
      value="<%=Password%>">
    </td>
  </tr>
  <tr>
    <td>
      <font color="#0000FF"><nobr>User Group:</nobr></font>
    </td>
    <td>
      <%=DBA.getListBox("User_Group","input",User_Group,System)%>
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

