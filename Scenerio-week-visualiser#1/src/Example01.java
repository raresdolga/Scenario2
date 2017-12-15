import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class Example01 extends Frame {
    public Figure roomToFill;
    public ArrayList<Figure> figures;

    public Example01(Figure roomToFill, ArrayList<Figure> figures){
        super("Java 2D Example01");
        setSize(1000,1000);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {dispose(); System.exit(0);}
        });

        this.roomToFill = roomToFill;
        this.figures = figures;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        int amp = 5;

        AffineTransform at = new AffineTransform();
        g2d.translate(getWidth() / 2, getHeight() / 2);
        g2d.scale(1, -1);
        g2d.transform(at);

        path.moveTo(0.0, 0.0);
        for (Point roomPoint : roomToFill.points) {
            path.lineTo(roomPoint.x_coordinate * amp, roomPoint.y_coordinate*amp);
        }
        path.closePath();
        g2d.setColor(Color.green);
        g2d.fill(path);

        for (Figure figure : figures) {
            path.moveTo(0.0, 0.0);
            for (Point point : figure.points) {
                path.lineTo(point.x_coordinate * amp, point.y_coordinate * amp);

            }
            path.closePath();
            g2d.setColor(Color.green);
            g2d.fill(path);
        }

//            path.moveTo(0.0, 0.0);
//            path.lineTo(10.0 * amp, 0.0 * amp);
//            path.lineTo(10.0 * amp, 10.0 * amp);
//            path.lineTo(0.0 * amp, 10.0 * amp);
//            path.closePath();

        g2d.setColor(Color.green);
        g2d.fill(path);
    }
}

