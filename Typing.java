import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import java.awt.AWTEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
//import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Typing extends JFrame {

	public float wpm;
	public float accuracy;
	int charsRight;
	int typed;
	int a = 0;
	int b = 1;
	Timer t;
	boolean flag = true;

	ArrayList<String> combo = new ArrayList<String>();

	private JPanel contentPane;

	public static void main(String[] args) throws Exception {

		UIManager.setLookAndFeel(new FlatDarkLaf());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Typing frame = new Typing();
					frame.setVisible(true);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//					frame.setUndecorated(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Typing() throws Exception {

		BufferedReader bufReader = new BufferedReader(new FileReader("wordlist.txt"));

		DefaultHighlightPainter redPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
		DefaultHighlightPainter orangePainter = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE);
		DefaultHighlightPainter cyanPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);

		Random rand = new Random();
		String line = bufReader.readLine();
		while (line != null) {
			combo.add(line);
			line = bufReader.readLine();
		}

		int x;
		String st = "";

		int i = 69905;

		for (int j = 0; j < 100; j++) {
			x = rand.nextInt();
			if (x < 0) {
				x = x * -1;
			}
			st += combo.get(x % i) + " ";
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 925, 468);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JEditorPane editorPane = new JEditorPane();

		editorPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		editorPane.setFont(new Font("Times New Roman", Font.BOLD, 25));

		contentPane.add(editorPane);
		editorPane.setEditable(false);
		editorPane.setText(st);

		JLabel lbltimer = new JLabel("0.0");
		lbltimer.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lbltimer, BorderLayout.EAST);

		lbltimer.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

		JButton TimerBtn = new JButton("Start");

		TimerBtn.setFont(new Font("Algerian", Font.BOLD | Font.ITALIC, 20));
		contentPane.add(TimerBtn, BorderLayout.PAGE_END);
		TimerBtn.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		TimerBtn.setSize(100, 30);

		TimerBtn.addActionListener(new ActionListener() {
			int k = 0;

			public void actionPerformed(ActionEvent e) {

				t = new Timer(1000, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						editorPane.requestFocus();
						DecimalFormat df = new DecimalFormat("#.00");
						float key = Float.valueOf(df.format(k));
						lbltimer.setText(String.valueOf(key));
						if (t.isRunning())
							k++;
						if (k == 30) {
							if (typed == 0)
								typed++;
							t.stop();

							float rightflt = (float) charsRight;
							accuracy = (float) (rightflt / typed) * 100;

							float a = Float.valueOf(df.format(accuracy));
							wpm = (float) (typed * (accuracy / 100)) / 4;
							float w = Float.valueOf(df.format(wpm));
							lbltimer.setText("Your Accuracy is " + String.valueOf(a) + " % "
									+ "and Your Words Per Minute is " + String.valueOf(w*2));
							editorPane.setVisible(false);
							lbltimer.setFont(new Font("Bodoi MT Condensed", Font.BOLD | Font.ITALIC, 35));
							contentPane.add(lbltimer, BorderLayout.CENTER);
							flag = false;
							TimerBtn.setVisible(false);

						}
					}
				});
				if (flag == true) {
					t.start();
				}

			}
		});

		editorPane.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {

				try {

					if (t.isRunning()) {

						char key = e.getKeyChar();
						String k = Character.toString(key);
						try {
							if (k.equals(editorPane.getText(a, 1))) {
								editorPane.getHighlighter().addHighlight(a, b, cyanPainter);
								charsRight++;
							} else {
								editorPane.getHighlighter().addHighlight(a, b, redPainter);
							}
							a++;
							b++;
							typed++;

						} catch (Exception e1) {
							System.out.println(e1);
						}
					}
				} catch (Exception e5) {
					System.out.println("Clock Not Started");
				}
			}
		});

	}
}
