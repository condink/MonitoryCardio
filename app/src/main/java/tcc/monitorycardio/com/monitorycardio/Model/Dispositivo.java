package tcc.monitorycardio.com.monitorycardio.Model;


import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;
import tcc.monitorycardio.com.monitorycardio.helper.Base64Custon;

public class Dispositivo implements Serializable {


    private String nomeFilhoDisp;
    private String data;
    private String numSerie;



    public Dispositivo(){

    }

    public void salvarDispositivo(){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        String idUsuario = Base64Custon.codificarBase64 ( autenticacao.getCurrentUser ().getEmail () );

        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase ();
        referenciaFirebase.child ( "dispositivo" ).child ( idUsuario ).push ().setValue ( this );
    }


    public String getNomeFilhoDisp() {
        return nomeFilhoDisp;
    }

    public void setNomeFilhoDisp(String nomeFilhoDisp) {
        this.nomeFilhoDisp = nomeFilhoDisp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }
/* public Dispositivo(Context context, String[] idFilhos){
        this.ctx = context;
        this.idFilhos = idFilhos;
    }*/



    /*@Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv = new TextView ( ctx );
        tv.setText ( idFilhos[i] );
        tv.setTextColor ( Color.GREEN );

        return tv;
    }*/
}
