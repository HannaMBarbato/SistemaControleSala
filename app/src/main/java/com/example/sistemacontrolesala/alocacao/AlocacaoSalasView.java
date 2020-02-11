package com.example.sistemacontrolesala.alocacao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.listaSalas.ListaSalasService;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alocacao_salas);

        setTitle("Alocações");

        Intent recebedora = getIntent();
        Bundle parametros = recebedora.getExtras();
        String idSalaString = parametros.getString("idSala");

        System.out.println("ID SALA NA ALOCACAO " + idSalaString);
        try {
            String listaAlocacaoString = new IdSalaCadastroAlocacao().execute(idSalaString).get();
            if (listaAlocacaoString.length() > 2) {
                System.out.println(listaAlocacaoString);
                JSONArray arrayAlocacao = new JSONArray(listaAlocacaoString);
                for (int i = 0; i < arrayAlocacao.length(); i++) {
                    JSONObject objetoAlocacao = arrayAlocacao.getJSONObject(i);
                    if (objetoAlocacao.has("nomeOrganizador") && objetoAlocacao.has("descricao") && objetoAlocacao.has("dataHoraInicio") && objetoAlocacao.has("dataHoraFim")) {
                        String nomeOrganizador = objetoAlocacao.getString("nomeOrganizador");
                        String descricao = objetoAlocacao.getString("descricao");
                        String horaInicio = objetoAlocacao.getString("dataHoraInicio");
                        String horaFim = objetoAlocacao.getString("dataHoraFim");

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

        ListView listAlocacao = findViewById(R.id.alocacaoSalaListView);
        listAlocacao.setAdapter(new AlocacaoAdapter(alocacoesListView, this));


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
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AlocacaoSalasView.this, ListaSalasView.class));
        finish();
    }
}