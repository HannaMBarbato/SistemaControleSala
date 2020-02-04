package com.example.sistemacontrolesala;

import android.content.Context;
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

public class MainActivity extends AppCompatActivity {

    private Button btnEntrar;
    private TextView txtCadastrar;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences("USER_DATA", 0);
        if(pref.contains("email")){
            startActivity(new Intent(MainActivity.this, ListaSalasView.class));
        }else {
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
                        resultAuth = new LoginUsuarioService().execute(email, password).get();

                        if (resultAuth.equals("Login efetuado com sucesso!")) {

                            pref = getSharedPreferences("USER_DATA", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email", email);
//                      editor.remove("email");
                            editor.commit();

                            String emailRecuperado;
                            emailRecuperado = pref.getString("email", null);
                            System.out.println("email recuperado: " + emailRecuperado);
                            startActivity(new Intent(MainActivity.this, ListaSalasView.class));
                            finish();
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
