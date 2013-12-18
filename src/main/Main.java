package main;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import bayes.enums.CategoryProbabilityMode;
import bayes.enums.ConProbabilityMode;
import documentmap.DocumentMap;
import documentmap.document.Document;
import io.clarin.ClarinXmlParser;
import io.document.DocumentLoader;



public class Main {
	
	private static final String DIR="D:\\download\\kpwr";
	private static final String KPWR=DIR+"\\kpwr";
	private static final String CSV=DIR+"\\csv";

	public static void main(String[] args) throws Exception {
//		final JFrame frame = new JFrame();
//		frame.setSize(new Dimension(800, 600));
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		DocumentMap map = new DocumentMap(1, 10, 5);
//		GraphPanel panel = new GraphPanel(map);
//		frame.setContentPane(panel);
//		SwingUtilities.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
//				frame.setVisible(true);
//			}
//		});
//		DocumentLoader loader = new DocumentLoader(new File("D:\\Studia\\IJN\\kpwr-1.1"), new File("D:\\Studia\\IJN\\csv"));
//		System.out.println(loader.loadDocuments(10, true, false));
		
//		ClarinWsdWorker w = new ClarinWsdWorker("C:\\Users\\XardaS\\Desktop\\kpwr");
//		w.disambiguateCorpus();
		
//		ClarinXmlParser p = new ClarinXmlParser(KPWR, CSV);
//		p.parse(false);
//		p.saveDictionary("C:\\Users\\XardaS\\Desktop\\dick.csv");
		test();
	}
	
	public static void test() throws IOException
	{
		DocumentMap map=new DocumentMap(1,1,1);
		List<String> args=new LinkedList<String>();
		args.add("LEARN_AND_CLASSIFY");
		args.add(DIR+"\\learn.csv");
		args.add("s");
		args.add("MULTINOMINAL");
		args.add("BY_WORD");
		args.add("SIMPLE_SMOOTHING");
		args.add(DIR+"\\test.csv");
		DocumentLoader loader=new DocumentLoader(new File(KPWR), new File(CSV));
		List<String> categories=loader.getCategoryList();
		List<Document> learning=new LinkedList<Document>();
		List<Document> test=new LinkedList<Document>();
		List<Document> test2=new LinkedList<Document>();
		for(String cat:categories)
		{
			learning.addAll(loader.loadDocuments(cat, 5, true));
			test.addAll(loader.loadDocuments(cat, 5, true));
			test.addAll(loader.loadDocuments(cat, 10, true));
		}
		map.createMap(args.toArray(new String[args.size()]), test, learning, categories);
		//map.mapDocuments(args, test2, null);
	}
}