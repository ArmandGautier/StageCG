package CoalitionGame;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class Coalition implements Comparable<Coalition> {
	
	TreeSet<Player> listPlayer = new TreeSet<Player>();
	
	/**
	 * @param listPlayer
	 */
	public Coalition(Collection<Player> listPlayer) {
		this.listPlayer.addAll(listPlayer);
	}

	/**
	 * @return the listPlayer
	 */
	public TreeSet<Player> getListPlayer() {
		return listPlayer;
	}

	@Override
	public int compareTo(Coalition o) {
		
		if ( this.listPlayer.size() < o.getListPlayer().size()) {
			return -1;
		}
		if ( this.listPlayer.size() > o.getListPlayer().size()) {
			return 1;
		}
		
		Iterator<Player> i1 = listPlayer.iterator();
		Iterator<Player> i2 = o.getListPlayer().iterator();
		
		while (i1.hasNext() && i2.hasNext()) {
			Player p1 = i1.next();
			Player p2 = i2.next();
			int compare = p1.compareTo(p2);
			if ( compare != 0 ) {
				return compare;
			}
		}
		return 0;
	}
	
	@Override
	public String toString() {
		String res = "";
		for (Player p : this.listPlayer) {
			res += p.toString()+" ";
		}
		return res;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if ( o instanceof Coalition ) {
			
			Coalition c = (Coalition) o;
			
			if ( this.listPlayer.size() != c.getListPlayer().size()) {
				return false;
			}
			
			for ( Player p : this.listPlayer) {
				if ( ! c.getListPlayer().contains(p)) {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		int res = 0;
		for ( Player p : this.listPlayer) {
			res += p.hashCode();
		}
		return res;
	}
	
}
