package com.props.project;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.impl.PojoPropertiesImpl;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.Entry;

public final class CPropertiesPojo extends PojoPropertiesImpl {
  public static final long serialVersionUID = 1;

  public final Entry log4jrootLogger = new Entry("log4j.rootLogger", new Comment("#  define the root logger with two appenders writing to console and file"), "CONSOLE, FILE");

  public final Entry log4jloggercomfoo = new Entry("log4j.logger.com.foo", new Comment("# define your own logger named com.foo"), "com.foo.MyLogger");

  public final Entry log4jloggercomfooappender = new Entry("log4j.logger.com.foo.appender", new Comment("# assign appender to your own logger"), "FILE");

  public final Entry log4jappenderFILE = new Entry("log4j.appender.FILE", new Comment("# define the appender named FILE "), "org.apache.log4j.FileAppender");

  public final Entry log4jappenderFILEFile = new Entry("log4j.appender.FILE.File", new Comment(""), "${user.home}/log.out");

  public final Entry log4jappenderCONSOLE = new Entry("log4j.appender.CONSOLE", new Comment("# define the appender named CONSOLE"), "org.apache.log4j.ConsoleAppender");

  public final Entry log4jappenderCONSOLEconversionPattern = new Entry("log4j.appender.CONSOLE.conversionPattern", new Comment(""), "%m%n");

  public CPropertiesPojo() {
    super();
  }

  public Properties init() {
    entries = new Entry[7];
    entries[0] = log4jrootLogger;
    entries[1] = log4jloggercomfoo;
    entries[2] = log4jloggercomfooappender;
    entries[3] = log4jappenderFILE;
    entries[4] = log4jappenderFILEFile;
    entries[5] = log4jappenderCONSOLE;
    entries[6] = log4jappenderCONSOLEconversionPattern;
    return this;
  }
}
