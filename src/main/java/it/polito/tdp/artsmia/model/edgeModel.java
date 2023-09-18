package it.polito.tdp.artsmia.model;

import java.util.Objects;

public class edgeModel {

	// modello dell'arco, per prendere i dati dal database
	// serve per modellare un arco,
	// una relazione tra due oggetti
	
	
	private ArtObject source;
	private ArtObject target;
	private Integer peso;
	public ArtObject getSource() {
		return source;
	}
	public ArtObject getTarget() {
		return target;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setSource(ArtObject source) {
		this.source = source;
	}
	public void setTarget(ArtObject target) {
		this.target = target;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "edgeModel [source=" + source + ", target=" + target + ", peso=" + peso + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(peso, source, target);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		edgeModel other = (edgeModel) obj;
		return Objects.equals(peso, other.peso) && Objects.equals(source, other.source)
				&& Objects.equals(target, other.target);
	}
	public edgeModel(ArtObject source, ArtObject target, Integer peso) {
		super();
		this.source = source;
		this.target = target;
		this.peso = peso;
	}
	
	
	
	
	
}
