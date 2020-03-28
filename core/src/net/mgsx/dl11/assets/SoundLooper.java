package net.mgsx.dl11.assets;

import com.badlogic.gdx.audio.Sound;

import net.mgsx.dl11.model.GameSettings;

public class SoundLooper {
	private final Sound sound;
	private long soundID;
	private boolean active;
	
	public SoundLooper(Sound sound) {
		super();
		this.sound = sound;
	}
	
	public boolean setActive(boolean active, float volume){
		if(active && !this.active){
			this.active = true;
			soundID = sound.loop(volume * GameSettings.SFX_VOLUME);
			return true;
		}
		else if(!active && this.active){
			this.active = false;
			sound.stop(soundID);
			soundID = -1;
		}
		return false;
	}
	
	public void stop(){
		if(active){
			active = false;
			sound.stop(soundID);
			soundID = -1;
		}
	}
}
