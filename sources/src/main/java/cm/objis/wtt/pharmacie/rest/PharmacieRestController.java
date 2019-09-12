package cm.objis.wtt.pharmacie.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.google.common.collect.Lists;

import cm.objis.wtt.pharmacie.domaine.dto.PharmacieDto;
import cm.objis.wtt.pharmacie.domaine.dto.ProduitDto;
import cm.objis.wtt.pharmacie.domaine.dto.ReponseDTO;
import cm.objis.wtt.pharmacie.domaine.entities.Pharmacie;
import cm.objis.wtt.pharmacie.domaine.entities.Produit;
import cm.objis.wtt.pharmacie.service.IService;

@RestController
@RequestMapping("/pharmacie/api")
public class PharmacieRestController {
	
	private final IService service;
	
	@Autowired
	public PharmacieRestController(IService service) {
		this.service = service;
	}
	
	@RequestMapping(value = "/liste-pharmacie", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PharmacieDto> listePharmacie() {
		
		// je récupère la liste de toutes les pharmacies
		final List<Pharmacie> mesPharmacies = service.listPharmacie();
		
		// Je transforme les pharmacies en pharmacieDTO (Data Transfer Object).
		final List<PharmacieDto> lesPharmaciesDtos = Lists.transform(mesPharmacies, 
				(Pharmacie input) -> { int nbProduit = input.getProduits().size();
										return new PharmacieDto(input.getNom(), input.getAdresse(), input.getTelephone(), nbProduit); 
									});
		
		return lesPharmaciesDtos;
	}
	
	@RequestMapping(value = "/creer-pharmacie", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReponseDTO enregistrePharmacie(@RequestBody PharmacieDto pharmacieDto) {
		
		if (service.pharmacieExiste(pharmacieDto.getNom()) != null) {
    		
    		return new ReponseDTO(0, "Une Pharmacie du même nom existe dejà !!");
    	} else {
    		
    		Pharmacie pharmacie = new Pharmacie(pharmacieDto.getNom(), pharmacieDto.getAdresse(), pharmacieDto.getTelephone());
    		service.enregistrerPharmacie(pharmacie);
    		
    		return new ReponseDTO(200, "Pharmacie enregistrée avec succès!");
    	}
    	
	}
	
	@RequestMapping(value = "/liste-produit/{nom}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProduitDto> listeProduitParPharmacie(@PathVariable("nom") String nomPharmacie) {
		
		List<Produit> produits = new ArrayList<Produit>();
		List<ProduitDto> lesProduitsDto = new ArrayList<ProduitDto>();
		
		Pharmacie pharmacie = service.pharmacieExiste(nomPharmacie);
		if(pharmacie == null) {
			//Je prépare un message d'erreur
			 
		} else {
			// Liste des produits de la pharmacie
			produits = service.listProduit(pharmacie);
			lesProduitsDto = Lists.transform(produits, 
					(Produit input) -> new ProduitDto(input.getReference(), input.getLibelle(), 
							input.getPrix(), input.getQuantite(), input.getPharmacie().getNom()));
		}
		
		return lesProduitsDto;
	}
	
	@RequestMapping(value = "/liste-produit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProduitDto> listeProduit() {
		
		// je récupère la Liste de tous les produits
		final List<Produit> mesProduits = service.listProduit();
		
		// Je transforme les produits en produitDTO Data Transfer Object.
		final List<ProduitDto> lesProduitsDto = Lists.transform(mesProduits, 
				(Produit input) -> new ProduitDto(input.getReference(), input.getLibelle(), 
						input.getPrix(), input.getQuantite(), input.getPharmacie().getNom()));
		
		return lesProduitsDto;
	}
	
	@RequestMapping(value = "/creer-produit/{nom}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReponseDTO enregistreProduit(@PathVariable("nom") String nomPharmacie, @RequestBody ProduitDto produitDto) {
		
		//Les valeurs de retour
		int codeReponse;
		String msgReponse;
				
		Produit produit = new Produit(produitDto.getReference(), produitDto.getLibelle(), produitDto.getPrix(), produitDto.getQuantite());
		Pharmacie pharmacie = service.pharmacieExiste(nomPharmacie);
		if(pharmacie == null) {
			codeReponse = 0; 
			msgReponse = "La pharmacie ("+ nomPharmacie +") n'existe pas !!";
		} else {
			produit.setPharmacie(pharmacie);
			service.enregistrerProduit(produit);
			
			codeReponse = 0; 
			msgReponse = "Produit enregistrée avec succès!";
		}
		
		return new ReponseDTO(codeReponse, msgReponse);
	}

}
