package asia.redact.bracket.properties.icu4j;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

/**
 * Used to generate some comparison UTF-8 vs. escaped properties files
 * 
 * @author dave
 *
 */
public class MakeUnicodeEscapedData {

	private static int line_iter = 0;

	private void gen(String[] fileNames) {

		for (int i = 0; i < fileNames.length; i++) {

			line_iter = 0;
			CharsetDetector detector = new CharsetDetector();

			try (FileInputStream in = new FileInputStream("src/test/resources/icu4j/" + fileNames[i]);
					BufferedInputStream bin = new BufferedInputStream(in);) {

				detector.setText(bin);
				CharsetMatch cm = detector.detect();

				System.out.println(fileNames[i]+" "+cm.getName()+" "+(cm.getConfidence()+" "+cm.getLanguage()));
			

			} catch (Exception e) {
				e.printStackTrace();
			}

			try (FileInputStream in = new FileInputStream("src/test/resources/icu4j/"+fileNames[i]);
					InputStreamReader bin = new InputStreamReader(in, StandardCharsets.UTF_8);
					BufferedReader breader = new BufferedReader(bin);
					FileOutputStream out = new FileOutputStream("src/test/resources/icu4j/"+fileNames[i]+".out.properties");
					OutputStreamWriter wout = new OutputStreamWriter(out);
					BufferedWriter bw = new BufferedWriter(wout);

			) {

				bw.append("# ");
				bw.append(fileNames[i]);
				bw.append(" properties file, random lorem ipsem contents but should be legal chars ");
				bw.newLine();
				bw.newLine();

				breader.lines().forEachOrdered((s) -> {
					try {

						if (s.trim().length() > 0) {
							bw.append("line");
							bw.append(String.valueOf(line_iter));
							bw.append("=");
							bw.append(s);
							bw.newLine();
							line_iter++;
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
					
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	
	public void create() {

		String [] fileNames = { "Arabic", "Chinese", "Hindi", "Latin" };
		this.gen(fileNames);
	}

}
