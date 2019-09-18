package com.spotifydb.controllers;

import com.spotifydb.controllers.models.ViewArtist;
import com.spotifydb.repositories.DynamoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/artists")
public class ArtistController {

  private DynamoRepository repo;

  @Async
  @ResponseBody
  @GetMapping("/{id}")
  public CompletableFuture<ViewArtist> getArtistById(@PathVariable String id){
    CompletableFuture<Map<String, AttributeValue>> artistItem = repo.getArtistById(id);
    ViewArtist artist = new ViewArtist(artistItem.join());
    return CompletableFuture.completedFuture(artist);
  }
}
