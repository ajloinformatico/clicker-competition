package es.lojo.clickercompetition.demo.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyDates {
    /**
     * Para obtener la fecha con el formato deseado:
     * con SimpleDateFormat("formato").format() -> le indico lel formato en Strin 00 - 00 - 0000
     * dentro de format instancio un objeto fecha que devuelve la fecha de hoy
     * @return {String} fecha formateada a string
     */
    public static String today() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());

    }

    /**
     *To obtain yesterday's date in the desired format:
     * with SimpleDateFormat ("format"). format () -> I indicate the format in Strin 00 - 00 - 0000
     * inside format I instantiate a Date () object
     * As a Date argument to obtain yesterday's day I pass a Date object with .getTime () + TimeUnit I get the desired day
     * of TimeUnit I call the method DAYS.toMillis (-1) the -1 indicates the number of days that remain from the current date
     * Being -1 subtracts one from today, if you use instead of DAYS MOUTH it would be months the same with HOURS
     * if I indicate instead of toMillis (-1) -> (-7) it would be seven days less than the current date
     *
     * @return {String} wiched desired date formating to String
     */
    public static String getDay(int daysBack) {
        return  new SimpleDateFormat("dd-MM-yyyy").format( new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(daysBack)));
    }


}
