package sid.org.classe;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


/**
 * 
 * @author guali
 *
 */
@Entity
public class Utilisateur {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codeUtilisateur;
	private String nom;
	@Column(unique =true )
	private String mail;
	private String adresse;
	private String motDePasse;
	private String codePostal;
	@OneToMany(mappedBy="utilisateur",fetch=FetchType.LAZY)	
	private Collection<Pret>prets;
	@ManyToOne
	@JoinColumn(name = "ID_ROLES")
	private Roles roles;
	
	
	public Utilisateur() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Utilisateur(String nom, String mail, String adresse, String motDePasse, String codePostal, Roles roles) {
		super();
		this.nom = nom;
		this.mail = mail;
		this.adresse = adresse;
		this.motDePasse = motDePasse;
		this.codePostal = codePostal;
		this.roles = roles;
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


	public Roles getRoles() {
		return roles;
	}


	public void setRoles(Roles roles) {
		this.roles = roles;
	}


	public Long getCodeUtilisateur() {
		return codeUtilisateur;
	}
	
	
}
