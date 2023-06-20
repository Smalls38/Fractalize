package com.god.fractal;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.god.fractal.GodFractal;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.useVsync(false); //disable Vsync
		config.setForegroundFPS(8000); //i just like the number 8000
		config.setTitle("Fractalize");
		config.setWindowedMode(1920,1080);
		new Lwjgl3Application(new GodFractal(), config);
	}
}
