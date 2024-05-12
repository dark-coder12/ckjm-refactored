
package gr.spinellis.ckjm;

import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;

import java.util.*;
import java.lang.reflect.Modifier;
// ClassVisitor.java

import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;

import java.util.*;
import java.lang.reflect.Modifier;

public class ClassVisitor extends org.apache.bcel.classfile.EmptyVisitor {
	private JavaClass visitedClass;
	private ConstantPoolGen cp;
	private String myClassName;
	private ClassMetricsContainer cmap;
	private ClassMetrics cm;
	private HashSet<String> efferentCoupledClasses = new HashSet<String>();
	private HashSet<String> responseSet = new HashSet<String>();
	private ArrayList<TreeSet<String>> mi = new ArrayList<TreeSet<String>>();

	public ClassVisitor(JavaClass jc, ClassMetricsContainer classMap) {
		visitedClass = jc;
		cp = new ConstantPoolGen(visitedClass.getConstantPool());
		cmap = classMap;
		myClassName = jc.getClassName();
		cm = cmap.getMetrics(myClassName);
	}

	public ClassMetrics getMetrics() { return cm; }

	public void start() {
		visitJavaClass(visitedClass);
	}

	public void visitJavaClass(JavaClass jc) {
		String super_name = jc.getSuperclassName();
		String package_name = jc.getPackageName();

		String className = jc.getClassName();

		cm.javaInformation.setVisited();
		if (jc.isPublic())
			cm.javaInformation.setPublic();
		ClassMetrics pm = cmap.getMetrics(super_name);

		pm.incNoc();
		try {
			cm.setDit(jc.getSuperClasses().length);
		} catch (ClassNotFoundException ex) {
			System.err.println("Error obtaining all superclasses of " + jc);
		}
		registerSuperCoupling(super_name);

		String[] ifs = jc.getInterfaceNames();
		for (String anIf : ifs)
			registerCoupling(anIf);

		Field[] fields = jc.getFields();
		for (Field field : fields)
			field.accept(this);

		Method[] methods = jc.getMethods();
		for (Method method : methods)
			method.accept(this);
	}

	private void registerSuperCoupling(String superName) {
		if (superName != null && !superName.isEmpty())
			registerCoupling(superName);
	}

	private void registerCoupling(String className) {
		if ((MetricsFilter.isJdkIncluded() || !JavaHandler.isJdkClass(className)) &&
				!myClassName.equals(className)) {
			efferentCoupledClasses.add(className);
			cmap.getMetrics(className).addAfferentCoupling(myClassName);
		}
	}

	public void registerCoupling(Type t) {
		registerCoupling(className(t));
	}

	static String className(Type t) {
		String ts = t.toString();
		if (t.getType() <= Constants.T_VOID) {
			return "java.PRIMITIVE";
		} else if (t instanceof ArrayType) {
			ArrayType at = (ArrayType) t;
			return className(at.getBasicType());
		} else {
			return t.toString();
		}
	}

	public void registerMethodInvocation(String className, String methodName, Type[] args) {
		registerCoupling(className);

		incRFC(className, methodName, args);
	}

	public void registerFieldAccess(String className, String fieldName) {
		registerCoupling(className);
		if (className.equals(myClassName))
			mi.get(mi.size() - 1).add(fieldName);
	}

	public void visitField(Field field) {
		registerCoupling(field.getType());
	}

	private void incRFC(String className, String methodName, Type[] arguments) {
		String argumentList = Arrays.asList(arguments).toString();
		String args = argumentList.substring(1, argumentList.length() - 1);
		String signature = className + "." + methodName + "(" + args + ")";
		responseSet.add(signature);
	}

	public void visitMethod(Method method) {
		MethodGen mg = new MethodGen(method, visitedClass.getClassName(), cp);

		Type result_type = mg.getReturnType();
		Type[] argTypes = mg.getArgumentTypes();

		registerCoupling(mg.getReturnType());
		for (Type argType : argTypes)
			registerCoupling(argType);

		String[] exceptions = mg.getExceptions();
		for (String exception : exceptions)
			registerCoupling(exception);

		incRFC(myClassName, method.getName(), argTypes);

		cm.incWmc();
		if (Modifier.isPublic(method.getModifiers()))
			cm.incNpm();
		mi.add(new TreeSet<String>());
		MethodVisitor factory = new MethodVisitor(mg, this);
		factory.start();
	}

	public void end() {
		cm.setCbo(efferentCoupledClasses.size());
		cm.setRfc(responseSet.size());

		int lcom = 0;
		for (int i = 0; i < mi.size(); i++)
			for (int j = i + 1; j < mi.size(); j++) {
				TreeSet<?> intersection = (TreeSet<?>) mi.get(i).clone();
				intersection.retainAll(mi.get(j));
				if (intersection.size() == 0)
					lcom++;
				else
					lcom--;
			}
		cm.setLcom(lcom > 0 ? lcom : 0);
	}
}
