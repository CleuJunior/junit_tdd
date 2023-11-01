package br.com.cleonildojunior.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.cleonildojunior.service.CourseService;

/**
 * CourseBusiness = SUT - System (Method) Under Test
 */
public class CourseBusiness {

    /** CourseService is a Dependency */
    private final CourseService service;

    public CourseBusiness(CourseService service) {
        this.service = service;
    }
    
    public List<String> retriveCoursesRelatedToSpring(String student) {
        if("Foo Bar".equals(student))
            return new ArrayList<>();

        return this.service
                .retrieveCourses(student)
                .stream()
                .filter(c -> c.contains("Spring"))
                .toList();
    }
    
    public void deleteCoursesNotRelatedToSpring(String student) {
        
        var allCourses = service.retrieveCourses(student);
        
        for (String course : allCourses) {
            if (!course.contains("Spring")) {
                service.deleteCourse(course);
            }
        }

    }
    
}
