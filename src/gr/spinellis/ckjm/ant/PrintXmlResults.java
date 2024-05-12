package gr.spinellis.ckjm.ant;

import gr.spinellis.ckjm.CkjmOutputHandler;
import gr.spinellis.ckjm.ClassMetrics;

import java.io.OutputStream;
import java.io.PrintStream;

public class PrintXmlResults implements CkjmOutputHandler {
    private final OutputStream outputStream;

    public PrintXmlResults(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void printHeader() {
        try (PrintStream printStream = new PrintStream(outputStream)) {
            printStream.println("<?xml version=\"1.0\"?>");
            printStream.println("<ckjm>");
        } catch (Exception e) {
            System.err.println("Error occurred while writing XML header: " + e.getMessage());
        }
    }

    @Override
    public void processClassMetrics(String name, ClassMetrics c) {
        try (PrintStream printStream = new PrintStream(outputStream)) {
            printStream.print("<class>\n" +
                    "<name>" + name + "</name>\n" +
                    "<wmc>" + c.getWmc() + "</wmc>\n" +
                    "<dit>" + c.getDit() + "</dit>\n" +
                    "<noc>" + c.getNoc() + "</noc>\n" +
                    "<cbo>" + c.getCbo() + "</cbo>\n" +
                    "<rfc>" + c.getRfc() + "</rfc>\n" +
                    "<lcom>" + c.getLcom() + "</lcom>\n" +
                    "<ca>" + c.getCa() + "</ca>\n" +
                    "<npm>" + c.getNpm() + "</npm>\n" +
                    "</class>\n");
        } catch (Exception e) {
            System.err.println("Error occurred while writing class metrics: " + e.getMessage());
        }
    }

    public void printFooter() {
        try (PrintStream printStream = new PrintStream(outputStream)) {
            printStream.println("</ckjm>");
        } catch (Exception e) {
            System.err.println("Error occurred while writing XML footer: " + e.getMessage());
        }
    }
}