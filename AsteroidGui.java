package Asteroid;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;
import java.awt.event.MouseEvent;

public class AsteroidGui extends GraphicsProgram {
	final private int WIDTH = 850;
	final private int HEIGHT = 500;
	final private int LABEL_HEIGHT = 16;
	final private int SCORE_X = 30;
	final private int LIVES_X = WIDTH - 100;
	final private int NO_STARS = 30;
	
	private JButton start;
	private JButton reset;
	private GImage banner;
	private GImage ship;
	private GImage [] asteroids;
	private GImage [] aliens;
	private GOval [] stars;
	private Timer asteroidTimer;
	
	private GLabel scoreLabel;
	private GLabel livesLabel;
	
	private boolean gameOver = false;
	private boolean started;
	private int score = 0;
	private int lives = 3;
	private int screenDivisors = 15;
	private double speed = 5;
	private double shipSpeed = 20;
	private RandomGenerator randgen = RandomGenerator.getInstance();
	
    public GImage makeAsteroid(int numDivs)
    {
        String asteroidImage = "img/Asteroid.png";
        GImage asteroid = new GImage(asteroidImage);
        asteroid.setSize(30, 18);
        double locx = randgen.nextDouble(WIDTH, getWidth()-asteroid.getWidth());
        int whichdiv = randgen.nextInt(0,numDivs);
        double locy = whichdiv*HEIGHT/numDivs-1;
        asteroid.setLocation(locx, locy);
        return asteroid;
    }

	
	public void init() {
		setSize(WIDTH,HEIGHT);
		setTitle("Kugnus Asteroid");
		setBackground(new Color(0,0,84));
		
		banner = new GImage("img/SpaceJet.png");
		banner.setSize(650, 384);
		banner.setLocation(100, 50);
		start = new JButton("Start");
		reset = new JButton("Reset");
		
		started = false;
		
		add(start, NORTH);
		add(reset, NORTH);
		add(banner);
		
		scoreLabel = new GLabel("SCORE: "+score);
		livesLabel = new GLabel("LIVES: "+lives);
		scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
		livesLabel.setFont(new Font("Arial", Font.BOLD, 18));
		scoreLabel.setColor(Color.YELLOW);
		livesLabel.setColor(Color.YELLOW);
		add(scoreLabel);		
		add(livesLabel);		
		
		stars = new GOval [NO_STARS];
		for (int i = 0; i < NO_STARS; i++) {
			int x = WIDTH;
			int y = i*HEIGHT/NO_STARS;
			stars[i] = new GOval(x, y, 2, 1);
			stars[i].setColor(Color.YELLOW);
			stars[i].setFilled(true);
			add(stars[i]);
		}
		
		ship = new GImage("img/SpaceJet-Horizontal.png");
		ship.setSize(140, 120);
		ship.setLocation(SCORE_X, (HEIGHT-ship.getHeight())/2);
		
		start.addActionListener(this);
		reset.addActionListener(this);
		addKeyListeners();
		
		asteroidTimer = new javax.swing.Timer(1, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
		        int whichdiv = randgen.nextInt(0,screenDivisors-1);
		        add(asteroids[whichdiv]);
			}
		});		
	}
	
    public void keyPressed (KeyEvent event)
    {
        int key = event.getKeyCode();
        if (key == KeyEvent.VK_UP)
        {
            ship.move(0, -shipSpeed);
        }
        if (key == KeyEvent.VK_DOWN)
        {
        	ship.move(0, shipSpeed);
        }
        if (key == KeyEvent.VK_LEFT)
        {
        	ship.move(-shipSpeed, 0);
        }
        if (key == KeyEvent.VK_RIGHT)
        {
        	ship.move(shipSpeed, 0);
        }

    }

	void SetGUI() {
		scoreLabel.setLocation(SCORE_X, LABEL_HEIGHT);
		livesLabel.setLocation(LIVES_X, LABEL_HEIGHT);
		scoreLabel.sendToFront();

		asteroids = new GImage[screenDivisors];
        for (int i=0; i < screenDivisors; i++) {
        	asteroids[i] = makeAsteroid(screenDivisors);
            add(asteroids[i]);        	
        }
	}
	
	public void actionPerformed(ActionEvent e) {
		String bt = e.getActionCommand();
		System.out.println(bt);
		
		if(bt.equals("Start")) {
			started = true;
			System.out.println("Started");
			asteroidTimer.start();
		} else if(bt.equals("Reset")) {
			score = 0;
			lives = 3;
			SetGUI();
		}
	}
	
	public void moveStars() {
		int index = randgen.nextInt(0,NO_STARS-1);
		double x = (stars[index].getX()-0.5)%WIDTH;
		double y =  stars[index].getY();
		if(x < 0) {
			x = WIDTH;
		}
		stars[index].setLocation(x,y);
	}
	
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		remove(banner);
		add(ship);
		SetGUI();			

		while(!gameOver) {
			moveStars();
			if (started)  {
				for (int i = 0; i < screenDivisors; i++) {
					asteroids[i].move(-speed, 0);

					if (asteroids[i].getX() < 0) {
						asteroids[i].setLocation(WIDTH, asteroids[i].getY());
					}
				}
                
                pause(0.005);
			}
		}
		
	}

}
