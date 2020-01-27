package com.example.sistemacontrolesala.listaSalas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaSalaDao {

    public List<ListaSala> listaSalas() {
        ArrayList listaSalas = new ArrayList<>(Arrays.asList(
                new ListaSala("recife_pe", "Conselho Jedi"),
                new ListaSala("belo_horizonte_mg", "Conselho Elrond"),
                new ListaSala("foz_do_iguacu_pr", "Ministerio da magia")));

        return listaSalas;
    }
}
