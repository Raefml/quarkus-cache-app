package com.example.application.controller;

import com.example.domain.service.CategoryService;
import com.example.domain.model.Category;
import io.quarkus.infinispan.client.Remote;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.infinispan.client.hotrod.RemoteCache;

import java.util.List;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryController {

    @Inject
    CategoryService categoryService;

    @Inject
    @Remote("categoryCache")
    RemoteCache<Long, Category> cache;

    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
        try {
            System.out.println("Recherche catégorie ID: " + id);

            long startTime = System.currentTimeMillis();
            Category category = cache.get(id);
            if (category == null) {
                category = categoryService.getCategoryById(id);
                if (category != null) {
                    cache.put(id, category);
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Temps de réponse: " + (endTime - startTime) + "ms");

            return Response.ok(category).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    public Response createCategory(Category category) {
        try {
            Category createdCategory = categoryService.createCategory(category);
            cache.put(createdCategory.getId(), createdCategory);
            return Response.status(Response.Status.CREATED).entity(createdCategory).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") Long id, Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            cache.put(id, updatedCategory);
            return Response.ok(updatedCategory).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        try {
            categoryService.deleteCategory(id);
            cache.remove(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    public Response getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return Response.ok(categories).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
