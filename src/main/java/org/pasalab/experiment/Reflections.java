package org.pasalab.experiment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by summerDG on 2018/1/23.
 */
public class Reflections {
  public static Object invokeGetter(Object obj, String fieldName) {
    StringBuffer sb = new StringBuffer();

    sb.append("get");

    sb.append(fieldName.substring(0, 1).toUpperCase());

    sb.append(fieldName.substring(1));

    try {
      Method getter = obj.getClass().getMethod(sb.toString());
      return getter.invoke(obj);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    return null;
  }
  public static Object invokeMethod(Object obj, String methodName, Object[] args) {
    try {
      Method method = obj.getClass().getMethod(methodName);
      return method.invoke(obj, args);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }
}
