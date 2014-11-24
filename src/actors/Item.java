package actors;

import zombies.Stage;

public class Item extends Actor {

    private int vy;

    public Item(Stage stage) {
        super(stage);
        setFrameSpeed(5);
    }

    @Override
    public void act() {
        vy = 0;
        //Los objetos caen del cielo hasta el suelo del escenario
        if (y + height < Stage.BASE_GROUND) {
            vy = Stage.FALL_SPEED;
        }
        y += vy;
        //Si algun objeto cae por debajo del suelo lo vuelve a colocar sobre este
        if (y + height > Stage.BASE_GROUND) {
            y = Stage.BASE_GROUND - height;
        }
        //Actualiza los frames del objeto
        t++;
        if (t % frameSpeed == 0) {
            t = 0;
            currentFrame = (currentFrame + 1) % spriteNames.length;

        }
    }

}
