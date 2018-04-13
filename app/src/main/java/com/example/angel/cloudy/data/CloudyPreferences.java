package com.example.angel.cloudy.data;

import android.content.Context;

public class CloudyPreferences {

    /**
     * Por el momento se define una variable por Default para la ubicacion,
     * se resolvera el seleccionar la ubicacion definida por el usuario.
     */
    private static final String DEFAULT_WEATHER_LOCATION = "Monterrey,MX";


    /** Este método regresa la ubicación definida por usuario, por el momento se tiene una ubicación default*/
    public static String getPreferredWeatherLocation(Context context) {

        return getDefaultWeatherLocation();
    }

    /** Este método regresara la unidad de medicion elegida por el usuario*/
    public static boolean isMetric(Context context) {
        return true;
    }

    private static String getDefaultWeatherLocation() {

        return DEFAULT_WEATHER_LOCATION;
    }

}