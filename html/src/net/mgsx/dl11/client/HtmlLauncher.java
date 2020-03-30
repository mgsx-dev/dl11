package net.mgsx.dl11.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;

import net.mgsx.dl11.DL11Game;
import net.mgsx.dl11.model.GameSettings;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                GwtApplicationConfiguration config = new GwtApplicationConfiguration(GameSettings.HUD_WIDTH * 2, GameSettings.HUD_HEIGHT * 2);
                config.preferFlash = false;
                return config;
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new DL11Game();
        }
        
        @Override
        public void onModuleLoad () {
            super.onModuleLoad();
            com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {
                  public void onResize(ResizeEvent ev) {
                	  final int width = ev.getWidth();
                	  final int height = ev.getHeight();
                	  getCanvasElement().setWidth(width);
                	  getCanvasElement().setHeight(height);
                	  
                	  // TODO try with this to confirms there is no need for the post runnable trick
                	  // Gdx.graphics.setWindowedMode(width, height);
                	  // TODO see gwt fullscreen mode already implemented !
                	  
                	  Gdx.app.postRunnable(new Runnable() {
    					@Override
    					public void run() {
    						Gdx.app.getApplicationListener().resize(width, height);
    					}
    				});
                  }
                });
            disableAudioLogs();
        }

        public static native void disableAudioLogs() /*-{
    		$wnd.soundManager.setupOptions.debugMode = false;
    	}-*/;
        
        public static native int getWndWidth() /*-{
    		return $wnd.innerWidth;
    	}-*/;
        
        public static native int getWndHeight() /*-{
    		return $wnd.innerHeight;
    	}-*/;
}