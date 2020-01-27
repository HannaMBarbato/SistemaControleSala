package com.example.sistemacontrolesala.listaSalas;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.ResourcesUtil;

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

        ImageView imagem = viewCriada.findViewById(R.id.itemSalaImagem);
        Drawable drawableImgListaSala = ResourcesUtil.devolveDrawable(context, listaSala.getImagem());
        imagem.setImageDrawable(drawableImgListaSala);

        TextView titulo = viewCriada.findViewById(R.id.itemSalaTitulo);
        titulo.setText(listaSala.getTitulo());

        return viewCriada;
    }
}
