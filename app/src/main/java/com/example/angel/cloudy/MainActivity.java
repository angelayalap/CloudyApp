package com.example.angel.cloudy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angel.cloudy.data.CloudyPreferences;
import com.example.angel.cloudy.utilities.NetworkUtils;
import com.example.angel.cloudy.utilities.OpenWeatherJsonUtils;
import com.example.angel.cloudy.ForecastAdapter.ForecastAdapterOnClickHandler;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements ForecastAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Referenciamos el ReclyclerView al xml*/
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);

        /*Creamos un LinearLauoyt (Vertical) para manejar el RecyclerView*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        /*ForecastAdapter es responsable de relacionar los datos con las vistas.*/
        mForecastAdapter = new ForecastAdapter(this);
        mRecyclerView.setAdapter(mForecastAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        /* Cargamos la informacion de clima cuando nuestras vistas esta configuradas*/
        loadWeatherData();
    }
    @Override // Pasamos la informacion de clima a la DetailActivity
    public void onClick(String weatherForDay) {

        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, weatherForDay);

        startActivity(intentToStartDetailActivity);
    }

    /**
     * El método loadWeatherData obtiene las preferencias de localizacion del usuario, y
     * un método secundario obtiene los datos del clima para la ubicacion.
     */
    private void loadWeatherData() {
        showWeatherDataView();

        String location = CloudyPreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    /**
     * El método showWeatherDataView oculta el mensaje de error y
     * se asegura de mostrar los datos del clima
     */
    private void showWeatherDataView() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* Oculta los Datos visible, para despues mostrar mensaje de error */
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**Metodo que usa esquema URI para mostrar ubicacion en mapa
     * @referencia <a"https://developer.android.com/guide/components/intents-common.html#Maps">*/
    private void openLocationInMap() {
        String addressString = "Monterrey, MX";
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "No se pudo llamar " + geoLocation.toString()
                    + ", No hay aplicaccion receptora instalada");
        }
    }

    /** La clase FecthWeatherTask se encargara de manejar los requests para conectividad ASYNC
        AsyncTask requiere los metodos onPreExecute, doInBackground y onPostExecute para realizar sincronizacion
     */
    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override //El método doInBackground realiza las peticiones
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {   // Si no tiene parametro regresa null
                return null;
            }
            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location); // construye la URL para pedir los datos
            try {
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                String[] simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
                return simpleJsonWeatherData; // regresa la peticion realizada en JSON
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override // El método onPostExecute muestra los resultados de las peticiones
        protected void onPostExecute(String[] weatherData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showWeatherDataView();
                mForecastAdapter.setWeatherData(weatherData);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Se usa getMenuInflater para manejar el menu inflater*/
        MenuInflater inflater = getMenuInflater();
        /* Se usa el metodo inflate del infflater para el layout del menu*/
        inflater.inflate(R.menu.forecast, menu);
        /* Regresa True para que el menu sea mostrado en Toolbar */
        return true;
    }

    // Override onOptionsItemSelected para manejar los Menus
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) { // Menu actualiza info de Clima
            mForecastAdapter.setWeatherData(null);
            loadWeatherData();
            return true;
        }

        if (id == R.id.action_map) { // Menu muestra mapa
            openLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
