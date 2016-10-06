package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;

public class updateAccountSLCD
{
  // class variables
  private String dropDir, procDir, logDir;
  private DBAccess dba;
  private StringUtil su;
  private String runTS, logMessage;
  private BufferedWriter logWriter;
  private File logFile;
  // class constants
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String SRVLCDLY = "SRVLCDLY";
  private final String CNTL = "CNTL";
  private final String PERIOD = ".";
  private final String ZERO = "0";

  private updateAccountSLCD()
  {
    dba = new DBAccess();
    su = new StringUtil();
    // get parameters from properties files
    dropDir = EBANProperties.getEBANProperty(EBANProperties.UASLCDDROPDIR);
    procDir = EBANProperties.getEBANProperty(EBANProperties.UASLCDPROCDIR);
    logDir = EBANProperties.getEBANProperty(EBANProperties.UASLCDLOGDIR);
  }

  public static void main(String[] args)
  {
    updateAccountSLCD uaslcd = new updateAccountSLCD();
    uaslcd.control();
  }

  public void control()
  {
    if(openLogFile())
    {
      // process update files
      File dropDirectory = new File(dropDir);
      File[] fileArray = dropDirectory.listFiles();
      if (dropDirectory.length()==0)
      {
        logMessage = "   No files in drop directory";
        writeToLogFile(logMessage);
        logMessage = "   ";
        writeToLogFile(logMessage);
      }
      for (int i = 0; i < fileArray.length; i++)
      {
        String dropFilename = fileArray[i].getName();
        File dropFile = new File(dropDir+File.separator+dropFilename);
        long controlCount = 0, dropCount = 0;
        // ignore all files except service location code updates
        if ((dropFilename.startsWith(SRVLCDLY)&&(!dropFilename.endsWith(CNTL))))
        {
          logMessage = "   Processing update file "+dropFilename;
          writeToLogFile(logMessage);
          // check for control file and obtain its record count
          String cntlFilename = dropFilename+PERIOD+CNTL;
          File cntlFile = new File(dropDir+File.separator+cntlFilename);
          if (cntlFile.exists())
          {
            controlCount = getControlCount(cntlFile);
            if (controlCount==-1)
            {
              logMessage = "   Failed to obtain control count from "+cntlFilename;
              writeToLogFile(logMessage);
            }
            else
            {
              // read and process the update file
              int updateResult = 0, insertCount = 0, ignoreCount = 0, updateCount = 0, failCount = 0;
              try
              {
                //dropFile = new File(dropDir+File.separator+dropFilename);
                BufferedReader dfr = new BufferedReader(new FileReader(dropFile));
                String dfrLine = dfr.readLine();
                while(dfrLine!=null)
                {
                  String accountNo = ZERO+dfrLine.substring(0,7);
                  String serviceLocationCode = dfrLine.substring(7,16);
                  String serviceLocationDescription = dfrLine.substring(16,dfrLine.length());
                  updateResult = dba.updateAccountSLCD(accountNo,serviceLocationCode,serviceLocationDescription);
                  switch (updateResult)
                  {
                    case -1:
                      failCount++;
                      break;
                    case 0:
                      ignoreCount++;
                      break;
                    case 1:
                      insertCount++;
                      break;
                    case 2:
                      updateCount++;
                      break;
                    default:
                      logMessage = "      Unexpected return value of "+updateResult+" on calling dba.updateAccountSLCD";
                      writeToLogFile(logMessage);
                  }
                  dropCount++;
                  dfrLine = dfr.readLine();
                }
                dfr.close();
              }
              catch (Exception ex)
              {
                logMessage = "   Issue reading the update file : "+ex.getMessage();
                writeToLogFile(logMessage);
              }
              // on completion check that expected number of records have been processed
              logMessage = "   "+controlCount+" update records successfully processed";
              writeToLogFile(logMessage);
              if (ignoreCount>0)
              {
                logMessage = "      Unchanged : "+ignoreCount;
                writeToLogFile(logMessage);
              }
              if (updateCount>0)
              {
                logMessage = "      Updated   : "+updateCount;
                writeToLogFile(logMessage);
              }
              if (insertCount>0)
              {
                logMessage = "      Inserted  : "+insertCount;
                writeToLogFile(logMessage);
              }
              if (failCount>0)
              {
                logMessage = "      Failed    : "+insertCount;
                writeToLogFile(logMessage);
              }
              if (dropCount==controlCount)
              {
                // move drop file and control file to processed directory
                File movedDropFile = new File(procDir+File.separator+dropFilename);
                File movedCntlFile = new File(procDir+File.separator+cntlFilename);
                if (movedDropFile.exists())
                {
                  logMessage = "   *** cannot move drop file to processed files directory as it already exists ***";
                  writeToLogFile(logMessage);
                }
                else
                  dropFile.renameTo(movedDropFile);
                if (movedCntlFile.exists())
                {
                  logMessage = "   *** cannot move control file to processed files directory as it already exists ***";
                  writeToLogFile(logMessage);
                }
                else
                  cntlFile.renameTo(movedCntlFile);
              }
              else
              // dont move drop file and control file if the number of records processed disagrees with the control file
              {
                logMessage = "   **** Discrepancy: Expected number from control file is actually "+controlCount+" ***";
                writeToLogFile(logMessage);
              }
            }
          }
          else
          {
            logMessage = "   Cannot find expected control file "+cntlFilename;
            writeToLogFile(logMessage);
          }
          writeToLogFile(" ");
        }
      }
      // close log file
      closeLogFile();
    }
    else
      System.out.println("Abandoning processing");
  }

  // get record count from control file
  private long getControlCount(File controlFile)
  {
    long count = 0;
    try
    {
      BufferedReader cfr = new BufferedReader(new FileReader(controlFile));
      String cfrLine = cfr.readLine().trim();
      String recordCount = "";
      boolean done = false;
      //for (int i = 0; i < fileArray.length; i++)
      for (int j = cfrLine.length()-1; j > 1; j--)
      {
        String test = cfrLine.substring(j,j+1);
        if (test.startsWith(" "))
        {
          done = true;
          count = Long.parseLong(recordCount);
        }
        else
          if(!(done))
            recordCount = test + recordCount;
      }
      cfr.close();
    }
    catch (Exception ex)
    {
      System.out.println("Error reading control file : " + ex.getMessage());
      count = -1;
    }
    return count;
  }

  private boolean openLogFile()
  {
    boolean result = false;
    // open a new log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      runTS = su.reformatDate("now", null, DTFMT);
      logFile = new File(logDir + File.separator + runTS + "_updateAccountSLCD_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("updateAccountSLCD processing started at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
      writeToLogFile(" ");
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error opening log file: " + ex.getMessage());
    }
    return result;
  }

  private void closeLogFile()
  {
    // at end of program close log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("updateAccountSLCD processing ended at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
      logWriter.close();
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

}



