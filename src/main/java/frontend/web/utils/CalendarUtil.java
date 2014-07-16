/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.web.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class CalendarUtil {

  static private Hashtable<Integer, String> daysOfWeek = null;
  static private Hashtable<Integer, String> monthsOfYear = null;
  static private Hashtable<Integer, String> monthsOfYearShort = null;
  private Logger log = null;
  Calendar cal = null;

  public CalendarUtil() {
    log = Logger.getLogger(CalendarUtil.class);
    cal = Calendar.getInstance();
  }

  public int getDayOfMonth() {
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  public String getDayOfWeek() {
    return getDayOfWeek(cal);
  }
  
  public String getMonthOfYear() {
    return getMonthOfYear(cal);
  }

  public String getMonthOfYearShort() {
    return getMonthOfYearShort(cal);
  }
  static public String getDayOfWeek(Calendar cal) {
    if (daysOfWeek == null) {
      FacesContext facesContext = FacesContext.getCurrentInstance();

      try {
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "daysOfWeek");
        String value = "";
        daysOfWeek = new Hashtable<Integer, String>();
        for (int k = 1; k <= 7; k++) {
          value = bundle.getString(String.valueOf(k));
          daysOfWeek.put(k, value);
        }
      } catch (Exception e) {
        
      }
    }

    return daysOfWeek.get(cal.get(Calendar.DAY_OF_WEEK));
  }
  
  static public String getMonthOfYear(Calendar cal) {
    if (monthsOfYear == null) {
      FacesContext facesContext = FacesContext.getCurrentInstance();

      try {
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "monthsOfYear");
        String value = "";
        monthsOfYear = new Hashtable<Integer, String>();
        for (int k = 0; k < 12; k++) {
          value = bundle.getString(String.valueOf(k));
          monthsOfYear.put(k, value);
        }
      } catch (Exception e) {
        
      }
    }
    return monthsOfYear.get(cal.get(Calendar.MONTH));
  }

  static public String getMonthOfYearShort(Calendar cal) {
    if (monthsOfYearShort == null) {
      FacesContext facesContext = FacesContext.getCurrentInstance();

      try {
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "monthsOfYearShort");
        String value = "";
        monthsOfYearShort = new Hashtable<Integer, String>();
        for (int k = 0; k < 12; k++) {
          value = bundle.getString(String.valueOf(k));
          monthsOfYearShort.put(k, value);
        }
      } catch (Exception e) {
        
      }
    }
    return monthsOfYearShort.get(cal.get(Calendar.MONTH));
  }

  static public Date getDateValue(String datePart) {
   if(datePart == null){
      return null;
    }   
    Date dateValue = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");    
    try {
      dateValue = sdf.parse(datePart);
    } catch (ParseException e) {
    }
    return dateValue;
  }  
  
  
  static public String formatDateValue(Date datePart) {
    if(datePart == null){
      return null;
    }
    String stringValue = null;
    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    stringValue = df.format(datePart);

    return stringValue;
  }  
  
    static public String formatDateTimeValue(Date datePart) {
    if(datePart == null){
      return null;
    }
    String stringValue = null;
    DateFormat df = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");
    stringValue = df.format(datePart);

    return stringValue;
  }  
  
  static public String formatDateValueMerged(Date datePart) {
    if(datePart == null){
      return null;
    }
    String stringValue = null;
    DateFormat df = new SimpleDateFormat("ddMMyyyy");
    stringValue = df.format(datePart);

    return stringValue;
  }  
}
