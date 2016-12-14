package com.gdx.invader.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdx.invader.main.Game;

import static com.gdx.invader.states.Play.*;

/**
 * Created by Apelsinas Jr on 2014.11.15.
 */
public class HUD {
    private Player player;
    private TextureRegion crystals;
    private TextureRegion heart;
    private TextureRegion[] font;
    private TextureRegion bg;
    private TextureRegion gg;

    public HUD(Player player) {
        this.player = player;
        Texture tex = Game.res.getTexture("hud");
        Texture tex2 = Game.res.getTexture("bg");
        Texture tex3 = Game.res.getTexture("gg");
        bg = new TextureRegion(tex2, 320, 240);
        gg = new TextureRegion(tex3, 320, 240);
        crystals = new TextureRegion(tex, 16, 0, 16, 16);
        heart = new TextureRegion(tex, 0, 0, 16, 16);

        font = new TextureRegion[11];
        for(int i = 0; i < 6; i++) {
            font[i] = new TextureRegion(tex, 0 + i * 9, 16, 9, 9);
        }
        for(int i = 0; i < 5; i++) {
            font[i + 6] = new TextureRegion(tex, 0 + i * 9, 25, 9, 9);
        }
    }

    public void Render(SpriteBatch sb){
        sb.begin();
        sb.draw(crystals, 10, 10);
        sb.draw(heart, 10, 35);
        if (!gameOn)
            sb.draw(bg, 10, 10);
        if (won)
            sb.draw(gg, 0, 10);
        drawString(sb, player.getNumCrystals() + " / " + player.getTotalCrystals(), 42, 13);
        drawString(sb, ((int)player.getLives() -(int)lostLives) + " / " + (int)player.getLives(), 42, 38);
        sb.end();
    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == '/') c = 10;
            else if(c >= '0' && c <= '9') c -= '0';
            else continue;
            sb.draw(font[c], x + i * 9, y);
        }
    }

}
