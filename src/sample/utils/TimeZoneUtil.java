package sample.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Utility class for converting time and date to UTC for storage in a database.
 */
public class TimeZoneUtil {

    /**
     * Converts a given time and date string to UTC for storage in a database.
     * The input time and date string should be in the format "yyyy-MM-dd HH:mm:ss".
     *
     * @param dateTime The input time and date string to be converted to UTC.
     * @return The converted time and date string in UTC format, "yyyy-MM-dd HH:mm:ss".
     */
    public static String convertTimeDateUTC(String dateTime) {
        Timestamp currentTimeStamp = Timestamp.valueOf(String.valueOf(dateTime));
        LocalDateTime LocalDT = currentTimeStamp.toLocalDateTime();
        ZonedDateTime zoneDT = LocalDT.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime utcDT = zoneDT.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime localOUT = utcDT.toLocalDateTime();
        String utcOUT = localOUT.format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss"));
        return utcOUT;
    }
}

