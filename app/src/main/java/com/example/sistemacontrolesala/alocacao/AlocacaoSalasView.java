package com.example.sistemacontrolesala.alocacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sistemacontrolesala.CadastroAlocacao;
import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.listaSalas.ListaSala;
import com.example.sistemacontrolesala.listaSalas.ListaSalaDao;
import com.example.sistemacontrolesala.listaSalas.ListaSalasAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.List;

public class AlocacaoSalasView extends AppCompatActivity {

    private String dataBr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alocacao_salas);

        setTitle("Alocações");

        ListView listAlocacao= findViewById(R.id.alocacaoSalaListView);
        List<Alocacao> listaAlocacao= new AlocacaoDao().listaAlocacao();
        listAlocacao.setAdapter(new AlocacaoAdapter(listaAlocacao, this));

        FloatingActionButton btnCadastroAlocacao = findViewById(R.id.floatingActionButton);
        btnCadastroAlocacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AlocacaoSalasView.this, CadastroAlocacao.class));
            }
        });

        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setDateSelected(CalendarDay.today(), true);
        //passar o mes voltar e marcar o dia (?)
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
               Toast.makeText(AlocacaoSalasView.this, "" + date, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
