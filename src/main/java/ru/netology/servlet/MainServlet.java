package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepositoryStubImpl;
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
    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
    final var repository = context.getBean(PostRepositoryStubImpl.class);
    final var service = context.getBean(PostService.class);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final String path = req.getRequestURI();
      final String method = req.getMethod();
      if (path.equals(BASE)) {
        if (method.equals(METHOD_GET)) {
          controller.all(resp);
        } else if (method.equals(METHOD_POST)) {
          controller.save(req.getReader(), resp);
        }
      } else if (path.matches(BASE_BY_ID)) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
        if (method.equals(METHOD_GET)) {
          controller.getById(id, resp);
        } else if (method.equals(METHOD_DELETE)) {
          controller.removeById(id, resp);
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

