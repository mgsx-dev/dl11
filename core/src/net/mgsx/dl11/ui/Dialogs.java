package net.mgsx.dl11.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.model.GameSettings;
import net.mgsx.dl11.utils.UniControl;

public class Dialogs {

	private static class DialogState{
		boolean finished = false;
		boolean skipped = false;
	}
	
	public static void spawnInfo(Stage stage, String text, int align, Runnable endCallback) {
		Skin skin = Assets.i.skin;
		
		DialogState state = new DialogState();
		
		text += "\n{BLINK=gray;red;8;0.5}Press X{ENDBLINK}{EVENT=hint}";
		
		Table alert = new Table(skin);
		alert.setBackground("typing");

		TypingLabel label = new TypingLabel(text, skin);
		label.setWrap(true);
		alert.add(label).width(GameSettings.HUD_WIDTH - 40).pad(10); // XXX padding

		Table backdrop = new Table(skin){
			@Override
			public void act(float delta) {
				if(UniControl.isActionJustPressed()){
					if(!state.finished){
						state.skipped = true;
						label.skipToTheEnd();
					}
					else if(!hasActions()){
						// TODO SFX closing ?
						addAction(Actions.sequence(Actions.alpha(0, .3f), Actions.run(endCallback), Actions.removeActor()));
					}
					
				}
				super.act(delta);
			}
		};
		backdrop.setTouchable(Touchable.enabled);
		backdrop.setBackground(skin.newDrawable("white", new Color(0,0,0,.5f)));
		backdrop.setFillParent(true);
		
		
		label.setTypingListener(new TypingAdapter(){
			@Override
			public void end() {
				state.finished = true;
				super.end();
			}
			@Override
			public void event(String event) {
				if(event.equals("hint")){
					Assets.i.audio.playTypingHint();
				}
			}
			@Override
			public void onChar(Character ch) {
				if(!state.skipped){
					if(ch == ' '){
						Assets.i.audio.playTypingBlank();
					}else if(ch == '\n'){
						Assets.i.audio.playTypingLine();
					}else{
						Assets.i.audio.playTypingChar();
					}
				}
			}
		});
		
		Cell cell = backdrop.add(alert).expand();
		if(Align.isTop(align)){
			cell.top();
		}else if(Align.isBottom(align)){
			cell.bottom();
		}else{
			cell.center();
		}
		
		stage.addActor(backdrop);
	}

}
