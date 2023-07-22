package hw3;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import api.Tile;
/**
 * Utility class with static methods for saving and loading game files.
 */
public class GameFileUtil {
	/**
	 * Saves the current game state to a file at the given file path.
	 * <p>
	 * The format of the file is one line of game data followed by multiple lines of
	 * game grid. The first line contains the: width, height, minimum tile level,
	 * maximum tile level, and score. The grid is represented by tile levels. The
	 * conversion to tile values is 2^level, for example, 1 is 2, 2 is 4, 3 is 8, 4
	 * is 16, etc. The following is an example:
	 * 
	 * <pre>
	 * 5 8 1 4 100
	 * 1 1 2 3 1
	 * 2 3 3 1 3
	 * 3 3 1 2 2
	 * 3 1 1 3 1
	 * 2 1 3 1 2
	 * 2 1 1 3 1
	 * 4 1 3 1 1
	 * 1 3 3 3 3
	 * </pre>
	 * 
	 * @param filePath the path of the file to save
	 * @param game     the game to save
	 */
	public static void save(String filePath, ConnectGame game) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			int width = game.getGrid().getWidth();
			int height = game.getGrid().getHeight();
			int minTileLevel = game.getMinTileLevel();
			int maxTileLevel = game.getMaxTileLevel();
			long score = game.getScore();
			Grid grid = game.getGrid();
		
			writer.write(String.format("%d %d %d %d %d\n", width, height, minTileLevel, maxTileLevel, score));
			
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {
					
					writer.write(Integer.toString(grid.getTile(col, row).getLevel()));
					
					if (col < width - 1) {
						writer.write(" ");
					}
				}
				if (row < height - 1) {
					writer.write("\n");
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the file at the given file path into the given game object. When the
	 * method returns the game object has been modified to represent the loaded
	 * game.
	 * <p>
	 * See the save() method for the specification of the file format.
	 * 
	 * @param filePath the path of the file to load
	 * @param game     the game to modify
	 * @throws FileNotFoundException 
	 */
	public static void load(String filePath, ConnectGame game) {
			try {
				Scanner reader = new Scanner(new FileInputStream(filePath));
				
				int width = reader.nextInt();
				int height = reader.nextInt();
				int minTile = reader.nextInt();
				int maxTile = reader.nextInt();
				long gameScore =  reader.nextLong();
				
				game.setGrid(new Grid(width, height));
				
				game.setScore(gameScore);
				
				game.setMaxTileLevel(maxTile);
				game.setMinTileLevel(minTile);
				
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						game.getGrid().setTile(new Tile(reader.nextInt()), j, i);
					}
				}
				reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
