import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;


public class S27794Kowieski extends JFrame{

    private List<Kwadrat> listaKwadraty = new ArrayList<Kwadrat>();
    int bokKwadratu = 60;
    int szybkosc = 5;

    int maxczas = 1200;
    int interwal = 50;
    double szansa = 0.2;
    private int kwadratyDno = 0;
    private int kwadratyKlik=0;

    private int czas = 0;

    private int kwadratyUtworzone=0;
    private int wynik = 0 ;

    private boolean czyMenu = false;
    class Kwadrat
    {
        private int x;
        private int y;
        private Color color;

        public Kwadrat(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Color getColor() {
            return color;
        }

        public void move()
        {
            y+=szybkosc;
        }

        public boolean trafiony(int xMyszy,int yMyszy)
        {
            if (xMyszy>=this.getX()&&xMyszy<=(this.getX()+bokKwadratu)&&yMyszy>=this.getY()&&yMyszy<=(this.getY()+bokKwadratu)) return true;
            else return false;
        }
    }

    void nowyKwadrat()
    {
        int x = (int)(Math.random()*400);
        Color kolor = new Color((int)(Math.random()*0x1000000));
        Kwadrat kwadrat = new Kwadrat(x,0,kolor);
        listaKwadraty.add(kwadrat);
        kwadratyUtworzone++;
    }

    private S27794Kowieski()
    {

        int wysokosc = 600;
        int szerokosc = 400;
        int wysokoscA = wysokosc+50;
        setLayout(new BorderLayout());
        JPanel glowny = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Kwadrat kwadrat: listaKwadraty ) {
                    g.setColor(kwadrat.getColor());
                    g.fillRect(kwadrat.getX(),kwadrat.getY(),bokKwadratu,bokKwadratu);
                }
            }
        };
        glowny.setPreferredSize(new Dimension(szerokosc,wysokosc));
        glowny.setBackground(Color.blue);
        glowny.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Iterator<Kwadrat> kwadratIterator = listaKwadraty.iterator();
                while (kwadratIterator.hasNext())
                {
                    Kwadrat kwadrat = kwadratIterator.next();
                    if (kwadrat.trafiony(e.getX(),e.getY())) {
                        System.out.println("Klikając w x="+e.getX()+" i y= "+e.getY()+" udalo sie zniknac kwadrat o x="+kwadrat.getX()+" i y="+kwadrat.getY());
                        kwadratIterator.remove();
                        kwadratyKlik++;
                    }
                    //else System.out.println("Klikając w x="+e.getX()+" i y= "+e.getY()+"nie udalo sie trafić kwadrat o x="+kwadrat.getX()+" i y="+kwadrat.getY());
                    repaint();


                }
            }
        });





        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(glowny);
        JPanel dolny = new JPanel();
        JLabel skore = new JLabel();
        dolny.add(skore);


        add(dolny,BorderLayout.SOUTH);
        setSize(new Dimension(szerokosc,wysokoscA));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (Math.random()<szansa)
                {
                    nowyKwadrat();
                }
                Iterator<Kwadrat> kwadratIterator = listaKwadraty.iterator();
                while (kwadratIterator.hasNext())
                {
                    Kwadrat kwadrat = kwadratIterator.next();
                    kwadrat.move();
                    if(kwadrat.getY()>=wysokosc)
                    {
                        kwadratIterator.remove();
                        kwadratyDno++;
                    }
                    skore.setText("Score: "+(kwadratyUtworzone/(kwadratyUtworzone-kwadratyDno))*100+"%");

                }
                System.out.println("wow");
                if (kwadratyUtworzone>0)
                {
                    //System.out.println((int)(((kwadratyUtworzone-kwadratyDno)/(float)kwadratyUtworzone)*100.0)+"%");
                    skore.setText("Score: "+(int)(((kwadratyUtworzone-kwadratyDno)/(float)kwadratyUtworzone)*100.0)+"%");
                }

                glowny.repaint();
                dolny.repaint();
                czas++;
                if (czas>maxczas)
                {
                    int wynikProc = (int)(((kwadratyUtworzone-kwadratyDno)/(float)kwadratyUtworzone)*100.0);
                    if (wynikProc>60)
                        JOptionPane.showMessageDialog(glowny,"Wygrales 8)");
                    else
                    JOptionPane.showMessageDialog(glowny,"Przegrales :/");

                    this.cancel();
                    dispose();
                    System.exit(0);
                }



            }
        };

       // timer.scheduleAtFixedRate(timerTask,100,interwal);

        JPanel oknoM = new JPanel(new GridLayout(7,1,5,5));

        oknoM.setPreferredSize(new Dimension(350, 400));

        JLabel tytul = new JLabel("Kwadraty");
        tytul.setFont(new Font("Arial",1,40));
        tytul.setHorizontalAlignment(oknoM.getWidth()/2);

        JLabel celemGry = new JLabel("Celem gry jest jej ukończenie z wynikiem powyżej 60%");
        celemGry.setHorizontalAlignment(oknoM.getWidth()/2);

        JPanel okno1 = new JPanel(new GridLayout(1,2));
        JLabel wybierzSzybkosc = new JLabel("Wybierz szybkosc");
        wybierzSzybkosc.setBorder(new EmptyBorder(5,5,5,5));
        JSlider szybkoscKw = new JSlider(1,10,1);
        szybkoscKw.setBorder(new EmptyBorder(5,5,5,5));
        szybkoscKw.setValue(2);
        okno1.add(wybierzSzybkosc);
        okno1.add(szybkoscKw);


        JPanel okno2 = new JPanel(new GridLayout(1,2));
        JLabel wybierzCzest = new JLabel("Wybierz czestotliwosc");
        wybierzCzest.setBorder(new EmptyBorder(5,5,5,5));
        JSlider czestKw = new JSlider(1,10,1);
        czestKw.setBorder(new EmptyBorder(5,5,5,5));
        czestKw.setValue(3);
        okno2.add(wybierzCzest);
        okno2.add(czestKw);

        JPanel okno3 = new JPanel(new GridLayout(1,2));
        JLabel wybierzWielk = new JLabel("Wybierz wielkość kwadratu");
        wybierzWielk.setBorder(new EmptyBorder(5,5,5,5));
        String[] listaWielk = {"MAŁE","ŚREDNIE","DUŻE","OGROMNE"};
        JComboBox<String> komboWielk = new JComboBox<>(listaWielk);
        komboWielk.setBorder(new EmptyBorder(5,5,5,5));
        komboWielk.setSelectedIndex(2);
        okno3.add(wybierzWielk);
        okno3.add(komboWielk);

        JPanel okno4 = new JPanel(new GridLayout(1,2));
        JLabel wybierzCzas = new JLabel("Wybierz czas gry(sek)");
        wybierzCzas.setBorder(new EmptyBorder(5,5,5,5));
        JSpinner spinCzas = new JSpinner();
        spinCzas.setBorder(new EmptyBorder(5,5,5,5));
        spinCzas.setValue(60);
        okno4.add(wybierzCzas);
        okno4.add(spinCzas);

        JButton startGra = new JButton("Start");
        startGra.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                szybkosc=szybkoscKw.getValue();
                szansa=0.04*czestKw.getValue();
                maxczas= (20*(int) spinCzas.getValue());
                bokKwadratu = switch (komboWielk.getSelectedIndex())
                {
                    case 0 -> 25;
                    case 1 -> 55;
                    case 2 -> 80;
                    case 3 -> 100;
                    default -> 40;
                };
                oknoM.setVisible(false);
                timer.scheduleAtFixedRate(timerTask,100,interwal);
            }
        });

        oknoM.add(tytul);
        oknoM.add(celemGry);
        oknoM.add(okno1);
        oknoM.add(okno2);
        oknoM.add(okno3);
        oknoM.add(okno4);
        oknoM.add(startGra);

        glowny.add(oknoM);
        JComboBox<String> wielkoscKw = new JComboBox<>();


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                ()-> {
                    S27794Kowieski gra = new S27794Kowieski();
                    gra.setVisible(true);
                }
        );
    }


}
