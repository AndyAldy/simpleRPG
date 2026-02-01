package simplerpg.object;

import java.awt.Rectangle;

public class Weapon {
    public String name;
    public int damage;
    public int range; // Jangkauan serangan
    public int attackCooldown; // Kecepatan serang

    public Weapon(String name, int damage, int range, int attackCooldown) {
        this.name = name;
        this.damage = damage;
        this.range = range;
        this.attackCooldown = attackCooldown;
    }
    
    // Hitbox area serangan (depan pemain)
    public Rectangle getAttackArea(int playerX, int playerY, String direction) {
        int x = playerX;
        int y = playerY;
        
        // Sesuaikan area serangan berdasarkan arah hadap
        switch(direction) {
            case "UP": y -= range; break;
            case "DOWN": y += 48; break; // 48 adalah ukuran player
            case "LEFT": x -= range; break;
            case "RIGHT": x += 48; break;
        }
        
        // Area serangan diasumsikan kotak 48x48 pixel (atau sesuai range)
        return new Rectangle(x, y, 48, 48);
    }
}