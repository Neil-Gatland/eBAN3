<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed, System, Page, Message;
    String C2_Ref, Service_Reference;
    String NewSpeed="", eDate="",eBanSummary="";
    String NewBAN="";
    String DeleteTrial="",User,GCID;
    %>
    <%
    Page="AmendServiceHandler.jsp";
    session.setAttribute("PageSent",Page);
    ButtonPressed=(String)request.getParameter("ButtonPressed");
    Service_Reference=BAN.getService_Reference();
    NewSpeed=(String)request.getParameter("LLU_Speed");
    eDate=(String)request.getParameter("eDateh");
    eBanSummary=(String)request.getParameter("BAN_Summary");
    System=(String)session.getAttribute("System");
    GCID=BAN.getGlobalCustomerId();
    DeleteTrial=ctBAN.LLUChangeAllowed
                  (Service_Reference,eDate,NewSpeed,GCID);
    User=(String)session.getAttribute("User_Id");

    if (DeleteTrial=="X")
    {
      Message="A change to the speed of the circuit has already been "+
              "made in the last month. Only one change per month is "+
              "allowed.";
    }
    else
    {
      if (DeleteTrial=="C")
      {
        Message="Customer has exceeeded allowed number of changes for this "+
                "effective month.";
      }
      else
      {
        NewBAN=ctBAN.LLUBanUpdate(DeleteTrial,eBanSummary,User,GCID,Service_Reference,eDate,NewSpeed);
        if (NewBAN=="")
        {
          Message="Update has unexpected failed: Contact Danet Support on 01273 727972";
        }
        else
        {
          Message=NewBAN;
        }
      }
    }
    session.setAttribute("Message",Message);

    %>
      <jsp:forward page="AmendService.jsp"/>
    <%
    %>
</body>
</html>