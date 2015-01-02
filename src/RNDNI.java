import java.util.ArrayList;



public class RNDNI {

	public static void main(String[] args) {
		ArrayList<Integer> dnis = new ArrayList<Integer>();
		
		if (args.length > 0) {
			try {
				for (int i = 0; i < args.length; i++) {
					dnis.add(Integer.parseInt(args[i]));
				}
			} catch (Exception e) {
				System.err.println("Par\u00E1metros incorrectos");
				System.err.println("Uso: RNDNI dni1 [dni2 dni3...]");
			}
		} else {
			System.err.println("Faltan par\u00E1metros");
			System.err.println("Uso: RNDNI dni1 [dni2 dni3...]");
		}
		
		RedNeuronalDNI redNeuronal = new RedNeuronalDNI();
		for (Integer dni : dnis) {
			System.out.println(redNeuronal.averiguarLetra(dni));
		}
	}

}
