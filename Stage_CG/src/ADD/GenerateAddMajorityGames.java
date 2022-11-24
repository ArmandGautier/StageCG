package ADD;

import java.util.ArrayList;
import java.util.HashMap;

public class GenerateAddMajorityGames {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ADD<String> generateAddMajorityGames(int n) {
		
		ArrayList<String> listVar = new ArrayList<String>();
		
		for (int i=1; i<=2*n+1; i++) {
			listVar.add("joueur"+i);
		}
		
		HashMap<Integer,Node<?>> nodes = new HashMap<Integer,Node<?>>();
		Node zero = new Node(0,0,true);
		Node un = new Node(1,1,true);
		nodes.put(1, un);
		nodes.put(0, zero);
		Node<String> root = createADD(n,1,0,nodes,zero,un);
		
		ADD<String> res = new ADD(nodes,root,listVar);
		
		return res;
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Node createADD(int n, int depth, int currSize, HashMap<Integer, Node<?>> nodes, Node zero, Node un) {
		
		String s = "" + depth + currSize;
		int hash = 2 + s.hashCode();
		
		if (nodes.containsKey(hash)) {
			return nodes.get(hash);
		}
		
		Node leftChild;
		
		if (n+currSize < depth) {
			leftChild = zero;
		}
		else {
			leftChild = createADD(n,depth+1,currSize,nodes,zero,un);
		}
		
		Node rightChild;
		
		if (currSize==n) {
			rightChild = un;
		}
		else {
			rightChild = createADD(n,depth+1,currSize+1,nodes,zero,un);
		}
		
		Node curr = new Node(nodes.size(),"joueur"+depth);
		curr.setLeftChild(leftChild);
		curr.setRightChild(rightChild);
		
		nodes.put(hash, curr);
		
		return curr;
	}

}
