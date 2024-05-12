package gr.spinellis.ckjm.ant;

import gr.spinellis.ckjm.MetricsFilter;
import gr.spinellis.ckjm.PrintPlainResults;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;

public class CkjmTask extends MatchingTask {
    private File outputFile;

    private File classDir;

    private Path extdirs;

    private String format;

    public CkjmTask() {
        this.format = "plain";
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setOutputfile(File outputfile) {
        this.outputFile = outputfile;
    }

    public void setClassdir(File classDir) {
        this.classDir = classDir;
    }

    public void setExtdirs(Path e) {

        if (extdirs == null) {
            extdirs = e;
        } else {
            extdirs.append(e);
        }
    }

    public Path getExtdirs() {

        return extdirs;
    }

    public Path createExtdirs() {
        if (extdirs == null) {
            extdirs = new Path(getProject());
        }
        return extdirs.createPath();
    }

    public void execute() throws BuildException {
        if (classDir == null) {
            throw new BuildException("classdir attribute must be set!");
        }
        if (!classDir.exists()) {
            throw new BuildException("classdir does not exist!");
        }
        if (!classDir.isDirectory()) {
            throw new BuildException("classdir is not a directory!");
        }

	if (extdirs != null && extdirs.size() > 0) {
	    if (System.getProperty("java.ext.dirs").length() == 0)
		System.setProperty("java.ext.dirs", extdirs.toString());
	    else
		System.setProperty("java.ext.dirs",
		    System.getProperty("java.ext.dirs") + File.pathSeparator +
		    extdirs);
	}

        DirectoryScanner ds = super.getDirectoryScanner(classDir);

        String files[] = ds.getIncludedFiles();
        if (files.length == 0) {
            log("No class files in specified directory " + classDir);
        } else {
            for (int i = 0; i < files.length; i++) {
                files[i] = classDir.getPath() + File.separatorChar + files[i];
            }

            try {
                OutputStream outputStream = new FileOutputStream(outputFile);

                if (format.equals("xml")) {
                    PrintXmlResults outputXml = new PrintXmlResults(
                            new PrintStream(outputStream));

                    outputXml.printHeader();
                    MetricsFilter.runMetrics(files, outputXml);
                    outputXml.printFooter();
                } else {
                    PrintPlainResults outputPlain = new PrintPlainResults(
                            new PrintStream(outputStream));
                    MetricsFilter.runMetrics(files, outputPlain);
                }

                outputStream.close();

            } catch (IOException ioe) {
                throw new BuildException("Error file handling: "
                        + ioe.getMessage());
            }
        }
    }
}
