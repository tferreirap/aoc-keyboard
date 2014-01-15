package com.thiago.aockeyboard.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import android.util.Log;

/**
 * @author Felipe Jose
 */
public class TCPClient {
	protected Socket clientSocket = null;
	protected PrintStream outToServer;
	protected BufferedReader inFromServer;
	protected String ipAddr;
	protected Integer port;
	protected String log = "";
	private static TCPClient instance;
	private TCPClient() {
	}

	public static TCPClient getInstace() {
		if (instance == null) {
			instance = new TCPClient();
		}
		return instance;

	}
	public synchronized String getRetorno(){
		return log;
	}
	
	public synchronized void setRetorno(String val){
		this.log = val;
	}
	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * Method that create the thread and sends the data
	 * 
	 * @param data
	 */
	public void send(String data) {
		Thread sendThread = new SendDataThread(data);
		sendThread.start();
//		try {
//			sendThread.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * Thread that send and receive data
	 * 
	 * @author Felipe Jose
	 */
	class SendDataThread extends Thread {

		String data;

		public SendDataThread(String data) {
			this.data = data;
		}

		@Override
		public void run() {
			try {
			if (clientSocket == null) {
				clientSocket = new Socket(ipAddr, port);
			}
				outToServer = new PrintStream(clientSocket.getOutputStream());
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				clientSocket.setKeepAlive(true);
				Log.i("AOCKeyboard", "Connected");


			if (data != null) {
				outToServer.println(data);
			}
			String dataRecv = "";
			while (dataRecv.length() < 1) {
				
				System.out.println("Waiting for server response...");
				dataRecv = inFromServer.readLine();
				Log.i("dataRecv" , dataRecv);
				setRetorno(dataRecv);
				sleep(100);
			}
			
				finalize();
			
		}catch (IOException e){
			clientSocket = null;
			Log.i("IOException" , e.getMessage());
			setRetorno( "IOException");
		}catch (Throwable e) {
			clientSocket = null;
			if (e.getMessage() != null){
			Log.i("Throwable" , e.getMessage());}
			
		} 
		}
	}
}