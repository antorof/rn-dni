

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase que modela una red neuronal de un número indeterminado de 
 * entradas, una capa oculta con un número indeterminado de nodos
 * y una única salida.
 * <p>
 * Nota: Para que el fichero {@code .net} se lea correctamente los pesos
 * tienen que estar al revés ({@code 8: 1,89164,  7: 0,03796,  6:-0,47484,  5:-0,23870...});
 * si no, hay que comentar la línea 116 y descomentar la 117.
 * 
 * @author Antonio Toro
 */
public class RedNeuronalDNI 
{	
	public String name,
	              netFile;
	public int inputCount  = 8,
	           hiddenCount = 8;
//	public boolean normalized;
//	public double normalizationMin,
//	              normalizationMax;

//	public static ArrayList<ArrayList<Double>> pesos;
//	public static ArrayList<Double> bias;
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
	 * Constructor de una red neuronal de una única salida.
	 * 
	 * @param netFile       Archivo {@code .net} de la red neuronal
	 * @param normalized    Indicar {@code true} o {@code false} según esté
	 *                      normalizada o no la red neuronal
	 * @param normalization Dos paramentos que son los valores minimo y maximo para 
	 *                      desnormalizar la red
	 */
	public RedNeuronalDNI(String netFile, boolean normalized, double... normalization) {
//		this.netFile = netFile;
//		this.normalized = normalized;
		
//		if (normalized) {
//			try{
//				this.normalizationMin = normalization[0];
//				this.normalizationMax = normalization[1];
//			} catch (ArrayIndexOutOfBoundsException e) {
//				System.err.println("Error al crear la red \nNo has indicado los valores minimo y maximo para desnormalizar");
//				System.exit(1);
//			}
//		}
//		
//		pesos = new ArrayList<ArrayList<Double>>();
//		bias = new ArrayList<Double>();
//		
//		FileReader fr = null;
//		BufferedReader br = null;
//		String linea;
//		ArrayList<String> lineasBias = new ArrayList<String>();
//		
//		try {
//			fr = new FileReader (netFile);
//			br = new BufferedReader(fr);
//			
//			// Saltamos las 3 primeras lineas
//			saltarLineas(br,3);
//			
//			// Cogemos el nombre de la red
//			linea = br.readLine();
//			this.name = linea.split(":\\s+")[1];
//
//			// Saltamos otras 3 lineas
//			saltarLineas(br,23);
//			
//			// Almacenamos las lineas que contienen los BIAS y contamos el numero de nodos
//			// de entrada y de capa oculta
//			boolean biasLeidos = false;
//			do {
//				linea = br.readLine();
//				
//				String[] arr = linea.split("\\|"); 
//				if(arr[5].trim().equals("i")) {
//					inputCount++;
//				}
//				else if (arr[5].trim().equals("h")) {
//					hiddenCount++;
//					lineasBias.add(linea);
//				}
//				else if (arr[5].trim().equals("o")) {
//					biasLeidos = true;
//					lineasBias.add(linea);
//				}
//				
//			} while (!biasLeidos);
//			
//			// Ahora extraemos los BIAS de las lineas que hemos guardado
//			for (int i = 0; i < lineasBias.size(); i++) {
//				String str = lineasBias.get(i);
//				
//				String[] arr = str.split("\\|"); 
//				
//				bias.add( Double.parseDouble(arr[4].trim().replace(',','.')) );
//			}
//			
//			// Saltamos 7 lineas
//			saltarLineas(br, 7);
//			
//			// Ahora leemos los pesos de la capa oculta y del nodo de salida
//			for (int i = 0; i < hiddenCount+1; i++) { // hiddenCount+1 por el nodo de salida
//				linea = br.readLine();
//				String[] partes = linea.split(",*\\s*[0-9]+:\\s*"); 
//				
//				pesos.add(new ArrayList<Double>());
//				for (int j = partes.length-1; j > 0; j--) {
////				for (int j = 1; j < partes.length; j++) { // Sustituir esta linea
//					String peso = partes[j].trim();
//					pesos.get(i).add(Double.parseDouble(peso.trim().replace(',','.')));
//				}
//			}
//			
//			
//		} catch (IOException e) {
//			System.err.println("No se ha podido crear la red");
//			e.printStackTrace();
//		} 
	}
	
	/**
	 * Calcula la letra mediante la red neuronal para el DNI introducido. 
	 * @param dni DNI
	 */
	public Salida ejecutar(int dni) {
		double[] inputValues  = new double[inputCount];
		double[] hiddenValues = new double[hiddenCount];
		double   outputValue,
		         computedOutput,
		         error      = 0.0,
		         error_2    = 0.0,
		         error_aprx = 0.0;
		int contadorErrorClasif = 0;
		Salida salida = new Salida(dni);
		
		double[] digitos = new double[8];
		for(int tmp = dni, i = 7; i>=0; i--) {
			digitos[i] = tmp%10;
			tmp /= 10;
		}

		computedOutput = 0.0;
		inputValues = digitos;
		outputValue = dni%23;

		// Calculamos el valor computado de salida
		for (int i = 0; i < hiddenCount; i++) {
			double tmpHiddenValue = 0.0;
			
			for (int j = 0; j < inputValues.length; j++)
				tmpHiddenValue += inputValues[j] * pesos[i][j];
			
			hiddenValues[i] = tmpHiddenValue + bias[i];
			
			hiddenValues[i] = 1.0/(1.0+Math.exp(-hiddenValues[i]));
			
			computedOutput += hiddenValues[i] * pesos[pesos.length-1][i];
		}
		
		if(Math.round(computedOutput) != outputValue) {
			contadorErrorClasif++;
		}
		
		double tmpErr = computedOutput-outputValue;
		
		error_aprx += Math.abs(Math.round(computedOutput)-outputValue);
		error_2 += Math.pow(tmpErr,2);
		error += Math.abs(tmpErr);
		
		computedOutput = Math.round(computedOutput);

		salida.error = error;
		salida.error_2 = error_2;
		salida.error_aprx = error_aprx;
		salida.errorClasif = contadorErrorClasif;
		salida.samples = 1;
		salida.letra = (int)computedOutput;

		return salida;
	}
	
	/**
	 * Ejecuta la red neuronal sobre los datos de un fichero de muestras del tipo de
	 * JavaNNS.
	 * @param patternFile Archivo de datos sobre el que lanzar la red neuronal
	 */
//	public Salida ejecutar(String patternFile) {
//		String linea;
//		FileReader fr = null;
//		BufferedReader br = null;
//		int numEjemplos, numInputs;
//		double[] inputValues  = new double[inputCount];
//		double[] hiddenValues = new double[hiddenCount];
//		double   outputValue,
//		         computedOutput,
//		         error      = 0.0,
//		         error_2    = 0.0,
//		         error_aprx = 0.0;
//		int contadorErrorClasif = 0;
//		Salida salida = new Salida(this.name);
//		
//		try {
//			fr = new FileReader (patternFile);
//			br = new BufferedReader(fr);
//
//			// Saltamos 3 lineas
//			saltarLineas(br,3);
//
//			// Cogemos el numero de muestras
//			linea = br.readLine();
//			numEjemplos = Integer.parseInt( linea.split("\\s+")[4] );
//			
//			// Cogemos el numero de entradas
//			linea = br.readLine();
//			numInputs = Integer.parseInt( linea.split("\\s+")[5] );
//
//			// Si no coinciden el numero de entradas de la red y del archivo de muestras se sale
//			if (numInputs != this.inputCount) {
//				System.err.println("Number of inputs in pattern file ("+numInputs+") does "
//						+ "not match with number of inputs of the net ("+inputCount+")");
//				return null;
//			}
//			
//			// Salta 2 lineas
//			saltarLineas(br,2);
//			
//			// Leemos las muestras
//			while( (linea=br.readLine()) != null )
//			{
//				String[] lineaSeparada = linea.split("\\t");
//				computedOutput = 0.0;
//				
//				// Leemos las entradas
//				for (int i = 0; i < inputCount; i++) {
//					inputValues[i] = Double.parseDouble(lineaSeparada[i]);
//				}
//				// Leemos la salida
//				outputValue = Double.parseDouble(lineaSeparada[lineaSeparada.length-1]);
//
//				// Calculamos el valor computado de salida
//				for (int i = 0; i < hiddenCount; i++) {
//					double tmpHiddenValue = 0.0;
//					
//					for (int j = 0; j < inputValues.length; j++)
//						tmpHiddenValue += inputValues[j] * pesos[i][j];
//					
//					hiddenValues[i] = tmpHiddenValue + bias[i];
//					
//					hiddenValues[i] = 1.0/(1.0+Math.exp(-hiddenValues[i]));
//					
//					computedOutput += hiddenValues[i] * pesos[pesos.length-1][i];
//				}
//				
//				// Contabilizamos el error de clasificacion y el error cuadratico
////				if(normalized) {
////					if(Math.round(desnormalizar(computedOutput)) != Math.round(desnormalizar(outputValue))) {
////						contadorErrorClasif++;
////					}
////					
////					double tmpErr;
////					tmpErr = desnormalizar(computedOutput)-desnormalizar(outputValue);
////
////					error_aprx += Math.abs(Math.round(desnormalizar(computedOutput))-Math.round(desnormalizar(outputValue)));
////					error_2 += Math.pow(tmpErr,2);
////					error += Math.abs(tmpErr);
////				}
////				else { 
//					if(Math.round(computedOutput) != outputValue) {
//						contadorErrorClasif++;
//					}
//					
//					double tmpErr;
//					tmpErr = computedOutput-outputValue;
//					
//					error_aprx += Math.abs(Math.round(computedOutput)-outputValue);
//					error_2 += Math.pow(tmpErr,2);
//					error += Math.abs(tmpErr);
////				}
//			}
//
//			salida.error = error;
//			salida.error_2 = error_2;
//			salida.error_aprx = error_aprx;
//			salida.errorClasif = contadorErrorClasif;
//			salida.samples = numEjemplos;
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return salida;
//	}
	
	/**
	 * Salta un número de líneas de un {@link BufferedReader}
	 * @param br     Buffer del que se quiere saltar líneas
	 * @param numero Número de lineas a saltar
	 */
	private void saltarLineas(BufferedReader br, int numero) {
		try {
			for (int i = 0; i < numero; i++) {
				br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Imprime por pantalla los pesos de la red
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
	 * Imprime por pantalla los bias de la red
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
		public int    errorClasif = 0,
		              samples = 0;
		public String name;
		public int dni,
                   letra = -1;
		
//		public Salida(String name) {
//			this.name = name;
//		}

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
			str += " Error total medio: " + error/samples + "\n";
			str += " Error cuadr\u00E1tico: " + error_2 + "\n";
			str += " Error cuadr\u00E1tico medio: " + error_2/samples + "\n";
			str += " Error de aproximaci\u00F3n: " + error_aprx/samples + "\n";
			str += " Error de clasificaci\u00F3n: " + 100.0*errorClasif/samples + "%\n";
			str += " Letra obtenida:" + LETRAS_NIF.charAt(letra) + "\n";
			str += " Letra esperada:" + LETRAS_NIF.charAt(dni%23) + "\n";
			
			return str;
		}
	}
	
}
