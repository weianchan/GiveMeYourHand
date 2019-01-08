package student.assignment.taruc.givemeyourhand;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "Map: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        String locationTxt = intent.getExtras().get("Location").toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(locationTxt, 1);

        }catch (IOException e){
            Log.e(TAG, "IOException: " + e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "Found a location: " + address.toString());

            LatLng location = new LatLng(address.getLatitude(), address.getLongitude());

            mMap.addMarker(new MarkerOptions().position(location).title(locationTxt));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        }

    }
}
