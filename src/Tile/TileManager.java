package tile;

import game.GamePanel; // Pastikan package sesuai
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
        tile = new Tile[10]; // Maksimal 10 jenis ubin
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        
        getTileImage();
        loadMap(); 
    }
    
    // METHOD BARU: Untuk memperbesar gambar (Scale)
    public BufferedImage setup(BufferedImage image) {
        BufferedImage scaledImage = new BufferedImage(gp.TILE_SIZE, gp.TILE_SIZE, image.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(image, 0, 0, gp.TILE_SIZE, gp.TILE_SIZE, null);
        g2.dispose();
        return scaledImage;
    }
    
    public void getTileImage() {
        try {
            // 1. LOAD FILE UTAMA (Dungeon_Tileset.png)
            // Pastikan nama file di folder res sama persis (huruf besar/kecil)
            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/res/Dungeon_Tileset.png"));
            
            // Ukuran asli per kotak di gambar (Biasanya 16x16 pixel untuk pixel art)
            int TILE_ORIGINAL = 16; 
            
            // --- TILE 0: FLOOR (Pengganti Rumput) ---
            // Ambil koordinat x=16, y=16 (Baris ke-2, Kolom ke-2 dari gambar)
            tile[0] = new Tile();
            tile[0].image = setup(tileSheet.getSubimage(16, 16, TILE_ORIGINAL, TILE_ORIGINAL));
            
            // --- TILE 1: WALL (Tembok) ---
            // Ambil koordinat x=16, y=0 (Baris paling atas, bagian tembok)
            tile[1] = new Tile();
            tile[1].image = setup(tileSheet.getSubimage(16, 0, TILE_ORIGINAL, TILE_ORIGINAL));
            tile[1].collision = true; // Tidak bisa ditembus
            
            // --- TILE 2: VOID/HOLE (Pengganti Air) ---
            // Ambil area gelap/lubang. Kita ambil di x=0, y=48 (Baris ke-4)
            tile[2] = new Tile();
            tile[2].image = setup(tileSheet.getSubimage(0, 48, TILE_ORIGINAL, TILE_ORIGINAL));
            tile[2].collision = true; // Tidak bisa ditembus (Jatuh)
            
        } catch (Exception e) {
            System.out.println("Gagal load tileset! Pastikan file Dungeon_Tileset.png ada di folder res.");
            e.printStackTrace();
        }
    }
    
    public void loadMap() {
        // PETA MANUAL (0=Lantai, 1=Tembok, 2=Jurang)
        int mapData[][] = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, // Baris 1 (Tembok semua)
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,1,1,1,0,0,0,2,2,2,0,0,1}, // Ada tembok dan jurang di tengah
            {1,0,0,0,1,0,1,0,0,0,2,2,2,0,0,1},
            {1,0,0,0,1,0,1,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}  // Baris terakhir
        };
        
        // Loop mengisi array mapTileNum
        for(int col = 0; col < gp.maxScreenCol; col++) {
            for(int row = 0; row < gp.maxScreenRow; row++) {
                if(row < mapData.length && col < mapData[0].length){
                    mapTileNum[col][row] = mapData[row][col];
                }
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
            
            if(tile[tileNum] != null && tile[tileNum].image != null) {
                g2.drawImage(tile[tileNum].image, x, y, null);
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