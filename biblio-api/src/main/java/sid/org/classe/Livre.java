package sid.org.classe;

import java.util.Collection;



import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author guali
 *
 */

@XmlRootElement
@Entity

public class Livre {
	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codeLivre;
	private String nom;
	private String auteur;
	private String type;
	private String section;
	private String emplacement;
	private int nombreExemplaire;
	@OneToMany(mappedBy="livre",fetch=FetchType.LAZY)	
	private Collection<Pret>prets;
	
	public Livre() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Livre(String nom, String auteur,String type, String section, String emplacement, int nombreExemplaire) {
		super();
		this.nom = nom;
		this.auteur = auteur;
		this.type = type;
		this.section = section;
		this.emplacement = emplacement;
		this.nombreExemplaire = nombreExemplaire;
		
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

	public Long getCodeLivre() {
		return codeLivre;
	}
	
	

	
}
