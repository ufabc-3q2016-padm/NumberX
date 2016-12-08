package br.edu.ufabc.padm.numberx.model;


import java.util.ArrayList;
import java.util.List;

public class EnunciadoDAO {
    private static EnunciadoDAO dao;
    private List<Enunciado> enunciados;

    protected EnunciadoDAO() {}

    private void init() {
        enunciados = new ArrayList<>();
        // TODO: remove when "add" operation is implemented
        loadTestData();
    }

    private void loadTestData() {
        Enunciado c;

        c = new Enunciado();

        c.setTitulo("Exercicio 1");
        c.setDescricao("Resolva o sistema exibido na tab seguinte");

        enunciados.add(c);

        c = new Enunciado();
        c.setTitulo("Exercicio 2");
        c.setDescricao("Resolva o sistema exibido na tab seguinte");

        enunciados.add(c);

        c = new Enunciado();
        c.setTitulo("Exercicio 3");
        c.setDescricao("Resolva o sistema exibido na tab seguinte");

        enunciados.add(c);

        c = new Enunciado();
        c.setTitulo("Exercicio 4");
        c.setDescricao("Resolva o sistema exibido na tab seguinte");

        enunciados.add(c);
    }

    public static EnunciadoDAO newInstance() {
        if (dao == null) {
            dao = new EnunciadoDAO();
            dao.init();
        }

        return dao;
    }

    public void add(Enunciado enunciado) {
        enunciados.add(enunciado);
    }

    public void remove(int position) {
        enunciados.remove(position);}

    public Enunciado[] list(Enunciado enunciado) {
        return enunciados.toArray(new Enunciado[enunciados.size()]);
    }

    public int size() {
        return enunciados.size();
    }

    public Enunciado getItemAt(int pos) {
        return enunciados.get(pos);
    }

}
