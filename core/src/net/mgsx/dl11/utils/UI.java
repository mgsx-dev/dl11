package net.mgsx.dl11.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import net.mgsx.dl11.assets.Assets;

public class UI 
{
	public static Actor onChange(Actor actor, Consumer<ChangeEvent> callback){
		actor.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				callback.accept(event);
			}
		});
		return actor;
	}
	
	/**
	 * example: UI.booleanControl(new TextButton(...), v->GeoBattleSettings.i.training = v, ()->GeoBattleSettings.i.training);
	 * @param bt
	 * @param set
	 * @param get
	 * @return
	 */
	public static Button booleanControl(Button bt, Consumer<Boolean> set, Supplier<Boolean> get){
		bt.setChecked(get.get());
		onChange(bt, event->set.accept(bt.isChecked()));
		return bt;
	}

	public static <T extends Enum> Actor enumControl(T[] values, T value, Skin skin, Consumer<T> set) {
		SelectBox<T> box = new SelectBox<T>(skin);
		box.setItems(values);
		box.setSelected(value);
		box.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				set.accept(box.getSelected());
			}
		});
		return box;
	}

	public static <T> Actor enumAsRadioButtons(T[] values, T defaultValue, boolean vertical, Skin skin, Consumer<T> supplier) {
		ButtonGroup group = new ButtonGroup();
		group.setMinCheckCount(1);
		group.setMaxCheckCount(1);
		Table table = new Table();
		if(vertical){
			table.defaults().fillX();
		}else{
			table.defaults().fillY();
		}
		for(T value : values){
			TextButton bt = new TextButton(value.toString(), skin, "toggle");
			if(value == defaultValue) bt.setChecked(true);
			bt.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(bt.isChecked()) supplier.accept(value);
				}
			});
			group.add(bt);
			table.add(bt);
			if(vertical){
				table.row();
			}
		}
		return table;
	}

	public static Actor blinkButton(String text) {
		TextButton bt = new TextButton(text, Assets.i.skin){
			@Override
			public void act(float delta) {
				if(UniControl.isActionJustPressed()){
					fire(new ChangeEvent());
				}
				super.act(delta);
			}
		};
		bt.getLabel().addAction(Actions.forever(Actions.sequence(
				Actions.alpha(1),
				Actions.delay(.5f),
				Actions.alpha(.5f),
				Actions.delay(.5f)
			)));
		return bt;
	}
}
