package Example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import ADD.ADD;
import ADD.GenerationOfADDwithType;
import CoalitionGame.Player;
import GainFunction.TwoSkills;
import LinearProgram.EmptyCore;

public class Test1 {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		int nbPlayer = 5;
		ArrayList<String> l = new ArrayList<String>();
		int[] tab = {2,2};
		l.add("Batteur");
		l.add("Tireur");
		
        Class[] parameterTypes = new Class[3];
        parameterTypes[0] = int[].class;
        parameterTypes[1] = ArrayList.class;
        parameterTypes[2] = int[].class;
        Method m = TwoSkills.class.getMethod("minTireurBatteur", parameterTypes);
		
		ADD<Player> add = GenerationOfADDwithType.createADDandPlayer(nbPlayer, l, m, tab, true, false);
		add.writeADDinDOT("file.DOT");
		
		EmptyCore lp = new EmptyCore();
		
		lp.solve(add,nbPlayer);
		
		double[] sol = lp.getSol();
		
		int i=0;
		ArrayList<Player> lPlayer = add.getVarOrder();
		for ( Double d : sol ) {
			System.out.println("Gain du joueur "+i+"de type "+lPlayer.get(i).getType().getName()+" : "+d);
			i++;
		}

	}

}
