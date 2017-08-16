package Model;

public class ItemListaInvertida implements Comparable<ItemListaInvertida>{
	
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
    public int compareTo(ItemListaInvertida outroItem) {
        if (this.cod > outroItem.getCod()) {
          return -1;
     }
     if (this.cod < outroItem.getCod()) {
          return 1;
     }
     return 0;
    }

}
