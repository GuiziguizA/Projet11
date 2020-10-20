package sid.org.biblio.front.enumeration;

public enum Types {
	
	Types(""),
	Types1("nom"),
	Types2("auteur"),
	Types3("type");

	
	 
	private final String nom;
	

	Types( String nom) {
		 this.nom = nom;
	}


	public String getNom() {
		return nom;
	}




}

