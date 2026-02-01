package entity;

import game.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Random;

public class Enemy extends Entity {
    
    private boolean isBoss;
    private int size;
    private int attackCooldown = 0; // Agar Orc tidak memberi damage setiap milidetik
    
    public Enemy(GamePanel gp, int x, int y, boolean isBoss) {
        this.gp = gp; 
        this.x = x;
        this.y = y;
        this.isBoss = isBoss;
        
        // Ukuran Visual
        this.size = isBoss ? gp.TILE_SIZE * 3 : gp.TILE_SIZE * 2;
        
        // Hitbox (Disesuaikan dengan ukuran raksasa)
        solidArea = new Rectangle(20, 40, size - 40, size - 60);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        // Status
        if (isBoss) {
            this.name = "RAJA ORC";
            this.hp = 500; this.maxHp = 500; this.speed = 2;
        } else {
            this.name = "Orc Brute";
            this.hp = 50; this.maxHp = 50; this.speed = 1; 
        }
        
        getEnemyImage();
    }
    
    public void getEnemyImage() {
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/Orc.png"));
            
            // KUNCI PERBAIKAN ANIMASI & KEDIPAN:
            // Pastikan ukuran ini 32 jika aset aslinya 32x32.
            int w = 32; 
            int h = 32;
            
            // Kita ambil Kolom 2 (Walk 1) dan Kolom 3 (Walk 2) untuk animasi.
            // Kolom 1 (Idle) kita abaikan dulu biar jalannya mulus.
            
            // Baris 1: DOWN (Y=0)
            down1 = setup(spriteSheet.getSubimage(w, 0, w, h), size, size);
            down2 = setup(spriteSheet.getSubimage(w*2, 0, w, h), size, size);
            
            // Baris 2: LEFT (Y=32)
            left1 = setup(spriteSheet.getSubimage(w, h, w, h), size, size);
            left2 = setup(spriteSheet.getSubimage(w*2, h, w, h), size, size);
            
            // Baris 3: RIGHT (Y=64)
            right1 = setup(spriteSheet.getSubimage(w, h*2, w, h), size, size);
            right2 = setup(spriteSheet.getSubimage(w*2, h*2, w, h), size, size);
            
            // Baris 4: UP (Y=96)
            up1 = setup(spriteSheet.getSubimage(w, h*3, w, h), size, size);
            up2 = setup(spriteSheet.getSubimage(w*2, h*3, w, h), size, size);
            
        } catch(Exception e) {
            System.out.println("Gagal load Orc.png! Cek ukuran file.");
        }
    }
    
    public void setAction() {
        actionLockCounter++;
        if(actionLockCounter == 120) { 
            Random random = new Random();
            int i = random.nextInt(100) + 1; 
            if(i <= 25) direction = "UP";
            else if(i <= 50) direction = "DOWN";
            else if(i <= 75) direction = "LEFT";
            else direction = "RIGHT";
            actionLockCounter = 0;
        }
    }

    @Override
    public void update() {
        if (hp <= 0) return; 
        
        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);
        
        // --- LOGIKA SERANGAN ORC ---
        if (attackCooldown > 0) attackCooldown--;
        
        // Jika hitbox Orc bersentuhan dengan Player
        if(this.getBounds().intersects(gp.player.getBounds())) {
            if(attackCooldown == 0) {
                int damage = isBoss ? 20 : 10;
                gp.player.hp -= damage;
                attackCooldown = 60; // Jeda 1 detik sebelum bisa nyerang lagi
                System.out.println("Player terkena damage! Sisa HP: " + gp.player.hp);
            }
        }
        // ---------------------------

        if(!collisionOn) {
            switch(direction) {
                case "UP": y -= speed; break;
                case "DOWN": y += speed; break;
                case "LEFT": x -= speed; break;
                case "RIGHT": x += speed; break;
            }
        }
        
        // Update Counter Animasi
        spriteCounter++;
        if(spriteCounter > 12) {
            if(spriteNum == 1) spriteNum = 2;
            else if(spriteNum == 2) spriteNum = 1;
            spriteCounter = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (hp <= 0) return;

        BufferedImage image = null;
        switch(direction) {
            case "UP": image = (spriteNum == 1) ? up1 : up2; break;
            case "DOWN": image = (spriteNum == 1) ? down1 : down2; break;
            case "LEFT": image = (spriteNum == 1) ? left1 : left2; break;
            case "RIGHT": image = (spriteNum == 1) ? right1 : right2; break;
        }
        
        // HP BAR BOSS/ORC
        if (hp < maxHp) {
            double scale = (double)size / maxHp;
            double hpBarValue = scale * hp;
            g2.setColor(new Color(35, 35, 35));
            g2.fillRect(x + 10, y - 10, size - 20, 10);
            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(x + 10, y - 10, (int)hpBarValue, 10);
        }

        if(image != null) {
            g2.drawImage(image, x, y, null);
        } else {
            // Jika masih berkedip merah, berarti slicing di getEnemyImage masih salah
            g2.setColor(Color.RED);
            g2.fillRect(x, y, size, size);
        }
    }
}