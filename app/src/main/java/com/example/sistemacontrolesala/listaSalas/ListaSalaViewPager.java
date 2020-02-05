package com.example.sistemacontrolesala.listaSalas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

        if(position == 0){
            layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.item_salas_cardview_image, container, false);

           // ImageView imageView = view.findViewById(R.id.itemSalaImagem);
            //imageView.setImageResource(sala.getImagem());

            TextView nome = view.findViewById(R.id.itemSalaNome);
            nome.setText(sala.getNome());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AlocacaoSalasView.class);
                    intent.putExtra("param", sala.getNome());
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });
            container.addView(view, 0);
            return view;
        }

        if(position == 1){
            layoutInflater = LayoutInflater.from(context);
            View viewInf = layoutInflater.inflate(R.layout.item_sala_cardview_inf, container, false);

            TextView quantidadePessoasSentadas = viewInf.findViewById(R.id.itemSalaQuantidadePessoasSentadas);
            quantidadePessoasSentadas.setText("quantidade de pessoas: " + sala.getQuantidadePessoasSentadas());

            TextView multimidia = viewInf.findViewById(R.id.itemSalaPossuiMultimidia);
            multimidia.setText("possui multimidia: " + sala.isPossuiMultimidia());

            TextView arCondicionado = viewInf.findViewById(R.id.itemSalaPossuiArcon);
            arCondicionado.setText("possui ar condicionado: " + sala.isPossuiArcon());

            TextView areaSala = viewInf.findViewById(R.id.itemSalaAreaDaSala);
            areaSala.setText("area da sala: " + sala.getAreaDaSala());

            TextView localizacao = viewInf.findViewById(R.id.itemSalaLocalizacao);
            localizacao.setText("localizacao: " + sala.getLocalizacao());

            container.addView(viewInf, 0);
            return viewInf;
        }
        return null;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

