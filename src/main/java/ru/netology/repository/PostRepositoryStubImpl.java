package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PostRepositoryStubImpl implements PostRepository {
  private final ConcurrentMap<Long, Post> repository = new ConcurrentHashMap<Long, Post>();

  public List<Post> all() {
    return new ArrayList<>(repository.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.empty();
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      long count = repository.size() + 1;
      post.setId(count);
      repository.put(count, post);
    } else {
      if (!repository.containsKey(post.getId())) {
        throw new NotFoundException("Поста с таким id не существует");
      }
      repository.put(post.getId(), post);
    }
    return post;
  }

  public void removeById(long id) {
    repository.remove(id);
  }
}
