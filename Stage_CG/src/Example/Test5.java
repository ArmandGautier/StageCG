package Example;

import java.util.ArrayList;
import java.util.Comparator;

import CoalitionGame.Player;
import CoalitionGame.Type;
import Tools.GetSkill;
import Tools.Tools;

public class Test5 {

	public static void main(String[] args) {
		
		int nbPlayer = 7;
		int nbType = 6;
		ArrayList<Player> listPlayer = new ArrayList<Player>();
		ArrayList<Type> listType = new ArrayList<Type>();
		
		Tools.generatePlayerWithType(nbPlayer, GetSkill.getSkill(nbType), listPlayer, listType);
		
		listPlayer.sort(new Comparator<Player>() {
            public int compare(Player p1, Player p2) {
            	int nb1 = p1.getType().getNumberPlayerOfThisType();
            	int nb2 = p2.getType().getNumberPlayerOfThisType();
            	if (nb1==nb2) {
            		return p1.getType().getName().compareTo(p2.getType().getName());
            	}
                return nb2-nb1;
            }
		});
		
		listType.sort(new Comparator<Type>() {
			public int compare(Type t1, Type t2) {
            	int nb1 = t1.getNumberPlayerOfThisType();
            	int nb2 = t2.getNumberPlayerOfThisType();
            	if (nb1==nb2) {
            		return t1.getName().compareTo(t2.getName());
            	}
            	return nb2-nb1;
			}
		});
		
		for (Type type : listType) {
			System.out.println(type.getName()+" "+type.getNumberPlayerOfThisType()+" "+type.getName().hashCode());
		}
		
		for (int j=0; j<listPlayer.size(); j++) {
			System.out.println("Type du joueur "+j+" : "+listPlayer.get(j).getType().getName());
		}

	}

}
