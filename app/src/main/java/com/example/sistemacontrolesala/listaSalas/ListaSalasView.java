package com.example.sistemacontrolesala.listaSalas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sistemacontrolesala.MainActivity;
import com.example.sistemacontrolesala.alocacao.AlocacaoSalasView;
import com.example.sistemacontrolesala.R;

import java.util.ArrayList;
import java.util.List;

public class ListaSalasView extends AppCompatActivity {

    ViewPager viewPager;
    ImageAdapter adapter;
    List<ListaSala> listaSalas;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_salas);
        setTitle("Salas");


       // ListView listSalas = findViewById(R.id.listaSalaListView);

        listaSalas = new ArrayList<>();
        listaSalas.add(new ListaSala(R.drawable.foz_do_iguacu_pr, "Brochure"));
        listaSalas.add(new ListaSala(R.drawable.belo_horizonte_mg, "Sticker"));

       /* List<ListaSala> listaSala = listaSalas;
        listSalas.setAdapter(new ListaSalasAdapter(listaSalas, this));*/

        adapter = new ImageAdapter(listaSalas, this);

        viewPager = findViewById(R.id.viewPagerUm);
        viewPager.setAdapter(adapter);

        Integer[] colors_temp = {
                getResources().getColor(R.color.colorButton),
                getResources().getColor(R.color.colorPrimary)

        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ListaSalasView.this, MainActivity.class));
        finish();
    }

    public void clickCardView(View view) {
        startActivity(new Intent(ListaSalasView.this, AlocacaoSalasView.class));
        finish();
    }
}
