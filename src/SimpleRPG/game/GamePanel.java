package SimpleRPG.game;

import SimpleRPG.entity.Enemy;
import SimpleRPG.entity.Entity;
import SimpleRPG.entity.Player;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    
    // Setting Layar
    final int TILE_SIZE = 48;
    public final int SCREEN_WIDTH = TILE_SIZE * 16; // 768 px
    public final int SCREEN_HEIGHT = TILE_SIZE * 12; // 576 px
    
    // System
    Thread gameThread;
    KeyHandler keyH = new KeyHandler();
    
    // Entities
    public Player player = new Player(this, keyH);
    public ArrayList<Entity> enemies = new ArrayList<>();
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        
        setupGame();
    }
    
    public void setupGame() {
        // Spawn Musuh
        enemies.add(new Enemy(this, 300, 300, false)); // Goblin 1
        enemies.add(new Enemy(this, 500, 100, false)); // Goblin 2
        
        // Spawn BOSS
        enemies.add(new Enemy(this, 600, 400, true)); // RAJA IBLIS
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }
    
    public void update() {
        player.update();
        
        // Update semua musuh
        // Menggunakan loop terbalik supaya aman saat remove(i) jika musuh mati
        for (int i = 0; i < enemies.size(); i++) {
            Entity e = enemies.get(i);
            e.update();
            if (e.hp <= 0) {
                System.out.println(e.name + " Mati!");
                enemies.remove(i);
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // UI Info
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 15));
        g2.drawString("Player HP: " + player.hp, 10, 20);
        g2.drawString("Musuh Tersisa: " + enemies.size(), 10, 40);
        
        // Gambar Player
        player.draw(g2);
        
        // Gambar Semua Musuh
        for (Entity e : enemies) {
            e.draw(g2);
        }
        
        // Jika player mati
        if (player.hp <= 0) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.drawString("GAME OVER", SCREEN_WIDTH/2 - 150, SCREEN_HEIGHT/2);
            gameThread = null; // Stop game
        }
        
        g2.dispose();
    }
}