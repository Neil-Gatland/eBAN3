package Batch;

import java.io.*;
import java.text.*;
import java.util.*;

public class CrystalMigration2
{

  private CrystalMigration2()
  {
  }

  private void migrateReports()
  {
    try
    {
      File controlFile = new File("D:\\temp\\cm\\control.txt");
      FileReader fr = new FileReader(controlFile);
      BufferedReader br = new BufferedReader(fr);
      File noFile = new File("D:\\temp\\cm\\nofile.txt");
      noFile.createNewFile();
      FileWriter fw1 = new FileWriter(noFile);
      BufferedWriter bw1 = new BufferedWriter(fw1);
      File newControlFile = new File("D:\\temp\\cm\\newcontrol.txt");
      newControlFile.createNewFile();
      FileWriter fw2 = new FileWriter(newControlFile);
      BufferedWriter bw2 = new BufferedWriter(fw2);
      File notFoundFile = new File("D:\\temp\\cm\\invalid.txt");
      notFoundFile.createNewFile();
      FileWriter fw3 = new FileWriter(notFoundFile);
      BufferedWriter bw3 = new BufferedWriter(fw3);
      ////
      ////
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

      boolean eof = false;
      while (!eof)
      {
        String line = br.readLine();
        if (line == null)
        {
          eof = true;
        }
        else
        {
          int start = 0;
          int pos = line.indexOf(",");
          String gcId = line.substring(start, pos).trim();
          start = ++pos;
          pos = line.indexOf(",", start);
          String invoiceNo = line.substring(start, pos).trim();
          start = ++pos;
          pos = line.indexOf(",", start);
          String reportName = line.substring(start, pos).trim();
          start = ++pos;
          pos = line.indexOf(",", start);
          String fileLocation = line.substring(start, pos).trim();
          File reportFile = new File(fileLocation);

          start = ++pos;
          String reportDate = line.substring(start, line.length()).trim();

          if (!reportFile.exists())
          {
            bw1.write(line+"\n");
          }
          else
          {
            FileReader fr2 = new FileReader(reportFile);
            BufferedReader br2 = new BufferedReader(fr2);
            boolean eof2 = false;
            boolean parametersFound = false;
            String line2 = null;
            while (!eof2)
            {
              line2 = br2.readLine();
              if (line2 == null)
              {
                eof2 = true;
              }
              else
              {
              //@CustId @InvoiceNo
                if (line2.indexOf("@Global_Customer_Id") > 0)
                {
                  parametersFound = true;
                  break;
                }
              }
            }
            br2.close();
            fr2.close();
            if (!parametersFound)
            {
              bw3.write(line+"\n");
            }
            else
            {
              start = line2.indexOf("@Global_Customer_Id");
              start = line2.indexOf("- ", start);
              start += 4;
              gcId = line2.substring(start, start+3);
              start = line2.indexOf("- ", start);
              start += 4;
              invoiceNo = line2.substring(start, line2.indexOf(0, start));
              String outLine = gcId + "," + invoiceNo + "," + reportName + "," +
                fileLocation + "," + reportDate;
              /*boolean numericInvoice = true;
              try
              {
                int invNo = Integer.parseInt(invoiceNo);
              }
              catch (NumberFormatException ne)
              {
                numericInvoice = false;
              }*/
              if (!isAlphaNumeric(invoiceNo))
              {
                bw3.write(outLine+"\n");
              }
              else if (!isAlphaNumeric(gcId))
              {
                  bw3.write(outLine+"\n");
              }
              else
              {
                  bw2.write(outLine+"\n");
              }
            }
          }
        }
      }
      br.close();
      fr.close();
      bw3.close();
      bw2.close();
      bw1.close();
      fw3.close();
      fw2.close();
      fw1.close();
    }
    catch (Exception ex)
    {
      System.out.println(ex.getMessage());
    }
  }

  private boolean isAlphaNumeric(final String s) {
    final char[] chars = s.toCharArray();
    for (int x = 0; x < chars.length; x++) {
      final char c = chars[x];
      if ((c >= 'a') && (c <= 'z')) continue; // lowercase
      if ((c >= 'A') && (c <= 'Z')) continue; // uppercase
      if ((c >= '0') && (c <= '9')) continue; // numeric
      return false;
    }
    return true;
  }

  public static void main(String[] args)
  {
    CrystalMigration2 cm = new CrystalMigration2();
    cm.migrateReports();
  }

}



