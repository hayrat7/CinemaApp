package org.sid.entities;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Cinema {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 75)
	@NotNull
	@Size(min = 3,max = 20,message ="Le Nom Du Cinema doit être comprise entre 3 et 20")
	private String name;
	@NotNull(message = "Etrez un nombre")
	@DecimalMin(value = "-180.0")
	@DecimalMax(value = "180.0")
	private double longitude;
	@NotNull(message = "Etrez un nombre")
	@DecimalMin(value = "-90.0")
	@DecimalMax(value = "90.0")
	private double  latitude;
	@NotNull(message = "Etrez un nombre")
	@DecimalMin(value = "0.0")
	private double altitude;
	@NotNull
	private int nbrSalles;
	@OneToMany(mappedBy = "cinema")
	private Collection<Salle> salles;
	@NotNull
	@ManyToOne
	private Ville ville;
}
