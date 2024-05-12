package gr.spinellis.ckjm;

public class JavaHandler {
    private boolean visited;
    private boolean isPublicClass;

    public boolean isPublic() {
        return isPublicClass;
    }

    public void setPublic() {
        isPublicClass = true;
    }

    public void setVisited() {
        visited = true;
    }

    public boolean isVisited() {
        return visited;
    }
    public static boolean isJdkClass(String s) {
        return (s.startsWith("java.") ||
                s.startsWith("javax.") ||
                s.startsWith("org.omg.") ||
                s.startsWith("org.w3c.dom.") ||
                s.startsWith("org.xml.sax."));
    }
}
