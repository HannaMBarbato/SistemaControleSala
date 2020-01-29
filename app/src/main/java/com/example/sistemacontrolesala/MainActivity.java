package com.example.sistemacontrolesala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemacontrolesala.listaSalas.ListaSalasView;
import com.example.sistemacontrolesala.usuario.CadastroUsuario;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btnEntrar;
    private TextView txtCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editEmail = findViewById(R.id.editEmailLogin);
        final EditText editPassword = findViewById(R.id.editSenhaLogin);

        btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultAuth = null;
                final String email = editEmail.getText().toString();
                final String password = editPassword.getText().toString();
                try {
                    resultAuth = new Verificador().execute(email, password).get();

                    if (resultAuth.equals("Login efetuado com sucesso!")) {
                        startActivity(new Intent(MainActivity.this, ListaSalasView.class));
                        finish();
                    }else {
                        Toast.makeText(MainActivity.this, "errooou", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        txtCadastrar = findViewById(R.id.txtCadastrarMain);
        txtCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastroUsuario.class));
                finish();
            }
        });
    }

    public class Verificador extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String urlWS = "http://172.30.248.117:8080/ReservaDeSala/rest/usuario/login/";

            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urlWS);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("authorization", "secret");
                conn.setRequestProperty("email", strings[0]);
                conn.setRequestProperty("password", strings[1]);
                conn.setConnectTimeout(2000);

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();
        }
    }
}
