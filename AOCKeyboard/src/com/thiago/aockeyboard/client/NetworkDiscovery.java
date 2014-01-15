package com.thiago.aockeyboard.client;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class NetworkDiscovery {
	
	private static final int TIMEOUT = 500;
	protected static final String TAG = "SecondScreen";
	private static final int PORT = 1212;
	
	static ExecutorService es;
	
	private class Candidate{
		String ip;
		boolean active;
		
		public Candidate(String ip, boolean active) {
			this.ip = ip;
			this.active = active;
		}
		
		public String getIp() {
			return ip;
		}
		
		public boolean isActive() {
			return active;
		}
	}
	
	public static ArrayList<String> findTvInNetwork(String deviceIp){
		ArrayList<String> res = new ArrayList<String>();
		es = Executors.newFixedThreadPool(20);
		final List<Future<Candidate>> futures = new ArrayList<Future<Candidate>>();
		String[] ipArr = deviceIp.split("\\.");
		for(int i = 1 ; i < 254 ; i++){
			String ipNow = ipArr[0]+"."+ipArr[1]+"."+ipArr[2]+"."+i;
			futures.add(portIsOpen(es, ipNow, PORT, TIMEOUT));
		}
		try {
			es.awaitTermination(10, TimeUnit.SECONDS);
			es.shutdown();
		} catch (InterruptedException e1) {
			Log.e(TAG,e1.getMessage());
		}
		int open=0;
		for(final Future<Candidate> f : futures){
			try {
				if(f.get().isActive()){
					open++;
					res.add(f.get().getIp());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
        Log.v(TAG, "found "+open+" tvs");
        return res;
	}

	public static Future<Candidate> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
		  return es.submit(new Callable<Candidate>() {
		      @Override public Candidate call() {
		        try {
		          Socket socket = new Socket();
		          socket.connect(new InetSocketAddress(ip, port), timeout);
		          Log.v(TAG, "found using TCP connect "+ip+" on port=" + port);
		          socket.close();
		          NetworkDiscovery a = new NetworkDiscovery();
		          Candidate candidate = a.new Candidate(ip, true);
		          return candidate;
		        } catch (Exception ex) {
		          NetworkDiscovery a = new NetworkDiscovery();
		          Candidate candidate = a.new Candidate(ip, false);
		          return candidate;
		        }
		      }
		   });
	}
	
}
