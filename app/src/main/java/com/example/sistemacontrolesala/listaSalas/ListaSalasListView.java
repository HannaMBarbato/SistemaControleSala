package com.example.sistemacontrolesala.listaSalas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.viewpager.widget.ViewPager;

import com.example.sistemacontrolesala.R;

import java.util.List;

public class ListaSalasListView extends BaseAdapter {

    private List<Sala> listaSalas;
    private Context context;

    public ListaSalasListView(List<Sala> salas, Context context) {
        this.listaSalas = salas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaSalas.size();
    }

    @Override
    public Object getItem(int posicao) {
        return listaSalas.get(posicao).getId();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_salas_view_pager, viewGroup, false);

        ViewPager viewPager = viewCriada.findViewById(R.id.viewPagerAqui);
        ListaSalaViewPager adapter = new ListaSalaViewPager(listaSalas.get(posicao), context);
        viewPager.setAdapter(adapter);

        return viewCriada;
    }
}
