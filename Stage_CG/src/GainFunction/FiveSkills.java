package GainFunction;

import java.util.ArrayList;

import CoalitionGame.Type;
import Tools.Tools;

public class FiveSkills {
	
	// les traqueurs posent leurs pièges dans la fôret, les tireurs se placents à la sortie de la fôret, 
	// les batteurs font fuire les bêtes vers les pièges et les tireurs, les revendeurs permettent d'optimiser la vente du gibier
	// les pêcheurs apportent un petit plus
	public static int TireurBatteurTrappeurRevendeurPecheur(int[] types, ArrayList<Type> listType, int[] weight) {
		
		int indiceTireur = 0;
		int indiceBatteur = 0;
		int indiceTrappeur = 0;
		int indiceRevendeur = 0;
		int indicePecheur = 0;
		
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
							if ( t.getName() == "Pecheur") {
								indicePecheur = t.getNum();
							}
							else {
								System.out.println("le nom d'un Type n'est pas compatible avec cette méthode");
								return -1;
							}
						}
					}
				}
			}
		}
		
		int res = 2*Tools.min(weight[indicePecheur],types[indicePecheur]) + (4*Tools.min(weight[indiceTireur],types[indiceTireur]) + 3*Tools.min(weight[indiceTrappeur],types[indiceTrappeur])) * Tools.min(weight[indiceBatteur],types[indiceBatteur]) * (1+Tools.min(weight[indiceRevendeur],types[indiceRevendeur])); 
		
		return res;
	}

}
