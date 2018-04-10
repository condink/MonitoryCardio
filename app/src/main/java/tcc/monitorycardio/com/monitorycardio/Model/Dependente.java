package tcc.monitorycardio.com.monitorycardio.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;
import tcc.monitorycardio.com.monitorycardio.helper.Base64Custon;

/**
 * Created by israe on 08/03/2018.
 */

public class Dependente implements Serializable {

    private String idDepend;
    private String spinnerDepend;
    private String nomeDepend;
    private String emailDepend;
    private String senhaDepend;

    public Dependente(){

    }

    public void salvarDependente(){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        String idUsuario = Base64Custon.codificarBase64 ( autenticacao.getCurrentUser ().getEmail () );

        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase ();
        referenciaFirebase.child ( "dependente" ).child ( idUsuario ).child ( getIdDepend () ).setValue ( this );

    }

    @Exclude
    public String getIdDepend() {
        return idDepend;
    }

    public void setIdDepend(String idDepend) {
        this.idDepend = idDepend;
    }

    public String getNomeDepend() {
        return nomeDepend;
    }

    public void setNomeDepend(String nomeDepend) {
        this.nomeDepend = nomeDepend;
    }

    public String getEmailDepend() {
        return emailDepend;
    }

    public void setEmailDepend(String emailDepend) {
        this.emailDepend = emailDepend;
    }

    public String getSenhaDepend() {
        return senhaDepend;
    }

    public void setSenhaDepend(String senhaDepend) {
        this.senhaDepend = senhaDepend;
    }

    public String getSpinnerDepend() {
        return spinnerDepend;
    }

    public void setSpinnerDepend(String spinnerDepend) {
        this.spinnerDepend = spinnerDepend;
    }
}
