package zombies;

import resources.SoundCache;
import resources.SpriteCache;
import actors.Player;
import actors.Actor;
import actors.Item;
import actors.ItemBlock;
import actors.ItemMina;
import actors.Moon;
import actors.Zombie;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Zombies extends Canvas implements Stage, KeyListener {

    private final BufferStrategy strategy;
    private final SpriteCache spriteCache;
    private final SoundCache soundCache;
    private ArrayList actors;
    private Player player;
    private Moon moon;
    private int level;
    private BufferedImage background, backgroundTile;
    private boolean gameEnded = false, stopPause = true;

    public Zombies() {
        //Creamos la cache de sprites y sonidos
        spriteCache = new SpriteCache();
        soundCache = new SoundCache();
        //Inicializamos el nivel del juego
        level = 1;
        //Creamos el JFrame contenedor del canvas del juego
        JFrame ventana = new JFrame("Zombie Survival");
        JPanel panel = (JPanel) ventana.getContentPane();
        setBounds(0, 0, Stage.WIDTH, Stage.HEIGHT);
        panel.setPreferredSize(new Dimension(Stage.WIDTH, Stage.HEIGHT));
        panel.setLayout(null);
        panel.add(this);
        ventana.setBounds(0, 0, Stage.WIDTH, Stage.HEIGHT);
        ventana.setVisible(true);
        //Listener para cerrar el juego al pulsar el boton de cerrar de la ventana
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        ventana.setResizable(false);
        //Coloca la ventana en el centro de la pantalla
        ventana.setLocationRelativeTo(null);
        //Creamos el bufferstrategy para que almacene 2 escenarios (1 para mostrar y otro para generar)
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        //Ponemos la ventana al frente de otras ventanas
        requestFocus();
        //Añadimos los listeners del teclado a la ventana
        addKeyListener(this);
        //Deshabilitamos el dibujado automatico del canvas
        setIgnoreRepaint(true);
        //Ocultamos el cursor al estar dentro del canvas
        BufferedImage cursor = spriteCache.createCompatible(10, 10, Transparency.BITMASK);
        Toolkit t = Toolkit.getDefaultToolkit();
        Cursor c = t.createCustomCursor(cursor, new Point(5, 5), "null");
        setCursor(c);
    }

    public void gameOver() {
        gameEnded = true;
    }

    public void initWorld() {
        //Inicializamos el arraylist de los actores del juego
        actors = new ArrayList();
        //Creamos el jugador y lo colocamos en el centro del escenario
        player = new Player(this);
        player.setX(Stage.WIDTH / 2);
        player.setY(BASE_GROUND - player.getHeight());

        //Creamos la luna y ajustamos la duración inicial de su recorrido
        moon = new Moon(this);
        moon.setDuracion(10 * level);
        actors.add(moon);
        
        //Colocamos la imagen de fondo
        backgroundTile = spriteCache.getSprite("fondo.jpg");
        background = spriteCache.createCompatible(Stage.WIDTH, Stage.HEIGHT, Transparency.OPAQUE);
        Graphics2D g = (Graphics2D) background.getGraphics();
        g.setPaint(new TexturePaint(backgroundTile, new Rectangle(0, 0,
                backgroundTile.getWidth(), backgroundTile.getHeight())));
        g.fillRect(0, 0, background.getWidth(), background.getHeight());
        
        //Generamos los zombies iniciales
        changeStage();
    }

    public void changeStage() {
        //Cambia la duración del recorrido de la luna
        moon.setDuracion(10 * level);
        //Genera zombies a la izquierda y derecha del escenario
        //La cantidad de zombies depende del nivel actual
        generateZombiesLeft(level);
        generateZombiesRight(level);

    }

    public void generateZombiesLeft(int amount) {
        for (int i = 0; i < amount; i++) {
            Zombie z = new Zombie(this);
            z.setX(-10 * level * i);
            z.setY(Stage.BASE_GROUND - z.getHeight());
            z.setVelocidad(100 + new Random().nextInt(level));
            actors.add(z);
        }
    }

    public void generateZombiesRight(int amount) {
        for (int i = 0; i < amount; i++) {
            Zombie z = new Zombie(this);
            z.setX(Stage.WIDTH + (10 * level * i));
            z.setY(Stage.BASE_GROUND - z.getHeight());
            z.setVelocidad(100 + level + new Random().nextInt(level));
            actors.add(z);
        }
    }

    @Override
    public void addActor(Actor a) {
        actors.add(a);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public void updateWorld() {
        int i = 0;
        //Recorremos el arraylist de actores
        while (i < actors.size()) {
            Actor a = (Actor) actors.get(i);
            if (a.isMarkedForRemove()) { //Si el actor está marcado para ser eliminado, lo eliminamos del arraylist de actores
                actors.remove(i);
            } else { //En caso contrario, ejecutamos el act del actor
                a.act();
                i++;
            }
        }
        //Ejecutamos el act del jugador
        player.act();
        //Comprobamos si la luna ha finalizado su recorrido
        if (moon.hasFinished()) {
            level++;
            changeStage();
        }
        //Generamos objetos para que caigan de forma aleatoria sobre el mapa
        if (new Random().nextInt(250) > 248) {
            generateRandomItem();
        }
    }

    public void generateRandomItem() {
        Item i;
        //Indicamos si el objeto que va a ser generado es una mina o un bloque
        if (new Random().nextBoolean()) {
            i = new ItemBlock(this);
        } else {
            i = new ItemMina(this);
        }
        //Lo colocamos en la parte superior del mapa, en una X aleatoria
        i.setX(new Random().nextInt(Stage.WIDTH));
        i.setY(0);
        //Añadimos el objeto al array de actores
        addActor(i);
    }

    public void checkCollisions() {
        //Obtenemos las dimensiones del jugador
        Rectangle playerBounds = player.getBounds();
        //Recorremos el arraylist de actores
        for (int i = 0; i < actors.size(); i++) {
            Actor a1 = (Actor) actors.get(i);
            Rectangle r1 = a1.getBounds();
            //Comprobamos si el actor colisiona con el jugador
            if (r1.intersects(playerBounds)) {
                //Ejecutamos los métodos de colisión del jugador y del actor con el que colisiona
                player.colision(a1);
                a1.colision(player);
            }
            //Comprobamos la colisión del actor actual con todos los actores del arraylist que aún no han sido comprobados
            for (int j = i + 1; j < actors.size(); j++) {
                Actor a2 = (Actor) actors.get(j);
                Rectangle r2 = a2.getBounds();
                //Comprobamos si el actor colisiona con el otro actor
                if (r1.intersects(r2)) {
                    //Ejecutamos los metodos de colisión de ambos actores
                    a1.colision(a2);
                    a2.colision(a1);
                }
            }
        }
    }
    
    //Interfaz de usuario
    public void paintStatus(Graphics2D g) {
        //Dibujamos los bloques restantes y la flecha indicando si es el objeto
        //seleccionado por el jugador o no
        g.setPaint(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        if (player.getSelectedItem() == 0) {
            g.drawString("=> Blocks: " + player.getAvailableBlocks(), Stage.WIDTH - 87, 18);
        } else {
            g.drawString("Blocks: " + player.getAvailableBlocks(), Stage.WIDTH - 70, 18);
        }
        //Dibujamos las minas restantes y la flecha indicando si es el objeto
        //seleccionado por el jugador o no
        if (player.getSelectedItem() == 1) {
            g.drawString("=> Mines: " + player.getAvailableMines(), Stage.WIDTH - 82, 38);
        } else {
            g.drawString("Mines: " + player.getAvailableMines(), Stage.WIDTH - 65, 38);
        }
        //Dibujamos el nivel actual
        g.drawString("Stage level: " + level, Stage.WIDTH - 93, 58);

    }

    public void paintWorld() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        //Dibujamos el fondo en primer lugar
        g.drawImage(background, 0, 0, Stage.WIDTH, Stage.HEIGHT, this);
        //Recorremos el arraylist de actores
        for (int i = 0; i < actors.size(); i++) {
            Actor m = (Actor) actors.get(i);
            //Ejecutamos el método paint de cada actor, el cual se encarga de dibujarlo en el escenario
            m.paint(g);
        }
        //Ejecutamos el método de dibujo del jugador
        player.paint(g);
        
        //Dibujamos la interfaz de usuario
        paintStatus(g);
        
        //Indicamos al bufferedstrategy que hemos acabado de dibujar y que ya puede mostrar el resultado por pantalla
        strategy.show();
    }

    public void paintGameOver() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        //Dibujamos un texto en el medio de la pantalla de color rojo indicando que ha terminado la partida
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("GAME OVER", Stage.WIDTH / 2 - 70, Stage.HEIGHT / 2 - 40);
        //Lo mostramos
        strategy.show();
    }
    
    public void showPause() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        //Dibujamos un texto en el medio de la pantalla de color blanco indicando que el juego esta en pausa
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.drawString("PAUSED", Stage.WIDTH / 2 - 70, Stage.HEIGHT / 2 - 40);
        //Lo mostramos
        strategy.show();
    }

    @Override
    public SpriteCache getSpriteCache() {
        return spriteCache;
    }

    @Override
    public SoundCache getSoundCache() {
        return soundCache;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Vinculamos la pulsación de tecla con el método de pulsación de la clase player
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Vinculamos la liberación de tecla con el método de liberación de tecla de la clase player
        player.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void game() {
        //Inicializamos el juego
        initWorld();
        //Bucle principal, a ejecutar mientras el juego este visible y no haya terminado
        while (isVisible() && !gameEnded) {
            //Obtenemos el timestamp de inicio de la iteración
            long startTime = System.currentTimeMillis();
            if (!player.paused()) {
                //Reinicia la luna si acaba de salir de la pausa
                if (stopPause) {
                    moon.setInicio(System.currentTimeMillis());
                    stopPause = false;
                }
                //Actualizamos el mundo (posiciones)
                updateWorld();
                //Comprobamos las colisiones entre los actores del mundo
                checkCollisions();
                //Dibujamos el estado actual del mundo en pantalla
                paintWorld();                
            } else {
                stopPause = true;
                //Mostramos el texto de pausa
                showPause();
            }
            //Pausamos el hilo para conseguir un framerate de 60fps
            do {
                Thread.yield();
            } while (System.currentTimeMillis() - startTime < 17);
        }
        //Al salir del bucle, dibujamos el gameover
        paintGameOver();
        //Reproducimos el sonido de fin de partida
        soundCache.getAudioClip("gameover.wav");
        soundCache.playSound("gameover.wav");
    }

    @Override
    public void setGameOver() {
        gameEnded = true;
    }
}
