package net.mgsx.dl11.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HeroAnimator {

	public static final int FRONT = 0;
	public static final int BACK = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	// private Texture texture;
	
	// private final Array<Array<TextureRegion>> regions = new Array<Array<TextureRegion>>();
	private TextureRegion region;

	public HeroAnimator(TextureRegion region) {
		this.region = region;
		
		/*
		for(int i=0 ; i<4 ; i++){
			Array<TextureRegion> frames = new Array<TextureRegion>();
			regions.add(frames);
			for(int j=0 ; j<1 ; j++){
				frames.add(new TextureRegion(texture, i * 32, j * 64, 32, 64));
			}
			
		}
		*/
	}

	public TextureRegion get(int orientation, int frame) {
		return region;
	}

}
