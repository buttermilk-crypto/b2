package asia.redact.bracket.properties.io;

import java.io.IOException;
import java.io.StringReader;

public class AsciiToNativeFilter {

	String in;
	
	public AsciiToNativeFilter(String str) {
		this.in = str;
	}
	
	public String read() {
		StringBuffer holder = new StringBuffer();
		StringReader reader = new StringReader(in);
		try  (
		AsciiToNativeFilterReader filter = new AsciiToNativeFilterReader(reader); 
		){
			
			char [] cbuf = new char[1024];
			for(int count = 0;count!=-1; count = filter.read(cbuf)){
				holder.append(cbuf,0,count);
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return holder.toString();
	}

}
