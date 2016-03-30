package sim;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import world.Zone;
import world.Zone.ZoneClass;

public class Blueprint {
	protected List<List<Zone>> zonemap;
	
	/**
	 * Saves a zone map into a blueprint file.
	 * 
	 * @param filename : the filename to use
	 * @param zonemap : the zone map to save
	 * @throws IOException 
	 */
	public static void saveToBlueprint(String filename, List<List<Zone>> zonemap) throws IOException {
		int height = zonemap.size();
		int width = zonemap.get(0).size();
		
		String bp = "";
		
		for(int y = 0 ; y < height ; y++) {
			for(int x = 0 ; x < width ; x++) {
				switch(zonemap.get(y).get(x).getType()) {
					case COMMERCIAL:
						bp += 'a';
						break;
					case CULTURAL:
						bp += 'b';
						break;
					case FREE:
						bp += 'c';
						break;
					case INDUSTRY:
						bp += 'd';
						break;
					case PUBLIC_SERVICE:
						bp += 'e';
						break;
					case RESIDENTIAL:
						bp += 'f';
						break;
					case ROAD:
						bp += 'g';
						break;
					default:
						break;
				}
			}
			
			bp += '\n';
		}
		
		Files.write(Paths.get(filename), bp.getBytes(), StandardOpenOption.CREATE);
	}

	/**
	 * Loads the blueprint.
	 * 
	 * @param path : the path to the blueprint file.
	 * @throws IOException
	 */
	public Blueprint(String path) throws IOException {
		this.zonemap = new ArrayList<List<Zone>>();
		List<String> lines = Files.readAllLines(Paths.get(path));
		
		if(lines.isEmpty())
			return;
		
		for(int y = 0 ; y < lines.size() ; y++) {
			String line = lines.get(y);

			for(int x = 0 ; x < line.length() ; x++) {
				char c = line.charAt(x);
				
				if(c == '\n' || c == '\r')
					continue;
				
				List<Zone> row = new ArrayList<Zone>();
				
				switch(c) {
					case 'a':
						row.add(new Zone(x, y, ZoneClass.COMMERCIAL));
						break;
					case 'b':
						row.add(new Zone(x, y, ZoneClass.CULTURAL));
						break;
					case 'c':
						row.add(new Zone(x, y, ZoneClass.FREE));
						break;
					case 'd':
						row.add(new Zone(x, y, ZoneClass.INDUSTRY));
						break;
					case 'e':
						row.add(new Zone(x, y, ZoneClass.PUBLIC_SERVICE));
						break;
					case 'f':
						row.add(new Zone(x, y, ZoneClass.RESIDENTIAL));
						break;
					case 'g':
						row.add(new Zone(x, y, ZoneClass.ROAD));
						break;
					default:
						break;
				}
				
				this.zonemap.add(row);
			}
		}
	}
	
	/**
	 * Returns the zone map from the blueprint.
	 * @return the zone map
	 */
	public List<List<Zone>> getZoneMap() {
		return this.zonemap;
	}
}
