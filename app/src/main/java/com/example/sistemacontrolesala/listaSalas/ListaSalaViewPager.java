package com.example.sistemacontrolesala.listaSalas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.alocacao.AlocacaoSalasView;

public class ListaSalaViewPager extends PagerAdapter {

    private Sala sala;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListaSalaViewPager(Sala sala, Context context) {
        this.context = context;
        this.sala = sala;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        if (position == 0) {
            View view = layoutInflater.inflate(R.layout.item_salas_cardview_image, container, false);

            inicializaEConfiguraComponente(view, R.id.itemSalaNome, sala.getNome());
            redirecionaParaOutraActivity(view);

            container.addView(view, 0);
            return view;
        }

        if (position == 1) {
            View viewInf = layoutInflater.inflate(R.layout.item_sala_cardview_inf, container, false);

            inicializaEConfiguraComponente(viewInf, R.id.itemSalaQuantidadePessoasSentadas, "Capacidade de pessoas: " + sala.getQuantidadePessoasSentadas());
            setTextDeRespostaBoolean(viewInf, R.id.itemSalaPossuiMultimidia, sala.isPossuiMultimidia(), "Possui multimidia: Sim", "Possui multimidia: Nao");
            setTextDeRespostaBoolean(viewInf, R.id.itemSalaPossuiArcon, sala.isPossuiArcon(), "Possui ar condicionado: Sim", "Possui ar condicionado: Nao");
            inicializaEConfiguraComponente(viewInf, R.id.itemSalaAreaDaSala, "Area da sala: " + sala.getAreaDaSala());
            inicializaEConfiguraComponente(viewInf, R.id.itemSalaLocalizacao, "Localizacao: " + sala.getLocalizacao());

            redirecionaParaOutraActivity(viewInf);

            container.addView(viewInf, 0);
            return viewInf;
        }
        return null;
    }

    private void redirecionaParaOutraActivity(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlocacaoSalasView.class);
                Bundle parametros = new Bundle();
                parametros.putString("idSala", String.valueOf(sala.getId()));
                intent.putExtras(parametros);

                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

    private void setTextDeRespostaBoolean(View viewInf, int p, boolean possuiEquipamento, String textoTrue, String textoFalse) {
        TextView textView = viewInf.findViewById(p);
        if (possuiEquipamento) {
            textView.setText(textoTrue);
        } else {
            textView.setText(textoFalse);
        }
    }

    private void inicializaEConfiguraComponente(View view, int id, String novoTexto) {
        // ImageView imageView = view.findViewById(R.id.itemSalaImagem);
        //imageView.setImageResource(sala.getImagem());

        TextView text = view.findViewById(id);
        text.setText(novoTexto);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

