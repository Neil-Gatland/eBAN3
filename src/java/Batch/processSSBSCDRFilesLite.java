package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;
import java.util.zip.*;
import java.math.BigDecimal;

public class processSSBSCDRFilesLite
{

  // class variables
  private String downDir, dropDir, invDir, logDir, workDir, work2Dir, attachDir;
  private String runTS;
  private StringUtil su;
  private DBAccess dba;
  private BufferedWriter logWriter, workWriter;
  private File logFile, commandFile;
  private int accountId, paymentGroupId, customerId;
  private boolean processedDailyFiles;
  // class constants
  private final String SSBS = "SSBS";
  private final String PSCF = "Process SSBS CDR Files";
  private final String IFR = "Identify FTP Requests";
  private final String PFR = "Process FTP Requests";
  private final String NOTAVAILABLE = "Not available";
  private final String ACTIVE = "Active";
  private final String INACTIVE = "Inactive";
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String BACKSLASH = "\\";
  private final String FWDSLASH = "/";
  private final String PERIOD = ".";
  private final String COMMA = ",";
  private final String DBLQUOTE = "\"";
  private final String HYPHEN = "-";
  private final String PSTN = "PSTN";
  private final String TNBS = "TNBS";
  private final String ALARM = "Alarm-call";
  private final String DAILY = "DAILY";
  private final String TDAILY = "TDAILY";
  private final String USCORE = "_";
  private final String CDRS = "cdrs";
  private final String CSV = "csv";
  private final String ZIP = "zip";
  private final String TW = "TW";
  private final String CABLEDIRECT = "CABLE DIRECT";
  private final String PSTNANAME = "SSBS Monthly Invoiced PSTN CDRs";
  private final String DAILYANAME = "SSBS Daily PSTN CDRs";
  private final String TDAILYANAME = "SSBS Daily TNBS CDRs";
  private final String TNBSANAME = "SSBS Monthly Invoiced TNBS CDRs";
  private final String TNBSENDOFMONTHANAME = "SSBS End of Month TNBS CDRs";
  private final String ALARMANAME = "SSBS Alarm-call";
  private final int maxFileSize;
  private double vatRate = 1 +
          (Double.parseDouble(EBANProperties.getEBANProperty(EBANProperties.VATRATE))/100);
  private String spiritType;
  private final String TEL = "TEL";
  private final String ROUNDING = "ROUNDING";
  private final String CALLCHARGES = "Call Charges";
  private final String ROUNDINGCORR = "Rounding correction";

  private boolean checkRunControl()
  {
    boolean result = false;
    String status = dba.getRunControlStatus(SSBS,PSCF), message="";
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
        status = dba.getRunControlStatus(SSBS,IFR);
        if (!status.startsWith(INACTIVE))
        {
          message = "!!!cannot run while process Identify FTP Requests is running!!!";
          writeToLogFile(message);
          System.out.println(message);
        }
        else
        {
          status = dba.getRunControlStatus(SSBS,PFR);
          if (status.startsWith(ACTIVE))
          {
            message = "!!!cannot run while process Process FTP Requests is running!!!";
            writeToLogFile(message);
            System.out.println(message);
          }
          else
          {
            if (dba.updateRunControlStatus(SSBS,PSCF,ACTIVE))
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
    if (!dba.updateRunControlStatus(SSBS,PSCF,INACTIVE))
    {
      String message = "!!!failed to reset run control for this process to inactive!!!";
      writeToLogFile(message);
      System.out.println(message);
    }
  }

  private processSSBSCDRFilesLite()
  {
    su = new StringUtil();
    dba = new DBAccess();
    // get parameters from properties files
    downDir = EBANProperties.getEBANProperty(EBANProperties.PSCFDOWNDIR);
    dropDir = EBANProperties.getEBANProperty(EBANProperties.PSCFCDRDIR);
    invDir = EBANProperties.getEBANProperty(EBANProperties.PSCFINVALIDDIR);
    logDir = EBANProperties.getEBANProperty(EBANProperties.PSCFLOGDIR);
    workDir = EBANProperties.getEBANProperty(EBANProperties.PSCFWORKDIR);
    work2Dir = EBANProperties.getEBANProperty(EBANProperties.PSCFWORK2DIR);
    attachDir = EBANProperties.getEBANProperty(EBANProperties.PSCFATTACHDIR);
    maxFileSize =  Integer.parseInt(EBANProperties.getEBANProperty(EBANProperties.PSCFMAXSIZE));
  }

  private String truncatedAccountNumber( String accountNumber )
  {
    String result = "";
    boolean endLeadingZeroes = false;
    for (int i=0; i<accountNumber.length(); i++)
    {
      String test = accountNumber.substring(i,i+1);
      if (!test.startsWith("0"))
      {
        result = result + test;
        endLeadingZeroes = true;
      }
      else
      {
        if (endLeadingZeroes)
          result = result + test;
      }
    }
    return result;
  }

  private boolean breakUpZipFile
    (String zipfileName, String zipfileDirectory, String location,
     long zipfileSize, long maxSize, long CDRCount )
  {
    boolean success = false, ok = true;
    long noFiles = ( zipfileSize / maxSize ) + 1;
    long CDRsPerFile = ( CDRCount / noFiles ) + 1;
    String filePrefix = zipfileName.substring(0,zipfileName.length()-4);
    //System.out.println(location+" : noFiles = "+noFiles+" CDRsPerFile = "+CDRsPerFile+" filePrefix = "+filePrefix );
    // Remove attachment record for existing zip and create new attachment records for each new zip file
    if (dba.breakupAttachment(location,noFiles)==1)
    {
      String oldZipFilename = zipfileDirectory+BACKSLASH+zipfileName;
      File oldZip = new File(oldZipFilename);
      if (!oldZip.delete())
        System.out.println("Failed to delete original zip file "+oldZipFilename);
      File w2Dir = new File(work2Dir);
      File[] fileArray = w2Dir.listFiles();
      for (int i = 0; i < fileArray.length; i++)
      {
        File cdrFile = fileArray[i];
        String cdrFilename = cdrFile.getName();
        // Separate out account prefix and rest of cdrFilename
        String cdrFilenamePrefix = "", cdrFilenameSuffix = "";
        boolean hasSuffix = false;
        for(int j = 0; j<cdrFilename.length(); j++)
        {
          String test = cdrFilename.substring(j,j+1);
          if (test.startsWith(PERIOD))
          {
            hasSuffix = true;
            cdrFilenameSuffix = PERIOD;
          }
          else
          {
            if (hasSuffix)
              cdrFilenameSuffix = cdrFilenameSuffix + test;
            else
              cdrFilenamePrefix = cdrFilenamePrefix + test;
          }
        }
        try
        {
          BufferedReader cfBr = new BufferedReader( new FileReader(cdrFile));
          int lCount = 1, pfCount = 1;
          String cdrLine = cfBr.readLine();
          String partId = getPartId(pfCount);
          String newCDRFilename = cdrFilenamePrefix+"_Part_"+partId+cdrFilenameSuffix;
          String newzipFilename = filePrefix+"_Part_"+partId+".zip";
          openWorkFile(workDir+BACKSLASH+newCDRFilename);
          while (!(cdrLine==null))
          {
            workWriter.write(cdrLine+"\r\n");
            if (lCount>CDRsPerFile)
            {
              lCount = 0;
              pfCount++;
              partId = getPartId(pfCount);
              closeWorkFile();
              zipUpWorkFiles(newzipFilename,zipfileDirectory,true);
              newCDRFilename = cdrFilenamePrefix+"_Part_"+partId+cdrFilenameSuffix;
              newzipFilename = filePrefix+"_Part_"+partId+".zip";
              openWorkFile(workDir+BACKSLASH+newCDRFilename);
            }
            cdrLine = cfBr.readLine();
            lCount++;
          }
          cfBr.close();
          if (lCount > 0)
          {
            closeWorkFile();
            zipUpWorkFiles(newzipFilename,zipfileDirectory,true);
          }
          // Delete cdr file
          if (!cdrFile.delete())
            System.out.println("Failed to delete file "+cdrFilename+" in "+work2Dir);
          success = true;

        }
        catch(Exception ex)
        {
          System.out.println("Error reading cdr file "+cdrFilename+" in "+work2Dir);
        }
      }
    }
    return success;
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

  private boolean zipUpWorkFiles(String zipFile, String locDir, boolean delete)
  {
    boolean success = false, ok = true;
    try
    {
      // ensure zip file directory exists
      File zipDir = new File(locDir);
      if (!zipDir.exists())
      {
        if (!zipDir.mkdirs())
        {
          ok = false;
        }
      }
      // delete zip file if it already exists
      File zFile = new File(locDir+File.separator+zipFile);
      if (zFile.exists())
        if (!zFile.delete())
          ok = false;
      // zip up all files in the work directory into the specified zip file
      if (ok)
      {
        byte[] buf = new byte[1024];
        int len;
        ZipOutputStream zout =
          new ZipOutputStream
            (new FileOutputStream(locDir+File.separator+zipFile));
        File workd = new File (workDir);
        File[] list = workd.listFiles();
        for (int j = 0; j < list.length; j++)
        {
          String winFilename = list[j].getAbsolutePath();
          String winShortname = list[j].getName();
          File w = new File(winFilename);
          FileInputStream win = new FileInputStream(winFilename);
          zout.putNextEntry(new ZipEntry(winShortname));
          while ((len = win.read(buf,0,1024)) > 0)
          {
            zout.write(buf,0,len);
          }
          zout.closeEntry();
          win.close();
          if (delete)
            w.delete();
          else
          {
            File tempFile = new File(work2Dir+BACKSLASH+winShortname);
            if (!w.renameTo(tempFile))
              System.out.println("Failed to create file "+work2Dir+BACKSLASH+winShortname);
          }
        }
        zout.close();
        success = true;
     }
    }
    catch(Exception ex)
    {
      System.out.println(ex.getMessage());
    }
    return success;
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

  private String[] getStoredValues(String cdrFilename)
  {
    String[] values = new String[4];
    String value = "";
    int pos = 0, nameLength = cdrFilename.length(), valuePos = 0;
    for( int i=0; i<nameLength; i++)
    {
      String test = cdrFilename.substring(i,i+1);
      if (test.startsWith(USCORE))
      {
        if (valuePos<4)
        {
          values[valuePos]=value;
          value = "";
        }
        valuePos = valuePos + 1;
      }
      else
      {
        value = value + test;
      }
    }
    return values;
  }

  private void processCDRFiles()
  {
    // process files in the CDR drop directory
    processedDailyFiles = false;
    File dropDirectory = new File(dropDir);
    File[] fileArray = dropDirectory.listFiles();
    int noInvoiceCount = 0, noAccountCount = 0, successCount = 0;
    int missingTrailerCount = 0, CDRMismatchCount = 0, trailerMismatchCount = 0;
    int eofFailureCount = 0, invalidCount = 0;
    int fileCount = 0;
      for (int i = 0; i < fileArray.length; i++)
      {
        File cdrFile = fileArray[i];
        String filename = cdrFile.getName();
        String pathname = cdrFile.getAbsolutePath();
        // decode stored values in CDR filename
        String[] storedValues = new String[4];
        storedValues = getStoredValues(filename);
        String account = storedValues[0];
        // only insert leading zero if not a test account
        String accountNumber = "";
        if (account.startsWith("T"))
        {
          accountNumber = account;
        }
        else
        {
          accountNumber = "0" + account;
        }
        boolean reengineer = dba.reengineerSSBSCDR(accountNumber);
        boolean BVReport = dba.BVreportSSBSCDR(accountNumber);
        boolean kpi2Account = dba.kpi2Account(accountNumber);
        String cdrCount = storedValues[1];
        int noCDRs = su.toInt(cdrCount);
        String processingDate = storedValues[2];
        String type = storedValues[3];
        boolean endOfMonth = false;
        if (type.startsWith(TNBS))
          endOfMonth = dba.endOfMonthTNBS(filename);
        boolean invalid = false, newFormat = false;
        long checkDuration = 0, checkCharge = 0, trailerDuration = 0, trailerCharge = 0;
        //check that the account exists on ebilling
        if (dba.SSBSAccountExists(accountNumber))
        {
          // see if there is an invoice for the period if not a daily file
          long invoiceId = 0;
          if ((!type.startsWith(DAILY))&&(!type.startsWith(TDAILY)))
            invoiceId = dba.SSBSInvoiceId(accountNumber,processingDate);
          if ((invoiceId>0)||(type.startsWith(DAILY))||(type.startsWith(TDAILY)))
          {
            // Process the CDR file, line by line
            boolean empty = true;
            int lineCount = 0;
            // determine customer name and work out CDR file name format
            String name = dba.SSBSAccountName(accountNumber), suffix = "";
            if (type.startsWith(DAILY)||(type.startsWith(TDAILY)))
            {
             suffix = PERIOD + processingDate;
            }
            else if (name.startsWith(TW))
            {
              suffix = "";
            }
            else if (name.startsWith(CABLEDIRECT))
            {
              suffix = PERIOD + processingDate.substring(2,6);
            }
            else
            {
              suffix = PERIOD + processingDate.substring(4,6);
            }
            // Work out new location of file in case of reformatting
            String shortAccount = truncatedAccountNumber(account);
            String cdrFilename = shortAccount + suffix;
            File newLocation = new File(workDir + File.separator + cdrFilename);
            try
            {
              BufferedReader cfr = new BufferedReader(new FileReader(cdrFile));
              String cfrLine = cfr.readLine(), prevLine = "";
              if (!(cfrLine==null))
              {
                // on first line check for new extended format if PSTN (includes daily) or TNBS
                if ((type.startsWith(PSTN))||(type.startsWith(DAILY))||(type.startsWith(TNBS))||(type.startsWith(TDAILY)))
                {
                  int recLength = cfrLine.length();
                  if ((((type.startsWith(PSTN))||(type.startsWith(DAILY)))&&(recLength>105))||
                       ((type.startsWith(TNBS)||(type.startsWith(TDAILY)))&&(recLength>96)))
                    newFormat = true;
                }
                // for PSTN and TNBS files in the new blue format flagged for
                // re-engineering write out file in new format
                if ((reengineer)&&(newFormat)&&(noCDRs>0))
                {
                  //open reformatted file
                  if (!openWorkFile(workDir + File.separator + cdrFilename))
                  {
                    invalid = true;
                  }
                }
                // for PSTN and TNBS files not in the new format flagged
                // for appending open work file
                if ((!reengineer)&&(noCDRs>0))
                {
                  //open appended file
                  if (!openWorkFile(workDir + File.separator + cdrFilename))
                  {
                    invalid = true;
                  }
                }
                while (!(cfrLine==null))
                {
                  int duration = 0, charge = 0;
                  // get duration and charge if PSTN or TNBS
                  if ((type.startsWith(PSTN))||(type.startsWith(DAILY))||(type.startsWith(TNBS))||(type.startsWith(TDAILY)))
                  {
                    String cli = "", date = "", time = "";
                    String productCode = "", costCentre = "", cliId = "";
                    String band = "", destination = "", bandId = "";
                    if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
                    {
                      duration = su.toInt(cfrLine.substring(77,86));
                      charge = su.toInt(cfrLine.substring(86,95));
                      cli = cfrLine.substring(0,16).trim();
                      // cater for date and time position change in new extended format
                      if (newFormat)
                      {
                        date = "20"+cfrLine.substring(20,22)+
                          cfrLine.substring(18,20)+cfrLine.substring(16,18);
                        time = cfrLine.substring(22,24).trim();
                        costCentre="";
                      }
                      else
                      {
                        date = "20"+cfrLine.substring(22,24)+
                          cfrLine.substring(20,22)+cfrLine.substring(18,20);
                        time = cfrLine.substring(24,26).trim();
                        costCentre="";
                      }
                      band = cfrLine.substring(96,99).trim();
                      destination = cfrLine.substring(52,77).trim();
                      bandId = band+destination;
                      productCode = cfrLine.substring(95,96);
                      cliId = productCode+cli+costCentre;
                    }
                    else
                    {
                      duration = su.toInt(cfrLine.substring(59,68));
                      charge = su.toInt(cfrLine.substring(68,77));
                      cli = cfrLine.substring(0,16).trim();
                      date = cfrLine.substring(16,24).trim();
                      time = cfrLine.substring(24,26).trim();
                      costCentre = "";
                      cliId = productCode+cli+costCentre;
                    }
                    // ignoring trailer record add into file totals
                    //if (((cfrLine.startsWith("9999999999999999"))&&((type.startsWith(PSTN))||(type.startsWith(DAILY))))||
                    //     ((cfrLine.substring(16,35).startsWith("999999999999999999"))&&((type.startsWith(TNBS)))||(type.startsWith(TDAILY))))
                    if (((cfrLine.startsWith("9999999999999999"))&&((type.startsWith(PSTN))||(type.startsWith(DAILY))))||
                        ((cfrLine.substring(16,35).startsWith("999999999999999999"))&&((type.startsWith(TNBS))||(type.startsWith(TDAILY)))))
                    {
                      trailerDuration = duration;
                      trailerCharge = charge;
                    }
                    else
                    {
                      checkDuration = checkDuration + duration;
                      checkCharge = checkCharge + charge;
                    }
                  }
                  else
                  // populate cost for Alarm-call
                  {
                     String cost = "";
                     for (int k=98; k<cfrLine.length(); k++)
                     {
                      String test = cfrLine.substring(k,k+1);
                      //ignore spaces and periods
                      if (!((test.startsWith(" "))||(test.startsWith(PERIOD))))
                        cost = cost + test;
                     }
                     cost = cost + '0';
                     checkCharge = checkCharge + su.toInt(cost);
                  }
                  lineCount++;
                  prevLine = cfrLine;
                  // for PSTN and TNBS files in the new blue format flagged for
                  // reformatting write out file in new format ignoring trailer
                  if ((reengineer)&&(newFormat)&&(noCDRs>0))
                  {
                    if (!( ((cfrLine.startsWith("9999999999999999"))&&((type.startsWith(PSTN))||(type.startsWith(DAILY)))) ||
                         ((cfrLine.substring(16,35).startsWith("999999999999999999"))&&((type.startsWith(TNBS)))&&(type.startsWith(TDAILY))) ))
                    {
                      // re-engineer line of file
                      if (!reformatWorkFile(cfrLine,type,accountNumber,processingDate))
                        invalid = true;
                    }
                  }
                  // for PSTN and TNBS not flagged for reformatting but flagged for appending of service location code
                  // write out with appended service location description
                  if ((!reengineer)&&(noCDRs>0))
                  {
                    if (!( ((cfrLine.startsWith("9999999999999999"))&&((type.startsWith(PSTN))||(type.startsWith(DAILY)))) ||
                         ((cfrLine.substring(16,35).startsWith("999999999999999999"))&&((type.startsWith(TNBS)))&&(type.startsWith(TDAILY))) ))
                    {
                      // re-engineer line of file
                      if (!appendWorkFile(cfrLine))
                        invalid = true;
                    }
                  }
                  cfrLine = cfr.readLine();
                }
              }
              // PSTN and TNBS trailer processing
              if (((type.startsWith(PSTN))||(type.startsWith(DAILY)))||(type.startsWith(TNBS))||(type.startsWith(TDAILY)))
              {
                if ( ((prevLine.startsWith("9999999999999999"))&&((type.startsWith(PSTN))||(type.startsWith(DAILY)))) ||
                     ((prevLine.substring(16,35).startsWith("999999999999999999"))&&((type.startsWith(TNBS)))&&(type.startsWith(TDAILY))) )
                {
                  lineCount = lineCount - 1;
                  if (lineCount!=noCDRs)
                  {
                    // CDR Count/Line Count mismatch!
                    writeToLogFile("Not expected number of CDRs for "+filename);
                    CDRMismatchCount++;
                    invalid = true;
                  }
                  // Amend to rule out truncation of trailer totals
                  long revisedDuration = checkDuration, revisedCharge = checkCharge;
                  String workValue = "";
                  if (checkDuration > 999999999)
                  {
                    workValue = Long.toString(checkDuration);
                    workValue = workValue.substring(workValue.length()-9,workValue.length());
                    revisedDuration = su.toInt(workValue);
                  }
                  if (checkCharge > 999999999)
                  {
                    workValue = Long.toString(checkCharge);
                    workValue = workValue.substring(workValue.length()-9,workValue.length());
                    revisedCharge = su.toInt(workValue);
                  }
                  if (((revisedDuration!=trailerDuration)||(revisedCharge!=trailerCharge))&&(noCDRs<1000000))
                  {
                    System.out.println("revisedDuration = "+revisedDuration);
                    System.out.println("trailerDuration = "+trailerDuration);
                    System.out.println("revisedCharge = "+revisedCharge);
                    System.out.println("trailerCharge = "+trailerCharge);
                    // duration and/or charge mismatch!
                    writeToLogFile("Trailer/File totals mismatch for "+filename);
                    trailerMismatchCount++;
                    invalid = true;
                  }
                }
                else
                {
                  // expected trailer is missing! No longer treat as error
                  missingTrailerCount++;
                }
              }
              // Alarm-call - verify CDR count to line count
              else
              {
                if (lineCount!=noCDRs)
                {
                  // CDR Count/Line Count mismatch!
                  writeToLogFile("Not expected number of CDRs for "+filename);
                  CDRMismatchCount++;
                  invalid = true;
                }
              }
              cfr.close();
            }
            catch(Exception Ex)
            {
              System.out.println(Ex.getMessage()+" for file "+filename);
            }
            boolean ok = true;
            if ( ((reengineer)&&(newFormat)&&(noCDRs>0)) || ((!reengineer)&&(noCDRs>0)) )
            {
              // close re-engineered or appended file and delete original cdr file
              if (closeWorkFile())
              {
                if (!cdrFile.delete())
                {
                  writeToLogFile("Failure deleting reformatted cdr file " + filename);
                  eofFailureCount++;
                  ok = false;
                }
              }
              else
              {
                writeToLogFile("Failure closing reformatted file for " + filename);
                eofFailureCount++;
                ok = false;
              }
            }
            else
            {
              if (!cdrFile.renameTo(newLocation))
              {
                writeToLogFile("Failure moving to work directory cdr file " + filename);
                eofFailureCount++;
                ok = false;
              }
            }
            // check if CDR file is valid
            if (invalid)
            {
              invalidCount++;
              File newLocation2 = new File(invDir+File.separator+filename);
              if (!newLocation.renameTo(newLocation2))
              {
                writeToLogFile("Failed to move invalid file "+filename+" to invalid directory");
              }
              // delete any files left in the work directory
              File workDirectory = new File(workDir);
              File[] workArray = workDirectory.listFiles();
              for (int k = 0; k < workArray.length; k++)
              {
                File workFile = workArray[k];
                if (!workFile.delete())                {
                  writeToLogFile("Failed to delete file "+workFile.getName()+" in work directory");                }
              }
            }
            else
            {
              String zipFilename =
                shortAccount + USCORE + processingDate + USCORE + type+ PERIOD + ZIP;
              if (ok)
              {
                int[] Ids = dba.getAccountIds(accountNumber);
                accountId = Ids[0];
                paymentGroupId = Ids[1];
                customerId = Ids[2];
                if (accountId!=-1)
                {
                  String locationDir = "", location = "";
                  if ((type.startsWith(DAILY))||(type.startsWith(TDAILY)))
                  {
                    locationDir = attachDir + BACKSLASH + SSBS + BACKSLASH +
                      customerId + BACKSLASH + paymentGroupId + BACKSLASH +
                      accountId + BACKSLASH;
                    location =  SSBS + FWDSLASH + customerId + FWDSLASH +
                      paymentGroupId + FWDSLASH + accountId + FWDSLASH +
                      zipFilename;
                  }
                  else
                  {
                    locationDir = attachDir + BACKSLASH + SSBS + BACKSLASH +
                      customerId + BACKSLASH + paymentGroupId + BACKSLASH +
                      accountId + BACKSLASH + invoiceId + BACKSLASH;
                    location =  SSBS + FWDSLASH + customerId + FWDSLASH +
                      paymentGroupId + FWDSLASH + accountId + FWDSLASH +
                      invoiceId + FWDSLASH + zipFilename;
                  }
                  if (zipUpWorkFiles(zipFilename,locationDir,false))
                  {
                    String aName = "";
                    if (type.startsWith(PSTN))
                      aName = PSTNANAME;
                    else if (type.startsWith(DAILY))
                      aName = DAILYANAME;
                    else if (type.startsWith(TDAILY))
                      aName = TDAILYANAME;
                    else if (type.startsWith(TNBS))
                    {
                      if (endOfMonth)
                        aName = TNBSENDOFMONTHANAME;
                      else
                        aName = TNBSANAME;
                    }
                    else
                      aName = ALARMANAME;
                    // use invoice id for monthly files and account id for daily
                    // files to create attachment and dataflow output file rows
                    long id = invoiceId;
                    if ((type.startsWith(DAILY))||(type.startsWith(TDAILY)))
                      id = accountId;
                    long returnValue  =
                      dba.insertSSBSCDR
                        (filename,zipFilename,processingDate,
                         noCDRs,checkDuration,checkCharge,
                         location,aName,id);
                    if (returnValue==1)
                    {
                      successCount++;
                      if ((type.startsWith(DAILY))||(type.startsWith(TDAILY)))
                        processedDailyFiles = true;
                      // get size of zip file
                      File zFile = new File(locationDir+zipFilename);
                      long zSize = zFile.length();
                      long maxSize = 1024 * 1024 * maxFileSize;
                      if (zSize>maxSize)
                      {
                        if (!breakUpZipFile(zipFilename,locationDir,location, zSize,maxSize,noCDRs))
                          writeToLogFile("Failed to break up zip file for file " + filename);
                      }
                      else
                      {
                        // delete files from work2Dir
                        emptyDirectory(work2Dir);
                      }
                    }
                    else
                    {
                      writeToLogFile("Database failure for cdr file " + filename + " : " + returnValue);
                      eofFailureCount++;
                    }
                  }
                  else
                  {
                    writeToLogFile("Failure zipping cdr file " + filename);
                    eofFailureCount++;
                  }
                }
                else
                {
                  writeToLogFile("Failure getting ids for file " + filename);
                  eofFailureCount++;
                }
              }
            }
          }
          else
          {
          writeToLogFile("Invoice not located for monthly file "+filename);
          noInvoiceCount++;
          }
        }
        else
        {
          writeToLogFile("Account not set up for file "+filename);
          noAccountCount++;
        }
        fileCount++;
      }
    // summarise CDR file processing
    writeToLogFile(" ");
    if (fileCount==0)
      writeToLogFile("No outstanding cdrs to process");
    else
    {
      String plural = "s";
      if (fileCount==1)
        plural = "";
      writeToLogFile(fileCount+" CDR file"+plural+" to process");
      if (noAccountCount!=0)
        writeToLogFile("   Account not set up       : "+noAccountCount);
      if (noInvoiceCount!=0)
        writeToLogFile("   No invoice for period    : "+noInvoiceCount);
      //if (missingTrailerCount!=0)
        //writeToLogFile("   Missing trailer          : "+missingTrailerCount);
      if (CDRMismatchCount!=0)
        writeToLogFile("   CDR Count Mismatch       : "+CDRMismatchCount);
      if (trailerMismatchCount!=0)
        writeToLogFile("   Trailer Totals Mismatch  : "+trailerMismatchCount);
      if (eofFailureCount!=0)
        writeToLogFile("   End of File Issues       : "+eofFailureCount);
      if (invalidCount!=0)
        writeToLogFile("   Invalid File             : "+invalidCount);
      if (successCount!=0)
        writeToLogFile("   Successfully transferred : "+successCount);
      if (processedDailyFiles)
      {
        String[] ncArgs = new String[1];
        ncArgs[0] = "SSBS";
        NotifyControl.main(ncArgs);
      }
    }
    writeToLogFile(" ");
  }

  private void scanDownloadedFiles()
  {
    // obtain list of downloaded files via rows on the
    // Dataflow Input File table with status of 'Identified' or 'Failed'
    int size = dba.getSSBSDownloadListSize();
    int successCount = 0 , failedCount = 0, notDownloadedCount = 0;
    String[] downloadedFileList = new String[size];
    downloadedFileList = dba.getSSBSDownloadList(size);
    // process each file checking it exists and modifying status
    for(int i = 0; i<size; i++)
    {
      String filename = downloadedFileList[i];
      File downloadFile = new File(downDir+File.separator+filename);
      boolean exists = false;
      String newFTPStatus = "Failed";
      if (downloadFile.exists())
      {
        exists = true;
        newFTPStatus = "OK";
      }
      if (dba.updateFTPStatus(filename,newFTPStatus))
      {
        // if updated to OK move file to CDR drop directory
        if (exists)
        {
          File newLocation = new File(dropDir+File.separator+filename);
          if (!downloadFile.renameTo(newLocation))
          {
            writeToLogFile("Failed to move file "+filename+" to CDR Drop directory");
            failedCount++;
          }
          else
          {
            successCount++;
          }
        }
        else
        {
          notDownloadedCount++;
        }
      }
      else
        writeToLogFile("Failed to update FTP Status to "+newFTPStatus+" fpr file "+filename);
    }
    // summarise results
    writeToLogFile(" ");
    if (size==0)
      writeToLogFile("No outstanding FTP file requests to process");
    else
    {
      String plural="s";
      if (size==1)
        plural = "";
      String message = size+" FTP file request"+plural+" identifed";
      writeToLogFile(message);
      writeToLogFile("   Not downloaded                      : "+notDownloadedCount);
      writeToLogFile("   Failed move to CDR drop directory   : "+failedCount);
      writeToLogFile("   Successfully transferred            : "+successCount);
    }
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

  private boolean openWorkFile(String filename)
  {
    boolean success = false;
    File workFile = new File (filename);
    try
    {
      workWriter = new BufferedWriter(new FileWriter(workFile));
      success = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error opening work file " + filename + " : " + ex.getMessage());
    }
    return success;
  }

  // function to reformat blue format data into re-engineered green format
  private boolean reformatWorkFile(String line, String type, String accountNumber, String processingDate)
  {
    String newline = DBLQUOTE;
    boolean success = false;
    try
    {
      // Account Number
      newline = newline+accountNumber+" ";
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Service Location Number use service location code
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        newline = newline+line.substring(104,113)+"   ";
      else
        newline = newline+line.substring(95,104)+"   ";
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Calling number (outbound) or Dialled Number (inbound)
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        // Destination Code plus Number Called
        newline = newline+reformatNumber(line.substring(30,38),line.substring(38,52),24);
      else
        // Number Called
        newline = newline+reformatNumber(line.substring(0,16),"",24);
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Tone Dialler Id (hard coded to two spaces)
      newline = newline+"  "+DBLQUOTE+COMMA+DBLQUOTE;
      // Billing Date
      newline = newline+processingDate+DBLQUOTE+COMMA+DBLQUOTE;
      // Product Identification Code use Service Type
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        newline = newline+picPSTN(line.substring(95,96),line.substring(0,16),line.substring(97,99));
      else
        newline = newline+line.substring(30,33);
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // CLI (outbound) or TAD (inbound)
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        // CLI
        newline = newline+reformatNumber(line.substring(0,16),"",12);
      else
        // TAD
        newline = newline+reformatNumber(line.substring(77,95),"",12);
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Call Date
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        // Start Date in DDMMYY reformatted to YYYYMMDD
        newline = newline+"20"+line.substring(20,22)+line.substring(18,20)+line.substring(16,18);
      else
        // Start Date
        newline = newline+line.substring(16,24);
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Call Time use start time of call and pad for missing tenth of second value
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        newline = newline+line.substring(22,28)+" ";
      else
        newline = newline+line.substring(24,30)+" ";
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Location Description
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        // Destination Description
        newline = newline+line.substring(52,72);
      else
        // Origin Description
        newline = newline+line.substring(34,54);
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Dialled Number (outbound) or Originating Number (inbound)
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        // Destination Code plus Number Called
        newline = newline+reformatNumber(line.substring(30,38),line.substring(38,52),12);
      else
        // Originating Number
        newline = newline+line.substring(104,111)+"     ";
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Rating Period (hard coded to a single space)
      newline = newline+" "+DBLQUOTE+COMMA+DBLQUOTE;
      // Unused x2 (both set to five spaces"
      newline = newline+"     "+DBLQUOTE+COMMA+DBLQUOTE+"     "+DBLQUOTE+COMMA+DBLQUOTE;
      // Duration
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        newline = newline+line.substring(77,86);
      else
        newline = newline+line.substring(59,68);
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Cost
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        newline = newline+line.substring(86,95);
      else
        newline = newline+line.substring(68,77);
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Cost Centre
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        // Use CLI Extension if available otherwise use cost centre
        if (line.substring(113,120).startsWith(" "))
          newline = newline+line.substring(101,104)+"    ";
        else
          newline = newline+line.substring(113,120);
      else
        // Not available for Callink so hard code to seven spaces
        newline = newline+"       ";
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Call Type / Charge Band
      if ((type.startsWith(PSTN))||(type.startsWith(DAILY)))
        // Destination Type plus Distance Code
        newline = newline+line.substring(96,97)+line.substring(99,101);
      else
        // Not available for Callink so hard code to three spaces
        newline = newline+"   ";
      newline = newline+DBLQUOTE+COMMA+DBLQUOTE;
      // Service Location Description
      newline = newline+"                              "+DBLQUOTE;
      workWriter.write(newline);
      workWriter.newLine();
      success = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error writing to work file : " + ex.getMessage());
      System.out.println(line);
      System.out.println(newline);
    }
    return success;
  }

  // function to reformat blue format data into re-engineered green format
  private boolean appendWorkFile(String line)
  {
    boolean result = false;
    try
    {
      workWriter.write(line);
      workWriter.newLine();
      result = true;
    }
    catch (java.io.IOException ex)
    {
      System.out.println("Error writing to work file : " + ex.getMessage());
      System.out.println(line);
    }
    return result;
  }

  private String picPSTN (String serviceType, String numberCalled, String callType )
  {
    String result="";
    if (numberCalled.trim().startsWith("118"))
      result = "813";
    else
    {
      if ((serviceType.startsWith("0"))||(serviceType.startsWith("X")))
      {
        if (numberCalled.trim().startsWith("028"))
        {
          result = "200";
        }
        else
        {
          result = "020";
        }
      }
      else if (serviceType.startsWith("1"))
      {
        if (numberCalled.trim().startsWith("028"))
        {
          result = "210";
        }
        else
        {
          result = "050";
        }
      }
      else if  ((serviceType.startsWith("4"))||(serviceType.startsWith("Z"))) // WLR
      {
        result = "020";
      }
      else if  (serviceType.startsWith("5"))  // VPN
      {
        if ((callType.startsWith("10"))||
            (callType.startsWith("13"))||
            (callType.startsWith("16"))||
            (callType.startsWith("19"))||
            (callType.startsWith("22")))
        {
          result = "050";
        }
        else
        {
          result = "020";
        }
      }
      else // unexpected Service Type
      {
        result = "???";
      }
    }
    //System.out.println("Result = <"+result+">");
    return result;
  }

  // function to standarise formatting of telephone numbers
  private String reformatNumber (String part1, String part2, int length)
  {
    String result = "", finalPart1 = "", finalPart2 = "", testChar = "";
    // remove spaces for first part of phone number, ignore if blank
    for(int i=0; i<part1.length(); i++)
    {
      testChar = part1.substring(i,i+1);
      if (!testChar.startsWith(" "))
        finalPart1 = finalPart1 + testChar;
    }
    // remove spaces for second part of phone number
    if (part2.length()>0)
    {
      for(int i=0; i<part2.length(); i++)
      {
        testChar = part2.substring(i,i+1);
        if (!testChar.startsWith(" "))
          finalPart2 = finalPart2 + testChar;
      }
    }
    else
      finalPart2 = "";
    result = finalPart1 + finalPart2;
    // pad out with spaces to required length
    int leftLen = length - result.length();
    for(int i=0; i<leftLen; i++ )
    {
      result = result+" ";
    }
    // Check if number length is greater than required
    if (result.length()>length)
    {
      int over = result.length() - length;
      // If more than one leading zero reduce down to one
      int leadZeroCount = 0;
      boolean reachedNonZero = false;
      for(int i=0; i<length; i++)
      {
        if (!reachedNonZero)
        {
          if (result.substring(i,i+1).startsWith("0"))
            leadZeroCount++;
          else
            reachedNonZero = true;
        }
      }
      if (leadZeroCount>1)
        result = result.substring(leadZeroCount-1,result.length());
      // If still longer then length then truncate number
      if (result.length()>length)
        result = result.substring(0,length);
    }
    return result;
  }

  private boolean closeWorkFile()
  {
    boolean success = false;
    try
    {
      workWriter.close();
      workWriter = null;
      success = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error closing work file : " + ex.getMessage());
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
      logFile = new File(logDir + File.separator + runTS + "_processSSBSCDRFiles_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("processSSBSCDRFiles processing started at " +
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

  private void closeLogFile()
  {
    // at end of program close log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("processSSBSCDRFiles processing ended at " +
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

  public static void main(String[] args)
  {
    // control processing
    processSSBSCDRFilesLite pscf = new processSSBSCDRFilesLite();
    pscf.openLogFile();
    if (pscf.checkRunControl())
    {
      pscf.scanDownloadedFiles();
      pscf.processCDRFiles();
      pscf.resetRunControl();
    }
    pscf.closeLogFile();
  }

}



