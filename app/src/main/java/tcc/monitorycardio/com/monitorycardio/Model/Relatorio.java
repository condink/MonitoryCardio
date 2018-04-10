package tcc.monitorycardio.com.monitorycardio.Model;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;
import tcc.monitorycardio.com.monitorycardio.helper.Base64Custon;

public class Relatorio {

    private String id;
    private String nomeFilho;
    private String textoMin;
    private String textoMax;

    public Relatorio(){

    }

    public void salvarRelatorio(){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        String idUsuario = Base64Custon.codificarBase64 ( autenticacao.getCurrentUser ().getEmail () );

        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase ();
        referenciaFirebase.child ( "relatorio" ).child ( idUsuario ).push ().setValue ( this );

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeFilho() {
        return nomeFilho;
    }

    public void setNomeFilho(String nomeFilho) {
        this.nomeFilho = nomeFilho;
    }

    public String getTextoMin() {
        return textoMin;
    }

    public void setTextoMin(String textoMin) {
        this.textoMin = textoMin;
    }

    public String getTextoMax() {
        return textoMax;
    }

    public void setTextoMax(String textoMax) {
        this.textoMax = textoMax;
    }
}
