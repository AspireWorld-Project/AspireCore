package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final CrashReport theReportedExceptionCrashReport;
	public ReportedException(CrashReport p_i1356_1_) {
		theReportedExceptionCrashReport = p_i1356_1_;
	}

	public CrashReport getCrashReport() {
		return theReportedExceptionCrashReport;
	}

	@Override
	public Throwable getCause() {
		return theReportedExceptionCrashReport.getCrashCause();
	}

	@Override
	public String getMessage() {
		return theReportedExceptionCrashReport.getDescription();
	}
}