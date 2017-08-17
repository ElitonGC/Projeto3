package Model;

import java.io.Serializable;

public class ItemListaInvertida implements Serializable {

    private int cod;
    private int qdt;

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getQdt() {
        return qdt;
    }

    public void setQdt(int qdt) {
        this.qdt = qdt;
    }

}
