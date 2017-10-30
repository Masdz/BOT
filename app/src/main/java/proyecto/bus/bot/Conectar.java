package proyecto.bus.bot;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class Conectar {
    private String url;
    private Context contexto;
    private RequestQueue queue ;

    public Conectar(Context contexto, String url) {
        this.url = url;
        this.contexto = contexto;
        queue = Volley.newRequestQueue(contexto);
    }

    public RequestQueue getQueue(){
        return queue;
    }

    public void post(Cordenadas param, String port){
        final String portf=port;
        final Cordenadas paramf=param;
        try {
            StringRequest postrequest = new StringRequest(Request.Method.POST, url+port,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Respuesta ",response);
                            //Toast.makeText(contexto,response, Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error!=null) {
                                //Log.d("Error", error.getCause().getMessage());
                                //Toast.makeText(contexto, "Idefinido ", Toast.LENGTH_LONG).show();
                            }
                        }
                     }
             ) {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", paramf.getId() + "");
                    params.put("latitud", paramf.getLatitud() + "");
                    params.put("longitud", paramf.getLongitud() + "");
                    params.put("velocidad", paramf.getVelocidad() + "");
                    return params;
                }
            };
            queue.add(postrequest);
        }catch (Exception e){
            Toast.makeText(contexto,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
}

}
