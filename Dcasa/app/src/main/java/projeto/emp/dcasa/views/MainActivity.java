package projeto.emp.dcasa.views;

import projeto.emp.dcasa.models.PROFESSIONAL_TYPE;
import projeto.emp.dcasa.models.Professional;
import projeto.emp.dcasa.models.User;
import projeto.emp.dcasa.utils.MainMapFragment;
import projeto.emp.dcasa.utils.MapWrapperLayout;
import projeto.emp.dcasa.utils.OnInfoWindowElemTouchListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import projeto.emp.dcasa.R;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private List<Professional> professionals;
    private List<Professional> professionalsSelected;
    private List<PROFESSIONAL_TYPE> typesList;
    private ImageButton ib_electrician;
    private ImageButton ib_fitter;
    private ImageButton ib_plumber;
    private Boolean electrician_pressed;
    private Boolean plumber_pressed;
    private Boolean fitter_pressed;
    private Button btn_call;
    private TextView tv_profession;
    private TextView tv_name_professional;
    private TextView tv_cpf;
    private TextView tv_phone_number;
    private HashMap<Marker, Professional> professionalMarkerMap;

    private View infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;
    private MapFragment mapFragment;
    private MapWrapperLayout mapWrapperLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
//        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
//        final GoogleMap map = mapFragment.getMap();

        mapFragment = new MainMapFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.map, mapFragment);
        ft.commit();

        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);

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
        User user = new User(new Location("Rua das Uburanas, Campina Grande"),"Maria");
        Professional elec = new Professional();
        elec.setName("José Luiz");
        elec.setType(PROFESSIONAL_TYPE.ELECTRICIAN);
        elec.setLocation(new Location("Rua Rodrigues Alves Campina Grande"));
        elec.getLocation().setLatitude(-7.21966);
        elec.getLocation().setLongitude(-35.89938);
        elec.setCpf("486.136.632-14");
        elec.setPhone_number("(83)98645-4545");
        elec.addEvaluation(user, 3);
        professionals.add(elec);

        Professional plum = new Professional();
        plum.setName("João Melo");
        plum.setType(PROFESSIONAL_TYPE.PLUMBER);
        plum.setLocation(new Location("Avenida Juvênio Arruda Campina Grande"));
        plum.getLocation().setLatitude(-7.21078);
        plum.getLocation().setLongitude(-35.91883);
        plum.setCpf("576.373.113-17");
        plum.setPhone_number("(83)98645-8888");
        plum.addEvaluation(user, 4);
        professionals.add(plum);

        Professional fitter = new Professional();
        fitter.setName("Severino Miguel");
        fitter.setType(PROFESSIONAL_TYPE.FITTER);
        fitter.setLocation(new Location("Avenida Dr. Francisco Pinto Campina Grande"));
        fitter.getLocation().setLatitude(-7.20681);
        fitter.getLocation().setLongitude(-35.90913);
        fitter.setCpf("051.276.452-20");
        fitter.setPhone_number("(83)98645-8080");
        fitter.addEvaluation(user, 5);
        professionals.add(fitter);

        return professionals;
    }

    private void handleNewLocation(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        ((MainMapFragment) mapFragment).placeMarker(latLng);
        //mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        mapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnected(Bundle bundle) {
        loadProfessionalsSelected();
    }

    private void loadLocationsOnMap() {
        mapFragment.getMap().clear();
        if (mLastLocation == null) {
            mLastLocation =  LocationServices.FusedLocationApi.getLastLocation (
                    mGoogleApiClient );
        }

        if  ( mLastLocation !=  null )  {
//            for (Professional p : professionalsSelected) {
//                handleLocationsProfessionals(p);
//            }
            setUpProfessionalsMarker();
            handleNewLocation(mLastLocation);

        } else {
            Log.i("MY LOCATION", "NULL");
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    protected  void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected  void onStop ()  {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

    }


    private void setUpProfessionalsMarker() {

        professionalMarkerMap = new HashMap<Marker, Professional>();
        Marker marker;
        LatLng latLng;
        for (Professional prof: professionalsSelected) {
            latLng = new LatLng(prof.getLocation().getLatitude(),prof.getLocation().getLongitude());
            marker = ((MainMapFragment) mapFragment).placeMarker(prof, latLng);
            professionalMarkerMap.put(marker, prof);
        }

        mapFragment.getMap().setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                final Professional professionalInfo = professionalMarkerMap.get(marker);
                infoWindow = null;

                if (!marker.getTitle().equals("Minha localização")) {
                    infoWindow = getLayoutInflater().inflate(R.layout.infowindow_professional, null);

                    tv_profession = (TextView) infoWindow.findViewById(R.id.tv_profession);
                    tv_profession.setText(professionalInfo.getType().getType());

                    tv_name_professional = (TextView) infoWindow.findViewById(R.id.tv_name_professional);
                    tv_name_professional.setText(professionalInfo.getName());

                    tv_cpf = (TextView) infoWindow.findViewById(R.id.tv_cpf);
                    tv_cpf.setText(professionalInfo.getCpf());

                    tv_phone_number = (TextView) infoWindow.findViewById(R.id.tv_phone_number);
                    tv_phone_number.setText(professionalInfo.getPhone_number());


                    RatingBar rate_bar = (RatingBar) infoWindow.findViewById(R.id.evaluataion_bar);

                    rate_bar.setRating(professionalInfo.getAverageEvaluations());
                    Log.d("Rating", professionalInfo.getAverageEvaluations()+"");

                    infoButton = (Button)infoWindow.findViewById(R.id.btn_call);

                    // Setting custom OnTouchListener which deals with the pressed state
                    // so it shows up
                    infoButtonListener = new OnInfoWindowElemTouchListener(infoButton)
                    {
                        @Override
                        protected void onClickConfirmed(View v, Marker marker) {
                            // Here we can perform some action triggered after clicking the button
                            Toast.makeText(MainActivity.this, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
                        }
                    };
                    infoButton.setOnTouchListener(infoButtonListener);
                }
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

    }

}