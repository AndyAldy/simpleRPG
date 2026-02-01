package entity;

import game.GamePanel;
import java.awt.*;

public class Enemy extends Entity {
    
    private GamePanel gp;
    private boolean isBoss;
    
    public Enemy(GamePanel gp, int x, int y, boolean isBoss) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.isBoss = isBoss;
        
        if (isBoss) {
            this.name = "RAJA IBLIS";
            this.hp = 500;
            this.maxHp = 500;
            this.speed = 2;
        } else {
            this.name = "Goblin";
            this.hp = 50;
            this.maxHp = 50;
            this.speed = 1;
        }
    }

    @Override
    public void update() {
        if (hp <= 0) return; // Mati
        
        // AI Sederhana: Kejar Player
        if (x < gp.player.x) x += speed;
        if (x > gp.player.x) x -= speed;
        if (y < gp.player.y) y += speed;
        if (y > gp.player.y) y -= speed;
        
        // Jika Boss Low HP, dia mengamuk (Speed naik)
        if (isBoss && hp < 100) speed = 4;
        
        // Cek tabrakan dengan player (Player kena damage)
        if (this.getBounds().intersects(gp.player.getBounds())) {
            gp.player.hp -= isBoss ? 2 : 1;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (hp <= 0) return;

        // Warna berbeda untuk Boss dan Musuh Biasa
        if (isBoss) {
            g2.setColor(hp < 100 ? Color.RED : Color.MAGENTA); // Merah kalau ngamuk
            g2.fillRect(x, y, 64, 64); // Boss lebih besar
        } else {
            g2.setColor(Color.GREEN);
            g2.fillRect(x, y, 48, 48);
        }
        
        // HP Bar di atas kepala
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 10, 48, 5);
        g2.setColor(Color.GREEN);
        double hpScale = (double)hp/maxHp * 48; // Panjang bar sesuai persentase HP
        g2.fillRect(x, y - 10, (int)hpScale, 5);
    }
    
    @Override 
    public Rectangle getBounds() {
        // Boss hitbox lebih besar
        return new Rectangle(x, y, isBoss ? 64 : 48, isBoss ? 64 : 48);
    }
}