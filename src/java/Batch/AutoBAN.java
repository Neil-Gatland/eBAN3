/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

/**
 *
 * @author taitchison
 */
import DBUtilities.DBAccess;
import JavaUtil.EBANProperties;
import JavaUtil.StringUtil;
import java.io.*;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class AutoBAN {
    
    private final String autoBANInputDir =
        EBANProperties.getEBANProperty("autoBANInputDir");
    private final String autoBANProcessedDir =
        EBANProperties.getEBANProperty("autoBANProcessedDir");
    private final String autoBANRejectDir =
        EBANProperties.getEBANProperty("autoBANRejectDir");
    private final String autoBANLogDir =
        EBANProperties.getEBANProperty("autoBANLogDir");
    private final String monitorDir =
        EBANProperties.getEBANProperty("monitorDir");
    private final String gcbAdminAddress =
        EBANProperties.getEBANProperty("gcbAdminAddress");
    private BufferedWriter logWriter;
    private String runTS;
    private String accountNo;
    private String gcId;
    private String gcIdPrev;
    private final String TENZEROES = "0000000000";
    private final String IMPLEMENTED = "Implemented";
    private final String CREATE = "Create";
    private final String AMEND = "Amend";
    private final String CEASE = "Cease";
    private final String INSTALL = "Install";
    private final String ONEOFF = "One Off";
    private final String MONTHLY = "Monthly";
    private final String QUARTERLY = "Quarterly";
    private final String ANNUAL = "Annual";
    private final String CHARGE = "Charge";
    private final String CREDIT = "Credit";
    private final String CW = "C&W";
    private final String LINENUMBER = "Line Number";
    private DBAccess dba;
    private Connection transactionConn;
    private boolean valid;
    private boolean titleFound;
    private ArrayList errorMessages;
    private ArrayList adminMessages;
    private ArrayList emailAddresses;
    private String chargeFrequency;
    private String chargeDescription;
    private String chargeType;
    private String gsr;
    private String currencyCode;
    private String c2c3QRef;
    private String revenueType;
    private String banSummary;
    private String banRaisedBy;
    private String siteId;
    private String c2Ref;
    private String loginId;
    private String loginIdPrev;
    private String userFirstname;
    private double chargeAmount;
    private long unitQuantity;
    private Date circuitBillingStartDate;
    private Date billingDate;
    private Date periodStartDate;
    private Date periodEndDate;
    private java.sql.Date fromChargeValidDate;
    private final String[] CHARGETYPES = 
        {"Install Monthly Re-Occurring Charge",
            "Install Quarterly Re-Occurring Charge",
            "Install Annual Re-Occurring Charge",
            "Cease Re-Occurring Charge",
            "One Off Charge",
            "Credit"};
    private final String[][] REVENUETYPES = {
        {"Discounts", "Installations", "Maintenance", "One Off Charge", "OPEX", 
            "Professional Services", "Rentals", "Usage International", "Usage National"},
            {"DI", "IT", "MA", "OO", "OP", "PS", "RE", "UI", "UN"}};
    private long chargeId;
    private StringUtil su;
    private final String DATETIMEFORMAT = "yyyyMMddHHmmss";
    private File logFile;
    private long rowNo;
    private FileInputStream fis;
    private Workbook workbook;
    private boolean adminEmailRequired;
    private String customerDetails;
    private boolean debug = 
        EBANProperties.getEBANProperty("autoBANDebug", "N").equalsIgnoreCase("Y");
    private String bRBPrefix = 
        EBANProperties.getEBANProperty("autoBANBRBPrefix", "");
    private String prReference;
    
    public AutoBAN() 
    {
        dba = new DBAccess();            
        su = new StringUtil();
        runTS = su.reformatDate("now", null, DATETIMEFORMAT);
        logFile = new File(autoBANLogDir + File.separator + "AutoBAN_" +
            runTS + "_log.txt");
        transactionConn = null;
    }        
          
    public static void main(String[] args) throws Exception, FileNotFoundException, IOException 
    {
        AutoBAN ab = new AutoBAN();
        ab.processXLS();
    }
    
    private void createLogFile()
    {
        try
        {
            logWriter = new BufferedWriter(new FileWriter(logFile));
            GregorianCalendar gc = new GregorianCalendar();
            String now = su.DateToString(gc.getTime(), DATETIMEFORMAT);
            writeToLogFile("AutoBAN processing started at " +
            now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
            now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
            now.substring(4, 6) + "/" + now.substring(0, 4), false);
        }
        catch (Exception ex)
        {
            System.out.println("Error creating log file: " + ex.getMessage());
            System.exit(1);
        }
    }

    private void writeToLogFile(String message, boolean admin)
    {
        try
        {
            if (message != null)
            {
                if (admin)
                {
                    adminMessages.add(message);    
                    adminEmailRequired = true;
                }    
                logWriter.write(message);
                logWriter.newLine();
            }
        }
        catch (Exception ex)
        {
            closeLogFile();
            System.out.println("Error writing message '" + message +
            "' to log file: " + ex.getMessage());
            System.exit(1);
        }
    }

    private void closeLogFile()
    {
        try
        {
            GregorianCalendar gc = new GregorianCalendar();
            String now = su.DateToString(gc.getTime(), DATETIMEFORMAT);
            writeToLogFile("AutoBAN processing ended at " +
            now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
            now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
            now.substring(4, 6) + "/" + now.substring(0, 4), false);
            logWriter.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error closing log file: " + ex.getMessage());
            System.exit(1);
        }
    }

    private boolean insertBPRCQueue()
        throws Exception
    {
        boolean success = false;
        try
        {
            String sql = "{? = call givn..insert_bprc_queue" +
                    "(?,?,?,?,?,?,?)}";
            CallableStatement cstmt = transactionConn.prepareCall(sql);
            cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
            cstmt.setString(2, gcId);
            cstmt.setString(3, "GM Billing");
            cstmt.setString(4, "N"); 
            cstmt.setString(5, "C");
            cstmt.setString(6, "0");
            cstmt.setString(7, "Y");
            cstmt.setString(8, "AutoBAN");
            cstmt.execute();
            long seqNo = cstmt.getLong(1);
            success = true;
        }
        catch (Exception ex)
        {
            writeToLogFile("Error in insertBPRCQueue(): " + ex.getMessage(), true);
        }
        finally
        {
            return success;
        }
    }

    private boolean authoriseChargeBAN()
        throws Exception
    {
        boolean success = false;
        try
        {
//System.out.println("authoriseChargeBAN()");            
            String sql = "{? = call eban..authorise_charge_ban" +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement cstmt = transactionConn.prepareCall(sql);
            cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
            cstmt.setString(2, IMPLEMENTED);
            cstmt.setString(3, banSummary);
            cstmt.setString(4, banRaisedBy + userFirstname); //??
            cstmt.setDate(5, new java.sql.Date((new Date()).getTime()));
            cstmt.setString(6, gsr);
            cstmt.setDouble(7, chargeAmount);
            cstmt.setString(8, null);
            cstmt.setDate(9, new java.sql.Date(billingDate.getTime())); //??
            cstmt.setString(10, null); //?? end date
            cstmt.setString(11, chargeDescription);
            cstmt.setString(12, "0"); //number of splits
            cstmt.setString(13, siteId);
            cstmt.setString(14, null);
            cstmt.setString(15, null);
            cstmt.setDouble(16, 100); //split1 - 100
            cstmt.setString(17, null);
            cstmt.setString(18, null);
            cstmt.setString(19, chargeFrequency);
            cstmt.setString(20, currencyCode);
            cstmt.setString(21, revenueType);
            cstmt.setString(22, null);
            cstmt.setString(23, null);
            cstmt.setString(24, null);
            cstmt.setInt(25, 1);
            cstmt.setString(26, gcId);
            cstmt.setString(27, CREATE);
            cstmt.setString(28, CW);
            cstmt.setString(29, c2c3QRef);
            cstmt.setString(30, null);
            cstmt.setString(31, customerDetails);
            cstmt.execute();
            success = cstmt.getString(1).equals("0");
//System.out.println("success: " + (success?"true":"false"));            
        }
        catch (Exception ex)
        {
            writeToLogFile("Error in authoriseChargeBAN(): " + ex.getMessage(), true);
        }
        finally
        {
            return success;
        }
    }

    private boolean authoriseOneOffChargeBAN()
        throws Exception
    {
        boolean success = false;
        try
        {
            String sql = "{? = call eban..authorise_one_off_charge_ban" +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement cstmt = transactionConn.prepareCall(sql);
            cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
            cstmt.setString(2, IMPLEMENTED);
            cstmt.setString(3, banSummary);
            cstmt.setString(4, banRaisedBy + userFirstname); //??
            cstmt.setDate(5, new java.sql.Date((new Date()).getTime()));
            cstmt.setString(6, gsr);
            cstmt.setDouble(7, chargeAmount);
            cstmt.setString(8, null);
            cstmt.setDate(9, new java.sql.Date(billingDate.getTime())); //??
            cstmt.setString(10, chargeDescription);
            cstmt.setString(11, "0"); //number of splits
            cstmt.setString(12, siteId);
            cstmt.setString(13, null);
            cstmt.setString(14, null);
            cstmt.setDouble(15, 100); //split1 - 100
            cstmt.setString(16, null);
            cstmt.setString(17, null);
            cstmt.setString(18, currencyCode);
            cstmt.setString(19, revenueType);
            cstmt.setString(20, null);
            cstmt.setString(21, null);
            cstmt.setString(22, null);
            cstmt.setString(23, gcId);
            cstmt.setString(24, CREATE);
            cstmt.setString(25, CW);
            cstmt.setString(26, c2c3QRef);
            cstmt.setString(27, null);
            cstmt.setDate(28, periodStartDate==null?null:new java.sql.Date(periodStartDate.getTime())); //??
            cstmt.setDate(29, periodEndDate==null?null:new java.sql.Date(periodEndDate.getTime())); //??
            cstmt.setString(30, customerDetails);
            cstmt.execute();
            success = cstmt.getString(1).equals("0");
        }
        catch (Exception ex)
        {
            writeToLogFile("Error in authoriseOneOffChargeBAN(): " + ex.getMessage(), true);
        }
        finally
        {
            return success;
        }
    }

    private boolean authoriseCreditBAN()
        throws Exception
    {
        boolean success = false;
        try
        {
            String sql = "{? = call eban..authorise_credit_ban" +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement cstmt = transactionConn.prepareCall(sql);
            cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
            cstmt.setString(2, IMPLEMENTED);
            cstmt.setString(3, banSummary);
            cstmt.setString(4, banRaisedBy + userFirstname); //??
            cstmt.setDate(5, new java.sql.Date((new Date()).getTime()));
            cstmt.setString(6, gsr);
            cstmt.setDouble(7, chargeAmount);
            cstmt.setString(8, null);
            cstmt.setDate(9, new java.sql.Date(billingDate.getTime())); //??
            cstmt.setString(10, chargeDescription);
            cstmt.setString(11, "0"); //number of splits
            cstmt.setString(12, siteId);
            cstmt.setString(13, null);
            cstmt.setString(14, null);
            cstmt.setDouble(15, 100); //split1 - 100
            cstmt.setString(16, null);
            cstmt.setString(17, null);
            cstmt.setString(18, currencyCode);
            cstmt.setString(19, revenueType);
            cstmt.setString(20, "0");
            cstmt.setString(21, "1");
            cstmt.setString(22, "4");
            cstmt.setString(23, gcId);
            cstmt.setString(24, CREATE);
            cstmt.setString(25, CW);
            cstmt.setString(26, c2c3QRef);
            cstmt.setString(27, null);
            cstmt.setDate(28, periodStartDate==null?null:new java.sql.Date(periodStartDate.getTime())); //??
            cstmt.setDate(29, periodEndDate==null?null:new java.sql.Date(periodEndDate.getTime())); //??
            cstmt.setString(30, customerDetails);
            cstmt.setString(31, prReference);
            cstmt.execute();
            success = cstmt.getString(1).equals("0");
        }
        catch (Exception ex)
        {
            writeToLogFile("Error in authoriseCreditBAN(): " + ex.getMessage(), true);
        }
        finally
        {
            return success;
        }
    }

    private boolean updateChargeBAN()
        throws Exception
    {
        boolean success = false;
        try
        {
            //String sql = "{? = call eban..update_charge_ban_no_tran" +
            String sql = "{? = call eban..update_charge_ban" +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement cstmt = transactionConn.prepareCall(sql);
            cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
            cstmt.setString(2, IMPLEMENTED);
            cstmt.setString(3, banSummary);
            cstmt.setString(4, banRaisedBy + userFirstname); //??
            cstmt.setDate(5, new java.sql.Date((new Date()).getTime()));
            cstmt.setString(6, gsr);
            cstmt.setDouble(7, chargeAmount);
            cstmt.setString(8, null);
            cstmt.setDate(9, fromChargeValidDate); //??
            cstmt.setDate(10, new java.sql.Date(billingDate.getTime())); //?? end date
            cstmt.setString(11, chargeDescription);
            cstmt.setString(12, "0"); //number of splits
            cstmt.setString(13, siteId);
            cstmt.setString(14, null);
            cstmt.setString(15, null);
            cstmt.setDouble(16, 100); //split1 - 100
            cstmt.setString(17, null);
            cstmt.setString(18, null);
            cstmt.setString(19, chargeFrequency);
            cstmt.setString(20, currencyCode);
            cstmt.setString(21, revenueType);
            cstmt.setString(22, null);
            cstmt.setString(23, null);
            cstmt.setString(24, null);
            cstmt.setLong(25, unitQuantity);
            cstmt.setString(26, gcId);
            cstmt.setString(27, AMEND);
            cstmt.setString(28, CW);
            cstmt.setLong(29, chargeId); //??
            cstmt.setString(30, "N");
            cstmt.setString(31, c2Ref);
            cstmt.setString(32, c2c3QRef);
            cstmt.setString(33, customerDetails);
            cstmt.execute();
            success = cstmt.getString(1).equals("0");
        }
        catch (Exception ex)
        {
            writeToLogFile("Error in updateChargeBAN(): " + ex.getMessage(), true);
        }
        finally
        {
            return success;
        }
    }

    private void extractCustomerDetails(Cell thisCell) {
        customerDetails = null;
        try {
            if (thisCell != null) {
                if (thisCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    customerDetails = Long.toString((long)thisCell.getNumericCellValue());
                } else if (thisCell.getCellType() == Cell.CELL_TYPE_STRING) {
                    customerDetails = thisCell.getStringCellValue();
                }
            }
        } catch (IllegalStateException ex) {
            
        }
            
    }
    
    private void determineAccountNo(Cell thisCell)
    {
        accountNo = null;
        StringBuffer sb = new StringBuffer();
        if (thisCell == null)
        {
            valid = false;   
            addErrorMessage("No Account Id specified");
        }
        else
        {
            try
            {
                sb.append(Long.toString((long)thisCell.getNumericCellValue()));
            }
            catch (IllegalStateException ex) 
            {
                try
                {
                    String cellValue = thisCell.getStringCellValue();
                    for (int i = 0; i < cellValue.length(); i++)
                    {
                        char cA[] = {cellValue.charAt(i)};  
                        if (StringUtil.isNumeric(new String(cA)))
                        {
                            sb.append(cellValue.charAt(i)); 
                        }    
                    }    
                    if (sb.length() == 0)
                    {
                        valid = false;   
                        errorMessages.add("Invalid Account Id.");
                    }    
                }
                catch (IllegalStateException ex2)
                {
                    valid = false;
                    addErrorMessage("Invalid Account Id.");
                }
            }    
        }
        if (sb.length() < 10)
        {
            accountNo = TENZEROES.substring(sb.length()) + sb.toString();    
        }    
    }
  
    private void getGCID()
    { 
        gcId = null;
        if (accountNo != null)
        {
            Connection conn = null;
            String sql = null;
            try
            {
                conn = dba.connectExt();
                sql = "select global_customer_id " +
                    "from givn_ref..invoice_region (nolock) " + 
                    "where account_id = '" + accountNo + "' ";
                Statement thisStmt = conn.createStatement();
                ResultSet rs = thisStmt.executeQuery(sql);
                if (rs.next())
                {
                    gcId = rs.getString(1);
                }   
                else
                {    
                    valid = false;
                    addErrorMessage("Account Id not found on GCB.");
                }   
            }
            catch (Exception ex)
            {
                valid = false;
                writeToLogFile("Error in getGCID(): " + ex.getMessage(), true);
            }   
            finally
            {
                dba.freeConnectExt(conn);
            }        
        }    
    }
    
    private void getUserDetails()
    { 
        Connection conn = null;
        String sql = null;
        try
        {
            conn = dba.connectExt();
            sql = "select v.user_firstname, v.logon_id, u.email_address " +
                "from givn..customer_access_view v (nolock), eban..eban_user u (nolock) " + 
                "where v.global_customer_id = '" + gcId + "' " +
                "and (v.logon_id like 'W%' or v.logon_id like'Y%') " +
                "and not v.logon_id like '%C' " +
                "and v.logon_id = u.login_id "    ;
            Statement thisStmt = conn.createStatement();
            ResultSet rs = thisStmt.executeQuery(sql);
            if (rs.next())
            {
                userFirstname = " - " + rs.getString(1);
                loginId = rs.getString(2);
                String eA = rs.getString(3);
                if ((eA != null) && (!emailAddresses.contains(eA)))
                {
                    emailAddresses.add(eA);
                }
            }   
            else
            {    
                userFirstname = "";
                loginId = "";
            }   
        }
        catch (Exception ex)
        {
            writeToLogFile("Error in getUserDetails(): " + ex.getMessage(), true);
        }   
        finally
        {
            dba.freeConnectExt(conn);
        }        
    }
    
    private void getChargeDetails()
    { 
        Connection conn = null;
        String sql = null;
        try
        {
            conn = dba.connectExt();
            sql = "select charge_id, from_charge_valid_date, " +
                "revenue_type_code, unit_quantity, c2_ref_no, charge_frequency " + 
                "from gcd..charge (nolock) " +
                "where global_srv_reference = '" + gsr + "' " +
                "and global_customer_id = '" + gcId + "' " +
                "and gross_amount = " + Double.toString(chargeAmount) + " " +
                "and site_id = '" + siteId + "' " +
                "and to_charge_valid_date is null ";
            
            Statement thisStmt = conn.createStatement();
            ResultSet rs = thisStmt.executeQuery(sql);
            if (rs.next())
            {
                chargeId = rs.getLong(1);
                fromChargeValidDate = rs.getDate(2);
                revenueType = rs.getString(3);
                unitQuantity = rs .getLong(4);
                c2Ref = rs.getString(5);
                chargeFrequency = rs.getString(6);
                if (rs.next()) //jdbc driver doesn't support rs.isLast()
                {
                    valid = false;
                    addErrorMessage("Cannot cease charge - multiple charges found for " +
                        "these details  - GSR = " + gsr + ", amount = " +
                        Double.toString(chargeAmount));
                }    
                else if (billingDate.before(fromChargeValidDate))
                {
                    valid = false;
                    addErrorMessage("Cease date cannot be before " +
                        "charge period start date.");
                }
            }   
            else
            {    
                valid = false;
                addErrorMessage("Cannot cease charge - no charge found for " +
                    "these details  - GSR = " + gsr + ", amount = " +
                    Double.toString(chargeAmount));
            }   
        }
        catch (Exception ex)
        {
            writeToLogFile("Error in getChargeDetails(): " + ex.getMessage(), true);
        }   
        finally
        {
            dba.freeConnectExt(conn);
        }        
    }
    
    private void validateGSR(Cell thisCell)
    { 
        circuitBillingStartDate = null;
        if (thisCell == null)
        {
            valid = false;
            addErrorMessage("No GSR specified.");
        }    
        else
        {
            try
            {
                String cellValue = thisCell.getStringCellValue();
                gsr = cellValue;
                if (su.hasNoValue(gsr)) {
                    valid = false;
                    addErrorMessage("No GSR specified.");
                } else {
                    Connection conn = null;
                    String sql = null;
                    try
                    {
                        conn = dba.connectExt();
                        sql = "select global_customer_id, billing_start_date " +
                            "from gcd..global_customer_billing (nolock) " + 
                            "where service_reference = '" + cellValue + "' " + 
                            "and billing_cessation_date is null";
                        Statement thisStmt = conn.createStatement();
                        ResultSet rs = thisStmt.executeQuery(sql);
                        if (rs.next()) 
                        {
                            circuitBillingStartDate = new Date(rs.getDate(2).getTime());
                            //java.sql.Date bsd = rs.getDate(2);
                            if ((gcId != null) && (!gcId.equals(rs.getString(1))))
                            {    
                                valid = false;
                                addErrorMessage("Global Customer Id from GSR does not match " +
                                        "Global Customer Id from Account Id.");
                            }    
                        }  
                        else
                        {
                            valid = false;
                            addErrorMessage("GSR does not exist on GCB or it has been ceased.");
                        }    
                    }
                    catch (Exception ex)
                    {
                        valid = false;
                        writeToLogFile("Error in validateGSR(): " + ex.getMessage(), true);
                    }   
                    finally
                    {
                        dba.freeConnectExt(conn);
                    }        
                }
            }
            catch (IllegalStateException ex)
            {
                valid = false;
                addErrorMessage("Invalid GSR specified.");
            }    
        }    
    }
    
    private void validateSiteId(Cell thisCell)
    { 
        if (thisCell == null)
        {
            valid = false;
            addErrorMessage("No Site Id specified.");
        }    
        else
        {
            try
            {
                siteId = thisCell.getStringCellValue();
                if (su.hasNoValue(siteId)) {
                    valid = false;
                    addErrorMessage("No Site Id specified.");
                } else {
                    Connection conn = null;
                    String sql = null;
                    try
                    {
                        conn = dba.connectExt();
                        sql = "select global_customer_id " +
                            "from givn_ref..site (nolock) " + 
                            "where site_id = '" + siteId + "' ";
                        Statement thisStmt = conn.createStatement();
                        ResultSet rs = thisStmt.executeQuery(sql);
                        if (rs.next())
                        {
                            if ((gcId != null) && (!gcId.equals(rs.getString(1))))
                            {    
                                valid = false;
                                addErrorMessage("Global Customer Id from Site Id does not match " +
                                        "Global Customer Id from Account Id.");
                            }    
                        }    
                        else
                        {
                            valid = false;
                            addErrorMessage("Site Id does not exist on GCB.");
                        }    
                    }
                    catch (Exception ex)
                    {
                        valid = false;
                        writeToLogFile("Error in validateSiteId(): " + ex.getMessage(), true);
                    }   
                    finally
                    {
                        dba.freeConnectExt(conn);
                    }        
                }
            }
            catch (IllegalStateException ex)
            {
                valid = false;
                addErrorMessage("Invalid site id specified.");
            }    
        }
    }    

    private void validateChargeType(Cell thisCell)
    { 
        boolean found = false;
        int i = 0;
        if (thisCell == null)
        {
            valid = false;
            addErrorMessage("No charge type specified.");
        }    
        else
        {
            try
            {
                String cellValue = thisCell.getStringCellValue();
                if (su.hasNoValue(cellValue)) {
                    valid = false;
                    addErrorMessage("No charge type specified.");
                } else {
                    for (i = 0; i < CHARGETYPES.length; i++)
                    {
                        if (cellValue.equals(CHARGETYPES[i]))
                        {
                            found = true;
                            switch(i)
                            {
                                case 0: 
                                    chargeFrequency = MONTHLY;
                                    chargeType = INSTALL;
                                    break;
                                case 1: 
                                    chargeFrequency = QUARTERLY;
                                    chargeType = INSTALL;
                                    break;
                                case 2: 
                                    chargeFrequency = ANNUAL;
                                    chargeType = INSTALL;
                                    break;
                                case 3:
                                    chargeFrequency = "tba";
                                    chargeType = CEASE;
                                    break;
                                case 4:    
                                    chargeFrequency = ONEOFF;
                                    chargeType = CHARGE;
                                    break;
                                default:    
                                    chargeFrequency = ONEOFF;
                                    chargeType = CREDIT;
                            }    
                            break;
                        }    
                    }
                    if (!found)
                    {
                        valid = false;
                        addErrorMessage("Invalid charge type specified.");
                    }
                }
            }
            catch (IllegalStateException ex)
            {
                valid = false;
                addErrorMessage("Invalid charge type specified.");
            }    
        }
    }
    
    private Date validateDate(Cell thisCell, String dateName)
    { 
        Date thisDate = null;
        if ((thisCell == null) && (dateName.equals("Billing date")))
        {
            valid = false;
            addErrorMessage(dateName + " - no date specified.");
        }    
        else
        {
            try
            {   
                if (thisCell != null)
                {
                    thisDate = thisCell.getDateCellValue();
                }
            }
            catch (IllegalStateException ex)
            {
//System.out.println("inv date: " + thisCell.getStringCellValue());         
                valid = false;
                addErrorMessage(dateName + " - invalid date format.");
            }    
        }
        return thisDate;
    }

    private void validateChargeAmount(Cell thisCell)
    { 
        if (thisCell == null)
        {
            valid = false;
            addErrorMessage("No charge amount specified.");
        }    
        else
        {
            try
            {    
                chargeAmount = thisCell.getNumericCellValue();
            }
            catch (IllegalStateException ex)
            {
                valid = false;
                addErrorMessage("Invalid charge amount.");
            }    
        }
    }

    private void validateCurrencyCode(Cell thisCell)
    { 
        if (thisCell == null)
        {
            valid = false;
            addErrorMessage("No currency code specified.");
        }    
        else
        {
            try
            {
                String cellValue = thisCell.getStringCellValue();
                if (su.hasNoValue(cellValue)) {
                    valid = false;
                    addErrorMessage("No currency code specified.");
                } else {
                    Connection conn = null;
                    String sql = null;
                    try
                    {
                        conn = dba.connectExt();
                        sql = "select currency_code " +
                            "from eban..country_currency " + 
                            "where currency_code = '" + cellValue + "' ";
                        Statement thisStmt = conn.createStatement();
                        ResultSet rs = thisStmt.executeQuery(sql);
                        if (rs.next())
                        {
                            currencyCode = cellValue;
                        }    
                        else
                        {
                            valid = false;
                            addErrorMessage("Invalid currency code.");
                        }    
                    }
                    catch (Exception ex)
                    {
                        valid = false;
                        writeToLogFile("Error in validateCurrencyCode(): " + ex.getMessage(), true);
                    }   
                    finally
                    {
                        dba.freeConnectExt(conn);
                    }        
                }
            }
            catch (IllegalStateException ex)
            {
                valid = false;
                addErrorMessage("Invalid currency code.");
            }    
        }
    }    

    private void validateRef(Cell thisCell)
    { 
        if (thisCell == null)
        {
            valid = false;
            addErrorMessage("No C2 / C3 or Q reference specified.");
        }    
        else
        {
            try
            {    
                c2c3QRef = thisCell.getStringCellValue().toUpperCase();
                if (su.hasNoValue(c2c3QRef)) {
                    valid = false;
                    addErrorMessage("No C2 / C3 or Q reference specified.");
                } /*else if ((!c2c3QRef.startsWith("C2")) && (!c2c3QRef.startsWith("C3")) &&
                    (!c2c3QRef.startsWith("Q")))
                {
                    valid = false;
                    addErrorMessage("Invalid C2 / C3 or Q reference.");
                }*/
            }
            catch (IllegalStateException ex)
            {
                valid = false;
                addErrorMessage("Invalid C2 / C3 or Q reference.");
            }    
        }
    }

    private void validateChargeDescription(Cell thisCell)
    { 
        if (thisCell == null)
        {
            valid = false;
            addErrorMessage("No charge description specified.");
        }    
        else
        {
            try
            {    
                chargeDescription = thisCell.getStringCellValue();
                if (su.hasNoValue(chargeDescription)) {
                    valid = false;
                    addErrorMessage("No charge description specified.");
                }
            }
            catch (IllegalStateException ex)
            {
                valid = false;
                addErrorMessage("Invalid charge description.");
            }    
        }
    }

    private void validateRevenueType(Cell thisCell)
    { 
        if (thisCell == null)
        {
            valid = false;
            addErrorMessage("No revenue type specified.");
        }    
        else
        {
            try
            {   
                boolean rTFound = false;
                String rT = thisCell.getStringCellValue();
                for (int i = 0; i < REVENUETYPES[0].length; i++)
                {
                    if (rT.equals(REVENUETYPES[0][i]))
                    {
                        rTFound = true;
                        revenueType = REVENUETYPES[1][i];
                        break;
                    }    
                }
                if (!rTFound)
                {
                    valid = false;
                    addErrorMessage("Invalid revenue type.");
                }    
            }
            catch (IllegalStateException ex)
            {
                valid = false;
                addErrorMessage("Invalid revenue type.");
            }    
        }
    }

    private void validateBANSummary(Cell thisCell)
    { 
        if (thisCell != null)
        {
            try
            {    
                banSummary = thisCell.getStringCellValue();
            }
            catch (IllegalStateException ex)
            {
                valid = false;
                addErrorMessage("Invalid BAN summary.");
            }    
        }
    }

    private void validateBANRaisedBy(Cell thisCell)
    { 
        if (thisCell == null)
        {
            valid = false;
            addErrorMessage("No BAN raised by specified.");
        }    
        else
        {
            try
            {    
                String bRB = thisCell.getStringCellValue();
                if (su.hasNoValue(bRB)) {
                    valid = false;
                    addErrorMessage("No BAN raised by specified.");
                } else {
                    banRaisedBy = su.hasNoValue(bRBPrefix)?bRB:bRBPrefix + " " +
                       bRB; 
                }
            }
            catch (IllegalStateException ex)
            {
                valid = false;
                addErrorMessage("Invalid BAN raised by.");
            }    
        }
    }

    private void validatePRReference(Cell thisCell)
    { 
        if (thisCell == null)
        {
            valid = false;
            addErrorMessage("No PR Reference specified for credit BAN.");
        }    
        else
        {
            try
            {    
                prReference = thisCell.getStringCellValue();
                if (su.hasNoValue(prReference)) {
                    valid = false;
                    addErrorMessage("No PR Reference specified for credit BAN.");
                } else if ((!prReference.startsWith("PR")) || 
                        (prReference.length() != 8) || 
                        (!StringUtil.isNumeric(prReference.substring(2)))) {
                    valid = false;
                    addErrorMessage("PR Reference must start with 'PR' " +
                            "followed by 6 numbers");
                }    
            }
            catch (IllegalStateException ex)
            {
                valid = false;
                addErrorMessage("Invalid PR Reference specified for credit BAN.");
            }    
        }
    }

    private void sendErrorEmail()
    { 
        File emailFile = null;
        BufferedWriter emailWriter = null;
        try
        {
            String emailTS = su.reformatDate("now", null, DATETIMEFORMAT);
            emailFile = new File(monitorDir + File.separator + "monitor-" +
                "readfromfile-" + emailTS + ".txt");
            emailWriter = new BufferedWriter(new FileWriter(emailFile));
            if (emailAddresses.isEmpty())
            {
                emailAddresses.add(gcbAdminAddress);
                errorMessages.add(0, "No email address found for invalid AutoBAN");
            }
            for (Iterator it = emailAddresses.iterator(); it.hasNext(); )
            {
                emailWriter.write("addr:" + (String)it.next());
                emailWriter.newLine();
            }
            emailWriter.write("title:" + (String)errorMessages.get(0));
            emailWriter.newLine();
            for (int i = 1; i < errorMessages.size(); i++)
            {
                emailWriter.write("message:" + (String)errorMessages.get(i));
                emailWriter.newLine();
            }    
        }
        catch (Exception ex)
        {
            writeToLogFile("Error creating email: " + ex.getMessage(), true);
            System.out.println("Error creating email: " + ex.getMessage());
            //System.exit(1);
        }
        finally
        {
            try
            {
                emailWriter.close();
            }
            catch (IOException iox)
            {
                writeToLogFile("Error closing emailWriter: " + iox.getMessage(), true);
                System.out.println("Error closing emailWriter: " + iox.getMessage());
            }    
        }
    }

    private void sendAdminEmail()
    { 
        if (adminEmailRequired)
        {
            adminEmailRequired = false;
            File emailFile = null;
            BufferedWriter emailWriter = null;
            try
            {
                String emailTS = su.reformatDate("now", null, DATETIMEFORMAT);
                emailFile = new File(monitorDir + File.separator + "monitor-" +
                    "readfromfile-a" + emailTS + ".txt");
                emailWriter = new BufferedWriter(new FileWriter(emailFile));
                emailWriter.write("addr:" + gcbAdminAddress);
                emailWriter.newLine();
                emailWriter.write("title: AutoBAN Technical Difficulties");
                emailWriter.newLine();
                for (int i = 0; i < adminMessages.size(); i++)
                {
                    emailWriter.write("message:" + (String)adminMessages.get(i));
                    emailWriter.newLine();
                }    
            }
            catch (Exception ex)
            {
                writeToLogFile("Error creating email: " + ex.getMessage(), false);
                System.out.println("Error creating email: " + ex.getMessage());
                //System.exit(1);
            }
            finally
            {
                try
                {
                    emailWriter.close();
                }
                catch (IOException iox)
                {
                    writeToLogFile("Error closing emailWriter: " + iox.getMessage(), false);
                    System.out.println("Error closing emailWriter: " + iox.getMessage());
                }    
            }
        }
    }

    private void validateBillingDate()
    { 
        if ((chargeType.equals(INSTALL)) && (circuitBillingStartDate != null) && 
            (circuitBillingStartDate.after(billingDate)))
        {
            valid = false;
            addErrorMessage("Start date cannot be before circuit billing start date.");
        }    
    }

    private void validatePeriodDates()
    { 
        if (((periodStartDate == null) && (periodEndDate != null)) ||
        ((periodStartDate != null) && (periodEndDate == null)))
        {
            valid = false;
            addErrorMessage("Charge period start date and charge period " +
                "end date must be entered together.");
        }
        else if (periodEndDate.before(periodStartDate))
        {
            valid = false;
            addErrorMessage("Charge period end date cannot be before " +
                "charge period start date.");
        }
    }//Double.toString(rowNo)

    private void addErrorMessage(String message)
    {
//System.out.println("error message: " + message);            
        errorMessages.add("Row " + Long.toString(rowNo) + " - " + message);
    }
    
    private void findTitleRow(Cell thisCell)
    {
        try
        {
            String cellValue = thisCell.getStringCellValue(); 
//System.out.println("0, cellValue: " + cellValue);            
       // System.out.println("cell type: " + Integer.toString(thisCell.getCellType()));            
            titleFound = cellValue.equals(LINENUMBER);
        }
        catch (Exception ex)
        {
//System.out.println("0, cellValue: " + ex.getMessage());            
        }
    }

    private boolean validateRow(Row row)
    {
/*        int count = 0;
for (Iterator it = row.cellIterator(); it.hasNext(); ) 
{
    Cell thisCell = (Cell)it.next();
    try
    {
        System.out.println(Integer.toString(count++) + " cell type: " + Integer.toString(thisCell.getCellType()));            
    }   
    catch (IllegalStateException ex)
    {
    }
    
} */   
        boolean hasContents = false;
        try
        {
            rowNo = (long)row.getCell(0).getNumericCellValue();
            if (rowNo > 0)
            {    
                hasContents = true;
/*if (debug) {
   System.out.println("1, rowNo: " + Double.toString(row.getCell(0).getNumericCellValue())); 
}*/            
//System.out.println("1, rowNo: " + Double.toString(row.getCell(0).getNumericCellValue()));            
                determineAccountNo(row.getCell(1));    
//System.out.println("2, accountNo: " + accountNo);            
                getGCID();    
//System.out.println("3, gcId:" + gcId);            
                validateGSR(row.getCell(3));    
//System.out.println("4");            
                validateSiteId(row.getCell(4));   
//System.out.println("5");            
                validateChargeType(row.getCell(6)); 
//System.out.println("6");            
                billingDate = validateDate(row.getCell(8), "Billing date");
//System.out.println("7");            
                validateBillingDate();
//System.out.println("8");            
                validateChargeAmount(row.getCell(9));
//System.out.println("9");            
                validateCurrencyCode(row.getCell(10));
//System.out.println("10");            
                validateRef(row.getCell(11));
//System.out.println("11");            
                validateChargeDescription(row.getCell(12));
//System.out.println("12");            
                validateRevenueType(row.getCell(13));
//System.out.println("13");            
                periodStartDate = validateDate(row.getCell(14), "Charge period start date");
//System.out.println("13a");            
                periodEndDate = validateDate(row.getCell(15), "Charge period end date");
//System.out.println("13b");            
                if ((chargeFrequency.equals(ONEOFF)) && 
                    ((periodStartDate != null) || (periodEndDate != null)))
                {
//System.out.println("13c");            
                    validatePeriodDates();
//System.out.println("13d");            
                }
//System.out.println("13e");            
                //customerDetails = row.getCell(16).getStringCellValue().isEmpty()
                  //  ?null:row.getCell(16).getStringCellValue();
                extractCustomerDetails(row.getCell(16));
//System.out.println("14");            
                validateBANSummary(row.getCell(17));
/*if (debug) {
   System.out.println("Raised By: " + row.getCell(18).getStringCellValue()); 
}*/            
//System.out.println("15");            
                validateBANRaisedBy(row.getCell(18));
//System.out.println(chargeType);            
                if (chargeType.equals(CREDIT))
                {
//System.out.println("1");            
                    validatePRReference(row.getCell(19));
//System.out.println("2");            
                } 
                else 
                {
//System.out.println("3");            
                    prReference = null;
//System.out.println("4");            
                }    
                if ((gcIdPrev.equals("")) || (!gcIdPrev.equals(gcId)))
                {
                    getUserDetails();
                }
                if (chargeType.equals(CEASE))
                {
                    getChargeDetails();
                }
            }
            /*else
            {
                valid = false;
            }*/
        }
        catch (IllegalStateException ex)
        {
            //valid = false;
            //errorMessages.add("Column A is not numeric.");
            /*try
            {
                String cellValue = row.getCell(0).getStringCellValue(); 
                titleFound = cellValue.equals(LINENUMBER);
            }
            catch (IllegalStateException ex2)
            {
                
            }*/
        }
        finally
        {
            return hasContents;
        }
    }
    
    private void processXLS()
    {
        try
        {
            createLogFile();
//System.out.println("a");            
            File xlsDir = new File(autoBANInputDir);
            File[] xlsArray = xlsDir.listFiles();
//System.out.println("b");            
            if (xlsArray != null)
            {
//System.out.println("c");            
                for (int i = 0; i < xlsArray.length; i++)
                {
//System.out.println("c" + i);            
                    fis = new FileInputStream(xlsArray[i]);
                    workbook = WorkbookFactory.create(fis); 
                    Sheet sheet = workbook.getSheetAt(0);
                    transactionConn = dba.connectExt();
                    transactionConn.setAutoCommit(false);
                    valid = true;
                    titleFound = false;
                    gcIdPrev = "";
                    emailAddresses = new ArrayList();
                    adminEmailRequired = false;
                    errorMessages = new ArrayList();
                    adminMessages = new ArrayList();
                    errorMessages.add("Errors found in BAN request " + xlsArray[i].getName());
                    for (Iterator rIt = sheet.rowIterator(); rIt.hasNext(); )
                    {
                        Row row = (Row)rIt.next();
//System.out.println("d, row num: " + Integer.toString(row.getRowNum()));            
                        if (row.getCell(0) != null)
                        {
                            if (!titleFound)
                            {
                                findTitleRow(row.getCell(0));
                            }  
                            else
                            {    
//System.out.println("e");            
                                if (validateRow(row))
                                {
//System.out.println("f");            
                                    if (valid)
                                    {
    //System.out.println("g");            
                                        if (chargeFrequency.equals(ONEOFF))
                                        {
    //System.out.println("h");            
                                            if(chargeType.equals(CHARGE))
                                            {
    //System.out.println("i");            
                                                valid = authoriseOneOffChargeBAN();
                                            }
                                            else
                                            {
    //System.out.println("j");            
                                                valid = authoriseCreditBAN();
                                            }
                                        }
                                        else
                                        {
    //System.out.println("k");            
                                            if (chargeType.equals(INSTALL))
                                            {
    //System.out.println("l");            
                                                valid = authoriseChargeBAN();
    //System.out.println("l-1");            
                                            }
                                            else
                                            {
    //System.out.println("m");            
                                                valid = updateChargeBAN();   
                                            }
                                        }
                                    }
                                }
                            }
                        }    
                        /*else if (titleFound)
                        {
                            break;
                        } */  
                    }
//System.out.println("n");
                    workbook = null;
                    fis.close();
                    fis = null;
                    if (valid)
                    {
//System.out.println("o");            
                        valid = insertBPRCQueue();
//System.out.println("p");            
                        if (valid)
                        {
                            transactionConn.commit();
                            //move to processed dir
    //System.out.println("q: " + autoBANProcessedDir);            
                            if (!xlsArray[i].renameTo(new File(autoBANProcessedDir + 
                                File.separator + runTS + "_" + xlsArray[i].getName())))
                            {
    //System.out.println("q1");            
                                writeToLogFile("Unable to move file " +
                                    xlsArray[i].getName() + " from " +
                                    autoBANInputDir + " to " + autoBANProcessedDir +
                                    ".  The file has been processed successfully.", true);
    //System.out.println("q2");            
                                writeToLogFile("Second attempt to move file.", true);
    //System.out.println("q3");            
                                if (xlsArray[i].renameTo(new File(autoBANProcessedDir + 
                                    File.separator + runTS + "_" + xlsArray[i].getName())))
                                {
    //System.out.println("q4");            
                                    writeToLogFile("Second attempt succeeded.", true);
    //System.out.println("q5");            
                                }
                                else
                                {
    //System.out.println("q6");            
                                    writeToLogFile("Second attempt failed.", true);
                                    valid = false;
    //System.out.println("q7");            
                                }    
                            }
    //System.out.println("q8");            
                        }
                    }
                    if (!valid)
                    {
//System.out.println("r");            
                        try
                        {
//System.out.println("s");            
                            transactionConn.rollback();
                        }
                        catch (Exception exc)
                        {
//System.out.println("t");            
                            writeToLogFile("Exception rolling back transaction: " +
                                exc.getMessage() + ".  This may be because " +
                                "there was nothing to rollback but it might " +
                                "be worth checking.", true);
                        }    
                                
//System.out.println("u");            
                        if (!xlsArray[i].renameTo(new File(autoBANRejectDir + 
                            File.separator + runTS + "_" + xlsArray[i].getName())))
                        {
                            writeToLogFile("Unable to move file " +
                                xlsArray[i].getName() + " from " +
                                autoBANInputDir + " to " + autoBANRejectDir +
                                ".", true);
                            writeToLogFile("Second attempt to move file.", true);
                            if (xlsArray[i].renameTo(new File(autoBANRejectDir + 
                                File.separator + runTS + "_" + xlsArray[i].getName())))
                            {
                                writeToLogFile("Second attempt succeeded.", true);
                            }
                            else
                            {
                                writeToLogFile("Second attempt failed.", true);
                            }    
                        }    
                        for (Iterator it = errorMessages.iterator(); it.hasNext(); )
                        {
//System.out.println("v");            
                            writeToLogFile((String)it.next(), false);
                        }    
//System.out.println("w");            
                        sendErrorEmail();
                    }
//System.out.println("x");            
                    transactionConn.setAutoCommit(true);
//System.out.println("y");            
                    dba.freeConnectExt(transactionConn);
                    sendAdminEmail();
                }
            }
        }
        catch (Exception ex)
        {
//System.out.println("z");            
            writeToLogFile(ex.getMessage(), true);
        }    
        finally
        {
            try
            {
                if (transactionConn != null)
                {    
                    transactionConn.setAutoCommit(true);
                    dba.freeConnectExt(transactionConn);
                }
                if (workbook != null)
                {
                    workbook = null;
                }
                if (fis != null)
                {
                    try
                    {    
                        fis.close();
                    }
                    catch (Exception ex3)
                    {
                        //do not report this
                    }
                    finally
                    {
                        fis = null;
                    }    
                }    
            }
            catch (Exception ex2)
            {
//System.out.println("f");            
                writeToLogFile(ex2.getMessage(), true);
            }
            finally
            {
//System.out.println("g");            
                sendAdminEmail();
                closeLogFile();
            }    
            
        }
    }        

}
