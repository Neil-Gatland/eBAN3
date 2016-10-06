package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;
import java.util.zip.*;

public class loadRQR09
{

  // class variables
  private DBAccess dba;
  private StringUtil su;
  private String logDir, dropDir, archiveDir, runTS, logFilename, message, completionMessage;
  private BufferedWriter logWriter;
  private String callMonth;
  private boolean ok;
  private long processSeqNo;
  // class constants
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String PROCESSNAME = "Load Summarised Call Data";
  private final String YES = "Y";
  private final String NO = "N";
  private final String UPDATEDBY = "loadRQR09";
  private final String RQR09PREFIX = "RQR9CNTL.M";
  private final String PROCESSPREFIX = "RQR09 load";
  private final String CSV = ".csv";

  private loadRQR09()
  {
    dba = new DBAccess();
    su = new StringUtil();
    logDir = EBANProperties.getEBANProperty(EBANProperties.REVSHARELOGDIR);
    dropDir = EBANProperties.getEBANProperty(EBANProperties.REVSHAREDROPDIR);
    archiveDir = EBANProperties.getEBANProperty(EBANProperties.REVSHAREARCHIVEDROPDIR);
  }

  private void control()
  {
    boolean hasRun = false;
    boolean hasControlFile = false;
    // open log file
    runTS = su.reformatDate("now", null, DTFMT);
    logFilename = logDir + File.separator + runTS + "_loadRQR09_log.txt";
    if (openLogFile())
    {
      // get current call month
      callMonth = dba.getCurrentCallMonth();
      // check for process row with status of requested
      if (dba.checkRequestProcessExists(callMonth,PROCESSNAME))
      {
        hasRun = true;
        int result = 0;
        String rqr09ControlFileName = "", rqr09ControlFilePath = "";
        File rqr09Control = null;
        // get process seq no
        processSeqNo = dba.getRequestProcessSeqNo(callMonth,PROCESSNAME);
        fullMessage("   Load RQR09 has started");
        // update process status to running
        ok = dba.setRequestProcessRunning(callMonth,processSeqNo,UPDATEDBY);
        long rowCount = 0;
        // check that the control file exists for the current month
        if (ok)
        {
          rqr09ControlFileName = RQR09PREFIX+callMonth.substring(2,4)+callMonth.substring(4,6);
          rqr09ControlFilePath = dropDir+File.separator+rqr09ControlFileName;
          rqr09Control = new File(rqr09ControlFilePath);
          if (!rqr09Control.exists())
          {
            ok = false;
            completionMessage="   Expected RQR09 control file "+rqr09ControlFilePath+" is missing";
          }
          else
          {
            hasControlFile = true;
            fullMessage("   Expected RQR09 control file "+rqr09ControlFilePath+" is being processed");
          }
        }
        // process the control file
        if (ok)
        {
          try
          {
            BufferedReader cntlFile = new BufferedReader(new FileReader(rqr09ControlFilePath));
            String cntlLine = cntlFile.readLine();
            boolean first = true;
            while((cntlLine!=null)&&(ok))
            {
              String zipFileName = cntlLine.substring(0,18);
              String internalFileName = cntlLine.substring(0,14);
              String zipFileCountText = cntlLine.substring(20,cntlLine.length()).trim();
              String rqr09InternalName = "", workRQR09Pathname = "";
              long zipFileCount = Long.parseLong(zipFileCountText.trim());
              long actualCount = 0;
              // Check that the zip file exists
              File zipFile = new File(dropDir+File.separator+zipFileName);
              if (!zipFile.exists())
              {
                ok = false;
                completionMessage="   Expected zip file "+zipFileName+" is missing";
              }
              // for first zip file cleardown RQR09 for the call month
              if ((first)&(ok))
              {
                fullMessage("   Clearing down RQR09 for processed call month "+callMonth);
                result = dba.cleardownRQR09(callMonth);
                if (result!=0)
                {
                  ok = false;
                  completionMessage="   Failed to cleardown RQR09 for processed call month "+callMonth+" : return value = "+result;
                }
                first = false;
              }
              // Extract the zip file
              if (ok)
              {
                fullMessage("   Extracting zip file "+zipFileName);
                try
                {
                  ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
                  ZipEntry rqr09 = null;
                  while((rqr09=zis.getNextEntry())!=null)
                  {
                    rqr09InternalName = rqr09.getName();
                    workRQR09Pathname = dropDir+File.separator+rqr09InternalName;
                    OutputStream out = new FileOutputStream(workRQR09Pathname);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = zis.read(buf)) > 0)
                    {
                        out.write(buf, 0, len);
                    }
                    out.close();
                  }
                  zis.closeEntry();
                  zis.close();
                }
                catch(java.io.IOException ex)
                {
                  ok = false;
                  completionMessage="   Serious error processing zip file "+zipFileName+" : "+ex.getMessage();
                }
                if (ok)
                {
                  fullMessage("   RQR09 data has been unzipped sucessfully: Now loading RQR09 data");
                  if (!rqr09InternalName.equals(internalFileName))
                    fullMessage("    WARNING: internal file name is "+rqr09InternalName+" : It should be "+internalFileName);
                }
                // Process the unzipped RQR09 data
                if (ok)
                {
                  // Process extracted RQR09 work file
                  try
                  {
                    BufferedReader rqr09BR = new BufferedReader(new FileReader(new File(workRQR09Pathname)));
                    String rqr09Line = rqr09BR.readLine();
                    while ((rqr09Line!=null)&&(ok))
                    {
                      // get rqr09 data items from the record line
                      rqr09Descriptor rqr09Record = new rqr09Descriptor(rqr09Line);
                      String accountNo = rqr09Record.getAccountNo();
                      String providerName = rqr09Record.getProviderName();
                      // Temporarily no longer use product code for rating
                      //String productCode = rqr09Record.getProductCode();
                      String productName = rqr09Record.getProductName();
                      String recordCallMonth = rqr09Record.getCallMonth();
                      String source = rqr09Record.getSource();
                      String NTS = rqr09Record.getNTS();
                      String primeCalls = rqr09Record.getPrimeCalls();
                      String standardCalls = rqr09Record.getStandardCalls();
                      String economyCalls = rqr09Record.getEconomyCalls();
                      String primeHours = rqr09Record.getPrimeHours();
                      String primeMinutes = rqr09Record.getPrimeMinutes();
                      String primeSeconds = rqr09Record.getPrimeSeconds();
                      String primeCharge = rqr09Record.getPrimeCharge();
                      String standardHours = rqr09Record.getStandardHours();
                      String standardMinutes = rqr09Record.getStandardMinutes();
                      String standardSeconds = rqr09Record.getStandardSeconds();
                      String standardCharge = rqr09Record.getStandardCharge();
                      String economyHours = rqr09Record.getEconomyHours();
                      String economyMinutes = rqr09Record.getEconomyMinutes();
                      String economySeconds = rqr09Record.getEconomySeconds();
                      String economyCharge = rqr09Record.getEconomyCharge();
                      String totalHours = rqr09Record.getTotalHours();
                      String totalMinutes = rqr09Record.getTotalMinutes();
                      String totalSeconds = rqr09Record.getTotalSeconds();
                      String totalCharge = rqr09Record.getTotalCharge();
                      String calls = rqr09Record.getCalls();
                      String networkSource = rqr09Record.getNetworkSource();
                      String differentiatedBillingFlag = rqr09Record.getDifferentiatedBillingFlag();
                      String usageCode = rqr09Record.getUsageCode();
                      String serviceLevelCode = rqr09Record.getServiceLevelCode();
                      // If old format RQR09 use product code, if new format then 
                      // use last three digits of service level code as product code
                      String productCode = "";
                      if ((serviceLevelCode.startsWith("SCPRC"))||(serviceLevelCode.startsWith("SCNTS")))
                          productCode = rqr09Record.getProductCode();
                      else
                      {
                          productCode = 
                            serviceLevelCode.substring(serviceLevelCode.length()-3, serviceLevelCode.length());                          
                      }
                      // check that there current record is not future dated, if it is then exit the loop
                      if (Long.parseLong(recordCallMonth)>Long.parseLong(callMonth))
                      {
                        ok = false;
                        completionMessage = "   Failure as future dated call month found of "+recordCallMonth;
                      }
                      else
                      {
                        // insert RQR09 record
                        // NOTE THAT PRIME VALUES ARE MAPPED TO WEEKEND VALUES,
                        // STANDARD VALUES TO DAY VALUES AND ECONOMY VALUES TO EVENING VALUES!
                        if (dba.insertRQR09(
                              callMonth,accountNo,providerName,productCode,
                              productName,recordCallMonth,source,NTS,
                              standardCalls,economyCalls,primeCalls,
                              standardHours,standardMinutes,standardSeconds,standardCharge,
                              economyHours,economyMinutes,economySeconds,economyCharge,
                              primeHours,primeMinutes,primeSeconds,primeCharge,
                              totalHours,totalMinutes,totalSeconds,totalCharge,
                              calls,networkSource,PROCESSNAME,differentiatedBillingFlag,
                              usageCode,serviceLevelCode))
                          actualCount++;
                        else
                        {
                            // exit loop if DB insert fails
                            ok = false;
                            System.out.println("actualCount = "+actualCount);
                            System.out.println("recordCallMonth = "+recordCallMonth);
                            completionMessage = "   Failure to insert RQR09 data";
                        }
                      }
                      rqr09Line = rqr09BR.readLine();
                    }
                    rqr09BR.close();
                  }
                  catch(java.io.IOException ex)
                  {
                    ok = false;
                    completionMessage = "   File I/O error processing work RQR09 file "+workRQR09Pathname+" : "+ex.getMessage();
                  }
                }
                // Delete the work file
                if (ok)
                {
                  File wFile = new File(workRQR09Pathname);
                  if (!wFile.delete())
                  {
                    ok = false;
                    completionMessage = "   Failed to delete work file "+workRQR09Pathname;
                  }
                }
                // move the ZIP file to the work directory
                if (ok)
                {
                  File archiveZip = new File(archiveDir+File.separator+zipFileName);
                  if (!zipFile.renameTo(archiveZip))
                  {
                    ok = false;
                    completionMessage = "   Failed to move zip file to the archive";
                  }
                }
                // Check the number of RQR09 records in the zip file
                if (ok)
                {
                  if (zipFileCount!=actualCount)
                  {
                    ok = false;
                    completionMessage=
                      "   Not expected number of records in zip file "+
                      zipFileName+
                      " : "+
                      actualCount+" processed"+
                      " : "+
                      zipFileCount+" expected";
                  }
                  else
                  {
                    fullMessage("   Zip file "+zipFileName+" successfully processed : "+actualCount+" rows loaded");
                    rowCount = rowCount + actualCount;
                  }
                }
              }
              cntlLine = cntlFile.readLine();
            }
            cntlFile.close();
          }
          catch(java.io.IOException ex)
          {
            ok = false;
            completionMessage = "   File I/O error processing control file : "+ex.getMessage();
          }
        }
        // update process row and process control for completion
        if (ok)
        {
          completionMessage = "   RQR09 load completed : "+rowCount+" rows have been inserted";
          ok = dba.setRequestProcessCompleted(callMonth,PROCESSPREFIX,processSeqNo,YES,UPDATEDBY);
          if (!ok)
            completionMessage = "   Failed to set process for successful completion : "+completionMessage;
        }
        else
        {
          boolean testOK = dba.setRequestProcessCompleted(callMonth,PROCESSPREFIX,processSeqNo,NO,UPDATEDBY);
          if (!testOK)
            completionMessage = "  Failed to set process to failed completion : "+completionMessage;
        }
        // cleardown RQR09 if there has been a problem
        if (!ok)
        {
          fullMessage("   Clearing down RQR09 for processed call month "+callMonth+" after failure");
          result = dba.cleardownRQR09(callMonth);
          if (result!=0)
              completionMessage=
                "   Failed to cleardown RQR09 for processed call month "+
                callMonth+
                " after failure : return value = "+
                result;
        }
        // Populate the import report for the current call month
        if (ok)
        {
          fullMessage("   RQR09 data loaded : Now populating the Import Report table");
          result = dba.populateImportReport(callMonth,UPDATEDBY);
          if (result!=0)
          {
            ok = false;
            completionMessage="   Failed to populate import report : return value = "+result;
          }
        }
        // output completion message
        fullMessage(completionMessage);
        // move RQR09 control file to the archive drop directory
        if (hasControlFile)
        {
          File rqr09ControlFileArchiveLocation = new File(archiveDir+File.separator+rqr09ControlFileName);
          if(!rqr09Control.renameTo(rqr09ControlFileArchiveLocation))
            fullMessage("Failed to move RQR09 file "+rqr09ControlFileName+" to archive directory");
        }
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
    loadRQR09 lR = new loadRQR09();
    lR.control();
  }

}



