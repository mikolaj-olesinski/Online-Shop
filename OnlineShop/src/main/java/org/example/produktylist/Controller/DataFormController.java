package org.example.produktylist.Controller;

import org.example.produktylist.Service.DataFormService;
import org.example.produktylist.Entity.DataForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler obsługujący żądania związane z formularzami danych.
 */
@Controller
@RequestMapping("/data-forms")
public class DataFormController {

    private final DataFormService dataFormService;

    public DataFormController(DataFormService dataFormService) {
        this.dataFormService = dataFormService;
    }

    /**
     * Metoda zwracająca formularz dodawania nowego formularza danych.
     * @param model model danych
     * @return nazwa widoku
     */
    @GetMapping("/{id}")
    public String viewDataForm(@PathVariable long id, Model model) {
        DataForm dataForm = dataFormService.getDataFormById(id);
        model.addAttribute("dataForm", dataForm);
        return "dataForms/view";
    }

    /**
     * Metoda zwracająca formularz dodawania nowego formularza danych.
     * @param model model danych
     * @return nazwa widoku
     */
    @PostMapping("/{id}/edit")
    public String editDataForm(@PathVariable long id, @ModelAttribute DataForm updatedDataForm, Model model) {
        DataForm existingDataForm = dataFormService.getDataFormById(id);

        if (existingDataForm == null) {
            System.out.println("DataForm o ID " + id + " nie istnieje w bazie.");
            return "redirect:/error";
        }

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

        dataFormService.updateDataForm(existingDataForm);

        model.addAttribute("dataForm", existingDataForm);

        return "dataForms/view";
    }
}
