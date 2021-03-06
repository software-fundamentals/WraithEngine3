package util_handling;

import java.io.PrintWriter;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.utils.logging.Log;
import net.whg.we.utils.logging.LogOutput;
import net.whg.we.utils.logging.LogPrintWriterOut;
import net.whg.we.utils.logging.LogProperty;

public class LogTest
{
	@Test
	public void logProperty_disposing()
	{
		LogProperty property = new LogProperty();
		Assert.assertNotEquals(property.getPropertyCount(), 0);

		property.dispose();

		Assert.assertEquals(property.getPropertyCount(), 0);
	}

	@Test
	public void logProperty_setProperty()
	{
		LogProperty property = new LogProperty();
		property.setProperty("1", "abc");

		String val = property.getProperty("1");

		Assert.assertEquals(val, "abc");
	}

	@Test
	public void logProperty_getNullProperty()
	{
		LogProperty property = new LogProperty();
		Assert.assertNull(property.getProperty("1"));
	}

	@Test
	public void logProperty_timestamp()
	{
		LogProperty property = new LogProperty();
		LocalTime time = LocalTime.of(14, 4, 16);

		property.setTimeStamp(time);

		Assert.assertEquals(property.getTimeStamp(), "14:04:16");
	}

	@Test
	public void logProperty_threadName()
	{
		LogProperty property = new LogProperty();

		property.setThreadName("main");

		Assert.assertEquals(property.getThreadName(), "main");
	}

	@Test
	public void logProperty_message()
	{
		LogProperty property = new LogProperty();

		property.setMessage("Test Message");

		Assert.assertEquals(property.getMessage(), "Test Message");
	}

	@Test
	public void logProperty_message_format()
	{
		LogProperty property = new LogProperty();

		property.setMessage("Test Message, %s: %d", "Value", 3);

		Assert.assertEquals(property.getMessage(), "Test Message, Value: 3");
	}

	@Test
	public void logProperty_clear_property()
	{
		LogProperty property = new LogProperty();

		property.setProperty("Color", "Red");
		property.clearProperty("Color");

		Assert.assertNull(property.getProperty("Color"));
	}

	@Test
	public void logProperty_clearMainProperties()
	{
		LogProperty property = new LogProperty();

		property.clearProperty(LogProperty.MESSAGE_PROPERTY);
		property.clearProperty(LogProperty.SEVERITY_PROPERTY);
		property.clearProperty(LogProperty.THREAD_PROPERTY);
		property.clearProperty(LogProperty.TIME_PROPERTY);
		property.clearProperty(LogProperty.INDENT_PROPERTY);

		Assert.assertNotNull(property.getProperty(LogProperty.MESSAGE_PROPERTY));
		Assert.assertNotNull(property.getProperty(LogProperty.SEVERITY_PROPERTY));
		Assert.assertNotNull(property.getProperty(LogProperty.THREAD_PROPERTY));
		Assert.assertNotNull(property.getProperty(LogProperty.TIME_PROPERTY));
		Assert.assertNotNull(property.getProperty(LogProperty.INDENT_PROPERTY));
	}

	@Test
	public void logProperty_severity()
	{
		LogProperty property = new LogProperty();

		property.setSeverity(Log.TRACE);
		Assert.assertEquals(property.getSeverity(), "Trace");

		property.setSeverity(Log.DEBUG);
		Assert.assertEquals(property.getSeverity(), "Debug");

		property.setSeverity(Log.INFO);
		Assert.assertEquals(property.getSeverity(), "Info");

		property.setSeverity(Log.WARN);
		Assert.assertEquals(property.getSeverity(), "Warn");

		property.setSeverity(Log.ERROR);
		Assert.assertEquals(property.getSeverity(), "Error");

		property.setSeverity(Log.FATAL);
		Assert.assertEquals(property.getSeverity(), "Fatal");
	}

	@Test(expected = IllegalArgumentException.class)
	public void logProperty_wrongSeverity()
	{
		LogProperty property = new LogProperty();
		property.setSeverity(-1);
	}

	@Test
	public void logProperty_init()
	{
		LogProperty property = new LogProperty();

		Assert.assertNotNull(property.getTimeStamp());
		Assert.assertNotNull(property.getThreadName());
		Assert.assertNotNull(property.getMessage());
		Assert.assertNotNull(property.getSeverity());
		Assert.assertNotNull(property.getIndent());
	}

	@Test(expected = IllegalArgumentException.class)
	public void logProperty_setNegativeIndent()
	{
		new LogProperty().setIndent(-1);
	}

	@Test
	public void logProperty_toString()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message");
		property.setSeverity(Log.WARN);

		Assert.assertEquals("[10:08:06][Warn][main] Test Message", property.toString());
	}

	@Test
	public void logProperty_toString_Lines()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message\nLine2");
		property.setSeverity(Log.WARN);

		Assert.assertEquals("[10:08:06][Warn][main] Test Message\n[10:08:06][Warn][main] Line2",
				property.toString());
	}

	@Test
	public void logProperty_toString_Indent()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message");
		property.setSeverity(Log.WARN);
		property.setIndent(1);

		Assert.assertEquals(2, Log.SPACES_PER_INDENT);

		Assert.assertEquals("[10:08:06][Warn][main]   Test Message", property.toString());
	}

	@Test
	public void logProperty_toString_BigIndent()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message");
		property.setSeverity(Log.WARN);
		property.setIndent(5);

		Assert.assertEquals(2, Log.SPACES_PER_INDENT);

		Assert.assertEquals("[10:08:06][Warn][main]           Test Message", property.toString());
	}

	@Test
	public void logProperty_toString_Indent_Lines()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message\nLine2");
		property.setSeverity(Log.WARN);
		property.setIndent(1);

		Assert.assertEquals(2, Log.SPACES_PER_INDENT);

		Assert.assertEquals("[10:08:06][Warn][main]   Test Message\n[10:08:06][Warn][main]   Line2",
				property.toString());
	}

	@Test
	public void logProperty_toMapString()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message");
		property.setSeverity(Log.WARN);
		property.setProperty("abc", "def");

		Assert.assertEquals(
				"{Indent=0, abc=def, Message=Test Message, Time=10:08:06, Severity=Warn, Thread=main}",
				property.toMapString());
	}

	@Test
	public void logProperty_clone()
	{
		LogProperty original = new LogProperty().setMessage("abc");
		LogProperty clone = new LogProperty(original);

		Assert.assertEquals("abc", clone.getMessage());
	}

	@Test
	public void log_setLogLevel()
	{
		Log.dispose();
		Log.setLogLevel(Log.DEBUG);

		Assert.assertEquals(Log.DEBUG, Log.getLogLevel());
	}

	@Test(expected = IllegalArgumentException.class)
	public void log_setNegativeLogLevel()
	{
		Log.dispose();
		Log.setLogLevel(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void log_setLargeLogLevel()
	{
		Log.dispose();
		Log.setLogLevel(Log.FATAL + 1);
	}

	@Test
	public void log_levels_are_continous()
	{
		Assert.assertEquals(0, Log.TRACE);
		Assert.assertEquals(1, Log.DEBUG);
		Assert.assertEquals(2, Log.INFO);
		Assert.assertEquals(3, Log.WARN);
		Assert.assertEquals(4, Log.ERROR);
		Assert.assertEquals(5, Log.FATAL);
	}

	@Test
	public void log_setOutput_PrintWriter()
	{
		Log.dispose();
		PrintWriter pw = Mockito.mock(PrintWriter.class);
		Log.setOutput(new LogPrintWriterOut(pw));

		Log.info("Hello");

		Mockito.verify(pw).println(Mockito.anyString());
	}

	@Test
	public void log_ignoreLowLogLevel()
	{
		Log.dispose();
		Log.setLogLevel(Log.INFO);

		PrintWriter pw = Mockito.mock(PrintWriter.class);
		Log.setOutput(new LogPrintWriterOut(pw));

		Log.trace("Hello");

		Mockito.verify(pw, Mockito.never()).println(Mockito.anyString());
	}

	@Test
	public void log_defaultLogLevel()
	{
		Log.dispose();
		Assert.assertEquals(Log.INFO, Log.getLogLevel());
	}

	@Test
	public void log_defaultOut()
	{
		Log.dispose();

		LogPrintWriterOut out = (LogPrintWriterOut) Log.getOutput();
		Assert.assertNotNull(out.getPrintWriter());
	}

	@Test
	public void log_logProperty()
	{
		Log.dispose();

		LogOutput out = Mockito.mock(LogOutput.class);
		Log.setOutput(out);

		LogProperty prop = Log.get();
		Log.log(prop);

		Mockito.verify(out).println(prop);
	}

	private class HoldLastProperty implements LogOutput
	{
		LogProperty _last;

		@Override
		public void println(LogProperty property)
		{
			_last = new LogProperty(property);
		}
	}

	@Test
	public void log_logFatal()
	{
		Log.dispose();

		HoldLastProperty out = new HoldLastProperty();
		Log.setOutput(out);

		Log.fatalf("An error has occured on line %d!", new RuntimeException(), 42);

		Assert.assertEquals("Fatal", out._last.getSeverity());
		Assert.assertEquals("An error has occured on line 42!", out._last.getMessage());
		Assert.assertNotNull(out._last.getProperty("Exception"));

		Log.fatalf("An error has occured on line %d!", 128);

		Assert.assertEquals("Fatal", out._last.getSeverity());
		Assert.assertEquals("An error has occured on line 128!", out._last.getMessage());
		Assert.assertNull(out._last.getProperty("Exception"));
	}

	@Test
	public void log_normal()
	{
		Log.dispose();
		Log.setLogLevel(Log.TRACE);

		HoldLastProperty out = new HoldLastProperty();
		Log.setOutput(out);

		Log.trace("Trace");
		Assert.assertEquals("Trace", out._last.getMessage());

		Log.debug("Debug");
		Assert.assertEquals("Debug", out._last.getMessage());

		Log.info("Info");
		Assert.assertEquals("Info", out._last.getMessage());

		Log.warn("Warn");
		Assert.assertEquals("Warn", out._last.getMessage());

		Log.error("Error");
		Assert.assertEquals("Error", out._last.getMessage());

		Log.fatal("Fatal");
		Assert.assertEquals("Fatal", out._last.getMessage());
	}

	@Test
	public void log_normal_args()
	{
		Log.dispose();
		Log.setLogLevel(Log.TRACE);

		HoldLastProperty out = new HoldLastProperty();
		Log.setOutput(out);

		Log.tracef("Trace %s", "Args");
		Assert.assertEquals("Trace Args", out._last.getMessage());

		Log.debugf("Debug %s", "Args");
		Assert.assertEquals("Debug Args", out._last.getMessage());

		Log.infof("Info %s", "Args");
		Assert.assertEquals("Info Args", out._last.getMessage());

		Log.warnf("Warn %s", "Args");
		Assert.assertEquals("Warn Args", out._last.getMessage());

		Log.errorf("Error %s", "Args");
		Assert.assertEquals("Error Args", out._last.getMessage());

		Log.fatalf("Fatal %s", "Args");
		Assert.assertEquals("Fatal Args", out._last.getMessage());
	}

	@Test
	public void log_ignoreLogLevel()
	{
		Log.dispose();
		Log.setLogLevel(Log.FATAL);

		LogOutput out = Mockito.mock(LogOutput.class);
		Log.setOutput(out);

		Log.trace("Trace");
		Log.tracef("Tracef");
		Log.debug("Debug");
		Log.debugf("Debugf");
		Log.info("Info");
		Log.infof("Infof");
		Log.warn("Warn");
		Log.warnf("Warnf");
		Log.error("Error");
		Log.errorf("Errorf");
		Log.errorf("ErrorfException", new RuntimeException());

		Mockito.verify(out, Mockito.never()).println(Mockito.any());
	}

	@Test
	public void log_indention()
	{
		Log.dispose();

		HoldLastProperty out = new HoldLastProperty();
		Log.setOutput(out);

		Log.info("Message1");
		Assert.assertEquals(0, out._last.getIndent());

		Log.indent();
		Log.info("Message2");
		Assert.assertEquals(1, out._last.getIndent());
		Log.unindent();
		Log.info("Message3");
		Assert.assertEquals(0, out._last.getIndent());
	}

	@Test
	public void log_indentManually()
	{
		Log.dispose();

		HoldLastProperty out = new HoldLastProperty();
		Log.setOutput(out);

		Log.setIndentLevel(10);
		Log.info("Message1");
		Assert.assertEquals(10, out._last.getIndent());

		Assert.assertEquals(10, Log.getIndentLevel());

		Log.resetIndent();
		Assert.assertEquals(0, Log.getIndentLevel());

		Log.setIndentLevel(-1);
		Assert.assertEquals(0, Log.getIndentLevel());
	}

	@Test
	public void log_unindentTooMuch()
	{
		Log.dispose();

		HoldLastProperty out = new HoldLastProperty();
		Log.setOutput(out);

		Log.unindent();
		Log.info("Message1");
		Assert.assertEquals(0, out._last.getIndent());
	}
}
