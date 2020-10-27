package bedirhansisman;

public class HelperRunner extends Thread {

	String sentence;

	public HelperRunner(String sentence) {
		this.sentence = sentence;
	}

	@Override
	public void run() {
		System.out.println("[INFO] : [" + this.getName() + " baþladý.]");
		count();
		System.out.println("[INFO] : [" + this.getName() + " bitti.]");
	}

	public synchronized void count() {
		String[] words = sentence.split(" ");

		for (String word : words) {
			if (Globals.words.containsKey(word) == false && word.equals("") == false) {
				Globals.words.put(word, 1);
			} else if (Globals.words.containsKey(word) == true && word.equals("") == false) {
				try {
					int howMany = Globals.words.get(word) + 1;
					Globals.words.put(word, howMany);
				} catch (Exception e) {
					System.out.println(e.getLocalizedMessage());
				}
				
			}

		}

		Globals.processCounter = Globals.processCounter + 1;

	}

}
