package cm.objis.wtt.pharmacie.domaine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author thierry WADJI
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacieDto {
	
	private String nom;
	private String adresse;
	private String telephone;
	private int nbProduit;


}
