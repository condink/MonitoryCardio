package tcc.monitorycardio.com.monitorycardio.Model;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import tcc.monitorycardio.com.monitorycardio.Activity.CadastroFilho;
import tcc.monitorycardio.com.monitorycardio.Activity.CadastroUsuario;
import tcc.monitorycardio.com.monitorycardio.DAO.ConfiguracaoFirebase;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String endereco;
    private String CEP;
    private String CPF;
    private String telefone;
    private String email;
    private String senha;


    private List<Dispositivo> dispositivos;
    private Dispositivo dispositivo;

    private List<Relatorio> relatorios;
    private Relatorio relatorio;

    private List<Dependente> dependentes;
    private Dependente dependente;

    private CadastroFilho cadastroFilho;

    private CadastroUsuario cadastroUsuario;


    public Usuario (){
        dispositivos = new ArrayList<> ();
        relatorios = new ArrayList<> ();
        dependentes = new ArrayList<> (  );
    }


    public  void salvar(){

        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase ();
        /*Cada "CHILD" é o nó dentro do firebase*/
        referenciaFirebase.child ( "usuario" ).child ( getId ()).setValue ( this );

    }



    /*Anotação "@Exclude" para não aparecer no banco de dados as informações desejadas*/

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public List<Dispositivo> getDispositivos() {
        return dispositivos;
    }

    public void setDispositivos(List<Dispositivo> dispositivos) {
        this.dispositivos = dispositivos;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public CadastroUsuario getCadastroUsuario() {
        return cadastroUsuario;
    }

    public List<Relatorio> getRelatorios() {
        return relatorios;
    }

    public void setRelatorios(List<Relatorio> relatorios) {
        this.relatorios = relatorios;
    }

    public Relatorio getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(Relatorio relatorio) {
        this.relatorio = relatorio;
    }


    public List<Dependente> getDependentes() {
        return dependentes;
    }

    public void setDependentes(List<Dependente> dependentes) {
        this.dependentes = dependentes;
    }

    public Dependente getDependente() {
        return dependente;
    }

    public void setDependente(Dependente dependente) {
        this.dependente = dependente;
    }

    public void setCadastroUsuario(CadastroUsuario cadastroUsuario) {
        this.cadastroUsuario = cadastroUsuario;


    }
}
