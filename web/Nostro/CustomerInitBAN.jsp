<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="noscBAN" class="DBUtilities.NostroCustomerBANBean" scope="session"/>

<%! String SQL;
    String ActionCustomer="";
    String Message;
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    DBAccess DBA = new DBAccess();
    String PageSent,SelectMode,InputMode,BAN_Identifier;
    String Product,Global_Customer_Id,Global_Customer_Name;
    String BAN_Summary,BAN_Reason,Product_Type="";
    String Required_BAN_Effective_Date;
    int BEDay, BEMonth, BEYear;
    String NostroRefs;

%>
<%//Set Initial values
    String Mode=noscBAN.getMode().equals("Add")?"Initiate":noscBAN.getMode();
    String Action=noscBAN.getAction();
    SelectMode=noscBAN.getSelectMode();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=noscBAN.getInputMode();

  //Are we coming from the handler page, or a fresh start?

  //Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
    Message=SU.isNull((String)session.getAttribute("Message"),"");
    PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
    Global_Customer_Id=noscBAN.getGlobalCustomerId();
    Global_Customer_Name=noscBAN.getGlobalCustomerName();
    BAN_Summary = noscBAN.getBAN_Summary();
    BAN_Reason = noscBAN.getBAN_Reason();
    BEDay = noscBAN.getBANEffectiveDateDays();
    BEMonth = noscBAN.getBANEffectiveDateMonths();
    BEYear = noscBAN.getBANEffectiveDateYears();
    Required_BAN_Effective_Date=SU.isNull(noscBAN.getRequired_BAN_Effective_Date(),"Today");
    NostroRefs=noscBAN.getNostroRefs();
%>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--
  function addarrowClick()
  {
  <%if (disableDate)
    {%>
    alert("Add not allowed");
  <%}
    else
    {%>
    if (CustomerInitBAN.New_Nostro_Reference.value == "")
    {
      alert("Please enter a new Nostro Reference to add");
    }
    else
    {
      CustomerInitBAN.Nostro_Reference[CustomerInitBAN.Nostro_Reference.length] =
        new Option(CustomerInitBAN.New_Nostro_Reference.value,
          CustomerInitBAN.New_Nostro_Reference.value, false, false);
      CustomerInitBAN.New_Nostro_Reference.value = "";
      storeNostroRefs();
    }
  <%}%>
  }

  function removearrowClick()
  {
  <%if (disableDate)
    {%>
    alert("Remove not allowed");
  <%}
    else
    {%>
    if (CustomerInitBAN.Nostro_Reference.selectedIndex == -1)
    {
      alert("No Nostro Reference selected for removal");
    }
    else
    {
      CustomerInitBAN.Nostro_Reference.remove(CustomerInitBAN.Nostro_Reference.selectedIndex);
      storeNostroRefs();
    }
  <%}%>
  }

  function storeNostroRefs()
  {
    var nr = "";
    for (i=0; i < CustomerInitBAN.Nostro_Reference.length; i++)
    {
      nr += CustomerInitBAN.Nostro_Reference[i].value + ",";
    }
    CustomerInitBAN.NostroRefs.value = nr;
  }

  //-->
  </script>
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
      <td colspan=2>
	<form name="CustomerInitBAN" method="post" action="CustomerInitBANHandler.jsp">
        <%=HB.getBANMenu_Bar("BAN",Mode,Action,(String)session.getAttribute("System"))%>

        <input name="ButtonPressed" type=hidden value="">
        <input name="RejectReason" type=hidden value="">
        <input name="NostroRefs" type=hidden value="<%=NostroRefs%>">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
	    <!-- this is a spacer column-->
	    <td width="13">&nbsp;</td>
	    <td valign="top" align="left" NOWRAP width=10><!--Formerly Menu Column--></td>
	    <!-- this is a spacer column-->
	    <td width="13">&nbsp;</td>
	    <td  valign ="Top"><!-- Main table starts here-->
	      <table width="100%" border="0" cellpadding="0" cellspacing="0" id=3>
		<tr>
		  <td valign="top" colspan=2>
		    <h3 align="center"><%=Mode%> a Customer</h3>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(noscBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" height="341" colspan=2>
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td colspan=4>
			  <table width=100%>
                            <!--
			    <tr>
			      <td><b>Product:</b></td>
			      <td class=bluebold><%=noscBAN.getProductName()%></td>
			      <td cosplan=2>&nbsp;</td>
			    </tr>
                            -->
			    <tr>
			      <td><b>Customer:</b></td>
			      <td class=bluebold><%=Global_Customer_Name%></td>
			      <td><b>Billing Customer Reference:</b></td>
			      <td class=bluebold><%=Global_Customer_Id%></td>
			    </tr>
			    <tr>
			      <td><b>Billing Advice Status:</td>
			      <td class=bluebold><%=noscBAN.getBanStatus()%></td>
			      <td><b>Requested By:</td>
			      <td class=bluebold>
				<%=SU.isNull(noscBAN.getBanCreatedBy(),(String)session.getAttribute("User_Name"))%><br>
			      </td>
			    </tr>
			  </table>
			</td>
		      </tr>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
			<td class=<%=noscBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
		          <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=noscBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
		        </td>
                        <!--
		        <td class=<%=noscBAN.getClass("BAN_Reason")%>>
			  <b><small>BAN Reason
		          <br><textarea class=tworows name="BAN_Reason" cols="40" rows="2" <%=noscBAN.getMode("BAN_Reason")%>><%=BAN_Reason%></textarea>
		        </td>
                        -->
		        <td class=<%=noscBAN.getClass("BANEffectiveDateh")%>>BAN Effective Date<br>
                          <%=HB.getDays("CustomerInitBAN", "BANEffectiveDate", BEDay, false, disableDate)%>
                          <%=HB.getMonths("CustomerInitBAN", "BANEffectiveDate", BEMonth, false, disableDate)%>
                          <%=HB.getYears("CustomerInitBAN", "BANEffectiveDate", BEYear, false, disableDate)%>
                          <input type="hidden" name="BANEffectiveDateh" value="<%=Required_BAN_Effective_Date%>">
		        </td>
			<td>
			  <span style="VISIBILITY: <%=noscBAN.getRejectVisibility()%>">
			    <b><small><%=noscBAN.getBanStatus()%> Reason
		            <br><textarea class=red cols="40" rows="2" READONLY><%=noscBAN.getRejectReason()%></textarea>
			  </span>
		        </td>
                        <td>&nbsp;</td>
		      </tr>
		<tr>
                  <td colspan=4>&nbsp;
                  </td>
                </tr>
                  <td class=<%=noscBAN.getClass("NostroRefs")%> valign="top">
		    <b><small>New Sales Order Number<br>
		    <input class=inp type=text name="New_Nostro_Reference" size="35" <%=InputMode%>>
                  </td>
		  <td align="center">
                    <%=HB.getImageAsAnchor("addarrow")%><br>&nbsp;<br>
                    <%=HB.getImageAsAnchor("removearrow")%>
		  </td>
                  <td class=<%=noscBAN.getClass("NostroRefs")%>>
		    <b><small>Existing Sales Order Numbers<br>
		    <%=DBA.getListBox("Nostro_Reference",InputMode,"",
                      Global_Customer_Id,3,"style=\"height:80\"",false)%>
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


