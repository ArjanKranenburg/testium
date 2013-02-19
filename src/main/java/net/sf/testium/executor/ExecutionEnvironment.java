package net.sf.testium.executor;

import java.io.File;

import org.testtoolinterfaces.utils.RunTimeData;

public class ExecutionEnvironment {
	private File scriptDir;
	private File logDir;
	private RunTimeData rtData;

	public ExecutionEnvironment(File scriptDir, File logDir,
			RunTimeData rtData) {
		this.scriptDir = scriptDir;
		this.logDir = logDir;
		this.rtData = rtData;
	}

	public File getLogFile( String anId ) {
		return new File(this.getLogDir(), anId + "_log.xml");
	}

	/**
	 * @return the scriptDir
	 */
	public File getScriptDir() {
		return scriptDir;
	}
	
	/**
	 * @param scriptDir the scriptDir to set
	 */
	public void setScriptDir(File scriptDir) {
		this.scriptDir = scriptDir;
	}

	/**
	 * @return the logDir
	 */
	public File getLogDir() {
		return logDir;
	}

	/**
	 * @param logDir the logDir to set
	 */
	public void setLogDir(File logDir) {
		this.logDir = logDir;
	}

	/**
	 * @return the rtData
	 */
	public RunTimeData getRtData() {
		return rtData;
	}

	/**
	 * @param rtData the rtData to set
	 */
	public void setRtData(RunTimeData rtData) {
		this.rtData = rtData;
	}
}
