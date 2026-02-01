package entity;

import game.GamePanel;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

// Class ini harus ABSTRACT karena dia cuma cetakan
public abstract class Entity {
    
    public GamePanel gp; 
    public int x, y;
    public int speed;
    
    // --- GAMBAR ANIMASI ---
    // Gerakan Jalan
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    // Gerakan Serang (Attack) - Saya lengkapi untuk semua arah
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, 
                         attackLeft1, attackLeft2, attackRight1, attackRight2;
    
    public String direction = "DOWN";
    
    // --- COUNTER & STATUS ANIMASI ---
    public int spriteCounter = 0;
    public int spriteNum = 1;
    
    // Status Sedang Apa?
    public boolean collisionOn = false;
    public boolean isMoving = false;    // Sedang jalan?
    public boolean isAttacking = false; // Sedang nyerang?
    
    // --- COLLISION AREA ---
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    
    // --- STATUS KARAKTER ---
    public int hp;
    public int maxHp;
    public String name;
    
    // --- AI & LOGIKA LAINNYA ---
    public int actionLockCounter = 0; // Wajib ada untuk Enemy.java
    
    // ------------------------------------------------------------

    // Helper untuk membesarkan gambar (Dipakai oleh Player & Enemy)
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