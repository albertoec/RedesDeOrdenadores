package hilos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import ejecutable.Servidor;

/**
 * 
 * @author Expploitt --> Moisés Carral Ortiz
 * @author cloclick --> Alberto Estévez Caldas
 */
public class NuevoCliente extends Thread {

	Socket cliente;
	File fichero;

	public NuevoCliente(Socket cliente, File fichero) {
		this.cliente = cliente;
		this.fichero = fichero;

	}

	@Override
	public void run() {

		DataOutputStream out;
		DataInputStream in;
		new Servidor();
		try {
			// Servidor servidor = new Servidor();
			while (true) {
				out = new DataOutputStream(cliente.getOutputStream());
				in = new DataInputStream(cliente.getInputStream());
				out.writeUTF("\t" + Servidor.refranero() + "\n\n\t" + "¿Desea recibir otro mensaje?(S/N)");
				if (in.readUTF().equalsIgnoreCase("S")) {
					continue;
				} else {
					cliente.close();
					break;
				}
			}
		} catch (IOException e) {
		} finally {
			System.out.println("\n\tConexión TCP finalizada! --> Cliente: " + cliente.getInetAddress() + "\n\n\tEsperando clientes...\n\n");
		}
	}

}
