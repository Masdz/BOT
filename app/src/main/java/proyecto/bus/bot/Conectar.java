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

    public void post(Map<String,String> param, String port){
        final String portf=port;
        final Map<String,String> paramf=param;
        try {
            StringRequest postrequest = new StringRequest(Request.Method.POST, url+port,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Respuesta ",response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error!=null) {
                                Log.d("Error", "Error al conectar con el servidor");
                            }
                        }
                     }
             ) {
                @Override
                public Map<String, String> getParams() {
                    return paramf;
                }
            };
            queue.add(postrequest);
        }catch (Exception e){
            Toast.makeText(contexto,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
}

    public void enviarCordenadas(Cordenadas cor){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", cor.getId() + "");
        params.put("latitud", cor.getLatitud() + "");
        params.put("longitud", cor.getLongitud() + "");
        params.put("velocidad", cor.getVelocidad() + "");
        post(params, "/ENVIARCORDENADAS");
    }

    public void EliminarCordenadas(int id){
         Map<String,String> params=new HashMap<String, String>();
        params.put("id", id + "");
        post(params,"/ELIMINARCORDENADAS");
    }

}
