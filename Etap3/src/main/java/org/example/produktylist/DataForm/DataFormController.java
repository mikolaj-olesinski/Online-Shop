package org.example.produktylist.DataForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/data-forms")
public class DataFormController {

    private final DataFormService dataFormService;

    public DataFormController(DataFormService dataFormService) {
        this.dataFormService = dataFormService;
    }

    @GetMapping("/{id}")
    public String viewDataForm(@PathVariable long id, Model model) {
        DataForm dataForm = dataFormService.getDataFormById(id);
        model.addAttribute("dataForm", dataForm);
        return "dataForms/view";
    }

    @PostMapping("/{id}/edit")
    public String editDataForm(@PathVariable long id, @ModelAttribute DataForm updatedDataForm, Model model) {
        DataForm existingDataForm = dataFormService.getDataFormById(id);

        // Sprawdzenie, czy DataForm istnieje
        if (existingDataForm == null) {
            // Jeśli DataForm jest null, wypisujemy komunikat w konsoli
            System.out.println("DataForm o ID " + id + " nie istnieje w bazie.");
            return "redirect:/error"; // Można przekierować na stronę błędu, jeśli chcesz
        }

        // Aktualizacja danych formularza
        existingDataForm.setFirstName(updatedDataForm.getFirstName());
        existingDataForm.setLastName(updatedDataForm.getLastName());
        existingDataForm.setEmail(updatedDataForm.getEmail());
        existingDataForm.setPhone(updatedDataForm.getPhone());
        existingDataForm.setCompany(updatedDataForm.getCompany());
        existingDataForm.setAddress(updatedDataForm.getAddress());
        existingDataForm.setPostalCode(updatedDataForm.getPostalCode());
        existingDataForm.setCity(updatedDataForm.getCity());
        existingDataForm.setState(updatedDataForm.getState());
        existingDataForm.setCountry(updatedDataForm.getCountry());
        existingDataForm.setDeliveryType(updatedDataForm.getDeliveryType());
        existingDataForm.setPaymentType(updatedDataForm.getPaymentType());
        existingDataForm.setNotes(updatedDataForm.getNotes());

        // Zaktualizowanie formularza w bazie
        dataFormService.updateDataForm(existingDataForm);

        // Dodanie zaktualizowanego formularza do modelu
        model.addAttribute("dataForm", existingDataForm);

        // Przekierowanie na stronę z widokiem formularza
        return "dataForms/view";
    }
}
