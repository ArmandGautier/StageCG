package ADD;

import java.util.ArrayList;
import java.util.HashMap;

public class GenerateAdd1of2Games {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ADDV1<String> generateAdd1of2Games(int n) {
		
		ArrayList<String> listVar = new ArrayList<String>();
		
		for (int i=0; i<n; i++) {
			listVar.add("x"+(n-i));
			listVar.add("y"+(n-i));
		}
		
		HashMap<Integer,Node<?>> nodes = new HashMap<Integer,Node<?>>();
		Node zero = new Node(0,0,true);
		nodes.put(0, zero);
		Node<String> root = createADD(n,nodes,zero);
		
		ADDV1<String> res = new ADDV1(nodes,root,listVar);
		
		return res;
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Node<String> createADD(int n, HashMap<Integer,Node<?>> nodes, Node zero) {
		
		if (n==1) {
			
			Node<String> X = new Node(nodes.size(),"x"+n);
			nodes.put(X.getId(), X);
			Node<String> Y = new Node(nodes.size(),"y"+n);
			nodes.put(Y.getId(), Y);
			
			Node un = new Node(nodes.size(),1,true);
			nodes.put(nodes.size(), un);
			
			X.setRightChild(un);
			X.setLeftChild(Y);
			
			Y.setRightChild(un);
			Y.setLeftChild(zero);
			
			return X;
			
		}
		
		Node<String> prev = createADD(n-1,nodes,zero);
		
		Node<String> X = new Node(nodes.size(),"x"+n);
		nodes.put(X.getId(), X);
		Node<String> Y = new Node(nodes.size(),"y"+n);
		nodes.put(Y.getId(), Y);
		
		X.setRightChild(prev);
		X.setLeftChild(Y);
		
		Y.setRightChild(prev);
		Y.setLeftChild(zero);
		
		return X;

	}

}
