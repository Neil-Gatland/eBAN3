package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;
import java.util.zip.*;

public class closeCurrentMonth
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
  private final String PROCESSNAME = "Close Current Month";
  private final String YES = "Y";
  private final String NO = "N";
  private final String UPDATEDBY = "closeCurrentMonth";
  private final String PROCESSPREFIX = "Close of processing month";

  private closeCurrentMonth()
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
    logFilename = logDir + File.separator + runTS + "_closeCurrentMonth_log.txt";
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
        fullMessage("   Close current processing month has started");
        // update process status to running
        ok = dba.setRequestProcessRunning(callMonth,processSeqNo,UPDATEDBY);
        // Check that all required processes have been run
        boolean processesRun = dba.checkProcessesRun(callMonth);
        if (!processesRun)
          fullMessage("   Cannot close processing with incompleted processes outstanding");
        // Check that there are no outstanding invoices to upload
        boolean noOutstandingUploads = true;
        long outstandingInvoiceCount = dba.countOutstandingUploads();
        if (outstandingInvoiceCount>0)
        {
          noOutstandingUploads = false;
          fullMessage("   Cannot close processing with "+outstandingInvoiceCount+" invoices not yet uploaded to ebilling");
        }
        // If checks passed perform close processing
        if ((processesRun)&&(noOutstandingUploads))
        {
          // archive billable, suspense and invoice reports
          fullMessage("   Archiving billable, suspense and invoice reports");
          result = dba.populateArchiveReports(callMonth,UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to archive billable, suspense and invoice reports : return value = "+result;
          }
          // archive written off suspense
          fullMessage("   Archiving off written off RQR09");
          result = dba.archiveWrittenOff();
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to archive written off RQR09 : return value = "+result;
          }
          // archive RQR09
          fullMessage("   Archiving off invoiced RQR09 and not invoiced RQR09 Suspense for non-PRS");
          result = dba.archiveRQR09();
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to archive invoiced RQR09 and not invoiced RQR09 Suspense for non-PRS : return value = "+result;
          }
        }
        else
        {
          ok = false;
          completionMessage = "   Processing still outstanding";
        }
        // create new process control row for the next month
        if (ok)
        {
          boolean insertOK = dba.insertNextProcessControl(callMonth,UPDATEDBY);
          if (!insertOK)
          {
            ok = false;
            completionMessage="   Failed to insert new Process Control row : return value = "+result;
          }
        }
        // update process row and process control for completion
        if (ok)
        {
          completionMessage = "   Close processing month completed";
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
      if (writeToLogFile("closeCurrentMonth processing started at " +
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
      if (writeToLogFile("closeCurrentMonth processing ended at " +
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
    closeCurrentMonth cCM = new closeCurrentMonth();
    cCM.control();
  }

}



