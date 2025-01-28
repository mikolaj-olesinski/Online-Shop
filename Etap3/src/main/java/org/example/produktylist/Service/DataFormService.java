package org.example.produktylist.Service;

import org.example.produktylist.Entity.DataForm;
import org.example.produktylist.Repository.DataFormRepository;
import org.springframework.stereotype.Service;

@Service
public class DataFormService {

    private final DataFormRepository dataFormRepository;

    public DataFormService(DataFormRepository dataFormRepository) {
        this.dataFormRepository = dataFormRepository;
    }

    public DataForm getDataFormById(long id) {
        return dataFormRepository.findById(id).orElseThrow(() -> new RuntimeException("DataForm not found"));
    }

    public DataForm updateDataForm(DataForm dataForm) {
        return dataFormRepository.save(dataForm);
    }
}
