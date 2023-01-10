package GainFunction;

import java.util.ArrayList;

import CoalitionGame.Type;
import Tools.Tools;

public class TwoSkills {
	
	// chaque chasseur et p�cheur rapporte un gain suppl�mentaire � la coalition, peut d'int�r�t c'est toujours la grande coalition qui se forme.
	public static int ChasseurPecheur(int[] types, ArrayList<Type> listType, int[] poids) {
		int res = 0;
		for ( Type t : listType ) {
			res += poids[t.getNum()] * types[t.getNum()];
		}
		return res;
	}
	
	// c'est trois fois la m�me, pas beaucoup de possibilit�s � deux types

	// un nombre optimal de tireurs et de batteurs par coalition est d�fini par patronIdeal (en fonction de l'espace de chasse par exemple) 
	public static int minTireurBatteur(int[] types, ArrayList<Type> listType, int[] patronIdeal) {
		int res = 1;
		for ( Type t : listType ) {
			res = res * Tools.min(patronIdeal[t.getNum()],types[t.getNum()]);
		}
		return res;
	}
	
	// un nombre optimal de tireurs et de pisteurs par coalition est d�fini par patronIdeal (en fonction du nombre de cible/proie par exemple) 
	public static int minTireurPisteur(int[] types, ArrayList<Type> listType, int[] patronIdeal) {
		int res = 1;
		for ( Type t : listType ) {
			res = res * Tools.min(patronIdeal[t.getNum()],types[t.getNum()]);
		}
		return res;
	}
	
	// un nombre optimal de tireurs et de revendeurs par coalition est d�fini par patronIdeal (en fonction du r�seaux du revendeur par exemple) 
	public static int minTireurRevendeur(int[] types, ArrayList<Type> listType, int[] patronIdeal) {
		int res = 1;
		for ( Type t : listType ) {
			res = res * Tools.min(patronIdeal[t.getNum()],types[t.getNum()]);
		}
		return res;
	}
	
	// trappeur/traqueur : alliance sans int�r�t

}
