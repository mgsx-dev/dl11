package net.mgsx.dl11.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PixelPerfectViewport extends FitViewport {
	
	private float divisor;

	public PixelPerfectViewport(float worldWidth, float worldHeight, float divisor) {
		super(worldWidth, worldHeight);
		this.divisor = divisor;
	}

	@Override
	public void update(int screenWidth, int screenHeight, boolean centerCamera) {
		
		// get the min screen/world rate from width and height
		float wRate = screenWidth / (getWorldWidth() * divisor);
		float hRate = screenHeight / (getWorldHeight() * divisor);
		float rate = Math.min(wRate, hRate);
		
		// round it down and limit to one
		int iRate = Math.max(1, MathUtils.floor(rate));
		
		// compute rounded viewport dimension
		int viewportWidth = (int)(getWorldWidth() * iRate * divisor);
		int viewportHeight = (int)(getWorldHeight() * iRate * divisor);
		
		// Center.
		setScreenBounds((screenWidth - viewportWidth) / 2, (screenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);

		apply(centerCamera);
	}
}
