package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread implements Runnable {
	
	private Socket remoteClient;
	private int id;
	
	private PrintWriter out;
	private BufferedReader in;
	
	private boolean timeout;
	private boolean finish;
	private float ping = 0;
	
	public ClientThread(Socket remoteClient, int id) {
		this.remoteClient = remoteClient;
		this.timeout = false;
		this.ping = 0;
		this.id = id;
		
		try {
			out = new PrintWriter(remoteClient.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(remoteClient.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

	}
	
	public void sendData(String data) {
		out.println(data);
	}
	
	public List<String> recieveData() {
		List<String> datas = new ArrayList<String>();
		
		try {
			while(in.ready()) {
				datas.add(in.readLine());
			}
			return datas;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isFinish() {
		return finish;
	}
	
	public float getPing() {
		return ping;
	}
	
	public boolean isTimeout() {
		return timeout;
	}
	
	public Socket getSocket() {
		return remoteClient;
	}
	
	public int getId() {
		return id;
	}

}
