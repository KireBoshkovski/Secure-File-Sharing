package mk.ukim.finki.ib.filesharing.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.model.UploadedFile;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.service.FileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping({"/", "/home", "/files"})
@AllArgsConstructor
public class TempController {
    private final FileService fileService;

    @GetMapping
    public String index(@AuthenticationPrincipal User user, Model model) {
        List<UploadedFile> files = fileService.findByAccess(user);
        model.addAttribute("files", files);
        return "home";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access")
    public String showAllAccessibleFilesByUsername(@AuthenticationPrincipal User user, Model model) {
        List<UploadedFile> files = fileService.findByAccess(user);
        model.addAttribute("files", files);
        return "files-access";
    }

    @GetMapping("/created")
    public String showCreatedByUser(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("files", fileService.findByOwner(user));
        return "files-created";
    }
}
