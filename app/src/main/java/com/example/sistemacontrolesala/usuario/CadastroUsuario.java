package com.example.sistemacontrolesala.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemacontrolesala.FiltroDesativaEmoji;
import com.example.sistemacontrolesala.MainActivity;
import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.organizacao.Organizacao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CadastroUsuario extends AppCompatActivity {

    private EditText editNome, editEmail, editSenha;
    private Button btnCadastroUsuario;
    private Spinner spinner;
    private int idOrganizacao;

    private List<String> nomesOrganizacoes = new ArrayList<>();
    private List<Organizacao> listOrganizacao = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        inicializaComponentesDaTela();
        setVisibilidadeBtnCadastrarUsuarioESpinner();
        configuraBtnCadastrarUsuario();
        configuraFocoDoEditEmail();
    }

    private void setVisibilidadeBtnCadastrarUsuarioESpinner() {
        btnCadastroUsuario.setEnabled(false);
        btnCadastroUsuario.setBackgroundResource(R.drawable.botao_customizado_enable);
        spinner.setVisibility(View.GONE);
    }

    private void configuraFocoDoEditEmail() {
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
                                    String listaOrganizacao;
                                    listaOrganizacao = new IdOrganizacaoParaCadastroUsuarioService().execute(dominio).get();
                                    if (listaOrganizacao.equals("Servidor nao responde")) {
                                        Toast.makeText(CadastroUsuario.this, "Servidor nao responde", Toast.LENGTH_SHORT).show();
                                    } else {
                                        JSONArray arrayOrganizacoes = new JSONArray(listaOrganizacao);

                                        listOrganizacao.clear();
                                        if (arrayOrganizacoes.length() > 0) {
                                            converteJsonParaAddOrganizacaoNaLista(arrayOrganizacoes);
                                            listaDeOrganizacoesParaSpinner();
                                            configuraAdapterDoSpinner();
                                        } else {
                                            acaoParaDominioDoEmailErrado();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(CadastroUsuario.this, "Servidor nao responde", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    configuraSpinner();
                } else {
                    setVisibilidadeBtnCadastrarUsuarioESpinner();
                }
            }
        });
    }

    private void converteJsonParaAddOrganizacaoNaLista(JSONArray arrayOrganizacoes) throws JSONException {
        for (int i = 0; i < arrayOrganizacoes.length(); i++) {
            JSONObject object = arrayOrganizacoes.getJSONObject(i);
            Organizacao organizacao = pegaInformacoesOrganizacao(object);
            listOrganizacao.add(organizacao);
        }
    }

    private Organizacao pegaInformacoesOrganizacao(JSONObject objetoOrganizacao) throws JSONException {
        try
        {
            int id = 0;
            String nome = "";
            String tipoOrganizacao = "";
            if (objetoOrganizacao.has("id") && objetoOrganizacao.has("nome") && objetoOrganizacao.has("tipoOrganizacao")) {
                id = objetoOrganizacao.getInt("id");
                nome = objetoOrganizacao.getString("nome");
                tipoOrganizacao = objetoOrganizacao.getString("tipoOrganizacao");
            }
            return criarOrganizacao(id, nome, tipoOrganizacao);
        }
        catch (Exception e)
        {
            Toast.makeText(CadastroUsuario.this, "", Toast.LENGTH_SHORT);
            e.printStackTrace();
            return null;
        }
    }

    private Organizacao criarOrganizacao(int id, String nome, String tipoOrganizacao) {
        Organizacao novaOrganizacao = new Organizacao();
        novaOrganizacao.setId(id);
        novaOrganizacao.setNome(nome);
        novaOrganizacao.setTipoOrganizacao(tipoOrganizacao);
        return novaOrganizacao;
    }

    private void configuraBtnCadastrarUsuario() {
        btnCadastroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificaDadosDeCadastroUsuario(editNome, editEmail, editSenha);
            }
        });
    }

    private void verificaDadosDeCadastroUsuario(EditText editNome, EditText editEmail, EditText editSenha) {
        String nome = editNome.getText().toString();
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        if (nome.isEmpty()) {
            editNome.setError("Digite um nome");
        } else if (email.isEmpty()) {
            editEmail.setError("Digite um email");
        } else if (!email.contains("@") || !email.contains(".") || email.contains(" ")) {
            editEmail.setError("Digite um email valido");
        } else if (senha.isEmpty()) {
            editSenha.setError("Digite uma senha");
        } else {
            cadastraUsuario(nome, email, senha);
        }
    }

    private void cadastraUsuario(String nome, String email, String senha) {
        String resultAuth;
        JSONObject usuarioJson = retornaUsuarioEmJsonObject(nome, email, senha);

        try {
            String novoUsuarioEncoded = Base64.encodeToString(usuarioJson.toString().getBytes("UTF-8"), Base64.NO_WRAP);

            resultAuth = new CadastroUsuarioService().execute(novoUsuarioEncoded).get();
            tratarRespostaDoServidor(resultAuth);
        } catch (Exception e) {
            Toast.makeText(CadastroUsuario.this, "Servidor nao responde", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    private JSONObject retornaUsuarioEmJsonObject(String nome, String email, String senha) {
        JSONObject usuarioJson = new JSONObject();

        try {
            usuarioJson.put("nome", nome);
            usuarioJson.put("email", email);
            usuarioJson.put("senha", senha);
            usuarioJson.put("idOrganizacao", idOrganizacao);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(CadastroUsuario.this, "Erro ao cadastrar dados do usuario", Toast.LENGTH_LONG).show();
        }
        return usuarioJson;
    }

    private void tratarRespostaDoServidor(String resultAuth) {
        if (resultAuth.equals("Servidor nao responde")) {
            Toast.makeText(this, "Servidor nao responde", Toast.LENGTH_SHORT).show();
        } else {
            if(resultAuth.equals("O email informado já está cadastrado")){
                editEmail.setError("Email informado ja existe");
            }else if (resultAuth.equals("Usuário criado com sucesso")) {
                acaoParaCadastroEfetuadoComSucesso();
            } else {
                Toast.makeText(CadastroUsuario.this, "Erro ao cadastrar usuario", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void acaoParaCadastroEfetuadoComSucesso() {
        btnCadastroUsuario.setEnabled(false);
        Toast.makeText(CadastroUsuario.this, "Cadastro efetuado com sucesso", Toast.LENGTH_LONG).show();
        startActivity(new Intent(CadastroUsuario.this, MainActivity.class));
        finish();
    }

    private void listaDeOrganizacoesParaSpinner() {
        nomesOrganizacoes.clear();
        nomesOrganizacoes.add("- Selecione uma organização -");
        if (nomesOrganizacoes.size() > 0) {
            for (int i = 0; i < listOrganizacao.size(); i++) {
                nomesOrganizacoes.add(listOrganizacao.get(i).getNome());
            }
        }
    }

    private void configuraAdapterDoSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CadastroUsuario.this, android.R.layout.simple_spinner_item, nomesOrganizacoes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
    }

    private void acaoParaDominioDoEmailErrado() {
        Toast.makeText(CadastroUsuario.this, "Nao existe empresas com o dominio do seu email", Toast.LENGTH_LONG).show();
        editEmail.setError("Digite um email valido");
        setVisibilidadeBtnCadastrarUsuarioESpinner();
    }


    private void configuraSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    btnCadastroUsuario.setEnabled(false);
                    btnCadastroUsuario.setBackgroundResource(R.drawable.botao_customizado_enable);
                }
                if (position > 0) {
                    idOrganizacao = listOrganizacao.get(position - 1).getId();
                    btnCadastroUsuario.setEnabled(true);
                    btnCadastroUsuario.setBackgroundResource(R.drawable.botao_customizado);
                }
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

        desabilitaEmojiDoTeclado();
    }

    private void desabilitaEmojiDoTeclado() {
        editNome.setFilters(new InputFilter[]{new FiltroDesativaEmoji()});
        editEmail.setFilters(new InputFilter[]{new FiltroDesativaEmoji()});
        editSenha.setFilters(new InputFilter[]{new FiltroDesativaEmoji()});
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CadastroUsuario.this, MainActivity.class));
        finish();
    }

}