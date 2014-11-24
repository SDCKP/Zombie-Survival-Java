package actors;

import zombies.Stage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public final class Player extends Actor {

    protected static final int PLAYER_SPEED = 4, JUMP_HEIGHT = 100;
    protected int blocksLeft, minesLeft;
    protected int vx;
    protected int vy;
    protected String spriteState;
    private int lastMove, surface;
    private boolean left, right, jump, canLeft, canRight, canJump;
    private boolean jumping, falling, running, onTop, gamePaused;
    private long runTimer, diff, topTimer;
    private int currentItem;
    private final int availableItems;

    public Player(Stage stage) {
        super(stage);
        //Carga los sprites del personaje
        loadSprites();
        //Inicializa las variables de estado
        jumping = false;
        falling = false;
        running = false;
        canLeft = true;
        canRight = true;
        canJump = true;
        gamePaused = false;
        availableItems = 2;
        onTop = false;
        currentItem = 0;
        //Carga el sonido del salto
        soundCache.getAudioClip("jump.wav");
        //Ajusta la cantidad de minas y bloques iniciales
        if (Stage.debug) {
            blocksLeft = 99;
            minesLeft = 99;
        } else {
            blocksLeft = 5;
            minesLeft = 1;
        }
        //Estado por defecto del personaje (mirando a la izquierda)
        spriteState = "stand_left";
        //Indicamos que la superficie actual donde esta el personaje es el suelo del escenario
        surface = Stage.BASE_GROUND;
        //Timer para auto-correr
        runTimer = System.currentTimeMillis();
        //Timer para la caida desde los bloques
        topTimer = runTimer;
        //Hitbox de colisión con los zombis, inferior al tamaño de la imagen por motivos de jugabilidad
        hitbox = new Rectangle(x + 5, y + 5, width - 10, height - 10);
    }

    @Override
    public void act() {
        super.act();
        hitbox.setLocation(x + 5, y + 5);
        //Si no esta sobre un cubo por más de 50ms indica el cambio de estado
        if (System.currentTimeMillis() - topTimer > 50) {
            onTop = false;
        }
        //Aumenta la x y la y dependiendo de la velocidad
        x += vx;
        y += vy;
        //Evita que el personaje se salga por los bordes del mapa
        if (x < 0) {
            x = 0;
        }
        if (x > Stage.WIDTH - 32) {
            x = Stage.WIDTH - 32;
        }
        if (y > Stage.HEIGHT - 32) {
            y = Stage.HEIGHT - 32;
        }

        diff = System.currentTimeMillis();
        //Si no esta saltando, ajusta la superficie de reposo a la base del escenario
        if (!jumping) {
            surface = Stage.BASE_GROUND;
        }
        //Si esta sobre un cubo, cambia la superficie al cubo e indica que puede saltar
        if (onTop) {
            surface = y + height;
            canJump = true;
        }
        //Si el jugador no está en reposo sobre la superficie, ajusta la velocidad de caida
        if (surface >= y + height) {
            vy = PLAYER_SPEED;
        }
        //Cambia el sprite entre izquierda y derecha dependiendo hacia donde este mirando
        if (vx == 0 && !jumping && !falling || onTop) {
            frameSpeed = 5;
            spriteState = (lastMove == 0) ? "stand_left" : "stand_right";
        }
        //Actualiza la velocidad
        updateSpeed();
        //Calculos de salto
        jump();
    }

    public int getVx() {
        return vx;
    }

    public void setVx(int i) {
        vx = i;
    }

    public int getVy() {
        return vy;
    }

    public void setVy(int i) {
        vy = i;
    }

    protected void updateSpeed() {
        vx = 0;
        vy = 0;
        //Si se pulsa izquierda y puede ir a la izquierda
        if (left && canLeft) {
            //Calculador para el auto-correr
            if (diff - runTimer > 250 || jumping || falling) {
                runTimer = System.currentTimeMillis();
                running = true;
            }
            //Si esta corriendo cambia la velocidad del personaje
            if (running) {
                spriteState = "run_left_" + currentFrame;
                vx = -PLAYER_SPEED / 2;
            } else {
                frameSpeed = 2;
                spriteState = "run_left_" + currentFrame;
                vx = -PLAYER_SPEED;
            }
            //Si va hacia la izquierda significa que también puede ir a la derecha
            canRight = true;
        }
        //Si se pulsa derecha y puede ir a la derecha
        if (right && canRight) {
            //Calculador del auto-correr
            if (diff - runTimer > 250 || jumping || falling) {
                runTimer = System.currentTimeMillis();
                running = true;
            }
            //Si esta corriendo cambia la velocidad del personaje
            if (running) {
                spriteState = "run_right_" + currentFrame;
                vx = PLAYER_SPEED / 2;
            } else {
                frameSpeed = 2;
                spriteState = "run_right_" + currentFrame;
                vx = PLAYER_SPEED;
            }
            //Si va hacia la derecha significa que también puede ir a la izquierda
            canLeft = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (!markedForRemoval) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    left = false;
                    running = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    right = false;
                    running = false;
                    break;
                case KeyEvent.VK_SPACE:
                    jump = false;
                    break;
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (!markedForRemoval) {
            //Pulsación de teclas por parte del usuario
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT://Izquierda
                    left = true;
                    lastMove = 0;
                    break; 
                case KeyEvent.VK_RIGHT://Derecha
                    right = true;
                    lastMove = 1;
                    break; 
                case KeyEvent.VK_SPACE://Salto
                    if (canJump) {
                        jump = true;
                    }
                    break; 
                case KeyEvent.VK_SHIFT://Colocar objeto
                    place();
                    break; 
                case KeyEvent.VK_CONTROL://Siguiente objeto
                    nextItem();
                    break; 
                case KeyEvent.VK_ENTER://Pausa
                    gamePaused = !gamePaused;
                    break; 
            }
        }
    }

    private void jump() {
        //Si se pulsa saltar y se puede saltar
        if (jump && canJump) {
            //Reproducimos el sonido de salto
            soundCache.playSound("jump.wav");
            //Ajustamos el estado del personaje
            jumping = true;
            falling = false;
        }
        //Si está saltando
        if (jumping) {
            //Cambiamos el sprite
            spriteState = "jump";
            //Reduce la velocidad cuando pasa del 85% de la altura del salto
            if (y + height >= surface - ((JUMP_HEIGHT * 85) / 100)) { //Entre el 0 y el 85% de la altura del salto: velocidad normal
                vy = -PLAYER_SPEED;
            } else if (y + height >= surface - JUMP_HEIGHT) { //Entre el 86 y el 100% de la altura del salto: velocidad a la mitad
                vy = -PLAYER_SPEED / 2;
            } else { //Ha llegado al pnto más alto del salto, ahora cae
                jumping = false;
                falling = true;
            }
            //Cuando salta puede desplazarse a ambos lados, pero no puede saltar de nuevo
            canRight = true;
            canLeft = true;
            canJump = false;
        }
        //Si esta cayendo
        if (falling) {
            //Si no esta sobre un cubo, cambiamos el sprite la de caida
            if (!onTop) {
                spriteState = "fall";
            }
            //Si esta cayendo, indicamos que no esta saltando y ajustamos la velocidad de caida
            if (y + height <= surface) {
                jumping = false;
                vy = PLAYER_SPEED;
            } else { //Si no esta cayendo, le quitamos la velocidad de caida, indicamos que no esta cayendo y le permitimos saltar de nuevo
                vy = 0;
                falling = false;
                canJump = true;
            }
            //Mientras cae puede ir tanto a izquierda como a derecha
            canRight = true;
            canLeft = true;
        }
    }

    private void place() {
        //Coloca un objeto en la posicion donde está mirando
        switch (currentItem) {
            case 0: //Block
                //Comprueba que tiene bloques
                if (blocksLeft > 0) {
                    //Quitamos un bloque de nuestro contador
                    blocksLeft--;
                    //Creamos el bloque
                    Block b = new Block(stage);
                    //Lo colocamos según hacia donde estemos mirando
                    if (lastMove == 0) {
                        b.place(x - b.width, y);
                    } else if (lastMove == 1) {
                        b.place(x + width, y);
                    }
                    //Añadimos el bloque al array de actores del escenario
                    stage.addActor(b);
                }
                break;
            case 1: //Mina
                //Comprueba que tienes minas
                if (minesLeft > 0) {
                    //Quitamos una mina de nuestro contador
                    minesLeft--;
                    //Creamos la mina
                    Mina m = new Mina(stage);
                    //Lo colocamos según hacia donde estemos mirando
                    if (lastMove == 0) {
                        m.place(x - m.width, y);
                    } else if (lastMove == 1) {
                        m.place(x + width, y);
                    }
                    //Añadimos el bloque al array de actores del escenario
                    stage.addActor(m);
                }
                break;
        }

    }

    public void nextItem() {
        //Rotamos a través de la cantidad de objetos disponibles
        if (currentItem < availableItems - 1) {
            currentItem++;
        } else {
            currentItem = 0;
        }
    }

    public void kill() {
        //Cambia el sprite y finaliza el juego
        spriteState = "die";
        stage.setGameOver();
    }

    public int getSelectedItem() {
        return currentItem;
    }

    @Override
    public boolean colision(Actor a) {
        //Si colisiona con un zombie, devuelve true y el checkCollisions nos elimina
        if (a instanceof Zombie) {
            if (getBounds().intersects(a.getBounds())) {
                return true;
            }
        }
        //Si pisamos una mina, morimos
        if (a instanceof Mina) {
            if (((Mina) a).getVy() == 0) {
                kill();
            }
        }
        //Colisiones con los bloques
        if (a instanceof Block) {
            //Cuando estamos sobre un bloque
            if (a.getY() < y + height && a.getY() + 6 > y + height && a.getX() < x + (width / 1.5) && a.getX() > x - (width / 1.5)) {
                //Cambiamos la superficie
                surface = (a.getY() + 1 - height);
                y = surface;
                //Podemos desplazarnos lateralmente
                canLeft = true;
                canRight = true;
                //Indicamos que estamos sobre un bloque
                onTop = true;
                //Estamos cayendo (para que cuando nos salgamos del bloque caigamos a la superficie base del escenario
                falling = true;
                //Cronometro sobre el bloque
                topTimer = System.currentTimeMillis();
            } else { //No estamos sobre un bloque
                //Tenemos un bloque a nuestra derecha
                if (a.getX() > x) {
                    //Si estamos sobre la superficie (sin saltar ni caer) nos coloca al lado del bloque (evita que nos metamos dentro de este)
                    if (y - height == surface) {
                        x = a.getX() - a.getWidth();
                    }
                    //Indicamos que no puede desplazarse a la derecha porque hay un bloque
                    canRight = false;
                }
                //Tenemos un bloque a nuestra izquierda
                if (a.getX() < x) {
                    //Si estamos sobre la superficie (sin saltar ni caer) nos coloca al lado del bloque (evita que nos metamos dentro de este)
                    if (y - height == surface) {
                        x = a.getX() + a.getWidth();
                    }
                    //Indicamos que no puede desplazarse a la izquierda porque hay un bloque
                    canLeft = false;
                }
            }
            //Nos permite caer cuando la mitad de nuestro personaje pasa el borde del bloque
            if (a.getY() + a.getHeight() > y && a.getY() + a.getHeight() - 6 < y && a.getX() < x + (width / 1.5) && a.getX() > x - (width / 1.5)) {
                jumping = false;
                falling = true;
            }
        }
        return false;
    }

    @Override
    public void paint(Graphics2D g) {
        //Dibuja el sprite actual del jugador
        g.drawImage(spriteCache.getSprite("player_" + spriteState + ".gif"), x, y, stage);
        if (Stage.debug) {
            g.setPaint(Color.RED);
            g.drawRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
        }
    }

    public int getAvailableBlocks() {
        return blocksLeft;
    }

    public void loadSprites() {
        String[] spr = new String[]{"player_stand_left.gif", "player_stand_right.gif",
            "player_run_left_0.gif", "player_run_left_1.gif", "player_run_left_2.gif",
            "player_run_left_3.gif", "player_run_left_4.gif", "player_run_left_5.gif",
            "player_run_right_0.gif", "player_run_right_1.gif", "player_run_right_2.gif",
            "player_run_right_3.gif", "player_run_right_4.gif", "player_run_right_5.gif",
            "player_jump.gif", "player_fall.gif"};
        setSpriteNames(spr);
    }

    public int getSurface() {
        return surface;
    }

    public int getAvailableMines() {
        return minesLeft;
    }

    public boolean paused() {
        return gamePaused;
    }
}
