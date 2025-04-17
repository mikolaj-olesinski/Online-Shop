package org.example.produktylist;

import org.example.produktylist.Service.DataFormService;
import org.example.produktylist.Entity.DataForm;
import org.example.produktylist.Repository.DataFormRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataFormServiceTest {

    @Mock
    private DataFormRepository dataFormRepository;

    private DataFormService dataFormService;
    private DataForm testDataForm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dataFormService = new DataFormService(dataFormRepository);

        testDataForm = DataForm.builder()
                .id(1L)
                .address("Test Address")
                .city("Test City")
                .country("Test Country")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    void getDataFormById_ExistingDataForm_ReturnsDataForm() {
        when(dataFormRepository.findById(1L)).thenReturn(java.util.Optional.of(testDataForm));

        DataForm result = dataFormService.getDataFormById(1L);

        assertNotNull(result);
        assertEquals("Test Address", result.getAddress());
        assertEquals("Test City", result.getCity());
        verify(dataFormRepository).findById(1L);
    }

    @Test
    void getDataFormById_NonExistingDataForm_ThrowsException() {
        when(dataFormRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dataFormService.getDataFormById(999L);
        });
        assertEquals("DataForm not found", exception.getMessage());
    }

    @Test
    void updateDataForm_Success() {
        testDataForm.setCity("Updated City");
        when(dataFormRepository.save(testDataForm)).thenReturn(testDataForm);

        DataForm result = dataFormService.updateDataForm(testDataForm);

        assertNotNull(result);
        assertEquals("Updated City", result.getCity());
        verify(dataFormRepository).save(testDataForm);
    }

}
