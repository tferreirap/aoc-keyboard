package com.thiago.aockeyboard;

import java.util.LinkedList;
import java.util.List;

import com.thiago.aockeyboard.client.TCPClient;
import com.thiago.aockeyboard.keyboard.Keyboard;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	
	//private Keyboard keys;
	private TCPClient tcpClient;
	private Integer port = 1212;				// same port as SecondScreen???
	private Vibrator vibe;
	private List<View> components;
	
	private boolean buttonsVisible = true;
	private ImageButton settingsBut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		vibe = (Vibrator) MainActivity.this
				.getSystemService(Context.VIBRATOR_SERVICE);
		
		setupTCPClient();
		setupComponents();
		
		// Making the buttons transparent initially
		toggleVisibility();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setupComponents() {
		this.components = new LinkedList<View>();
		
		this.components.add(this.findViewById(R.id.button_1));
		this.components.add(this.findViewById(R.id.button_2));
		this.components.add(this.findViewById(R.id.button_3));
		this.components.add(this.findViewById(R.id.button_4));
		this.components.add(this.findViewById(R.id.button_5));
		this.components.add(this.findViewById(R.id.button_6));
		this.components.add(this.findViewById(R.id.button_7));
		this.components.add(this.findViewById(R.id.button_8));
		this.components.add(this.findViewById(R.id.button_9));
		this.components.add(this.findViewById(R.id.button_0));
		
		this.components.add(this.findViewById(R.id.button_q));
		this.components.add(this.findViewById(R.id.button_w));
		this.components.add(this.findViewById(R.id.button_e));
		this.components.add(this.findViewById(R.id.button_r));
		this.components.add(this.findViewById(R.id.button_t));
		this.components.add(this.findViewById(R.id.button_y));
		this.components.add(this.findViewById(R.id.button_u));
		this.components.add(this.findViewById(R.id.button_i));
		this.components.add(this.findViewById(R.id.button_o));
		this.components.add(this.findViewById(R.id.button_p));
		
		this.components.add(this.findViewById(R.id.button_a));
		this.components.add(this.findViewById(R.id.button_s));
		this.components.add(this.findViewById(R.id.button_d));
		this.components.add(this.findViewById(R.id.button_f));
		this.components.add(this.findViewById(R.id.button_g));
		this.components.add(this.findViewById(R.id.button_h));
		this.components.add(this.findViewById(R.id.button_j));
		this.components.add(this.findViewById(R.id.button_k));
		this.components.add(this.findViewById(R.id.button_l));
		
		this.components.add(this.findViewById(R.id.button_z));
		this.components.add(this.findViewById(R.id.button_x));
		this.components.add(this.findViewById(R.id.button_c));
		this.components.add(this.findViewById(R.id.button_v));
		this.components.add(this.findViewById(R.id.button_b));
		this.components.add(this.findViewById(R.id.button_n));
		this.components.add(this.findViewById(R.id.button_m));
		
		this.components.add(this.findViewById(R.id.toggleButton_Shift));
		this.components.add(this.findViewById(R.id.button_space));
		this.components.add(this.findViewById(R.id.button_back));
		this.components.add(this.findViewById(R.id.button_enter));

		this.components.add(this.findViewById(R.id.button_dot));
		this.components.add(this.findViewById(R.id.button_at));
		this.components.add(this.findViewById(R.id.button_underline));
		
		settingsBut = (ImageButton) findViewById(R.id.imageButton_settings);
		settingsBut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				toggleVisibility();
			}
		});
	}
	
	public void buttonOnClick(View view) {
		vibe.vibrate(20);
		view.playSoundEffect(SoundEffectConstants.CLICK);
		Button but = (Button) view;
		String keyText = but.getText().toString().toLowerCase();
		Log.wtf("MyTag", keyText);
		
		tcpClient.send(keyText);
		
	}
	
	private void toggleVisibility() {
		float alpha = buttonsVisible ? 0.0f : 1.0f;
		for (View component : this.components) 
			component.setAlpha(alpha);
		
		buttonsVisible = !buttonsVisible;
		
	}
	
	private void setupTCPClient() {
		tcpClient = TCPClient.getInstace();
		tcpClient.setPort(port);
	}
	

}
