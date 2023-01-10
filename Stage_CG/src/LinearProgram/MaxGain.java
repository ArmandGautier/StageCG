package LinearProgram;

import java.util.HashMap;
import java.util.Iterator;

import ADD.Node;
import DAG.DAG;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class MaxGain {
	
	double constructionTime = 0;
	double solvingTime = 0;
	double[] edgeRight;
	double[] edgeLeft;
	double[] du;
	double[] xi;
	double obj;
	boolean solved = false;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void solve(DAG dag, int nbPlayer) {
		
		try {
			
			IloCplex cplex = new IloCplex();
			//cplex.setOut(null);
			cplex.setParam(IloCplex.Param.TimeLimit, 480);
			cplex.setParam(IloCplex.Param.ClockType,1);
			
			double time1 = System.currentTimeMillis();
			
			// creation des variables
			
			IloNumVar[] Du = new IloNumVar[dag.getNbNodes()];
			IloNumVar[] Xi = new IloNumVar[nbPlayer];
			
			for (int i=0; i<dag.getNbNodes(); i++) {
				Du[i] = cplex.numVar(0, Double.MAX_VALUE, "D"+i);
			}
			
			for (int i=0; i<nbPlayer; i++) {
				Xi[i] = cplex.numVar(0, Double.MAX_VALUE, "X"+i);
			}
			
			int nbNodes = dag.getNbNodes();
			
			IloIntVar[] Eud = new IloIntVar[nbNodes];
			IloIntVar[] Eug = new IloIntVar[nbNodes];
			
			for (int i=0; i<nbNodes; i++) {
				Eud[i] = cplex.boolVar("Eud"+i);
				Eug[i] = cplex.intVar(0, nbPlayer-1,"Eug"+i);
			}
			
			// creation des contraintes
			
			cplex.addEq(0, Du[dag.getRoot().getId()]);
			
			IloLinearIntExpr[] expr = new IloLinearIntExpr[nbNodes];
			
			for (int k=0; k<nbNodes; k++) {
				expr[k] = cplex.linearIntExpr();
			}
			
			for (int i=0; i<nbPlayer; i++) {
				
				IloLinearIntExpr c = cplex.linearIntExpr();
				HashMap<Integer,Node> hash = (HashMap<Integer, Node>) dag.getListNodes().get(i);
				Iterator<Node> it = hash.values().iterator();
				
				while (it.hasNext()) {
					
					Node node = it.next();
					Node lChild = node.getLeftChild();
					Node rChild = node.getRightChild();
					
					cplex.add(cplex.ifThen(cplex.eq(0,Eug[node.getId()]), cplex.ge(Du[lChild.getId()],0)));
					cplex.add(cplex.ifThen(cplex.le(1,Eug[node.getId()]), cplex.eq(Du[lChild.getId()],Du[node.getId()])));
					
					cplex.add(cplex.ifThen(cplex.eq(0,Eud[node.getId()]), cplex.ge(Du[rChild.getId()],0)));
					cplex.add(cplex.ifThen(cplex.eq(1,Eud[node.getId()]), cplex.eq(Du[rChild.getId()],cplex.sum(Xi[i],Du[node.getId()]))));
					
					if (i<nbPlayer-1) {
						expr[lChild.getId()].addTerm(1,Eug[node.getId()]);
						expr[rChild.getId()].addTerm(1,Eud[node.getId()]);
					}
					
					if (i>0) {
						cplex.addEq(cplex.sum(Eug[node.getId()],Eud[node.getId()]),expr[node.getId()]);
					}
					
					c.addTerm(1,Eud[node.getId()]);

				}
				
				cplex.addEq(1, c);
			}
			
			HashMap<Integer,Node> hash = (HashMap<Integer, Node>) dag.getListNodes().get(nbPlayer);
			Iterator<Node> it = hash.values().iterator();
			
			while (it.hasNext()) {
				Node node = it.next();
				cplex.addEq(node.getValue(),Du[node.getId()]);
				cplex.addEq(0, Eud[node.getId()]);
				cplex.addEq(0, Eug[node.getId()]);
			}
			
			IloLinearNumExpr objective = cplex.linearNumExpr();
			
			for (int i=0; i<nbPlayer; i++) {
				objective.addTerm(1, Xi[i]);
			}
			
			cplex.addMaximize(objective);
			
			double time2 = System.currentTimeMillis();
			
			constructionTime = time2-time1;
			
			double time3 = System.currentTimeMillis();
			
			solved = cplex.solve();
			
			double time4 = System.currentTimeMillis();
			
			solvingTime = time4-time3;
			
			if (solved) {
				this.edgeLeft = new double[nbNodes];
				this.edgeRight = new double[nbNodes];
				this.du = new double[nbNodes];
				this.edgeLeft = cplex.getValues(Eug);
				this.edgeRight = cplex.getValues(Eud);
				this.du = cplex.getValues(Du);
				this.xi = cplex.getValues(Xi);
				this.obj = cplex.getObjValue();
			}
			
			cplex.close();
		}
		catch (IloException exc) {
			exc.printStackTrace();
		}
	}
	
	// TO DO version pour ADD

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
	 * @return the edgeRight
	 */
	public double[] getEdgeRight() {
		return edgeRight;
	}

	/**
	 * @return the edgeLeft
	 */
	public double[] getEdgeLeft() {
		return edgeLeft;
	}

	/**
	 * @return the solved
	 */
	public boolean isSolved() {
		return solved;
	}
	
	/**
	 * @return the du
	 */
	public double[] getDu() {
		return du;
	}
	
	/**
	 * @return the xi
	 */
	public double[] getXi() {
		return xi;
	}

	/**
	 * @return the obj
	 */
	public double getObj() {
		return obj;
	}
	
}
