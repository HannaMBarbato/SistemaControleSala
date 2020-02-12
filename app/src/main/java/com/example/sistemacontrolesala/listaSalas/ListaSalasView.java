package com.example.sistemacontrolesala.listaSalas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sistemacontrolesala.MainActivity;
import com.example.sistemacontrolesala.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaSalasView extends AppCompatActivity {

    List<Sala> salasListView = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_salas);

        Toolbar toolbar = findViewById(R.id.toolbarActivityListaSala);
        setSupportActionBar(toolbar);

        try {
            SharedPreferences pref = getSharedPreferences("USER_DATA", 0);
            String idOrganizacaoRecuperado = pref.getString("userIdOrganizacao", null);

            String listaSalasString = new ListaSalasService().execute(idOrganizacaoRecuperado).get();
            if (listaSalasString.length() > 2) {
                JSONArray arraySalas = new JSONArray(listaSalasString);
                for (int i = 0; i < arraySalas.length(); i++) {
                    JSONObject objetoSalas = arraySalas.getJSONObject(i);
                    if (objetoSalas.has("nome") && objetoSalas.has("idOrganizacao") && objetoSalas.has("quantidadePessoasSentadas")) {
                        int id = objetoSalas.getInt("id");

                        String nome = objetoSalas.getString("nome");
                        int quantidadePessoasSentadas = objetoSalas.getInt("quantidadePessoasSentadas");
                        boolean multimidia = objetoSalas.getBoolean("possuiMultimidia");
                        boolean arCondicionado = objetoSalas.getBoolean("possuiArcon");
                        double areaSala = objetoSalas.getDouble("areaDaSala");
                        String localizacao = objetoSalas.getString("localizacao");

                        Sala novaSala = new Sala();
                        novaSala.setId(id);

                        novaSala.setNome(nome);
                        novaSala.setQuantidadePessoasSentadas(quantidadePessoasSentadas);
                        novaSala.setPossuiMultimidia(multimidia);
                        novaSala.setPossuiArcon(arCondicionado);
                        novaSala.setAreaDaSala(areaSala);
                        novaSala.setLocalizacao(localizacao);

                        salasListView.add(novaSala);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            SharedPreferences pref = getSharedPreferences("USER_DATA", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("userEmail");
            editor.remove("userName");
            editor.remove("userId");
            editor.remove("userNomeOrganizacao");
            editor.remove("userTipoOrganizacao");
            editor.remove("userIdOrganizacao");
            editor.commit();

            startActivity(new Intent(ListaSalasView.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
