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
        
        // Atur area hitbox agar pas di kaki (biar kepala tidak nyangkut tembok)
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
            // Load Gambar
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/Player.png"));
            
            // Ukuran sprite asli (sesuaikan dengan gambar kamu, misal 16x16)
            int w = 16; 
            int h = 16;
            
            // Memotong gambar
            down1 = setup(spriteSheet.getSubimage(0, 0, w, h), gp.TILE_SIZE, gp.TILE_SIZE);
            down2 = setup(spriteSheet.getSubimage(w, 0, w, h), gp.TILE_SIZE, gp.TILE_SIZE);
            
            left1 = setup(spriteSheet.getSubimage(0, h, w, h), gp.TILE_SIZE, gp.TILE_SIZE);
            left2 = setup(spriteSheet.getSubimage(w, h, w, h), gp.TILE_SIZE, gp.TILE_SIZE);
            
            right1 = setup(spriteSheet.getSubimage(0, h*2, w, h), gp.TILE_SIZE, gp.TILE_SIZE);
            right2 = setup(spriteSheet.getSubimage(w, h*2, w, h), gp.TILE_SIZE, gp.TILE_SIZE);
            
            up1 = setup(spriteSheet.getSubimage(0, h*3, w, h), gp.TILE_SIZE, gp.TILE_SIZE);
            up2 = setup(spriteSheet.getSubimage(w, h*3, w, h), gp.TILE_SIZE, gp.TILE_SIZE);
            
        } catch(Exception e) {
            System.out.println("Error Player Image: " + e.getMessage());
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
            
            if(!collisionOn) {
                switch(direction) {
                    case "up": y -= speed; break;
                    case "down": y += speed; break;
                    case "left": x -= speed; break;
                    case "right": x += speed; break;
                }
            }
            
            spriteCounter++;
            if(spriteCounter > 12) {
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
        
        if(image != null) g2.drawImage(image, x, y, null);
        else {
            g2.setColor(Color.BLUE);
            g2.fillRect(x, y, gp.TILE_SIZE, gp.TILE_SIZE);
        }
    }
}