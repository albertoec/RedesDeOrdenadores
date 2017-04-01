package ejecutable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Random;

import hilos.NuevoCliente;

/**
 * Clase que implementa la funcionalidad del Servidor. Capaz de atender
 * peticiones que sigan el protocolo TCP e UDP
 * 
 * @author expploitt --> Moisés Carral Ortiz
 * @author cloclick --> Alberto Estévez Caldas
 */

public class Servidor extends Thread {

	private File fichero;
	private int puerto;
	public static int longitud = 0;
	public static HashMap<Integer, String> refranero = new HashMap<>();

	public Servidor() {

	}

	public Servidor(File fichero, int puerto) {
		super();
		this.fichero = fichero;
		this.puerto = puerto;
	}

	public File getFichero() {
		return fichero;
	}

	public void setFichero(File fichero) {
		this.fichero = fichero;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		if (!(args.length == 3)) {
			System.out.println("Uso: servidor puerto fichero-de-mensajes\n");
			System.exit(0);
		}

		Servidor servidor = new Servidor(new File(args[2]), Integer.parseInt(args[1]));

		// Leo el fichero con los mensajes a enviar a los clientes
		servidor.leerFichero();
		System.out.println("\n\n\t********** Server inicializado **********\n\n");

		Thread hiloTcp = new Thread(new Servidor(new File(args[2]), Integer.parseInt(args[1])));
		hiloTcp.start();

		while (true) {
			try {
				byte[] bufferEntrada = new byte[125];
				DatagramSocket servidorUdp = new DatagramSocket(Integer.parseInt(args[1]));
				DatagramPacket paqueteEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);
				servidorUdp.receive(paqueteEntrada);
				System.out.println("\n\tConexión UDP establecida! --> Cliente:" + paqueteEntrada.getAddress());
				
				byte[] bufferSalida = Servidor.refranero().getBytes();
				DatagramPacket paqueteSalida = new DatagramPacket(bufferSalida, bufferSalida.length, paqueteEntrada.getAddress(),
						paqueteEntrada.getPort());
				servidorUdp.send(paqueteSalida);
				servidorUdp.close();
				System.out.println("\n\tConexión UDP finalizada! --> Cliente: " + paqueteSalida.getAddress());
			} catch (NumberFormatException e) {
			} catch (SocketException e) {
			} catch (IOException e) {
			} finally {
				System.out.println("\n\n\tEsperando clientes...\n");
			}
		}

	}

	/**
	 * 
	 * @return
	 */
	public static String refranero() {

		// Generamos un número aleatorio entre 0 y (Longitud-1) y le sumamos
		// 1 para no tener en cuenta la cabecera del fichero
		int puntero = new Random().nextInt(longitud);
		return refranero.get(puntero);
	}

	public void leerFichero() {

		String line = "";

		try {
			BufferedReader bf = new BufferedReader(new FileReader(this.fichero));

			// Obtenemos el número de líneas del fichero
			while ((line = bf.readLine()) != null) {
				if (line.contains("*"))
					continue;
				refranero.put(longitud, line);
				// System.out.println(line + refranero.keySet() + "\n");
				longitud++;
			}

			bf.close();

		} catch (FileNotFoundException e) {
			System.out.println("Fichero inexistente, por favor, asegúrese de que el fichero está en la ruta adecuada");
		} catch (IOException e) {
		}
	}

	@Override
	public void run() {

		try {

		
			ServerSocket serverSocket = new ServerSocket(this.getPuerto());

			while (true) {
				System.out.println("\n\n\tEsperando clientes...\n");
				Socket cliente = serverSocket.accept();
				System.out.println("\n\tConexión TCP establecida! ---> Cliente: " + cliente.getInetAddress());
				Thread nuevoCliente = new Thread(new NuevoCliente(cliente, this.getFichero()));
				nuevoCliente.start();
	
			}
		} catch (IOException e) {
		}
	}

}
