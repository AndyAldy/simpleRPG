package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public abstract class Entity {
    public int x, y;
    public int speed;
    
    // Gambar Animasi (Atas, Bawah, Kiri, Kanan - masing2 punya 2 pose)
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction = "DOWN";
    
    public int spriteCounter = 0;
    public int spriteNum = 1; // Pose 1 atau Pose 2
    
    public Rectangle solidArea; // Area Hitbox
    public boolean collisionOn = false;
    
    public int hp;
    public int maxHp;
    public String name;
    
    public abstract void draw(Graphics2D g2);
    public abstract void update();
    
    public Rectangle getBounds() {
        return new Rectangle(x + solidArea.x, y + solidArea.y, solidArea.width, solidArea.height);
    }
}