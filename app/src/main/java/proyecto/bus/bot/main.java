package proyecto.bus.bot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class main extends AppCompatActivity {
    Conectar conectar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Conectar conectar=new Conectar(this,"http://localhost");192.168.1.87
        conectar=new Conectar(this,"http://192.168.1.87");

    }
    public void enviar(View v){
        conectar.post(new Cordenadas(2,4,5),"3000/ENVIARCORDENADAS");
    }
}
