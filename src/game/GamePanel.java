package game;

import entity.Entity;
import entity.Player;
import entity.Enemy;
import tile.TileManager; // Import baru
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    
    // Setting Layar
    public final int TILE_SIZE = 48;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int SCREEN_WIDTH = TILE_SIZE * maxScreenCol; 
    public final int SCREEN_HEIGHT = TILE_SIZE * maxScreenRow;
    
    // System
    Thread gameThread;
    KeyHandler keyH = new KeyHandler();
    
    // --- KOMPONEN BARU ---
    public TileManager tileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    // ---------------------
    
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
        // Tambahkan Musuh, Boss, dll di sini nanti
        enemies.add(new Enemy(this, 300, 300, false)); 
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
        for (Entity e : enemies) e.update();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // 1. GAMBAR TILE/BACKGROUND DULUAN (Layer paling bawah)
        tileM.draw(g2);
        
        // 2. GAMBAR PLAYER & MUSUH (Layer tengah)
        player.draw(g2);
        for (Entity e : enemies) e.draw(g2);
        
        // 3. UI (Layer paling atas)
        g2.setColor(Color.WHITE);
        g2.drawString("HP: " + player.hp, 10, 20);
        
        g2.dispose();
    }
}