package com.example.angel.cloudy.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Esta Clase se usara para comunicarse con el servidor de clima.
 */
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    // se subio un Dummy de Datos a un servidor que tenemos acceso para realizar pruebas
    private static final String STATIC_WEATHER_URL = "http://f9.com.mx/staticdata";
    // Se realizaron pruebas con la API Weatherbit
    private static final String DYNAMIC_WEATHER_URL = "http://api.weatherbit.io/v2.0/forecast/daily";

    private static final String FORECAST_BASE_URL = DYNAMIC_WEATHER_URL;

    /*NOTA: Los variables no impactan las respuestas del Dummy de Datos, solo las de la API
    Se toma como referencia los parametros de http://openweathermap.org/weather-data para definir variables */

    /* Formato que queremos regrese la API Weatherbit maneja XML y JSON*/
    private static final String format = "json";
    /* Unidades de medicion que queremos que regrese la API */
    private static final String units = "M"; //M
    /* Numero de días que queremos regrese la API */
    private static final int numDays = 14;
    /*Llave generada en la API para realizar consultas*/
    private static final String apiKey = "4be6de292e2c40c6abe8be29a6f5b52d"; //35a24d3850bf4206353a8cf3b219f755
    /*Idioma en que se desea respuesta de la API soli afecta la descripcion del clima*/
    private static final String apiLang = "es";

    /*Parametros de ubicacion necesitados por la API*/
    final static String QUERY_PARAM = "city"; //q
    final static String LAT_PARAM = "lat";
    final static String LON_PARAM = "lon";
    final static String FORMAT_PARAM = "mode"; //mode
    final static String UNITS_PARAM = "units";
    final static String DAYS_PARAM = "days"; //cnt
    final static String API_KEY = "key"; //appid
    final static String API_LANG = "lang";


    /**
     * Construye la URL para comunicarse con el Servidor Weatherbit API usando Ubicacion.
     *
     * @param locationQuery La ubicacion que sera buscada.
     * @return Regresa la URL que usara para obtener respuesta de Servidor de Datos.
     */
    public static URL buildUrl(String locationQuery) {
        Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationQuery)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(API_LANG,apiLang)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

     /**
      * Una vez generada la URL este metodo la usa para regresar la respuesta del HTTP
     *
     * @param url URL para obtener la respuesta de HTTP.
     * @return Regresa el contenido de la respuesta de HTTP.
     * @throws IOException Si se detecta error en conexion o lectura dispara esxcepcion
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
