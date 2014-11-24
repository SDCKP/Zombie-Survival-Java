package zombies;

import resources.SoundCache;
import resources.SpriteCache;
import actors.Actor;
import actors.Player;
import java.awt.image.ImageObserver;

public interface Stage extends ImageObserver {
	public static final int WIDTH=640;
	public static final int HEIGHT=480;
	public static final int SPEED=10;
        public static final int FALL_SPEED = 2;
        public static final int BASE_GROUND = HEIGHT-30;
        public static final boolean debug = false;
	public SpriteCache getSpriteCache();
        public SoundCache getSoundCache();
        public Player getPlayer();
        public void setGameOver();
        
        public void addActor(Actor a);

}
