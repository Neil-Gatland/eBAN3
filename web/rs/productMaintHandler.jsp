<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="PRD" class="DBUtilities.ProductBean" scope="session"/>
<jsp:useBean id="PGB" class="DBUtilities.ProductGroupBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","productMaintHandler");
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
        <jsp:forward page="productMaint.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="productMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("Missing Data Identification"))
    {
      %>
        <jsp:forward page="missingDataIdentification.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="productMaint.jsp">
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
    else if (ButtonPressed.equals("Product Group Maintenance"))
    {
      PGB.Reset();
      PGB.setUserId((String)session.getAttribute("User_Id"));
      PGB.setProductGroup(PRD.getProductGroup());
      PGB.getProductGroupFromDB();
      PGB.populateProductList();
      PGB.setAction("Amend");
      PGB.setMode("Amend");
      PGB.setMessage("<font color=blue><b>To create a new Product for " +
        "this Product Group, select 'Add New Product' " +
        "from the 'Options' menu above.</b></font>");
      %>
        <jsp:forward page="productGroupMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Refresh"))
    {
      String action = PRD.getAction();
      PRD.Reset();
      PRD.setAction(action);
      if (action.equals("Add"))
      {
        PRD.setMode("Create");
        PRD.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (action.equals("Delete"))
      {
        PRD.getProductFromDB();
        PRD.setMode("Delete");
        PRD.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      else if (action.equals("Amend"))
      {
        PRD.getProductFromDB();
        PRD.setMode("Amend");
        PRD.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      %>
	<jsp:forward page="productMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Delete"))
    {
      PRD.setMode("Confirm");
      PRD.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to delete this Product or 'Cancel' to abort the operation</b></font>");
      %>
	<jsp:forward page="productMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = PRD.getAction();
      if (action.equals("Add"))
      {
        if (PRD.createProduct())
        {
          RSB.setProductId(PRD.getProductCode());
          String returnTo = PRD.getReturnTo();
          if (returnTo.equals("productGroupMaint.jsp"))
          {
            PGB.populateProductList();
            PGB.setAction("Amend");
            PGB.setMode("Amend");
            PGB.setMessage(PRD.getMessage());
          }
          else
          {
            RSB.setMessage(PRD.getMessage());
          }
          %>
            <jsp:forward page="<%=returnTo%>"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="productMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Delete"))
      {
        if (PRD.deleteProduct())
        {
          String returnTo = PRD.getReturnTo();
          if (returnTo.equals("productGroupMaint.jsp"))
          {
            PGB.populateProductList();
            PGB.setAction("Amend");
            PGB.setMode("Amend");
            PGB.setMessage(PRD.getMessage());
          }
          else
          {
            RSB.setMessage(PRD.getMessage());
          }
          %>
            <jsp:forward page="<%=returnTo%>"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="productMaint.jsp"/>
          <%
        }
      }
      else if (action.equals("Amend"))
      {
        if (PRD.updateProduct())
        {
          RSB.setMessage(PRD.getMessage());
          %>
            <jsp:forward page="referenceDataMenu.jsp"/>
          <%
        }
        else
        {
          %>
            <jsp:forward page="productMaint.jsp"/>
          <%
        }
      }
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      if (PRD.getAction().equals("Amend"))
      {
        PRD.setMode("Amend");
        PRD.setMessage("<font color=blue><b>Amend as required and then select " +
          "'Update' from the 'Options' menu above. Mandatory fields are shown in blue.</b></font>");
      }
      else if (PRD.getAction().equals("Add"))
      {
        PRD.setMode("Create");
        PRD.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      else if (PRD.getAction().equals("Delete"))
      {
        PRD.setMode("Delete");
        PRD.setMessage("<font color=blue><b>Select 'Delete' from the 'Options' menu above</b></font>");
      }
      %>
	<jsp:forward page="productMaint.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        PRD.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (PRD.isValid(ButtonPressed))
        {
          PRD.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            PRD.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to commit your updates or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            PRD.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create these entries or 'Cancel' to amend them</b></font>");
          }
        }
        %>
        <jsp:forward page="productMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        RSB.getLatestProcessControl();
        %>
        <jsp:forward page="productMaint.jsp"/>
        <%
      }
    }
%>
</body>
</html>
