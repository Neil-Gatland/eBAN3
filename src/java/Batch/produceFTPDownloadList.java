package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;

public class produceFTPDownloadList
{

  // class variables
  private String dropDir, logDir, procDir, commDir;
  private String cdrDir, tcpip, username, password;
  private String runTS;
  private StringUtil su;
  private DBAccess dba;
  private BufferedWriter logWriter, commandWriter;
  private File logFile, commandFile;
  private int accountCount, failedAccountCount;
  // class constants
  private final String SSBS = "SSBS";
  private final String PFDL = "Produce FTP Download List";
  private final String PSCF = "Process SSBS CDR Files";
  private final String FTP = "FTP";
  private final String NOTAVAILABLE = "Not available";
  private final String ACTIVE = "Active";
  private final String INACTIVE = "Inactive";
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String BACKSLASH = "\\";
  private final String PERIOD = ".";
  private final String UNDEFINED = "Undefined";
  private final String PSTN = "PSTN";
  private final String TNBS = "TNBS";
  private final String DAILY = "DAILY";
  private final String TDAILY = "TDAILY";
  private final String ALARM = "Alarm-call";
  private final String DSSTART = "mcllmbp.live.databill.ds.";
  private final String ACCTPREFIX = "c";
  private final String USCORE = "_";
  private final String CDRS = "cdrs";
  private final String CSV = "csv";
  private final String DIALUP = "DIAL UP";
  private final String DBLQUOTE = "\"";

  private boolean checkRunControl()
  {
    boolean result = false;
    String status = dba.getRunControlStatus(SSBS,PFDL), message="";
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
          status = dba.getRunControlStatus(SSBS,FTP);
          if (status.startsWith(ACTIVE))
          {
            message = "!!!cannot run while process SSBS FTP is running!!!";
            writeToLogFile(message);
            System.out.println(message);
          }
          else
          {
            if (dba.updateRunControlStatus(SSBS,PFDL,ACTIVE))
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

  private void resetRunControl()
  {
    if (!dba.updateRunControlStatus(SSBS,PFDL,INACTIVE))
    {
      String message = "!!!failed to rest run control for this process to inactive!!!";
      writeToLogFile(message);
      System.out.println(message);
    }
  }

  private produceFTPDownloadList()
  {
    su = new StringUtil();
    dba = new DBAccess();
    // get parameters from properties files
    dropDir = EBANProperties.getEBANProperty(EBANProperties.PFDLDROPDIR);
    logDir = EBANProperties.getEBANProperty(EBANProperties.PFDLLOGDIR);
    procDir = EBANProperties.getEBANProperty(EBANProperties.PFDLPROCDIR);
    commDir = EBANProperties.getEBANProperty(EBANProperties.PFDLCOMMDIR);
    cdrDir = EBANProperties.getEBANProperty(EBANProperties.PFDLCDRDIR);
    tcpip = EBANProperties.getEBANProperty(EBANProperties.PFDLTCPIP);
    username = EBANProperties.getEBANProperty(EBANProperties.PFDLUSERNAME);
    password = EBANProperties.getEBANProperty(EBANProperties.PFDLPASSWORD);
  }

  // processes all the report files held in the report drop directory
  private void processReportFiles()
  {
    writeToLogFile(" ");
    int fileCount = 0, successCount = 0;
    accountCount = 0;
    failedAccountCount = 0;
    boolean first = true;
    try
    {
      // Process report files directory
      File directory = new File(dropDir);
      File[] fileArray = directory.listFiles();
      for (int i = 0; i < fileArray.length; i++)
      {
        // at start write out intial ftp commands
        if (first)
        {
          openCommandFile();
          writeToCommandFile("open " + tcpip);
          writeToCommandFile(username);
          writeToCommandFile(password);
          writeToCommandFile("quote site trailingblanks");
          writeToCommandFile("lcd ..");
          writeToCommandFile("lcd " + DBLQUOTE + cdrDir + DBLQUOTE);
          first = false;
        }
        File reportFile = fileArray[i];
        String pathname = reportFile.getAbsolutePath();
        String filename = reportFile.getName();
        // process the file and move to processed directory if successful
        if (processedReportFile(reportFile))
        {
          File rfMove = new File(procDir+BACKSLASH+filename);
          if (!reportFile.renameTo(rfMove))
            writeToLogFile("failed to move processed report file: "+pathname);
          successCount++;
        }
        fileCount++;
      }
      writeToLogFile(" ");
      // summarise report files processed in log file
      if (fileCount==0)
      {
        writeToLogFile("No report files in drop directory");
        System.out.println("No report files in drop directory");
      }
      else
      {
        writeToCommandFile("bye");
        closeCommandFile();
        // delete command file if no accounts identifed
        if (accountCount==0)
        {
          commandFile.delete();
        }
        else
        {
          writeToLogFile("No accounts written to command file            : "+accountCount);
          if (failedAccountCount>0)
          writeToLogFile("No failed accounts not written to command file : "+failedAccountCount);
        }
        writeToLogFile(" ");
        String finalMessage = successCount + " report file";
        if (successCount>1)
          finalMessage = finalMessage + "s";
        finalMessage = finalMessage + " out of " + fileCount + " successfully processed";
        System.out.println(finalMessage);
        writeToLogFile(finalMessage);
      }
      writeToLogFile(" ");

    }
    catch(Exception ex)
    {
      writeToLogFile("Error in processReportFiles : " + ex.getMessage());
    }
  }

  // determines report type from the supplied file name
  private String reportType ( String reportFilename )
  {
    String result = UNDEFINED, test = "";

    for (int i=0 ; i < reportFilename.length(); i++)
    {
      String testChar = reportFilename.substring(i,i+1).toUpperCase();
      if (testChar.startsWith(PERIOD))
        test = "";
      else
        test = test + testChar;
      if (test.endsWith("BRBR61"))
        result = PSTN;
      if ((test.endsWith("QRBR61"))||(test.endsWith("YRBR61")))
        result = TNBS;
      if (test.endsWith("XRBR61"))
        result = ALARM;
      if (test.endsWith("DRBR61"))
        result = DAILY;
      if (test.endsWith("TRBR61"))
        result = TDAILY;
    }
    return result;
  }

  // process an individual report file
  private boolean processedReportFile( File reportFile)
  {
    boolean success = false;
    String pathname = reportFile.getAbsolutePath();
    String filename = reportFile.getName();
    String type = reportType(filename);
    if (type.startsWith(UNDEFINED))
      writeToLogFile("File " + pathname + " is not a expected report type");
    else
    {
      try
      {
        String datePrefix = "", datasetSuffix = "";
        if (type.startsWith(PSTN))
        {
          datePrefix = "m";
          datasetSuffix = "a1";
        }
        else if (type.startsWith(TNBS))
        {
          datePrefix = "l";
          datasetSuffix = "a1";
        }
        else if (type.startsWith(DAILY))
        {
          datePrefix = "d";
          datasetSuffix = "a";
        }
        else if (type.startsWith(TDAILY))
        {
          datePrefix = "p";
          datasetSuffix = "a";
        }
        else
        {
          datePrefix = "m";
          datasetSuffix = "x1";
        }
        BufferedReader br = new BufferedReader(new FileReader(reportFile));
        String line = br.readLine();
        if (line!=null)
        {
          boolean eof = false, inBody = true;
          String cc = "", DOB = "", month = "", procDate = "", lastAccount = "9999999";
          int lineCount = 1;
          while(!eof)
          {
            line = br.readLine();
            if (line==null)
              eof = true;
            else
            {
              // check if in report body
              cc = line.substring(0,1);
              if (cc.startsWith("1"))
              {
                lineCount = 0;
                if (line.substring(2,6).startsWith("PAGE"))
                {
                  inBody = true;
                }
                else
                {
                  inBody = false;
                }
              }
              // get DOB and month values to determine processing date
              if ((inBody)&&(line.substring(48,64).startsWith("DAY OF BILLING :")))
              // PSTN and TNBS format
              {
                DOB = line.substring(65,66);
                month = line.substring(90,94);
                procDate = dba.getSSBSProccessingDate(DOB,month);
              }
              else if ((inBody)&&(line.substring(45,61).startsWith("DAY OF BILLING :")))
              // Alarm-call format
              {
                DOB = line.substring(62,63);
                // reverse month as held in MMYY format
                month = line.substring(95,97)+line.substring(93,95);
                procDate = dba.getSSBSProccessingDate(DOB,month);
              }
              else if ((inBody)&&(line.substring(45,64).startsWith("MONTHLY DATABILLS :")))
              {
                // TNBS Monthly - use DOB Z to get month end date and over-ride date suffix
                DOB = "Z";
                month = line.substring(90,94);
                procDate = dba.getSSBSProccessingDate(DOB,month);
                datePrefix = "t";
              }
              else if (type.startsWith(TDAILY))
              {
                // TNBS DAILY

                DOB = line.substring(65,66);
                month = filename.substring(8,13);
                // code to convert month (which is julian date into YYYYMMYY format)
                Calendar date = Calendar.getInstance();
                date.set(Calendar.YEAR,Integer.parseInt(month.substring(0,2)));
                date.set(Calendar.DAY_OF_YEAR,Integer.parseInt(month.substring(2,5)));
                procDate =
                  "20"+
                  expandToTwoDigits(date.get(date.YEAR))+
                  expandToTwoDigits(date.get(date.MONTH)+1)+
                  expandToTwoDigits(date.get(date.DAY_OF_MONTH));
              }
              else if ((inBody)&&(line.substring(47,64).startsWith("DAILY DATABILLS :")))
              {
                // DAILY
                DOB = line.substring(65,66);
                month = line.substring(90,95);
                // code to convert month (which is julian date into YYYYMMYY format)
                Calendar date = Calendar.getInstance();
                date.set(Calendar.YEAR,Integer.parseInt(month.substring(0,2)));
                date.set(Calendar.DAY_OF_YEAR,Integer.parseInt(month.substring(2,5)));
                procDate =
                  "20"+
                  expandToTwoDigits(date.get(date.YEAR))+
                  expandToTwoDigits(date.get(date.MONTH)+1)+
                  expandToTwoDigits(date.get(date.DAY_OF_MONTH));
              }
              // look for account and cdr count lines
              if ( ((inBody)&&(type.startsWith(ALARM))&&(lineCount>4)) ||
                   ((inBody)&&(!type.startsWith(ALARM))&&(lineCount>5)) )
              {
                if ((!line.substring(63,64).startsWith(" "))&&((!line.substring(11,12).startsWith("T"))))
                {
                  String account = line.substring(39,46).trim();
                  switch (account.length())
                  {
                    case 1 : account = "000000" + account;
                    break;
                    case 2 : account = "00000" + account;
                    break;
                    case 3 : account = "0000" + account;
                    break;
                    case 4 : account = "000" + account;
                    break;
                    case 5 : account = "00" + account;
                    break;
                    case 6 : account = "0" + account;
                    break;
                  }
                  String count = line.substring(49,64).trim();
                  String media = line.substring(11,18).trim();
                  String datasetName =
                    DSSTART+datePrefix+month+PERIOD+ACCTPREFIX+account+PERIOD+
                    datasetSuffix;
                  String newFilename =
                    account+USCORE+count+USCORE+procDate+USCORE+
                    type+USCORE+CDRS+PERIOD+CSV;
                  // ignoring duplicates write to command file and update database
                  if ((!account.equals(lastAccount)))
                  {
                    if (type.startsWith(DAILY))
                    {
                      if (!media.startsWith(DIALUP))
                      {
                        // Only process daily files if media is not dial up
                        if (dba.insertSSBSFTP(newFilename,procDate,filename,account)==1)
                        {
                          writeToCommandFile("get '"+datasetName+"' "+newFilename);
                          accountCount++;
                        }
                        else
                        {
                          writeToLogFile("Failed for account "+account+" ("+newFilename+","+procDate+")");
                          failedAccountCount++;
                        }
                      }
                    }
                    else
                    {
                      if (dba.insertSSBSFTP(newFilename,procDate,filename,account)==1)
                      {
                        writeToCommandFile("get '"+datasetName+"' "+newFilename);
                        accountCount++;
                      }
                      else
                      {
                        writeToLogFile("Failed for account "+account+" ("+newFilename+","+procDate+")");
                        failedAccountCount++;
                      }
                    }
                  }
                  lastAccount = account;
                }
              }
              lineCount++;
            }
          }
          writeToLogFile("Successfully processed report file " + pathname);
          success = true;
        }
        else
          writeToLogFile("File " + pathname + " is empty");
        br.close();
      }
      catch(Exception ex)
      {
        writeToLogFile
          ("Error in processedReportFile(" + pathname + ") : " + ex.getMessage());
      }
    }
    return success;
  }

  private void openLogFile()
  {
    // open a new log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      runTS = su.reformatDate("now", null, DTFMT);
      logFile = new File(logDir + File.separator + runTS + "_produceFTPDownloadList_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("produceFTPDownloadList processing started at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
    }
    catch (Exception ex)
    {
      System.out.println("Error opening log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void openCommandFile()
  {
    // open the command file
    try
    {
      runTS = su.reformatDate("now", null, DTFMT);
      commandFile = new File(commDir + File.separator + runTS + "_command_file.txt");
      commandWriter = new BufferedWriter(new FileWriter(commandFile));
    }
    catch (Exception ex)
    {
      System.out.println("Error opening command file: " + ex.getMessage());
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
      writeToLogFile("produceFTPDownloadList processing ended at " +
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

  private void closeCommandFile()
  {
    // close command file
    try
    {
      commandWriter.close();
    }
    catch (Exception ex)
    {
      System.out.println("Error closing command file: " + ex.getMessage());
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

  private void writeToCommandFile(String message)
  {
    // write supplied text to program command file
    try
    {
      commandWriter.write(message);
      commandWriter.newLine();
    }
    catch (Exception ex)
    {
      System.out.println("Error writing message '" + message +
        "' to command file : " + ex.getMessage());
      System.exit(1);
    }
  }

  private String expandToTwoDigits( int inDatePart)
  {
    String result = Integer.toString(inDatePart);
    if (inDatePart<10)
      result = "0" + result;
    return result;
  }

  public static void main(String[] args)
  {
    // control processing
    produceFTPDownloadList pfdl = new produceFTPDownloadList();
    pfdl.openLogFile();
    if (pfdl.checkRunControl())
    {
      pfdl.processReportFiles();
      pfdl.resetRunControl();
    }
    pfdl.closeLogFile();
  }

}



