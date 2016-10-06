package JavaUtil;

public class StoredProcParamDescriptor {

  private int position;
  private int typeNumber;
  private String type;
  private Object value;
  private String[] typeArray = {"STRING","LONG","INT","BOOLEAN","FLOAT","DOUBLE"};

  public StoredProcParamDescriptor(int position, String type,
    Object value)
  {
    this.position = position;
    this.type = type;
    this.value = value;
    setTypeNumber();
  }

  public int getPosition()
  {
    return position;
  }

  public String getType()
  {
    return type;
  }

  public void setTypeNumber()
  {
    boolean found = false;
    for (int i = 0; i < 6; i++)
    {
      if (typeArray[i].equalsIgnoreCase(type))
      {
        found = true;
        typeNumber = i;
        break;
      }
    }
    if (!found)
    {
      typeNumber = -1;
    }
  }

  public int getTypeNumber()
  {
    return typeNumber;
  }

  public Object getValue()
  {
    return value;
  }

  public String getStringValue()
  {
    return (String)value;
  }

  public long getLongValue()
  {
    return ((Long)value).longValue();
  }

  public int getIntValue()
  {
    return ((Integer)value).intValue();
  }

  public boolean getBooleanValue()
  {
    return ((Boolean)value).booleanValue();
  }

  public float getFloatValue()
  {
    return ((Float)value).floatValue();
  }

  public double getDoubleValue()
  {
    return ((Double)value).doubleValue();
  }

}