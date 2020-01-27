package com.example.sistemacontrolesala.listaSalas;

public class ListaSala {

    private int id;
    private String imagem;
    private String titulo;

    public ListaSala(String imagem, String titulo) {
        this.imagem = imagem;
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
