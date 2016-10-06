<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%! String SQL;
    String ActionCustomer="";
    String Global_Customer_Id="";
    String Global_Customer_Name="";
    String Message;
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    DBAccess DBA = new DBAccess();
    String PageSent;
%>
<%//Set Initial values
  //Are we coming from the handler page, or a fresh start?

  Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  if (PageSent.endsWith("BANMenuHandler.jsp")
  || (Message.endsWith("charges for this circuit<br>"))
  || (PageSent.endsWith("BANMenuHandler.jsp") && BAN.getAction().compareTo("Authorise")==0))
  {
    Global_Customer_Id=BAN.getGlobalCustomerId();
  }
  else
  {
    //Store old values
    session.setAttribute("Global_Customer",BAN.getGlobalCustomerId());

    Global_Customer_Id="";
  }
  if (Global_Customer_Id.compareTo("")==0)
    {ActionCustomer="alert('You must select a Customer for this option')";}
  else
    {ActionCustomer="submitForm(this)";}
%>
  <a name="top"></a>
  <table id=1 width ="100%">
    <tr>
      <td colspan=3>
        <%@ include file="../includes/Page_Header4.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
      <form name="accountSetup" method="post" action="accountSetupHandler.jsp?page=1">
      <%=HB.getBANMenu_Bar("Test1","","",(String)session.getAttribute("System"))%>
      <input name="ButtonPressed" type=hidden value="">
      </td>
    </tr>
    <tr>
      <td colspan=3>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
	    <td width="13">&nbsp;
	    <!-- this is a spacer column-->
	    </td>
	    <td width="100" valign="top" align="left">
	    <!--former menu column-->
	    </td>
	    <td width="12"><!-- a spacer column-->
		&nbsp;
	    </td>
	    <td valign="top">
	      <h3 align="center"><b>Account Set-up (1)</b></h3>
	       <%=Message%><br>
	       <%session.setAttribute("Message","");%></font>
	      <table width="100%" border="0">
	        <tr>
		  <td align=left colspan=4><font color=blue><b>Mandatory fields are shown in blue</b></font>
		  </td>
		</tr>
		<tr>
		  <td align=left><font color="#0000FF">Customer Name</font>
		  </td>
		  <td align=left colspan=3><font color="#0000FF">Billing Customer Reference</font>
		  </td>
		</tr>
		<tr>
		  <td align=left>
		    <%=DBA.getListBox("Global_Customer","submit",Global_Customer_Id,"")%>
		  </td>
		  <td align=left colspan=3>
		    <input class=inp type=text name="BC_Reference" size="35">
		  </td>
		</tr>
		<tr>
		  <td align=left colspan=3><font color="#0000FF">Accounts</font>
		  </td>
		  <td align=left><font color="#0000FF">Change BANs in progress</font>
		  </td>
		</tr>
		<tr>
		  <td align=left colspan=3>
    	            <textarea class=tworows name="Old_Nostro_Reference" cols="80" rows="2" ></textarea>
		  </td>
		  <td align=left>
    	            <textarea class=tworows name="Old_Nostro_Reference" cols="40" rows="2" ></textarea>
		  </td>
		</tr>
		<tr>
		  <td align=left><font color="#0000FF">Billing Customer Name</font>
		  </td>
		  <td align=left><font color="#0000FF">Billing Customer Address Line 1</font>
		  </td>
		  <td align=left>Billing Customer Address Line 5
		  </td>
		  <td align=left><font color="#0000FF">Month of change</font>
		  </td>
		</tr>
		<tr>
		  <td align=left>
		    <input class=inp type=text name="BC_Reference" size="35">
		  </td>
		  <td align=left>
		    <input class=inp type=text name="BC_Reference" size="35">
		  </td>
		  <td align=left>
		    <input class=inp type=text name="BC_Reference" size="35">
		  </td>
		  <td align=left>
                    <select>
                      <option>Jan 2004</option>
                      <option>Dec 2003</option>
                    </select>
		  </td>
		</tr>
		<tr>
		  <td align=left><font color="#0000FF">Billing Customer Contact Name</font>
		  </td>
		  <td align=left><font color="#0000FF">Billing Customer Address Line 2</font>
		  </td>
		  <td align=left>ZIP/Post Code
		  </td>
		  <td align=left><font color="#0000FF">Account Invoice Currency</font>
		  </td>
		</tr>
		<tr>
		  <td align=left>
		    <input class=inp type=text name="BC_Reference" size="35">
		  </td>
		  <td align=left>
		    <input class=inp type=text name="BC_Reference" size="35">
		  </td>
		  <td align=left>
		    <input class=inp type=text name="BC_Reference" size="35">
		  </td>
		  <td align=left>
                    <select>
                      <option>GBP</option>
                      <option>USD</option>
                    </select>
		  </td>
		</tr>
		<tr>
		  <td align=left><font color="#0000FF">Primary Contract Reference</font>
		  </td>
		  <td align=left>Billing Customer Address Line 3
		  </td>
		  <td align=left><font color="#0000FF">Country</font>
		  </td>
		</tr>
		<tr>
		  <td align=left>
		    <input class=inp type=text name="BC_Reference" size="35">
		  </td>
		  <td align=left>
		    <input class=inp type=text name="BC_Reference" size="35">
		  </td>
		  <td align=left>
                    <select>
                      <option>UK</option>
                      <option>USA</option>
                    </select>
		  </td>
		</tr>
		<tr>
		  <td align=left><font color="#0000FF">C&W Contracting Entity</font>
		  </td>
		  <td align=left colspan=3>Billing Customer Address Line 4
		  </td>
		</tr>
		<tr>
		  <td align=left>
                    <select>
                      <option>C&W UK</option>
                      <option>CWUSA</option>
                    </select>
		  </td>
		  <td align=left colspan=3>
		    <input class=inp type=text name="BC_Reference" size="35">
		  </td>
		</tr>
	      </table>
		</form>
	      </td>
	    </tr>
	  </table><!--2-->
	</td>
      </tr>
     <tr>
      <td height="100">&nbsp
      </td>
  </table><!--1-->
</body>
</html>


