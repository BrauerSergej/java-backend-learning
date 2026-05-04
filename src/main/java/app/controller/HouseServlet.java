package app.controller;

import app.domain.House;
import app.service.HouseService;
import app.service.HouseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class HouseServlet extends HttpServlet {

    private final HouseService service = new HouseServiceImpl();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        ObjectMapper mapper = new ObjectMapper();
        Writer writer = resp.getWriter();
        resp.setContentType("application/json");
        try {
            if (id == null) {
                List<House> houses = service.getAll();
                mapper.writeValue(writer, houses);
            } else {
                Long numericId = Long.parseLong(id);
                House house = service.getById(numericId);

                if (house != null) {
                    mapper.writeValue(writer, house);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("House with ID " + id + " not found"));
                }
            }


        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            ObjectMapper mapper = new ObjectMapper();
            House house = mapper.readValue(req.getReader(), House.class);
            House savedHouse = service.save(house);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(resp.getWriter(), savedHouse);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
        try {
            Long numericId = Long.parseLong(id);
            service.deleteById(numericId);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }

    }
}
