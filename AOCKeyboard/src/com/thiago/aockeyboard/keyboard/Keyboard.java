package com.thiago.aockeyboard.keyboard;

import java.util.Collection;
import java.util.HashSet;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.thiago.aockeyboard.client.TCPClient;

/**
 * Class to manager the keyboard to the SecondScreen application
 * 
 * @author iglesonFreire
 */
public class Keyboard {

	private EditText editText;
	private boolean initialized;
	private InputMethodManager imm;
	private Collection<OnSendListener> listenersSend;
	private Collection<OnKeyClickListener> listenersKey;

	public Keyboard(EditText editText, InputMethodManager imm) {
		this.editText = editText;
		this.editText.setOnKeyListener(new View.OnKeyListener() {
			private boolean warned = false;

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (!warned) {
					warnsOnKeyListeners(keyCode);
				}
				warned = !warned;
				return false;
			}
		});
		this.imm = imm;
		listenersSend = new HashSet<OnSendListener>();
		listenersKey = new HashSet<OnKeyClickListener>();
		this.initialized = false;
	}

	/**
	 * Init the keyboard, making the EditText visible and calling the keyboard.
	 * 
	 * @throws IllegalStateException
	 *             throws this exception if this keyboard is already
	 *             initialized.
	 */
	public void init() {
		if (!this.initialized) {
			this.editText.setVisibility(View.VISIBLE);
			this.imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			this.initialized = true;
		} else {
			throw new IllegalStateException(
					"the keyboard is already initialized");
		}
	}

	/**
	 * Finish the keyboard, making the EditText invisible and calling back the
	 * keyboard.
	 * 
	 * @throws IllegalStateException
	 *             throws this exception if this keyboard isn't initialized.
	 */
	public void finish() {
		if (this.initialized) {
			this.imm.hideSoftInputFromWindow(this.editText.getWindowToken(), 0);
			this.editText.setText("");
			this.editText.setVisibility(View.INVISIBLE);
			this.initialized = false;
		} else {
			throw new IllegalStateException("the keyboard isn't initialized");
		}
	}

	/**
	 * Send the data from the asynchronous server.
	 */
	public void send() {
		if (this.initialized) {
			TCPClient.getInstace().send(this.editText.getText().toString());
			warnsOnSendListeners();
		} else {
			throw new IllegalStateException(
					"the keyboard is already initialized");
		}
	}

	public void addOnSendListener(OnSendListener listener) {
		this.listenersSend.add(listener);
	}

	public void addOnKeyClickListener(OnKeyClickListener listener) {
		this.listenersKey.add(listener);
	}

	private void warnsOnSendListeners() {
		for (OnSendListener listener : listenersSend) {
			listener.warns(this.editText.getText().toString());
		}
	}

	private void warnsOnKeyListeners(int keyCode) {
		for (OnKeyClickListener listener : listenersKey) {
			listener.warns(keyCode);
		}
	}
}
