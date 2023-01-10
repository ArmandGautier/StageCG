package Example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import ADD.ADD;
import ADD.GenerationOfADDwithType;
import CoalitionGame.Coalition;
import CoalitionGame.CoalitionGame;
import CoalitionGame.GenerationOfCGwithType;
import CoalitionGame.Player;
import CoalitionGame.StructureOfCoalition;
import CoalitionGame.Type;
import DAG.DAG;
import LinearProgram.EmptyCore;
import LinearProgram.FindCore;
import Tools.GetMethod;
import Tools.GetSkill;
import Tools.Tools;

public class Test3 {
	
	// Test de comparaison des performances pour Nu, AddO, AddOo et AddNo

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException, CloneNotSupportedException {
		
		System.out.println("Go");
		
		int nbPlayer = 5;
		int limNbPlayer = 20;
		int nbGame = 200;
		int nbType = 2;
		int limNbType = 6;
		ArrayList<Player> listPlayer = new ArrayList<Player>();
		ArrayList<Type> listType = new ArrayList<Type>();
		
		int indiceInfoGame = 0;
		
		File file = new File("testMoy.csv");
		
	    if (!file.exists()) {
	       file.createNewFile();
		}
	    
	    String content;
	    
	    content = "nbPlayer, nbType, t1Nu, t1AddO, t1AddOo, t1AddNo, t2Nu, t2AddO, t2AddOo, t2AddNo, t3Nu, t3AddO, t3AddOo, t3AddNo, t4Nu, t4AddO, t4AddOo, t4AddNo, n1AddO, n1AddOo, n1AddNo, n2AddO, n2AddOo, n2AddNo, nbEmptyCore\n";
		  
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    bw.write(content);
		
		for (int i=nbPlayer; i<=limNbPlayer; i++) {
			for ( int j=nbType; j<=limNbType; j++) {
				
				System.out.println("nbPlayer : "+i+" et nbType "+j);
			    
				File file2 = new File("testInfoGame"+indiceInfoGame+".csv");
				
			    if (!file2.exists()) {
			       file2.createNewFile();
				}
				  
			    FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
			    BufferedWriter bw2 = new BufferedWriter(fw2);
			    
			    content = "nbPlayer, nbType, t1Nu, t1AddO, t1AddOo, t1AddNo, t2Nu, t2AddO, t2AddOo, t2AddNo, t3Nu, t3AddO, t3AddOo, t3AddNo, t4Nu, t4AddO, t4AddOo, t4AddNo, n1AddO, n1AddOo, n1AddNo, n2AddO, n2AddOo, n2AddNo, emptyCore\n";
			    
			    bw2.write(content);
			    
			    double t1Nu = 0;
			    double t1AddO = 0;
			    double t1AddOo = 0;
			    double t1AddNo = 0;
			    double t2Nu = 0;
			    double t2AddO = 0;
			    double t2AddOo = 0;
			    double t2AddNo = 0;
			    double t3Nu = 0;
			    double t3AddO = 0;
			    double t3AddOo = 0;
			    double t3AddNo = 0;
			    double t4Nu = 0;
			    double t4AddO = 0;
			    double t4AddOo = 0;
			    double t4AddNo = 0;
			    int n1AddO = 0;
			    int n1AddOo = 0;
			    int n1AddNo = 0;
			    int n2AddO = 0;
			    int n2AddOo = 0;
			    int n2AddNo = 0;  
			    int nbEmptyCore = 0;
			    
			    for (int k=0; k<nbGame; k++) {
				
					Tools.generatePlayerWithType(i, GetSkill.getSkill(j), listPlayer, listType);
					
					int[] weight = Tools.generateRandomWeight(listType,i);
					
					double time1 = 0;
					double time2 = 0;
					double time3 = 0;
					double time4 = 0;
					double time;
					
					time = System.currentTimeMillis();
					CoalitionGame g = GenerationOfCGwithType.createCGwithPlayer(listType, listPlayer, GetMethod.getMethod(j), weight);
					time1 = System.currentTimeMillis() - time;
					
					time = System.currentTimeMillis();
					ADD<Player> addNo = GenerationOfADDwithType.createADDwithPlayer(listType, listPlayer, GetMethod.getMethod(j), weight, false);
					DAG<Player> dagNo = addNo.createDAG();
					time4 = System.currentTimeMillis() - time;
					
					time = System.currentTimeMillis();
					ADD<Player> addO = GenerationOfADDwithType.createADDwithPlayer(listType, listPlayer, GetMethod.getMethod(j), weight, true);
					DAG<Player> dagO = addO.createDAG();
					time2 = System.currentTimeMillis() - time;
					
					time = System.currentTimeMillis();
					ADD<Player> addOo = GenerationOfADDwithType.createADDwithPlayerOtherOrder(listType, listPlayer, GetMethod.getMethod(j), weight, true);
					DAG<Player> dagOo = addOo.createDAG();
					time3 = System.currentTimeMillis() - time;
				
					FindCore lpCg = new FindCore(g);
					lpCg.linearProgramforCG(new StructureOfCoalition(new Coalition(listPlayer)));
					
					EmptyCore lpAddO = new EmptyCore();
					lpAddO.solve(dagO,i);
					
					EmptyCore lpAddOo = new EmptyCore();
					lpAddOo.solve(dagOo,i);
					
					EmptyCore lpAddNo = new EmptyCore();
					lpAddNo.solve(dagNo,i);
					
					double tempsCg = lpCg.getSolvingTime() + lpCg.getConstructTime();
					double tempsAddO = lpAddO.getSolvingTime() + lpAddO.getConstructionTime();
					double tempsAddOo = lpAddOo.getSolvingTime() + lpAddOo.getConstructionTime();
					double tempsAddNo = lpAddNo.getSolvingTime() + lpAddNo.getConstructionTime();
					
					boolean emptyCore = false;
					
					if (lpAddO.getCos() > 0.0001) {
						emptyCore = true;
					}
					
					//System.out.println(lpCg.isSolved());
					//System.out.println(lpAddO.getCos());
					//System.out.println(lpAddOo.getCos());
					//System.out.println(lpAddNo.getCos());
					
				    t1Nu += time1;
				    t1AddO += time2;
				    t1AddOo += time3;
				    t1AddNo += time4;
				    t2Nu += tempsCg;
				    t2AddO += tempsAddO;
				    t2AddOo += tempsAddOo;
				    t2AddNo += tempsAddNo;
				    t3Nu += lpCg.getConstructTime();
				    t3AddO += lpAddO.getConstructionTime();
				    t3AddOo += lpAddOo.getConstructionTime();
				    t3AddNo += lpAddNo.getConstructionTime();
				    t4Nu += lpCg.getSolvingTime();
				    t4AddO += lpAddO.getSolvingTime();
				    t4AddOo += lpAddOo.getSolvingTime();
				    t4AddNo += lpAddNo.getSolvingTime();
				    
				    n1AddO += addO.getNbNodes();
				    n1AddOo += addOo.getNbNodes();
				    n1AddNo += addNo.getNbNodes();
				    n2AddO += dagO.getNbNodes();
				    n2AddOo += dagOo.getNbNodes();
				    n2AddNo += dagNo.getNbNodes();
				    
				    if (emptyCore)
				    	nbEmptyCore++;
					
					listType.clear();
					listPlayer.clear();
					
					content = i+", "+j+", ";
					content += time1+", "+time2+", "+time3+", "+time4+", ";
					content += tempsCg +", "+tempsAddO+", "+tempsAddOo+", "+tempsAddNo+", ";
					content += lpCg.getConstructTime()+", "+lpAddO.getConstructionTime()+", "+lpAddOo.getConstructionTime()+", "+lpAddNo.getConstructionTime()+", ";
					content += lpCg.getSolvingTime()+", "+lpAddO.getSolvingTime()+", "+lpAddOo.getSolvingTime()+", "+lpAddNo.getSolvingTime()+", ";
					content += addO.getNbNodes()+", "+addOo.getNbNodes()+", "+addNo.getNbNodes()+", ";
					content += dagO.getNbNodes()+", "+dagOo.getNbNodes()+", "+dagNo.getNbNodes()+", "+emptyCore+"\n";
					bw2.write(content);
			    }
			    
			    bw2.close();
			    indiceInfoGame++;
			    
				content = i+", "+j+", ";
				content += t1Nu/nbGame+", "+t1AddO/nbGame+", "+t1AddOo/nbGame+", "+t1AddNo/nbGame+", ";
				content += t2Nu/nbGame+", "+t2AddO/nbGame+", "+t2AddOo/nbGame+", "+t2AddNo/nbGame+", ";
				content += t3Nu/nbGame+", "+t3AddO/nbGame+", "+t3AddOo/nbGame+", "+t3AddNo/nbGame+", ";
				content += t4Nu/nbGame+", "+t4AddO/nbGame+", "+t4AddOo/nbGame+", "+t4AddNo/nbGame+", ";
				content += n1AddO/nbGame+", "+n1AddOo/nbGame+", "+n1AddNo/nbGame+", ";
				content += n2AddO/nbGame+", "+n2AddOo/nbGame+", "+n2AddNo/nbGame+", "+nbEmptyCore+"\n";
				
			    bw.write(content);
			}
		}
		
		bw.close();

	}

}
