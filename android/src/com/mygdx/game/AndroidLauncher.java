package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("a");
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		System.out.println("a");
		initialize(new PlatformerMain(), config);
	}
}
