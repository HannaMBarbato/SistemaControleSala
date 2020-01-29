package com.example.sistemacontrolesala.listaSalas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.sistemacontrolesala.MainActivity;
import com.example.sistemacontrolesala.alocacao.AlocacaoSalasView;
import com.example.sistemacontrolesala.R;

import java.util.List;

public class ListaSalasView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_salas);
        setTitle("Salas");

        ListView listSalas = findViewById(R.id.listaSalaListView);
        List<ListaSala> listaSalas = new ListaSalaDao().listaSalas();
        listSalas.setAdapter(new ListaSalasAdapter(listaSalas, this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ListaSalasView.this, MainActivity.class));
        finish();
    }

    public void clickCardView(View view) {
        startActivity(new Intent(ListaSalasView.this, AlocacaoSalasView.class));
        finish();
    }
}
