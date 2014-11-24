package actors;

import resources.SpriteCache;
import zombies.Stage;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import resources.SoundCache;

public class Actor {

    protected int x, y;
    protected int width, height;
    protected String[] spriteNames;
    protected int currentFrame;
    protected int frameSpeed;
    protected int t;
    protected Stage stage;
    protected SpriteCache spriteCache;
    protected boolean markedForRemoval;
    protected SoundCache soundCache;
    protected HashMap subSprites;
    protected Rectangle hitbox;

    public Actor(Stage stage) {
        this.stage = stage;
        spriteCache = stage.getSpriteCache();
        soundCache = stage.getSoundCache();
        currentFrame = 0;
        frameSpeed = 1;
        t = 0;
        subSprites = new HashMap();
        markedForRemoval = false;
    }

    public void paint(Graphics2D g) {
        //Dibuja el actor en la pantalla
        g.drawImage(spriteCache.getSprite(spriteNames[currentFrame]), x, y, stage);
    }

    public Rectangle getBounds() {
        //Devuelve las dimensiones del actor
        return new Rectangle(x, y, width, height);
    }

    public void remove() {
        //Marca al actor para ser eliminado
        markedForRemoval = true;
    }

    public boolean isMarkedForRemove() {
        return markedForRemoval;
    }

    public boolean colision(Actor a) {
        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int i) {
        x = i;
    }

    public int getY() {
        return y;
    }

    public void setY(int i) {
        y = i;
    }

    public int getFrameSpeed() {
        return frameSpeed;
    }

    public void setFrameSpeed(int i) {
        frameSpeed = i;
    }

    public void setSpriteNames(String[] names) {
        spriteNames = names;
        height = 0;
        width = 0;
        //Recorre el array de nombres de sprites y va creando imagenes a partir de ellos, obteniendolas de
        //los archivos correspondientes a través del spritechache
        for (int i = 0; i < names.length; i++) {
            BufferedImage image = spriteCache.getSprite(spriteNames[i]);
            height = Math.max(height, image.getHeight());
            width = Math.max(width, image.getWidth());
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int i) {
        height = i;
    }

    public void setWidth(int i) {
        width = i;
    }

    public void act() {
        t++;
        //Cambia el sprite a mostrar del actor
        if (t % frameSpeed == 0) {
            t = 0;
            currentFrame = (currentFrame + 1) % spriteNames.length;
            //Caso especial para los sprites del jugador (dependen del movimiento)
            if (this instanceof Player) {
                currentFrame = (currentFrame + 1) % 6;
            }
            //Caso especial para los zombies (dependen de hacia donde caminen)
            if (this instanceof Zombie) {
                currentFrame = (currentFrame + 1) % 7;
            }
        }
    }
}
