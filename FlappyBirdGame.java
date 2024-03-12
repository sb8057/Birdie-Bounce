import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlappyBirdGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BIRD_SIZE = 30;
    private static final int PIPE_WIDTH = 50;
    private static final int PIPE_SPACING = 200;
    private static final int PIPE_VELOCITY = 5;
    private static final int GRAVITY = 1;

    private int birdY;
    private int birdVelocity;
    private List<Rectangle> pipes;
    private int score;

    private boolean isGameRunning;
    private Timer timer;

    public FlappyBirdGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);

        birdY = HEIGHT / 2;
        birdVelocity = 0;
        pipes = new ArrayList<>();
        score = 0;

        isGameRunning = true;
        timer = new Timer(20, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
        spawnPipe();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw bird
        g.setColor(Color.BLUE);
        g.fillRect(WIDTH / 4, birdY, BIRD_SIZE, BIRD_SIZE);

        // Draw pipes
        g.setColor(Color.GREEN);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        // Draw score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
    }

    private void spawnPipe() {
        int pipeHeight = new Random().nextInt(HEIGHT - 200) + 100;
        pipes.add(new Rectangle(WIDTH, 0, PIPE_WIDTH, pipeHeight));
        pipes.add(new Rectangle(WIDTH, pipeHeight + PIPE_SPACING, PIPE_WIDTH, HEIGHT - pipeHeight - PIPE_SPACING));
    }

    private void updateGame() {
        birdVelocity += GRAVITY;
        birdY += birdVelocity;

        List<Rectangle> pipesToRemove = new ArrayList<>();
        for (Rectangle pipe : pipes) {
            pipe.x -= PIPE_VELOCITY;
            if (pipe.x + PIPE_WIDTH < 0) {
                pipesToRemove.add(pipe);
            }
            if (pipe.intersects(new Rectangle(WIDTH / 4, birdY, BIRD_SIZE, BIRD_SIZE))) {
                isGameRunning = false;
            }
        }
        pipes.removeAll(pipesToRemove);

        if (birdY < 0 || birdY > HEIGHT) {
            isGameRunning = false;
        }

        if (isGameRunning && pipes.isEmpty()) {
            spawnPipe();
            score++;
        }

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameRunning) {
            updateGame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            birdVelocity = -15;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird Game");
        FlappyBirdGame game = new FlappyBirdGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}