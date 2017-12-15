import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Rares on 11.12.2017.
 */
public class GUI extends JFrame implements ActionListener {
    JFrame frame;
    Button nextButton;
    Parser p;
    JPanel graphics;
    JPanel container;
    public GUI(){
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        frame = new JFrame("buttons");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(container);
        frame.setSize(250,250);
        frame.setVisible(true);
        nextButton = new Button("Next");
        nextButton.addActionListener(this);
        JPanel b = new JPanel();
        b.add(nextButton);
        container.add(b);
        p = new Parser();
        graphics = new JPanel();
        container.add(graphics);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == nextButton &&  p.readNextLine()){
            container.remove(graphics);
            graphics = new Painting(p.roomF,p.figs);
            container.add(graphics);
            container.revalidate();
            container.repaint();
        }
    }
}
