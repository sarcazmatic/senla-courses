package com.senla.courses.service.literature;

import com.senla.courses.dto.LiteratureDTO;

import java.util.List;

public interface LiteratureService {

    Long add(LiteratureDTO literatureDTO, Long id);

    LiteratureDTO edit(LiteratureDTO literatureDTO, Long id);

    LiteratureDTO findById(Long id);

    List<LiteratureDTO> findByText(String text, int from, int size);

    List<LiteratureDTO> findByModuleId(Long moduleId, int from, int size);

    void delete(Long id);

}
