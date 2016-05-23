package projeto.emp.dcasa.utils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import projeto.emp.dcasa.R;
import projeto.emp.dcasa.models.PROFESSIONAL_TYPE;
import projeto.emp.dcasa.models.Professional;

public class MainMapFragment extends MapFragment {

    public Marker placeMarker(Professional profInfo, LatLng latLng) {
        Marker m = null;
        if (profInfo.getType().equals(PROFESSIONAL_TYPE.ELECTRICIAN)) {
            m = getMap().addMarker(new MarkerOptions()
                    .title(profInfo.getNome())
                    .snippet(profInfo.getPhone_number())
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.electrician_location_icon)));

        } else if (profInfo.getType().equals(PROFESSIONAL_TYPE.PLUMBER)) {
            m = getMap().addMarker(new MarkerOptions()
                    .title(profInfo.getNome())
                    .snippet(profInfo.getPhone_number())
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.plumber_location_icon)));

        } else if (profInfo.getType().equals(PROFESSIONAL_TYPE.FITTER)) {
            m = getMap().addMarker(new MarkerOptions()
                    .title(profInfo.getNome())
                    .snippet(profInfo.getPhone_number())
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.fitter_location_icon)));
        }
        return m;
    }

    public Marker placeMarker(LatLng latLng) {
        Marker m = getMap().addMarker(new MarkerOptions()
                .title("Minha localização")
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.user_location_icon)));
        return m;
    }

}
