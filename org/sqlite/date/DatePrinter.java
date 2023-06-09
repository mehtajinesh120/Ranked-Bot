package org.sqlite.date;

import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public interface DatePrinter {
  String format(long paramLong);
  
  String format(Date paramDate);
  
  String format(Calendar paramCalendar);
  
  StringBuffer format(long paramLong, StringBuffer paramStringBuffer);
  
  StringBuffer format(Date paramDate, StringBuffer paramStringBuffer);
  
  StringBuffer format(Calendar paramCalendar, StringBuffer paramStringBuffer);
  
  String getPattern();
  
  TimeZone getTimeZone();
  
  Locale getLocale();
  
  StringBuffer format(Object paramObject, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\date\DatePrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */