<html>
<head>
</head>
<%@ page import="DBUtilities.*,JavaUtil.StringUtil"%>

<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%! JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    String ButtonPressed,System;
%>
<%
    session.setAttribute("formname","billSubmissionHandler");
    ButtonPressed=request.getParameter("ButtonPressed");
    System=(String)session.getAttribute("System");


    if (ButtonPressed.startsWith("Desktop"))
    {
      %>
	<jsp:forward page="newDesktop.jsp"/>
      <%
    }
    else if (ButtonPressed.equals("Log Off"))
    {
      %>
	<jsp:forward page="Login.jsp?type=l"/>
      <%
    }
    else if (ButtonPressed.equals("Raise Query"))
    {
      %>
        <jsp:forward page="billSubmission.jsp">
         <jsp:param name="query" value="true"/>
          <jsp:param name="fromPage" value="billSubmission"/>
        </jsp:forward>
      <%
    }
    else
    {
      BAN.setRunTime(request.getParameter("runTime"));
      BAN.setRunType(request.getParameter("runType"));
      BAN.setBackdates(request.getParameter("backdates"));
      BAN.setScheduleCrystal(request.getParameter("scheduleCrystal")!=null);
      if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("subBill")))
      {
        String confirmMsg = null;
        if (ButtonPressed.equals("Submit"))
        {
          if (BAN.reportsRunning())
          {
            %>
              <jsp:forward page="billSubmission.jsp"/>
            <%
          }
          else if ((BAN.getRunType().equals("close")) && (!BAN.checkCanClose()))
          {
            BAN.setMessage("<font color=red>Errors must be resolved before " +
              "this bill can be closed. Click on the 'V' button for details.</font>");
            %>
              <jsp:forward page="newDesktop.jsp"/>
            <%
          }
          else
          {
            confirmMsg = BAN.determineSubmissionMessage();
          }
        }
        if (confirmMsg != null)
        {
        %>
          <jsp:forward page="billSubmission.jsp">
           <jsp:param name="submitConfirm" value="true"/>
            <jsp:param name="confirmMsg" value="<%=confirmMsg%>"/>
          </jsp:forward>
        <%
        }
        else
        {
          int seqId = BAN.submitBillJob();
          //if (seqId == 0)
          if (BAN.getRunTime().equals("overnight"))
          {
            if (seqId > 0)
            {
              BAN.setMessage("<font color=blue>Customer " + BAN.getGlobalCustomerId() +
              " submitted for overnight processing</font>");
            }
          %>
            <jsp:forward page="billSubmission.jsp"/>
          <%
          }
          else if (seqId == -1)
          {
          %>
            <jsp:forward page="billSubmission.jsp"/>
          <%
          }
          else
          {
            if (!BAN.insertMBLog(seqId))
            {
            %>
              <jsp:forward page="billSubmission.jsp"/>
            <%
            }
            else
            {
              boolean running = false;
              for (int i = 0; i < 6; i++)
              {
                if (BAN.billJobRunning())
                {
                  running = true;
                  break;
                }
                Thread.sleep(5000);
              }
              if (running)
              {
                String grid = BAN.getLogMessages(false);
              %>
                <jsp:forward page="newDesktop.jsp">
                 <jsp:param name="mblog" value="true"/>
                </jsp:forward>
              <%
              }
              else
              {
                int numJobs = BAN.countBillJobsRunning();
                if (numJobs == 0)
                {
                  BAN.setMessage("<font color=red>Your job does not appear " +
                    "to have started. Please check the Queue and/or the " +
                    "Billing Log.</font>");
                }
                else if (numJobs > 0)
                {
                  BAN.setMessage("<font color=blue>Warning:-There are currently " +
                  numJobs + " job(s) running or awaiting processing. " +
                  "Your job will not start immediately, but will be placed " +
                  "in a queue.</font>");
                }
                %>
                  <jsp:forward page="newDesktop.jsp"/>
                <%
              }
            }
          }
        }
      }
      else
      {
      %>
	<jsp:forward page="billSubmission.jsp"/>
      <%
      }
    }
    %>
</body>
</html>
