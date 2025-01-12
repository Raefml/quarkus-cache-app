package com.example.application.controller;

import com.example.domain.service.ProductService;
import com.example.domain.model.Product;
import io.quarkus.infinispan.client.Remote;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.infinispan.client.hotrod.RemoteCache;

import java.util.List;


@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    ProductService productService;

    @Inject
    @Remote("productCache")
    RemoteCache<Long, Product> cache;

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        try {
            System.out.println("Instance sur port: " + System.getProperty("quarkus.http.port"));
            System.out.println("Recherche produit ID: " + id);

            long startTime = System.currentTimeMillis();
            Product product = cache.get(id);
            if (product == null) {
                product = productService.getProductById(id);
                if (product != null) {
                    cache.put(id, product);
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Temps de réponse: " + (endTime - startTime) + "ms");

            return Response.ok(product).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    @POST
    public Response createProduct(Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            // Ajouter le produit créé dans le cache
            cache.put(createdProduct.getId(), createdProduct);
            return Response.status(Response.Status.CREATED).entity(createdProduct).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            cache.put(id, updatedProduct);
            return Response.ok(updatedProduct).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        try {
            productService.deleteProduct(id);
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
    public Response getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return Response.ok(products).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
