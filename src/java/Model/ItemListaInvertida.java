package Model;

import java.io.Serializable;

public class ItemListaInvertida implements Serializable, Comparable {

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

    @Override
    public int compareTo(Object o) {
        if (o instanceof ItemListaInvertida) {
            return new Integer(this.cod).compareTo(((ItemListaInvertida) o).getCod());
        } else {
            return -1;
        }
    }

}
