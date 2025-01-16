package ro.pub.cs.systems.eim.practicaltest02v9;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Coordonatele pentru Ghelmegioaia (centrul hărții)
        LatLng ghelmegioaia = new LatLng(44.2058, 23.4749);

        // Coordonatele pentru București (marker)
        LatLng bucharest = new LatLng(44.4268, 26.1025);

        // Adaugă un marker pe București
        mMap.addMarker(new MarkerOptions().position(bucharest).title("Marker în București"));
        mMap.addMarker(new MarkerOptions().position(ghelmegioaia).title("Marker în Ghelmegioaia"));
        // Setează camera centrată pe Ghelmegioaia
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ghelmegioaia, 10));
    }
}