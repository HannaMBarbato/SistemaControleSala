package com.example.sistemacontrolesala.alocacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemacontrolesala.R;

import java.text.DateFormat;
import java.util.Date;

public class CadastroAlocacao extends AppCompatActivity {


    public static TextView stringData;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_alocacao);

        pref = getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = pref.edit();

        TextView organizador = findViewById(R.id.cadastroAlocacaoTxtOrganizador);
        organizador.setText("Organizador: " + pref.getString("userName", null));

        stringData = findViewById(R.id.cadastroAlocacaoTxtData);

        Intent recebedora = getIntent();
        Bundle parametros = recebedora.getExtras();

        if (parametros != null) {
            String data = parametros.getString("Data");
            stringData.setText(data);
        } else {
            Toast.makeText(CadastroAlocacao.this, "Data nula", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CadastroAlocacao.this, AlocacaoSalasView.class));
        finish();
    }
}
