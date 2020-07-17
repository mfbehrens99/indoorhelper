// License: GPL. For details, see LICENSE file.
package parser.helper;

import java.util.ArrayList;
import java.util.Vector;

import org.openstreetmap.josm.tools.Logging;

import nl.tue.buildingsmart.express.population.EntityInstance;
import nl.tue.buildingsmart.express.population.ModelPopulation;
import parser.data.Point3D;
import parser.data.ifc.IFCShapeRepresentationIdentity;
import parser.helper.IFCShapeRepresentationCatalog.AdvancedBrepRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.AdvancedSweptSolidRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.BoundingBoxRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.BrepRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.CSGRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.ClippingRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.CurveRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.IfcBooleanOperandType;
import parser.helper.IFCShapeRepresentationCatalog.IfcBoundedCurveTypes;
import parser.helper.IFCShapeRepresentationCatalog.LoopSubRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.MappedRepresentatiobTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.ProfileDefRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.SurfaceModelRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.SweptSolidRepresentationTypeItems;
import parser.helper.IFCShapeRepresentationCatalog.TessellationRepresentationTypeItems;

/**
 * Class helps parsing BIM data with providing methods to extract OSM relevant data
 * @author rebsc
 *
 */
public class IFCShapeDataExtractor {

	public static Point3D defaultPoint = new Point3D(-99.0, -99.0, -99.0);

	/**
	 * Extract representation data from IFCREPRESENTATIONITEM body
	 * @param ifcModel ifc Model
	 * @param bodyRepresentation representation of body
	 * @return List of points representing object shape or null if object type not supported
	 */
	public static ArrayList<Point3D> getDataFromBodyRepresentation(ModelPopulation ifcModel, IFCShapeRepresentationIdentity bodyRepresentation) {
		ArrayList<Point3D> shapeRep = new ArrayList<>();

		// get IFCOBJECT and REPRESENTATIONIDENTIFIER
		EntityInstance repObject = bodyRepresentation.getRepresentationObjectEntity();

		// get IFCREPRESENTATIONITEMS
		ArrayList<EntityInstance> bodyItems = repObject.getAttributeValueBNasEntityInstanceList("Items");

		// extract informations from IfcRepresentationItems
		for(EntityInstance item : bodyItems) {
			// get type of item
			String repItemType = IFCShapeRepresentationIdentifier.getRepresentationItemType(ifcModel, bodyRepresentation, item);
			if(repItemType == null) return null;

			// handle types
			if(repItemType.equals(AdvancedBrepRepresentationTypeItems.IfcAdvancedBrep.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(AdvancedSweptSolidRepresentationTypeItems.IfcSweptDiskSolid.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(AdvancedSweptSolidRepresentationTypeItems.IfcSweptDiskSolidPolygonal.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(BrepRepresentationTypeItems.IfcFacetedBrep.name())) {
				return getShapeDataFromIfcFacetedBrep(ifcModel, item);
			}
			else if(repItemType.equals(CSGRepresentationTypeItems.IfcBooleanResult.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(CSGRepresentationTypeItems.IfcCsgSolid.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(CSGRepresentationTypeItems.IfcPrimitive3D.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(TessellationRepresentationTypeItems.IfcTessellatedFaceSet.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(ClippingRepresentationTypeItems.IfcBooleanClippingResult.name())) {
				return getShapeDataFromIfcBooleanResult(ifcModel, item, IfcBooleanOperator.DIFFERENCE);
			}
			else if(repItemType.equals(SurfaceModelRepresentationTypeItems.IfcTessellatedItem.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(SurfaceModelRepresentationTypeItems.IfcShellBasedSurfaceModel.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(SurfaceModelRepresentationTypeItems.IfcFaceBasedSurfaceModel.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(SurfaceModelRepresentationTypeItems.IfcFacetedBrep.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(SweptSolidRepresentationTypeItems.IfcExtrudedAreaSolid.name())) {
				return getShapeDataFromIfcExtrudedAreaSolid(ifcModel, item);
			}
			else if(repItemType.equals(SweptSolidRepresentationTypeItems.IfcRevolvedAreaSolid.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(MappedRepresentatiobTypeItems.IfcMappedItem.name())) {
				// TODO extract data
			}
		}

		return shapeRep;
	}

	/**
	 * Extract representation data from IfcRepresentationItem box
	 * @param ifcModel ifc Model
	 * @param boxRepresentation representation of box
	 * @return List of points representing object shape or null if object type not supported
	 */
	public static ArrayList<Point3D> getDataFromBoxRepresentation(ModelPopulation ifcModel, IFCShapeRepresentationIdentity boxRepresentation) {
		ArrayList<Point3D> shapeRep = new ArrayList<>();

		// get IFCObject and RepresentationIdentifier
		EntityInstance repObject = boxRepresentation.getRepresentationObjectEntity();

		// get IfcRepresentationItems
		ArrayList<EntityInstance> boxItems = repObject.getAttributeValueBNasEntityInstanceList("Items");

		// extract informations from IfcRepresentationItems
		for(EntityInstance item : boxItems) {
			// get type of IfcRepresentationItem
			String repItemType = IFCShapeRepresentationIdentifier.getRepresentationItemType(ifcModel, boxRepresentation, item);
			if(repItemType == null) return null;

			if(repItemType.equals(BoundingBoxRepresentationTypeItems.IfcBoundingBox.name())) {
				// TODO extract data
			}
		}

		return shapeRep;
	}

	/**
	 * Extract representation data from IfcRepresentationItem axis
	 * @param ifcModel ifc Model
	 * @param axisRepresentation representation of axis
	 * @return List of points representing object shape or null if object type not supported
	 */
	static ArrayList<Point3D> getDataFromAxisRepresentation(ModelPopulation ifcModel, IFCShapeRepresentationIdentity axisRepresentation) {
		ArrayList<Point3D> shapeRep = new ArrayList<>();

		// get IFCObject and RepresentationIdentifier
		EntityInstance repObject = axisRepresentation.getRepresentationObjectEntity();

		// get IfcRepresentationItems of object
		ArrayList<EntityInstance> axisItems = repObject.getAttributeValueBNasEntityInstanceList("Items");

		// extract informations from IfcRepresentationItems
		for(EntityInstance item : axisItems) {
			// get type of IfcRepresentationItem
			String repItemType = IFCShapeRepresentationIdentifier.getRepresentationItemType(ifcModel, axisRepresentation, item);
			if(repItemType == null) return null;

			if(repItemType.equals(CurveRepresentationTypeItems.IfcBoundedCurve.name())) {
				// TODO extract data
			}
			else if(repItemType.equals(CurveRepresentationTypeItems.IfcPolyline.name())) {
				// TODO extract data
			}
		}

		return shapeRep;
	}

	/**
	 * Method extracts shape representation coordinates from IFCFACETEDBREP object
	 * @param ifcModel ifc model
	 * @param faceBrepItem to get shape representation coordinates for
	 * @return points representing shape of IFCFACETEDBREP
	 */
	private static ArrayList<Point3D> getShapeDataFromIfcFacetedBrep(ModelPopulation ifcModel, EntityInstance faceBrepItem) {
		// get IFCCLOSEDSHELL stored in IFCFACETEDBREP.OUTER
		EntityInstance closedShell = faceBrepItem.getAttributeValueBNasEntityInstance("Outer");
		return getShapeDataFromIfcClosedShell(ifcModel, closedShell);

	}

	/**
	 * Method extracts shape representation coordinates from IFCCLOSEDSHELL object
	 * @param ifcModel ifc model
	 * @param shellItem to get shape representation coordinates for
	 * @return points representing shape of IFCCLOSEDSHELL
	 */
	private static ArrayList<Point3D> getShapeDataFromIfcClosedShell(ModelPopulation ifcModel, EntityInstance shellItem){
		// get IFCFACEs of IFCCLOSEDSHELL
		ArrayList<EntityInstance> facesOfClosedShell = shellItem.getAttributeValueBNasEntityInstanceList("CfsFaces");

		// get IFCFACEBOUNDs of every IFCFACE
		ArrayList<EntityInstance> faceBoundsOfClosedShell = new ArrayList<>();
		facesOfClosedShell.forEach(face->{
			faceBoundsOfClosedShell.addAll(face.getAttributeValueBNasEntityInstanceList("Bounds"));
		});

		// get IFCLOOPs of every IFCFACEBOUND
		ArrayList<EntityInstance> loopsOfClosedShell = new ArrayList<>();
		faceBoundsOfClosedShell.forEach(bound->{
			loopsOfClosedShell.addAll(bound.getAttributeValueBNasEntityInstanceList("Bound"));
		});

		// collect points of IFCLOOPs
		ArrayList<Point3D> shapePoints = new ArrayList<>();
		for(EntityInstance loop : loopsOfClosedShell) {
			ArrayList<Point3D> pointsOfLoop = getShapeDataFromIfcLoop(ifcModel, loop);
			if(pointsOfLoop == null)	return null;
			// workaround: Add points of every loop to shapePoints but also add a default point after each loop as separator (needed later on for rendering)
			shapePoints.addAll(pointsOfLoop);
			shapePoints.add(defaultPoint);
		}

		return shapePoints;
	}

	/**
	 * Method extracts shape representation coordinates from IFCLOOP object
	 * @param ifcModel ifc model
	 * @param loop to get shape representation coordinates for
	 * @return points representing shape of IFCLOOP
	 */
	private static ArrayList<Point3D> getShapeDataFromIfcLoop(ModelPopulation ifcModel, EntityInstance loop){
		// get loop type
		String loopType = IFCShapeRepresentationIdentifier.getIFCLoopType(ifcModel, loop);
		if(loopType == null)	return null;

		if(loopType.equals(LoopSubRepresentationTypeItems.IfcPolyLoop.name())) {
			// get all IFCCARTESIANPOINTs
			ArrayList<Point3D> cartesianPointsOfClosedShell = new ArrayList<>();
			for(EntityInstance cPoint : loop.getAttributeValueBNasEntityInstanceList("Polygon")){
				Point3D cPointAsPoint3D	= IfcCartesianCoordinateToPoint3D(cPoint);
				if(cPointAsPoint3D == null)	return null;
				cartesianPointsOfClosedShell.add(cPointAsPoint3D);
			}
			return cartesianPointsOfClosedShell;
		}
		// other loop types are not supported right now
		return null;
	}

	/**
	 * Extracts coordinate data from IFCBOOLEANRESULT. If IFCBOOLEANRESULT holds operands of type IFCBOOLEANRESULT it will
	 * recursive run thru every operation.
	 * @param ifcModel ifc model
	 * @param resultEntity to get coordinates from
	 * @param operator IFCBOOLEANOPERATOR
	 * @return Extracts coordinate data from IFCBOOLEANRESULT
	 */
	private static ArrayList<Point3D> getShapeDataFromIfcBooleanResult(ModelPopulation ifcModel, EntityInstance resultEntity, IfcBooleanOperator operator) {
		// get and identify both operands
		EntityInstance operand1 = resultEntity.getAttributeValueBNasEntityInstance("FirstOperand");
		EntityInstance operand2 = resultEntity.getAttributeValueBNasEntityInstance("SecondOperand");

		// extract shape data from operands
		ArrayList<Point3D> pointsOfOperand1 = getShapeDataFromBooleanOperand(ifcModel, operand1);
		ArrayList<Point3D> pointsOfOperand2 = getShapeDataFromBooleanOperand(ifcModel, operand2);
		if(pointsOfOperand1 == null || pointsOfOperand2 == null)	return null;

		// do operation
		if(operator.equals(IfcBooleanOperator.DIFFERENCE)) {
			ArrayList<Point3D> pointsOfOperand1Copy = pointsOfOperand1;
			for(Point3D point1 : pointsOfOperand1) {
				for(Point3D point2 : pointsOfOperand2) {
					if(point1.equalsPoint3D(point2)) {
						pointsOfOperand1Copy.remove(point1);
					}
				}
			}
			return pointsOfOperand1Copy;
		}
		if(operator.equals(IfcBooleanOperator.INTERSECTION)) {
			// TODO implement
		}
		if(operator.equals(IfcBooleanOperator.UNION)) {
			// TODO implement
		}

		return null;
	}

	/**
	 * Method extracts shape data from boolean operand. For this the operand will be identified and
	 * handled dependent on type.
	 * @param ifcModel ifc model
	 * @param operand to get shape data from
	 * @return points representing shape of operand
	 */
	private static ArrayList<Point3D> getShapeDataFromBooleanOperand(ModelPopulation ifcModel, EntityInstance operand){
		String operandType = IFCShapeRepresentationIdentifier.getIfcBooleanOperandType(ifcModel, operand);

		if(operandType == null)	return null;

		if(operandType.equals(IfcBooleanOperandType.IfcSolidModel.name())) {

		}
		if(operandType.equals(IfcBooleanOperandType.IfcHalfSpaceSolid.name())) {

		}
		if(operandType.equals(IfcBooleanOperandType.IfcPolygonalBoundedHalfSpace.name())) {
			return getShapeDataFromIfcPolygonalBoundedHalfSpace(ifcModel, operand);
		}
		if(operandType.equals(IfcBooleanOperandType.IfcBooleanResult.name()) || operandType.equals(IfcBooleanOperandType.IfcBooleanClippingResult.name())) {
			String operand1Operator = (String) operand.getAttributeValueBN("Operator");
			if(operand1Operator.equals("." + IfcBooleanOperator.DIFFERENCE + "."))
				return getShapeDataFromIfcBooleanResult(ifcModel, operand, IfcBooleanOperator.DIFFERENCE);
			if(operand1Operator.equals("." + IfcBooleanOperator.INTERSECTION + "."))
				return getShapeDataFromIfcBooleanResult(ifcModel, operand, IfcBooleanOperator.INTERSECTION);
			if(operand1Operator.equals("." + IfcBooleanOperator.UNION + "."))
				return getShapeDataFromIfcBooleanResult(ifcModel, operand, IfcBooleanOperator.UNION);
		}
		if(operandType.equals(IfcBooleanOperandType.IfcCsgPrimitive3D.name())) {

		}
		if(operandType.equals(SweptSolidRepresentationTypeItems.IfcExtrudedAreaSolid.name())) {
			return getShapeDataFromIfcExtrudedAreaSolid(ifcModel, operand);
		}
		if(operandType.equals(IfcBooleanOperandType.IfcFacetedBrep.name())) {
			return getShapeDataFromIfcFacetedBrep(ifcModel, operand);
		}
		// other types are not supported right now
		return null;
	}

	/**
	 *  Method extracts shape representation coordinates from IFCPOLYGONALBUNDEDHALFSPACE object
	 * @param ifcModel ifc model
	 * @param polygon object to get shape coordinates from
	 * @return points representing shape of IFCPOLYGONALBUNDEDHALFSPACE
	 */
	private static ArrayList<Point3D> getShapeDataFromIfcPolygonalBoundedHalfSpace(ModelPopulation ifcModel, EntityInstance polygon){
		// TODO handle rotation to parent system

		// get local origin position
		EntityInstance localSystemPosition = polygon.getAttributeValueBNasEntityInstance("Position");
		EntityInstance locationPoint = localSystemPosition.getAttributeValueBNasEntityInstance("Location");
		Point3D locationPoint3D = IfcCartesianCoordinateToPoint3D(locationPoint);
		if(locationPoint3D == null)	return null;

		// get boundary
		EntityInstance localPolygonBoundary = polygon.getAttributeValueBNasEntityInstance("PolygonalBoundary");
		String localPolygonBoundaryType = IFCShapeRepresentationIdentifier.getIfcBoundedCurveType(ifcModel, localPolygonBoundary);

		// get coordinates of boundary
		if(localPolygonBoundaryType.equals(IfcBoundedCurveTypes.IfcPolyline.name())) {
			ArrayList<Point3D> pointsOfPolyline = getCoordinatesFromIfcPolyline(localPolygonBoundary);
			pointsOfPolyline.forEach(point->{
				point = new Point3D(locationPoint3D.getX()+ point.getX(), locationPoint3D.getY() + point.getY(), 0.0);
			});
			return pointsOfPolyline;
		}

		// other types are not supported right now
		return null;
	}

	/**
	 * Method extracts shape representation coordinates from IFCEXTRUDEDAREASOLID object
	 * @param ifcModel ifc model
	 * @param extrudedArea to get shape representation for
	 * @return points representing shape of IFCEXTRUDEDAREASOLID
	 */
	private static ArrayList<Point3D> getShapeDataFromIfcExtrudedAreaSolid(ModelPopulation ifcModel, EntityInstance extrudedArea) {
		// get POSITION attribute and extract local object origin coordinates
		EntityInstance axisPlacement = extrudedArea.getAttributeValueBNasEntityInstance("Position");
		EntityInstance locationPoint = axisPlacement.getAttributeValueBNasEntityInstance("Location");
		//object axis origin
		Point3D locationPoint3D = IfcCartesianCoordinateToPoint3D(locationPoint);
		if(locationPoint3D == null)	return null;

		// get IFCPROFILEDEF attribute
		EntityInstance profileDef = extrudedArea.getAttributeValueBNasEntityInstance("SweptArea");
		// handle different SweptArea types
		String sweptAreaType = IFCShapeRepresentationIdentifier.getIFCProfileDefType(ifcModel, profileDef);
		if(sweptAreaType == null)	return null;

		if(sweptAreaType.equals(ProfileDefRepresentationTypeItems.IfcRectangleProfileDef.name())) {
			// extract XDim, YDim
			double xDim = prepareDoubleString((String)profileDef.getAttributeValueBN("XDim"));
			double yDim = prepareDoubleString((String)profileDef.getAttributeValueBN("YDim"));
			double halfxDim = xDim/2.0;
			double halfyDim = yDim/2.0;

			// get points of shape
			ArrayList<Point3D> cartesianPointsOfSArea = new ArrayList<>();
			cartesianPointsOfSArea.add(new Point3D(locationPoint3D.getX()-halfxDim, locationPoint3D.getY()-halfyDim, 0.0));
			cartesianPointsOfSArea.add(new Point3D(locationPoint3D.getX()+halfxDim, locationPoint3D.getY()-halfyDim, 0.0));
			cartesianPointsOfSArea.add(new Point3D(locationPoint3D.getX()+halfxDim, locationPoint3D.getY()+halfyDim, 0.0));
			cartesianPointsOfSArea.add(new Point3D(locationPoint3D.getX()-halfxDim, locationPoint3D.getY()+halfyDim, 0.0));
			cartesianPointsOfSArea.add(new Point3D(locationPoint3D.getX()-halfxDim, locationPoint3D.getY()-halfyDim, 0.0));
			return cartesianPointsOfSArea;
		}
		if(sweptAreaType.equals(ProfileDefRepresentationTypeItems.IfcArbitraryClosedProfileDef.name())) {
			String profileType = (String)profileDef.getAttributeValueBN("ProfileType");

			if(profileType.equals(".AREA.")) {
				// extract polyline coordinates
				EntityInstance outerCurve = profileDef.getAttributeValueBNasEntityInstance("OuterCurve");

				// check if curve is represented by polyline
				if(IFCShapeRepresentationIdentifier.isIfcPolyline(ifcModel, outerCurve)) {
					// extract coordinates
					ArrayList<Point3D> pointsOfPolyline = getCoordinatesFromIfcPolyline(outerCurve);
					pointsOfPolyline.forEach(point ->{
						point = new Point3D(locationPoint3D.getX()+ point.getX(), locationPoint3D.getY() + point.getY(), 0.0);
					});
					return pointsOfPolyline;
				}
			}
			if(profileType.equals(".CURVE.")) {
				// not supported right now
			}
		}
		// other types are not supported right now
		return null;
	}

	/**
	 * Method extracts local coordinates of polyline
	 * @param polyline o get coordinates from
	 * @return coordinates of polyline (local)
	 */
	private static ArrayList<Point3D> getCoordinatesFromIfcPolyline(EntityInstance polyline){
		ArrayList<EntityInstance> points = polyline.getAttributeValueBNasEntityInstanceList("Points");
		ArrayList<Point3D> cartesianPointsOfSArea = new ArrayList<>();
		points.forEach(point ->{
			Point3D pointAsPoint3D = IfcCartesianCoordinateToPoint3D(point);
			cartesianPointsOfSArea.add(new Point3D(pointAsPoint3D.getX(),pointAsPoint3D.getY(), 0.0));
		});
		return cartesianPointsOfSArea;
	}

	/**
	 * Transforms IFCCARTESIANCOORDINATE entity into Point3D
	 * @param cartesianCoordinate to transform
	 * @return coordinate as Point3D
	 */
	private static Point3D IfcCartesianCoordinateToPoint3D(EntityInstance cartesianCoordinate) {
		@SuppressWarnings("unchecked")
		Vector<String> objectCoords = (Vector<String>)cartesianCoordinate.getAttributeValueBN("Coordinates");
		if(objectCoords.isEmpty())	return null;
		double x = prepareDoubleString(objectCoords.get(0));
		double y = prepareDoubleString(objectCoords.get(1));
		double z = 0.0;
		if(objectCoords.size() == 3) 	prepareDoubleString(objectCoords.get(2));
		if(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
			return null;
		}
		return new Point3D(x, y, z);
	}

	/**
	 * Parses string of double value from IFC file into proper double
	 * @param doubleString String of coordinate
	 * @return double representing double
	 */
	private static double prepareDoubleString(String doubleString) {
		if(doubleString.endsWith(".")) {
			doubleString = doubleString + "0";
		}
		try {
			return Double.parseDouble(doubleString);
		}catch(NumberFormatException e) {
			Logging.error(e.getMessage());
			return Double.NaN;
		}
	}

	/**
	 * This type defines the three Boolean operators used in the definition of CSG solids.
	 *
	 */
	private enum IfcBooleanOperator{
		UNION,
		INTERSECTION,
		DIFFERENCE
	}


}