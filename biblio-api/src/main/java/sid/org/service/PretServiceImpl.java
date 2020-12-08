package sid.org.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sid.org.classe.Livre;
import sid.org.classe.Pret;
import sid.org.classe.Utilisateur;
import sid.org.dao.LivreRepository;
import sid.org.dao.PretRepository;
import sid.org.dao.UtilisateurRepository;
import sid.org.exception.BadException;
import sid.org.exception.EntityAlreadyExistException;
import sid.org.exception.LivreIndisponibleException;
import sid.org.exception.ResultNotFoundException;

@Service
public class PretServiceImpl implements PretService {

	private static final Logger logger = LoggerFactory.getLogger(PretServiceImpl.class);
	@Autowired
	private PretRepository pretRepository;
	@Autowired
	private DateService dateService;
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	@Autowired
	private LivreRepository livreRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private HttpService httpService;
	@Autowired
	private ConnectionApiService connectionApiService;

	@Value("${pret.statut1}")
	private String encours;

	@Value("${pret.statut2}")
	private String prolonge;

	@Value("${pret.statut3}")
	private String depasse;

	@Value("${pret.statut4}")
	private String remis;

	@Value("${pret.statut5}")
	private String enAttente;

	@Value("${pret.time}")
	private int time;

	@Value("${mail.subject}")
	private String subject;
	@Value("${mail.bibliotheque}")
	private String biblioMail;

	@Value("${api.url}")
	private String apiUrl;
	@Value("${spring.api.identifiant}")
	private String mail;
	@Value("${spring.api.motDePasse}")
	private String motDePasse;
	@Value("${livre.max.nombre.exemplaire}")
	private int MultipleListDattente;

	/*
	 * Creation d'un Pret + decrementation nombreExemplaire pour le livre emprunté
	 * 
	 * @param Long idLivre
	 * 
	 * @param String mail
	 *
	 * @return Pret
	 */

	@Override
	@Transactional
	public void creerPret(Long idLivre, String mail)
			throws ResultNotFoundException, LivreIndisponibleException, EntityAlreadyExistException, BadException {
		Date date1 = new Date();
		Pret pret = new Pret();
		Optional<Utilisateur> utilisateur = utilisateurRepository.findByMail(mail);
		Optional<Livre> livre = livreRepository.findByCodeLivre(idLivre);

		if (!utilisateur.isPresent()) {
			throw new ResultNotFoundException("l'utilisateur n'existe pas");
		}
		if (!livre.isPresent()) {
			throw new ResultNotFoundException("le livre n'existe pas");
		}

		List<Pret> listpret = pretRepository.findByUtilisateurAndLivre(utilisateur.get(), livre.get());
		Optional<Pret> pret1 = trouverPretenCours(listpret);

		if (!pret1.equals(Optional.empty())) {

			if (pret1.isPresent() && !pret1.get().getStatut().equals(remis)
					&& !pret1.get().getStatut().equals(enAttente)) {
				throw new EntityAlreadyExistException("La reservation existe deja pour ce livre");
			}
			if (pret1.isPresent() && livre.get().getNombreExemplaire() == 0 && pret1.get().getStatut().equals(enAttente)
					&& !pret1.get().getStatut().equals(remis)) {
				throw new EntityAlreadyExistException("La reservation existe deja pour ce livre ");
			}
		}
		if (livre.get().getNombreExemplaire() == 1 && !livre.get().getListeDattente().isEmpty()) {

		}
		if (livre.get().getNombreListeDattente() == livre.get().getNombreExemplaireFixe() * MultipleListDattente) {
			throw new BadException("La liste d'attente est complète");
		} else if (livre.get().getNombreExemplaire() < 1) {

			livre.get().getListeDattente().add(mail);

			livre.get().setNombreListeDattente(livre.get().getNombreListeDattente() + 1);

			pret.setLivre(livre.get());
			pret.setUtilisateur(utilisateur.get());
			pret.setStatut(enAttente);
			pret.setPosition(livre.get().getNombreListeDattente());

			pretRepository.saveAndFlush(pret);

		} else if (pret1.equals(Optional.empty())) {

			pret.setLivre(livre.get());
			pret.setUtilisateur(utilisateur.get());
			pret.setDateDeDebut(date1);
			pret.setDateDeFin(dateService.modifierDate(date1, time));
			pret.setNombreLivres(1);
			pret.setStatut(encours);
			livre.get().setNombreExemplaire(livre.get().getNombreExemplaire() - 1);
			pretRepository.saveAndFlush(pret);
		} else if (pret1.get().getStatut().equals(enAttente) && livre.get().getNombreExemplaire() != 0) {
			pret1.get().setDateDeDebut(date1);
			pret1.get().setDateDeFin(dateService.modifierDate(date1, time));
			pret1.get().setNombreLivres(1);
			pret1.get().setStatut(encours);
			livre.get().setNombreExemplaire(livre.get().getNombreExemplaire() - 1);
			pretRepository.saveAndFlush(pret1.get());

		} else {

			pret.setLivre(livre.get());
			pret.setUtilisateur(utilisateur.get());
			pret.setDateDeDebut(date1);
			pret.setDateDeFin(dateService.modifierDate(date1, time));
			pret.setNombreLivres(1);
			pret.setStatut(encours);
			livre.get().setNombreExemplaire(livre.get().getNombreExemplaire() - 1);
			pretRepository.saveAndFlush(pret);
		}
		if (livre.get().getDateDeRetour() == null) {
			livre.get().setDateDeRetour(modifierLaDateDeRetour(livre.get(), remis));
		}
		livreRepository.saveAndFlush(livre.get());

	}

	/*
	 * Trouver le pret avec statut encours dans une list de pret
	 * 
	 * @param List<Pret>prets
	 * 
	 *
	 * @return Optional<Pret>
	 */
	@Override
	public Optional<Pret> trouverPretenCours(List<Pret> prets) {

		List<Pret> listPret = new ArrayList<Pret>();
		for (Pret pret : prets) {
			if (pret.getStatut().equals(remis)) {

			} else {
				listPret.add(pret);
			}

		}
		if (!listPret.isEmpty()) {
			Optional<Pret> pret1 = Optional.of(listPret.get(0));
			return pret1;
		} else {
			Optional<Pret> pret1 = Optional.empty();
			return pret1;
		}

	}

	/*
	 * SupprimerPret
	 * 
	 * @param Long Id
	 * 
	 *
	 *
	 */
	@Transactional
	@Override
	public void supprimerPret(Long id, Optional<String> statutPret) throws ResultNotFoundException, BadException {
		Optional<Pret> pret = pretRepository.findById(id);

		logger.info(statutPret.get());
		logger.info(enAttente);

		if (!statutPret.get().equals("encours1")) {
			if (!pret.isPresent()) {
				throw new ResultNotFoundException("ce pret n'existe pas");
			}
			logger.info(pret.get().getStatut());
			if (!pret.get().getStatut().equals(enAttente)) {
				throw new BadException("le pret ne peut pas etre supprimer");
			}
		}

		if (!pret.isPresent()) {
			throw new ResultNotFoundException("ce pret n'existe pas");
		}

		Optional<Livre> livre = livreRepository.findById(pret.get().getLivre().getCodeLivre());
		logger.info(pret.get().getStatut());
		if (!pret.get().getStatut().equals(enAttente) && !pret.get().getStatut().equals(remis)) {
			livre.get().setNombreExemplaire(livre.get().getNombreExemplaire() + pret.get().getNombreLivres());
			livre.get().setNombreListeDattente(0);
			livre.get().setDateDeRetour(null);
			logger.info(String.valueOf(livre.get().getNombreListeDattente()) + "pret autre que remis et attente");
		}

		if (pret.get().getStatut().equals(enAttente)) {
			modifierLesPositionsDesPretsEnListeDattentes(livre.get().getCodeLivre(), pret.get().getPosition());
			livre.get().setNombreListeDattente(livre.get().getNombreListeDattente() - 1);
			ArrayList<String> listmail = new ArrayList<String>(livre.get().getListeDattente());
			listmail.remove(pret.get().getUtilisateur().getMail());
			livre.get().setListeDattente(listmail);
			logger.info(String.valueOf(livre.get().getNombreListeDattente()) + "pret en attente ");
		}
		livreRepository.saveAndFlush(livre.get());
		pretRepository.delete(pret.get());

	}

	/*
	 * Afficher Page de prets en fonction d'un e-mail
	 * 
	 * @param String mail
	 * 
	 * @param int page
	 * 
	 * @param int size
	 *
	 * @return Page<Pret>
	 */

	@Override
	public Page<Pret> afficherPrets(String mail, int page, int size) throws ResultNotFoundException {
		if (size == 0) {
			throw new ResultNotFoundException("le parametre size est incorrect");
		}
		Optional<Utilisateur> utilisateur = utilisateurRepository.findByMail(mail);
		if (!utilisateur.isPresent()) {
			throw new ResultNotFoundException("l'utilisateur est introuvable");
		}

		Pageable pageable = PageRequest.of(page, size);
		Page<Pret> listPretUtilisateur = pretRepository.findByUtilisateur(utilisateur.get(), pageable);

		return listPretUtilisateur;
	}

	/*
	 * Afficher la Liste de tous lesPrets
	 * 
	 * @param Long id
	 * 
	 *
	 * @return List<Pret>prets
	 */
	@Override
	public List<Pret> afficherPrets() throws ResultNotFoundException {
		List<Pret> prets = pretRepository.findAll();

		return prets;

	}

	/*
	 * Afficher une liste de Prets en fonction d'un statut
	 * 
	 * @param Long id
	 * 
	 *
	 * @return List<Pret>prets
	 */
	@Override
	public List<Pret> afficherPrets(String statut) throws ResultNotFoundException {
		List<Pret> prets = pretRepository.findByStatut(statut);

		return prets;

	}

	/*
	 * Afficher Un pret
	 * 
	 * @param Long id
	 * 
	 *
	 * @return Pret
	 */
	@Override
	public Pret afficherUnPret(Long id) throws ResultNotFoundException {
		Optional<Pret> pret = pretRepository.findById(id);
		if (!pret.isPresent()) {
			throw new ResultNotFoundException("ce pret n'existe pas");
		}
		return pret.get();
	}

	/*
	 * Modifier le statut d'un Pret en statut3 uniquement si le pret a un statut1 ou
	 * statut2
	 * 
	 * @param Long id
	 * 
	 *
	 *
	 */

	@Override
	public void modifierStatut(Long id) throws ResultNotFoundException {
		Date aujourdhui = new Date();
		Optional<Pret> pret = pretRepository.findById(id);
		if (!pret.isPresent()) {
			throw new ResultNotFoundException("le pret n'existe pas");
		}
		if (pret.get().getDateDeFin().compareTo(aujourdhui) > 0 && pret.get().getStatut().equals(prolonge)) {
			pret.get().setStatut(depasse);
			pretRepository.saveAndFlush(pret.get());
		} else if (pret.get().getDateDeFin().compareTo(aujourdhui) > 0 && pret.get().getStatut().equals(encours)) {
			pret.get().setStatut(depasse);
			pretRepository.saveAndFlush(pret.get());
		}
	}

	/*
	 * Appliquer la methode modifier Statut sur tout les pret si la da de fin de
	 * pret est depassé
	 * 
	 * 
	 *
	 *
	 */
	@Override
	public void modifierStatutsPrets() throws ResultNotFoundException {

		ArrayList<Pret> prets = (ArrayList<Pret>) afficherPrets();
		if (!prets.isEmpty()) {
			for (int i = 0; i < prets.size(); i++) {
				modifierStatut(prets.get(i).getId());
			}
		}
	}

	/*
	 * Modifier le statut d'un Pret en statut remis ou statut prolonge en fonction
	 * du String methode
	 * 
	 * @param id du pret
	 * 
	 * @param String methode equals a remise on
	 *
	 *
	 */
	@Override
	@Transactional
	public void modifierPret(Long idPret, String methode) throws ResultNotFoundException, EntityAlreadyExistException {
		Optional<Pret> pret = pretRepository.findById(idPret);

		if (!pret.isPresent()) {
			throw new ResultNotFoundException("Ce pret n'existe pas");
		}
		Optional<Livre> livre = livreRepository.findByCodeLivre(pret.get().getLivre().getCodeLivre());
		if (!livre.isPresent()) {
			throw new ResultNotFoundException("Ce livre n'existe pas");
		}

		if (pret.get().getStatut().equals(remis) || pret.get().getStatut().equals(depasse)) {
			throw new EntityAlreadyExistException("Le pret ne peut pas etre prolongé");
		}
		if (methode.equals("remise") && !pret.get().getStatut().equals(enAttente)) {
			pret.get().setStatut(remis);
			pret.get().setDateDeRendu(new Date());
			if (livre.get().getNombreExemplaire() < 1 && livre.get().getListeDattente().size() > 0) {

				emailService.envoyerLeMail(livre);

				Pret pretenAttente = trouverPretEnAttente(livre.get());

				connectionApiService.connectApiTimer(pretenAttente.getId());
			}
			livre.get().setNombreExemplaire(livre.get().getNombreExemplaire() + 1);
			livre.get().setDateDeRetour(null);
			modifierLesPositionsDesPretsEnListeDattentes(livre.get().getCodeLivre(), pret.get().getPosition());
			pretRepository.saveAndFlush(pret.get());

		} else if (!pret.get().getStatut().equals(enAttente)) {
			pret.get().setStatut(prolonge);
			pret.get().setDateDeFin(dateService.modifierDate(pret.get().getDateDeFin(), time));
			pretRepository.saveAndFlush(pret.get());
			livre.get().setDateDeRetour(modifierLaDateDeRetour(livre.get(), remis));
		} else {
			throw new EntityAlreadyExistException("Ce pret ne peu pas etre modifier par le méthode modifier pret");
		}

		livreRepository.saveAndFlush(livre.get());
	}

	@Override
	public void verifierPrêt(Long idPret) throws ResultNotFoundException, EntityAlreadyExistException, BadException {
		Optional<Pret> pret = pretRepository.findById(idPret);

		if (!pret.isPresent()) {
			throw new ResultNotFoundException("le pret n'existe pas");
		}
		Optional<Livre> livre = livreRepository.findByNom(pret.get().getLivre().getNom());

		if (!livre.isPresent()) {
			throw new ResultNotFoundException("le livre n'existe pas");
		}

		if (pret.get().getStatut().equals(enAttente)) {
			this.supprimerPret(idPret, Optional.of(enAttente));

			livre.get().getListeDattente().remove(0);

			livreRepository.saveAndFlush(livre.get());
			logger.info("le pret a ete supprime ");
			if (livre.get().getListeDattente().size() >= 1) {
				emailService.envoyerLeMail(livre);
				Pret pretenAttente = trouverPretEnAttente(livre.get());

				connectionApiService.connectApiTimer(pretenAttente.getId());

				logger.info(" envoie mail personne suivante");

			}

		} else if (livre.get().getListeDattente().size() != 0) {
			if (pret.get().getUtilisateur().getMail() == livre.get().getListeDattente().get(0)) {
				livre.get().getListeDattente().remove(0);
				logger.info("le pret a ete complété");
			} else {
				logger.info("ce pret n'est pas en rapport avec un pret en attente");

			}
			livreRepository.saveAndFlush(livre.get());

		} else {
			throw new EntityAlreadyExistException("le pret n'a pas e étre complété");
		}

	}

	public Pret trouverPretEnAttente(Livre livre) throws ResultNotFoundException {
		List<Pret> pretsEnAttente = pretRepository.findByStatutAndLivre(enAttente, livre);
		if (pretsEnAttente.isEmpty()) {
			throw new ResultNotFoundException("il n'y a pas de livre en attente");
		}
		Pret pret = pretsEnAttente.get(0);

		return pret;
	}

	private Date modifierLaDateDeRetour(Livre livre, String statut) throws ResultNotFoundException {

		List<Pret> listPret = pretRepository.findLivreandStatutNotByOrderByDateDeFinAsc(livre, statut);

		Date date = listPret.get(0).getDateDeFin();
		return date;

	}

<<<<<<< HEAD
	private void modifierLesPositionsDesPretsEnListeDattentes(Long idLivre, int positionLivreSupprime)
			throws ResultNotFoundException {
=======
	@Override
	public void modifierLesPositionsDesPretsEnListeDattentes(Long idLivre) throws ResultNotFoundException {
>>>>>>> feature/tests
		Optional<Livre> livre = livreRepository.findById(idLivre);
		if (!livre.isPresent()) {
			throw new ResultNotFoundException("le livre est introuvable");
		}
		List<String> listMail = livre.get().getListeDattente();
		if (livre.get().getNombreListeDattente() > 1) {
			for (int i = 0; i < listMail.size(); i++) {
				Optional<Utilisateur> utilisateur = utilisateurRepository.findByMail(listMail.get(i));
				if (!utilisateur.isPresent()) {
					throw new ResultNotFoundException("l'utilisateur est introuvable");
				}
				Optional<Pret> pret = pretRepository.findByUtilisateurAndLivreAndStatut(utilisateur.get(), livre.get(),
						enAttente);
				if (positionLivreSupprime < pret.get().getPosition()) {
					pret.get().setPosition(pret.get().getPosition() - 1);
				}
				pretRepository.saveAndFlush(pret.get());
				livreRepository.saveAndFlush(livre.get());
			}
		}

	}

}