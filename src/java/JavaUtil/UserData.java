package JavaUtil;
import java.util.*;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class UserData
{
  protected static Hashtable userData=new Hashtable();
  protected static Random keyGenerator=new Random();

  public UserData()
  {
  }
  public static String createUserData(String userKey)
  {
    //String userKey = ""+keyGenerator.nextLong();
    userData.put(userKey,new Hashtable());
    return userKey;
  }
  public static Hashtable getUserData(String userKey)
  {
    if (userKey == null) return null;
    return (Hashtable) userData.get(userKey);
  }
  public static void clearUserData(String userKey)
  {
    if (userKey == null) return;
    userData.remove(userKey);
  }
 public static void store(String userKey, String Name,Object Value)
 {
  Hashtable ud = getUserData(userKey);
  ud.put(Name,Value);
  userData.put(userKey,ud);
 }
 public static Object retrieve(String userKey, String Name)
 {
  if (userKey == null) return null;
  Hashtable ud = getUserData(userKey);
  return ud.get(Name);
 }
}

