package Model;

import java.io.Serializable;

public class SearchBean implements Serializable {

    private String link;
    private String titulo;
    private String texto;

    public SearchBean() {

    }

    public SearchBean(String link, String titulo, String texto) {
        this.link = link;
        this.titulo = titulo;
        this.texto = texto;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
