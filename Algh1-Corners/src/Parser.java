
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Rares on 11.12.2017.
 */
public class Parser {
    ArrayList<Point> points = new ArrayList<>();
    Figure roomF;
    ArrayList<Figure> figs = new ArrayList<>();
    BufferedReader buffer;
    //HashMap<Figure,Integer> cost = new HashMap<>();
    GeometryFactory geometryFactory;
    public Parser(){
        Scanner scan;
        File file = new File("src/input.txt");
        try {
            InputStream in = new FileInputStream(file);
            Reader reader = new InputStreamReader(in);
            // buffer for efficiency
            buffer = new BufferedReader(reader);
            handleCharacters();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

    }
    // metoda asta ii modificata
    public void parse(String line){
        int i = 0,j;
        figs = new ArrayList<>();
        String[] delimiter = line.split("#");
        String room = delimiter[0];
        String[] figures = delimiter[1].split(";");
        //split points
        roomF = splitDouble(room);
        for(String f : figures){
           // System.out.println(f);
            figs.add(splitDouble(f));
           //polig.add(parsePolygone(f));
        }

    }
    //new method
    public Figure splitDouble(String f){
        int i = 0,j;
        i = 0;
        ArrayList<Point> points = new ArrayList<>();
        while(i < f.length()){
            if(f.charAt(i) == '('){
                j = i;
                while( f.charAt(j) != ',')
                    j++;
                double x = Double.parseDouble(f.substring(i+1, j));
                //System.out.print( " " + x + " ");
                i = j + 1;
                while( f.charAt(j) != ')')
                    j++;
                double y = Double.parseDouble(f.substring(i, j));
                //System.out.print(y);
                i = j;
                Point p = new Point(x,y);
                points.add(p);
            }
            i++;
        }
        //System.out.println();
        return new Figure((double)(f.charAt(0)),points);
    }
    public void handleCharacters() {
        try {
            String line;
            while ((line = buffer.readLine()) != null) {
                parse(line);
                break;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
