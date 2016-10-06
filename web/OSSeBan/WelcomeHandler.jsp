<html>
<head>
</head>
<body>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="DBUtilities.OSSChargeBANBean"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="cuBAN" class="DBUtilities.OSSCustomerBANBean" scope="session"/>
<!--jsp:useBean id="chBAN" class="DBUtilities.OSSChargeBANBean" scope="session"/-->
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>
<jsp:useBean id="caBAN" class="DBUtilities.CarrierBANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%  String ButtonPressed, banId, listType;
    String userId = (String)session.getAttribute("User_Id");
    OSSChargeBANBean chBAN = (OSSChargeBANBean)session.getAttribute("chBAN" + userId);
    if (chBAN == null)
    {
      chBAN = new OSSChargeBANBean();
      session.setAttribute("chBAN" + userId, chBAN);
    }
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    if (ButtonPressed.startsWith("Connect"))
    {
      %>
	<jsp:forward page="DesktopLogon.jsp"/>
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
          <jsp:forward page="OSSBANMenu.jsp"/>
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
        if (count == 4)
        {
          BAN.setUserId((String)session.getAttribute("User_Id"));
          //Find out what type of BAN it is
          if (BAN.findBanType())
          {//Redirect the request according to the Ban Type
            //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
            String groupName = (String)session.getAttribute("Group_Name");
            if ((!BAN.getBanType().startsWith("Customer")) &&
              (groupName.equals("MANS")))
            {
              session.setAttribute("Message","<font color=red>You are not authorized to view this BAN</font>");
              %>
              <jsp:forward page="Welcome.jsp"/>
              <%
            }
            else if (BAN.getBanType().startsWith("OSS Charge"))
            {
              chBAN.Reset();
              chBAN.setBanIdentifier(banId);
              //ctBAN.setCircuitRefFromBAN(request.getParameter("BAN_Id"));
              ctBAN.getCircuit(banId);
              chBAN.getChargeBan();
              if (DBA.validAction(listType, chBAN.getBanStatus()))
              {
                chBAN.setCircuit_Reference(ctBAN.getCircuit_Reference());
                chBAN.setAction(listType);
                chBAN.setdefaultFieldModes();
                chBAN.setIsDirect(true);
                %>
                <jsp:forward page="ChargeBAN.jsp"/>
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
            else if (BAN.getBanType().startsWith("Circuit"))
            {
              ctBAN.setBanIdentifier(banId);
              ctBAN.getCircuitBAN();
              if (DBA.validAction(listType, ctBAN.getBanStatus()))
              {
                ctBAN.setAction(listType);
                ctBAN.setdefaultFieldModes();
                ctBAN.setIsDirect(true);
                %>
                  <jsp:forward page="CircuitBAN.jsp"/>
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
            else if (BAN.getBanType().startsWith("Customer"))
            {
              cuBAN.setBanIdentifier(banId);
              cuBAN.setGroupName(groupName);
              cuBAN.getCustomerBAN();
              if (((groupName.equals("MANS")) && (!cuBAN.getAccount_Id().startsWith("5"))) ||
                ((!groupName.equals("MANS")) && (cuBAN.getAccount_Id().startsWith("5"))))
              {
                session.setAttribute("Message","<font color=red>You are not authorized to view this BAN</font>");
                %>
                <jsp:forward page="Welcome.jsp"/>
                <%
              }
              else
              {
                if (DBA.validAction(listType, cuBAN.getBanStatus()))
                {
                  cuBAN.setAction(listType);
                  cuBAN.setdefaultFieldModes();
                  cuBAN.setIsDirect(true);
                  %>
                    <jsp:forward page="CustomerBAN.jsp"/>
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
            }
            else if (BAN.getBanType().startsWith("Carrier"))
            {
              caBAN.setBanIdentifier(banId);
              caBAN.getCarrierBAN();
              if (DBA.validAction(listType, caBAN.getBanStatus()))
              {
                caBAN.setAction(listType);
                caBAN.setdefaultFieldModes();
                caBAN.setIsDirect(true);
                %>
                  <jsp:forward page="CarrierBAN.jsp"/>
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
              session.setAttribute("Message","Unknown BAN type (" + BAN.getBanType() +
                ") for BAN Id :- " +
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