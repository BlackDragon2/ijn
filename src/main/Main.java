package main;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ui.GraphPanel;
import documentmap.DocumentMap;

public class Main {

	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DocumentMap map = new DocumentMap(1, 10, 5);
		GraphPanel panel = new GraphPanel(map);
		frame.setContentPane(panel);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
}