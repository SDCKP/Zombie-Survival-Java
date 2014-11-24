package actors;

import zombies.Stage;
import java.awt.Graphics2D;

public class Mina extends Block {

    public Mina(Stage stage) {
        super(stage);
        //Carga el sprite de la mina
        setSpriteNames(new String[]{"mina.gif"});
    }

    @Override
    public boolean colision(Actor a) {
        //Si colisiona con un bloque
        if (a instanceof Block) {
            //Reduce la vida del bloque a la mitad
            ((Block) a).currentHealth /= 2;
            //Elimina la mina
            remove();
            return true;
        }
        //Si colisiona con un zombi
        if (a instanceof Zombie) {
            //Solo se detona cuando la mina no esta cayendo
            if (vy == 0) {
                //mata al zombi
                ((Zombie) a).kill();
                //Elimina la mina
                remove();
                return true;
            }
        }
        return false;
    }

    public int getVy() {
        return vy;
    }

    @Override
    public void paint(Graphics2D g) {
        //Dibuja la mina en el escenario
        g.drawImage(spriteCache.getSprite(spriteNames[0]), x, y, stage);
    }

}
