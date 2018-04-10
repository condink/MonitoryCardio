package tcc.monitorycardio.com.monitorycardio.Model;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;
import tcc.monitorycardio.com.monitorycardio.helper.Base64Custon;

public class Filho implements Serializable {

    private String nomeFilho;
    private String CPF;
    private String data;

   // private Filho filho;

    public Filho(){

    }

    public void salvarFilho(){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        String idUsuario = Base64Custon.codificarBase64 ( autenticacao.getCurrentUser ().getEmail () );

        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase ();
        referenciaFirebase.child ( "filho" ).child ( idUsuario ).push ().setValue ( this );


    }

    public String getNomeFilho() {
        return nomeFilho;
    }

    public void setNomeFilho(String nomeFilho) {
        this.nomeFilho = nomeFilho;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
