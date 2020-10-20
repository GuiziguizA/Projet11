package sid.org.biblio.front.classe;

import java.util.Date;



public class Pret {

	private Long id;
	private String statut;
	private Livre livre;
	private Date dateDeDebut;
	private Date dateDeFin;
	private Utilisateur utilisateur;
	private Date dateDeRendu;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public Livre getLivre() {
		return livre;
	}
	public void setLivre(Livre livre) {
		this.livre = livre;
	}
	
	public Date getDateDeDebut() {
		return dateDeDebut;
	}
	public void setDateDeDebut(Date dateDeDebut) {
		this.dateDeDebut = dateDeDebut;
	}
	public Date getDateDeFin() {
		return dateDeFin;
	}
	public void setDateDeFin(Date dateDeFin) {
		this.dateDeFin = dateDeFin;
	}
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}
	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	public Date getDateDeRendu() {
		return dateDeRendu;
	}
	public void setDateDeRendu(Date dateDeRendu) {
		this.dateDeRendu = dateDeRendu;
	}
	
	
	
}
