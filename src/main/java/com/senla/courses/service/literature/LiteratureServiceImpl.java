package com.senla.courses.service.literature;

import com.senla.courses.dao.LiteratureDAO;
import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dto.LiteratureDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.LiteratureMapper;
import com.senla.courses.model.Literature;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LiteratureServiceImpl implements LiteratureService {

    private final ModuleDAO moduleDAO;
    private final LiteratureDAO literatureDAO;
    private final LiteratureMapper literatureMapper;

    @Override
    public Long add(LiteratureDTO literatureDTO, Long id) {
        Literature literature = literatureMapper.fromLiteratureDTO(literatureDTO);
        literature.setModule(moduleDAO.find(id).orElseThrow(()
                -> new NotFoundException("На нашли литературы по id " + id)));
        return literatureDAO.save(literature);
    }

    @Override
    public LiteratureDTO edit(LiteratureDTO literatureDTO, Long id) {
        Literature literature = literatureDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти задачу"));
        return literatureMapper.fromLiterature(literatureDAO.update(literatureMapper.updateLiterature(literature, literatureDTO)));
    }

    @Override
    public LiteratureDTO findById(Long id) {
        Literature literature = literatureDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти задачу"));
        return literatureMapper.fromLiterature(literature);
    }

    @Override
    public List<LiteratureDTO> findByText(String text, int from, int size) {
        List<Literature> literatureList = literatureDAO.findAllByText(text, from, size);
        if (literatureList.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        return literatureList.stream().map(literatureMapper::fromLiterature).toList();
    }

    @Override
    public List<LiteratureDTO> findByModuleId(Long moduleId, int from, int size) {
        List<Literature> literatureList = literatureDAO.findByModuleId(moduleId, from, size);
        if (literatureList.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        return literatureList.stream().map(literatureMapper::fromLiterature).toList();
    }

    @Override
    public void delete(Long id) {
        literatureDAO.deleteById(id);
    }

}
