package main;

import io.document.DocumentLoader;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ui.GraphPanel;
import documentmap.DocumentMap;

public class Main {

	public static void main(String[] args) throws IOException {
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
		DocumentLoader loader = new DocumentLoader(new File("D:\\Studia\\IJN\\kpwr-1.1"), new File("D:\\Studia\\IJN\\csv"));
		System.out.println(loader.loadDocuments(10, true, false));
	}
}