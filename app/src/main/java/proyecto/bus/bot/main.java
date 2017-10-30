package proyecto.bus.bot;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationListener;

public class main extends AppCompatActivity {
    Conectar conectar;
    Cordenadas cordenadas;
    LocationManager locationManager;
    Context contexto;
    TextView mos;
    EditText setID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contexto = this;
        //Conectar conectar=new Conectar(this,"http://localhost");192.168.1.87
        conectar = new Conectar(this, "http://192.168.137.1");
        cordenadas = new Cordenadas();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mos=(TextView)findViewById(R.id.mos);
        setID=(EditText)findViewById(R.id.tfid);
        Button b2=(Button)findViewById(R.id.bset);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cordenadas.setId(Integer.parseInt(setID.getText()+""));
            }
        });
        //ActualizarUbicacion();
        Localizacion local=new Localizacion(contexto,cordenadas,conectar,mos);
        Button b=(Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActualizarUbicacion();
            }
        });
    }

    public void enviar() {
        conectar.post(cordenadas, ":3000/ENVIARCORDENADAS");
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void ActualizarUbicacion() {
        if (!isLocationEnabled()) {
            Toast.makeText(contexto,"Servicio de ubicacion desabilitado",Toast.LENGTH_LONG).show();
            mos.setText("Ubicacion no disponible");
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(contexto,"Permiso de ubicacion denegado",Toast.LENGTH_LONG).show();
                mos.setText("Permiso de ubicacion denegado");
                return;
            }
            mos.setText("Obteniendo ubicacion");
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000, 0, locationListenerGPS);
        }
    }
    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            cordenadas.setLatitud(location.getLatitude());
            cordenadas.setLongitud(location.getLongitude());
            cordenadas.setVelocidad(location.getSpeed());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    enviar();
                    //Toast.makeText(contexto, "GPS Provider update", Toast.LENGTH_SHORT).show();
                    Log.d("Actualizado","Localicacion actualizada");
                    mos.setText(cordenadas.desp());
                }
            });
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };
}
