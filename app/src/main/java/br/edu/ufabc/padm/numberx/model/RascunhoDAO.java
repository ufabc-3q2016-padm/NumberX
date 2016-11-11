package br.edu.ufabc.padm.numberx.model;


import java.util.ArrayList;
import java.util.List;

public class RascunhoDAO {
    private static RascunhoDAO dao;
    private List<Rascunho> rascunhos;

    protected RascunhoDAO() {}

    private void init() {
        rascunhos = new ArrayList<>();
        // TODO: remove when "add" operation is implemented
        loadTestData();
    }

    private void loadTestData() {
        Rascunho c;

        c = new Rascunho();

        c.setTitulo("Sistemas lineares");
        c.setDescricao("Atividade sobre sistemas lineares");
        c.setPostDate("21/02/2016");

        rascunhos.add(c);

        c = new Rascunho();
        c.setTitulo("Matrizes");
        c.setDescricao("Atividade sobre matrizes");
        c.setPostDate("12/04/2016");

        rascunhos.add(c);
    }

    public static RascunhoDAO newInstance() {
        if (dao == null) {
            dao = new RascunhoDAO();
            dao.init();
        }

        return dao;
    }

    public void add(Rascunho rascunho) {
        rascunhos.add(rascunho);
    }

    public void remove(int position) {
        rascunhos.remove(position);}

    public Rascunho[] list(Rascunho rascunho) {
        return rascunhos.toArray(new Rascunho[rascunhos.size()]);
    }

    public int size() {
        return rascunhos.size();
    }

    public Rascunho getItemAt(int pos) {
        return rascunhos.get(pos);
    }

}
