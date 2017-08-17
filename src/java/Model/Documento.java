package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Documento implements Serializable, Comparable<Documento> {

	
	private String title, link;
	private Centroide centroide;
	private int qtdTermosDiferentes;
	private int qtdTermos = 0;
	private List<Seed> seeds;
	private int codigo;

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
        

    
    public int getCodigo() {
        return codigo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Centroide getCentroide() {
        return centroide;
    }

    public void setCentroide(Centroide centroide) {
        this.centroide = centroide;
    }

    public List<Seed> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<Seed> seeds) {
        this.seeds = seeds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQtdTermosDiferentes() {
        return qtdTermosDiferentes;
    }

    public void setQtdTermosDiferentes(int qtdTermosDiferentes) {
        this.qtdTermosDiferentes = qtdTermosDiferentes;
    }

    public int getQtdTermos() {
        return qtdTermos;
    }

    public void setQtdTermos(int qtdTermos) {
        this.qtdTermos = qtdTermos;
    }

    public boolean isDomain(String domain) {
        return centroide.isDomain(domain);
    }

	

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Integer) {
            return obj.equals(this.codigo);
        } else if (obj instanceof Documento) {
            return (this.codigo == ((Documento) obj).getCodigo());
        } else {
            return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    @Override
    public int compareTo(Documento outroDoc) {
     if (this.codigo > outroDoc.getCodigo()) {
          return 1;
     }
     if (this.codigo < outroDoc.getCodigo()) {
          return -1;
     }
     return 0;
    }
    
    public double calcularScore(List<String> termosConsulta, int N, int ft){
        List<Double> list_wdt = new ArrayList<Double>(), list_wqt = new ArrayList<Double>();
        double wdt, wqt, Wd, Sd = 0;
        for(String termo: termosConsulta){
           wdt = calcularWDT(termo);
           list_wdt.add(wdt);
           wqt = Math.log(1 + (N/ft));
           list_wqt.add(wqt);
           Sd += wdt*wqt;
        }
        Wd = calcularWd(list_wdt);
        
        return Sd/Wd;
    }

    private double calcularWDT(String termo) {
        Termo t = centroide.getTermoValue(termo);
        return 1 + (Math.log(t.getQuantidade()));
    }

    private double calcularWd(List<Double> list_wdt) {
        double soma_Wd = 0;
        for(double wdt: list_wdt){
            soma_Wd += Math.pow(wdt,2);;
        }
        return Math.sqrt(soma_Wd);
    }
}
