package asia.redact.bracket.properties.line;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Representation of a line of text in a properties file. Index is the absolute offset of this line in the 
 * file at the time it was read. The line end is not included in "text", it is found in the "ending" variable.</p>
 * 
 * <p>This class is thread-safe.</p>
 * 
 * @author Dave
 *
 */

public class Line implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	final String text;
	final long index; // in file
	final LineEnding ending; // not included in the text
	
	public Line(String text, long index, LineEnding ending) {
		this.text = text;
		this.index = index;
		this.ending = ending;
	}
	
	/**
	 * Tokenize a line of text from a properties file.
	 * 
	 * @return
	 */
	public List<PropertiesToken> tokens(){
		
		List<PropertiesToken> list = new ArrayList<PropertiesToken>();
		
		if(isEmptyLine()){
			list.add(nominalEnding());
			return list;
		}
		if(isPrivateComment()){
			return list;
		}
			
		if(isCommentLine()){
			list.add(new PropertiesToken(PropertiesTokenType.COMMENT,commentContents()));
			list.add(nominalEnding());
			return list;
		}
		
		if(isNaturalLine()){	
			String [] parts = naturalLineContents();
			list.add(new PropertiesToken(PropertiesTokenType.KEY,parts[0]));
			list.add(new PropertiesToken(PropertiesTokenType.SEPARATOR,parts[1]));
			list.add(new PropertiesToken(PropertiesTokenType.VALUE,parts[2]));
			list.add(nominalEnding());
		}else{
			list.add(new PropertiesToken(PropertiesTokenType.VALUE,logicalLineContents()));
			list.add(nominalEnding());
		}
		
		return list;
	}
	
	/**
	 * Return true if this line is a value extension of a line (part of a logical line)
	 * 
	 * @return
	 */
	public boolean hasContinuation(){
		if(text.length()==0)return false;
		if(text.charAt(text.length()-1)=='\\')return true;
		return false;
	}
	
	/**
	 * Used to identify our private (transient) comment system for a comment header
	 * 
	 * @return
	 */
	public boolean isPrivateComment(){
		for(int i=0;i<text.length();i++){
			char ch = text.charAt(i);
			if(Character.isWhitespace(ch)) continue;
			// first non-whitespace char is here
			if(ch=='#' && text.length()>i+2){
				if(text.charAt(i+1)==';'){
					if(text.charAt(i+2)==';'){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public String logicalLineContents(){
		
		// count any initial whitespace on continuation line
		int countBlank = 0;
		for(int i =0;i<text.length();i++){
			if(Character.isWhitespace(text.charAt(i))){
				countBlank++;
				continue;
			}else{
				break;
			}
		}
		
		// removes initial whitespace on the continuation as per spec
		if(hasContinuation()){
			if(countBlank>0) {
				return text.substring(countBlank,text.length()-1);
			}else {
				return text.substring(0, text.length()-1);
			}
		}else{
			// if end of logical lines, check for trailing whitespace and trim it
			
			int countTrailingBlank = 0;
			for(int i=text.length()-1;i>0;i--){
				if(Character.isWhitespace(text.charAt(i))){
					countTrailingBlank++;
					continue;
				}else{
					break;
				}
			}
			
			return text.substring(countBlank,text.length()-countTrailingBlank);
			
		}
	}
	
	/**
	 * Natural lines contain keys, a delimiter, and a value (or initial value part)
	 * 
	 * @return
	 */
	public boolean isNaturalLine(){
		int sepIndex=-1;
		char previous = '\0';
		char ch = '\0';
		for(int i=0;i<text.length();i++){
		    previous=ch;
			ch = text.charAt(i);
			if((ch == '='||ch==':') && previous != '\\'){
				//I see an actual delimiter
				sepIndex=i;
				break;
			}
		}
		return sepIndex>=0;
	}
	
	public boolean isCommentLine(){
		for(int i=0;i<text.length();i++){
			char ch = text.charAt(i);
			if(Character.isWhitespace(ch)) continue;
			// first non-whitespace char is here
			return ch=='#' || ch=='!';
		}
		return false;
	}
	
	public boolean isEmptyLine(){
		return text == null || text.trim().length()==0;
	}
	
	public String commentContents(){
		int startComment=0;
		for(int i=0;i<text.length();i++){
			char ch = text.charAt(i);
			if(Character.isWhitespace(ch)) continue;
			// first non-whitespace char is here
			if(ch=='#' || ch=='!'){
					startComment=i;
					break;
			}else{
				throw new RuntimeException("not a comment line, use method isCommentLine() as guard");
			}
		}
		return text.substring(startComment+1);
	}
	
	public String [] naturalLineContents(){
		String [] retVals = new String[3];
		
		// initial whitespace
		int countBlank = 0;
		for(int i =0;i<text.length();i++){
			if(Character.isWhitespace(text.charAt(i))){
				countBlank++;
				continue;
			}else{
				break;
			}
		}
		
		String line = null;
		if(countBlank>0)line = text.substring(countBlank);
		else line = text;
		
		int sepIndex=-1;
		char previous = '\0';
		char ch = '\0';
		boolean sawEscapedDelimiter=false;
		for(int i=0;i<line.length();i++){
		    previous=ch;
			ch = line.charAt(i);
			if((ch == '='||ch==':') && previous == '\\'){
				sawEscapedDelimiter=true;
			}
			if((ch == '='||ch==':') && previous != '\\'){
				//I see an actual delimiter
				sepIndex=i;
				break;
			}
		}
		
		if(sepIndex==-1){
			throw new RuntimeException("not a natural line (no key), use method isNaturalLine() as guard");
		}
		
		//first purge escaped delimiters from the key, if needed
		if(sawEscapedDelimiter){
			String key = line.substring(0, sepIndex);
			previous = '\0';
			ch = '\0';
			StringBuilder b = new StringBuilder();
			for(int i=0;i<key.length();i++){
			    previous=ch;
				ch = line.charAt(i);
				if((ch == '='||ch==':') && previous == '\\'){
					b.deleteCharAt(b.length()-1);
				}
				b.append(ch);
			}
			retVals[0] = b.toString();
		}else{
			retVals[0] = line.substring(0, sepIndex);
		}
		
		// Issue #1 - check for end of key white space and if found, trim it
		if(retVals[0].endsWith(" ") || retVals[0].endsWith("\t")){
			retVals[0] = retVals[0].trim();
			
		}
		
		retVals[1]=String.valueOf(line.charAt(sepIndex));
		if(hasContinuation()){
			
			// first look for whitespace at start of value
			// start at sepIndex
			countBlank = 0;
			for(int i=sepIndex+1; i<text.length(); i++){
				if(Character.isWhitespace(text.charAt(i))){
					countBlank++;
					continue;
				}else{
					break;
				}
			}
			retVals[2]=line.substring(sepIndex+1+countBlank,line.length()-1);
		}else{
			// if no continuation, trim end whitespace if there is any
			retVals[2]=line.substring(sepIndex+1).trim();
		}
		
		return retVals;
	}

	public String getText() {
		return text;
	}

	public long getIndex() {
		return index;
	}

	public LineEnding getEnding() {
		return ending;
	}
	
	private PropertiesToken nominalEnding(){
		boolean has = hasContinuation();
			switch(ending){
				case CRLF: {
					if(has) return PropertiesToken.ESCAPED_CRLF;
					else return PropertiesToken.CRLF;
				}
				case CR: {
					if(has) return PropertiesToken.ESCAPED_CR;
					else return PropertiesToken.CR;
				}
				case LF: {
					if(has) return PropertiesToken.ESCAPED_LF;
					else return PropertiesToken.LF;
				}
				default: return null;
			}
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append(index);
		buf.append(", ");
		buf.append(text);
		buf.append(", ");
		buf.append(ending);
		return buf.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ending == null) ? 0 : ending.hashCode());
		result = prime * result + (int) (index ^ (index >>> 32));
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (ending != other.ending)
			return false;
		if (index != other.index)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}