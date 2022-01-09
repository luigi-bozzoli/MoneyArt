package it.unisa.c02.moneyart.utils.formattingDates;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormattingDates {

   /**
    * La data viene restituita con il tempo a mezzanotte
    * @param date la data originaria
    * @return la data con il tempo impostato a mezzanotte
   */
   public static Date setMidnightTime (Date date) {
       if (date == null) return null;
       Date dateM = date;

       dateM.setHours(0);
       dateM.setMinutes(0);
       dateM.setSeconds(0);

       return dateM;
   }

   private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
}