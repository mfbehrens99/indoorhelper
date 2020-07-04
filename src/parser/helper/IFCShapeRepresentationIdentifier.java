// License: GPL. For details, see LICENSE file.
package parser.helper;

import java.util.ArrayList;
import java.util.Vector;

import org.openstreetmap.josm.tools.Logging;

import nl.tue.buildingsmart.express.population.EntityInstance;
import nl.tue.buildingsmart.express.population.ModelPopulation;
import parser.data.ifc.IFCShapeRepresentationIdentity;
import parser.helper.IFCShapeRepresentationCatalog.AdvancedBrepRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.AdvancedSweptSolidRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.BoundingBoxRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.BrepRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.CSGRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.ClippingRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.CurveRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.MappedRepresentatiobTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.RepresentationIdentifier;
import parser.helper.IFCShapeRepresentationCatalog.RepresentationType;
import parser.helper.IFCShapeRepresentationCatalog.SurfaceModelRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.SweptSolidRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.TessellationRepresentationTypeItems;

/**
 * Class to identify the type on an IFCSHAPEREPRESENTATION object
 * @author rebsc
 *
 */
public class IFCShapeRepresentationIdentifier {

	/**
	 * Method gets IFCSHAPEREPRESENTATION.REPRESENTATIONIDENTIFIER and IFCSHAPEREPRESENTATION.REPRESENTATIONTYPE of object
	 * @param shapeRepresentation IFCShapeRepresentationIdentity
	 * @return Returns object containing IFCSHAPEREPRESENTATION.REPRESENTATIONIDENTIFIER and IFCSHAPEREPRESENTATION.REPRESENTATIONTYPE
	 */
	public static IFCShapeRepresentationIdentity identifyShapeRepresentation(EntityInstance shapeRepresentation){
		IFCShapeRepresentationIdentity rep = new IFCShapeRepresentationIdentity();
		String identifier = prepareRepresentationAttribute(shapeRepresentation.getAttributeValueBN("RepresentationIdentifier").toString());
		String type = prepareRepresentationAttribute(shapeRepresentation.getAttributeValueBN("RepresentationType").toString());

		// set representationObjectId
		rep.setRepresentationObjectEntity(shapeRepresentation);

		// TODO the performance of iterating thru every value might needs to be improved!
		// get identifier
		for (RepresentationIdentifier i : RepresentationIdentifier.values()) {
			for(String ident : getIdentifierTags(i.name())) {
				if(identifier.equals(ident)) {
			    	rep.setIdentifier(i);
			    	break;
			    }
			}
		}
		// get type
		for (RepresentationType t : RepresentationType.values()) {
			for(String typ : getIdentifierTags(t.name())) {
				if(type.equals(typ)) {
			    	rep.setType(t);
			    	break;
			    }
			}
		}
		return rep;
	}

	/**
	 * Gets the type of specific IFCSHAPEREPRESENTATION.ITEMS. If item type is not supported for IFCSHAPEREPRESENTATION.REPRESENTATIONTYPE
	 * method will return null.
	 * @param ifcModel ifcModel
	 * @param ident IFCREPRESENTATIONTYPEOBJECT object
	 * @param item to get the representation type for (IFCSHAPEREPRESENTATION.ITEM packed into EntityInstance object)
	 * @return String with IFCSHAPEREPRESENTATION.ITEM type definition or null if not allowed in standard
	 */
	public static String getRepresentationItemType(ModelPopulation ifcModel, IFCShapeRepresentationIdentity ident, EntityInstance item) {

		//TODO cleanup code

		if(ident.getType().equals(RepresentationType.AdvancedBrep)) {
			// here IFC standard only allows IFCADVANCEDBREP and IFCFACETEDBREP as item

			// get all IFCADVANCEDBREP and IFCFACETEDBREP objects and check if item is part of it
			ArrayList<EntityInstance> ifcAdvancedBrep = new ArrayList<>();
			for(String flag : getIdentifierTags(AdvancedBrepRepresentationTypeItems.IfcAdvancedBrep.name())) {
				ifcAdvancedBrep.addAll(ifcModel.getInstancesOfType(flag));
			}
			ArrayList<EntityInstance> ifcFacetedBrep = new ArrayList<>();
			for(String flag : getIdentifierTags(AdvancedBrepRepresentationTypeItems.IfcFacetedBrep.name())) {
				ifcFacetedBrep.addAll(ifcModel.getInstancesOfType(flag));
			}

			if(ifcAdvancedBrep.contains(item))	return AdvancedBrepRepresentationTypeItems.IfcAdvancedBrep.name();
			if(ifcFacetedBrep.contains(item))	return AdvancedBrepRepresentationTypeItems.IfcFacetedBrep.name();
			Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + item.getEntityDefinition() + " is not supported");
			return null;
		}

		if(ident.getType().equals(RepresentationType.AdvancedSweptSolid)) {
			// here IFC standard only allows IFCSWEPTDISKSOLID and IFCSWEPTDISKSOLIDPOLYGON as item

			// get all IFCSWEPTDISKSOLID and IFCSWEPTDISKSOLIDPOLYGON objects and check if item is part of it
			ArrayList<EntityInstance>  ifcSweptDiskSolids = new ArrayList<>();
			for(String flag : getIdentifierTags(AdvancedSweptSolidRepresentationTypeItems.IfcSweptDiskSolid.name())) {
				ifcSweptDiskSolids.addAll(ifcModel.getInstancesOfType(flag));
			}
			ArrayList<EntityInstance> IfcSweptDiskSolidPolygonals = new ArrayList<>();
			for(String flag : getIdentifierTags(AdvancedSweptSolidRepresentationTypeItems.IfcSweptDiskSolidPolygonal.name())) {
				IfcSweptDiskSolidPolygonals.addAll(ifcModel.getInstancesOfType(flag));
			}

			if(ifcSweptDiskSolids.contains(item))			return AdvancedSweptSolidRepresentationTypeItems.IfcSweptDiskSolid.name();
			if(IfcSweptDiskSolidPolygonals.contains(item))	return AdvancedSweptSolidRepresentationTypeItems.IfcSweptDiskSolidPolygonal.name();
			Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + item.getEntityDefinition() + " is not supported");
			return null;
		}

		if(ident.getType().equals(RepresentationType.Brep)) {
			// here IFC standard only allows IFCFACETEDBREP as item

			// get all IFCFACETEDBREP objects and check if item is part of it
			for(String flag : getIdentifierTags(BrepRepresentationTypeItems.IfcFacetedBrep.name())) {
				if(ifcModel.getInstancesOfType(flag).contains(item)){
					return BrepRepresentationTypeItems.IfcFacetedBrep.name();
				}
			}
			Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + item.getEntityDefinition() + " is not supported");
			return null;
		}

		if(ident.getType().equals(RepresentationType.CSG)) {
			// here IFC standard only allows IFCCSGSOLID, IFCBOOLEANRESULT and IFCPRIMITIVE3D as items

			// get all IFCCSGSOLID, IFCBOOLEANRESULT and IFCPRIMITIVE3D objects and check if item is part of it
			ArrayList<EntityInstance> ifcBooleanResults = new ArrayList<>();
			for(String flag : getIdentifierTags(CSGRepresentationTypeItems.IfcBooleanResult.name())) {
				ifcBooleanResults.addAll(ifcModel.getInstancesOfType(flag));
			}
			ArrayList<EntityInstance> ifcCSGSolids = new ArrayList<>();
			for(String flag : getIdentifierTags(CSGRepresentationTypeItems.IfcCsgSolid.name())) {
				ifcCSGSolids.addAll(ifcModel.getInstancesOfType(flag));
			}
			ArrayList<EntityInstance> ifcPrimitive3Ds = new ArrayList<>();
			for(String flag : getIdentifierTags(CSGRepresentationTypeItems.IfcPrimitive3D.name())) {
				ifcPrimitive3Ds.addAll(ifcModel.getInstancesOfType(flag));
			}

			if(ifcBooleanResults.contains(item))	return CSGRepresentationTypeItems.IfcBooleanResult.name();
			if(ifcCSGSolids.contains(item))			return CSGRepresentationTypeItems.IfcCsgSolid.name();
			if(ifcPrimitive3Ds.contains(item))		return CSGRepresentationTypeItems.IfcPrimitive3D.name();
			Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + item.getEntityDefinition() + " is not supported");
			return null;
		}

		if(ident.getType().equals(RepresentationType.Tessellation)) {
			// here IFC standard only allows IFCTESSELATEDFACESET as item

			// get all IFCTESSELATEDFACESET objects and check if item is part of it
			for(String flag : getIdentifierTags(TessellationRepresentationTypeItems.IfcTessellatedFaceSet.name())) {
				if(ifcModel.getInstancesOfType(flag).contains(item)){
					return TessellationRepresentationTypeItems.IfcTessellatedFaceSet.name();
				}
			}
			Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + item.getEntityDefinition() + " is not supported");
			return null;
		}

		if(ident.getType().equals(RepresentationType.Clipping)) {
			// here IFC standard only allows IFCBOOLEANCLIPPINGRESULT as item

			// get all IFCBOOLEANCLIPPINGRESULT objects and check if item is part of it
			for(String flag : getIdentifierTags(ClippingRepresentationTypeItems.IfcBooleanClippingResult.name())) {
				if(ifcModel.getInstancesOfType(flag).contains(item)){
					return ClippingRepresentationTypeItems.IfcBooleanClippingResult.name();
				}
			}
			Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + item.getEntityDefinition() + " is not supported");
			return null;
		}

		if(ident.getType().equals(RepresentationType.Curve2D) || ident.getType().equals(RepresentationType.Curve3D)) {
			// here IFC standard only allows IFCBOUNDEDCURVE as item

			// get all IFCBOUNDEDCURVE objects and check if item is part of it
			ArrayList<EntityInstance> ifcBoundedCurves = new ArrayList<>();
			for(String flag : getIdentifierTags(CurveRepresentationTypeItems.IfcBoundedCurve.name())) {
				ifcBoundedCurves.addAll(ifcModel.getInstancesOfType(flag));
			}
			ArrayList<EntityInstance> ifcPolylines = new ArrayList<>();
			for(String flag : getIdentifierTags(CurveRepresentationTypeItems.IfcPolyline.name())) {
				ifcPolylines.addAll(ifcModel.getInstancesOfType(flag));
			}

			if(ifcBoundedCurves.contains(item))		return CurveRepresentationTypeItems.IfcBoundedCurve.name();
			if(ifcPolylines.contains(item))			return CurveRepresentationTypeItems.IfcPolyline.name();

			Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + item.getEntityDefinition() + " is not supported");
			return null;
		}

		if(ident.getType().equals(RepresentationType.SurfaceModel)) {
			// here IFC standard only allows IFCTESSELLATEDITEM, IFCSCHELLBASEDSURFACEMODEL and
			// IFCFACEBASEDSURFACEMODEL as item

			// get all IFCTESSELLATEDITEM, IFCSCHELLBASEDSURFACEMODEL and
			// IFCFACEBASEDSURFACEMODEL objects and check if item is part of it
			ArrayList<EntityInstance> ifcTessellatedItems = new ArrayList<>();
			for(String flag : getIdentifierTags(SurfaceModelRepresentationTypeItems.IfcTessellatedItem.name())) {
				ifcTessellatedItems.addAll(ifcModel.getInstancesOfType(flag));
			}
			ArrayList<EntityInstance> ifcShellBasedSurfaceModels = new ArrayList<>();
			for(String flag : getIdentifierTags(SurfaceModelRepresentationTypeItems.IfcShellBasedSurfaceModel.name())) {
				ifcShellBasedSurfaceModels.addAll(ifcModel.getInstancesOfType(flag));
			}
			ArrayList<EntityInstance> ifcFaceBasedSurfaceModels = new ArrayList<>();
			for(String flag : getIdentifierTags(SurfaceModelRepresentationTypeItems.IfcFaceBasedSurfaceModel.name())) {
				ifcFaceBasedSurfaceModels.addAll(ifcModel.getInstancesOfType(flag));
			}
			ArrayList<EntityInstance> ifcFacetedBreps = new ArrayList<>();
			for(String flag : getIdentifierTags(SurfaceModelRepresentationTypeItems.IfcFacetedBrep.name())) {
				ifcFacetedBreps.addAll(ifcModel.getInstancesOfType(flag));
			}

			if(ifcTessellatedItems.contains(item))			return SurfaceModelRepresentationTypeItems.IfcTessellatedItem.name();
			if(ifcShellBasedSurfaceModels.contains(item))	return SurfaceModelRepresentationTypeItems.IfcShellBasedSurfaceModel.name();
			if(ifcFaceBasedSurfaceModels.contains(item))	return SurfaceModelRepresentationTypeItems.IfcFaceBasedSurfaceModel.name();
			if(ifcFacetedBreps.contains(item))				return SurfaceModelRepresentationTypeItems.IfcFacetedBrep.name();
			Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + item.getEntityDefinition() + " is not supported");
			return null;
		}

		if(ident.getType().equals(RepresentationType.SweptSolid)) {
			// here IFC standard only allows IFCEXTRUDEDAREASOLID, IFCREVOLVEDAREASOLID as item

			// get all IFCEXTRUDEDAREASOLID, IFCREVOLVEDAREASOLID objects and check if item is part of it
			ArrayList<EntityInstance> ifcExtrudedAreaSolid = new ArrayList<>();
			for(String flag : getIdentifierTags(SweptSolidRepresentationTypeItems.IfcExtrudedAreaSolid.name())) {
				ifcExtrudedAreaSolid.addAll(ifcModel.getInstancesOfType(flag));
			}
			Vector<EntityInstance> ifcRevolvedAreaSolid = new Vector<>();
			for(String flag : getIdentifierTags(SweptSolidRepresentationTypeItems.IfcRevolvedAreaSolid.name())) {
				ifcRevolvedAreaSolid.addAll(ifcModel.getInstancesOfType(flag));
			}

			if(ifcExtrudedAreaSolid.contains(item))		return SweptSolidRepresentationTypeItems.IfcExtrudedAreaSolid.name();
			if(ifcRevolvedAreaSolid.contains(item))		return SweptSolidRepresentationTypeItems.IfcRevolvedAreaSolid.name();
			Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + item.getEntityDefinition() + " is not supported");
			return null;
		}

		if(ident.getType().equals(RepresentationType.BoundingBox)) {
			// here IFC standard only allows IFCBOUNDINGBOX as item

			// get all IFCBOUNDINGBOX objects and check if item is part of it
			for(String flag : getIdentifierTags(BoundingBoxRepresentationTypeItems.IfcBoundingBox.name())) {
				if(ifcModel.getInstancesOfType(flag).contains(item)){
					return BoundingBoxRepresentationTypeItems.IfcBoundingBox.name();
				}
			}
			Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + item.getEntityDefinition() + " is not supported");
			return null;
		}

		if(ident.getType().equals(RepresentationType.MappedRepresentation)) {
			// here IFC standard only allows IFCMAPPEDITEM as item

			// get all IFCMAPPEDITEM objects and check if item is part of it
			for(String flag : getIdentifierTags(MappedRepresentatiobTypeItems.IfcMappedItem.name())) {
				if(ifcModel.getInstancesOfType(flag).contains(item)){
					return MappedRepresentatiobTypeItems.IfcMappedItem.name();
				}
			}
			Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + item.getEntityDefinition() + " is not supported");
			return null;
		}

		Logging.info(IFCShapeRepresentationIdentifier.class.getName() + ": " + ident.getType() + " RepresentationType is not supported");
		return null;
	}

	/**
	 * Removes unnecessary chars from representation attribute string
	 * @param attribute representation attribute (REPRESENTATIONIDENTIFIER, REPRESENTATIONTYPE)
	 * @return prepared string
	 */
	private static String prepareRepresentationAttribute(String attribute) {
		return attribute.substring(1, attribute.length()-1);
	}

	/**
	 * Returns array of strings including identifier in origin version, uppercase and lowercase
	 * @param identifier string
	 * @return array with versions of identifier
	 */
	private static ArrayList<String> getIdentifierTags(String identifier){
		ArrayList<String> idents = new ArrayList<>();
		idents.add(identifier);
		idents.add(identifier.toLowerCase());
		idents.add(identifier.toUpperCase());
		return idents;
	}

}

