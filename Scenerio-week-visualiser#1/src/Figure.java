
import org.locationtech.jts.algorithm.MinimumDiameter;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;

public class Figure {
    double cost;
    ArrayList<Point> points;
    Coordinate[] coordinates;
    //cos 90 , sin 90
    private int[][] rotationMatrix = {{0,1}, {-1, 0}};

    public Figure(double cost, ArrayList<Point> points){
        this.cost = cost;
        this.points = new ArrayList<Point>(points);
        adaptLibrary();
    }

    public void rotate90(){
        for(Point p : points){
            matixMult(p);
        }
    }

    private void matixMult(Point p){
        double x = p.x_coordinate*rotationMatrix[0][0] + p.y_coordinate*rotationMatrix[0][1];
        double y = p.x_coordinate*rotationMatrix[1][0] + p.y_coordinate*rotationMatrix[1][0];
         p.x_coordinate = x;
         p.y_coordinate = y;
    }

    public void translate(double x, double y){
        for(Point p : points){
            p.x_coordinate += x;
            p.y_coordinate += y;
        }

    }
    public Geometry encapsulate(){
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing ring = geometryFactory.createLinearRing( coordinates );
        LinearRing holes[] = null; // use LinearRing[] to represent holes
        Polygon polygon = geometryFactory.createPolygon(ring, holes);
        MinimumDiameter mp = new MinimumDiameter( polygon);
        Geometry minEncaps = mp.getMinimumRectangle();
        return  minEncaps;
    }

    public void adaptLibrary(){
        coordinates = new Coordinate[points.size()];
        for(int i = 0; i < points.size(); i++){
            coordinates[i] =  (new Coordinate(points.get(i).x_coordinate, points.get(i).y_coordinate));
        }
    }

    public Point[] minPoint(){
        double xMin = 1000, yMin = 1000;
        double xMax = 0, yMax = 0;
        for(Point p : points){
            if(p.y_coordinate > yMax)
                yMax = p.y_coordinate;
            if(p.x_coordinate > xMax)
                xMax = p.x_coordinate;
            if(p.y_coordinate  < yMin)
                yMin = p.y_coordinate;
            if(p.x_coordinate < xMin)
                xMin = p.x_coordinate;
        }
        Point min = new Point(xMin, yMin);
        Point max = new Point(xMax, yMax);
        Point [] array = {min,max};
        return array;
    }
    public double computeArea ( )
    {
        double area = 0 ;
        ArrayList <Point> puncte = points  ;
        int len = puncte . size() ;
        for ( int i = 0 ; i < len - 1 ; i ++ )
        {
            double x = puncte . get ( i ) . x_coordinate ;
            double y = puncte . get ( i ) . y_coordinate ;
            double urm_x = puncte . get ( i + 1 ) . x_coordinate ;
            double urm_y = puncte . get ( i + 1 ) . y_coordinate ;
            area += ( ( x * urm_y - urm_x * y ) / 2 ) ;
        }
        return area ;
    }
}
