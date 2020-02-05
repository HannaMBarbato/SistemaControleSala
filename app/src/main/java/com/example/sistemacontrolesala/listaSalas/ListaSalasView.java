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

import com.example.sistemacontrolesala.MainActivity;
import com.example.sistemacontrolesala.R;

import java.util.ArrayList;
import java.util.List;

public class ListaSalasView extends AppCompatActivity {

    List<ListaSala> listaSalasViewPager;
    List<ListaSala> listaSalasListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_salas);
        setTitle("Salas");

        listaSalasListView = new ArrayList<>();
        listaSalasListView.add(new ListaSala(R.drawable.foz_do_iguacu_pr, "Brochure","20"));
        listaSalasListView.add(new ListaSala(R.drawable.belo_horizonte_mg, "Sticker","6"));
        listaSalasListView.add(new ListaSala(R.drawable.belo_horizonte_mg, "Sticker","8"));
        listaSalasListView.add(new ListaSala(R.drawable.belo_horizonte_mg, "Sticker","10"));

        ListView listSalas = findViewById(R.id.listaSalaListView);
        listSalas.setAdapter(new ListaSalasAdapter(listaSalasListView, this));
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
