package pl.kj.bachelors.daily.infrastructure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
    public static String getToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar now = Calendar.getInstance();

        return dateFormat.format(now.getTime());
    }

    public static boolean isHourInFuture(String hour, String timeZoneId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        boolean result;
        try {
            Date subject = dateFormat.parse(String.format("%s %s", getToday(), hour));
            result = subject.getTime() > Calendar.getInstance(TimeZone.getTimeZone(timeZoneId)).getTimeInMillis();
        } catch (ParseException e) {
            result = false;
        }

        return result;
    }
}
