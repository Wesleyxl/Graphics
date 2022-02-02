import javax.swing.*;
import java.awt.*;

public class Game extends Canvas implements Runnable {

    // variables
    public static JFrame frame;
    private boolean isRunning = true;

    public Game()
    {
        int WIDTH = 160;
        int HEIGHT = 120;
        int SCALE = 4;
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
    }

    public void initFrame()
    {
        frame = new JFrame("Game #1");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start()
    {
        Thread thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop()
    {
    
    }

    public static void main (String[] args)
    {
        Game game = new Game();
        game.start();
    }

    public void tick()
    {

    }

    public void render()
    {

    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        int frames = 0;
        double timer = System.currentTimeMillis();

        while (isRunning)
        {
            long now  = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1)
            {
                tick();
                render();
                frames++;
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000)
            {
                System.out.println("FPS " + frames);
                frames = 0;
                timer += 1000;
            }
        }
    }
}
