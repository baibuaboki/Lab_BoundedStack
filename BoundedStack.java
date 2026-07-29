import java.util.ArrayList;
import java.util.List;

/**
 * BoundedStack คือ ADT แบบ Mutable เก็บข้อมูลแบบ LIFO (Last-In-First-Out)
 * โดยมีการจำกัดความจุสูงสุด (Capacity)
 *
 * @param <E> ประเภทของข้อมูลที่จะเก็บใน Stack
 */
public class BoundedStack<E> {

    // ============================================================
    // 1. Private Representation (Fields)
    // ============================================================
    private final List<E> items;
    private final int capacity;

    // ============================================================
    // 2. Specifications: AF & RI
    // ============================================================
    // Abstraction Function (AF):
    //   AF(items, capacity) = สแตกที่มีข้อมูลเก็บอยู่ตั้งแต่ items.get(0) (ก้นสแตก)
    //                         ไปจนถึง items.get(items.size() - 1) (ยอดสแตก)
    //                         โดยมีความจุสูงสุดไม่เกิน capacity
    //
    // Representation Invariant (RI):
    //   - items != null
    //   - capacity > 0
    //   - items.size() <= capacity
    //   - ข้อมูลทุกตัวใน items ต้องไม่เป็น null (ไม่อนุญาตให้เก็บ null)

    // ============================================================
    // 3. Representative Checker
    // ============================================================
    private void checkRep() {
        assert items != null : "items list must not be null";
        assert capacity > 0 : "capacity must be positive";
        assert items.size() <= capacity : "size cannot exceed capacity";
        for (E item : items) {
            assert item != null : "elements cannot be null";
        }
    }

    // ============================================================
    // 4. Creator
    // ============================================================
    /**
     * สร้าง BoundedStack ใหม่พร้อมกำหนดความจุสูงสุด
     * @param capacity ความจุสูงสุดของ Stack (ต้องมากกว่า 0)
     * @throws IllegalArgumentException ถ้า capacity <= 0
     */
    public BoundedStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.items = new ArrayList<>();
        checkRep();
    }

    // ============================================================
    // 5. Observers
    // ============================================================
      /**
     * คืนค่าจำนวนสมาชิกที่มีอยู่ใน stack ขณะนี้
     * @return จำนวนสมาชิกปัจจุบันของ stack
     */
    public int size() {
        return items.size();
    }
      /**
     * บอกความจุสูงสุดที่กำหนดไว้ตอนสร้าง
     * @return ค่าความจุที่กำหนด
     */
    public int capacity() {
        return capacity;
    }
    /**
     * ตรวจสอบว่า stack ว่างหรือไม่
     * @return true ถ้า stack ว่าง, false ถ้า stack ไม่ว่าง
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
    /**
     * ตรวจสอบว่า stack เต็มหรือไม่
     * @return true ถ้า stack เต็ม, false ถ้ายังไม่เต็ม
     */
    public boolean isFull() {
        return items.size() == capacity;
    }

    /**
     * แอบดูข้อมูลตัวบนสุดของ Stack โดยไม่ดึงออก
     * @return ข้อมูลตัวบนสุด
     * @throws IllegalStateException ถ้า Stack ว่างเปล่า
     */
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot peek from an empty stack");
        }
        return items.get(items.size() - 1);
    }

    // ============================================================
    // 6. Mutators
    // ============================================================
    /**
     * เพิ่มข้อมูลลงบนสุดของ Stack
     * @param item ข้อมูลที่ต้องการเพิ่ม (ต้องไม่เป็น null)
     * @throws IllegalArgumentException ถ้า item เป็น null
     * @throws IllegalStateException ถ้า Stack เต็ม
     */
    public void push(E item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (isFull()) {
            throw new IllegalStateException("Cannot push into a full stack");
        }
        items.add(item);
        checkRep(); // ตรวจสอบความถูกต้องของ RI หลังแก้ไขข้อมูล
    }

    /**
     * ดึงข้อมูลตัวบนสุดของ Stack ออก และคืนค่าข้อมูลนั้น
     * @return ข้อมูลตัวบนสุดที่ถูกดึงออก
     * @throws IllegalStateException ถ้า Stack ว่างเปล่า
     */
    public E pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot pop from an empty stack");
        }
        E removed = items.remove(items.size() - 1);
        checkRep(); // ตรวจสอบความถูกต้องของ RI หลังแก้ไขข้อมูล
        return removed;
    }

    // ============================================================
    // 7. Producer
    // ============================================================
    /**
     * คัดลอก Stack ปัจจุบันเพื่อสร้าง BoundedStack ก้อนใหม่ที่มีข้อมูลและ ความจุเหมือนเดิม
     * @return BoundedStack ก้อนใหม่
     */
    public BoundedStack<E> copy() {
        BoundedStack<E> newStack = new BoundedStack<>(this.capacity);
        for (E item : this.items) {
            newStack.push(item);
        }
        return newStack;
    }
}