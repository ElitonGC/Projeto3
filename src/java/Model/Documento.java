package Model;

import java.util.List;

public class Documento {
	
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



	public boolean isDomain(String domain){
		return centroide.isDomain(this.title, domain);
	}

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Integer){
            return obj.equals(this.codigo);
        }else if(obj instanceof Documento) {
            return (this.codigo == ((Documento)obj).getCodigo());
        }else{
            return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
        }
    }       
	
}
