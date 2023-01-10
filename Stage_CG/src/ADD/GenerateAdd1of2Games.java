package ADD;

import java.util.ArrayList;

import java.util.HashMap;
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenerateAdd1of2Games {
	
	public static ADDSimple generateAdd1of2Games(int n) {
		
		ArrayList listVar = new ArrayList();
		
		for (int i=0; i<n; i++) {
			listVar.add("x"+(n-i));
			listVar.add("y"+(n-i));
		}
		
		HashMap<Integer,Node> nodes = new HashMap<Integer,Node>();
		Node zero = new Node(0,0);
		nodes.put(0, zero);
		Node root = createADD(n,nodes,zero);
		
		ADDSimple res = new ADDSimple(nodes,root,listVar);
		
		return res;
		
	}

	private static Node createADD(int n, HashMap<Integer,Node> nodes, Node zero) {
		
		if (n==1) {
			
			Node un = new Node(nodes.size(),1);
			nodes.put(nodes.size(), un);
			
			Node<String> Y = new Node(nodes.size(),"y"+n,un,zero);
			nodes.put(Y.getId(), Y);
			Node<String> X = new Node(nodes.size(),"x"+n,un,Y);
			nodes.put(X.getId(), X);
			
			return X;
			
		}
		
		Node<String> prev = createADD(n-1,nodes,zero);
		
		Node<String> Y = new Node(nodes.size(),"y"+n,prev,zero);
		nodes.put(Y.getId(), Y);
		Node<String> X = new Node(nodes.size(),"x"+n,prev,Y);
		nodes.put(X.getId(), X);
		
		return X;

	}

}
