package com.example.sistemacontrolesala;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemacontrolesala.listaSalas.ListaSalasView;
import com.example.sistemacontrolesala.usuario.CadastroUsuario;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button btnEntrar;
    private TextView txtTituloMain, txtCadastrar;
    private EditText editEmail, editSenha;
    private ImageView imgIcon;
    private ProgressBar carregando;

    private SharedPreferences pref;

    private int idUsuario;
    private String nomeUsuario;
    private String emailUsuario;

    private String nomeOrganizacao;
    private String tipoOrganizacao;
    private int idOrganizacao;

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

        carregando = findViewById(R.id.carregando);
        imgIcon = findViewById(R.id.imgIcon);
        txtTituloMain = findViewById(R.id.txtTituloMain);
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
        String senha = editPassword.getText().toString();

        if (email.isEmpty()) {
            editEmail.setError("Digite um email");
        } else if (!email.contains("@") || !email.contains(".")) {
            editEmail.setError("Digite um email valido");
        } else if (senha.isEmpty()) {
            editPassword.setError("Digite uma senha");
        } else {
            verificaLogin(email, senha);
        }
    }

    private void verificaLogin(String email, String senha) {
        String resultAuth;
        try {
            componentesGone(false, View.VISIBLE, View.GONE);

            resultAuth = new LoginUsuarioService().execute(email, senha).get();

            if (resultAuth.equals("Servidor nao responde")) {
                Toast.makeText(this, "Servidor nao responde", Toast.LENGTH_SHORT).show();

                componentesGone(true, View.GONE, View.VISIBLE);
            } else {
                if (resultAuth.contains("Senha incorreta")) {
                    Toast.makeText(getApplicationContext(), "Senha incorreta", Toast.LENGTH_SHORT).show();
                    componentesGone(true, View.GONE, View.VISIBLE);
                    return;
                }

                if (resultAuth.contains("Usuário não encontrado")) {
                    componentesGone(true, View.GONE, View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Usuario nao cadastrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "Login realizado com sucesso", Toast.LENGTH_SHORT).show();

                JSONObject usuarioJSON = new JSONObject(resultAuth);
                if (usuarioJSON.has("email") && usuarioJSON.has("id") && usuarioJSON.has("nome") && usuarioJSON.has("idOrganizacao")) {
                    pegaDadosDoWebService(usuarioJSON);
                    salvaAtributosDoUsuarioEOrganizacao(idUsuario, nomeUsuario, emailUsuario, nomeOrganizacao, tipoOrganizacao, idOrganizacao);

                    startActivity(new Intent(MainActivity.this, ListaSalasView.class));
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Servidor nao responde", Toast.LENGTH_SHORT).show();
            componentesGone(true, View.GONE, View.VISIBLE);
        }
    }

    private void componentesGone(boolean b, int visible, int gone) {
        btnEntrar.setEnabled(b);
        carregando.setVisibility(visible);
        imgIcon.setVisibility(gone);
        txtTituloMain.setVisibility(gone);
        editEmail.setVisibility(gone);
        editSenha.setVisibility(gone);
        btnEntrar.setVisibility(gone);
        txtCadastrar.setVisibility(gone);
    }

    private void pegaDadosDoWebService(JSONObject usuarioJSON) throws JSONException {
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