package entity;

import game.GamePanel;
import game.KeyHandler;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player extends Entity {
    
    KeyHandler keyH;
    
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        // HITBOX: Sesuaikan area sensitif tabrakan (biar kepala tidak nyangkut tembok)
        solidArea = new Rectangle(8, 16, 32, 32); 
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        setDefaultValues();
        getPlayerImage();
    }
    
    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
        maxHp = 100;
        hp = maxHp;
    }
    
public void getPlayerImage() {
        try {
            // 1. Load gambar barumu (Pastikan nama file sama persis)
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/Player-1.png"));
            
            // 2. Tentukan ukuran satu kotak karakter (Ukur pixelnya!)
            // Saya tebak ini 16x16 atau 32x32. Coba 16 dulu, kalau kekecilan ganti 32.
            int frameWidth = 16; 
            int frameHeight = 16; 
            
            // 3. Ambil gambar dari baris paling atas (y = 0)
            
            // -- Animasi 1 (Kaki Kiri) --
            // Mengambil frame ke-1 (x = 0)
            BufferedImage img1 = setup(spriteSheet.getSubimage(0, 0, frameWidth, frameHeight), gp.TILE_SIZE, gp.TILE_SIZE);
            
            // -- Animasi 2 (Kaki Kanan) --
            // Mengambil frame ke-2 (x = frameWidth) -> Geser ke kanan satu kotak
            BufferedImage img2 = setup(spriteSheet.getSubimage(frameWidth, 0, frameWidth, frameHeight), gp.TILE_SIZE, gp.TILE_SIZE);
            
            // 4. Aplikasikan ke semua arah (Karena gambarnya cuma punya hadap kanan)
            up1 = img1; 
            up2 = img2;
            
            down1 = img1; 
            down2 = img2;
            
            left1 = img1; 
            left2 = img2;
            
            right1 = img1; 
            right2 = img2;
            
        } catch(Exception e) {
            System.out.println("Gagal load gambar! Cek nama file /res/Player-1.png");
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if(keyH.up || keyH.down || keyH.left || keyH.right) {
            
            if (keyH.up) direction = "up";
            else if (keyH.down) direction = "down";
            else if (keyH.left) direction = "left";
            else if (keyH.right) direction = "right";
            
            // Cek Tabrakan
            collisionOn = false;
            gp.cChecker.checkTile(this);
            
            // Jika tidak nabrak, gerak
            if(!collisionOn) {
                switch(direction) {
                    case "up": y -= speed; break;
                    case "down": y += speed; break;
                    case "left": x -= speed; break;
                    case "right": x += speed; break;
                }
            }
            
            // Animasi Jalan
            spriteCounter++;
            if(spriteCounter > 12) { // Ganti gambar setiap 12 frame
                if(spriteNum == 1) spriteNum = 2;
                else if(spriteNum == 2) spriteNum = 1;
                spriteCounter = 0;
            }
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
        
        if(image != null) 
            g2.drawImage(image, x, y, null);
        else {
            g2.setColor(Color.BLUE);
            g2.fillRect(x, y, gp.TILE_SIZE, gp.TILE_SIZE);
        }
    }
}