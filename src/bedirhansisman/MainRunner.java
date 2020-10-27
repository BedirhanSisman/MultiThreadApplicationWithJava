package bedirhansisman;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MainRunner extends Thread {

	String path;

	public MainRunner(String path) {
		this.path = path;
	}

	@Override
	public void run() {
		System.out.println("[INFO] : [" + this.getName() + " baþladý.]");
		String text = readFile(path);
		List<String> sentences = divideBySentences(text);
		calculateAverageWordCount(sentences);
		startHelperThreads(sentences);
		System.out.println("[INFO] : [" + this.getName() + " bitti.]");
	}

	public String readFile(String path) {
		String text = "";
		try {
			File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if (data.endsWith(" ") == false)
					data += " ";
				text += data;
			}
			myReader.close();
			return text;
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred. readFile");
			e.printStackTrace();
		}
		return text;
	}

	public List<String> divideBySentences(String text) {
		List<String> sentences = new ArrayList<>();

		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
		iterator.setText(text);
		int start = iterator.first();
		for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
			String sentence = text.substring(start, end).trim();
			sentences.add(sentence.substring(0, sentence.length() - 1)); // For deleting "." or "?"
		}

		return sentences;
	}

	public void calculateAverageWordCount(List<String> sentences) {

		int average = 0, sum = 0;

		for (String s : sentences) {
			sum += s.split(" ").length;
		}

		average = (int) Math.ceil((double) sum / (double) sentences.size());

		System.out.println("Sentence Count : " + sentences.size());
		System.out.println("Avg. Word Count : " + average);
		System.out.println();

	}

	public void startHelperThreads(List<String> sentences) {
		List<HelperRunner> threadList = new ArrayList<>();

		for (String s : sentences) { // Helper Threads are creating. Count is equals to quantity of sentences.
										// Default 5 is unnecessary. :)
			HelperRunner hr = new HelperRunner(s);
			threadList.add(hr);
		}

		for (HelperRunner r : threadList) { // Starting all of helper threads.
			r.start();
		}

		for (HelperRunner r : threadList) { // To prevent finishing main thread until current threads will finish.
			try {
				r.join();
			} catch (InterruptedException e) {
			}
		}
		
		while (Globals.processCounter < sentences.size()) {	}
		
		/*Globals.words.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.forEach(entry -> {
					System.out.println(entry.getKey() + " " + entry.getValue());
				});*/ //=> Causes mistake 
		
		List<String> keys = new ArrayList<>(Globals.words.keySet());
		List<Integer> values = new ArrayList<>(Globals.words.values());
		
		for(int i = 0; i < values.size(); i++) {
			for(int j = i + 1; j < values.size(); j++) {
				if(values.get(i) < values.get(j)) {
					Integer tempV = values.get(i);
					values.set(i, values.get(j));
					values.set(j, tempV);
					
					String tempK = keys.get(i);
					keys.set(i, keys.get(j));
					keys.set(j, tempK);
				}
			}
		}

		for(int i = 0; i < values.size(); i++) {
			System.out.println(keys.get(i) + " - " + values.get(i));
		}
	}
}
