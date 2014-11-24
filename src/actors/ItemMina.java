package actors;

import zombies.Stage;

public class ItemMina extends Item {

    public ItemMina(Stage stage) {
        super(stage);
        //Carga los sprites del objeto mina
        setSpriteNames(new String[]{"item_mina_0.gif", "item_mina_1.gif"});
    }

    @Override
    public boolean colision(Actor a) {
        //Si cae sobre un zombi el objeto se elimina
        if (a instanceof Zombie) {
            remove();
        }
        //Si cae sobre el jugador aumenta el contador de bloques de este (y se elimina el objeto bloque)
        if (a instanceof Player) {
            ((Player) a).minesLeft++;
            remove();
        }
        //Si cae sobre un bloque se coloca sobre este
        
        if (a instanceof Block) {
            y = a.getY() - height;
        }
        return false;
    }

}
