public class Main {
    //Gradebook->Student->Subject->Assessment(Assignment, Exam, Quiz)
    public static void main(String[] args) {
        Gradebook gradebook = new Gradebook(); 
        gradebook.load();
        // gradebook.addStudent(new Student("bob"));
        // gradebook.addStudent(new Student("joe"));
        System.out.println(gradebook.toString());
    }
}
