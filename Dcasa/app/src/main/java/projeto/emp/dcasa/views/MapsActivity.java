package projeto.emp.dcasa.views;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import projeto.emp.dcasa.R;
import projeto.emp.dcasa.models.PROFESSIONAL_TYPE;
import projeto.emp.dcasa.models.Professional;

public class MapsActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private List<Professional> professionals;
    private List<Professional> professionalsSelected;
    List<PROFESSIONAL_TYPE> typesList;
    private ImageButton ib_electrician;
    private ImageButton ib_fitter;
    private ImageButton ib_plumber;
    private Boolean electrician_pressed;
    private Boolean plumber_pressed;
    private Boolean fitter_pressed;
    private Button btn_call;
    private View infoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        electrician_pressed = true;
        plumber_pressed = true;
        fitter_pressed = true;
        typesList = new ArrayList<PROFESSIONAL_TYPE>();
        typesList.add(PROFESSIONAL_TYPE.ELECTRICIAN);
        typesList.add(PROFESSIONAL_TYPE.FITTER);
        typesList.add(PROFESSIONAL_TYPE.PLUMBER);

        if  ( mGoogleApiClient ==  null )  {
            mGoogleApiClient =  new  GoogleApiClient. Builder ( this )
                    . addConnectionCallbacks ( this )
                    . addOnConnectionFailedListener ( this )
                    . addApi ( LocationServices. API )
                    . build ();
        }

        professionals = criaProfissionais();

//        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setCustomView(R.layout.actionbar_custom_view);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);

        ib_electrician = (ImageButton) findViewById(R.id.ib_electrician);
        ib_electrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (electrician_pressed) {
                    electrician_pressed = false;
                    ib_electrician.setImageResource(R.mipmap.light_blue_electrician_button);
                    deleteTypeFromSelecteds(PROFESSIONAL_TYPE.ELECTRICIAN);
                } else if (!electrician_pressed) {
                    electrician_pressed = true;
                    ib_electrician.setImageResource(R.mipmap.dark_blue_electrician_button);
                    addTypeToSelecteds(PROFESSIONAL_TYPE.ELECTRICIAN);
                }
            }
        });

        ib_fitter = (ImageButton) findViewById(R.id.ib_fitter);
        ib_fitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fitter_pressed) {
                    fitter_pressed = false;
                    ib_fitter.setImageResource(R.mipmap.light_blue_fitter_button);
                    deleteTypeFromSelecteds(PROFESSIONAL_TYPE.FITTER);
                } else if (!fitter_pressed) {
                    fitter_pressed = true;
                    ib_fitter.setImageResource(R.mipmap.dark_blue_fitter_button);
                    addTypeToSelecteds(PROFESSIONAL_TYPE.FITTER);
                }
            }
        });

        ib_plumber = (ImageButton) findViewById(R.id.ib_plumber);
        ib_plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (plumber_pressed) {
                    plumber_pressed = false;
                    ib_plumber.setImageResource(R.mipmap.light_blue_plumber_button);
                    deleteTypeFromSelecteds(PROFESSIONAL_TYPE.PLUMBER);
                } else if (!plumber_pressed) {
                    plumber_pressed = true;
                    ib_plumber.setImageResource(R.mipmap.dark_blue_plumber_button);
                    addTypeToSelecteds(PROFESSIONAL_TYPE.PLUMBER);
                }
            }
        });

    }

    private void loadProfessionalsSelected() {
        professionalsSelected = new ArrayList<Professional>();
        for (PROFESSIONAL_TYPE type : typesList) {
            for (Professional professional: professionals) {
                if(professional.getType().equals(type)) {
                    professionalsSelected.add(professional);
                }
            }
        }
        loadLocationsOnMap();
    }


    private void addTypeToSelecteds(PROFESSIONAL_TYPE type) {
        typesList.add(type);
        loadProfessionalsSelected();
    }

    private void deleteTypeFromSelecteds(PROFESSIONAL_TYPE type) {
        typesList.remove(type);
        loadProfessionalsSelected();
    }


    private List<Professional> criaProfissionais() {
        List<Professional> professionals = new ArrayList<Professional>();
        Professional elec = new Professional();
        elec.setNome("José Luiz");
        elec.setType(PROFESSIONAL_TYPE.ELECTRICIAN);
        elec.setLocation(new Location("Rua Rodrigues Alves Campina Grande"));
        professionals.add(elec);

        Professional plum = new Professional();
        plum.setNome("João Melo");
        plum.setType(PROFESSIONAL_TYPE.PLUMBER);
        plum.setLocation(new Location("Avenida Juvênio Arruda Campina Grande"));
        professionals.add(plum);

        Professional fitter = new Professional();
        fitter.setNome("Severino Miguel");
        fitter.setType(PROFESSIONAL_TYPE.FITTER);
        fitter.setLocation(new Location("Avenida Dr. Francisco Pinto Campina Grande"));
        professionals.add(fitter);

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

    private void handleLocationsProfessionals(Professional professional) {
        Log.d(TAG, professional.getLocation().toString());
        LatLng latLng;
        if (professional.getLocation().getLongitude() == 0d || professional.getLocation().getLatitude() == 0d) {
            latLng = getLatLng(professional.getLocation().getProvider(), professional);
        } else {
           latLng = new LatLng(professional.getLocation().getLatitude(),professional.getLocation().getLongitude());
        }

        if (professional.getType().equals(PROFESSIONAL_TYPE.ELECTRICIAN)) {
            mMap.addMarker(new MarkerOptions().position(latLng)
                    .title(professional.getNome())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.electrician_location_icon)));

        } else if (professional.getType().equals(PROFESSIONAL_TYPE.PLUMBER)) {
            mMap.addMarker(new MarkerOptions().position(latLng)
                    .title(professional.getNome())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.plumber_location_icon)));

        } else if (professional.getType().equals(PROFESSIONAL_TYPE.FITTER)) {
            mMap.addMarker(new MarkerOptions().position(latLng)
                    .title(professional.getNome())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.fitter_location_icon)));
        }
    }

    public LatLng getLatLng(String location, Professional professional){

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
            professional.getLocation().setLatitude(address.getLatitude());
            professional.getLocation().setLatitude(address.getLongitude());
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
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if (!marker.getTitle().equals("Minha localização")) {
                    infoWindow = getLayoutInflater().inflate(R.layout.infowindow_professional, null);
                    btn_call = (Button) infoWindow.findViewById(R.id.btn_call);
                    btn_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView tv = (TextView)infoWindow.findViewById(R.id.tv_phone);
                            Log.d("CLICK", "Clicou!");

                            String number = tv.getText().toString();


                            Uri uri = Uri.parse("tel:"+number);
                            Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                            startActivity(intent);
                        }
                    });
                }
                return infoWindow;
            }
        });
        mMap.setMyLocationEnabled(false); //se colocar false, sai o ponto azul e o botão para dar zoom
    }


    @Override
    public void onConnected(Bundle bundle) {
        loadProfessionalsSelected();
    }

    private void loadLocationsOnMap() {
        mMap.clear();
        if (mLastLocation == null) {
            mLastLocation =  LocationServices.FusedLocationApi.getLastLocation (
                    mGoogleApiClient );
        }

        if  ( mLastLocation !=  null )  {
            for (Professional p : professionalsSelected) {
                handleLocationsProfessionals(p);
            }
            handleNewLocation(mLastLocation);
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
