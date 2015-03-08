package com.example.basicweatherapplication;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.karthik.basicweatherapplication.R;


public class MainActivity extends ActionBarActivity {
	public static Location userLocation;
	public static boolean userLocationReceived=false;
	
	Button getLocationByGPS;
	Button getLocationManually;
	EditText editTextCity;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getLocationByGPS=(Button) findViewById(R.id.get_From_GPS_Button);
		getLocationManually=(Button) findViewById(R.id.enter_Manually_Button);
		editTextCity=(EditText)findViewById(R.id.editText1);
		
		
		final LocationManager mLocationManager;
		mLocationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);

		
		
		
		getLocationByGPS.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {


					if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
						final AlertDialog.Builder builder = new Builder(MainActivity.this);
					    builder.setMessage("Your GPS seems to be disabled, please enable it.")
					           .setCancelable(false)
					           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
					                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					               }
					           })
					           .setNegativeButton("No", new DialogInterface.OnClickListener() {
					               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
					                    dialog.cancel();
					               }
					           });
					    final AlertDialog alert = builder.create();
					    alert.show();
					}	

					 class FetchCity extends AsyncTask<String, Void, Location>{
						 public Location userlocation=null;
					     public Geocoder geocoder;
					     @Override
						protected void onPreExecute() {
						// TODO Auto-generated method stub
						super.onPreExecute();
						geocoder=new Geocoder(getApplicationContext(),Locale.US);
			            mLocationManager.requestLocationUpdates(
			                    LocationManager.NETWORK_PROVIDER, 0, 0,
			                    new LocationListener() {
									
									@Override
									public void onStatusChanged(String provider, int status, Bundle extras) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void onProviderEnabled(String provider) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void onProviderDisabled(String provider) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void onLocationChanged(Location location) {
										userlocation=location;
										
									}
								});
						 
						 }
						 
						@Override
						protected Location doInBackground(String... params) {
							// TODO Auto-generated method stub
							int i=0;
				            while (userlocation==null) {
				            }
				            return userlocation;				            
							}
						
						@Override
						protected void onPostExecute(Location userLocation) {
							// TODO Auto-generated method stub
							super.onPostExecute(userLocation);
							
							try {
								List<Address> addresses=	geocoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 10);
								String city = addresses.get(0).getLocality().toString();
								Intent i=new Intent(getApplicationContext(), DisplayWeather.class);
								i.putExtra("city", city);
								startActivity(i);
							

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();

							}
					}
					 }
				
					FetchCity fetchCity=new FetchCity();
					fetchCity.execute();
					}
				});
		
		getLocationManually.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String city=editTextCity.getText().toString();
				Intent i=new Intent(getApplicationContext(), DisplayWeather.class);
				i.putExtra("city", city);
				startActivity(i);
			
			
					
			}
		});
	

	}


	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	
	public static class AdFragment extends Fragment {
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                             Bundle savedInstanceState) {
	        return inflater.inflate(R.layout.fragment_ad, container, false);
	    }

	    @Override
	    public void onActivityCreated(Bundle bundle) {
	        super.onActivityCreated(bundle);
	        AdView mAdView = (AdView) getView().findViewById(R.id.adView);
	        AdRequest adRequest = new AdRequest.Builder().build();
	        mAdView.loadAd(adRequest);
	    }
	}

}
