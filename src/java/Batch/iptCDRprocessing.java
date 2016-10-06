package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;
import java.util.zip.*;
import java.math.BigDecimal;

public class iptCDRprocessing
{

  // class variables
  private String runTS;
  private StringUtil su;
  private DBAccess dba;
  private BufferedWriter logWriter, csvWriter;
  private File logFile, csvFile;
  private String logDir, dropDir, rejDir, procDir, attachDir, workDir, csvFilename;
  private String message;
  private long customerId, paymentGroupId, accountId, invoiceId;
  private String summarySwitch;
  private Hashtable cliSummary, dateSummary, timeSummary, bandSummary;
  // class constants
  private final String ICP = "IP Trunking CDR Processing";
  private final String SSBS = "SSBS";
  private final String ARBOR = "Arbor";
  private final String NOTAVAILABLE = "Not available";
  private final String ACTIVE = "Active";
  private final String INACTIVE = "Inactive";
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String USCORE = "_";
  private final String CDR = "CDR";
  private final String IPTCDR = "IPT_CDR";
  private final String CDREXT = ".TXT";
  private final String CSVEXT = ".csv";
  private final String ZIPEXT = ".zip";
  private final String BCKSLASH = "\\";
  private final String FWDSLASH = "/";
  private final String COLON = ":";
  private final String COMMA = ",";
  private final String PERIOD = ".";
  private final String DBLQUOTE = "\"";
  private final String CLI = "CLI";
  private final String BAND = "BAND";
  private final String SUM = "SUM";
  private final String SUMMARY = "SUMMARY";

  // check that cdr file count matches control file count
  private boolean CDRCountMatch
    ( String cdrFilename, File cdrFile, long controlfileCDRCount, String accountNo, String datepart)
  {
    boolean success = false;
    long fileCount = 0, totalDuration = 0, totalCharge = 0;
    csvFilename = cdrFilename.substring(0, cdrFilename.length() - 7 ) + "cdr.csv";
    openCSVFile(csvFilename);
    if (summarySwitch.startsWith("Y"))
    {
      // initialise summary hashtables
      cliSummary = new Hashtable();
      dateSummary = new Hashtable();
      timeSummary = new Hashtable();
      bandSummary = new Hashtable();
    }
    try
    {
      // open CDR file and read first line
      BufferedReader cfReader = new BufferedReader(new FileReader(cdrFile));
      String cfLine = cfReader.readLine();
      while (!(cfLine==null))
      {
        if (summarySwitch.startsWith("Y"))
        {
          // store values for summaries
          int duration = su.toInt(cfLine.substring(115,124));
          int charge = su.toInt(cfLine.substring(124,133));
          String cli = cfLine.substring(30,46).trim();
          String date = cfLine.substring(46,52);
          date = "20"+cfLine.substring(50,52)+cfLine.substring(48,50)+cfLine.substring(46,48);
          String time = cfLine.substring(52,54);
          String band = cfLine.substring(134,137);
          String destination = cfLine.substring(90,115);
          String productCode = cfLine.substring(133,134);
          String costCentre = cfLine.substring(139,142);
          //System.out.println(duration);
          //System.out.println(charge);
          //System.out.println(cli);
          //System.out.println(date);
          //System.out.println(time);
          //System.out.println(band);
          //System.out.println(destination);
          //System.out.println(productCode);
          //System.out.println(costCentre);
          String bandId = band+destination;
          String cliId = productCode+cli+costCentre;
          // update summaries
          updateSummary(dateSummary,date,1,duration,charge*10);
          updateSummary(timeSummary,time,1,duration,charge*10);
          updateCLISummary(cliSummary,cliId,1,duration,charge*10,cli,productCode,costCentre);
          updateBandSummary(bandSummary,bandId,1,duration,charge*10,band,destination);
          // update totals
          totalDuration = totalDuration + duration;
          totalCharge = totalCharge + charge;
        }
        // increment cdr count and read next line
        fileCount++;
        writeToCSVFile(reformatToCSV(cfLine));
        cfLine = cfReader.readLine();
      }
      cfReader.close();
    }
    catch(java.io.IOException ex)
    {
      logMessage("   Failed reading CDR File "+cdrFile.getName()+ex.getMessage());
    }
    if (fileCount==controlfileCDRCount)
    {
      success = true;
      if (summarySwitch.startsWith("Y"))
        if (!produceSummaries(accountNo,datepart,fileCount,totalCharge,totalDuration))
          logMessage("   Failed to produce summary files for CDR File "+cdrFile.getName());
    }
    else
    {
      logMessage
        ("   CDR count of "+fileCount+" for CDR file "+cdrFile.getName()+
         " does not match control file CDR count of "+controlfileCDRCount);
      File movedCDRFile = new File(rejDir+File.separator+cdrFile.getName());
      if (!cdrFile.renameTo(movedCDRFile))
        logMessage("   Failed to move CDR file "+cdrFile.getName()+" to reject directory");
    }
    closeCSVFile();
    return success;
  }
  
  private String reformatToCSV( String cdrFileline)
  {
    String reformattedLine = "";
    reformattedLine = 
        DBLQUOTE + cdrFileline.substring(0, 10).trim() + DBLQUOTE + COMMA +     // Arbor Account Number
        DBLQUOTE + cdrFileline.substring(10, 20).trim() + DBLQUOTE + COMMA +    // SSBS Account Number    
        DBLQUOTE + cdrFileline.substring(20, 30).trim() + DBLQUOTE + COMMA +    // Billing System
        DBLQUOTE + cdrFileline.substring(30,46).trim() + DBLQUOTE + COMMA +     // CLI
        DBLQUOTE + cdrFileline.substring(46,48).trim() + FWDSLASH +             // Start Date
            cdrFileline.substring(48,50).trim() + FWDSLASH +
            cdrFileline.substring(50,52).trim() + DBLQUOTE + COMMA + 
        DBLQUOTE + cdrFileline.substring(52,54).trim() + COLON +                // Start Time
            cdrFileline.substring(54,56).trim() + COLON + 
            cdrFileline.substring(56,58).trim() + DBLQUOTE + COMMA +     
        DBLQUOTE + cdrFileline.substring(58,60).trim() + FWDSLASH +             // Process Date
            cdrFileline.substring(60,62).trim() + FWDSLASH +
            cdrFileline.substring(62,66).trim() + DBLQUOTE + COMMA +
        DBLQUOTE + cdrFileline.substring(66,68).trim() + DBLQUOTE + COMMA +     // Bearer Capability
        DBLQUOTE + cdrFileline.substring(68,76).trim() + 
                   cdrFileline.substring(76,90).trim() + DBLQUOTE + COMMA +     // Destination Number
        DBLQUOTE + cdrFileline.substring(90, 115).trim() + DBLQUOTE + COMMA +   // Destination Description
        DBLQUOTE + cdrFileline.substring(115,124).trim() + DBLQUOTE + COMMA +   // Duration
        DBLQUOTE + cdrFileline.substring(124,131).trim() + PERIOD +             // Cost 
                   cdrFileline.substring(131,133).trim() + DBLQUOTE + COMMA +   
        DBLQUOTE + cdrFileline.substring(133,134) + DBLQUOTE + COMMA +          // Service Type 
        DBLQUOTE + cdrFileline.substring(134,135) + DBLQUOTE + COMMA +          // Destination Type 
        DBLQUOTE + cdrFileline.substring(135,137) + DBLQUOTE + COMMA +          // Call Type 
        DBLQUOTE + cdrFileline.substring(137,139) + DBLQUOTE + COMMA +          // Distance Code 
        DBLQUOTE + cdrFileline.substring(139,142) + DBLQUOTE + COMMA +          // Cost Centre   
        DBLQUOTE + cdrFileline.substring(142,151) + DBLQUOTE + COMMA +          // Service Location Code  
        DBLQUOTE + cdrFileline.substring(151,158) + DBLQUOTE + COMMA +          // CLI Extension      
        DBLQUOTE + cdrFileline.substring(158,170) + DBLQUOTE + COMMA +          // Originating Trunk      
        DBLQUOTE + cdrFileline.substring(170,173) + DBLQUOTE + COMMA +          // Currency      
        DBLQUOTE + cdrFileline.substring(173,177) + DBLQUOTE + COMMA;           // Company Code    
        String destType = cdrFileline.substring(134,135).trim();               // Determine Distance Type
        String distCode = cdrFileline.substring(137,139).trim();
        String callType = cdrFileline.substring(135,137).trim();
        String distType = "unknown";
        if (destType.startsWith("1"))
        {
            distType = "Other";
            if ((distCode.startsWith("01"))||(distCode.startsWith("03")))
                distType = "National";
            else
            {    
                if ((distCode.startsWith("05"))||(distCode.startsWith("07")))
                    distType = "Local";
                else
                    if (callType.startsWith("04"))
                        distType = "Non Geo";
            }    
        }
        if (destType.startsWith("2"))
             distType = "International";
        reformattedLine = 
                reformattedLine + DBLQUOTE + distType + DBLQUOTE;               // Distance Type
    return reformattedLine;
  }

  private boolean produceSummaries
    ( String accountNo,
      String datepart,
      long totalCalls,
      long totalCost,
      long totalDuration )
  {
    boolean success = false;
    String outLine = "";
    // remove any files in work directory
    initialiseWorkDir();
    try
    {
      // create cli summary file
      String cliFilename = workDir+File.separator+accountNo+USCORE+CLI+USCORE+datepart+CSVEXT;
      BufferedWriter cliBW = new BufferedWriter(new FileWriter(cliFilename));
      outLine =
        "Cable & Wireless - Summary by Calling Line for "+COMMA+accountNo+COMMA+
        "Date" + COMMA + datepart + COMMA +
        "Total Calls " + COMMA + totalCalls + COMMA +
        "Value " + COMMA + makeString(totalCost*10) + COMMA +
        "Total Seconds " + COMMA + totalDuration;
      writeLine(cliBW,outLine);
      outLine =
        "Product Code" + COMMA +
        "Calling Line" + COMMA +
        "Calls" + COMMA +
        "Cost" + COMMA +
        "Seconds" + COMMA +
        "Cost Centre";
      writeLine(cliBW,outLine);
      Vector clV = new Vector(cliSummary.keySet());
      Collections.sort(clV);
      for (Iterator it = clV.iterator(); it.hasNext();)
      {
        String key = (String)it.next();
        SSBSTotalCLIDescriptor stcd = (SSBSTotalCLIDescriptor)cliSummary.get(key);
        outLine =
          stcd.getProductCode() + COMMA +
          "'" + stcd.getCLI() + COMMA +
          stcd.getTotalCalls() + COMMA +
          makeString(stcd.getTotalAmount()) + COMMA +
          stcd.getTotalDuration()+COMMA +
          "'" + stcd.getCostCentre();
        writeLine(cliBW,outLine);
      }
      outLine = "End of Report" + COMMA + COMMA + COMMA + COMMA + COMMA;
      writeLine(cliBW,outLine);
      cliBW.close();
      String bandFilename = workDir+File.separator+accountNo+USCORE+BAND+USCORE+datepart+CSVEXT;
      BufferedWriter bandBW = new BufferedWriter(new FileWriter(bandFilename));
      outLine =
        "Cable & Wireless - Summary of Call Destinations for" + COMMA +
        accountNo + COMMA +
        "Date" + COMMA + datepart + COMMA +
        "Total Calls " + COMMA + totalCalls + COMMA +
        "Value " + COMMA + makeString(totalCost*10) + COMMA +
        "Total Seconds " + COMMA + totalDuration;
      writeLine(bandBW,outLine);
      outLine =
        "Charge Band" + COMMA +
        "Destination" + COMMA +
        "Calls" + COMMA +
        "Cost" + COMMA + "Seconds";
      writeLine(bandBW,outLine);
      Vector bV = new Vector(bandSummary.keySet());
      Collections.sort(bV);
      for (Iterator it = bV.iterator(); it.hasNext();)
      {
        String key = (String)it.next();
        SSBSTotalBandDescriptor stbd = (SSBSTotalBandDescriptor)bandSummary.get(key);
        outLine =
          "'" + stbd.getBand() + COMMA +
          DBLQUOTE + stbd.getDestination() + DBLQUOTE + COMMA +
          stbd.getTotalCalls() + COMMA +
          makeString(stbd.getTotalAmount()) + COMMA +
          stbd.getTotalDuration();
          writeLine(bandBW,outLine);
      }
      outLine = "End of Report" + COMMA + COMMA + COMMA + COMMA;
      writeLine(bandBW,outLine);
      bandBW.close();
      // create date/time summary
      String sumFilename =workDir+File.separator+accountNo+USCORE+SUM+USCORE+datepart+CSVEXT;
      BufferedWriter sumBW = new BufferedWriter(new FileWriter(sumFilename));
      outLine =
        "Cable & Wireless - Summary by Date and General for" + COMMA +
        accountNo + COMMA +
        "Date" + COMMA + datepart + COMMA +
        "Total Calls " + COMMA + totalCalls + COMMA +
        "Value " + COMMA + makeString(totalCost*10) + COMMA +
        "Total Seconds " + COMMA + totalDuration;
      writeLine(sumBW,outLine);
      outLine = COMMA + COMMA + COMMA;
      writeLine(sumBW,outLine);
      outLine = "Time (hh:00)" + COMMA + "Calls" + COMMA + "Cost" + COMMA + "Seconds";
      writeLine(sumBW,outLine);
      // determine if any time summaries are missing
      for (int i=0; i < 24; i++)
      {
        String testHour = "";
        if (i<10)
          testHour = "0" + Integer.toString(i);
        else
          testHour = Integer.toString(i);
        if (!timeSummary.containsKey(testHour))
        {
          ebillzTotalDescriptor etd = new ebillzTotalDescriptor(testHour,0,0,0);
          timeSummary.put(testHour,etd);
        }
      }
      //process time hastable
      Vector tV = new Vector(timeSummary.keySet());
      Collections.sort(tV);
      for (Iterator it = tV.iterator(); it.hasNext();)
      {
        String key = (String)it.next();
        ebillzTotalDescriptor etd = (ebillzTotalDescriptor)timeSummary.get(key);
        outLine =
          "'" + etd.getId() + ":00" + COMMA +
          etd.getTotalCalls() + COMMA +
          makeString(etd.getTotalAmount()) + COMMA +
          etd.getTotalDuration();
        writeLine(sumBW,outLine);
      }
      outLine = COMMA + COMMA + COMMA;
      writeLine(sumBW,outLine);
      outLine = "Date (yyyymmdd)" + COMMA + "Calls" + COMMA + "Cost" + COMMA + "Seconds";
      writeLine(sumBW,outLine);
      // process date hashtable
      Vector dV = new Vector(dateSummary.keySet());
      Collections.sort(dV);
      for (Iterator it = dV.iterator(); it.hasNext();)
      {
        String key = (String)it.next();
        ebillzTotalDescriptor etd = (ebillzTotalDescriptor)dateSummary.get(key);
        outLine =
          "'" + etd.getId() + COMMA +
          etd.getTotalCalls() + COMMA +
          makeString(etd.getTotalAmount()) + COMMA +
          etd.getTotalDuration();
        writeLine(sumBW,outLine);
      }
      outLine = "End of Report"+ COMMA + COMMA + COMMA;
      writeLine(sumBW,outLine);
      sumBW.close();
      // Determine file location
      String locationDir =
        ARBOR+FWDSLASH+customerId+FWDSLASH+paymentGroupId+FWDSLASH+accountId+FWDSLASH+invoiceId+FWDSLASH;
      String attachPath =
        attachDir+BCKSLASH+ARBOR+BCKSLASH+customerId+BCKSLASH+paymentGroupId+BCKSLASH+accountId+BCKSLASH+invoiceId+BCKSLASH;
      String attachZIPName =
        accountNo+USCORE+datepart+USCORE+IPTCDR+USCORE+SUMMARY+ZIPEXT;
      String location =
          locationDir+attachZIPName;
      String attachment =
          attachPath+attachZIPName;
      //System.out.println(location);
      //System.out.println(attachment);
      // create attachment row
      if (dba.iptInsertSumAttachment(location,invoiceId,datepart))
      {
        boolean OK = true;
        File zFile = new File(attachment);
        if (zFile.exists())
          if (!zFile.delete())
          {
            logMessage("   Failed to delete existing summary zip file "+attachment);
            OK = false;
          }
        // check if the attachment directory exist
        File aDir = new File(attachPath);
        if (!aDir.exists())
          if (!aDir.mkdirs())
          {
            logMessage("   Failed to create attachment directory "+attachPath+" for summary zip");
            OK = false;
          }
        if (OK)
        {
          // Create zip file of IP Trunking CDR file
          try
          {
            byte[] buf = new byte[1024];
            int len;
            ZipOutputStream zout =
              new ZipOutputStream
                (new FileOutputStream(zFile));
            File[] workArray = new File(workDir).listFiles();
            for (int i = 0; i < workArray.length; i++)
            {
              FileInputStream win = new FileInputStream(workArray[i]);
              zout.putNextEntry(new ZipEntry(workArray[i].getName()));
              while ((len = win.read(buf,0,1024)) > 0)
              {
                zout.write(buf,0,len);
              }
              zout.closeEntry();
              win.close();
            }
            zout.close();
          }
          catch(java.io.IOException ex)
          {
            // any failures in creating zip file make sure it is deleted
            logMessage("   ZIP processing failed for "+attachment);
            if (zFile.exists())
              zFile.delete();
          }

          success = true;
        }
      }
      else
      {
        logMessage("   Failed to create attachment for summary zip "+location);
      }
    }
    catch(Exception ex)
    {
      System.out.println("Error producing summaries for account "+accountNo+" for "+datepart+" : "+ex.getMessage());
    }
    // remove any files in work directory
    initialiseWorkDir();
    return success;
  }

  private String makeString (long amount)
  {
    String output = "", negPrefix = "";
    long absAmount = 0;
    if (amount<1)
    {
      absAmount = amount * -1;
      negPrefix = "-";
    }
    else
      absAmount = amount;
    String work = Long.toString(absAmount).trim();
    int len = work.length();
    switch (len)
    {
      case 1 : work = "0000" + work;
      break;
      case 2 : work = "000" + work;
      break;
      case 3 : work = "00" + work;
      break;
      case 4 : work = "0" + work;
      break;
    }
    len = work.length();
    output = negPrefix + work.substring(0,len-4) + "." + work.substring(len-4,len);
    return output;
  }

  private void initialiseWorkDir()
  {
    File wDir = new File(workDir);
    File[] wdFiles = wDir.listFiles();
    for (int i = 0; i < wdFiles.length; i++)
    {
      File workFile = wdFiles[i];
      workFile.delete();
    }
  }

  // process cdr file associated with a control file
  private boolean processedCDRFile (String accountNo, String datePart, String controlfileDir, long cfCDRCount)
  {
    boolean success = false;
    // determine expected name of CDR file
    String cdrFilename = accountNo+USCORE+datePart+USCORE+CDR+CDREXT;
    //System.out.println(cdrFilename);
    // check that expected CDR file exists
    File cdrFile = new File(controlfileDir+File.separator+cdrFilename);
    if (cdrFile.exists())
    {
      // gets account id, payment group id and customer id for account
      long[] ids = new long[3];
      ids = dba.getIdsForAccount(accountNo);
      if (ids[0]!=-1)
      {
        accountId = ids[0];
        paymentGroupId = ids[1];
        customerId = ids[2];
        // Determine file location
        String locationDir =
          ARBOR+FWDSLASH+customerId+FWDSLASH+paymentGroupId+FWDSLASH+accountId+FWDSLASH+invoiceId+FWDSLASH;
        String attachPath =
          attachDir+BCKSLASH+ARBOR+BCKSLASH+customerId+BCKSLASH+paymentGroupId+BCKSLASH+accountId+BCKSLASH+invoiceId+BCKSLASH;
        String attachZIPName =
          accountNo+USCORE+datePart+USCORE+IPTCDR+ZIPEXT;
        String location =
          locationDir+attachZIPName;
        String attachment =
          attachPath+attachZIPName;
        //System.out.println(location);
        //System.out.println(attachment);
        // create attachment row
        if (dba.iptInsertAttachment(location,invoiceId,datePart))
        {
          // check that the cdr file count matches the control file cdr count
          boolean OK = CDRCountMatch(cdrFilename, cdrFile,cfCDRCount, accountNo, datePart);
          // check that zip file does not already exist
          File zFile = new File(attachment);
          if (zFile.exists())
            if (!zFile.delete())
            {
              logMessage("   Failed to delete existing log file "+attachment);
              OK = false;
            }
          // check if the attachment directory exist
          File aDir = new File(attachPath);
          if (!aDir.exists())
            if (!aDir.mkdirs())
            {
              logMessage("   Failed to create attachment directory "+attachPath);
              OK = false;
            }
          if (OK)
          {
            // Create zip file of IP Trunking CDR file
            try
            {
              byte[] buf = new byte[1024];
              int len;
              ZipOutputStream zout =
                new ZipOutputStream
                (new FileOutputStream(zFile));
              FileInputStream win = new FileInputStream(cdrFile);
              zout.putNextEntry(new ZipEntry(cdrFilename));
              while ((len = win.read(buf,0,1024)) > 0)
              {
                zout.write(buf,0,len);
              }
              zout.closeEntry();
              win.close();
              win = new FileInputStream(csvFile);
              zout.putNextEntry(new ZipEntry(csvFilename));
              while ((len = win.read(buf,0,1024)) > 0)
              {
                zout.write(buf,0,len);
              }
              zout.closeEntry();
              win.close();
              zout.close();
              if (!cdrFile.delete())
                logMessage("  Failed to delete cdr file "+cdrFilename);
              else
              {    
                  if (!csvFile.delete())
                    logMessage("Failed to delete csv file "+csvFilename);
                  else
                    success = true;                
              }  
            }
            catch(java.io.IOException ex)
            {
              // any failures in creating zip file make sure it is deleted
              logMessage("   ZIP processing failed for "+attachment);
              if (zFile.exists())
                zFile.delete();
            }
          }
        }
        else
          logMessage("   Failed to create attachment for "+cdrFilename);
      }
      else
        logMessage("   Unable to determine ids for account "+accountNo);
    }
    else
    {
      logMessage("   Expected CDR file "+cdrFilename+" is missing");
    }
    return success;
  }

  // process control files in the drop directory
  private void processDroppedFiles()
  {
    // process control files in the drop directory
    File dropDirectory = new File(dropDir);
    File[] fileArray = dropDirectory.listFiles();
    int controlFileCount = 0, failureCount = 0, successCount = 0;
    for (int i = 0; i < fileArray.length; i++)
      {
        File controlFile = fileArray[i];
        String controlFilename = controlFile.getName();
        if (controlFilename.endsWith("CONTROL.TXT"))
        {
          // Determine Arbor account number and file date from filename
          // and get CDR count from file contents
          //System.out.println("Processing control file "+controlFilename);
          String accountNo = controlFilename.substring(0,8).trim();
          String datePart = controlFilename.substring(9,17);
          //System.out.println("Account no = "+accountNo);
          //System.out.println("Date part = "+datePart);
          boolean controlFileOK = false;
          if (dba.accountExists(accountNo))
          {
            invoiceId = dba.iptArborInvoiceId(accountNo,datePart);
            //System.out.println(invoiceId);
            if (invoiceId>0)
            {
              long controlfileCDRCount = controlfileCDRCount(controlFilename,dropDir);
              //System.out.println(controlfileCDRCount);
              if (controlfileCDRCount>0)
              {
                if (processedCDRFile(accountNo,datePart,dropDir, controlfileCDRCount))
                  controlFileOK = true;
              }
            }
            else if (invoiceId==0)
              logMessage("   Cannot find matching invoice for control file "+controlFilename);
            else if (invoiceId==-1)
              logMessage("   Too many matching invoices in month for control file "+controlFilename);
            else
              logMessage("   Unexpected error determining matching invoice in month for control file "+controlFilename);
          }
          else
          {
            logMessage("   Cannot process control file "+controlFilename+" as the Arbor account does not exist");
          }
          if (controlFileOK)
          {
            if (!moveFile(controlFilename,dropDir,procDir))
              System.out.println("   Failed to move control file "+controlFilename+" to processed directory");
            successCount++;
          }
          else
          {
            if (!moveFile(controlFilename,dropDir,rejDir))
              System.out.print("   Failed to move control file "+controlFilename+" to reject directory");
            failureCount++;
          }
          controlFileCount++;
        }
      }
    // write run statistics to log file
    logMessage(" ");
    if (controlFileCount==0)
      logMessage("   No control files to be processed");
    else if (controlFileCount==1)
      logMessage("   1 control file processed in this run");
    else
      logMessage("   "+controlFileCount+" control files processed in this run");
    if (successCount==1)
      logMessage("      1 control file successfully processed");
    else if (successCount>1)
      logMessage("      "+successCount+" control files successfully processed");
    if (failureCount==1)
      logMessage("      1 control file could not be processed");
    else if (failureCount>1)
      logMessage("      "+failureCount+" control files could not be processed");
  }

  // check process is not running and if not set run control to prevent duplicate running
  private boolean checkRunControl()
  {
    boolean result = false;
    String status = dba.getRunControlStatus(SSBS,ICP), message="";
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
        if (dba.updateRunControlStatus(SSBS,ICP,ACTIVE))
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
    return result;
  }

  private boolean moveFile(String name, String currentDir, String newDir)
  {
    boolean result = false;
    File original = new File(currentDir+File.separator+name);
    File destination = new File(newDir+File.separator+name);
    if (original.renameTo(destination))
      result = true;
    return result;
  }

  // get cdr count from control file
  private long controlfileCDRCount(String cfName, String cfDir)
  {
    long count= -1;
    String fullname = cfDir+File.separator+cfName;
    try
    {
      BufferedReader cfReader = new BufferedReader(new FileReader(new File(fullname)));
      String countText = cfReader.readLine().trim();
      cfReader.close();
      //System.out.println(countText);
      count = Long.parseLong(countText);
    }
    catch(java.io.IOException ex)
    {
      System.out.println("Error opening control file "+fullname+" : " + ex.getMessage());
    }
    return count;
  }

  // reset run control to allow future running of this process
  private void resetRunControl()
  {
    if (!dba.updateRunControlStatus(SSBS,ICP,INACTIVE))
    {
      String message = "!!!failed to reset run control for this process to inactive!!!";
      writeToLogFile(message);
      System.out.println(message);
    }
  }

  private iptCDRprocessing()
  {
    su = new StringUtil();
    dba = new DBAccess();
    // get parameters from properties files
    logDir = EBANProperties.getEBANProperty(EBANProperties.ICPLOGDIR);
    dropDir = EBANProperties.getEBANProperty(EBANProperties.ICPDROPDIR);
    rejDir = EBANProperties.getEBANProperty(EBANProperties.ICPREJDIR);
    procDir = EBANProperties.getEBANProperty(EBANProperties.ICPPROCDIR);
    attachDir = EBANProperties.getEBANProperty(EBANProperties.ICPATTACHDIR);
    summarySwitch = EBANProperties.getEBANProperty(EBANProperties.ICPSUMSWITCH);
    workDir = EBANProperties.getEBANProperty(EBANProperties.ICPWORKDIR);
  }

  private void openLogFile()
  {
    // open a new log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      runTS = su.reformatDate("now", null, DTFMT);
      logFile = new File(logDir + File.separator + runTS + "_iptCDRprocessing_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("iptCDRprocessing started at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
      writeToLogFile(" ");
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
      writeToLogFile(" ");
      writeToLogFile("iptCDRprocessing ended at " +
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
  
  private void openCSVFile(String csvFilename)
  {
    // open a new CSV file
    try
    {
      csvFile = new File(workDir + File.separator + csvFilename);
      csvWriter = new BufferedWriter(new FileWriter(csvFile));
      String csvHeader = 
              DBLQUOTE + "Arbor Account Number" + DBLQUOTE + COMMA +
              DBLQUOTE + "SSBS Account Name" + DBLQUOTE + COMMA +
              DBLQUOTE + "Billing System" + DBLQUOTE + COMMA +
              DBLQUOTE + "CLI" + DBLQUOTE + COMMA +
              DBLQUOTE + "Start Date" +DBLQUOTE + COMMA +
              DBLQUOTE + "Start Time" + DBLQUOTE + COMMA +
              DBLQUOTE + "Process Date" + DBLQUOTE + COMMA +
              DBLQUOTE + "Bearer Capability" + DBLQUOTE + COMMA +
              DBLQUOTE + "Destination Number" + DBLQUOTE + COMMA +
              DBLQUOTE + "Destination Description" + DBLQUOTE + COMMA +
              DBLQUOTE + "Duration" + DBLQUOTE + COMMA +
              DBLQUOTE + "Cost" + DBLQUOTE + COMMA +
              DBLQUOTE + "Service Type" + DBLQUOTE + COMMA +
              DBLQUOTE + "Destination Type" + DBLQUOTE + COMMA +
              DBLQUOTE + "Call Type" + DBLQUOTE + COMMA +
              DBLQUOTE + "Distance Code" + DBLQUOTE + COMMA +
              DBLQUOTE + "Cost Centre Code" + DBLQUOTE + COMMA +
              DBLQUOTE + "Service Location Code" + DBLQUOTE + COMMA +
              DBLQUOTE + "CLI Extension" + DBLQUOTE + COMMA +
              DBLQUOTE + "Originating Trunk" + DBLQUOTE + COMMA +
              DBLQUOTE + "Currency Code" + DBLQUOTE + COMMA +
              DBLQUOTE + "Company Code" + DBLQUOTE + COMMA +
              DBLQUOTE + "Distance Type" + DBLQUOTE;
      writeToCSVFile(csvHeader);  
      
    }
    catch (Exception ex)
    {
      System.out.println("Error opening csv file: " + csvFilename + " : " + ex.getMessage());
    }
  }
  
  private void closeCSVFile()
  {
    // at end of program close log file
    try
    {
      csvWriter.close();
    }
    catch (Exception ex)
    {
      System.out.println("Error closing csv file: " + ex.getMessage());
    }
  }   
  
  // write supplied text to program log file
  private void writeToCSVFile(String csvLine)
  {
    try
    {
      csvWriter.write(csvLine);
      csvWriter.newLine();
    }
    catch (Exception ex)
    {
      System.out.println("Error writing line to CSV file : '" + csvLine +
        " : " + ex.getMessage());
    }
  }
  
  // log message to both log file and standard output
  private void logMessage(String message)
  {
    System.out.println(message);
    writeToLogFile(message);
  }

  // write supplied text to program log file
  private void writeToLogFile(String message)
  {

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

  // standard function to write a line to a file
  private void writeLine(BufferedWriter bw, String line)
  {
    try
    {
      bw.write(line);
      bw.newLine();
    }
    catch(Exception ex)
    {
      System.out.println("Error writing to BufferedWriter "+bw+" "+ex.getMessage());
    }
  }

  // standard function to write to a file
  private void writeFile(FileWriter fw, String data)
  {
    try
    {
      fw.write(data);
    }
    catch(Exception ex)
    {
      System.out.println("Error writing to FileWriter "+fw+" "+ex.getMessage());
    }
  }

  // generic function to update date or time summary
  private void updateSummary(Hashtable summary, String id,
    long calls, long duration, long amount)
  {
    ebillzTotalDescriptor etd = null;
    if (summary.containsKey(id))
    {
      etd = (ebillzTotalDescriptor)summary.get(id);
      etd.incrementTotalCalls(calls);
      etd.incrementTotalDuration(duration);
      etd.incrementTotalAmount(amount);
    }
    else
    {
      etd = new ebillzTotalDescriptor(id,duration,amount,calls);
    }
    summary.put(id,etd);
  }

  // fuunction to update CLI summary
  private void updateCLISummary(Hashtable CLISummary, String id,
    long calls, long duration, long amount, String cli, String productCode, String costCentre)
  {
    SSBSTotalCLIDescriptor stcd = null;
    if (cliSummary.containsKey(id))
    {
      stcd = (SSBSTotalCLIDescriptor)cliSummary.get(id);
      stcd.incrementTotalAmount(amount);
      stcd.incrementTotalCalls(calls);
      stcd.incrementTotalDuration(duration);
    }
    else
    {
      stcd = new SSBSTotalCLIDescriptor(id,duration,amount,calls,cli,productCode,costCentre);
    }
    cliSummary.put(id,stcd);
  }

  // function to update band summary
  private void updateBandSummary(Hashtable bandSummary, String id,
    long calls, long duration, long amount, String band, String destination)
  {
    SSBSTotalBandDescriptor stbd = null;
    if (bandSummary.containsKey(id))
    {
      stbd = (SSBSTotalBandDescriptor)bandSummary.get(id);
      stbd.incrementTotalAmount(amount);
      stbd.incrementTotalCalls(calls);
      stbd.incrementTotalDuration(duration);
    }
    else
    {
      stbd = new SSBSTotalBandDescriptor(id,duration,amount,calls,band,destination);
    }
    bandSummary.put(id,stbd);
  }

  public static void main(String[] args)
  {
    // control processing
    iptCDRprocessing icp = new iptCDRprocessing();
    icp.openLogFile();
    if (icp.checkRunControl())
    {
      icp.processDroppedFiles();
      icp.resetRunControl();
    }
    icp.closeLogFile();
  }

}



