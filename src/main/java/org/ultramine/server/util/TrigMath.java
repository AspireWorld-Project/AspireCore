package org.ultramine.server.util;

import net.jafama.FastMath;

public class TrigMath {
	static final double sq2p1 = 2.414213562373095048802e0;
	static final double sq2m1 = .414213562373095048802e0;
	static final double p4 = .161536412982230228262e2;
	static final double p3 = .26842548195503973794141e3;
	static final double p2 = .11530293515404850115428136e4;
	static final double p1 = .178040631643319697105464587e4;
	static final double p0 = .89678597403663861959987488e3;
	static final double q4 = .5895697050844462222791e2;
	static final double q3 = .536265374031215315104235e3;
	static final double q2 = .16667838148816337184521798e4;
	static final double q1 = .207933497444540981287275926e4;
	static final double q0 = .89678597403663861962481162e3;
	static final double PIO2 = 1.5707963267948966135E0;

	private static double mxatan(double arg) {
		double argsq = arg * arg;
		return ((((p4 * argsq + p3) * argsq + p2) * argsq + p1) * argsq + p0)
				/ (((((argsq + q4) * argsq + q3) * argsq + q2) * argsq + q1) * argsq + q0) * arg;
	}

	private static double msatan(double arg) {
		return arg < sq2m1 ? mxatan(arg)
				: arg > sq2p1 ? PIO2 - mxatan(1 / arg) : PIO2 / 2 + mxatan((arg - 1) / (arg + 1));
	}

	public static double atan(double arg) {
		return FastMath.atan(arg);
	}

	public static double atan2(double arg1, double arg2) {
		return FastMath.atan2(arg1, arg2);
	}
}
