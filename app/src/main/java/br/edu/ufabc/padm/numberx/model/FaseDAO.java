package br.edu.ufabc.padm.numberx.model;


import java.util.ArrayList;
import java.util.List;

public class FaseDAO {
    private static FaseDAO dao;
    private List<Fase> fases;

    protected FaseDAO() {}

    private void init() {
        fases = new ArrayList<>();
        // TODO: remove when "add" operation is implemented
        loadTestData();
    }

    private void loadTestData() {
        Fase c;

        c = new Fase();

        c.setTitulo("Fase 1");

        fases.add(c);

        c = new Fase();
        c.setTitulo("Fase 2");

        fases.add(c);

        c = new Fase();
        c.setTitulo("Fase 3");

        fases.add(c);

        c = new Fase();
        c.setTitulo("Fase 4");

        fases.add(c);

    }

    public static FaseDAO newInstance() {
        if (dao == null) {
            dao = new FaseDAO();
            dao.init();
        }

        return dao;
    }

    public void add(Fase fase) {
        fases.add(fase);
    }

    public void remove(int position) {
        fases.remove(position);}

    public Fase[] list(Fase fase) {
        return fases.toArray(new Fase[fases.size()]);
    }

    public int size() {
        return fases.size();
    }

    public Fase getItemAt(int pos) {
        return fases.get(pos);
    }

}
