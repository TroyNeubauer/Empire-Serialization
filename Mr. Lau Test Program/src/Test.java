import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Test {

	private static String androidFile = new File("C:\\Java\\Android").getAbsolutePath();

	public static void add(String file, ArrayList<File> list) {
		File f = new File(file);
		if (f.getAbsolutePath().equals(androidFile)) {
			System.out.println("Skipping android");
			return;
		}
		if (f.isDirectory()) {
			for (File child : f.listFiles()) {
				add(child.getPath(), list);
			}
		} else {// f is a file
			if (!file.contains(".java")) {
				return;
			}
			list.add(f);
		}

	}

	public static void main(String[] args) throws Exception {
		System.out.println("Listing files...");
		ArrayList<File> list = new ArrayList<>();
		add("C:\\Java", list);
		int count = 0;
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./output.txt")));
		for (File file : list) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			writer.write("Reading file \"" + file + "\"\n");
			String line;
			while ((line = reader.readLine()) != null) {
				char[] chars = line.toCharArray();
				int spaceCount = 0;
				for (int i = 0; i < chars.length; i++) {
					char current = chars[i];
					if (current == '\t') {
						count++;
					}
					if ((current == ' ')) {
						spaceCount++;
					} else {
						spaceCount = 0;
					}
					if (spaceCount == 4)
						count++;
				}
			}
			reader.close();
		}
		writer.close();
		System.out.println("\n\n\n-----------------------------\n\n");
		System.out.println("Total tabs: " + count);

		// Keep insertion order
		/*
		 * 
		 * Map<String, String> results = new LinkedHashMap<String, String>(); String nativeSub = "native-"; String[]
		 * phrases = new String[] { "java.version", "java.vendor", "java.vendor.url", "java.home",
		 * "java.vm.specification.version", "java.vm.specification.vendor", "java.vm.specification.name",
		 * "java.vm.version", "java.vm.vendor", "java.vm.name", "java.specification.version",
		 * "java.specification.vendor", "java.specification.name", "java.class.version", "java.class.path",
		 * "java.library.path", "java.io.tmpdir", "java.compiler", "java.ext.dirs", "os.name", "os.arch", "os.version",
		 * "file.separator", "path.separator", "line.separator", "user.name", "user.home", "user.dir" };
		 * 
		 * for (String phrase : phrases) { String result; if (phrase.length() > nativeSub.length() &&
		 * phrase.substring(0, nativeSub.length()).equals(phrase)) { result =
		 * System.getenv(phrase.substring(nativeSub.length())); } else { result = System.getProperty(phrase); }
		 * results.put(phrase, result); } int maxLength = -1; for (String key : results.keySet()) { int length =
		 * key.length() + 2;// +2 to account for the quotes if (length > maxLength) { maxLength = length; } } for
		 * (Entry<String, String> entry : results.entrySet()) { String key = "\"" + entry.getKey() + "\""; while
		 * (key.length() < maxLength) { key += " "; } System.out.println("Key: " + key + " = \"" + entry.getValue() +
		 * "\""); }
		 */
	}

}
