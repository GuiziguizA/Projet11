package sid.org.dto;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import sid.org.classe.Pret;

public class UtilisateurDto {
	
	
	
	@NotBlank(message = "le champ nom ne peut pas etre vide")
	private String nom;
	@Column(unique =true )
	@Email(message="L'adresse mail saisie n'est pas valide")
	@NotBlank(message = "le champ mail ne peut pas etre vide")
	private String mail;
	@NotBlank(message = "le champ adresse ne peut pas etre vide")
	private String adresse;
	@NotBlank(message = "le champ mot de passe ne peut pas etre vide")
	private String motDePasse;
	@NotBlank(message = "le champ code postal ne peut pas etre vide")
	private String codePostal;

	public UtilisateurDto(String nom,
			String mail,
			 String adresse,
			 String motDePasse,
			 String codePostal) {
		super();
		this.nom = nom;
		this.mail = mail;
		this.adresse = adresse;
		this.motDePasse = motDePasse;
		this.codePostal = codePostal;
	
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	
	
	
}
