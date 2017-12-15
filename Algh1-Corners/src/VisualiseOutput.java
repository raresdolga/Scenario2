import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import processing.core.PApplet;
import processing.core.PShape;

import java.io.PrintWriter;
import java.util.ArrayList;

public class VisualiseOutput extends PApplet {
    ArrayList<PShape> listOfShapes = new ArrayList<>();
    PShape roomShape;


    public void settings(){
        size(1000,800);
    }
    public void setup(){
        Parser p = new Parser();
        //p.readLines();
        Figure myRoom = p.roomF;
        ArrayList<Figure> figures = p.figs;
        int amp = 5; // ai grija ca amplificatorul s-ar putea sa-ti futa

        pushMatrix();
        translate(width/3, height/3);
        scale(1, -1);

        /* ---------drawing the room-------------*/
        roomShape = createShape();
        roomShape.beginShape();
        for (Point currentPoint : myRoom.points) {
            roomShape.vertex((float)currentPoint.x_coordinate *amp, (float)currentPoint.y_coordinate *amp);
        }
        roomShape.endShape(CLOSE);
        roomShape.setFill(color(145, 69, 69));
        // shape(roomShape,(float) Math.random()*400,(float) Math.random()*400);
        /*---------------------------------------*/


        /* ------------drawing each figure---------------*/
        for (Figure figure : figures) {
            PShape figureShape = createShape();
            figureShape.beginShape();
            for (Point currentPoint : figure.points) {
                figureShape.vertex((float)currentPoint.x_coordinate *amp, (float)currentPoint.y_coordinate*amp);

            }
            figureShape.endShape(CLOSE);
            figureShape.setFill(color(138,223,40));
            listOfShapes.add(figureShape);

        }

        /*---------------------------------------------------------*/

    }
    public void draw(){
        pushMatrix();
        Parser p = new Parser();
        //p.readLines();
        Figure myRoom = p.roomF;
        ArrayList<Figure> figures = p.figs;

        translate(width/4, 3*height/4);
        scale(1, -1);
        shape(roomShape,0,0);
        int i = 0;
        for(PShape figureShape:listOfShapes){
            float r = (float)Math.random() * 244;
            float g = (float)Math.random() * 244;
            float b = (float)Math.random() * 244;
            figureShape.setFill(color(r,g,b));
            shape(figureShape,(float) p.figs.get(i).points.get(0).x_coordinate,(float)p.figs.get(i).points.get(0).y_coordinate);
            i++;
        }

        popMatrix();
        noLoop();
    }

    public void mousePressed(){
        //background(64);
    }
    public static void main(String[] args){
        String[] processingArgs = {"MySketch"};
        VisualiseOutput visualise = new VisualiseOutput();

        /* parsing and collecting input */
        Parser p = new Parser();
        Figure myRoom = null;
        ArrayList<Figure> figures = null;
        ArrayList<Polygon> parseNew = null;
        GreedyCorner gC;
        for(int i = 1; i <= 9; i++){
            p.handleCharacters();
            /*myRoom = p.roomF;
            //figures = p.figs;
            gC = new GreedyCorner(myRoom,figures,parseNew);
            gC.cross();
            for(Geometry g : gC.resultArray){
                System.out.println(g);
            }
            write(gC.resultArray);
            //System.out.println(myRoom);
            */
        }

        p.handleCharacters();
        myRoom = p.roomF;
        figures = p.figs;
        //parseNew = p.polig;
        gC = new GreedyCorner(myRoom,figures);
        gC.cross();
        for(Geometry g : gC.resultArray){
            System.out.println(g);
        }
        write(gC.resultArray);
        /* ------------------------------- */



        /* --------------------------------------- */
        PApplet.runSketch(processingArgs, visualise);
        //PApplet.main("MySketch", args);
    }
    private static void write(ArrayList<Geometry> geom){
        try {
            PrintWriter writer = new PrintWriter("src/output.txt", "UTF-8");
            Coordinate[] coord;
            int i = 0;
            writer.print("1: ");
            for(Geometry g : geom){

                coord = g.getCoordinates();
                for(i = 0; i < coord.length - 2; i++){
                    writer.print("(" + coord[i].x + ", " + coord[i].y + ")" + ",");
                }
                writer.print("(" + coord[i].x + ", " + coord[i].y + ")" + ";");
            }
            System.out.println();
            writer.close();
        }
        catch (Exception e){

        }
    }

}

