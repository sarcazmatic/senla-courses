package com.senla.courses.service.courses;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dto.CourseDTO;
import com.senla.courses.dto.CourseMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final CourseDAO courseDAO;

    @Override
    public Long addCourse(CourseDTO courseDTO) {
        return courseDAO.save(courseMapper.fromCourseDTO(courseDTO));
    }
}
