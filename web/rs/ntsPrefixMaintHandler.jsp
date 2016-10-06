<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Enumeration"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="NTS" class="DBUtilities.NTSBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    Enumeration FormFields = request.getParameterNames();
    String FormField = null;
    session.setAttribute("formname","accountMaintHandler");
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
        <jsp:forward page="ntsPrefixMaint.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="ntsPrefixMaint"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.equals("View All Processes"))
    {
      RSB.populateProcessList();
      %>
        <jsp:forward page="ntsPrefixMaint.jsp">
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
    else if (ButtonPressed.equals("Refresh"))
    {
      String action = NTS.getAction();
      if (action.equals("Add"))
      {
        NTS.Reset();
        NTS.setAction(action);
        NTS.setMode("Create");
        NTS.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      }
      NTS.populateNTSPrefixList();
      %>
	<jsp:forward page="ntsPrefixMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Confirm"))
    {
      String action = NTS.getAction();
      if (action.equals("Add"))
      {
        NTS.createProductNTSPrefix();
        //NTS.setNTSPrefix("");
      }
      else //(action.equals("Delete"))
      {
        NTS.deleteProductNTSPrefix();
        NTS.setProductGroup("");
        NTS.setProductCode("");
        NTS.setNTSPrefix("");
      }
      NTS.setAction("Add");
      NTS.setMode("Create");
      String message = NTS.getMessage();
      NTS.populateNTSPrefixList();
      NTS.setMessage(message);
      %>
        <jsp:forward page="ntsPrefixMaint.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Cancel"))
    {
      if(NTS.getAction().equals("Delete"))
      {
        NTS.setProductGroup("");
        NTS.setProductCode("");
        NTS.setNTSPrefix("");
      }
      NTS.setAction("Add");
      NTS.setMode("Create");
      NTS.setMessage("<font color=blue><b>Mandatory fields are shown in blue</b></font>");
      NTS.populateNTSPrefixList();
      %>
	<jsp:forward page="ntsPrefixMaint.jsp"/>
      <%
    }
    else
    {
      //store form values in Bean
      while (FormFields.hasMoreElements())
      {
        FormField=(String)FormFields.nextElement();
        NTS.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
      {
        if (NTS.isValid(ButtonPressed))
        {
          NTS.setMode("Confirm");
          if (ButtonPressed.equals("Update"))
          {
            NTS.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to commit your updates or 'Cancel' to abort the operation</b></font>");
          }
          else
          {
            NTS.setMessage("<font color=blue><b>Select 'Confirm' from the " +
              "'Options' menu above to create this Product NTS Prefix combination or 'Cancel' to amend it</b></font>");
          }
        }
        %>
        <jsp:forward page="ntsPrefixMaint.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("list_Delete"))
      {
        NTS.setProductGroup(request.getParameter("listProductGroup"));
        NTS.setProductCode(request.getParameter("listProductCode"));
        NTS.setNTSPrefix(request.getParameter("listNTSPrefix"));
        NTS.setAction("Delete");
        NTS.setMode("Confirm");
        NTS.setMessage("<font color=blue><b>Select 'Confirm' from the " +
          "'Options' menu above to delete this Product NTS Prefix combination or 'Cancel' to abort the operation</b></font>");
        /*NTS.deleteProductNTSPrefix(request.getParameter("listProductCode"),
          request.getParameter("listNTSPrefix"));
        NTS.populateNTSPrefixList();*/
        %>
          <jsp:forward page="ntsPrefixMaint.jsp"/>
        <%
      }
      else
      {//No button pressed, so must be a Refresh
        RSB.getLatestProcessControl();
        %>
        <jsp:forward page="ntsPrefixMaint.jsp"/>
        <%
      }
    }
%>
</body>
</html>
