package entity;

import game.GamePanel;
import game.KeyHandler;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player extends Entity {
    
    KeyHandler keyH;
    int shootCooldown = 0; // Supaya menembak ada jedanya
    
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        // Hitbox
        solidArea = new Rectangle(8, 16, 32, 32); 
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        setDefaultValues();
        getPlayerImage();
    }
    
    public void setDefaultValues() {
        x = 100; y = 100;
        speed = 4;
        direction = "DOWN";
        maxHp = 100;
        hp = maxHp;
    }
    
    public void getPlayerImage() {
        try {
            // LOAD GAMBAR STRIP
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/Player.png"));
            
            // ASUMSI: Ukuran per frame di strip adalah 32x32 pixel (karena aset side-scrolling biasanya agak besar)
            // Jika nanti gambarnya terlihat aneh, coba ganti frameW dan frameH jadi 16.
            int frameW = 8; 
            int frameH = 13;
            
            // --- POTONG GAMBAR JALAN (Frame 1 & 2) ---
            BufferedImage imgWalk1 = setup(spriteSheet.getSubimage(0, 0, frameW, frameH), gp.TILE_SIZE, gp.TILE_SIZE);
            BufferedImage imgWalk2 = setup(spriteSheet.getSubimage(frameW, 0, frameW, frameH), gp.TILE_SIZE, gp.TILE_SIZE);
            
            // --- POTONG GAMBAR MENEMBAK (Asumsi ada di Frame 3 & 4) ---
            // Kita ambil frame agak belakang. x = frameW * 2 dan frameW * 3
            BufferedImage imgShoot1 = setup(spriteSheet.getSubimage(frameW * 2, 0, frameW, frameH), gp.TILE_SIZE, gp.TILE_SIZE);
            // Jika hanya ada 1 pose menembak, gunakan imgShoot1 untuk keduanya.
            BufferedImage imgShoot2 = imgShoot1; 
            
            // Aplikasikan ke semua arah (Karena asetnya side-scrolling cuma hadap kanan)
            up1 = imgWalk1; up2 = imgWalk2;
            down1 = imgWalk1; down2 = imgWalk2;
            left1 = imgWalk1; left2 = imgWalk2;
            right1 = imgWalk1; right2 = imgWalk2;
            
            // Aplikasikan gambar serangan
            attackUp1 = imgShoot1; attackUp2 = imgShoot2;
            attackDown1 = imgShoot1; attackDown2 = imgShoot2;
            attackLeft1 = imgShoot1; attackLeft2 = imgShoot2;
            attackRight1 = imgShoot1; attackRight2 = imgShoot2;
            
        } catch(Exception e) {
            System.out.println("Gagal load Player-1.png! Cek ukuran frameW/frameH.");
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        // Cooldown menembak berkurang setiap frame
        if(shootCooldown > 0) shootCooldown--;

        // LOGIKA MENEMBAK
        if (keyH.attack && shootCooldown == 0) {
            isAttacking = true;
            shootCooldown = 30; // Jeda setengah detik (30 frame) antar tembakan
            System.out.println("Dor! Menembak!");
            // Nanti di sini tambahkan logika peluru
        } 
        
        // Jika cooldown sudah selesai, stop pose menembak
        if (shootCooldown < 10) {
            isAttacking = false;
        }

        // LOGIKA PERGERAKAN
        if(keyH.up || keyH.down || keyH.left || keyH.right) {
            isMoving = true;
            if (keyH.up) direction = "UP";
            if (keyH.down) direction = "DOWN";
            if (keyH.left) direction = "LEFT";
            if (keyH.right) direction = "RIGHT";
            
            collisionOn = false;
            gp.cChecker.checkTile(this);
            
            // Hanya jalan jika tidak nabrak DAN tidak sedang menembak (biar nembaknya diam di tempat)
            if(collisionOn == false && isAttacking == false) {
                switch(direction) {
                    case "UP": y -= speed; break;
                    case "DOWN": y += speed; break;
                    case "LEFT": x -= speed; break;
                    case "RIGHT": x += speed; break;
                }
            }
            
            // Animasi Jalan Counter
            spriteCounter++;
            if(spriteCounter > 10) {
                if(spriteNum == 1) spriteNum = 2;
                else if(spriteNum == 2) spriteNum = 1;
                spriteCounter = 0;
            }
        } else {
            isMoving = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        
        // LOGIKA PEMILIHAN GAMBAR
        if(isAttacking) {
            // --- SEDANG MENEMBAK ---
            // (Karena aset cuma hadap kanan, kita pakai attackRight untuk semua)
            image = (spriteNum == 1) ? attackRight1 : attackRight2;
        } else {
            // --- SEDANG JALAN/DIAM ---
            switch(direction) {
                case "UP": image = isMoving ? (spriteNum == 1 ? up1 : up2) : up1; break;
                case "DOWN": image = isMoving ? (spriteNum == 1 ? down1 : down2) : down1; break;
                case "LEFT": image = isMoving ? (spriteNum == 1 ? left1 : left2) : left1; break;
                case "RIGHT": image = isMoving ? (spriteNum == 1 ? right1 : right2) : right1; break;
            }
        }
        
        if(image != null) {
            g2.drawImage(image, x, y, null);
        } else {
            // Fallback
            g2.setColor(java.awt.Color.BLUE);
            g2.fillRect(x, y, gp.TILE_SIZE, gp.TILE_SIZE);
        }
    }
}