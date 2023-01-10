package ADD;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenerateAddSquareGames {

	public static ADDSimple generateAddSquareGames(int n) {
		
		ArrayList<String> listVar = new ArrayList<String>();
		
		for (int i=1; i<=n; i++) {
			listVar.add("joueur"+i);
		}
		
		HashMap<Integer,Node> nodes = new HashMap<Integer,Node>();
		Node<String> root = createADD(n,1,0,nodes);
		
		ADDSimple res = new ADDSimple(nodes,root,listVar);
		
		return res;
		
	}

	private static Node createADD(int n, int depth, int currSize, HashMap<Integer, Node> nodes) {
		
		String s = "" + depth + currSize; 
		int hash = n*n +  s.hashCode();
		
		if (nodes.containsKey(hash)) {
			return nodes.get(hash);
		}
		
		Node leftChild;
		
		if (depth == n) {
			int v = currSize*currSize;
			if ( nodes.containsKey(v)) {
				leftChild = nodes.get(v);
			}
			else {
				leftChild = new Node(nodes.size(),v);
				nodes.put(v, leftChild);
			}
		}
		else {
			leftChild = createADD(n,depth+1,currSize,nodes);
		}
		
		Node rightChild;
		
		if (depth == n) {
			int v = (currSize+1)*(currSize+1);
			if ( nodes.containsKey(v)) {
				rightChild = nodes.get(v);
			}
			else {
				rightChild = new Node(nodes.size(),v);
				nodes.put(v, rightChild);
			}
		}
		else {
			rightChild = createADD(n,depth+1,currSize+1,nodes);
		}
		
		Node curr = new Node(nodes.size(),"joueur"+depth,rightChild,leftChild);
		
		nodes.put(hash, curr);
		
		return curr;
	}
	
}
