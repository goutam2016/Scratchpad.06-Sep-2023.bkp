package org.gb.sample.cassandra;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "mammals", name = "primate")
public class Primate {

	@PartitionKey
	@Column(name = "species")
	private String species;

	@Column(name = "arboreal")
	private Boolean arboreal;

	@Column(name = "avgweight")
	private Double avgWeight;

	@Column(name = "genus")
	private Genus genus;

	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}

	public Boolean getArboreal() {
		return arboreal;
	}
	public void setArboreal(Boolean arboreal) {
		this.arboreal = arboreal;
	}

	public Double getAvgWeight() {
		return avgWeight;
	}
	public void setAvgWeight(Double avgWeight) {
		this.avgWeight = avgWeight;
	}

	public Genus getGenus() {
		return genus;
	}
	public void setGenus(Genus genus) {
		this.genus = genus;
	}
}