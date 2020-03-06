package com.example.sistemacontrolesala.alocacao;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.listaSalas.ListaSalasView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class AlocacaoSalasView extends AppCompatActivity {
    private List<Alocacao> alocacoesListView;
    private String idSalaString;
    private AlocacaoAdapter adapter;
    private Alocacao novaAlocacao;
    private ListView listAlocacao;
    private MaterialCalendarView calendarView;
    private List<Alocacao> listaPorData;
    private ToggleButton tbUpDown;
    private Collection<CalendarDay> listaDeDiasComReservas = new ArrayList<>();

    public AlocacaoSalasView() {
        alocacoesListView = new ArrayList<>();
        listaPorData = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alocacao_salas);

        setTitle("Alocações");

        calendarView = findViewById(R.id.calendarView);
        tbUpDown = findViewById(R.id.toggleButton);

        calendarView.setDateSelected(CalendarDay.today(), true);

        tbUpDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                } else {
                    calendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                }
            }
        });

        Intent recebedora = getIntent();
        Bundle parametros = recebedora.getExtras();
        idSalaString = parametros.getString("idSala");

        SharedPreferences pref = getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("idSala", Integer.parseInt(idSalaString));
        editor.commit();

        System.out.println("ID SALA ALOCACAO VIEW " + idSalaString);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (selected) {
                   // calendarView.addDecorator(new DateDecorate(getApplicationContext(), ContextCompat.getColor(getApplicationContext(), R.color.corBranco), calendarView.getSelectedDates()));
                    getAlocacaoPorDia();
                }

            }
        });

        System.out.println("DATA CALENDAR VIIIIIIEW " + calendarView.getSelectedDate());

        FloatingActionButton btnCadastroAlocacao = findViewById(R.id.floatingActionButton);
        btnCadastroAlocacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String data = dateFormat.format(calendarView.getSelectedDate().getDate());

                Intent enviadora = new Intent(getApplicationContext(), CadastroAlocacao.class);

                Bundle parametros = new Bundle();
                parametros.putString("DataStr", data);
                parametros.putLong("Date", calendarView.getSelectedDate().getDate().getTime());
                enviadora.putExtras(parametros);

                startActivity(enviadora);
            }
        });
        listAlocacao = findViewById(R.id.alocacaoSalaListView);
        listAlocacao.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                new AlertDialog.Builder(AlocacaoSalasView.this)
                        .setIcon(R.drawable.ic_action_delete_alocacao)
                        .setTitle("Remove Alocacao")
                        .setMessage("Tem certeza que deseja remover esta alocacao?")
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String resultAuth = "";

                                String idAlocacaoRecuperado = "";
                                idAlocacaoRecuperado = String.valueOf(listaPorData.get(position).getId());
                                int idUsuarioDaReservaEfetuada = listaPorData.get(position).getIdUsuario();

                                System.out.println("ID ALOCACAO " + idAlocacaoRecuperado);

                                SharedPreferences pref = getSharedPreferences("USER_DATA", 0);
                                if (idUsuarioDaReservaEfetuada == Integer.parseInt(pref.getString("userId", null))) {
                                    try {
                                        resultAuth = new CancelaAlocacaoService().execute(idAlocacaoRecuperado).get();
                                        if (resultAuth.equals("A reserva foi cancelada com sucesso")) {
                                            listaPorData.remove(position);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(AlocacaoSalasView.this, "Reserva excluida com sucesso", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(AlocacaoSalasView.this, "Erro ao exluir reserva", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(AlocacaoSalasView.this, "Voce nao tem permicao para excluir esta reserva", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("NAO", null)
                        .show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        getReservas(idSalaString);
        getAlocacaoPorDia();
        super.onResume();
    }

    private void getAlocacaoPorDia() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = dateFormat.format(calendarView.getSelectedDate().getDate());

        listaPorData.clear();

        System.out.println("DATAAAaAA " + data);

        for (int i = 0; i < alocacoesListView.size(); i++) {
            Alocacao alocacao = alocacoesListView.get(i);
            System.out.println("DESCRICAO " + alocacao.getDataInicio());
            if (alocacao.getDataInicio().equals(data)) {
                listaPorData.add(alocacao);
            }
        }
        Log.e("TAMANHO DA LISTAAAAA", String.valueOf(alocacoesListView.size()));

        adapter = new AlocacaoAdapter(listaPorData, this);
        listAlocacao.setAdapter(adapter);
    }

    private void getReservas(String idSalaString) {
        try {
            alocacoesListView.clear();
            String listaAlocacaoString = new IdSalaAlocacaoService().execute(idSalaString).get();
            if (listaAlocacaoString.length() > 2) {
                System.out.println("OBJETO ALOCACAO " + listaAlocacaoString);
                JSONArray arrayAlocacao = new JSONArray(listaAlocacaoString);
                for (int i = 0; i < arrayAlocacao.length(); i++) {
                    JSONObject objetoAlocacao = arrayAlocacao.getJSONObject(i);
                    if (objetoAlocacao.has("nomeOrganizador") && objetoAlocacao.has("descricao") && objetoAlocacao.has("dataHoraInicio") && objetoAlocacao.has("dataHoraFim")) {
                        int id = objetoAlocacao.getInt("id");

                        String nomeOrganizador = objetoAlocacao.getString("nomeOrganizador");
                        String descricao = objetoAlocacao.getString("descricao");
                        String dataHoraInicio = objetoAlocacao.getString("dataHoraInicio");
                        String dataHoraFim = objetoAlocacao.getString("dataHoraFim");

                        int idUsuario = objetoAlocacao.getInt("idUsuario");

                        String horaInicio = dataHoraInicio.substring(dataHoraInicio.indexOf("T") + 1, dataHoraInicio.indexOf("Z") - 3);
                        String horaFim = dataHoraFim.substring(dataHoraInicio.indexOf("T") + 1, dataHoraInicio.indexOf("Z") - 3);
                        String dataInicio = dataHoraInicio.substring((0), dataHoraInicio.indexOf("T"));

                        System.out.println("DATA INICIO ALOCACAO VIEW " + dataInicio);

                        novaAlocacao = new Alocacao();

                        novaAlocacao.setId(id);
                        novaAlocacao.setOrganizador(nomeOrganizador);
                        novaAlocacao.setDescricao(descricao);
                        novaAlocacao.setHoraInicio(horaInicio);
                        novaAlocacao.setHoraFim(horaFim);
                        novaAlocacao.setDataInicio(dataInicio);
                        novaAlocacao.setIdUsuario(idUsuario);

                        alocacoesListView.add(novaAlocacao);
                        Date dataComReserva = new SimpleDateFormat("yyyy-MM-dd").parse(dataInicio);
                        listaDeDiasComReservas.add(new CalendarDay(dataComReserva));

                        Log.e("TAMANHO DA LISTAAAAA", String.valueOf(alocacoesListView.size()));
                    }
                }
                calendarView.addDecorator(new DateDecorate(getApplicationContext(), ContextCompat.getColor(getApplicationContext(), R.color.colorButton), listaDeDiasComReservas));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
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