<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="gcBAN" class="DBUtilities.GCBCustomerBANBean" scope="session"/>
<jsp:useBean id="irBAN" class="DBUtilities.InvoiceRegionBANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%! String ButtonPressed, banId, listType;%>
<%
    BAN.setMessage("");
    ButtonPressed=(String)request.getParameter("ButtonPressed");

    if (ButtonPressed.startsWith("Connect"))
    {
      %>
	<jsp:forward page="DesktopLogon.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Clear"))
    {
      BAN.setBanIdentifier("");
      %>
        <jsp:forward page="Welcome.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="Welcome.jsp">
          <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="Welcome"/>
        </jsp:forward>
      <%
    }
    else if (ButtonPressed.startsWith("here"))
    {
      listType = (String)request.getParameter("listType");
      banId = ((String)request.getParameter("banId")).trim();
      boolean allBlank = true;
      if (banId.length() > 0)
      {
        char[] chArray = banId.toCharArray();
        for (int i = 0; i < banId.length(); i++)
        {
          if (chArray[i] != ' ')
          {
            allBlank = false;
            break;
          }
        }
      }
      if (listType.equals("Create"))
      {
        if ((banId.equals("")) || (allBlank))
        {
          %>
          <jsp:forward page="CustomerBANMenu.jsp"/>
          <%
        }
        else
        {
          BAN.setBanIdentifier(banId);
          //BAN.setAction(listType);
          BAN.setIsDirect(true);
          session.setAttribute("Message","Create option not applicable if BAN Id already selected");
          %>
          <jsp:forward page="Welcome.jsp"/>
          <%
        }
      }
      if ((banId.equals("")) || (allBlank))
      {
        response.sendRedirect("ListBans.jsp?"+listType);
        //response.flushBuffer();
      }
      else
      {
        int start = banId.indexOf("-");
        int count = 0;
        while (start != -1)
        {
          count++;
          start = banId.indexOf("-", start+1);
        }
        BAN.setBanIdentifier(banId);
        BAN.setAction(listType);
        BAN.setIsDirect(true);
        if ((count == 4) || (count == 5))
        {
          BAN.setUserId((String)session.getAttribute("User_Id"));
          //Find out what type of BAN it is
          if (BAN.findBanType())
          {//Redirect the request according to the Ban Type
            //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
            String groupName = (String)session.getAttribute("Group_Name");
            if (BAN.getBanType().startsWith("Customer"))
            {
              gcBAN.Reset();
              gcBAN.setBanIdentifier(banId);
              gcBAN.setGroupName(groupName);
              gcBAN.getCustomerBAN();
              if (DBA.validAction(listType, gcBAN.getBanStatus()))
              {
                gcBAN.setAction(listType);
                gcBAN.setdefaultFieldModes();
                gcBAN.setMessage("");
                gcBAN.setIsDirect(true);
                %>
                  <jsp:forward page="GCBCustomerBAN.jsp"/>
                <%
              }
              else
              {
                session.setAttribute("Message","Invalid action for BAN Id :- " +
                  banId);
                %>
                <jsp:forward page="Welcome.jsp"/>
                <%
              }
            }
            else if (BAN.getBanType().startsWith("Invoice"))
            {
              irBAN.Reset();
              irBAN.setBanIdentifier(banId);
              irBAN.setGroupName(groupName);
              irBAN.getInvoiceRegionBAN();
              gcBAN.Reset();
              gcBAN.setGlobalCustomerId(irBAN.getGlobalCustomerId());
              gcBAN.getCustomer();
              if (DBA.validAction(listType, irBAN.getBanStatus()))
              {
                irBAN.setAction(listType);
                irBAN.setdefaultFieldModes();
                irBAN.setMessage("");
                irBAN.setIsDirect(true);
                %>
                  <jsp:forward page="InvoiceRegionBAN.jsp"/>
                <%
              }
              else
              {
                session.setAttribute("Message","Invalid action for BAN Id :- " +
                  banId);
                %>
                <jsp:forward page="Welcome.jsp"/>
                <%
              }
            }
            else
            {//Unknown BAN Type
              session.setAttribute("Message","Unknown BAN type for BAN Id :- " +
                banId);
              %>
              <jsp:forward page="Welcome.jsp"/>
              <%
            }
          }
          else
          {//Couldn't find BAN
              session.setAttribute("Message","No BAN found for BAN Id :- " +
                banId);
            %>
             <jsp:forward page="Welcome.jsp"/>
            <%
          }
        }
        else
        {//invalid BAN id
            session.setAttribute("Message","Invalid BAN Id");
          %>
           <jsp:forward page="Welcome.jsp"/>
          <%
        }
      }
    }
%>
</body>
</html>