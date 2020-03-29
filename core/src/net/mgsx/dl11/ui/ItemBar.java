package net.mgsx.dl11.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class ItemBar extends Table
{
	private Array<Image> images = new Array<Image>();
	private int value;
	private TextureRegion regionOn;
	private TextureRegion regionOff;
	
	public ItemBar(int init, int max, TextureRegion regionOn,  TextureRegion regionOff, Skin skin) {
		super(skin);
		
		this.regionOn = regionOn;
		this.regionOff = regionOff;
		
		setRound(false);
		
		this.value = init;
		
		defaults().pad(.01f);
		
		for(int i=0 ; i<max ; i++){
			Image img = new Image(regionOff);
			img.setScaling(Scaling.fit);
			images.add(img);
			add(img).size(16f / 32f);
		}
		
	}
	
	@Override
	public void act(float delta) {
		
		for(int i=0 ; i<images.size ; i++){
			if(i < value){
				((TextureRegionDrawable)images.get(i).getDrawable()).setRegion(regionOn);
				// images.get(i).setColor(Color.WHITE);
			}else{
				((TextureRegionDrawable)images.get(i).getDrawable()).setRegion(regionOff);
				// images.get(i).setColor(Color.GRAY);
			}
			// images.get(i).getColor().a = i < value ? 1 : .2f;
		}
		super.act(delta);
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
