package projeto.emp.dcasa.views;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import projeto.emp.dcasa.R;
import projeto.emp.dcasa.models.PROFESSIONAL_TYPE;
import projeto.emp.dcasa.models.Professional;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private List<Professional> professionals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        if  ( mGoogleApiClient ==  null )  {
            mGoogleApiClient =  new  GoogleApiClient. Builder ( this )
                    . addConnectionCallbacks ( this )
                    . addOnConnectionFailedListener ( this )
                    . addApi ( LocationServices. API )
                    . build ();
        }

        professionals = criaProfissionais();
    }

    private List<Professional> criaProfissionais() {
        List<Professional> professionals = new ArrayList<Professional>();
        Professional prof = new Professional();
        prof.setNome("José Luiz");
        prof.setType(PROFESSIONAL_TYPE.ELECTRICIAN);
        prof.setLocation(new Location("Rua Rodrigues Alves Campina Grande"));
        professionals.add(prof);

        return professionals;
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
//        MarkerOptions options = new MarkerOptions()
//                .position(latLng)
//                .title("I am here!");
//        mMap.addMarker(options);
        mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude))
                .title("Minha localização")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.user_location_icon)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
    }

    private void handleLocationsProfessionals(Professional professinal) {
        Log.d(TAG, professinal.getLocation().toString());

        double latitude = professinal.getLocation().getLatitude();
        double longitude = professinal.getLocation().getLongitude();

        Log.i("LOCATION", getLatLng(professinal.getLocation().getProvider()).longitude + "");
        Log.i("LOCATIONSS", getLatLng(professinal.getLocation().getProvider()).latitude+"");

        if (professinal.getType().equals(PROFESSIONAL_TYPE.ELECTRICIAN)) {
            mMap.addMarker(new MarkerOptions().position(getLatLng(professinal.getLocation().getProvider()))
                    .title(professinal.getNome())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.electrician_location_icon)));

        } else if (professinal.getType().equals(PROFESSIONAL_TYPE.PLUMBER)) {
            mMap.addMarker(new MarkerOptions().position(getLatLng(professinal.getLocation().getProvider()))
                    .title(professinal.getNome())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.plumber_location_icon)));

        } else if (professinal.getType().equals(PROFESSIONAL_TYPE.FITTER)) {
            mMap.addMarker(new MarkerOptions().position(getLatLng(professinal.getLocation().getProvider()))
                    .title(professinal.getNome())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.fitter_location_icon)));
        }
    }

    public LatLng getLatLng(String location){

        List<Address> addressList = null;
        if(location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());

            return latLng;

        }
        return new LatLng(0, 0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
//      mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(false); //se colocar false, sai o ponto azul e o botão para dar zoom
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation =  LocationServices.FusedLocationApi.getLastLocation (
                mGoogleApiClient );
        if  ( mLastLocation !=  null )  {
            handleNewLocation(mLastLocation);
            for (Professional p : professionals) {
                handleLocationsProfessionals(p);
            }
        } else {
            Log.i("MY LOCATION", "NULL");
        }
    }

    @Override
    public void onConnectionSuspended(int i) { }

    protected  void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected  void onStop ()  {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }
}
