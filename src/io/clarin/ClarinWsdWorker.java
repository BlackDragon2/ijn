package io.clarin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class ClarinWsdWorker {
	private static final String URL = "http://www.clarin-pl.eu/ws/wsd.php";
	private static final String READY = "READY";
	private Map<String, String> encoding;
	private String corpusPath;
	private HttpClient client;

	public ClarinWsdWorker(String corpusPath) {
		this.corpusPath = corpusPath;
		initializeEncodingFix();
	}

	public void disambiguateCorpus() throws Exception {
		File dir = new File(corpusPath);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				File[] filesInDir = file.listFiles(new FileFilter() {

					@Override
					public boolean accept(File file) {
						String fileName = file.getName();
						return fileName.endsWith(".xml_dis");
					}
				});
				for (File fileInDir : filesInDir) {
					List<String> lines = Files.readAllLines(fileInDir.toPath(), Charset.forName("UTF-8"));
					StringBuilder sb = new StringBuilder();
					for (String line : lines) {
						sb.append(line).append("\r\n");
					}
					String dis = disambiguateXml(sb.toString());
					String newFileName = file.getAbsolutePath() + File.separator + fileInDir.getName().split("\\.")[0] + ".xml_wsd";
					Files.write(new File(newFileName).toPath(), dis.getBytes("UTF-8"), StandardOpenOption.CREATE);
					System.out.println(newFileName);
				}
			}
		}
	}

	public String disambiguateXml(String content) throws Exception {
		String result = "";
		client = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(URL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "send"));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("lexicon", "1"));
		params.add(new BasicNameValuePair("input", "ccl"));
		params.add(new BasicNameValuePair("output", "ccl"));
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse response = client.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(instream));
			String token = br.readLine();
			token = parseClarinResponse(token);
			while (!checkStatus(token)) {
				Thread.sleep(2000);
			}
			result = getXml(token);
		}
		return result;
	}

	private String getXml(String token) throws Exception {
		client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(URL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", token));
		params.add(new BasicNameValuePair("action", "get"));
		post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		String content = "";
		if (entity != null) {
			InputStream instream = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(instream));
			content = br.readLine();
			content = fixEncoding(content);
			content = content.substring(8, content.length() - 2);
			br.close();
		}
		return content;
	}

	private boolean checkStatus(String token) throws Exception {
		client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(URL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", token));
		params.add(new BasicNameValuePair("action", "check"));
		post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		String status = "";
		if (entity != null) {
			InputStream instream = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(instream));
			status = br.readLine();
			br.close();
			status = parseClarinResponse(status);
			System.out.println(status);
		}
		return status.equals(READY);
	}

	private String parseClarinResponse(String response) {
		String[] split = response.split(":");
		String result = "";
		for (int i = 1; i < split.length; i++) {
			result += split[i];
		}
		result = result.substring(1, result.length() - 2);
		return result;
	}

	private void initializeEncodingFix() {
		encoding = new HashMap<>();
		encoding.put("\\u0104", "•");
		encoding.put("\\u0105", "π");
		encoding.put("\\u0106", "∆");
		encoding.put("\\u0107", "Ê");
		encoding.put("\\u0118", " ");
		encoding.put("\\u0119", "Í");
		encoding.put("\\u0141", "£");
		encoding.put("\\u0142", "≥");
		encoding.put("\\u0143", "—");
		encoding.put("\\u0144", "Ò");
		encoding.put("\\u00d3", "”");
		encoding.put("\\u00f3", "Û");
		encoding.put("\\u015a", "å");
		encoding.put("\\u015b", "ú");
		encoding.put("\\u0179", "è");
		encoding.put("\\u017a", "ü");
		encoding.put("\\u017b", "Ø");
		encoding.put("\\u017c", "ø");
	}

	private String fixEncoding(String text) {
		String result = text;
		for (String code : encoding.keySet()) {
			result = result.replace(code, encoding.get(code));
		}
		result = result.replaceAll("\\\\n", "\\\n");
		result = result.replaceAll("\\\\", "");
		return result;
	}
}