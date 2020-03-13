package com.example.sistemacontrolesala.alocacao;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.material.textfield.TextInputLayout;

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
    private Button btnSalvar;
    private int idSalaRecuperado;
    private String idUsuarioRecuperado;

    private TextInputLayout txtInputHoraInicio, txtInputHoraFim, txtInputDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_alocacao);

        getPrefNomeOrganizador();
        getDataDaActivityAlocacao(dateLong);
        inicializaComponentesDaTela();
        possibilitaClickNosTextHora();
        configuraBtnSalvar();
    }

    private void possibilitaClickNosTextHora() {
        txtHoraInicio.setOnClickListener(this);
        txtHoraFim.setOnClickListener(this);
    }

    private void configuraBtnSalvar() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descricao = editDescricao.getText().toString();
                String strHoraInicio = txtHoraInicio.getText().toString();
                String strHoraFim = txtHoraFim.getText().toString();

                verificaDadosProCadastroDeAlocacao(descricao, strHoraInicio, strHoraFim);
            }
        });
    }

    private void verificaDadosProCadastroDeAlocacao(String descricao, String strHoraInicio, String strHoraFim) {
        if (descricao.isEmpty()) {
            txtInputDescricao.setError("Este campo nao pode ficar vazio");
        } else if (strHoraInicio.isEmpty()) {
            txtInputDescricao.setError(null);
            txtInputHoraInicio.setError("Campo nao pode ficar vazio");
        } else if (strHoraFim.isEmpty()) {
            txtInputHoraInicio.setError(null);
            txtInputHoraFim.setError("Este campo nao pode ficar vazio");
        } else if (strHoraInicio.equals(strHoraFim)) {
            txtInputHoraFim.setError("Horario de fim igual a de inicio. Digite uma hora valida");
        } else if (dateInicio > dateFim) {
            txtInputHoraFim.setError("Horario de fim tem que ser maior que horario de inicio.");
        } else {
            txtInputHoraFim.setError(null);
            getDadosDoSharedPreferences();
            JSONObject reservaSalaJson = retornaReservaEmJsonObject(descricao, idSalaRecuperado, idUsuarioRecuperado);
            fazRequestProWebService(reservaSalaJson);
        }
    }

    private void fazRequestProWebService(JSONObject reservaSalaJson) {
        String resultAuth;
        try {
            String novaReservaEncode = Base64.encodeToString(reservaSalaJson.toString().getBytes("UTF-8"), Base64.NO_WRAP);
            resultAuth = new CadastroAlocacaoService().execute(novaReservaEncode).get();

            verificaRespostaDoWebService(resultAuth);
        } catch (Exception e) {
            Toast.makeText(CadastroAlocacao.this, "Servidor nao responde", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void verificaRespostaDoWebService(String resultAuth) {
        if (resultAuth.equals("Servidor nao responde")) {
            Toast.makeText(CadastroAlocacao.this, "Servidor nao responde", Toast.LENGTH_SHORT).show();
        } else if (resultAuth.equals("A sala já está reservada para o horário selecionado")) {
            Toast.makeText(CadastroAlocacao.this, "Ja tem reserva para este bloco de hora", Toast.LENGTH_SHORT).show();
        } else {
            if (resultAuth.equals("Reserva realizada com sucesso")) {
                Toast.makeText(CadastroAlocacao.this, "Reserva efetuada com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(CadastroAlocacao.this, "Erro ao reservar sala", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getDadosDoSharedPreferences() {
        pref = getSharedPreferences("USER_DATA", 0);
        idSalaRecuperado = pref.getInt("idSala", 0);
        idUsuarioRecuperado = pref.getString("userId", null);
    }

    private JSONObject retornaReservaEmJsonObject(String descricao, int idSalaRecuperado, String idUsuarioRecuperado) {
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
        return reservaSalaJson;
    }

    private void inicializaComponentesDaTela() {
        txtHoraInicio = findViewById(R.id.cadastroAlocacaoTxtHoraInicio);
        txtHoraFim = findViewById(R.id.cadastroAlocacaoTxtHoraFim);
        editDescricao = findViewById(R.id.cadastroAlocacaoEditDesc);
        btnSalvar = findViewById(R.id.cadastroAlocacaoBtnSalvar);

        txtInputHoraInicio = findViewById(R.id.txtInputHoraInicio);
        txtInputHoraFim = findViewById(R.id.txtInputHoraFim);
        txtInputDescricao = findViewById(R.id.txtInputDescricao);
    }


    @Override
    public void onClick(View view) {
        final SimpleDateFormat formataHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getDataDaActivityAlocacao(dateLong));
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        timePickerDialogHoraInicio(view, formataHora, calendar, hour, minute);
        timePickerDialogHoraFim(view, formataHora, calendar, hour, minute);
    }

    private void timePickerDialogHoraFim(View view, final SimpleDateFormat formataHora, final Calendar calendar, int hour, int minute) {
        if (view == txtHoraFim) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    getHora(hourOfDay, minute, calendar, formataHora, txtHoraFim);
                    dateFim = calendar.getTime().getTime();
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTimeInMillis(dateFim);
                    calendar1.add(Calendar.HOUR_OF_DAY, -3);
                    dateFim = calendar1.getTimeInMillis();
                }
            }, hour, minute, android.text.format.DateFormat.is24HourFormat(this));
            timePickerDialog.show();
        }
    }

    private void timePickerDialogHoraInicio(View view, final SimpleDateFormat formataHora, final Calendar calendar, int hour, int minute) {
        if (view == txtHoraInicio) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    getHora(hourOfDay, minute, calendar, formataHora, txtHoraInicio);
                    dateInicio = calendar.getTime().getTime();
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTimeInMillis(dateInicio);
                    calendar1.add(Calendar.HOUR_OF_DAY, -3);
                    dateInicio = calendar1.getTimeInMillis();
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
        new AlertDialog.Builder(CadastroAlocacao.this)
                .setIcon(R.drawable.ic_cancel)
                .setTitle("Cancelar")
                .setMessage("Deseja cancelar sua reserva?")
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("NAO", null)
                .show();
    }
}
