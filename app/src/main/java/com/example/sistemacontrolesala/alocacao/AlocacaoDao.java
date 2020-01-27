package com.example.sistemacontrolesala.alocacao;

import com.example.sistemacontrolesala.listaSalas.ListaSala;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlocacaoDao {

    public List<Alocacao> listaAlocacao() {
        ArrayList listaAlocacao = new ArrayList<>(Arrays.asList(
                new Alocacao("colorButton","teste", "Reuniao com cliente para aprovacao do layout pedido","00:00","00:01"),
                new Alocacao("colorButton","Jaime Lourenco", "Reuniao para termos de aprendiz","13:30","15:00"),
                new Alocacao("colorButton","Jaime Lourenco", "Reuniao para termos de aprendiz","13:30","15:00"),
                new Alocacao("colorButton","teste", "Ministerio da magia","00:00","00:00")));

        return listaAlocacao;
    }
}
