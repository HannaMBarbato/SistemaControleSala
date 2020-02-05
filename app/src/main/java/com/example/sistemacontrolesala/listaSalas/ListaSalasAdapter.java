package com.example.sistemacontrolesala.listaSalas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.viewpager.widget.ViewPager;

import com.example.sistemacontrolesala.R;

import java.util.List;

public class ListaSalasAdapter extends BaseAdapter {

    private final List<ListaSala> listaSalas;
    private Context context;

    public ListaSalasAdapter(List<ListaSala> listaSalas, Context context) {
        this.listaSalas = listaSalas;
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
        ImageAdapter adapter = new ImageAdapter(listaSalas.get(posicao), context);
        viewPager.setAdapter(adapter);

        return viewCriada;
    }
}
