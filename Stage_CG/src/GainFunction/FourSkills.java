package GainFunction;

import java.util.ArrayList;

import CoalitionGame.Type;
import Tools.Tools;

public class FourSkills {

	// les traqueurs posent leurs pièges dans la fôret, les tireurs se placents à la sortie de la fôret, 
	// les batteurs font fuire les bêtes vers les pièges et les tireurs, les revendeurs permettent d'optimiser la vente du gibier
	public static int TireurBatteurTrappeurRevendeur(int[] types, ArrayList<Type> listType, int[] patronIdeal) {
		
		int indiceTireur = 0;
		int indiceBatteur = 0;
		int indiceTrappeur = 0;
		int indiceRevendeur = 0;
		
		for ( Type t : listType ) {
			if ( t.getName() == "Trappeur") {
				indiceTrappeur = t.getNum();
			}
			else {
				if ( t.getName() == "Batteur") {
					indiceBatteur = t.getNum();
				}
				else {
					if ( t.getName() == "Tireur") {
						indiceTireur = t.getNum();
					}
					else {
						if ( t.getName() == "Revendeur") {
							indiceRevendeur = t.getNum();
						}
						else {
							System.out.println("le nom d'un Type n'est pas compatible avec cette méthode");
							return -1;
						}
					}
				}
			}
		}
		
		int res = (Tools.min(patronIdeal[indiceTireur],types[indiceTireur]) + Tools.min(patronIdeal[indiceTrappeur],types[indiceTrappeur])) * Tools.min(patronIdeal[indiceBatteur],types[indiceBatteur]) * (1+Tools.min(patronIdeal[indiceRevendeur],types[indiceRevendeur])); 
		
		return res;
	}

}
