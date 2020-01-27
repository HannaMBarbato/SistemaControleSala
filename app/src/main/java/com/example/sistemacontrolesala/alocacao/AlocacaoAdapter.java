package com.example.sistemacontrolesala.alocacao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sistemacontrolesala.R;

import java.util.List;

public class AlocacaoAdapter extends BaseAdapter {

    private final List<Alocacao> listaAlocacao;
    private final Context context;

    public AlocacaoAdapter(List<Alocacao> listaAlocacao, Context context) {
        this.listaAlocacao = listaAlocacao;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaAlocacao.size();
    }

    @Override
    public Object getItem(int posicao) {
        return listaAlocacao.get(posicao);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_alocacao, viewGroup, false);
        Alocacao alocacao = listaAlocacao.get(posicao);

        ImageView imagem = viewCriada.findViewById(R.id.itemAlocacaoTira);
        imagem.setImageResource(R.color.colorButton);

        TextView organizador = viewCriada.findViewById(R.id.itemAlocacaoNomeOrganizador);
        organizador.setText(alocacao.getOrganizador());

        TextView descricao = viewCriada.findViewById(R.id.itemAlocacaoDescricao);
        descricao.setText(alocacao.getDescricao());

        TextView horaInicio = viewCriada.findViewById(R.id.itemAlocacaoHorarioInicio);
        horaInicio.setText(alocacao.getHoraInicio());

        TextView horaFim = viewCriada.findViewById(R.id.itemAlocacaoHorarioFim);
        horaFim.setText(alocacao.getHoraFim());

        return viewCriada;
    }
}
