package com.example.sistemacontrolesala.alocacao;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemacontrolesala.MainActivity;
import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.usuario.CadastroUsuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CadastroAlocacao extends AppCompatActivity implements View.OnClickListener {

    public TextView stringData;
    SharedPreferences pref;

    private EditText editDescricao;
    private TextView txtHoraInicio, txtHoraFim;
    Context context = this;

    private int idSala, idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_alocacao);

        getPrefNomeOrganizador();
        getDataDaActivityAlocacao();

        txtHoraInicio = findViewById(R.id.cadastroAlocacaoTxtHoraInicio);
        txtHoraFim = findViewById(R.id.cadastroAlocacaoTxtHoraFim);
        txtHoraInicio.setOnClickListener(this);
        txtHoraFim.setOnClickListener(this);

        editDescricao = findViewById(R.id.cadastroAlocacaoEditDesc);

        Button btnSalvar = findViewById(R.id.cadastroAlocacaoBtnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultAuth = "";

                String descricao = editDescricao.getText().toString();
                String horaInicio = txtHoraInicio.getText().toString();
                String horaFim = txtHoraFim.getText().toString();

                JSONObject reservaSalaJson = new JSONObject();

                try {
                    reservaSalaJson.put("idSala", idSala);
                    reservaSalaJson.put("idUsuario", idUsuario);
                    reservaSalaJson.put("descricao", descricao);
                    //datas ficam em strings
                    reservaSalaJson.put("dataHoraInicio", horaInicio);
                    reservaSalaJson.put("dataHoraFim", horaFim);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CadastroAlocacao.this, "Erro ao inserir dados da reserva", Toast.LENGTH_LONG).show();
                }

                try {
                    String novaReserva = Base64.encodeToString(reservaSalaJson.toString().getBytes("UTF-8"), Base64.NO_WRAP);

                    resultAuth = new AlocacaoService().execute(novaReserva).get();
                    if (resultAuth.equals("Reserva realizada com sucesso")) {
                        Toast.makeText(CadastroAlocacao.this, "Reserva efetuada com sucesso", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CadastroAlocacao.this, AlocacaoSalasView.class));
                        finish();
                    } else {
                        Toast.makeText(CadastroAlocacao.this, "Erro ao reservar sala", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(CadastroAlocacao.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        final SimpleDateFormat formataHora = new SimpleDateFormat("HH:mm a", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        if (view == txtHoraInicio) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    getHora(hourOfDay, minute, calendar, formataHora, txtHoraInicio);
                }
            }, hour, minute, android.text.format.DateFormat.is24HourFormat(context));
            timePickerDialog.show();
        }

        if (view == txtHoraFim) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    getHora(hourOfDay, minute, calendar, formataHora, txtHoraFim);
                }
            }, hour, minute, android.text.format.DateFormat.is24HourFormat(this));
            timePickerDialog.show();
        }
    }

    private void getHora(int hourOfDay, int minute, Calendar calendar, SimpleDateFormat formataHora, TextView hora) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        hora.setText(formataHora.format(calendar.getTime()));
    }

    private void getDataDaActivityAlocacao() {
        stringData = findViewById(R.id.cadastroAlocacaoTxtData);
        Intent recebedora = getIntent();
        Bundle parametros = recebedora.getExtras();
        if (parametros != null) {
            String data = parametros.getString("DataStr");
            long dateLong = parametros.getLong("Date");
            System.out.println("DATE LONG " +dateLong);
            stringData.setText(data);
        } else {
            Toast.makeText(CadastroAlocacao.this, "Data nula", Toast.LENGTH_LONG).show();
        }
    }

    private void getPrefNomeOrganizador() {
        pref = getSharedPreferences("USER_DATA", 0);
        final SharedPreferences.Editor editor = pref.edit();
        TextView organizador = findViewById(R.id.cadastroAlocacaoTxtOrganizador);
        organizador.setText("Organizador: " + pref.getString("userName", null));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CadastroAlocacao.this, AlocacaoSalasView.class));
        finish();
    }
}
