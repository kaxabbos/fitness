package com.fitness.controller;

import com.fitness.controller.main.Attributes;
import com.fitness.model.Trainers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/trainers")
public class TrainersCont extends Attributes {
    @GetMapping("/add")
    public String TrainerAdd(Model model) {
        AddAttributes(model);
        return "addTrainer";
    }

    @GetMapping("/delete/{id}")
    public String TrainerDelete(@PathVariable Long id) {
        trainersRepo.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/add")
    public String TrainerAddNew(Model model, @RequestParam String name, @RequestParam String producer, @RequestParam String country, @RequestParam String muscle, @RequestParam int optimal, @RequestParam int max, @RequestParam MultipartFile file) {
        String res = "";
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = "trainers/" + uuidFile + "_" + file.getOriginalFilename();
                    file.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (Exception e) {
                model.addAttribute("message", "Некорректный данные!");
                AddAttributes(model);
                return "addTrainer";
            }
        }

        Trainers trainer = new Trainers(name, res, producer, country, muscle, optimal, max);
        trainersRepo.save(trainer);

        return "redirect:/trainers/add";
    }

    @GetMapping("/edit/{id}")
    public String TrainerEdit(Model model, @PathVariable Long id) {
        AddAttributesTrainerEdit(model, id);
        return "editTrainer";
    }

    @PostMapping("/edit/{id}")
    public String TrainerEditOld(Model model, @RequestParam String name, @RequestParam String producer, @RequestParam String country, @RequestParam String muscle, @RequestParam int optimal, @RequestParam int max, @RequestParam MultipartFile file, @PathVariable Long id) {
        Trainers trainer = trainersRepo.getReferenceById(id);

        trainer.setName(name);
        trainer.setProducer(producer);
        trainer.setCountry(country);
        trainer.setMuscle(muscle);
        trainer.setOptimal(optimal);
        trainer.setMax(max);

        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String res = "";
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = "trainers/" + uuidFile + "_" + file.getOriginalFilename();
                    file.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (Exception e) {
                AddAttributesTrainerEdit(model, id);
                model.addAttribute("message", "Некорректный данные!");
                return "editTrainer";
            }
            trainer.setFile(res);
        }

        trainersRepo.save(trainer);
        return "redirect:/";
    }
}
