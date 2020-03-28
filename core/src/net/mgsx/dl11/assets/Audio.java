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
		lifeAlarm = new SoundLooper(getSFX("needmed"));
		dronesFire = new SoundLooper(getSFX("tir-1")); // TODO ?
		dronesActive = new SoundLooper(getSFX("laserdetection"));
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
		playSFX("biplettre", 1);
	}
	public void playTypingBlank(){
		playSFX("bipespace", 1);
	}
	public void playTypingLine(){
		playSFX("bipfinligne", 1);
	}
	public void playTypingHint(){
		playSFX("pressx", 1);
	}

	
	public void playMapOpen(){
		playSFX("entremap", 1);
	}
	public void playMapClose(){
		playSFX("sortiemap", 1);
	}
	
	public void playPickupFuel(){
		playSFX("fioul", 1);
	}
	public void playPickupMedpack(){
		playSFX("med", 1);
	}
	
	public void playState(GameState game){
		lifeAlarm.setActive(game.heroLife <= GameSettings.HERO_LIFE_ALARM, 1);
	}
	
	public void clearState() {
		lifeAlarm.stop();
		clearDrones();
	}
	
	public void playDroneStates(boolean firing, boolean activated){
		// stack SFXs
		dronesFire.setActive(firing, 1);
		dronesActive.setActive(activated, 1);
	}
	
	public void clearDrones(){
		dronesFire.stop();
		dronesActive.stop();
	}
	
	public void playDroneDetect(){
		playSFX("lasersurfaisceau", 1);
	}
	
	public void playHeroHurtByDrone(){
		playSFX("lasertouche", 1);
	}
	public void playHeroHurtByRadiation(){
		playSFX("geiger", 1);
	}
	public void playHeroDead(){
		playSFX("failed", 1);
	}
	
	public void playCarNoFioul(){
		playSFX("nofioul", 1);
	}
	
	
	public void playMenuButton(){
		playSFX("start", 1);
	}
	
	public void playBonusDenied(){
		playSFX("sacplein", 1);
	}
	
	public void playLastTileNoCar(){
		playSFX("lastmapnocar", 1);
	}
	
	public void playLastTileWithCar(){
		playSFX("lastmapcar", 1);
	}
	
	public void playExitingCar(){
		playSFX("sortiecar", 1);
	}

}
