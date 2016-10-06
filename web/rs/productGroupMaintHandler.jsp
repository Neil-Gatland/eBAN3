<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="PGB" class="DBUtilities.ProductGroupBean" scope="session"/>
<jsp:useBean id="PRD" class="DBUtilities.ProductBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","productGroupMaintHandler");
    ButtonPressed = request.getParameter("ButtonPressed");
    if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="productGroupMaint.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="productGroupMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="productGroupMaint.jsp">
         <jsp:param name="viewProcess" value="true"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Revenue Share Desktop"))
    {
      RSB.canCloseMonth();
      %>
        <jsp:forward page="rsDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Reference Data Menu"))
    {
      %>
        <jsp:forward page="referenceDataMenu.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Add New Product"))
    {
      PRD.Reset();
      PRD.setUserId((String)session.getAttribute("User_Id"));
      PRD.setProductGroup(PGB.getProductGroup());
      PRD.setReturnTo("productGroupMaint.jsp");
      PRD.setAction("Add");
      PRD.setMode("Create");
      PRD.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      %>
        <jsp:forward page="productMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("list_ProductDelete"))
    {
      PRD.Reset();
      PRD.setUserId((String)session.getAttribute("User_Id"));
      PRD.setProductGroup(PGB.getProductGroup());
      PRD.setProductCode(request.getParameter("productId"));
      PRD.getProductFromDB();
      PRD.setReturnTo("productGroupMaint.jsp");
      PRD.setAction("Delete");
      PRD.setMode("Delete");
      PRD.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      %>
        <jsp:forward page="productMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      String action = PGB.getAction();
      String pG = PGB.getProductGroup();
      PGB.Reset();
      PGB.setAction(action);
      if (action.equals("Add"))
      {
        PGB.setMode("Create");
        PGB.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        PGB.setProductGroup(pG);
        PGB.getProductGroupFromDB();
        PGB.populateProductList();
        PGB.setMode("Delete");
        PGB.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        PGB.setProductGroup(pG);
        PGB.getProductGroupFromDB();
        PGB.populateProductList();
        PGB.setMode("Amend");
        PGB.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="productGroupMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      PGB.setMode("Confirm");
      PGB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this Product Group or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="productGroupMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = PGB.getAction();
      if (action.equals("Add"))
      {
        if (PGB.createProductGroup())
        {
          RSB.setProductGroupId(PGB.getProductGroup());
          PGB.setAction("Amend");
          PGB.setMode("Amend");
          PGB.setMessage("<font color=blue><b>Product group created. To create a new Product for " +
            "this Product Group, select 'Add New Product' " +
            "from the 'Options' menu above.</b></font>");
          %>
            <jsp:forward page="productGroupMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Delete"))
      {
        if (PGB.deleteProductGroup())
        {
          RSB.setMessage(PGB.getMessage());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="productGroupMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Amend"))
      {
        if (PGB.updateProductGroup())
        {
          RSB.setMessage(PGB.getMessage());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="productGroupMaint.jsp"/>
          <%
        }
      }
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      if (PGB.getAction().equals("Amend"))
      {
        PGB.setMode("Amend");
        PGB.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      else if (PGB.getAction().equals("Add"))
      {
        PGB.setMode("Create");
        PGB.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (PGB.getAction().equals("Delete"))
      {
        PGB.setMode("Delete");
        PGB.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      %>
	<jsp:forward page="productGroupMaint.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        PGB.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (PGB.isValid(ButtonPressed))
        {
          PGB.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            PGB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to commit your updates or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            PGB.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create these entries or 'Cancel' to amend them</b></font>");
          }
        }
        %>
        <jsp:forward page="productGroupMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        RSB.getLatestProcessControl();
        %>
        <jsp:forward page="productGroupMaint.jsp"/>
        <%
      }
    }
%>
</body>
</html>
