Part 1:

	After Checking the doAnalysis method by running the TestDominatorFinder, I believe that the output for all fo the statments shows which statments they are dominated by.

Part 2:

	After running the TestSootCallGraph.java main method when set up to determine the CHA call graph, we get the output: 
<Example: Animal selectAnimal()> may call <Cat: void <init>()>
<Example: void main(java.lang.String[])> may call <Example: Animal selectAnimal()>
Total Edges:12
<Example: void main(java.lang.String[])> may call <Cat: void saySomething()>
<Example: void main(java.lang.String[])> may call <Fish: void saySomething()>
<Animal: void <init>()> may call <java.lang.Object: void <init>()>
<Cat: void <init>()> may call <Animal: void <init>()>
<Cat: void saySomething()> may call <java.lang.System: void <clinit>()>
<Cat: void saySomething()> may call <java.io.PrintStream: void println(java.lang.String)>
<Cat: void saySomething()> may call <java.lang.Object: void <clinit>()>
<Fish: void saySomething()> may call <java.lang.System: void <clinit>()>
<Fish: void saySomething()> may call <java.io.PrintStream: void println(java.lang.String)>
<Fish: void saySomething()> may call <java.lang.Object: void <clinit>()>
Time Elapsed (Millis): 834

	After running the TestSootCallGraph.java main method when set up to determine the PTA call graph, we get the output:
<Example: Animal selectAnimal()> may call <Cat: void <init>()>
<Example: void main(java.lang.String[])> may call <Example: Animal selectAnimal()>
<Example: void main(java.lang.String[])> may call <Cat: void saySomething()>
Total Edges:7
<Animal: void <init>()> may call <java.lang.Object: void <init>()>
<Cat: void <init>()> may call <Animal: void <init>()>
<Cat: void saySomething()> may call <java.lang.System: void <clinit>()>
<Cat: void saySomething()> may call <java.lang.Object: void <clinit>()>
Time Elapsed (Millis): 1155

From these two tests, we can determine that PTA will have a higher precision in the answer, as it has to go in and directly find exactly what classes and edges that are used. 
On the other hand, CHA is much faster in its execution as it simply looks through most of the possible answers by the hierarchy of the classes but will inevitably be much less precise.

Part 3:
	For Logging Method Calls:
		Before Commenting out "Options.v().set_output_format(1);": 
			Jimple files included in Github.
		After  Commenting out "Options.v().set_output_format(1);":
			Class files included in Github. Output of running java Example is
				C:\Users\Ryan\...\sootOutput> java Example
				calling selectAnimal
				calling saySomething
				purr

	For Tracing Heap Accesses:
		I have added the necessary code in order to properly instument the HelloThread thread, and running the TestSootLoggingHeap.java class, we go in to sootOutput and execute "java HelloThread", where the actually changed code can be found.
The output upon running the TestSootLoggingHeap.java is as below:
Actually Working
About to run
Statement:  r0 := @this: HelloThread$TestThread
Statement:  <HelloThread: int x> = 1
Statement:  specialinvoke r0.<java.lang.Thread: void <init>()>()
Statement:  return
has Ref
isStatic:  true
staticinvoke <Log: void logFieldAcc(java.lang.Object,java.lang.String,boolean,boolean)>("HelloThread", "<HelloThread: int x>", 1, 1)
Statement:  return
Statement:  r0 := @this: HelloThread
Statement:  specialinvoke r0.<java.lang.Object: void <init>()>()
Statement:  return
Statement:  r0 := @this: HelloThread$TestThread
Statement:  <HelloThread: int x> = 0
has Ref
isStatic:  true
staticinvoke <Log: void logFieldAcc(java.lang.Object,java.lang.String,boolean,boolean)>("HelloThread$TestThread", "<HelloThread: int x>", 1, 1)
Statement:  $i0 = r0.<HelloThread$TestThread: int y>
has Ref
isStatic:  false
staticinvoke <Log: void logFieldAcc(java.lang.Object,java.lang.String,boolean,boolean)>("HelloThread$TestThread", "<HelloThread$TestThread: int y>", 0, 0)
Statement:  $i1 = $i0 + 1
Statement:  r0.<HelloThread$TestThread: int y> = $i1
has Ref
isStatic:  false
Statement:  r0 := @parameter0: java.lang.String[]
Statement:  $r2 = new HelloThread$TestThread
staticinvoke <Log: void logFieldAcc(java.lang.Object,java.lang.String,boolean,boolean)>("HelloThread$TestThread", "<HelloThread$TestThread: int y>", 0, 1)
Statement:  return
Statement:  specialinvoke $r2.<HelloThread$TestThread: void <init>()>()
Statement:  virtualinvoke $r2.<HelloThread$TestThread: void start()>()
Statement:  $i3 = $r2.<HelloThread$TestThread: int y>
has Ref
isStatic:  false
staticinvoke <Log: void logFieldAcc(java.lang.Object,java.lang.String,boolean,boolean)>("HelloThread", "<HelloThread$TestThread: int y>", 0, 0)
Statement:  $i1 = <HelloThread: int x>
has Ref
isStatic:  true
staticinvoke <Log: void logFieldAcc(java.lang.Object,java.lang.String,boolean,boolean)>("HelloThread", "<HelloThread: int x>", 1, 0)
Statement:  $i2 = 1 / $i1
Statement:  i0 = $i3 + $i2
Statement:  $r3 = <java.lang.System: java.io.PrintStream out>
has Ref
isStatic:  true
staticinvoke <Log: void logFieldAcc(java.lang.Object,java.lang.String,boolean,boolean)>("HelloThread", "<java.lang.System: java.io.PrintStream out>", 1, 0)
Statement:  virtualinvoke $r3.<java.io.PrintStream: void println(int)>(i0)
Statement:  return
About to write output

The output upon running the HelloThread class (java HelloThread) can be:
Thread main wrote static field <HelloThread: int x>
Thread main read instance field <HelloThread$TestThread: int y> of object HelloThread
Thread main read static field <HelloThread: int x>
Thread main read static field <java.lang.System: java.io.PrintStream out>
Thread Thread-0 wrote static field <HelloThread: int x>
1
Thread Thread-0 read instance field <HelloThread$TestThread: int y> of object HelloThread$TestThread
Thread Thread-0 wrote instance field <HelloThread$TestThread: int y> of object HelloThread$TestThread