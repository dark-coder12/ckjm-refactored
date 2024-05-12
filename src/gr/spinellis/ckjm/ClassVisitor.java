package gr.spinellis.ckjm;

import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;
import java.util.*;
import java.lang.reflect.Modifier;

public class ClassVisitor extends org.apache.bcel.classfile.EmptyVisitor implements Visitor {
	private JavaClass visitedClass;
	private ConstantPoolGen constantPool;
	private ClassMetrics classMetrics;
	private String className;
	private ClassMetricsContainer classMetricsContainer;

	private HashSet<String> efferentCoupledClasses = new HashSet<>();
	private HashSet<String> responseSet = new HashSet<>();
	private ArrayList<TreeSet<String>> methodInvocationList = new ArrayList<>();

	public ClassVisitor(JavaClass javaClass, ClassMetricsContainer classMap) {
		visitedClass = javaClass;
		constantPool = new ConstantPoolGen(visitedClass.getConstantPool());
		classMetricsContainer = classMap;
		className = visitedClass.getClassName();
		classMetrics = classMetricsContainer.getMetrics(className);
	}

	public ClassMetrics getMetrics() {
		return classMetrics;
	}

	public void start() {
		visitJavaClass(visitedClass);
	}

	public void visitJavaClass(JavaClass javaClass) {
		processClassInformation(javaClass);
		processClassHierarchy(javaClass);
		processInterfaces(javaClass);
		processFields(javaClass);
		processMethods(javaClass);
	}

	public void processClassInformation(JavaClass javaClass) {
		classMetrics.javaInformation.setVisited();
		if (javaClass.isPublic())
			classMetrics.javaInformation.setPublic();
	}

	private void processClassHierarchy(JavaClass javaClass) {
		String superClassName = javaClass.getSuperclassName();
		classMetricsContainer.getMetrics(superClassName).incNoc();
		try {
			classMetrics.setDit(javaClass.getSuperClasses().length);
		} catch (ClassNotFoundException ex) {
			System.err.println("Error obtaining all superclasses of " + javaClass);
		}
		registerSuperCoupling(superClassName);
	}

	private void processInterfaces(JavaClass javaClass) {
		String[] interfaceNames = javaClass.getInterfaceNames();
		for (String anInterface : interfaceNames)
			registerCoupling(anInterface);
	}

	private void processFields(JavaClass javaClass) {
		Field[] fields = javaClass.getFields();
		for (Field field : fields)
			visitField(field);
	}

	private void processMethods(JavaClass javaClass) {
		Method[] methods = javaClass.getMethods();
		for (Method method : methods)
			visitMethod(method);
	}

	public void visitMethod(Method method) {
		MethodGen methodGen = new MethodGen(method, visitedClass.getClassName(), constantPool);

		Type resultType = methodGen.getReturnType();
		Type[] argTypes = methodGen.getArgumentTypes();

		processMethodReturnType(resultType);
		processMethodArguments(argTypes);
		processMethodExceptions(methodGen);
		incrementRFC(className, method.getName(), argTypes);

		classMetrics.incWmc();
		if (Modifier.isPublic(method.getModifiers()))
			classMetrics.incNpm();
		createMethodInvocationList();
		visitMethodGen(methodGen);
	}

	private void processMethodReturnType(Type returnType) {
		registerCoupling(returnType);
	}

	private void processMethodArguments(Type[] argumentTypes) {
		for (Type argType : argumentTypes)
			registerCoupling(argType);
	}

	private void processMethodExceptions(MethodGen methodGen) {
		String[] exceptions = methodGen.getExceptions();
		for (String exception : exceptions)
			registerCoupling(exception);
	}

	private void createMethodInvocationList() {
		methodInvocationList.add(new TreeSet<>());
	}

	private void visitMethodGen(MethodGen methodGen) {
		MethodVisitor methodVisitor = new MethodVisitor(methodGen, this);
		methodVisitor.start();
	}

	public void registerCoupling(String className) {
		if ((MetricsFilter.isJdkIncluded() || !JavaHandler.isJdkClass(className)) &&
				!this.className.equals(className)) {
			efferentCoupledClasses.add(className);
			classMetricsContainer.getMetrics(className).addAfferentCoupling(this.className);
		}
	}
	public void registerCoupling(Type type) {
		registerCoupling(getClassName(type));
	}

	public String getClassName(Type type) {
		TypeHandler handler = TypeHandlerFactory.createTypeHandler(type);
		return handler.getClassName();
	}

	public void incrementRFC(String className, String methodName, Type[] arguments) {
		String argumentList = Arrays.asList(arguments).toString();
		String args = argumentList.substring(1, argumentList.length() - 1);
		String signature = className + "." + methodName + "(" + args + ")";
		responseSet.add(signature);
	}

	private void registerSuperCoupling(String superName) {
		if (superName != null && !superName.isEmpty())
			registerCoupling(superName);
	}

	public void registerFieldAccess(String className, String fieldName) {
		registerCoupling(className);
		if (className.equals(this.className))
			methodInvocationList.get(methodInvocationList.size() - 1).add(fieldName);
	}

	public void visitField(Field field) {
		registerCoupling(field.getType());
	}

	public void end() {
		classMetrics.setCbo(efferentCoupledClasses.size());
		classMetrics.setRfc(responseSet.size());

		int lcom = 0;
		for (int i = 0; i < methodInvocationList.size(); i++)
			for (int j = i + 1; j < methodInvocationList.size(); j++) {
				TreeSet<?> intersection = (TreeSet<?>) methodInvocationList.get(i).clone();
				intersection.retainAll(methodInvocationList.get(j));
				if (intersection.size() == 0)
					lcom++;
				else
					lcom--;
			}
		classMetrics.setLcom(lcom > 0 ? lcom : 0);
	}
}
