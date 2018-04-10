package tcc.monitorycardio.com.monitorycardio.ListaFirebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;
import tcc.monitorycardio.com.monitorycardio.Model.Filho;
import tcc.monitorycardio.com.monitorycardio.helper.Base64Custon;


public class ListaFIlhoFirebase implements Serializable {

    private Filho filho;

    private List<String> filhos = new ArrayList<> (  );

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao ();
    private DatabaseReference referencia = ConfiguracaoFirebase.getFirebase ();
    private DatabaseReference filhoRef;

    public void listarFilhos(){

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

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    } );
}

}
