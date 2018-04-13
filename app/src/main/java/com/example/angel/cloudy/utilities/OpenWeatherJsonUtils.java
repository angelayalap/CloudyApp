package com.example.angel.cloudy.utilities;

import android.content.ContentValues;
import android.content.Context;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class OpenWeatherJsonUtils {

    /*Debido a que la API OpenWeatherMap no regresa valores de Temperatura Max y Min en version gratuita,
     * Se opta por usar Weatherbit es posible que se encuentre en codigo referencias OpenWeatherMap*/
    private static final Random randomNumber = new Random();

    /**
     * Este metodo analiza la respuesta JSON de una web y regresa un arreglo de Strings
     * describiendo el pronostico del clima de varios dias.
     *
     * @param forecastJsonStr Es la respuesta JSON del servidor
     * @return Regresa un Arreglo de Strings describiendo datos de clima
     * @throws JSONException Si ocurro algun error con los datos JSON se dispara excepcion
     *
     * @referencia Se usa Weatherbit API como referencia
     *             https://www.weatherbit.io/api/weather-forecast-16-day
     */
    public static String[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        /*Informacion de clima. Se asocian variables para los valores usados por API Weatherbit
        Para la API la informacion del pronostico de cada dia es un elemento del arreglo "data" */
        final String OWM_LIST = "data";

        /* Para la API OpenWeatherMap todas las temperaturas son hijos del objeto "main" en la version de paga "temp"
        * Para la API Weatherbit se usan los siguientes valores definidos en las variables*/
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max_temp";
        final String OWM_MIN = "min_temp";
        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "description";
        //final String OWM_MESSAGE_CODE = "cod";

        /* Arreglo tipo String que contendra la informacion de cada día*/
        String[] parsedWeatherData = null;

        /*Se crea objeto JSON*/
        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        /*Se crea el arreglo JSON tiene como parametro la lista que contendra la info de cada dia*/
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
        parsedWeatherData = new String[weatherArray.length()];

        /*Se obtiene la fecha actual del sistema y se normaliza con la clase CloudyDatUtils*/
        long localDate = System.currentTimeMillis();
        long utcDate = CloudyDateUtils.getUTCDateFromLocal(localDate);
        long startDay = CloudyDateUtils.normalizeDate(utcDate);

        /*Se recorre el arreglo de cada dia obtenido*/
        for (int i = 0; i < weatherArray.length(); i++) {
            String date;
            String highAndLow;

            /* Valores que seran recolectados */
            long dateTimeMillis;
            double high;
            double low;
            String description;

            /* Se obtiene del objeto JSON el dia */
            JSONObject dayForecast = weatherArray.getJSONObject(i);
            dateTimeMillis = startDay + CloudyDateUtils.DAY_IN_MILLIS * i;
            date = CloudyDateUtils.getFriendlyDateString(context, dateTimeMillis, false);

            /*Se obtiene la descripcion del clima*/
            //JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            JSONObject weatherObject = dayForecast.getJSONObject(OWM_WEATHER);
            description = weatherObject.getString(OWM_DESCRIPTION);

            /*Se obtienen las temperaturas Max y Min*/
            //JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            high = dayForecast.getDouble(OWM_MAX);
            low = dayForecast.getDouble(OWM_MIN) ;
            highAndLow = CloudyWeatherUtils.formatHighLows(context, high, low);

            /*Se llena el arreglo que regresara la información y continua el ciclo*/
            parsedWeatherData[i] = date + " | " + description + " | " + highAndLow;
        }

        return parsedWeatherData;
    }


}
