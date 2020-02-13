package com.example.sistemacontrolesala.alocacao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.listaSalas.ListaSalasView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AlocacaoSalasView extends AppCompatActivity {
    List<Alocacao> alocacoesListView = new ArrayList<>();
    String idSalaString;
    private AlocacaoAdapter adapter = new AlocacaoAdapter(alocacoesListView, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alocacao_salas);

        setTitle("Alocações");

        Intent recebedora = getIntent();
        Bundle parametros = recebedora.getExtras();
        idSalaString = parametros.getString("idSala");

        SharedPreferences pref = getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("idSala", Integer.parseInt(idSalaString));
        editor.commit();

        ListView listAlocacao = findViewById(R.id.alocacaoSalaListView);
        listAlocacao.setAdapter(adapter);

        final MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setDateSelected(CalendarDay.today(), true);

        FloatingActionButton btnCadastroAlocacao = findViewById(R.id.floatingActionButton);
        btnCadastroAlocacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String data = dateFormat.format(calendarView.getSelectedDate().getDate());

                Intent enviadora = new Intent(getApplicationContext(), CadastroAlocacao.class);

                Bundle parametros = new Bundle();
                parametros.putString("DataStr", data);
                parametros.putLong("Date", calendarView.getSelectedDate().getDate().getTime());
                enviadora.putExtras(parametros);

                startActivity(enviadora);
            }
        });
    }

    @Override
    protected void onResume() {
        getReservas(idSalaString);
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    private void getReservas(String idSalaString) {
        try {
            alocacoesListView.clear();
            String listaAlocacaoString = new IdSalaAlocacaoService().execute(idSalaString).get();
            if (listaAlocacaoString.length() > 2) {
                System.out.println(listaAlocacaoString);
                JSONArray arrayAlocacao = new JSONArray(listaAlocacaoString);
                for (int i = 0; i < arrayAlocacao.length(); i++) {
                    JSONObject objetoAlocacao = arrayAlocacao.getJSONObject(i);
                    if (objetoAlocacao.has("nomeOrganizador") && objetoAlocacao.has("descricao") && objetoAlocacao.has("dataHoraInicio") && objetoAlocacao.has("dataHoraFim")) {
                        String nomeOrganizador = objetoAlocacao.getString("nomeOrganizador");
                        String descricao = objetoAlocacao.getString("descricao");
                        String dataHoraInicio = objetoAlocacao.getString("dataHoraInicio");
                        String dataHoraFim = objetoAlocacao.getString("dataHoraFim");

                        String horaInicio = dataHoraInicio.substring(dataHoraInicio.indexOf("T") + 1, dataHoraInicio.indexOf("Z") - 3);
                        String horaFim = dataHoraFim.substring(dataHoraInicio.indexOf("T") + 1, dataHoraInicio.indexOf("Z") - 3);

                        Alocacao novaAlocacao = new Alocacao();

                        novaAlocacao.setOrganizador(nomeOrganizador);
                        novaAlocacao.setDescricao(descricao);
                        novaAlocacao.setHoraInicio(horaInicio);
                        novaAlocacao.setHoraFim(horaFim);

                        alocacoesListView.add(novaAlocacao);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void deletarReserva(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AlocacaoSalasView.this, ListaSalasView.class));
        finish();

        SharedPreferences pref = getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("idSala");
    }
}