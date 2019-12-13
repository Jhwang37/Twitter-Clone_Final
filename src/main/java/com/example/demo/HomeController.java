package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    messageRepository messageRepository;
    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listMessage(Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("datetime", new Date());
        return "messagelist";
    }

    @GetMapping("/add")
    public String messageForm(Model model) {
        model.addAttribute("message", new Message());
        return "messageshow";
    }

    @PostMapping("/process")
    public String processForm(@Valid Message message, BindingResult result,
                              @RequestParam("file") MultipartFile file) {
        if (result.hasErrors() & file.isEmpty()) {
            return "redirect:/add";
        }
        try {
           Map uploadResult = cloudc.upload(file.getBytes(),
                   ObjectUtils.asMap("resourcetype", "auto"));
                message.setPic(uploadResult.get("url").toString());
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/add";
        }

        LocalDate date = LocalDate.now();
        message.setDate(date);
        messageRepository.save(message);
        return "redirect:/";
    }

//    @PostMapping("/add")
//    public String processPic(@ModelAttribute Message message, @RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return "redirect:/add";
//        }
//        try {
//            Map uploadResult = cloudc.upload(file.getBytes(),
//                    ObjectUtils.asMap("resourcetype", "auto"));
//            message.setPic(uploadResult.get("url").toString());
//            messsageRepository.save(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "redirect:/add";
//        }
//        return "redirect:/";

    }

