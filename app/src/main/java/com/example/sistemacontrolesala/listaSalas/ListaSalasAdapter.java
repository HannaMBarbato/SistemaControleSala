package com.example.sistemacontrolesala.listaSalas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.sistemacontrolesala.R;

import java.util.List;

public class ListaSalasAdapter extends BaseAdapter {

    private final List<ListaSala> listaSalas;
    private final Context context;

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

        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_salas, viewGroup, false);
        ListaSala listaSala = listaSalas.get(posicao);

        ViewPager viewPager = viewCriada.findViewById(R.id.itemSalaImagem);
        ImageAdapter adapter = new ImageAdapter(context);
        viewPager.setAdapter(adapter);

        TextView titulo = viewCriada.findViewById(R.id.itemSalaTitulo);
        titulo.setText(listaSala.getTitulo());

        return viewCriada;
    }

}
