package sid.org.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LivreDto {

	@Valid
	@NotBlank(message = "le champ nom ne peut pas etre vide")
	private String nom;
	@NotBlank(message = "le champ auteur ne peut pas etre vide")
	private String auteur;
	@NotBlank(message = "le champ type ne peut pas etre vide")
	private String type;
	@NotBlank(message = "le champ section ne peut pas etre vide")
	private String section;
	@NotBlank(message = "le champ emplacement ne peut pas etre vide")
	private String emplacement;
	@NotNull(message = "le champ nombre d'exemplaire ne peut pas etre vide")
	private int nombreExemplaire;
	@NotNull(message = "le champ nombre d'exemplaire ne peut pas etre vide")
	private List<String> listeDattente;

	public LivreDto(String nom, String auteur, String type, String section, String emplacement, int nombreExemplaire,
			List<String> list) {
		super();
		this.nom = nom;
		this.auteur = auteur;
		this.type = type;
		this.section = section;
		this.emplacement = emplacement;
		this.nombreExemplaire = nombreExemplaire;
		this.listeDattente = null;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getEmplacement() {
		return emplacement;
	}

	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}

	public int getNombreExemplaire() {
		return nombreExemplaire;
	}

	public void setNombreExemplaire(int nombreExemplaire) {
		this.nombreExemplaire = nombreExemplaire;
	}

	public List<String> getListeDattente() {
		return listeDattente;
	}

	public void setListeDattente(List<String> listeDattente) {
		this.listeDattente = listeDattente;
	}

}
