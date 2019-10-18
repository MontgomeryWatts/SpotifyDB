package com.spotifydb.controllers;

import com.spotifydb.controllers.models.ViewAlbum;
import com.spotifydb.repositories.DynamoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/albums")
public class AlbumController {
  @Autowired
  private DynamoRepository repo;
  private static Logger logger = LoggerFactory.getLogger(AlbumController.class);

  @Async
  @ResponseBody
  @GetMapping("/{id}")
  public CompletableFuture<ResponseEntity<ViewAlbum>> getAlbumById(@PathVariable String id){
    logger.info("GET request received at /albums/{}", id);

    CompletableFuture<Map<String, AttributeValue>> future = repo.getAlbumById(id);
    Map<String, AttributeValue> item = future.join();

    if (item.isEmpty()) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());

    ViewAlbum album = new ViewAlbum(item);
    return CompletableFuture.completedFuture(ResponseEntity.ok(album));
  }
}
