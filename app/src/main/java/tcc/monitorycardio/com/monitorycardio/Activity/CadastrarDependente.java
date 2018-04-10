package tcc.monitorycardio.com.monitorycardio.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.EmptyStackException;

import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;
import tcc.monitorycardio.com.monitorycardio.Model.Dependente;
import tcc.monitorycardio.com.monitorycardio.Model.Usuario;
import tcc.monitorycardio.com.monitorycardio.R;
import tcc.monitorycardio.com.monitorycardio.helper.Base64Custon;


public class CadastrarDependente extends AppCompatActivity {

    private Button finalizarDepend;
    private Button escolherDepend;
    private Button ativarDepend;
    private Button limparDepend;

    private RadioGroup radioGroup_Depend;
    private RadioButton radioButtonEscolha;

    private AlertDialog.Builder mensagem;

    private EditText nomeDepend;
    private EditText emailDepend;
    private EditText senhaDepend;

    private Intent intent;
    private Usuario usuario;
    private Dependente dependente;

    private FirebaseAuth autenticacao;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_cadastrar_dependente );

        nomeDepend = findViewById ( R.id.nomeDepend );
        emailDepend = findViewById ( R.id.emailDepend );
        senhaDepend = findViewById ( R.id.senhaDepend );

        radioGroup_Depend = findViewById ( R.id.radioGrup_depend );

        escolherDepend = findViewById ( R.id.escolherDepend );
        ativarDepend = findViewById ( R.id.botaoAtivar_Depend );
        finalizarDepend = findViewById ( R.id.finalizarDepend );
        limparDepend = findViewById ( R.id.limparDepend );

        nomeDepend.setEnabled ( false );
        emailDepend.setEnabled ( false );
        senhaDepend.setEnabled ( false );

        ativarDepend.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Toast.makeText ( CadastrarDependente.this, "Escolha a opção SIM!", Toast.LENGTH_LONG ).show ();
            }
        } );

        escolherDepend.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {


                int idBotaoEsolhido = radioGroup_Depend.getCheckedRadioButtonId ();
                if (idBotaoEsolhido > 0) {
                    radioButtonEscolha = findViewById ( idBotaoEsolhido );
                            switch (idBotaoEsolhido) {
                            case R.id.radioNao:
                                bloquearDados ();
                                finalizarDependente ();
                                break;
                            case R.id.radioSim:
                                liberarDados ();
                                finalizarDependente ();
                                break;
                        }
                    }
                }
         } );


        finalizarDepend.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Toast.makeText ( CadastrarDependente.this, "Escolha a opção SIM ou NÃO!", Toast.LENGTH_LONG ).show ();
            }
        } );

        }

    public void bloquearDados(){

        nomeDepend.setEnabled ( false );
        nomeDepend.setText ( "" );
        emailDepend.setEnabled ( false );
        emailDepend.setText ( "" );
        senhaDepend.setEnabled ( false );
        senhaDepend.setText ( "" );
        Toast.makeText ( CadastrarDependente.this, "Clique em FINALIZAR", Toast.LENGTH_LONG ).show ();

        ativarDepend.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Toast.makeText ( CadastrarDependente.this, "Escolha a opção SIM!", Toast.LENGTH_LONG ).show ();
            }
        } );

    }
    public void liberarDados(){

        nomeDepend.setEnabled ( true );
        nomeDepend.requestFocus ();
        emailDepend.setEnabled ( true );
        senhaDepend.setEnabled ( true );

        limparDepend.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                nomeDepend.setText ( "" );
                emailDepend.setText ( "" );
                senhaDepend.setText ( "" );
                nomeDepend.requestFocus ();
            }
        } );

        ativarDepend.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                if( nomeDepend.length () == 0 || emailDepend.length () == 0 || senhaDepend.length () == 0 ){

                    validaCampos ();

                }else{
                    dependente = new Dependente ();
                    dependente.setNomeDepend ( nomeDepend.getText ().toString () );
                    dependente.setEmailDepend ( emailDepend.getText ().toString () );
                    dependente.setSenhaDepend ( senhaDepend.getText ().toString () );

                    cadastrarDependente ();

                    nomeDepend.setText ( "" );
                    emailDepend.setText ( "" );
                    senhaDepend.setText ( "" );
                    nomeDepend.requestFocus ();
                }
            }
        } );

    }

    public void cadastrarDependente(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        autenticacao.createUserWithEmailAndPassword ( dependente.getEmailDepend (), dependente.getSenhaDepend () )
                .addOnCompleteListener ( CadastrarDependente.this, new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful ()){
                            Toast.makeText ( CadastrarDependente.this, "Sucesso ao cadastrar o dependente!", Toast.LENGTH_LONG ).show ();

                            String identificadorDepend = Base64Custon.codificarBase64 ( dependente.getEmailDepend () );
                            dependente.setIdDepend ( identificadorDepend );

                            dependente.salvarDependente ();

                        }else{
                            /* Tratamento de exceções com FireBase*/
                            String erroExcecao = "";
                            try {

                                throw task.getException ();

                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroExcecao = "Digite uma senha mais forte, contendo caracteres, número e letra maiuscúla";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroExcecao = "O E-mail digitado é invalido, digite um novo E-mail.";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroExcecao = "Este E-mail ja está em uso no MonitoryCardio";
                            } catch (EmptyStackException e) {
                                erroExcecao = "Preencha todos os campos!";
                            } catch (Exception e) {
                                erroExcecao = "Ao cadastrar usuário";
                                e.printStackTrace ();
                            }
                            Toast.makeText ( CadastrarDependente.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG ).show ();
                        }
                    }
                } );

    }

    public void finalizarDependente(){
        finalizarDepend.setOnClickListener ( new View.OnClickListener () {
        @Override
        public void onClick(View view) {

            intent = new Intent ( CadastrarDependente.this, MainActivity.class );
            startActivity ( intent );
            finish ();
            Toast.makeText ( CadastrarDependente.this, "Você concluiu o cadastro!", Toast.LENGTH_LONG ).show ();


        }
    } );
}
    //Validando campos
    private boolean validaCampos() {

        boolean res = false;

        String nomeC = nomeDepend.getText ().toString ();
        String emailC = emailDepend.getText ().toString ();
        String senhaC = senhaDepend.getText ().toString ();


        if (res = isCampoVazio ( nomeC )) {
            nomeDepend.requestFocus ();
        } else if (res = isCampoVazio ( emailC )) {
            emailDepend.requestFocus ();
        } else if (res = isCampoVazio ( senhaC )) {
            senhaDepend.requestFocus ();
        }

        if (res) {
            mensagem = new AlertDialog.Builder ( CadastrarDependente.this );
            mensagem.setTitle ( "Atenção" );
            mensagem.setIcon ( android.R.drawable.ic_delete );
            mensagem.setMessage ( "Obrigatório preencher todos os campos!" );
            mensagem.setPositiveButton ( "Ok", new DialogInterface.OnClickListener () {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText ( CadastrarDependente.this, "Preencha todos os campos!!", Toast.LENGTH_SHORT ).show ();
                }
            } );
            mensagem.create ();
            mensagem.show ();
            mensagem.setCancelable ( false );

        }
        return true;
    }

    public boolean isCampoVazio(String valor){
        boolean resultado = (TextUtils.isEmpty ( valor ) || valor.trim ().isEmpty ());
        return  resultado;
    }

    public static class Preferencias {

        private Context contexto;
        private SharedPreferences preferences;
        private final String NOME_ARQUIVO = "MonioryCardio.preferencias";
        private final int MODE = 0;
        private SharedPreferences.Editor editor;

        private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";

        public Preferencias(Context contextoParametro){

            contexto = contextoParametro;
            preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE );
            editor = preferences.edit();

        }

        public void salvarDados( String identificadorUsuario ){

            editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
            editor.commit();

        }

        public String getIdentificador(){
            return preferences.getString(CHAVE_IDENTIFICADOR, null);
        }

    }
}
