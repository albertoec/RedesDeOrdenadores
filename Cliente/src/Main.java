
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 
 * @author Expploitt --> Moisés Carral Ortiz
 * @author cloclick --> Alberto Estévez Caldas
 * 
 */
public class Main {

	public static void main(String[] args) {

		if (args.length != 4) {
			System.out.println("\nUso: cliente -(udp|tcp) ip_servidor puerto\n");
			System.exit(0);
		}

		if (!(args[0].equals("cliente") && (args[1].equals("-tcp") || args[1].equals("-udp")))) {
			System.out.println("\nUso: cliente -(udp|tcp) ip_servidor puerto\n");
			System.exit(0);
		}

		// String cliente = args[0];
		String protocolo = args[1].replace("'", "");
		String dirIp = args[2];
		String respuesta;
		int puerto = Integer.parseInt(args[3]);
		InetAddress ip;
		Scanner teclado = new Scanner(System.in);

		switch (protocolo.toLowerCase()) {

		case "-tcp":

			try {
				ip = InetAddress.getByName(dirIp);
				Socket socket = new Socket(ip, puerto);
				System.out.println("\n\n\t********** Conexión TCP Establecida **********\n\n");
				System.out.println("\n\t Mensajes recibidos: \n");
				while (true) {
					DataInputStream entrada = new DataInputStream(socket.getInputStream());
					DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
					System.out.println("\t" + entrada.readUTF() + "\n\n\t");
					respuesta = teclado.nextLine();
					salida.writeUTF(respuesta);
				}
			} catch (ConnectException e1) {
				System.out.println("\nServidor no disponible, inténtelo de nuevo o más tarde!\n");
			} catch (EOFException e2) {
				System.out.println("\n\n\t********** Conexión TCP Finalizada **********\n\n");
			} catch (IOException e) {
			} finally {
				teclado.close();
				System.exit(1);
			}
			break;

		case "-udp":

			try {
				ip = InetAddress.getByName(dirIp);
				byte[] buffer = new byte[125];
				DatagramSocket udp = new DatagramSocket();
				DatagramPacket paqueteSalida = new DatagramPacket(buffer, buffer.length, ip, puerto);
				udp.send(paqueteSalida);
				// Esperamos 3 segundos, si no tenemos respuesta pasamos!
				udp.setSoTimeout(3000);
				udp.receive(paqueteSalida);
				String entrada = new String(paqueteSalida.getData());
				System.out.println("\n\t Mensajes recibidos: \n\n" + "\t" + entrada + "\n");
				udp.close();

			} catch (SocketException e) {
			} catch (SocketTimeoutException e) {
				System.out.println("\n\tTiempo de respuesta finalizado! Inténtelo de nuevo ...\n\n");
			} catch (UnknownHostException e) {
				System.out.println("\n\tIp del host desconocida!\n");
			} catch (IOException e) {
			}

			break;

		default:
			System.out.println("Uso: cliente -(udp|tcp) ip_servidor puerto\n");
			System.exit(0);
		}

	}
}
