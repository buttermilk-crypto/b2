/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013-2014 David R. Smith for cryptoregistry.com
 *
 */
package asia.redact.bracket.properties.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Set;
import java.util.Map.Entry;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.values.ValueModel;

/**
 * Joins an OutputFormatter with a file to output. RandomAccessFiles can handle 
 * file locking semantics for example if the data is being written to a file share 
 * (shared file system)
 *  
 * @author Dave
 *
 */
public class RandomAccessFileOutputAdapter {

	Properties props;

	public RandomAccessFileOutputAdapter(Properties props) {
		super();
		this.props = props;
	}

	/**
	 * Write it out. The file must be closed externally to this operation.
	 * 
	 */
	public void writeTo(RandomAccessFile file, OutputFormat format) throws IOException {
		Set<Entry<String,ValueModel>> set = props.asMap().entrySet();
		file.writeChars(format.formatHeader());
		for(Entry<String,ValueModel> e: set) {
			String key = e.getKey();
			ValueModel model = e.getValue();
			file.writeBytes(format.format(key, model.getSeparator(),model.getValues(),model.getComments()));
		}
		
		file.writeBytes(format.formatFooter());
	}

}
