package net.sf.testium.configuration;

import java.io.File;

import net.sf.testium.Testium;

import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.TTIException;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * @author Arjan Kranenburg 
 * 
 *  <KeywordDefinitionsWriter>
 *    <OutputBaseDirectory>...</OutputBaseDirectory>
 *    <XslSourceDirectory>...</XslSourceDirectory>
 *  </KeywordDefinitionsWriter>
 * 
 */

public class KeywordDefinitionsWriterXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "KeywordDefinitionsWriter";

	private static final String CFG_OUTPUT_BASE_DIRECTORY = "OutputBaseDirectory";
	private static final String CFG_XSL_DIRECTORY = "XslSourceDirectory";
	private static final String CFG_XSL_FILENAME = "XslFileName";

	public static final String DEFAULT_OUTPUT_BASE_DIR = "keywords";
	public static final String DEFAULT_XSL_FILENAME = "Keywords.xsl";
	
	private final GenericTagAndStringXmlHandler outputBaseDirXmlHandler;
	private final GenericTagAndStringXmlHandler xslSourceDirXmlHandler;
	private final GenericTagAndStringXmlHandler xslFileNameXmlHandler;

	private final RunTimeData rtData;

	private File outputBaseDir;
	private File xslSourceDir;
	private String xslFileName;
	
	public KeywordDefinitionsWriterXmlHandler(XMLReader anXmlReader,
			RunTimeData anRtData ) {
		super(anXmlReader, START_ELEMENT);
		Trace.println(Trace.CONSTRUCTOR);

		rtData = anRtData;

		outputBaseDir = getDefaultOutputBasedir( rtData );
		xslSourceDir = null;
		xslFileName = DEFAULT_XSL_FILENAME;

		outputBaseDirXmlHandler = new GenericTagAndStringXmlHandler(
				anXmlReader, CFG_OUTPUT_BASE_DIRECTORY);
		this.addElementHandler(outputBaseDirXmlHandler);

		xslSourceDirXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader,
				CFG_XSL_DIRECTORY);
		this.addElementHandler(xslSourceDirXmlHandler);

		xslFileNameXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader,
				CFG_XSL_FILENAME);
		this.addElementHandler(xslFileNameXmlHandler);
	}

	@Override
	public void handleStartElement(String aQualifiedName) {
		// nop
	}

	@Override
	public void handleCharacters(String aValue) {
		// nop
	}

	@Override
	public void handleEndElement(String aQualifiedName) {
		// nop
	}

	@Override
	public void processElementAttributes(String aQualifiedName, Attributes att) {
		// nop
	}

	@Override
	public void handleGoToChildElement(String aQualifiedName) {
		// nop
	}

	@Override
	public void handleReturnFromChildElement(String aQualifiedName,
			XmlHandler aChildXmlHandler) throws TTIException {
		Trace.println(Trace.UTIL, "handleReturnFromChildElement( "
				+ aQualifiedName + " )", true);

		if (aQualifiedName.equalsIgnoreCase(CFG_OUTPUT_BASE_DIRECTORY)) {
			String outputBaseDirName = outputBaseDirXmlHandler.getValue();
			outputBaseDirXmlHandler.reset();

			outputBaseDirName = rtData.substituteVars(outputBaseDirName);
			outputBaseDir = new File ( outputBaseDirName );

		} else if (aQualifiedName.equalsIgnoreCase(CFG_XSL_DIRECTORY)) {
			String xslDirName = xslSourceDirXmlHandler.getValue();
			xslSourceDirXmlHandler.reset();

			xslDirName = rtData.substituteVars(xslDirName);
			xslSourceDir = new File( xslDirName );
		} else if (aQualifiedName.equalsIgnoreCase(CFG_XSL_FILENAME)) {
			xslFileName = xslFileNameXmlHandler.getValue();
			xslFileNameXmlHandler.reset();
		}
		// else ignore
	}

	public KeywordDefinitionsConfiguration getConfiguration() {
		return new KeywordDefinitionsConfiguration( outputBaseDir, xslSourceDir, xslFileName );
	}

	/**
	 * 
	 */
	public static File getDefaultOutputBasedir(RunTimeData rtData ) {
		File baseDir = rtData.getValueAsFile(Testium.BASEDIR).getParentFile().getParentFile();
		return new File( baseDir, DEFAULT_OUTPUT_BASE_DIR );
	}
}
