package GainFunction;

import java.util.ArrayList;

import CoalitionGame.Type;
import Tools.Tools;

public class ThreeSkills {
	
	//les batteurs rabattent les bêtes vers les tireurs, les revendeurs maximisent la revente
	public static int TireurBatteurRevendeur(int[] types, ArrayList<Type> listType, int[] patronIdeal) {
		
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
						System.out.println("le nom d'un Type n'est pas compatible avec cette méthode");
						return -1;
					}
				}
			}
		}
		
		int res = Tools.min(patronIdeal[indiceTireur],types[indiceTireur]) * Tools.min(patronIdeal[indiceBatteur],types[indiceBatteur]) * (1+Tools.min(patronIdeal[indiceRevendeur],types[indiceRevendeur]));
		
		return res;
	}
	
	//tireur et pisteur non compatible et revendeur maximisent le gain
	public static int TireurPisteurRevendeur(int[] types, ArrayList<Type> listType, int[] patronIdeal) {
		
		int indiceTireur = 0;
		int indicePisteur = 0;
		int indiceRevendeur = 0;
		
		for ( Type t : listType ) {
			if ( t.getName() == "Revendeur") {
				indiceRevendeur = t.getNum();
			}
			else {
				if ( t.getName() == "Pisteur") {
					indicePisteur = t.getNum();
				}
				else {
					if ( t.getName() == "Tireur") {
						indiceTireur = t.getNum();
					}
					else {
						System.out.println("le nom d'un Type n'est pas compatible avec cette méthode");
						return -1;
					}
				}
			}
		}
		
		int res = Tools.max(Tools.min(patronIdeal[indiceTireur],types[indiceTireur]),Tools.min(patronIdeal[indicePisteur],types[indicePisteur])) * Tools.max(1,Tools.min(patronIdeal[indiceRevendeur],types[indiceRevendeur]));
		
		return res;
	}

	// les traqueurs posent leurs pièges dans la fôret, les tireurs se placents à la sortie de la fôret, les batteurs font fuire les bêtes vers les pièges et les tireurs
	public static int TireurBatteurTrappeur(int[] types, ArrayList<Type> listType, int[] patronIdeal) {
		
		int indiceTireur = 0;
		int indiceBatteur = 0;
		int indiceTrappeur = 0;
		
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
						System.out.println("le nom d'un Type n'est pas compatible avec cette méthode");
						return -1;
					}
				}
			}
		}
		
		int res = (Tools.min(patronIdeal[indiceTireur],types[indiceTireur]) + Tools.min(patronIdeal[indiceTrappeur],types[indiceTrappeur])) * Tools.min(patronIdeal[indiceBatteur],types[indiceBatteur]);
		
		return res;
	}
}
