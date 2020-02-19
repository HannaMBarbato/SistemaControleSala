package com.example.sistemacontrolesala;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemacontrolesala.listaSalas.ListaSalasView;
import com.example.sistemacontrolesala.usuario.CadastroUsuario;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button btnEntrar;
    private TextView txtCadastrar;
    private SharedPreferences pref;

    private int idUsuario;
    private String nomeUsuario;
    private String emailUsuario;

    private String nomeOrganizacao;
    private String tipoOrganizacao;
    private int idOrganizacao;

    private EditText editEmail;
    private EditText editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences("USER_DATA", 0);
        if (pref.contains("userEmail")) {
            startActivity(new Intent(MainActivity.this, ListaSalasView.class));
            finish();
        } else {
            setContentView(R.layout.activity_main);

            inicializaComponentesDaTela();
            configuraComponentesDaTela(editEmail, editSenha);
        }
    }

    private void inicializaComponentesDaTela() {
        editEmail = findViewById(R.id.editEmailLogin);
        editSenha = findViewById(R.id.editSenhaLogin);
        btnEntrar = findViewById(R.id.btnEntrar);
        txtCadastrar = findViewById(R.id.txtCadastrarMain);
    }

    private void configuraComponentesDaTela(final EditText editEmail, final EditText editSenha) {
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaDadosDoLogin(editEmail, editSenha);
            }
        });

        txtCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastroUsuario.class));
                finish();
            }
        });
    }

    private void validaDadosDoLogin(EditText editEmail, EditText editPassword) {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.isEmpty()) {
            editEmail.setError("Digite um email");
        } else if (password.isEmpty()) {
            editPassword.setError("Digite uma senha");
        } else {
            verificaLogin(email, password);
        }
    }

    private void verificaLogin(String email, String password) {
        String resultAuth;
        try {
            resultAuth = new LoginUsuarioService().execute(email, password).get();
            if (resultAuth.length() > 0) {
                Toast.makeText(getApplicationContext(), "Login realizado com sucesso", Toast.LENGTH_SHORT).show();

                JSONObject usuarioJSON = new JSONObject(resultAuth);

                if (usuarioJSON.has("email") && usuarioJSON.has("id") && usuarioJSON.has("nome") && usuarioJSON.has("idOrganizacao")) {

                    retornaDadosDoWebService(usuarioJSON);
                    salvaAtributosDoUsuarioEOrganizacao(idUsuario, nomeUsuario, emailUsuario, nomeOrganizacao, tipoOrganizacao, idOrganizacao);

                    startActivity(new Intent(MainActivity.this, ListaSalasView.class));
                    finish();
                }
            } else {
                Toast.makeText(MainActivity.this, "Usuario nao cadastrado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void retornaDadosDoWebService(JSONObject usuarioJSON) throws JSONException {
        idUsuario = usuarioJSON.getInt("id");
        nomeUsuario = usuarioJSON.getString("nome");
        emailUsuario = usuarioJSON.getString("email");

        JSONObject organizacaoJson = usuarioJSON.getJSONObject("idOrganizacao");

        nomeOrganizacao = organizacaoJson.getString("nome");
        tipoOrganizacao = organizacaoJson.getString("tipoOrganizacao");
        idOrganizacao = organizacaoJson.getInt("id");
    }

    private void salvaAtributosDoUsuarioEOrganizacao(int idUsuario, String nomeUsuario, String emailUsuario, String nomeOrganizacao, String tipoOrganizacao, int idOrganizacao) {
        pref = getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("userEmail", emailUsuario);
        editor.putString("userName", nomeUsuario);
        editor.putString("userId", Integer.toString(idUsuario));
        editor.putString("userNomeOrganizacao", nomeOrganizacao);
        editor.putString("userTipoOrganizacao", tipoOrganizacao);
        editor.putString("userIdOrganizacao", Integer.toString(idOrganizacao));
        editor.commit();
    }
}