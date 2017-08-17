package Model;

import java.io.Serializable;

public class Termo implements Serializable {

    private String termo;
    private int peso;
    private int quantidade;
    
    public Termo(){
        termo = "";
        peso = 0;
        quantidade = 0;
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

}
