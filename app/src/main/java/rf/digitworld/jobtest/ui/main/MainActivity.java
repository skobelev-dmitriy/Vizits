package rf.digitworld.jobtest.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rf.digitworld.jobtest.R;
import rf.digitworld.jobtest.data.model.Organization;
import rf.digitworld.jobtest.data.model.Vizit;
import rf.digitworld.jobtest.ui.base.BaseActivity;
import rf.digitworld.jobtest.util.DialogFactory;

public class MainActivity extends BaseActivity implements MainMvpView, OnMapReadyCallback {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject MainPresenter mMainPresenter;
    @Inject
    VizitsAdapter mVizitsAdapter;

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.map)
    MapView mapView;

    private GoogleMap map;


    private String activeOrgId;
    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setAdapter(mVizitsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mVizitsAdapter.setListener(new VizitsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Vizit item) {
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                mMainPresenter.setActiveVizit(item);
            }
        });
        mMainPresenter.attachView(this);
        mMainPresenter.syncOrganizations();
        initMap(savedInstanceState);


    }

    private void initMap(Bundle savedInstanceState){
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            Log.e("Address Map", "Could not initialize google play", e);
        }
        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
            case ConnectionResult.SUCCESS:
                mapView = (com.google.android.gms.maps.MapView) findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                if (mapView != null) {
                    mapView.getMapAsync(this);


                }

                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(this, "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(this, "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, GooglePlayServicesUtil.isGooglePlayServicesAvailable(this), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        map.getUiSettings().setMapToolbarEnabled(true);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String id=marker.getSnippet();
                if(!id.equals(activeOrgId)){
                    mMainPresenter.setActiveOrg(id);
                    mVizitsAdapter.refresh();
                    activeOrgId=id;
                }else{
                    mMainPresenter.setActiveOrg(null);
                    activeOrgId=null;
                }
                return false;
            }
        });
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Position", cameraPosition.toString());

            }
        });

        setCamera(getCoordinate());

    }
    public void setCamera(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        map.animateCamera(cameraUpdate);
    }

    public LatLng getCoordinate() {

        LatLng coordinate=new LatLng(55.790112, 37.538071);

        return coordinate;
    }
    private void addMarkers(List<Vizit> responce) {
        map.clear();
        String activeVizitOrg=null;
        if (responce.size() != 0) {
            MarkerOptions marker;
            HashSet<Organization> hashSet=new HashSet<>();
            for(Vizit vizit: responce) {
                if(vizit.isSelected()){
                    activeVizitOrg=vizit.getOrganizationId();
                }
                hashSet.add(vizit.getOrganization());
            }
            for(Organization organization: hashSet) {
                marker = new MarkerOptions();
                marker.position(new LatLng(organization.getLatitude(),organization.getLongitude()));
                int icon;

                if(organization.isSelected()||organization.getOrganizationId().equals(activeVizitOrg)){
                    icon=organization.getMarkerActive();
                    setCamera(new LatLng(organization.getLatitude(),organization.getLongitude()));
                }else{
                    icon=organization.getMarker();
                }

                if(icon!=0){
                    marker.icon(BitmapDescriptorFactory.fromResource(icon));
                }

                //marker.title(atm.getAId());
                marker.snippet(organization.getOrganizationId());

                map.addMarker(marker);
            }

        }
    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }
    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mMainPresenter.detachView();
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showVizits(List<Vizit> vizits) {
        mVizitsAdapter.setVizits(vizits);
        mVizitsAdapter.notifyDataSetChanged();
        addMarkers(vizits);
    }



    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_vizits))
                .show();
    }

    @Override
    public void showVizitsEmpty() {
        mVizitsAdapter.setVizits(Collections.<Vizit>emptyList());
        mVizitsAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.empty_vizits, Toast.LENGTH_LONG).show();
    }

}
