package actors;

import zombies.Stage;
import java.awt.Graphics2D;

public class Block extends Actor {

    protected final int MAX_HEALTH = 500;
    protected int currentHealth;
    protected int vy;
    protected String spriteState;

    public Block(Stage stage) {
        super(stage);
        currentHealth = MAX_HEALTH;
        vy = 0;
        //Agregamos los sprites de los cubos en sus diferentes estados
        setSpriteNames(new String[]{"block_100.jpg", "block_80.jpg", "block_60.jpg", "block_40.jpg", "block_20.jpg"});
        //Indicamos que el sprite actual al crear el cubo es el del 100% de vida
        spriteState = "100";
    }

    public void place(int x, int y) {
        //Coloca el cubo en el mapa, es decir, en la posicion indicada
        this.x = x;
        this.y = y;
    }

    public void decreaseHealth(int value) {
        //Quita vida al cubo
        currentHealth -= value;
    }

    @Override
    public void act() {
        vy = 0;
        //Si la vida llega a 0, elimina el cubo
        if (currentHealth <= 0) {
            remove();
        }
        //Si el cubo no está en el suelo, cae (ajustamos su velocidad de caida a la indicada en el stage)
        if (y + height < Stage.BASE_GROUND) {
            vy = Stage.FALL_SPEED;
        }
        //Cambiamos la posicion "y" dependiendo de la velocidad de caida
        y += vy;
        //Comprobamos que el cubo no cae por debajo del suelo, si es así lo colocamos en el suelo
        if (y + height > Stage.BASE_GROUND) {
            y = Stage.BASE_GROUND - height;
        }
        //Cambiamos el sprite del cubo dependiendo de la vida que le quede
        if (currentHealth * 100 / MAX_HEALTH > 80) { //Vida entre 100% y 81%
            spriteState = "100";
        } else if (currentHealth * 100 / MAX_HEALTH < 80 && currentHealth * 100 / MAX_HEALTH > 60) { //Entre 80% y 61%
            spriteState = "80";
        } else if (currentHealth * 100 / MAX_HEALTH < 60 && currentHealth * 100 / MAX_HEALTH > 40) { //Entre 60% y 40%
            spriteState = "60";
        } else if (currentHealth * 100 / MAX_HEALTH < 40 && currentHealth * 100 / MAX_HEALTH > 20) { //Entre 40% y 20%
            spriteState = "40";
        } else if (currentHealth * 100 / MAX_HEALTH < 20) { //Menos del 20%
            spriteState = "20";
        }
    }

    @Override
    public boolean colision(Actor a) {
        //Si colisiona con otro bloque se coloca encima de este
        if (a instanceof Block && a.getBounds().intersects(getBounds())) {
            a.setY(y - a.getHeight());
            return true;
        }
        //Si colisiona con un zombie desde arriba mata al zombie y el bloque pierde la mitad de su vida actual
        if (a instanceof Zombie) {
            if (a.getY() > y + height - 6 && a.getY() < y + height) {
                a.remove();
                currentHealth /= 2;
            }
        }
        return false;
    }

    @Override
    public void paint(Graphics2D g) {
        //Dibuja el bloque en el estado actual
        g.drawImage(spriteCache.getSprite("block_" + spriteState + ".jpg"), x, y, stage);
    }
}
