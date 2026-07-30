public class TestRunner {

    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("       STARTING BOUNDED STACK TEST SUITE      ");
        System.out.println("==============================================\n");

        testCreatorAndInitialState();
        testPushAndPopNormal();
        testBoundaryFullStack();
        testBoundaryEmptyStack();
        testExceptionHandling();
        testProducerCopy();
        testCapacityOne();
        testPeekAliasingBehavior();
        testNearFullBoundary();
        System.out.println("\n==============================================");
        System.out.println("                 TEST SUMMARY                 ");
        System.out.println("==============================================");
        System.out.println(" PASSED : " + passedTests);
        System.out.println(" FAILED : " + failedTests);
        System.out.println(" TOTAL  : " + (passedTests + failedTests));
        System.out.println("==============================================");
    }

    // ============================================================
    // Helper Methods สำหรับตรวจเช็กผลลัพธ์ (ไม่ต้องพึ่ง JUnit)
    // ============================================================
    private static void assertEquals(Object expected, Object actual, String testName) {
        if ((expected == null && actual == null) || (expected != null && expected.equals(actual))) {
            System.out.println("[PASS] " + testName);
            passedTests++;
        } else {
            System.out.println("[FAIL] " + testName + " -> Expected: " + expected + ", Actual: " + actual);
            failedTests++;
        }
    }

    private static void assertTrue(boolean condition, String testName) {
        if (condition) {
            System.out.println("[PASS] " + testName);
            passedTests++;
        } else {
            System.out.println("[FAIL] " + testName + " -> Expected: true, Actual: false");
            failedTests++;
        }
    }

    // ============================================================
    // Test Cases
    // ============================================================

    // 1. ทดสอบ Creator และสถานะเริ่มต้น
    private static void testCreatorAndInitialState() {
        System.out.println("--- Test Group 1: Creator & Initial State ---");
        BoundedStack<String> stack = new BoundedStack<>(3);
        
        assertEquals(0, stack.size(), "Initial size should be 0");
        assertEquals(3, stack.capacity(), "Capacity should be 3");
        assertTrue(stack.isEmpty(), "New stack should be empty");
        assertTrue(!stack.isFull(), "New stack should not be full");
        System.out.println();
    }

    // 2. ทดสอบการ Push และ Pop แบบปกติ
    private static void testPushAndPopNormal() {
        System.out.println("--- Test Group 2: Normal Push & Pop ---");
        BoundedStack<Integer> stack = new BoundedStack<>(5);
        
        stack.push(10);
        stack.push(20);
        
        assertEquals(2, stack.size(), "Size should be 2 after pushing twice");
        assertEquals(20, stack.peek(), "Peek should return last pushed element (20)");
        assertEquals(20, stack.pop(), "Pop should return last pushed element (20)");
        assertEquals(10, stack.pop(), "Pop should return remaining element (10)");
        assertTrue(stack.isEmpty(), "Stack should be empty after popping all items");
        System.out.println();
    }

    // 3. ทดสอบ Boundary Case: เมื่อ Stack เต็มความจุ
    private static void testBoundaryFullStack() {
        System.out.println("--- Test Group 3: Boundary Case (Full Stack) ---");
        BoundedStack<String> stack = new BoundedStack<>(2);
        
        stack.push("A");
        stack.push("B");

        assertTrue(stack.isFull(), "Stack should be full when items count equals capacity");
        
        // ทดสอบว่าถ้าเต็มแล้วสั่ง push จะโยน IllegalStateException
        try {
            stack.push("C");
            assertEquals("Exception Thrown", "No Exception", "Should throw IllegalStateException when pushing to full stack");
        } catch (IllegalStateException e) {
            assertTrue(true, "Correctly caught Exception when pushing to full stack");
            assertEquals(2, stack.size(), "Size should remain 2 when trying to push to full stack");
        }
        System.out.println();
    }

    // 4. ทดสอบ Boundary Case: เมื่อ Stack ว่างเปล่า
    private static void testBoundaryEmptyStack() {
        System.out.println("--- Test Group 4: Boundary Case (Empty Stack) ---");
        BoundedStack<String> stack = new BoundedStack<>(3);

        // ทดสอบว่าถ้าว่างแล้วสั่ง pop จะโยน IllegalStateException
        try {
            stack.pop();
            assertEquals("Exception Thrown", "No Exception", "Should throw IllegalStateException when popping empty stack");
        } catch (IllegalStateException e) {
            assertTrue(true, "Correctly caught Exception when popping empty stack");
            assertEquals(0, stack.size(), "Size should remain 0 when trying to pop from empty stack");
        }

        // ทดสอบว่าถ้าว่างแล้วสั่ง peek จะโยน IllegalStateException
        try {
            stack.peek();
            assertEquals("Exception Thrown", "No Exception", "Should throw IllegalStateException when peeking empty stack");
        } catch (IllegalStateException e) {
            assertTrue(true, "Correctly caught Exception when peeking empty stack");
        }
        System.out.println();
    }

    // 5. ทดสอบการดักจับ Exception ในกรณีอื่นๆ (Defensive Programming)
    private static void testExceptionHandling() {
        System.out.println("--- Test Group 5: Exception Handling ---");
        
        // เคสที่ 1: ตั้งค่า capacity <= 0
        try {
            new BoundedStack<String>(0);
            assertEquals("Exception Thrown", "No Exception", "Should throw IllegalArgumentException for capacity 0");
        } catch (IllegalArgumentException e) {
            assertTrue(true, "Correctly caught Exception for non-positive capacity");
        }
        // เคสที่ 2: ตั้งค่า capacity ติดลบ 
        try {
            new BoundedStack<String>(-5);
            assertEquals("Exception Thrown", "No Exception", "Should throw IllegalArgumentException for negative capacity");
        } catch (IllegalArgumentException e) {
            assertTrue(true, "Correctly caught Exception for negative capacity");
        }

        // เคสที่ 3: ลอง push ค่า null
        BoundedStack<String> stack = new BoundedStack<>(3);
        try {
            stack.push(null);
            assertEquals("Exception Thrown", "No Exception", "Should throw IllegalArgumentException when pushing null");
        } catch (IllegalArgumentException e) {
            assertTrue(true, "Correctly caught Exception when pushing null");
        }

        // เคสที่ 4 — capacity มหาศาล (ไม่ควร throw) 
        BoundedStack<String> hugeStack = new BoundedStack<>(Integer.MAX_VALUE);   
        assertTrue(hugeStack.isEmpty(), "Huge stack should be empty");                                    
        assertTrue(!hugeStack.isFull(), "Huge stack should not be full");                                    
        System.out.println();
    }

    // 6. ทดสอบ Producer (copy) ว่าแยก Object จากกันจริงไหม
    private static void testProducerCopy() {
        System.out.println("--- Test Group 6: Producer (copy) ---");
        BoundedStack<String> original = new BoundedStack<>(3);
        original.push("X");
        original.push("Y");

        BoundedStack<String> cloned = original.copy();

        // ตรวจสอบว่าข้อมูลเริ่มต้นเหมือนกัน
        assertEquals(original.size(), cloned.size(), "Cloned stack should have same size");
        assertEquals(original.peek(), cloned.peek(), "Cloned stack should have same top element");

        // แก้ไข cloned แล้วตรวจสอบว่า original ไม่ถูกเปลี่ยนตาม (Aliasing Test)
        cloned.pop();
        assertEquals(2, original.size(), "Original size should remain 2 after popping from clone");
        assertEquals(1, cloned.size(), "Cloned size should decrease to 1");
        System.out.println();
    }
    // 7. ทดสอบกรณีขอบเขตพิเศษ: ความจุ = 1
    private static void testCapacityOne() {
    System.out.println("--- Test Group 7: Capacity = 1 (edge case) ---");
    BoundedStack<String> stack = new BoundedStack<>(1);
    
    assertTrue(stack.isEmpty(), "New stack with capacity 1 should be empty");
    stack.push("A");
    assertTrue(stack.isFull(), "Stack should be full after pushing 1 item into capacity 1 stack");
    assertEquals("A", stack.pop(), "Popped item should be 'A'");
    assertTrue(stack.isEmpty(), "Stack should be empty after popping the only item");
}
    // 8. ทดสอบ peek() aliasing (ข้อจำกัดที่รู้อยู่ — พิสูจน์ด้วย test แทนคำอธิบายใน README)
private static void testPeekAliasingBehavior() {                     
    System.out.println("--- Test Group 8: peek() Aliasing (Known Limitation) ---");

    BoundedStack<int[]> stack = new BoundedStack<>(1);        

    int[] original = new int[]{42};                            
    stack.push(original);

    int[] fromPeek = stack.peek();
    fromPeek[0] = 99;                                       
    int[] peekAgain = stack.peek();

    assertEquals(99, peekAgain[0], "Peek should reflect changes to the returned array");                  
    System.out.println();                                          
}
    // 9. ทดสอบ Boundary Case: เกือบเต็ม (capacity - 1) — partition ที่ขาดหายไปจากกลุ่มเดิม
private static void testNearFullBoundary() {                      
    System.out.println("--- Test Group 9: Near-Full Boundary ---");

    BoundedStack<String> stack = new BoundedStack<>(3);       

    stack.push("A");                                            
    stack.push("B");                                            

    assertTrue(!stack.isFull(), "Stack should not be full when size is less than capacity");                                   
    assertTrue(!stack.isEmpty(), "Stack should not be empty when size is greater than 0");                                   
    assertEquals(2, stack.size(), "Stack should have 2 items when size is less than capacity");                    

    System.out.println();
}
}