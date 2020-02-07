package com.example.sistemacontrolesala.alocacao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.listaSalas.ListaSalasView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.List;

public class AlocacaoSalasView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alocacao_salas);

        setTitle("Alocações");

        ListView listAlocacao = findViewById(R.id.alocacaoSalaListView);
        List<Alocacao> listaAlocacao = new AlocacaoDao().listaAlocacao();
        listAlocacao.setAdapter(new AlocacaoAdapter(listaAlocacao, this));

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