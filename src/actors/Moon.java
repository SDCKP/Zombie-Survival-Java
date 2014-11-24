package actors;

import zombies.Stage;

public class Moon extends Actor {
    
    private int vx;
    private long duracion, inicio;

    public Moon(Stage stage) {
        super(stage);
        //Ajustamos la posicion de inicio de la luna
        this.y = 280;
        this.x = 0;
        //Indicamos el sprite de la luna
        setSpriteNames(new String[] {"moon.gif"});
    }
    
    public void setDuracion(long seg) {
        //Inicializamos la luna y sus contadores
        inicio = System.currentTimeMillis();
        this.duracion = seg*1000;
        this.y = 280;
        this.x = 0;
    }
    
    public long getDuracion() {
        return duracion;
    }
    
    public void setInicio(long in) {
        inicio = in;
        this.y = 280;
        this.x = 0;
    }
    
    @Override
    public void act() {
        //Si la luna está dentro de la pantalla
        if (x >= 0 && x+width < Stage.WIDTH) {
            int xAnterior = x;
            //Colocamos la luna según la formula que depende de el tiempo que le quede a el nivel para acabar
            x = (int)(((System.currentTimeMillis()-inicio) * Stage.WIDTH) / (duracion));
            //Si esta en la primera mitad de la pantalla, la luna sube
            if (xAnterior != x && x <= Stage.WIDTH/2-(width/2)) {
                y--;
            }
            //Cuando pasa de la mitad de la pantalla la luna baja
            if (xAnterior != x && x >= Stage.WIDTH/2-(width/2)) {
                y++;
            }
        }
    }
    
    public boolean hasFinished() {
        //Indica si la luna ha finalizado o no
        return x+width >= Stage.WIDTH;
    }

}
