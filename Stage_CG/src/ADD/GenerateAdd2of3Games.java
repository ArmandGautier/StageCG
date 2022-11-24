package ADD;

import java.util.ArrayList;
import java.util.HashMap;

public class GenerateAdd2of3Games {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ADDV1<String> generateAdd2of3Games(int n) {
		
		ArrayList<String> listVar = new ArrayList<String>();
		
		for (int i=0; i<n; i++) {
			listVar.add("x"+(n-i));
			listVar.add("y"+(n-i));
			listVar.add("z"+(n-i));
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
			Node<String> Y1 = new Node(nodes.size(),"y"+n);
			nodes.put(Y1.getId(), Y1);
			Node<String> Y2 = new Node(nodes.size(),"y"+n);
			nodes.put(Y2.getId(), Y2);
			Node<String> Z = new Node(nodes.size(),"z"+n);
			nodes.put(Z.getId(), Z);
			
			Node un = new Node(nodes.size(),1,true);
			nodes.put(nodes.size(), un);
			
			X.setRightChild(Y2);
			X.setLeftChild(Y1);
			
			Y1.setRightChild(Z);
			Y1.setLeftChild(zero);
			
			Y2.setRightChild(un);
			Y2.setLeftChild(Z);
			
			Z.setRightChild(un);
			Z.setLeftChild(zero);
			
			return X;
			
		}
		
		Node<String> prev = createADD(n-1,nodes,zero);
		
		Node<String> X = new Node(nodes.size(),"x"+n);
		nodes.put(X.getId(), X);
		Node<String> Y1 = new Node(nodes.size(),"y"+n);
		nodes.put(Y1.getId(), Y1);
		Node<String> Y2 = new Node(nodes.size(),"y"+n);
		nodes.put(Y2.getId(), Y2);
		Node<String> Z = new Node(nodes.size(),"z"+n);
		nodes.put(Z.getId(), Z);
		
		X.setRightChild(Y2);
		X.setLeftChild(Y1);
		
		Y1.setRightChild(Z);
		Y1.setLeftChild(zero);
		
		Y2.setRightChild(prev);
		Y2.setLeftChild(Z);
		
		Z.setRightChild(prev);
		Z.setLeftChild(zero);
		
		return X;

	}

}
