package com.example.angel.cloudy.utilities;

import android.content.Context;
import android.util.Log;

import com.example.angel.cloudy.R;
import com.example.angel.cloudy.data.CloudyPreferences;

public class CloudyWeatherUtils {


    /** Este método convierte la temperatura de Celcios a Farenheit     */
    private static double celsiusToFahrenheit(double temperatureInCelsius) {
        double temperatureInFahrenheit = (temperatureInCelsius * 1.8) + 32;
        return temperatureInFahrenheit;
    }

    /** Este método da formato necesario para presentar los datos en °C o °F
     * de acuerdo a las preferencias del usuario.
     */
    public static String formatTemperature(Context context, double temperature) {
        int temperatureFormatResourceId = R.string.format_temperature_celsius;

        if (!CloudyPreferences.isMetric(context)) {
            temperature = celsiusToFahrenheit(temperature);
            temperatureFormatResourceId = R.string.format_temperature_fahrenheit;
        }
        return String.format(context.getString(temperatureFormatResourceId), temperature);
    }

    /** Este método da formato a las Temperaturas Max y Min redondeando numeros*/
    public static String formatHighLows(Context context, double high, double low) {
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String formattedHigh = formatTemperature(context, roundedHigh);
        String formattedLow = formatTemperature(context, roundedLow);

        String highLowStr = formattedHigh + " / " + formattedLow;
        return highLowStr;
    }


}
