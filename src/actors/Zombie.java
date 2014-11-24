package actors;

import zombies.Stage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import resources.SoundCache;

public class Zombie extends Actor {

    protected int vx;
    protected final int ZOMBIE_SPEED = 1;
    protected String spriteState;
    private long walktime, killTimer;
    private int velocidad;

    public Zombie(Stage stage) {
        super(stage);
        //Carga los sprites de los zombis
        loadSprites();
        setFrameSpeed(17);
        walktime = System.currentTimeMillis();
        //Contador de muerte, para mostrar el sprite de el zombi quemandose si muere
        killTimer = 0;
        //El sprite por defecto
        spriteState = "walk_left_0";
        //Cargamos el audio de su muerte al pisar una mina
        soundCache.getAudioClip("explosion.wav");
        //Indicamos la hitbox de colision con el jugador (inferior al sprite del zombi)
        hitbox = new Rectangle(x + 10, y + 10, width - 15, height - 15);
    }

    public void kill() {
        //Inicializamos el contador
        killTimer = System.currentTimeMillis();
        //Cambiamos el sprite
        spriteState = "die";
        //Reproducimos el sonido de la muerte
        soundCache.playSound("explosion.wav");
    }

    @Override
    public void act() {
        super.act();
        hitbox.setLocation(x + 10, y + 10);
        //Comprueba que el zombi no esta muriendose
        if (killTimer == 0) {
            //Si el jugador esta a su izquierda, va hacia la izquierda y cambia su sprite
            if (x > stage.getPlayer().getX()) {
                spriteState = "walk_left_" + currentFrame;
                vx = -ZOMBIE_SPEED;
            } else { //Si el jugador esta a su derecha, va hacia la derecha y cambia su sprite
                spriteState = "walk_right_" + currentFrame;
                vx = ZOMBIE_SPEED;
            }
            //Ajusta la velocidad para que vaya más lento (es un zombi al fin y al cabo)
            if (System.currentTimeMillis() - walktime > velocidad) {
                walktime = System.currentTimeMillis();
                x += vx;
            }
        }
        //Si ha estado más de 500 ms muriendose, lo eliminamos
        if (killTimer != 0 && System.currentTimeMillis() - killTimer > 500) {
            remove();
        }

    }

    public int getVx() {
        return vx;
    }

    public void setVx(int i) {
        vx = i;
    }

    @Override
    public boolean colision(Actor m) {
        //Si choca con un bloque
        if (m instanceof Block) {
            //Le quita vida al bloque paulatinamente
            ((Block) m).decreaseHealth(1);
            //Mueve al zombi a los laterales del cubo para evitar que se meta dentro de este
            if (m.getX() > x) {
                x = m.getX() - m.getWidth();
            }
            if (m.getX() < x) {
                x = m.getX() + m.getWidth();
            }
            return true;
        }
        //Si colisiona con un jugador
        if (m instanceof Player) {
            //Si sus hitbox coinciden
            if (hitbox.intersects(m.hitbox)) {
                //Mata al jugador (finaliza la partida)
                ((Player) m).kill();
            }
        }
        return false;
    }

    @Override
    public void paint(Graphics2D g) {
        //Dibuja el sprite actual del zombi
        g.drawImage(spriteCache.getSprite("zombie_" + spriteState + ".gif"), x, y, stage);
        if (Stage.debug) {
            g.setPaint(Color.RED);
            g.drawRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
        }
    }

    public final void loadSprites() {
        String[] spr = new String[]{"zombie_walk_left_0.gif", "zombie_walk_left_1.gif",
            "zombie_walk_left_2.gif", "zombie_walk_left_3.gif", "zombie_walk_left_4.gif",
            "zombie_walk_left_5.gif", "zombie_walk_left_6.gif", "zombie_walk_right_0.gif",
            "zombie_walk_right_1.gif", "zombie_walk_right_2.gif", "zombie_walk_right_3.gif",
            "zombie_walk_right_4.gif", "zombie_walk_right_5.gif", "zombie_walk_right_6.gif"};
        setSpriteNames(spr);
    }

    public void setVelocidad(int vel) {
        this.velocidad = vel;
    }
}
