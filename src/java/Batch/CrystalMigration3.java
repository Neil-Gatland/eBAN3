package Batch;

import java.io.*;
import java.text.*;
import java.util.*;

public class CrystalMigration3
{

  private CrystalMigration3()
  {
  }

  private void migrateReports()
  {
    try
    {
      File rootFile = new File("D:\\temp\\cm\\drop");
      File[] customers = rootFile.listFiles();
      for (int i = 0; i < customers.length; i++)
      {
        File[] invoices = customers[i].listFiles();
        for (int j = 0; j < invoices.length; j++)
        {
          File[] reports = invoices[j].listFiles();
          for (int k = 0; k < reports.length; k++)
          {
            String newName = reports[k].getName().replace(' ', '_');
            reports[k].renameTo(new File("D:\\temp\\cm\\drop\\" +
              customers[i].getName() + "\\" + invoices[j].getName() + "\\" +
              newName));
          }
        }
      }
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
    CrystalMigration3 cm = new CrystalMigration3();
    cm.migrateReports();
  }

}



