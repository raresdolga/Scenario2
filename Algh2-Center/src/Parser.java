import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Rares on 11.12.2017.
 */
public class Parser {
    BufferedReader buffer;
    Polygon roomF;
    //HashMap<Polygon,Double> figs = new HashMap<>();
    ArrayList<Polygon> figs = new ArrayList<>();
    GeometryFactory geometryFactory = new GeometryFactory();
    public Parser(){
        Scanner scan;
        File file = new File("src\\input.txt");
        try {
            InputStream in = new FileInputStream(file);
            Reader reader = new InputStreamReader(in);
            // buffer for efficiency
            buffer = new BufferedReader(reader);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

    }
    // metoda asta ii modificata
    public void parse(String line){
        figs = new ArrayList<>();
        int i = 0,j;
        String[] delimiter = line.split("#");
        String room = delimiter[0];
        System.out.println("room " + room.charAt(0) + room.charAt(1) );
        String[] figures = delimiter[1].split(";");
        //split points
        ArrayList<Polygon>dummy1 = new ArrayList<>();
        splitDouble(room, dummy1);
        // will have just one object
        for(Polygon poly : dummy1)
            roomF = poly;
        int nr = 0;
        for(String f : figures){
            splitDouble(f,figs);
            nr++;
        }


    }
    //new method
    public void splitDouble(String f, ArrayList<Polygon> map){
        int i = 0,j;
        i = 0;
        ArrayList<Coordinate> points = new ArrayList<Coordinate>();
        while(i < f.length()){
            if(f.charAt(i) == '('){
                j = i;
                while( f.charAt(j) != ',')
                    j++;
                double x = Double.parseDouble(f.substring(i+1, j));
                i = j + 1;
                while( f.charAt(j) != ')')
                    j++;
                double y = Double.parseDouble(f.substring(i, j));
                i = j;
                Coordinate p = new Coordinate(x,y);
                points.add(p);
            }
            i++;
        }
        Coordinate [] coor = transformCoordinates(points);
        LinearRing ring = geometryFactory.createLinearRing(coor);
        LinearRing holes[] = null; // use LinearRing[] to represent holes
        Polygon polygon = geometryFactory.createPolygon(ring, holes);
        //(double) f.charAt(0)
        map.add(polygon);
    }

    private Coordinate[] transformCoordinates(ArrayList<Coordinate> points){
        int i;

        Coordinate [] coordinates = new Coordinate[points.size() + 1];
        for(i  = 0; i < points.size(); i++){
            coordinates[i] =  (new Coordinate(points.get(i).x, points.get(i).y));
            //System.out.println(coordinates[i].x + "vs " + points.get(i).x_coordinate + " " + coordinates[i].y + "vs " + points.get(i).y_coordinate);
        }
        coordinates[i] =  (new Coordinate(points.get(0).x, points.get(0).y));

        return coordinates;
    }

    public boolean readNextLine() {
        try {
            String line = null;
            if((line = buffer.readLine()) != null) {
                parse(line);
                return true;
            }
            return false;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
