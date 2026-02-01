package entity;

import game.GamePanel;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Entity {
    
    public GamePanel gp;
    public int x, y;
    public int speed;
    
    // --- GAMBAR ANIMASI ---
    // Menyimpan 2 frame untuk setiap arah (Kaki kiri & kanan)
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction = "down"; // Arah default
    
    public int spriteCounter = 0;
    public int spriteNum = 1; // 1 atau 2 (untuk ganti-ganti kaki)
    
    // --- COLLISION (TABRAKAN) ---
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    
    // --- STATUS ---
    public int hp;
    public int maxHp;
    public String name;
    public boolean invincible = false;
    public int actionLockCounter = 0; // Untuk delay AI musuh

    // --- UTILITY: SETUP GAMBAR ---
    // Method ini akan membesarkan gambar kecil (pixel art) ke ukuran game (48x48)
    public BufferedImage setup(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(image, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }

    public abstract void draw(Graphics2D g2);
    public abstract void update();
    
    public Rectangle getBounds() {
        return new Rectangle(x + solidArea.x, y + solidArea.y, solidArea.width, solidArea.height);
    }
}