package CoalitionGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import Tools.Tools;

public class CoalitionGame {
	
	ArrayList<Player> listPlayer = new ArrayList<Player>();
	TreeMap<Coalition,Double> nu = new TreeMap<Coalition,Double>();
	
	/**
	 * @param setPlayer
	 * @param nu
	 */
	public CoalitionGame(ArrayList<Player> listPlayer, TreeMap<Coalition,Double> nu) {
		this.listPlayer = listPlayer;
		this.nu = nu;
	}

	/**
	 * @return the listPlayer
	 */
	public ArrayList<Player> getListPlayer() {
		return listPlayer;
	}

	/**
	 * @return the function nu
	 */
	public TreeMap<Coalition, Double> getNu() {
		return nu;
	}
	
	public ArrayList<Double> computeShapley() {
		ArrayList<Double> res = new ArrayList<Double>();
		int nbPermutation = Tools.factorial(this.listPlayer.size());
		HashSet<ArrayList<Player>> listPermutation = Tools.createPermutation(this.listPlayer);
		
		for (int i=0; i<this.listPlayer.size(); i++) {
			double val = 0;
			for ( ArrayList<Player> l : listPermutation) {
				val += marginalContribution(this.listPlayer.get(i),l);
			}
			res.add(val/nbPermutation);
		}
		return res;
	}

	private double marginalContribution(Player p, ArrayList<Player> permutation) {
		
		// On créé une nouvelle liste pour pouvoir supprimer des éléments sans impacter la liste "permutation"
		ArrayList<Player> l = new ArrayList<Player>();
		l.addAll(permutation);
		
		int indexPlayer = l.indexOf(p);
		
		// On supprime les joueurs qui arrive après p dans la permutation
		
		for (int i=l.size()-1; i>indexPlayer; i--) {
			l.remove(i);
		}
		
		Coalition withP = new Coalition(l);
		l.remove(indexPlayer);
		Coalition withoutP = new Coalition(l);
		
		double v1 = this.nu.get(withP);
		double v2 = this.nu.get(withoutP);
		
		return v1 - v2;
	}
	
}
