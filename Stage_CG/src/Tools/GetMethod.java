package Tools;

import java.lang.reflect.Method;
import java.util.ArrayList;

import GainFunction.FiveSkills;
import GainFunction.FourSkills;
import GainFunction.SixSkills;
import GainFunction.ThreeSkills;
import GainFunction.TwoSkills;

public class GetMethod {
	
	public static Method getMethod(int nbType) throws NoSuchMethodException, SecurityException {
        Class[] parameterTypes = new Class[3];
        parameterTypes[0] = int[].class;
        parameterTypes[1] = ArrayList.class;
        parameterTypes[2] = int[].class;
		switch (nbType) {
		case 2 : return TwoSkills.class.getMethod("minTireurBatteur", parameterTypes);
		case 3 : return ThreeSkills.class.getMethod("TireurBatteurTrappeur", parameterTypes);
		case 4 : return FourSkills.class.getMethod("TireurBatteurTrappeurRevendeur", parameterTypes);
		case 5 : return FiveSkills.class.getMethod("TireurBatteurTrappeurRevendeurPecheur", parameterTypes);
		case 6 : return SixSkills.class.getMethod("TireurBatteurRevendeurWithXP", parameterTypes);
		default : System.out.println("Nbplayer non compatible");
		break;
		}
		return null;
	}

}
