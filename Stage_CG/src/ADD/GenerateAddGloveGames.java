package ADD;

import java.util.ArrayList;
import java.util.HashMap;

import Tools.Tools;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenerateAddGloveGames {
	
	// Bugué

	public static ADDSimple generateAddGloveGames(int m, int n) {
		
		ArrayList listVar = new ArrayList<String>();
		
		for (int i=1; i<=m; i++) {
			listVar.add("left"+i);
		}
		
		for (int i=1; i<=n; i++) {
			listVar.add("right"+i);
		}
		
		HashMap<Integer,Node> nodes = new HashMap<Integer,Node>();
		Node zero = new Node(0,0);
		nodes.put(0, zero);
		int maxPair = Tools.min(m,n);
		Node root = createADD(m,n,1,0,0,nodes,maxPair);
		
		ADDSimple res = new ADDSimple(nodes,root,listVar);
		
		return res;
		
	}

	private static Node createADD(int m, int n, int depth, int nbPair, int min, HashMap<Integer, Node> nodes, int maxPair) {
		
		String s = "" + depth + nbPair + min; 
		int hash = Tools.min(m,n) +  s.hashCode();
		
		if (nodes.containsKey(hash)) {
			return nodes.get(hash);
		}
		
		Node leftChild;
		
		if (depth <= m) {
			if (depth==m && min==0) {
				leftChild = nodes.get(0);
			}
			else {
				leftChild = createADD(m,n,depth+1,0,min,nodes,maxPair);
			}
		}
		else {
			if (depth == m+n) {
				if (nodes.containsKey(nbPair)) {
					leftChild = nodes.get(nbPair);
				}
				else {
					leftChild = new Node(nodes.size(),nbPair);
					nodes.put(nbPair, leftChild);
				}
			}
			else {
				leftChild = createADD(m,n,depth+1,nbPair,Tools.min(min,(n+m-depth)),nodes,maxPair);
			}
		}
		
		Node rightChild;
		
		if (depth <= m) {
			if (min == n-1) {
				rightChild = createADD(m,n,m+1,0,n,nodes,maxPair);
			}
			else {
				rightChild = createADD(m,n,depth+1,0,min+1,nodes,maxPair);
			}
		}
		else {
			if (depth == m+n) {
				if (nodes.containsKey(nbPair+1)) {
					rightChild = nodes.get(nbPair+1);
				}
				else {
					rightChild = new Node(nodes.size(),nbPair+1);
					nodes.put(nbPair+1, rightChild);
				}
			}
			else {
				// problem
				if (nbPair==min-1 || nbPair==maxPair-1) {
					if (nodes.containsKey(nbPair+1)) {
						rightChild = nodes.get(nbPair+1);
					}
					else {
						rightChild = new Node(nodes.size(),nbPair+1);
						nodes.put(nbPair+1, rightChild);
					}
				}
				else {
					rightChild = createADD(m,n,depth+1,nbPair+1,min-1,nodes,maxPair);
				}
			}
		}
		
		Node curr;
		if (depth<=m) {
			curr = new Node(nodes.size(),"left"+depth,rightChild,leftChild);
		}
		else {
			curr = new Node(nodes.size(),"right"+(depth-m),rightChild,leftChild);
		}
		
		nodes.put(hash, curr);
		
		return curr;
	}
	
}
