<html>
<head>
</head>
<%@ page import="JavaUtil.*,java.util.Enumeration"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="siteBAN" class="DBUtilities.SiteBANBean" scope="session"/>
    <%!
        String Error_Message,Message,ButtonPressed,Mode,Action;
	Enumeration FormFields;
	String FormField,PageSent="";
	boolean Errors;
	JavaUtil.UserData UD = new UserData();
	StringUtil SU = new StringUtil();
    %>
    <%
      Mode=siteBAN.getMode();
      Action=siteBAN.getAction();
      //PageSent=(String)session.getAttribute("PageSent");
      //session.setAttribute("PageSent",request.getRequestURI());
      ButtonPressed=(String)request.getParameter("ButtonPressed");
      if (ButtonPressed.startsWith("Clear"))
      {
        FormFields=request.getParameterNames();
	while (FormFields.hasMoreElements())
	{
	  FormField=(String)FormFields.nextElement();
	  siteBAN.setParameter(FormField,"");
	}
        %>
          <jsp:forward page="SiteBAN.jsp"/>
        <%
      }
      else if (ButtonPressed.equals("Raise Query"))
      {
        %>
          <jsp:forward page="SiteBAN.jsp">
           <jsp:param name="mode" value="<%=Mode%>"/>
           <jsp:param name="query" value="true"/>
            <jsp:param name="fromPage" value="SiteBAN"/>
          </jsp:forward>
        <%
      }
      else if (ButtonPressed.startsWith("Create BAN Menu"))
      {
        %>
          <jsp:forward page="OSSBANMenu.jsp"/>
        <%
      }
      else if (ButtonPressed.startsWith("Back To"))
      {
        %>
          <jsp:forward page="ListBans.jsp"/>
        <%
      }
      //store form values in Bean
      FormFields=request.getParameterNames();
      while (FormFields.hasMoreElements())
      {
	FormField=(String)FormFields.nextElement();
	siteBAN.setParameter(FormField,request.getParameter(FormField));
      }
      if ((ButtonPressed.startsWith("Save Draft"))   ||
	  (ButtonPressed.startsWith("Submit"))
  	)
      {
        //Validation
        if (!siteBAN.isValid(ButtonPressed))
        {
            %>
            <jsp:forward page="SiteBAN.jsp"/>
            <%
        }
        else
        {//Set non-form BAN values
	  siteBAN.setUserId((String)session.getAttribute("User_Id"));
	  //Create/Amend a BAN
          boolean success = false;
          success = siteBAN.authoriseSiteBAN();

          if (success)
          {//Success - get the BAN ID
            BAN.setBanIdentifier(siteBAN.getBanIdentifier());
            session.setAttribute("Message",siteBAN.getMessage());
            %>
              <jsp:forward page="OSSBANMenu.jsp"/>
            <%
          }
          else
          {//Failed
            %>
            <jsp:forward page="SiteBAN.jsp"/>
            <%
	  }//end of if create
	}//end of If valid
      }//end of save button pressed
      else
      {//no button pressed, so must have been an autorefresh from a dropdown
        %>
          <jsp:forward page="SiteBAN.jsp"/>
        <%
      }
      %>
</body>
</html>