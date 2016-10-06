package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;
import java.util.zip.*;

public class rateRQR09
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
  private final String PROCESSNAME = "Summarised Call Data Rating";
  private final String YES = "Y";
  private final String NO = "N";
  private final String UPDATEDBY = "rateRQR09";
  private final String PROCESSPREFIX = "RQR09 rating";

  private rateRQR09()
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
    logFilename = logDir + File.separator + runTS + "_rateRQR09_log.txt";
    if (openLogFile())
    {
      // get current call month
      callMonth = dba.getCurrentCallMonth();
      // check for process row with status of requested
      if (dba.checkRequestProcessExists(callMonth,PROCESSNAME))
      {
        hasRun = true;
        String rqr09Filename = "", rqr09Pathname = "",rqr09ExtractedPathname ="";
        File rqr09Zip = null;
        // get process seq no
        processSeqNo = dba.getRequestProcessSeqNo(callMonth,PROCESSNAME);
        fullMessage("   RQR09 rating has started");
        String rerate = "";
        // update process status to running
        ok = dba.setRequestProcessRunning(callMonth,processSeqNo,UPDATEDBY);
        // Cleardown billable and suspense reports for the current call month
        if (ok)
        {
          fullMessage("   Clearing down the billable and suspense reports");
          int result = dba.cleardownRatingReports(callMonth);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to clear down reports prior to rating : return value = "+result;
          }
        }
        // move suspended RQR09 that could not be rated back to RQR09
        if (ok)
        {
          fullMessage("   Bringing suspended RQR09 back for rating");
          int result = dba.moveSuspendedRQR09("R",UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to move suspended RQR09 : return value = "+result;
          }
        }
        // suspense all RQR09 that is incorrectly posted
        if (ok)
        {
          fullMessage("   Moving incorrectly posted RQR09 to suspense");
          int result = dba.suspendIncorrectlyPostedRQR09(UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to suspense incorrectly posted RQR09 : return value = "+result;
          }
        }
        // Identify orphan accounts and move RQR09 data to suspense
        if (ok)
        {
          fullMessage("   Moving RQR09 for orphan accounts to suspense");
          int result = dba.suspendOrphanAccountRQR09(UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to suspense RQR09 for orphan accounts : return value = "+result;
          }
        }
        //Identify orphan accounts and move RQR09 data to suspense
        if (ok)
        {
          fullMessage("   Moving RQR09 for unallocated account products to suspense");
          int result = dba.suspendUnallocatedProductsRQR09(UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to suspense RQR09 for unallocated account products : return value = "+result;
          }
        }
        // Get rerate flag value
        if (ok)
          rerate = dba.getRerateFlag(callMonth,processSeqNo);
        // Process Master Accounts
        if (ok)
        {
          fullMessage("   Processing Master Accounts");
          java.sql.ResultSet masterAccountRS = dba.getMasterAccountsForRating(rerate);
          try
          {
            //while (masterAccountRS.next())
            while ((masterAccountRS.next())&&(ok))
            {
              String masterAccountNumber = masterAccountRS.getString("Master_Account_Number");
              String masterAccountName = masterAccountRS.getString("Master_Account_Name");
              long masterAccountId = masterAccountRS.getLong("Master_Account_Id");
              fullMessage("      Processing master account "+masterAccountName+"/"+masterAccountNumber);
              int result = 0;
              //Process remaining Master Account / Product Code combinations
              if (ok)
              {
                java.sql.ResultSet productCodeRS = dba.getProductCodeForRating(masterAccountId,rerate);
                try
                {
                  while(productCodeRS.next())
                  {
                    String productCode = productCodeRS.getString(1);
                    fullMessage("         Processing master account / product code combination "+masterAccountNumber+"/"+productCode);
                    // SQLSERVER Procedure will rate master account / product combination
                    result = dba.rateMasterAccountProduct(callMonth,rerate,UPDATEDBY,masterAccountId,productCode);
                    if (result==0)
                      fullMessage(
                        "         Master account / product code combination "+
                        masterAccountNumber+"/"+productCode+
                        " rating completed");
                    else
                      fullMessage(
                        "         Failed to rate master account / product code combination "+
                        masterAccountNumber+"/"+productCode+
                        " : result = "+
                        result);
                  }
                }
                catch(java.sql.SQLException ex)
                {
                  ok = false;
                  completionMessage=
                    "   Failed processing product codes for master account id "+masterAccountId+" : "+ex.getMessage();
                }
                finally
                {
                  try
                  {
                    productCodeRS.close();
                    productCodeRS=null;
                  }
                  catch(java.sql.SQLException ex)
                  {
                    ok = false;
                    completionMessage=
                    "   Failed to close productCodeRS "+ex.getMessage();
                  }
                }
              }
            }
          }
          catch(java.sql.SQLException ex)
          {
            ok = false;
            completionMessage="   Failed processing master accounts to rate : "+ex.getMessage();
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
        // Populate the billable report for the current call month
        if (ok)
        {
          fullMessage("   Populating the Billable Report table");
          int result = dba.populateBillableReport(callMonth,UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to populate billable report : return value = "+result;
          }
        }
        // Populate the suspense report for the current call month
        if (ok)
        {
          fullMessage("   Populating the Suspense Report table");
          int result = dba.populateSuspenseReport(callMonth,UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to populate suspense report : return value = "+result;
          }
        }
        // Identify unexpected product NTS
        if (ok)
        {
          fullMessage("   Identifying unexpected Product NTS");
          int result = dba.identifyUnexpectedProductNTS(UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to identify unexpected product NTS : return value = "+result;
          }
        }
        // update process row and process control for completion
        if (ok)
        {
          completionMessage = "   RQR09 rating completed";
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
      if (writeToLogFile("loadRQR09 processing started at " +
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
      if (writeToLogFile("loadRQR09 processing ended at " +
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
    rateRQR09 rR = new rateRQR09();
    rR.control();
  }

}



