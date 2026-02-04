package entity;

import game.GamePanel;
import game.KeyHandler;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler keyH;
    
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        // Atur ukuran Hitbox (area tabrakan) agar pas di kaki karakter
        // x=8, y=16, lebar=32, tinggi=32 (Sesuaikan jika karaktermu gemuk/kurus)
        solidArea = new Rectangle(8, 16, 32, 32); 
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        setDefaultValues();
        getPlayerImage();
    }
    
    public void setDefaultValues() {
        x = 100; y = 100;
        speed = 4;
        direction = "down"; // Arah awal menghadap bawah
    }
    
    public void getPlayerImage() {
        try {
            // 1. LOAD GAMBAR UTAMA (Sprite Sheet)
            // Pastikan nama file di folder res sama persis (huruf besar/kecil berpengaruh!)
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/Player.png"));
            
            // 2. TEKNIK MEMOTONG (CROP)
            // Asumsi: Ukuran per frame di gambar adalah 16x16 pixel atau 32x32 pixel.
            // Kamu harus tahu ukuran asli sprite-mu. Misal kita pakai 48x48 pixel sebagai standar di game ini.
            int frameW = 16; // Lebar satu frame di gambar asli (misal 16px)
            int frameH = 16; // Tinggi satu frame di gambar asli
            
            // Baris 1: Atas, Baris 2: Bawah, dsb (Tergantung susunan gambarmu)
            // Contoh di bawah untuk sprite sheet standar RPG Maker (3 kolom x 4 baris)
            
            // UP (Menghadap Atas)
            up1 = setup(spriteSheet.getSubimage(0, frameH * 3, frameW, frameH)); 
            up2 = setup(spriteSheet.getSubimage(frameW, frameH * 3, frameW, frameH));
            
            // DOWN (Menghadap Bawah)
            down1 = setup(spriteSheet.getSubimage(0, 0, frameW, frameH));
            down2 = setup(spriteSheet.getSubimage(frameW, 0, frameW, frameH));
            
            // LEFT (Menghadap Kiri)
            left1 = setup(spriteSheet.getSubimage(0, frameH, frameW, frameH));
            left2 = setup(spriteSheet.getSubimage(frameW, frameH, frameW, frameH));
            
            // RIGHT (Menghadap Kanan)
            right1 = setup(spriteSheet.getSubimage(0, frameH * 2, frameW, frameH));
            right2 = setup(spriteSheet.getSubimage(frameW, frameH * 2, frameW, frameH));
            
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Gagal memuat Player.png! Cek nama file/folder.");
        }
    }
    
    // Helper untuk memperbesar gambar pixel art agar tidak pecah saat di-zoom
    public BufferedImage setup(BufferedImage image) {
        BufferedImage scaledImage = new BufferedImage(gp.TILE_SIZE, gp.TILE_SIZE, image.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(image, 0, 0, gp.TILE_SIZE, gp.TILE_SIZE, null);
        g2.dispose();
        return scaledImage;
    }
    public void update() {
        if(keyH.up || keyH.down || keyH.left || keyH.right) {
            
            // 1. Tentukan Arah
            if (keyH.up) direction = "UP";
            else if (keyH.down) direction = "DOWN";
            else if (keyH.left) direction = "LEFT";
            else if (keyH.right) direction = "RIGHT";
            
            // 2. Cek Tabrakan Tembok
            collisionOn = false;
            gp.cChecker.checkTile(this);
            
            // 3. Kalau tidak nabrak, baru jalan
            if(collisionOn == false) {
                switch(direction) {
                    case "UP": y -= speed; break;
                    case "DOWN": y += speed; break;
                    case "LEFT": x -= speed; break;
                    case "RIGHT": x += speed; break;
                }
            }
            
            // 4. Update Animasi (Ganti gambar setiap 12 frame)
            spriteCounter++;
            if(spriteCounter > 12) {
                if(spriteNum == 1) spriteNum = 2;
                else if(spriteNum == 2) spriteNum = 1;
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        
        // Pilih gambar berdasarkan arah dan spriteNum (Kaki kiri/kanan)
        switch(direction) {
            case "UP": image = (spriteNum == 1) ? up1 : up2; break;
            case "DOWN": image = (spriteNum == 1) ? down1 : down2; break;
            case "LEFT": image = (spriteNum == 1) ? left1 : left2; break;
            case "RIGHT": image = (spriteNum == 1) ? right1 : right2; break;
        }
        
        if(image != null) {
            g2.drawImage(image, x, y, gp.TILE_SIZE, gp.TILE_SIZE, null);
        } else {
            // Fallback (Kotak Biru)
            g2.setColor(Color.BLUE);
            g2.fillRect(x, y, gp.TILE_SIZE, gp.TILE_SIZE);
        }
    }
}