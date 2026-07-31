
/**
 * Test runner 
 */
public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;

    /** helper กลาง — พิมพ์ PASS/FAIL และนับผลให้เอง */
    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    public static void main(String[] args) {
        boolean assertsOn = false;
        assert assertsOn = true;
        if (!assertsOn) {
            System.out.println("WARNING: assertions disabled"
                    + " - re-run with: java -ea TextRunner\n");
        }

        System.out.println("=== TestRunner Suite ===\n");

        testcreator();
        testproducers();
        testobserver();
        testMutator();

        
        System.out.println("\n=== Summary ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total : " + (passed + failed));
        System.out.println(failed == 0 ? "ALL TESTS PASSED" : "SOME TESTS FAILED");

        if (failed > 0) {
            System.exit(1);
        }
    }



    // 1. ทดสอบ Creator และสถานะเริ่มต้น
    private static void testcreator(){
        System.out.println("-- Creators --");

        BoundedStack<Integer> empty = new BoundedStack<>(3);
        check("new -> empty", empty.size() == 0);
        check("new -> capacity ", empty.capacity() == 3);

        BoundedStack<Integer> one_cap = new BoundedStack<>(1);
        check("new -> cap 1", one_cap.capacity() == 1);

        boolean threw0 = false;
        try {
            new BoundedStack<Integer>(0);
        } catch (IllegalArgumentException e) {
            threw0 = true;
        }
        check("new -> cap 0", threw0);

        boolean threwNegative = false;
        try {
            new BoundedStack<Integer>(-1);
        } catch (IllegalArgumentException e) {
            threwNegative = true;
        }
        check("new -> cap -1", threwNegative);
    }

    // 2. ทดสอบ Producer (copy) ว่าแยก Object จากกันจริงไหม
    private static void testproducers(){
        System.out.println("\n-- Producer (shuffled) --");

        BoundedStack<Integer> original = new BoundedStack<>(3);
        original.push(10);
        original.push(20);
        original.push(30);

        BoundedStack<Integer> copy = original.copy();
        check("copy -> same size", copy.size() == original.size());
        check("copy -> same capacity", copy.capacity() == original.capacity());
        check("copy -> top", copy.peek().intValue() == 30);
        check("copy -> independent", copy != original);

        original.pop();
        check("copy -> original", copy.size() == 3 && copy.peek().intValue() == 30);

        BoundedStack<Integer> empty = new BoundedStack<>(2);
        BoundedStack<Integer> emptyCopy = empty.copy();
        check("copy -> empty stack", emptyCopy.isEmpty() && emptyCopy.capacity() == 2);
    }

    // 3. ทดสอบเมธอดที่ใช้ดูสถานะของ stack โดยไม่เปลี่ยนข้อมูล
    private static void testobserver(){
        System.out.println("\n-- Observers --");

        BoundedStack<Integer> s = new BoundedStack<>(3);
        check("empty -> size", s.size() == 0);
        check("empty -> capacity", s.capacity() == 3);
        check("empty -> isEmpty", s.isEmpty());
        check("empty -> isFull", !s.isFull());

        boolean threwPeek = false;
        try {
            s.peek();
        } catch (IllegalStateException e) {
            threwPeek = true;
        }
        check("empty -> peek throws", threwPeek);

        s.push(10);
        check("after push -> size", s.size() == 1);
        check("after push -> isEmpty", !s.isEmpty());
        check("after push -> isFull", !s.isFull());
        check("after push -> peek", s.peek().intValue() == 10);

        s.push(20);
        s.push(30);
        check("full -> isFull", s.isFull());
        check("full -> size", s.size() == 3);
        check("full -> peek top", s.peek().intValue() == 30);
    }
    
    // 4. ทดสอบการ push() และ pop() ใน stack
    private static void testMutator(){
        System.out.println("\n-- Mutators --");

        BoundedStack<Integer> s = new BoundedStack<>(3);
        s.push(10);
        check("push -> size", s.size() == 1);
        check("push -> peek", s.peek().intValue() == 10);

        s.push(20);
        s.push(30);
        check("push -> full", s.isFull());
        check("push -> size at capacity", s.size() == 3);

        boolean threwFull = false;
        try {
            s.push(40);
        } catch (IllegalStateException e) {
            threwFull = true;
        }
        check("push -> full stack throws", threwFull);

        boolean threwNull = false;
        try {
            s.push(null);
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("push -> null throws", threwNull);

        check("pop -> first value", s.pop().intValue() == 30);
        check("pop -> second value", s.pop().intValue() == 20);
        check("pop -> third value", s.pop().intValue() == 10);
        check("pop -> empty after all", s.isEmpty());

        boolean threwEmptyPop = false;
        try {
            s.pop();
        } catch (IllegalStateException e) {
            threwEmptyPop = true;
        }
        check("pop -> empty stack throws", threwEmptyPop);
    }
}