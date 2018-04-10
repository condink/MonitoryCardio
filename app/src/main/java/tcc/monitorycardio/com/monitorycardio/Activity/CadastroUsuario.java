package tcc.monitorycardio.com.monitorycardio.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.EmptyStackException;

import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;
import tcc.monitorycardio.com.monitorycardio.Model.Usuario;
import tcc.monitorycardio.com.monitorycardio.R;
import tcc.monitorycardio.com.monitorycardio.helper.Base64Custon;

public class CadastroUsuario extends AppCompatActivity{


    private EditText nome;
    private EditText endereco;
    private EditText CEP;
    private EditText CPF;
    private EditText telefone;
    private EditText email;
    private EditText senha;

    private Button botaoSalva;
    private Button botaoLimpar;


    private AlertDialog.Builder mensagem;

    private Intent intent;
    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_cadastro_usuario );

        nome = findViewById ( R.id.texto_nome );
        endereco = findViewById ( R.id.texto_endereco );
        CEP = findViewById ( R.id.texto_CEP );
        CPF = findViewById ( R.id.texto_CPF_FIlho );
        telefone = findViewById ( R.id.texto_telefone );
        email = findViewById ( R.id.texto_email );
        senha = findViewById ( R.id.texto_senha );

        botaoSalva = findViewById ( R.id.botaoSalvar );
        botaoLimpar = findViewById ( R.id.botaoLimpar_usuario );


        /*Formatar os campos de Telefone, CEP e CPF*/
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter ( "(NN) NNNNN-NNNN" );
        SimpleMaskFormatter simpleMaskCEP = new SimpleMaskFormatter ( "NNNNN-NNN" );
        SimpleMaskFormatter simpleMaskCPF = new SimpleMaskFormatter ( "NNN.NNN.NNN-NN" );

        MaskTextWatcher maskTelefone = new MaskTextWatcher ( telefone, simpleMaskTelefone );
        telefone.addTextChangedListener ( maskTelefone );

        MaskTextWatcher maskCEP = new MaskTextWatcher ( CEP, simpleMaskCEP );
        CEP.addTextChangedListener ( maskCEP );

        MaskTextWatcher maskCPF = new MaskTextWatcher ( CPF, simpleMaskCPF );
        CPF.addTextChangedListener ( maskCPF );

        botaoLimpar.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                //Limpar campos
                        nome.setText ( "" );
                        endereco.setText ( "" );
                        CEP.setText ( "" );
                        CPF.setText ( "" );
                        telefone.setText ( "" );
                        email.setText ( "" );
                        senha.setText ( "" );
                        nome.requestFocus ();


            }
        } );

        botaoSalva.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

               if (nome.length () == 0 || endereco.length () == 0 || CEP.length () == 0 || CPF.length () == 0 ||
                       telefone.length () == 0 || email.length () == 0 || senha.length () == 0){

                   validaCampos ();

                }else {

                   usuario = new Usuario ();
                   usuario.setNome ( nome.getText ().toString () );
                   usuario.setEndereco ( endereco.getText ().toString () );
                   usuario.setCEP ( CEP.getText ().toString () );
                   usuario.setCPF ( CPF.getText ().toString () );
                   usuario.setTelefone ( telefone.getText ().toString () );
                   usuario.setEmail ( email.getText ().toString () );
                   usuario.setSenha ( senha.getText ().toString () );
                   cadastrarUsuario ();
               }
            }
        } );
    }

    private void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
        autenticacao.createUserWithEmailAndPassword ( usuario.getEmail (), usuario.getSenha () )
                .addOnCompleteListener ( CadastroUsuario.this, new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful ()) {

                        Toast.makeText ( CadastroUsuario.this, "Sucesso ao cadastrar o usuário", Toast.LENGTH_LONG ).show ();
                    /*Recuperar o ID do usuário que esta no banco de dados para diferenciar no nó do fireBase*/

                        String identificadorUsuario = Base64Custon.codificarBase64 ( usuario.getEmail () );
                        usuario.setId ( identificadorUsuario );
                        usuario.salvar ();

                        intent = new Intent ( CadastroUsuario.this, CadastroFilho.class );
                        startActivity ( intent );

                    } else {
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
                        Toast.makeText ( CadastroUsuario.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG ).show ();
                    }
                }

        } );
    }

    //Validando campos
    private boolean validaCampos() {

        boolean res = false;

        String nomeC = nome.getText ().toString ();
        String enderecoC = endereco.getText ().toString ();
        String cepC = CEP.getText ().toString ();
        String cpfC = CPF.getText ().toString ();
        String telefoneC = telefone.getText ().toString ();
        String emailC = email.getText ().toString ();
        String senhaC = senha.getText ().toString ();

        if (res = isCampoVazio ( nomeC )) {
            nome.requestFocus ();
            } else if (res = isCampoVazio ( enderecoC )) {
            endereco.requestFocus ();
        } else if (res = isCampoVazio ( cepC )) {
            CEP.requestFocus ();
        } else if (res = isCampoVazio ( cpfC )) {
            CPF.requestFocus ();
        } else if (res = isCampoVazio ( telefoneC )) {
            telefone.requestFocus ();
        } else if (res = isCampoVazio ( emailC )) {
            email.requestFocus ();
            return res;
        } else if (res = isCampoVazio ( senhaC )) {
            senha.requestFocus ();
        }

        if (res) {
            mensagem = new AlertDialog.Builder(CadastroUsuario.this);
            mensagem.setTitle("Atenção");
            mensagem.setIcon ( android.R.drawable.ic_delete);
            mensagem.setMessage("Obrigatório preencher todos os campos!");
            mensagem.setPositiveButton ( "Ok", new DialogInterface.OnClickListener () {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText ( CadastroUsuario.this, "Preencha todos os campos!", Toast.LENGTH_SHORT ).show ();
                }
            } );
            mensagem.create ();
            mensagem.show();
            mensagem.setCancelable ( false );

        }
       return true;
    }

    public boolean isCampoVazio(String valor){
        boolean resultado = (TextUtils.isEmpty ( valor ) || valor.trim ().isEmpty ());
        return  resultado;
    }
}

