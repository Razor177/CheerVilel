import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Random;

/* [MatrixDisplayWithMouse.java]
 * A small program showing how to use the MatrixDisplayWithMouse class
 * @author Mangat
 */



class MatrixDisplayWithMouse extends JFrame {

    int maxX,maxY, GridToScreenRatio;
    Tile[][] matrix;
    Game gg;


    /**
     * MatrixDisplayWithMouse
     * sets up a panal with mouse and will show the game to user
     * @param title - the title
     * @param game - the game object
     */
    MatrixDisplayWithMouse(String title, Game game) {

        super(title);

        this.matrix = game.getMap();
        gg = game;

        maxX = Toolkit.getDefaultToolkit().getScreenSize().width; // creates the max x
        maxY = Toolkit.getDefaultToolkit().getScreenSize().height; // creates the may y
        GridToScreenRatio = maxY / (matrix.length+1);  //ratio to fit in screen as square map

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // makes it so that u can close Jframe
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        this.add(new MatrixPanel());

        this.setVisible(true); // makes it visable
    }

    /**
     * refresh
     * will redraw the game on the screen
     */
    public void refresh() {
        this.repaint();
    }

    //Inner Class

    /**
     * MatrixPanel
     * innerClass - the panel that we see
     */
    class MatrixPanel extends JPanel {

        MatrixPanel() {
            addMouseListener(new MatrixPanelMouseListener());
        } // makes the panel be able to be affected by mouse

        public void paintComponent(Graphics g) {
            super.repaint();

            setDoubleBuffered(true);
            g.setColor(Color.BLACK);
            g.drawOval(50, 50, 50, 50);


            // for each of the tiles, the game will find what type of tile it is, and then draw it based on the given color
            for(int i = 0; i<matrix[0].length;i=i+1)  {
                for(int j = 0; j<matrix.length;j=j+1)  {

                    if (matrix[i][j] instanceof Dirt) {
                        g.setColor(Color.getHSBColor(60f/360f, 0.2f, 0.6f));
                    } else if (matrix[i][j] instanceof Plant) {
                        g.setColor(Color.GREEN);
                    } else if (matrix[i][j] instanceof FruitTree) {
                        g.setColor(Color.getHSBColor(120f, 0.8f, 0.4f));
                    } else if (matrix[i][j] instanceof Rock) {
                        g.setColor(Color.BLACK);
                    } else if (matrix[i][j] instanceof Human) {
                        Human toDrawHuman = (Human) matrix[i][j];
                        if (toDrawHuman.getAge() < (toDrawHuman.getMaxAge() / toDrawHuman.getLegalRatio())) { // if the human is still a baby
                            g.setColor(Color.orange);
                        } else if (toDrawHuman.getGender()) {
                            g.setColor(Color.BLUE);
                        } else if (!toDrawHuman.getGender()) {
                            g.setColor(Color.MAGENTA);
                        }
                    } else if (matrix[i][j] instanceof Zombie) {
                        g.setColor(Color.red);
                    }

                    g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
                    g.setColor(Color.BLACK);
                    g.drawRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
                }
            }


        }
    }


    /**
     * MatrixPanelMouseListener
     * the Mouse Listener for panel
     */
    class MatrixPanelMouseListener implements MouseListener{

        public void mousePressed(MouseEvent e) {

            int x = e.getPoint().x / GridToScreenRatio; // gets x cord of cllick
            int y = e.getPoint().y / GridToScreenRatio; // gets y cord of click

            if (((x != 0) && !(x >= gg.getWidth())) && ((y != 0) && !(y >= gg.getLength()))) { // as long as its not at the edges, itll create a zombie where u clicked f
                gg.createRandomZombie(y,x);
            }

        }

        public void mouseReleased(MouseEvent e) {
            //System.out.println("Mouse released; # of clicks: " + e.getClickCount());
        }

        public void mouseEntered(MouseEvent e) {
           // System.out.println("Mouse entered");
        }

        public void mouseExited(MouseEvent e) {
           // System.out.println("Mouse exited");
        }

        public void mouseClicked(MouseEvent e) {
           // System.out.println("Mouse clicked (# of clicks: "+ e.getClickCount() + ")");
        }

    }

}