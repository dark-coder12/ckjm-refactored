package gr.spinellis.ckjm;

import org.apache.bcel.classfile.*;

import java.io.*;
import java.util.Map;

public class MetricsFilter {
	private static boolean includeJdk = false;
	private static boolean onlyPublic = false;

	public static boolean isJdkIncluded() {
		return includeJdk;
	}

	public static boolean includeAll() {
		return !onlyPublic;
	}

	private static void parseArguments(String[] args, int argp, String parameter, boolean flag) {
		if (args.length > argp && args[argp].equals(parameter)) {
			flag = true;
			argp++;
		}
	}

	public static void runMetrics(String[] files, CkjmOutputHandler outputHandler) {
		ClassMetricsContainer cm = new ClassMetricsContainer();

		for (String file : files) {
			JavaClass jc = ClassLoader.loadClass(file);
			ClassParser.parseClass(jc, cm);
		}

		Map<String, ClassMetrics> metricsMap = cm.getClassMetricsMap();

		MetricsPrinter printer = new MetricsPrinter(new PrintPublicVisitedStrategy());
		printer.printMetrics(outputHandler, metricsMap);
	}

	public static void main(String[] args) {
		int argp = 0;
		parseArguments(args, argp, "-s", includeJdk);
		parseArguments(args, argp, "-p", onlyPublic);

		ClassMetricsContainer cm = new ClassMetricsContainer();

		if (args.length == 0) {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			try {
				String s;
				while ((s = in.readLine()) != null) {
					JavaClass jc = ClassLoader.loadClass(s);
					ClassParser.parseClass(jc, cm);
				}
			} catch (Exception e) {
				System.err.println("Error reading line: " + e);
				System.exit(1);
			}
		}

		for (String arg : args) {
			JavaClass jc = ClassLoader.loadClass(arg);
			ClassParser.parseClass(jc, cm);
		}

		Map<String, ClassMetrics> metricsMap = cm.getClassMetricsMap();

		CkjmOutputHandler handler = new PrintPlainResults(System.out);
		MetricsPrinter printer = new MetricsPrinter(new PrintPublicVisitedStrategy());
		printer.printMetrics(handler, metricsMap);
	}
}