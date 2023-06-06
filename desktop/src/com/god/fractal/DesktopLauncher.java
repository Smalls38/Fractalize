package com.god.fractal;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.god.fractal.GodFractal;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.useVsync(false);
		config.setForegroundFPS(6000);
		config.setTitle("Fractalize");
		config.setWindowedMode(2400,1300);
		new Lwjgl3Application(new GodFractal(), config);
	}
}
