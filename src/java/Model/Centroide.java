package Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Centroide implements Serializable {

    private List<Termo> termos;

    public Centroide() {
        this.termos = new ArrayList<Termo>();
    }

    public List<Termo> getTermos() {
        return this.termos;
    }

    public void removerTermo(Termo centroide) {
        for (int i = 0; i < termos.size(); i++) {
            if (termos.get(i).getTermo().equals(centroide.getTermo())) {
                termos.remove(i);
            }
        }
    }

    private void addQuantidadePesoTermo(int peso, String termo) {

        for (int i = 0; i < termos.size(); i++) {
            if (termos.get(i).getTermo().equals(termo)) {
                int quantidade = termos.get(i).getQuantidade();
                termos.get(i).setQuantidade(quantidade + 1);
                int pesoAntigo = termos.get(i).getPeso();
                termos.get(i).setPeso(peso + pesoAntigo);;
            }
        }

    }

    private int retornaPeso(String tag) throws FileNotFoundException {
        int peso = 1;
        BufferedReader br = new BufferedReader(new FileReader("pesos.txt"));
        try {
            while (br.ready()) {
                String linha[] = br.readLine().split(" ");
                if (linha[0].equals(tag)) {
                    peso = Integer.parseInt(linha[1]);
                }
            }
            br.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return peso;
    }

    private boolean termoJaExiste(String termo) {

        for (int i = 0; i < termos.size(); i++) {
            if (termos.get(i).getTermo().equals(termo)) {
                return true;
            }
        }
        return false;
    }

    public void armazenarTermo(String t, String tag) throws FileNotFoundException {

        if (termoJaExiste(t)) {
            int peso = retornaPeso(tag);
            addQuantidadePesoTermo(peso, t);
        } else {
            int peso = retornaPeso(tag);
            Termo termo = new Termo();
            termo.setTermo(tag);
            termo.setPeso(peso);
            termo.setQuantidade(1);
            termo.setTermo(t);

            termos.add(termo);
        }
    }

    public boolean isDomain(String title, String domain) {
        for (int i = 0; i < termos.size(); i++) {
            Termo t = termos.get(i);
            if (t.getTermo().equals(domain) && domain.contains(title)) {
                return true;
            }
        }
        return false;

    }
    
    public Termo getTermoValue(String termo) {
        for (int i = 0; i < termos.size(); i++) {
            Termo t = termos.get(i);
            if (t.getTermo().equals(termo)) {
                return t;
            }
        }

        return null;
    }

}
