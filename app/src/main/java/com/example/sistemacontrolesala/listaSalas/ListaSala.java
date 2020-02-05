package com.example.sistemacontrolesala.listaSalas;

public class ListaSala {

    private int id;
    private int imagem;
    private String titulo;

    private String quantidadePessoasSentadas;
    private String possuiMultimidia;
    private boolean possuiArcon;
    private double areaDaSala;
    private String localizacao;
    private double latitude;
    private double longitude;
    private boolean ativo;
    private String dataCriacao;
    private String dataAlteracao;

    public ListaSala(int imagem, String titulo, String quantidadePessoasSentadas) {
        this.imagem = imagem;
        this.titulo = titulo;
        this.quantidadePessoasSentadas = quantidadePessoasSentadas;
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

    public String getQuantidadePessoasSentadas() {
        return quantidadePessoasSentadas;
    }

    public void setQuantidadePessoasSentadas(String quantidadePessoasSentadas) {
        this.quantidadePessoasSentadas = quantidadePessoasSentadas;
    }

    public String getPossuiMultimidia() {
        return possuiMultimidia;
    }

    public void setPossuiMultimidia(String possuiMultimidia) {
        this.possuiMultimidia = possuiMultimidia;
    }

    public boolean isPossuiArcon() {
        return possuiArcon;
    }

    public void setPossuiArcon(boolean possuiArcon) {
        this.possuiArcon = possuiArcon;
    }

    public double getAreaDaSala() {
        return areaDaSala;
    }

    public void setAreaDaSala(double areaDaSala) {
        this.areaDaSala = areaDaSala;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(String dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}
