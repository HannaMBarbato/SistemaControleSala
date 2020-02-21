package com.example.sistemacontrolesala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    private Button btnEntrar;
    private TextView txtTituloMain, txtCadastrar;
    private SharedPreferences pref;

    private int idUsuario;
    private String nomeUsuario;
    private String emailUsuario;

    private String nomeOrganizacao;
    private String tipoOrganizacao;
    private int idOrganizacao;

    private EditText editEmail, editSenha;

    private ProgressBar carregando;
    private ImageView imgIcon;

    boolean verificaNet;

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

        if (checkConnection(this) == false) {
            Toast.makeText(MainActivity.this, "Sem conexao com a Internet", Toast.LENGTH_LONG).show();
        }
        if (checkConnection(this) == true) {
            if (email.isEmpty()) {
                editEmail.setError("Digite um email");
            } else if (!email.contains("@") && email.contains(".")) {
                editEmail.setError("Digite um email valido");
            } else if (senha.isEmpty()) {
                editPassword.setError("Digite uma senha");
            } else {
                verificaLogin(email, senha);
            }
        }
    }


    private void verificaLogin(String email, String senha) {
        String resultAuth;
        try {
            btnEntrar.setEnabled(false);
            carregando.setVisibility(View.VISIBLE);
            imgIcon.setVisibility(View.GONE);
            txtTituloMain.setVisibility(View.GONE);
            editEmail.setVisibility(View.GONE);
            editSenha.setVisibility(View.GONE);
            btnEntrar.setVisibility(View.GONE);
            txtCadastrar.setVisibility(View.GONE);

            resultAuth = new LoginUsuarioService().execute(email, senha).get();

            if (resultAuth.length() > 0) {

                if (resultAuth.contains("Senha incorreta")) {
                    Toast.makeText(getApplicationContext(), "Senha incorreta", Toast.LENGTH_SHORT).show();
                    btnEntrar.setEnabled(true);
                    carregando.setVisibility(View.GONE);
                    imgIcon.setVisibility(View.VISIBLE);
                    txtTituloMain.setVisibility(View.VISIBLE);
                    editEmail.setVisibility(View.VISIBLE);
                    editSenha.setVisibility(View.VISIBLE);
                    btnEntrar.setVisibility(View.VISIBLE);
                    txtCadastrar.setVisibility(View.VISIBLE);
                    return;
                }

                Toast.makeText(getApplicationContext(), "Login realizado com sucesso", Toast.LENGTH_SHORT).show();

                JSONObject usuarioJSON = new JSONObject(resultAuth);

                if (usuarioJSON.has("email") && usuarioJSON.has("id") && usuarioJSON.has("nome") && usuarioJSON.has("idOrganizacao")) {

                    retornaDadosDoWebService(usuarioJSON);
                    salvaAtributosDoUsuarioEOrganizacao(idUsuario, nomeUsuario, emailUsuario, nomeOrganizacao, tipoOrganizacao, idOrganizacao);

                    startActivity(new Intent(MainActivity.this, ListaSalasView.class));
                    finish();
                }
            } else {
                btnEntrar.setEnabled(true);
                carregando.setVisibility(View.GONE);
                imgIcon.setVisibility(View.VISIBLE);
                txtTituloMain.setVisibility(View.VISIBLE);
                editEmail.setVisibility(View.VISIBLE);
                editSenha.setVisibility(View.VISIBLE);
                btnEntrar.setVisibility(View.VISIBLE);
                txtCadastrar.setVisibility(View.VISIBLE);
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

  /*  public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }*/

    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    //return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }


    /*public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_statut", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_statut", "" + e.getMessage());
                }
            }
        }
        Log.i("update_statut", "Network is available : FALSE ");
        return false;
    }*/

}
