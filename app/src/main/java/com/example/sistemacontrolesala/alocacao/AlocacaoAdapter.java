package com.example.sistemacontrolesala.alocacao;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.ResourcesUtil;

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
        return 0;
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_alocacao, viewGroup, false);
        Alocacao alocacao = listaAlocacao.get(posicao);

       /* ImageView imagem = viewCriada.findViewById(R.id.itemAlocacaoTira);
        Drawable drawableImgListaAlocacao = ResourcesUtil.devolveDrawable(context, alocacao.getImagem());
        imagem.setImageDrawable(drawableImgListaAlocacao);*/

       /*ImageView imagem = viewCriada.findViewById(R.id.itemAlocacaoTira);
       imagem.setColorFilter(alocacao.getImagem());*/


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
