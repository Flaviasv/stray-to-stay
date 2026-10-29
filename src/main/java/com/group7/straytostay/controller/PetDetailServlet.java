package com.group7.straytostay.controller;

import com.group7.straytostay.model.Pet;
import com.group7.straytostay.model.Shelter;
import com.group7.straytostay.service.PetService;
import com.group7.straytostay.service.ShelterService;
import com.group7.straytostay.util.InputValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * GET /pet?id=N — one listing.
 *
 * Request attributes passed to pets/pet-detail.jsp:
 *   pet      Pet
 *   shelter  Shelter   the shelter that listed the pet
 */
@WebServlet("/pet")
public class PetDetailServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/pets/pet-detail.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = InputValidator.parseLong(req.getParameter("id"), -1);
        Optional<Pet> pet = id > 0 ? new PetService().findById(id) : Optional.empty();
        if (pet.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Shelter shelter = new ShelterService().findById(pet.get().getShelterId()).orElse(null);

        req.setAttribute("pet", pet.get());
        req.setAttribute("shelter", shelter);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }
}
