package net.mgsx.dl11.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;

public class MeterUI extends Table
{
	private Image bar;
	private final float pad = .1f;

	public MeterUI(Color color, Skin skin) {
		super(skin);
		setRound(false);
		
		setBackground("white");
		bar = new Image(skin, "white");
		bar.setScaling(Scaling.stretch);
		addActor(bar);
		
		bar.setColor(color);
		
		setColor(Color.BLACK);
	}
	
	public void setValue(float value){
		if(value > 0){
			bar.setBounds(pad, pad, value * (getWidth() - pad * 2), getHeight() - pad * 2);
			bar.setVisible(true);
		}else{
			bar.setVisible(false);
		}
	}
	
}
