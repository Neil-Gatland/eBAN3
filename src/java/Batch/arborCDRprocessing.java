package Batch;

import java.io.*;
import java.sql.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;
import java.util.zip.*;
import java.math.BigDecimal;

public class arborCDRprocessing
{

  // class variables
  private String runTS, logMessage;
  private StringUtil su;
  private DBAccess dba;
  private String logDir, dropDir, procDir, rejDir, attachDir, workDir, work2Dir, orphanDir, failDir;
  private long maxRecordsCount;
  private String status;
  private BufferedWriter logWriter, partBW;
  private File logFile;
  private Hashtable dateSummary, timeSummary, cliSummary, bandSummary;
  // class constants
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String ACP ="Arbor CDR Processing";
  private final String ARBOR = "Arbor";
  private final String NOTAVAILABLE = "Not available";
  private final String ACTIVE = "Active";
  private final String INACTIVE = "Inactive";
  private final String cfEXT = "CNTL.csv";
  private final String cdrEXT = "CDR.csv";
  private final String dcfEXT = "DAILY_CNTL.csv";
  private final String mcfEXT = "MONTHLY_CNTL.csv";
  private final String dEXT = "DAILY_CDR.csv";
  private final String mEXT = "MONTHLY_CDR.csv";
  private final String csvEXT = ".csv";
  private final String zipEXT = ".zip";
  private final String sSUM = "SUM";
  private final String cSUM = "CLI";
  private final String bSUM = "BAND";
  private final String cdrSUM = "CDR_SUMMARY";
  private final String D = "D";
  private final String M = "M";
  private final String X = "X";
  private final String IDENTIFIED = "Identified";
  private final String DUPLICATE = "Duplicate";
  private final String FAILED = "Failed";
  private final String EMPTY = "Empty";
  private final String PROCESSED = "Processed";
  private final String ORPHAN = "Orphan";
  private final String NULL = "NULL";
  private final String TRAILER = "TRA";
  private final String DBLQUOTE = "\"";
  private final String COMMA = ",";
  private final String USCORE = "_";
  private final String SPACE = " ";
  private final String FWDSLASH = "/";
  private final String DAILY = "Arbor Daily CDRs";
  private final String MONTHLY = "Arbor Monthly Invoiced CDRs";
  private final String SUMMARY = "Arbor Monthly Invoiced CDR Summaries ";
  private final String ACCOUNT = "account";
  private final String INVOICE = "invoice";

  private void processCDRFiles()
  {
    // Processing totals
    int identifiedCount = 0, fileMissingCount = 0;
    int accountMissingCount = 0, invoiceMissingCount = 0, fileIssuesCount = 0, successCount = 0;
    int dailyCDRCount = 0;
    // Get all the identified CDR files
    ResultSet iCDRFiles = dba.getIdentifiedArborCDRFiles();
    try
    {
      while(iCDRFiles.next())
      {
        long cdrFileId = iCDRFiles.getLong("Arbor_CDR_File_Id");
        String cdrFilename = iCDRFiles.getString("CDR_Filename");
        String type = iCDRFiles.getString("Type");
        String fileDate = iCDRFiles.getString("File_Date");
        String accountNumber = iCDRFiles.getString("Account_Number");
        String invoiceNo = iCDRFiles.getString("Invoice_No");
        long cdrCount = iCDRFiles.getLong("CDR_Count");
        long totalDuration = iCDRFiles.getLong("Total_Duration");
        long totalVolume = iCDRFiles.getLong("Total_Volume");
        long totalRated = iCDRFiles.getLong("Total_Rated");
        long totalBilled = iCDRFiles.getLong("Total_Billed");
        identifiedCount++;
        // Various control totals
        long checkCount = 0, checkDuration = 0, checkVolume = 0, checkRated = 0, checkBilled = 0;
        // final status and status detail
        String finalStatus = PROCESSED;
        String finalStatusDetail = NULL;
        //
        String aPath = "", lPath = "";
        long iId = 0;
        // Check that the file can be found
        File cdrFile = new File(workDir+File.separator+cdrFilename);
        if (cdrFile.exists())
        {
          boolean OK = true;
          // Process contents of the CDR file
          BufferedReader cdrBR = openBufferedFile(workDir,cdrFilename);
          String cdrLineFull = getBufferedFileLine(cdrBR);
          String cdrLineControl = cdrLineFull.substring(1,2);
          String cdrLine = cdrLineFull.substring(2,cdrLineFull.length()).trim();
          // Various shared variables
          long accountId = 0, invoiceId = 0;
          int[] fileIds = new int[3];
          String zipFilename = "";
          // Check for reading issues and empty files
          if (cdrLineControl.equals("F"))
          {
            logAndOutput
              ("   FILE I/O ERROR : Failed to read CDR file "+cdrFilename+" in the work directory");
            OK = false;
            finalStatus = FAILED;
            finalStatusDetail = "Failure reading file";
          }
          else if (cdrLineControl.equals("E"))
          {
            logAndOutput("   FILE I/O ERROR : CDR file "+cdrFilename+" in the work directory is empty");
            OK = false;
            finalStatus = EMPTY;
            finalStatusDetail = NULL;
          }
          //
          if (OK)
          {
            // create summary hash tables if monthly cdr
            if (type.equals(M))
            {
              dateSummary = new Hashtable();
              timeSummary = new Hashtable();
              cliSummary = new Hashtable();
              bandSummary = new Hashtable();
            }
            while ((!cdrLineControl.equals("E"))&&(OK))
            {
              String[] dataItems = getLineData(cdrLine);
              String fileAccountNumber = dataItems[1].trim();
              String fileInvoiceNo = dataItems[2];
              // check account number in CDR record matches to file account number (ignoring CDR trailer record)
              if (!fileAccountNumber.equals(TRAILER))
              {
                if ((!fileAccountNumber.equals(accountNumber))||((!fileInvoiceNo.equals(invoiceNo))&&(type.equals(M))))
                {
                  OK = false;
                  if (type.equals(D))
                  {
                    logAndOutput
                      ("   FILE CONTENT ERROR : CDR account number "+fileAccountNumber+" mismatch for CDR file "+cdrFilename);
                    finalStatusDetail = "Mismatch between DB and file account numbers";
                  }
                  else
                  {
                    logAndOutput
                      ("   FILE CONTENT ERROR : CDR account number/invoice no "+
                       fileAccountNumber+"/"+fileInvoiceNo+
                       " mismatch for CDR file "+
                       cdrFilename);
                    finalStatusDetail = "Mismatch between DB and file account numbers/invoice numbers";
                  }
                  finalStatus = FAILED;
                  fileIssuesCount++;
                }
                if (OK)
                {
                  // obtain relevant amount items
                  long duration = makeStringLong(dataItems[31]);
                  long volume = makeStringDecimalLong(dataItems[32]);
                  long rated = makeStringDecimalLong(dataItems[44],8);
                  long billed = 0;
                  if (type.equals(M))
                     billed = makeStringDecimalLong(dataItems[46],8);
                  // increment control totals
                  checkCount++;
                  checkDuration = checkDuration + duration;
                  checkVolume = checkVolume + volume;
                  checkRated = checkRated + rated;
                  checkBilled = checkBilled + billed;
                  // update summaries if a monthly CDR file
                  if (type.equals(M))
                  {
                    // date summary
                    String date = dataItems[28];
                    updateSummary(dateSummary,date,1,duration,volume,rated,billed);
                    // time summary
                    String time = dataItems[29].substring(0,2);
                    updateSummary(timeSummary,time,1,duration,volume,rated,billed);
                    // cli summary
                    String cli = dataItems[5].trim();
                    String serviceType = dataItems[4].trim();
                    String costCentre = dataItems[8].trim();
                    String cliKey = cli + serviceType + costCentre;
                    updateCliSummary(cliSummary,cliKey,1,duration,volume,rated,billed,cli,serviceType,costCentre);
                    // band summary
                    String chargeBand = dataItems[19].trim();
                    String destination = dataItems[36].trim();
                    String bandKey = chargeBand + destination;
                    updateBandSummary(bandSummary,bandKey,1,duration,volume,rated,billed,chargeBand,destination);
                  }
                }
              }
              // read next line
              cdrLineFull = getBufferedFileLine(cdrBR);
              cdrLineControl = cdrLineFull.substring(1,2);
              cdrLine = cdrLineFull.substring(2,cdrLineFull.length()).trim();
            }
          }
          // Close the CDR file
          if (OK)
            if (!closeBufferedFile(cdrBR))
            {
              logAndOutput("   FILE I/O ERROR : Failed to close CDR file "+cdrFilename+" in the work directory");
              OK = false;
              finalStatus = FAILED;
              finalStatusDetail = "Failed to close file";
            }
          if (OK)
          {
            // check file control totals against DB control totals
            boolean controlTotalsOK = true;
            String controlMessage = "   CONTROLS ERROR: Controls totals mismatch (DB/File) for cdr file "+cdrFilename+" ";
            if (cdrCount!=checkCount)
            {
              controlTotalsOK = false;
              controlMessage = controlMessage + "count:"+cdrCount+"/"+checkCount + " ";
            }
            if (totalDuration!=checkDuration)
            {
              controlTotalsOK = false;
              controlMessage = controlMessage + "duration:"+totalDuration+"/"+checkDuration + " ";
            }
            if (totalVolume!=checkVolume)
            {
              controlTotalsOK = false;
              controlMessage = controlMessage +
                "volume:"+makeLongDecimalString(totalVolume)+"/"+makeLongDecimalString(checkVolume) + " ";
            }
            if (totalRated!=checkRated)
            {
              controlTotalsOK = false;
              controlMessage = controlMessage +
                "rated:"+makeLongDecimalString(totalRated,8)+"/"+makeLongDecimalString(checkRated,8) + " ";
            }
            if (totalBilled!=checkBilled)
            {
              controlTotalsOK = false;
              controlMessage = controlMessage +
                "billed:"+makeLongDecimalString(totalBilled,8)+"/"+makeLongDecimalString(checkBilled,8) + " ";
            }
            if (!controlTotalsOK)
            {
              logAndOutput(controlMessage);
              finalStatus = FAILED;
              finalStatusDetail = controlMessage.substring(3,controlMessage.length());
              OK = false;
              fileIssuesCount++;
            }
          }
          if (OK)
          {
            // For daily file check if account exists, for monthly file also check that invoice exists
            String dbCheckMessage = "";
            boolean dbCheckFail = false;
            accountId = dba.accountId(accountNumber);
            invoiceId = 0;
            if ( (type.equals(M)) && (accountId>0) )
              invoiceId = dba.invoiceId(invoiceNo);
            if ( (accountId==0) || ( (type.equals(M)) && (invoiceId==0) ) )
            {
              dbCheckFail = true;
              if (accountId==0)
              {
                dbCheckMessage = "   DB ERROR: Missing account "+accountNumber+" for CDR file "+cdrFilename;
                accountMissingCount++;
              }
              else
              {
                dbCheckMessage = "   DB ERROR: Missing invoice "+invoiceNo+" for CDR file "+cdrFilename;
                invoiceMissingCount++;
              }
            }
            if (dbCheckFail)
            {
              logAndOutput(dbCheckMessage);
              finalStatus = FAILED;
              finalStatusDetail = dbCheckMessage.substring(3,dbCheckMessage.length());
              OK = false;
            }
          }
          if (OK)
          {
            if (cdrCount > maxRecordsCount)
            {
              emptyDirectory(work2Dir);
              long noFiles = ( cdrCount / maxRecordsCount ) + 1;
              long fileCDRCount = ( cdrCount / noFiles ) + 1;
              long currentFileCount = 0;
              int partNo = 1;
              String partId = getPartId(partNo);
              String partFilename = cdrFilename.substring(0,cdrFilename.length()-4)+"_Part"+partId+csvEXT;
              partBW = openBufferedWriter(work2Dir,partFilename);
              //Open work CDR file
              BufferedReader cdrFileBR = openBufferedFile(workDir,cdrFilename);
              String lineFull= getBufferedFileLine(cdrFileBR);
              String lineControl = lineFull.substring(1,2);
              String line = lineFull.substring(2,lineFull.length()).trim();
              // Process work CDR file
              while (!lineControl.equals("E"))
              {
                writeLine(partBW,line);
                currentFileCount++;
                if (currentFileCount==fileCDRCount)
                {
                  // close part file and open next one
                  closeBufferedWriter(partBW);
                  currentFileCount = 0;
                  partNo++;
                  partId = getPartId(partNo);
                  partFilename = cdrFilename.substring(0,cdrFilename.length()-4)+"_Part"+partId+".csv";
                  partBW = openBufferedWriter(work2Dir,partFilename);
                }
                // read next line in work CDR file
                lineFull= getBufferedFileLine(cdrFileBR);
                lineControl = lineFull.substring(1,2);
                line = lineFull.substring(2,lineFull.length()).trim();
              }
              // Close CDR file
              closeBufferedFile(cdrFileBR);
              // close final part file
              closeBufferedWriter(partBW);
              // Now process each part file
              File w2Dir = new File(work2Dir);
              File[] partFileArray = w2Dir.listFiles();
              for (int i = 0; i < partFileArray.length; i++)
              {
                // Create zip file from part CDR file
                File partCDRFile = partFileArray[i];
                String partCDRFilename = partCDRFile.getName();
                zipFilename = partCDRFilename.substring(0,partCDRFilename.length()-4) + zipEXT;
                fileIds = dba.getAccountIds(accountNumber);
                String attachmentPath =
                  attachDir + File.separator +
                  ARBOR + File.separator +
                  fileIds[2] + File.separator +
                  fileIds[1] + File.separator +
                  accountId + File.separator;
                if (type.equals(M))
                  attachmentPath = attachmentPath + invoiceId + File.separator;
                aPath = attachmentPath;
                String zipFullPath = attachmentPath + zipFilename;
                // Remove existing attachment if it exists
                File zFile = new File(zipFullPath);
                if (zFile.exists())
                  if (!zFile.delete())
                  {
                    logAndOutput("   FILE I/O Error : Failed to delete existing zip file "+zipFullPath);
                    OK = false;
                  }
                // check if attachment directory exists
                if (OK)
                {
                  File aFile = new File(attachmentPath);
                  if (!aFile.exists())
                    if (!aFile.mkdirs())
                    {
                      logAndOutput("   FILE I/O Error : Failed to create attachment directory "+attachmentPath);
                      OK = false;
                    }
                }
                if (OK)
                {
                  // Create zip file for part CDR file
                  try
                  {
                    byte[] buf = new byte[1024];
                    int len;
                    ZipOutputStream zout =
                      new ZipOutputStream(new FileOutputStream(zFile));
                    FileInputStream win = new FileInputStream(partCDRFile);
                    zout.putNextEntry(new ZipEntry(partCDRFilename));
                    while ((len = win.read(buf,0,1024)) > 0)
                    {
                      zout.write(buf,0,len);
                    }
                    zout.closeEntry();
                    win.close();
                    zout.close();
                  }
                  catch(java.io.IOException ex)
                  {
                    logAndOutput("   FILE I/O Error : Failed to zip file "+zipFullPath);
                    OK = false;
                    if (zFile.exists())
                      zFile.delete();
                  }
                }
                // Create attachment for zip file and delete CDR file
                if (OK)
                {
                  String name = DAILY;
                  long id = accountId;
                  String aType = ACCOUNT;
                  long cost = totalRated;
                  String location =
                    ARBOR + FWDSLASH +
                    fileIds[2] + FWDSLASH +
                    fileIds[1] + FWDSLASH +
                    accountId + FWDSLASH;
                  if (type.equals(M))
                  {
                    location = location + invoiceId + FWDSLASH;
                    name = MONTHLY;
                    id = invoiceId;
                    iId = invoiceId;
                    aType = INVOICE;
                    cost = totalBilled;
                  }
                  // add part id onto the displayed name
                  name = name + " Part " + partCDRFilename.substring(partCDRFilename.length() -5, partCDRFilename.length() - 4);
                  lPath = location;
                  location = location + zipFilename;
                  int result =
                    dba.insertArborAttachment
                      (location,name,id,aType,fileDate,zipFilename,cdrCount,totalDuration,cost);
                  if (result==-1)
                  {
                    logAndOutput("   DB ERROR : Failed to insert attachment for "+location);
                    OK = false;
                  }
                  else
                    if (!partCDRFile.delete())
                    {
                      logAndOutput("   FILE I/O ERROR : Failed to delete part CDR file "+partCDRFilename);
                      OK = false;
                    }
                }
              }
              if (OK)
              {
                // Delete original CDR file
                if (!cdrFile.delete())
                  {
                    logAndOutput("   FILE I/O ERROR : Failed to delete original CDR file "+cdrFilename);
                    OK = false;
                  }
                  else
                    successCount++;
              }
            }
            else
            {
              // create single zip file and single attachment
              if (OK)
              {
                // create zip file from CDR file
                zipFilename = cdrFilename.substring(0,cdrFilename.length()-4) + zipEXT;
                fileIds = dba.getAccountIds(accountNumber);
                String attachmentPath =
                  attachDir + File.separator +
                  ARBOR + File.separator +
                  fileIds[2] + File.separator +
                  fileIds[1] + File.separator +
                  accountId + File.separator;
                if (type.equals(M))
                  attachmentPath = attachmentPath + invoiceId + File.separator;
                aPath = attachmentPath;
                String zipFullPath = attachmentPath + zipFilename;
                // Remove existing attachment if it exists
                File zFile = new File(zipFullPath);
                if (zFile.exists())
                  if (!zFile.delete())
                  {
                    logAndOutput("   FILE I/O Error : Failed to delete existing zip file "+zipFullPath);
                    OK = false;
                  }
                // check if attachment directory exists
                if (OK)
                {
                  File aFile = new File(attachmentPath);
                  if (!aFile.exists())
                    if (!aFile.mkdirs())
                    {
                      logAndOutput("   FILE I/O Error : Failed to create attachment directory "+attachmentPath);
                      OK = false;
                    }
                }
                if (OK)
                {
                  try
                  {
                    byte[] buf = new byte[1024];
                    int len;
                    ZipOutputStream zout =
                      new ZipOutputStream(new FileOutputStream(zFile));
                    FileInputStream win = new FileInputStream(cdrFile);
                    zout.putNextEntry(new ZipEntry(cdrFilename));
                    while ((len = win.read(buf,0,1024)) > 0)
                    {
                      zout.write(buf,0,len);
                    }
                    zout.closeEntry();
                    win.close();
                    zout.close();
                  }
                  catch(java.io.IOException ex)
                  {
                    logAndOutput("   FILE I/O Error : Failed to zip file "+zipFullPath);
                    OK = false;
                    if (zFile.exists())
                      zFile.delete();
                  }
                }
              }
              // Create attachment for zip file and delete CDR file
              if (OK)
              {
                String name = DAILY;
                long id = accountId;
                String aType = ACCOUNT;
                long cost = totalRated;
                String location =
                  ARBOR + FWDSLASH +
                  fileIds[2] + FWDSLASH +
                  fileIds[1] + FWDSLASH +
                  accountId + FWDSLASH;
                if (type.equals(M))
                {
                  location = location + invoiceId + FWDSLASH;
                  name = MONTHLY;
                  id = invoiceId;
                  iId = invoiceId;
                  aType = INVOICE;
                  cost = totalBilled;
                }
                lPath = location;
                location = location + zipFilename;
                int result =
                  dba.insertArborAttachment(location,name,id,aType,fileDate,zipFilename,cdrCount,totalDuration,cost);
                if (result==-1)
                {
                  logAndOutput("   DB ERROR : Failed to insert attachment for "+location);
                  OK = false;
                }
                else
                  if (!cdrFile.delete())
                  {
                    logAndOutput("   FILE I/O ERROR : Failed to delete CDR file "+cdrFilename);
                    OK = false;
                  }
                  else
                    successCount++;
              }
            }
          }
          // for monthly files produce summaries
          if ((OK)&&(type.equals(M)))
            produceSummaries
              (accountNumber,
               fileDate,
               cdrCount,
               totalDuration,
               totalVolume,
               totalRated,
               totalBilled,
               aPath,
               lPath,
               iId);
        }
        else
        {
          logAndOutput("   FILE I/O ERROR : Cannot find CDR file "+cdrFilename+" in the work directory");
          fileMissingCount++;
          finalStatus = FAILED;
          finalStatusDetail = "Failed to find file in the work directory";
        }
        // Update the CDR file on the database
        int result = dba.updateArborCDRFile(cdrFileId,finalStatus,finalStatusDetail);
        if (result<0)
          logAndOutput("   DB ERROR : Unable to update cdr file "+cdrFilename+" to status "+finalStatus);
        else
          if (type.equals("D"))
            dailyCDRCount++;
      }
    }
    catch(java.sql.SQLException ex)
    {
      logAndOutput("   DB ERROR: Accessing identified CDR files : "+ex.getMessage());
    }
    try
    {
      iCDRFiles.close();
      iCDRFiles = null;
    }
    catch(java.sql.SQLException ex)
    {
      logAndOutput("   DB ERROR: Failed to close result set of identified CDR files : "+ex.getMessage());
    }
    // run statistics for processing the cdr files
    logAndOutput("   ");
    logAndOutput(" Total number of cdr files identifed         : "+identifiedCount);
    logAndOutput("   ");
    logAndOutput(" Total number of missing cdr files           : "+fileMissingCount);
    logAndOutput("   ");
    logAndOutput(" Total number with missing account           : "+accountMissingCount);
    logAndOutput(" Total number with missing invoice           : "+invoiceMissingCount);
    logAndOutput(" Total number with file content issues       : "+fileIssuesCount);
    logAndOutput("   ");
    logAndOutput(" Total number successfully loaded            : "+successCount);
    // create email for daily CDRs if any have been loaded
    if (dailyCDRCount>0)
    {
      String[] ncArgs = new String[1];
      ncArgs[0] = "Arbor";
      NotifyControl.main(ncArgs);
    }
  }

  private void produceSummaries
    ( String accountNumber,
      String fileDate,
      long totalCalls,
      long totalDuration,
      long totalVolume,
      long totalRated,
      long totalBilled,
      String attachmentPath,
      String locationPath,
      long invoiceId)
  {
    String outputLine = "";
    // Determine summary filenames
    String filenamePrefix = accountNumber + USCORE + fileDate + USCORE;
    String sumFilename = filenamePrefix + sSUM + csvEXT;
    String cliFilename = filenamePrefix + cSUM + csvEXT;
    String bandFilename = filenamePrefix + bSUM + csvEXT;
    // Delete files if they already exist in the work directory
    deleteWorkSummaryFile(sumFilename);
    deleteWorkSummaryFile(cliFilename);
    deleteWorkSummaryFile(bandFilename);
    // produce summary files
    try
    {
      // create cli summary file
      BufferedWriter cliBW = new BufferedWriter(new FileWriter(workDir + File.separator + cliFilename));
      outputLine =
        "Vodafone UK - Summary by Calling Line for"+COMMA+accountNumber+COMMA+
        "Date" + COMMA + fileDate + COMMA +
        "Total Calls" + COMMA + totalCalls + COMMA +
        "Total Duration" + COMMA + totalDuration + COMMA +
        "Total Volume" + COMMA + makeLongDecimalString(totalVolume) + COMMA +
        "Total Rated" + COMMA + makeLongDecimalString(totalRated,8) + COMMA +
        "Total Billed" + COMMA + makeLongDecimalString(totalBilled,8);
      writeLine(cliBW,outputLine);
      outputLine =
        "Service Type" + COMMA +
        "Calling Line" + COMMA +
        "Calls" + COMMA +
        "Duration" + COMMA +
        "Volume" + COMMA +
        "Rated" + COMMA +
        "Billed" + COMMA +
        "Cost Centre";
      writeLine(cliBW,outputLine);
      Vector clV = new Vector(cliSummary.keySet());
      Collections.sort(clV);
      for (Iterator it = clV.iterator(); it.hasNext();)
      {
        String key = (String)it.next();
        ArborCLIDescriptor acd = (ArborCLIDescriptor)cliSummary.get(key);
        outputLine =
          acd.getServiceType() + COMMA +
          "'" + acd.getCli() + COMMA +
          acd.getTotalCalls() + COMMA +
          acd.getTotalDuration()+ COMMA +
          makeLongDecimalString(acd.getTotalVolume()) + COMMA +
          makeLongDecimalString(acd.getTotalRated(),8) + COMMA +
          makeLongDecimalString(acd.getTotalBilled(),8) + COMMA +
          "'" + acd.getCostCentre();
        writeLine(cliBW,outputLine);
      }
      outputLine = "End of Report" + COMMA + COMMA + COMMA + COMMA + COMMA + COMMA + COMMA;
      writeLine(cliBW,outputLine);
      cliBW.close();
      // create band summary
      BufferedWriter bandBW = new BufferedWriter(new FileWriter(workDir + File.separator + bandFilename));
      outputLine =
        "Vodafone UK - Summary of Call Destinations for" + COMMA +
        accountNumber + COMMA +
        "Date" + COMMA + fileDate + COMMA +
        "Total Calls " + COMMA + totalCalls + COMMA +
        "Total Duration " + COMMA + totalDuration + COMMA +
        "Total Volume" + COMMA + makeLongDecimalString(totalVolume) + COMMA +
        "Total Rated" + COMMA + makeLongDecimalString(totalRated,8) + COMMA +
        "Total Billed" + COMMA + makeLongDecimalString(totalBilled,8);
      writeLine(bandBW,outputLine);
      outputLine =
        "Charge Band" + COMMA +
        "Destination" + COMMA +
        "Calls " + COMMA +
        "Duration " + COMMA +
        "Volume" + COMMA +
        "Rated" + COMMA +
        "Billed" + COMMA;
      writeLine(bandBW,outputLine);
      Vector bV = new Vector(bandSummary.keySet());
      Collections.sort(bV);
      for (Iterator it = bV.iterator(); it.hasNext();)
      {
        String key = (String)it.next();
        ArborBandDescriptor abd = (ArborBandDescriptor)bandSummary.get(key);
        outputLine =
          "'" + abd.getChargeBand() + COMMA +
          DBLQUOTE + abd.getDestination() + DBLQUOTE + COMMA +
          abd.getTotalCalls() + COMMA +
          abd.getTotalDuration() + COMMA +
          makeLongDecimalString(abd.getTotalVolume()) + COMMA +
          makeLongDecimalString(abd.getTotalRated(),8) + COMMA +
          makeLongDecimalString(abd.getTotalBilled(),8);
          writeLine(bandBW,outputLine);
      }
      outputLine = "End of Report" + COMMA + COMMA + COMMA + COMMA + COMMA + COMMA;
      writeLine(bandBW,outputLine);
      bandBW.close();
      // create date/time summary
      BufferedWriter sumBW = new BufferedWriter(new FileWriter(workDir + File.separator + sumFilename));
      outputLine =
        "Vodafone UK - Summary by Date and General for" + COMMA +
        accountNumber + COMMA +
        "Date" + COMMA + fileDate + COMMA +
        "Total Calls " + COMMA + totalCalls + COMMA +
        "Total Duration " + COMMA + totalDuration + COMMA +
        "Total Volume" + COMMA + makeLongDecimalString(totalVolume) + COMMA +
        "Total Rated" + COMMA + makeLongDecimalString(totalRated,8) + COMMA +
        "Total Billed" + COMMA + makeLongDecimalString(totalBilled,8);
      writeLine(sumBW,outputLine);
      outputLine = COMMA + COMMA + COMMA + COMMA + COMMA;
      writeLine(sumBW,outputLine);
      outputLine =
        "Time (hh:00)" + COMMA +
        "Calls" + COMMA +
        "Duration" + COMMA +
        "Volume" + COMMA +
        "Rated" + COMMA +
        "Billed";
      writeLine(sumBW,outputLine);
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
          ArborSumDescriptor asd = new ArborSumDescriptor(testHour,0,0,0,0,0);
          timeSummary.put(testHour,asd);
        }
      }
      //process time hastable
      Vector tV = new Vector(timeSummary.keySet());
      Collections.sort(tV);
      for (Iterator it = tV.iterator(); it.hasNext();)
      {
        String key = (String)it.next();
        ArborSumDescriptor asd = (ArborSumDescriptor)timeSummary.get(key);
        outputLine =
          "'" + asd.getId() + ":00" + COMMA +
          asd.getTotalCalls()  + COMMA +
          asd.getTotalDuration() + COMMA +
          makeLongDecimalString(asd.getTotalVolume()) + COMMA +
          makeLongDecimalString(asd.getTotalRated(),8) + COMMA +
          makeLongDecimalString(asd.getTotalBilled(),8);
        writeLine(sumBW,outputLine);
      }
      outputLine = COMMA + COMMA + COMMA;
      writeLine(sumBW,outputLine);
      outputLine =
        "Date (dd/mm/yyyy)" + COMMA +
        "Calls" + COMMA +
        "Duration" + COMMA +
        "Volume" + COMMA +
        "Rated" + COMMA +
        "Billed";
      writeLine(sumBW,outputLine);
      // process date hashtable
      Vector dV = new Vector(dateSummary.keySet());
      Collections.sort(dV);
      for (Iterator it = dV.iterator(); it.hasNext();)
      {
        String key = (String)it.next();
        ArborSumDescriptor asd = (ArborSumDescriptor)dateSummary.get(key);
        outputLine =
          "'" + asd.getId() + COMMA +
          asd.getTotalCalls()  + COMMA +
          asd.getTotalDuration() + COMMA +
          makeLongDecimalString(asd.getTotalVolume()) + COMMA +
          makeLongDecimalString(asd.getTotalRated(),8) + COMMA +
          makeLongDecimalString(asd.getTotalBilled(),8);
        writeLine(sumBW,outputLine);
      }
      outputLine = "End of Report"+ COMMA + COMMA + COMMA + COMMA + COMMA;
      writeLine(sumBW,outputLine);
      sumBW.close();
      // create summary zip file and copy in the three summary files
      String summaryZipFilename = filenamePrefix + cdrSUM + zipEXT;
      File szFile = new File(attachmentPath + summaryZipFilename);
      if (szFile.exists())
        szFile.delete();

      try
      {
        byte[] buf = new byte[1024];
        int len;
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(szFile));
        FileInputStream winCli = new FileInputStream(workDir + File.separator + cliFilename);
        zout.putNextEntry(new ZipEntry(cliFilename));
        while ((len = winCli.read(buf,0,1024)) >0)
        {
          zout.write(buf,0,len);
        }
        zout.closeEntry();
        FileInputStream winBand = new FileInputStream(workDir + File.separator + bandFilename);
        zout.putNextEntry(new ZipEntry(bandFilename));
        while ((len = winBand.read(buf,0,1024)) >0)
        {
          zout.write(buf,0,len);
        }
        zout.closeEntry();
        FileInputStream winSum = new FileInputStream(workDir + File.separator + sumFilename);
        zout.putNextEntry(new ZipEntry(sumFilename));
        while ((len = winSum.read(buf,0,1024)) >0)
        {
          zout.write(buf,0,len);
        }
        zout.closeEntry();
        zout.close();
        winCli.close();
        winBand.close();
        winSum.close();
      }
      catch(java.io.IOException ex)
      {
        // any failures in creating zip file make sure it is deleted
        logAndOutput("   FILE I/O ERROR: ZIP Summary creation failed : "+ex.getMessage());
        if (szFile.exists())
          szFile.delete();
      }
      // delete the three summary files
      deleteWorkSummaryFile(sumFilename);
      deleteWorkSummaryFile(cliFilename);
      deleteWorkSummaryFile(bandFilename);
      // create the attachment for the summary file
       if (dba.insertArborSumAttachment(locationPath+summaryZipFilename,SUMMARY,invoiceId,fileDate)==-1)
        logAndOutput("   DB ERROR : Failed to create attachment for CDR summary file "+"locationPath+summaryZipFilename");
    }
    catch(Exception ex)
    {
      logAndOutput
        ("   SUMMARY ERROR: Error producing summaries for account "+accountNumber+" for "+fileDate+" : "+ex.getMessage());
    }
  }

  private void deleteWorkSummaryFile (String filename)
  {
    File sFile = new File(workDir + File.separator + filename);
    if (sFile.exists())
      sFile.delete();
  }

  // process control and cdr files dropped into the drop directory
  private void processDroppedFiles()
  {
    // Processing totals
    int controlFileCount = 0, duplicateCount = 0, rejectCount = 0, emptyCount = 0;
    int cdrFileCount = 0, orphanCDRCount = 0, processedCDRs = 0, missingCDRs = 0;
    // Process all the control files held in the drop directory. Set up each
    // control file on the DB with identified status unless it already exists
    // on the database where it would set up with duplicate status instead
    File dropDirectory = new File(dropDir);
    FilenameFilter controlFilter = new FilenameFilter()
    {
      public boolean accept(File dir, String name)
      {
        return name.endsWith(cfEXT);
      }
    };
    File[] controlFileArray = dropDirectory.listFiles(controlFilter);
    logAndOutput("Stage one - control file processing");
    logAndOutput("===================================");
    if (controlFileArray.length>0)
      logAndOutput("   ");
    for (int i = 0; i < controlFileArray.length; i++)
    {
      boolean OK = true, cdrProcessingOK = true, controlFileEmpty = true;
      int cdrCount = 0, existingCount = 0;
      String controlFilename = controlFileArray[i].getName();
      long controlFileId = 0;
      controlFileCount++;
      // Identify the date and type of the control file
      String controlFileDate = controlFilename.substring(0,8);
      String controlFileType = X;
      if (controlFilename.endsWith(dcfEXT))
        controlFileType = D;
      else if (controlFilename.endsWith(mcfEXT))
        controlFileType = M;
      if (controlFileType.equals(X))
      {
        OK = false;
        logAndOutput("   Cannot process control file "+controlFilename+" with invalid type");
      }
      // check for duplicate
      if (OK)
      {
        status = IDENTIFIED;
        String statusDetail = NULL;
        existingCount = dba.arborControlFileExists(controlFilename);
        if (existingCount==-1)
        {
          status = FAILED;
          statusDetail = "Unable to determine if "+controlFilename+" already exists";
          OK = false;
        }
        else if (existingCount>0)
        {
          status = DUPLICATE;
          statusDetail = "   "+existingCount;
          if (existingCount==1)
            statusDetail = statusDetail+" instance has already been processed";
          else
            statusDetail = statusDetail+" instances have already been processed";
        }
        // create initial DB record for the control file
        if (OK)
        {
          controlFileId =
            dba.insertArborControlFile
              (controlFilename,
               controlFileType,
               controlFileDate,
               status,
               statusDetail);
          if (controlFileId<=0)
          {
            OK = false;
            logAndOutput("   DB ERROR: Unable to create Arbor_Control_File for "+controlFilename);
          }
        }
      }
      if (OK)
      {
        // Process contents of the control file
        BufferedReader cfBR = openBufferedFile(dropDir,controlFilename);
        if (cfBR==null)
          OK = false;
        else
        {
          String cfLineFull = getBufferedFileLine(cfBR);
          String cfLineStatus = cfLineFull.substring(1,2);
          String cfLine = cfLineFull.substring(2,cfLineFull.length()).trim();
          if (cfLineStatus.equals("F"))
            cdrProcessingOK = false;
          else
          {
            // process lines until end of file
            while (!cfLineStatus.equals("E"))
            {
              // If the CDR file does not already exist on the DB then set up
              // the DB status as identified and move the file into the work
              // directory. If it does exist on the DB set up the DB status as
              // duplicate and move it into the the work directory. If the CDR
              // file does not exist in the drop directory set up the DB status
              // as failed
              String[] cfDataArray = getLineData(cfLine);
              String accountNumber = cfDataArray[1];
              if (!accountNumber.equals(EMPTY))
              {
                controlFileEmpty = false;
                cdrCount++;
                cdrFileCount++;
                String invoiceNo = cfDataArray[2];
                if (invoiceNo.length()==0)
                  invoiceNo = NULL;
                long recordCount = makeStringLong(cfDataArray[3]);
                long totalDuration = makeStringLong(cfDataArray[4]);
                // these three values will be multiplied by 100 and stored as longs
                long totalVolume = makeStringDecimalLong(cfDataArray[5]);
                long totalRatedAmount = makeStringDecimalLong(cfDataArray[6],8);
                long totalBilledAmount = makeStringDecimalLong(cfDataArray[7],8);
                // work out the CDR file name
                String cdrFilename = accountNumber+USCORE+controlFileDate+USCORE;
                if (controlFileType.equals("D"))
                  cdrFilename = cdrFilename+dEXT;
                else
                  cdrFilename = cdrFilename+mEXT;
                // check if the CDR filename already exists on the database
                int existsCount = dba.arborCDRFileExists(cdrFilename);
                String cdrStatus = IDENTIFIED;
                String cdrStatusDetail = NULL;
                if (existsCount==-1)
                {
                  cdrStatus = FAILED;
                  cdrStatusDetail = "Unable to determine if "+cdrFilename+" already exists";
                  cdrProcessingOK = false;
                }
                else if (existsCount>0)
                {
                  cdrStatus = DUPLICATE;
                  cdrStatusDetail = "   "+existsCount;
                  if (existsCount==1)
                    cdrStatusDetail = cdrStatusDetail+" instance has already been processed";
                  else
                    cdrStatusDetail = cdrStatusDetail+" instance have already been processed";
                }
                // move CDR file
                if (!cdrStatus.equals(FAILED))
                {
                  File workCDRFile = new File(workDir+File.separator+cdrFilename);
                  if (workCDRFile.exists())
                  {
                    cdrProcessingOK = false;
                    cdrStatus = FAILED;
                    cdrStatusDetail = "Cannot move file to work directory as it already exists";
                  }
                  else
                  {
                    File currentCDRFile = new File(dropDir+File.separator+cdrFilename);
                    if (currentCDRFile.exists())
                    {
                      if (!currentCDRFile.renameTo(workCDRFile))
                      {
                        cdrProcessingOK = false;
                        cdrStatus = FAILED;
                        cdrStatusDetail = "Failed to move file to work directory";
                      }
                    }
                    else
                    {
                      cdrProcessingOK = false;
                      cdrStatus = FAILED;
                      cdrStatusDetail = "File not present in drop directory";
                      missingCDRs++;
                    }
                  }
                }
                //set up db entry for CDR file
                long cdrFileId =
                  dba.insertArborCDRFile
                    (controlFileId,
                     cdrFilename,
                     controlFileType,
                     controlFileDate,
                     accountNumber,
                     invoiceNo,
                     recordCount,
                     totalDuration,
                     totalVolume,
                     totalRatedAmount,
                     totalBilledAmount,
                     cdrStatus,
                     cdrStatusDetail);
                if (cdrFileId<=0)
                {
                  cdrProcessingOK = false;
                  logAndOutput("   DB ERROR: Unable to create Arbor_CDR_File for "+cdrFilename);
                }
                else
                  processedCDRs++;
              }
              // get next line
              cfLineFull = getBufferedFileLine(cfBR);
              cfLineStatus = cfLineFull.substring(1,2);
              cfLine = cfLineFull.substring(2,cfLineFull.length()).trim();
            }
            if (!closeBufferedFile(cfBR))
              cdrProcessingOK = false;
          }
        }
      }
      if (cdrCount==0)
      {
        controlFileEmpty=true;
        cdrProcessingOK=false;
      };
      // At the end of processing the control file if it is empty reset the
      // status on the DB to empty and move it into the reject directory
      // If there is a problem with processing any of it's CDR files reset it's
      // DB status to failed and move it into the reject directory
      // Otherwise it is OK so reset it's status to processed (unless it is
      // currently a duplicate where it is moved into the reject directory) and
      // move it into the processed directory
      if (OK)
      {
        File currentControlFile = new File(dropDir+File.separator+controlFilename);
        String newControlFileStatus = PROCESSED;
        String newControlFileStatusDetail = NULL;
        if (cdrProcessingOK)
        {
          if (status.equals(DUPLICATE))
          {
            controlFilename = controlFilename.substring(0,controlFilename.length()-4)+USCORE+(existingCount+1)+".csv";
            newControlFileStatus = DUPLICATE;
          }
          File newControlFile = new File(procDir+File.separator+controlFilename);
          if (newControlFile.exists())
          {
            logAndOutput("   FILE I/O ERROR: Cannot move control file "+controlFilename+" as file already exists");
            newControlFileStatus = FAILED;
            newControlFileStatusDetail = "File already present in processed directory";
          }
          else
          {
            if (!currentControlFile.renameTo(newControlFile))
            {
              logAndOutput("   FILE I/O ERROR: Cannot move control file "+controlFilename+" to processed directory "+procDir);
              newControlFileStatus = FAILED;
              newControlFileStatusDetail = "Unable to move control file to processed directory";
            }
            else
              if (status.equals(DUPLICATE))
                duplicateCount++;
          }
        }
        else if (controlFileEmpty)
        {
          newControlFileStatus = EMPTY;
          File newControlFile = new File(rejDir+File.separator+controlFilename);
          if (newControlFile.exists())
          {
            logAndOutput
              ("   FILE I/O ERROR: Cannot move control file "+controlFilename+" to reject directory as file already exists");
            newControlFileStatus = FAILED;
            newControlFileStatusDetail = "Empty control file already present in reject directory";
          }
          else
          {
            if (!currentControlFile.renameTo(newControlFile))
            {
              logAndOutput("   FILE I/O ERROR: Cannot move control file "+controlFilename+" to reject directory "+rejDir);
              newControlFileStatus = FAILED;
              newControlFileStatusDetail = "Unable to move control file to reject directory";
            }
            else
              emptyCount++;
          }
        }
        else
        {
          newControlFileStatus = FAILED;
          File newControlFile = new File(rejDir+File.separator+controlFilename);
          if (newControlFile.exists())
          {
            logAndOutput
              ("   FILE I/O ERROR: Cannot move control file "+controlFilename+" to reject directory as file already exists");
            newControlFileStatusDetail = "Rejected control file already present in reject directory";
          }
          else
          {
            if (!currentControlFile.renameTo(newControlFile))
            {
              logAndOutput("   FILE I/O ERROR: Cannot move control file "+controlFilename+" to reject directory "+rejDir);
              newControlFileStatusDetail = "Unable to move rejected control file to reject directory";
            }
            else
              rejectCount++;
          }
        }
        // Update DB status of control file
        int result =
          dba.updateArborControlFile(controlFileId,newControlFileStatus,newControlFileStatusDetail);
        if (result<0)
        {
          logAndOutput("   DB ERROR : Unable to update control file "+controlFilename);
          OK = false;
        }
      }
    }
    // Pick up any orphan CDR files in the drop directory, setting them
    // up on the DB with orphan status and move into the orphan directory
    FilenameFilter cdrFilter = new FilenameFilter()
    {
      public boolean accept(File dir, String name)
      {
        return name.endsWith(cdrEXT);
      }
    };
    File[] cdrFileArray = dropDirectory.listFiles(cdrFilter);
    for (int i = 0; i < cdrFileArray.length; i++)
    {
      String cdrFilename = cdrFileArray[i].getName();
      // Identify the date and type of the CDR file
      String accountNumber = cdrFilename.substring(0,8);
      String cdrFileDate = cdrFilename.substring(9,17);
      String cdrFileType = X;
      if (cdrFilename.endsWith(dEXT))
        cdrFileType = D;
      else if (cdrFilename.endsWith(mEXT))
        cdrFileType = M;
      // move to the orphan directory
      File currentFile = new File(dropDir+File.separator+cdrFilename);
      File newFile = new File(orphanDir+File.separator+cdrFilename);
      if (newFile.exists())
        logAndOutput
          ("   FILE I/O ERROR: Cannot move orphan CDR file "+cdrFilename+" as it already exists in the orphan directory "+orphanDir);
       else
      {
        if(!currentFile.renameTo(newFile))
        {
          logAndOutput("   FILE I/O ERROR: Cannot move orphan CDR file "+cdrFilename+" to orphan directory "+orphanDir);
        }
        else
        {
          long cdrFileId =
            dba.insertArborCDRFile
              (0,
               cdrFilename,
               cdrFileType,
               cdrFileDate,
               accountNumber,
               NULL,
               0,
               0,
               0,
               0,
               0,
               ORPHAN,
               NULL);
          if (cdrFileId<=0)
            logAndOutput("   DB ERROR: Failed to create DB record for orphan cdr file "+cdrFilename);
          else
            orphanCDRCount++;
        }
      }
    }
    // run statistics for processing the files in the drop directory
    logAndOutput("   ");
    logAndOutput(" Total number of control files processed     : "+controlFileCount);
    logAndOutput("   ");
    logAndOutput(" Total number of empty control files         : "+emptyCount);
    logAndOutput(" Total number of duplicate control files     : "+duplicateCount);
    logAndOutput(" Total number of failed control files        : "+rejectCount);
    logAndOutput("   ");
    logAndOutput(" Total number of CDR files identified        : "+cdrFileCount);
    logAndOutput("   ");
    logAndOutput(" Total number of missing CDR files           : "+missingCDRs);
    logAndOutput(" Total number of orphan CDR files            : "+orphanCDRCount);
    logAndOutput("   ");
    logAndOutput("Stage two - cdr file processing");
    logAndOutput("===================================");
  }

  private arborCDRprocessing()
  {
    su = new StringUtil();
    dba = new DBAccess();
    // get parameters from properties files
    logDir=EBANProperties.getEBANProperty(EBANProperties.ARBORCDRLOGDIR);
    dropDir=EBANProperties.getEBANProperty(EBANProperties.ARBORCDRDROPDIR);
    procDir=EBANProperties.getEBANProperty(EBANProperties.ARBORCDRPROCDIR);
    rejDir=EBANProperties.getEBANProperty(EBANProperties.ARBORCDRREJDIR);
    attachDir=EBANProperties.getEBANProperty(EBANProperties.ARBORCDRATTACHDIR);
    workDir=EBANProperties.getEBANProperty(EBANProperties.ARBORCDRWORKDIR);
    work2Dir=EBANProperties.getEBANProperty(EBANProperties.ARBORCDRWORK2DIR);
    orphanDir=EBANProperties.getEBANProperty(EBANProperties.ARBORCDRORPHANDIR);
    failDir=EBANProperties.getEBANProperty(EBANProperties.ARBORCDRFAILDIR);
    maxRecordsCount=Long.parseLong(EBANProperties.getEBANProperty(EBANProperties.ARBORCDRMAXRECORDSCOUNT));
  }

  // open a BuffferedReader for a file
  private BufferedReader openBufferedFile(String directory, String filename)
  {
    // open file for reading
    BufferedReader readFile = null;
    try
    {
      readFile = new BufferedReader(new FileReader(directory+File.separator+filename));
    }
    catch (Exception ex)
    {
      logAndOutput("Failed to open file "+directory+File.separator+filename+" : "+ex.getMessage());
    }
    return readFile;
  }

  // get next line from a BufferedReader
  private String getBufferedFileLine(BufferedReader readFile)
  {
    String fileLine = "F";
    try
    {
      fileLine = readFile.readLine();
      if (fileLine==null)
        fileLine = "E";
      else
        fileLine = " "+fileLine;
    }
    catch (Exception ex)
    {
      logAndOutput("Failed to read BufferedReader : "+ex.getMessage());
    }
    return " "+fileLine;
  }

  // close a BufferedReader
  private boolean closeBufferedFile(BufferedReader readFile)
  {
    // close file
    boolean result = false;
    try
    {
      readFile.close();
      result = true;
    }
    catch (Exception ex)
    {
      logAndOutput("Failed to close BufferedReader : "+ex.getMessage());
    }
    return result;
  }

  // open a BuffferedWriter
  private BufferedWriter openBufferedWriter(String directory, String filename)
  {
    // open file for writer
    BufferedWriter writeFile = null;
    try
    {
      writeFile = new BufferedWriter(new FileWriter(directory+File.separator+filename));
    }
    catch (Exception ex)
    {
      logAndOutput("Failed to open file "+directory+File.separator+filename+" : "+ex.getMessage());
    }
    return writeFile;
  }

  // write next line for a bufferedWriter
  private String writeBufferedLine(BufferedWriter writeFile, String fileLine)
  {
    try
    {
      writeFile.write(fileLine);
    }
    catch (Exception ex)
    {
      logAndOutput("Failed to write BufferedWriter : "+ex.getMessage());
    }
    return " "+fileLine;
  }

   // close a BufferedWriter
  private boolean closeBufferedWriter(BufferedWriter writeFile)
  {
    // close file
    boolean result = false;
    try
    {
      writeFile.close();
      result = true;
    }
    catch (Exception ex)
    {
      logAndOutput("Failed to close BufferedWriter : "+ex.getMessage());
    }
    return result;
  }

  // open log file
  private void openLogFile()
  {
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      runTS = su.reformatDate("now", null, DTFMT);
      logFile = new File(logDir + File.separator + runTS + "_arborCDRprocessing_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("arborCDRprocessing started at " +
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

  // at end of program close log file
  private void closeLogFile()
  {
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile(" ");
      writeToLogFile("arborCDRprocessing ended at " +
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

  // check process is not running and if not set run control to prevent duplicate running
  private boolean checkRunControl()
  {
    boolean result = false;
    String status = dba.getRunControlStatus(ARBOR,ACP);
    if (status.startsWith(NOTAVAILABLE))
      logAndOutput("!!!Expected job control row for this process is missing!!!");
    else
      if (status.startsWith(ACTIVE))
        logAndOutput("!!!this process is already running!!!");
      else
        if (dba.updateRunControlStatus(ARBOR,ACP,ACTIVE))
          result = true;
        else
          logAndOutput("!!!cannot set run control for this process!!!");
    return result;
  }

  // reset run control to allow future running of this process
  private void resetRunControl()
  {
    if (!dba.updateRunControlStatus(ARBOR,ACP,INACTIVE))
      logAndOutput("!!!failed to reset run control for this process to inactive!!!");
  }

  // send message to log and standard output
  private void logAndOutput(String message)
  {
    writeToLogFile(message);
    System.out.println(message);
  }

  // unpacks data held in csv quote delimted format into a string array
  private String[] getLineData(String linedata)
  {
    String[] dataArray = new String [100];
    // test for empty file where there is no data or account numner starts
    // with a space
    boolean empty = false;
    if (linedata.length()==0)
      empty = true;
    else
      if (linedata.substring(0,1).equals(SPACE))
        empty = true;
    if (empty)
    {
      dataArray[1]=EMPTY;
    }
    else
    {
      String work = "";
      int arrayPos = 1; // note ignore 0 position in array
      boolean newItem = true;
      // scan through line data
      for (int i=0; i<linedata.length(); i++)
      {
        String testChar = linedata.substring(i,i+1);
        if (newItem)
        {
          if (testChar.equals(DBLQUOTE))
            newItem=false;
          if (testChar.equals(COMMA))
          {
            // ignore commas outside data
          }
          else
          {
            newItem = false;
            work = testChar;
          }
        }
        else
        {
          if (testChar.equals(DBLQUOTE))
          {
            // ignore double quote, remove initial double quote from item
            // and rest to look for new item
            work = work.substring(1,work.length());
            dataArray[arrayPos]=work;
            //System.out.println(arrayPos+" : "+work);
            work = "";
            arrayPos++;
            newItem = true;
          }
          else
            // keep building new item in the array
            work = work+testChar;
        }
      }
    }
    return dataArray;
  }

  // converts a string integer 999999999 format into a long
  private long makeStringLong( String amount)
  {
    long result = 0;
    if (!(amount==null))
      if (amount.length()>0)
        result = Long.parseLong(amount);
    return result;
  }

  // converts a string decimal in 999990.00 format into a long multiplied by 100
  private long makeStringDecimalLong( String amount)
  {
    String noDecimalPoint = "0";
    if (!(amount==null))
      if (amount.length()>3)
      {
        noDecimalPoint =
          amount.substring(0,amount.length()-3)+
          amount.substring(amount.length()-2,amount.length());
      }
    long result = Long.parseLong(noDecimalPoint);
    return result;
  }

  // converts a string decimal into a long
  private long makeStringDecimalLong( String amount, int decPlaces)
  {
    String noDecimalPoint = "0";
    if (!(amount==null))
      if (amount.length()>decPlaces+1)
      {
        noDecimalPoint =
          amount.substring(0,amount.length()-decPlaces-1)+
          amount.substring(amount.length()-decPlaces,amount.length());
      }
    long result = Long.parseLong(noDecimalPoint);
    return result;
  }

  // format decimal stored as long to 99999990.99
  private String makeLongDecimalString( long amount)
  {
    String result = "";
    String work = Long.toString(amount);
    if (amount>99)
    {
      result = work.substring(0,work.length()-2)+"."+work.substring(work.length()-2,work.length());
    }
    else if (amount>9)
    {
      result = "0."+work;
    }
    else
    {
      result = "0.0"+work;
    }
    return result;
  }

  // format decimal stored as long to formatted string
  private String makeLongDecimalString( long amount, int decPlaces)
  {
    String result = "";
    String work = Long.toString(amount);
    int workLength = work.length();
    if (workLength > decPlaces)
      result = work.substring(0,workLength-decPlaces)+"."+work.substring(workLength-decPlaces,workLength);
    else
    {
      result = "0.";
      int loopCount = decPlaces-workLength;
      for (int i=0; i<loopCount ;i++)
      {
        result = result + "0";
      }
      result = result + work;
    }
    return result;
  }

  // Update date or time summary
  private void updateSummary
    (Hashtable summary, String id, long calls, long duration, long volume, long rated, long billed)
  {
    ArborSumDescriptor asd = null;
    if (summary.containsKey(id))
    {
      asd = (ArborSumDescriptor)summary.get(id);
      asd.incrementTotalCalls(calls);
      asd.incrementTotalDuration(duration);
      asd.incrementTotalVolume(volume);
      asd.incrementTotalRated(rated);
      asd.incrementTotalBilled(billed);
    }
    else
    {
      asd = new ArborSumDescriptor(id,calls,duration,volume,rated,billed);
    }
    summary.put(id,asd);
  }

  // Update cli summary
  private void updateCliSummary
    (Hashtable summary, String id, long calls, long duration, long volume, long rated, long billed,
     String cli, String serviceType, String costCentre)
  {
    ArborCLIDescriptor acd = null;
    if (summary.containsKey(id))
    {
      acd = (ArborCLIDescriptor)summary.get(id);
      acd.incrementTotalCalls(calls);
      acd.incrementTotalDuration(duration);
      acd.incrementTotalVolume(volume);
      acd.incrementTotalRated(rated);
      acd.incrementTotalBilled(billed);
    }
    else
    {
      acd = new ArborCLIDescriptor(id,calls,duration,volume,rated,billed,cli,serviceType,costCentre);
    }
    summary.put(id,acd);
  }

  // Update band summary
  private void updateBandSummary
    (Hashtable summary, String id, long calls, long duration, long volume, long rated, long billed,
     String chargeBand, String destination)
  {
    ArborBandDescriptor abd = null;
    if (summary.containsKey(id))
    {
      abd = (ArborBandDescriptor)summary.get(id);
      abd.incrementTotalCalls(calls);
      abd.incrementTotalDuration(duration);
      abd.incrementTotalVolume(volume);
      abd.incrementTotalRated(rated);
      abd.incrementTotalBilled(billed);
    }
    else
    {
      abd = new ArborBandDescriptor(id,calls,duration,volume,rated,billed,chargeBand,destination);
    }
    summary.put(id,abd);
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

    private String getPartId ( int partCount)
  {
    String partId = "";
    int asciiCode = partCount + 64;
    switch (asciiCode)
    {
      case 65: partId = "A";
        break;
      case 66: partId = "B";
        break;
      case 67: partId = "C";
        break;
      case 68: partId = "D";
        break;
      case 69: partId = "E";
        break;
      case 70: partId = "F";
        break;
      case 71: partId = "G";
        break;
      case 72: partId = "H";
        break;
      case 73: partId = "I";
        break;
      case 74: partId = "J";
        break;
      case 75: partId = "K";
        break;
      case 76: partId = "L";
        break;
      case 77: partId = "M";
        break;
      case 78: partId = "N";
        break;
      case 79: partId = "O";
        break;
      case 80: partId = "P";
        break;
      case 81: partId = "Q";
        break;
      case 82: partId = "R";
        break;
      case 83: partId = "S";
        break;
      case 84: partId = "T";
        break;
      case 85: partId = "U";
        break;
      case 86: partId = "V";
        break;
      case 87: partId = "W";
        break;
      case 88: partId = "X";
        break;
      case 89: partId = "Y";
        break;
      case 90: partId = "Z";
        break;
      default: partId = "?";
    }
    return partId;
  }

  private void emptyDirectory(String dir)
  {
    File w2Dir = new File(dir);
    File[] fileArray = w2Dir.listFiles();
    for (int i = 0; i < fileArray.length; i++)
    {
      File f = fileArray[i];
      if (!f.delete())
        System.out.println("Failed to delete file"+f.getName()+" from directory "+dir);
    }
  }

  // initial program control
  public static void main(String[] args)
  {
    // control processing
    arborCDRprocessing acp = new arborCDRprocessing();
    acp.openLogFile();
    if (acp.checkRunControl())
    {
      acp.processDroppedFiles();
      acp.resetRunControl();
      acp.processCDRFiles();
    }
    acp.closeLogFile();
  }

}



