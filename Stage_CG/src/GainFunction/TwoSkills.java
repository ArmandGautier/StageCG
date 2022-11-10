package GainFunction;

import java.util.ArrayList;

import CoalitionGame.Type;
import Tools.Tools;

public class TwoSkills {
	
	// chaque chasseur et pêcheur rapporte un gain supplémentaire à la coalition, peut d'intérêt c'est toujours la grande coalition qui se forme.
	public static int ChasseurPecheur(int[] types, ArrayList<Type> listType, int[] weight) {
		int res = 0;
		for ( Type t : listType ) {
			res += weight[t.getNum()] * types[t.getNum()];
		}
		return res;
	}
	
	// c'est trois fois la même, pas beaucoup de possibilités à deux types

	// un nombre optimal de tireurs et de batteurs par coalition est défini par weight (en fonction de l'espace de chasse par exemple) 
	public static int minTireurBatteur(int[] types, ArrayList<Type> listType, int[] weight) {
		int res = 1;
		for ( Type t : listType ) {
			res = res * Tools.min(weight[t.getNum()],types[t.getNum()]);
		}
		return res;
	}
	
	// un nombre optimal de tireurs et de pisteurs par coalition est défini par weight (en fonction du nombre de cible/proie par exemple) 
	public static int minTireurPisteur(int[] types, ArrayList<Type> listType, int[] weight) {
		int res = 1;
		for ( Type t : listType ) {
			res = res * Tools.min(weight[t.getNum()],types[t.getNum()]);
		}
		return res;
	}
	
	// un nombre optimal de tireurs et de revendeurs par coalition est défini par weight (en fonction du réseaux du revendeur par exemple) 
	public static int minTireurRevendeur(int[] types, ArrayList<Type> listType, int[] weight) {
		int res = 1;
		for ( Type t : listType ) {
			res = res * Tools.min(weight[t.getNum()],types[t.getNum()]);
		}
		return res;
	}
	
	// trappeur/traqueur : alliance sans intérêt

}
