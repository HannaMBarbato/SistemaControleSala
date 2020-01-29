package com.example.sistemacontrolesala.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sistemacontrolesala.MainActivity;
import com.example.sistemacontrolesala.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CadastroUsuario extends AppCompatActivity {

    private EditText editNome, editEmail, editSenha;
    private Button btnCadastroUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        editNome = findViewById(R.id.editNomeCadastro);
        editEmail = findViewById(R.id.editEmailCadastro);
        editSenha = findViewById(R.id.editSenhaCadastro);

        btnCadastroUsuario = findViewById(R.id.btnCadastrarUsuario);
        btnCadastroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultAuth = "";

                String nome = editNome.getText().toString();
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                JSONObject usuarioJson = new JSONObject();

                try {
                    usuarioJson.put("nome", nome);
                    usuarioJson.put("email", email);
                    usuarioJson.put("senha", senha);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CadastroUsuario.this, "Houve algum erro", Toast.LENGTH_LONG).show();
                }

                try {
                    String novoUsuarioEncoded = Base64.encodeToString(usuarioJson.toString().getBytes("UTF-8"), Base64.NO_WRAP);

                    resultAuth = new CadastroUsuarioConexao().execute(novoUsuarioEncoded).get();
                    if (resultAuth.equals("Usuário criado com sucesso")) {
                        Toast.makeText(CadastroUsuario.this, "Cadastro efetuado com sucesso", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CadastroUsuario.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(CadastroUsuario.this, "Erroooou", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class CadastroUsuarioConexao extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String urlWS = "http://172.30.248.117:8080/ReservaDeSala/rest/usuario/cadastro/";

            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urlWS);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("authorization", "secret");
                conn.setRequestProperty("novoUsuario", strings[0]);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CadastroUsuario.this, MainActivity.class));
        finish();
    }
}
