<html>
<head>
  <title>Provider Master Account Summary</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="JavaUtil.RSAccountDescriptor"%>
<%@ page import="JavaUtil.RSProductDescriptor"%>
<%@ page import="JavaUtil.RSRatingSchemeDescriptor"%>
<%@ page import="JavaUtil.RSThresholdDescriptor"%>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<jsp:useBean id="PRO" class="DBUtilities.ProviderBean" scope="session"/>
<jsp:useBean id="MAC" class="DBUtilities.MasterAccountBean" scope="session"/>
<%
  boolean nextMonth = request.getParameter("fromSelf")==null?false:
    request.getParameter("fromSelf").equals("next")?true:false;

  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String callMonth = nextMonth?RSB.getNextCallMonth():RSB.getLatestCallMonth();
  Collection accounts = MAC.getAccounts();
  Collection thresholds = MAC.getThresholds();
  Collection macSchemes = MAC.getAppliedRatingSchemes(callMonth, "");
  String defaultRatingScheme = MAC.getSignName().equals("")?"(none)":MAC.getSignName();

//test
/*  response.setHeader("CacheControl", "no-cache");
  response.setHeader("Pragma", "no-cache"); // for http 1.0 browsers
  response.setIntHeader("Expires", -1);
  // change page content type to Excel
  response.setContentType("application/vnd.ms-word");
  response.setHeader("content-disposition","attachment; filename=test.doc");
*/
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function valAndSub()
{
}


function window_onload()
{
  window.focus();
}
</script>
<body language=javascript onload="return window_onload()">
<form name="pmaSummary" method="post" action="pmaSummary.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="<%=nextMonth?"this":"next"%>">
<a name="top"></a>
<table width="100%" border="0" id=1>
  <tr>
    <td align=center height=30 valign=center>
      <input class=button type=button value=Print style="width:70" onclick="window.print();return false;">
    </td>
    <td align=center height=30 valign=center>
      <input class=button type=button value="<%=nextMonth?"Show Current Month":"Show Next Month"%>" style="width:210" onclick="submit();">
    </td>
    <td align=center height=30 valign=center>
      <input class=button type=button value=Close style="width:70" onClick="window.close();">
    </td>
  </tr>
  <tr>
    <td colspan="3">
      <table width="100%" border="1" id=2>
        <tr>
          <td>
            <table width="100%" border="0" id=3>
              <tr>
                <td colspan="4"><b><u>Provider Details:</u></b>
                </td>
              </tr>
              <tr>
                <td>Provider Name:
                </td>
                <td><%=PRO.getProviderName()%>
                </td>
                <td>NPID:
                </td>
                <td><%=PRO.getNPID()%>
                </td>
              </tr>
              <tr>
                <td>VAT Number:
                </td>
                <td><%=PRO.getVATNumber()%>
                </td>
                <td>SAP Vendor Number:
                </td>
                <td><%=PRO.getSAPVendorNumber()%>
                </td>
              </tr>
              <tr>
                <td>Bank Details Returned:
                </td>
                <td><%=PRO.getBankDetailsReturned()%>
                </td>
                <td>Self Bill Agreement:
                </td>
                <td><%=PRO.getSelfBillAgreement()%>
                </td>
              </tr>
              <tr>
                <td>VAT Exempt:
                </td>
                <td><%=PRO.getVATExemptInd()%>
                </td>
                <td>Customer Contract:
                </td>
                <td><%=PRO.getCustomerContract()%>
                </td>
              </tr>
              <tr>
                <td>Upload to SAP:
                </td>
                <td><%=PRO.getSAPUpload()%>
                </td>
              </tr>
            </table><!--table 3-->
          </td>
        </tr>
        <tr>
          <td>
            <table width="100%" border="0" id=4>
              <tr>
                <td colspan="4"><b><u>Master Account Details:</u></b>
                </td>
              </tr>
              <tr>
                <td>Master Account Name:
                </td>
                <td><%=MAC.getMasterAccountName()%>
                </td>
                <td>Master Account Number:
                </td>
                <td><%=MAC.getMasterAccountNumber()%>
                </td>
              </tr>
              <tr>
                <td>Frequency:
                </td>
                <td><%=MAC.getFrequencyCode()%>
                </td>
                <td>Default Rating Scheme:
                </td>
                <td><%=defaultRatingScheme%>
                </td>
              </tr>
              <tr>
                <td>Managed:
                </td>
                <td><%=MAC.getManagedMasterAccount()%>
                </td>
                <td>Channel:
                </td>
                <td><%=MAC.getMasterAccountChannel()%>
                </td>
              </tr>
              <tr>
                <td>Approved Product Groups:
                </td>
                <td colspan="4"><%=MAC.getApprovedProductGroups()%>
                </td>
              </tr>
              <tr>
                <td><u>Thresholds Applied</u>
                </td>
                <td colspan="4">
                </td>
              </tr>
              <tr>
                <td colspan="4">
                  <table width="100%" border="1" id=20>
                    <tr>
                      <td><b>Threshold Type</b>
                      </td>
                      <td><b>PRS</b>
                      </td>
                      <td><b>Minimum Amount</b>
                      </td>
                      <td><b>Minimum Minutes</b>
                      </td>
                    </tr>
<%for (Iterator it5 = thresholds.iterator(); it5.hasNext(); )
{
RSThresholdDescriptor rst = (RSThresholdDescriptor)it5.next();
%>
                    <tr>
                      <td><%=rst.getType()%>
                      </td>
                      <td><%=rst.getPRSFlag()%>
                      </td>
                      <td><%=rst.getMinAmount()%>
                      </td>
                      <td><%=rst.getMinMinutes()%>
                      </td>
                    </tr>
<%}%>
                  </table><!--table 20-->
                </td>
              </tr>
              <tr>
                <td><u>Rating Schemes Applied <%=nextMonth?"<font color=red><b>":""%>(<%=callMonth%>)<%=nextMonth?"</b></font>":""%>:</u>
                </td>
                <td colspan="4">
                </td>
              </tr>
              <tr>
                <td colspan="4">
                  <table width="100%" border="1" id=10>
                    <tr>
                      <td><b>Product Code</b>
                      </td>
                      <td><b>Rating Type</b>
                      </td>
                      <td><b>From Call Month</b>
                      </td>
                      <td><b>Duration</b>
                      </td>
                      <td><b>Range Start</b>
                      </td>
                      <td><b>Range End</b>
                      </td>
                      <td><b>Day Rate</b>
                      </td>
                      <td><b>Evening Rate</b>
                      </td>
                      <td><b>Weekend Rate</b>
                      </td>
                    </tr>
<%for (Iterator it4 = macSchemes.iterator(); it4.hasNext(); )
{
RSRatingSchemeDescriptor rsr = (RSRatingSchemeDescriptor)it4.next();
%>
                    <tr>
                      <td><%=rsr.getProductCode()%>
                      </td>
                      <td><%=rsr.getRatingType()%>
                      </td>
                      <td><%=rsr.getFromCallMonth()%>
                      </td>
                      <td><%=rsr.getDuration()%>
                      </td>
                      <td><%=rsr.getRangeStart()%>
                      </td>
                      <td><%=rsr.getRangeEnd()%>
                      </td>
                      <td><%=rsr.getDayRate()%>
                      </td>
                      <td><%=rsr.getEveningRate()%>
                      </td>
                      <td><%=rsr.getWeekendRate()%>
                      </td>
                    </tr>
<%}%>
                  </table><!--table 10-->
                </td>
              </tr>
<%if (!defaultRatingScheme.equals("(none)"))
  {
    Collection defaultScheme = MAC.getDefaultRatingScheme(callMonth);
%>
              <tr>
                <td><u>Default Rating Scheme (<%=defaultRatingScheme%>) <%=nextMonth?"<font color=red><b>":""%>(<%=callMonth%>)<%=nextMonth?"</b></font>":""%>:</u>
                </td>
                <td colspan="4">
                </td>
              </tr>
              <tr>
                <td colspan="4">
                  <table width="100%" border="1" id=10>
                    <tr>
                      <td><b>Product Code</b>
                      </td>
                      <td><b>Rating Type</b>
                      </td>
                      <td><b>From Call Month</b>
                      </td>
                      <td><b>Duration</b>
                      </td>
                      <td><b>Range Start</b>
                      </td>
                      <td><b>Range End</b>
                      </td>
                      <td><b>Day Rate</b>
                      </td>
                      <td><b>Evening Rate</b>
                      </td>
                      <td><b>Weekend Rate</b>
                      </td>
                    </tr>
<%for (Iterator it5 = defaultScheme.iterator(); it5.hasNext(); )
{
RSRatingSchemeDescriptor rsr = (RSRatingSchemeDescriptor)it5.next();
%>
                    <tr>
                      <td><%=rsr.getProductCode()%>
                      </td>
                      <td><%=rsr.getRatingType()%>
                      </td>
                      <td><%=rsr.getFromCallMonth()%>
                      </td>
                      <td><%=rsr.getDuration()%>
                      </td>
                      <td><%=rsr.getRangeStart()%>
                      </td>
                      <td><%=rsr.getRangeEnd()%>
                      </td>
                      <td><%=rsr.getDayRate()%>
                      </td>
                      <td><%=rsr.getEveningRate()%>
                      </td>
                      <td><%=rsr.getWeekendRate()%>
                      </td>
                    </tr>
<%}%>
                  </table><!--table 10-->
                </td>
              </tr>
<%}%>
            </table><!--table 4-->
          </td>
        </tr>
        <tr>
          <td>
            <table width="100%" border="0" id=5>
              <tr>
                <td colspan="4"><b><u>Account Details:</u></b>
                </td>
              </tr>
<%for (Iterator it = accounts.iterator(); it.hasNext(); )
  {
    RSAccountDescriptor rsa = (RSAccountDescriptor)it.next();
    Collection products = MAC.getAccountProducts(rsa.getAccountId());
    Collection accSchemes = MAC.getAppliedRatingSchemes(callMonth, rsa.getAccountNo());
%>
              <tr>
                <td colspan="4">
                  <table width="100%" border="1" id=6>
                    <tr>
                      <td>
                        <table width="100%" border="0" id=7>
                          <tr>
                            <td>Account Name:
                            </td>
                            <td><%=rsa.getAccountName()%>
                            </td>
                            <td>Account Number:
                            </td>
                            <td><%=rsa.getAccountNo()%>
                            </td>
                          </tr>
                          <tr>
                            <td>Invoice:
                            </td>
                            <td colspan="3"><%=rsa.getInvoiceInd()%>
                            </td>
                          </tr>
                          <tr>
                            <td><u>Rating Schemes Applied <%=nextMonth?"<font color=red><b>":""%>(<%=callMonth%>)<%=nextMonth?"</b></font>":""%>:</u>
                            </td>
                            <td colspan="3">
                            </td>
                          </tr>
                          <tr>
                            <td colspan="4">
                              <table width="100%" border="1" id=9>
                                <tr>
                                  <td><b>Product Code</b>
                                  </td>
                                  <td><b>Rating Type</b>
                                  </td>
                                  <td><b>From Call Month</b>
                                  </td>
                                  <td><b>Duration</b>
                                  </td>
                                  <td><b>Range Start</b>
                                  </td>
                                  <td><b>Range End</b>
                                  </td>
                                  <td><b>Day Rate</b>
                                  </td>
                                  <td><b>Evening Rate</b>
                                  </td>
                                  <td><b>Weekend Rate</b>
                                  </td>
                                </tr>
<%for (Iterator it3 = accSchemes.iterator(); it3.hasNext(); )
  {
    RSRatingSchemeDescriptor rsr = (RSRatingSchemeDescriptor)it3.next();
%>
                                <tr>
                                  <td><%=rsr.getProductCode()%>
                                  </td>
                                  <td><%=rsr.getRatingType()%>
                                  </td>
                                  <td><%=rsr.getFromCallMonth()%>
                                  </td>
                                  <td><%=rsr.getDuration()%>
                                  </td>
                                  <td><%=rsr.getRangeStart()%>
                                  </td>
                                  <td><%=rsr.getRangeEnd()%>
                                  </td>
                                  <td><%=rsr.getDayRate()%>
                                  </td>
                                  <td><%=rsr.getEveningRate()%>
                                  </td>
                                  <td><%=rsr.getWeekendRate()%>
                                  </td>
                                </tr>
<%}%>
                              </table><!--table 9-->
                            </td>
                          </tr>
                          <tr>
                            <td><u>Allocated Products:</u>
                            </td>
                            <td colspan="3">
                            </td>
                          </tr>
                          <tr>
                            <td colspan="4">
                              <table width="100%" border="1" id=8>
                                <tr>
                                  <td><b>Product Code</b>
                                  </td>
                                  <td><b>Description</b>
                                  </td>
                                  <td><b>Product Group</b>
                                  </td>
                                </tr>
<%for (Iterator it2 = products.iterator(); it2.hasNext(); )
  {
    RSProductDescriptor rsp = (RSProductDescriptor)it2.next();
%>
                                <tr>
                                  <td><%=rsp.getProductCode()%>
                                  </td>
                                  <td><%=rsp.getProductDescription()%>
                                  </td>
                                  <td><%=rsp.getProductGroup()%>
                                  </td>
                                </tr>
<%}%>
                              </table><!--table 8-->
                            </td>
                          </tr>
                        </table><!--table 7-->
                      </td>
                    </tr>
                  </table><!--table 6-->
                </td>
              </tr>
<%}%>
            </table><!--table 5-->
          </td>
        </tr>
      </table><!--table 2-->
    </td>
  </tr>
</table><!--table 1-->
</form>
</body>
</html>


