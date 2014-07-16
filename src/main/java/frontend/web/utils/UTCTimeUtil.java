/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.web.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author armen
 */
public class UTCTimeUtil {

    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date getUTCdatetimeAsDate() {
        //note: doesn't check for null
        return stringDateToDate(getUTCdatetimeAsString());
    }

    public static String getUTCdatetimeAsString() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        final String utcTime = sdf.format(new Date());

        return utcTime;
    }

    public static Date stringDateToDate(String StrDate) {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

        try {
            dateToReturn = (Date) dateFormat.parse(StrDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateToReturn;
    }
}
