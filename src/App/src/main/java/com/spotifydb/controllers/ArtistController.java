package com.spotifydb.controllers;

import com.spotifydb.repositories.DynamoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/artists")
public class ArtistController {

  private DynamoRepository repo;

  @GetMapping("/{id}")
  @ResponseBody
  public void getArtistById(@PathVariable String id){

  }
}
