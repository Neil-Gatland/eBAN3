<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="crcBAN" class="DBUtilities.ConglomReportConfigBANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  session.setAttribute("formname","conglomReportConfigMaint");
  String SQL;
  //DBAccess DBA = new DBAccess();

  String action;
  String Menu_Headings[]={"Go To", "Options"/*, "Admin"*/};
  String Options[][] = {{"Log Off","Conglom Desktop","Conglom Billing Menu","Conglom Cust Profile Maintenanance"},
                            {"Submit","","Refresh","Raise Query"}};

  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  String reportType = crcBAN.getReportType();
  boolean isLocal = reportType.endsWith("L");
  String startPos1 = crcBAN.getStartPos(1);
  String length1 = crcBAN.getLength(1);
  String conglomDataItem1 = crcBAN.getConglomDataItem(1);
  String conglomSort1 = crcBAN.getConglomSort(1);
  String startPos2 = crcBAN.getStartPos(2);
  String length2 = crcBAN.getLength(2);
  String conglomDataItem2 = crcBAN.getConglomDataItem(2);
  String conglomSort2 = crcBAN.getConglomSort(2);
  String startPos3 = crcBAN.getStartPos(3);
  String length3 = crcBAN.getLength(3);
  String conglomDataItem3 = crcBAN.getConglomDataItem(3);
  String conglomSort3 = crcBAN.getConglomSort(3);
  String startPos4 = crcBAN.getStartPos(4);
  String length4 = crcBAN.getLength(4);
  String conglomDataItem4 = crcBAN.getConglomDataItem(4);
  String conglomSort4 = crcBAN.getConglomSort(4);
  String startPos5 = crcBAN.getStartPos(5);
  String length5 = crcBAN.getLength(5);
  String conglomDataItem5 = crcBAN.getConglomDataItem(5);
  String conglomSort5 = crcBAN.getConglomSort(5);
  String conglomSubLevel1 = crcBAN.getConglomSubLevel1();
  String conglomSubLevel2 = crcBAN.getConglomSubLevel2();
  String billedProduct = crcBAN.getBilledProductId();
  String feedSource = crcBAN.getBilledProductFeedSource();
  String displayCustomer = BAN.getGlobalCustomerId() + " " +
    BAN.getConglomCustomerName() + " (" + BAN.getConglomCustomerId() + ")";
  String Mode=crcBAN.getMode();
  String Action=crcBAN.getAction();
  String SelectMode=crcBAN.getSelectMode();
  String InputMode=crcBAN.getInputMode();
  String sortOrder = "";
  String allSources = Action.equals("Add")?"all":"";

  if (Mode.equals("Confirm"))
  {
    Options[1][0] = "Confirm";
    Options[1][1] = "Cancel";
    //disableDate = true;
  }
  else if (Mode.equals("View"))
  {
    Options[1][0] = "";
    Options[1][2] = "";
    //disableDate = true;
  }
  else if (Mode.equals("Delete"))
  {
    Options[1][0] = "Delete";
    Options[1][2] = "";
    //disableDate = true;
  }
  else if (Mode.equals("Amend"))
  {
    Options[1][0] = "Update";
    Options[1][1] = isLocal?"Update & Reload":"";
    Options[1][2] = "";
    //disableDate = true;
  }

%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function saveDescription(itemNo)
{

}
</script>
<form name="conglomReportConfigMaint" method="post" action="conglomReportConfigMaintHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<a name="top"></a>
<table id=1 border=0 width="100%">
  <tr>
    <td colspan=3>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
    <td colspan=3>
      <%=HB.getMenu_Bar()%>
    </td>
  </tr>
  <tr>
   <td colspan=3>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
      <tr id=2>
	<!-- this is a spacer column-->
	<td width="1" id=2>&nbsp;</td>
	<td width="1" valign="top" align="left" id=2>
	</td>
	<td width="12" id=2><!-- a spacer column-->
	  &nbsp;
	</td>
	<td valign="top" id=2>
	  <h2 align="center"><%=Action%> Conglomerate Report Configuration</h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="6" width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","conglomReportConfigMaint");
	      %>
    	        <%=SU.hasNoValue(crcBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
                <%crcBAN.setMessage("");%>
              <td>
            </tr>
            <tr>
              <td width="15%"><b>Customer:</b></td>
              <td align=left class=bluebold><%=displayCustomer%></td>
              <td colspan=4>
                &nbsp;
              </td>
            </tr>
            <tr>
              <td colspan=6>
                <hr>
              </td>
            </tr>
            <tr>
              <td colspan=6>
                <table border=0 width=100%>
                  <tr>
                    <td class=<%=crcBAN.getClass("conglomBilledProduct")%>><b>Product Name
                      <br><%=DBA.getListBox("conglomBilledProduct",crcBAN.getSelectMode("conglomBilledProduct"),
                        billedProduct,feedSource,allSources,1,"",true)%>
                    </td>
                    <td>
                    </td>
                    <td>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=crcBAN.getClass("conglomReportType")%>><b>Report Type
                      <br><%=DBA.getListBox("conglomReportType",crcBAN.getSelectMode("conglomReportType"),
                        reportType,"x",1,"",true)%>
                    </td>
                    <td>
                    </td>
                    <td class=optional valign=bottom>
                    </td>
                  </tr>
<%if (isLocal)
  {%>
                  <tr>
                    <td colspan=3 align=left>
                      <table border=1 width=60%>
                        <tr>
                          <td width=22% class=<%=crcBAN.getClass("conglomDataItem1")%> style="border-color:silver"><b>Data Item 1
                            <br><%=DBA.getListBox("conglomDataItem1",crcBAN.getSelectMode("conglomDataItem1"),
                              conglomDataItem1,Long.toString(BAN.getConglomCustomerId()),billedProduct,reportType.substring(0,reportType.length()-1))%>
                          </td>
                          <td width=30% class=<%=crcBAN.getClass("pos1")%>  style="border-color:silver" valign=bottom>&nbsp;start position
                            <input style="width:30px;height:18px;font-size:xx-small;" type=text name="startPos1" value="<%=startPos1%>">
                            for
                            <input style="width:30px;height:18px;font-size:xx-small;" type=text name="length1" value="<%=length1%>">
                            characters
                          </td>
                          <td width=10% class=<%=crcBAN.getClass("conglomSort1")%>><b>Sort
                            <br><%=DBA.getListBox("conglomSort1",crcBAN.getSelectMode("conglomSort1"),
                              conglomSort1,"x",1,"style=\"width:40px\"",true)%>
                          </td>
                        </tr>
                        <tr>
                          <td width=22% class=<%=crcBAN.getClass("conglomDataItem2")%>><b>Data Item 2
                            <br><%=DBA.getListBox("conglomDataItem2",crcBAN.getSelectMode("conglomDataItem2"),
                              conglomDataItem2,Long.toString(BAN.getConglomCustomerId()),billedProduct,reportType.substring(0,reportType.length()-1))%>
                          </td>
                          <td width=30% class=<%=crcBAN.getClass("pos2")%> valign=bottom>&nbsp;start position
                            <input style="width:30px;height:18px;font-size:xx-small;" type=text name="startPos2" value="<%=startPos2%>">
                            for
                            <input style="width:30px;height:18px;font-size:xx-small;" type=text name="length2" value="<%=length2%>">
                            characters
                          </td>
                          <td valign=bottom width=10% class=<%=crcBAN.getClass("conglomSort2")%>>
                            <br><%=DBA.getListBox("conglomSort2",crcBAN.getSelectMode("conglomSort2"),
                              conglomSort2,"x",1,"style=\"width:40px\"",true)%>
                          </td>
                        </tr>
                        <tr>
                          <td width=22% class=<%=crcBAN.getClass("conglomDataItem3")%>><b>Data Item 3
                            <br><%=DBA.getListBox("conglomDataItem3",crcBAN.getSelectMode("conglomDataItem3"),
                              conglomDataItem3,Long.toString(BAN.getConglomCustomerId()),billedProduct,reportType.substring(0,reportType.length()-1))%>
                          </td>
                          <td width=30% class=<%=crcBAN.getClass("pos3")%> valign=bottom>&nbsp;start position
                            <input style="width:30px;height:18px;font-size:xx-small;" type=text name="startPos3" value="<%=startPos3%>">
                            for
                            <input style="width:30px;height:18px;font-size:xx-small;" type=text name="length3" value="<%=length3%>">
                            characters
                          </td>
                          <td valign=bottom width=10% class=<%=crcBAN.getClass("conglomSort3")%>>
                            <br><%=DBA.getListBox("conglomSort3",crcBAN.getSelectMode("conglomSort3"),
                              conglomSort3,"x",1,"style=\"width:40px\"",true)%>
                          </td>
                        </tr>
                        <tr>
                          <td width=22% class=<%=crcBAN.getClass("conglomDataItem4")%>><b>Data Item 4
                            <br><%=DBA.getListBox("conglomDataItem4",crcBAN.getSelectMode("conglomDataItem4"),
                              conglomDataItem4,Long.toString(BAN.getConglomCustomerId()),billedProduct,reportType.substring(0,reportType.length()-1))%>
                          </td>
                          <td width=30% class=<%=crcBAN.getClass("pos4")%> valign=bottom>&nbsp;start position
                            <input style="width:30px;height:18px;font-size:xx-small;" type=text name="startPos4" value="<%=startPos4%>">
                            for
                            <input style="width:30px;height:18px;font-size:xx-small;" type=text name="length4" value="<%=length4%>">
                            characters
                          </td>
                          <td valign=bottom width=10% class=<%=crcBAN.getClass("conglomSort4")%>>
                            <br><%=DBA.getListBox("conglomSort4",crcBAN.getSelectMode("conglomSort4"),
                              conglomSort4,"x",1,"style=\"width:40px\"",true)%>
                          </td>
                        </tr>
                        <tr>
                          <td width=22% class=<%=crcBAN.getClass("conglomDataItem5")%>><b>Data Item 5
                            <br><%=DBA.getListBox("conglomDataItem5",crcBAN.getSelectMode("conglomDataItem5"),
                              conglomDataItem5,Long.toString(BAN.getConglomCustomerId()),billedProduct,reportType.substring(0,reportType.length()-1))%>
                          </td>
                          <td width=30% class=<%=crcBAN.getClass("pos5")%> valign=bottom>&nbsp;start position
                            <input style="width:30px;height:18px;font-size:xx-small;" type=text name="startPos5" value="<%=startPos5%>">
                            for
                            <input style="width:30px;height:18px;font-size:xx-small;" type=text name="length52" value="<%=length5%>">
                            characters
                          </td>
                          <td valign=bottom width=10% class=<%=crcBAN.getClass("conglomSort5")%>>
                            <br><%=DBA.getListBox("conglomSort5",crcBAN.getSelectMode("conglomSort5"),
                              conglomSort5,"x",1,"style=\"width:40px\"",true)%>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
<%}%>
                  <tr>
                    <td class=optional valign=bottom><b>Report Sub-totalling
                    </td>
                    <td>
                    </td>
                    <td>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=crcBAN.getClass("conglomSubLevel1")%>><b>First Level
                      <br><%=DBA.getListBox("conglomSubLevel1",crcBAN.getInputMode("conglomSubLevel1"),
                        conglomSubLevel1,"")%>
                    </td>
                    <td>
                    </td>
                    <td>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=crcBAN.getClass("conglomSubLevel2")%>><b>Second Level
                      <br><%=DBA.getListBox("conglomSubLevel2",crcBAN.getInputMode("conglomSubLevel2"),
                        conglomSubLevel2,"")%>
                    </td>
                    <td>
                    </td>
                    <td>
                    </td>
                  </tr>
                </table>
	      </td>
	    </tr>
	  </table><!--table 3-->
      </td>
      <td width="1">&nbsp;</td><!--spacer-->
      <td width="1" valign="top"><!--Right Hand column-->
      </td>
    </tr>
  </table><!--2-->
    </td>
  </tr>
</table><!--1-->
</form>
</body>
</html>


