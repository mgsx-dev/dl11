package net.mgsx.dl11.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

import net.mgsx.dl11.model.GameSettings;
import net.mgsx.dl11.model.GameState;

public class Audio {

	private final ObjectMap<String, Sound> sfxMap = new ObjectMap<String, Sound>();
	private final ObjectMap<String, Music> musicMap = new ObjectMap<String, Music>();
	
	private Music currentMusic;
	
	private final SoundLooper lifeAlarm;
	private final SoundLooper dronesFire;
	private final SoundLooper dronesActive;
	
	public Audio() {
		lifeAlarm = new SoundLooper(getSFX("alarm-1"));
		dronesFire = new SoundLooper(getSFX("laserlong-10"));
		dronesActive = new SoundLooper(getSFX("alertlong-1"));
	}
	
	public void playMusicMenu(){
		playMusic("GameJam1");
	}
	
	public void playMusicHero(){
		playMusic("Nappes2");
	}
	
	public void playMusicCar(){
		playMusic("Vehicle2");
	}
	
	public void playMusicFail(){
		playMusic("GameJam1"); // TODO other music ?
	}
	
	public void playMusicOutro(){
		playMusic("GameJam1"); // TODO other music ?
	}
	
	private void playMusic(String name) {
		Music music = getMusic(name);
		if(music == currentMusic){
			return;
		}
		if(currentMusic != null){
			currentMusic.stop();
			currentMusic = null;
		}
		currentMusic = music;
		music.setLooping(true);
		music.setVolume(GameSettings.MUSIC_VOLUME);
		music.play();
	}
	
	private Music getMusic(String name) {
		Music music = musicMap.get(name);
		if(music == null){
			musicMap.put(name, music = Gdx.audio.newMusic(Gdx.files.internal("music/" + name + ".mp3")));
		}
		return music;
	}
	
	private void playSFX(String name, float volume) {
		Sound sound = getSFX(name);
		sound.play(volume * GameSettings.SFX_VOLUME);
	}
	
	private Sound getSFX(String name) {
		Sound sound = sfxMap.get(name);
		if(sound == null){
			sfxMap.put(name, sound = Gdx.audio.newSound(Gdx.files.internal("sfx/" + name + ".wav")));
		}
		return sound;
	}

	public void playTypingChar(){
		playSFX("bip-2", 1);
	}
	public void playTypingBlank(){
		playSFX("bip-3", 1);
	}
	public void playTypingLine(){
		playSFX("bip-7", 1);
	}
	public void playTypingHint(){
		playSFX("boing-1", 1);
	}

	
	public void playMapOpen(){
		playSFX("boing-2", 1);
	}
	public void playMapClose(){
		playSFX("boing-3", 1);
	}
	
	public void playPickupFuel(){
		playSFX("explose-5", 1);
	}
	public void playPickupMedpack(){
		playSFX("explose-6", 1);
	}
	
	public void playState(GameState game){
		lifeAlarm.setActive(game.heroLife <= GameSettings.HERO_LIFE_ALARM, 1);
	}
	
	public void clearState() {
		lifeAlarm.stop();
		dronesFire.stop();
		dronesActive.stop();
	}
	
	public void playDroneStates(boolean firing, boolean activated){
		// stack SFXs
		dronesFire.setActive(firing, 1);
		dronesActive.setActive(activated, 1);
	}
	
	public void playDroneDetect(){
		playSFX("laser-2", 1);
	}
	
	public void playHeroHurtByDrone(){
		playSFX("explose-6", 1);
	}
	public void playHeroHurtByRadiation(){
		playSFX("explose-6", 1);
	}
	public void playHeroDead(){
		playSFX("explose-6", 1);
	}
	
	
	
	public void playMenuButton(){
		playSFX("explose-6", 1);
	}
	
	public void playCarOff(){
		playSFX("explose-6", 1);
	}
	
	public void playBonusDenied(){
		playSFX("explose-6", 1);
	}
	
	public void playLastTileNoCar(){
		playSFX("explose-6", 1);
	}
	
	public void playLastTileWithCar(){
		playSFX("explose-6", 1);
	}
	
	public void playExitingCar(){
		playSFX("explose-6", 1);
	}

}
