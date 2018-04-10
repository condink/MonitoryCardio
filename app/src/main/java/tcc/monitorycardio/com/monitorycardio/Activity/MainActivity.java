package tcc.monitorycardio.com.monitorycardio.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;
import tcc.monitorycardio.com.monitorycardio.Model.Usuario;
import tcc.monitorycardio.com.monitorycardio.R;

public class MainActivity extends AppCompatActivity {

    private TextView textoCadastrar;
    private TextView email;
    private TextView senha;
    private Button botaoLogar;

    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        email = findViewById ( R.id.editText_email );
        senha = findViewById ( R.id.editText_senha );
        botaoLogar = findViewById ( R.id.botaoLogar );
        textoCadastrar = findViewById ( R.id.textoCadastrar );

        botaoLogar.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                String textoEmail = email.getText ().toString ();
                String textoSenha = senha.getText ().toString ();

                if (!textoEmail.isEmpty ()){
                    if (!textoSenha.isEmpty ()){

                        usuario = new Usuario ();
                        usuario.setEmail ( email.getText ().toString () );
                        usuario.setSenha ( senha.getText ().toString () );
                        validarLogin ();

                    }else {
                        Toast.makeText ( MainActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT ).show ();
                    }
                }else {
                    Toast.makeText ( MainActivity.this, "Preencha a email!", Toast.LENGTH_SHORT ).show ();
                }

            }
        } );

        textoCadastrar.setOnClickListener ( new View.OnClickListener ()

        {
            @Override
            public void onClick(View view) {
                startActivity ( new Intent ( MainActivity.this, CadastroUsuario.class ) );
            }
        } );

    }
    private  void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        if(autenticacao.getCurrentUser () != null){

        }
    }

    private void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        autenticacao.signInWithEmailAndPassword (
                usuario.getEmail (),
                usuario.getSenha ()
        ).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful ()){


                    Toast.makeText ( MainActivity.this, "Sucesso ao realizar o login", Toast.LENGTH_LONG ).show ();
                    finish ();
                }else{
                    String erroExcecao = "";
                    try{
                        throw task.getException ();
                    } catch (FirebaseAuthInvalidUserException e) {
                        erroExcecao = "E-mail digitado errado ou não existe!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Senha digitada errado!";
                    } catch (Exception e) {
                        erroExcecao = "Ao realizar o login";
                        e.printStackTrace ();
                    }


                    Toast.makeText ( MainActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG ).show ();
                }
            }
        } );

    }
    /*private void abrirTela(){

        finish ();

    }*/
}

