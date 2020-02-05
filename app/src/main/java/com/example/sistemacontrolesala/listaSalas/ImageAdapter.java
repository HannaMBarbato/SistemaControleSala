package com.example.sistemacontrolesala.listaSalas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.sistemacontrolesala.R;
import com.example.sistemacontrolesala.alocacao.AlocacaoSalasView;

public class ImageAdapter extends PagerAdapter {

    private ListaSala listaSalas;
    private LayoutInflater layoutInflater;
    private Context context;

    public ImageAdapter(ListaSala listaSalas, Context context) {
        this.context = context;
        this.listaSalas = listaSalas;
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

            ImageView imageView = view.findViewById(R.id.itemSalaImagem);
            TextView title = view.findViewById(R.id.itemSalaTitulo);

            imageView.setImageResource(listaSalas.getImagem());
            title.setText(listaSalas.getTitulo());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AlocacaoSalasView.class);
                    intent.putExtra("param", listaSalas.getTitulo());
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
            quantidadePessoasSentadas.setText(listaSalas.getQuantidadePessoasSentadas());

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

