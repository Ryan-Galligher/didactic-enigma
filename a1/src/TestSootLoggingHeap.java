import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.options.Options;

public class TestSootLoggingHeap extends BodyTransformer {

	private static SootMethodRef logFieldAccMethod;
	Pattern pattern = Pattern.compile("\\<(.*?)\\>");
	Matcher matcher;

	public static void main(String[] args)	{

		System.out.println("Actually Working");
		String mainclass = "HelloThread";

		//output Jimple
		//Options.v().set_output_format(1);

//		//set classpath
	    String javapath = System.getProperty("java.class.path");
	    String jredir = System.getProperty("java.home")+"/lib/rt.jar";
	    String path = javapath+File.pathSeparator+jredir;
	    Scene.v().setSootClassPath(path);

        //add an intra-procedural analysis phase to Soot
	    TestSootLoggingHeap analysis = new TestSootLoggingHeap();
	    PackManager.v().getPack("jtp").add(new Transform("jtp.TestSootLoggingHeap", analysis));

        //load and set main class
	    Options.v().set_app(true);
	    SootClass appclass = Scene.v().loadClassAndSupport(mainclass);
	    Scene.v().setMainClass(appclass);
		SootClass logClass = Scene.v().loadClassAndSupport("Log");
		logFieldAccMethod = logClass.getMethod("void logFieldAcc(java.lang.Object,java.lang.String,boolean,boolean)").makeRef();
	    Scene.v().loadNecessaryClasses();

	    System.out.println("About to run");
        //start working
	    PackManager.v().runPacks();

	    System.out.println("About to write output");
	    PackManager.v().writeOutput();
	}

	@Override
	protected void internalTransform(Body b, String phaseName,
		Map<String, String> options) {

		 /* SootClass systemClass = Scene.v().tryLoadClass("java.lang.System", 1);
		 SootClass printClass = Scene.v().tryLoadClass("java.io.PrintStream", 2);
		 SootMethodRef printMethod = printClass.getMethod("void println(java.lang.String)").makeRef();
		 SootFieldRef fr = Scene.v().makeFieldRef(systemClass,"out",RefType.v("java.io.PrintStream"),true);
		 StaticFieldRef sfr = Jimple.v().newStaticFieldRef(fr);
		 Local r = Jimple.v().newLocal("$r2",RefType.v("java.io.PrintStream"));
		 AssignStmt newAssignStmt = Jimple.v().newAssignStmt(r, sfr);
*/
		
		//we don't instrument Log class
		if(!b.getMethod().getDeclaringClass().getName().equals("Log"))
		{
	/*		b.getLocals().add(r);
			Unit toInsert = b.getUnits().getFirst();
			while(toInsert instanceof IdentityStmt)
				toInsert = b.getUnits().getSuccOf(toInsert);
			b.getUnits().insertBefore(newAssignStmt,toInsert);
*/
			Iterator<Unit> it = b.getUnits().snapshotIterator();
		    while(it.hasNext()){
		    	Stmt stmt = (Stmt)it.next();
		    	System.out.println("Statement:  " + stmt.toString());
		    	if (stmt.containsFieldRef()) {
		    		//your code starts here
		    		matcher = pattern.matcher(stmt.toString());
		    		String name;
		    		if (matcher.find()) {
		    			name = "<" + matcher.group(1) + ">";
		    		} else {
		    			name = "";
		    		}
		    		System.out.println("has Ref");
		    		Object thread = b.getMethod().getDeclaringClass();
		    		boolean isStatic = stmt.getFieldRef() instanceof StaticFieldRef;
		    		System.out.println("isStatic:  " + isStatic);
//		    		boolean isStatic = false;//stmt.getInvokeExpr().getClass().isInstance(stmt.getInvokeExpr().getClass());
		    		boolean isWrite = stmt.toString().matches(".*\\> = .*");
		    		//System.out.println(stmt.toString() + " stmt.isAssignment() == " + stmt.toString().matches(".*\\> = .*"));
					InvokeExpr printExpr = Jimple.v().newStaticInvokeExpr(logFieldAccMethod, 
							(List<? extends Value>) Arrays.asList(new Object[] {
									StringConstant.v(thread.toString()),
									StringConstant.v(name) ,
									IntConstant.v(isStatic ? 1:0),
									IntConstant.v(isWrite ? 1:0) } ));
					System.out.println(printExpr.toString());
					//printExpr.setArg(0, thread);
					//printExpr.setArg(1, (Value)((Object) name));
					//printExpr.setArg(2, (Value) ((Object) isStatic));
					//printExpr.setArg(3,  (Value) ((Object) true));
		    		InvokeStmt invokeStmt = Jimple.v().newInvokeStmt(printExpr);
		    		b.getUnits().insertBefore(invokeStmt, stmt);
		    	}
		    }
		}
	}
}