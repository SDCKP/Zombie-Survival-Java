package actors;

import zombies.Stage;

public class ItemBlock extends Item {

    public ItemBlock(Stage stage) {
        super(stage);
        //Carga los sprites del objeto block
        setSpriteNames(new String[]{"item_block_0.gif", "item_block_1.gif"});
    }

    @Override
    public boolean colision(Actor a) {
        //Si cae sobre un zombi el objeto se elimina
        if (a instanceof Zombie) {
            remove();
        }
        //Si cae sobre el jugador aumenta el contador de bloques de este (y se elimina el objeto bloque)
        if (a instanceof Player) {
            ((Player) a).blocksLeft++;
            remove();
        }
        //Si cae sobre un bloque se coloca sobre este
        if (a instanceof Block) {
            y = a.getY() - height;
        }
        return false;
    }

}
