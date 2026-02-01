package SimpleRPG.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Entity {
    public int x, y;
    public int speed;
    public int hp;
    public int maxHp;
    public String name;
    public String direction = "DOWN"; // Arah hadap
    
    // Untuk Assets Gambar
    public BufferedImage image; 
    
    public abstract void update();
    public abstract void draw(Graphics2D g2);
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, 48, 48); // Ukuran default entity
    }
}