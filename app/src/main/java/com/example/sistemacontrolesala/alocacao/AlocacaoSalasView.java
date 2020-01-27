package com.example.sistemacontrolesala.alocacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.sistemacontrolesala.CadastroAlocacao;
import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.listaSalas.ListaSala;
import com.example.sistemacontrolesala.listaSalas.ListaSalaDao;
import com.example.sistemacontrolesala.listaSalas.ListaSalasAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AlocacaoSalasView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alocacao_salas);

        setTitle("Alocações");

        ListView listAlocacao= findViewById(R.id.alocacaoSalaListView);
        List<Alocacao> listaAlocacao= new AlocacaoDao().listaAlocacao();
        listAlocacao.setAdapter(new AlocacaoAdapter(listaAlocacao, this));

        FloatingActionButton btnCadastroAlocacao = findViewById(R.id.floatingActionButton);
        btnCadastroAlocacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AlocacaoSalasView.this, CadastroAlocacao.class));
            }
        });
    }
}
