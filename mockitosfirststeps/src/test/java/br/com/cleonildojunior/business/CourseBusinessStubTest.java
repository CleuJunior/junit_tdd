package br.com.cleonildojunior.business;

import br.com.cleonildojunior.service.stubs.CourseServiceStub;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseBusinessStubTest {

    @Test
    void testCoursesRelatedToSpring_When_UsingAStub() {

        // Given / Arrange
        CourseBusiness business = new CourseBusiness(new CourseServiceStub());
        
        // When / Act
        List<String> filteredCourses = business.retriveCoursesRelatedToSpring("Leandro");
        
        // Then / Assert
        assertEquals(4, filteredCourses.size());
    }
    
    @Test
    void testCoursesRelatedToSpring_When_UsingAFooBarStudent() {
        
        // Given / Arrange
        CourseBusiness business = new CourseBusiness(new CourseServiceStub());
        
        // When / Act
        List<String> filteredCourses = business.retriveCoursesRelatedToSpring("Foo Bar");
        
        // Then / Assert
        assertEquals(0, filteredCourses.size());
    }

}
