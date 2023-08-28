package com.ocho.what2do.common.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OauthController {
  @GetMapping("/oauth2/sign-up")
  public String signup() {
    return "oauth2/sign-up";
  }

  @GetMapping("/oauth2/kakao")
  public String KakaoLogin(@RequestParam String code, Model model) {
    model.addAttribute("code", code);
    return "oauth2/getcode";
  }
}
