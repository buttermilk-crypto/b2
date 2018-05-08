/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.line;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;

/**
 * <p>This class's original source code was taken from <b>Apache Harmony</b> project. That
 * source-code repo is no longer online.</p>
 *  
 * <p>Read a properties file line-by-line using line().</p>
 * 
 * <p>The idea is that as we read lines, we return not the line of text but a Line object which
 * has knowledge of its internal parts, such as if the line has a key, is an extended line, etc.</p>
 * 
 * <p>There is one extension: a comment line which starts with #;; is treated as transient (not read in). 
 * This token is used later to generate a transient header and footer if desired.</p>
 * 
 * <p>Note: because this is a Reader, the caller must call close() on it when done.</p>
 * 
 * @see Line
 * 
 */
public class LineScanner extends Reader {

    private final Reader in;

    private char[] buf;
    private int pos;
    private int end;
    private int mark = -1;
    private int markLimit = -1;
    private long totalRead=0;
    private int delimiterLength;
    private LineEnding delimiter;

    public LineScanner(Reader in) {
        super (in);
        this.in = in;
        buf = new char[8192];
    }

    public LineScanner(Reader in, int size) {
        super (in);
        if (size <= 0) {
            throw new IllegalArgumentException("buf must be given a size");
        }
        this.in = in;
        buf = new char[size];
    }

    @Override
    public void close() throws IOException {
        synchronized (lock) {
            if (!isClosed()) {
                in.close();
                buf = null;
            }
        }
    }

    private int fillBuf() throws IOException {
        // assert(pos == end);

        if (mark == -1 || (pos - mark >= markLimit)) {
            /* mark isn't set or has exceeded its limit. use the whole buffer */
            int result = in.read(buf, 0, buf.length);
            if (result > 0) {
                mark = -1;
                pos = 0;
                end = result;
            }
            return result;
        }

        if (mark == 0 && markLimit > buf.length) {
            /* the only way to make room when mark=0 is by growing the buffer */
            int newLength = buf.length * 2;
            if (newLength > markLimit) {
                newLength = markLimit;
            }
            char[] newbuf = new char[newLength];
            System.arraycopy(buf, 0, newbuf, 0, buf.length);
            buf = newbuf;
        } else if (mark > 0) {
            /* make room by shifting the buffered data to left mark positions */
            System.arraycopy(buf, mark, buf, 0, buf.length - mark);
            pos -= mark;
            end -= mark;
            mark = 0;
        }

        /* Set the new position and mark position */
        int count = in.read(buf, pos, buf.length - pos);
        if (count != -1) {
            end += count;
        }
        return count;
    }

    private boolean isClosed() {
        return buf == null;
    }

    @Override
    public void mark(int markLimit) throws IOException {
        if (markLimit < 0) {
            throw new IllegalArgumentException();
        }
        synchronized (lock) {
            if (isClosed()) {
                throw new IOException("Closed reader");
            }
            this .markLimit = markLimit;
            mark = pos;
        }
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public int read() throws IOException {
        synchronized (lock) {
            if (isClosed()) {
                throw new IOException("Buffer closed"); //$NON-NLS-1$
            }
            /* Are there buffered characters available? */
            if (pos < end || fillBuf() != -1) {
            	totalRead++;
                return buf[pos++];
            }
            return -1;
        }
    }

    @Override
    public int read(char[] buffer, int offset, int length)
            throws IOException {
        synchronized (lock) {
            if (isClosed()) {
                throw new IOException("");
            }
            if (offset < 0 || offset > buffer.length - length
                    || length < 0) {
                throw new IndexOutOfBoundsException();
            }
            int outstanding = length;
            while (outstanding > 0) {

                /*
                 * If there are bytes in the buffer, grab those first.
                 */
                int available = end - pos;
                if (available > 0) {
                    int count = available >= outstanding ? outstanding
                            : available;
                    System.arraycopy(buf, pos, buffer, offset, count);
                    pos += count;
                    offset += count;
                    outstanding -= count;
                }

                /*
                 * Before attempting to read from the underlying stream, make
                 * sure we really, really want to. We won't bother if we're
                 * done, or if we've already got some bytes and reading from the
                 * underlying stream would block.
                 */
                if (outstanding == 0
                        || (outstanding < length && !in.ready())) {
                    break;
                }

                // assert(pos == end);

                /*
                 * If we're unmarked and the requested size is greater than our
                 * buffer, read the bytes directly into the caller's buffer. We
                 * don't read into smaller buffers because that could result in
                 * a many reads.
                 */
                if ((mark == -1 || (pos - mark >= markLimit))
                        && outstanding >= buf.length) {
                    int count = in.read(buffer, offset, outstanding);
                    if (count > 0) {
                        offset += count;
                        outstanding -= count;
                        mark = -1;
                    }

                    break; // assume the source stream gave us all that it could
                }

                if (fillBuf() == -1) {
                    break; // source is exhausted
                }
            }

            int count = length - outstanding;
            int retVal = (count > 0 || count == length) ? count : -1;
            totalRead+=retVal;
            return retVal;
        }
    }

    /**
     * This is the interface into the parser, don't use the other public methods (the obligatory ones from Reader).
     * 
     * @return a Line object encapsulating a line
     */
    public Line line(){
    	 synchronized (lock) {
	    	try {
				String text = readLine();
				if(text != null){
					long startIndex = totalRead-text.length()-delimiterLength;
					return new Line(text,startIndex,delimiter);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
    	 }
    }

    String readLine() throws IOException {
        synchronized (lock) {
            if (isClosed()) {
                throw new IOException("Buffer closed"); 
            }
            /* has the underlying stream been exhausted? */
            if (pos == end && fillBuf() == -1) {
                return null;
            }
            for (int charPos = pos; charPos < end; charPos++) {
                char ch = buf[charPos];
                if (ch > '\r') {
                    continue;
                }
                if (ch == '\n') {
                    String res = new String(buf, pos, charPos - pos);
                    pos = charPos + 1;
                    totalRead+=res.length()+1;
                    delimiterLength=1;
                    delimiter=LineEnding.LF;
                    return res;
                } else if (ch == '\r') {
                    String res = new String(buf, pos, charPos - pos);
                    pos = charPos + 1;
                    totalRead+=res.length()+1;
                    delimiterLength=1;
                    delimiter=LineEnding.CR;
                    if (((pos < end) || (fillBuf() != -1))
                            && (buf[pos] == '\n')) {
                    	totalRead++;
                    	delimiterLength=2;
                    	delimiter=LineEnding.CRLF;
                        pos++;
                    }
                    return res;
                }
            }

            char eol = '\0';
            StringBuilder result = new StringBuilder(80);
            /* Typical Line Length */

            result.append(buf, pos, end - pos);
            while (true) {
                pos = end;

                /* Are there buffered characters available? */
                if (eol == '\n') {
                	totalRead+=result.length()+1;
                	delimiterLength=1;
                	delimiter=LineEnding.LF;
                    return result.toString();
                }
                // attempt to fill buffer
                if (fillBuf() == -1) {
                    // characters or null.
                    String tmp = result.length() > 0 || eol != '\0' ? result.toString() : null;
                    if(tmp!=null) totalRead+=result.length();
                    delimiterLength=1;
                    if(eol=='\r')delimiter=LineEnding.CR;
                    else delimiter=LineEnding.LF;
                    return tmp;
                }
                for (int charPos = pos; charPos < end; charPos++) {
                    char c = buf[charPos];
                    if (eol == '\0') {
                        if ((c == '\n' || c == '\r')) {
                            eol = c;
                        }
                    } else if (eol == '\r' && c == '\n') {
                        if (charPos > pos) {
                            result.append(buf, pos, charPos - pos - 1);
                        }
                        pos = charPos + 1;
                        totalRead+=result.length()+2;
                        delimiterLength=2;
                        delimiter=LineEnding.CRLF;
                        return result.toString();
                    } else {
                    	
                        if (charPos > pos) {
                            result.append(buf, pos, charPos - pos - 1);
                        }
                        pos = charPos;
                        totalRead+=result.length()+1;
                        delimiterLength=1;
                      
                       if(eol=='\r') delimiter=LineEnding.CR;
                       else delimiter=LineEnding.LF;
                       return result.toString();
                    }
                }
                if (eol == '\0') {
                    result.append(buf, pos, end - pos);
                } else {
                    result.append(buf, pos, end - pos - 1);
                }
            }
        }

    }

   
    @Override
    public boolean ready() throws IOException {
        synchronized (lock) {
            if (isClosed()) {
                throw new IOException("Buffer closed");
            }
            return ((end - pos) > 0) || in.ready();
        }
    }

    @Override
    public void reset() throws IOException {
        synchronized (lock) {
            if (isClosed()) {
                throw new IOException("Buffer closed"); 
            }
            if (mark == -1) {
                throw new IOException("mark == -1"); 
            }
            pos = mark;
        }
    }

    @Override
    public long skip(long amount) throws IOException {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        synchronized (lock) {
            if (isClosed()) {
                throw new IOException(""); //$NON-NLS-1$
            }
            if (amount < 1) {
                return 0;
            }
            if (end - pos >= amount) {
                pos += amount;
                return amount;
            }

            long read = end - pos;
            pos = end;
            while (read < amount) {
                if (fillBuf() == -1) {
                    return read;
                }
                if (end - pos >= amount - read) {
                    pos += amount - read;
                    return amount;
                }
                // Couldn't get all the characters, skip what we read
                read += (end - pos);
                pos = end;
            }
            totalRead+=amount;
            return amount;
        }
    }

	public long getTotalRead() {
		return totalRead;
	}

	public LineEnding getDelimiter() {
		return delimiter;
	}
}

enum LineEnding {
	CR("\r"),LF("\n"),CRLF("\r\n");
	
	private final String ending;
	
	private LineEnding(String chars){
		this.ending=chars;
	}

	public String getEnding() {
		return ending;
	}
}

/**
 * Container for token type and text
 * 
 * @author Dave
 *
 */
class PropertiesToken implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final PropertiesToken EOF = new PropertiesToken(PropertiesTokenType.EOF,"");
	public static final PropertiesToken ESCAPED_CRLF = new PropertiesToken(PropertiesTokenType.LOGICAL_LINE_BREAK,"\\\r\n");
	public static final PropertiesToken ESCAPED_CR = new PropertiesToken(PropertiesTokenType.LOGICAL_LINE_BREAK,"\\\r");
	public static final PropertiesToken ESCAPED_LF = new PropertiesToken(PropertiesTokenType.LOGICAL_LINE_BREAK,"\\\n");
	public static final PropertiesToken CRLF = new PropertiesToken(PropertiesTokenType.NATURAL_LINE_BREAK,"\r\n");
	public static final PropertiesToken CR = new PropertiesToken(PropertiesTokenType.NATURAL_LINE_BREAK,"\r");
	public static final PropertiesToken LF = new PropertiesToken(PropertiesTokenType.NATURAL_LINE_BREAK,"\n");
	
	public final PropertiesTokenType type;
	String text;
	
	public PropertiesToken(PropertiesTokenType type, String text) {
		super();
		this.type = type;
		this.text = text;
	}
	
	public String toString(){
		return new StringBuilder().append(type).append(":").append(text).toString();
	}
	
	public static PropertiesToken eof(){
		return new PropertiesToken(PropertiesTokenType.EOF,"");
	}
}

/**
 * Tokens seen in a scan.
 * 
 * @author Dave
 *
 */
enum PropertiesTokenType {
	META_DATA, 
	NATURAL_LINE_BREAK,
	LOGICAL_LINE_BREAK,
	KEY,
	SEPARATOR,
	VALUE,
	COMMENT,
	EOF;
}

