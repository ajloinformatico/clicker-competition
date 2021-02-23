package es.lojo.clickercompetition.demo.utilities;

import lombok.Data;

import java.util.Locale;

@Data
public class StringManagement {

    /**
     * internal capitalize
     * @param cadena {String}: String to capitalize
     * @return {String}
     */
    public static  String capitalize(String cadena){
        return cadena.substring(0,1).toUpperCase() +
                cadena.substring(1).toLowerCase();
    }

}
