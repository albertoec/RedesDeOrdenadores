package hilos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import ejecutable.Servidor;

public class HiloTcp implements Runnable {

	Servidor hiloTcp;

	public HiloTcp(Servidor servidor) {
		hiloTcp = servidor;

		try {

			ServerSocket serverSocket = new ServerSocket(servidor.getPuerto());
		
			while (true) {

				Socket cliente = serverSocket.accept();
				System.out.println("\n\tConexión establecida! ---> Cliente: " + cliente.getInetAddress());
				Runnable nuevoCliente = new NuevoCliente(cliente, servidor.getFichero());
				nuevoCliente.run();
			}

		} catch (IOException e) {
			System.out.println("\n\tConexión finalizada!");
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
