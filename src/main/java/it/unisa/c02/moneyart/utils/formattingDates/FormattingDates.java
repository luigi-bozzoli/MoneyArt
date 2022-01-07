package it.unisa.c02.moneyart.utils.formattingDates;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormattingDates {

    /**
     * Converte una stringa contente una data nel formato "dd/MM/yyyy"
     * in tipo Date (java.util.Date)
     * @param dataS la stringa contente una data nel formato "dd/MM/yyyy"
     * @return la stessa data in formato Date
     */
    public static Date fromStringToDate (String dataS) throws ParseException {
        if (dataS == null) return null;

        return sdf.parse(dataS);

    }

   /**
    * Converte un tipo Date in una stringa del formato "dd/MM/yyyy"
    */
   public static String fromDateToString (Date data) {
      if (data == null) return null;

      return sdf.format(data);

   }

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