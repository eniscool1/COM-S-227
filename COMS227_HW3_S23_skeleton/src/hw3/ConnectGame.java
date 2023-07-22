package hw3;

import java.util.ArrayList;
import java.util.Random;

import api.ScoreUpdateListener;
import api.ShowDialogListener;
import api.Tile;


// I do not understand why failing tests, the game works perfectly

/**
 * Class that models a game.
 */
public class ConnectGame {
	private ShowDialogListener dialogListener;
	private ScoreUpdateListener scoreListener;
	
	/**
	 * the grid that models the game grid
	 */
	private Grid gameGrid;
	
	/**
	 * ArrayList that holds the selected tiles
	 */
	private ArrayList<Tile> tileArray;
	
	/**
	 * the max level a tile can be at any given point
	 */
	private int maxTileLevel;
	
	/**
	 * the minimum level a tile can be at any given point
	 */
	private int minTileLevel;
	
	/**
	 * the player's score
	 */
	private long score;
	
	/**
	 * the random that allows the class to be seeded
	 */
	private Random rand;
	
	/**
	 * Constructs a new ConnectGame object with given grid dimensions and minimum
	 * and maximum tile levels.
	 * 
	 * @param width  grid width
	 * @param height grid height
	 * @param min    minimum tile level
	 * @param max    maximum tile level
	 * @param rand   random number generator
	 */
	public ConnectGame(int width, int height, int min, int max, Random rand) {
		// TODO
		gameGrid = new Grid(width, height);
		maxTileLevel = max;
		minTileLevel = min;
		
		this.rand = rand;
		tileArray = new ArrayList<Tile>();
	}

	/**
	 * Gets a random tile with level between minimum tile level inclusive and
	 * maximum tile level exclusive. For example, if minimum is 1 and maximum is 4,
	 * the random tile can be either 1, 2, or 3.
	 * <p>
	 * DO NOT RETURN TILES WITH MAXIMUM LEVEL
	 * 
	 * @return a tile with random level between minimum inclusive and maximum
	 *         exclusive
	 */
	public Tile getRandomTile() {
		// TODO
		return new Tile(rand.nextInt(maxTileLevel - minTileLevel) + minTileLevel);
	}

	/**
	 * Regenerates the grid with all random tiles produced by getRandomTile().
	 */
	public void radomizeTiles() {
		// TODO	
		for (int row = 0; row < gameGrid.getWidth(); row++) {
			for (int col = 0; col < gameGrid.getHeight(); col++) {
				gameGrid.setTile(getRandomTile(), row, col);
				gameGrid.getTile(row, col).setLocation(row, col);
			}
		}
	}

	/**
	 * Determines if two tiles are adjacent to each other. They may be next to each
	 * other horizontally, vertically, or diagonally.
	 * 
	 * @param t1 one of the two tiles
	 * @param t2 one of the two tiles
	 * @return true if they are next to each other horizontally, vertically, or
	 *         diagonally on the grid, false otherwise
	 */
	public boolean isAdjacent(Tile t1, Tile t2) {
		// TODO
		if (t1 == null || t2 == null) {
			return false;
		}
		
		int x1 = t1.getX();
		int x2 = t2.getX();
		
		int y1 = t1.getY();
		int y2 = t2.getY();
		if (Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1) {
			return true;
		}
		
//		if ()
		return false;
	}

	/**
	 * Indicates the user is trying to select (clicked on) a tile to start a new
	 * selection of tiles.
	 * <p>
	 * If a selection of tiles is already in progress, the method should do nothing
	 * and return false.
	 * <p>
	 * If a selection is not already in progress (this is the first tile selected),
	 * then start a new selection of tiles and return true.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return true if this is the first tile selected, otherwise false
	 */
	public boolean tryFirstSelect(int x, int y) {
		// TODO
		if (tileArray.isEmpty()) {
			tileArray.add(gameGrid.getTile(x, y));
			gameGrid.getTile(x, y).setSelect(true);
			return true;
		}
		return false;
		
	}

	/**
	 * Indicates the user is trying to select (mouse over) a tile to add to the
	 * selected sequence of tiles. The rules of a sequence of tiles are:
	 * 
	 * <pre>
	 * 1. The first two tiles must have the same level.
	 * 2. After the first two, each tile must have the same level or one greater than the level of the previous tile.
	 * </pre>
	 * 
	 * For example, given the sequence: 1, 1, 2, 2, 2, 3. The next selected tile
	 * could be a 3 or a 4. If the use tries to select an invalid tile, the method
	 * should do nothing. If the user selects a valid tile, the tile should be added
	 * to the list of selected tiles.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 */
	public void tryContinueSelect(int x, int y) {
		// TODO
		// could be cleaned up more with other local variables
		
		if(tileArray.isEmpty()) {
			return;
		}
		
		int size = tileArray.size();
		int tileLevel = gameGrid.getTile(x, y).getLevel();
		int lastTileLevel = tileArray.get(size - 1).getLevel();
		
		//handles if the tiles are not adjacent and the case where nothing is selected and when the tile level do not work
		if (!isAdjacent(tileArray.get(size - 1), gameGrid.getTile(x, y))) {
			return;
		}
		if (size == 0 || tileLevel > lastTileLevel+1) {
			return;
		}
		if (gameGrid.getTile(x, y) == null) {
			return;
		}
		if (!gameGrid.getTile(x, y).isSelected()) {
			
			// handles the case where the first two must be the same level
			if ((tileLevel == lastTileLevel) && size == 1) {
				tileArray.add(gameGrid.getTile(x, y));
				gameGrid.getTile(x, y).setSelect(true);
				return;
			}
			if ((tileLevel == lastTileLevel || tileLevel == lastTileLevel+1) && size >= 2) {
				tileArray.add(gameGrid.getTile(x, y));
				gameGrid.getTile(x, y).setSelect(true);
				return;
			}
		}
		if (size > 1 && gameGrid.getTile(x, y) == (tileArray.get(tileArray.size() - 2))) {
			unselect(tileArray.get((tileArray.size() - 1)).getX(), tileArray.get((tileArray.size() - 1)).getY());
			return;
		}
		
	}


	/**
	 * Indicates the user is trying to finish selecting (click on) a sequence of
	 * tiles. If the method is not called for the last selected tile, it should do
	 * nothing and return false. Otherwise it should do the following:
	 * 
	 * <pre>
	 * 1. When the selection contains only 1 tile reset the selection and make sure all tiles selected is set to false.
	 * 2. When the selection contains more than one block:
	 *     a. Upgrade the last selected tiles with upgradeLastSelectedTile().
	 *     b. Drop all other selected tiles with dropSelected().
	 *     c. Reset the selection and make sure all tiles selected is set to false.
	 * </pre>
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return return false if the tile was not selected, otherwise return true
	 */
	public boolean tryFinishSelection(int x, int y) {
		
		if (tileArray.isEmpty()) {
			return false;
		}
		
		int size = tileArray.size();		
		
		if (size == 0) {
			return false;
		}
		
		if (!(gameGrid.getTile(x, y) == (tileArray.get(size - 1)))) {
			return false;
		}
		
		// handles if there is only one selected
		if (size == 1) {
			tileArray.get(0).setSelect(false);
			tileArray.clear();
			return true;
		}
		
		// DO NOT CHANGE ORDER
		
		for ( int i=0; i < tileArray.size(); i++) {
			score += tileArray.get(i).getValue();
		}
		
		upgradeLastSelectedTile();		
		
		dropSelected();
		
		tileArray.clear();
	
		for(int col = 0; col<gameGrid.getWidth();col++) {
		    for(int row = 0; row<gameGrid.getHeight();row++) {
		    	gameGrid.getTile(col, row).setSelect(false);
		    }
		}
		
		scoreListener.updateScore(score);
		return true;
	}

	/**
	 * Increases the level of the last selected tile by 1 and removes that tile from
	 * the list of selected tiles. The tile itself should be set to unselected.
	 * <p>
	 * If the upgrade results in a tile that is greater than the current maximum
	 * tile level, both the minimum and maximum tile level are increased by 1. A
	 * message dialog should also be displayed with the message "New block 32,
	 * removing blocks 2". Not that the message shows tile values and not levels.
	 * Display a message is performed with dialogListener.showDialog("Hello,
	 * World!");
	 */
	public void upgradeLastSelectedTile() {
		// TODO
		int x = tileArray.get(tileArray.size() - 1).getX();
		int y = tileArray.get(tileArray.size() - 1).getY();
		int currLevel = tileArray.get(tileArray.size() - 1).getLevel();
		
		gameGrid.getTile(x, y).setLevel(currLevel + 1);
		
		if (currLevel + 1 > maxTileLevel) {
			dialogListener.showDialog("New block " + Math.pow(2,maxTileLevel + 1) + ", removing blocks " + Math.pow(2,minTileLevel));
			maxTileLevel++;
			minTileLevel++;
			dropLevel(minTileLevel-1);
		}
		
		tileArray.remove(tileArray.size() - 1);
		gameGrid.getTile(x, y).setSelect(false);
	}

	/**
	 * Gets the selected tiles in the form of an array. This does not mean selected
	 * tiles must be stored in this class as a array.
	 * 
	 * @return the selected tiles in the form of an array
	 */
	public Tile[] getSelectedAsArray() {
		// TODO
		Tile[] result = new Tile[tileArray.size()];
		for (int i=0; i < tileArray.size(); i++) {
			result[i] = tileArray.get(i);
		}
		return result;
	}

	/**
	 * Removes all tiles of a particular level from the grid. When a tile is
	 * removed, the tiles above it drop down one spot and a new random tile is
	 * placed at the top of the grid.
	 * 
	 * @param level the level of tile to remove
	 */
	public void dropLevel(int level) {
		// TODO
		for (int col = 0; col < gameGrid.getWidth(); col++) {
			for (int row = 0; row < gameGrid.getHeight(); row++) {
				if (gameGrid.getTile(col, row).getLevel() == level) {
//					Tile tile = gameGrid.getTile(col, row);
					int rowTemp = row;
					while (row > 0) {
						Tile top = gameGrid.getTile(col, row-1);
						if (top.getLevel() == level) {
							continue;
						} else {
							gameGrid.setTile(top, col, row);
							gameGrid.getTile(col, row).setLocation(col, row);
							row--;
						}
					}
					row = rowTemp;
					gameGrid.setTile(getRandomTile(), col, 0);
					gameGrid.getTile(col, 0).setLocation(col, 0);
				}
			}
		}
	}

	/**
	 * Removes all selected tiles from the grid. When a tile is removed, the tiles
	 * above it drop down one spot and a new random tile is placed at the top of the
	 * grid.
	 */
	public void dropSelected() {
		// TODO
		for (int i = 0; i < tileArray.size(); i++) {
			int x = tileArray.get(i).getX();
			int y = tileArray.get(i).getY();
			while (y > 0) {
				Tile top = gameGrid.getTile(x, y-1);
				gameGrid.setTile(top, x, y);
				gameGrid.getTile(x, y).setLocation(x, y);
				y--;
			}
			gameGrid.setTile(getRandomTile(), x, 0);
			gameGrid.getTile(x, 0).setLocation(x, 0);
		}
	}
	/**
	 * Remove the tile from the selected tiles.
	 * 
	 * @param x column of the tile
	 * @param y row of the tile
	 */
	public void unselect(int x, int y) {
		// TODO
		tileArray.remove(gameGrid.getTile(x, y));
		gameGrid.getTile(x, y).setSelect(false);
	}

	/**
	 * Gets the player's score.
	 * 
	 * @return the score
	 */
	public long getScore() {
		// TODO
		return score;
	}

	/**
	 * Gets the game grid.
	 * 
	 * @return the grid
	 */
	public Grid getGrid() {
		// TODO
		return gameGrid;
	}

	/**
	 * Gets the minimum tile level.
	 * 
	 * @return the minimum tile level
	 */
	public int getMinTileLevel() {
		// TODO
		return minTileLevel;
	}

	/**
	 * Gets the maximum tile level.
	 * 
	 * @return the maximum tile level
	 */
	public int getMaxTileLevel() {
		// TODO
		return maxTileLevel;
	}

	/**
	 * Sets the player's score.
	 * 
	 * @param score number of points
	 */
	public void setScore(long score) {
		// TODO
		this.score = score;
	}

	/**
	 * Sets the game's grid.
	 * 
	 * @param grid game's grid
	 */
	public void setGrid(Grid grid) {
		// TODO
		gameGrid = grid;
	}

	/**
	 * Sets the minimum tile level.
	 * 
	 * @param minTileLevel the lowest level tile
	 */
	public void setMinTileLevel(int minTileLevel) {
		// TODO
		this.minTileLevel = minTileLevel;

	}

	/**
	 * Sets the maximum tile level.
	 * 
	 * @param maxTileLevel the highest level tile
	 */
	public void setMaxTileLevel(int maxTileLevel) {
		// TODO
		this.maxTileLevel = maxTileLevel;
	}

	/**
	 * Sets callback listeners for game events.
	 * 
	 * @param dialogListener listener for creating a user dialog
	 * @param scoreListener  listener for updating the player's score
	 */
	public void setListeners(ShowDialogListener dialogListener, ScoreUpdateListener scoreListener) {
		this.dialogListener = dialogListener;
		this.scoreListener = scoreListener;
	}

	/**
	 * Save the game to the given file path.
	 * 
	 * @param filePath location of file to save
	 */
	public void save(String filePath) {
		GameFileUtil.save(filePath, this);
	}

	/**
	 * Load the game from the given file path
	 * 
	 * @param filePath location of file to load
	 */
	public void load(String filePath) {
		GameFileUtil.load(filePath, this);
	}
}
