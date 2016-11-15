package br.edu.ufabc.padm.numberx.model;


import java.util.ArrayList;
import java.util.List;

public class ModuloDAO {
    private static ModuloDAO dao;
    private List<Modulo> modulos;

    protected ModuloDAO() {}

    private void init() {
        modulos = new ArrayList<>();
        // TODO: remove when "add" operation is implemented
        loadTestData();
    }

    private void loadTestData() {
        Modulo c;

        c = new Modulo();

        c.setTitulo("Sistemas Lineares");
        c.setDescricao("Modulo a respeito de sistemas lineares");
        c.setProgressStatus("(5/10)");

        modulos.add(c);

        c = new Modulo();
        c.setTitulo("Matrizes");
        c.setDescricao("Modulo a respeito de matrizes");
        c.setProgressStatus("(2/15)");

        modulos.add(c);

        c = new Modulo();
        c.setTitulo("Eliminação de Gauss");
        c.setDescricao("Tudo sobre o método de escalonamento.");
        c.setProgressStatus("(0/10)");

        modulos.add(c);

        c = new Modulo();
        c.setTitulo("Matrizes 2");
        c.setDescricao("Continuação do estudo de matrizes.");
        c.setProgressStatus("(0/5)");

        modulos.add(c);
    }

    public static ModuloDAO newInstance() {
        if (dao == null) {
            dao = new ModuloDAO();
            dao.init();
        }

        return dao;
    }

    public void add(Modulo modulo) {
        modulos.add(modulo);
    }

    public void remove(int position) {
        modulos.remove(position);}

    public Modulo[] list(Modulo modulo) {
        return modulos.toArray(new Modulo[modulos.size()]);
    }

    public int size() {
        return modulos.size();
    }

    public Modulo getItemAt(int pos) {
        return modulos.get(pos);
    }

}
