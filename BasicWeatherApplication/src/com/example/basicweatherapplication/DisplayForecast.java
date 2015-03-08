package com.example.basicweatherapplication;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.karthik.basicweatherapplication.R;

public class DisplayForecast extends Activity{

	TextView tempValue1;
	TextView tempValue2;
	TextView tempValue3;
	TextView humValue1;
	TextView humValue2;
	TextView humValue3;
	Intent i1;
	String city;
	Handler handler;
	
	@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	

	handler = new Handler();
	i1 = getIntent();
	city=i1.getStringExtra("city");
	
	setContentView(R.layout.activity_display_forecast);
	tempValue1=(TextView)findViewById(R.id.temp1);
	tempValue2=(TextView)findViewById(R.id.temp2);
	tempValue3=(TextView)findViewById(R.id.temp3);
	humValue1=(TextView)findViewById(R.id.hum1);
	humValue2=(TextView)findViewById(R.id.hum2);
	humValue3=(TextView)findViewById(R.id.hum3);
	
	updateForecastData(city);
}

	private void updateForecastData(String city2) {
	    new Thread(){
	        public void run(){
	            final JSONObject json = RemoteFetch.getForecastJSON(getApplicationContext(), city);
	            if(json == null){
	                handler.post(new Runnable(){
	                    public void run(){
	                        Toast.makeText(getApplicationContext(), R.string.place_not_found,Toast.LENGTH_LONG).show(); 
	                    }
	                });
	            } else {
	                handler.post(new Runnable(){
	                    public void run(){
	                        renderForecast(json);
	                    }
	                });
	            }               
	        }
	    }.start();
	}
	
	private void renderForecast(JSONObject json){
	    try {
	    	Forecast[] fc=new Forecast[3];
	    	JSONArray jArr = json.getJSONArray("list");
	    	
	    	 for (int i=0; i < jArr.length(); i++) {
	    	        JSONObject jDayForecast = jArr.getJSONObject(i);
	    	        JSONObject jTempObj = jDayForecast.getJSONObject("temp");
	    	        double temp=jTempObj.getDouble("day");
	    	        double hum=jDayForecast.getDouble("pressure");
	    	        fc[i]=new Forecast(temp, hum);
	    	 }
	    	

	        humValue1.setText("Humidity: " + fc[0].getHum()+"hPa");

	        humValue2.setText("Humidity: " + fc[1].getHum()+"hPa");

	        humValue3.setText("Humidity: " + fc[2].getHum()+"hPa");

	        
	        tempValue1.setText(
	                    String.format("%.2f", fc[0].getTemp())+ " ℃");
	        tempValue2.setText(
                    String.format("%.2f", fc[1].getTemp())+ " ℃");
	        tempValue3.setText(
                    String.format("%.2f", fc[2].getTemp())+ " ℃");

	        
	    }catch(Exception e){
	        Log.e("Basic weather","One or more fields not found in the JSON data");
	    }
	}
	
	private class Forecast{
		Double temp;
		Double hum;
		public Forecast(Double t, Double h) {
			temp=t;
			hum=h;
		}
		public Double getTemp() {
			return temp;
		}
		public Double getHum() {
			return hum;
		}
	}

}
