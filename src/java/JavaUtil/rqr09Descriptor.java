package JavaUtil;

import java.util.*;

public class rqr09Descriptor {

  // rqr09 record descriptor used to load line data and get separate data items

  // rqr09 data item variables
  private String accountNo;
  private String providerName;
  private String productCode;
  private String productName;
  private String callMonth;
  private String source;
  private String NTS;
  private String primeCalls;
  private String standardCalls;
  private String economyCalls;
  private String primeHours;
  private String primeMinutes;
  private String primeSeconds;
  private String primeCharge;
  private String standardHours;
  private String standardMinutes;
  private String standardSeconds;
  private String standardCharge;
  private String economyHours;
  private String economyMinutes;
  private String economySeconds;
  private String economyCharge;
  private String totalHours;
  private String totalMinutes;
  private String totalSeconds;
  private String totalCharge;
  private String calls;
  private String networkSource;
  private String differentiatedBillingFlag;
  private String usageCode;
  private String serviceLevelCode;
  // Character constant values
  private static String DOUBLEQUOTE = "\"";
  private static String COMMA = ",";
  private static String SINGLEQUOTE = "'";

  public rqr09Descriptor(String rqr09Line)
  {
    // decode file line into separate data items
    long rqr09LineLength = rqr09Line.length();
    int itemCount = 1;
    String[] dataItems = new String[50];
    dataItems[1] = "";
    boolean firstDoubleQuote = false;
    for (int i=0; i<rqr09LineLength; i++)
    {
      String testChar = rqr09Line.substring(i,i+1);
      // process characters loading strings within double quotes ignoreing embedded commas
      if (testChar.equals(DOUBLEQUOTE))
      {
        // toggle first double quote boolean
        if (firstDoubleQuote)
          firstDoubleQuote = false;
        else
          firstDoubleQuote = true;
      }
      else if (testChar.equals(SINGLEQUOTE))
      {
        // ignore single quotes as they will cause subsequent RQR09 inserts to fail
      }
      else
      {
        // until a comma is reached when not within a double quote set
        // load characters into current string in the data array
        if ((testChar.equals(COMMA))&&(!firstDoubleQuote))
        {
          itemCount++;
          dataItems[itemCount] = "";
        }
        else
          dataItems[itemCount] = dataItems[itemCount] + testChar;
      }
    }
    accountNo = dataItems[1];
    providerName = dataItems[2];
    productCode = dataItems[3];
    usageCode = dataItems[3];
    serviceLevelCode = dataItems[4];
    productName = dataItems[5];
    callMonth = dataItems[6].substring(3,7)+dataItems[6].substring(0,2);
    source = dataItems[7];
    NTS = dataItems[8];
    primeCalls = dataItems[9];
    standardCalls = dataItems[10];
    economyCalls = dataItems[11];
    primeHours = dataItems[12];
    primeMinutes = dataItems[13];
    primeSeconds = dataItems[14];
    primeCharge = dataItems[15];
    standardHours = dataItems[16];
    standardMinutes = dataItems[17];
    standardSeconds = dataItems[18];
    standardCharge = dataItems[19];
    economyHours = dataItems[20];
    economyMinutes = dataItems[21];
    economySeconds = dataItems[22];
    economyCharge = dataItems[23];
    totalHours = dataItems[24];
    totalMinutes = dataItems[25];
    totalSeconds = dataItems[26];
    totalCharge = dataItems[27];
    calls = dataItems[28];
    networkSource = dataItems[29];
    differentiatedBillingFlag = dataItems[30];
  }

  public String getAccountNo()
  {
    return accountNo;
  }

  public String getProviderName()
  {
    return providerName;
  }

  public String getProductCode()
  {
    return productCode;
  }

  public String getProductName()
  {
    return productName;
  }

  public String getCallMonth()
  {
    return callMonth;
  }

  public String getSource()
  {
    return source;
  }

  public String getNTS()
  {
    return NTS;
  }

  public String getPrimeCalls()
  {
    return primeCalls;
  }

  public String getStandardCalls()
  {
    return standardCalls;
  }

  public String getEconomyCalls()
  {
    return economyCalls;
  }

  public String getPrimeHours()
  {
    return primeHours;
  }

  public String getPrimeMinutes()
  {
    return primeMinutes;
  }

  public String getPrimeSeconds()
  {
    return primeSeconds;
  }

  public String getPrimeCharge()
  {
    return primeCharge;
  }

  public String getStandardHours()
  {
    return standardHours;
  }

  public String getStandardMinutes()
  {
    return standardMinutes;
  }

  public String getStandardSeconds()
  {
    return standardSeconds;
  }

  public String getStandardCharge()
  {
    return standardCharge;
  }

  public String getEconomyHours()
  {
    return economyHours;
  }

  public String getEconomyMinutes()
  {
    return economyMinutes;
  }

  public String getEconomySeconds()
  {
    return economySeconds;
  }

  public String getEconomyCharge()
  {
    return economyCharge;
  }

  public String getTotalHours()
  {
    return totalHours;
  }

  public String getTotalMinutes()
  {
    return totalMinutes;
  }

  public String getTotalSeconds()
  {
    return totalSeconds;
  }

  public String getTotalCharge()
  {
    return totalCharge;
  }

  public String getCalls()
  {
    return calls;
  }

  public String getNetworkSource()
  {
    return networkSource;
  }

  public String getDifferentiatedBillingFlag()
  {
    return differentiatedBillingFlag;
  }
  
  public String getUsageCode()
  {
    return usageCode;
  }
  
  public String getServiceLevelCode()
  {
    return serviceLevelCode;
  }

}