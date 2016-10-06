package Batch;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import DBUtilities.*;
import JavaUtil.*;
import java.math.BigDecimal;

public class ebillzControl
{
  // class variables
  private DBAccess dba;
  private StringUtil su;
  private String dropDir, recycleDir, invalidDir, logDir, attachDir, workDir, dir;
  private BufferedWriter logWriter;
  private File logFile;
  private String runTS;
  private String ebillzFileName;
  private int fileCount = 0, invalidFormatCount = 0, missingAccountCount = 0,
    attachmentFailureCount = 0, invoiceFailureCount = 0, moveFailureCount = 0,
    summaryFailureCount = 0, successCount = 0;
  private int[] accountIds;
  private String fileType, accountNumber, invoiceNumber, pdfName, startDate, endDate;
  private boolean monthly, invoicePDF, processedDailyFiles;
  private int accountId, paymentGroupId, customerId, invoiceId;
  private String filename, path, location;
  private ZipOutputStream zout;
  private String[] results = new String[5];
  private Hashtable cliSummary, dateSummary, timeSummary, accountSummary;
  private String runMode;
  // class constants
  private final String DAILY = "DAILY";
  private final String MONTHLY = "MONTHLY";
  private final String FIRST = "01";
  private final String HYPHEN = "-";
  private final String EBILLZ = "ebillz";
  private final String EC = "ebillzControl";
  private final String NOTAVAILABLE = "Not available";
  private final String ACTIVE = "Active";
  private final String INACTIVE = "Inactive";
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String ENERGIS = "energis";
  private final String FWDSLASH = "/";
  private final String BCKSLASH = "\\";
  private final String USCORE = "_";
  private final String CDRS = "CDRs";
  private final String TO = "to";
  private final String OTHER = "Other";
  private final String MONTHLYCDRS = "Monthly_CDRs";
  private final String MONTHLYWLRCDRS = "Monthly_WLR_CDRs";
  private final String PDF = ".pdf";
  private final String ZIP = ".zip";
  private final String CDR = "cdr";
  private final String CLI = "CLI";
  private final String SUM = "SUM";
  private final String CSV = ".csv";
  private final String BATCH = "batch";
  private final String ACCOUNT = "account";
  private final String INVOICE = "invoice";
  private final String INVOICEMC = "Invoice";
  private final String REPORT = "report";
  private final String OTHERREPORTS = "Other Reports(s)";
  private final String MTHLYCDRS = "Monthly CDRS";
  private final String MTHLYWLRCDRS = "Monthly WLR CDRS";
  private final String NULL = "NULL";
  private final String DOUBLEQUOTE = "\"";
  private final String COMMA = ",";
  private final String DEFAULT = "Default";
  private final String NTSPRS = "NTS-PRS";
  private final String BSKYB = "BSkyB";
  private final String CRYSTAL = "Crystal";
  private final String DEFAULTSTART = "StartTime";
  private final String NTSPRSSTART = "CustomerCode";
  private final String BSKYBSTART = "BSkyB";
  private final String PEAK = "PEAK";
  private final String OPEAK = "OPEAK";
  private final String WEND = "WEND";
  private final String DAYTIME = "D";
  private final String EVENING = "E";
  private final String WEEKEND = "W";

  private ebillzControl()
  {
    dba = new DBAccess();
    su = new StringUtil();
    dropDir = EBANProperties.getEBANProperty(EBANProperties.EZDROPDIR);
    recycleDir = EBANProperties.getEBANProperty(EBANProperties.EZRECYCDIR);
    invalidDir = EBANProperties.getEBANProperty(EBANProperties.EZINVALIDDIR);
    logDir = EBANProperties.getEBANProperty(EBANProperties.EZLOGDIR);
    attachDir = EBANProperties.getEBANProperty(EBANProperties.EZATTACHDIR);
    workDir = EBANProperties.getEBANProperty(EBANProperties.EZWORKDIR);
  }

  private boolean checkRunControl()
  {
    boolean result = false;
    String status = dba.getRunControlStatus(EBILLZ,EC), message="";
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
        if (dba.updateRunControlStatus(EBILLZ,EC,ACTIVE))
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

  private void resetRunControl()
  {
    if (!dba.updateRunControlStatus(EBILLZ,EC,INACTIVE))
    {
      String message = "!!!failed to rest run control for this process to inactive!!!";
      writeToLogFile(message);
      System.out.println(message);
    }
  }


  private boolean validFormat(String line, String format)
  {
    boolean valid = false;
    if (line==null)
      System.out.println("Empty CDR file for account "+accountNumber);
    else    {
      if ( ( (format==DEFAULT) && (!line.startsWith(DEFAULTSTART)) ) ||
           ( (format==NTSPRS) &&  (!line.startsWith(NTSPRSSTART)) ) ||
           ( (format==BSKYB) &&   (!line.substring(1,line.length()).startsWith(BSKYBSTART)) ) )
        System.out.println("CDR file for account "+accountNumber+" is not in expected format");
      else
        valid = true;
    }
    return valid;
  }

  private String[] decodeSummaryLine(String format, String line)
  {
    //System.out.println("Format is "+format);
    //System.out.println(line);
    String[] results = new String[7];
    String[] columns = new String[20];
    int column = 0;
    String work = "";
    for (int i = 0; i < line.length(); i++)
    {
      String test = line.substring(i,i+1);
      // transfer column value when comma reached
      if (test.startsWith(COMMA))
      {
        columns[column] = work;
        column = column + 1;
        work = "";
      }
      else
      {
        // ignore double quotes when setting up column values
        if (!test.startsWith(DOUBLEQUOTE))
          work = work + test;
      }
    }
    // transfer last column at end of line
    columns[column] = work;
    if (format.startsWith(CRYSTAL))
    {
      results[0] = columns[0].trim();
      results[1] = columns[1].substring(8,10)+BCKSLASH+columns[1].substring(5,7)+BCKSLASH+columns[1].substring(0,4);
      results[2] = columns[1].substring(11,13);
      results[3] = columns[5].trim();
      results[4] = columns[7].trim();
      results[5] = columns[3].trim();
      String band = columns[8].trim();
      if (band.startsWith(PEAK))
        results[6] = DAYTIME;
      else if (band.startsWith(OPEAK))
        results[6] = EVENING;
      else if (band.startsWith(WEND))
        results[6] = WEEKEND;
      else
        results[6] = "";
    }
    if (format.startsWith(DEFAULT))
    {
      results[0] = columns[1].trim();
      results[1] = columns[0].substring(0,10).trim();
      results[2] = columns[0].substring(11,13).trim();
      results[3] = columns[3].trim();
      results[4] = columns[4].trim();
      results[5] = columns[6].trim();
      results[6] = columns[5].trim();
    }
    if (format.startsWith(NTSPRS))
    {
      if (accountNumber.startsWith("1") || accountNumber.startsWith("3"))
        results[0] = columns[3].trim();
      else
        results[0] = columns[2].trim();
      results[1] = columns[1].substring(0,10).trim();
      results[2] = columns[1].substring(11,13).trim();
      results[3] = columns[6].trim();
      results[4] = columns[7].trim();
      results[5] = "";
      results[6] = "";
    }
    if (format.startsWith(BSKYB))
    {
      results[0] = columns[4].trim();
      results[1] = columns[1].substring(0,6).trim() + "20" + columns[1].substring(6,8).trim();
      results[2] = columns[2].substring(9,11).trim();
      results[3] = columns[8].trim();
      results[4] = columns[9].trim();
      results[5] = "";
      results[6] = "";
    }
    return results;
  }

  private void updateAccountSummary(Hashtable accountSummary,
    String accountNumber, String period, String callCategory, String timeOfDay,
    long calls, long duration, long amount)
  {
    String id = accountNumber+period+callCategory+timeOfDay;
    ebillzSummaryDescriptor esd = null;
    if (accountSummary.containsKey(id))
    {
      esd = (ebillzSummaryDescriptor)accountSummary.get(id);
      esd.incrementTotalCalls(calls);
      esd.incrementTotalDuration(duration);
      esd.incrementTotalAmount(amount);
    }
    else
    {
      esd = new ebillzSummaryDescriptor(id,accountNumber,period,callCategory,timeOfDay,duration,amount,calls);
    }
    accountSummary.put(id,esd);
  }

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

  private boolean createSummaries(String period)
  {
    boolean success = false;
    boolean empty = false;
    String filename = dir+BCKSLASH+ebillzFileName;
    String format = dba.cdrFormat(accountId);
    String accountName = dba.getAccountName(accountId);
    String[] returnValues = new String[7];
    String cli = "", date ="", hour = "", duration = "", amount = "";
    String category = "", timeOfDay = "";
    long callsTotal = 0, workDuration = 0, durationTotal = 0;
    long workAmount = 0, amountTotal = 0;
    String formattedTotal ="", outLine = "";
    cliSummary = new Hashtable();
    dateSummary = new Hashtable();
    timeSummary = new Hashtable();
    // if CDR format is default or Crystal clear down account summaries for this account for this period
    if ((format.startsWith(DEFAULT))||(format.startsWith(CRYSTAL)))
    {
      if (!dba.deleteEbillzAccountSummaries(accountNumber,period))
        System.out.println("Failed to delete account summaries for "+accountNumber);
      accountSummary = new Hashtable();
    }
    try{
      FileReader fr = new FileReader(filename);
      BufferedReader br = new BufferedReader(fr);
      String line = br.readLine();
      int len = line.length();
      boolean eof = false;

      if (validFormat(line,format))
      {
        // ignore first line of column headings if not Crystal format
        if (!format.startsWith(CRYSTAL))
          line = br.readLine();
        if (line==null)
        {
          //System.out.println("Empty CDR file with headings for account "+accountNumber);
          empty = true;
          br.close();
          fr.close();
        }
        else
        {
          // process CDR data
          while (!eof)
          {
            callsTotal = callsTotal + 1;
            returnValues = decodeSummaryLine(format,line);
            cli = returnValues[0];
            date = returnValues[1];
            hour = returnValues[2]+":00";
            duration = returnValues[3];
            amount = returnValues[4];
            category = returnValues[5];
            timeOfDay = returnValues[6];
            workDuration = su.toInt(duration);
            durationTotal = durationTotal + workDuration;
            workAmount = makeLong(amount);
            amountTotal = amountTotal + workAmount;
            // populate all the summaries
            updateSummary(cliSummary,cli,1,workDuration,workAmount);
            updateSummary(dateSummary,date,1,workDuration,workAmount);
            updateSummary(timeSummary,hour,1,workDuration,workAmount);
            // if CDR format is default or Crystal update account summary
            if ((format.startsWith(DEFAULT))||(format.startsWith(CRYSTAL)))
            {
              updateAccountSummary(accountSummary,accountNumber,period,category,timeOfDay,1,workDuration,workAmount);
            }
            line = br.readLine();
            if (line==null)
              eof = true;
          }
        // close the cdr file
        br.close();
        fr.close();
        }
      }
      // only produce summaries if file is not empty
      if (!empty)
      {
        // open CLI file
        String cliFilename = workDir + FWDSLASH + accountNumber + USCORE + period +
          USCORE + accountName + USCORE+ CLI + USCORE + invoiceNumber + CSV;
        FileWriter cliFW = new FileWriter(cliFilename);
        BufferedWriter cliBW = new BufferedWriter(cliFW);
        outLine = "Vodafone UK - Summary by Calling Line for" + COMMA +
          accountNumber + COMMA + "Date" + COMMA + period + COMMA;
        writeLine(cliBW,outLine);
        outLine = "Calling Line" + COMMA + "Calls" + COMMA;
        if (format.startsWith(BSKYB))
          outLine = outLine+ "Rebate";
        else
          outLine = outLine+ "Cost";
        outLine = outLine + COMMA + "Seconds";
        writeLine(cliBW,outLine);
        // open SUM file
        String sumFilename = workDir + FWDSLASH + accountNumber + USCORE + period +
        USCORE + accountName + USCORE+ SUM + USCORE + invoiceNumber + CSV;
        FileWriter sumFW = new FileWriter(sumFilename);
        BufferedWriter sumBW = new BufferedWriter(sumFW);
        outLine = "Vodafone UK - Summary by Date and General for" + COMMA +
          accountNumber + COMMA + COMMA;
        writeLine(sumBW,outLine);
        formattedTotal = makeString(amountTotal);
        outLine = "Bill Date" + COMMA + period + COMMA + "Account Number" + COMMA +
          accountNumber + COMMA + "Total Calls" + COMMA + callsTotal + COMMA;
        if (format.startsWith(BSKYB))
          outLine = outLine + "Rebate";
        else
          outLine = outLine + "Value";
        outLine = outLine +  COMMA + formattedTotal + COMMA + "Total Seconds" + COMMA + durationTotal;
        writeLine(sumBW,outLine);
        // process cli hashtable
        Vector cV = new Vector(cliSummary.keySet());
        Collections.sort(cV);
        for (Iterator it = cV.iterator(); it.hasNext();)
        {
          String key = (String)it.next();
          ebillzTotalDescriptor etd = (ebillzTotalDescriptor)cliSummary.get(key);
          outLine = "'" + etd.getId() + COMMA + etd.getTotalCalls() + COMMA +
            makeString(etd.getTotalAmount()) + COMMA + etd.getTotalDuration();
          writeLine(cliBW,outLine);
        }
        // close CLI file
        outLine = "End of Report" + COMMA+ COMMA + COMMA;
        writeLine(cliBW,outLine);
        cliBW.close();
        cliFW.close();
        // produce blank line for SUM
        outLine = COMMA + COMMA + COMMA;
        writeLine(sumBW,outLine);
        // produce time header for SUM
        outLine = "Time (hh:00)" + COMMA + "Calls" + COMMA;
        if (format.startsWith(BSKYB))
          outLine = outLine + "Rebate";
        else
          outLine = outLine + "Cost";
        outLine = outLine + COMMA + "Seconds";
        writeLine(sumBW,outLine);
        // determine if any time summaries are missing
        for (int i=0; i < 24; i++)
        {
          String testHour = "";
          if (i<10)
            testHour = "0" + Integer.toString(i) + ":00";
          else
            testHour = Integer.toString(i) + ":00";
          if (!timeSummary.containsKey(testHour))
          {
            ebillzTotalDescriptor etd = new ebillzTotalDescriptor(testHour,0,0,0);
            timeSummary.put(testHour,etd);
          }
        }
        // process time hashtable
        Vector tV = new Vector(timeSummary.keySet());
        Collections.sort(tV);
        for (Iterator it = tV.iterator(); it.hasNext();)
        {
          String key = (String)it.next();
          ebillzTotalDescriptor etd = (ebillzTotalDescriptor)timeSummary.get(key);
          outLine = "'" + etd.getId() + COMMA + etd.getTotalCalls() + COMMA +
            makeString(etd.getTotalAmount()) + COMMA + etd.getTotalDuration();
          writeLine(sumBW,outLine);
        }
        // produce blank line for SUM
        outLine = COMMA + COMMA + COMMA;
        writeLine(sumBW,outLine);
        // produce date header for SUM
        outLine = "Date (dd/mm/yyyy)" + COMMA + "Calls" + COMMA;
        if (format.startsWith(BSKYB))
          outLine = outLine + "Rebate";
        else
          outLine = outLine + "Cost";
        outLine = outLine + COMMA + "Seconds";
        writeLine(sumBW,outLine);
        // process date hashtable
        Vector dV = new Vector(dateSummary.keySet());
        Collections.sort(dV);
        for (Iterator it = dV.iterator(); it.hasNext();)
        {
          String key = (String)it.next();
          ebillzTotalDescriptor etd = (ebillzTotalDescriptor)dateSummary.get(key);
          outLine = "'" + etd.getId() + COMMA + etd.getTotalCalls() + COMMA +
            makeString(etd.getTotalAmount()) + COMMA + etd.getTotalDuration();
        writeLine(sumBW,outLine);
        }
        // close SUM file
        outLine = "End of Report"+ COMMA+ COMMA + COMMA;
        writeLine(sumBW,outLine);
        sumBW.close();
        sumFW.close();
        // if default or Crystal format write out account summaries to the db
        if ((format.startsWith(DEFAULT))||(format.startsWith(CRYSTAL)))
        {
          // process accountSummary hashtable
          Vector aV = new Vector(accountSummary.keySet());
          Collections.sort(aV);
          for (Iterator it = aV.iterator(); it.hasNext();)
          {
            String key = (String)it.next();
            ebillzSummaryDescriptor esd = (ebillzSummaryDescriptor)accountSummary.get(key);
            if (dba.updateEbillzAccountSummary(
              esd.getPeriod(),esd.getAccountNumber(),esd.getCallCategory(),esd.getTimeOfDay(),
              esd.getTotalCalls(),esd.getTotalDuration(),esd.getTotalAmount())!=0)
            {
              System.out.println("Failed to create account summaries for "+accountNumber);
            }
          }
        }
      }
      success = true;
    }
    catch(Exception ex)
    {
      System.out.println("Error producing summaries for "+filename+" : "+ex.getMessage());
    }
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

  private long makeLong (String input)
  {
    long result = 0;
    input = input.trim();
    String output = "";
    int dp = 0;
    boolean decimalPart = false;
    for (int i = 0; i < input.length(); i++)
    {
      String test = input.substring(i,i+1).trim();
      if (test.startsWith("."))
      {
        decimalPart = true;
      }
      else
      {
        output = output + test;
        if (decimalPart)
          dp = dp + 1;
      }
    }
    switch (dp)
    {
      case 0 : output = output + "0000";
        break;
      case 1 : output = output + "000";
        break;
      case 2 : output = output + "00";
        break;
      case 3 : output = output + "0";
        break;
    }
    result = Long.parseLong(output);
    return result;
  }

  private void moveTo(String newDir)
  {
    String oldFullPath = dir + BCKSLASH + ebillzFileName;
    String newFullPath = newDir + BCKSLASH + ebillzFileName;
    File currentF = new File(oldFullPath);
    File newF = new File(newFullPath);
    currentF.renameTo(newF);
  }

  private boolean moveMonthlySet()
  {
    boolean success = false;
    // check if destination path exists and if it does not then create it
    String mPath = attachDir + FWDSLASH + path.substring(0,path.length()-1);
    File directory = new File(mPath);
    if (!directory.exists())
      if (!directory.mkdirs())
        System.out.println("Failed to create directory "+mPath);
    // set up abstract entries for current file and destination file
    String oldFilename = dir+BCKSLASH+ebillzFileName;
    String newFilename = mPath + FWDSLASH + filename;
    File old = new File(oldFilename);
    File newf = new File(newFilename);
    // if the destination file exists delete it
    if (newf.exists())
      newf.delete();
    // move the monthly CDR file into the work directory where the SUM and CLI are located
    File temp = new File(workDir + FWDSLASH + ebillzFileName);
    if (!old.renameTo(temp))
      System.out.println("Move of CDR file "+ebillzFileName+" to work directory has failed");
    try
    {
      /// zip up the three files and move to destination
      byte[] buf = new byte[1024];
      int len;
      zout = new ZipOutputStream(new FileOutputStream(newFilename));
      File workd = new File (workDir);
      File[] list = workd.listFiles();
      for (int i = 0; i < list.length; i++)
      {
        String winFilename = list[i].getAbsolutePath();
        String winShortname = list[i].getName();
        File w = new File(winFilename);
        FileInputStream win = new FileInputStream(winFilename);
        zout.putNextEntry(new ZipEntry(winShortname));
        while ((len = win.read(buf)) > 0)
        {
          zout.write(buf, 0, len);
        }
        zout.closeEntry();
        win.close();
        w.delete();
      }
      zout.close();
      success = true;
    }
    catch(Exception ex)
    {

    }
    return success;
  }

  private boolean moveFile()
  {
    boolean success = false;
    // check if destination path exists and if it does not then create it
    String mPath = attachDir + FWDSLASH + path.substring(0,path.length()-1);
    File directory = new File(mPath);
    if (!directory.exists())
      if (!directory.mkdirs())
        System.out.println("Failed to create directory "+mPath);
    // set up abstract entries for current file and destination file
    String oldFilename = dir+BCKSLASH+ebillzFileName;
    String newFilename = mPath + FWDSLASH + filename;
    File old = new File(oldFilename);
    File newf = new File(newFilename);
    // for invoice pdfs delete the destination file if it exists and move
    if (invoicePDF)
    {
      if (newf.exists())
        newf.delete();
      if (old.renameTo(newf))
        success = true;
      else
        System.out.println("PDF move to "+newFilename+" has failed");
    }
    // for all other files ...
    else
    {
      try
      {
        // see if zip already exists, if it does use it, otherwise create it
        byte[] buf = new byte[1024];
        int len;
        if (newf.exists())
        {
          // move files in zip into work directory unless same name as current file
          FileInputStream zin = new FileInputStream(newf);
          ZipInputStream zis = new ZipInputStream(zin);
          ZipEntry zipEntry;
          while((zipEntry = zis.getNextEntry()) != null)
          {
            String outFilename = zipEntry.getName();
            if (outFilename!=ebillzFileName)
            {
              String outFilePath = "";
              if (outFilename.startsWith(workDir))
                outFilePath = outFilename;
              else
                outFilePath = workDir + BCKSLASH + outFilename;
              OutputStream wout = new FileOutputStream(outFilePath);
              while ((len = zis.read(buf)) > 0)
              {
                wout.write(buf, 0, len);
              }
              wout.close();
            }
          }
          zis.close();
          zin.close();
          // move current file to work directory
          File newold = new File(workDir + BCKSLASH + ebillzFileName);
          if (newold.exists())
              newold.delete();
          old.renameTo(newold);
          // delete existing zip
          newf.delete();
          // create new zip file and move in files from work directory
          zout = new ZipOutputStream(new FileOutputStream(newFilename));
          File workd = new File (workDir);
          File[] list = workd.listFiles();
          for (int i = 0; i < list.length; i++)
          {
            String winFilename = list[i].getAbsolutePath();
            String winShortname = list[i].getName();
            File w = new File(winFilename);
            FileInputStream win = new FileInputStream(winFilename);
            zout.putNextEntry(new ZipEntry(winShortname));
            while ((len = win.read(buf)) > 0)
            {
              zout.write(buf, 0, len);
            }
            zout.closeEntry();
            win.close();
            w.delete();
          }
          zout.close();
          success = true;
        }
        else
        {
          // Create a new ZIP file
          FileInputStream in = new FileInputStream(old.getAbsolutePath());
          zout = new ZipOutputStream(new FileOutputStream(newFilename));
          zout.putNextEntry(new ZipEntry(old.getName()));
          while ((len = in.read(buf)) > 0)
          {
            zout.write(buf, 0, len);
          }
          zout.closeEntry();
          in.close();
          old.delete();
          zout.close();
          success = true;
        }
      }
      catch (Exception ex)
      {
        System.out.println("Failed on zip "+newFilename+" : "+ex.getMessage());
      }
    }
    return success;
  }

  private boolean processFiles(String mode, String period)
  {
    // process all files in supplied directory
    boolean success = false;
    processedDailyFiles = false;
    if (mode=="drop")
      dir = dropDir;
    else
      dir = recycleDir;
    // reset period to first of current month is supplied period is MONTHLY
    if (period.startsWith(MONTHLY))
      period = dailyRunPeriod();
    if (validRunPeriod(period,mode))
    {
      try
      {
        File directory = new File(dir);
        File[] fileArray = directory.listFiles();
        if (fileArray!=null)
        {
          for (int i = 0; i < fileArray.length; i++)
          {
            //System.out.print(". ");
            File ebillzFile = fileArray[i];
            fileCount=fileCount+1;
            // use ebillzFileName class to get file details from the file name
            ebillzFileName = ebillzFile.getName();
            ebillzFileName efn = new ebillzFileName(ebillzFileName);
            fileType = efn.getFileType();
            invoiceNumber = efn.getInvoiceNumber();
            accountNumber = efn.getAccountNumber();
            pdfName = efn.getPDFName();
            startDate = efn.getStartDate();
            endDate = efn.getEndDate();
            monthly = efn.getMonthly();
            invoicePDF = efn.getInvoicePDF();
            if (fileType=="invalid")
            {
              writeToLogFile(
                "   Cannot process file "+ ebillzFileName +
                " with invalid file name format");
              invalidFormatCount = invalidFormatCount + 1;
              moveTo(invalidDir);
            }
            else
            {
              // check that account exists
              if (dba.accountExists(accountNumber))
              {
                accountIds = dba.getAccountIds(accountNumber);
                accountId = accountIds[0];
                paymentGroupId = accountIds[1];
                customerId = accountIds[2];
                // determine type of processing for file
                if (accountId!=-1)
                {
                  if (fileType=="cdr")
                  {
                    if (monthly)
                    {
                      // monthly cdr
                      filename = EBILLZ + USCORE + accountNumber + USCORE +
                        period + USCORE + invoiceNumber + USCORE +
                        MONTHLYCDRS + ZIP;
                      if (invoiceNumber.compareTo("")==0)
                      {
                        invoiceId = dba.ebillzInvoiceWLR(accountId,period);
                        if (invoiceId==-1)
                        {
                          writeToLogFile(
                            "   cannot process WLR file "+ebillzFileName+" until invoice created");
                          invoiceFailureCount = invoiceFailureCount + 1;
                          moveTo(recycleDir);
                        }
                        else
                        {
                          String invoiceNo = dba.ebillzInvoiceNo(invoiceId);
                          filename = EBILLZ + USCORE + accountNumber + USCORE +
                            period + USCORE + invoiceNo + USCORE +
                            MONTHLYWLRCDRS + ZIP;
                          path = ENERGIS + FWDSLASH + EBILLZ + FWDSLASH +
                            customerId + FWDSLASH + paymentGroupId + FWDSLASH +
                            accountId + FWDSLASH + invoiceId + FWDSLASH;
                          location = path + filename;
                          if(dba.invoiceAttachmentExistsWLR(invoiceId,period))
                          {
                            if (dba.updateInvoiceAttachmentWLR(location,invoiceId,period))
                            {
                              if (!moveFile())
                              {
                                writeToLogFile
                                  ("   failed to move file " + ebillzFileName + " into the attachments directory");
                                  moveFailureCount = moveFailureCount + 1;
                              }
                              else
                                successCount = successCount + 1;
                              }
                            else
                            {
                              writeToLogFile("   failed to update existing invoice attachment for "+ebillzFileName);
                              attachmentFailureCount = attachmentFailureCount + 1;
                            }
                          }
                          else
                          {
                            if (dba.createAttachment(
                                  location,MTHLYWLRCDRS,invoiceId,INVOICE,
                                  period,BATCH,CDR,NULL))
                            {
                              if (!moveFile())
                              {
                                writeToLogFile
                                  ("   failed to move file " + ebillzFileName + " into the attachments directory");
                                  moveFailureCount = moveFailureCount + 1;
                              }
                              else
                                successCount = successCount + 1;
                              }
                            else
                            {
                              writeToLogFile("   failed to create invoice attachment for "+ebillzFileName);
                              attachmentFailureCount = attachmentFailureCount + 1;
                            }
                          }
                        }
                      }
                      else
                      {
                        invoiceId = dba.ebillzInvoice(invoiceNumber,accountId,period);
                        if (invoiceId==-1)
                        {
                          writeToLogFile("   invoice processing failed for "+ebillzFileName);
                          invoiceFailureCount = invoiceFailureCount + 1;
                        }
                        else
                        {
                          path = ENERGIS + FWDSLASH + EBILLZ + FWDSLASH +
                            customerId + FWDSLASH + paymentGroupId + FWDSLASH +
                            accountId + FWDSLASH + invoiceId + FWDSLASH;
                          location = path + filename;
                          if(dba.invoiceCDRAttachmentExists(invoiceId,period))
                          {
                            if (dba.updateCDRInvoiceAttachment(location,invoiceId,period))
                            {
                              if (!createSummaries(period))
                              {
                                writeToLogFile
                                  ("   failed to create SUM and CLI for file " + ebillzFileName);
                                  summaryFailureCount = summaryFailureCount + 1;
                              }
                              else
                              {
                                if (!moveMonthlySet())
                                {
                                  writeToLogFile
                                    ("   failed to move file " + ebillzFileName + " into the attachments directory");
                                    moveFailureCount = moveFailureCount + 1;
                                }
                                else
                                  successCount = successCount + 1;
                                }
                              }
                            else
                            {
                              writeToLogFile("   failed to update existing invoice attachment for "+ebillzFileName);
                              attachmentFailureCount = attachmentFailureCount + 1;
                            }
                          }
                          else
                          {
                            if (dba.createAttachment(
                                  location,MTHLYCDRS,invoiceId,INVOICE,period,BATCH,CDR,NULL))
                            {
                              if (!createSummaries(period))
                              {
                                writeToLogFile
                                  ("   failed to create SUM and CLI for file " + ebillzFileName);
                                  summaryFailureCount = summaryFailureCount + 1;
                              }
                              else
                              {
                                if (!moveMonthlySet())
                                {
                                  writeToLogFile
                                    ("   failed to move file " + ebillzFileName + " into the attachments directory");
                                    moveFailureCount = moveFailureCount + 1;
                                }
                                else
                                  successCount = successCount + 1;
                              }
                            }
                            else
                            {
                              writeToLogFile("   failed to create invoice attachment for "+ebillzFileName);
                              attachmentFailureCount = attachmentFailureCount + 1;
                            }
                          }
                        }
                      }

                    }
                    else
                    {
                      // daily/weekly cdr
                      filename = EBILLZ + USCORE + accountNumber + USCORE + startDate + USCORE;
                      if (startDate!=endDate)
                        filename = filename + TO + USCORE + endDate + USCORE;
                      filename = filename + CDRS + ZIP;
                      path = ENERGIS + FWDSLASH + EBILLZ + FWDSLASH + customerId + FWDSLASH +
                         paymentGroupId + FWDSLASH + accountId + FWDSLASH;
                      location = path + filename;
                      if (dba.accountAttachmentExists(accountId,endDate))
                        if (dba.updateAccountAttachment(location,accountId,endDate))
                        {
                          if (!moveFile())
                          {
                            writeToLogFile
                              ("   failed to move file " + ebillzFileName + " into the attachments directory");
                              moveFailureCount = moveFailureCount + 1;
                          }
                          else
                            successCount = successCount + 1;
                            processedDailyFiles = true;
                        }
                        else
                        {
                          writeToLogFile("   failed to update existing account attachment for "+ebillzFileName);
                          attachmentFailureCount = attachmentFailureCount + 1;
                        }
                      else
		        if (dba.createAttachment
                            (location,CDRS,accountId,ACCOUNT,endDate,BATCH,CDR,NULL))
                        {
                          if (!moveFile())
                          {
                            writeToLogFile
                              ("   failed to move file " + ebillzFileName + " into the attachments directory");
                              moveFailureCount = moveFailureCount + 1;
                          }
                          else
                            successCount = successCount + 1;
                            processedDailyFiles = true;
                        }
                        else
                        {
                          writeToLogFile("   failed to create account attachment for "+ebillzFileName);
                          attachmentFailureCount = attachmentFailureCount + 1;
                        }
                    }
                  }
                  else
                  {
                    if (invoicePDF)
                    {
                      // invoice pdf
                      filename = EBILLZ + USCORE + accountNumber + USCORE +
                        period + USCORE + invoiceNumber + PDF;
                      invoiceId = dba.ebillzInvoice(invoiceNumber,accountId,period);
                      if (invoiceId==-1)
                      {
                        writeToLogFile("   invoice processing failed for "+ebillzFileName);
                          invoiceFailureCount = invoiceFailureCount + 1;
                      }
                      else
                      {
                        path = ENERGIS + FWDSLASH + EBILLZ + FWDSLASH +
                          customerId + FWDSLASH + paymentGroupId + FWDSLASH +
                          accountId + FWDSLASH + invoiceId + FWDSLASH;
                        location = path + filename;
                        if (dba.invoicePDFAttachmentExists(invoiceId,period))
                        {
                          if (dba.updatePDFInvoiceAttachment(location,invoiceId,period))
                          {
                            if (!moveFile())
                            {
                              writeToLogFile
                                ("   failed to move file " + ebillzFileName + " into the attachments directory");
                                moveFailureCount = moveFailureCount + 1;
                            }
                            else
                              successCount = successCount + 1;
                          }
                          else
                          {
                            writeToLogFile("   failed to update existing invoice attachment for "+ebillzFileName);
                            attachmentFailureCount = attachmentFailureCount + 1;
                          }
                        }
                        else
                        {
                          if (dba.createAttachment(
                                location,INVOICEMC,invoiceId,INVOICE,period,BATCH,REPORT,NULL))
                          {
                            if (!moveFile())
                            {
                              writeToLogFile
                                ("   failed to move file " + ebillzFileName + " into the attachments directory");
                                moveFailureCount = moveFailureCount + 1;
                            }
                            else
                              successCount = successCount + 1;
                          }
                          else
                          {
                            writeToLogFile("   failed to create invoice attachment for "+ebillzFileName);
                            attachmentFailureCount = attachmentFailureCount + 1;
                          }
                        }
                      }
                    }
                    else
                    {
                      filename = EBILLZ + USCORE + accountNumber + USCORE +
                        period + USCORE + invoiceNumber + USCORE + OTHER + ZIP;
                      invoiceId = dba.ebillzInvoice(invoiceNumber,accountId,period);
                      if (invoiceId==-1)
                      {
                        writeToLogFile("   invoice processing failed for "+ebillzFileName);
                          invoiceFailureCount = invoiceFailureCount + 1;
                      }
                      else
                      {
                        path = ENERGIS + FWDSLASH + EBILLZ + FWDSLASH +
                          customerId + FWDSLASH + paymentGroupId + FWDSLASH +
                          accountId + FWDSLASH + invoiceId + FWDSLASH;
                        location = path + filename;
                        if (dba.invoiceOtherAttachmentExists(invoiceId,period))
                        {
                          if (dba.updateOtherInvoiceAttachment(location,invoiceId,period))
                          {
                            if (!moveFile())
                            {
                              writeToLogFile
                                ("   failed to move file " + ebillzFileName + " into the attachments directory");
                                moveFailureCount = moveFailureCount + 1;
                            }
                            else
                              successCount = successCount + 1;
                          }
                          else
                          {
                            writeToLogFile("   failed to update existing invoice attachment for "+ebillzFileName);
                            attachmentFailureCount = attachmentFailureCount + 1;
                          }
                        }
                        else
                        {
                          if (dba.createAttachment(
                                location,OTHERREPORTS,invoiceId,INVOICE,period,BATCH,REPORT,NULL))
                          {
                            if (!moveFile())
                            {
                              writeToLogFile
                                ("   failed to move file " + ebillzFileName + " into the attachments directory");
                                moveFailureCount = moveFailureCount + 1;
                            }
                            else
                              successCount = successCount + 1;
                          }
                          else
                          {
                            writeToLogFile("   failed to create invoice attachment for "+ebillzFileName);
                            attachmentFailureCount = attachmentFailureCount + 1;

                          }
                        }
                      }
                    }
                  }
                }
              }
              else
              {
                writeToLogFile(
                  "   Cannot process file "+ ebillzFileName +
                  " as account "+accountNumber+ " does not exist");
                missingAccountCount = missingAccountCount + 1;
                moveTo(invalidDir);
              }
            }
          }
        }
        else
        {
          writeToLogFile("   No "+mode+" files in directory "+dir);
        }
      }
      catch (Exception ex)
      {
        writeToLogFile("Error in processFiles("+mode+","+dir+"): " + ex.getMessage());
      }
    }
    return success;
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
        "' to log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void openLogFile()
  {
    // open a new log file
    try
    {
      runTS = su.reformatDate("now", null, DTFMT);
      logFile = new File(logDir + File.separator + runTS + "_ebillz_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("ebillz processing started at " +
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
      writeToLogFile("ebillz processing ended at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
      writeToLogFile("   files processed         : "+fileCount);
      if (invalidFormatCount>0)
        writeToLogFile("   invalid format          : "+invalidFormatCount);
      if (missingAccountCount>0)
        writeToLogFile("   missing account         : "+missingAccountCount);
      if (invoiceFailureCount>0)
        writeToLogFile("   invoice failure         : "+invoiceFailureCount);
      if (attachmentFailureCount>0)
        writeToLogFile("   attachment failures     : "+attachmentFailureCount);
      if (moveFailureCount>0)
        writeToLogFile("   move failures           : "+moveFailureCount);
      if (summaryFailureCount>0)
        writeToLogFile("   create summary failure  : "+summaryFailureCount);
      writeToLogFile("   sucessfully processed   : "+successCount);
      logWriter.close();
      if (processedDailyFiles)
      {
        String[] ncArgs = new String[1];
        ncArgs[0] = "ebillz";
        NotifyControl.main(ncArgs);
      }
    }
    catch (Exception ex)
    {
      System.out.println("Error closing log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private String dailyRunPeriod()
  {
    String period = "";
    GregorianCalendar gc = new GregorianCalendar();
    String now = su.DateToString(gc.getTime());
    period = FIRST + HYPHEN + now.substring(3,6) + HYPHEN + now.substring(9,11);
    return period;
  }

  private static String extractRunPeriod(String[] args)
  {
    // get supplied period
    String runPeriod = null;
    if (args.length == 0)
    {
      System.out.println("No run date supplied");
      System.exit(1);
    }
    else
    {
      runPeriod = args[0];
    }
    return runPeriod;
  }

  private static String tidyFileName(String filename)
  {
    String tidiedName = filename;
    if (filename.endsWith(".PDF"))
      tidiedName = filename.substring(0,filename.length()-4)+".pdf";
    return tidiedName;
  }

  private boolean validRunPeriod(String runPeriod, String mode)
  {
    // check that supplied period is in 01-MON-YY format
    String year = "";
    String month = "";
    String day = "";
    String hyphen1 = "";
    String hyphen2 = "";
    boolean validFormat = true;
    if ((runPeriod.startsWith(DAILY))||(runPeriod.startsWith(MONTHLY)))
    {
      runPeriod = dailyRunPeriod();
    }
    if (runPeriod.length()!=9)
    {
      validFormat = false;
    }
    else
    {
      day = runPeriod.substring(0,2).trim();
      month = runPeriod.substring(3,6).toUpperCase();
      year = runPeriod.substring(7,9);
      hyphen1 = runPeriod.substring(2,3);
      hyphen2 = runPeriod.substring(6,7);
      if ( (!(day.startsWith("01"))) || (!(hyphen1.startsWith("-"))) || (!(hyphen2.startsWith("-"))) )
        validFormat = false;
      else if (!(su.isNumeric(year) ))
        validFormat = false;
      else if ( (!(month.startsWith("JAN"))) && (!(month.startsWith("FEB"))) && (!(month.startsWith("MAR"))) &&
                (!(month.startsWith("APR"))) && (!(month.startsWith("MAY"))) && (!(month.startsWith("JUN"))) &&
                (!(month.startsWith("JUL"))) && (!(month.startsWith("AUG"))) && (!(month.startsWith("SEP"))) &&
                (!(month.startsWith("OCT"))) && (!(month.startsWith("NOV"))) && (!(month.startsWith("DEC"))) )
        validFormat = false;
    }
    if ( (!(validFormat)) && (mode.startsWith("drop")) )
    {
      System.out.println("Period not supplied in valid 01-MON-YY format");
      writeToLogFile("   Period not supplied in valid 01-MON-YY format");
    }
    return validFormat;
  }

  private boolean DBConnected()
  {
    // check connection to oracle database
    if (dba.oracleTest())
    {
      System.out.println("Database connection successful");
      return true;
    }
    else
    {
      System.out.println("Database connection failed");
      writeToLogFile("JOB ABANDONED DUE TO FAILURE TO MAKE DATABASE CONNECTION TO EBILLING");
      return false;
    }
  }

  public static void main(String[] args)
  {
    // control process flow
    ebillzControl dc = new ebillzControl();
    dc.openLogFile();
    String period = extractRunPeriod(args);
    if (dc.DBConnected())
    {
      if (dc.checkRunControl())
      {
        dc.processFiles("recycle", period);
        dc.processFiles("drop", period);
        dc.resetRunControl();
      }
    }
    dc.closeLogFile();
    System.out.println("ebillz file processing completed");
  }

}



