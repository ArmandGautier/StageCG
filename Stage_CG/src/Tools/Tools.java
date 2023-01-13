package Tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import CoalitionGame.Coalition;
import CoalitionGame.Player;
import CoalitionGame.Type;

public class Tools {
	
	public static int puissance2(int n) { 
		  if (n == 1) {
		     return 2;
		  }
		  else {
		     return 2 * puissance2(n - 1);
		  }
		}
	
	public static int factorial(int n) { 
		  if (n == 0 || n == 1) {
			  return 1;
		   }
		  else {
		      return n * factorial(n - 1);
		  }
		}

	/**
	 * @param size
	 * @param listPlayer
	 * @return all possible coalition of size "size"
	 */
	public static Collection<Coalition> createCoalitionOfSize(int size, ArrayList<Player> listPlayer) {
		TreeSet<Coalition> listCoalition = new TreeSet<Coalition>();
		
		if ( size == 0 ) {
			TreeSet<Player> temp = new TreeSet<Player>();
			listCoalition.add(new Coalition(temp));
		}
		
		else if (size == 1) {
			for (int i=0; i<listPlayer.size(); i++) {
				TreeSet<Player> temp = new TreeSet<Player>();
				temp.add(listPlayer.get(i));
				listCoalition.add(new Coalition(temp));
			}
		}
		
		else if (size == listPlayer.size()) {
			TreeSet<Player> temp = new TreeSet<Player>();
			for (int i=0; i<listPlayer.size(); i++) {
				temp.add(listPlayer.get(i));
			}
			listCoalition.add(new Coalition(temp));
		}
		
		else {
			for (int i=0; i<=listPlayer.size()-size; i++) {
				TreeSet<Player> temp = new TreeSet<Player>();
				temp.add(listPlayer.get(i));
				function(i,size,temp,listCoalition,listPlayer);
			}
		}
		
		return listCoalition;
	}
	
	// pas opti du tout : il faudrait faire un algo itératif plutot que recursif
	
	private static void function(int i, int size, TreeSet<Player> list, TreeSet<Coalition> listCoalition, ArrayList<Player> listPlayer) {
		if ( size == list.size() ) {
			listCoalition.add(new Coalition(list));
		}
		else {
			for (int j=i+1; j< listPlayer.size(); j++) {
				
				// on créé un nouveau set dans lequel on ajoutera le joueur i
				TreeSet<Player> temp = new TreeSet<Player>();
				temp.addAll(list);
				
				temp.add(listPlayer.get(j));
				function(j,size,temp,listCoalition,listPlayer);
			}
		}
	}
	
	/**
	 * @param listPlayer
	 * @return all possible permutations for the set "listPlayer" using the Heap algorithm
	 */
	public static HashSet<ArrayList<Player>> createPermutation(ArrayList<Player> listPlayer) {
		
		HashSet<ArrayList<Player>> listPermutation = new HashSet<ArrayList<Player>>();
		
		// nouvelle liste sur laquelle on effectuera les permutations
		ArrayList<Player> list = new ArrayList<Player>(); 
		list.addAll(listPlayer);
		
		// on créé une nouvelle liste pour chaque nouvelle permutation, y compris l'initiale, pour éviter les conflits de mémoire commune
		ArrayList<Player> permutation = new ArrayList<Player>(); 
		permutation.addAll(list);
		listPermutation.add(permutation);
		
		int n = listPlayer.size();
		int[] compteur = new int[n];
		
		// algo de Heap
		
		int i=0;
		
		while (i<n) {
			if (compteur[i] < i) {
				if (i%2 == 0) {
					Player temp = list.get(0);
					list.set(0, list.get(i));
					list.set(i, temp);
				}
				else {
					Player temp = list.get(compteur[i]);
					list.set(compteur[i], list.get(i));
					list.set(i, temp);
				}
				
			    permutation = new ArrayList<Player>(); 
			    permutation.addAll(list);
				listPermutation.add(permutation);
				
				compteur[i]++;
				i=0;
			}
			else {
				compteur[i]=0;
				i++;
			}
		}
		
		return listPermutation;
	}
	
	/**
	 * @param size
	 * @param listPlayer
	 * @return all possible coalition of size "size" with gain compute by method
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public static void createCoalitionOfSize(int size, ArrayList<Player> listPlayer, TreeMap<Coalition,Double> nu, Method method, ArrayList<Type> listType, int[] patronIdeal) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if ( size == 0 ) {
			TreeSet<Player> temp = new TreeSet<Player>();
			nu.put(new Coalition(temp), Tools.getValOfCoalition(temp, method,listType,patronIdeal));
		}
		
		else if (size == 1) {
			for (int i=0; i<listPlayer.size(); i++) {
				TreeSet<Player> temp = new TreeSet<Player>();
				temp.add(listPlayer.get(i));
				nu.put(new Coalition(temp), getValOfCoalition(temp,method,listType,patronIdeal));
			}
		}
		
		else if (size == listPlayer.size()) {
			TreeSet<Player> temp = new TreeSet<Player>();
			for (int i=0; i<listPlayer.size(); i++) {
				temp.add(listPlayer.get(i));
			}
			nu.put(new Coalition(temp), getValOfCoalition(temp,method,listType,patronIdeal));
		}
		
		else {
			for (int i=0; i<=listPlayer.size()-size; i++) {
				TreeSet<Player> temp = new TreeSet<Player>();
				temp.add(listPlayer.get(i));
				function(i,size,temp,listPlayer,nu,method,listType,patronIdeal);
			}
		}
	}

	// pas opti du tout : il faudrait faire un algo itératif plutot que recursif
	
	private static void function(int i, int size, TreeSet<Player> list, ArrayList<Player> listPlayer, TreeMap<Coalition,Double> nu, Method method, ArrayList<Type> listType, int[] patronIdeal) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if ( size == list.size() ) {
			nu.put(new Coalition(list), getValOfCoalition(list,method,listType,patronIdeal));
		}
		else {
			for (int j=i+1; j< listPlayer.size(); j++) {
				
				// on créé un nouveau set dans lequel on ajoutera le joueur i
				TreeSet<Player> temp = new TreeSet<Player>();
				temp.addAll(list);
				
				temp.add(listPlayer.get(j));
				function(j,size,temp,listPlayer,nu,method,listType,patronIdeal);
			}
		}
	}
	
	private static Double getValOfCoalition(TreeSet<Player> temp, Method method, ArrayList<Type> listType, int[] patronIdeal) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<Integer,Integer> mapOfType = new HashMap<Integer,Integer>();
		for ( Player p : temp) {
			if (mapOfType.containsKey(p.getType().getNum())) {
				Integer v = mapOfType.get(p.getType().getNum());
				v++;
				mapOfType.put(p.getType().getNum(), v);
			}
			else {
				mapOfType.put(p.getType().getNum(), 1);
			}
		}
		int[] tab = new int[listType.size()];
		for (Integer type : mapOfType.keySet()) {
			tab[type] = mapOfType.get(type);
		}
		int res = (int) method.invoke(null, tab, listType, patronIdeal);
		return (double) res;
	}
	
	public static double min(double x, double y) {
		if ( x<y )
			return x;
		return y;
	}

	public static int min(int x, int y) {
		if ( x<y )
			return x;
		return y;
	}

	public static int pow(int i, int j) {
		int res = 1;
		for (int k=0; k<j; k++) {
			res = res*i;
		}
		return res;
	}
	
	public static void generateListType(ArrayList<String> nameOfSkills, ArrayList<Type> listType) {
		// on crée la liste des Types, une compétence => un type
		int i=0;
		for ( String name : nameOfSkills) {
			listType.add(new Type(i,name));
			i++;
		}
	}
	
	public static void generateListPlayer(int nbPlayer, ArrayList<Player> listPlayer, ArrayList<Type> listType) {
		
		// on crée la liste des joueurs, on tire au hasard leur type
		
		Random random = new Random();
		
		for (int p=0; p<nbPlayer; p++) {
			int val = random.nextInt(0, listType.size());
			listPlayer.add(new Player(p,listType.get(val)));
			listType.get(val).addPlayer();
		}
	}
	
	public static void generatePlayerWithType(int nbPlayer, ArrayList<String> nameOfSkills, ArrayList<Player> listPlayer, ArrayList<Type> listType) {
		
		// on crée la liste des Types, une compétence => un type
		
		Tools.generateListType(nameOfSkills, listType);
		
		// on crée la liste des joueurs, on tire au hasard leur type
		
		Tools.generateListPlayer(nbPlayer, listPlayer, listType);
		
	}

	public static int max(int x, int y) {
		if ( x<y )
			return y;
		return x;
	}
	
	public static double abs(double x) {
		if ( x < 0) {
			return - x;
		}
		return x;
	}

	public static int[] generatePatronIdeal(ArrayList<Type> listType, int nbPlayer) {
		int[] res = new int[listType.size()];
		Random random = new Random();
		for (int i=0; i<listType.size(); i++) {
			res[i]=random.nextInt(1, nbPlayer);
		}
		return res;
	}

	public static double sum(double[] sol, ArrayList<Player> listPlayer, ArrayList<Player> varOrder) {
		double res = 0;
		for (Player o : listPlayer) {
			res += sol[varOrder.indexOf(o)];
		}
		return res;
	}

	/**
	 * @param listPlayer
	 * @return l'ensemble de toutes les coalitions possibles
	 */
	public static TreeSet<Coalition> createCoalition(ArrayList<Player> listPlayer) {
		TreeSet<Coalition> listCoalition = new TreeSet<Coalition>();
		for (int i=0; i<=listPlayer.size(); i++) {
			listCoalition.addAll(Tools.createCoalitionOfSize(i,listPlayer));
		}
		return listCoalition;
	}

	
	public static TreeMap<Coalition, Double> giveValue(TreeSet<Coalition> listCoalition) {
		TreeMap<Coalition,Double> nu = new TreeMap<Coalition,Double>();
		Scanner scan = new Scanner(System.in);
		for (Coalition c : listCoalition) {
			System.out.println(c.toString());
		    System.out.println("What is the gain for this coalition ? Please enter a number: ");
		    double number = scan.nextInt();
			nu.put(c,number);
		}
		scan.close();
		return nu;	
	}

}
