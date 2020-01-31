package com.example.sistemacontrolesala.listaSalas;

public class ListaSala {

    private int id;
    private int imagem;
    private String titulo;

    public ListaSala(int imagem, String titulo) {
        this.imagem = imagem;
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
