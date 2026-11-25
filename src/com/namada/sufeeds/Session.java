// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

// Remembers who is logged in. There is only ever one logged-in student at a time,
// and every screen needs the same answer, so the field is static (one shared copy).
public class Session {

    private static Student currentStudent;

    // Nobody should create a Session object; it only holds static helpers.
    private Session() {
    }

    public static Student getCurrentStudent() {
        return currentStudent;
    }

    public static void setCurrentStudent(Student student) {
        currentStudent = student;
    }

    // Handy check for screens: if this is false, send the user back to login.
    public static boolean isLoggedIn() {
        return currentStudent != null;
    }

    // Call on logout, so the next person cannot inherit this student's identity.
    public static void clear() {
        currentStudent = null;
    }
}