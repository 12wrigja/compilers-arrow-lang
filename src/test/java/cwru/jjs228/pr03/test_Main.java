package cwru.jjs228.pr03;


import static cwru.jjs228.pr03.TestHelper.fileCompareTest;

import org.junit.Test;

public class test_Main {

	@Test
	public void testExample0() {
		fileCompareTest(new String[] { "-A","examples/0-ex-input.txt" },"a.ast",
				"examples/0-ex-output.txt");
	}
	
	@Test
	public void testExample1() {
		fileCompareTest(new String[] {"-A", "examples/1-ex-input.txt" },"a.ast",
				"examples/1-ex-output.txt");
	}
	
	@Test
	public void testExample2(){
		fileCompareTest(new String[]{"examples/2-ex-input.txt"},"a.ast","examples/2-ex-output.txt");
	}

	@Test
	public void testExample3() {
		fileCompareTest(new String[] { "examples/3-ex-input.txt" },"a.ast",
				"examples/3-ex-output.txt");
	}
	
	@Test
	public void testExample4() {
		fileCompareTest(new String[] { "examples/4-ex-input.txt" },"a.ast",
				"examples/4-ex-output.txt");
	}
	
	@Test
	public void testExample5() {
		fileCompareTest(new String[] { "examples/5-ex-input.txt" },"a.ast",
				"examples/5-ex-output.txt");
	}
	
	@Test
	public void testExample6() {
		fileCompareTest(new String[] { "examples/6-ex-input.txt" },"a.ast",
				"examples/6-ex-output.txt");
	}
	
	@Test
	public void testExample7() {
		fileCompareTest(new String[] { "examples/7-ex-input.txt" },"a.ast",
				"examples/7-ex-output.txt");
	}
	
	@Test
	public void testExample8() {
		fileCompareTest(new String[] { "examples/8-ex-input.txt" },"a.ast",
				"examples/8-ex-output.txt");
	}
	
	@Test
	public void testExample9() {
		fileCompareTest(new String[] { "examples/9-ex-input.txt" },"a.ast",
				"examples/9-ex-output.txt");
	}
	
	@Test
	public void testExample10() {
		fileCompareTest(new String[] { "examples/10-ex-input.txt" },"a.ast",
				"examples/10-ex-output.txt");
	}
	
	@Test
	public void testExample11() {
		fileCompareTest(new String[] { "examples/11-ex-input.txt" },"a.ast",
				"examples/11-ex-output.txt");
	}
	@Test
	public void testExample12() {
		fileCompareTest(new String[] { "examples/12-ex-input.txt" },"a.ast",
				"examples/12-ex-output.txt");
	}
	
	@Test
	public void testExample13() {
		fileCompareTest(new String[] { "examples/13-ex-input.txt" },"a.ast",
				"examples/13-ex-output.txt");
	}
	
	@Test
	public void testExample14() {
		fileCompareTest(new String[] { "examples/14-ex-input.txt" },"a.ast",
				"examples/14-ex-output.txt");
	}
	
	@Test
	public void testExample15() {
		fileCompareTest(new String[] { "examples/15-ex-input.txt" },"a.ast",
				"examples/15-ex-output.txt");
	}
	
	@Test
	public void testExample16() {
		fileCompareTest(new String[] { "examples/16-ex-input.txt" },"a.ast",
				"examples/16-ex-output.txt");
	}
	
	@Test
	public void testExample17() {
		fileCompareTest(new String[] { "examples/17-ex-input.txt" },"a.ast",
				"examples/17-ex-output.txt");
	}
	
	@Test
	public void testExample18() {
		fileCompareTest(new String[] { "examples/18-ex-input.txt" },"a.ast",
				"examples/18-ex-output.txt");
	}
	
	@Test
	public void testExample19() {
		fileCompareTest(new String[] { "examples/19-ex-input.txt" },"a.ast",
				"examples/19-ex-output.txt");
	}
	
	@Test
	public void testExample20() {
		fileCompareTest(new String[] { "examples/20-ex-input.txt" },"a.ast",
				"examples/20-ex-output.txt");
	}
	
	@Test
	public void testExample21() {
		fileCompareTest(new String[] { "examples/21-ex-input.txt" },"a.ast",
				"examples/21-ex-output.txt");
	}
	
	@Test
	public void testExample22() {
		fileCompareTest(new String[] { "examples/22-ex-input.txt" },"a.ast",
				"examples/22-ex-output.txt");
	}
	
	@Test
	public void testExample23() {
		fileCompareTest(new String[] { "examples/23-ex-input.txt" },"a.ast",
				"examples/23-ex-output.txt");
	}
	
	@Test
	public void testExample24() {
		fileCompareTest(new String[] { "examples/24-ex-input.txt" },"a.ast",
				"examples/24-ex-output.txt");
	}
	
	@Test
	public void testExample25() {
		fileCompareTest(new String[] { "examples/25-ex-input.txt" },"a.ast",
				"examples/25-ex-output.txt");
	}
	
	@Test
	public void testExample26() {
		fileCompareTest(new String[] { "examples/26-ex-input.txt" }, "a.ast",
				"examples/26-ex-output.txt");
	}
	
	@Test
	public void testTypeExample0() {
		fileCompareTest(new String[] { "typeExamples/0-ex-input.txt" },"b.ast",
				"typeExamples/0-ex-output.txt");
	}
	
	@Test
	public void testTypeExample1() {
		fileCompareTest(new String[] { "typeExamples/1-ex-input.txt" },"b.ast",
				"typeExamples/1-ex-output.txt");
	}
	
	@Test
	public void testTypeExample2() {
		fileCompareTest(new String[] { "typeExamples/2-ex-input.txt" },"b.ast",
				"typeExamples/2-ex-output.txt");
	}
	
	@Test
	public void testTypeExample3() {
		fileCompareTest(new String[] { "typeExamples/3-ex-input.txt" },"b.ast",
				"typeExamples/3-ex-output.txt");
	}
	
	@Test
	public void testTypeExample4() {
		fileCompareTest(new String[] { "typeExamples/4-ex-input.txt" },"b.ast",
				"typeExamples/4-ex-output.txt");
	}
	
	@Test
	public void testTypeExample5() {
		fileCompareTest(new String[] { "typeExamples/5-ex-input.txt" },"b.ast",
				"typeExamples/5-ex-output.txt");
	}
	
	@Test
	public void testTypeExample6() {
		fileCompareTest(new String[] { "typeExamples/6-ex-input.txt" },"b.ast",
				"typeExamples/6-ex-output.txt");
	}
	
	@Test
	public void testTypeExample7() {
		fileCompareTest(new String[] { "typeExamples/7-ex-input.txt" },"b.ast",
				"typeExamples/7-ex-output.txt");
	}
	
	@Test
	public void testTypeExample8() {
		fileCompareTest(new String[] { "typeExamples/8-ex-input.txt" },"b.ast",
				"typeExamples/8-ex-output.txt");
	}
	
	@Test
	public void testTypeExample9() {
		fileCompareTest(new String[] { "typeExamples/0-ex-input.txt" },"b.ast",
				"typeExamples/0-ex-output.txt");
	}
	
	@Test
	public void testTypeExample10() {
		fileCompareTest(new String[] { "typeExamples/10-ex-input.txt" },"b.ast",
				"typeExamples/10-ex-output.txt");
	}
	
	@Test
	public void testTypeExample11() {
		fileCompareTest(new String[] { "typeExamples/11-ex-input.txt" },"b.ast",
				"typeExamples/11-ex-output.txt");
	}
	
	@Test
	public void testTypeExample12() {
		fileCompareTest(new String[] { "typeExamples/12-ex-input.txt" },"b.ast",
				"typeExamples/12-ex-output.txt");
	}
	
	@Test
	public void testTypeExample13() {
		fileCompareTest(new String[] { "typeExamples/13-ex-input.txt" },"b.ast",
				"typeExamples/13-ex-output.txt");
	}
	
	@Test
	public void testTypeExample14() {
		fileCompareTest(new String[] { "typeExamples/14-ex-input.txt" },"b.ast",
				"typeExamples/14-ex-output.txt");
	}
	
	@Test
	public void testTypeExample15() {
		fileCompareTest(new String[] { "typeExamples/15-ex-input.txt" },"b.ast",
				"typeExamples/15-ex-output.txt");
	}
	
	@Test
	public void testTypeExample16() {
		fileCompareTest(new String[] { "typeExamples/16-ex-input.txt" },"b.ast",
				"typeExamples/16-ex-output.txt");
	}
	
	@Test
	public void testTypeExample17() {
		fileCompareTest(new String[] { "typeExamples/17-ex-input.txt" },"b.ast",
				"typeExamples/17-ex-output.txt");
	}
	
	@Test
	public void testTypeExample18() {
		fileCompareTest(new String[] { "typeExamples/18-ex-input.txt" },"b.ast",
				"typeExamples/18-ex-output.txt");
	}
	
	@Test
	public void testTypeExample19() {
		fileCompareTest(new String[] { "typeExamples/19-ex-input.txt" },"b.ast",
				"typeExamples/19-ex-output.txt");
	}
	
	@Test
	public void testTypeExample20() {
		fileCompareTest(new String[] { "typeExamples/20-ex-input.txt" },"b.ast",
				"typeExamples/20-ex-output.txt");
	}
	
	@Test
	public void testTypeExample21() {
		fileCompareTest(new String[] { "typeExamples/21-ex-input.txt" },"b.ast",
				"typeExamples/21-ex-output.txt");
	}
	
	@Test
	public void testTypeExample22() {
		fileCompareTest(new String[] { "typeExamples/22-ex-input.txt" },"b.ast",
				"typeExamples/22-ex-output.txt");
	}
	
	@Test
	public void testTypeExample23() {
		fileCompareTest(new String[] { "typeExamples/23-ex-input.txt" },"b.ast",
				"typeExamples/23-ex-output.txt");
	}
	
	/*@Test
	public void testExample27() {
		fileCompareTest(new String[] { "examples/27-ex-input.txt" },
				"examples/27-ex-output.txt");
	}*/
	
	/*@Test
	public void testExample28() {
		fileCompareTest(new String[] { "examples/28-ex-input.txt" },
				"examples/28-ex-output.txt");
	}*/
}

