package tcc.monitorycardio.com.monitorycardio.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;
import tcc.monitorycardio.com.monitorycardio.ListaFirebase.ListaFIlhoFirebase;
import tcc.monitorycardio.com.monitorycardio.Model.Dispositivo;
import tcc.monitorycardio.com.monitorycardio.Model.Filho;
import tcc.monitorycardio.com.monitorycardio.R;
import tcc.monitorycardio.com.monitorycardio.helper.Base64Custon;

public class IncluirDispositivo extends AppCompatActivity  {

    private Spinner spinnerSelecao;
    private EditText numSerie;
    private EditText dataDispositivo;

    private Button ativarDispositivo;
    private Button proximoActivity;
    private Button botaoLimpar;

    private AlertDialog.Builder mensagem;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
    private DatabaseReference referencia = ConfiguracaoFirebase.getFirebase ();
    private DatabaseReference filhoRef;

    private Filho filho;
    private Dispositivo dispositivo;
    private Intent intent;
    private ListaFIlhoFirebase listaFIlhoFirebase;

    private ValueEventListener valorListenerFilho;

    private CadastroUsuario cadastroUsuario;

    //Array list

    private ArrayAdapter<String> filhoAdapter;

    private List<String> filhos = new ArrayList<> (  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_incluir_dispositivo );

        spinnerSelecao = findViewById ( R.id.spinner_nomeFilho );
        numSerie = findViewById ( R.id.textoSerieDispositivo );
        dataDispositivo = findViewById ( R.id.textoData_Compra );

        ativarDispositivo = findViewById ( R.id.botaoAtivar );
        proximoActivity = findViewById ( R.id.botaoProxDispositivo );
        botaoLimpar = findViewById ( R.id.botaoLimpar_dispos );

        SimpleMaskFormatter simpleMaskSerie = new SimpleMaskFormatter ( "NNNN" );
        SimpleMaskFormatter simpleMaskData = new SimpleMaskFormatter ( "NN/NN/NNNN" );

        MaskTextWatcher maskSerie = new MaskTextWatcher ( numSerie, simpleMaskSerie );
        numSerie.addTextChangedListener ( maskSerie );

        MaskTextWatcher maskData = new MaskTextWatcher ( dataDispositivo, simpleMaskData );
        dataDispositivo.addTextChangedListener ( maskData );

        //Criação da classe Spinner com chamdas

        String emailUsuario = autenticacao.getCurrentUser ().getEmail ();
        String idUsuario = Base64Custon.codificarBase64 ( emailUsuario );
        filhoRef = referencia.child ( "filho" ).child ( idUsuario );


        filhoRef.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren ()){

                    String filho = areaSnapshot.getValue (Filho.class).getNomeFilho ();
                    filhos.add ( filho );

                }

                filhoAdapter = new ArrayAdapter<> ( IncluirDispositivo.this,
                        android.R.layout.simple_spinner_dropdown_item, filhos );
                filhoAdapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
                spinnerSelecao.setAdapter ( filhoAdapter );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );


        ativarDispositivo.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                if (numSerie.length () == 0 || dataDispositivo.length () == 0) {

                    validaCampos ();
                }else {
                    autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();

                    dispositivo = new Dispositivo ();
                    dispositivo.setNumSerie ( numSerie.getText ().toString () );
                    dispositivo.setData ( dataDispositivo.getText ().toString () );
                    dispositivo.setNomeFilhoDisp ( (spinnerSelecao).toString () );

                    dispositivo.salvarDispositivo ();

                    Toast.makeText ( IncluirDispositivo.this, "Sucesso ao cadastrar seu dispositivo.", Toast.LENGTH_SHORT ).show ();

                    //Limpar Campos
                    numSerie.setText ( "" );
                    dataDispositivo.setText ( "" );
                    numSerie.requestFocus ();
                }
            }
        } );

        // Próxima activity
        proximoActivity.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                intent = new Intent ( IncluirDispositivo.this, DefinirRelatorio.class );
                startActivity ( intent );

            }
        } );

        botaoLimpar.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                //Limpar Campos
                numSerie.setText ( "" );
                dataDispositivo.setText ( "" );
                numSerie.requestFocus ();

            }
        })  ;

    }

     //Validando campos
    private boolean validaCampos() {

        boolean res = false;

        String numC = numSerie.getText ().toString ();
        String dataC = dataDispositivo.getText ().toString ();

        if (res = isCampoVazio ( numC )) {
            numSerie.requestFocus ();
        } else if (res = isCampoVazio ( dataC )) {
            dataDispositivo.requestFocus ();
        }

        if (res) {

            mensagem = new AlertDialog.Builder(IncluirDispositivo.this);
            mensagem.setTitle("Atenção");
            mensagem.setIcon ( android.R.drawable.ic_delete);
            mensagem.setMessage("Obrigatório preencher todos os campos!");
            mensagem.setPositiveButton ( "Ok", new DialogInterface.OnClickListener () {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText ( IncluirDispositivo.this, "Preencha todos os campos!", Toast.LENGTH_SHORT ).show ();
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





