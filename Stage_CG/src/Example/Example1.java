package Example;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import CoalitionGame.Coalition;
import CoalitionGame.CoalitionGame;
import CoalitionGame.Player;
import CoalitionGame.StructureOfCoalition;
import LinearProgram.FindCore;
import Tools.Tools;

public class Example1 {

	public static void main(String[] args) {
		
		ArrayList<Player> listPlayer = new ArrayList<Player>();
		TreeMap<Coalition,Double> nu = new TreeMap<Coalition,Double>();
		
		int nbPlayer = 3;
		
		for (int i=0; i<nbPlayer; i++) {
			listPlayer.add(new Player(i));
		}
		
		TreeSet<Coalition> listCoalition = new TreeSet<Coalition>();
		
		for (int i=0; i<=nbPlayer; i++) {
			listCoalition.addAll(Tools.createCoalitionOfSize(i,listPlayer));
		}
		
		Scanner scan = new Scanner(System.in);
		for (Coalition c : listCoalition) {
			System.out.println(c.toString());
		    System.out.println("What is the gain for this coalition ? Please enter a number: ");
		    double number = scan.nextInt();
			nu.put(c,number);
		}
		scan.close();
		
		CoalitionGame game = new CoalitionGame(listPlayer,nu);
		
		FindCore solveur = new FindCore(game);
		
		StructureOfCoalition t = new StructureOfCoalition(new Coalition(listPlayer));
		
		solveur.linearProgramforCG(t);
		solveur.print_results();
		
		ArrayList<Double> shap = game.computeShapley();
		
		for (int i=0; i<shap.size(); i++) {
			System.out.println("X"+i+" = "+shap.get(i));
		}
		
	}

}
