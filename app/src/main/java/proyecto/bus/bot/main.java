package proyecto.bus.bot;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class main extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    final long INTERVAL = 10000;
    final long FASTINTERVAL = 1000;

    Conectar conectar;
    Cordenadas cordenadas;
    Context contexto;
    TextView mos;
    EditText setID;
    GoogleApiClient mgapiClient;
    LocationRequest mLocationRequest;
    boolean actualizando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contexto = this;
        //Conectar conectar=new Conectar(this,"http://localhost");192.168.1.87
        conectar = new Conectar(this, "http://192.168.137.1");
        cordenadas = new Cordenadas();
        mos = (TextView) findViewById(R.id.mos);
        setID = (EditText) findViewById(R.id.tfid);
        Button b2 = (Button) findViewById(R.id.bset);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conectar.EliminarCordenadas(cordenadas.getId());
                cordenadas.setId(Integer.parseInt(setID.getText() + ""));
            }
        });
        //ActualizarUbicacion();
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActualizarUbicacion();
            }
        });
        iniApiClient();
        crearLocationRequest();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mgapiClient != null) {
            if (mgapiClient.isConnected()) {
                actualizarUbicaciones();
            } else {
                mgapiClient.connect();
            }
        }
    }

    @Override
    protected void onDestroy(){
        detenerActualizacion();
        super.onDestroy();

    }

    private void actualizarUbicaciones() {
        if (mgapiClient != null && mgapiClient.isConnected() && !actualizando) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mos.setText("Permisos denegados");
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mgapiClient, mLocationRequest, this);
            actualizando=true;
            mos.setText("Obteniendo ubicacion...");
        }
    }

    private void detenerActualizacion() {
        if (mgapiClient != null && mgapiClient.isConnected() && !actualizando) {
           LocationServices.FusedLocationApi.removeLocationUpdates(mgapiClient, this);
           mgapiClient.disconnect();
           actualizando=false;
        }
    }

    private void iniApiClient(){
        if (mgapiClient== null){
            mgapiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
        }
    }

    private void crearLocationRequest(){
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTINTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        actualizarUbicaciones();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null && conectar!=null&& cordenadas!=null){
            cordenadas.setLongitud(location.getLongitude());
            cordenadas.setLatitud(location.getLatitude());
            cordenadas.setVelocidad(location.getSpeed());
            mos.setText(cordenadas.desp());
            conectar.enviarCordenadas(cordenadas);
        }
    }

}
