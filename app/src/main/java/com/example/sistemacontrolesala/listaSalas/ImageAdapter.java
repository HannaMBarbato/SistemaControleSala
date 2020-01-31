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

import java.util.List;

public class ImageAdapter extends PagerAdapter {

    private List<ListaSala> listaSalas;
    private LayoutInflater layoutInflater;
    private Context context;

    public ImageAdapter(List<ListaSala> listaSalas, Context context) {
        this.context = context;
        this.listaSalas = listaSalas;
    }

    @Override
    public int getCount() {
        return listaSalas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_salas, container, false);

        ImageView imageView;
        TextView title;

        imageView = view.findViewById(R.id.itemSalaImagem);
        title = view.findViewById(R.id.itemSalaTitulo);

        imageView.setImageResource(listaSalas.get(position).getImagem());
        title.setText(listaSalas.get(position).getTitulo());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlocacaoSalasView.class);
                intent.putExtra("param", listaSalas.get(position).getTitulo());
                context.startActivity(intent);
                // finish();
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}

