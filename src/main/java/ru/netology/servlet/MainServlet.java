package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;

  private static final String METHOD_GET = "GET";
  private static final String METHOD_POST = "POST";
  private static final String METHOD_DELETE = "DELETE";
  private static final String BASE = "/api/posts";
  private static final String BASE_BY_ID = BASE + "/\\d+";

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final String path = req.getRequestURI();
      final String method = req.getMethod();
      // primitive routing
      if (path.equals(BASE)) {
        if (method.equals(METHOD_GET)) {
          controller.all(resp);
          return;
        } else if (method.equals(METHOD_POST)) {
          controller.save(req.getReader(), resp);
          return;
        }
      } else if (path.matches(BASE_BY_ID)) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
        if (method.equals(METHOD_GET)) {
          controller.getById(id, resp);
          return;
        } else if (method.equals(METHOD_DELETE)) {
          controller.removeById(id, resp);
          return;
        }
      } else {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

