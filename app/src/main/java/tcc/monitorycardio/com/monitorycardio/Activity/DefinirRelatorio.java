package tcc.monitorycardio.com.monitorycardio.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;
import tcc.monitorycardio.com.monitorycardio.Model.Filho;
import tcc.monitorycardio.com.monitorycardio.Model.Relatorio;
import tcc.monitorycardio.com.monitorycardio.Model.Usuario;
import tcc.monitorycardio.com.monitorycardio.R;

public class DefinirRelatorio extends AppCompatActivity {

    private AlertDialog.Builder mensagem;
    private RadioButton radioGrupIdade;
    private RadioGroup radioGroup;

    private Spinner spinnerRelatorio;

    private SeekBar seekBarMin;
    private SeekBar seekBarMax;

    private EditText textoMin;
    private EditText textoMax;

    private Relatorio relatorio;
    private Filho filho;
    private Usuario usuario;
    private Intent intent;
    private CadastroUsuario cadastroUsuario;

    private FirebaseAuth autenticacao;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference filhoRef = FirebaseDatabase.getInstance ().getReference ();

    private Button salvarRelatorio;
    private Button proximaActivity;
    private Button escolherRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_definir_relatorio );

        salvarRelatorio = findViewById ( R.id.botaoSalvarRelatorio );
        proximaActivity = findViewById ( R.id.proxRelatorio );
        escolherRadio = findViewById ( R.id.esolherRadio );

        textoMin = findViewById ( R.id.texto_minimo );
       textoMax = findViewById ( R.id.texto_maximo );

        SimpleMaskFormatter simpleMaskMin = new SimpleMaskFormatter ( "NNN" );
        SimpleMaskFormatter simpleMaskMax = new SimpleMaskFormatter ( "NNN" );

        MaskTextWatcher maskMin = new MaskTextWatcher ( textoMin, simpleMaskMin );
        textoMin.addTextChangedListener ( maskMin );

        MaskTextWatcher maskMax = new MaskTextWatcher ( textoMax, simpleMaskMax );
        textoMax.addTextChangedListener ( maskMax );

        seekBarMax = findViewById ( R.id.seekBar_maximo );
        seekBarMin = findViewById ( R.id.seekBar_minimo );

        radioGroup = findViewById ( R.id.radioIdade );
        int selecaoID = radioGroup.getCheckedRadioButtonId ();
        radioGrupIdade = findViewById ( selecaoID );

        intent = getIntent ();
        usuario = new Usuario ();
        usuario = (Usuario) intent.getSerializableExtra ("usuario" );

        DatabaseReference filho = filhoRef.child ( "filho" );

        /*filho.child ( "filho" ).addValueEventListener ( new ValueEventListener () {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final List<Filho> filhos = new ArrayList<Filho> (  );

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren ()){
                        Filho filho = areaSnapshot.child ( "nomeFilho" ).getValue (Filho.class);
                        filhos.add ( filho );
                    }

                    spinnerRelatorio = findViewById ( R.id.spinnerRelatorio );
                    final ArrayAdapter<Filho> filhoAdapter = new ArrayAdapter<Filho> ( DefinirRelatorio.this,
                            android.R.layout.simple_spinner_dropdown_item, filhos);
                    filhoAdapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
                    spinnerRelatorio.setAdapter ( filhoAdapter );


                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

*/
        //Colocar valor padrão no Radio selecionado
        escolherRadio.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                int idBotaoEsolhido = radioGroup.getCheckedRadioButtonId ();
                if (idBotaoEsolhido > 0) {
                    radioGrupIdade = findViewById ( idBotaoEsolhido );
                    switch (idBotaoEsolhido) {
                        case R.id.radioRelatorio1:
                            textoMin.setText ( "40" );
                            textoMax.setText ( "170" );
                            break;
                        case R.id.radioRelatorio2:
                            textoMin.setText ( "50" );
                            textoMax.setText ( "180" );
                            break;
                        case R.id.radioRelatorio3:
                            textoMin.setText ( "60" );
                            textoMax.setText ( "190" );
                            break;

                    }

                }

                seekBarMin.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener () {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        textoMin.setText (String.valueOf ( progress ));

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Toast.makeText ( DefinirRelatorio.this, "Defina o valor mínimo para acionar o alerta!", Toast.LENGTH_SHORT ).show ();
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Toast.makeText ( DefinirRelatorio.this, "Valor definido!", Toast.LENGTH_SHORT ).show ();
                    }
                } );

                seekBarMax.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener () {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        textoMax.setText ( String.valueOf ( progress ) );
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Toast.makeText ( DefinirRelatorio.this, "Defina o valor máximo para acionar o alerta!", Toast.LENGTH_SHORT ).show ();
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Toast.makeText ( DefinirRelatorio.this, "Valor definido!", Toast.LENGTH_SHORT ).show ();
                    }

                } );
            }
        } );

        salvarRelatorio.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

               if(textoMin.length()==0 || textoMax.length ()==0) {

                   mensagem = new AlertDialog.Builder ( DefinirRelatorio.this );
                   mensagem.setTitle ( "Atenção" );
                   mensagem.setIcon ( android.R.drawable.ic_delete );
                   mensagem.setMessage ( "Obrigatório esolher filho e idade!" );
                   mensagem.setPositiveButton ( "Ok", new DialogInterface.OnClickListener () {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           Toast.makeText ( DefinirRelatorio.this, "Preencha os campos!!", Toast.LENGTH_SHORT ).show ();
                       }
                   } );
                   mensagem.create ();
                   mensagem.show ();
                   mensagem.setCancelable ( false );


                }else {

                   autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
                   relatorio = new Relatorio ();
                   relatorio.setTextoMin ( textoMin.getText ().toString () );
                   relatorio.setTextoMax ( textoMax.getText ().toString () );

                   relatorio.salvarRelatorio ();

                   Toast.makeText ( DefinirRelatorio.this, "Sucesso ao cadastrar seu relatório!.", Toast.LENGTH_SHORT ).show ();

               }

        }
        } );

        proximaActivity.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                intent =  new Intent ( DefinirRelatorio.this, CadastrarDependente.class );
                startActivity ( intent );

            }
        } );

    }

}





