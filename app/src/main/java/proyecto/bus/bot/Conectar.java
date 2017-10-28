package proyecto.bus.bot;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by masdz on 28/10/17.
 */

public class Conectar {
    private String json="{\n" +
            "id=2,\n" +
            "name='QUE WE'\n" +
            "}";
    public Conectar(Context contexto){
        int id=0;
        String name="";
        try{
            JSONObject mjson=new JSONObject(json);
            id=mjson.getInt("id");
            name=mjson.getString("name");

        }catch(Exception e){
            Toast.makeText(contexto,"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        Toast.makeText(contexto,"id="+id+" name="+name,Toast.LENGTH_LONG).show();
    }
}
