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

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenerationOfADDwithType {
	
	/**
	 * @param nbPlayer
	 * @param nameOfSkills
	 * @param method
	 * @param patronIdeal
	 * @param orderedVar
	 * @return the representation of the ADD with HashMaps
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static ADD createADDandPlayer(int nbPlayer, ArrayList<String> nameOfSkills, Method method, int[] patronIdeal, boolean orderedVar) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
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
			
		}

		HashMap<Integer,HashMap<Integer,Node>> nodesTemp = new HashMap<Integer,HashMap<Integer,Node>>();
		HashMap<Integer,HashMap<Integer,Node>> nodesFinal = new HashMap<Integer,HashMap<Integer,Node>>();
		for (int i=0; i<=nbPlayer; i++) {
			HashMap<Integer,Node> h = new HashMap<Integer,Node>();
			nodesTemp.put(i, h);
			HashMap<Integer,Node> k = new HashMap<Integer,Node>();
			nodesFinal.put(i, k);
		}
		
		int[] types = new int[nameOfSkills.size()];
		Arrays.fill(types, 0);
		
		ArrayList<Integer> l = new ArrayList<Integer>();
		
		Node racine;
		if (orderedVar)
			racine = creationOrdered(listPlayer,listType,0,types,nodesTemp,nodesFinal,method,patronIdeal,l);
		else
			racine = creation(listPlayer,listType,0,types,nodesFinal,method,patronIdeal,l);
		
		ADD add = new ADD(nodesFinal,racine,listPlayer);
		return add;
	}
	
	/**
	 * @param nbPlayer
	 * @param nameOfSkills
	 * @param method
	 * @param patronIdeal
	 * @param orderedVar
	 * @return the representation of the ADD with HashMaps
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static ADD createADDandPlayerOtherOrder(int nbPlayer, ArrayList<String> nameOfSkills, Method method, int[] patronIdeal, boolean orderedVar) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ArrayList<Player> listPlayer = new ArrayList<Player>(nbPlayer);
		ArrayList<Type> listType = new ArrayList<Type>(nameOfSkills.size());
		Tools.generatePlayerWithType(nbPlayer, nameOfSkills, listPlayer, listType);
		
		if (orderedVar) {
			
			// on trie nos joueurs pour qu'ils soient rangés par type, du type comptant le moins de joueur à celui en comptant le plus
			// objectif : essayer d'optimiser la taille de l'ADD
			
			listPlayer.sort(new Comparator<Player>() {
	            public int compare(Player p1, Player p2) {
	            	int nb1 = p1.getType().getNumberPlayerOfThisType();
	            	int nb2 = p2.getType().getNumberPlayerOfThisType();
	            	if (nb1==nb2) {
	            		return -(p1.getType().getName().compareTo(p2.getType().getName()));
	            	}
	                return nb1-nb2;
	            }
			});
			
			listType.sort(new Comparator<Type>() {
				public int compare(Type t1, Type t2) {
	            	int nb1 = t1.getNumberPlayerOfThisType();
	            	int nb2 = t2.getNumberPlayerOfThisType();
	            	if (nb1==nb2) {
	            		return -(t1.getName().compareTo(t2.getName()));
	            	}
	            	return nb1-nb2;
				}
			});
			
		}

		HashMap<Integer,HashMap<Integer,Node>> nodesTemp = new HashMap<Integer,HashMap<Integer,Node>>();
		HashMap<Integer,HashMap<Integer,Node>> nodesFinal = new HashMap<Integer,HashMap<Integer,Node>>();
		for (int i=0; i<=nbPlayer; i++) {
			HashMap<Integer,Node> h = new HashMap<Integer,Node>();
			nodesTemp.put(i, h);
			HashMap<Integer,Node> k = new HashMap<Integer,Node>();
			nodesFinal.put(i, k);
		}
		
		int[] types = new int[nameOfSkills.size()];
		Arrays.fill(types, 0);
		
		ArrayList<Integer> l = new ArrayList<Integer>();
		
		Node racine;
		if (orderedVar)
			racine = creationOrdered(listPlayer,listType,0,types,nodesTemp,nodesFinal,method,patronIdeal,l);
		else
			racine = creation(listPlayer,listType,0,types,nodesFinal,method,patronIdeal,l);
		
		ADD add = new ADD(nodesFinal,racine,listPlayer);
		
		return add;
	}
	
	/**
	 * @param listType
	 * @param listPlayer
	 * @param method
	 * @param patronIdeal
	 * @param orderedVar
	 * @return the representation of the ADD with HashMaps
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static ADD createADDwithPlayer(ArrayList<Type> listType, ArrayList<Player> listPlayer, Method method, int[] patronIdeal, boolean orderedVar) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
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
			
		}

		HashMap<Integer,HashMap<Integer,Node>> nodesTemp = new HashMap<Integer,HashMap<Integer,Node>>();
		HashMap<Integer,HashMap<Integer,Node>> nodesFinal = new HashMap<Integer,HashMap<Integer,Node>>();
		for (int i=0; i<=listPlayer.size(); i++) {
			HashMap<Integer,Node> h = new HashMap<Integer,Node>();
			nodesTemp.put(i, h);
			HashMap<Integer,Node> k = new HashMap<Integer,Node>();
			nodesFinal.put(i, k);
		}
		
		int[] types = new int[listType.size()];
		Arrays.fill(types, 0);
		
		ArrayList<Integer> l = new ArrayList<Integer>();
		
		Node racine;
		if (orderedVar)
			racine = creationOrdered(listPlayer,listType,0,types,nodesTemp,nodesFinal,method,patronIdeal,l);
		else
			racine = creation(listPlayer,listType,0,types,nodesFinal,method,patronIdeal,l);
		
		ADD add = new ADD(nodesFinal,racine,listPlayer);
		
		return add;
	}
	
	/**
	 * @param listType
	 * @param listPlayer
	 * @param method
	 * @param patronIdeal
	 * @param orderedVar
	 * @return the representation of the ADD with HashMaps
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static ADD<Player> createADDwithPlayerOtherOrder(ArrayList<Type> listType, ArrayList<Player> listPlayer, Method method, int[] patronIdeal, boolean orderedVar) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if (orderedVar) {
			
			// on trie nos joueurs pour qu'ils soient rangés par type, du type comptant le moins de joueur à celui en comptant le plus
			// objectif : essayer d'optimiser la taille de l'ADD
			
			listPlayer.sort(new Comparator<Player>() {
	            public int compare(Player p1, Player p2) {
	            	int nb1 = p1.getType().getNumberPlayerOfThisType();
	            	int nb2 = p2.getType().getNumberPlayerOfThisType();
	            	if (nb1==nb2) {
	            		return -(p1.getType().getName().compareTo(p2.getType().getName()));
	            	}
	                return nb1-nb2;
	            }
			});
			
			listType.sort(new Comparator<Type>() {
				public int compare(Type t1, Type t2) {
	            	int nb1 = t1.getNumberPlayerOfThisType();
	            	int nb2 = t2.getNumberPlayerOfThisType();
	            	if (nb1==nb2) {
	            		return -(t1.getName().compareTo(t2.getName()));
	            	}
	            	return nb1-nb2;
				}
			});
			
		}

		HashMap<Integer,HashMap<Integer,Node>> nodesTemp = new HashMap<Integer,HashMap<Integer,Node>>();
		HashMap<Integer,HashMap<Integer,Node>> nodesFinal = new HashMap<Integer,HashMap<Integer,Node>>();
		for (int i=0; i<=listPlayer.size(); i++) {
			HashMap<Integer,Node> h = new HashMap<Integer,Node>();
			nodesTemp.put(i, h);
			HashMap<Integer,Node> k = new HashMap<Integer,Node>();
			nodesFinal.put(i, k);
		}
		
		int[] types = new int[listType.size()];
		Arrays.fill(types, 0);
		
		ArrayList<Integer> l = new ArrayList<Integer>();
		
		Node racine;
		if (orderedVar)
			racine = creationOrdered(listPlayer,listType,0,types,nodesTemp,nodesFinal,method,patronIdeal,l);
		else
			racine = creation(listPlayer,listType,0,types,nodesFinal,method,patronIdeal,l);
		
		ADD add = new ADD(nodesFinal,racine,listPlayer);
		
		return add;
	}
	
	/**
	 * @param listPlayer
	 * @param listType
	 * @param indicePlayer
	 * @param types
	 * @param nodes
	 * @param method
	 * @param patronIdeal
	 * @param nbNode
	 * @return the root of the ADD
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private static Node creation(ArrayList<Player> listPlayer, ArrayList<Type> listType, int indicePlayer, int[] types, HashMap<Integer,HashMap<Integer,Node>> nodes, Method method, int[] patronIdeal, ArrayList<Integer> nbNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		// cas où on est sur une feuille
		
		if (indicePlayer==listPlayer.size()) {
			
			int val = (int) method.invoke(null,types,listType,patronIdeal);
			if ( nodes.get(indicePlayer).containsKey(val)) {
				return nodes.get(indicePlayer).get(val);
			}
			Node curr = new Node(nbNode.size(),val);
			
			nbNode.add(1);
			
			HashMap<Integer,Node> hash = nodes.get(indicePlayer);
			hash.put(val, curr);
			nodes.put(indicePlayer,hash);
			
			return curr;
		}
		
		// cas où on est sur un noeud interne 
		
		int[] copie = types.clone();
		
		Node leftChild = creation(listPlayer,listType,indicePlayer+1,copie,nodes,method,patronIdeal,nbNode);
		
		int type = listPlayer.get(indicePlayer).getType().getNum();
		types[type]++;
		
		Node rightChild = creation(listPlayer,listType,indicePlayer+1,types,nodes,method,patronIdeal,nbNode);
		
		// cas où le noeud sera inutile
		
		if (rightChild.equals(leftChild)) {
			return leftChild;
		}
		
		// On vérifie si ce Noeud existe déjà 
		
		Node curr = new Node(nbNode.size(),listPlayer.get(indicePlayer),listPlayer.get(indicePlayer).getType(),rightChild,leftChild);
		
		Integer key = curr.hashCode();
		
		if ( nodes.get(indicePlayer).containsKey(key)) {
			return nodes.get(indicePlayer).get(key);
		}
		
		HashMap<Integer,Node> hash = nodes.get(indicePlayer);
		hash.put(key, curr);
		nodes.put(indicePlayer,hash);
		
		nbNode.add(1);
		
		return curr;
	}

	/**
	 * @param listPlayer
	 * @param listType
	 * @param indicePlayer
	 * @param types
	 * @param nodesTemp
	 * @param nodesFinal
	 * @param method
	 * @param patronIdeal
	 * @param nbNode
	 * @return the root of the ADD
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private static Node creationOrdered(ArrayList<Player> listPlayer, ArrayList<Type> listType, int indicePlayer, int[] types, HashMap<Integer,HashMap<Integer,Node>> nodesTemp, HashMap<Integer,HashMap<Integer,Node>> nodesFinal, Method method, int[] patronIdeal, ArrayList<Integer> nbNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		// cas où on est sur une feuille
		
		if (indicePlayer==listPlayer.size()) {
			
			int val = (int) method.invoke(null,types,listType,patronIdeal);
			
			if ( nodesFinal.get(indicePlayer).containsKey(val)) {
				return nodesFinal.get(indicePlayer).get(val);
			}
			
			Node curr = new Node(nbNode.size(),val);
			
			nbNode.add(1);
			
			HashMap<Integer,Node> hashFinal = nodesFinal.get(indicePlayer);
			hashFinal.put(val, curr);
			nodesFinal.put(indicePlayer,hashFinal);
			
			return curr;
		}
		
		// on regarde si un noeud équivalent ( au sens de la composition de la coalition partielle qu'il représente) existe déjà
		
		Integer key=0;
		String s = "";
		for (int i=0; i<types.length;i++) {
			if (patronIdeal[i] < types[i])
				s += patronIdeal[i] + " joueur de type " + listType.get(i).getName();
			else 
				s += types[i] + " joueur de type " + listType.get(i).getName();
		}
		key = s.hashCode();
		
		if ( nodesTemp.get(indicePlayer).containsKey(key)) {
			return nodesTemp.get(indicePlayer).get(key);
		}
		
		// cas où on est sur un noeud interne 
		
		int[] copie = types.clone();
		int type = listPlayer.get(indicePlayer).getType().getNum();
		Node leftChild;
		Node rightChild;
		
		leftChild = creationOrdered(listPlayer,listType,indicePlayer+1,copie,nodesTemp,nodesFinal,method,patronIdeal,nbNode);
		
		types[type]++;
		
		// Cas où le nombre de joueur idéal pour le type du joueur d'indice "indicePlayer" va être atteint
		
		if (types[type] == patronIdeal[type]) {
			
			// on va calculer le nombre de joueur de ce type restant à passer ( qui seront des noeuds inutiles dans l'ADD )
			
			// A tester
			
			/*
			 * int x = listPlayer.get(indicePlayer).getType().getNumberPlayerOfThisType() - patronIdeal[type];
			 */
			
			int nbPlayerOfType = listPlayer.get(indicePlayer).getType().getNumberPlayerOfThisType();
			int nbPlayerOfPrecedentType = 0;
			for (Type t : listType) {
				if ( t.getNum()==type) {
					break;
				}
				nbPlayerOfPrecedentType+=t.getNumberPlayerOfThisType();
			}
			int nbPlayerPassed = (indicePlayer+1) - nbPlayerOfPrecedentType;
			int x = nbPlayerOfType - nbPlayerPassed;
			
			rightChild = creationOrdered(listPlayer,listType,indicePlayer+x+1,types,nodesTemp,nodesFinal,method,patronIdeal,nbNode);
		}
		
		// Cas où le nombre de joueur idéal pour le type du joueur d'indice "indicePlayer" ne va pas être atteint
		
		else {
			rightChild = creationOrdered(listPlayer,listType,indicePlayer+1,types,nodesTemp,nodesFinal,method,patronIdeal,nbNode);
		}
		
		// cas où le noeud sera inutile
		
		if (rightChild.equals(leftChild)) {
			return leftChild;
		}
		
		// on créé le noeud et on vérifie si un noeud existant n'a pas le même hash
		
		Node curr = new Node(nbNode.size(),listPlayer.get(indicePlayer),listPlayer.get(indicePlayer).getType(),rightChild,leftChild);
		
		if ( nodesFinal.get(indicePlayer).containsKey(curr.getHash())) {
			return nodesFinal.get(indicePlayer).get(curr.getHash());
		}
		
		HashMap<Integer,Node> hashFinal = nodesFinal.get(indicePlayer);
		hashFinal.put(curr.getHash(), curr);
		nodesFinal.put(indicePlayer,hashFinal);
		
		HashMap<Integer,Node> hashTemp = nodesTemp.get(indicePlayer);
		hashTemp.put(key, curr);
		nodesTemp.put(indicePlayer,hashTemp);
		
		nbNode.add(1);
		
		return curr;
	}

}
