package LinearProgram;

import java.util.Iterator;

import ADD.Node;
import DAG.DAG;
import DAG.DAGSimple;
import ilog.concert.*;
import ilog.cplex.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EmptyCore {
	
	double constructionTime = 0;
	double solvingTime = 0;
	boolean solved = false;
	double[] sol;
	double cos = 0;
	
	public void solve(DAGSimple dag, int nbPlayer) {
		
		try {
			
			IloCplex cplex = new IloCplex();
			cplex.setOut(null);
			cplex.setParam(IloCplex.Param.ClockType,1);
			
			double time1 = System.currentTimeMillis();
			
			// creation des variables
			
			IloNumVar[] Du = new IloNumVar[dag.getNbNodes()];
			
			for (int i=0; i<dag.getNbNodes(); i++) {
				Du[i] = cplex.numVar(0, Double.MAX_VALUE, "D"+i);
			}
			
			IloNumVar[] Xi = new IloNumVar[nbPlayer];
			
			for (int i=0; i<nbPlayer; i++) {
				Xi[i] = cplex.numVar(0, Double.MAX_VALUE, "X"+i);
			}
			
			// creation des contraintes
			
			cplex.addEq(0, Du[dag.getRoot().getId()]);
			
			Iterator<Node> it = dag.getIteratorOnNodes();
			
			while (it.hasNext()) {
				Node<?> node = it.next();
				if (node.isLeaf()) {
					cplex.addGe(Du[node.getId()],node.getValue());
				}
				else {
					Node<?> rChild = node.getRightChild();
					Node<?> lChild = node.getLeftChild();
					IloLinearNumExpr expr = cplex.linearNumExpr();
					expr.addTerm(1,Du[node.getId()]);
					expr.addTerm(1,Xi[dag.getVarOrder().indexOf(node.getIdVariable())]);
					cplex.addGe(expr, Du[rChild.getId()]);
					cplex.addGe(Du[node.getId()], Du[lChild.getId()]);
				}
			}
			
			cplex.addMinimize(cplex.sum(Xi));
			
			double time2 = System.currentTimeMillis();
			
			constructionTime = time2-time1;
			
			double time3 = System.currentTimeMillis();
			
			solved = cplex.solve();
			
			double time4 = System.currentTimeMillis();
			
			solvingTime = time4-time3;
			
			if (solved) {
				
				double gainN = 0;
				Node node = dag.getRoot();
				
				while (! node.isLeaf()) {
					node = node.getRightChild();
				}
				
				gainN = node.getValue();
				
				sol = cplex.getValues(Xi);
				double sum = 0;
				for ( int i=0; i<sol.length; i++) {
					sum += sol[i];
				}
		
				cos = sum - gainN;
			
			}
			
			cplex.close();
		}
		catch (IloException exc) {
			exc.printStackTrace();
		}
	}
	
	public void solve(DAG dag, int nbPlayer) {
		
		try {
			
			IloCplex cplex = new IloCplex();
			cplex.setOut(null);
			cplex.setParam(IloCplex.Param.ClockType,1);
			
			double time1 = System.currentTimeMillis();
			
			// creation des variables
			
			IloNumVar[] Du = new IloNumVar[dag.getNbNodes()];
			
			for (int i=0; i<dag.getNbNodes(); i++) {
				Du[i] = cplex.numVar(0, Double.MAX_VALUE, "D"+i);
			}
			
			IloNumVar[] Xi = new IloNumVar[nbPlayer];
			
			for (int i=0; i<nbPlayer; i++) {
				Xi[i] = cplex.numVar(0, Double.MAX_VALUE, "X"+i);
			}
			
			// creation des contraintes
			
			cplex.addEq(0, Du[dag.getRoot().getId()]);
			
			Iterator<Node> it = dag.getIteratorOnNodes();
			
			while (it.hasNext()) {
				Node node = it.next();
				if (node.isLeaf()) {
					cplex.addGe(Du[node.getId()],node.getValue());
				}
				else {
					Node rChild = node.getRightChild();
					Node lChild = node.getLeftChild();
					IloLinearNumExpr expr = cplex.linearNumExpr();
					expr.addTerm(1,Du[node.getId()]);
					expr.addTerm(1,Xi[dag.getVarOrder().indexOf(node.getIdVariable())]);
					cplex.addGe(expr, Du[rChild.getId()]);
					cplex.addGe(Du[node.getId()], Du[lChild.getId()]);
				}
			}
			
			cplex.addMinimize(cplex.sum(Xi));
			
			double time2 = System.currentTimeMillis();
			
			constructionTime = time2-time1;
			
			double time3 = System.currentTimeMillis();
			
			solved = cplex.solve();
			
			double time4 = System.currentTimeMillis();
			
			solvingTime = time4-time3;
			
			if (solved) {
				
				double gainN = 0;
				Node node = dag.getRoot();
				
				while (! node.isLeaf()) {
					node = node.getRightChild();
				}
				
				gainN = node.getValue();
				
				sol = cplex.getValues(Xi);
				double sum = 0;
				for ( int i=0; i<sol.length; i++) {
					sum += sol[i];
				}
		
				cos = sum - gainN;
			
			}
			
			cplex.close();
		}
		catch (IloException exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * @return the constructionTime
	 */
	public double getConstructionTime() {
		return constructionTime;
	}

	/**
	 * @return the solvingTime
	 */
	public double getSolvingTime() {
		return solvingTime;
	}

	/**
	 * @return the solved
	 */
	public boolean isSolved() {
		return solved;
	}

	/**
	 * @return the sol
	 */
	public double[] getSol() {
		return sol;
	}

	/**
	 * @return the cos
	 */
	public double getCos() {
		return cos;
	}	
	
	/**
	 * 
	 */
	public void print_results() {
		if (cos > 0.000001) {
			System.out.println("Le coeur est vide, le COS = " + cos);
		}
		else {
			System.out.println("Dans le pay-off solution : ");
			for (int i=0; i<sol.length; i++) {
				System.out.println("le joueur "+i+" gagne "+this.sol[i]);
			}
		}
	}
	
}
