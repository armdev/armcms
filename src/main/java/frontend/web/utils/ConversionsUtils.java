package frontend.web.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Armen Arzumanyan
 */
public class ConversionsUtils {

  static public Long getLong(Object o) {
    if (o instanceof Short) {
      return new Long(((Short) o).intValue());
    }
    else if (o instanceof Integer) {
      return new Long(((Integer) o).intValue());
    }
    else if (o instanceof BigDecimal) {
      return new Long(((BigDecimal) o).longValue());
    }
    else {
      return (Long) o;
    }
  }

  static public Integer getInteger(Object o) {
    if (o instanceof Byte) {
      return new Integer(((Byte) o).intValue());
    }
    else if (o instanceof Short) {
      return new Integer(((Short) o).intValue());
    }
    else if (o instanceof Long) {
      return new Integer(((Long) o).intValue());
    }
    else {
      return (Integer) o;
    }
  }
  
  static public Double getDouble(Object o) {
    if (o instanceof Float) {
      return new Double(((Float) o).doubleValue());
    }
    else {
      return (Double) o;
    }
  }

  static public String formatDateForDB(Date inputDate) {
    String pattern = "yyyy-MM-dd";
    String retValue = "";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    if (inputDate == null) {
      return retValue;
    }
    retValue = sdf.format(inputDate);
    return retValue;
  }
  
  static public String formatDate(Date date){
    if(date == null){
      return "";
    }
    String pattern = "dd.MM.yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(date);    
  }
  
  static public String formatDate(java.sql.Date date){
    if(date == null){
      return "";
    }
    String pattern = "dd.MM.yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(date);   
  }

  static public String formatTime(Date date){
    if(date == null){
      return "";
    }
    String pattern = "kk:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(date);    
  }

  static public String formatDouble(Double value){
    if(value == null){
      return "";
    }
    DecimalFormatSymbols fs = new DecimalFormatSymbols();
    fs.setGroupingSeparator(',');
    fs.setDecimalSeparator('.');
    
    DecimalFormat nf = new DecimalFormat("#,###,###,##0.00",fs);    
    return nf.format(value);    
  }
  
  static public String formatDoubleWithOneDecimal(Double value){
    if(value == null){
      return "";
    }
    DecimalFormatSymbols fs = new DecimalFormatSymbols();
    fs.setGroupingSeparator(',');
    fs.setDecimalSeparator('.');
    
    DecimalFormat nf = new DecimalFormat("#,###,###,##0.0",fs);    
    return nf.format(value);    
  }
  
   static public String formatFloatWithOneDecimal(Float value){
    if(value == null){
      return "";
    }
    DecimalFormatSymbols fs = new DecimalFormatSymbols();
    fs.setGroupingSeparator(',');
    fs.setDecimalSeparator('.');
    
    DecimalFormat nf = new DecimalFormat("#,###,###,##0.0",fs);    
    return nf.format(value);    
  }
  
  static public String formatDoubleWithoutDecimals(Double value){
    if(value == null){
      return "";
    }
    DecimalFormatSymbols fs = new DecimalFormatSymbols();
    fs.setGroupingSeparator(',');
    fs.setDecimalSeparator('.');
    
    DecimalFormat nf = new DecimalFormat("#,###,###,##0",fs);    
    return nf.format(value);    
  }

  static public String formatInteger(Integer value){
    if(value == null){
      return "";
    }    
    return String.valueOf(value);
  }
  
}
