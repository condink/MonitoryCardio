package tcc.monitorycardio.com.monitorycardio.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;

import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;
import tcc.monitorycardio.com.monitorycardio.Model.Filho;
import tcc.monitorycardio.com.monitorycardio.R;

public class CadastroFilho extends AppCompatActivity {

    private TextView nomeFilho;
    private TextView CPFfilho;
    private TextView dataFilho;

    private Button botaoSalvarFilho;
    private Button botaoProximoFilho;
    private Button botaoLimpar;

    private AlertDialog.Builder mensagem;

    private Filho filho;
    private Intent intent;

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_cadastro_filho );

        nomeFilho = findViewById ( R.id.texto_filho );
        CPFfilho = findViewById ( R.id.texto_CPF_FIlho );
        dataFilho = findViewById ( R.id.texto_data_filho );

        botaoSalvarFilho = findViewById ( R.id.botaoSalvarFilho );
        botaoProximoFilho = findViewById ( R.id.botaoProximaFilho );
        botaoLimpar = findViewById ( R.id.botaoLimpar_filho );

        SimpleMaskFormatter simpleMaskCPF = new SimpleMaskFormatter ( "NNN.NNN.NNN-NN" );
        SimpleMaskFormatter simpleMaskData = new SimpleMaskFormatter ( "NN/NN/NNNN" );

        MaskTextWatcher maskCPF = new MaskTextWatcher ( CPFfilho, simpleMaskCPF );
        CPFfilho.addTextChangedListener ( maskCPF );

        MaskTextWatcher maskData = new MaskTextWatcher ( dataFilho, simpleMaskData );
        dataFilho.addTextChangedListener ( maskData );

        botaoLimpar.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                nomeFilho.setText ( "" );
                CPFfilho.setText ( "" );
                dataFilho.setText ( "" );
                nomeFilho.requestFocus ();

            }
        } );

        botaoSalvarFilho.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if (nomeFilho.length () == 0 || CPFfilho.length () == 0 || dataFilho.length () == 0){

                    validaCampos ();

                }else {
                    autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();

                    filho = new Filho ();
                    filho.setNomeFilho ( nomeFilho.getText ().toString () );
                    filho.setCPF ( CPFfilho.getText ().toString () );
                    filho.setData ( dataFilho.getText ().toString () );

                    Toast.makeText ( CadastroFilho.this, "Sucesso ao cadastrar seu filho.", Toast.LENGTH_SHORT ).show ();

                    filho.salvarFilho ();

                    // Limpar camposw
                    nomeFilho.setText ( "" );
                    CPFfilho.setText ( "" );
                    dataFilho.setText ( "" );
                    nomeFilho.requestFocus ();

                }


            }
        } );

        botaoProximoFilho.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                intent = new Intent ( CadastroFilho.this, IncluirDispositivo.class );
                startActivity ( intent );
            }
        } );

    }

    //Validando campos
    private boolean validaCampos() {

        boolean res = false;

        String nomeC = nomeFilho.getText ().toString ();
        String cpfC = CPFfilho.getText ().toString ();
        String dataC = dataFilho.getText ().toString ();


        if (res = isCampoVazio ( nomeC )) {
            nomeFilho.requestFocus ();
        } else if (res = isCampoVazio ( dataC )) {
            dataFilho.requestFocus ();
        } else if (res = isCampoVazio ( cpfC )) {
            CPFfilho.requestFocus ();
        }

        if (res) {
            mensagem = new AlertDialog.Builder(CadastroFilho.this);
            mensagem.setTitle("Atenção");
            mensagem.setIcon ( android.R.drawable.ic_delete);
            mensagem.setMessage("Obrigatório preencher todos os campos!");
            mensagem.setPositiveButton ( "Ok", new DialogInterface.OnClickListener () {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText ( CadastroFilho.this, "Preencha todos os campos!", Toast.LENGTH_SHORT ).show ();
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



