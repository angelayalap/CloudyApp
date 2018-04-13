package com.example.angel.cloudy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private String[] mWeatherData;
    private final ForecastAdapterOnClickHandler mClickHandler;

    //Interfaz que recibe mensajes onClick
    public interface ForecastAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    public ForecastAdapter(ForecastAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View view) {
            super(view);
            mWeatherTextView = (TextView) view.findViewById(R.id.weather_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String weatherForDay = mWeatherData[adapterPosition];
            mClickHandler.onClick(weatherForDay);
        }
    }

    /**
     * El siguiente Override es llamado cada que un ViewHolder es creado.
     *
     * @param viewGroup Recibe el ViewGroup donde esta contenido el ViewHolders.
     * @param viewType  Recibe el tipo de elemento.
     * @return Regresa un nuevo ForecastAdapterViewHolder que contendra la vista de cada elemento.
     */
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new ForecastAdapterViewHolder(view);
    }


    /**
     * OnBindViewHolder  es llamado para mostrar la info en determinada posicion
     * En este metodo se actualiza el contenido del ViewHolder para mostrar los detalles del clima
     *
     * @param forecastAdapterViewHolder Recibe el ViewHolder  que sera actualizado para representar el contenido
     *                                  el elemento dada una posicion.
     * @param position                  Recibe la posicion del elemento.
     */
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        String weatherForThisDay = mWeatherData[position];
        forecastAdapterViewHolder.mWeatherTextView.setText(weatherForThisDay);
    }


    /** Este metodo regresa el numero de elementos a mostrar*/
    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.length;
    }

    /** Este método es usada para establecerel pronostico de clima en el ForecastAdapter*/
    public void setWeatherData(String[] weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }
}