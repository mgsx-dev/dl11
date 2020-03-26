package net.mgsx.dl11.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class ItemBar extends Table
{
	private Array<Image> images = new Array<Image>();
	private int value;
	
	public ItemBar(int init, int max, TextureRegion region, Skin skin) {
		super(skin);
		
		setRound(false);
		
		this.value = init;
		
		defaults().pad(.01f);
		
		for(int i=0 ; i<max ; i++){
			Image img = new Image(region);
			img.setScaling(Scaling.stretch);
			images.add(img);
			add(img).size(.5f);
		}
		
	}
	
	@Override
	public void act(float delta) {
		
		for(int i=0 ; i<images.size ; i++){
			images.get(i).getColor().a = i < value ? 1 : .2f;
		}
		super.act(delta);
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
