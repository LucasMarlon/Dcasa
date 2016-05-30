package projeto.emp.dcasa.views;

import projeto.emp.dcasa.adapters.DrawerListAdapter;
import projeto.emp.dcasa.models.NavItem;
import projeto.emp.dcasa.models.PROFESSIONAL_TYPE;
import projeto.emp.dcasa.models.Professional;
import projeto.emp.dcasa.models.User;
import projeto.emp.dcasa.utils.MainMapFragment;
import projeto.emp.dcasa.utils.MapWrapperLayout;
import projeto.emp.dcasa.utils.MySharedPreferences;
import projeto.emp.dcasa.utils.OnInfoWindowElemTouchListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import projeto.emp.dcasa.R;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public static final String TAG = MainActivity.class.getSimpleName();
    private List<Professional> professionals;
    private List<Professional> professionalsSelected;
    private List<PROFESSIONAL_TYPE> typesList;
    private ImageButton ib_electrician;
    private ImageButton ib_fitter;
    private ImageButton ib_plumber;
    private Boolean electrician_pressed;
    private Boolean plumber_pressed;
    private Boolean fitter_pressed;
    private TextView tv_profession;
    private TextView tv_name_professional;
    private TextView tv_cpf;
    private HashMap<Marker, Professional> professionalMarkerMap;

    private ViewGroup infoWindow;
    private Button btn_call;
    private OnInfoWindowElemTouchListener btn_call_listener;
    private MainMapFragment mapFragment;
    private MapWrapperLayout mapWrapperLayout;
    private MySharedPreferences preferences;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<NavItem> mNavItems;
    private Context context;
    private android.support.v7.app.ActionBar actionBar;
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        preferences = new MySharedPreferences(getApplicationContext());

        mapFragment = new MainMapFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.map, mapFragment);
        ft.commit();

        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);

        electrician_pressed = true;
        plumber_pressed = true;
        fitter_pressed = true;
        typesList = new ArrayList<PROFESSIONAL_TYPE>();
        typesList.add(PROFESSIONAL_TYPE.ELECTRICIAN);
        typesList.add(PROFESSIONAL_TYPE.FITTER);
        typesList.add(PROFESSIONAL_TYPE.PLUMBER);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        professionals = createProfessionals();

        selectProfessionals();

        mNavItems = new ArrayList<>();
        if (preferences.isUserLoggedIn()) {
            setmDrawer(mNavItems);
        }
        context = this;
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void loadProfessionalsSelected() {
        professionalsSelected = new ArrayList<Professional>();
        for (PROFESSIONAL_TYPE type : typesList) {
            for (Professional professional : professionals) {
                if (professional.getType().equals(type)) {
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

    private void handleNewLocation(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        mapFragment.placeMarker(latLng);
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
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }

        if  ( mLastLocation !=  null )  {
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
            marker = mapFragment.placeMarker(prof, latLng);
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

                mapWrapperLayout.init(mapFragment.getMap(), getPixelsFromDp(MainActivity.this, 39 + 20));

                infoWindow = null;

                if (!marker.getTitle().equals("Minha localização")) {
                    infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.infowindow_professional, null);

                    tv_profession = (TextView) infoWindow.findViewById(R.id.tv_profession);
                    tv_profession.setText(professionalInfo.getType().getType());

                    tv_name_professional = (TextView) infoWindow.findViewById(R.id.tv_name_professional);
                    tv_name_professional.setText(professionalInfo.getName());

                    tv_cpf = (TextView) infoWindow.findViewById(R.id.tv_cpf);
                    tv_cpf.setText(professionalInfo.getCpf());

                    RatingBar rate_bar = (RatingBar) infoWindow.findViewById(R.id.evaluataion_bar);

                    rate_bar.setRating(professionalInfo.getAverageEvaluations());

                    btn_call = (Button) infoWindow.findViewById(R.id.btn_call);

                    // Setting custom OnTouchListener which deals with the pressed state
                    // so it shows up
                    btn_call_listener = new OnInfoWindowElemTouchListener(btn_call)
                    {
                        @Override
                        protected void onClickConfirmed(View v, Marker marker) {

                            if (preferences.isUserLoggedIn()) {
                                preferences.saveProfessionalSelected(professionalInfo);
                                Uri uri = Uri.parse("tel:" + professionalInfo.getPhone_number());
                                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                                startActivity(intent);
                            } else {
                                setView(MainActivity.this, CadastreOrLoginActivity.class);
                            }
                        }
                    };
                    btn_call.setOnTouchListener(btn_call_listener);
                }
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

    }
    public void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }

    private void selectProfessionals() {
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

    private List<Professional> createProfessionals() {
        List<Professional> professionals = new ArrayList<Professional>();
        User user = new User("Maria");

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

        elec = new Professional();
        elec.setName("Francisco Couto");
        elec.setType(PROFESSIONAL_TYPE.ELECTRICIAN);
        elec.setLocation(new Location("Rua Florípes Coutinho Campina Grande"));
        elec.getLocation().setLatitude(-7.22192);
        elec.getLocation().setLongitude(-35.92162);
        elec.setCpf("853.218.642-41");
        elec.setPhone_number("(83)9989-6459");
        elec.addEvaluation(user, 4);
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

        plum = new Professional();
        plum.setName("Mário Veloso Santos");
        plum.setType(PROFESSIONAL_TYPE.PLUMBER);
        plum.setLocation(new Location("Rua Damasco Campina Grande"));
        plum.getLocation().setLatitude(-7.23531);
        plum.getLocation().setLongitude(-35.90284);
        plum.setCpf("656.124.141-02");
        plum.setPhone_number("(83)9985-8568");
        plum.addEvaluation(user, 3);
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

        fitter = new Professional();
        fitter.setName("Carlos Henrique");
        fitter.setType(PROFESSIONAL_TYPE.FITTER);
        fitter.setLocation(new Location("Rua José Lins do Rego Campina Grande"));
        fitter.getLocation().setLatitude(-7.21170);
        fitter.getLocation().setLongitude(-35.89309);
        fitter.setCpf("173.620.525-02");
        fitter.setPhone_number("(83)98888-4310");
        fitter.addEvaluation(user, 2);
        professionals.add(fitter);

        return professionals;
    }

    public void setmDrawer(ArrayList<NavItem> mNavItems) {
        mNavItems.add(new NavItem("Avaliar Serviço", R.mipmap.light_blue_electrician_button));
        mNavItems.add(new NavItem("Sair", R.mipmap.light_blue_electrician_button));


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter2 = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter2);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    if (preferences.isProfessionalSelected()) {
                       setView(MainActivity.this, EvaluationActivity.class);
                    } else {
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Você precisa utilizar um serviço antes!")
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setView(MainActivity.this, MainActivity.class);
                                    }
                                })
                                .create()
                                .show();
                    }
                }
                if (position == 1) {
                   preferences.logoutUser();
                    setView(MainActivity.this, LoginActivity.class);
                }
            }
        });


//    	actionBar =  getSupportActionBar();
//    	actionBar.setDisplayHomeAsUpEnabled(false);
//    	actionBar.setHomeAsUpIndicator(R.mipmap.fitter_location_icon);
//    	actionBar.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mTitle = getTitle().toString();
        mDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                mDrawerLayout,
                R.mipmap.fitter_location_icon,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

}