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
    
    public Enemy(GamePanel gp, int x, int y, boolean isBoss) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.isBoss = isBoss;
        
        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        if (isBoss) {
            this.name = "RAJA ORC";
            this.hp = 200;
            this.maxHp = 200;
            this.speed = 2; 
        } else {
            this.name = "Orc Warrior";
            this.hp = 20;
            this.maxHp = 20;
            this.speed = 1; // Musuh biasa lebih lambat
        }
        
        getEnemyImage();
    }
    
    public void getEnemyImage() {
        try {
            // Load gambar Orc.png yang sudah kamu upload
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/Orc.png"));
            
            int w = 16; 
            int h = 16;
            int size = isBoss ? gp.TILE_SIZE * 2 : gp.TILE_SIZE; // Boss ukurannya 2x lipat
            
            // Potong sprite Orc (Asumsi layout sama dengan Player)
            down1 = setup(spriteSheet.getSubimage(0, 0, w, h), size, size);
            down2 = setup(spriteSheet.getSubimage(w, 0, w, h), size, size);
            
            left1 = setup(spriteSheet.getSubimage(0, h, w, h), size, size);
            left2 = setup(spriteSheet.getSubimage(w, h, w, h), size, size);
            
            right1 = setup(spriteSheet.getSubimage(0, h*2, w, h), size, size);
            right2 = setup(spriteSheet.getSubimage(w, h*2, w, h), size, size);
            
            up1 = setup(spriteSheet.getSubimage(0, h*3, w, h), size, size);
            up2 = setup(spriteSheet.getSubimage(w, h*3, w, h), size, size);
            
        } catch(Exception e) {
            System.out.println("Gagal load Orc.png: " + e.getMessage());
        }
    }
    
    // AI SEDERHANA: Gerak Acak
    public void setAction() {
        actionLockCounter++;
        
        if(actionLockCounter == 120) { // Setiap 2 detik (120 frame) ganti arah
            Random random = new Random();
            int i = random.nextInt(100) + 1; // Angka 1-100
            
            if(i <= 25) direction = "up";
            else if(i <= 50) direction = "down";
            else if(i <= 75) direction = "left";
            else direction = "right";
            
            actionLockCounter = 0;
        }
    }

    @Override
    public void update() {
        setAction(); // Tentukan mau jalan ke mana
        
        collisionOn = false;
        gp.cChecker.checkTile(this); // Cek tembok
        
        if(!collisionOn) {
            switch(direction) {
                case "up": y -= speed; break;
                case "down": y += speed; break;
                case "left": x -= speed; break;
                case "right": x += speed; break;
            }
        }
        
        // Animasi Kaki
        spriteCounter++;
        if(spriteCounter > 12) {
            if(spriteNum == 1) spriteNum = 2;
            else if(spriteNum == 2) spriteNum = 1;
            spriteCounter = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch(direction) {
            case "up": image = (spriteNum == 1) ? up1 : up2; break;
            case "down": image = (spriteNum == 1) ? down1 : down2; break;
            case "left": image = (spriteNum == 1) ? left1 : left2; break;
            case "right": image = (spriteNum == 1) ? right1 : right2; break;
        }
        
        // Gambar HP Bar jika terluka
        if(hp < maxHp) {
            g2.setColor(Color.RED);
            g2.fillRect(x, y - 10, gp.TILE_SIZE, 5);
            g2.setColor(Color.GREEN);
            double scale = (double)gp.TILE_SIZE * ((double)hp/maxHp);
            g2.fillRect(x, y - 10, (int)scale, 5);
        }

        if(image != null) 
            g2.drawImage(image, x, y, null);
        else {
            // Fallback kalau gambar error: Kotak Merah
            g2.setColor(Color.RED);
            g2.fillRect(x, y, gp.TILE_SIZE, gp.TILE_SIZE);
        }
    }
}