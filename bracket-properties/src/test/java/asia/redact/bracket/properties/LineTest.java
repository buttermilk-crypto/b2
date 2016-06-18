package asia.redact.bracket.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import asia.redact.bracket.properties.line.Line;
import asia.redact.bracket.properties.line.LineScanner;

public class LineTest {

	@Test
	public void test0() {
		
		List<Line> lines = new ArrayList<Line>();
		try (
				InputStream in = this.getClass().getResourceAsStream("/crlf/compare.properties");
				InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
				LineScanner scanner = new LineScanner(reader);
		) {
			Line line = null;
			while((line = scanner.line()) != null) {
				lines.add(line);
			}
			
		}catch(IOException x){
			x.printStackTrace();
		}
		
		try (
				InputStream in = this.getClass().getResourceAsStream("/crlf/compare.mac.properties");
				InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
				LineScanner scanner = new LineScanner(reader);
		) {
			Line line = null;
			while((line = scanner.line()) != null) {
				lines.add(line);
			}
			
		}catch(IOException x){
			x.printStackTrace();
		}
		
		try (
				InputStream in = this.getClass().getResourceAsStream("/crlf/compare.unix.properties");
				InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
				LineScanner scanner = new LineScanner(reader);
		) {
			Line line = null;
			while((line = scanner.line()) != null) {
				lines.add(line);
			}
			
		}catch(IOException x){
			x.printStackTrace();
		}
		
		Assert.assertTrue(lines.size()==15);
	}
	
	

}
