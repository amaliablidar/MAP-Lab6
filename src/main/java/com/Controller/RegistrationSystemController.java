package com.Controller;
import com.Exception.InvalidInputException;
import com.Model.Course;
import com.Model.Student;
import com.Model.Teacher;
import com.Repository.JDBC_CourseRepo;
import com.Repository.JDBC_StudentRepo;
import com.Repository.JDBC_TeacherRepo;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;



public class RegistrationSystemController {

    private final JDBC_StudentRepo student;
    private final JDBC_TeacherRepo teacher;
    private final JDBC_CourseRepo course;

    /**
     * initializarea controlerului
     *
     */
    public RegistrationSystemController(JDBC_StudentRepo studentRepository, JDBC_TeacherRepo teacherRepository, JDBC_CourseRepo courseRepository) {
        this.student = studentRepository;
        this.teacher = teacherRepository;
        this.course = courseRepository;
    }

    /**
     * @return repository-ul pentru student
     */
    public JDBC_StudentRepo getStudentRepository() {
        return student;
    }

    /**
     * @return repository-ul pentru profesor
     */
    public JDBC_TeacherRepo getTeacherRepository() {
        return teacher;
    }

    /**
     * @return repository-ul pentru cursuri
     */
    public JDBC_CourseRepo getCourseRepository() {
        return course;
    }

    /**
     * @return lista cursurilor cu locuri disponibile
     */
    public List<Course> retrieveCoursesWithFreePlaces() {
        ArrayList<Course>coursesWithFreePlaces= (ArrayList<Course>) course.findAll();

        return coursesWithFreePlaces
                .stream()
                .filter(courseObj -> (courseObj.getMaxStudents() -courseObj.getStudentsList().size())>0)
                .collect(Collectors.toList());

    }
    /**
     * cauta un profesor
     * @param ID-int, id-ul profesorului
     * @return Profesorul daca a fost gasit
     */
    public Teacher findTeacher(int ID)
    {
        Teacher foundTeacher;
        foundTeacher=teacher.findOne(ID);

        return foundTeacher;
    }


    /**
     * Metoda inregistreaza un student la un curs si de asemenea ii adauga cursul in lista de cursuri personala
     *
     * @param studentToBeRegistered-studentul care trebuie inregistrat
     * @param courseToBeRegistered-cursul la care trebuie inregistrat studentul
     * @return true- daca studentul a fost inregistrat cu succes, null in caz contrar
     */
    public boolean register(Student studentToBeRegistered, Course courseToBeRegistered) throws InvalidInputException {

        Student foundStudent = this.student.findOne(studentToBeRegistered.getId());
        Course foundCourse = this.course.findOne(courseToBeRegistered.getId());

        if (foundStudent == null || foundCourse == null) {
            throw new InputMismatchException("Id not found!");
        }
        List<Student>studentsEnrolledForCourse=foundCourse.getStudentsList();
        if (studentsEnrolledForCourse.size()==courseToBeRegistered.getMaxStudents())
            return false;
        if(studentToBeRegistered.getTotalCredits()+studentToBeRegistered.getTotalCredits()>30)
            return false;

        boolean alreadyEnrolledStudent=studentsEnrolledForCourse
                .stream()
                .anyMatch(student1 ->student1.compareTo(studentToBeRegistered));

        if (alreadyEnrolledStudent)
            return false;
        int creditNr = foundStudent.getTotalCredits() + foundCourse.getCredits();
        foundStudent.setTotalCredits(creditNr);
        student.update(foundStudent);
        student.saveEnrolment(studentToBeRegistered.getId(),foundCourse.getId());
        return true;

    }


    /**
     * cauta un curs
     * @param ID-int, id-ul cursului
     * @return cursul daca a fost gasit
     */
    public Course findCourse(int ID)
    {
        Course foundCourse;
        foundCourse=course.findOne(ID);
        return foundCourse;
    }


    /**
     * toti studentii inscrisi la un anumit curs
     * @param courseID-id-ul cursului in cauza
     * @return lista cu studentii inscrisi la curs
     */
    public List<Student> studentsEnrolled(int courseID) {
        try{
            Course foundCourse=findCourse(courseID);
            if (foundCourse != null) {
                return foundCourse.getStudentsList();
            }} catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     *
     * @param teacher-profesorul la care se vrea sa se afiseze cursurile
     * @return lista de cursuri a unui profesor
     */
    public List<Course>coursesTeacher(Teacher teacher)
    {
        if (this.teacher.findOne(teacher.getId()) != null) {
            return teacher.getCourses();
        }

        return null;
    }


    /**
     * cauta un student
     * @param ID-int, id-ul studentului
     * @return studentul daca a fost gasit
     */
    public Student findStudent(int ID)
    {
        Student foundStudent;
        foundStudent=student.findOne(ID);

        return foundStudent;
    }
    /**
     * toate cursurile
     * @return lista de cursuri
     */
    public List<Course>getAllCourses()
    {
        return (List<Course>)course.findAll();
    }




}