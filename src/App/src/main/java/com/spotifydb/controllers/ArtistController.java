package com.spotifydb.controllers;

import com.spotifydb.controllers.models.ViewArtist;
import com.spotifydb.controllers.models.ViewArtistAlbum;
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
    CompletableFuture<Map<String, AttributeValue>> future = repo.getArtistById(id);
    Map<String, AttributeValue> item = future.join();

    if (item.isEmpty()) return CompletableFuture.completedFuture(null);

    ViewArtist artist = new ViewArtist(item);
    return CompletableFuture.completedFuture(artist);
  }

  @Async
  @ResponseBody
  @GetMapping("/{id}/albums")
  public CompletableFuture<ViewArtistAlbum[]> getArtistAlbumsByArtistId(@PathVariable String id) {
    return CompletableFuture.completedFuture(null);
  }
}
