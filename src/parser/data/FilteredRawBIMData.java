// License: GPL. For details, see LICENSE file.
package parser.data;

import java.util.Vector;

import nl.tue.buildingsmart.express.population.EntityInstance;

/**
 * Data structure holding specific BIM data elements.
 * @author rebsc
 */
public class FilteredRawBIMData {
	private EntityInstance ifcSite;	// root of data set
	private Vector<EntityInstance> areaObjects;
	private Vector<EntityInstance> wallObjects;
	private Vector<EntityInstance> columnObjects;
	private Vector<EntityInstance> doorObjects;
	private Vector<EntityInstance> stairObjects;

	public EntityInstance getIfcSite() {
		return ifcSite;
	}

	public void setIfcSite(EntityInstance ifcSite) {
		this.ifcSite = ifcSite;
	}

	public Vector<EntityInstance> getAreaObjects() {
		return areaObjects;
	}

	public void setAreaObjects(Vector<EntityInstance> areaObjects) {
		this.areaObjects = areaObjects;
	}

	public Vector<EntityInstance> getWallObjects() {
		return wallObjects;
	}

	public void setWallObjects(Vector<EntityInstance> wallObjects) {
		this.wallObjects = wallObjects;
	}

	public Vector<EntityInstance> getColumnObjects() {
		return columnObjects;
	}

	public void setColumnObjects(Vector<EntityInstance> columnObjects) {
		this.columnObjects = columnObjects;
	}

	public Vector<EntityInstance> getDoorObjects() {
		return doorObjects;
	}

	public void setDoorObjects(Vector<EntityInstance> doorObjects) {
		this.doorObjects = doorObjects;
	}

	public Vector<EntityInstance> getStairObjects() {
		return stairObjects;
	}

	public void setStairObjects(Vector<EntityInstance> stairObjects) {
		this.stairObjects = stairObjects;
	}

}
