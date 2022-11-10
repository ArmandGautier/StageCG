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
	public static ADD<Player> createADDandPlayer(int nbPlayer, ArrayList<String> nameOfSkills, Method method, int[] weight, boolean orderedVar) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ArrayList<Player> listPlayer = new ArrayList<Player>(nbPlayer);
		ArrayList<Type> listType = new ArrayList<Type>(nameOfSkills.size());
		Tools.generatePlayerWithType(nbPlayer, nameOfSkills, listPlayer, listType);
		
		if (orderedVar) {
		
			// on trie nos joueurs pour qu'ils soient rangés par type, du type comptant le plus de joueur à celui en comptant le moins
			// objectif : essayer d'optimiser la taille de l'ADD
			
			listPlayer.sort(new Comparator<Player>() {
	            public int compare(Player p1, Player p2) {
	                return p2.getType().getNumberPlayerOfThisType() - p1.getType().getNumberPlayerOfThisType();
	            }
			});
			
			/*
			for (int j=0; j<nbPlayer; j++) {
				System.out.println("Type du joueur "+j+" : "+listPlayer.get(j).getType());
			}
			*/
			
		}

		HashMap<Integer,Node> nodes = new HashMap<Integer,Node>();
		int[] types = new int[nameOfSkills.size()];
		Arrays.fill(types, 0);
		
		Node racine = creation(listPlayer,0,types,nodes,method,listType,weight);
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
	                return p1.getType().getNumberPlayerOfThisType() - p2.getType().getNumberPlayerOfThisType();
	            }
			});
			
			/*
			for (int j=0; j<nbPlayer; j++) {
				System.out.println("Type du joueur "+j+" : "+listPlayer.get(j).getType());
			}
			*/
			
		}

		HashMap<Integer,Node> nodes = new HashMap<Integer,Node>();
		int[] types = new int[nameOfSkills.size()];
		Arrays.fill(types, 0);
		
		Node racine = creation(listPlayer,0,types,nodes,method,listType,weight);
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
	public static ADD<Player> createADDwithPlayer(ArrayList<Type> listType, ArrayList<Player> listPlayer, Method method, int[] weight, boolean orderedVar) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		System.out.println("On trie les variables");
		
		if (orderedVar) {
		
			// on trie nos joueurs pour qu'ils soient rangés par type, du type comptant le plus de joueur à celui en comptant le moins
			// objectif : essayer d'optimiser la taille de l'ADD
			
			listPlayer.sort(new Comparator<Player>() {
	            public int compare(Player p1, Player p2) {
	                return p2.getType().getNumberPlayerOfThisType() - p1.getType().getNumberPlayerOfThisType();
	            }
			});
			
			/*
			for (int j=0; j<listPlayer.size(); j++) {
				System.out.println("Type du joueur "+j+" : "+listPlayer.get(j).getType().getName());
			}
			*/
		}
		
		System.out.println("On lance la fonction récursive");

		HashMap<Integer,Node> nodes = new HashMap<Integer,Node>();
		int[] types = new int[listType.size()];
		Arrays.fill(types, 0);
		
		Node racine = creation(listPlayer,0,types,nodes,method,listType,weight);
		ADD<Player> add = new ADD<Player>(nodes,racine,listPlayer);
		
		System.out.println("add créé");
		
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
	                return p1.getType().getNumberPlayerOfThisType() - p2.getType().getNumberPlayerOfThisType();
	            }
			});
			
			/*
			for (int j=0; j<listPlayer.size(); j++) {
				System.out.println("Type du joueur "+j+" : "+listPlayer.get(j).getType().getName());
			}
			*/
		}

		HashMap<Integer,Node> nodes = new HashMap<Integer,Node>();
		int[] types = new int[listType.size()];
		Arrays.fill(types, 0);
		
		Node racine = creation(listPlayer,0,types,nodes,method,listType,weight);
		ADD<Player> add = new ADD<Player>(nodes,racine,listPlayer);
		
		return add;
	}
	
	/*
	 * Fonction de création de l'ADD pour tout nombre de type et de joueurs 
	 */
	private static Node creation(ArrayList<Player> listPlayer, int indicePlayer, int[] types, HashMap<Integer,Node> nodes, Method method, ArrayList<Type> listType, int[] weight) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		// cas où on est sur une feuille
		
		if (indicePlayer==listPlayer.size()) {
			
			int val = (int) method.invoke(null,types,listType,weight);
			if ( nodes.containsKey(val)) {
				return nodes.get(val);
			}
			Node curr = new Node(nodes.size(),val,true);
			nodes.put(val, curr);
			return curr;
		}
		
		// cas où on est sur un noeud interne 
		
		int[] copie = types.clone();
		
		Node leftChild = creation(listPlayer,indicePlayer+1,copie,nodes,method,listType,weight);
		
		int type = listPlayer.get(indicePlayer).getType().getNum();
		types[type]++;
		
		Node rightChild = creation(listPlayer,indicePlayer+1,types,nodes,method,listType,weight);
		
		// On vérifie si ce Noeud existe déjà 
		
		Node curr = new Node(nodes.size(),indicePlayer,listPlayer.get(indicePlayer).getType());
		curr.setLeftChild(leftChild);
		curr.setRightChild(rightChild);
		
		Integer key = curr.hashCode();
		
		if ( nodes.containsKey(key)) {
			return nodes.get(key);
		}
		
		nodes.put(key, curr);
		return curr;
	}

}
