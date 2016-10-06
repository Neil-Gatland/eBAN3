<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<title>Create New Credit</title>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="acb" class="DBUtilities.AssureChargeBean" scope="session"/>

<%
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  JavaUtil.UserData UD = new UserData();
  DBAccess DBA = new DBAccess();
  String sharedPath=
        EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
  String CLI_Id, Message, Credit_Amount="0.00", Credit_Description="",CR_DR="Credit";
  CLI_Id=(String)request.getParameter("CLI_Id");
  BAN.setCLI_Id(CLI_Id);
  Message=BAN.getCLI_Details();
  String SelectMode=acb.getSelectMode();
  boolean disableDate=SelectMode.equals("DISABLED")?true:false;
  int VDay = acb.getVisitDateDays();
  int VMonth = acb.getVisitDateMonths();
  int VYear = acb.getVisitDateYears();
  String Visit_Date=acb.getVisit_Date();
  String AddCreditMessage="";
  AddCreditMessage=SU.isNull((String)session.getAttribute("AddCreditMessage"),"");
  session.setAttribute("AddCreditMessage","");

  if (AddCreditMessage!="")
  {
    Credit_Amount=BAN.getCredit_Amount();
    Credit_Description=BAN.getCredit_Description();
    CR_DR=BAN.getCR_DR();
    Visit_Date=acb.getVisit_Date();
    VDay=BAN.getVDay();
    VMonth=BAN.getVMonth();
    VYear=BAN.getVYear();
  }
  else
  {
    Credit_Amount="0.00";
    Credit_Description="";
    CR_DR="credit";
    Visit_Date=SU.isNull(acb.getVisit_Date(),"Today");
    VDay = acb.getVisitDateDays();
    VMonth = acb.getVisitDateMonths();
    VYear = acb.getVisitDateYears();
  }

%>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--
function submitbuttonClick()
  {
    if ((Add_Credit.AddCreditMessage.value=="credit")||
        (Add_Credit.AddCreditMessage.value=="debit"))
    {
      alert(Add_Credit.CR_DR.value+" already created, please close window");
    }
    else
    {
      if ((Add_Credit.Credit_Description.value.replace(" ","")=="")||
          (Add_Credit.Credit_Value.value==""))
      {
        alert('Credit description and amount must both be supplied');
      }
      else
      {
        if ((parseFloat(Add_Credit.Credit_Value.value)==0)||
            (isNaN(parseFloat(Add_Credit.Credit_Value.value))))
        {
          alert('Credit amount must be non-zero numeric value');
        }
        else
        {
          var message = '\nPress OK to confirm that you wish'+
                        '\nto create the requested '+
                        Add_Credit.CR_DR.value +
                        '.';
          if (confirm(message))
          {
            Add_Credit.submit();
          }
        }
      }
    }
  }
  //-->
  </script>
  <a name="top"></a>
  <table id=1 width ="100%">
    <tr>
      <td>
        <form name="Add_Credit" method="post" action="SubmitCredit.jsp">
        <input name="AddCreditMessage" type=hidden value=<%=AddCreditMessage%>>
        <input name="ButtonPressed" type=hidden value="">
        <font color=#FF0000><%=Message%></font>
      </td>
    </tr>
    <tr>
      <td>
	<table border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
	    <td colspan=8 valign="top"><br>
	      <h3 align="center"><b>Create Home Workers Voice Credit</b></h3>
            </td>
          </tr>
          <tr>
            <td align=left>
              <font color="#0000FF">
              Customer Name:
              </font>
            </td>
            <td align=left>
              <font color="#000000"><b>
              <%=BAN.getCLI_Details_Customer_Name()%>
              </b></font>
            </td>
          </tr>
          <tr>
            <td>
              &nbsp;
            </td>
          </tr>
          <tr>
            <td align=left>
              <font color="#0000ff">
              Staff No:
              </font>
            </td>
            <td align=left>
              <%=BAN.getCLI_Details_Staff_Number()%>&nbsp;
            </td>
            <td align=left>
              <font color="#0000FF">
              User Name:
              </font>
            </td>
            <td align=left>
              <%=BAN.getCLI_Details_User_Name()%>&nbsp;
            </td>
            <td align=left>
              <font color="#0000FF">
              User CLI:
              </font>
            </td>
            <td align=left>
              <%=BAN.getCLI_Details_CLI()%>&nbsp;
            </td>
          </tr>
          <tr>
            <td align=left>
              &nbsp;&nbsp;&nbsp;
            </td>
          </tr>
          <tr>
            <td align=left>
              <font color="#0000FF">
              Charge From:
              </font>
            </td>
            <td align=left>
              <%=BAN.getCLI_Details_Charge_From()%>
            </td>
            <td align=left>
              <font color="#0000FF">
              Charge To:
              </font>
            </td>
            <td align=left>
              <%=BAN.getCLI_Details_Charge_To()%>
            </td>
            <td align=left>
              <font color="#0000FF">
              Total Charge:&nbsp;
              </font>
            </td>
            <td align=left>
              <%=BAN.getCLI_Details_Call_Charge()%>
            </td>
          </tr>
          <tr>
            <td>
              &nbsp;
            </td>
          </tr>
          <tr>
            <td align=left>
              <font color="#0000FF">
              Calls Charge:&nbsp;
              </font>
            </td>
            <td align=left>
              <%=BAN.getCLI_Details_Calls_Charge()%>&nbsp;
            </td>
            <td align=left>
              <font color="#0000FF">
              Management Charge:&nbsp;
              </font>
            </td>
            <td align=left>
              <%=BAN.getCLI_Details_Management_Charge()%>&nbsp;
            </td>
            <td align=left>
              <font color="#0000FF">
              Amex Markup:&nbsp;
              </font>
            </td>
            <td align=left>
              <%=BAN.getCLI_Details_Markup_Charge()%>&nbsp;
            </td>
          </tr>
          <tr>
            <td>
              &nbsp;
            </td>
          </tr>
          <tr>
            <td colspan=8>
              <font color="#0000FF"><hr></font>
            <td>
          </tr>
          <tr>
            <td>
              &nbsp;
            </td>
          </tr>
          <tr>
            <td>
              <font color="#0000FF">
                Credit Description:&nbsp;
              </font>
            </td>
            <td colspan=6>
              <input class=inp type=text name="Credit_Description" size="110"
                value="<%=Credit_Description%>">
            </td>
          </tr>
          <tr>
            <td>
              &nbsp;
            <td>
          </tr>
          <tr>
            <td>
              <font color="#0000FF">
                Credit Amount:
              </font>
            </td>
            <td colspan=2>
              <input class=inp type=text name="Credit_Value" size="20"
                value="<%=Credit_Amount%>">
            </td>
          </tr>
          <tr>
            <td>
              <font color="#0000FF">
              Credit/Debit:
            </td>
            <td colspan=6>
              <select style="width:50px" id="CR_DR" name="CR_DR">
              <%if (CR_DR.equals("credit")){%>
                <option selected value="credit">CR</option>
                <option value="debit">DR</option>
              <%}else{%>
                <option value="credit">CR</option>
                <option selected value="debit">DR</option>
              <%}%>
              </select>
            </td>
          </tr>
          <tr>
            <td>
              &nbsp;
            <td>
          </tr>
          <tr>
            <td>
              <font color="#0000FF">
                Date of Credit:
              </font>
            </td>
            <td class=<%=acb.getClass("VisitDateh")%>>
              <%=HB.getDays("Add_Credit", "VisitDate", VDay, false, disableDate)%>
              <%=HB.getMonths("Add_Credit", "VisitDate", VMonth, false, disableDate)%>
              <%=HB.getYears("Add_Credit", "VisitDate", VYear, false, disableDate)%>
              <input type="hidden" name="VisitDateh" value="<%=Visit_Date%>">
            </td>
          </tr>
          <tr>
            <td colspan=6>
              &nbsp;
            </td>
          </tr>
          <tr>
            <td colspan=6>
              <font color="#FF0000">
              <%=AddCreditMessage%>
              </font>
            </td>
          </tr>
          <tr>
            <td colspan=8 align="center">
	      <%=HB.getImageAsAnchor("submitbutton")%>
            </td>
          </tr>
        </table><!--2-->
      </td>
      <td>
        </form>
      <td>
    </tr>
  </table><!--1-->
</body>
</html>