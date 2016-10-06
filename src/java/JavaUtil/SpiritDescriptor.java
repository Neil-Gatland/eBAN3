package JavaUtil;

import java.math.BigDecimal;
import java.util.*;

public class SpiritDescriptor {

  private String houseCode;
  private String cli;
  private String accountNo;
  private String billingSource;
  private long gross;
  private long vat;
  private Date billingDate;

  public SpiritDescriptor(String houseCode, String cli, String accountNo,
    String billingSource, long gross, long vat, Date billingDate)
  {
    this.houseCode = houseCode;
    this.cli = cli;
    this.accountNo = accountNo;
    this.billingSource = billingSource;
    this.gross = gross;
    this.vat = vat;
    this.billingDate = billingDate;
  }

  public String getHouseCode()
  {
    return houseCode;
  }

  public String getCLI()
  {
    return cli;
  }

  public String getAccountNo()
  {
    return accountNo;
  }

  public String getBillingSource()
  {
    return billingSource;
  }

  public Date getBillingDate()
  {
    return billingDate;
  }

  public long getGross()
  {
    return gross;
  }

  public long getVat()
  {
    return vat;
  }

  public String getGrossAsString()
  {
    String ret = null;
    String temp = String.valueOf(gross);
    int pos = 0;
    int len = temp.length();
    if (len < 3)
    {
      if (len == 1)
        ret = "0.0" + temp;
      else
        ret = "0." + temp;
    }
    else
    {
      pos = temp.length() - 2;
      ret = temp.substring(0, pos) + "." + temp.substring(pos);
    }
    return ret;
  }

  public String getVatAsString()
  {
    String ret = null;
    String temp = String.valueOf(vat);
    int pos = 0;
    int len = temp.length();
    if (len < 3)
    {
      if (len == 1)
        ret = "0.0" + temp;
      else
        ret = "0." + temp;
    }
    else
    {
      pos = temp.length() - 2;
      ret = temp.substring(0, pos) + "." + temp.substring(pos);
    }
    return ret;
  }


}