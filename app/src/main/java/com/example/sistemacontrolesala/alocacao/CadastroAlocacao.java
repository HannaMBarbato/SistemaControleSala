package com.example.sistemacontrolesala.alocacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemacontrolesala.R;

import java.text.DateFormat;
import java.util.Date;

public class CadastroAlocacao extends AppCompatActivity {


    public static TextView stringData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_alocacao);

        stringData = findViewById(R.id.cadastroAlocacaoTxtData);

        Intent recebedora = getIntent();
        Bundle parametros = recebedora.getExtras();

        if (parametros != null) {
            String data = parametros.getString("Data");
            stringData.setText(data);



        } else {
            Toast.makeText(CadastroAlocacao.this, "ta nulo Hanna", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CadastroAlocacao.this, AlocacaoSalasView.class));
        finish();
    }
}
