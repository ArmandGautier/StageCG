package Example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import ADD.ADD;
import ADD.GenerationOfADDwithType;
import ADD.Node;
import CoalitionGame.Player;
import CoalitionGame.Type;
import DAG.DAG;
import LinearProgram.EmptyCore;
import LinearProgram.TestCore;
import LinearProgram.findStableSC;
import Tools.GetMethod;
import Tools.GetSkill;
import Tools.Tools;

public class Test8 {
	
	// test pour comparer les algos DAG vs ADD pour savoir si le C-Core est vide.

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CloneNotSupportedException, IOException {
		
		int nbPlayer = 20;
		
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
	    
	    content = "nbPlayer, nbType, moyNbAddNode, moyNbDagNode, moyTimeSCAdd, moyTimeSCDag, nbGameSCused, nb1, nb2, nb3, nb4\n";
		  
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
			    
			    content = "nbPlayer, nbType, nbAddNode, nbDagNode, COS, timeSCAdd, timeSCDag, ADDSolved, DagSolved, nbAddVar, nbAddCon, nbDagVar, nbDagCon";
			    
			    int moyNbNodeAdd=0;
			    int moyNbNodeDag=0;
			    double moyTimeSCAdd=0;
			    double moyTimeSCDag=0;
			    int nbGameSCused = 0;
			    int nb1 = 0;
			    int nb2 = 0;
			    int nb3 = 0;
			    int nb4 = 0;
			    
			    for (int k=0; k<nbGame; k++) {
			    	
			    	//System.out.println("Game"+k);
				
					Tools.generatePlayerWithType(i, GetSkill.getSkill(j), listPlayer, listType);
					
					int[] weight = Tools.generatePatronIdeal(listType,i);
					
					if (k==0) {
						for (int g=0; g<weight.length; g++) {
							content += ", weight"+g;
						}
						content += "\n";
						bw2.write(content);
					}
			
					/*
					for (int g=0; g<weight.length; g++) {
						System.out.println(weight[g]);
					}
					*/
					Method m = GetMethod.getMethod(j);
					
				    double timeSCAdd=0;
				    double timeSCDag=0;
				    boolean isSolved = false;
				    boolean addSolved = false;
				    boolean dagSolved = false;
					
					ADD add = GenerationOfADDwithType.createADDwithPlayer(listType, listPlayer, m, weight, true);
					
					DAG dag = add.createDAG();
					/*
					dag.writeDAGinDOT("dag.DOT");
					add.writeADDinDOT("add.DOT");
					*/
					EmptyCore lp = new EmptyCore();
					
					lp.solve(dag,i);
					
					double[] sol = lp.getSol();
					double cos = lp.getCos();
					
					findStableSC sCAdd = new findStableSC();
					findStableSC sCDag = new findStableSC();
					
					if (cos > 0.0001) {
						
						nbGameSCused++;
						
						sCAdd.solve(add, i, sol);
						sCDag.solve(dag, i, sol);
						
						timeSCAdd = sCAdd.getSolvingTime();
						timeSCDag = sCDag.getSolvingTime();
						moyTimeSCAdd+=timeSCAdd;
						moyTimeSCDag+=timeSCDag;
						moyNbNodeAdd+=add.getNbNodes();
						moyNbNodeDag+=dag.getNbNodes();
						addSolved = sCAdd.isSolved();
						dagSolved = sCDag.isSolved();
						
						double[] arcsDroitAdd = sCAdd.getEdgeRight();
						double[] arcsGaucheAdd = sCAdd.getEdgeLeft();
						double[] arcsDroitDag = sCDag.getEdgeRight();
						double[] arcsGaucheDag = sCDag.getEdgeLeft();
	
						// reconstruire chemin into vérifier avec TestCore que c'est ok
						
						HashMap<Integer,ArrayList<Player>> listCoalitionADD = new HashMap<Integer,ArrayList<Player>>();
						HashMap<Integer,ArrayList<Player>> listCoalitionDAG = new HashMap<Integer,ArrayList<Player>>();
						for (int h=1; h<=nbPlayer; h++) {
							ArrayList<Player> coalitionADD = new ArrayList<Player>();
							ArrayList<Player> coalitionDAG = new ArrayList<Player>();
							listCoalitionADD.put(h, coalitionADD);
							listCoalitionDAG.put(h, coalitionDAG);
						}
						
						//System.out.println("ADD "+sCAdd.isSolved());
						//System.out.println("DAG "+sCDag.isSolved());
						
						if (dagSolved) {
							
							Node nodeDAG = dag.getRoot();
							int chemin = 1;
							
							while (chemin <= nbPlayer) {
								while (! nodeDAG.isLeaf()) {
									int idNode = nodeDAG.getId();
									if (arcsDroitDag[idNode]==1) {
										arcsDroitDag[idNode]--;
										listCoalitionDAG.get(chemin).add((Player) nodeDAG.getIdVariable());
										nodeDAG = nodeDAG.getRightChild();
									}
									else {
										if (arcsGaucheDag[idNode]>0) {
											arcsGaucheDag[idNode]--;
											nodeDAG = nodeDAG.getLeftChild();
										}
										else {
											break;
										}
									}
								}
								
								if (nodeDAG.isLeaf()) {
									if (Tools.abs(nodeDAG.getValue() - Tools.sum(sol,listCoalitionDAG.get(chemin),dag.getVarOrder())) > 0.0001) {
										System.out.println(nodeDAG.getValue());
										System.out.println(Tools.sum(sol,listCoalitionDAG.get(chemin),dag.getVarOrder()));
										System.out.println("pasOkok : problème valeur feuille");
										dag.writeDAGinDOT("dag.DOT");
										add.writeADDinDOT("add.DOT");
									}
								}
								chemin++;
								nodeDAG = dag.getRoot();
							}
						}
							
						if (addSolved) {
							
							Node node = add.getRoot();
							int chemin = 1;
							
							while (chemin <= nbPlayer) {
								while (! node.isLeaf()) {
									int idNode = node.getId();
									if (arcsDroitAdd[idNode]==1) {
										arcsDroitAdd[idNode]--;
										listCoalitionADD.get(chemin).add((Player) node.getIdVariable());
										node = node.getRightChild();
									}
									else {
										if (arcsGaucheAdd[idNode]>0) {
											arcsGaucheAdd[idNode]--;
											node = node.getLeftChild();
										}
										else {
											break;
										}
									}
								}
								
								if (node.isLeaf()) {
									if (Tools.abs(node.getValue() - Tools.sum(sol,listCoalitionADD.get(chemin),dag.getVarOrder())) > 0.0001) {
										System.out.println(node.getValue());
										System.out.println(Tools.sum(sol,listCoalitionADD.get(chemin),dag.getVarOrder()));
										System.out.println("pasOkok : problème valeur feuille");
										dag.writeDAGinDOT("dag.DOT");
										add.writeADDinDOT("add.DOT");
									}
								}
								chemin++;
								node = add.getRoot();
							}
						}
						
						
						if (dagSolved) {
							if (addSolved) {
								nb1++;
							}
							else {
								nb2++;
							}
						}
						else {
							if (addSolved) {
								nb3++;
							}
							else {
								nb4++;
							}
						}
					
						if (dagSolved) {
							if (addSolved) {
								nb4++;
							}
							else {
								nb3++;
							}
						}
						else {
							if (addSolved) {
								nb2++;
							}
							else {
								nb1++;
							}
						}	
					
						
						if ( ! TestCore.isStable(dag, sol)) {
							System.out.println("pasOkok : notStable");
							dag.writeDAGinDOT("dag.DOT");
							add.writeADDinDOT("add.DOT");
						}
						
						/*
						for (Integer key : listCoalition.keySet()) {
							System.out.println("Dans la coalition " + key + " il y a :");
							for ( Player p : listCoalition.get(key)) {
								System.out.println("Le joueur "+p.getRepresentation() + " de type "+ p.getType().getName());
							}
						}
						*/
					}
					
					content = i + ", " + j + ", " + add.getNbNodes() + ", " + dag.getNbNodes() + ", " + cos + ", " + timeSCAdd + ", " + timeSCDag + ", " + addSolved + ", " + dagSolved; 
					content += ", " + sCAdd.getNbVariable();
					content += ", " + sCAdd.getNbContrainte();
					content += ", " + sCDag.getNbVariable();
					content += ", " + sCDag.getNbContrainte();
					for (int g=0; g<weight.length; g++) {
						content += ", "+weight[g];
					}
					
					content += "\n";
					bw2.write(content);
					
					listType.clear();
					listPlayer.clear();
			    }
			    
			    if (nbGameSCused==0)
			    	nbGameSCused++;
			    
			    bw2.close();
			    indiceInfoGame++;
			    content =   i + ", " + j + ", " + moyNbNodeAdd/nbGameSCused + ", " + moyNbNodeDag/nbGameSCused + ", " + moyTimeSCAdd/nbGameSCused + ", " + moyTimeSCDag/nbGameSCused + ", " + nbGameSCused + ", " + nb1 + ", " + nb2 + ", " + nb3 + ", " + nb4 + "\n";;
			    bw.write(content);
			}
		}
		
		bw.close();
	}

}
