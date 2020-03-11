package com.example.sistemacontrolesala.listaSalas;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sistemacontrolesala.MainActivity;
import com.example.sistemacontrolesala.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaSalasView extends AppCompatActivity {

    private List<Sala> salasListView = new ArrayList<>();
    private SharedPreferences pref;

    private String nome, localizacao;
    private int quantidadePessoasSentadas, id;
    private boolean multimidia, arCondicionado;
    private double areaSala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_salas);

        configuraToolbar();
        fazRequestParaWebService();
        configuraListView();
    }

    private void configuraToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarActivityListaSala);
        setSupportActionBar(toolbar);
    }

    private void fazRequestParaWebService() {
        try {
            String idOrganizacaoRecuperado = pegaIdOrganizacaoDoSharedPreferences();

            String resultAuth = new ListaSalasService().execute(idOrganizacaoRecuperado).get();

            if (resultAuth.equals("Servidor nao responde")) {
                Toast.makeText(this, "Servidor nao responde", Toast.LENGTH_SHORT).show();
            } else {
                verificaDadosDoServidor(resultAuth);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Servidor nao responde", Toast.LENGTH_SHORT).show();
        }
    }

    private String pegaIdOrganizacaoDoSharedPreferences() {
        pref = getSharedPreferences("USER_DATA", 0);
        return pref.getString("userIdOrganizacao", null);
    }

    private void verificaDadosDoServidor(String listaSalasString) throws JSONException {
        if (listaSalasString.length() > 2) {
            percorreJsonArrayEArmazenaEmJsonObject(listaSalasString);
        }
    }

    private void percorreJsonArrayEArmazenaEmJsonObject(String listaSalasString) throws JSONException {
        JSONArray arraySalas = new JSONArray(listaSalasString);
        for (int i = 0; i < arraySalas.length(); i++) {
            JSONObject objetoSalas = arraySalas.getJSONObject(i);
            verificaObjetoSalaContemInfos(objetoSalas);
        }
    }

    private void verificaObjetoSalaContemInfos(JSONObject objetoSalas) throws JSONException {
        if (objetoSalas.has("nome") && objetoSalas.has("idOrganizacao") && objetoSalas.has("quantidadePessoasSentadas")) {
            getDadosDoWebService(objetoSalas);

            Sala novaSala = criarNovaSala(id, nome, quantidadePessoasSentadas, multimidia, arCondicionado, areaSala, localizacao);
            salasListView.add(novaSala);

        }
    }

    private void getDadosDoWebService(JSONObject objetoSalas) throws JSONException {
        id = objetoSalas.getInt("id");
        nome = objetoSalas.getString("nome");
        quantidadePessoasSentadas = objetoSalas.getInt("quantidadePessoasSentadas");
        multimidia = objetoSalas.getBoolean("possuiMultimidia");
        arCondicionado = objetoSalas.getBoolean("possuiArcon");
        areaSala = objetoSalas.getDouble("areaDaSala");
        localizacao = objetoSalas.getString("localizacao");
    }

    private Sala criarNovaSala(int id, String nome, int quantidadePessoasSentadas, boolean multimidia, boolean arCondicionado, double areaSala, String localizacao) {
        Sala novaSala = new Sala();
        novaSala.setId(id);

        novaSala.setNome(nome);
        novaSala.setQuantidadePessoasSentadas(quantidadePessoasSentadas);
        novaSala.setPossuiMultimidia(multimidia);
        novaSala.setPossuiArcon(arCondicionado);
        novaSala.setAreaDaSala(areaSala);
        novaSala.setLocalizacao(localizacao);
        return novaSala;
    }

    private void configuraListView() {
        ListView listSalas = findViewById(R.id.listaSalaListView);
        listSalas.setAdapter(new ListaSalasListView(salasListView, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_suspenso, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_suspensoSair) {
            alertDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    private void alertDialog() {
        new AlertDialog.Builder(ListaSalasView.this)
                .setIcon(R.drawable.ic_logout)
                .setTitle("Vai sair?")
                .setMessage("Leve um casaco pode ficar frio lá fora.")
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeInfoDoSharedPreferences();
                        acaoParaLogout();
                    }
                }).setNegativeButton("NAO", null)
                .show();
    }

    private void removeInfoDoSharedPreferences() {
        pref = getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("userEmail");
        editor.remove("userName");
        editor.remove("userId");
        editor.remove("userNomeOrganizacao");
        editor.remove("userTipoOrganizacao");
        editor.remove("userIdOrganizacao");
        editor.commit();
    }

    private void acaoParaLogout() {
        Toast.makeText(ListaSalasView.this, "Hey, volte logo!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ListaSalasView.this, MainActivity.class));
        finish();
    }
}