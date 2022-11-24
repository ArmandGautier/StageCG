package ADD;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import CoalitionGame.Player;
import CoalitionGame.Type;
import Tools.Tools;

public class GenerationOfADDwithType {
	
	/*
	 * On génére un ADD (sans supprimer les noeuds inutiles) à partir d'un nombre de joueur (variable), d'une liste de compétences possibles 
	 * et d'une méthode calculant le gain d'une coalition en fonction du nombre de memebre de chaque type.
	 */
	/**
	 * @param nbPlayer
	 * @param nameOfSkills, array of skill's name
	 * @param method, function to compute the gain of a coalition
	 * @param weight, array of weight for every different skill
	 * @return the representation of a coalition game in ADD
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static ADD<Player> createADDandPlayer(int nbPlayer, ArrayList<String> nameOfSkills, Method method, int[] weight, boolean orderedVar, boolean bis) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ArrayList<Player> listPlayer = new ArrayList<Player>(nbPlayer);
		ArrayList<Type> listType = new ArrayList<Type>(nameOfSkills.size());
		Tools.generatePlayerWithType(nbPlayer, nameOfSkills, listPlayer, listType);
		
		if (orderedVar) {
		
			// on trie nos joueurs pour qu'ils soient rangés par type, du type comptant le plus de joueur à celui en comptant le moins
			// objectif : essayer d'optimiser la taille de l'ADD
			
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
			
			/*
			for (int j=0; j<nbPlayer; j++) {
				System.out.println("Type du joueur "+j+" : "+listPlayer.get(j).getType());
			}
			*/
			
		}

		HashMap<Integer,HashMap<Integer,Node>> nodes = new HashMap<Integer,HashMap<Integer,Node>>();
		for (int i=0; i<=nbPlayer; i++) {
			HashMap<Integer,Node> h = new HashMap<Integer,Node>();
			nodes.put(i, h);
		}
		int[] types = new int[nameOfSkills.size()];
		Arrays.fill(types, 0);
		ArrayList<Integer> l = new ArrayList<Integer>();
		Node racine;
		if (bis)
			racine = creationBis(listPlayer,listType,0,types,nodes,method,weight,l);
		else
			racine = creation(listPlayer,0,types,nodes,method,listType,weight,l);
		ADD<Player> add = new ADD<Player>(nodes,racine,listPlayer);
		
		return add;
	}
	
	/**
	 * @param nbPlayer
	 * @param nameOfSkills, array of skill's name
	 * @param method, function to compute the gain of a coalition
	 * @param weight, array of weight for every different skill
	 * @return the representation of a coalition game in ADD
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static ADD<Player> createADDandPlayerOtherOrder(int nbPlayer, ArrayList<String> nameOfSkills, Method method, int[] weight, boolean orderedVar) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ArrayList<Player> listPlayer = new ArrayList<Player>(nbPlayer);
		ArrayList<Type> listType = new ArrayList<Type>(nameOfSkills.size());
		Tools.generatePlayerWithType(nbPlayer, nameOfSkills, listPlayer, listType);
		
		if (orderedVar) {
		
			// on trie nos joueurs pour qu'ils soient rangés par type, du type comptant le plus de joueur à celui en comptant le moins
			// objectif : essayer d'optimiser la taille de l'ADD
			
			listPlayer.sort(new Comparator<Player>() {
	            public int compare(Player p1, Player p2) {
	            	int nb1 = p1.getType().getNumberPlayerOfThisType();
	            	int nb2 = p2.getType().getNumberPlayerOfThisType();
	            	if (nb1==nb2) {
	            		return p2.getType().getName().compareTo(p1.getType().getName());
	            	}
	                return nb1-nb2;
	            }
			});
			
			listType.sort(new Comparator<Type>() {
				public int compare(Type t1, Type t2) {
	            	int nb1 = t1.getNumberPlayerOfThisType();
	            	int nb2 = t2.getNumberPlayerOfThisType();
	            	if (nb1==nb2) {
	            		return t2.getName().compareTo(t1.getName());
	            	}
	            	return nb1-nb2;
				}
			});
			
			/*
			for (int j=0; j<nbPlayer; j++) {
				System.out.println("Type du joueur "+j+" : "+listPlayer.get(j).getType());
			}
			*/
			
		}

		HashMap<Integer,HashMap<Integer,Node>> nodes = new HashMap<Integer,HashMap<Integer,Node>>();
		for (int i=0; i<=nbPlayer; i++) {
			HashMap<Integer,Node> h = new HashMap<Integer,Node>();
			nodes.put(i, h);
		}
		int[] types = new int[nameOfSkills.size()];
		Arrays.fill(types, 0);
		ArrayList<Integer> l = new ArrayList<Integer>();
		
		Node racine = creation(listPlayer,0,types,nodes,method,listType,weight,l);
		ADD<Player> add = new ADD<Player>(nodes,racine,listPlayer);
		
		return add;
	}
	
	/*
	 * On génére un ADD (sans supprimer les noeuds inutiles) à partir d'une liste de joueur (variable), d'une liste de compétences
	 * et d'une méthode calculant le gain d'une coalition en fonction du nombre de memebre de chaque type.
	 */
	/**
	 * @param listType , the list of different type
	 * @param listPlayer, the list of player
	 * @param method, function to compute the gain of a coalition
	 * @param weight, array of weight for every different skill, the weight define the ideal number of player in a coalition
	 * @return the representation of a coalition game in ADD
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static ADD<Player> createADDwithPlayer(ArrayList<Type> listType, ArrayList<Player> listPlayer, Method method, int[] weight, boolean orderedVar, boolean bis) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if (orderedVar) {
		
			// on trie nos joueurs pour qu'ils soient rangés par type, du type comptant le plus de joueur à celui en comptant le moins
			// objectif : essayer d'optimiser la taille de l'ADD
			
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
			
			/*for (Type type : listType) {
				System.out.println(type.getName()+" "+type.getNumberPlayerOfThisType());
			}
			
			for (int j=0; j<listPlayer.size(); j++) {
				System.out.println("Type du joueur "+j+" : "+listPlayer.get(j).getType().getName());
			}*/
			
		}

		HashMap<Integer,HashMap<Integer,Node>> nodes = new HashMap<Integer,HashMap<Integer,Node>>();
		for (int i=0; i<=listPlayer.size(); i++) {
			HashMap<Integer,Node> h = new HashMap<Integer,Node>();
			nodes.put(i, h);
		}
		int[] types = new int[listType.size()];
		Arrays.fill(types, 0);
		ArrayList<Integer> l = new ArrayList<Integer>();
		Node racine;
		if (bis)
			racine = creationBis(listPlayer,listType,0,types,nodes,method,weight,l);
		else
			racine = creation(listPlayer,0,types,nodes,method,listType,weight,l);
		ADD<Player> add = new ADD<Player>(nodes,racine,listPlayer);
		
		return add;
	}
	
	/**
	 * @param listType , the list of different type
	 * @param listPlayer, the list of player
	 * @param method, function to compute the gain of a coalition
	 * @param weight, array of weight for every different skill, the weight define the ideal number of player in a coalition
	 * @return the representation of a coalition game in ADD
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static ADD<Player> createADDwithPlayerOtherOrder(ArrayList<Type> listType, ArrayList<Player> listPlayer, Method method, int[] weight, boolean orderedVar) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if (orderedVar) {
		
			// on trie nos joueurs pour qu'ils soient rangés par type, du type comptant le plus de joueur à celui en comptant le moins
			// objectif : essayer d'optimiser la taille de l'ADD
			
			listPlayer.sort(new Comparator<Player>() {
	            public int compare(Player p1, Player p2) {
	            	int nb1 = p1.getType().getNumberPlayerOfThisType();
	            	int nb2 = p2.getType().getNumberPlayerOfThisType();
	            	if (nb1==nb2) {
	            		return p2.getType().getName().compareTo(p1.getType().getName());
	            	}
	                return nb1-nb2;
	            }
			});
			
			listType.sort(new Comparator<Type>() {
				public int compare(Type t1, Type t2) {
	            	int nb1 = t1.getNumberPlayerOfThisType();
	            	int nb2 = t2.getNumberPlayerOfThisType();
	            	if (nb1==nb2) {
	            		return t2.getName().compareTo(t1.getName());
	            	}
	            	return nb1-nb2;
				}
			});
			
			/*
			for (int j=0; j<listPlayer.size(); j++) {
				System.out.println("Type du joueur "+j+" : "+listPlayer.get(j).getType().getName());
			}
			*/
		}

		HashMap<Integer,HashMap<Integer,Node>> nodes = new HashMap<Integer,HashMap<Integer,Node>>();
		for (int i=0; i<=listPlayer.size(); i++) {
			HashMap<Integer,Node> h = new HashMap<Integer,Node>();
			nodes.put(i, h);
		}
		int[] types = new int[listType.size()];
		Arrays.fill(types, 0);
		ArrayList<Integer> l = new ArrayList<Integer>();
		
		Node racine = creation(listPlayer,0,types,nodes,method,listType,weight,l);
		ADD<Player> add = new ADD<Player>(nodes,racine,listPlayer);
		
		return add;
	}
	
	/*
	 * Fonction de création de l'ADD pour tout nombre de type et de joueurs 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Node creation(ArrayList<Player> listPlayer, int indicePlayer, int[] types, HashMap<Integer,HashMap<Integer,Node>> nodes, Method method, ArrayList<Type> listType, int[] weight, ArrayList<Integer> nbNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		// cas où on est sur une feuille
		
		if (indicePlayer==listPlayer.size()) {
			
			int val = (int) method.invoke(null,types,listType,weight);
			if ( nodes.get(indicePlayer).containsKey(val)) {
				return nodes.get(indicePlayer).get(val);
			}
			Node<?> curr = new Node(nbNode.size(),val,true);
			nbNode.add(1);
			HashMap<Integer,Node> h = nodes.get(indicePlayer);
			h.put(val, curr);
			nodes.put(indicePlayer,h);
			return curr;
		}
		
		// cas où on est sur un noeud interne 
		
		int[] copie = types.clone();
		
		Node leftChild = creation(listPlayer,indicePlayer+1,copie,nodes,method,listType,weight,nbNode);
		
		int type = listPlayer.get(indicePlayer).getType().getNum();
		types[type]++;
		
		Node rightChild = creation(listPlayer,indicePlayer+1,types,nodes,method,listType,weight,nbNode);
		
		// On vérifie si ce Noeud existe déjà 
		
		Node curr = new Node(nbNode.size(),listPlayer.get(indicePlayer),listPlayer.get(indicePlayer).getType());
		curr.setLeftChild(leftChild);
		curr.setRightChild(rightChild);
		
		Integer key = curr.hashCode();
		
		if ( nodes.get(indicePlayer).containsKey(key)) {
			return nodes.get(indicePlayer).get(key);
		}
		
		HashMap<Integer,Node> h = nodes.get(indicePlayer);
		h.put(key, curr);
		nodes.put(indicePlayer,h);
		nbNode.add(1);
		return curr;
	}
	
	/*
	 * Fonction de création de l'ADD pour tout nombre de type et de joueurs 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Node creationBis(ArrayList<Player> listPlayer, ArrayList<Type> listType, int indicePlayer, int[] types, HashMap<Integer,HashMap<Integer,Node>> nodes, Method method, int[] weight, ArrayList<Integer> nbNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		// cas où on est sur une feuille
		
		
		
		if (indicePlayer==listPlayer.size()) {
			
			int val = (int) method.invoke(null,types,listType,weight);
			if ( nodes.get(indicePlayer).containsKey(val)) {
				return nodes.get(indicePlayer).get(val);
			}
			Node<?> curr = new Node(nbNode.size(),val,true);
			nbNode.add(1);
			HashMap<Integer,Node> h = nodes.get(indicePlayer);
			h.put(val, curr);
			nodes.put(indicePlayer,h);
			return curr;
		}
		
		Integer key=0;
		String s = "";
		for (int i=0; i<types.length;i++) {
			if (weight[i] < types[i])
				s += weight[i] + " joueur de type " + listType.get(i).getName();
			else 
				s += types[i] + " joueur de type " + listType.get(i).getName();
		}
		key = s.hashCode();
		
		if ( nodes.get(indicePlayer).containsKey(key)) {
			return nodes.get(indicePlayer).get(key);
		}
		
		// cas où on est sur un noeud interne 
		
		int[] copie = types.clone();
		int type = listPlayer.get(indicePlayer).getType().getNum();
		Node leftChild;
		Node rightChild;
		
		leftChild = creationBis(listPlayer,listType,indicePlayer+1,copie,nodes,method,weight,nbNode);
		
		types[type]++;
		
		if (types[type] == weight[type]) {
			int nbPlayerOfType = listPlayer.get(indicePlayer).getType().getNumberPlayerOfThisType();
			int nbPlayerOfPrecedentType = 0;
			for (Type t : listType) {
				if ( t.getNum()==type) {
					//System.out.println("type courrant : "+t.getName());
					break;
				}
				//System.out.println("type = "+t.getName()+" nbPlayer de ce type = "+t.getNumberPlayerOfThisType());
				nbPlayerOfPrecedentType+=t.getNumberPlayerOfThisType();
			}
			int nbPlayerPassed = (indicePlayer+1) - nbPlayerOfPrecedentType;
			int x = nbPlayerOfType - nbPlayerPassed;
			/*System.out.println("NPOT : "+nbPlayerOfType);
			System.out.println("NPOPT : "+nbPlayerOfPrecedentType);
			System.out.println("IP : "+indicePlayer);
			System.out.println("NPP : "+nbPlayerPassed);
			System.out.println("x : "+x);*/
			rightChild = creationBis(listPlayer,listType,indicePlayer+x+1,types,nodes,method,weight,nbNode);
		}
		else {
			rightChild = creationBis(listPlayer,listType,indicePlayer+1,types,nodes,method,weight,nbNode);
		}
		
		if (rightChild.equals(leftChild)) {
			return leftChild;
		}
		
		Node curr = new Node(nbNode.size(),listPlayer.get(indicePlayer),listPlayer.get(indicePlayer).getType());
		curr.setLeftChild(leftChild);
		curr.setRightChild(rightChild);
		
		for ( Node n : nodes.get(indicePlayer).values()) {
			if (curr.equals(n)) {
				return n;
			}
		}
		
		HashMap<Integer,Node> h = nodes.get(indicePlayer);
		h.put(key, curr);
		nodes.put(indicePlayer,h);
		nbNode.add(1);
		
		return curr;
	}

}
