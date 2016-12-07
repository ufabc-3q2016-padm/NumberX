package br.edu.ufabc.padm.numberx.model;

import java.util.ArrayList;
import java.util.List;


public class DAO {

    public static class Fase {
        private static Fase dao;
        private List<br.edu.ufabc.padm.numberx.model.Fase> fases;

        Fase() {}

        private void init() {
            fases = new ArrayList<>();
            loadTestData();
        }

        private void loadTestData() {
            br.edu.ufabc.padm.numberx.model.Fase c;

            c = new br.edu.ufabc.padm.numberx.model.Fase();

            c.setTitulo("Fase 1");

            fases.add(c);

            c = new br.edu.ufabc.padm.numberx.model.Fase();
            c.setTitulo("Fase 2");

            fases.add(c);

            c = new br.edu.ufabc.padm.numberx.model.Fase();
            c.setTitulo("Fase 3");

            fases.add(c);

            c = new br.edu.ufabc.padm.numberx.model.Fase();
            c.setTitulo("Fase 4");

            fases.add(c);

        }

        public static Fase newInstance() {
            if (dao == null) {
                dao = new Fase();
                dao.init();
            }

            return dao;
        }

        public void add(br.edu.ufabc.padm.numberx.model.Fase fase) {
            fases.add(fase);
        }

        public void remove(int position) {
            fases.remove(position);}

        public br.edu.ufabc.padm.numberx.model.Fase[] list(br.edu.ufabc.padm.numberx.model.Fase fase) {
            return fases.toArray(new br.edu.ufabc.padm.numberx.model.Fase[fases.size()]);
        }

        public int size() {
            return fases.size();
        }

        public br.edu.ufabc.padm.numberx.model.Fase getItemAt(int pos) {
            return fases.get(pos);
        }
    }

    public static class Modulo {
        private static Modulo dao;
        private List<br.edu.ufabc.padm.numberx.model.Modulo> modulos;

        Modulo() {}

        private void init() {
            modulos = new ArrayList<>();
            loadTestData();
        }

        private void loadTestData() {
            br.edu.ufabc.padm.numberx.model.Modulo c;

            c = new br.edu.ufabc.padm.numberx.model.Modulo();

            c.setTitulo("Sistemas Lineares");
            c.setDescricao("Modulo a respeito de sistemas lineares");
            c.setProgressStatus("(5/10)");

            modulos.add(c);

            c = new br.edu.ufabc.padm.numberx.model.Modulo();
            c.setTitulo("Matrizes");
            c.setDescricao("Modulo a respeito de matrizes");
            c.setProgressStatus("(2/15)");

            modulos.add(c);

            c = new br.edu.ufabc.padm.numberx.model.Modulo();
            c.setTitulo("Eliminação de Gauss");
            c.setDescricao("Tudo sobre o método de escalonamento.");
            c.setProgressStatus("(0/10)");

            modulos.add(c);

            c = new br.edu.ufabc.padm.numberx.model.Modulo();
            c.setTitulo("Matrizes 2");
            c.setDescricao("Continuação do estudo de matrizes.");
            c.setProgressStatus("(0/5)");

            modulos.add(c);
        }

        public static Modulo newInstance() {
            if (dao == null) {
                dao = new Modulo();
                dao.init();
            }

            return dao;
        }

        public void add(br.edu.ufabc.padm.numberx.model.Modulo modulo) {
            modulos.add(modulo);
        }

        public void remove(int position) {
            modulos.remove(position);}

        public br.edu.ufabc.padm.numberx.model.Modulo[] list(br.edu.ufabc.padm.numberx.model.Modulo modulo) {
            return modulos.toArray(new br.edu.ufabc.padm.numberx.model.Modulo[modulos.size()]);
        }

        public int size() {
            return modulos.size();
        }

        public br.edu.ufabc.padm.numberx.model.Modulo getItemAt(int pos) {
            return modulos.get(pos);
        }
    }

    public static class Rascunho{
        private static Rascunho dao;
        private List<br.edu.ufabc.padm.numberx.model.Rascunho> rascunhos;

        Rascunho() {}

        private void init() {
            rascunhos = new ArrayList<>();
            loadTestData();
        }

        private void loadTestData() {
            br.edu.ufabc.padm.numberx.model.Rascunho c;

            c = new br.edu.ufabc.padm.numberx.model.Rascunho();

            c.setTitulo("Sistema");
            c.setDescricao("Sistema da aula de ontem.");
            c.setPostDate("21/02/2016");

            rascunhos.add(c);

            c = new br.edu.ufabc.padm.numberx.model.Rascunho();
            c.setTitulo("Matriz");
            c.setDescricao("Matriz 4x4 que preciso escalonar");
            c.setPostDate("12/04/2016");

            rascunhos.add(c);
        }

        public static Rascunho newInstance() {
            if (dao == null) {
                dao = new Rascunho();
                dao.init();
            }

            return dao;
        }

        public void add(br.edu.ufabc.padm.numberx.model.Rascunho rascunho) {
            rascunhos.add(rascunho);
        }

        public void remove(int position) {
            rascunhos.remove(position);}

        public br.edu.ufabc.padm.numberx.model.Rascunho[] list(br.edu.ufabc.padm.numberx.model.Rascunho rascunho) {
            return rascunhos.toArray(new br.edu.ufabc.padm.numberx.model.Rascunho[rascunhos.size()]);
        }

        public int size() {
            return rascunhos.size();
        }

        public br.edu.ufabc.padm.numberx.model.Rascunho getItemAt(int pos) {
            return rascunhos.get(pos);
        }
    }
}
