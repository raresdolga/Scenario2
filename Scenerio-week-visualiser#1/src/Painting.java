

import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;

import javax.print.attribute.standard.PrinterResolution;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.min;

/**
 * Created by Rares on 11.12.2017.
 */
public class Painting extends JPanel {
    public Polygon roomToFill;
    public ArrayList <Polygon> figures = new ArrayList<>();
    public Painting(Polygon roomToFill, ArrayList < Polygon > figures){
        this.roomToFill = roomToFill;
        this.figures = figures;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        int amp = 15;
        path.moveTo(0.0, 0.0);
        AffineTransform at = new AffineTransform();
        g2d.translate(getWidth() / 2, getHeight() / 2);
        g2d.scale(1, -1);
        g2d.transform(at);
        ArrayList <Coordinate> roomPoints = getPolygonPoints( roomToFill) ;
        ArrayList <Polygon> polygons = new ArrayList<>() ;
        polygons=figures ;
        polygons.sort((o1,o2) -> {
                      double Area = o1.getArea() ;
                      double Area2 = o2.getArea() ;
                      return Double.compare(Area,Area2) ;
                }
        ) ;
        ArrayList <Geometry> solution = Greedy ( polygons ) ;
        for (Coordinate roomPoint : roomPoints) {
            path.lineTo(roomPoint.x * amp, roomPoint.y*amp);
        }
        path.closePath();
        g2d.setColor(Color.red);
        g2d.fill(path);

        path.moveTo(0.0, 0.0);
        for (Coordinate roomPoint : roomPoints) {
            path.lineTo(roomPoint.x * amp, roomPoint.y * amp);
        }
        path.closePath();
        g2d.setColor(Color.red);
        g2d.fill(path);
        for ( Geometry sol : solution )
        {
            ArrayList <Coordinate> points = getPolygonPoints( sol ) ;
            path.moveTo(0.0, 0.0);
            for ( Coordinate solPoint : points )
            {
                path.lineTo( solPoint.x *amp,solPoint.y*amp );
            }
            path.closePath();
            g2d.setColor(Color.green);
            g2d.fill(path);
        }
        write ( solution ) ;
    }
    public ArrayList <Geometry > Greedy ( ArrayList < Polygon > a ) {
        ArrayList<Geometry> solution = new ArrayList<>(); // Solutia de umplere
        Double area = roomToFill.getArea() ;
        Double covered = new Double(0) ;
        for (Polygon polygon : a ) {
            boolean ok = true ;
            for ( double x = -80.0   ; x <= 80.0 && ok == true  ; x ++ )
            {
                for ( double y = -80.0 ; y <= 80.0 && ok == true ; y ++ )
                {

                    Geometry translation = translate(x / 30, y / 30 , polygon) ;
                    if (roomToFill.contains(translation) ) {
                        Boolean intersect = false;
                        for (Geometry polyg : solution) {
                            intersect |= polyg.intersects(translation);
                        }
                        if (intersect == false) {
                            ok = false;
                            solution.add(translation);
                        }
                    }
                }
            }
        }

        for ( Geometry polygon : solution )
        {
            covered += polygon.getArea() ;
        }
        double ratio = covered / area ;
        System.out.println ( ratio ) ;
        return solution ;
    }
    public ArrayList<Coordinate> getPolygonPoints ( Geometry a )
    {
        ArrayList < Coordinate > points = new ArrayList<>() ;
        Coordinate[] coordinates = a.getCoordinates() ;
        for ( Coordinate cord : coordinates )
        {
            points.add ( new Coordinate ( cord.x , cord.y ) ) ;
        }
        return points ;
    }
    public void printPolygonCoordonates ( Geometry a )
    {
        ArrayList <Coordinate > points = getPolygonPoints( a ) ;
        int i = 0 ;
        for ( i = 0 ; i < points.size() - 1 ; i++ )
        {
            Coordinate point = points.get ( i ) ;
            System.out.print ("(" + point.x + "," + point.y + ")," ) ;
        }
        Coordinate point = points.get ( i ) ;
        System.out.print ("(" + point.x + "," + point.y + ")" ) ;
        System.out.println(";") ;
    }

    public static Geometry translate(double x1, double y1, Geometry g){
        AffineTransformation trans = AffineTransformation.translationInstance(x1,y1);
        Geometry geom = trans.transform( g);
        geom.apply(trans);
        return geom;
    }
    private static void write(ArrayList<Geometry> geom){
        try {
            PrintWriter writer = new PrintWriter("src/output.txt", "UTF-8");
            Coordinate[] coord;
            int i = 0;
            for(Geometry g : geom){
                coord = g.getCoordinates();

                for(i = 0; i < coord.length - 2; i++){
                    writer.print(" (" + coord[i].x + ", " + coord[i].y + ")" + ",");
                }
                writer.print(" (" + coord[i].x + ", " + coord[i].y + ")" + ";");
            }

            writer.close();
        }
        catch (Exception e){

        }
    }
}
