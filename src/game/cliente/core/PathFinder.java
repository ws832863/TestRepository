package game.cliente.core;

import game.cliente.utils.Util;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.AStarHeuristic;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.heuristics.ClosestHeuristic;

/**
 * @author Michel Montenegro
 */
public class PathFinder  implements TileBasedMap{
	
	private Path path;
	
	/**
	 * Guarda os locais visitados na busca
	 */
	private boolean[][] visited = new boolean[Util.WIDTH_MAP_IN_TILES][Util.HEIGHT_MAP_IN_TILES];
	
	   public boolean[][] getVisited() {
		return visited;
	}

	/**
	    * Calculate path from (sx,sy) to (ex,ey)
	    *
	    * @param sx - start x of the path
	    * @param sy - start y of the path
	    * @param ex - end x of the path
	    * @param ey - end y of the path
	    */
	   public Path updatePath(int sx, int sy, int ex, int ey) throws SlickException {
		  resetVisited();
		  								//new ManhattanHeuristic(0);
		  								//new ClosestHeuristic();
		  								//new ClosestSquaredHeuristic();
		  AStarHeuristic  pathHeuristic = new ClosestHeuristic();
		  // find any blocked paths
	      AStarPathFinder pathfinder = new AStarPathFinder(this, 1000, false, pathHeuristic);
	      Mover dummyMover = new Mover() {};
	      path = pathfinder.findPath(dummyMover, sx, sy, ex, ey);
	      return path;
	   }
	
	@Override
	public boolean blocked(PathFindingContext context, int x, int y) {
	    if (Util.OBJECTS_OF_WORLD[x][y] == Util.TERRAIN_GROUND){
	         return false;
	    } else if (Util.OBJECTS_OF_WORLD[x][y] == Util.TERRAIN_WALL){
	         return true;
	    } else if (Util.OBJECTS_OF_WORLD[x][y] == Util.TERRAIN_MONTAIN){
	         return true;
	    } else if (Util.OBJECTS_OF_WORLD[x][y] == Util.TERRAIN_WATER){
	         return true;
	    } else if (Util.OBJECTS_OF_WORLD[x][y] == Util.TERRAIN_OBJECT_PLAYER_NETWORK){
	         return false;
	    } else if (Util.OBJECTS_OF_WORLD[x][y] == Util.TERRAIN_MOB_SPAWN){
	         return false;
	    } else if (Util.OBJECTS_OF_WORLD[x][y] == Util.TERRAIN_OBJECT_NPC){
	         return true;
	    }
		return false;
	}

	@Override
	public float getCost(PathFindingContext context, int x, int y) {
	    if (Util.OBJECTS_OF_WORLD[x][y] == Util.TERRAIN_GROUND){
	         return 0;
	    } else if (Util.OBJECTS_OF_WORLD[x][y] == Util.TERRAIN_OBJECT_PLAYER_NETWORK){
	         return 1;
	    } else if (Util.OBJECTS_OF_WORLD[x][y] == Util.TERRAIN_MOB_SPAWN){
	         return 2;
	    }
		return 0;
	}

	@Override
	public int getHeightInTiles() {
		return Util.HEIGHT_MAP_IN_TILES;
	}

	@Override
	public int getWidthInTiles() {
		return Util.WIDTH_MAP_IN_TILES;
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		visited[x][y]=true;
		
	}

	
	public void resetVisited() {
		for (int i = 0; i < Util.WIDTH_MAP_IN_TILES; i++) {
			for (int j = 0; j < Util.HEIGHT_MAP_IN_TILES; j++) {
				visited[i][j]=false;
			}
		}
	}
}
