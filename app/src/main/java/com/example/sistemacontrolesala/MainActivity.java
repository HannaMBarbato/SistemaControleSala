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

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button btnEntrar;
    private TextView txtCadastrar;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences("USER_DATA", 0);
        if (pref.contains("userEmail")) {
            startActivity(new Intent(MainActivity.this, ListaSalasView.class));
            finish();
        } else {
            setContentView(R.layout.activity_main);

            final EditText editEmail = findViewById(R.id.editEmailLogin);
            final EditText editPassword = findViewById(R.id.editSenhaLogin);

            btnEntrar = findViewById(R.id.btnEntrar);
            btnEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String resultAuth = null;
                    String email = editEmail.getText().toString();
                    String password = editPassword.getText().toString();
                    try {
                        resultAuth = new LoginUsuarioService().execute(email, password).get();

                        if (resultAuth.length() > 0) {
                            Toast.makeText(getApplicationContext(), "Login realizado com sucesso", Toast.LENGTH_LONG).show();

                            JSONObject usuarioJSON = new JSONObject(resultAuth);

                            if (usuarioJSON.has("email") && usuarioJSON.has("id") && usuarioJSON.has("nome") && usuarioJSON.has("idOrganizacao")) {
                                int id = usuarioJSON.getInt("id");
                                String nome = usuarioJSON.getString("nome");
                                String emailJson = usuarioJSON.getString("email");

                                JSONObject organizacaoJson = usuarioJSON.getJSONObject("idOrganizacao");
                                String nomeOrganizacao = organizacaoJson.getString("nome");
                                String tipoOrganizacao = organizacaoJson.getString("tipoOrganizacao");
                                int idOrganizacao = organizacaoJson.getInt("id");


                                pref = getSharedPreferences("USER_DATA", 0);
                                SharedPreferences.Editor editor = pref.edit();

                                editor.putString("userEmail", emailJson);
                                editor.putString("userName", nome);
                                editor.putString("userId", Integer.toString(id));
                                editor.putString("userNomeOrganizacao", nomeOrganizacao);
                                editor.putString("userTipoOrganizacao", tipoOrganizacao);
                                editor.putString("userIdOrganizacao", Integer.toString(idOrganizacao));
                                editor.commit();

                                System.out.println(pref.getString("userEmail", null));
                                System.out.println(pref.getString("userName", null));
                                System.out.println(pref.getString("userId", null));
                                System.out.println(pref.getString("userNomeOrganizacao", null));
                                System.out.println(pref.getString("userTipoOrganizacao", null));
                                System.out.println(pref.getString("userIdOrganizacao", null));

                                startActivity(new Intent(MainActivity.this, ListaSalasView.class));
                                finish();

                            }
                        } else {
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
    }
}
