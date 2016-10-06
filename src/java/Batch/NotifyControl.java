package Batch;

import java.io.*;
import java.text.*;
import java.util.*;


/**
 * The command line user interface class for email notification.
 *
 * <P>This class just processes the command line parameters, invokes the appropriate
 * methods in {@link NotifyGenerator}, and outputs some statistical log data.</P>
 *
 * <P>Copyright (c) 2007  Danet Ltd</P>
 *
 * @author Tim Aitchison
 * @version 1.0
 */

public class NotifyControl
{

  public static final String WEEK = "WEEKLY";

  private NotifyControl()
  {
  }

  private boolean generateEmails(String source)
  {
    NotifyGenerator ng = new NotifyGenerator();
    return ng.generateAndSendAll(source);
  }

  private boolean generateKostanzaEmails(String source)
  {
    NotifyGenerator ng = new NotifyGenerator(source);
    return ng.generateKostanzaEmails(source);
  }

  private boolean generateCompletionEmail(String source)
  {
    NotifyGenerator ng = new NotifyGenerator();
    return ng.generateCompletionEmail(source);
  }

  private boolean generateStatsEmail(String source)
  {
    NotifyGenerator ng = new NotifyGenerator();
    return ng.generateStatsEmail(source);
  }

  private boolean generateArborEmail(String source)
  {
    NotifyGenerator ng = new NotifyGenerator();
    return ng.generateArborEmail(source);
  }

  private boolean generateMonitorEmail(String source)
  {
    NotifyGenerator ng = new NotifyGenerator();
    return ng.generateMonitorEmail(source);
  }

  private boolean generateQueryEmail()
  {
    NotifyGenerator ng = new NotifyGenerator();
    return ng.generateQueryEmail();
  }

  private boolean getMail()
  {
    NotifyGenerator ng = new NotifyGenerator();
    ng.getMail();
    return true;
  }

  private boolean deleteAutoresponse()
  {
    NotifyGenerator ng = new NotifyGenerator();
    ng.deleteAutoresponse();
    return true;
  }

  private boolean sendAutoresponse()
  {
    NotifyGenerator ng = new NotifyGenerator();
    ng.sendAutoresponse();
    return true;
  }

  private void generateTestSignEmail(String toAddress, String cert)
  {
    NotifyGenerator ng = new NotifyGenerator();
    ng.sendSignedOpaqueEnvelopedEmail2(toAddress, "test sign", "test sign and return",
      null, cert);
  }

  private void generateTestEmail(String toAddress)
  {
    NotifyGenerator ng = new NotifyGenerator();
    ng.sendTestEmail(toAddress, "test sign", "test sign content");
  }

  private void generateBulkInvoiceEmail(String toAddress, String accountNo)
  {
    NotifyGenerator ng = new NotifyGenerator();
    ng.generateBulkInvoiceEmail(toAddress, accountNo);
  }

  public static void main(String[] args)
  {
    //String step = extractStep(args);
    //String fileName = extractFileName(args);
    NotifyControl nc = null;
    String source = "none";
    if ((args.length == 1) || (args.length == 2) || (args.length == 3))
    {
      source = args[0];
    }
    try
    {
      nc = new NotifyControl();
      if (source.equalsIgnoreCase("ebillz") || source.equalsIgnoreCase("ssbs") || source.equals("Arbor"))
      {
        nc.generateCompletionEmail(source);
      }
      else if (source.startsWith("stats"))
      {
        nc.generateStatsEmail(source);
      }
      else if (source.startsWith("arbor"))
      {
        nc.generateArborEmail(source);
      }
      else if (source.startsWith("monitor"))
      {
        nc.generateMonitorEmail(source);
      }
      else if (source.startsWith("abqs"))
      {
        nc.generateQueryEmail();
      }
      else if (source.equals("get"))
      {
        nc.getMail();
      }
      else if (source.equals("delAuto"))
      {
        nc.deleteAutoresponse();
      }
      else if (source.equals("sendAuto"))
      {
        nc.sendAutoresponse();
      }
      else if (source.startsWith("testsign"))
      {
        nc.generateTestSignEmail(args[1], args[2]);
      }
      else if (source.equals("test"))
      {
        nc.generateTestEmail(args[1]);
      }
      else if (source.equalsIgnoreCase("bulkInv"))
      {
        nc.generateBulkInvoiceEmail(args[1], args[2]);
      }
      else if (source.equals("testWait"))
      {
        Thread.sleep(Long.parseLong(args[1]));
      }
      else if (source.equalsIgnoreCase("Kostanza"))
      {
        nc.generateKostanzaEmails(source);
      }
      else
      {
        nc.generateEmails(source);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      System.err.println();
    }
    finally
    {
    }
  }
}



