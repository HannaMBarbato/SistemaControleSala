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

import com.example.sistemacontrolesala.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CadastroAlocacao extends AppCompatActivity implements View.OnClickListener {

    public TextView stringData;
    private SharedPreferences pref;
    private EditText editDescricao;
    private TextView txtHoraInicio, txtHoraFim;
    private Context context = this;
    private long dateInicio, dateFim, dateLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_alocacao);

        getPrefNomeOrganizador();
        getDataDaActivityAlocacao(dateLong);

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

                SharedPreferences pref = getSharedPreferences("USER_DATA", 0);
                
                int idSalaRecuperado = pref.getInt("idSala", 0);
                System.out.println("ID SALA CADASTRO " + idSalaRecuperado);

                String idUsuarioRecuperado = pref.getString("userId", null);
                System.out.println("ID USUARIO CADASTRO " + idUsuarioRecuperado);

                JSONObject reservaSalaJson = new JSONObject();
                try {
                    reservaSalaJson.put("id_sala", idSalaRecuperado);
                    reservaSalaJson.put("id_usuario", Integer.parseInt(idUsuarioRecuperado));
                    reservaSalaJson.put("descricao", descricao);
                    reservaSalaJson.put("data_hora_inicio", dateInicio);
                    reservaSalaJson.put("data_hora_fim", dateFim);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CadastroAlocacao.this, "Erro ao inserir dados da reserva", Toast.LENGTH_LONG).show();
                }

                try {
                    String novaReservaEncode = Base64.encodeToString(reservaSalaJson.toString().getBytes("UTF-8"), Base64.NO_WRAP);

                    resultAuth = new CadastroAlocacaoService().execute(novaReservaEncode).get();
                    if (resultAuth.equals("Reserva realizada com sucesso")) {
                        Toast.makeText(CadastroAlocacao.this, "Reserva efetuada com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CadastroAlocacao.this, "Erro ao reservar sala", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(CadastroAlocacao.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        final SimpleDateFormat formataHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getDataDaActivityAlocacao(dateLong));
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        if (view == txtHoraInicio) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    getHora(hourOfDay, minute, calendar, formataHora, txtHoraInicio);
                    dateInicio = calendar.getTime().getTime();
                    System.out.println("DATA E HORA INICIO LONG " + dateInicio);
                }
            }, hour, minute, android.text.format.DateFormat.is24HourFormat(context));
            timePickerDialog.show();
        }

        if (view == txtHoraFim) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    getHora(hourOfDay, minute, calendar, formataHora, txtHoraFim);
                    dateFim = calendar.getTime().getTime();
                    System.out.println("DATA E HORA FIm LONG " + dateFim);
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

    private Long getDataDaActivityAlocacao(Long dateLong) {
        stringData = findViewById(R.id.cadastroAlocacaoTxtData);
        Intent recebedora = getIntent();
        Bundle parametros = recebedora.getExtras();
        if (parametros != null) {
            String data = parametros.getString("DataStr");
            dateLong = parametros.getLong("Date");
            System.out.println("DATE LONG " + dateLong);

            stringData.setText(data);

            return dateLong;
        } else {
            Toast.makeText(CadastroAlocacao.this, "Data nula", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private void getPrefNomeOrganizador() {
        pref = getSharedPreferences("USER_DATA", 0);
        TextView organizador = findViewById(R.id.cadastroAlocacaoTxtOrganizador);
        organizador.setText("Organizador: " + pref.getString("userName", null));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
