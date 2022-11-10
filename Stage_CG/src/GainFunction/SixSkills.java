package GainFunction;

import java.util.ArrayList;

import CoalitionGame.Type;
import Tools.Tools;

public class SixSkills {

	public static int TireurBatteurRevendeurWithXP(int[] types, ArrayList<Type> listType, int[] weight) {
		
		int indiceTireurNoXP = 0;
		int indiceBatteurNoXP = 0;
		int indiceRevendeurNoXP = 0;
		int indiceTireur = 0;
		int indiceBatteur = 0;
		int indiceRevendeur = 0;
		
		for ( Type t : listType ) {
			if ( t.getName() == "Revendeur") {
				indiceRevendeur = t.getNum();
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
						if ( t.getName() == "RevendeurNoXP") {
							indiceRevendeurNoXP = t.getNum();
						}
						else {
							if ( t.getName() == "BatteurNoXP") {
								indiceBatteurNoXP = t.getNum();
							}
							else {
								if ( t.getName() == "TireurNoXP") {
									indiceTireurNoXP = t.getNum();
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
		}
		
		// le nombre de tireur + si il y a au moins un tireur, le nombre de tireur sans XP, un tireur rapporte 5 fois plus qu'un tireur sans XP 
		int gainTireur = Tools.min(weight[indiceTireurNoXP],types[indiceTireurNoXP]) * Tools.min(1,types[indiceTireur]) + 5*Tools.min(weight[indiceTireur],types[indiceTireur]);
		// le nombre de batteur + le nombre de batteur sans XP, un batteur rapporte 2 fois plus qu'un batteur sans XP
		int gainBatteur = Tools.min(weight[indiceBatteurNoXP],types[indiceBatteurNoXP]) + 2*Tools.min(weight[indiceBatteur],types[indiceBatteur]);
		// le nombre de revendeur + le nombre de revendeur sans XP, un revendeur rapporte 5 fois plus qu'un revendeur sans XP
		int gainRevendeur = Tools.min(weight[indiceRevendeurNoXP],types[indiceRevendeurNoXP]) + 5*Tools.min(weight[indiceRevendeur],types[indiceRevendeur]);
		
		int res = (gainTireur*gainBatteur) * Tools.max(1, gainRevendeur);
				
		return res;
	}
	
}
