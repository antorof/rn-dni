


/**
 * Clase que modela una red neuronal que averigua la letra
 * del NIF a partir del DNI.
 * 
 * @author Antonio Toro
 */
public class RedNeuronalDNI 
{	
	public String name,
	              netFile;
	public static int inputNodes = 8;
	public static int hiddenNodes = 8;
	public static Double[][] pesos;
	public static Double[] bias;
	
	public static final String LETRAS_NIF = "TRWAGMYFPDXBNJZSQVHLCKE";
	
	static  {
		pesos = new Double[][]{
				{2.34142, 0.42671, 0.93997, -0.12917, 1.20164, -0.6876, -0.38963, 2.4701},
				{-0.66354, 1.65856, -0.10463, 0.79321, -2.43095, 0.81937, -1.32148, 1.41329},
				{-0.80218, -0.7698, 0.88339, 0.19834, -1.35149, -1.0466, -1.80542, 0.17678},
				{-1.2144, 1.828, 0.45429, 2.87657, -0.49211, 0.34896, 1.28112, 1.31905},
				{0.99711, 0.98967, 0.11285, 0.44396, 0.45432, 0.28506, 0.54005, 0.88151},
				{0.58314, 0.29642, 0.90706, 0.197, 0.95723, 1.11503, 1.15063, 0.32798},
				{-0.78009, -2.36015, 0.71165, -0.48197, -0.9393, -1.02753, 1.17197, -0.88474},
				{1.10487, -1.58285, 0.55707, 0.7622, -1.92754, -0.80365, -1.23522, 2.28496},
				{3.76518, -0.86146, -2.59259, 3.52851, 2.27698, 1.71459, 2.43247, 0.69074}
		};
		bias = new Double[]{0.16759, 1.89163, 0.65921, -0.87907, -0.73981, -0.69733, 0.01567, 1.27238, 1.8572};		
	}
	
	public RedNeuronalDNI() { }
	
	/**
	 * Calcula la letra mediante la red neuronal para el DNI introducido. 
	 * @param dni DNI
	 */
	public Salida averiguarLetra(int dni) {
		int inputCount = 0;
		double[] inputValues  = new double[inputNodes];
		double[] hiddenValues = new double[hiddenNodes];
		double   outputValue,
		         computedOutput,
		         error      = 0.0,
		         error_2    = 0.0,
		         error_aprx = 0.0;
		Salida salida = new Salida(dni);
		
		for(int tmp = dni; tmp > 0; tmp /= 10 )
			inputCount++;

		if (inputCount != 8) {
			System.err.println("DNI no v\u00E1lido");
			return null;
		}
		
		double[] digitos = new double[8];
		for(int tmp = dni, i = 7; i>=0; i--) {
			digitos[i] = tmp%10;
			tmp /= 10;
		}
		
		computedOutput = 0.0;
		inputValues = digitos;
		outputValue = dni%23;

		// Calculamos el valor computado de salida
		for (int i = 0; i < hiddenNodes; i++) {
			double tmpHiddenValue = 0.0;
			
			for (int j = 0; j < inputValues.length; j++)
				tmpHiddenValue += inputValues[j] * pesos[i][j];
			
			hiddenValues[i] = tmpHiddenValue + bias[i];
			
			hiddenValues[i] = 1.0/(1.0+Math.exp(-hiddenValues[i]));
			
			computedOutput += hiddenValues[i] * pesos[pesos.length-1][i];
		}
		
		double tmpErr = computedOutput-outputValue;
		
		error_aprx += Math.abs(Math.round(computedOutput)-outputValue);
		error_2 += Math.pow(tmpErr,2);
		error += Math.abs(tmpErr);
		
		computedOutput = Math.round(computedOutput);

		salida.error = error;
		salida.error_2 = error_2;
		salida.error_aprx = error_aprx;
		salida.letra = (int)computedOutput;

		return salida;
	}
	
	/**
	 * Imprime por pantalla los pesos de la red.
	 */
	public void printPesos() {
		for (Double[] arrayList : pesos) {
			for (Double db : arrayList) {
				System.out.print(db+" ");
			}
			System.out.println("");
		}
	}
	
	/**
	 * Imprime por pantalla los bias de la red.
	 */
	public void printBias() {
		for (Double db : bias) {
			System.out.print(db+" ");
		}
		System.out.println("");
	}
	
	/**
	 * Clase que modela la salida de la ejecución de la red neuronal.
	 * 
	 * @author Antonio Toro
	 */
	public class Salida {
		public double error       = 0.0,
		              error_2     = 0.0,
		              error_aprx  = 0.0;
		public String name;
		public int dni,
                   letra = -1;

		public Salida(int dni) {
			this.name = ""+dni;
			this.dni = dni;
		}
		
		@Override
		public String toString() {
			String str = "";

			str += this.name + "\n";
//			str += " N\u00FAmero de muestras: " + samples + "\n";
			str += " Error total: " + error + "\n";
//			str += " Error total medio: " + error/samples + "\n";
			str += " Error cuadr\u00E1tico: " + error_2 + "\n";
//			str += " Error cuadr\u00E1tico medio: " + error_2/samples + "\n";
			str += " Error de aproximaci\u00F3n: " + error_aprx + "\n";
			str += " Letra obtenida:" + LETRAS_NIF.charAt(letra) + "\n";
			str += " Letra esperada:" + LETRAS_NIF.charAt(dni%23) + "\n";
			
			return str;
		}
	}
	
}
