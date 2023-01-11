package LinearProgram;

import java.util.HashMap;
import java.util.Iterator;

import ADD.ADD;
import ADD.Node;
import DAG.DAG;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class findStableSC {
	
	double constructionTime = 0;
	double solvingTime = 0;
	double[] edgeRight;
	double[] edgeLeft;
	double[] du;
	double[] vu;
	double nbVariable = 0;
	double nbContrainte = 0;
	boolean solved = false;
	
	// PLNE avec les if
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void solve(DAG dag, int nbPlayer, double[] Xi) {
		
		try {
			
			IloCplex cplex = new IloCplex();
			cplex.setOut(null);
			cplex.setParam(IloCplex.Param.TimeLimit, 480);
			cplex.setParam(IloCplex.Param.ClockType,1);
			
			double time1 = System.currentTimeMillis();
			
			// creation des variables
			
			IloNumVar[] Du = new IloNumVar[dag.getNbNodes()];
			
			for (int i=0; i<dag.getNbNodes(); i++) {
				Du[i] = cplex.numVar(0, Double.MAX_VALUE, "D"+i);
			}
			
			int nbNodes = dag.getNbNodes();
			
			IloIntVar[] Eud = new IloIntVar[nbNodes];
			IloIntVar[] Eug = new IloIntVar[nbNodes];
			
			for (int i=0; i<nbNodes; i++) {
				Eud[i] = cplex.boolVar("Eud"+i);
				Eug[i] = cplex.intVar(0, nbPlayer-1,"Eug"+i);
			}
			
			this.nbVariable = 3*nbNodes;
			
			// creation des contraintes
			
			cplex.addEq(0, Du[dag.getRoot().getId()]);
			
			this.nbContrainte += 1;
			
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
					
					this.nbContrainte += 4;
					
					if (i<nbPlayer-1) {
						expr[lChild.getId()].addTerm(1,Eug[node.getId()]);
						expr[rChild.getId()].addTerm(1,Eud[node.getId()]);
					}
					
					if (i>0) {
						cplex.addEq(cplex.sum(Eug[node.getId()],Eud[node.getId()]),expr[node.getId()]);
						this.nbContrainte += 1;
					}
					
					c.addTerm(1,Eud[node.getId()]);

				}
				
				cplex.addEq(1, c);
				this.nbContrainte += 1;
			}
			
			HashMap<Integer,Node> hash = (HashMap<Integer, Node>) dag.getListNodes().get(nbPlayer);
			Iterator<Node> it = hash.values().iterator();
			
			while (it.hasNext()) {
				Node node = it.next();
				cplex.addEq(node.getValue(),Du[node.getId()]);
				cplex.addEq(0, Eud[node.getId()]);
				cplex.addEq(0, Eug[node.getId()]);
				this.nbContrainte += 3;
			}
			
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
			}
			
			cplex.close();
		}
		catch (IloException exc) {
			exc.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void solve(ADD add, int nbPlayer, double[] Xi) {
		
		try {
			
			IloCplex cplex = new IloCplex();
			cplex.setOut(null);
			cplex.setParam(IloCplex.Param.TimeLimit, 480);
			cplex.setParam(IloCplex.Param.ClockType,1);
			
			double time1 = System.currentTimeMillis();
			
			// creation des variables
			
			IloNumVar[] Du = new IloNumVar[add.getNbNodes()];
			
			for (int i=0; i<add.getNbNodes(); i++) {
				Du[i] = cplex.numVar(0, Double.MAX_VALUE, "D"+i);
			}
			
			int nbNodes = add.getNbNodes();
			
			IloIntVar[] Eud = new IloIntVar[nbNodes];
			IloIntVar[] Eug = new IloIntVar[nbNodes];
			
			for (int i=0; i<nbNodes; i++) {
				Eud[i] = cplex.boolVar("Eud"+i);
				Eug[i] = cplex.intVar(0, nbPlayer-1,"Eug"+i);
			}
			
			this.nbVariable = 3*nbNodes;
			
			// creation des contraintes
			
			cplex.addEq(0, Du[add.getRoot().getId()]);
			
			this.nbContrainte += 1;
			
			IloLinearIntExpr[] expr = new IloLinearIntExpr[nbNodes];
			
			for (int k=0; k<nbNodes; k++) {
				expr[k] = cplex.linearIntExpr();
			}
			
			int indRoot = add.getVarOrder().indexOf(add.getRoot().getIdVariable());
			
			for (int i=indRoot; i<nbPlayer; i++) {
				
				IloLinearIntExpr c = cplex.linearIntExpr();
				HashMap<Integer,Node> hash = (HashMap<Integer, Node>) add.getNodes().get(i);
				Iterator<Node> it = hash.values().iterator();
				
				while (it.hasNext()) {
					
					Node node = it.next();
					Node lChild = node.getLeftChild();
					Node rChild = node.getRightChild();
				
					cplex.add(cplex.ifThen(cplex.eq(0,Eug[node.getId()]), cplex.ge(Du[lChild.getId()],0)));
					cplex.add(cplex.ifThen(cplex.le(1,Eug[node.getId()]), cplex.eq(Du[lChild.getId()],Du[node.getId()])));
					
					cplex.add(cplex.ifThen(cplex.eq(0,Eud[node.getId()]), cplex.ge(Du[rChild.getId()],0)));
					cplex.add(cplex.ifThen(cplex.eq(1,Eud[node.getId()]), cplex.eq(Du[rChild.getId()],cplex.sum(Xi[i],Du[node.getId()]))));
					
					this.nbContrainte += 4;
					
					if (i<nbPlayer-1) {
						expr[lChild.getId()].addTerm(1,Eug[node.getId()]);
						expr[rChild.getId()].addTerm(1,Eud[node.getId()]);
					}
					
					if (i>indRoot) {
						cplex.addEq(cplex.sum(Eug[node.getId()],Eud[node.getId()]),expr[node.getId()]);
						this.nbContrainte += 1;
					}
					
					c.addTerm(1,Eud[node.getId()]);

				}
				
				if (Xi[i]==0) {
					cplex.addGe(1, c);
				}
				else {
					cplex.addEq(1, c);
				}
				this.nbContrainte += 1;
			}
			
			HashMap<Integer,Node> hash = (HashMap<Integer, Node>) add.getNodes().get(nbPlayer);
			Iterator<Node> it = hash.values().iterator();
			
			while (it.hasNext()) {
				Node node = it.next();
				cplex.addEq(node.getValue(),Du[node.getId()]);
				cplex.addEq(0, Eud[node.getId()]);
				cplex.addEq(0, Eug[node.getId()]);
				this.nbContrainte += 3;
			}
			
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
			}
			
			cplex.close();
		}
		catch (IloException exc) {
			exc.printStackTrace();
		}
	}
	
	// BUGUE : linéarisation des ifs
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void solveV2(DAG dag, int nbPlayer, double[] Xi) {
		
		try {
			
			IloCplex cplex = new IloCplex();
			cplex.setOut(null);
			cplex.setParam(IloCplex.Param.TimeLimit, 480);
			cplex.setParam(IloCplex.Param.ClockType,1);
			
			double time1 = System.currentTimeMillis();
			
			double delta = 0;
			for (int i=0; i<Xi.length; i++) {
				delta += Xi[i];
			}
			
			// creation des variables
			
			IloNumVar[] Du = new IloNumVar[dag.getNbNodes()];
			
			for (int i=0; i<dag.getNbNodes(); i++) {
				Du[i] = cplex.numVar(0, Double.MAX_VALUE, "D"+i);
			}
			
			int nbNodes = dag.getNbNodes();
			
			IloIntVar[] Eud = new IloIntVar[nbNodes];
			IloIntVar[] Eug = new IloIntVar[nbNodes];
			IloIntVar[] Vu = new IloIntVar[nbNodes];
			IloNumVar[] Wud = new IloNumVar[nbNodes];
			IloNumVar[] Wug = new IloNumVar[nbNodes];
			
			for (int i=0; i<nbNodes; i++) {
				Eud[i] = cplex.boolVar("Eud"+i);
				Eug[i] = cplex.intVar(0, nbPlayer-1,"Eug"+i);
				Vu[i] = cplex.intVar(0,1,"Vu"+i);
				Wud[i] = cplex.numVar(0,1,"Wud"+i);
				Wug[i] = cplex.numVar(0,1,"Wug"+i);
			}
			
			// creation des contraintes
			
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int i=0; i<nbNodes; i++) {
				objective.addTerm(1,Vu[i]);
			}
			cplex.addMaximize(objective);
			
			cplex.addEq(0, Du[dag.getRoot().getId()]);
			cplex.addEq(nbPlayer-1, Eug[dag.getRoot().getId()]);
			
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
					
					cplex.addEq(Wud[node.getId()], cplex.diff(1,Eud[node.getId()]));
					cplex.addEq(Wug[node.getId()], cplex.diff(1,Vu[node.getId()]));
				
					cplex.addGe(cplex.sum(cplex.prod(Wud[node.getId()],delta),Du[rChild.getId()]),cplex.sum(Xi[i],Du[node.getId()]));
					
					cplex.addGe(Eug[node.getId()],Vu[node.getId()]);
					cplex.addGe(cplex.sum(cplex.prod(Wug[node.getId()],delta),Du[lChild.getId()]),cplex.sum(Xi[i],Du[node.getId()]));

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
			
			double time2 = System.currentTimeMillis();
			
			constructionTime = time2-time1;
			
			double time3 = System.currentTimeMillis();
			
			System.out.println(cplex.toString());
			
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
				this.vu = cplex.getValues(Vu);
			}
			
			cplex.close();
		}
		catch (IloException exc) {
			exc.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void solveV2(ADD add, int nbPlayer, double[] Xi) {
		
		try {
			
			IloCplex cplex = new IloCplex();
			cplex.setOut(null);
			cplex.setParam(IloCplex.Param.TimeLimit, 480);
			cplex.setParam(IloCplex.Param.ClockType,1);
			
			double time1 = System.currentTimeMillis();
			
			double delta = 0;
			for (int i=0; i<Xi.length; i++) {
				delta += Xi[i];
			}
			
			// creation des variables
			
			IloNumVar[] Du = new IloNumVar[add.getNbNodes()];
			
			for (int i=0; i<add.getNbNodes(); i++) {
				Du[i] = cplex.numVar(0, Double.MAX_VALUE, "D"+i);
			}
			
			int nbNodes = add.getNbNodes();
			
			IloIntVar[] Eud = new IloIntVar[nbNodes];
			IloIntVar[] Eug = new IloIntVar[nbNodes];
			IloIntVar[] Vu = new IloIntVar[nbNodes];
			IloNumVar[] Wud = new IloNumVar[nbNodes];
			IloNumVar[] Wug = new IloNumVar[nbNodes];
			
			for (int i=0; i<nbNodes; i++) {
				Eud[i] = cplex.boolVar("Eud"+i);
				Eug[i] = cplex.intVar(0, nbPlayer-1,"Eug"+i);
				Vu[i] = cplex.intVar(0,1,"Vu"+i);
				Wud[i] = cplex.numVar(0,1,"Wud"+i);
				Wug[i] = cplex.numVar(0,1,"Wug"+i);
			}
			
			// creation des contraintes
			
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int i=0; i<nbNodes; i++) {
				objective.addTerm(1, Vu[i]);
			}
			cplex.addMaximize(objective);
			
			cplex.addEq(0, Du[add.getRoot().getId()]);
			
			IloLinearIntExpr[] expr = new IloLinearIntExpr[nbNodes];
			
			for (int k=0; k<nbNodes; k++) {
				expr[k] = cplex.linearIntExpr();
			}
			
			int indRoot = add.getVarOrder().indexOf(add.getRoot().getIdVariable());
			
			for (int i=indRoot; i<nbPlayer; i++) {
				
				IloLinearIntExpr c = cplex.linearIntExpr();
				HashMap<Integer,Node> hash = (HashMap<Integer, Node>) add.getNodes().get(i);
				Iterator<Node> it = hash.values().iterator();
				
				while (it.hasNext()) {
					
					Node node = it.next();
					Node lChild = node.getLeftChild();
					Node rChild = node.getRightChild();
					
					cplex.addEq(Wud[node.getId()], cplex.diff(1,Eud[node.getId()]));
					cplex.addEq(Wug[node.getId()], cplex.diff(1,Vu[node.getId()]));
				
					cplex.addGe(cplex.sum(cplex.prod(Wud[node.getId()],delta),Du[rChild.getId()]),cplex.sum(Xi[i],Du[node.getId()]));
					
					cplex.addGe(Eug[node.getId()],Vu[node.getId()]);
					cplex.addGe(cplex.sum(cplex.prod(Wug[node.getId()],delta),Du[lChild.getId()]),cplex.sum(Xi[i],Du[node.getId()]));
	
					if (i<nbPlayer-1) {
						expr[lChild.getId()].addTerm(1,Eug[node.getId()]);
						expr[rChild.getId()].addTerm(1,Eud[node.getId()]);
					}
					
					if (i>indRoot) {
						cplex.addEq(cplex.sum(Eug[node.getId()],Eud[node.getId()]),expr[node.getId()]);
					}
					
					c.addTerm(1,Eud[node.getId()]);

				}
				
				if (Xi[i]==0) {
					cplex.addGe(1, c);
				}
				else {
					cplex.addEq(1, c);
				}
			}
			
			HashMap<Integer,Node> hash = (HashMap<Integer, Node>) add.getNodes().get(nbPlayer);
			Iterator<Node> it = hash.values().iterator();
			
			while (it.hasNext()) {
				Node node = it.next();
				cplex.addEq(node.getValue(),Du[node.getId()]);
				cplex.addEq(0, Eud[node.getId()]);
				cplex.addEq(0, Eug[node.getId()]);
			}
			
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
				this.vu = cplex.getValues(Vu);
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
	 * @return
	 */
	public double[] getVu() {
		return vu;
	}

	/**
	 * @return the nbVariable
	 */
	public double getNbVariable() {
		return nbVariable;
	}

	/**
	 * @return the nbContrainte
	 */
	public double getNbContrainte() {
		return nbContrainte;
	}
	
}
