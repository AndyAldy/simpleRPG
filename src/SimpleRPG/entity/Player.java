package SimpleRPG.entity;

import SimpleRPG.game.GamePanel;
import SimpleRPG.game.KeyHandler;
import simplerpg.object.Weapon;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler keyH;
    public Weapon currentWeapon;
    public int attackTimer = 0; // Untuk cooldown
    
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        // Setup Status Awal
        this.name = "Hero";
        this.x = 100;
        this.y = 100;
        this.speed = 4;
        this.maxHp = 100;
        this.hp = maxHp;
        
        // Setup Senjata Awal
        this.currentWeapon = new Weapon("Pedang Besi", 20, 48, 30);
        
        loadAssets();
    }
    
    private void loadAssets() {
        try {
            // Coba load gambar (pastikan file ada di folder res)
            image = ImageIO.read(getClass().getResourceAsStream("/res/player.png"));
        } catch (Exception e) {
            System.out.println("Gagal load gambar player, pakai kotak warna.");
        }
    }

    @Override
    public void update() {
        // Gerakan
        if (keyH.up) { y -= speed; direction = "UP"; }
        if (keyH.down) { y += speed; direction = "DOWN"; }
        if (keyH.left) { x -= speed; direction = "LEFT"; }
        if (keyH.right) { x += speed; direction = "RIGHT"; }
        
        // Logika Serangan
        if (attackTimer > 0) attackTimer--;
        
        if (keyH.attack && attackTimer == 0) {
            System.out.println("Menyerang dengan " + currentWeapon.name + "!");
            attackTimer = currentWeapon.attackCooldown;
            
            // Cek kena musuh
            Rectangle attackArea = currentWeapon.getAttackArea(x, y, direction);
            for(Entity e : gp.enemies) {
                if(e.getBounds().intersects(attackArea)) {
                    e.hp -= currentWeapon.damage;
                    System.out.println("Hit " + e.name + "! Sisa HP: " + e.hp);
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        // Jika ada gambar, pakai gambar. Jika tidak, pakai kotak biru.
        if (image != null) {
            g2.drawImage(image, x, y, 48, 48, null);
        } else {
            g2.setColor(Color.BLUE);
            g2.fillRect(x, y, 48, 48);
        }
        
        // Gambar efek serangan sekilas (kotak merah transparan)
        if (keyH.attack) {
            g2.setColor(new Color(255, 0, 0, 100));
            Rectangle area = currentWeapon.getAttackArea(x, y, direction);
            g2.fillRect(area.x, area.y, area.width, area.height);
        }
    }
}