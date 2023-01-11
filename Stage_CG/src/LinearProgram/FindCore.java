package LinearProgram;

import java.util.Iterator;

import CoalitionGame.Coalition;
import CoalitionGame.CoalitionGame;
import CoalitionGame.Player;
import CoalitionGame.StructureOfCoalition;
import ilog.concert.*;
import ilog.cplex.*;

public class FindCore {
	
	CoalitionGame game;
	boolean solved = false;
	double[] valueOf_Xi;
	double constructTime;
	double solvingTime;
	
	/**
	 * @param game
	 */
	public FindCore(CoalitionGame game) {
		this.game = game;
	}
	
	public void linearProgramforCG(StructureOfCoalition listCoalition) {
		
		try {
			
			IloCplex cplex = new IloCplex();
			cplex.setOut(null);
			cplex.setParam(IloCplex.Param.ClockType,1);
			
			double time1=System.currentTimeMillis();
			
			// creation des variables
			
			IloNumVar[] Xi = new IloNumVar[this.game.getListPlayer().size()];
			
			for (int i=0; i<this.game.getListPlayer().size(); i++) {
				Xi[i] = cplex.numVar(0, Double.MAX_VALUE, "X"+i);
			}
			
			// creation des contraintes
			
			// Pour chaque coalition la somme des Xi doit etre superieure ou egale au gain des membres de la coalition
			
			Iterator<Coalition> it = this.game.getNu().keySet().iterator();
			
			while (it.hasNext()) {
				
				Coalition c = it.next();
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for (Player p : c.getListPlayer()) {
					expr.addTerm(1,Xi[p.getRepresentation()]);
				}
				cplex.addLe(this.game.getNu().get(c),expr);	
				
			}
			
			// pour chaque coalition de la structure de coalition pour laquelle on cherche une solution, 
			// la somme des Xi correspondant aux joueurs jouant dans cette coaliton doit être égale à la valeur de cette coalition par nu
			
			for ( Coalition c : listCoalition.getStruct() ) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for (Player p : c.getListPlayer()) {
					expr.addTerm(1,Xi[p.getRepresentation()]);
				}
				cplex.addEq(this.game.getNu().get(c),expr);
			}
			
			double time2=System.currentTimeMillis();
			this.constructTime = time2-time1;
			
			double time3=System.currentTimeMillis();
			this.solved = cplex.solve();
			double time4=System.currentTimeMillis();
			this.solvingTime = time4 - time3;
			
			if (this.solved) {
				this.valueOf_Xi = cplex.getValues(Xi);
			}
			
			cplex.close();
			
		}
		catch (IloException exc) {
			exc.printStackTrace();
		}
	}

	public void linearProgramMIPforCG(StructureOfCoalition listCoalition) {

		try {
			
			IloCplex cplex = new IloCplex();
			cplex.setOut(null);
			cplex.setParam(IloCplex.Param.ClockType,1);
			
			double time1=System.currentTimeMillis();
			
			// creation des variables
			
			IloIntVar[] Xi = new IloIntVar[this.game.getListPlayer().size()];
			
			for (int i=0; i<this.game.getListPlayer().size(); i++) {
				Xi[i] = cplex.intVar(0, Integer.MAX_VALUE, "X"+i);
			}
			
			// creation des contraintes
			
			// Pour chaque coalition la somme des Xi doit etre superieure ou egale au gain des membres de la coalition
			
			Iterator<Coalition> it = this.game.getNu().keySet().iterator();
			
			while (it.hasNext()) {
				
				Coalition c = it.next();
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for (Player p : c.getListPlayer()) {
					expr.addTerm(1,Xi[p.getRepresentation()]);
				}
				cplex.addLe(this.game.getNu().get(c),expr);	
				
			}
			
			// La somme des Xi doit être égale à la somme des gains des coalitions de la structure de coalion pour laquelle on cherche un payoff
			
			for ( Coalition c : listCoalition.getStruct() ) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for (Player p : c.getListPlayer()) {
					expr.addTerm(1,Xi[p.getRepresentation()]);
				}
				cplex.addEq(this.game.getNu().get(c),expr);
			}
			
			cplex.setParam(IloCplex.Param.MIP.Pool.Capacity,50);
			cplex.setParam(IloCplex.Param.MIP.Pool.Intensity,4);
			
			double time2=System.currentTimeMillis();
			this.constructTime = time2-time1;
			
			double time3=System.currentTimeMillis();
			this.solved = cplex.populate();
			double time4=System.currentTimeMillis();
			this.solvingTime = time4 - time3;
			
			if (this.solved) {
				this.valueOf_Xi = cplex.getValues(Xi);
			}
			
			System.out.println("NbSolution generee : "+cplex.getSolnPoolNsolns());
			cplex.writeSolutions("Sol");
			
			cplex.close();
			
		}
		catch (IloException exc) {
			exc.printStackTrace();
		}
	}
	
	public void print_results() {
		if (this.solved) {
			System.out.println("Dans le pay-off solution : ");
			for (int i=0; i<this.game.getListPlayer().size(); i++) {
				System.out.println("le joueur "+i+" gagne "+this.valueOf_Xi[i]);
			}
		}
		else {
			System.out.println("Le programme linéaire n'a pas ete lance ou alors il n'a pas de solution");
		}
	}

	/**
	 * @return the constructTime
	 */
	public double getConstructTime() {
		return constructTime;
	}

	/**
	 * @param constructTime the constructTime to set
	 */
	public void setConstructTime(double constructTime) {
		this.constructTime = constructTime;
	}

	/**
	 * @return the solvingTime
	 */
	public double getSolvingTime() {
		return solvingTime;
	}

	/**
	 * @param solvingTime the solvingTime to set
	 */
	public void setSolvingTime(double solvingTime) {
		this.solvingTime = solvingTime;
	}

	/**
	 * @return the solved
	 */
	public boolean isSolved() {
		return solved;
	}
	
}
