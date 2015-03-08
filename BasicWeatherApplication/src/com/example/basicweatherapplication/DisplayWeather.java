package com.example.basicweatherapplication;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.karthik.basicweatherapplication.R;

public class DisplayWeather extends Activity {

	
	Typeface weatherFont;
    
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    Handler	handler;    
    Button forecast;
    Intent i1;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_display_weather);
	
	handler = new Handler();
	i1 = getIntent();
	String city=i1.getStringExtra("city");
	
	
	forecast=(Button) findViewById(R.id.forecastButton);
	cityField = (TextView) findViewById(R.id.city_field);
    updatedField = (TextView) findViewById(R.id.updated_field);
    detailsField = (TextView) findViewById(R.id.details_field);
    currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
    weatherFont = Typeface.createFromAsset(getApplication().getAssets(), "weather.ttf");     
	
    forecast.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			
Intent i=new Intent(getApplicationContext(),DisplayForecast.class);
i.putExtra("city", i1.getStringExtra("city"));
	startActivity(i);
		}
	});
    
    weatherIcon = (TextView) findViewById(R.id.weather_icon);
    weatherIcon.setTypeface(weatherFont);
	updateWeatherData(city);
}
	
	
	private void updateWeatherData(final String city){
	    new Thread(){
	        public void run(){
	            final JSONObject json = RemoteFetch.getJSON(getApplicationContext(), city);
	            if(json == null){
	                handler.post(new Runnable(){
	                    public void run(){
	                        Toast.makeText(getApplicationContext(), R.string.place_not_found,Toast.LENGTH_LONG).show(); 
	                    }
	                });
	            } else {
	                handler.post(new Runnable(){
	                    public void run(){
	                        renderWeather(json);
	                    }
	                });
	            }               
	        }
	    }.start();
	}
	
	private void renderWeather(JSONObject json){
	    try {
	        cityField.setText(json.getString("name").toUpperCase(Locale.US) + 
	                ", " + 
	                json.getJSONObject("sys").getString("country"));
	         
	        JSONObject details = json.getJSONArray("weather").getJSONObject(0);
	        JSONObject main = json.getJSONObject("main");
	        detailsField.setText(
	                details.getString("description").toUpperCase(Locale.US) +
	                "\n" + "Humidity: " + main.getString("humidity") + "%" +
	                "\n" + "Pressure: " + main.getString("pressure") + " hPa");
	         
	        currentTemperatureField.setText(
	                    String.format("%.2f", main.getDouble("temp"))+ " ℃");
	 
	        DateFormat df = DateFormat.getDateTimeInstance();
	        String updatedOn = df.format(new Date(json.getLong("dt")*1000));
	        updatedField.setText("Last update: " + updatedOn);
	 
	        setWeatherIcon(details.getInt("id"),
	                json.getJSONObject("sys").getLong("sunrise") * 1000,
	                json.getJSONObject("sys").getLong("sunset") * 1000);
	         
	    }catch(Exception e){
	        Log.e("Basic weather","One or more fields not found in the JSON data");
	    }
	}
	
	private void setWeatherIcon(int actualId, long sunrise, long sunset){
	    int id = actualId / 100;
	    String icon = "";
	    if(actualId == 800){
	        long currentTime = new Date().getTime();
	        if(currentTime>=sunrise && currentTime<sunset) {
	            icon = getApplicationContext().getString(R.string.weather_sunny);
	        } else {
	            icon = getApplicationContext().getString(R.string.weather_clear_night);
	        }
	    } else {
	        switch(id) {
	        case 2 : icon = getApplicationContext().getString(R.string.weather_thunder);
	                 break;         
	        case 3 : icon = getApplicationContext().getString(R.string.weather_drizzle);
	                 break;     
	        case 7 : icon = getApplicationContext().getString(R.string.weather_foggy);
	                 break;
	        case 8 : icon = getApplicationContext().getString(R.string.weather_cloudy);
	                 break;
	        case 6 : icon = getApplicationContext().getString(R.string.weather_snowy);
	                 break;
	        case 5 : icon = getApplicationContext().getString(R.string.weather_rainy);
	                 break;
	        }
	    }
	    
	    weatherIcon.setText(icon);
	}

	
}
