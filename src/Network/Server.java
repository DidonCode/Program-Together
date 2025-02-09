package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Repository.Repository;
import Tools.Logs;

public class Server implements Runnable {
	
	private ServerSocket serverSocket;
	private List<Socket> remoteClient;
	
	public ClientThread[] clientThreads;
	
	private int port;
	public int maxClient;
	
	public boolean isHost = false;
	
	public Server(int port, int maxClient) {
		this.port = port;
		this.maxClient = maxClient;
		
		this.remoteClient = new ArrayList<Socket>(maxClient);
		this.clientThreads = new ClientThread[maxClient];
	}
	
	public void start() {
		if(isHost) return;
		
		try {
			serverSocket = new ServerSocket(this.port);
			serverSocket.setSoTimeout(10);
		}catch (IOException e) {
			new Logs("Error to start hosting", e, false);
			return;
		}
		
		isHost = true;
	}
	
	public void stop() {
		if(!isHost) return;
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			new Logs("Error to stop hosting", e, false);
			return;
		}
		
		isHost = false;
		serverSocket = null;
	}
	
	public void run() {
		this.connexionRequest();
	}
	
	public void newClientCallback(ClientThread client) {}
	
	public void timeoutClientCallBack(ClientThread client) {}
	
	public void connexionRequest() {
		if(!isHost) return;
		
		Socket tempSocket = null;
		
		try {
			tempSocket = serverSocket.accept();
			
			if(tempSocket != null && remoteClient.size() < maxClient) {
				remoteClient.add(tempSocket);
				System.out.println(tempSocket.getInetAddress() + " connected");
				for(int i = 0; i < clientThreads.length; i++) {
					if(clientThreads[i] == null) {
						clientThreads[i] = new ClientThread(remoteClient.get(i), i);
						clientThreads[i].run();
						newClientCallback(clientThreads[i]);
						return;
					}
				}
			}
		} catch (IOException e) {
			
		}
	}
	
	public void connexionPing() {
		for(int i = 0; i < remoteClient.size(); i++) {
			if(clientThreads[i] != null && clientThreads[i].isFinish()) {
				if(clientThreads[i].isTimeout()) {
					System.err.println("/" + clientThreads[i].getSocket().getInetAddress().getHostAddress() + " disconected");
					timeoutClientCallBack(clientThreads[i]);
					remoteClient.remove(clientThreads[i].getSocket());
					clientThreads[i] = null;
				}else{
					System.out.println("Ping: " + clientThreads[i].getPing() + " ms for " + clientThreads[i].getSocket().getInetAddress().getHostAddress());
					clientThreads[i].run();
				}
			}
		}
	}
	
	public void sendDataAll(String data) {
		for(int i = 0; i < remoteClient.size(); i++) {
			if(clientThreads[i] != null) {
				clientThreads[i].sendData(data);
			}
		}
	}
	
	public void sendData(String data, ClientThread client) {
		if(client == null) return;
		client.sendData(data);
	}
	
	public void sendData(String data, String ip) {
		for(int i = 0; i < clientThreads.length; i++) {
			if(clientThreads[i] != null && clientThreads[i].getSocket().getInetAddress().getHostAddress().contentEquals(ip)) {
				clientThreads[i].sendData(data);
			}
		}
	}
	
	public HashMap<ClientThread, List<String>> getDataAll() {
		HashMap<ClientThread, List<String>> datas = new HashMap<ClientThread, List<String>>();
		
		for(int i = 0; i < remoteClient.size(); i++) {
			if(clientThreads[i] != null) {
				List<String> data = clientThreads[i].recieveData();
				if(data == null) continue;
				
				datas.put(clientThreads[i], data);
			}
		}
		
		return datas;
	}
	
}
