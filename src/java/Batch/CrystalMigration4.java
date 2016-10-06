package Batch;

import java.io.*;
import java.text.*;
import java.util.*;

public class CrystalMigration4
{

  private CrystalMigration4()
  {
  }

  private void readFiles(String path)
  {
    try
    {
      File rootFile = new File(path);
      System.out.println("Found:"+rootFile.exists());
      File[] reports = rootFile.listFiles();
      for (int k = 0; k < reports.length; k++)
      {
        System.out.println("Name:"+reports[k].getName());
      }
    }
    catch (Exception ex)
    {
      System.out.println(ex.getMessage());
    }
  }

  public static void main(String[] args)
  {
    CrystalMigration4 cm = new CrystalMigration4();
    String path = args[0];
    cm.readFiles(path);
  }

}



