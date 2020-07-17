// License: GPL. For details, see LICENSE file.
package parser.data.ifc;

import nl.tue.buildingsmart.express.population.EntityInstance;
import parser.helper.IFCShapeRepresentationCatalog.RepresentationIdentifier;
import parser.helper.IFCShapeRepresentationCatalog.RepresentationType;

/**
 * Class representing identifier and type on an IFCSHAPEREPRESENTATION
 * @author rebsc
 *
 */
public class IFCShapeRepresentationIdentity {
	private EntityInstance representationObjectEntity;
	private RepresentationIdentifier identifier;
	private RepresentationType type;

	public IFCShapeRepresentationIdentity() {
		this.representationObjectEntity = null;
		this.identifier = null;
		this.type = null;
	}

	public IFCShapeRepresentationIdentity(EntityInstance representationObject, RepresentationIdentifier identifier, RepresentationType type) {
		this.setRepresentationObjectEntity(representationObject);
		this.identifier = identifier;
		this.type = type;
	}

	public RepresentationIdentifier getIdentifier() {
		return identifier;
	}

	public String getIdentifierString() {
		return identifier.name();
	}

	public RepresentationType getType() {
		return type;
	}

	public String getTypeString() {
		return type.name();
	}

	public void setIdentifier(RepresentationIdentifier identifier) {
		this.identifier = identifier;
	}

	public void setType(RepresentationType type) {
		this.type = type;
	}

	public boolean isFilled() {
		if(this.identifier != null && this.type != null) return true;
		return false;
	}

	public EntityInstance getRepresentationObjectEntity() {
		return representationObjectEntity;
	}

	public void setRepresentationObjectEntity(EntityInstance representationObjectEntity) {
		this.representationObjectEntity = representationObjectEntity;
	}


}