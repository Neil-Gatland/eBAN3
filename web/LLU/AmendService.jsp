<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="ctBAN" class="DBUtilities.CircuitBANBean" scope="session"/>
<jsp:useBean id="acb" class="DBUtilities.AssureChargeBean" scope="session"/>

<%
  String SQL;
  String Customer_Id="", Global_Customer_Name="";
  String C2_Ref="", Service_Reference="";
  String Message,ActionService;
  String BAN_Summary="", Customer_Reference="", Circuit_Description="";
  String Circuit_Speed="", Product_Type="", Effective_Date="";
  String From_Site="", To_Site="";
  String eDate=acb.getVisit_Date();
  int eDay=acb.getVisitDateDays();
  int eMonth=acb.getVisitDateMonths();
  int eYear=acb.getVisitDateMonths();;
  boolean disableDate=false;
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  JavaUtil.UserData UD = new UserData();
  DBAccess DBA = new DBAccess();
  String PageSent;
  String sharedPath=
        EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
  String colspan;
  String system = (String)session.getAttribute("System");
  String groupName = (String)session.getAttribute("Group_Name");
  String email = EBANProperties.getEBANProperty(EBANProperties.PROCESSEMAIL);
  String Header = "Header";
    //Are we coming from the handler page, or a fresh start?

  Message=(String)session.getAttribute("Message");
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  if (PageSent.endsWith("Handler.jsp"))
  {
    Customer_Id=BAN.getGlobalCustomerId();
    Service_Reference=BAN.getService_Reference();
    Global_Customer_Name=BAN.getGlobalCustomerName();

    ctBAN.getCircuit();
    C2_Ref=ctBAN.getLLUC2RefNo();
    Customer_Reference=ctBAN.getCustomer_Reference();
    Circuit_Description=ctBAN.getCircuit_Description();
    Circuit_Speed=ctBAN.getSpeed();
    Product_Type=ctBAN.getGCB_Product_Type();
    From_Site=ctBAN.getFullFromDets(Service_Reference);
    To_Site=ctBAN.getFullToDets(Service_Reference);

    eDate=SU.isNull(acb.getVisit_Date(),"Today");
    eDay = acb.getVisitDateDays();
    eMonth = acb.getVisitDateMonths();
    eYear = acb.getVisitDateYears();
  }
  else
  {
    eDate=acb.getVisit_Date();
    eDay=BAN.getVDay();
    eMonth=BAN.getVMonth();
    eYear=BAN.getVYear();
  }

%>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--
  function submitbuttonClick()
  {
    var OldSpeed=AmendService.OldSpeed.value;
    var NewSpeed=AmendService.LLU_Speed.value;
    var EffDate=AmendService.eDateh.value;
    if (NewSpeed=="")
    {
      alert('Circuit speed has not been selected');
    }
    else
    {
      if (NewSpeed==OldSpeed)
      {
        alert('Selected circuit speed is unchanged from original circuit speed');
      }
      else
      {
        var message = '\nPlease confirm that you wish to'+
                        '\nupdate the circuit speed to '+
                        NewSpeed+
                        '\n(effective date '+
                        EffDate+
                        ')';
        if (confirm(message))
        {
          AmendService.submit();
        }
      }
    }
  }
  //-->
  </script>
  <a name="top"></a>
  <table id=1 width ="100%">
    <tr>
      <td colspan=3>
        <%@ include file="../includes/Page_Header4.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
        <form name="AmendService" method="post" action="AmendServiceHandler.jsp">
          <%=
          HB.getBANMenu_Bar("Menu","","","")
          %>
        <input name="ButtonPressed" type=hidden value="">
        <input name="OldSpeed" type=hidden value=<%=Circuit_Speed%>>
      </td>
    </tr>
    <tr>
      <td colspan=3>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
	    <td valign="top"><br>
	      <h3 align="center"><b>LLU Circuit Speed Update - Amend A Circuit</b></h3>
                <table>
                  <tr>
                    <td width="22">
                      &nbsp;
                    </td>
                    <td>
                      <font color="red"><%=Message%></font><br>
	              <%session.setAttribute("Message","");%></font>
                    </td>
                  </tr>
                </table>
	        <table width="100%" border="0">
		  <tr>
		    <td colspan=1>
                      <table>
                        <tr>
                          <td width="22">
                            &nbsp;
                          </td>
                          <td>
                            <b>Customer:</b>
                          </td>
                          <td width="22">
                            &nbsp;
                          </td>
                          <td class=bluebold>
                            <%=Global_Customer_Name%>
                          </td>
                          <td width="472"
                            &nbsp;
                          </td>
                          <td>
                            <b>Requested By:</b>
                          </td>
                          <td width="22">
                            &nbsp;
                          </td>
                          <td class=bluebold>
                            <%=(String)session.getAttribute("User_Name")%>
                          </td>
                        </tr>
                        <tr>
                          <td width="22">
                            &nbsp;
                          </td>
                          <td colspan="7">
                            <hr>
                          </td>
                        </tr>
                      </table>
                      <table>
                        <tr>
                          <td width="22">
                            &nbsp;
                          </td>
                          <td>
                            <table width=100%>
                              <tr>
                                <td width="20%" colspan="1">
      			          <b><small>BAN summary<br>
                                    <textarea class=tworows
                                      name="BAN_Summary" cols="40" rows="2">
                                      <%=BAN_Summary%>
                                    </textarea>
                                </td>
                                <td colspan="1">
                                  &nbsp;
                                </td>
                                <td width="50%" colspan="2" Class="bluebold">
      			          Enter the new circuit speed and effective date,
                                  optionally enter BAN summary data and submit
                                  to action update of circuit speed.
                                </td>
                              </tr>
                              <tr>
			        <td width="50%" colspan="2" class=<%=ctBAN.getClass("Customer_Reference")%>>Circuit Reference
			          <br><input class=inp style="width:400px" type=text name="Circuit_Reference"
                                    value="<%=Service_Reference%>" readonly="false">
			        </td>
			        <td width="50%" colspan="2" class=<%=ctBAN.getClass("Customer_Reference")%>>Customer Reference
			          <br><input class=inp style="width:400px" type=text name="Customer_Reference"
                                    value="<%=Customer_Reference%>" readonly="false">
			        </td>
                              </tr>
                              <tr>
			        <td width="50%" colspan="2" class=<%=ctBAN.getClass("Customer_Reference")%>>Circuit Description
			          <br><input class=inp style="width:400px" type=text name="Circuit_Description"
                                    value="<%=Circuit_Description%>" readonly="false">
			        </td>
                                <td align=left width="25%" colspan="1" class=<%=ctBAN.getClass("Circuit_Reference")%>>Circuit Speed
			          <br><%=DBA.getListBox("LLU_Speed","",Circuit_Speed,Circuit_Speed)%>
		                </td>
                                <td align=left width="25%" colspan="1" valign="middle">
                                  &nbsp;<br><%=Circuit_Speed%>&nbsp;<i>(current value)</i>
                                </td>
                              <tr>
			        <td width="50%" colspan="2" class=<%=ctBAN.getClass("Customer_Reference")%>>Product Type
			          <br><input class=inp style="width:100px" type=text name="Product_Type"
                                    value="<%=Product_Type%>" readonly="false">
			        </td>
                                <td class=<%=acb.getClass("VisitDateh")%>>Effective_Date<br>
                                  <%=HB.getDays("AmendService", "eDate", eDay, false, disableDate)%>
                                  <%=HB.getMonths("AmendService", "eDate", eMonth, false, disableDate)%>
                                  <%=HB.getYears("AmendService", "eDate", eYear, false, disableDate)%>
                                  <input type="hidden" name="eDateh" value="<%=eDate%>">
                                </td>
                              </tr>
                              <tr>
			        <td width="50%" colspan="2" class=<%=ctBAN.getClass("Customer_Reference")%>>From Site
			          <br><input class=inp style="width:400px" type=text name="From_Site"
                                    value="<%=From_Site%>" readonly="false">
			        </td>
			        <td width="50%" colspan="2" class=<%=ctBAN.getClass("Customer_Reference")%>>To Site
			          <br><input class=inp style="width:400px" type=text name="To_Site"
                                    value="<%=To_Site%>" readonly="false">
			        </td>
                              </tr>
                              <tr>
			        <td width="50%" colspan="2" class=<%=ctBAN.getClass("Customer_Reference")%>>C2 Ref No
			          <br><input class=inp style="width:400px" type=text name="C2_Ref_No"
                                    value="<%=C2_Ref%>" readonly="false">
			        </td>
                                <td width=50% colspan="2" align="center" valign="bottom">
                                  <%=HB.getImageAsAnchor("submitbutton")%>
                                </td>
                              </tr>
                              <tr>
                                <td>
                                  &nbsp;
                                </td>
                              </tr>
                              <tr>
                                <td class="bluebold" align="left">
                                  Change History
                                </td>
                              </tr>
                              <tr>
                                <td width="100%" colspan=2 class=bluebold>
                                  <iframe frameborder=0 width="100%" height=20 id=GridHeader name=GridHeader
                                    src="HistoryGridHeader.jsp" scrolling="no">
                                  </iframe>
	                          <%=SU.isNull((String)session.getAttribute("Message"),"")%><br>
	                          <%
	                            session.setAttribute("formname","listHistory");
                                    session.setAttribute("Message","");
	                          %>
	                          <iframe frameborder=0 width="100%" height=80 id=GridData name=GridData
                                    src="HistoryGrid.jsp" srolling="yes" ></iframe>
	                      </td>
                            </table>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
        </table>
      </td>
      <td>&nbsp;
      </td>
    </tr>
    </table><!--2-->
      </td>
    </tr>
    <tr>
      <td height="100">&nbsp;
      </td>
        </form>
    </tr>
  </table><!--1-->
</body>
</html>