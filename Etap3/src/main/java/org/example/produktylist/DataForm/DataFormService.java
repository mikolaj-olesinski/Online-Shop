package org.example.produktylist.DataForm;

import org.springframework.stereotype.Service;

@Service
public class DataFormService {

    private final DataFormRepository dataFormRepository;

    public DataFormService(DataFormRepository dataFormRepository) {
        this.dataFormRepository = dataFormRepository;
    }

    // Pobranie formularza danych po ID
    public DataForm getDataFormById(long id) {
        return dataFormRepository.findById(id).orElseThrow(() -> new RuntimeException("DataForm not found"));
    }

    // Aktualizacja formularza danych
    public DataForm updateDataForm(DataForm dataForm) {
        return dataFormRepository.save(dataForm);
    }
}
