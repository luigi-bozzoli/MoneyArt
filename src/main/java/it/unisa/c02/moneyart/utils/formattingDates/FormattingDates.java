package it.unisa.c02.moneyart.utils.formattingDates;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormattingDates {

   public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /*                    Test di utilizzo
    public static void main(String[] args) throws ParseException {
        long  millis=System.currentTimeMillis();
        java.util.Date date = new java.util.Date(millis);
        System.out.println("stampa data: \n"+date);

        String date1 = "12/11/1998";
        System.out.println(sdf.parse(date1)); //converte la stringa in data (tipo Date) e la stampa
    }
    */

}
