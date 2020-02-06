package com.example.sistemacontrolesala.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sistemacontrolesala.MainActivity;
import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.organizacao.Organizacao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CadastroUsuario extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editNome, editEmail, editSenha;
    private Button btnCadastroUsuario;
    private Spinner spinner;
    private int idOrganizacao;
    private List<Organizacao> listOrganizacao = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idOrganizacao = listOrganizacao.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
                    usuarioJson.put("idOrganizacao", idOrganizacao);
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
                        Toast.makeText(CadastroUsuario.this, "Erro ao criar usuario", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String emailAfterTextChange = editEmail.getText().toString();
                    if (emailAfterTextChange.contains("@")) {
                        String[] emailCompleto = emailAfterTextChange.split("@");
                        if (emailCompleto.length > 1) {
                            String dominio = emailCompleto[1];
                            if (dominio.contains(".")) {
                                try {
                                    String listaOrganizacao = new IdOrganizacaoCadastroUsuarioService().execute(dominio).get();
                                    System.out.println("dominio: " + dominio);
                                    System.out.println("empresa " + listaOrganizacao);

                                    JSONArray arrayOrganizacoes = new JSONArray(listaOrganizacao);

                                    List<String> listaDeStrings = new ArrayList<>();
                                    if (arrayOrganizacoes.length() > 0) {
                                        for (int i = 0; i < arrayOrganizacoes.length(); i++) {
                                            JSONObject objetoOrganizacao = arrayOrganizacoes.getJSONObject(i);
                                            if (objetoOrganizacao.has("id") && objetoOrganizacao.has("nome") && objetoOrganizacao.has("tipoOrganizacao")) {
                                                int id = objetoOrganizacao.getInt("id");
                                                String nome = objetoOrganizacao.getString("nome");
                                                String tipoOrganizacao = objetoOrganizacao.getString("tipoOrganizacao");

                                                Organizacao novaOrganizacao = new Organizacao();
                                                novaOrganizacao.setId(id);
                                                novaOrganizacao.setNome(nome);
                                                novaOrganizacao.setTipoOrganizacao(tipoOrganizacao);

                                                listOrganizacao.add(novaOrganizacao);
                                                listaDeStrings.add(novaOrganizacao.getNome());
                                            }
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CadastroUsuario.this, android.R.layout.simple_spinner_item, listaDeStrings);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner.setAdapter(adapter);
                                        spinner.setVisibility(View.VISIBLE);
                                    } else {
                                        //nada
                                    }
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CadastroUsuario.this, MainActivity.class));
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
}
