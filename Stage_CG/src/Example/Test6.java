package Example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import ADD.ADD;
import ADD.GenerationOfADDwithType;
import CoalitionGame.Player;
import CoalitionGame.Type;
import DAG.DAG;
import LinearProgram.EmptyCore;
import Tools.GetMethod;
import Tools.GetSkill;
import Tools.Tools;

public class Test6 {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CloneNotSupportedException, IOException {
		
		int nbPlayer = 40;
		ArrayList<Player> listPlayer = new ArrayList<Player>();
		ArrayList<Type> listType = new ArrayList<Type>();
		
		System.out.println("Go");
		
		int nbGame = 50;
		int nbType = 4;
		
		int indiceInfoGame = 0;
		
		File file = new File("testMoy.csv");
		
	    if (!file.exists()) {
	       file.createNewFile();
		}
	    
	    String content;
	    
	    content = "nbPlayer, nbType, timeCreateADDbis, timeCreateDAG, nbNodeADDbis, nbNodeDAG, timeSolveDAG\n";
		  
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    bw.write(content);
		
		for (int i=20; i<=nbPlayer; i++) {
			for ( int j=2; j<=nbType; j++) {
				
				System.out.println("nbPlayer = "+i+" et Nbtype = "+j);
			    
				File file2 = new File("testInfoGame"+indiceInfoGame+".csv");
				
			    if (!file2.exists()) {
			       file2.createNewFile();
				}
				  
			    FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
			    BufferedWriter bw2 = new BufferedWriter(fw2);
			    
			    content = "nbPlayer, nbType, timeCreateADDbis, timeCreateDAG, nbNodeADDbis, nbNodeDAG, timeSolveDAG, emptyCore, cosDAG\n";
			    
			    bw2.write(content);
			    
			    double moytimeAddbis = 0;
			    double moytimeDAG = 0;
			    double moynbNodeAddbis = 0;
			    double moynbNodeDAG = 0;
			    double moySolveTimeDAG = 0;
			    
			    for (int k=0; k<nbGame; k++) {
				
					Tools.generatePlayerWithType(i, GetSkill.getSkill(j), listPlayer, listType);
					
					int[] weight = Tools.generateRandomWeight(listType,i);
					
					Method m = GetMethod.getMethod(j);
					
				    double timeAddbis;
				    double timeDAG;
				    double nbNodeAddbis;
				    double nbNodeDAG;
				    double solveTimeDAG = 0;
					
					double start2 = System.currentTimeMillis();
					ADD<Player> addBis = GenerationOfADDwithType.createADDwithPlayer(listType, listPlayer, m, weight, true, true);
					timeAddbis=System.currentTimeMillis()-start2;
					//addBis.writeADDinDOT("fileBis.DOT");
					//System.out.println(addBis.getNbNodes());
					
					double start3 = System.currentTimeMillis();
					DAG<?> dag = addBis.createDAG();
					timeDAG=System.currentTimeMillis()-start3;
					//dag.writeDAGinDOT("dag.DOT");
					
					/*
					add.writeADDinDOT("file.DOT");
					addBis.writeADDinDOT("fileBis.DOT");
					dag.writeDAGinDOT("dag.DOT");
					*/
					
				    nbNodeAddbis=addBis.getNbNodes();
				    nbNodeDAG=dag.getNbNodes();
					
					EmptyCore lpDAG = new EmptyCore();
					lpDAG.solve(dag,i);
					//System.out.println(lpDAG.getSolvingTime());
					
				    solveTimeDAG = lpDAG.getSolvingTime();
				    
					double cosDag = lpDAG.getCos();
					boolean emptyCore = true;
					//System.out.println(cosDag);
					
					if (cosDag < 0.0001) {
						emptyCore = false;
					}
					
					listType.clear();
					listPlayer.clear();
					
					content =  "" + i + ", " + j + ", " + timeAddbis + ", " + timeDAG + ", " + nbNodeAddbis + ", " + nbNodeDAG + ", " + solveTimeDAG + ", " + emptyCore + ", " + cosDag + "\n";
					bw2.write(content);
					
				    moytimeAddbis += timeAddbis;
				    moytimeDAG += timeDAG;
				    moynbNodeAddbis += nbNodeAddbis;
				    moynbNodeDAG += nbNodeDAG;
				    moySolveTimeDAG += solveTimeDAG;
					
			    }
			    
			    bw2.close();
			    indiceInfoGame++;
			    content =  "" + i + ", " + j + ", " + moytimeAddbis/nbGame + ", " + moytimeDAG/nbGame + ", " + moynbNodeAddbis/nbGame + ", " + moynbNodeDAG/nbGame + ", " + moySolveTimeDAG/nbGame + "\n";
			    bw.write(content);
			}
		}
		
		bw.close();
	}

}
