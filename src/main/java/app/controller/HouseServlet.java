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
// И если запрос идет на /houses, вот, то, благодаря этому конфигу, Tomcat понимает, что этот запрос нужно
// адресовать сервлету, который называется HouseServlet. А что это за сервлет, который называется HouseServlet?
// А он это видит уже из этой настройки. Он видит, что HouseServlet — так называется сервлет, который написан
// у нас в этом классе. В классе HouseServlet. И если, допустим, пришел GET-запрос вот с этим URL, да,
// то Tomcat тогда сам обратится к нашему классу HouseServlet и вызовет у него метод doGet.
// А если пришел DELETE-запрос, он из этого же класса вызовет метод doDelete. А если PUT, то doPut.
// А если POST, то doPost.

public class HouseServlet extends HttpServlet {

    private final HouseService service = new HouseServiceImpl();
    // Какие вообще запросы бывают
    // Get-запросы -> обычно для получения информации от сервера
    // например, список автомобилей или один автомобиль по идентификатору
    // Чтобы, например, клиент мог получить от нас список автомобилей, он должен отправить нам
    // GET-запрос на адрес, на котором работает наше приложение, которое мы пишем.
    // GET http://10.20.30.40:8081/houses       получение всех домов
    //                                ? - после вопросительного знака пойдут параметры
    // GET http://10.20.30.40:8081/houses?id=5       получение одного дома по ид


    // Чтобы принять запрос нам нужно переопределить метод
    @Override
    // входящие параметры HttpServletRequest req - запрос клиента, HttpServletResponse resp - ответ клиенту
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Нам нужно сделать обработку запроса и формирование ответа
        // Здесь мы достаем из запроса идентификатор, чтобы понять, пришел ли он вообще. Он может либо прийти, либо нет,
        // в зависимости от того, хочет ли клиент все дома или нет.
        // id нет → не указан конкретный дом
        String id = req.getParameter("id");
        // Это и есть тот самый объект, который умеет преобразовывать любой Java-объект в JSON и наоборот,
        // JSON в любой Java-объект
        ObjectMapper mapper = new ObjectMapper();
        // Поток вывода - по которому наша информация пойдет в resp
        Writer writer = resp.getWriter();
        //  А здесь мы устанавливаем в response хедер, заголовок, который говорит о том, что мы будем
        //  отправлять информацию именно в виде JSON, чтобы клиент, прочитав этот заголовок, понял,
        //  что информация пришла именно в виде JSON, а не в виде чего-то еще.
        resp.setContentType("application/json");
        // Если у нас IDшник null, это говорит о том, что клиент хочет все дома получить, поэтому он никакой
        // идентификатор нам не прислал.
        try {
            if (id == null) {
                // Клиент хочет все дома
                List<House> houses = service.getAll();
                //  И первым аргументом в этот метод мы передаем поток, то есть куда писать. А вторым аргументом передаем
                //  список домов, то есть что именно мы пишем в этот поток.
                // что делает эта строчка? Она делает два действия. Она весь список автомобилей преобразует в JSON,
                // и этот JSON записывает в response, чтобы клиенту был отправлен список автомобилей.
                mapper.writeValue(writer, houses);
            } else {
                // Клиент хочет один дом по id
                // Прежде чем вызывать метод getById, вот этот вот идентификатор надо преобразовать в Long
                Long numericId = Long.parseLong(id);
                House house = service.getById(numericId);

                if (house != null) {
                    mapper.writeValue(writer, house);
                } else {
                    // Улучшение: если дом не найден, возвращаем 404
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("House with ID " + id + " not found"));
                }
            }


        } catch (NumberFormatException e) {
            // Улучшение: если ID — не число, возвращаем 400
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
