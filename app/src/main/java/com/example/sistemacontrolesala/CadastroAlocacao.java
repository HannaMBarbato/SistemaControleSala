package com.example.sistemacontrolesala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.sistemacontrolesala.alocacao.AlocacaoSalasView;
import com.example.sistemacontrolesala.listaSalas.ListaSalasView;

public class CadastroAlocacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_alocacao);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CadastroAlocacao.this, AlocacaoSalasView.class));
        finish();
    }
}
