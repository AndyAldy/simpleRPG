package tile;

import game.GamePanel;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

public class TileManager {
    
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];
    
    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10]; // Kita buat maksimal 10 jenis ubin dulu
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        
        getTileImage();
        loadMap(); // Load peta manual (array)
    }
    
    public void getTileImage() {
        try {
            // INDEX 0: RUMPUT (Bisa jalan)
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/grass.png"));
            
            // INDEX 1: TEMBOK (Collision = true)
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/wall.png"));
            tile[1].collision = true;
            
            // INDEX 2: AIR (Collision = true)
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/water.png"));
            tile[2].collision = true;
            
        } catch (Exception e) {
            System.out.println("Gagal load gambar tiles. Pastikan file ada di folder res/");
        }
    }
    
    public void loadMap() {
        // PETA SEDERHANA (1 = Tembok, 0 = Rumput, 2 = Air)
        // 16 Kolom x 12 Baris
        int mapData[][] = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,1,1,1,0,0,0,2,2,2,0,0,1},
            {1,0,0,0,1,0,1,0,0,0,2,2,2,0,0,1}, // Ada rintangan tembok di tengah
            {1,0,0,0,1,0,1,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        
        // Rotasi array agar sesuai koordinat [col][row]
        for(int col = 0; col < gp.maxScreenCol; col++) {
            for(int row = 0; row < gp.maxScreenRow; row++) {
                mapTileNum[col][row] = mapData[row][col];
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        
        while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
            int tileNum = mapTileNum[col][row];
            
            // Gambar tiles
            if(tile[tileNum] != null && tile[tileNum].image != null) {
                g2.drawImage(tile[tileNum].image, x, y, gp.TILE_SIZE, gp.TILE_SIZE, null);
            } else {
                // Fallback jika gambar tidak ketemu: Gambar kotak warna
                if(tileNum == 0) g2.setColor(java.awt.Color.GREEN); // Rumput
                if(tileNum == 1) g2.setColor(java.awt.Color.GRAY);  // Tembok
                if(tileNum == 2) g2.setColor(java.awt.Color.BLUE);  // Air
                g2.fillRect(x, y, gp.TILE_SIZE, gp.TILE_SIZE);
            }
            
            col++;
            x += gp.TILE_SIZE;
            
            if(col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.TILE_SIZE;
            }
        }
    }
}