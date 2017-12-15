import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.util.AffineTransformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Rares on 13.12.2017.
 */
public class GreedyCorner {
    public LinkedList<Coordinate> queue = new LinkedList<Coordinate>();
    Parser p;
    HashMap<Geometry, Integer> result;
    ArrayList<Geometry> resultArray;
    Polygon room;
    GeometryFactory geometryFactory;
    HashMap<Polygon, Double> polTransformed;
    HashMap<Polygon, Integer> remaining;
    ArrayList<Polygon> figuresInput;

    public GreedyCorner(Figure room, ArrayList<Figure> figures){
        //p.readLines();
        queue = new LinkedList<>();
        result = new HashMap<>();
        geometryFactory = new GeometryFactory();
        this.room = transformType(room);
        polTransformed = new HashMap<>();
        resultArray = new ArrayList<>();
        figuresInput = new ArrayList<>();
        remaining = new HashMap<>();
        for(Figure f : figures){
            Polygon fig = transformType(f);
            polTransformed.put(fig,f.cost);
            remaining.put(fig, remaining.getOrDefault(fig,0) + 1);
            figuresInput.add(fig);
        }

        //figuresInput = polFig;
        /*for(Polygon poliG : polFig){

            remaining.put(poliG, remaining.getOrDefault(poliG,0) + 1);
            //figuresInput.add(fig);
        }*/
        for(Point p : room.points){
            queue.addLast(new Coordinate(p.x_coordinate, p.y_coordinate));
        }
       // System.out.println(remaining.size() + " " + polTransformed.size());
    }

    public void cross(){
        figuresInput.sort((o1,o2) -> {
                    double Area = o1.getArea()*polTransformed.get(o1) ;
                    double Area2 = o2.getArea()*polTransformed.get(o2);
                    return -Double.compare(Area,Area2) ;
                }
        ) ;

        System.out.println("room " + room);
        Coordinate corner;
        while(queue.size() != 0){
            corner = queue.removeFirst();
            //choose area
            //System.out.println(corner.x_coordinate + " " + corner.y_coordinate);
            Geometry newPos = Maximize(corner);
            if(newPos != null) {
                result.put(newPos, 0);
                resultArray.add(newPos);
                //System.out.println(newPos);
                Coordinate[] coor = newPos.getCoordinates();
                Point newPoint;
               /* for (int i = 0; i < coor.length - 1; i++) {
                    //newPoint = new Point(coor[i].x, coor[i].y);
                    queue.addLast(coor[i]);
                }*/
            }
        }
    }

    private Geometry Maximize(Coordinate c){
        //will be sorted by area and unit cost
        Geometry newTranslated;
        Geometry rotation = null;
        int ok;
        for(Polygon current : figuresInput){
            if(remaining.containsKey(current)) {
                System.out.println(current);
                ok = 0;
                //newTranslated = translate(corner.x_coordinate, corner.y_coordinate, current);
            /*while (ok <= 4){
                rotation = rotate((Math.PI/2)*ok, newTranslated);
                //System.out.println(rotation);
                if(!intersects(newTranslated) && room.contains(newTranslated))
                    return rotation;
                ok++;
            }*/
                //System.out.println(current);
                Coordinate[] coor = current.getCoordinates();
                for(int i = 0; i < coor.length - 1; i++){
                newTranslated = translate(c, current,coor[i]);
                //System.out.println(newTranslated);
                if (!intersects(newTranslated) && room.contains(newTranslated)) {

                    remaining.put(current, remaining.get(current) - 1);
                    if(remaining.get(current) == 0)
                        remaining.remove(current);
                    return newTranslated;
                }
               }
           }
        }

        return null;
    }

    private boolean intersects(Geometry current){
        for(Geometry fKey : result.keySet()){
          //  System.out.println(fKey);
            if(current.intersects(fKey))
                return true;
        }
        return false;
    }

    private Figure transformCoordinateFigure(Geometry f){
        int i;
        ArrayList<Point> points = new ArrayList<>();
        Coordinate [] coordinates = f.getCoordinates();
        for(i  = 0; i < coordinates.length - 1; i++){
           points.add(new Point(coordinates[i].x, coordinates[i].y));
            //System.out.println(coordinates[i].x + "vs " + points.get(i).x_coordinate + " " + coordinates[i].y + "vs " + points.get(i).y_coordinate);
        }
       // coordinates[i] =  (new Coordinate(points.get(0).x_coordinate, points.get(0).y_coordinate));

        return new Figure(0,points);
    }

    public Coordinate[] transformCoordinates(Figure f){
        int i;
        ArrayList<Point> points = f.points;
        Coordinate [] coordinates = new Coordinate[points.size() + 1];
        for(i  = 0; i < points.size(); i++){
            coordinates[i] =  (new Coordinate(points.get(i).x_coordinate, points.get(i).y_coordinate));
            //System.out.println(coordinates[i].x + "vs " + points.get(i).x_coordinate + " " + coordinates[i].y + "vs " + points.get(i).y_coordinate);
        }
        coordinates[i] =  (new Coordinate(points.get(0).x_coordinate, points.get(0).y_coordinate));

        return coordinates;
    }

    private Polygon transformType(Figure f){
        Coordinate[] coordinates = transformCoordinates(f);
        Polygon polygon = geometryFactory.createPolygon(coordinates);

        return polygon;
    }


    //Use math.PI
    public Geometry rotate(Double theta, Geometry g){
        Coordinate[]first = g.getCoordinates();
        //double x = first[0].x;
        //double y = first[0].y;
        AffineTransformation trans = new AffineTransformation();
        trans.setToRotation(theta,0,0);
        Geometry geom = trans.transform(g);
        geom.apply(trans);
        return geom;
    }

    // directly modifies the object so need to create new ones
    public static Geometry translate(Coordinate C, Geometry g, Coordinate newP){
        AffineTransformation trans = new AffineTransformation();
        trans.translate(C.x - newP.x ,C.y - newP.y);
        Geometry geom = trans.transform( g);
        geom.apply(trans);
        return geom;
    }

}
