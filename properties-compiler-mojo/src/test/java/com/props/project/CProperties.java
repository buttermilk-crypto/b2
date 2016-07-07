package com.props.project;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.impl.PropertiesImpl;
import asia.redact.bracket.properties.values.BasicValueModel;
import asia.redact.bracket.properties.values.Comment;

public final class CProperties extends PropertiesImpl {
  public static final long serialVersionUID = 1;

  public CProperties() {
    super(true);
  }

  public Properties init() {
    super.init();
    final String [] svar0 = {"CONSOLE, FILE"};
    map.put("log4j.rootLogger", new BasicValueModel(new Comment("#  define the root logger with two appenders writing to console and file"), '=', svar0));
    final String [] svar1 = {"com.foo.MyLogger"};
    map.put("log4j.logger.com.foo", new BasicValueModel(new Comment("# define your own logger named com.foo"), '=', svar1));
    final String [] svar2 = {"FILE"};
    map.put("log4j.logger.com.foo.appender", new BasicValueModel(new Comment("# assign appender to your own logger"), '=', svar2));
    final String [] svar3 = {"org.apache.log4j.FileAppender"};
    map.put("log4j.appender.FILE", new BasicValueModel(new Comment("# define the appender named FILE "), '=', svar3));
    final String [] svar4 = {"${user.home}/log.out"};
    map.put("log4j.appender.FILE.File", new BasicValueModel(new Comment(""), '=', svar4));
    final String [] svar5 = {"org.apache.log4j.ConsoleAppender"};
    map.put("log4j.appender.CONSOLE", new BasicValueModel(new Comment("# define the appender named CONSOLE"), '=', svar5));
    final String [] svar6 = {"%m%n"};
    map.put("log4j.appender.CONSOLE.conversionPattern", new BasicValueModel(new Comment(""), '=', svar6));
    return this;
  }
}
