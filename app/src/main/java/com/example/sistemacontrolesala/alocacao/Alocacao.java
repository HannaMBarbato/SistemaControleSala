package com.example.sistemacontrolesala.alocacao;

public class Alocacao {

    //chave estrangeira id salas
    private String imagem;
    private String organizador;
    private String descricao;
    private String horaInicio;
    private String horaFim;

    public Alocacao(String imagem, String organizador, String descricao, String horaInicio, String horaFim) {
        this.imagem = imagem;
        this.organizador = organizador;
        this.descricao = descricao;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getOrganizador() {
        return organizador;
    }

    public void setOrganizador(String organizador) {
        this.organizador = organizador;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }
}
