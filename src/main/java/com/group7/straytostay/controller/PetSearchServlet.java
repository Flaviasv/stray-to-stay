package com.group7.straytostay.controller;

import com.group7.straytostay.model.PageResult;
import com.group7.straytostay.model.Pet;
import com.group7.straytostay.model.PetSearchCriteria;
import com.group7.straytostay.model.Size;
import com.group7.straytostay.model.Species;
import com.group7.straytostay.service.PetService;
import com.group7.straytostay.util.InputValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /pets — browse and filter available pets.
 *
 * Request attributes passed to pets/pet-list.jsp:
 *   page     PageResult&lt;Pet&gt;   the pets on this page plus paging numbers
 *   criteria PetSearchCriteria  the filters as applied, so the form can show them again
 *   species  Species[]          values for the species drop-down
 *   sizes    Size[]             values for the size drop-down
 */
@WebServlet("/pets")
public class PetSearchServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/pets/pet-list.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PetSearchCriteria criteria = readCriteria(req);
        PageResult<Pet> page = new PetService().search(criteria);

        req.setAttribute("page", page);
        req.setAttribute("criteria", criteria);
        req.setAttribute("species", Species.values());
        req.setAttribute("sizes", Size.values());
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    /** Reads the filter parameters. Invalid values fall back to "not applied". */
    static PetSearchCriteria readCriteria(HttpServletRequest req) {
        PetSearchCriteria c = new PetSearchCriteria();
        c.setSpecies(InputValidator.parseEnum(Species.class, req.getParameter("species"), null));
        c.setSize(InputValidator.parseEnum(Size.class, req.getParameter("size"), null));
        c.setBreed(InputValidator.clean(req.getParameter("breed"), 80));
        c.setCity(InputValidator.clean(req.getParameter("city"), 80));
        c.setMaxAgeMonths(InputValidator.parseInteger(req.getParameter("maxAgeMonths")));
        c.setGoodWithKids(InputValidator.parseBoolean(req.getParameter("goodWithKids")));
        c.setPage(InputValidator.parseInt(req.getParameter("page"), 1));
        c.setPageSize(InputValidator.parseInt(req.getParameter("pageSize"), PetSearchCriteria.DEFAULT_PAGE_SIZE));
        return c;
    }
}
