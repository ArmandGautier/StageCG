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

public class Test4 {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CloneNotSupportedException, IOException {
		
		int nbPlayer = 18;
		ArrayList<Player> listPlayer = new ArrayList<Player>();
		ArrayList<Type> listType = new ArrayList<Type>();
		
		System.out.println("Go");
		
		int nbGame = 200;
		int nbType = 6;
		
		int indiceInfoGame = 0;
		
		File file = new File("testMoy.csv");
		
	    if (!file.exists()) {
	       file.createNewFile();
		}
	    
	    String content;
	    
	    content = "nbPlayer, nbType, timeCreateADD, timeCreateADDbis, timeCretaeDAG, nbNodeADD, nbNodeADDbis, nbNodeDAG, timeSolveADD, timeSolveDAG, nbEmptyCore\n";
		  
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    bw.write(content);
		
		for (int i=5; i<=nbPlayer; i++) {
			for ( int j=2; j<=nbType; j++) {
				
				System.out.println("nbPlayer = "+i+" et Nbtype = "+j);
			    
				File file2 = new File("testInfoGame"+indiceInfoGame+".csv");
				
			    if (!file2.exists()) {
			       file2.createNewFile();
				}
				  
			    FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
			    BufferedWriter bw2 = new BufferedWriter(fw2);
			    
			    content = "nbPlayer, nbType, timeCreateADD, timeCreateADDbis, timeCretaeDAG, nbNodeADD, nbNodeADDbis, nbNodeDAG, timeSolveADD, timeSolveDAG, emptyCore, cosADD, cosDAG\n";
			    
			    bw2.write(content);
			    
			    double moytimeAdd = 0;
			    double moytimeAddbis = 0;
			    double moytimeDAG = 0;
			    double moynbNodeAdd = 0;
			    double moynbNodeAddbis = 0;
			    double moynbNodeDAG = 0;
			    double moySolveTimeADD = 0;
			    double moySolveTimeDAG = 0;
			    double nbEmptyCore = 0;
			    
			    for (int k=0; k<nbGame; k++) {
				
					Tools.generatePlayerWithType(i, GetSkill.getSkill(j), listPlayer, listType);
					
					int[] weight = Tools.generateRandomWeight(listType,i);
					
					Method m = GetMethod.getMethod(j);
					
				    double timeAdd;
				    double timeAddbis;
				    double timeDAG;
				    double nbNodeAdd;
				    double nbNodeAddbis;
				    double nbNodeDAG;	    
				    double solveTimeADD = 0;
				    double solveTimeDAG = 0;
					
			        double start1 = System.currentTimeMillis();
					ADD<Player> add = GenerationOfADDwithType.createADDwithPlayer(listType, listPlayer, m, weight, true, false);
					timeAdd=System.currentTimeMillis()-start1;
					//add.writeADDinDOT("file.DOT");
					//System.out.println(add.getNbNodes());
					
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
					
				    nbNodeAdd=add.getNbNodes();
				    nbNodeAddbis=addBis.getNbNodes();
				    nbNodeDAG=dag.getNbNodes();
					
					EmptyCore lpADD = new EmptyCore();
					lpADD.solve(add,i);
					//System.out.println(lpADD.getSolvingTime());
					
					EmptyCore lpDAG = new EmptyCore();
					lpDAG.solve(dag,i);
					//System.out.println(lpDAG.getSolvingTime());
					
				    solveTimeADD = lpADD.getSolvingTime();
				    solveTimeDAG = lpDAG.getSolvingTime();
					
					double cos = lpADD.getCos();
					double cosDag = lpDAG.getCos();
					boolean emptyCore = true;
					//System.out.println(cos);
					//System.out.println(cosDag);
					
					if (cos < 0.0001) {
						if (cosDag < 0.0001) {
							emptyCore = false;
						}
					}
					
					if (emptyCore)
						nbEmptyCore++;
					
					listType.clear();
					listPlayer.clear();
					
					content =  "" + i + ", " + j + ", " + timeAdd + ", " + timeAddbis + ", " + timeDAG + ", " + nbNodeAdd + ", " + nbNodeAddbis + ", " + nbNodeDAG + ", " + solveTimeADD + ", " + solveTimeDAG + ", " + emptyCore + ", " + cos + ", " + cosDag + "\n";
					bw2.write(content);
					
				    moytimeAdd += timeAdd;
				    moytimeAddbis += timeAddbis;
				    moytimeDAG += timeDAG;
				    moynbNodeAdd += nbNodeAdd;
				    moynbNodeAddbis += nbNodeAddbis;
				    moynbNodeDAG += nbNodeDAG;
				    moySolveTimeADD += solveTimeADD;
				    moySolveTimeDAG += solveTimeDAG;
					
			    }
			    
			    bw2.close();
			    indiceInfoGame++;
			    content =  "" + i + ", " + j + ", " + moytimeAdd/nbGame + ", " + moytimeAddbis/nbGame + ", " + moytimeDAG/nbGame + ", " + moynbNodeAdd/nbGame + ", " + moynbNodeAddbis/nbGame + ", " + moynbNodeDAG/nbGame + ", " + moySolveTimeADD/nbGame + ", " + moySolveTimeDAG/nbGame + ", " + nbEmptyCore +"\n";
			    bw.write(content);
			}
		}
		
		bw.close();
	}

}
