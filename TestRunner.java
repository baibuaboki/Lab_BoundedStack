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

        System.out.println("\n==============================================");
        System.out.println("                 TEST SUMMARY                 ");
        System.out.println("==============================================");
        System.out.println(" PASSED : " + passedTests);
        System.out.println(" FAILED : " + failedTests);
        System.out.println(" TOTAL  : " + (passedTests + failedTests));
        System.out.println("==============================================");
    }

    // ============================================================
    // Helper Methods สำหรับตรวจเช็กผลลัพธ์
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
    // Test Cases (6 กลุ่มหลักสำหรับซ้อมเขียน)
    // ============================================================

    // 1. ทดสอบ Creator และสถานะเริ่มต้น
    private static void testCreatorAndInitialState() {
        System.out.println("--- Test Group 1: Creator & Initial State ---");
        // TODO: เขียนเช็ก size == 0, capacity, isEmpty, !isFull
        
        System.out.println();
    }

    // 2. ทดสอบการ Push และ Pop แบบปกติ
    private static void testPushAndPopNormal() {
        System.out.println("--- Test Group 2: Normal Push & Pop ---");
        // TODO: เขียน push 2 ตัว แล้วลอง peek และ pop ออกดูค่า LIFO
        
        System.out.println();
    }

    // 3. ทดสอบ Boundary Case: เมื่อ Stack เต็มความจุ
    private static void testBoundaryFullStack() {
        System.out.println("--- Test Group 3: Boundary Case (Full Stack) ---");
        // TODO: เขียน push ให้เต็ม เช็ก isFull แล้วใช้ try-catch ดักจับ IllegalStateException
        
        System.out.println();
    }

    // 4. ทดสอบ Boundary Case: เมื่อ Stack ว่างเปล่า
    private static void testBoundaryEmptyStack() {
        System.out.println("--- Test Group 4: Boundary Case (Empty Stack) ---");
        // TODO: ใช้ try-catch ดักจับ IllegalStateException เมื่อ pop() หรือ peek() ตอนสแตกรอบว่าง
        
        System.out.println();
    }

    // 5. ทดสอบการดักจับ Exception กรณี Defensive Programming
    private static void testExceptionHandling() {
        System.out.println("--- Test Group 5: Exception Handling ---");
        // TODO: ใช้ try-catch ดักจับ IllegalArgumentException ตอนสร้าง capacity <= 0 หรือ push(null)
        
        System.out.println();
    }

    // 6. ทดสอบ Producer (copy) การแยก Object (Aliasing)
    private static void testProducerCopy() {
        System.out.println("--- Test Group 6: Producer (copy) ---");
        // TODO: สั่ง copy() แล้วลองแก้ตัวก๊อปปี้ เช็กว่าตัว original ไม่เปลี่ยนตาม
        
        System.out.println();
    }
}