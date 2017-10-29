package proyecto.bus.bot;

import android.content.Context;
import android.util.Log;

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
        final Cordenadas paramf=param;
        StringRequest postrequest=new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", error.getMessage());
                }
            }
        ){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params=new HashMap<String, String>();
                params.put("id", paramf.getId()+"");
                params.put("latitud", paramf.getLatitud()+"");
                params.put("longitud", paramf.getLongitud()+"");
                return params;
            }
        };
        queue.add(postrequest);
    }

}
