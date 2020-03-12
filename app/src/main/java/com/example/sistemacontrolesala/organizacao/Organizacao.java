package com.example.sistemacontrolesala.organizacao;

public class Organizacao {

    private int id;
    private String nome;
    private String tipoOrganizacao;

    /*private int idOrganizacaoPai;
    private String dominio;
    private int ativo;
    private String dataCriacao;
    private String dataAlteracao;*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    /*public int getIdOrganizacaoPai() {
        return idOrganizacaoPai;
    }

    public void setIdOrganizacaoPai(int idOrganizacaoPai) {
        this.idOrganizacaoPai = idOrganizacaoPai;
    }*/

    public String getTipoOrganizacao() {
        return tipoOrganizacao;
    }

    public void setTipoOrganizacao(String tipoOrganizacao) {
        this.tipoOrganizacao = tipoOrganizacao;
    }

   /* public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public int getAtivo() {
        return ativo;
    }

    public void setAtivo(int ativo) {
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
    }*/
}
