package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;

public class processFTPRequests
{

  // class variables
  private String downDir, logDir, tcpip, username, password, runTS, message;
  private DBAccess dba;
  private StringUtil su;
  //private sun.net.ftp.FtpClient ftpObj;
  private File logFile;
  private BufferedWriter logWriter;
  private boolean connected, emptyRun;
  // class constants
  private final String SSBS = "SSBS";
  private final String IFR = "Identify FTP Requests";
  private final String PSCF = "Process SSBS CDR Files";
  private final String PFR = "Process FTP Requests";
  private final String NOTAVAILABLE = "Not available";
  private final String ACTIVE = "Active";
  private final String INACTIVE = "Inactive";
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String BACKSLASH = "\\";
  private final String DBLQUOTE = "\"";

  // check that the job can be run
  private boolean checkRunControl()
  {
    boolean result = false;
    String status = dba.getRunControlStatus(SSBS,PFR), message="";
    if (status.startsWith(NOTAVAILABLE))
    {
      message = "!!!Expected job control row for this process is missing!!!";
      writeToLogFile(message);
      System.out.println(message);
    }
    else
    {
      if (status.startsWith(ACTIVE))
      {
        message = "!!!this process is already running!!!";
        writeToLogFile(message);
        System.out.println(message);
      }
      else
      {
        status = dba.getRunControlStatus(SSBS,PSCF);
        if (!status.startsWith(INACTIVE))
        {
          message = "!!!cannot run while process SSBS CDR files is running!!!";
          writeToLogFile(message);
          System.out.println(message);
        }
        else
        {
          status = dba.getRunControlStatus(SSBS,IFR);
          if (status.startsWith(ACTIVE))
          {
            message = "!!!cannot run while identify FTP requests is running!!!";
            writeToLogFile(message);
            System.out.println(message);
          }
          else
          {
            if (dba.updateRunControlStatus(SSBS,PFR,ACTIVE))
            {
              result = true;
            }
            else
            {
              message = "!!!cannot set run control for this process!!!";
              writeToLogFile(message);
              System.out.println(message);
            }
          }
        }
      }
    }
    return result;
  }

  // reset run control to allow job to be run again
  private void resetRunControl()
  {
    if (!dba.updateRunControlStatus(SSBS,PFR,INACTIVE))
    {
      String message = "!!!failed to reset run control for this process to inactive!!!";
      writeToLogFile(message);
      System.out.println(message);
    }
  }

  private processFTPRequests()
  {
    su = new StringUtil();
    dba = new DBAccess();
    // get parameters from properties files
    downDir = EBANProperties.getEBANProperty(EBANProperties.PFDLCDRDIR);
    logDir = EBANProperties.getEBANProperty(EBANProperties.PFDLLOGDIR);
    tcpip = EBANProperties.getEBANProperty(EBANProperties.PFDLTCPIP);
    username = EBANProperties.getEBANProperty(EBANProperties.PFDLUSERNAME);
    password = EBANProperties.getEBANProperty(EBANProperties.PFDLPASSWORD);
  }

  // control processing of FTP requests
  private void controlProcessing()
  {
    // Check for number of outstanding ftp requests
    int requestCount = dba.ftpRequestsCount(), successCount = 0, failedCount = 0;
    message = "   ";
    System.out.println(message);
    writeToLogFile(message);
    message = "   "+requestCount+" outstanding FTP requests identified";
    System.out.println(message);
    writeToLogFile(message);
    message = "   ";
    System.out.println(message);
    writeToLogFile(message);
    // connect to host via FTP if there are outstanding requests
    if (requestCount>0)
    {
      emptyRun = false;
      ftpUtil fu = new ftpUtil(tcpip,username,password);
      connected = fu.connectToHost();
      message = "   ";
      writeToLogFile(message);
      System.out.println(message);
      if (connected)
      {
        message = "   Connected to "+tcpip+" with "+username+"/"+password;
        writeToLogFile(message);
        System.out.println(message);
        // process FTP requests
        String [][] ftpRequests = dba.ftpRequests(requestCount);
        for(int i = 0; i<requestCount; i++)
        {
          String filename = ftpRequests[i][0];
          String ssbsOriginalFilename = ftpRequests[i][1];
          if (fu.transferFile(downDir,ssbsOriginalFilename,filename))
          {
            // sucessful transfer recorded on DB
            if (dba.updateFTPStatus(filename,"Identified"))
            {
              message = "   Successful transfer of "+ssbsOriginalFilename+" to "+filename;
              writeToLogFile(message);
              System.out.println(message);
              successCount++;
            }
            // sucessful transfer not recorded on DB
            else
            {
              message = "   DB ERROR: Unable to record successful transfer of "+ssbsOriginalFilename+" to "+filename;
              writeToLogFile(message);
              System.out.println(message);
              failedCount++;
            }
          }
          else
          {
            // failed transfer recorded on DB
            if (dba.updateFTPStatus(filename,"Request Failed"))
            {
              message = "   FTP ERROR: Failed to transfer "+ssbsOriginalFilename+" to "+filename;
              writeToLogFile(message);
              System.out.println(message);
            }
            // failed transfer not recorded on DB (saty as original request)
            else
            {
              message = "   DB ERROR: Unable to record unsuccessful transfer of "+ssbsOriginalFilename+" to "+filename;
              writeToLogFile(message);
              System.out.println(message);
            }
            failedCount++;
          }
        }
        // end of processing messages
        message = "   ";
        writeToLogFile(message);
        System.out.println(message);
        message = "   Number of successful FTP requests : "+successCount;
        writeToLogFile(message);
        System.out.println(message);
        message = "   Number of failed FTP requests     : "+failedCount;
        writeToLogFile(message);
        System.out.println(message);
        message = "   ";
        writeToLogFile(message);
        System.out.println(message);
        // disconnect from host
        fu.disconnectFromHost();
        message = "   Disconnected";
        writeToLogFile(message);
        System.out.println(message);
        message = "   ";
        writeToLogFile(message);
        System.out.println(message);
      }
      else
      {
        message = "   FTP ERROR: Failed to connect to host "+tcpip+" with "+username+"/"+password;
        writeToLogFile(message);
        System.out.println(message);
        message = "   ";
        writeToLogFile(message);
        System.out.println(message);
      }
    }
    else
      emptyRun = true;
  }

  private void openLogFile()
  {
    // open a new log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      runTS = su.reformatDate("now", null, DTFMT);
      logFile = new File(logDir + File.separator + runTS + "_processFTPRequests_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      message =
        "processFTPRequests processing started at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4);
      writeToLogFile(message);
      System.out.println(message);
    }
    catch (Exception ex)
    {
      System.out.println("Error opening log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void closeLogFile()
  {
    // at end of program close log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      message =
        "processFTPRequests processing ended at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4);
      writeToLogFile(message);
      System.out.println(message);
      logWriter.close();
      if (emptyRun)
        if (logFile.delete())
          System.out.println("Deleting log file on empty run");
        else
          System.out.println("!!! Failed to delete log file on empty run");
    }
    catch (Exception ex)
    {
      System.out.println("Error closing log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void writeToLogFile(String message)
  {
    // write supplied text to program log file
    try
    {
      logWriter.write(message);
      logWriter.newLine();
    }
    catch (Exception ex)
    {
      System.out.println("Error writing message '" + message +
        "' to log file : " + ex.getMessage());
      System.exit(1);
    }
  }

  public static void main(String[] args)
  {
    // control processing
    processFTPRequests pfr = new processFTPRequests();
    pfr.openLogFile();
    if (pfr.checkRunControl())
    {
      pfr.controlProcessing();
      pfr.resetRunControl();
    }
    pfr.closeLogFile();
  }

}



