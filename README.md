# Lab: Bounded Stack Implementation (ADT)

## 📌 Overview
โปรเจกต์นี้เป็นการลงมือสร้าง **BoundedStack** ซึ่งเป็น Abstract Data Type (ADT) ประเภท **Mutable LIFO Stack** ที่จำกัดความจุสูงสุด (Capacity) โดยเขียนด้วยภาษา Java และประยุกต์ใช้หลักการออกแบบ ซ่อนรายละเอียดภายใน (Representation Independence) การดักจับข้อผิดพลาด (Defensive Programming) และการตรวจสอบข้อบังคับสภาวะ (Representation Invariant Verification)

---

## Design & Specification

### 1. Abstract Function (AF)
อธิบายการแมปสถานะตัวแปรภายในไปสู่สถานะเชิงนามธรรมที่ผู้ใช้มองเห็น:
* `AF(items, capacity)` = สแตกที่มีข้อมูลเก็บอยู่ตั้งแต่ `items.get(0)` (ก้นสแตก) ไปจนถึง `items.get(items.size() - 1)` (ยอดสแตก) โดยมีความจุสูงสุดไม่เกิน `capacity`

### 2. Representation Invariant (RI)
กฎเหล็กที่สถานะภายในต้องเป็นจริงเสมอ:
1. `items != null` (ตัวเก็บข้อมูลต้องถูกสร้างขึ้นแล้วเสมอ)
2. `capacity > 0` (ความจุสูงสุดต้องเป็นจำนวนเต็มบวก)
3. `items.size() <= capacity` (จำนวนสมาชิกปัจจุบันต้องไม่เกินความจุสูงสุด)
4. `forall item in items: item != null` (ไม่อนุญาตให้เก็บสมาชิกที่เป็นค่า `null`)

---

## ADT Method Classification

| ประเภท (Role) | เมธอด | คำอธิบาย |
| :--- | :--- | :--- |
| **Creator** | `BoundedStack(int capacity)` | สร้าง Stack ใบใหม่ขึ้นมาจากศูนย์ พร้อมกำหนดขนาดความจุ |
| **Observer** | `size()`, `capacity()`, `isEmpty()`, `isFull()`, `peek()` | อ่าน/คืนค่าสถานะ โดยไม่มีการแก้ไขข้อมูลภายใน |
| **Mutator** | `push(E item)`, `pop()` | แก้ไข/เปลี่ยนแปลงข้อมูลใน Stack ใบเดิม |
| **Producer** | `copy()` | สร้าง Stack ใบใหม่ที่มีข้อมูลเหมือนเดิม โดยไม่กระทบ Object เดิม |

---

## 🛡️ Defensive Programming & Exception Handling

มีการดักจับข้อผิดพลาดล่วงหน้าเพื่อป้องกันไม่ให้ ADT หลุดออกจากกฎ RI:
* **`IllegalArgumentException`**: โยนเมื่อส่ง `capacity <= 0` (รวมทั้งค่าติดลบ) หรือเมื่อสั่ง `push(null)`
* **`IllegalStateException`**: โยนเมื่อสั่ง `push` ใส่ Stack ที่เต็มแล้ว (`isFull()`) หรือสั่ง `pop()` / `peek()` ใน Stack ที่ว่างเปล่า (`isEmpty()`)
* **Assertion (`checkRep()`)**: ใช้ตรวจ Representation Invariant หลังทุก mutator เรียกจบ — เปิดใช้งานได้ด้วย flag `-ea` เท่านั้น (ดูหัวข้อ How to Run)

---

## 🎯 ทำไมถึงออกแบบแบบนี้ (Design Decisions)

สรุปสั้นๆ ว่าทำไมตัดสินใจแบบนี้ในแต่ละจุด และมีทางเลือกอื่นอะไรที่คิดแล้วไม่เลือก

| เรื่อง | เลือกอะไร | เพราะอะไร | ทางเลือกอื่นที่ไม่เลือก |
|---|---|---|---|
| Mutable/Immutable | Mutable | stack ปกติควร push/pop แล้วเปลี่ยนของเดิมเลย เหมือนกองจานจริง ไม่ต้องสร้างกองใหม่ทุกครั้ง | Immutable — ต้องคืน object ใหม่ทุกครั้งที่ push ดูแปลกและเปลืองโดยไม่จำเป็น |
| capacity ≤ 0 | ห้าม throw IllegalArgumentException | stack ที่ push อะไรไม่ได้เลย (capacity=0) ไม่มีประโยชน์ ควรเตือนตั้งแต่สร้าง | ปล่อยผ่านให้ capacity=0 ใช้ได้ — แต่จะดูเหมือน bug มากกว่าตั้งใจ |
| push/pop ตอนเต็ม/ว่าง | throw Exception | เป็นความผิดของคนเรียกใช้ (client) เอง ไม่ใช่ bug ในโค้ดเรา จึงต้อง throw ตลอด | ใช้ assertion แทน — จะปิดได้เวลารันจริง (ไม่ใส่ -ea) ซึ่งอันตราย เพราะ error จาก client จะไม่ถูกจับเลย |
| Representation | ArrayList, ไม่มี `top` แยก | items.size()-1 บอกตำแหน่งบนสุดได้อยู่แล้ว ถ้ามี top แยกอีกตัวต้องคอย sync กันตลอด เสี่ยง bug | เก็บ top index แยก — เร็วกว่านิดหน่อยแต่มีของซ้ำที่ต้องดูแล 2 ที่ |
| Representation: ArrayList vs array | ArrayList | ทดลองเปลี่ยนจริงเป็น `Object[]` (ดูหัวข้อ "ทดลองเปลี่ยน Representation" ด้านล่าง) พบว่า client (test) ไม่ต้องแก้ logic เลยแม้แต่บรรทัดเดียวทั้งสองแบบ — แต่ array ต้องจองพื้นที่หน่วยความจำทั้งหมดทันทีตอนสร้าง (`new Object[capacity]`) ขณะที่ ArrayList จองแบบ lazy (ขยายทีหลังตามการใช้งานจริง) ทำให้ ArrayList รองรับเคส capacity มหาศาลได้โดยไม่ต้องเขียน logic พิเศษเพิ่ม | array (`Object[]`) — ต้อง cast ตอนอ่านค่ากลับเป็น `E` (unchecked cast) และล้มเหลวทันทีด้วย `OutOfMemoryError` เมื่อ capacity = `Integer.MAX_VALUE` เพราะจองพื้นที่ล่วงหน้าทั้งหมด |
| push(null) | throw IllegalArgumentException | มองว่า null ก็คือ argument ที่ผิดแบบหนึ่ง จัดกลุ่มเดียวกับ error อื่นในคลาสนี้ | NullPointerException — เป็นธรรมเนียมของ Java แต่เลือกไม่ใช้เพื่อให้ exception type สม่ำเสมอกัน |
| Generic `<E>` | ทำเป็น generic | ใช้ได้กับข้อมูลทุกชนิด ไม่ต้องเขียนใหม่ทุกครั้ง แถมไม่ได้ยากขึ้นมากเพราะใช้ ArrayList<E> อยู่แล้ว | เก็บเฉพาะ int/String — เขียนง่ายกว่าแต่ใช้ซ้ำไม่ได้ |
| copy() (Producer) | เรียก push() ซ้ำทีละตัว | ได้ validation (เช่น null check) ฟรีไปด้วย ไม่ต้องเขียนซ้ำ | copy ArrayList ตรงๆ — เร็วกว่าแต่ข้าม validation ไปเลย เสี่ยง bug ถ้า push() เปลี่ยนทีหลัง |

---

## 🔬 ทดลองเปลี่ยน Representation (ตอบคำถาม: client จะพังกี่บรรทัด?)

เพื่อพิสูจน์ว่ากำแพงนามธรรม (abstraction) แน่นหนาจริง ได้ลองทำสำเนา `BoundedStack.java` ขึ้นมาอีกชุด แล้วเปลี่ยน representation จาก `private final List<E> items` เป็น `private final Object[] items` (พร้อมตัวแปร `count` แทน `items.size()`) โดย **ไม่แตะ public method signature ใดๆ เลย** จากนั้นเอา `TestRunner.java` ตัวเดิมมารันกับ ADT เวอร์ชันใหม่ (เปลี่ยนแค่ชื่อ type ตอนประกาศตัวแปร ไม่แก้ logic การเรียกใช้แม้แต่บรรทัดเดียว)

**ผลลัพธ์:**
- **Correctness**: test เดิมทั้งหมดที่ไม่เกี่ยวกับ capacity มหาศาลผ่านหมด — ยืนยันว่า client ไม่ต้องแก้โค้ดเรียกใช้เลยเมื่อเปลี่ยน representation เพราะ `items` เป็น `private` และไม่มี method ไหนคืน reference ของตัวเก็บข้อมูลออกไปตรงๆ
- **พบ trade-off ที่ซ่อนอยู่**: เคสทดสอบ capacity = `Integer.MAX_VALUE` ที่ผ่านปกติกับ ArrayList (เพราะจองพื้นที่แบบ lazy) กลับล้มด้วย `OutOfMemoryError: Requested array size exceeds VM limit` เมื่อใช้ array ธรรมดา เพราะ `new Object[capacity]` ต้องจองพื้นที่ทั้งหมดทันทีตอนสร้าง ไม่มีทางเลี่ยงด้วยข้อจำกัดของภาษา Java เอง

**สรุป**: การเปลี่ยน representation ไม่ทำให้ client พังในแง่ compile หรือ logic เลย (0 บรรทัดที่ต้องแก้) แต่ abstraction ปกป้องแค่ "หน้าตาการเรียกใช้" เท่านั้น ไม่ได้ปกป้องพฤติกรรมเชิง performance/memory ที่ต่างกันระหว่าง implementation — เป็นเหตุผลเสริมที่ยืนยันว่าเลือก ArrayList ถูกทางแล้วสำหรับ ADT ตัวนี้

---

## ⚠️ ข้อจำกัดที่รู้อยู่: peek() กับข้อมูลที่แก้ไขได้ (mutable)

`peek()` คืนค่า element ตรงๆ ไม่ได้ copy ให้ใหม่ ถ้า `E` เป็นชนิดที่แก้ไขได้ (เช่น `int[]`) แล้ว client เอาค่าที่ได้จาก peek() ไปแก้ จะกระทบข้อมูลจริงในสแตกทันที เพราะเป็น object ตัวเดียวกัน (aliasing)

รู้ว่ามีจุดนี้อยู่ แต่ไม่ได้แก้เพราะการ copy ทุกครั้งที่ peek() จะทำให้โค้ดซับซ้อนขึ้นเยอะ (ต้องแก้ปัญหาเรื่อง copy ค่า generic type `E` ซึ่งไม่มีวิธีมาตรฐานใน Java) และงานส่วนใหญ่ที่ใช้ stack แบบนี้มักเก็บของที่แก้ไม่ได้อยู่แล้ว (เช่น String, Integer) จึงไม่ใช่ปัญหาจริงในทางปฏิบัติ

**พฤติกรรมนี้ถูกยืนยันด้วย test ที่รันได้จริงแล้ว** ไม่ใช่แค่คำอธิบายลอยๆ — ดู `testPeekAliasingBehavior()` (Test Group 8 ใน `TestRunner.java`) ซึ่ง push อาเรย์ที่แก้ไขได้เข้า stack แล้วแก้ค่าจากภายนอกผ่านตัวแปรที่ได้จาก `peek()` โดยตรง จากนั้นเรียก `peek()` ซ้ำเพื่อยืนยันว่าค่าที่อยู่ในสแตกเปลี่ยนตามจริง — ผลคือ `[PASS]` ยืนยันว่า aliasing เกิดขึ้นจริงตามที่ออกแบบไว้

---

## 🚀 How to Run Tests

โปรเจกต์นี้ใช้ระบบ Test ที่เขียนขึ้นเองโดย **ห้ามใช้ JUnit Framework**

1. **คอมไพล์ไฟล์ทั้งหมด:**
   ```bash
   javac BoundedStack.java TestRunner.java
   ```

2. **รัน test runner (ต้องใส่ flag `-ea` เพื่อเปิดใช้งาน assertion ใน `checkRep()`):**
   ```bash
   java -ea TestRunner
   ```

   ⚠️ **สำคัญ:** ถ้ารันด้วย `java TestRunner` เฉยๆ (ไม่มี `-ea`) assertion ทั้งหมดใน `checkRep()` จะถูกข้ามไปเงียบๆ โดย Java runtime — test จะยังรันได้ปกติแต่จะไม่ยืนยัน Representation Invariant เลย

3. **ผลลัพธ์ที่คาดหวัง:** โปรแกรมจะพิมพ์ `[PASS]`/`[FAIL]` ของแต่ละเคส ตามด้วยสรุปยอดรวม PASSED / FAILED / TOTAL ท้ายผลลัพธ์
