package com.senla.courses.service.courses;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dto.course.CourseDTO;
import com.senla.courses.dto.course.CourseMapper;
import com.senla.courses.model.Course;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final CourseDAO courseDAO;

    @Override
    public Long addCourse(CourseDTO courseDTO) {
        return courseDAO.save(courseMapper.fromCourseDTO(courseDTO));
    }

    @Override
    public CourseDTO findCourse(Long id) {
        Course course = courseDAO.find(id);
        return courseMapper.fromCourse(course);
    }

    @Override
    public CourseDTO editCourse(CourseDTO courseDTO) {
        Course courseIn = courseMapper.fromCourseDTO(courseDTO);
        Course courseOut = courseDAO.update(courseIn);
        return courseMapper.fromCourse(courseOut);
    }

    @Override
    public List<CourseDTO> findCourses(String text, int from, int size) {
        List<Course> courses = courseDAO.findAll(text, from, size);
        return courses.stream().map(courseMapper::fromCourse).toList();
    }

    @Override
    public void deleteCourse(Long id) {
        courseDAO.deleteById(id);
    }

}
