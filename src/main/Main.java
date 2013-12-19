package main;

import io.document.DocumentLoader;
import io.documentmap.ObjectIOManager;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ui.GraphPanel;
import bayes.params.BayesParams;
import documentmap.DocumentMap;
import documentmap.document.Document;

public class Main {

	private static final String DIR = "C:/Users/XardaS/Desktop";
	private static final String KPWR = DIR + "/kpwr_merged";
	private static final String CSV = DIR + "/csv";

	public static void main(String[] args) throws Exception {
		// final JFrame frame = new JFrame();
		// frame.setSize(new Dimension(800, 600));
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// DocumentMap map = new DocumentMap(1, 10, 5);
		// GraphPanel panel = new GraphPanel(map);
		// frame.setContentPane(panel);
		// SwingUtilities.invokeLater(new Runnable() {
		//
		// @Override
		// public void run() {
		// frame.setVisible(true);
		// }
		// });
		// DocumentLoader loader = new DocumentLoader(new
		// File("D:\\Studia\\IJN\\kpwr-1.1"), new File("D:\\Studia\\IJN\\csv"));
		// System.out.println(loader.loadDocuments(10, true, false));

		// ClarinWsdWorker w = new
		// ClarinWsdWorker("C:\\Users\\XardaS\\Desktop\\kpwr");
		// w.disambiguateCorpus();

		// ClarinXmlParser p = new ClarinXmlParser(KPWR, CSV);
		// p.parse(false);
		// p.saveDictionary("C:\\Users\\XardaS\\Desktop\\dick.csv");
		test();
	}

	public static void test() throws IOException {
		DocumentMap map = new DocumentMap(12, 50, 50);
		List<String> args = new LinkedList<String>();
		args.add("LEARN_AND_CLASSIFY");
		args.add(DIR + "\\learn.csv");
		args.add("s");
		args.add("MULTINOMINAL");
		args.add("BY_WORD");
		args.add("SIMPLE_SMOOTHING");
		args.add(DIR + "\\test.csv");
		BayesParams params = new BayesParams(args.toArray(new String[args.size()]));
		DocumentLoader loader = new DocumentLoader(new File(KPWR), new File(CSV));
		List<String> categories = loader.getCategoryList();
		List<Document> learning = new LinkedList<Document>();
		List<Document> test = new LinkedList<Document>();
		List<Document> test2 = new LinkedList<Document>();
		for (String cat : categories) {
			learning.addAll(loader.loadDocuments(cat, 5, true));
			test.addAll(loader.loadDocuments(cat, 5, true));
			test2.addAll(loader.loadDocuments(cat, 10, true));
		}
		map.createMap(params, test, learning, categories);
		ObjectIOManager.save(map, new File("temp.map"));
		args.clear();
		args.add("CLASSIFY_ONLY");
		args.add(DIR + "\\learnLEARNED.csv");
		args.add("MULTINOMINAL");
		args.add(DIR + "\\test2.csv");
		params = new BayesParams(args.toArray(new String[args.size()]));
		List<int[]> list = map.mapDocuments(params, test2, null);
		final JFrame frame = new JFrame();
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphPanel panel = new GraphPanel(map, list, test2);
		frame.setContentPane(panel);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
}