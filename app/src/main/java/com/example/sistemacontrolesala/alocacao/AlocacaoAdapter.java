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

        textView(viewCriada, R.id.itemAlocacaoNomeOrganizador, alocacao.getOrganizador());
        textView(viewCriada, R.id.itemAlocacaoDescricao, alocacao.getDescricao());
        textView(viewCriada, R.id.itemAlocacaoHorarioInicio, alocacao.getHoraInicio());
        textView(viewCriada, R.id.itemAlocacaoHorarioFim, alocacao.getHoraFim());

        return viewCriada;
    }

    private void textView(View viewCriada, int id, String novoTexto) {
        TextView textView = viewCriada.findViewById(id);
        textView.setText(novoTexto);
    }
}
