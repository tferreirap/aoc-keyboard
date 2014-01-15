package com.thiago.aockeyboard;

import java.util.ArrayList;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.thiago.aockeyboard.client.NetworkDiscovery;
import com.thiago.aockeyboard.client.TCPClient;
import com.thiago.aockeyboard.client.util.Util;

@SuppressLint("NewApi")
public class ConnectActivity extends Activity {

	private LinkedList<View> components;
	private TCPClient tcpClient;
	ProgressDialog dialog;
	public ArrayList<String> ips = new ArrayList<String>();
	private String ipTV = "";
	
	// *************** HANDLERS **************
	
	private Handler handlerOpenDialog = new Handler() {
        @Override
            public void handleMessage(Message msg) {
	            dialog = ProgressDialog.show(ConnectActivity.this, "Searching AOC TVs", "Searching...");
	            dialog.setIcon(R.drawable.search_icon);
	            dialog.setCancelable(false);
        }
    };
    private Handler handlerCloseDialog = new Handler() {
        @Override
            public void handleMessage(Message msg) {
            dialog.dismiss();
        }
    };
    
    private Handler handlerRefreshIps = new Handler(){
       	
    	public void handleMessage(Message msg) {
    		if(ips.size()==0){
    			Toast.makeText(getApplicationContext(), "No TV found, please check your connection.",Toast.LENGTH_LONG)
    			.show();
    		}
    		for(String ip : ips){
    			ipTV = ip;
    			//tEdit.setText(ip);
    			conectar();	// chamar a tela do conectar 
    		}
    	};
    	
    };
    
    // *************** END OF HANDLERS ***************
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		tcpClient = TCPClient.getInstace();
		Log.wtf("WTF", "onCreate of ConnectActivity ----------");
//		new Handler().postDelayed(new Runnable() {		// Talvez eles usem pra dar tempo fazer algo
//			@Override
//			public void run() {
//				setButtonsEnabled(true);
//			}
//		}, 1000);
		
		buscarTVs();	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connect, menu);
		return true;
	}
	
	public void conectar(){
		//String ipAddr = tEdit.getText().toString();
		String ipAddr = ipTV;
		
		if (ipAddr == null || ipAddr.equals("") ){
			Toast.makeText(getApplicationContext(), "Ip may not be empty", Toast.LENGTH_SHORT).show();
		}
		
		else{
			tcpClient.setIpAddr(ipAddr);
			Intent mainIntent = new Intent(ConnectActivity.this, MainActivity.class);
			ConnectActivity.this.startActivity(mainIntent);
			ConnectActivity.this.finish();
		}
	}
	
    private void buscarTVs(){
		Runnable waitingThread = new Runnable() {
			
			@Override
			public void run() {
				try{
					handlerOpenDialog.sendEmptyMessage(0);
				}
				catch(Exception e){
					Log.e("AOCKeyboard", e.getMessage());
				}
			}
		};
		
		Thread waitingThreadWorker = new Thread(waitingThread);
		waitingThreadWorker.start();

		Runnable discoveryThread = new Runnable() {
			
			@Override
			public void run() {
				try{
					ips = NetworkDiscovery.findTvInNetwork(Util.getIPAddress(true));
					handlerRefreshIps.sendEmptyMessage(0);
					handlerCloseDialog.sendEmptyMessage(0);
				}
				catch(Exception e){
					Log.e("AOCKeyboard", e.getMessage());
				}
			}
		};
		Thread discoveryThreadWorker = new Thread(discoveryThread);
		discoveryThreadWorker.start();
    }
	
    public void searchButton(View view) {
    	buscarTVs();
    }
    
	// Check if the content of a TextView is valid, in this case, IP ADDRESS
/*	
	private void enableFilter() {
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart)
							+ source.subSequence(start, end)
							+ destTxt.substring(dend);
					if (!resultingTxt
							.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
						return "";
					} else {
						String[] splits = resultingTxt.split("\\.");
						for (int i = 0; i < splits.length; i++) {
							if (Integer.valueOf(splits[i]) > 255) {
								return "";
							}
						}
					}
				}
				return null;
			};
		};
		tEdit.setFilters(filters);

	}
*/

}
