import java.util.ArrayList;

public class Figure {
    double cost;
    double area;
    ArrayList<Point> points;


    public Figure(double cost, ArrayList<Point> points){
        this.cost = cost;
        this.points = new ArrayList<Point>(points);
    }

    public Point getCenter(){
        return getCenter(points);
    }

    private Point getCenter(ArrayList<Point> points){
        double x_coordinate = 0;
        double y_coordinate = 0;
        for(Point point : points ){
            x_coordinate = x_coordinate + point.x_coordinate;
            y_coordinate = y_coordinate + point.y_coordinate;
        }
        x_coordinate = x_coordinate/ points.size();
        y_coordinate = y_coordinate/points.size();

        Point center = new Point(x_coordinate, y_coordinate);
        return center;
    }

    public double getRadius(){
        double radius = 0;
        Point center = getCenter();
        for(Point point : points){
            double arg1 = Math.pow(point.x_coordinate-center.x_coordinate,2);
            double arg2 = Math.pow(point.y_coordinate-center.y_coordinate,2);
            if(Math.sqrt(arg1 + arg2)  > radius)
                radius = Math.sqrt(arg1 +arg2);
        }
        return radius;
    }

    public double getX_LengthOfFigureRect(){
        double maxLength = 0;
        double minLength = 0;
        for(Point point:points){
            if(point.x_coordinate > maxLength)
                maxLength = point.x_coordinate;
            if(point.x_coordinate < minLength)
                minLength = point.x_coordinate;
        }
        return maxLength - minLength;
    }

    public double getY_heightOfFigureRect(){
        double maxHeight = 0;
        double minHeight = 0;
        for(Point point:points){
            if(point.y_coordinate > maxHeight)
                maxHeight = point.x_coordinate;
            if(point.y_coordinate < minHeight)
                minHeight = point.y_coordinate;
        }
        return maxHeight - minHeight;
    }


}

