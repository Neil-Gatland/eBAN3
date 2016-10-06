package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;
import java.util.zip.*;

public class masterAccountInvoicing
{

  // class variables
  private DBAccess dba;
  private StringUtil su;
  private String logDir, runTS, logFilename, message, completionMessage;
  private BufferedWriter logWriter;
  private String callMonth;
  private boolean ok;
  private long processSeqNo;
  // class constants
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String PROCESSNAME = "Master Account Invoicing";
  private final String YES = "Y";
  private final String NO = "N";
  private final String UPDATEDBY = "masterAccountInvoicing";
  private final String PROCESSPREFIX = "Invoicing";

  private masterAccountInvoicing()
  {
    dba = new DBAccess();
    su = new StringUtil();
    logDir = EBANProperties.getEBANProperty(EBANProperties.REVSHARELOGDIR);
  }

  private void control()
  {
    boolean hasRun = false;
    // open log file
    runTS = su.reformatDate("now", null, DTFMT);
    logFilename = logDir + File.separator + runTS + "_masterAccountInvoicing_log.txt";
    if (openLogFile())
    {
      // get current call month
      callMonth = dba.getCurrentCallMonth();
      // check for process row with status of requested
      if (dba.checkRequestProcessExists(callMonth,PROCESSNAME))
      {
        hasRun = true;
        int result = 0;
        // get process seq no
        processSeqNo = dba.getRequestProcessSeqNo(callMonth,PROCESSNAME);
        fullMessage("   Invoicing has started");
        // update process status to running
        ok = dba.setRequestProcessRunning(callMonth,processSeqNo,UPDATEDBY);
        // Cleardown invoice and suspense reports for the current call month
        if (ok)
        {
          fullMessage("   Clearing down the invoice, billable and suspense reports");
          result = dba.cleardownInvoicingReports(callMonth);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to clear down reports prior to invoicing : return value = "+result;
          }
        }
        // move suspended RQR09 that could not be invoiced back to RQR09
        if (ok)
        {
          fullMessage("   Bringing suspended RQR09 back for invoicing");
          result = dba.moveSuspendedRQR09("I",UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to move suspended RQR09 : return value = "+result;
          }
        }
        // reset any Non Invoiced Reason on RQR09 for PRS
        if (ok)
        {
          fullMessage("   Reset Not Invoiced Reason for PRS");
          result = dba.resetNotInvoicedPRS(UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to reset Not Invoiced Reason for PRS : return value = "+result;
          }
        }
        // Process master accounts ready to be invoiced
        if (ok)
        {
          fullMessage("   Processing Master Accounts");
          java.sql.ResultSet masterAccountRS = dba.getMasterAccountsForInvoicing();
          try
          {
            while ((masterAccountRS.next())&&(ok))
            {
              String masterAccountNumber = masterAccountRS.getString("Master_Account_Number");
              String masterAccountName = masterAccountRS.getString("Master_Account_Name");
              long masterAccountId = masterAccountRS.getLong("Master_Account_Id");
              String frequencyCode = masterAccountRS.getString("Frequency_Code");
              String invoiced = masterAccountRS.getString("Invoiced");
              fullMessage("      Processing master account "+masterAccountName+"/"+masterAccountNumber);
              // Check that customer is set up and all the reference data is provided
              if (ok)
              {
                result = dba.suspendMasterAccountData(callMonth,masterAccountId,UPDATEDBY);
                if (result!=0)
                {
                  ok = false;
                  completionMessage =
                    "   Failed checking master account "+
                    masterAccountName+"/"+masterAccountNumber+
                    " set up and reference data";
                }
              }
              // Remove any data for accounts that should not be invoiced
              if (ok)
              {
                result = dba.suspendAccountsToIgnore(masterAccountId,UPDATEDBY);
                if (result!=0)
                {
                  ok = false;
                  completionMessage =
                    "   Failed suspending ignored accounts for "+masterAccountName+"/"+masterAccountNumber;
                }
              }
              // Produce any non-PRS invoice if monthly or valid month for quarter
              if (ok)
              {
                result = dba.masterAccountNonPRSInvoicing(callMonth,masterAccountId,UPDATEDBY,frequencyCode);
                if (result!=0)
                {
                  ok = false;
                  completionMessage =
                    "   Failed on Non-PRS invoicing for "+masterAccountName+"/"+masterAccountNumber;
                }
              }
              // Produce any PRS invoices
              if (ok)
              {
                result = dba.masterAccountPRSInvoicing(callMonth,masterAccountId,UPDATEDBY);
                if (result!=0)
                {
                  ok = false;
                  completionMessage =
                    "   Failed on PRS invcoing for "+masterAccountName+"/"+masterAccountNumber;
                }
              }
            }
          }
          catch(java.sql.SQLException ex)
          {
            ok = false;
            completionMessage="   Failed processing master accounts to invoice : "+ex.getMessage();
          }
          finally
          {
            try
            {
              masterAccountRS.close();
              masterAccountRS = null;
            }
            catch(java.sql.SQLException ex)
            {
              ok = false;
              completionMessage="   Failed to closed masterAccountRS : "+ex.getMessage();
            }
          }
        }
        // Populate the invoice report for the current call month
        if (ok)
        {
          fullMessage("   Populating the Invoice Report table");
          result = dba.populateInvoiceReport(callMonth,UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to populate invoice report : return value = "+result;
          }
        }
        // Populate the suspense report for the current call month
        if (ok)
        {
          fullMessage("   Populating the Suspense Report table");
          result = dba.populateSuspenseReport(callMonth,UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to populate suspense report : return value = "+result;
          }
        }
        // Populate the billable report for the current call month ADDED AFTER UAT
        if (ok)
        {
          fullMessage("   Populating the Billable Report table");
          result = dba.populateBillableReport(callMonth,UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to populate billable report : return value = "+result;
          }
        }
        // Identify unexpected product NTS
        if (ok)
        {
          fullMessage("   Identifying unexpected Product NTS");
          result = dba.identifyUnexpectedProductNTS(UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to identify unexpected product NTS : return value = "+result;
          }
        }
        // update process row and process control for completion
        if (ok)
        {
          completionMessage = "   Invoicing completed";
          ok = dba.setRequestProcessCompleted(callMonth,PROCESSPREFIX,processSeqNo,YES,UPDATEDBY);
          if (!ok)
            completionMessage = "   Failed to set process for successful completion : "+completionMessage;
        }
        else
        {
          ok = dba.setRequestProcessCompleted(callMonth,PROCESSPREFIX,processSeqNo,NO,UPDATEDBY);
          if (!ok)
            completionMessage = "  Failed to set process to failed completion : "+completionMessage;
        }
        // output completion message
        fullMessage(completionMessage);
      }
      // close logfile (and delete if no processing has occurred)
      if (closeLogFile())
      {
        if (!hasRun)
        {
          // delete logfile if no request to run outstanding
          File lFile = new File(logFilename);
          if (!lFile.delete())
            System.out.println("Failed to delete empty logfile");
        }
      }
    }
  }

  // output message to batch job, log file and database
  private void fullMessage(String messageText)
  {
    writeToLogFile(messageText);
    System.out.println(messageText);
    String shorterMessage = messageText.substring(3,messageText.length());
    dba.insertProcessMessage(callMonth,processSeqNo,shorterMessage,UPDATEDBY);
  }

  private boolean openLogFile()
  {
    boolean result = false;
    // open a new log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      logWriter = new BufferedWriter(new FileWriter(new File(logFilename)));
      String now = su.DateToString(gc.getTime(), DTFMT);
      if (writeToLogFile("masterAccountInvoicing processing started at " +
            now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
            now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
            now.substring(4, 6) + "/" + now.substring(0, 4)))
        result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error opening log file "+logFilename+" : "+ex.getMessage());
    }
    return result;
  }

  private boolean writeToLogFile(String message)
  {
    boolean result = false;
    // write supplied text to program log file
    try
    {
      logWriter.write(message);
      logWriter.newLine();
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error writing message '"+message+"' to log file: "+ex.getMessage());
    }
    return result;
  }

  private boolean closeLogFile()
  {
    boolean result = false;
    // at end of program close log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      if (writeToLogFile("masterAccountInvoicing processing ended at " +
            now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
            now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
            now.substring(4, 6) + "/" + now.substring(0, 4)))
      {
        logWriter.close();
        result = true;
      }
    }
    catch (Exception ex)
    {
      System.out.println("Error closing log file: " + ex.getMessage());
    }
    return result;
  }

  public static void main(String[] args)
  {
    masterAccountInvoicing mAI = new masterAccountInvoicing();
    mAI.control();
  }

}



