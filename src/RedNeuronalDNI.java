

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase que modela una red neuronal de un n�mero indeterminado de 
 * entradas, una capa oculta con un n�mero indeterminado de nodos
 * y una �nica salida.
 * <p>
 * Nota: Para que el fichero {@code .net} se lea correctamente los pesos
 * tienen que estar al rev�s ({@code 8: 1,89164,  7: 0,03796,  6:-0,47484,  5:-0,23870...});
 * si no, hay que comentar la l�nea 116 y descomentar la 117.
 * 
 * @author Antonio Toro
 */
public class RedNeuronalDNI 
{	
	public String name,
	              netFile;
	public int inputCount  = 0,
	           hiddenCount = 0;
	public final boolean normalized;
	public double normalizationMin,
	              normalizationMax;
	
	public ArrayList<ArrayList<Double>> pesos;
	public ArrayList<Double> bias;
	
	public static final String LETRAS_NIF = "TRWAGMYFPDXBNJZSQVHLCKE";
	
	/**
	 * Constructor de una red neuronal de una �nica salida.
	 * 
	 * @param netFile       Archivo {@code .net} de la red neuronal
	 * @param normalized    Indicar {@code true} o {@code false} seg�n est�
	 *                      normalizada o no la red neuronal
	 * @param normalization Dos paramentos que son los valores minimo y maximo para 
	 *                      desnormalizar la red
	 */
	public RedNeuronalDNI(String netFile, boolean normalized, double... normalization) {
		this.netFile = netFile;
		this.normalized = normalized;
		
		if (normalized) {
			try{
				this.normalizationMin = normalization[0];
				this.normalizationMax = normalization[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("Error al crear la red \nNo has indicado los valores minimo y maximo para desnormalizar");
				System.exit(1);
			}
		}
		
		pesos = new ArrayList<ArrayList<Double>>();
		bias = new ArrayList<Double>();
		
		FileReader fr = null;
		BufferedReader br = null;
		String linea;
		ArrayList<String> lineasBias = new ArrayList<String>();
		
		try {
			fr = new FileReader (netFile);
			br = new BufferedReader(fr);
			
			// Saltamos las 3 primeras lineas
			saltarLineas(br,3);
			
			// Cogemos el nombre de la red
			linea = br.readLine();
			this.name = linea.split(":\\s+")[1];

			// Saltamos otras 3 lineas
			saltarLineas(br,23);
			
			// Almacenamos las lineas que contienen los BIAS y contamos el numero de nodos
			// de entrada y de capa oculta
			boolean biasLeidos = false;
			do {
				linea = br.readLine();
				
				String[] arr = linea.split("\\|"); 
				if(arr[5].trim().equals("i")) {
					inputCount++;
				}
				else if (arr[5].trim().equals("h")) {
					hiddenCount++;
					lineasBias.add(linea);
				}
				else if (arr[5].trim().equals("o")) {
					biasLeidos = true;
					lineasBias.add(linea);
				}
				
			} while (!biasLeidos);
			
			// Ahora extraemos los BIAS de las lineas que hemos guardado
			for (int i = 0; i < lineasBias.size(); i++) {
				String str = lineasBias.get(i);
				
				String[] arr = str.split("\\|"); 
				
				bias.add( Double.parseDouble(arr[4].trim().replace(',','.')) );
			}
			
			// Saltamos 7 lineas
			saltarLineas(br, 7);
			
			// Ahora leemos los pesos de la capa oculta y del nodo de salida
			for (int i = 0; i < hiddenCount+1; i++) { // hiddenCount+1 por el nodo de salida
				linea = br.readLine();
				String[] partes = linea.split(",*\\s*[0-9]+:\\s*"); 
				
				pesos.add(new ArrayList<Double>());
				for (int j = partes.length-1; j > 0; j--) {
//				for (int j = 1; j < partes.length; j++) { // Sustituir esta linea
					String peso = partes[j].trim();
					pesos.get(i).add(Double.parseDouble(peso.trim().replace(',','.')));
				}
			}
			
			
		} catch (IOException e) {
			System.err.println("No se ha podido crear la red");
			e.printStackTrace();
		} 
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
		SalidaDNI salida = new SalidaDNI(dni);
		
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
				tmpHiddenValue += inputValues[j] * pesos.get(i).get(j);
			
			hiddenValues[i] = tmpHiddenValue + bias.get(i);
			
			hiddenValues[i] = 1.0/(1.0+Math.exp(-hiddenValues[i]));
			
			computedOutput += hiddenValues[i] * pesos.get(pesos.size()-1).get(i);
		}
		
		// Contabilizamos el error de clasificacion y el error cuadratico
		if(normalized) {
			if(Math.round(desnormalizar(computedOutput)) != outputValue) {
				contadorErrorClasif++;
			}
			
			double tmpErr = desnormalizar(computedOutput)-outputValue;

			error_aprx += Math.abs(Math.round(desnormalizar(computedOutput))-outputValue);
			error_2 += Math.pow(tmpErr,2);
			error += Math.abs(tmpErr);
			
			computedOutput = Math.round(desnormalizar(computedOutput));
		}
		else { 
			if(Math.round(computedOutput) != outputValue) {
				contadorErrorClasif++;
			}
			
			double tmpErr = computedOutput-outputValue;
			
			error_aprx += Math.abs(Math.round(computedOutput)-outputValue);
			error_2 += Math.pow(tmpErr,2);
			error += Math.abs(tmpErr);
			
			computedOutput = Math.round(computedOutput);
		}

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
	public Salida ejecutar(String patternFile) {
		String linea;
		FileReader fr = null;
		BufferedReader br = null;
		int numEjemplos, numInputs;
		double[] inputValues  = new double[inputCount];
		double[] hiddenValues = new double[hiddenCount];
		double   outputValue,
		         computedOutput,
		         error      = 0.0,
		         error_2    = 0.0,
		         error_aprx = 0.0;
		int contadorErrorClasif = 0;
		Salida salida = new Salida(this.name);
		
		try {
			fr = new FileReader (patternFile);
			br = new BufferedReader(fr);

			// Saltamos 3 lineas
			saltarLineas(br,3);

			// Cogemos el numero de muestras
			linea = br.readLine();
			numEjemplos = Integer.parseInt( linea.split("\\s+")[4] );
			
			// Cogemos el numero de entradas
			linea = br.readLine();
			numInputs = Integer.parseInt( linea.split("\\s+")[5] );

			// Si no coinciden el numero de entradas de la red y del archivo de muestras se sale
			if (numInputs != this.inputCount) {
				System.err.println("Number of inputs in pattern file ("+numInputs+") does "
						+ "not match with number of inputs of the net ("+inputCount+")");
				System.exit(1);
			}
			
			// Salta 2 lineas
			saltarLineas(br,2);
			
			// Leemos las muestras
			while( (linea=br.readLine()) != null )
			{
				String[] lineaSeparada = linea.split("\\t");
				computedOutput = 0.0;
				
				// Leemos las entradas
				for (int i = 0; i < inputCount; i++) {
					inputValues[i] = Double.parseDouble(lineaSeparada[i]);
				}
				// Leemos la salida
				outputValue = Double.parseDouble(lineaSeparada[lineaSeparada.length-1]);

				// Calculamos el valor computado de salida
				for (int i = 0; i < hiddenCount; i++) {
					double tmpHiddenValue = 0.0;
					
					for (int j = 0; j < inputValues.length; j++)
						tmpHiddenValue += inputValues[j] * pesos.get(i).get(j);
					
					hiddenValues[i] = tmpHiddenValue + bias.get(i);
					
					hiddenValues[i] = 1.0/(1.0+Math.exp(-hiddenValues[i]));
					
					computedOutput += hiddenValues[i] * pesos.get(pesos.size()-1).get(i);
				}
				
				// Contabilizamos el error de clasificacion y el error cuadratico
				if(normalized) {
					if(Math.round(desnormalizar(computedOutput)) != Math.round(desnormalizar(outputValue))) {
						contadorErrorClasif++;
					}
					
					double tmpErr;
					tmpErr = desnormalizar(computedOutput)-desnormalizar(outputValue);

					error_aprx += Math.abs(Math.round(desnormalizar(computedOutput))-Math.round(desnormalizar(outputValue)));
					error_2 += Math.pow(tmpErr,2);
					error += Math.abs(tmpErr);
				}
				else { 
					if(Math.round(computedOutput) != outputValue) {
						contadorErrorClasif++;
					}
					
					double tmpErr;
					tmpErr = computedOutput-outputValue;
					
					error_aprx += Math.abs(Math.round(computedOutput)-outputValue);
					error_2 += Math.pow(tmpErr,2);
					error += Math.abs(tmpErr);
				}
			}

			salida.error = error;
			salida.error_2 = error_2;
			salida.error_aprx = error_aprx;
			salida.errorClasif = contadorErrorClasif;
			salida.samples = numEjemplos;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return salida;
	}
	
	/**
	 * Salta un n�mero de l�neas de un {@link BufferedReader}
	 * @param br     Buffer del que se quiere saltar l�neas
	 * @param numero N�mero de lineas a saltar
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
		for (ArrayList<Double> arrayList : pesos) {
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
	 * Desnormaliza un n�mero seg�n los parametros de normalizaci�n de la red
	 * @param number N�mero a desnormalizar
	 * @return El n�mero desnormalizado
	 */
	private double desnormalizar(double number) {
		return desnormalizar(number, normalizationMin, normalizationMax);
	}
	
	/**
	 * Desnormaliza un n�mero entre dos valores.
	 * @param number N�mero a desnormalizar
	 * @param min    M�nimo valor para desnormalizar
	 * @param max    M�ximo valor para desnormalizar
	 * @return El n�mero desnormalizado
	 */
	public static double desnormalizar(double number, double min, double max) {
		return number * (max-min) + min;
	}
	
	/**
	 * Clase que modela la salida de la ejecuci�n de la red neuronal sobre
	 * un conjunto de datos.
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
		
		public Salida(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			String str = "";

			str += this.name + "\n";
			str += " N\u00FAmero de muestras: " + samples + "\n";
			str += " Error total: " + error + "\n";
			str += " Error total medio: " + error/samples + "\n";
			str += " Error cuadr\u00E1tico: " + error_2 + "\n";
			str += " Error cuadr\u00E1tico medio: " + error_2/samples + "\n";
			str += " Error de aproximaci\u00F3n: " + error_aprx/samples + "\n";
			str += " Error de clasificaci\u00F3n: " + 100.0*errorClasif/samples + "%\n";
			
			return str;
		}
	}
	
	public class SalidaDNI extends Salida {
		public int dni,
		           letra = -1;
		
		public SalidaDNI(int dni) {
			super(""+dni);
			this.dni = dni;
		}

		
		@Override
		public String toString() {
			String str = super.toString();

			str += " Letra obtenida:" + LETRAS_NIF.charAt(letra) + "\n";
			str += " Letra esperada:" + LETRAS_NIF.charAt(dni%23) + "\n";
			
			return str;
		}
	}

}
