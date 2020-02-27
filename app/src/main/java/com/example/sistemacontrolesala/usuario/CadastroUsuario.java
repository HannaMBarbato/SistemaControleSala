package com.example.sistemacontrolesala.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemacontrolesala.MainActivity;
import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.organizacao.Organizacao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        inicializaComponentesDaTela();
        configuraSpinner();

        btnCadastroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificaDadosDeCadastroUsuario();
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
                                    String listaOrganizacao = new IdOrganizacaoParaCadastroUsuarioService().execute(dominio).get();
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

    private void verificaDadosDeCadastroUsuario() {
        String nome = editNome.getText().toString();
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        if(nome.isEmpty()){
            editNome.setError("Digite um nome");
        }else if (email.isEmpty()) {
            editEmail.setError("Digite um email");
        } else if (!email.contains("@") && email.contains(".")) {
            editEmail.setError("Digite um email valido");
        } else if (senha.isEmpty()) {
            editSenha.setError("Digite uma senha");
        } else {
            cadastraUsuario(nome, email, senha);
        }
    }

    private void cadastraUsuario(String nome, String email, String senha) {
        String resultAuth;
        JSONObject usuarioJson = new JSONObject();

        try {
            usuarioJson.put("nome", nome);
            usuarioJson.put("email", email);
            usuarioJson.put("senha", senha);
            usuarioJson.put("idOrganizacao", idOrganizacao);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(CadastroUsuario.this, "Erro ao atualizar dados do usuario", Toast.LENGTH_LONG).show();
        }

        try {
            String novoUsuarioEncoded = Base64.encodeToString(usuarioJson.toString().getBytes("UTF-8"), Base64.NO_WRAP);

            resultAuth = new CadastroUsuarioService().execute(novoUsuarioEncoded).get();
            if (resultAuth.equals("Usuário criado com sucesso")) {
                Toast.makeText(CadastroUsuario.this, "Cadastro efetuado com sucesso", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CadastroUsuario.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(CadastroUsuario.this, "Erro ao cadastrar usuario", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configuraSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idOrganizacao = listOrganizacao.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void inicializaComponentesDaTela() {
        spinner = findViewById(R.id.spinner);
        editNome = findViewById(R.id.editNomeCadastro);
        editEmail = findViewById(R.id.editEmailCadastro);
        editSenha = findViewById(R.id.editSenhaCadastro);
        btnCadastroUsuario = findViewById(R.id.btnCadastrarUsuario);
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
}
