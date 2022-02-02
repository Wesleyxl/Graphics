import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {

    // variables
    public static JFrame frame;

    private boolean isRunning = true;
    private final int SCALE = 4;
    private final int WIDTH = 160;
    private final int HEIGHT = 120;
    private Thread thread;
    private final BufferedImage image;
    private final BufferedImage[] player;
    private int frames = 0;
    private int curAnimation = 0;

    public Game()
    {
        SpriteSheet sheet = new SpriteSheet("/spriteSheet.png");
        player = new BufferedImage[2];
        player[0] = sheet.getImage(0, 0,16, 16);
        player[1] = sheet.getImage(16, 0, 16, 16);
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
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
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }


    public synchronized void stop()
    {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main (String[] args)
    {
        Game game = new Game();
        game.start();
    }

    public void tick()
    {
        frames++;
        int maxFrames = 20;
        if (frames > maxFrames)
        {
            frames = 0;
            curAnimation++;
            int maxAnimation = 2;
            if (curAnimation >= maxAnimation)
            {
                curAnimation = 0;
            }
        }
    }

    public void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = image.getGraphics();
        g.setColor(new Color(19,19,19));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        /* Game Render */
        g.drawImage(player[curAnimation], 20, 20, null);

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        bs.show();
    }

    @Override
    public void run()
    {
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
        stop();
    }
}
