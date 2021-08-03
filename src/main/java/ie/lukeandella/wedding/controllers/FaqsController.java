package ie.lukeandella.wedding.controllers;

import ie.lukeandella.wedding.pojos.Faq;
import ie.lukeandella.wedding.pojos.Gift;
import ie.lukeandella.wedding.services.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
public class FaqsController {

    @Autowired
    FaqService faqService;

    @Autowired
    public FaqsController(FaqService faqService){
        this.faqService = faqService;
    }

    @GetMapping("/faqs")
    public String showAllFaqs(Model model){
        model.addAttribute("faqs", faqService.getFaqs());
        model.addAttribute("faq_to_add_or_update", new Faq());
        return "faqs/faqs";
    }

    /*
        * Update a FAQ item
     */
    @PostMapping("/faqs/update/{id}")
    public String updateGiftInfo(@PathVariable("id") Long faqId, @ModelAttribute("faq_to_add_or_update") Faq faqToUpdate, Model model){
        //Update the FAQ object with info provided by the admin
        faqService.updateFaq(faqId, faqToUpdate.getQuestion(), faqToUpdate.getAnswer(), LocalDateTime.now());
        model.addAttribute("updated_faq", faqService.getFaqById(faqId));
        return "faqs/faq-updated";
    }

    /*
        * Delete a FAQ item
     */
    @PostMapping("/faqs/delete/{id}")
    public String deleteFaq(@PathVariable("id") Long faqId, Model model){
        model.addAttribute("faq", faqService.getFaqById(faqId));
        faqService.deleteFaq(faqId);
        return "faqs/faq-deleted";
    }
}
