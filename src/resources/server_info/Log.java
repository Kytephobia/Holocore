package resources.server_info;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Log {
	
	private static final DateFormat LOG_FORMAT = new SimpleDateFormat("dd-mm-yy HH:mm:ss.SSS");
	private static final Log LOG = new Log("log.txt");
	
	private final File file;
	private BufferedWriter writer;
	private boolean open;
	
	private Log(String filename) {
		this.file = new File(filename);
		open = false;
	}
	
	private synchronized void open() throws IOException {
		if (!open)
			writer = new BufferedWriter(new FileWriter(file));
		open = true;
	}
	
	private synchronized void close() throws IOException {
		if (open)
			writer.close();
		open = false;
	}
	
	private synchronized void write(String str) throws IOException {
		if (open) {
			writer.write(str);
			writer.newLine();
			writer.flush();
		}
	}
	
	protected static final void start() {
		try {
			LOG.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static final void stop() {
		try {
			LOG.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Logs the string to the server log file, formatted to display the log
	 * severity, time, tag and message.
	 * @param level the log level of this message between INFO and ASSERT
	 * @param tag the tag to use for the log
	 * @param str the format string for the log
	 * @param args the string format arguments, if specified
	 */
	public static final void log(LogLevel level, String tag, String str, Object ... args) {
		String date = LOG_FORMAT.format(System.currentTimeMillis());
		String logStr = String.format(str, args);
		String log = String.format("%s %c/[%s]: %s", date, level.getChar(), tag, logStr);
		synchronized (LOG) {
			try {
				LOG.write(log);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Logs the string to the server log file, formatted to display the log
	 * severity as INFO, as well as the time, tag and message.
	 * @param level the log level of this message between INFO and ASSERT
	 * @param tag the tag to use for the log
	 * @param str the format string for the log
	 * @param args the string format arguments, if specified
	 */
	public static final void i(String tag, String message, Object ... args) {
		log(LogLevel.INFO, tag, message, args);
	}
	
	/**
	 * Logs the string to the server log file, formatted to display the log
	 * severity as DEBUG, as well as the time, tag and message.
	 * @param level the log level of this message between INFO and ASSERT
	 * @param tag the tag to use for the log
	 * @param str the format string for the log
	 * @param args the string format arguments, if specified
	 */
	public static final void d(String tag, String message, Object ... args) {
		log(LogLevel.DEBUG, tag, message, args);
	}
	
	/**
	 * Logs the string to the server log file, formatted to display the log
	 * severity as WARN, as well as the time, tag and message.
	 * @param level the log level of this message between INFO and ASSERT
	 * @param tag the tag to use for the log
	 * @param str the format string for the log
	 * @param args the string format arguments, if specified
	 */
	public static final void w(String tag, String message, Object ... args) {
		log(LogLevel.WARN, tag, message, args);
	}
	
	/**
	 * Logs the string to the server log file, formatted to display the log
	 * severity as ERROR, as well as the time, tag and message.
	 * @param level the log level of this message between INFO and ASSERT
	 * @param tag the tag to use for the log
	 * @param str the format string for the log
	 * @param args the string format arguments, if specified
	 */
	public static final void e(String tag, String message, Object ... args) {
		log(LogLevel.ERROR, tag, message, args);
	}
	
	/**
	 * Logs the string to the server log file, formatted to display the log
	 * severity as ASSERT, as well as the time, tag and message.
	 * @param level the log level of this message between INFO and ASSERT
	 * @param tag the tag to use for the log
	 * @param str the format string for the log
	 * @param args the string format arguments, if specified
	 */
	public static final void a(String tag, String message, Object ... args) {
		log(LogLevel.ASSERT, tag, message, args);
	}
	
	public static enum LogLevel {
		INFO	('I'),
		DEBUG	('D'),
		WARN	('W'),
		ERROR	('E'),
		ASSERT	('A');
		
		private char c;
		
		LogLevel(char c) {
			this.c = c;
		}
		
		public char getChar() { return c; }
	}
	
}