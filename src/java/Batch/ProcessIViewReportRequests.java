/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import DBUtilities.DBAccess;
import JavaUtil.EBANProperties;
import JavaUtil.StringUtil;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPReply;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPTransferType;
import java.io.*;
import java.sql.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Properties;
import java.util.Enumeration;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.Map;

//import java.nio.charset.Charset;
//import java.nio.ByteBuffer;
//import java.nio.CharBuffer;

/**
 *
 * @author taitchison
 */
public class ProcessIViewReportRequests {
    private DBAccess dba;
    private StringUtil su;
    private final String EXTRACT = "Extract";
    private final String DATETIMEFORMAT = "yyyyMMddHHmmssSSS";
    private String globalCustomerId;
    private String invoiceNo;
    private String accountNumber;
    private String accountName;
    private String currencyCode;
    private String taxPointDate; 
    private String periodFromDate;
    private String periodToDate;
    private String lastInvoiceNo;
    private String trialInd;
    private String reportName;
    private String reportType;
    private String reportFilename;
    private String outputFilename;
    private String outputFilePath;
    private String billingSource;
    private String sourceBillingSystem;
    //private String ftpBillingSystem;
//    private String fileInvoiceNo;
    private String billedProductId;
    private String ldFlag;
    private Double invoiceTotal;
    private Double invoiceNetAmount;
    private Double invoiceTaxAmount;
    private String storedProc;
    private String subType;
    private String fileExt;
    private String runTS;
    private final String iViewReportDir =
        EBANProperties.getEBANProperty("iView.reportDir");
    private final String iViewReportArchiveDir =
        EBANProperties.getEBANProperty("iView.reportArchiveDir");
    private final String iViewOutputDir =
        EBANProperties.getEBANProperty("iView.outputDir");
    private final String iViewArchiveDir =
        EBANProperties.getEBANProperty("iView.archiveDir");
    private final String iViewRejectDir =
        EBANProperties.getEBANProperty("iView.rejectDir");
    private final String trialOutputDir =
        EBANProperties.getEBANProperty("iView.trialOutputDir");
    private final String logDir =
        EBANProperties.getEBANProperty("iView.logDir");
    //private final String ftpServerName =
      //  EBANProperties.getEBANProperty("iView.ftpServerName", "ftp.ukdevoteam.com"); //"testserver";
    private String ftpServerName;
    private final Properties ftpServers =
        EBANProperties.getEBANProperties("iView.ftpServers");
    private final int ftpServerPort = Integer.parseInt(
        EBANProperties.getEBANProperty("iView.ftpServerPort", "21")); //"21";
    //private String ftpUser;
    //private String ftpPassword;
    private final String ftpUser =
        EBANProperties.getEBANProperty("iView.ftpUser", "danet"); //"B2BFTP";
    private final String ftpPassword =
        EBANProperties.getEBANProperty("iView.ftpPassword", "danetftp"); //"B2BFTP";
    /*private final String gcb1FtpUser =
        EBANProperties.getEBANProperty("iView.gcb1FtpUser", "danet"); //"B2BFTP";
    private final String gcb1FtpPassword =
        EBANProperties.getEBANProperty("iView.gcb1FtpPassword", "danetftp"); //"B2BFTP";
    private final String gcb5FtpUser =
        EBANProperties.getEBANProperty("iView.gcb5FtpUser", "danet"); //"B2BFTP";
    private final String gcb5FtpPassword =
        EBANProperties.getEBANProperty("iView.gcb5FtpPassword", "danetftp"); //"B2BFTP";
    private final String gcb7FtpUser =
        EBANProperties.getEBANProperty("iView.gcb7FtpUser", "danet"); //"B2BFTP";
    private final String gcb7FtpPassword =
        EBANProperties.getEBANProperty("iView.gcb7FtpPassword", "danetftp"); //"B2BFTP";*/
    private final String ftpPassive =
        EBANProperties.getEBANProperty("iView.ftpPassive", null); //not there
    private final String ftpSite =
        EBANProperties.getEBANProperty("iView.ftpSite", null); //not required - run server specific command
    private final String ftpType =
        EBANProperties.getEBANProperty("iView.ftpType", "ASCII"); //"ASCII"
    private final String ftpServerDir =
        EBANProperties.getEBANProperty("iView.ftpServerDir", "timA"); //"deFTP"
    private final String ftpSeparator =
        EBANProperties.getEBANProperty("iView.ftpSeparator", "/"); //"deFTP"
    private final boolean debug =
        EBANProperties.getEBANProperty("iView.debug", "N").equalsIgnoreCase("Y"); 
    //private final String ftpTempDir =
      //  EBANProperties.getEBANProperty("iView.ftpTempDir"); //"deCalls"
    private final int archiveAfterDays = Integer.parseInt(
        EBANProperties.getEBANProperty("iView.archiveAfterDays", "30"));
    private final int deleteAfterDays = Integer.parseInt(
        EBANProperties.getEBANProperty("iView.deleteAfterDays", "90"));
    private final int deleteRequestAfterDays = Integer.parseInt(
        EBANProperties.getEBANProperty("iView.deleteRequestAfterDays", "90"));
    private BufferedWriter controlWriter;
    private File controlFile;
    private BufferedWriter logWriter;
    private File logFile;
    private FTPClient ftp = null;
    private final String CONTROLREC = "Control";
    private final String REPORTREC = "Report"; 
    private final String PIPE = "|";
    private final String NOFTP = "NOFTP";
    private final static String ARCHIVETRIAL = "ARCHIVETRIAL";
    private final static String DELETETRIAL = "DELETETRIAL";
    private final static String DELETEREQUEST = "DELETEREQUEST";
    private final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
    private long controlRecordCount;
    private boolean localTrialOnly;
    private boolean noFTP;
    private String thisSourceBillingSystem;
    private String thisTrialInd;
//    private Map ftpUsers;
//    private Map ftpPasswords;


    public ProcessIViewReportRequests() {
        dba = new DBAccess();            
        su = new StringUtil();
        runTS = su.reformatDate("now", null, DATETIMEFORMAT);
        logFile = new File(logDir + File.separator + "ProcessIViewReportRequests_" +
            runTS + "_log.txt");
        controlFile = new File(iViewOutputDir + File.separator +
            "GCB_TRANSFER_CONTROL_FILE_" + runTS + ".TXT");
        noFTP = false;
        thisSourceBillingSystem = "";
        thisTrialInd = "";
/*        ftpUsers = new HashMap();
        ftpUsers.put("gcb1", gcb1FtpUser);
        ftpUsers.put("gcb5", gcb5FtpUser);
        ftpUsers.put("gcb7", gcb7FtpUser);
        ftpPasswords = new HashMap();
        ftpPasswords.put("gcb1", gcb1FtpPassword);
        ftpPasswords.put("gcb5", gcb5FtpPassword);
        ftpPasswords.put("gcb7", gcb7FtpPassword);*/
    }
    
    public ProcessIViewReportRequests(String[] args) {
        this();
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase(NOFTP)) {
                noFTP = true;
            }
        }
    }

    
    public static void main(String[] args) {
        ProcessIViewReportRequests pIRR = new ProcessIViewReportRequests(args);
        if ((args.length > 0) && (args[0].equalsIgnoreCase("FTPC"))){
            pIRR.createLogFile();
            pIRR.determineFTPServer();
            pIRR.closeFTP();
            pIRR.closeLogFile();
        } else if ((args.length > 0) && (args[0].equalsIgnoreCase("FTPT"))){
            pIRR.createLogFile();
            pIRR.determineFTPServer();
            pIRR.transferTestFile();
            pIRR.closeFTP();
            pIRR.closeLogFile();
        } else if ((args.length > 0) && (args[0].equalsIgnoreCase(ARCHIVETRIAL))){
            pIRR.archiveTrialRequests();
        } else if ((args.length > 0) && (args[0].equalsIgnoreCase(DELETETRIAL))){
            pIRR.deleteTrialReports();
        } else if ((args.length > 0) && (args[0].equalsIgnoreCase(DELETEREQUEST))){
            pIRR.deleteRequestFiles();
        } else {
            pIRR.processRequests();
        }
    }

    private void processControlFile() {
        closeControlFile();
        if (controlRecordCount > 0) {
            if (transferFile(controlFile, true)) {
                moveFile(controlFile, new File(iViewArchiveDir + File.separator + 
                    thisSourceBillingSystem.toLowerCase() + File.separator + 
                    (thisTrialInd.equals("Y")?"Trial":"Invoice") + File.separator + 
                    "Control" + File.separator +     
                    File.separator + controlFile.getName()));
            } else {
                moveFile(controlFile, new File(iViewRejectDir + File.separator + 
                    thisSourceBillingSystem.toLowerCase() + File.separator + 
                    (thisTrialInd.equals("Y")?"Trial":"Invoice") + File.separator + 
                    "Control" + File.separator +     
                    File.separator + controlFile.getName()));
            }
        }
    }
    
    private boolean transferFile(File fileToSend, boolean isControl) {
        boolean success = false;
        try {
            if (noFTP == true) {
                writeToLogFile("FTP step deactivated for this run - " + 
                    fileToSend.getName() + " not sent");
                success = true;
            } else {
                if ((ftp == null)/* || (!ftpBillingSystem.equalsIgnoreCase(sourceBillingSystem))*/) {
                    /*if ((ftp != null) &&(!ftpBillingSystem.equalsIgnoreCase(sourceBillingSystem))) {
                        closeFTP();
                    }
                    ftpBillingSystem = sourceBillingSystem.toLowerCase();
                    ftpUser = (String)ftpUsers.get(ftpBillingSystem);
                    ftpPassword = (String)ftpPasswords.get(ftpBillingSystem);*/
                    initFTP();
                }
                writeToLogFile("Transferring " + fileToSend.getName() +
                    " to " + ftpSeparator + ftpServerDir + ftpSeparator + 
                    thisSourceBillingSystem.toLowerCase() + ftpSeparator + 
                    (thisTrialInd.equals("Y")?"Trial":"Invoice") + ftpSeparator + 
                    (isControl?"Control":"Data"));            
                ftp.chdir(ftpSeparator + ftpServerDir + ftpSeparator + 
                    thisSourceBillingSystem.toLowerCase() + ftpSeparator + 
                    (thisTrialInd.equals("Y")?"Trial":"Invoice") + ftpSeparator + 
                    (isControl?"Control":"Data"));
                if (fileToSend.getName().endsWith(".zip")) {
                    ftp.setType(FTPTransferType.BINARY);
                } else {
                    ftp.setType("ASCII".equalsIgnoreCase(ftpType)? FTPTransferType.ASCII
                                                                : FTPTransferType.BINARY);
                }
if (debug) System.out.println("ftp.put " + fileToSend.getAbsolutePath());            
                ftp.put(fileToSend.getAbsolutePath(), fileToSend.getName());
                FTPReply ftpR = ftp.getLastValidReply();
                if (ftpR == null) {
                    writeToLogFile("No valid reply");
                } else {
                    writeToLogFile("FTP reply: " + ftpR.getReplyCode() +
                        " " + ftpR.getReplyText());
                    success = ftpR.getReplyCode().equals("226");
if (debug) System.out.println("success: " + success);            
                }
            }
        } catch (Exception ex) {
            writeToLogFile("Transfer failed for " + fileToSend.getName());
            writeToLogFile("Error in transferFile(): " + ex.getMessage());
if (debug) System.out.println("Error in transferFile(): " + ex.getMessage());            
        } finally {
            return success;
        }
    }

    private boolean transferTestFile() {
        boolean success = false;
        File fileToSend = null;
        try {
            fileToSend = new File(EBANProperties.getEBANProperty("iView.testFile"));
                if ((ftp == null)/* || (!ftpBillingSystem.equalsIgnoreCase(sourceBillingSystem))*/) {
                    /*if ((ftp != null) &&(!ftpBillingSystem.equalsIgnoreCase(sourceBillingSystem))) {
                        closeFTP();
                    }
                    ftpBillingSystem = sourceBillingSystem.toLowerCase();
                    ftpUser = (String)ftpUsers.get(ftpBillingSystem);
                    ftpPassword = (String)ftpPasswords.get(ftpBillingSystem);*/
                    initFTP();
                }
                writeToLogFile("Transferring " + fileToSend.getName());
                if (fileToSend.getName().endsWith(".zip")) {
                    ftp.setType(FTPTransferType.BINARY);
                } else {
                    ftp.setType("ASCII".equalsIgnoreCase(ftpType)? FTPTransferType.ASCII
                                                                : FTPTransferType.BINARY);
                }
if (debug) System.out.println("ftp.put " + fileToSend.getAbsolutePath());            
                ftp.put(fileToSend.getAbsolutePath(), fileToSend.getName());
                FTPReply ftpR = ftp.getLastValidReply();
                if (ftpR == null) {
                    writeToLogFile("No valid reply");
                } else {
                    writeToLogFile("FTP reply: " + ftpR.getReplyCode() +
                        " " + ftpR.getReplyText());
                    success = ftpR.getReplyCode().equals("226");
if (debug) System.out.println("success: " + success);            
                }
        } catch (Exception ex) {
            writeToLogFile("Transfer failed for " + fileToSend.getName());
            writeToLogFile("Error in transferFile(): " + ex.getMessage());
if (debug) System.out.println("Error in transferFile(): " + ex.getMessage());            
        } finally {
            return success;
        }
    }

    private boolean moveFile(File fileToSend, File destinationFile) {
        boolean success = false;
        try {
            if (fileToSend.renameTo(destinationFile)) {
                success = true;
            } else {
                writeToLogFile("Unable to move file " + 
                    fileToSend.getAbsolutePath() + " to " +
                    destinationFile.getAbsolutePath());
            }
        } catch (Exception ex) {
            writeToLogFile("Error in moveFile((): " + ex.getMessage());
        } finally {
            return success;
        }
    }

    private boolean processFile() {
        boolean success = false;
        File outputFile = new File(outputFilePath);
        /*if (localTrialOnly) {
         * File trialFile = new File(trialOutputDir + File.separator + 
            outputFile.getName());
            * success = moveFile(outputFile, trialFile);
            * } else {
            */
        File archiveFile = new File(iViewArchiveDir + File.separator + 
            sourceBillingSystem.toLowerCase() + File.separator + 
            (trialInd.equals("Y")?"Trial":"Invoice") + File.separator + 
            "Data" + File.separator + outputFile.getName());
        //success = transferFile(outputFile, false)&& writeControlRecord() &&
          //  moveFile(outputFile, archiveFile);
        if (transferFile(outputFile, false)) {
            if (writeControlRecord()) {
                success = moveFile(outputFile, archiveFile);
            }
        }
        //}
        return success;
    }

    private boolean writeControlRecord() {
        boolean success = false;
        try {
            if (!invoiceNo.equals(lastInvoiceNo)) {
                lastInvoiceNo = invoiceNo;
                writeToControlFile(CONTROLREC + PIPE + globalCustomerId + PIPE + 
                    invoiceNo + PIPE + accountNumber + PIPE + accountName + 
                    PIPE + billingSource + PIPE + currencyCode + PIPE + 
                    taxPointDate + PIPE + periodFromDate + PIPE + periodToDate + 
                    PIPE + invoiceTotal + PIPE + invoiceNetAmount + PIPE + 
                    invoiceTaxAmount  + PIPE + trialInd);                                     
            }
            writeToControlFile(REPORTREC + PIPE + outputFilename + PIPE + 
                reportName);
            success = true;
        } catch (Exception ex) {
            writeToLogFile("Error in writeControlRecord(): " + ex.getMessage());
        } finally {
            return success;
        }
        
    }
    
    private boolean initFTP() {
        boolean success = false;
        try {
            writeToLogFile("Opening FTP connection: " + ftpServerName + ", " +
                ftpServerPort);
if (debug) System.out.println("ftp = new FTPClient(ftpServerName, ftpServerPort)");            
            ftp = new FTPClient(ftpServerName, ftpServerPort);
if (debug) System.out.println("ftp.login(ftpUser, ftpPassword)");            
            ftp.login(ftpUser, ftpPassword);
            FTPReply ftpR = ftp.getLastValidReply();
            if (ftpR == null) {
                writeToLogFile("No valid reply");
                return false;
            } else if (!ftpR.getReplyCode().equals("230")) {
if (debug) System.out.println("ftpR.getReplyCode(): " + ftpR.getReplyCode());            
                writeToLogFile("No valid reply");
                return false;
            }
            if (ftpPassive != null) {
if (debug) System.out.println("ftp.setConnectMode(FTPConnectMode.PASV)");            
                ftp.setConnectMode(FTPConnectMode.PASV);
            }
            if (ftpSite != null) {
if (debug) System.out.println("ftp.site(ftpSite)");            
                ftp.site(ftpSite);
            }
            if (ftpType != null) {
if (debug) System.out.println("ftp.setType");            
                ftp.setType("ASCII".equalsIgnoreCase(ftpType)? FTPTransferType.ASCII
                                                            : FTPTransferType.BINARY);
            }
            if (ftpServerDir != null) {
if (debug) System.out.println("ftp.chdir(ftpSeparator + ftpServerDir)");            
                ftp.chdir(ftpSeparator + ftpServerDir);
            }
            writeToLogFile("FTP connection established");
            success = true;
        } catch (Exception ex) {
            writeToLogFile("Exception in initFTP(): '" + ex.getMessage() );
            if (ftp != null) {
                closeFTP();
            }
        }
        return success;
    }

    private boolean determineFTPServer() {
        boolean success = false;
        /*ftpUser = gcb1FtpUser;
        ftpPassword = gcb1FtpPassword;
        ftpBillingSystem = "gcb1";*/
        Enumeration e = ftpServers.elements();
        while (e.hasMoreElements()) {
            String thisServer = (String)e.nextElement();
            if (establishFTP(thisServer)) {
                ftpServerName = thisServer;
                success = true;
                break;
            }
        }
        return success;
    }
    
    private boolean establishFTP(String ftpServerName) {
        boolean success = false;
        try {
            writeToLogFile("Opening FTP connection " + ftpServerName + ", " +
                ftpServerPort);
if (debug) System.out.println("ftp = new FTPClient(ftpServerName, ftpServerPort)");            
            ftp = new FTPClient(ftpServerName, ftpServerPort);
if (debug) System.out.println("ftp.login(ftpUser, ftpPassword)");            
            ftp.login(ftpUser, ftpPassword);
            FTPReply ftpR = ftp.getLastValidReply();
            if (ftpR == null) {
                writeToLogFile("No valid reply");
                return false;
            } else if (!ftpR.getReplyCode().equals("230")) {
                writeToLogFile("Reply code: " + ftpR.getReplyCode());
                return false;
            }
            if (ftpSite != null) {
if (debug) System.out.println("ftp.site(ftpSite)");            
                ftp.site(ftpSite);
            }
            if (ftpType != null) {
if (debug) System.out.println("ftp.setType");            
                ftp.setType("ASCII".equalsIgnoreCase(ftpType)? FTPTransferType.ASCII
                                                            : FTPTransferType.BINARY);
            }
            /*if (ftpServerDir != null) {
if (debug) System.out.println("ftp.chdir(ftpSeparator + ftpServerDir)");            
                ftp.chdir(ftpSeparator + ftpServerDir);
            }*/
            writeToLogFile("FTP connection established");
            success = true;
        } catch (Exception ex) {
            writeToLogFile("Exception in establishFTP(): '" + ex.getMessage() );
            if (ftp != null) {
                closeFTP();
            }
        }
        return success;
    }

    private void closeFTP() {
        if (ftp != null) {
            try {
                ftp.quit();
                writeToLogFile("Closed FTP connection");
            } catch (Exception ex) {
                writeToLogFile("Exception in closeFTP(): '" + ex.getMessage());
            }
            ftp = null;
        }
    }
    
    private void tidyUp() {
        try {
            //control file should not still be open - close it, log this
            //and move to reject
            if (controlWriter != null) {
                writeToLogFile("Error: control file was still open after " +
                    "processing - moving to " + iViewRejectDir);
                controlWriter.close();
                controlWriter = null;
                moveFile(controlFile, new File(iViewRejectDir + File.separator +
                    controlFile.getName()));
            }
            //there shouldn't be any files left in the output dir
            //if there are move them to reject and log each one
            File outDir = new File (iViewOutputDir);
            File[] filesLeft = outDir.listFiles();
            for (int i = 0; i < filesLeft.length; i++) {
                writeToLogFile("Error: file " + filesLeft[i].getName() + 
                " was still in output directory " + iViewOutputDir + 
                " after processing - moving to " + iViewRejectDir);
                moveFile(filesLeft[i], new File(iViewRejectDir + File.separator +
                    filesLeft[i].getName()));
            }
        } catch (Exception ex) {
            writeToLogFile("Error in tidyUp(): " + ex.getMessage());
        }
    }
    
    private void processRequests() {
        Connection conn = null;
        PreparedStatement thisStmt = null;
        String sql = null;
        controlRecordCount = 0;
        long succeeded = 0;
        long failed = 0;
        lastInvoiceNo = "";
        try {
if (debug) System.out.println("createLogFile()");            
            createLogFile();
//if (debug) System.out.println("createControlFile()");            
  //          createControlFile();
            //determine active ftp server
        //get all report requests
    if (debug) System.out.println("conn = dba.connectExt()");        
            conn = dba.connectExt();
            sql = "exec givn_ref..Get_Report_Requests ";
                //Statement thisStmt = conn.createStatement();
    if (debug) System.out.println("thisStmt = conn.prepareStatement(sql)");            
            thisStmt = conn.prepareStatement(sql);
                //ResultSet rs = thisStmt.executeQuery(sql);
    if (debug) System.out.println("boolean hasResults = thisStmt.execute()");            
            boolean hasResults = thisStmt.execute();
    if (debug) System.out.println("hasResults = thisStmt.getMoreResults()");            
            hasResults = thisStmt.getMoreResults();
    if (debug) System.out.println("ResultSet rs = thisStmt.getResultSet()");            
            ResultSet rs = thisStmt.getResultSet();
            if (rs.isBeforeFirst()) {
                if (determineFTPServer()) {
                //for each request
    if (debug) System.out.println("while (rs.next())");            
                    while (rs.next()) {
                        // get relevant data from current report request
                        long reportRequestId = rs.getInt("Report_Request_Id");
                        globalCustomerId = rs.getString("Global_Customer_Id").trim();
                        invoiceNo = rs.getString("Invoice_No").trim();
                        accountNumber = rs.getString("Account_Number").trim();
                        accountName = rs.getString("Account_Name").trim();
                        trialInd = rs.getString("Trial_Ind").trim();
                        reportName = rs.getString("Report_Name").trim();
                        reportType = rs.getString("Report_Type").trim();
                        reportFilename = rs.getString("Report_Filename").trim();
                        currencyCode = rs.getString("ISO_Currency_Code").trim();
                        taxPointDate = rs.getString("Tax_Point_Date").trim();
                        periodFromDate = rs.getString("Period_From_Date").trim();
                        periodToDate = rs.getString("Period_To_Date").trim();
                        billingSource = rs.getString("Billing_Source").trim();
                        //String fileInvoiceNo = rs(invoiceNo,billingSource);
                        billedProductId = rs.getString("Billed_Product_Id");
                        ldFlag = rs.getString("LD_Flag");
                        invoiceTotal = rs.getDouble("Invoice_Total");
                        sourceBillingSystem = rs.getString("Source_Billing_System");
                        storedProc = rs.getString("Stored_Proc");
                        subType = rs.getString("Sub_Type");
                        fileExt = rs.getString("File_Extension");
                        invoiceNetAmount = rs.getDouble("Net_Total");
                        invoiceTaxAmount = rs.getDouble("Tax_Total");
        if (debug) System.out.println("Processing report request: " + globalCustomerId + 
            ", " + invoiceNo + ", " + reportName);            
                        determineOutputFileName();
                        if ((!thisSourceBillingSystem.equals(sourceBillingSystem)) ||
                            (!thisTrialInd.equals(trialInd))) {
                            if ((!thisSourceBillingSystem.equals("")) &&
                                (!thisTrialInd.equals(""))) {
                                processControlFile();
                            }
                            thisSourceBillingSystem = sourceBillingSystem;
                            thisTrialInd = trialInd;
                            lastInvoiceNo = "";
                            createControlFile();
                        }
                        boolean success = false;
                        localTrialOnly = trialInd.equalsIgnoreCase("L");
                        if (localTrialOnly) {
                            //success = getLocalReport();
                        } else {
                            //report or extract?
                            if (reportType.equalsIgnoreCase(EXTRACT)) {
                                //if extract get data and zip up
        if (debug) System.out.println("getReport()");            
                                success = getReport();
                            } else {
                                //if report get xml
        if (debug) System.out.println("getXML()");            
                                success = getXML();
                            }
                        }
        if (debug) System.out.println("processFile()");            
                        if ((success) && processFile()) {
        if (debug) System.out.println("succeeded");
                            succeeded++;
                            if (trialInd.equals("Y")) {
                                deleteExistingTrialReport();
                            }
                        } else {
        if (debug) System.out.println("failed");
                            failed++;
                            if (!updateReportRequestStatus(reportRequestId, "Pending",
                                "Failed")) {
                                writeToLogFile("Unable to update Request_Status of " + 
                                    "Report_Request_Id " + reportRequestId + " to 'Failed'");
                            }
                            moveFile(new File(outputFilePath), new File(iViewRejectDir +
                                File.separator + outputFilename));
                        }
                //next request
                    }
        //if (debug) System.out.println("processControlFile()");
                    //processControlFile();
                    if ((!thisSourceBillingSystem.equals("")) &&
                        (!thisTrialInd.equals(""))) {
                        processControlFile();
                    }
                    long total = succeeded + failed;
                    if (total > 0) {
                        writeToLogFile("Requests processed: " + total + ", OK: " + 
                            succeeded + ", FAILED: " + failed);
                    } else {
                        writeToLogFile("No requests processed"); 
                    }
                    //update status of all successful requests to "Completed" 
                    if (!updateReportRequestStatus(-1, "Pending", "Completed")) {
                        writeToLogFile("Unable to update Request_Status from " +
                            "'Pending' to 'Completed'");
                    }
                } else {
                    writeToLogFile("Unable to establish ftp connection - terminating process"); 
                }
            } else {
                writeToLogFile("No requests to process"); 
            }
        } catch (Exception ex) {
            writeToLogFile("Error in processRequests(): " + ex.getMessage());
        } finally {
            try {
                //tidyUp();
                closeFTP();
                if (conn != null) {
                    thisStmt.close();
                    dba.freeConnectExt(conn);
                }
            } catch (Exception ex2) {
                writeToLogFile("Error in processRequests(): " + ex2.getMessage());
            } finally {
                closeLogFile();
            }
        }
    }
    
    private void archiveTrialRequests() {
        Connection thisConn = null;
        CallableStatement cstmt = null;
        String sql = null;
        try {
            createLogFile();
            writeToLogFile("Archiving all trial requests and output files older than " +
                archiveAfterDays + " days");
            thisConn = dba.connectExt();
            sql = "{? = call givn_ref..Archive_Trial_Report_Requests (?)}";
            cstmt = thisConn.prepareCall(sql);
            cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
            cstmt.setInt(2, archiveAfterDays);
            cstmt.execute();
            long retCode = cstmt.getLong(1);
            if (retCode == 0) {
                writeToLogFile("Request archive process has completed");
            } else {
                writeToLogFile("Error in givn_ref..Archive_Report_Requests, return code: " + 
                    retCode);
            }
            
            Calendar archiveCal = new GregorianCalendar();
            archiveCal.add(Calendar.DATE, archiveAfterDays*-1);
            Date archiveDate = new Date(archiveCal.getTimeInMillis());
//System.out.println("archiveDate: " + archiveDate.toString());
            File reportDir = new File(iViewReportDir);
            File[] listOfFiles = reportDir.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    Date fileDate = new Date(file.lastModified());
//System.out.println(file.getName() + ", fileDate: " + fileDate.toString());
                    if (fileDate.before(archiveDate)) {
                        int dot = file.getName().lastIndexOf(".");
                        String archiveFileName = file.getName().substring(0, dot) +
                            "_" + sdf.format(fileDate) + file.getName().substring(dot);
                        if (!file.renameTo(new File(iViewReportArchiveDir + 
                            File.separator + archiveFileName))) {
                            writeToLogFile("Cannot move file " + file.getName() +
                                " to archive directory " + iViewReportArchiveDir);
                        }
                    }
                }
            }
            writeToLogFile("File archive process has completed" );
        } catch (Exception ex) {
            writeToLogFile("Error in archiveTrialRequests(): " + ex.getMessage());
        } finally {
            try {
                cstmt.close();
                dba.freeConnectExt(thisConn);
            } catch (Exception ex2) {
                writeToLogFile("Error in archiveTrialRequests(): " + ex2.getMessage());
            } finally {
                closeLogFile();
            }    
        }
    }
    
    private void deleteExistingTrialReport() {
        try {
            File existingReport = new File(iViewReportDir + File.separator + 
                    reportFilename);
            if (existingReport.exists()) {
                existingReport.delete();
                writeToLogFile("Previous version of report " + reportFilename +
                        " deleted");
            }
        } catch (Exception ex) {
            writeToLogFile("Error in deleteExistingTrialReport(): " + ex.getMessage());
        }
    }
    
    
    private void deleteTrialReports() {
        try {
            createLogFile();
            writeToLogFile("Deleting all trial output files older than " +
                deleteAfterDays + " days");
            Calendar deleteCal = new GregorianCalendar();
            deleteCal.add(Calendar.DATE, deleteAfterDays*-1);
            Date deleteDate = new Date(deleteCal.getTimeInMillis());
//System.out.println("archiveDate: " + archiveDate.toString());
            File archiveDir = new File(iViewReportArchiveDir);
            File[] listOfFiles = archiveDir.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    Date fileDate = new Date(file.lastModified());
//System.out.println(file.getName() + ", fileDate: " + fileDate.toString());
                    if (fileDate.before(deleteDate)) {
                        if (!file.delete()) {
                            writeToLogFile("Cannot delete file " + file.getName() +
                                " from archive directory " + iViewReportArchiveDir);
                        }
                    }
                }
            }
            writeToLogFile("File deletion process has completed");
        } catch (Exception ex) {
            writeToLogFile("Error in deleteTrialReports(): " + ex.getMessage());
        } finally {
            closeLogFile();
        }
    }
    
    private void deleteRequestFiles() {
        String[] sources = {"gcb1", "gcb5", "gcb7"}; 
        String[] reqTypes = {"Invoice", "Trial"}; 
        String[] fileTypes = {"Control", "Data"}; 
        try {
            createLogFile();
            writeToLogFile("Deleting all request files older than " +
                deleteRequestAfterDays + " days");
            Calendar deleteCal = new GregorianCalendar();
            deleteCal.add(Calendar.DATE, deleteRequestAfterDays*-1);
            Date deleteDate = new Date(deleteCal.getTimeInMillis());
//System.out.println("archiveDate: " + archiveDate.toString());
            for (int i = 0; i < sources.length; i++) {
                for (int j = 0; j < reqTypes.length; j++) {
                    for (int k = 0; k < fileTypes.length; k++) {
                        File archiveDir = new File(iViewArchiveDir +
                            File.separator + sources[i] + File.separator +
                            reqTypes[j] + File.separator + fileTypes[k]);
//System.out.println("archiveDir: " + archiveDir.getPath());
                        if (archiveDir.isDirectory()) {
                            File[] listOfFiles = archiveDir.listFiles();
                            for (File file : listOfFiles) {
                                if (file.isFile()) {
                                    Date fileDate = new Date(file.lastModified());
                                    if (fileDate.before(deleteDate)) {
                                        if (!file.delete()) {
                                            writeToLogFile("Cannot delete file " + file.getName() +
                                                " from directory " + iViewReportArchiveDir);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
                
            writeToLogFile("File deletion process has completed");
        } catch (Exception ex) {
            writeToLogFile("Error in deleteTrialReports(): " + ex.getMessage());
        } finally {
            closeLogFile();
        }
    }
    
    private boolean getReport() {
        boolean success = false;
        Connection thisConn = null;
        PreparedStatement thisStmt = null;
        String sql = null;
        try {
            thisConn = dba.connectExt();
            sql = "exec " + storedProc + " '" + globalCustomerId + "', '" +
                invoiceNo + "' ";
            thisStmt = thisConn.prepareStatement(sql);
            boolean hasResults = thisStmt.execute();
            if (hasResults) {
                BufferedWriter bW = new BufferedWriter(new FileWriter(outputFilePath));
                while (hasResults) {
                    ResultSet rs = thisStmt.getResultSet();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int colCount = rsmd.getColumnCount();

                    while (rs.next()) {
                        StringBuffer sB = new StringBuffer();
                        for (int i = 1; i <= colCount; i++) {
                            sB.append(rs.getString(i));
                            sB.append(",");
                        }
                        sB.setLength(sB.length() - 1);
                        bW.write(sB.toString());
                        bW.newLine();
                    }
                    hasResults = thisStmt.getMoreResults();
                }
                bW.close();
                success = zipFile(outputFilePath);
            }
        } catch (Exception ex) {
            writeToLogFile("Error in getReport(): " + ex.getMessage());
        } finally {
            try {
                thisStmt.close();
                dba.freeConnectExt(thisConn);
            } catch (Exception ex2) {
                writeToLogFile("Error in getReport(): " + ex2.getMessage());
            } finally {
                return success;
            }    
        }  
    }
    
    private boolean zipFile(String filePath) {
        boolean success = false;
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        String zippedFile = filePath.substring(0, filePath.length()-3) + "zip";    
        try {
            // Create the ZIP file
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedFile));
            //String filePath = fileToZip;
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1);
            //System.out.println("fileName: "+fileName);
            String fileAttachment = filePath;
            // Compress the file
            FileInputStream in = new FileInputStream(fileAttachment);


            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(fileName));

            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Complete the entry
            out.closeEntry();
            in.close();
            File csvFile = new File(filePath);
            csvFile.delete();
            // Complete the ZIP file
            out.close();
            //assign zip file name to output file
            outputFilePath = zippedFile;
            outputFilename = new File(outputFilePath).getName();
            success = true;
        } catch (IOException ex) {
            writeToLogFile("Error in zipFile(): " + ex.getMessage());
        } finally {
            return success;
        }
    }
    
    private boolean getXML() {
        boolean success = false;
        Connection thisConn = null;
        Statement thisStmt = null;
        String sql = null;
        try {
            thisConn = dba.connectExt();
            sql = "exec " + storedProc + " '" + globalCustomerId + "', '" +
                invoiceNo + "' ";
            thisStmt = thisConn.createStatement();
            ResultSet rs = thisStmt.executeQuery(sql);
            //BufferedWriter bW = new BufferedWriter(new FileWriter(outputFilePath));
            Writer bW = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFilePath), "UTF8"));
            //bW.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
            //bW.write("<?xml version=\"1.0\" encoding=\"iso-8859-15\"?>");
            while (rs.next()) {
                String vX = rs.getString(1);
                bW.write(vX);
            }
            bW.close();
            /*FileOutputStream fos = new FileOutputStream(outputFilePath);
            Charset utf8charset = Charset.forName("UTF-8");
            //Charset iso88591charset = Charset.forName("ISO-8859-1");
            while (rs.next()) {
                byte[] vX = rs.getBytes(1);

                //ByteBuffer inputBuffer = ByteBuffer.wrap(new byte[]{(byte)0xC3, (byte)0xA2});
                ByteBuffer inputBuffer = ByteBuffer.wrap(vX);

                // decode UTF-8
                CharBuffer data = utf8charset.decode(inputBuffer);

                // encode ISO-8559-1
                //ByteBuffer outputBuffer = iso88591charset.encode(data);
                //byte[] outputData = outputBuffer.array();            
                fos.write(data);
            }
            fos.close();*/
            success = true;
        } catch (Exception ex) {
            writeToLogFile("Error in getXML(): " + ex.getMessage());
        } finally {
            try {
                thisStmt.close();
                dba.freeConnectExt(thisConn);
            } catch (Exception ex2) {
                writeToLogFile("Error in getXML(): " + ex2.getMessage());
            } finally {
                return success;
            }    
        }
    }
    
    private boolean updateReportRequestStatus(long reportRequestId, 
        String oldRequestStatus, String newRequestStatus) {
        boolean success = false;
        Connection thisConn = null;
        CallableStatement cstmt = null;
        String sql = null;
        try {
            thisConn = dba.connectExt();
            sql = "{? = call givn_ref..Update_Report_Request_Status (?,?,?)}";
            cstmt = thisConn.prepareCall(sql);
            cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
            cstmt.setLong(2, reportRequestId);
            cstmt.setString(3, oldRequestStatus);
            cstmt.setString(4, newRequestStatus); 
            cstmt.execute();
            success = cstmt.getLong(1) == 0;
        } catch (Exception ex) {
            writeToLogFile("Error in updateReportRequestStatus(): " + ex.getMessage());
        } finally {
            try {
                cstmt.close();
                dba.freeConnectExt(thisConn);
            } catch (Exception ex2) {
                writeToLogFile("Error in updateReportRequestStatus(): " + ex2.getMessage());
            } finally {
                return success;
            }    
        }
    }
    
    private void determineOutputFileName() {
        String mixedCase = globalCustomerId + "_" + invoiceNo + "_" + 
            sourceBillingSystem + "_" + 
            (trialInd.equalsIgnoreCase("Y")?"Trial":"Close") + "_" + 
            su.reformatDate("now", null, DATETIMEFORMAT) + "_" + subType + "." +
            fileExt;
        outputFilename = mixedCase.toUpperCase();
        outputFilePath = iViewOutputDir + File.separator + 
            sourceBillingSystem.toLowerCase() + File.separator + 
            (trialInd.equals("Y")?"Trial":"Invoice") + File.separator + 
            "Data" + File.separator + outputFilename;    
         
    }
    
    private void createControlFile()
    {
        controlFile = new File(iViewOutputDir + File.separator + 
            sourceBillingSystem.toLowerCase() + File.separator + 
            (trialInd.equals("Y")?"Trial":"Invoice") + File.separator + 
            "Control" + File.separator +     
            "GCB_TRANSFER_CONTROL_FILE_" + runTS + ".TXT");
        try
        {
            controlWriter = new BufferedWriter(new FileWriter(controlFile));
        }
        catch (Exception ex)
        {
            writeToLogFile("Error creating control file: " + ex.getMessage());
        }
    }

    private void writeToControlFile(String message)
    {
        try
        {
            if (message != null)
            {
                controlWriter.write(message);
                controlWriter.newLine();
                controlRecordCount++;
            }
        }
        catch (Exception ex)
        {
            writeToLogFile("Error writing message '" + message +
            "' to control file: " + ex.getMessage());
        }
    }

    private void closeControlFile()
    {
        if (controlWriter == null) {
                writeToLogFile("Error closing control file: controlWriter is null");
        } else {
            try {
                controlWriter.close();
                controlWriter = null;
            } catch (Exception ex) {
                writeToLogFile("Error closing control file: " + ex.getMessage());
            }
        }
    }

    private void createLogFile()
    {
        try
        {
            logWriter = new BufferedWriter(new FileWriter(logFile));
            String now = su.reformatDate("now", null, DATETIMEFORMAT);
            writeToLogFile("Report request processing started at " +
            now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
            now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
            now.substring(4, 6) + "/" + now.substring(0, 4));
        }
        catch (Exception ex)
        {
            System.out.println("Error creating log file: " + ex.getMessage());
            System.exit(1);
        }
    }

    private void writeToLogFile(String message)
    {
        try
        {
            if (message != null)
            {
                if (debug) {
                    System.out.println(message);
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
        if (logWriter == null) {
                System.out.println("Error closing log file: logWriter is null");
                System.exit(1);
        } else {
            try {
                String now = su.reformatDate("now", null, DATETIMEFORMAT);
                writeToLogFile("Report request processing ended at " +
                now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
                now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
                now.substring(4, 6) + "/" + now.substring(0, 4));
                logWriter.close();
            } catch (Exception ex) {
                System.out.println("Error closing log file: " + ex.getMessage());
                System.exit(1);
            }
        }
    }
}
